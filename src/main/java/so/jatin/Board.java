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

	public Board(int width, int height) {

		cost = new int[width][height];
		
		// At first there are no exits. So -1 indicates there's no route from here to an exit.
		for (int x = 0; x < width; x++)
			for (int y = 0; y < height; y++)
				cost[x][y] = -1;
	}

	public void addExit(int x, int y) {
		// Cell at (x,y) gets cost zero, and all its neighbours are informed to
		// update their costs.
		cost[x][y] = 0;
		
		Queue<Coordinate> queue = new LinkedList<Coordinate>();
		queue.add(new Coordinate(x, y));
		
		// This is a BFS that propagates out the cost changes due to the creation
		// of the exit cell.
		while (!queue.isEmpty()) {
			Coordinate coordinate = queue.remove();
			int myCost = cost[coordinate.x][coordinate.y];
			
			for (Coordinate neighbour : getNeighbours(coordinate)) {
				int neighbourCost = cost[neighbour.x][neighbour.y];
				
				// Only if a neighbour is impacted, add it to the queue
				if (neighbourCost == -1 || neighbourCost > myCost + 1) {
					cost[neighbour.x][neighbour.y] = myCost + 1;
					queue.add(neighbour); // propagate your changes!
				}
			}
		}
	}

	private List<Coordinate> getNeighbours(Coordinate coordinate) {
		List<Coordinate> neighbours = new ArrayList<Board.Coordinate>();

		if (coordinate.x > 0)
			neighbours.add(new Coordinate(coordinate.x - 1, coordinate.y));

		if (coordinate.y > 0)
			neighbours.add(new Coordinate(coordinate.x, coordinate.y - 1));

		if (coordinate.x < cost.length - 1)
			neighbours.add(new Coordinate(coordinate.x + 1, coordinate.y));

		if (coordinate.y < cost[0].length - 1)
			neighbours.add(new Coordinate(coordinate.x, coordinate.y + 1));

		return neighbours;
	}

	public int getCost(int x, int y) {
		return cost[x][y];
	}

	/*
	 * This is a convenience class for holding x and y coordinates.
	 */
	private class Coordinate {
		public final int x;
		public final int y;
		
		public Coordinate(int x, int y) {
			this.x = x;
			this.y = y;
		}

		@Override
		public String toString() {
			return "[" + x + ", " + y + "]";
		}
	}
}
