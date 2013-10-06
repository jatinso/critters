package so.jatin;

import java.awt.Point;

/**
 * This adds an exit to a board. That means it's cost goes to zero and it's 
 * neighborhood's costs likely go down.
 */
public class AddExitOperation implements BoardOperation {

	public void execute(Board board, Point startingPoint) {

		// point gets cost zero, and all its neighbors are informed to update their costs.
		board.setCost(startingPoint, 0);
		new BfsCostPropagation().execute(board, startingPoint);
	}
}
