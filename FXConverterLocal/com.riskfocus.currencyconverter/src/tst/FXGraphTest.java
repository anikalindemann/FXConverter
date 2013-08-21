package tst;
import logic.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class FXGraphTest{

	//Build test graph
	FXGraph fxGraph;
	
	@Before 
	public void initialize(){
	//Master List of Currencies
	String[] currencies = new String[7];
	currencies[0] = "USD";
	currencies[1] = "EUR";
	currencies[2] = "JPY";
	currencies[3] = "GBP";
	currencies[4] = "AUD";
	currencies[5] = "CHF";
	currencies[6] = "CAD";	
	fxGraph= new FXGraph(currencies);
	}
	
	@Test
	public void buildGraphTest(){	
		assertNotNull(fxGraph);
	}
	
	@Test
	public void addRateTest(){
		fxGraph.addRate("USD", "AUD", 17.29);
		assertEquals(17.29, fxGraph.getRate("USD", "AUD"), 0.01);
	}
	
	@Test
	public void addRateInverseTest(){
		fxGraph.addRate("USD", "AUD", 17.29);
		double inverseRate = 1/17.29;
		assertEquals(inverseRate, fxGraph.getRate("AUD", "USD"), 0.01);
	}
	
	@Test
	public void addRateTwiceTest(){
		fxGraph.addRate("USD", "AUD", 28);
		fxGraph.addRate("USD", "AUD", 10.0);
		assertEquals(10.0, fxGraph.getRate("USD", "AUD"), 0.01);
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addNegtiveRate(){
		fxGraph.addRate("USD", "AUD", -10.0);
		assertNull(fxGraph.getRate("USD", "AUD"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addZeroRate(){
		fxGraph.addRate("USD", "AUD", 0);
		assertNull(fxGraph.getRate("USD","AUD"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void addRateWithNECurrency(){
		fxGraph.addRate("SEK", "USD", 10.0);
		assertNull(fxGraph.getRate("SEK", "USD"));
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void noRateToGet(){
		fxGraph.getRate("USD", "AUD");
	}
}
