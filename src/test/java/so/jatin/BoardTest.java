package so.jatin;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BoardTest {

	private Board subject;

	@Test
	public void manyExits() {
		subject = new Board(5, 5);
		subject.addExit(1,  0);
		subject.addExit(2,  3);
		subject.addExit(4,  0);
		/*
		 * Costs should look like this (top left is (0,0)):
		 * 1 0 1 1 0
		 * 2 1 2 2 1
		 * 3 2 1 2 2
		 * 2 1 0 1 2
		 * 3 2 1 2 3
		 */
		assertEquals(0, subject.getCost(1, 0));
		assertEquals(0, subject.getCost(2, 3));
		assertEquals(0, subject.getCost(4, 0));
		assertEquals(1, subject.getCost(0, 0));
		assertEquals(1, subject.getCost(2, 0));
		assertEquals(2, subject.getCost(0, 1));
		assertEquals(2, subject.getCost(3, 1));
		assertEquals(3, subject.getCost(0, 2));
		assertEquals(1, subject.getCost(3, 3));
		assertEquals(2, subject.getCost(4, 2));

	}

}
