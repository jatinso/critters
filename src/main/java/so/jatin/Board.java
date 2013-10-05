package so.jatin;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The Board is a 2D matrix of cells. It contains various exits and
 * maintains at all times the shortest path to the closest exit from
 * any cell. Some of the cells can be marked damaged (obstacle cells)
 * which impact the paths from cells to exits.
 */
public class Board {
	
	private final int[][] cost;
	private static final int OBSTACLE = -2;
	private static final int INFINITY = -1;

	public Board(int width, int height) {

		cost = new int[width][height];
		// At first there are no exits. So there's no route from any cell to an exit.
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				cost[x][y] = INFINITY;
	}

	public void addExit(int x, int y) {
		// Cell at (x,y) gets cost zero, and all its neighbours are informed to
		// update their costs.
		setCost(x, y, 0);
		propagate(x, y);
		
	}
	
	public void removeObstacle(int x, int y) {
		int bestCost = getBestCost(new Point(x, y));
		setCost(x, y, bestCost);
		if (bestCost != INFINITY) // I.e. it can positively impact someone...
			propagate(x, y);
	}

	private void propagate(int x, int y) {
		Queue<Point> queue = new LinkedList<Point>();
		queue.add(new Point(x, y));

		// This is a BFS that propagates out the cost changes due to the creation
		// of the exit cell.
		while (!queue.isEmpty()) {
			Point point = queue.remove();
			int myCost = getCost(point);
			for (Point neighbor : getNeighbors(point)) {
				int neighborCost = getCost(neighbor);
				// Only if a neighbor is impacted, add it to the queue
				if (neighborCost == INFINITY || neighborCost > myCost + 1) {
					setCost(neighbor.x, neighbor.y, myCost + 1);
					queue.add(neighbor); // propagate your changes!
				}
			}
		}
		
	}

	/*
	 * Updates the cost and returns the old cost.
	 */
	private int setCost(int x, int y, int theCost) {
		int oldCost = cost[x][y];
		cost[x][y] = theCost;
		return oldCost;
	}

	private List<Point> getNeighbors(Point point) {
		List<Point> neighbors = new ArrayList<Board.Point>();

		// An obstacle is not a valid neighbor.
		if (point.x > 0 && !isBlocked(point.x - 1, point.y))
			neighbors.add(new Point(point.x - 1, point.y));

		if (point.y > 0 && !isBlocked(point.x, point.y - 1))
			neighbors.add(new Point(point.x, point.y - 1));

		if (point.x < cost.length - 1 && !isBlocked(point.x + 1, point.y))
			neighbors.add(new Point(point.x + 1, point.y));

		if (point.y < cost[0].length - 1 && !isBlocked(point.x, point.y + 1))
			neighbors.add(new Point(point.x, point.y + 1));

		return neighbors;
	}

	public void addObstacle(int x, int y) {
		
		// This list contains cells orphaned by the obstacle.
		List<Point> orphans = new ArrayList<Point>();
		int oldCost = getCost(x, y);
		cost[x][y] = OBSTACLE;	
		orphans.addAll(getOrphansFor(x, y, oldCost));

		// Now let's find out who else has become orphaned and make their costs INFINITY.
		int i = 0;
		while (i < orphans.size()) {
			Point orphan = orphans.get(i++);
			oldCost = setCost(orphan.x, orphan.y, INFINITY);
			orphans.addAll(getOrphansFor(orphan.x, orphan.y, oldCost));
		}
		
		// Let's find the best paths for each orphan to an exit.
		// This is an n^2 algorithm (where n is count of orphans) that keeps reducing cost round by round.
		boolean reducedCost = true;
		while (reducedCost) {
			reducedCost = false;
			for (Point orphan : orphans) {
				int bestCost = getBestCost(orphan);
				int currentCost = getCost(orphan);
				if (bestCost != INFINITY && (currentCost == INFINITY || bestCost < currentCost)) {
					setCost(orphan.x, orphan.y, bestCost);
					reducedCost = true;
				}
			}
		}
	}

	private int getBestCost(Point point) {

		int bestCost = INFINITY;
		for (Point neighbor : getNeighbors(point)) {
			int neighborCost = getCost(neighbor);
			if (bestCost == INFINITY || (neighborCost != INFINITY && bestCost > neighborCost))
				bestCost = neighborCost;
		}

		if (bestCost == INFINITY)
			return INFINITY;
		else
			return bestCost + 1; // since it was best neighbor cost.
	}

	/*
	 * An orphan is a neighbor of yours who could only travel to an exit
	 * via you, without incurring a greater cost. If he could take a different route
	 * for the same cost, then he's not orphaned by you becoming an obstacle.
	 */
	private List<Point> getOrphansFor(int x, int y, int oldCost) {
		List<Point> orphans = new ArrayList<Point>();

		for (Point neighbor : getNeighbors(new Point(x, y))) {
			// This neighbor is now worse off. So it's an orphan.
			int currentCost = getCost(neighbor);

			if (currentCost != INFINITY) {
				int bestCost = getBestCost(neighbor);
				if (bestCost == INFINITY || bestCost > oldCost + 1)
					orphans.add(neighbor);
			}
		}

		return orphans;
	}

	private boolean isBlocked(int x, int y) {
		return cost[x][y] == OBSTACLE;
	}

	/**
	 * Returns the cheapest cost to get to an exit. If there's no path to one, it
	 * returns -1, which represents infinity.
	 * 
	 * @param x
	 * @param y
	 * @return
	 */
	public int getCost(int x, int y) {
		if (isBlocked(x, y))
			return INFINITY;
		else
			return cost[x][y];
	}

	private int getCost(Point point) {
		return getCost(point.x, point.y);
	}

	/*
	 * This is a convenience class for holding x and y coordinates.
	 */
	private class Point {
		public final int x;
		public final int y;
		
		public Point(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "[" + x + ", " + y + "]";
		}
	}

}
