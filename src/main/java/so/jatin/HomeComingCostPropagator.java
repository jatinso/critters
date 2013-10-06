package so.jatin;

import java.awt.Point;
import java.util.List;

import edu.stanford.nlp.util.BinaryHeapPriorityQueue;
import edu.stanford.nlp.util.PriorityQueue;

public class HomeComingCostPropagator {

	/**
	 * Orphans are cells that have no paths recorded to exits although
	 * paths may exist. They are created when an exit is removed from
	 * a board, or when an obstacle is added.
	 * 
	 * This method contains the logic to link up orphans to nodes that
	 * do have paths to the exits.
	 * @param board
	 * @param orphans
	 */
	public static void findPathsForOrphans(Board board, List<Point> orphans) {
		PriorityQueue<Point> priorityQueue = new BinaryHeapPriorityQueue<Point>();

		for (Point orphan : orphans)
			priorityQueue.add(orphan, getCostAsPriority(board, orphan));

		// Let's find the best paths for each orphan to an exit.
		// This is an n*lg(n) algorithm where the lowest cost nodes are explored first.
		while (!priorityQueue.isEmpty()) {
			Point orphan = priorityQueue.removeFirst();
			board.setCost(orphan, board.calculateCostFromNeighbors(orphan));

			for (Point neighbor : board.getLiveNeighbors(orphan))
				if (priorityQueue.contains(neighbor)) // TODO Complexity?
					priorityQueue.changePriority(neighbor, getCostAsPriority(board, neighbor));
		}
	}

	/*
	 * We want to assign higher priority values for lower costs.
	 */
	private static double getCostAsPriority(Board board, Point point) {
		Integer bestCost = board.calculateCostFromNeighbors(point);
		if (bestCost == null)
			return -Integer.MAX_VALUE;
		else
			return -bestCost;
	}
}
