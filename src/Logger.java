import java.io.*;
import java.util.zip.GZIPOutputStream;

public class Logger {
    private final File storageFile;

    public Logger() {
        this.storageFile = new File("log.txt.gz");
        clearLogFile(); // Clear the log file on instantiation
    }

    private void clearLogFile() {
        try (GZIPOutputStream gzip = new GZIPOutputStream(new FileOutputStream(storageFile))) {
            // Overwrite the file with an empty GZIP stream
        } catch (IOException e) {
            throw new RuntimeException("Failed to clear log file", e);
        }
    }
    private void clearLogFileIfTooBig() {
        if (storageFile.exists() && storageFile.length() > 1024 * 1024) {
            clearLogFile();
        }
    }

    public void log(String string) {
        clearLogFileIfTooBig();
        try (GZIPOutputStream gzip = new GZIPOutputStream(new FileOutputStream(storageFile, true));
             Writer writer = new OutputStreamWriter(gzip)) {
             writer.write(string);
             writer.write(System.lineSeparator());
        } catch (IOException e) {
             throw new RuntimeException(e);
        }
    }
}