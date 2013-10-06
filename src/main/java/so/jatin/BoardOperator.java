package so.jatin;

import java.awt.Point;

/**
 * The BoardOperator operates on a 2D matrix of cells (a board) that
 * contains various exits and maintains at all times the shortest path 
 * to the closest exit from any cell. Some of the cells can be marked 
 * damaged (obstacles) which lengthen the paths from cells to exits.
 * 
 * It's a delegator that delegates to other classes.
 */
public class BoardOperator {

	private final Board board;
	
	public BoardOperator(Board board) {
		this.board = board;
	}

	public Integer getCost(Point point) {
		return board.getCost(point);
	}

	public void addExit(Point point) {
		new AddExitOperation().execute(board, point);
	}

	public void removeObstacle(Point point) {
		new RemoveObstacleOperation().execute(board, point);
	}

	public void putObstacle(Point point) {
		new CreateObstacleOperation().execute(board, point);
	}

	public void removeExit(Point point) {
		new RemoveExitOperation().execute(board, point);
	}
}
