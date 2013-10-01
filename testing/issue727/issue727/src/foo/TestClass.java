package foo;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestClass {
	@Test
	public void testFail()
	{
	  assertEquals(true, false);
	}
	
	@Test
	public void testPass()
	{
	  assertEquals(true, true);
	}
}
