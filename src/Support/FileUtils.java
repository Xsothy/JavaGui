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
    private static final String EXPENSE_IMAGES_DIR = "src/img/expenses/";
    
    /**
     * Saves an image file to the expenses directory.
     * 
     * @param sourcePath The source path of the image
     * @return The path where the image was saved, or null if the operation failed
     * @throws IOException If an I/O error occurs
     */
    public static String saveExpenseImage(String sourcePath) throws IOException {
        if (sourcePath == null || sourcePath.trim().isEmpty()) {
            return null;
        }
        
        // Create directory if it doesn't exist
        File directory = new File(EXPENSE_IMAGES_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            LOGGER.log(Level.WARNING, "Source file does not exist: {0}", sourcePath);
            return null;
        }
        
        // Generate a unique filename
        String extension = sourcePath.substring(sourcePath.lastIndexOf("."));
        String imageName = UUID.randomUUID().toString() + extension;
        String destinationPath = EXPENSE_IMAGES_DIR + imageName;
        
        // Copy the file
        Path source = Paths.get(sourcePath);
        Path destination = Paths.get(destinationPath);
        Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
        
        return destinationPath;
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
} 