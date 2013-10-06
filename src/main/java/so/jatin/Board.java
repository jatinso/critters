package so.jatin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * The Board simply holds the 2D matrix of cells, and provides some simple methods
 * like get/set costs for a cell, mark/unmark it as an exit/obstacle, and some 
 * functionality that looks at the immediate neighbors of a cell.
 */
public class Board {

	private final Integer[][] cost;
	private final boolean[][] obstacle;

	/**
	 * Construct a board with the specified dimensions.
	 * @param width
	 * @param height
	 */
	public Board(int width, int height) {
		cost = new Integer[width][height];
		obstacle = new boolean[width][height]; // all initialized to false by default.
		
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				cost[x][y] = null; // null cost means you can't reach an exit from here.
	}

	/**
	 * Returns the current cost to get from this point to an exit.
	 * @param point
	 * @return
	 */
	public Integer getCost(Point point) {
		return cost[point.x][point.y];
	}
	
	/**
	 * Set the cost of the given point as requested and return the old cost.
	 * @param point
	 * @param toCost
	 * @return the old cost (can be null)
	 */
	public Integer setCost(Point point, Integer toCost) {
		Integer oldCost = getCost(point);
		cost[point.x][point.y] = toCost;
		return oldCost;
	}

	/**
	 * Look at the immediate neighbors of this point and return the best cost.
	 * @param point
	 * @return the best cost when looking at immediate neighbors.
	 */
	public Integer calculateCostFromNeighbors(Point point) {
		Integer lowestCost = null;
		
		for (Point neighbor : getLiveNeighbors(point)) {
			Integer neighborsCost = getCost(neighbor);
			if (neighborsCost != null) {
				if (lowestCost == null || lowestCost > neighborsCost + 1) {
					lowestCost = neighborsCost + 1;
				}
			}
		}
		
		return lowestCost;
	}

	/**
	 * Return the immediate neighboring points that are not obstacles.
	 * @param point
	 * @return
	 */
	public List<Point> getLiveNeighbors(Point point) {
		List<Point> neighbors = new ArrayList<Point>();

		if (point.x > 0 && !obstacle[point.x - 1][point.y])
			neighbors.add(new Point(point.x - 1, point.y));

		if (point.y > 0 && !obstacle[point.x][point.y - 1])
			neighbors.add(new Point(point.x, point.y - 1));

		if (point.x < cost.length - 1 && !obstacle[point.x + 1][point.y])
			neighbors.add(new Point(point.x + 1, point.y));

		if (point.y < cost[0].length - 1 && !obstacle[point.x][point.y + 1])
			neighbors.add(new Point(point.x, point.y + 1));

		return neighbors;
	}

	/**
	 * Put an obstacle here. (This method is oblivious of the impact to the neighborhood)
	 * @param point
	 */
	public void putObstacle(Point point) {
		clearCost(point);
		obstacle[point.x][point.y] = true;
	}

	/**
	 * Clear out the cost. (This method is oblivious of the impact to the neighborhood)
	 * @param point
	 */
	public void clearCost(Point point) {
		cost[point.x][point.y] = null;
	}
}
