/* *******************************************
 *  Name: Shakthi Warnakulasuriya
 *  IIT ID: 20221274
 *  UoW ID: w1954044
 *
 *  Description: Grid Panel class
 *
 *  Last Updated: 24th April 2024
 *  Copyright © 2024 ShakthiW. All rights reserved
 ***********************************************/

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;

public class GridPanel extends JPanel implements KeyListener {
    // Constants for grid visualization
    private static final int NODE_SIZE = 30; // Size of each node in pixels
    private static final Color COLOR_START = Color.GREEN;
    private static final Color COLOR_END = Color.RED;
    private static final Color COLOR_BARRIER = Color.BLACK;
    private static final Color COLOR_EMPTY = Color.WHITE;
    private static final Color COLOR_SHORTEST_PATH = Color.ORANGE;

    // Data members for the puzzle and path
    private int[][] puzzle;
    private int[] startingNode;
    private int[] endingNode;
    private List<ShortestPath.Nodes> shortestPath;

    /**
     * Constructor for the GridPanel.
     */
    public GridPanel() {
        setFocusable(true); // Allow the panel to receive focus for key events
        addKeyListener(this); // Add key listener to the panel
    }

    /**
     * Gets the preferred size of the panel based on the puzzle dimensions.
     *
     * @return The preferred size as a Dimension object.
     */
    @Override
    public Dimension getPreferredSize() {
        if (puzzle != null) {
            int numRows = puzzle.length;
            int numCols = puzzle[0].length;
            return new Dimension(numCols * NODE_SIZE, numRows * NODE_SIZE);
        }
        return super.getPreferredSize();
    }

    /**
     * Sets the puzzle data for the grid panel.
     *
     * @param puzzle A 2D array representing the puzzle grid (0: empty space, 1: barrier).
     */
    public void setPuzzle(int[][] puzzle) {
        this.puzzle = puzzle;
        repaint(); // Redraw the panel when the puzzle changes
    }

    /**
     * Sets the starting node coordinates for the puzzle.
     *
     * @param startingNode An array containing the starting node's coordinates (x, y).
     */
    public void setStartingNode(int[] startingNode) {
        this.startingNode = startingNode;
        repaint(); // Redraw the panel when the starting node changes
    }

    /**
     * Sets the ending node coordinates for the puzzle.
     *
     * @param endingNode An array containing the ending node's coordinates (x, y).
     */
    public void setEndingNode(int[] endingNode) {
        this.endingNode = endingNode;
        repaint(); // Redraw the panel when the ending node changes
    }

    /**
     * Sets the shortest path for the puzzle.
     *
     * @param shortestPath A list of nodes representing the shortest path from the starting node to the ending node.
     */
    public void setShortestPath(List<ShortestPath.Nodes> shortestPath) {
        this.shortestPath = shortestPath;
        repaint(); // Redraw the panel when the shortest path changes
    }

    /**
     * Paints the grid panel with the puzzle, starting node, ending node, and shortest path (if available).
     *
     * @param graphics The graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics graphics) {
        super.paintComponent(graphics);
        if (puzzle != null) {
            int numRows = puzzle.length;
            int numCols = puzzle[0].length;

            // Set the size of the panel based on the dimensions of the puzzle
            setPreferredSize(new Dimension(numCols * NODE_SIZE, numRows * NODE_SIZE));

            // Draw grid
            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    Color color;
                    if (puzzle[row][col] == 0) {
                        color = COLOR_BARRIER; // Draw barrier
                        // Check if the node is part of the shortest path and update color
                        if (shortestPath != null) {
                            for (ShortestPath.Nodes node : shortestPath) {
                                if (node.x_Axis == row && node.y_Axis == col) {
                                    color = COLOR_SHORTEST_PATH;
                                    break;
                                }
                            }
                        }
                    } else {
                        color = COLOR_EMPTY; // Draw empty space
                    }
                    graphics.setColor(color);
                    graphics.fillRect(col * NODE_SIZE, row * NODE_SIZE, NODE_SIZE, NODE_SIZE);
                }
            }

            // Draw starting node
            if (startingNode != null) {
                graphics.setColor(COLOR_START);
                graphics.fillRect(startingNode[1] * NODE_SIZE, startingNode[0] * NODE_SIZE, NODE_SIZE, NODE_SIZE);
            }

            // Draw ending node
            if (endingNode != null) {
                graphics.setColor(COLOR_END);
                graphics.fillRect(endingNode[1] * NODE_SIZE, endingNode[0] * NODE_SIZE, NODE_SIZE, NODE_SIZE);
            }

            // Draw the shortest path as a line between consecutive nodes
            if (shortestPath != null) {
                graphics.setColor(COLOR_SHORTEST_PATH);
                for (int i = 0; i < shortestPath.size() - 1; i++) {
                    ShortestPath.Nodes currentNode = shortestPath.get(i);
                    ShortestPath.Nodes nextNode = shortestPath.get(i + 1);
                    int startX = currentNode.y_Axis * NODE_SIZE + NODE_SIZE / 2;
                    int startY = currentNode.x_Axis * NODE_SIZE + NODE_SIZE / 2;
                    int endX = nextNode.y_Axis * NODE_SIZE + NODE_SIZE / 2;
                    int endY = nextNode.x_Axis * NODE_SIZE + NODE_SIZE / 2;
                    graphics.drawLine(startX, startY, endX, endY);
                }
            }
        }
    }


    /**
     * Handles key press events.
     *
     * @param e The KeyEvent object containing information about the key pressed.
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (startingNode != null && endingNode != null && puzzle != null) {
                // Calculate the shortest path
                ShortestPath algorithm = new ShortestPath();
                List<ShortestPath.Nodes> path = algorithm.shortestPathDistance(puzzle, startingNode, endingNode);

                // Set the shortest path
                setShortestPath(path);

                // Trigger the display of the shortest path
                repaint();
            }
        }
    }

    // Empty implementations for unused key events
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
