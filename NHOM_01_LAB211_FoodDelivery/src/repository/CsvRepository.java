package repository;

import model.BaseEntity;
import exception.OptimisticLockException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

/**
 * Generic CSV Repository implementing physical file read/write with UTF-8
 * and support for NO_LOCK, FILE_LOCK, SYNCHRONIZED (per-resource key), and OPTIMISTIC locking.
 */
public abstract class CsvRepository<T extends BaseEntity> implements IRepository<T> {
    private String filePath;
    private String header;
    private Object fileMonitor;
    private ConcurrentHashMap<String, Object> keyLocks;

    public CsvRepository(String filePath, String header) {
        this.filePath = filePath;
        this.header = header;
        this.fileMonitor = new Object();
        this.keyLocks = new ConcurrentHashMap<>();
        this.ensureFileExists();
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public abstract T createEntity();

    public T parseLine(String line) {
        T entity = this.createEntity();
        entity.fromCsvLine(line);
        return entity;
    }

    private void ensureFileExists() {
        try {
            File file = new File(this.filePath);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
                if (this.header != null && !this.header.trim().isEmpty()) {
                    try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8))) {
                        bw.write(this.header);
                        bw.newLine();
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error initializing CSV file " + this.filePath + ": " + e.getMessage());
        }
    }

    @Override
    public synchronized List<T> findAll() {
        List<T> list = new ArrayList<>();
        File file = new File(this.filePath);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                try {
                    list.add(this.parseLine(line));
                } catch (Exception parseEx) {
                    // Skip malformed lines
                }
            }
        } catch (Exception e) {
            System.err.println("Error reading " + this.filePath + ": " + e.getMessage());
        }
        return list;
    }

    @Override
    public synchronized T findById(String id) {
        if (id == null) {
            return null;
        }
        List<T> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            T item = all.get(i);
            if (id.equals(item.getId())) {
                return item;
            }
        }
        return null;
    }

    public Optional<T> findByIdOptional(String id) {
        return Optional.ofNullable(this.findById(id));
    }

    @Override
    public synchronized void save(T entity) {
        if (entity == null) {
            return;
        }
        List<T> all = this.findAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (entity.getId().equals(all.get(i).getId())) {
                all.set(i, entity);
                found = true;
                break;
            }
        }
        if (!found) {
            all.add(entity);
        }
        this.saveAll(all);
    }

    @Override
    public synchronized void saveAll(List<T> entities) {
        synchronized (this.fileMonitor) {
            File file = new File(this.filePath);
            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, false), StandardCharsets.UTF_8))) {
                if (this.header != null && !this.header.trim().isEmpty()) {
                    bw.write(this.header);
                    bw.newLine();
                }
                if (entities != null) {
                    for (int i = 0; i < entities.size(); i++) {
                        T item = entities.get(i);
                        if (item != null) {
                            bw.write(item.toCsvLine());
                            bw.newLine();
                        }
                    }
                }
            } catch (Exception e) {
                System.err.println("Error saving to " + this.filePath + ": " + e.getMessage());
            }
        }
    }

    @Override
    public synchronized void deleteById(String id) {
        if (id == null) {
            return;
        }
        List<T> all = this.findAll();
        boolean removed = false;
        for (int i = 0; i < all.size(); i++) {
            if (id.equals(all.get(i).getId())) {
                all.remove(i);
                removed = true;
                break;
            }
        }
        if (removed) {
            this.saveAll(all);
        }
    }

    public Object getKeyLock(String key) {
        return this.keyLocks.computeIfAbsent(key, k -> new Object());
    }

    public void executeWithFileLock(Runnable action) {
        this.executeWithFileLock(() -> {
            action.run();
            return null;
        });
    }

    public <R> R executeWithFileLock(Supplier<R> action) {
        synchronized (this.fileMonitor) {
            File lockFile = new File(this.filePath + ".lock");
            try (FileOutputStream fos = new FileOutputStream(lockFile);
                 FileChannel channel = fos.getChannel();
                 FileLock lock = channel.lock()) {
                if (lock.isValid()) {
                    return action.get();
                }
                throw new IOException("Failed to acquire valid file lock on " + lockFile.getName());
            } catch (Exception e) {
                throw new RuntimeException("FileLock error on " + this.filePath, e);
            }
        }
    }

    public void updateWithOptimisticLock(T entity, long expectedVersion) throws OptimisticLockException {
        T current = this.findById(entity.getId());
        if (current == null) {
            throw new OptimisticLockException(entity.getId(), expectedVersion, -1L);
        }
        if (current.getVersion() != expectedVersion) {
            throw new OptimisticLockException(entity.getId(), expectedVersion, current.getVersion());
        }
        entity.incrementVersion();
        this.save(entity);
    }
}
