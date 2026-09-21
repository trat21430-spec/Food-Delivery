/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package repository;

/**
 *
 * @author HP
 */
import model.BaseEntity;
import java.io.*;
import java.util.*;

public abstract class CsvRepository<T extends BaseEntity> {
    private final String filePath;

    public CsvRepository(String filePath) {
        this.filePath = filePath;
    }

    public String getFilePath() {
        return this.filePath;
    }

    protected abstract T fromCsvLine(String line);
    protected abstract String toCsvLine(T entity);
    protected abstract String getHeader();

    public synchronized List<T> findAll() {
        List<T> list = new ArrayList<>();
        File file = new File(this.filePath);
        if (!file.exists()) {
            return list;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            boolean isHeader = true;
            while ((line = reader.readLine()) != null) {
                if (isHeader) { 
                    isHeader = false; 
                    continue; 
                }
                if (!line.trim().isEmpty()) {
                    list.add(this.fromCsvLine(line));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file at " + this.filePath + ": " + e.getMessage());
        }
        return list;
    }

    public synchronized T findById(String id) {
        return this.findAll().stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public synchronized void saveAll(List<T> entities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(this.filePath, false))) {
            writer.write(this.getHeader());
            writer.newLine();
            for (T entity : entities) {
                writer.write(this.toCsvLine(entity));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error writing CSV file to " + this.filePath + ": " + e.getMessage());
        }
    }
}
