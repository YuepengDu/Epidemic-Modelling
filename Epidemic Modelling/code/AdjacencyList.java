package code;

import java.io.PrintWriter;
import java.util.HashMap;
//import java.util.*;
import java.util.Map;

/**
 * Adjacency list implementation for the AssociationGraph interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2019.
 */
public class AdjacencyList extends AbstractGraph
{
	String[] vertices; // Array of vertices, each vertex added into the list will also be stored here
	String[] edges; // Array of edges, each edge added into the list will also be stored here
	private String[] anotherEdges; /* Array of edges, storing the edges added into the matrix but with the inverse direction as the input, 
                                      which ensures that all edges should be undirected in this graph by copying them in another direction */
	LinkedList[] adjacencyList;// The array of LinkedList represents for the adjacency list
	Map<String, String> statusOfIndividual = new HashMap<String, String>(); // The map for storing status of individual corresponding to the vertex respectively
	boolean notAddedToMap = true; // check whether the status of individual corresponding to the vertex has been added into the map above or not
    /**
	 * Constructs empty graph.
	 */
    public AdjacencyList() {
    } // end of AdjacencyList()


    public void addVertex(String vertLabel) {
        if(adjacencyList == null) { // Initialise the vertices array and the adjacencyList with the vertex label and its new corresponding linked list if the list is empty
        	vertices = new String[1];
        	vertices[0] = vertLabel;
        	LinkedList list = new LinkedList(null);
        	adjacencyList = new LinkedList[1];
        	adjacencyList[0] = list;
        }else {
        	boolean notExisted = true; // check if the vertex has been added into the list
        	for(int i=0; i<vertices.length; i++) {
        		if(vertices[i].equals(vertLabel)) {
        			notExisted = false;
        		}
        	}
        	if(notExisted) { // add the vertex label into the list if it's not empty
        		String[] temp;
        		temp = vertices;
        		vertices = new String[temp.length + 1];
        		for(int i=0; i<temp.length; i++) {
        			vertices[i] = temp[i];
        		}
        		vertices[temp.length] = vertLabel; // add the vertex label into the vertices array
        		
        		LinkedList list = new LinkedList(null);
        		LinkedList[] temp1 = adjacencyList;
        		adjacencyList = new LinkedList[temp.length+1];
        		for(int j=0; j<temp1.length; j++) {
        			adjacencyList[j] = temp1[j];
        		}
        		adjacencyList[temp1.length] = list; // add the vertex label into the adjacency list
        	}else {
        		System.err.print("The vertex you try to add has been added into the list.");
        	}
        }
    } // end of addVertex()


    public void addEdge(String srcLabel, String tarLabel) {
    	String edge = srcLabel + " " + tarLabel;// create the edge as a string
    	boolean srcLabelExisted = false;// check if the srcLabel has been added
    	boolean tarLabelExisted = false;// check if the tarLabel has been added
    	boolean edgeNotExisted = true;// check if the edge has been added
    	int indexOfSrcLabel = 0;// find the position of srcLabel in the matrix
		int indexOfTarLabel = 0;// find the position of tarLabel in the matrix
		
    	for(int i=0; i<vertices.length; i++) {
    		if(srcLabel.equals(vertices[i])) {
    			srcLabelExisted = true;
    			indexOfSrcLabel = i;
    		}
    		if(tarLabel.equals(vertices[i])) {
    			tarLabelExisted = true;
    			indexOfTarLabel = i;
    		}
    	}
    	
    	if(edges != null) {
	    	for(int i=0; i<edges.length; i++) {
	    		if(edge.equals(edges[i])) {
	    			edgeNotExisted = false;
	    		}
	    	}
    	}
    	
    	if(srcLabelExisted && tarLabelExisted && edgeNotExisted) {
    		if(edges == null) { // Initialise the edges array if it's empty and add the edge into it also add into the list 
    			edges = new String[1];
    			edges[0] = edge;
    		}else {
    			String[] temp;
        		temp = edges;
        		edges = new String[temp.length + 1];
        		for(int i=0; i<temp.length; i++) {
        			edges[i] = temp[i];
        		}
        		edges[temp.length] = edge; // add the new edge into the edges array
    		}
    		
    		Node tarNode = new Node(tarLabel);
    		adjacencyList[indexOfSrcLabel].addNode(tarNode); // add the target label to the linked list of source label, which represents for an edge between them
			
			Node srcNode = new Node(srcLabel);
			adjacencyList[indexOfTarLabel].addNode(srcNode); // add the source label to the linked list of target label as well, with the purpose of avoiding specific direction as input
    	}
    	
    } // end of addEdge()


