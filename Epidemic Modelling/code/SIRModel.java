package code;
import java.io.PrintWriter;
import java.util.Random;

/**
 * SIR model.
 *
 * @author Jeffrey Chan, 2021.
 */
public class SIRModel {

	/**
	 * Default constructor, modify as needed.
	 */
	public SIRModel() {

	} // end of SIRModel()

	/**
	 * Run the SIR epidemic model to completion, i.e., until no more changes to the
	 * states of the vertices for a whole iteration.
	 *
	 * @param graph             Input contracts graph.
	 * @param seedVertices      Set of seed, infected vertices.
	 * @param infectionProb     Probability of infection.
	 * @param recoverProb       Probability that a vertex can become recovered.
	 * @param sirModelOutWriter PrintWriter to output the necessary information per
	 *                          iteration (see specs for details).
	 */
	public void runSimulation(ContactsGraph graph, String[] seedVertices, float infectionProb, float recoverProb,
			PrintWriter sirModelOutWriter) {
       for(String vertex: graph.getMap().keySet()) {
    		if(graph.getMap().get(vertex).equals("I") || graph.getMap().get(vertex).equals("R")){
    			graph.getMap().put(vertex, "S");
    		}
    	} // Set all individual to Susceptible at first
    	
    	for(int k=0; k<seedVertices.length; k++) {
    		for(String vertex: graph.getMap().keySet()) {
    			if(vertex.equals(seedVertices[k])) {
    				graph.getMap().put(vertex, "I");
    			}
    		}
    	}
    	
    	StringBuilder sb = new StringBuilder();
        boolean IorRExisting = true; // check if the individual with status Infected or Recovered still existing
        int countIterations = 1;
        LinkedList infected = new LinkedList(null);
        while(IorRExisting && countIterations < 11) {
        	LinkedList infectedInThisRound = new LinkedList(null);
    	    boolean noIRExisting = true; // check if no individual with status Infected or Recovered still existing
    	    for(String vertex: graph.getMap().keySet()) {
    		    if(graph.getMap().get(vertex).equals("I")) {
    			    noIRExisting = false;
    		    }
    	    }
    	    if(noIRExisting == true) {
    		    IorRExisting = false;
    		    break;
    	    }
    	    
    	    sb.append(countIterations + ": [");
    	    Random random = new Random();
    	    for(int i=0; i<infected.getLength(); i++) {
    	    	String[] neighbours = graph.kHopNeighbours(1, infected.getNode(i).getVertex());
    	    	for(int j=0; j<neighbours.length; j++) {
    	    		if(graph.getMap().get(infected.getNode(i).getVertex()).equals("S")) {
    	    			double prob1 = random.nextDouble();
    	    			if(infectionProb > prob1) {
    	    	    		graph.toggleVertexState(graph.kHopNeighbours(1, infected.getNode(i).getVertex())[j]);
    	        			sb.append(graph.kHopNeighbours(1, infected.getNode(i).getVertex())[i] + " ");
    	        			infectedInThisRound.addNode(new Node(infected.getNode(i).getVertex()));
    	    			}
    	    		}
    	    	}
    	    }
    	    infected.clear();
    	    for(int i=0; i<seedVertices.length; i++) {
    	    	String[] neighbours = graph.kHopNeighbours(1, seedVertices[i]);
                for(int j=0; j<neighbours.length; j++) {
              	    if(neighbours[j] != null) {
              	    	if(graph.getMap().get(neighbours[j]).equals("S")) {
                  		    double prob = random.nextDouble();
                  		    if(infectionProb > prob) {
                  			    graph.toggleVertexState(graph.kHopNeighbours(1, seedVertices[i])[j]);
                    			sb.append(graph.kHopNeighbours(1, seedVertices[i])[j] + " ");
                    			infectedInThisRound.addNode(new Node(neighbours[j]));
                    			infected.addNode(new Node(neighbours[j]));
                  		    }
                  	    }
              	    }
                }
    	    }
    	    sb.append("] : [");
    	    for(String vertex: graph.getMap().keySet()) {
    	    	if(graph.getMap().get(vertex).equals("I")) {
    	    		if(!infectedInThisRound.contains(vertex)) {
    	    			Random random1 = new Random();
                		double prob = random1.nextDouble();
                		if(recoverProb > prob) {
                			graph.getMap().put(vertex, "R");
                			sb.append(vertex+ " ");
                		}
    	    		}
    	    	}
    	    }
    	    sb.append("]\n");
    	    countIterations++;
        }
        sirModelOutWriter.println(sb);
    } // end of runSimulation()
    
