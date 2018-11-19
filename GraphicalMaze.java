import java.awt.*;
import javax.swing.*;

/**
 * A graphical extension of the Maze class that allows the client to view the
 * maze as it is being searched.  As the client calls the various 'is' methods
 * on the maze (isWall, isVisited, etc.) or marks squares as visited, the board
 * pauses and animates the action on screen to show the algorithm's progress.
 * 
 * This version differs from the previous GraphicalMaze in that it is not a
 * complete graphical window but just a panel that can be put side-by-side
 * in a window with other graphical mazes.
 * 
 */
public class GraphicalMaze extends Maze {
	public static final int MAX_SQUARE_SIZE = 30;   // px
	public static final int MAX_PAUSE = 1000;       // milliseconds

	private static final Color VISITED_COLOR = new Color(192, 228, 255);
	
	private JPanel panel;
	private JLabel[][] labels;
	private JLabel titleLabel;
	private JLabel visitedCountLabel;
	private int delayMS;

	/**
	 * Constructs a new graphical maze using the given text string of lines.
	 * @param text the string of lines to pass; the complete contents of a maze input file
	 * @throws IllegalArgumentException if the file is in an invalid format
	 */
	public GraphicalMaze(String text, String title, int width, int height) {
		super(text);
		this.delayMS = MAX_PAUSE / 2;
		
		// add buttons in the middle for the maze squares
		Container center = createLabels(width, height);
		
		Container north = new JPanel();
		title = title.replace("MazeSolver", "");
		titleLabel = new JLabel(title);
		north.add(titleLabel);
		
		Container south = new JPanel();
		visitedCountLabel = new JLabel("Visited: 0");
		south.add(visitedCountLabel);
		
		panel = new JPanel(new BorderLayout());
		panel.setBackground(Color.BLACK);
		panel.add(center);
		panel.add(north, BorderLayout.NORTH);
		panel.add(south, BorderLayout.SOUTH);
		Dimension preferredSize = panel.getPreferredSize();
		preferredSize.width += 4;
		preferredSize.height += 2;
		panel.setPreferredSize(preferredSize);
	}
	
	/**
	 * Returns the primary graphical component used to display this maze.
	 */
	public JPanel getGraphicalComponent() {
		return panel;
	}

	/**
	 * Sets all squares in the maze as not visited.
	 */
	public void clearVisited() {
		super.clearVisited();
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				updateLabel(x, y);
			}
		}
		updateMoves();
	}
	
	/**
	 * Returns this graphical maze's animation delay in ms.
	 */
	public int getDelay() {
		return delayMS;
	}
	
	/**
	 * Returns whether or not the given x/y position is marked as visited (via setVisited).
	 * Also temporarily draws a colored '?' mark on that square to show the algorithm's progress.
	 * @param x x-position to check
	 * @param y y-position to check
	 * @return true if x/y is marked as visited, false if not
	 * @throws IllegalArgumentException if the x/y location is out of the bounds of the maze
	 */
	public boolean isVisited(int x, int y) {
		return isHelper(x, y, super.isVisited(x, y));
	}
	
	/**
	 * Returns whether or not the given x/y position is a wall of the maze ('#').
	 * Also temporarily draws a colored '?' mark on that square to show the algorithm's progress.
	 * @param x x-position to check
	 * @param y y-position to check
	 * @return true if x/y is a wall, false if not
	 * @throws IllegalArgumentException if the x/y location is out of the bounds of the maze
	 */
	public boolean isWall(int x, int y) {
		return isHelper(x, y, super.isWall(x, y));
	}
	
	/**
	 * Sets the animation delay of this graphical maze to the given number of ms.
	 */
	public void setDelay(int delay) {
		this.delayMS = delay;
	}
	
	/**
	 * Sets the title text that appears above this graphical maze to the given text.
	 */
	public void setTitle(String title) {
		titleLabel.setText(title);
	}
	
	/**
	 * Marks the given x/y position as having been visited (.).
	 * Also draws a colored '.' mark on that square to show the algorithm's progress.
	 * @param x x-position to mark
	 * @param y y-position to mark
	 * @throws IllegalArgumentException if the x/y location is out of the bounds of the maze
	 */
	public void setVisited(int x, int y) {
		super.setVisited(x, y);
		updateLabel(x, y);
		updateMoves();
		pause();
	}
	
	// sets up the button labels for each square in the maze
	private Container createLabels(int width, int height) {
		// calculate font size based on screen dimensions
		int squareSize = Math.min(MAX_SQUARE_SIZE, 
				Math.min(width / width(), height / height()) - 1);
		int fontSize = Math.max(4, Math.min(30, squareSize - 10));
		Font labelFont = new Font("SansSerif", Font.BOLD, fontSize);
		
		JPanel center = new JPanel(new GridLayout(height(), width(), 1, 1));
		center.setBackground(Color.BLACK);
		labels = new JLabel[height()][width()];
		Dimension labelSize = new Dimension(squareSize, squareSize);
		for (int y = 0; y < height(); y++) {
			for (int x = 0; x < width(); x++) {
				JLabel label = new JLabel();
				label.setHorizontalAlignment(JLabel.CENTER);
				label.setOpaque(true);
				label.setBackground(Color.WHITE);
				label.setForeground(Color.BLACK);
				label.setFont(labelFont);
				label.setPreferredSize(labelSize);
				labels[y][x] = label;
				updateLabel(x, y);
				center.add(label);
			}
		}
		return center;
	}
	
	// helper to call one of the maze's 'is' methods and temporarily show a '?' mark
	private boolean isHelper(int x, int y, boolean value) {
		labels[y][x].setText("?");
		labels[y][x].setBackground(Color.YELLOW);
		pause();
		updateLabel(x, y);
		return value;
	}
	
	// causes the board to pause for a given amount of time for animation
	private void pause() {
		if (delayMS > 0) {
			try {
				Thread.sleep(delayMS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	// helper to set the graphical text label at the given x/y position to its current maze state
	private void updateLabel(int x, int y) {
		char square = getSquare(x, y);
		labels[y][x].setText(String.valueOf(square));
		if (square == START) {
			labels[y][x].setBackground(Color.GREEN);
		} else if (square == END) {
			labels[y][x].setBackground(Color.PINK);
		} else if (square == WALL) {
			labels[y][x].setBackground(Color.LIGHT_GRAY);
		} else if (square == VISITED) {
			labels[y][x].setBackground(VISITED_COLOR);
		} else {
			labels[y][x].setBackground(Color.WHITE);
		}
	}
	
	// helper to set the graphical text label of how many squares are visited
	private void updateMoves() {
		visitedCountLabel.setText("Visited: " + visitedCount());
	}
}