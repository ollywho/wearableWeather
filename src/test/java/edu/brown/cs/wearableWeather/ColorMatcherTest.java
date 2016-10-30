package edu.brown.cs.wearableWeather;

import static org.junit.Assert.*;

import org.junit.Test;

public class ColorMatcherTest {
	
	@Test
	public void twoNeutrals() {
		Top top = new Top(1, "T-shirt", "base", "short", false, "white", 1, 1);
		Bottom bottom = new Bottom(2, "Chinos", "long", "tan", 3, 1);
		ColorMatcher color = new ColorMatcher();
		assertTrue(color.matches(top, bottom));
	}
	
	@Test
	public void topNeutral() {
		Top top = new Top(1, "T-shirt", "base", "short", false, "white", 1, 1);
		Bottom bottom = new Bottom(2, "Chinos", "long", "pink", 3, 1);
		ColorMatcher color = new ColorMatcher();
		assertTrue(color.matches(top, bottom));
	}
	
	@Test
	public void bottomNeutral() {
		Top top = new Top(1, "T-shirt", "base", "short", false, "burgundy", 1, 1);
		Bottom bottom = new Bottom(2, "Chinos", "long", "navy", 3, 1);
		ColorMatcher color = new ColorMatcher();
		assertTrue(color.matches(top, bottom));
	}
	
	@Test
	public void noNeutral() {
		Top top = new Top(1, "T-shirt", "base", "short", false, "yellow", 1, 1);
		Bottom bottom = new Bottom(2, "Chinos", "long", "red", 3, 1);
		ColorMatcher color = new ColorMatcher();
		assertTrue(!color.matches(top, bottom));
	}

}
