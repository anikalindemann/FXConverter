package logic;

import java.util.List;
import java.util.ListIterator;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.KShortestPaths;
import org.jgrapht.graph.DefaultWeightedEdge;

public abstract class PathFinder {
		
	protected FXGraph fxGraph;
	protected String baseCurrency;
	protected String floatCurrency;
	protected GraphPath<String, DefaultWeightedEdge> optimalGraphPath;
	protected double optimalGraphCost;
	
	public PathFinder(FXGraph fxGraph, String baseCurrency, String floatCurrency){
		this.fxGraph = fxGraph;
		this.baseCurrency = baseCurrency;
		this.floatCurrency = floatCurrency;
		}
	
	protected List<GraphPath<String, DefaultWeightedEdge>> getPathList(){
		try{
			KShortestPaths<String, DefaultWeightedEdge> pathFinder = fxGraph.getPathFinder(baseCurrency);
			List<GraphPath<String, DefaultWeightedEdge>> pathList = pathFinder.getPaths(floatCurrency);
			return pathList;
			}
		catch(NullPointerException notEnoughRatesInfo){
				throw new NullPointerException("Check that there are appropriate cross-currency rates.");
			}
	}
	
	protected abstract double findOptimalPathCost(List<GraphPath<String, DefaultWeightedEdge>> listOfAllPaths);
	
	protected double findPathCost(List<DefaultWeightedEdge> listOfEdges){
		ListIterator<DefaultWeightedEdge> edgeIterator = listOfEdges.listIterator();
		double pathCost = 1;
		while (edgeIterator.hasNext()){
				double fxRate = fxGraph.getRate(edgeIterator.next());
				pathCost = pathCost*fxRate;
		}
		return pathCost;
	}
	
	public double getOptimalGraphCost(){
		List<GraphPath<String, DefaultWeightedEdge>> listOfAllPaths = getPathList();
		double optimalCost = findOptimalPathCost(listOfAllPaths);
		return optimalCost;
		}
}
