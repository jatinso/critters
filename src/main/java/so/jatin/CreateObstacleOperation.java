package so.jatin;

import java.awt.Point;
import java.util.List;

/**
 * Put an obstacle at this point and update the neighborhood costs.
 */
public class CreateObstacleOperation implements BoardOperation {

	public void execute(Board board, Point startingPoint) {

		Integer oldCost = board.getCost(startingPoint);
		board.putObstacle(startingPoint);
		
		if (oldCost != null) { // I.e. this point can have a negative impact on the neighborhood.
			List<Point> orphans = OrphanFinder.findOrphans(board, startingPoint, oldCost);
			HomeComingCostPropagator.findPathsForOrphans(board, orphans);
		}
	}
}
