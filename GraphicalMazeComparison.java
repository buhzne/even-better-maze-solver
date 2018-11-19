import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;

/**
 * A graphical window that displays two mazes side-by-side and runs a maze solver
 * algorithm on each one, allowing you to compare the algorithms to see which
 * one solves the maze more quickly.
 */
public class GraphicalMazeComparison {
	private GraphicalMaze maze1;
	private GraphicalMaze maze2;
	private Solver solver1;
	private Solver solver2;

	/**
	 * Constructs a new graphical maze comparison using the given maze text
	 * and the given two algorithms to search for paths to solve it.
	 * @param mazeText the string of lines to pass; the complete contents of a maze input file
	 * @param solver1 the maze solver algorithm to look for a path in the first maze 
	 * @param solver2 the maze solver algorithm to look for a path in the second maze 
	 * @throws IllegalArgumentException if the file is in an invalid format
	 */
	public GraphicalMazeComparison(String mazeText, Solver solver1, Solver solver2) {
		setLookAndFeel();
		
		// don't let this thing get too big to fit on the screen
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		int mazeWidth  = screen.width  / 2 - 100;
		int mazeHeight = screen.height - 150;
		
		this.maze1 = new GraphicalMaze(
				mazeText, solver1.getClass().getName(), mazeWidth, mazeHeight);
		this.maze2 = new GraphicalMaze(
				mazeText, solver2.getClass().getName(), mazeWidth, mazeHeight);
		this.solver1 = solver1;
		this.solver2 = solver2;
		
		// add slider to control animation delay at bottom
		JSlider slider = new JSlider(0, GraphicalMaze.MAX_PAUSE - 10);
		slider.setValue(maze1.getDelay());
		slider.addChangeListener(new MazeSliderChangeListener());
		
		Container south = new JPanel();
		south.add(new JLabel("slow"));
		south.add(slider);
		south.add(new JLabel("fast"));
		
		JFrame frame = new JFrame("Maze Solver Comparison");
		// frame.add(center);
		frame.setContentPane(new JPanel(new BorderLayout(2, 2)));
		frame.add(maze1.getGraphicalComponent(), BorderLayout.WEST);
		frame.add(maze2.getGraphicalComponent(), BorderLayout.EAST);
		frame.add(south, BorderLayout.SOUTH);
		frame.pack();
		frame.setBackground(Color.BLACK);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// center on screen
		frame.setLocation((screen.width - frame.getWidth()) / 2, 
				(screen.height - frame.getHeight()) / 2);
		
		frame.setAlwaysOnTop(true);
		frame.setVisible(true);
		frame.toFront();
	}
	
	/**
	 * Instructs this maze comparison to start running both maze solver
	 * algorithms to try to find paths through their respective mazes.
	 */
	public void solveBoth() {
		System.out.println("Both algorithms searching for a path ...");
		System.out.println();
		
		Thread thread1 = new Thread(new MazeSolverRunnable(maze1, solver1));
		Thread thread2 = new Thread(new MazeSolverRunnable(maze2, solver2));
		thread1.start();
		thread2.start();
	}
	
	// Sets the GUI to use an OS-specific look and feel
	private void setLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();  // will not happen
		} catch (InstantiationException e) {
			e.printStackTrace();  // will not happen
		} catch (IllegalAccessException e) {
			e.printStackTrace();  // will not happen
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();  // will not happen
		}
	}
	
	/**
	 * A thread Runnable for executing maze solver algorithms in parallel.
	 */
	private class MazeSolverRunnable implements Runnable {
		private Maze maze;
		private Solver solver;
		
		/**
		 * Constructs a runnable to solve the given maze with the given algorithm.
		 */
		public MazeSolverRunnable(Maze maze, Solver solver) {
			this.maze = maze;
			this.solver = solver;
		}
		
		/**
		 * Executes the maze solver algorithm and prints the maze and
		 * various status messages afterward.
		 */
		public void run() {
			String solverClassName = solver.getClass().getName();
			boolean solved = solver.solve(maze);
			synchronized (MazeSolverRunnable.class) {
				if (solved) {
					System.out.println(solverClassName + ": Solved the maze (visited "
							+ maze.visitedCount() + " locations).");
				} else {
					System.out.println(solverClassName + ": No path to exit found (visited "
							+ maze.visitedCount() + " locations).");
				}
				System.out.println(maze);
				System.out.println();
			}
		}
	}
	
	/**
	 * A helper class to listen to change events on the speed slider.
	 */
	private class MazeSliderChangeListener implements ChangeListener {
		/**
		 * Called when the slider's value changes during a slide action.
		 */
		public void stateChanged(ChangeEvent e) {
			JSlider slider = (JSlider) e.getSource();
			int delayMS = GraphicalMaze.MAX_PAUSE - slider.getValue();
			maze1.setDelay(delayMS);
			maze2.setDelay(delayMS);
		}
	}
}
