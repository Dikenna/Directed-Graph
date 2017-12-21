package A6_Dijkstra;

import java.util.Map;
import java.util.HashMap;

public class Node {
	
	private long idNum;
	private String label;
	int inDegree;
	boolean visited;
	long dist;
	
	Map<Node,Edge> in_edges;
	Map<Node,Edge> out_edges;
	
	Node(long idNum, String label){
		this.idNum = idNum;
		this.label = label;
		in_edges = new HashMap<>();
		out_edges = new HashMap<>();
		inDegree = 0;
		visited = false;
		dist = 999999999;
	}
	
	public long getDist(){
		return dist;
	}
	
	public int getInDegree(){
		return inDegree;
	}
	
	public long getId(){
		return idNum;
	}
	
	public String getLabel(){
		return label;
	}
	
}
