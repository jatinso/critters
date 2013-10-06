package so.jatin;

import java.awt.Point;

/**
 * This is the command pattern. An operation on a board takes in
 * the board to work on, and the point to start at.
 */
public interface BoardOperation {

	void execute(Board board, Point startingPoint);
}
