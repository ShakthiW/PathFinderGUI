/* *******************************************
 *  Name: Shakthi Warnakulasuriya
 *  IIT ID: 20221274
 *  UoW ID: w1954044
 *
 *  Description: ShortestPathAlgorithm class
 *
 *  Last Updated: 24th April 2024
 *  Copyright © 2024 ShakthiW. All rights reserved
 ***********************************************/

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ShortestPath {
    static class PuzzleNodes implements Comparable<PuzzleNodes> {
        int x_Axis;
        int y_Axis;
        String path;  // directions and the coordinates
        int distanceLength;  //distance length from starting node to the end node


        PuzzleNodes(int x_Axis, int y_Axis, int distanceLength, String path) {
            this.x_Axis = x_Axis;
            this.y_Axis = y_Axis;
            this.distanceLength = distanceLength;
            this.path = path  + " (" + (y_Axis + 1) + ", " + (x_Axis + 1) +")\n" ;     //x and y axis starts from 1
        }

        @Override
        public String toString() {
            return "TOTAL DISTANCE : " + distanceLength +"\n"+ " \nSTART: " + path;
        }

        @Override
        public int compareTo(PuzzleNodes coordinate) {
            return this.distanceLength == coordinate.distanceLength ? this.path.compareTo(coordinate.path) : this.distanceLength - coordinate.distanceLength;
        }
    }

    static class Nodes extends JButton implements Comparable<Nodes> {
        int x_Axis;
        int y_Axis;
        String path;  // directions
        int distanceLength;  // distance length from starting node to the end node
        boolean isShortestPathNode;  // flag to indicate whether the node is part of the shortest path
        Nodes parent;  // reference to the parent node

        Nodes(int x_Axis, int y_Axis, int distanceLength, String path) {
            this.x_Axis = x_Axis;
            this.y_Axis = y_Axis;
            this.distanceLength = distanceLength;
            this.path = path + " (" + (y_Axis + 1) + ", " + (x_Axis + 1) + ")\n";
            this.isShortestPathNode = false;  // Initialize the flag as false
            this.parent = null;  // Initialize the parent as null

            setBackground(Color.white);
            setForeground(Color.black);
            setOpaque(true);
        }

        @Override
        public String toString() {
            return "TOTAL DISTANCE : " + distanceLength + "\n" + " \nSTART: " + path;
        }

        @Override
        public int compareTo(Nodes coordinate) {
            return this.distanceLength == coordinate.distanceLength ? this.path.compareTo(coordinate.path) : this.distanceLength - coordinate.distanceLength;
        }

        // Getter and setter for the parent node
        public Nodes getParent() {
            return parent;
        }

        public void setParent(Nodes parent) {
            this.parent = parent;
        }
    }

    /**
     * Finds the shortest path from a starting node to an ending node in a puzzle grid.
     *
     * @param puzzle A 2D array representing the puzzle grid, where 0 indicates a walkable path and 1 indicates a rock.
     * @param startingNode An array containing the starting node's coordinates (x, y).
     * @param finishingNode An array containing the ending node's coordinates (x, y).
     * @return A list of nodes representing the shortest path from the starting node to the ending node,
     *         or null if no path is found.
     */
    public List<Nodes> shortestPathDistance(int[][] puzzle, int[] startingNode, int[] finishingNode) {
        int rows = puzzle.length;
        int columns = puzzle[0].length;

        boolean[][] checkedNodes = new boolean[rows][columns]; // Keeps track of visited nodes
        Queue<Nodes> queue = new LinkedList<>(); // Acts as the Queue for breadth-first search
        Nodes startNode = new Nodes(startingNode[0], startingNode[1], 0, "");
        queue.add(startNode);

        String[] accessiblePaths = {"Move to UP", "Move to DOWN", "Move to LEFT", "Move to RIGHT"};
        int[][] accessibleCoordinates = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        List<Nodes> lastPath = null;

        while (!queue.isEmpty()) {
            Nodes nodePosition = queue.poll();
            if (nodePosition.x_Axis == finishingNode[0] && nodePosition.y_Axis == finishingNode[1]) {
                lastPath = setShortestPath(nodePosition); // Backtrack to find the shortest path
            }

            for (int i = 0; i < accessibleCoordinates.length; i++) {
                int row = nodePosition.x_Axis;
                int column = nodePosition.y_Axis;
                int distanceFromStart = nodePosition.distanceLength;
                String path = nodePosition.path;

                // Explore valid directions that are available
                while (row >= 0 && row < rows &&
                        column >= 0 && column < columns &&
                        puzzle[row][column] == 0 &&
                        (row != finishingNode[0] || column != finishingNode[1])) {

                    row = row + accessibleCoordinates[i][0];
                    column = column + accessibleCoordinates[i][1];
                    distanceFromStart += 1;
                }

                // Backtrack one step if not at the end node
                if (row != finishingNode[0] || column != finishingNode[1]) {
                    row -= accessibleCoordinates[i][0];
                    column -= accessibleCoordinates[i][1];
                    distanceFromStart -= 1;
                }

                if (!checkedNodes[row][column]) {
                    checkedNodes[nodePosition.x_Axis][nodePosition.y_Axis] = true;
                    Nodes finishNode = new Nodes(row, column, distanceFromStart, path + accessiblePaths[i]);
                    finishNode.setParent(nodePosition);
                    queue.add(finishNode);
                }
            }
        }

        if (lastPath != null && !lastPath.isEmpty()) {
            System.out.println("Shortest path:");
//            for (Nodes node : lastPath) {
//                System.out.println(node.path);
//            }
            System.out.println("Total distance: " + lastPath.get(lastPath.size() - 1).distanceLength);
        } else {
            System.out.println("No path found!");
        }
        return lastPath;
    }


    /**
     * Reconstructs the shortest path from the end node by backtracking through parent nodes.
     *
     * @param node The end node of the shortest path.
     * @return A list of nodes representing the shortest path from the starting node to the end node,
     *         in reverse order (end node first).
     */
    private List<ShortestPath.Nodes> setShortestPath(ShortestPath.Nodes node) {
        List<ShortestPath.Nodes> path = new LinkedList<>();
        while (node != null) {
            node.isShortestPathNode = true;
            path.add(0, node); // Add the node to the beginning of the list to maintain the order
            node = node.getParent();
        }
        return path;
    }

    private void setShortestPath(Nodes node, List<Nodes> shortestPath) {
        while (node != null) {
            node.isShortestPathNode = true;
            shortestPath.add(0, node); // Add the node to the beginning of the list to maintain the order
            node = (Nodes) node.getParent();
        }
    }

    /**
     * Finds the shortest path from a starting node to an ending node in a puzzle grid.
     *
     * @param puzzle A 2D array representing the puzzle grid, where 0 indicates a walkable path and 1 indicates a rock.
     * @param startingNode An array containing the starting node's coordinates (x, y).
     * @param finishingNode An array containing the ending node's coordinates (x, y).
     * @return A list of nodes representing the shortest path from the starting node to the ending node,
     *         or null if no path is found.
     */
    public String shortestPathDistancePZL(int[][] puzzle, int[] startingNode, int[] finishingNode) {
        // Get the dimensions of the puzzle grid
        int rows = puzzle.length;
        int columns = puzzle[0].length;

        // Create a boolean array to track visited nodes (initially all false)
        boolean[][] checkedNodes = new boolean[rows][columns];

        // Initialize a queue for Breadth-First Search (BFS)
        Queue<PuzzleNodes> queue = new LinkedList<>();

        // Create a PuzzleNodes object for the starting node with distance 0 and empty path
        PuzzleNodes startNode = new PuzzleNodes(startingNode[0], startingNode[1], 0, "");
        queue.add(startNode);

        // Define the possible directions for movement (up, down, left, right)
        String[] accessiblePaths = {"Move to UP", "Move to DOWN", "Move to LEFT", "Move to RIGHT"};
        int[][] accessibleCoordinates = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

        // Main loop for BFS exploration
        while (!queue.isEmpty()) {
            // Dequeue the first element from the queue (represents current node)
            PuzzleNodes nodePosition = queue.poll();

            // Check if the current node is the goal (ending node)
            if (nodePosition.x_Axis == finishingNode[0] && nodePosition.y_Axis == finishingNode[1]) {
                // If goal is found, return the path string representation
                return nodePosition.toString();
            }

            // Explore all four directions from the current node
            for (int i = 0; i < accessibleCoordinates.length; i++) {
                int row = nodePosition.x_Axis;
                int column = nodePosition.y_Axis;
                int distanceFromStart = nodePosition.distanceLength;
                String path = nodePosition.path;

                // Loop while the node can move in a particular direction (within grid boundaries, not a rock, and not the goal yet)
                while (row >= 0 && row < rows &&
                        column >= 0 && column < columns &&
                        puzzle[row][column] == 0 &&
                        (row != finishingNode[0] || column != finishingNode[1])) {

                    row = row + accessibleCoordinates[i][0];
                    column = column + accessibleCoordinates[i][1];
                    distanceFromStart += 1;  // Update distance
                }

                // Backtrack one step if not at the goal node (to avoid going out of bounds or hitting a rock)
                if (row != finishingNode[0] || column != finishingNode[1]) {
                    row -= accessibleCoordinates[i][0];
                    column -= accessibleCoordinates[i][1];
                    distanceFromStart -= 1;
                }

                // Check if the current position hasn't been visited before
                if (!checkedNodes[row][column]) {
                    // Mark the current node as visited
                    checkedNodes[nodePosition.x_Axis][nodePosition.y_Axis] = true;

                    // Create a new PuzzleNodes object for the explored position
                    PuzzleNodes finishNode = new PuzzleNodes(row, column, distanceFromStart, path + accessiblePaths[i]);
                    queue.add(finishNode);  // Add the explored position to the queue for further exploration
                }
            }
        }

        // If the loop finishes without finding the goal, return "No path found!"
        return "Couldn't Find any Path...!";
    }

}
