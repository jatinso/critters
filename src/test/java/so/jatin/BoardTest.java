package so.jatin;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {

	private Board subject;

	@Before
	public void setUp() {
		subject = new Board(7, 7);		
	}

	@Test
	public void test() {
		subject.addExit(5, 3);
		assertEquals(0, subject.getCost(5, 3));
		assertEquals(1, subject.getCost(6, 3));
		assertEquals(2, subject.getCost(6, 4));
		assertEquals(5, subject.getCost(0, 3));
		assertEquals(5, subject.getCost(2, 1));
	}

}
