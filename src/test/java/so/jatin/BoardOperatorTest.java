package so.jatin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BoardOperatorTest {

	private BoardOperator subject;

	@Test
	public void withObstaclesAddedAfterExits() {
		subject = new BoardOperator(5, 5);

		subject.addExit(1, 0);
		subject.addExit(2, 3);
		subject.addExit(4, 0);

		subject.addObstacle(0, 3);
		subject.addObstacle(2, 1);
		subject.addObstacle(3, 2);
		subject.addObstacle(1, 3);
		subject.addObstacle(3, 3);
		subject.addObstacle(2, 4);

		assertBoardWithCertainObstaclesAndExits();
	}
	
	@Test
	public void withObstaclesAddedBeforeExits() {
		subject = new BoardOperator(5, 5);

		subject.addObstacle(0, 3);
		subject.addObstacle(2, 1);
		subject.addObstacle(3, 2);
		subject.addObstacle(1, 3);
		subject.addObstacle(3, 3);
		subject.addObstacle(2, 4);

		subject.addExit(1, 0);
		subject.addExit(2, 3);
		subject.addExit(4, 0);
	}
	
	@Test
	public void withObstaclesAndExitsAddedIntermingled() {
		subject = new BoardOperator(5, 5);

		subject.addObstacle(2, 1);
		subject.addExit(1, 0);
		subject.addObstacle(3, 2);
		subject.addObstacle(0, 3);
		subject.addObstacle(1, 3);
		subject.addExit(2, 3);
		subject.addObstacle(3, 3);
		subject.addObstacle(2, 4);
		subject.addExit(4, 0);

	}

	@Test
	public void removeObstacle() {
		// Arrange
		subject = new BoardOperator(5, 5);

		subject.addExit(1,  0);
		subject.addExit(2,  3);
		subject.addExit(4,  0);

		subject.addObstacle(0, 3);
		subject.addObstacle(2, 1);
		subject.addObstacle(3, 2);
		subject.addObstacle(1, 3);
		subject.addObstacle(3, 3);
		subject.addObstacle(2, 4);

		// Act
		subject.removeObstacle(2, 4);

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
		assertEquals(1, subject.getCost(0, 0));
		assertEquals(2, subject.getCost(0, 1));
		assertEquals(3, subject.getCost(0, 2));
		assertEquals(-1, subject.getCost(0, 3));
		assertEquals(3, subject.getCost(0, 4));

		assertEquals(0, subject.getCost(1, 0));
		assertEquals(1, subject.getCost(1, 1));
		assertEquals(2, subject.getCost(1, 2));
		assertEquals(-1, subject.getCost(1, 3));
		assertEquals(2, subject.getCost(1, 4));

		assertEquals(1, subject.getCost(2, 0));
		assertEquals(-1, subject.getCost(2, 1));
		assertEquals(1, subject.getCost(2, 2));
		assertEquals(0, subject.getCost(2, 3));
		assertEquals(1, subject.getCost(2, 4));

		assertEquals(1, subject.getCost(3, 0));
		assertEquals(2, subject.getCost(3, 1));
		assertEquals(-1, subject.getCost(3, 2));
		assertEquals(-1, subject.getCost(3, 3));
		assertEquals(2, subject.getCost(3, 4));

		assertEquals(0, subject.getCost(4, 0));
		assertEquals(1, subject.getCost(4, 1));
		assertEquals(2, subject.getCost(4, 2));
		assertEquals(3, subject.getCost(4, 3));
		assertEquals(3, subject.getCost(4, 4));

	}

	@Test
	public void removeExit() {
		// Arrange
		subject = new BoardOperator(5, 5);

		subject.addExit(1, 0);
		subject.addExit(2, 3);
		subject.addExit(4, 0);

		subject.addObstacle(0, 3);
		subject.addObstacle(2, 1);
		subject.addObstacle(3, 2);
		subject.addObstacle(1, 3);
		subject.addObstacle(3, 3);
		subject.addObstacle(2, 4);

		// Act
		subject.removeExit(2, 3);

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
		assertEquals(1, subject.getCost(0, 0));
		assertEquals(2, subject.getCost(0, 1));
		assertEquals(3, subject.getCost(0, 2));
		assertEquals(-1, subject.getCost(0, 3));
		assertEquals(-1, subject.getCost(0, 4));

		assertEquals(0, subject.getCost(1, 0));
		assertEquals(1, subject.getCost(1, 1));
		assertEquals(2, subject.getCost(1, 2));
		assertEquals(-1, subject.getCost(1, 3));
		assertEquals(-1, subject.getCost(1, 4));

		assertEquals(1, subject.getCost(2, 0));
		assertEquals(-1, subject.getCost(2, 1));
		assertEquals(3, subject.getCost(2, 2));
		assertEquals(4, subject.getCost(2, 3));
		assertEquals(-1, subject.getCost(2, 4));

		assertEquals(1, subject.getCost(3, 0));
		assertEquals(2, subject.getCost(3, 1));
		assertEquals(-1, subject.getCost(3, 2));
		assertEquals(-1, subject.getCost(3, 3));
		assertEquals(5, subject.getCost(3, 4));

		assertEquals(0, subject.getCost(4, 0));
		assertEquals(1, subject.getCost(4, 1));
		assertEquals(2, subject.getCost(4, 2));
		assertEquals(3, subject.getCost(4, 3));
		assertEquals(4, subject.getCost(4, 4));

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
		assertEquals(1, subject.getCost(0, 0));
		assertEquals(2, subject.getCost(0, 1));
		assertEquals(3, subject.getCost(0, 2));
		assertEquals(-1, subject.getCost(0, 3));
		assertEquals(-1, subject.getCost(0, 4));

		assertEquals(0, subject.getCost(1, 0));
		assertEquals(1, subject.getCost(1, 1));
		assertEquals(2, subject.getCost(1, 2));
		assertEquals(-1, subject.getCost(1, 3));
		assertEquals(-1, subject.getCost(1, 4));

		assertEquals(1, subject.getCost(2, 0));
		assertEquals(-1, subject.getCost(2, 1));
		assertEquals(1, subject.getCost(2, 2));
		assertEquals(0, subject.getCost(2, 3));
		assertEquals(-1, subject.getCost(2, 4));

		assertEquals(1, subject.getCost(3, 0));
		assertEquals(2, subject.getCost(3, 1));
		assertEquals(-1, subject.getCost(3, 2));
		assertEquals(-1, subject.getCost(3, 3));
		assertEquals(5, subject.getCost(3, 4));

		assertEquals(0, subject.getCost(4, 0));
		assertEquals(1, subject.getCost(4, 1));
		assertEquals(2, subject.getCost(4, 2));
		assertEquals(3, subject.getCost(4, 3));
		assertEquals(4, subject.getCost(4, 4));
	}
}
