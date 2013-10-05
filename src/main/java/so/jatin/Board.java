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
		
		// At first there are no exits. So -1 indicates there's no route from here to an exit.
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				cost[x][y] = INFINITY;
	}

	public void addExit(int x, int y) {
		// Cell at (x,y) gets cost zero, and all its neighbours are informed to
		// update their costs.
		cost[x][y] = 0;
		
		Queue<Point> queue = new LinkedList<Point>();
		queue.add(new Point(x, y));
		
		// This is a BFS that propagates out the cost changes due to the creation
		// of the exit cell.
		while (!queue.isEmpty()) {
			Point point = queue.remove();
			int myCost = cost[point.x][point.y];
			
			for (Point neighbor : getNeighbors(point)) {
				int neighborCost = cost[neighbor.x][neighbor.y];
				
				// Only if a neighbour is impacted, add it to the queue
				if (neighborCost == INFINITY || neighborCost > myCost + 1) {
					cost[neighbor.x][neighbor.y] = myCost + 1;
					queue.add(neighbor); // propagate your changes!
				}
			}
		}
	}

	private List<Point> getNeighbors(Point point) {
		List<Point> neighbours = new ArrayList<Board.Point>();

		// An obstacle is not considered as a valid neighbour.
		if (point.x > 0 && !isBlocked(point.x - 1, point.y))
			neighbours.add(new Point(point.x - 1, point.y));

		if (point.y > 0 && !isBlocked(point.x, point.y - 1))
			neighbours.add(new Point(point.x, point.y - 1));

		if (point.x < cost.length - 1 && !isBlocked(point.x + 1, point.y))
			neighbours.add(new Point(point.x + 1, point.y));

		if (point.y < cost[0].length - 1 && !isBlocked(point.x, point.y + 1))
			neighbours.add(new Point(point.x, point.y + 1));

		return neighbours;
	}

	public void addObstacle(int x, int y) {
		/*
		 * When we add an obstacle, downstream cells (those with cost one higher than
		 * this cell) may need to recompute their costs. And if they change, then
		 * so would their downstream cells, and so on.
		 */
		Queue<Point> queue = new LinkedList<Point>();
		queue.addAll(getDownstreamNeighbors(x, y));
		cost[x][y] = OBSTACLE; // Note we have to update the cost AFTER the call to getDownstreamNeighbors().
		
		while (!queue.isEmpty()) {
			Point point = queue.remove();
			int updatedCost = getBestCost(point); // by looking at neighbors
			if (getCost(point) != updatedCost) { // I have changed. So propagate.
				queue.addAll(getDownstreamNeighbors(point.x, point.y));
				cost[point.x][point.y] = updatedCost; // Note we have to update the cost AFTER the call to getDownstreamNeighbors().
			}
		}
	}
	
	private int getBestCost(Point point) {

		int bestCost = INFINITY;
		for (Point neighbor : getNeighbors(point)) {
			int neighborCost = getCost(neighbor);
			if (bestCost == INFINITY || (neighborCost != INFINITY && bestCost > neighborCost))
				bestCost = getCost(neighbor);
		}

		return bestCost + 1; // since it was best neighbor cost.
	}

	private List<Point> getDownstreamNeighbors(int x, int y) {
		List<Point> downstreamNeighbors = new ArrayList<Point>();
		
		for (Point neighbor : getNeighbors(new Point(x, y)))
			if (getCost(neighbor) == getCost(x, y) + 1)
				downstreamNeighbors.add(neighbor);

		return downstreamNeighbors;
	}

	private boolean isBlocked(int x, int y) {
		return cost[x][y] == OBSTACLE;
	}

	public int getCost(int x, int y) {
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
