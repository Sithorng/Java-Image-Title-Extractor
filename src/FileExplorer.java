import java.io.File;
import java.util.Scanner;

public class FileExplorer {

    private Scanner scanner;

    public FileExplorer(Scanner scanner) {
        this.scanner = scanner;
    }

    public String executeFileExplorer() {
        System.out.print("Enter the drive letter using File Explorer (or 'exit' to quit): ");
        String driveLetter = scanner.nextLine();

        if ("exit".equalsIgnoreCase(driveLetter)) {
            System.out.println("Exiting File Explorer. Goodbye!");
            System.exit(0);
        }

        File drive = new File(driveLetter + ":\\");
        if (drive.exists() && drive.isDirectory()) {
            return getFilePathUsingExplorer(drive);
        } else {
            System.out.println("Invalid drive letter. Please enter a valid drive letter or 'exit' to quit.");
            // Add the following return statement to handle the error
            return executeFileExplorer();
        }
    }

    private String getFilePathUsingExplorer(File directory) {
        showFolderMenu(directory);
        System.out.print("Enter the file number or name (or 'back' to go up, 'exit' to quit): ");
        String input = scanner.nextLine();

        if ("exit".equalsIgnoreCase(input)) {
            System.out.println("Exiting File Explorer. Goodbye!");
            System.exit(0);
        } else if ("back".equalsIgnoreCase(input)) {
            File parent = directory.getParentFile();
            if (parent != null) {
                return getFilePathUsingExplorer(parent);
            } else {
                System.out.println("Already at the root. Cannot go back further.");
                // Fix: Add a return statement to handle the error
                return executeFileExplorer();
            }
        } else {
            try {
                int choice = Integer.parseInt(input);
                File[] files = directory.listFiles();
                if (files != null && choice >= 1 && choice <= files.length) {
                    File selectedFile = files[choice - 1];
                    if (selectedFile.isDirectory()) {
                        // If selected item is a directory, navigate into it
                        return getFilePathUsingExplorer(selectedFile);
                    } else if (selectedFile.isFile()) {
                        return selectedFile.getAbsolutePath();
                    } else {
                        System.out.println("Selected item is not a file. Please choose a file.");
                        // Fix: Add a return statement to handle the error
                        return getFilePathUsingExplorer(directory);
                    }
                } else {
                    System.out.println("Invalid number. Please enter a valid number or 'back' to go up.");
                    // Fix: Add a return statement to handle the error
                    return getFilePathUsingExplorer(directory);
                }
            } catch (NumberFormatException e) {
                File newFile = new File(directory, input);
                if (newFile.exists() && newFile.isFile()) {
                    return newFile.getAbsolutePath();
                } else {
                    System.out.println("Invalid file name. Please enter a valid file name or 'back' to go up.");
                    // Fix: Add a return statement to handle the error
                    return getFilePathUsingExplorer(directory);
                }
            }
        }
        // Add a default return statement to satisfy the compiler
        return input;
    }

    private void showFolderMenu(File directory) {
        System.out.println("\nCurrent Directory: " + directory.getAbsolutePath());
        System.out.println("Contents:");

        File[] files = directory.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + ". " + files[i].getName());
            }
        }
    }
}
