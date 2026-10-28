package repository;

import model.BaseEntity;
import exception.OptimisticLockException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

public abstract class CsvRepository<T extends BaseEntity> implements IRepository<T> {
    private String filePath;
    private String header;
    private ConcurrentHashMap<String, Object> keyLocks;

    public CsvRepository(String filePath, String header) {
        this.filePath = filePath;
        this.header = header;
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
<<<<<<< HEAD

=======
    
>>>>>>> e10ffdc516208d587aa591da018bcddb3663aff9
    public void setHeader(String header) {
        this.header = header;
    }

    private void ensureFileExists() {
        File file = new File(this.filePath);
        if (!file.exists()) {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                pw.println(this.header);
            } catch (IOException e) {
                System.err.println("Error initializing file " + this.filePath + ": " + e.getMessage());
            }
        }
    }

    public abstract T createEntity();

    @Override
    public synchronized List<T> findAll() {
        List<T> list = new ArrayList<>();
        File file = new File(this.filePath);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isFirstLine = true;
            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                if (line.trim().isEmpty()) {
                    continue;
                }
                T entity = this.createEntity();
                entity.fromCsvLine(line);
                list.add(entity);
            }
        } catch (IOException e) {
            System.err.println("Error reading " + this.filePath + ": " + e.getMessage());
        }
        return list;
    }

    @Override
    public synchronized T findById(String id) {
        List<T> all = this.findAll();
        for (int i = 0; i < all.size(); i++) {
            T entity = all.get(i);
            if (entity.getId().equals(id)) {
                return entity;
            }
        }
        return null;
    }

    @Override
    public synchronized void saveAll(List<T> entities) {
        File file = new File(this.filePath);
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(file, false)))) {
            pw.println(this.header);
            for (int i = 0; i < entities.size(); i++) {
                T entity = entities.get(i);
                pw.println(entity.toCsvLine());
            }
        } catch (IOException e) {
            System.err.println("Error saving all to " + this.filePath + ": " + e.getMessage());
        }
    }

    @Override
    public synchronized void save(T entity) {
        List<T> all = this.findAll();
        boolean updated = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(entity.getId())) {
                all.set(i, entity);
                updated = true;
                break;
            }
        }
        if (!updated) {
            all.add(entity);
        }
        this.saveAll(all);
    }

    @Override
    public synchronized void deleteById(String id) {
        List<T> all = this.findAll();
        List<T> filtered = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            T entity = all.get(i);
            if (!entity.getId().equals(id)) {
                filtered.add(entity);
            }
        }
        this.saveAll(filtered);
    }

    public Object getKeyLock(String key) {
        return this.keyLocks.computeIfAbsent(key, k -> new Object());
    }

    public void executeWithFileLock(Runnable action) {
        File lockFile = new File(this.filePath + ".lock");
        try (FileChannel channel = FileChannel.open(Paths.get(lockFile.getAbsolutePath()),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ)) {
            FileLock lock = channel.lock();
            try {
                action.run();
            } finally {
                lock.release();
            }
        } catch (IOException e) {
            System.err.println("File lock execution error on " + this.filePath + ": " + e.getMessage());
        }
    }

    public <R> R executeWithFileLock(Supplier<R> action) {
        File lockFile = new File(this.filePath + ".lock");
        try (FileChannel channel = FileChannel.open(Paths.get(lockFile.getAbsolutePath()),
                StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.READ)) {
            FileLock lock = channel.lock();
            try {
                return action.get();
            } finally {
                lock.release();
            }
        } catch (IOException e) {
            System.err.println("File lock execution error on " + this.filePath + ": " + e.getMessage());
            return null;
        }
    }

    public void updateWithOptimisticLock(T entity, long expectedVersion) throws OptimisticLockException {
        List<T> all = this.findAll();
        T currentInStore = null;
        int targetIndex = -1;

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(entity.getId())) {
                currentInStore = all.get(i);
                targetIndex = i;
                break;
            }
        }

        if (currentInStore != null && currentInStore.getVersion() != expectedVersion) {
            throw new OptimisticLockException(entity.getId(), expectedVersion, currentInStore.getVersion());
        }

        entity.incrementVersion();
        if (targetIndex >= 0) {
            all.set(targetIndex, entity);
        } else {
            all.add(entity);
        }
        this.saveAll(all);
    }
}
