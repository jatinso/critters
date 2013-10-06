package so.jatin;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import edu.stanford.nlp.util.BinaryHeapPriorityQueue;
import edu.stanford.nlp.util.PriorityQueue;

/**
 * The BoardOperator operates on a 2D matrix of cells (a board) that
 * contains various exits and maintains at all times the shortest path 
 * to the closest exit from any cell. Some of the cells can be marked 
 * damaged (obstacles) which lengthen the paths from cells to exits.
 * 
 * This class is responsible for propagating changes to the board.
 */
public class BoardOperator {

	private final Board board;
	
	public BoardOperator(Board board) {
		this.board = board;
	}

	/**
	 * Get the cost of traveling from this cell to an exit.
	 * @param point
	 * @return
	 */
	public Integer getCost(Point point) {
		return board.getCost(point);
	}

	/**
	 * This point becomes an exit. That means it's cost goes to zero and it's neighborhood's
	 * costs likely go down.
	 * @param point
	 */
	public void addExit(Point point) {
		// point gets cost zero, and all its neighbors are informed to update their costs.
		board.setCost(point, 0);
		propagateCosts(point);
	}

	/**
	 * Remove the obstacle previously at this point. That means we'll have to recompute
	 * what it's cost is, and the effect of this change on the neighborhood.
	 * @param point
	 */
	public void removeObstacle(Point point) {
		Integer lowestCost = board.calculateCostFromNeighbors(point);
		board.setCost(point, lowestCost);
		if (lowestCost != null) // I.e. it can positively impact someone...
			propagateCosts(point);
	}

	/*
	 * This performs a BFS traversal, updating cell costs outwards of the initial point.
	 */
	private void propagateCosts(Point point) {
		Queue<Point> queue = new LinkedList<Point>();
		queue.add(point);

		while (!queue.isEmpty()) {
			point = queue.remove();
			Integer pCost = board.getCost(point);
			
			if (pCost != null) { // i.e. p can positively impact others..
				for (Point neighbor : board.getLiveNeighbors(point)) {
					Integer neighborCost = board.getCost(neighbor);
					if (neighborCost == null || neighborCost > pCost + 1) {
						board.setCost(neighbor, pCost + 1);
						queue.add(neighbor); // propagate your changes!
					}
				}
			}
		}
	}

	/**
	 * Put an obstacle at this point and update the neighborhood costs.
	 * @param point
	 */
	public void putObstacle(Point point) {
		Integer oldCost = board.getCost(point);
		board.putObstacle(point);
		
		if (oldCost != null) { // I.e. this point can have a negative impact on the neighborhood.
			List<Point> orphans = findAllOrphans(point, oldCost);
			recomputeCostsForOrphans(orphans);
		}
	}

	/**
	 * Remove the exit (cost zero) from this point and update the neighborhood costs.
	 * @param point
	 */
	public void removeExit(Point point) {
		board.clearCost(point);
		List<Point> orphans = findAllOrphans(point, 0);
		orphans.add(point); // This point is now an orphan too.
		recomputeCostsForOrphans(orphans);
	}

	/*
	 * This method recomputes the best weights for these orphans.
	 */
	private void recomputeCostsForOrphans(List<Point> orphans) {
		PriorityQueue<Point> priorityQueue = new BinaryHeapPriorityQueue<Point>();

		for (Point orphan : orphans)
			priorityQueue.add(orphan, getCostAsPriority(orphan));

		// Let's find the best paths for each orphan to an exit.
		// This is an n*lg(n) algorithm where the lowest cost nodes are explored first.
		while (!priorityQueue.isEmpty()) {
			Point orphan = priorityQueue.removeFirst();
			board.setCost(orphan, board.calculateCostFromNeighbors(orphan));

			for (Point neighbor : board.getLiveNeighbors(orphan))
				if (priorityQueue.contains(neighbor)) // TODO Complexity?
					priorityQueue.changePriority(neighbor, getCostAsPriority(neighbor));
		}
	}

	/*
	 * We want to assign higher priority values for lower costs.
	 */
	private double getCostAsPriority(Point point) {
		Integer bestCost = board.calculateCostFromNeighbors(point);
		if (bestCost == null)
			return -Integer.MAX_VALUE;
		else
			return -bestCost;
	}

	/*
	 * Find all the chains of cells that would be orphaned by the input point's cost being cleared.
	 * 
	 * All orphans have their costs cleared as part of this method.
	 */
	private List<Point> findAllOrphans(Point point, Integer oldCost) {
		List<Point> orphans = new ArrayList<Point>();
		orphans.addAll(getOrphansFor(point, oldCost)); // initial orphans due to point.

		int i = 0;
		while (i < orphans.size()) { // now get all the chains of newly created orphans.
			Point orphan = orphans.get(i++);
			oldCost = board.getCost(orphan);
			board.clearCost(orphan);
			orphans.addAll(getOrphansFor(orphan, oldCost));
		}

		return orphans;
	}

	/*
	 * An orphan is a neighbor of yours who could only travel to an exit
	 * via you, without incurring a greater cost. If he could take a different route
	 * for the same cost, then he's not orphaned by you losing your path to an exit.
	 */
	private List<Point> getOrphansFor(Point point, int oldCost) {
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
