package so.jatin;

import java.awt.Point;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Traverse the board outwards using breadth first search, updating costs
 * as we go out, until we can't update any more.
 */
public class BfsCostPropagation implements BoardOperation {

	public void execute(Board board, Point startingPoint) {

		Queue<Point> queue = new LinkedList<Point>();
		queue.add(startingPoint);

		while (!queue.isEmpty()) {
			Point point = queue.remove();
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
}
