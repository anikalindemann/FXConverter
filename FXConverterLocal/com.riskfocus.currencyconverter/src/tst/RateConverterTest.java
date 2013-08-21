package tst;
import logic.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class RateConverterTest {

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
		
		//Add Some Rates
		fxGraph.addRate("USD", "EUR", 0.75);
		fxGraph.addRate("USD", "CAD", 1.03);
		fxGraph.addRate("USD", "GBP", 0.64);
		fxGraph.addRate("CAD", "JPY", 93.73);
		fxGraph.addRate("GBP", "CAD", 1.59);
		fxGraph.addRate("GBP", "JPY", 149.44);
		fxGraph.addRate("GBP", "AUD", 1.68);
		}
		
		
		
		@Test
		public void convertOnePathTest(){
			RateConverter rateConverter = new RateConverter(fxGraph);
			double converted = rateConverter.convert("USD", "EUR", 10.00);
			assertEquals(7.50, converted, 0.01);
		}
		
		@Test
		public void convertMultiPathTest(){
		/*Note that there are two paths from USD to JPY, but that [(USD:CAD) ; (CAD:JPY)] 
		  was followed rather than [(USD:GBP) ; (GBP:JPY)]
		  because the [(USD:CAD)] edge was added before [(USD:GBP)]
		 */
		RateConverter rateConverter = new RateConverter(fxGraph);	
		double converted = rateConverter.convert("USD", "JPY", 10.00);			
		assertEquals(965.419, converted, 0.01);	
		}
		
		@Test(expected=NullPointerException.class)
		public void noPathToCurrencyTest(){
		RateConverter rateConverter = new RateConverter(fxGraph);
		rateConverter.convert("USD", "CHF", 1);
		}
}
