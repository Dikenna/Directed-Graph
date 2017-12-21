package A6_Dijkstra;

public class EntryPair implements EntryPair_Interface {
    public String value;
    public int priority;
    public ShortestPathInfo spi;

    public EntryPair(ShortestPathInfo aS){
    	spi = aS;
    	priority = (int) aS.getTotalWeight();
    	value = aS.getDest();
    }
    
    public EntryPair(String aValue, int aPriority) {
        value = aValue;
        priority = aPriority;
    }

    public ShortestPathInfo getInfo(){
    	return spi;
    }
    
    public String getValue() {
        return value;
    }

    public int getPriority() {
        return priority;
    }
}