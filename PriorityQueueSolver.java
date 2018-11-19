/* 
 * PriorityQueueSolver class uses Java's priority queue class or my own implementation of a
 * HeapPriorityQueue to solve a maze. An inner class implements the Comparator interface to order
 * the point objects in the queue.
 */

import java.awt.Point;
import java.util.Comparator;

// TODO: comment out these two imports to use your own priority queue
import java.util.PriorityQueue;
import java.util.Queue;


public class PriorityQueueSolver implements Solver {
	Maze m; // Remembers the maze that is to be solved.
	
	// Accepts a maze to solve and returns whether there is a solution found.
	// Throws a NullPointerException if the maze passed is null.
	public boolean solve(Maze maze) {
		// TODO: implement this method
		if (maze == null)
			throw new NullPointerException();
		Comparator<Point> comp = new myComparatorClass();
		Queue<Point> solver = new PriorityQueue<Point>(999, comp);
		m = maze;
		solver.add(m.start());
		while (!solver.isEmpty()) {
			Point L = solver.remove();
			if (L.equals(m.end()))
				return true;
			else if (!maze.isVisited(L.x, L.y) && !m.isWall(L.x, L.y)) {
				maze.setVisited(L.x, L.y);
				addToQueue(L.x, L.y - 1, solver); // up neighbor
				addToQueue(L.x, L.y + 1, solver); // down neighbor
				addToQueue(L.x - 1, L.y, solver); // left neighbor
				addToQueue(L.x + 1, L.y, solver); // right neighbor
			}
		}
		return false;
	}
	
	// Private helper method that receives a point and returns whether the point is valid.
	private boolean isValid(Point p) {
		return m.isInBounds(p.x, p.y) && !m.isWall(p.x, p.y);
	}
	
	// Private helper method that receives an x/y coordinate and adds it into the queue.
	private void addToQueue(int x, int y, Queue<Point> solver) {
		Point p = new Point(x, y);
		if (isValid(p)) {
			solver.add(p);
		}
	}
	
	// Private inner class that implements the comparator interface to order points in the queue.
	private class myComparatorClass implements Comparator<Point> {
		
		// Accepts two point objects and returns an integer to order the points in the queue.
		public int compare(Point p1, Point p2) {
			int priority;
			if (p1.distance(m.end()) - p2.distance(m.end()) < 0)
				priority = -1;
			else if (p1.distance(m.end()) - p2.distance(m.end()) > 0)
				priority = 1;
			else // (p1.distance(m.end()) - p2.distance(m.end()) = 0)
				priority = 0;
			return priority;
		}
	}
}

