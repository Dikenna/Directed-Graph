package A6_Dijkstra;

public class Edge {
	Node startNode; String sLabel;
	Node endNode; String dLabel;
	long weight;
	long idNum;
	String eLabel;

	Edge(Node startNode, Node endNode){
		this.startNode = startNode;
		this.endNode = endNode;
	}

	Edge(Node startNode, Node endNode, long weight){
		this.startNode = startNode;
		this.endNode = endNode;
		this.weight = weight;
	}

	Edge(long idNum, String sLabel, String dLabel, long weight, String eLabel){
		this.idNum = idNum;
		this.weight = weight;
		this.sLabel = sLabel;
		this.dLabel = dLabel;
		this.eLabel = eLabel;
	}

}
