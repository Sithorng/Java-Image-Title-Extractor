import java.io.*;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Collections;

public class UniqueImageTitleCounter {

    public void execute(String inputFilePath, String outputDirectory) throws IOException {
        // Set to store unique image titles
        Set<String> uniqueTitles = new HashSet<>();

        // Display a message indicating the processing of the file
        System.out.println("Processing file: " + inputFilePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            // Read each line from the input file
            while ((line = reader.readLine()) != null) {
                // Extract and add unique image titles from the current line
                extractAndAddUniqueImageTitles(line, uniqueTitles);
            }
        } catch (IOException e) {
            // Handle IOException if file reading fails
            System.err.println("Error reading the file: " + e.getMessage());
            throw e; // Propagate the exception
        }

        // Sort unique titles into groups based on file extensions
        Map<String, List<String>> groupedTitles = groupTitlesByExtension(uniqueTitles);

        // Define output file path based on the input file name
        String outputFilePath = getOutputFilePath(inputFilePath, outputDirectory);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            // Write header to the output file
            writer.write("************ Unique Image Titles ************\n\n");

            // Write each group with titles to the output file
            for (Map.Entry<String, List<String>> entry : groupedTitles.entrySet()) {
                writer.write("************ " + entry.getKey().toUpperCase() + " ************\n\n");

                // Sort the titles within each group
                List<String> sortedTitles = new ArrayList<>(entry.getValue());
                Collections.sort(sortedTitles);

                // Write each title with extension to the output file
                for (String title : sortedTitles) {
                    writer.write(title + System.lineSeparator());
                }
            }

            // Write footer with total count to the output file
            writer.write("\n************ Total Count ************\n");
            writer.write("Total number of unique image titles: " + uniqueTitles.size());

            // Display a message indicating that titles have been extracted and saved
            System.out.println("Unique Image Titles have been extracted and saved to: " + outputFilePath);
        } catch (IOException e) {
            // Handle IOException if file writing fails
            System.err.println("Error writing to the file: " + e.getMessage());
            throw e; // Propagate the exception
        }
    }

    private Map<String, List<String>> groupTitlesByExtension(Set<String> uniqueTitles) {
        Map<String, List<String>> groupedTitles = new HashMap<>();

        for (String title : uniqueTitles) {
            // Extract the file extension from the title
            String extension = title.substring(title.lastIndexOf('.') + 1).toLowerCase();

            // Add the title to the corresponding group based on extension
            groupedTitles.computeIfAbsent(extension, k -> new ArrayList<>()).add(title);
        }

        return groupedTitles;
    }

    private void extractAndAddUniqueImageTitles(String line, Set<String> uniqueTitles) {
        // Regular expression to match image titles based on image extensions
        Pattern pattern = Pattern.compile("\\b(https?://\\S+\\.(jpg|jpeg|png|gif|bmp|tiff))\\b");

        // Matcher to find matches in the current line
        Matcher matcher = pattern.matcher(line);

        // Loop through matches and add unique titles to the set
        while (matcher.find()) {
            String imageUrl = matcher.group(1);
            String imageExtension = matcher.group(2);
            String imageTitle = getImageTitle(imageUrl, imageExtension);
            uniqueTitles.add(imageTitle);
        }
    }

    private String getImageTitle(String imageUrl, String imageExtension) {
        // Find the last index of '/' in the URL
        int lastIndex = imageUrl.lastIndexOf("/");
        if (lastIndex != -1) {
            // Extract the image title from the URL and concatenate the extension
            return imageUrl.substring(lastIndex + 1, imageUrl.length() - imageExtension.length() - 1)
                    + "." + imageExtension;
        } else {
            // Return a default title if '/' is not found in the URL
            return "Unknown Title." + imageExtension;
        }
    }

    private String getOutputFilePath(String inputFilePath, String outputDirectory) {
        // Derive the output file path based on the input file name
        File inputFile = new File(inputFilePath);
        String outputFileName = inputFile.getName().replaceFirst("[.][^.]+$", "") + "_result.txt";
        return outputDirectory + File.separator + outputFileName;
    }
}
