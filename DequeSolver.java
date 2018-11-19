/* 
 * The MazeSolver class runs an algorithm to find a solution for the maze passed to it.
 */

import java.awt.*;
import java.util.*;

public class DequeSolver implements Solver {
	
	// Receives a maze to solve and returns whether the maze has a solution to be solved. Throws new 
	// NullPointerException if the maze is null. 
	public boolean solve(Maze maze) {
		if (maze == null) 
			throw new NullPointerException();
		
		Deque<Point> solver = new ArrayDeque<Point>();
		solver.addFirst(maze.start());
		while (!solver.isEmpty()) {
			Point L = solver.removeFirst();
			if (L.equals(maze.end()))
				return true;
			else if (!maze.isVisited(L.x, L.y) && !maze.isWall(L.x, L.y)) {
				maze.setVisited(L.x, L.y);
				addToDeque(L.x, L.y - 1, maze, solver, L); // up neighbor
				addToDeque(L.x, L.y + 1, maze, solver, L); // down neighbor
				addToDeque(L.x - 1, L.y, maze, solver, L); // left neighbor
				addToDeque(L.x + 1, L.y, maze, solver, L); // right neighbor
			}
		}
		return false;
	}
	
	// Private helper method that receives a maze and point and returns whether the point is valid within t
	// the maze.
	private boolean isValid(Point p, Maze m) {
		return m.isInBounds(p.x, p.y) && !m.isWall(p.x, p.y);
	}
	
	// Private helper method that receives an x/y coordinate, maze, deque, and current point. Creates a 
	// new point with the given coordinates and compares with the current point to determine which is 
	// farther from the end, adding the closer ones at the beginning of the deque and the further ones 
	// at the end of the deque.
	private void addToDeque(int x, int y, Maze m, Deque<Point> solver, Point L) {
		Point p = new Point(x, y);
		if (isValid(p, m)) {
			if (p.distance(m.end()) < L.distance(m.end()))
				solver.addFirst(p);
			else 
				solver.addLast(p);
		}
	}
}