    protected class Node 
	{
		private String vertex; // The vertex this node represents for
		private Node nextNode; // The node which is directly connected with this node
		
		/**
		 * <pre> The constructor of Node
		 * @param vertex
		 *             the current vertex this node represents for
		 * </pre>
		 */
		public Node(String vertex) {
			this.vertex = vertex;
		}
		
		/**
		 * <pre> Get the vertex this node represents for
		 * @return the vertex as string
		 * </pre>
		 */
		public String getVertex() {
			return vertex;
		}
		
		/**
		 * <pre> Set the node which is directly connected with this node
		 * @param node
		 *           The node which is directly connected with this node
		 * </pre>
		 */
		public void setNext(Node node) {
			nextNode = node;
		}
		
		/**
		 * <pre> Get the node which is directly connected with this node
		 * @return the node which is directly connected with this node
		 * </pre>
		 */
		public Node getNext() {
			return nextNode;
		}
		
	}
	
	/**
	 * 
	 * Linked List we need to collect the nodes
	 * 
	 * @author Zico Zhong, 2021 & Alan Du
	 *
	 */
	protected class LinkedList 
	{
		private Node head; // the head node in the linked list
		
		/**
		 * 
		 * @param head
		 *           The head node of this linked list
		 */
		public LinkedList(Node head) {
			this.head = head;
		}
		
		/**
		 * Set the head node of this linked list
		 * @param head
		 *           The head node of this linked list
		 */
		public void setHead(Node head) {
			this.head = head;
		}
		
		/**
		 * Add the node to this linked list
		 * @param node
		 *           The node you're going to add into this linked list
		 */
		public void addNode(Node node) {
			if(head == null) {
				head = node;
			}else {
				Node obj = head;
				while(obj.getNext() != null) {
					obj = obj.getNext();
				}
				obj.setNext(node);
			}
		}
		
		/**
		 * Remove a specific node with the vertex it represents for
		 * @param vertex
		 *             The name of the vertex (as string)
		 */
		public void removeNode(String vertex) {
			Node obj = head;
			int count = 0;
			while(obj != null) {
				if(obj.getVertex().equals(vertex)) {
					if(obj.getNext() == null) {
						if(count == 0) {
							head = null;
							break;
						}else {
							getNode(count-1).setNext(null);
							break;
						}
					}else {
						if(count == 0) {
							head = head.getNext();
							break;
						}else {
							getNode(count-1).setNext(obj.getNext());
							break;
						}
					}
				}else {
					obj = obj.getNext();
					count++;
				}
			}
		}
		
		/**
		 * Get the node with its index in this linked list
		 * @param index
		 *            the index of a node you want to get
		 * @return the node with the specified index
		 */
		public Node getNode(int index) {
			Node obj = head;
			for(int count=0; count < index; count++) {
				obj = obj.getNext();
			}
			return obj;
		}
		
		/**
		 * Get the length of this linked list (the empty element in this list will not be considered)
		 * @return the size of this list 
		 */
		public int getLength() {
			int size = 1;
			if(head == null) {
				size = 0;
			}else {
				Node object = head;
				
				while(object.getNext() != null) {
					object = object.getNext();
					size = size + 1;
				}
			}
			return size;
		}
		
		public boolean contains(String vertex) {
			boolean exist = false;
			Node checker = head;
			
			if(checker ==null) {
				exist = false;
			}else {
				if(head.getVertex().equals(vertex)) {
					exist = true;
				}
				while(checker.getNext() != null) {
					checker = checker.getNext();
					if(checker.getVertex().equals(vertex)) {
						exist = true;
					}
				}
			}
			
			return exist;
		}
		
		public String[] toArray() {
			String[] arr = new String[1];
			arr[0] = "";
			
			if(head != null) {
				arr = new String[getLength()];
				for(int i=0; i<getLength(); i++) {
					arr[i] = getNode(i).getVertex();
				}
			}
			
			return arr;
			
		}
		
		public void clear() {
			head = null;
		}
	}
} // end of class SIRModel
