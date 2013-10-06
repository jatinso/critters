package so.jatin;

import java.awt.Point;

/**
 * Remove the obstacle previously at this point. That means we'll have to recompute
 * what it's cost is, and the effect of this change on the neighborhood.
 */
public class RemoveObstacleOperation implements BoardOperation {

	public void execute(Board board, Point startingPoint) {

		Integer lowestCost = board.calculateCostFromNeighbors(startingPoint);
		board.setCost(startingPoint, lowestCost);

		if (lowestCost != null) // I.e. it can positively impact someone...
			new BfsCostPropagation().execute(board, startingPoint);
	}
}
