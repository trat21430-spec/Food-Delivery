package repository;

import model.BaseEntity;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract base repository that handles all CSV file I/O.
 * Subclasses provide the entity type, CSV file path, and CSV header.
 *
 * @param <T> Entity type extending BaseEntity
 */
public abstract class CsvRepository<T extends BaseEntity> {

    /** @return relative path to the CSV file, e.g. "data/customers.csv" */
    protected abstract String getCsvFilePath();

    /** @return the header line written on first file creation */
    protected abstract String getCsvHeader();

    /** @return a new, empty instance of T used to call fromCsvLine() */
    protected abstract T createEmpty();

    // ---------------------------------------------------------------
    // Internal helpers
    // ---------------------------------------------------------------

    private void ensureFileExists() {
        File file = new File(getCsvFilePath());
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(getCsvHeader());
                bw.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // ---------------------------------------------------------------
    // Public CRUD operations
    // ---------------------------------------------------------------

    /** Read all records from CSV into a List. */
    public List<T> findAll() {
        ensureFileExists();
        List<T> result = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(getCsvFilePath()))) {
            String line;
            boolean first = true;
            while ((line = br.readLine()) != null) {
                if (first) { first = false; continue; } // skip header
                if (line.isBlank()) continue;
                T entity = createEmpty();
                entity.fromCsvLine(line);
                result.add(entity);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    /** Find a single record by its ID field. */
    public T findById(String id) {
        if (id == null || id.isBlank()) return null;
        for (T entity : findAll()) {
            if (id.equals(entity.getId())) {
                return entity;
            }
        }
        return null;
    }

    /** Append a new record to the CSV file (thread-safe with FileLock). */
    public synchronized void save(T entity) {
        ensureFileExists();
        try (FileOutputStream fos = new FileOutputStream(getCsvFilePath(), true);
             FileChannel channel = fos.getChannel();
             FileLock lock = channel.lock()) {
            byte[] data = (entity.toCsvLine() + System.lineSeparator()).getBytes();
            channel.write(java.nio.ByteBuffer.wrap(data));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Overwrite the entire CSV file with the given list (thread-safe). */
    public synchronized void rewriteAll(List<T> entities) {
        ensureFileExists();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getCsvFilePath(), false))) {
            bw.write(getCsvHeader());
            bw.newLine();
            for (T entity : entities) {
                bw.write(entity.toCsvLine());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Update an existing record by ID; replaces the matching row in place. */
    public synchronized void update(T updated) {
        List<T> all = findAll();
        boolean found = false;
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(updated.getId())) {
                updated.increaseVersion();
                all.set(i, updated);
                found = true;
                break;
            }
        }
        if (found) {
            rewriteAll(all);
        }
    }
}
