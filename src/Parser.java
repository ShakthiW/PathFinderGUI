/* *******************************************
 *  Name: Shakthi Warnakulasuriya
 *  IIT ID: 20221274
 *  UoW ID: w1954044
 *
 *  Description: Parser class
 *
 *  Last Updated: 24th April 2024
 *  Copyright © 2024 ShakthiW. All rights reserved
 ***********************************************/

import java.io.IOException;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Files;

public class Parser {
    public Scanner lineSc = null;
    private final ArrayList<String> linesArrayList = new ArrayList<>(); // ArrayList to store lines read from the file
    private boolean isRead;        //to verify the file reading process is successful
    private boolean isLoaded;      //to verify whether the file is loaded successfully
    private File fileInput;        //to save the relevant txt file

    // Data extracted from the parsed file
    public int[] endingNode;
    public int[] startingNode;
    public int[][] puzzle;

    // Reference to the GridPanel object for visualization
    private GridPanel gridPanel;

    /**
     * Sets the GridPanel object for visualization.
     *
     * @param gridPanel The GridPanel object used to display the puzzle.
     */
    public void setGridPanel(GridPanel gridPanel) {
        this.gridPanel = gridPanel;
    }

    /**
     * Checks if the file has been read successfully.
     *
     * @return True if the file was read successfully, false otherwise.
     */
    public Boolean asFileRead()
    {
        return this.isRead;
    }

    /**
     * Gets the puzzle data (2D array) parsed from the file.
     *
     * @return The puzzle data as a 2D array (1: empty space, 0: barrier), or null if the file is not read properly.
     */
    public int[][] getPuzzle() {
        if (checkFile())
        {
            gridPanel.setPuzzle(this.puzzle);
            return this.puzzle;
        }
        return null;
    }

    /**
     * Gets the starting node coordinates ([row, col]) parsed from the file.
     *
     * @return The starting node coordinates as an array ([row, col]), or null if the file is not read properly.
     */
    public int[] getStartingNode()
    {
        if (checkFile())
        {
            gridPanel.setStartingNode(this.startingNode);
            return this.startingNode;
        }
        return null;
    }

    /**
     * Gets the ending node coordinates ([row, col]) parsed from the file.
     *
     * @return The ending node coordinates as an array ([row, col]), or null if the file is not read properly.
     */
    public int[] getEndingNode()
    {
        if (checkFile())
        {
            gridPanel.setEndingNode(this.endingNode);
            return this.endingNode;
        }
        return null;
    }

    /**
     * Retrieves the lines from the parsed file and populates the `linesArrayList`.
     *
     * @throws IOException If there's an error reading the file content.
     */
    public void retrievingLines() throws IOException
    {
        if (this.isRead)
        {
            linesArrayList.addAll(Files.readAllLines(fileInput.toPath(), Charset.defaultCharset()));
            this.isLoaded = true;
        }
    }

    /**
     * Gets the filename of the parsed file.
     *
     * @return The filename of the parsed file, or null if the file is not loaded successfully.
     */
    public String retrievingFileName()
    {
        if (isLoaded)   //validating whether the file is successfully loaded
        {
            return fileInput.getName();
        }
        return null;
    }

    /**
     * Gets the list of lines read from the file.
     *
     * @return The list of lines from the file, or null if the file is not read successfully.
     */
    public ArrayList<String> getLinesArrayList()
    {
        if (this.isRead)
        {
            return this.linesArrayList;
        }
        return null;
    }

    /**
     * Reads the lines from the specified file path.
     *
     * @param path The path to the text file containing the puzzle data.
     * @throws FileNotFoundException If the file is not found.
     */
    public void fileRead(String path) throws FileNotFoundException
    {
        File fileInput;
        fileInput = new File(path); //retrieving the file name and accessing the relevant txt file

        if (fileInput.length() == 0) //checks whether the path of the file is given or not (the file path will be denoted as a long value)
        {
            throw new FileNotFoundException("File " + path + " does not exist");
        }

        this.fileInput = fileInput;
        this.isRead = true;
    }

    /**
     * Gets the parsed file object.
     *
     * @return The File object representing the parsed file, or null if the file is not read.
     */
    public File getInputFile()
    {
        if (this.isRead)
        {
            return fileInput;
        }
        return null;

    }

    /**
     * Checks if the file is loaded successfully (i.e., lines are retrieved from the file).
     *
     * @return True if the file is loaded successfully, false otherwise.
     */
    public Boolean checkFile()
    {
        if (this.asFileRead())
        {
            return this.isLoaded;
        }
        return null;
    }

    /**
     * Extracts the puzzle data from the loaded lines and populates the `puzzle`, `startingNode`, and `endingNode` fields.
     *
     * @return True if the data was extracted successfully, false otherwise.
     */
    public boolean retrieveValues(){        //adding nodes into the 2D array and searching for the starting node and the ending node
        ArrayList<String> linesArrayList1 = this.getLinesArrayList();

        // Find the maximum horizontal size (length of the longest line)
        int horizontalSize = 0;
        for (String line : linesArrayList1) {
            horizontalSize = Math.max(horizontalSize, line.trim().length());
        }

        // Create a 2D array to store the puzzle grid with padding for lines of different lengths
        this.puzzle = new int[linesArrayList1.size()][horizontalSize];
        int counterOfLines = 0;

        // Process each line to extract data
        for (int i = 0; i < linesArrayList1.size(); i++){
            String line = linesArrayList1.get(i);
            this.lineSc = new Scanner(line);
            int[] floor = new int[horizontalSize];
            int count = 0;

            while (lineSc.hasNext()) {
                if (count < horizontalSize){
                    String node = lineSc.nextLine();
                    node = node.replace("0", "1");  //restoring zeros with ones and dots with zeros
                    node = node.replace(".", "0");

                    if(node.contains("S")){
                        this.startingNode = new int[]{counterOfLines, node.indexOf("S")};    //starting point S is restoring with 0
                        node = node.replace("S", "0");
                    }
                    if(node.contains("F")){
                        this.endingNode = new int[]{counterOfLines, node.indexOf("F")};
                        node = node.replace("F", "0");                  //finishing point S is restoring with 0
                    }
                    String[] string = node.split("");

                    for (int j = 0; j < string.length; j++) {
                        floor[j] = Integer.valueOf(string[j]);              //storing each index into an integer array
                    }
                    counterOfLines++;
                }
                count++;
            }
            puzzle[i] = floor; // Copy the row data to the puzzle array
        }

        return true;
    }
}