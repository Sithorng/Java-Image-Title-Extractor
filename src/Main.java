import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("************ Image Title Extractor ************");
        System.out.println("This program extracts and counts unique titles of images from a text file.");

        while (true) {
            System.out.println("\nMenu:");
            System.out.println("1. Enter Text File Path and Process");
            System.out.println("2. Explore and Get Text File Path");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            switch (choice) {
                case 1:
                    // Get text file path directly
                    System.out.print("Enter the text file path: ");
                    String filePathDirectly = scanner.nextLine();
                    processImageTitles(filePathDirectly, scanner);
                    break;
                case 2:
                    // Explore and get text file path
                    FileExplorer fileExplorer = new FileExplorer(scanner);
                    String filePathFromExplorer = fileExplorer.executeFileExplorer();
                    processImageTitles(filePathFromExplorer, scanner);
                    break;
                case 3:
                    System.out.println("Exiting. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void processImageTitles(String inputFilePath, Scanner scanner) {
        UniqueImageTitleCounter counter = new UniqueImageTitleCounter();
        try {
            // Get output directory option from user
            System.out.println("\nChoose output directory option:");
            System.out.println("1. Store in the current directory");
            System.out.println("2. Enter the path of the output directory");
            System.out.print("Enter your choice: ");

            int outputOption = scanner.nextInt();
            scanner.nextLine(); // Consume the newline character

            String outputDirectory;

            switch (outputOption) {
                case 1:
                    // Store in the current directory
                    outputDirectory = ".";
                    break;
                case 2:
                    // Enter the path of the output directory
                    System.out.print("Enter the path of the output directory: ");
                    outputDirectory = scanner.nextLine();
                    break;
                default:
                    System.out.println("Invalid choice. Defaulting to the current directory.");
                    outputDirectory = ".";
                    break;
            }

            counter.execute(inputFilePath, outputDirectory);

            // Ask the user if they want to continue or exit
            System.out.print("\nDo you want to continue? (Enter 'yes' to continue, any other input to exit): ");
            String continueChoice = scanner.nextLine();
            if (!"yes".equalsIgnoreCase(continueChoice)) {
                System.out.println("Exiting. Goodbye!");
                scanner.close();
                System.exit(0);
            }
        } catch (IOException e) {
            System.err.println("Error processing the file: " + e.getMessage());
        }
    }
}
