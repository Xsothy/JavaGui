package Support;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utility class for file operations.
 */
public class FileUtils {
    private static final Logger LOGGER = Logger.getLogger(FileUtils.class.getName());
    /**
     * Directory for storing expense images.
     */
    private static final String EXPENSE_IMAGES_DIR = "src/img/expenses/";
    
    /**
     * Save an expense image to the expenses directory.
     * 
     * @param sourcePath The source path of the image
     * @return The path to the saved image, or null if the operation failed
     * @throws IOException If an error occurs while saving the file
     */
    public static String saveExpenseImage(String sourcePath) throws IOException {
        if (sourcePath == null || sourcePath.trim().isEmpty()) {
            return null;
        }
        
        // Create the directory if it doesn't exist
        File directory = new File(EXPENSE_IMAGES_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            LOGGER.log(Level.WARNING, "Source file does not exist: {0}", sourcePath);
            return null;
        }
        
        // Generate a unique file name
        String fileName = UUID.randomUUID().toString() + getFileExtension(sourcePath);
        String destinationPath = EXPENSE_IMAGES_DIR + fileName;
        
        // Copy the file
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        
        return destinationPath;
    }
    
    /**
     * Get the file extension from a path.
     * 
     * @param path The file path
     * @return The file extension, including the dot
     */
    private static String getFileExtension(String path) {
        int lastDotIndex = path.lastIndexOf('.');
        if (lastDotIndex >= 0) {
            return path.substring(lastDotIndex);
        }
        return ""; // No extension
    }
    
    /**
     * Gets the absolute path of a file in the expenses directory.
     * 
     * @param filename The filename
     * @return The absolute path
     */
    public static String getExpenseImagePath(String filename) {
        if (filename == null || filename.trim().isEmpty()) {
            return null;
        }
        
        File file = new File(EXPENSE_IMAGES_DIR + filename);
        return file.getAbsolutePath();
    }
    
    /**
     * Delete a file.
     * 
     * @param path The path to the file
     * @return true if the file was deleted, false otherwise
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.exists() && file.delete();
    }
} 