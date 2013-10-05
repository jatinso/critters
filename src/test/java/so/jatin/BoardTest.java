package so.jatin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BoardTest {

	private Board subject;

	@Test
	public void withObstaclesAddedAfterExits() {
		subject = new Board(5, 5);

		subject.addExit(1,  0);
		subject.addExit(2,  3);
		subject.addExit(4,  0);

		subject.addObstacle(2, 1);
		subject.addObstacle(3, 2);
		subject.addObstacle(1, 3);
		subject.addObstacle(3, 3);
		subject.addObstacle(2, 4);

		assertBoardWithCertainObstaclesAndExits();
	}
	
	@Test
	public void withObstaclesAddedBeforeExits() {
		subject = new Board(5, 5);

		subject.addObstacle(2, 1);
		subject.addObstacle(3, 2);
		subject.addObstacle(1, 3);
		subject.addObstacle(3, 3);
		subject.addObstacle(2, 4);

		subject.addExit(1,  0);
		subject.addExit(2,  3);
		subject.addExit(4,  0);
	}
	
	@Test
	public void withObstaclesAndExitsAddedIntermingled() {
		subject = new Board(5, 5);

		subject.addObstacle(2, 1);
		subject.addExit(1,  0);
		subject.addObstacle(3, 2);
		subject.addObstacle(1, 3);
		subject.addExit(2,  3);
		subject.addObstacle(3, 3);
		subject.addObstacle(2, 4);
		subject.addExit(4,  0);

	}
	
	private void assertBoardWithCertainObstaclesAndExits() {
		/*
		 * Costs should look like this (top left is (0,0)).
		 * X represents an obstacle, which has cost -2. 
		 * That just means there's no path from an obstacle to an exit.
		 * 
		 * 1 0 1 1 0
		 * 2 1 X 2 1
		 * 3 2 1 X 2
		 * 4 X 0 X 3
		 * 5 6 X 5 4
		 */
		
		// Assert
		assertEquals(1, subject.getCost(0, 0));
		assertEquals(2, subject.getCost(0, 1));
		assertEquals(3, subject.getCost(0, 2));
		assertEquals(4, subject.getCost(0, 3));
		assertEquals(5, subject.getCost(0, 4));

		assertEquals(0, subject.getCost(1, 0));
		assertEquals(1, subject.getCost(1, 1));
		assertEquals(2, subject.getCost(1, 2));
		assertEquals(-2, subject.getCost(1, 3));
		assertEquals(6, subject.getCost(1, 4));

		assertEquals(1, subject.getCost(2, 0));
		assertEquals(-2, subject.getCost(2, 1));
		assertEquals(1, subject.getCost(2, 2));
		assertEquals(0, subject.getCost(2, 3));
		assertEquals(-2, subject.getCost(2, 4));

		assertEquals(1, subject.getCost(3, 0));
		assertEquals(2, subject.getCost(3, 1));
		assertEquals(-2, subject.getCost(3, 2));
		assertEquals(-2, subject.getCost(3, 3));
		assertEquals(5, subject.getCost(3, 4));

		assertEquals(0, subject.getCost(4, 0));
		assertEquals(1, subject.getCost(4, 1));
		assertEquals(2, subject.getCost(4, 2));
		assertEquals(3, subject.getCost(4, 3));
		assertEquals(4, subject.getCost(4, 4));
	}
}
