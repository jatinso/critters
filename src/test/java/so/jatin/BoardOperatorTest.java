package so.jatin;

import static org.junit.Assert.*;

import java.awt.Point;

import org.junit.Before;
import org.junit.Test;

public class BoardOperatorTest {

	private BoardOperator subject;

	@Before
	public void setUp() {
		subject = new BoardOperator(new Board(5, 5));
	}

	@Test
	public void withObstaclesAddedAfterExits() {
		subject.addExit(new Point(1, 0));
		subject.addExit(new Point(2, 3));
		subject.addExit(new Point(4, 0));

		subject.putObstacle(new Point(0, 3));
		subject.putObstacle(new Point(2, 1));
		subject.putObstacle(new Point(3, 2));
		subject.putObstacle(new Point(1, 3));
		subject.putObstacle(new Point(3, 3));
		subject.putObstacle(new Point(2, 4));

		assertBoardWithCertainObstaclesAndExits();
	}
	
	@Test
	public void withObstaclesAddedBeforeExits() {
		subject.putObstacle(new Point(0, 3));
		subject.putObstacle(new Point(2, 1));
		subject.putObstacle(new Point(3, 2));
		subject.putObstacle(new Point(1, 3));
		subject.putObstacle(new Point(3, 3));
		subject.putObstacle(new Point(2, 4));

		subject.addExit(new Point(1, 0));
		subject.addExit(new Point(2, 3));
		subject.addExit(new Point(4, 0));

		assertBoardWithCertainObstaclesAndExits();
	}
	
	@Test
	public void withObstaclesAndExitsAddedIntermingled() {
		subject.putObstacle(new Point(2, 1));
		subject.addExit(new Point(1, 0));
		subject.putObstacle(new Point(3, 2));
		subject.putObstacle(new Point(0, 3));
		subject.putObstacle(new Point(1, 3));
		subject.addExit(new Point(2, 3));
		subject.putObstacle(new Point(3, 3));
		subject.putObstacle(new Point(2, 4));
		subject.addExit(new Point(4, 0));

		assertBoardWithCertainObstaclesAndExits();
	}

	@Test
	public void removeObstacle() {
		// Arrange
		subject.addExit(new Point(1, 0));
		subject.addExit(new Point(2, 3));
		subject.addExit(new Point(4, 0));

		subject.putObstacle(new Point(0, 3));
		subject.putObstacle(new Point(2, 1));
		subject.putObstacle(new Point(3, 2));
		subject.putObstacle(new Point(1, 3));
		subject.putObstacle(new Point(3, 3));
		subject.putObstacle(new Point(2, 4));

		// Act
		subject.removeObstacle(new Point(2, 4));

		// Assert
		/*
		 * Costs should look like this (top left is (0,0)).
		 * O represents an exit, which has cost 0.
		 * X represents an obstacle, which has cost -1.
		 * Cost -1 represents that there's no path to an exit.
		 * 
		 *  1 [O] 1  1 [O]
		 *  2  1 [X] 2  1
		 *  3  2  1 [X] 2
		 * [X][X][O][X] 3
		 *  3  2  1  2  3
		 */
		
		// Assert
		assertTrue(1 == subject.getCost(new Point(0, 0)));
		assertTrue(2 == subject.getCost(new Point(0, 1)));
		assertTrue(3 == subject.getCost(new Point(0, 2)));
		assertNull(subject.getCost(new Point(0, 3)));
		assertTrue(3 == subject.getCost(new Point(0, 4)));

		assertTrue(0 == subject.getCost(new Point(1, 0)));
		assertTrue(1 == subject.getCost(new Point(1, 1)));
		assertTrue(2 == subject.getCost(new Point(1, 2)));
		assertNull(subject.getCost(new Point(1, 3)));
		assertTrue(2 == subject.getCost(new Point(1, 4)));

		assertTrue(1 == subject.getCost(new Point(2, 0)));
		assertNull(subject.getCost(new Point(2, 1)));
		assertTrue(1 == subject.getCost(new Point(2, 2)));
		assertTrue(0 == subject.getCost(new Point(2, 3)));
		assertTrue(1 == subject.getCost(new Point(2, 4)));

		assertTrue(1 == subject.getCost(new Point(3, 0)));
		assertTrue(2 == subject.getCost(new Point(3, 1)));
		assertNull(subject.getCost(new Point(3, 2)));
		assertNull(subject.getCost(new Point(3, 3)));
		assertTrue(2 == subject.getCost(new Point(3, 4)));

		assertTrue(0 == subject.getCost(new Point(4, 0)));
		assertTrue(1 == subject.getCost(new Point(4, 1)));
		assertTrue(2 == subject.getCost(new Point(4, 2)));
		assertTrue(3 == subject.getCost(new Point(4, 3)));
		assertTrue(3 == subject.getCost(new Point(4, 4)));

	}