    public void toggleVertexState(String vertLabel) {
    	if(notAddedToMap) {
        	for(int i=0; i<vertices.length; i++) {
            	statusOfIndividual.put(vertices[i], Status.S.toString());
            	notAddedToMap = false;
            }
        } // Initialise the status of every individual
        String status = statusOfIndividual.get(vertLabel).toString();
        switch(status) {
        case "S":
        	statusOfIndividual.put(vertLabel, Status.I.toString());
        	break;
        case "I":
        	statusOfIndividual.put(vertLabel, Status.R.toString());
        	break;
        case "R":
        	statusOfIndividual.put(vertLabel, Status.S.toString());
        	break;
        } // toggle the statement of the vertex label with specified order
    } // end of toggleVertexState()


    public void deleteEdge(String srcLabel, String tarLabel) {
    	if(srcLabel != null && tarLabel != null) {
    		String edge = srcLabel + " " + tarLabel;// create edge as a string
        	String invertedEdge = tarLabel + " " + srcLabel; // check the edge when it's inputed in inverse order
        	int indexOfSrcLabel = 0;// find the position of srcLabel in the matrix
    		int indexOfTarLabel = 0;// find the position of tarLabel in the matrix
        	boolean edgeNotExisted = true;// check if the edge doesn't exist
        	
        	for(int i=0; i<vertices.length; i++) {
        		if(vertices[i] != null ) {
        			if(srcLabel.equals(vertices[i])) {
            			indexOfSrcLabel = i;
            		}
            		if(tarLabel.equals(vertices[i])) {
            			indexOfTarLabel = i;
            		}
        		}
        	}
        	
        	for(int indexOfEdge=0; indexOfEdge<edges.length; indexOfEdge++) {
        		if(edge.equals(edges[indexOfEdge]) || invertedEdge.equals(edges[indexOfEdge])) {
                    edgeNotExisted = false;
        			String[] temp = edges;
        			edges = new String[temp.length-1];
        			for(int j=0; j<indexOfEdge; j++) {
        				edges[j] = temp[j];
        			}
        			for(int k=indexOfEdge; k<temp.length-1; k++) {
        				edges[k] = temp[k+1];
        			} // remove the edge from the edge array
        		}
        	}
        	
        	adjacencyList[indexOfSrcLabel].removeNode(tarLabel); // remove the edge from the adjacency list
        	adjacencyList[indexOfTarLabel].removeNode(srcLabel); // remove the edge in another direction from the adjacency list 
        	
        	if(edgeNotExisted) {
        		System.err.println("The edge doesn't exist in the array edges");
        	}
    	}
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
    	boolean vertexNotExisted = true;// check if the vertex has been added
    	int indexOfVertex = 0;// find the position of vertex in vertices array
		for(int i=0; i<vertices.length;i++) {
			if(vertLabel.equals(vertices[i])) {
				vertexNotExisted = false;
				indexOfVertex = i;
			}
		}
		
		if(vertexNotExisted) {
    		System.err.println("The vertex doesn't exist in the array vertices");
    	}else {
    		LinkedList[] temp = adjacencyList;
    		adjacencyList = new LinkedList[temp.length-1];
    		for(int a=0; a<indexOfVertex; a++) {
    			adjacencyList[a] = temp[a];
    		}
    		for(int b=indexOfVertex; b<temp.length-1; b++) {
    			adjacencyList[b] = temp[b+1];
    		} // remove the vertex from the adjacency list
    		
    		for(int c=0; c<adjacencyList.length; c++) {
    			adjacencyList[c].removeNode(vertLabel);
    		} // remove the edge which also contains the vertex is going to be deleted from other linked lists of other vertices
    		
    		String[] temp1 = vertices;
			vertices = new String[temp1.length-1];
			for(int d=0; d<indexOfVertex; d++) {
				vertices[d] = temp1[d];
			}
			for(int e=indexOfVertex; e<temp1.length-1; e++) {
				vertices[e] = temp1[e+1];
			}// remove the vertex from the vertices array
			
			for(int l=0; l<edges.length; l++) {
				if(vertLabel.toCharArray()[0] == edges[l].charAt(0) || vertLabel.toCharArray()[0] == edges[l].charAt(2)) {
					String[] temp3 = edges;
	    			edges = new String[temp3.length-1];
	    			for(int m=0; m<l; m++) {
	    				edges[m] = temp3[m];
	    			}
	    			for(int n=l; n<temp3.length-1; n++) {
	    				edges[n] = temp3[n+1];
	    			}
				}
			} //remove the edge which also contains the vertex is going to be deleted from the edges array
			if(vertices.length == 0) {
				String[] empty = new String[0];
				edges = empty;
			}
    	}
		
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
    	String[] kHopNeighbours1 = new String[1]; // Initialise the k hop neighbours list
    	LinkedList hopNeighbours = new LinkedList(null); // the first list of neighbours of the vertex label, which means consisting of the vertices directly connected to the vertex label
    	int hopNum = 1; // the times counter of hopping, and we initialise it with 1 because we do find the neighbours of the vertex label once anyway in spite of what number the k value is
    	
    	for(int f=0; f<vertices.length; f++) {
    		if(vertices[f].equals(vertLabel)) {
    			hopNeighbours = adjacencyList[f];
    		}
    	} // The first time to get the neighbours vertex of the object vertex label
    	
    	while(hopNum < k) {
    		LinkedList nextHopNeighbours = new LinkedList(null); // The list consists of those vertices we need to get their neighbours later
    		for(int g=0; g<hopNeighbours.getLength(); g++) {
        		nextHopNeighbours.addNode(new Node(hopNeighbours.getNode(g).getVertex()));
    		} /* fill the nextHopNeighbours list, then finds the neighbours of them respectively. In other words, 
    		     we're getting the neighbours of previous one(s), the vertex label or the vertices connected to the vertex label indirectly */ 
    		
    		hopNeighbours = new LinkedList(null);
    		for(int m=0; m<nextHopNeighbours.getLength(); m++) {
    			for(int n=0; n<vertices.length; n++) {
        			if(vertices[n].equals(nextHopNeighbours.getNode(m).getVertex())) {
        				int num = adjacencyList[n].getLength();
        				for(int p=0; p<num; p++) {
        					if(!adjacencyList[n].getNode(p).getVertex().equals(vertLabel)) {
        						Node newNode = new Node(adjacencyList[n].getNode(p).getVertex());
                				hopNeighbours.addNode(newNode);
        					}
            			}
        			}
        		}
    		} // get the neighbours of those vertices we got above
    		hopNum++;
    	}
    	
    	kHopNeighbours1 = new String[hopNeighbours.getLength()];
    	for(int q=0; q<hopNeighbours.getLength(); q++) {
    		kHopNeighbours1[q] = hopNeighbours.getNode(q).getVertex();
    	}
    	
    	return clearDuplicates(kHopNeighbours1);
    } // end of kHopNeighbours()
    
