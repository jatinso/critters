package so.jatin;

import java.awt.Point;
import java.util.List;

/**
 * Remove the exit (whose cost was zero) from this point and 
 * update the neighborhood costs.
 */
public class RemoveExitOperation implements BoardOperation {

	public void execute(Board board, Point startingPoint) {
		
		board.clearCost(startingPoint); // it was zero.
		
		List<Point> orphans = OrphanFinder.findOrphans(board, startingPoint, 0);
		// This point is now an orphan too. We need to find a new path from it to a different exit.
		orphans.add(startingPoint);
		HomeComingCostPropagator.findPathsForOrphans(board, orphans);
	}
}