	@Test
	public void removeExit() {
		// Arrange
		subject.addExit(new Point(1, 0));
		subject.addExit(new Point(2, 3));
		subject.addExit(new Point(4, 0));

		subject.putObstacle(new Point(0, 3));
		subject.putObstacle(new Point(2, 1));
		subject.putObstacle(new Point(3, 2));
		subject.putObstacle(new Point(1, 3));
		subject.putObstacle(new Point(3, 3));
		subject.putObstacle(new Point(2, 4));

		// Act
		subject.removeExit(new Point(2, 3));

		// Assert
		/*
		 * Costs should look like this (top left is (0,0)).
		 * O represents an exit, which has cost 0.
		 * X represents an obstacle, which has cost -1.
		 * Cost -1 represents that there's no path to an exit.
		 * 
		 *  1 [O] 1  1 [O]
		 *  2  1 [X] 2  1
		 *  3  2  3 [X] 2
		 * [X][X] 4 [X] 3
		 * -1 -1 [X] 5  4
		 */
		
		// Assert
		assertTrue(1 == subject.getCost(new Point(0, 0)));
		assertTrue(2 == subject.getCost(new Point(0, 1)));
		assertTrue(3 == subject.getCost(new Point(0, 2)));
		assertNull(subject.getCost(new Point(0, 3)));
		assertNull(subject.getCost(new Point(0, 4)));

		assertTrue(0 == subject.getCost(new Point(1, 0)));
		assertTrue(1 == subject.getCost(new Point(1, 1)));
		assertTrue(2 == subject.getCost(new Point(1, 2)));
		assertNull(subject.getCost(new Point(1, 3)));
		assertNull(subject.getCost(new Point(1, 4)));

		assertTrue(1 == subject.getCost(new Point(2, 0)));
		assertNull(subject.getCost(new Point(2, 1)));
		assertTrue(3 == subject.getCost(new Point(2, 2)));
		assertTrue(4 == subject.getCost(new Point(2, 3)));
		assertNull(subject.getCost(new Point(2, 4)));

		assertTrue(1 == subject.getCost(new Point(3, 0)));
		assertTrue(2 == subject.getCost(new Point(3, 1)));
		assertNull(subject.getCost(new Point(3, 2)));
		assertNull(subject.getCost(new Point(3, 3)));
		assertTrue(5 == subject.getCost(new Point(3, 4)));

		assertTrue(0 == subject.getCost(new Point(4, 0)));
		assertTrue(1 == subject.getCost(new Point(4, 1)));
		assertTrue(2 == subject.getCost(new Point(4, 2)));
		assertTrue(3 == subject.getCost(new Point(4, 3)));
		assertTrue(4 == subject.getCost(new Point(4, 4)));

	}
	
	private void assertBoardWithCertainObstaclesAndExits() {
		/*
		 * Costs should look like this (top left is (0,0)).
		 * O represents an exit, which has cost 0.
		 * X represents an obstacle, which has cost -1.
		 * Cost -1 represents that there's no path to an exit.
		 * 
		 *  1 [O] 1  1 [O]
		 *  2  1 [X] 2  1
		 *  3  2  1 [X] 2
		 * [X][X][O][X] 3
		 * -1 -1 [X] 5  4
		 */
		
		// Assert
		assertTrue(1 == subject.getCost(new Point(0, 0)));
		assertTrue(2 == subject.getCost(new Point(0, 1)));
		assertTrue(3 == subject.getCost(new Point(0, 2)));
		assertNull(subject.getCost(new Point(0, 3)));
		assertNull(subject.getCost(new Point(0, 4)));

		assertTrue(0 == subject.getCost(new Point(1, 0)));
		assertTrue(1 == subject.getCost(new Point(1, 1)));
		assertTrue(2 == subject.getCost(new Point(1, 2)));
		assertNull(subject.getCost(new Point(1, 3)));
		assertNull(subject.getCost(new Point(1, 4)));

		assertTrue(1 == subject.getCost(new Point(2, 0)));
		assertNull(subject.getCost(new Point(2, 1)));
		assertTrue(1 == subject.getCost(new Point(2, 2)));
		assertTrue(0 == subject.getCost(new Point(2, 3)));
		assertNull(subject.getCost(new Point(2, 4)));

		assertTrue(1 == subject.getCost(new Point(3, 0)));
		assertTrue(2 == subject.getCost(new Point(3, 1)));
		assertNull(subject.getCost(new Point(3, 2)));
		assertNull(subject.getCost(new Point(3, 3)));
		assertTrue(5 == subject.getCost(new Point(3, 4)));

		assertTrue(0 == subject.getCost(new Point(4, 0)));
		assertTrue(1 == subject.getCost(new Point(4, 1)));
		assertTrue(2 == subject.getCost(new Point(4, 2)));
		assertTrue(3 == subject.getCost(new Point(4, 3)));
		assertTrue(4 == subject.getCost(new Point(4, 4)));
	}
}
