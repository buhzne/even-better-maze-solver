import java.awt.Point;

/**
 * A Maze object represents a 2-D maze with walls, open squares, a start and end
 * square. It is intended that a client can search through a maze for a path
 * from the start to the end and mark squares as 'visited' along the way.
 */
public class Maze {
	protected static final char START = 'S';
	protected static final char END = 'E';
	protected static final char WALL = '#';
	protected static final char OPEN = ' ';
	protected static final char VISITED = '.';

	private final char[][] squares;
	private final boolean[][] visited;
	private int visitedCount;
	private Point start;
	private Point end;

	/**
	 * Constructs a new maze using the given text string of lines.
	 * @param text the string of lines to pass; the complete contents of a maze input file
	 * @throws IllegalArgumentException if the file is in an invalid format
	 */
	public Maze(String text) {
		if (text == null || text.length() == 0) {
			throw new IllegalArgumentException("null text data");
		}
		String[] lines = text.replaceAll("[\n]+", "\n").split("\n");
		if (lines.length == 0 || lines[0].length() == 0) {
			throw new IllegalArgumentException("empty text data");
		}

		squares = new char[lines.length][lines[0].length()];
		visited = new boolean[lines.length][lines[0].length()];
		visitedCount = 0;

		int height = height();
		for (int y = 0; y < height; y++) {
			parseLine(lines[y], y);
		}

		if (start == null || end == null) {
			throw new IllegalStateException("missing start or end marker");
		}
	}

	/**
	 * Sets all squares in the maze as not visited.
	 */
	public void clearVisited() {
		for (int y = 0, h = height(); y < h; y++) {
			for (int x = 0, w = width(); x < w; x++) {
				visited[y][x] = false;
			}
		}
		visitedCount = 0;
	}

	/**
	 * Returns the location of the end square in the maze ('E').
	 * @return the end square
	 */
	public Point end() {
		return new Point(end); // must return a copy because Point is mutable
	}

	/**
	 * Returns the maze's height.
	 * @return maze's height; equal to the number of non-empty lines in the input file
	 */
	public int height() {
		return squares.length;
	}

	/**
	 * Returns whether or not the given x/y position is within the bounds of this maze.
	 * @param x x-position to check
	 * @param y y-position to check
	 * @return true if x/y is on the maze, false if not
	 */
	public boolean isInBounds(int x, int y) {
		return y >= 0 && y < height() && x >= 0 && x < width();
	}

	/**
	 * Returns whether or not the given x/y position is marked as visited (via setVisited).
	 * 
	 * @param x x-position to check
	 * @param y y-position to check
	 * @return true if x/y is marked as visited, false if not
	 * @throws IllegalArgumentException if the x/y location is out of the bounds of the maze
	 */
	public boolean isVisited(int x, int y) {
		checkIndexes(x, y);
		return visited[y][x];
	}

	/**
	 * Returns whether or not the given x/y position is a wall of the maze ('#').
	 * @param x x-position to check
	 * @param y y-position to check
	 * @return true if x/y is a wall, false if not
	 * @throws IllegalArgumentException if the x/y location is out of the bounds of the maze
	 */
	public boolean isWall(int x, int y) {
		checkIndexes(x, y);
		return squares[y][x] == WALL;
	}

	/**
	 * Marks the given x/y position as having been visited (.).
	 * @param x x-position to mark
	 * @param y y-position to mark
	 * @throws IllegalArgumentException if the x/y location is out of the bounds of the maze
	 */
	public void setVisited(int x, int y) {
		checkIndexes(x, y);
		if (!visited[y][x]) {
			visited[y][x] = true;
			visitedCount++;
		}
	}

	/**
	 * Returns the location of the start square in the maze ('E').
	 * @return the start square
	 */
	public Point start() {
		return new Point(start); // must return a copy because Point is mutable
	}

	/**
	 * Returns a string representation of this Maze. Generally that will look
	 * just like the input file it came from, except with dot marks (.) for each
	 * square marked as visited.
	 * @return maze's string representation
	 */
	public String toString() {
		StringBuilder sb = new StringBuilder((width() + 2) * (height() + 2));
		for (int y = 0, height = height(); y < height; y++) {
			for (int x = 0, width = width(); x < width; x++) {
				sb.append(getSquare(x, y));
			}
			if (y < height - 1) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}
	
	/**
	 * Returns a count of how many squares have been visited in the maze presently.
	 * @return count of how many squares are visited (initially 0)
	 */
	public int visitedCount() {
		return visitedCount;
	}

	/**
	 * Returns the maze's width.
	 * @return maze's width; equal to the number of characters per line in the input file
	 */
	public int width() {
		return squares[0].length;
	}

	/**
	 * Returns the character located at the given x/y position.
	 */
	protected char getSquare(int x, int y) {
		checkIndexes(x, y);
		if (squares[y][x] == OPEN && visited[y][x]) {
			return VISITED;
		} else {
			return squares[y][x];
		}
	}

	/**
	 * Throws an IllegalArgumentException if the given x/y location is off the board.
	 */
	private void checkIndexes(int x, int y) {
		if (!isInBounds(x, y)) {
			throw new IllegalArgumentException("illegal indexes: (" + x + ", " + y + ")");
		}
	}

	/**
	 * Helper called by constructor to read and split one line of text of the maze.
	 */
	private void parseLine(String line, int y) {
		int len = line.length();
		int width = width();
		if (len != width) {
			throw new IllegalArgumentException("line " + y + " wrong length " + len
					+ " (should be " + width + ")");
		}

		for (int x = 0; x < width; x++) {
			char c = line.charAt(x);
			squares[y][x] = c;
			if (c == START) {
				start = new Point(x, y);
			} else if (c == END) {
				end = new Point(x, y);
			} else if (c != WALL && c != OPEN) {
				throw new IllegalArgumentException("line " + y + " character " + x
						+ " (" + c + ", ASCII #" + (int) c + ") illegal");
			}
		}
	}
}
