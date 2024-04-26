/* *******************************************
 *  Name: Shakthi Warnakulasuriya
 *  IIT ID: 20221274
 *  UoW ID: w1954044
 *
 *  Description: Main class
 *
 *  Last Updated: 24th April 2024
 *  Copyright © 2024 ShakthiW. All rights reserved
 ***********************************************/

import javax.swing.*;
import java.io.File;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Scanner;


public class Main {
    private static String newInputFileName; // Stores the name of the newly input filename
    private final static Scanner input = new Scanner(System.in);
    private static String fileName;
    private static File getFile;    // to save the input file

    // Create instances of Parser and GridPanel
    static Parser parser = new Parser();   //parser object
    static GridPanel gridPanel = new GridPanel(); // Create an instance of GridPanel


    public static void main(String[] args) {

        // Introduction screen
        System.out.println();
        System.out.println("###########################################################");
        System.out.println("\t\t\t\tWELCOME TO THE SLIDING PUZZLE\t\t\t\t");
        System.out.println("###########################################################");
        System.out.println();

        // Set the GridPanel instance in the Parser object for visualization
        parser.setGridPanel(gridPanel); // Pass gridPanel instance to the parser
        loadNewFile();

        // Handle visualization mode ("m" prefix in filename)
        if (newInputFileName.startsWith("m")) {
            System.out.println("Starting visualization");

            // Create a JFrame window for visualization
            JFrame window = new JFrame();
            window.setVisible(true);
            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);

            // Add the GridPanel to the JFrame for displaying the puzzle
            window.add(gridPanel);

            window.pack();
            window.setLocationRelativeTo(null);
            window.setVisible(true);
        }
    }

    /**
     * Handles loading a new file, path finding calculation, and visualization.
     */
    private static void loadNewFile() {
        System.out.println("First, please put the relevant text file into the 'MazeFolder' \n");
        try {
            System.out.println("Note: Please enter a valid text file name below with the extension as well (.txt)");
            System.out.print("New Input File Name:  ");
            newInputFileName = input.nextLine();  //getting the file name

            // Read the file using the Parser object
            parser.fileRead("src/MazeFolder/" + newInputFileName);
            parser.retrievingLines();
            parser.retrieveValues();

            // Check if file reading was successful
            if (!parser.asFileRead()) {
                throw new Exception("File not available !");
            }

            fileName = parser.retrievingFileName();
            getFile = parser.getInputFile();

            // Start time measurement for path finding calculation
            Instant start = Instant.now();

            // Calculate the total distance (shortest path) based on the file type
            getTotalDistance();

            // End time measurement
            Instant end = Instant.now();

            // Print the elapsed time for path finding
            System.out.println(Duration.between(start, end));

            System.out.println("Shortest path founded.\n" + "SUCESSFULL!");

        } catch (Exception e) {
            System.out.println("\nException Occurred : " + e);
            System.out.println("\nUnable to load the file!!");
        }
    }

    /**
     * Calculates the total distance (shortest path) based on the file type ("p" for printing or "m" for visualization).
     */
    private static void getTotalDistance() {
        int[][] puzzleArray = parser.getPuzzle();
        int[] startingNodeArray = parser.getStartingNode();
        int[] endingNodeArray = parser.getEndingNode();

        if (newInputFileName.startsWith("p")) {
            ShortestPath shortestPathObject = new ShortestPath();

            System.out.println("\nFINDING THE SHORTEST PATH");
            System.out.println(shortestPathObject.shortestPathDistancePZL(puzzleArray, startingNodeArray, endingNodeArray));
        } else {

            ShortestPath shortestPathObject = new ShortestPath();

            System.out.println("\nFINDING THE SHORTEST PATH");
            List<ShortestPath.Nodes> lastPath = shortestPathObject.shortestPathDistance(puzzleArray, startingNodeArray, endingNodeArray);

            if (lastPath != null && !lastPath.isEmpty()) {
                ShortestPath.Nodes lastNode = lastPath.get(lastPath.size() - 1);
                System.out.print("Start Node:");
                System.out.println(lastNode.path); // Print the path of the last node
                System.out.println("Total distance: " + lastNode.distanceLength);
            } else {
                System.out.println("No path found!");
            }
        }
    }
}