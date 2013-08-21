package logic;

public class RateConverter{
	
	private FXGraph fxGraph;

	public RateConverter(FXGraph fxGraph){
		this.fxGraph = fxGraph;
	}
	
	public double convert(String baseCurrency, String floatCurrency, double amountToConvert){
				PathFinder optimalRateConversion = new ShortestPathFinder(fxGraph, baseCurrency, floatCurrency);
				double optimalRate = optimalRateConversion.getOptimalGraphCost();
				return optimalRate*amountToConvert;	
	}
}

