package com.github.ants280.slideGame.logic;

import java.util.Collection;
import java.util.HashSet;
import org.junit.Assert;
import org.junit.Test;

public class TileTest
{
	private static final Tile[] VALUES = Tile.values();
	
	@Test
	public void testMultipleTiles()
	{
		Assert.assertTrue(VALUES.length > 1);
	}

	@Test
	public void testFirstValueIs2()
	{
		Assert.assertSame(2, VALUES[0].getValue());
	}
	
	/**
	 * Tiles values should increase by a multiple of 2 from the previous tile.
	 */
	@Test
	public void testIncreasingValues()
	{
		int prevousValue = 1;
		for (Tile tile : VALUES)
		{
			Assert.assertEquals(
					"Bad Tile value for " + tile,
					prevousValue * 2,
					tile.getValue());
			prevousValue *= 2;
		}
	}
	
	@Test
	public void testGetDisplayValue()
	{
		for (Tile tile : VALUES)
		{
			Assert.assertEquals(
					"Bad display name for " + tile,
					tile.name().substring(2),
					tile.getDisplayValue());
		}
	}

	@Test
	public void testUniqueColors()
	{
		Collection<Long> colors = new HashSet<>();

		for (Tile tile : VALUES)
		{
			if (!colors.add(tile.getColor()))
			{
				Assert.fail("Duplicate color found on " + tile);
			}
		}
	}
}