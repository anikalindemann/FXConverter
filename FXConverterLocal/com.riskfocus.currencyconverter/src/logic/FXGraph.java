package logic;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.*;

public class FXGraph{
	
	private DefaultDirectedGraph<String, DefaultWeightedEdge> graph;
	private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock writeLock = readWriteLock.writeLock();
	private final Lock readLock = readWriteLock.readLock();
	
	public FXGraph(String[] currencies){
		graph = new DefaultDirectedWeightedGraph<String, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		for (String currency : currencies) graph.addVertex(currency);
		//numberOfVertices = currencies.length;
	}

	private void removeOldRate(String baseCurrency, String floatCurrency){
		if (graph.getEdge(baseCurrency, floatCurrency) != null){
			graph.removeEdge(baseCurrency, floatCurrency);
			graph.removeEdge(baseCurrency, floatCurrency);
			return;
		}
	}
	
	//TODO: Adjust JGrapht library so that BigDecimal can be used for conversion rate
	public void addRate(String baseCurrency, String floatCurrency, double conversionRate){
		if(conversionRate <= 0 ) throw new IllegalArgumentException("The rate needs to be positive.");
		try{	
			writeLock.lock();
			removeOldRate(baseCurrency, floatCurrency); 
			addRatePlaceHolder(baseCurrency, floatCurrency);
			setRate(baseCurrency, floatCurrency, conversionRate);
		}
		
		catch(IllegalArgumentException currencyNotInGraph){
			throw new IllegalArgumentException("This currency is not supported.");
		}
		
		finally{
			writeLock.unlock();
		}
	}
	
	//Overloaded Method: This one takes two vertices -- Only for testing purposes
	public double getRate(String baseCurrency, String floatCurrency){
		try{
			readLock.lock();
			DefaultWeightedEdge edge = graph.getEdge(baseCurrency, floatCurrency);
			if (edge == null) throw new IllegalArgumentException("There is no rate between these currencies."); 
			double rate = graph.getEdgeWeight(edge);
			return rate;
		}
		finally{
			readLock.unlock();
		}
	}
	
	//Overload Method: This one takes just an edge
	public double getRate(DefaultWeightedEdge edge){
		try{
			readLock.lock();
			double rate = graph.getEdgeWeight(edge);
			return rate;
		}
		finally{
			readLock.unlock();
		}
	}

	private void addRatePlaceHolder(String baseCurrency,String floatCurrency){
		graph.addEdge(baseCurrency, floatCurrency);
		graph.addEdge(floatCurrency, baseCurrency);
	}
	
	//TODO: Add ability to input two strings and double into getEdge() method in JGRapht library
	private void setRate(String baseCurrency, String floatCurrency, double conversionRate){
		DefaultWeightedEdge rate = graph.getEdge(baseCurrency, floatCurrency);
		DefaultWeightedEdge inverseRate = graph.getEdge(floatCurrency, baseCurrency);
		graph.setEdgeWeight(rate, conversionRate);
		graph.setEdgeWeight(inverseRate, 1/conversionRate);
	}
	
	
	//TODO: Find way to create list of all possible paths between two fixed nodes. 
	protected KShortestPaths<String, DefaultWeightedEdge> getPathFinder(String baseCurrency){
		KShortestPaths<String, DefaultWeightedEdge> pathFinder = new KShortestPaths<String, DefaultWeightedEdge>
		(graph, baseCurrency, 1);
		return pathFinder;
	}
	
}	

