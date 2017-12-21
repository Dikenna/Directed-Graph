package A6_Dijkstra;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

public class DiGraph implements DiGraph_Interface {

	Map<String, Node> nodes;
	Set<Edge> edges;
	Set<Long> node_ids;
	Set<Long> edge_ids;
	long no_nodes; long no_edges;
	LinkedHashMap<String, ShortestPathInfo> T = new LinkedHashMap<>();

	public DiGraph () { 
		no_nodes = 0; no_edges = 0;
		node_ids = new HashSet<>();
		edge_ids = new HashSet<>();
		nodes = new HashMap<>();
		edges = new HashSet<>();
	}

	public boolean addNode(Node node){
		return addNode(node.getId(), node.getLabel());		
	}

	@Override
	public boolean addNode(long idNum, String label) {
		if(label == null || nodes.containsKey(label) || idNum < 0 || node_ids.contains(idNum)) 
			return false;

		Node newNode = new Node(idNum, label); 

		nodes.put(label, newNode);
		T.put(label, new ShortestPathInfo(label, 0)); 

		node_ids.add(idNum);
		no_nodes++;

		return true;
	}

	@Override
	public boolean addEdge(long idNum, String sLabel, String dLabel, long weight, String eLabel) {
		if(idNum<0 || sLabel == null || dLabel == null || edge_ids.contains(idNum) ||
				!(nodes.containsKey(sLabel) && nodes.containsKey(dLabel)))
			return false;

		//Check if you're adding a duplicate edge and return true
		Edge new_edge = new Edge(idNum, sLabel, dLabel, weight, eLabel);
		Node s_node = nodes.get(sLabel); Node d_node = nodes.get(dLabel);

		if(s_node!=null && s_node.out_edges.get(d_node)!=null)
			if(s_node.out_edges.get(d_node).endNode == d_node)
				return false;

		edges.add(new_edge);
		if(s_node!=null) s_node.out_edges.put(d_node, new_edge);
		if(d_node!=null) d_node.in_edges.put(s_node, new_edge);
		new_edge.startNode = s_node;
		new_edge.endNode = d_node;
		edge_ids.add(idNum);

		no_edges++;
		return true;
	}

	@Override
	public boolean delNode(String label) {
		if(label==null || !nodes.containsKey(label))
			return false;
		Node to_remove = nodes.get(label);

		for (Map.Entry<Node,Edge> entry : to_remove.in_edges.entrySet()) {
			Edge connecting_edge = entry.getValue().startNode.out_edges.get(to_remove);
			entry.getValue().startNode.out_edges.remove(to_remove);
			edges.remove(connecting_edge);
			edge_ids.remove(connecting_edge.idNum);
			no_edges--;
		}
		for (Map.Entry<Node,Edge> entry : to_remove.out_edges.entrySet()) {
			Edge connecting_edge = entry.getValue().endNode.in_edges.get(to_remove);
			entry.getValue().endNode.in_edges.remove(to_remove);
			edges.remove(connecting_edge);
			edge_ids.remove(connecting_edge.idNum);
			no_edges--;
		}

		nodes.remove(label);
		T.remove(label);
		node_ids.remove(to_remove.getId());
		to_remove.in_edges.clear();
		to_remove.out_edges.clear();

		no_nodes--;
		return true;
	}

	@Override
	public boolean delEdge(String sLabel, String dLabel) {
		if(sLabel==null || dLabel == null || !nodes.containsKey(sLabel) || !nodes.containsKey(dLabel) || edges.isEmpty())
			return false;
		Node s_node = nodes.get(sLabel);
		Node d_node = nodes.get(dLabel);

		Edge to_remove = s_node.out_edges.get(d_node);
		if(to_remove==null) return false;

		s_node.out_edges.remove(d_node);
		d_node.in_edges.remove(s_node);

		edge_ids.remove(to_remove.idNum);
		edges.remove(to_remove);

		no_edges--;
		return true;
	}

	@Override
	public long numNodes() {
		return no_nodes;
	}

	@Override
	public long numEdges() {
		return no_edges;
	}

	@Override
	public String[] topoSort() {
		List<String> L = new ArrayList<String>();

		for (Map.Entry<String, Node> entry : nodes.entrySet()){
			entry.getValue().inDegree = entry.getValue().in_edges.size();
			if(entry.getValue().in_edges.size()==0)
				L.add(entry.getValue().getLabel());
		}

		if(L.size()==0) return null; String[] r = null;
		for(int i = 0; i < L.size(); i++){
			Node u = nodes.get(L.get(i));
			for(Map.Entry<Node, Edge> entry : u.out_edges.entrySet()){
				entry.getKey().inDegree--; 
				if(entry.getKey().getInDegree()==0)
					L.add(entry.getKey().getLabel());
			}
		}

		r = new String[L.size()];
		L.toArray(r);
		if(L.size() < nodes.size()) return null; //If List has less nodes than it should, it contains a cycle, return null.
		return r;
	} 

	@Override
	public ShortestPathInfo[] shortestPath(String label) {
		Node start = nodes.get(label);
		start.dist = 0;

		MinBinHeap H = new MinBinHeap();
		ShortestPathInfo temp = new ShortestPathInfo(label, (long) 0);
		H.insert(new EntryPair(temp));

		while(H.size() > 0){
			Node v = nodes.get(temp.getDest());
			H.delMin();
			T.replace(temp.getDest(), new ShortestPathInfo(temp.getDest(), v.getDist()));
			v.visited = true;

			for(Map.Entry<Node, Edge> entry : v.out_edges.entrySet())
				if(!entry.getKey().visited){
					long new_dist =  v.dist + entry.getValue().weight;
					if (new_dist < entry.getKey().dist){
						entry.getKey().dist = new_dist;
						ShortestPathInfo x = new ShortestPathInfo(entry.getKey().getLabel(), new_dist);
						H.insert(new EntryPair(x));
					}
				}
			if(H.size()>0)temp = H.getMin_();
		}

		for(Map.Entry<String, ShortestPathInfo> entry : T.entrySet())
			if (!entry.getKey().equals(label) && entry.getValue().getTotalWeight()==0)
				T.replace(entry.getKey(), new ShortestPathInfo(entry.getKey(), -1));

		ShortestPathInfo[] r = new ShortestPathInfo[T.size()];
		return T.values().toArray(r);
	}

}