package so.jatin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.stanford.nlp.util.BinaryHeapPriorityQueue;
import edu.stanford.nlp.util.PriorityQueue;

/**
 * The Board is a 2D matrix of cells. It contains various exits and
 * maintains at all times the shortest path to the closest exit from
 * any cell. Some of the cells can be marked damaged (obstacles)
 * which lengthen the paths from cells to exits.
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
		// Cell at (x,y) gets cost zero, and all its neighbors are informed to
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
		List<Point> neighbors = new ArrayList<Point>();

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
		int oldCost = setCost(x, y, OBSTACLE);
		List<Point> orphans = new ArrayList<Point>();
		findOrphansAndRecomputeCosts(x, y, oldCost, orphans);
	}

	public void removeExit(int x, int y) {
		int oldCost = setCost(x, y, INFINITY);
		List<Point> orphans = new ArrayList<Point>();
		orphans.add(new Point(x, y));
		findOrphansAndRecomputeCosts(x, y, oldCost, orphans);
	}

	/*
	 * This method expands the list of orphans by finding out what other cell
	 * would be orphaned as a result of the current orphans, and so on.
	 * 
	 * It then recomputes the best weights for these orphans.
	 */
	private void findOrphansAndRecomputeCosts(int x, int y, int oldCost, List<Point> orphans) {

		// First add all orphans for the original distressed point (x, y).
		orphans.addAll(getOrphansFor(x, y, oldCost));

		// Now let's find out who else has become orphaned and make their costs INFINITY.
		findAllOrphans(orphans);
		// Put them into a priority queue
		PriorityQueue<Point> priorityQueue = new BinaryHeapPriorityQueue<Point>();

		for (Point orphan : orphans)
			priorityQueue.add(orphan, getCostAsPriority(orphan)); // This PQ considers higher numbers as higher priority. So we negate best cost.

		// Let's find the best paths for each orphan to an exit.
		// This is an n*lg(n) algorithm where the best cost nodes are explored first.
		while (!priorityQueue.isEmpty()) {
			Point orphan = priorityQueue.removeFirst();
			setCost(orphan.x, orphan.y, getBestCost(orphan));
			for (Point neighbor : getNeighbors(orphan))
				if (priorityQueue.contains(neighbor)) // Complexity?
					priorityQueue.changePriority(neighbor, getCostAsPriority(neighbor));
		}
	}
	
	private double getCostAsPriority(Point point) {
		int bestCost = getBestCost(point);
		if (bestCost < 0)
			return -Integer.MAX_VALUE;
		else
			return -bestCost;
	}

	private void findAllOrphans(List<Point> orphans) {
		int i = 0;
		while (i < orphans.size()) {
			Point orphan = orphans.get(i++);
			int oldCost = setCost(orphan.x, orphan.y, INFINITY);
			orphans.addAll(getOrphansFor(orphan.x, orphan.y, oldCost));
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
}
