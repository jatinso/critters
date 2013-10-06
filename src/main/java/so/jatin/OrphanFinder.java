package so.jatin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class OrphanFinder {

	/**
	 * Find all the chains of cells that would be orphaned by the input point's cost being cleared.
	 * 
	 * All orphans have their costs cleared as part of this method.
	 */
	public static List<Point> findOrphans(Board board, Point startingPoint, int originalCost) {
		List<Point> orphans = new ArrayList<Point>();
		orphans.addAll(getOrphansFor(board, startingPoint, originalCost)); // initial orphans due to point.

		int i = 0;
		while (i < orphans.size()) { // now get all the chains of newly created orphans.
			Point orphan = orphans.get(i++);
			int oldCost = board.getCost(orphan);
			board.clearCost(orphan);
			orphans.addAll(getOrphansFor(board, orphan, oldCost));
		}

		return orphans;
	}

	/*
	 * An orphan is a neighbor of yours who could only travel to an exit
	 * via you, without incurring a greater cost. If he could take a different route
	 * for the same cost, then he's not orphaned by you losing your path to an exit.
	 */
	private static List<Point> getOrphansFor(Board board, Point point, int oldCost) {

		List<Point> orphans = new ArrayList<Point>();

		for (Point neighbor : board.getLiveNeighbors(point)) {
			// This neighbor is now worse off. So it's an orphan.
			Integer currentNeighborCost = board.getCost(neighbor);

			if (currentNeighborCost != null && currentNeighborCost == oldCost + 1) { // it potentially depended on point.
				Integer lowestCostAfterLosingParent = board.calculateCostFromNeighbors(neighbor);
				if (lowestCostAfterLosingParent == null || lowestCostAfterLosingParent > oldCost + 1) {
					orphans.add(neighbor);
				}
			}
		}

		return orphans;
	}
}
