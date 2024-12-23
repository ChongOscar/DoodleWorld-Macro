import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Storage {
    private final File storageFile;
    private final Map<String, String> data;

    // Constructor that initializes storage with a specified file path
    public Storage(String filePath) {
        this.storageFile = new File(filePath);
        this.data = new HashMap<>();
        load(); // Load existing data from the file
    }

    // Save a key-value pair
    public void setItem(String key, String value) {
        data.put(key, value);
        save();
    }

    // Retrieve a value by key
    public String getItem(String key) {
        return data.getOrDefault(key, null);
    }

    // Remove a key-value pair
    public void removeItem(String key) {
        if (data.containsKey(key)) {
            data.remove(key);
            save();
        }
    }

    // Clear all stored data
    public void clear() {
        data.clear();
        save();
    }

    // Load data from the storage file
    private void load() {
        if (!storageFile.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(storageFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] entry = line.split("=", 2);
                if (entry.length == 2) {
                    data.put(entry[0], entry[1]);
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to load storage: " + e.getMessage());
        }
    }

    // Save data to the storage file
    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(storageFile))) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                writer.write(entry.getKey() + "=" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Failed to save storage: " + e.getMessage());
        }
    }

    // Check if a key exists
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    // Get the number of stored items
    public int size() {
        return data.size();
    }
}
