package logic;

import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;

public class ShortestPathFinder extends PathFinder{
	
	public ShortestPathFinder(FXGraph fxGraph, String baseCurrency, String floatCurrency){
		super(fxGraph, baseCurrency, floatCurrency);
	}
	
	@Override
	protected double findOptimalPathCost(List<GraphPath<String, DefaultWeightedEdge>> pathList){
		optimalGraphPath = pathList.get(0);
		List<DefaultWeightedEdge> listOfEdges = optimalGraphPath.getEdgeList();
		return findPathCost(listOfEdges);
	}
}