    public String[] clearDuplicates(String[] arr) {
    	LinkedList list = new LinkedList(null);
    	
    	for(int z=0; z<arr.length; z++) {
    		if(!list.contains(arr[z])) {
    			list.addNode(new Node(arr[z]));
    		}
    	}
    	
    	return list.toArray(); // a method to return an array without duplications
    }


    public void printVertices(PrintWriter os) {
    	for(int i=0; i<vertices.length; i++) {
        	os.append("(" + vertices[i] + ", " + statusOfIndividual.get(vertices[i]) + ") ");
        }
        os.append("\n");
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
    	for(int i=0; i<edges.length; i++) {
        	os.append(edges[i] + "\n");
        }
    	anotherEdges= new String[edges.length];
    	for(int countj=0; countj<edges.length;countj++) {
    		String srcLabel1 = String.valueOf(edges[countj].charAt(2));
    		String tarLabel1 = String.valueOf(edges[countj].charAt(0));
    		anotherEdges[countj] =  srcLabel1 + " " + tarLabel1;	
    	}
    	for(int i=0; i<anotherEdges.length; i++) {
        	os.append(anotherEdges[i] + "\n");
        } // print the edges with inverse order as input, so that meets the requirement of 'undirected'
    } // end of printEdges()


	@Override
	public Map<String, String> getMap() {
		return statusOfIndividual;
	}
	
	/**
	 * 
	 * Node is created to represent a vertex in a graph
	 * Each node should be tightly assigned with a next node to represent an edge if it has another connected vertex
	 * 
	 * @author Zico Zhong, 2021 & Alan Du
	 *
	 */
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
	}

} // end of class AdjacencyList
