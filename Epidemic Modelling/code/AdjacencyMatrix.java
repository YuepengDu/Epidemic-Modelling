package code;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Adjacency matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */

enum Status{
	S, //SUSCEPTIBLE
	I, //INFECTED
	R //RECOVERED
}

public class AdjacencyMatrix extends AbstractGraph
{
	private String[] vertices; // Array of vertices, each vertex added into the matrix will also be stored here
	private String[] edges; // Array of edges, each edge added into the matrix will also be stored here, with the direction same as the input
	private String[] anotherEdges; /* Array of edges, storing the edges added into the matrix but with the inverse direction as the input, 
	                                  which ensures that all edges should be undirected in this graph by copying them in another direction */
	private int[][] adjacencyMatrix;
	Map<String, String> statusOfIndividual = new HashMap<String, String>(); // The map for storing status of individual corresponding to the vertex respectively
	boolean notAddedToMap = true; // check whether the status of individual corresponding to the vertex has been added into the map above or not
	
	/**
	 * Constructs empty graph.
	 */
    public AdjacencyMatrix() {
    } // end of AdjacencyMatrix()


    public void addVertex(String vertLabel) {
    	if(vertices == null) {
    		vertices = new String[1];
    		vertices[0] = vertLabel;
    	}else {
    		boolean vertexNotExisted = true;// check if the vertex has been added
    		for(int i=0; i<vertices.length;i++) {
    			if(vertLabel.equals(vertices[i])) {
    				vertexNotExisted = false;
    			}
    		}
    		if(vertexNotExisted) {
    			String[] temp;
        		temp = vertices;
        		vertices = new String[temp.length + 1];
        		for(int i=0; i<temp.length; i++) {
        			vertices[i] = temp[i];
        		}
        		vertices[temp.length] = vertLabel;
    		}
    	}//add the vertex into the vertices array
    	
        if(adjacencyMatrix == null) {
        	adjacencyMatrix = new int[1][1];
        	adjacencyMatrix[0][0] = 0;
        }else {
        	int[][] temp;
        	temp = adjacencyMatrix;
        	adjacencyMatrix = new int[temp.length + 1][temp[0].length + 1];
        	for(int i=0; i<temp.length; i++) {
        		for(int j=0; j<temp[i].length; j++) {
        			adjacencyMatrix[i][j] = temp[i][j];
        		}
        	}
        	for(int i=0; i<temp.length; i++) {
        		adjacencyMatrix[i][temp[i].length] = 0;
        	}
        	for(int i=0; i<adjacencyMatrix[temp.length].length; i++) {
        		adjacencyMatrix[temp.length][i] = 0;
        	}
        }// add the vertex to the adjacency matrix
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
    		if(edges == null) {
        		edges = new String[1];
        		edges[0] = edge;
        	}else {
        		String[] temp;
        		temp = edges;
        		edges = new String[temp.length + 1];
        		for(int i=0; i<temp.length; i++) {
        			edges[i] = temp[i];
        		}
        		edges[temp.length] = edge;
        	}//add the edge to the edges array
    		
    		for(int i=0; i<adjacencyMatrix.length; i++) {
    			for(int j=0; j<adjacencyMatrix[i].length; j++) {
    				if((i == indexOfSrcLabel && j == indexOfTarLabel) || (j == indexOfSrcLabel && i == indexOfTarLabel)) {
    					adjacencyMatrix[i][j] = 1;
    				}
    			}
    		}// add the edge to the matrix.
    	}else if(!srcLabelExisted || !tarLabelExisted){
    		System.err.println("At least one of the vertex of this edge doesn't exist in the vertices array.");
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
    	String edge = srcLabel + " " + tarLabel;// create edge as a string
    	String invertedEdge = tarLabel + " " + srcLabel; // check the edge when it's inputed in inverse order
    	int indexOfSrcLabel = 0;// find the position of srcLabel in the matrix
		int indexOfTarLabel = 0;// find the position of tarLabel in the matrix
    	boolean edgeNotExisted = true;// check if the edge doesn't exist
    	
    	for(int i=0; i<edges.length; i++) {
    		if(edge.equals(edges[i]) || invertedEdge.equals(edges[i])) {
    			edgeNotExisted = false;
    			
    			String[] temp = edges;
    			edges = new String[temp.length-1];
    			for(int j=0; j<i; j++) {
    				edges[j] = temp[j];
    			}
    			for(int k=i; k<temp.length-1; k++) {
    				edges[k] = temp[k+1];
    			}
    			// remove the edge from the edge array
    			
    			for(int q=0; q<vertices.length;q++) {
    				if(vertices[q].equals(srcLabel)) {
    					indexOfSrcLabel = q;
    				}
    				if(vertices[q].equals(tarLabel)) {
    					indexOfTarLabel = q;
    				}
    			}
    			for(int j=0; j<adjacencyMatrix.length; j++) {
        			for(int k=0; k<adjacencyMatrix[j].length; k++) {
        				if((k == indexOfSrcLabel && j == indexOfTarLabel) || (j == indexOfSrcLabel && k == indexOfTarLabel)) {
        					adjacencyMatrix[k][j] = 0;
        				}
        			}
        		}// remove the edge from the matrix.
    			
			}
    	}// delete the edge completely if it exists
    	
    	if(edgeNotExisted) {
  //  		System.err.println("The edge doesn't exist in the array edges");
    	}
    	
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
    	boolean vertexNotExisted = true;// check if the vertex has been added
    	int indexOfVertex = 0;// find the position of vertex in the 2d array matrix, two of the positions of this vertex should be the same in both matrix and matrix[i]
		for(int i=0; i<vertices.length;i++) {
			if(vertLabel.equals(vertices[i])) {
				vertexNotExisted = false;
				indexOfVertex = i;
			}
		}
		
		if(vertexNotExisted) {
    		System.err.println("The vertex doesn't exist in the array vertices");
    	}else {
    		int[][] temp1 = adjacencyMatrix;
			adjacencyMatrix = new int[temp1.length-1][temp1[0].length];
			for(int imj=0; imj<indexOfVertex; imj++) {
				for(int k=0; k<temp1[imj].length; k++) {
					adjacencyMatrix[imj][k] = temp1[imj][k];
				}
			}
			for(int imj=indexOfVertex; imj<temp1.length-1; imj++) {
				for(int k=0; k<temp1[imj].length;k++) {
					adjacencyMatrix[imj][k] = temp1[imj+1][k];
				}
			} 
			
			int[][] temp2 = adjacencyMatrix;
			if(temp2.length != 0 && temp2[0].length != 0) {
				adjacencyMatrix = new int[temp2.length][temp2[0].length-1];
			}
			for(int m=0; m<temp2.length; m++) {
				for(int n=0; n<indexOfVertex; n++) {
					adjacencyMatrix[m][n] = temp2[m][n];
    			}
    			for(int n=indexOfVertex; n<temp2.length-1; n++) {
    				adjacencyMatrix[m][n] = temp2[m][n+1];
    			}
			}// remove the vertex from the matrix
			
			String[] temp = vertices;
			vertices = new String[temp.length-1];
			for(int j=0; j<indexOfVertex; j++) {
				vertices[j] = temp[j];
			}
			for(int k=indexOfVertex; k<temp.length-1; k++) {
				vertices[k] = temp[k+1];
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
			} //remove the edge which also contains the vertex is going to be deleted
			if(vertices.length == 0) {
				String[] empty = new String[0];
				edges = empty;
			}
    	}// delete the vertex completely if it exists
		
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
        String[] kHopNeighbours = new String[1]; // Initialise the array of k hop neighbours
        int indexOfVertLabel = 0;
        
        for(int i=0; i<vertices.length; i++) {
        	if(vertLabel.equals(vertices[i])) {
        		indexOfVertLabel = i;
        	}
        } // get the index of vertex label
        
        if(k==1) {
        	for(int a=0; a<adjacencyMatrix[indexOfVertLabel].length; a++) {
        		if(adjacencyMatrix[indexOfVertLabel][a] == 1) {
        			if(kHopNeighbours[0] == null) {
        				kHopNeighbours[0] = vertices[a];
        			}else {
        				String[] temp = kHopNeighbours;
        				kHopNeighbours = new String[temp.length+1];
        				for(int b=0; b<temp.length; b++) {
        					kHopNeighbours[b] = temp[b];
        				}
        				kHopNeighbours[temp.length] = vertices[a];
        			}
        		}
        	} // Checking through the matrix by finding the vertices are connected to the vertex label, 1 means the corresponding vertex is connecting to the vertex label
        }else if(k == 2) {
        	int[] indexOfAdjVertices = new int[1];
        	for(int c=0; c<adjacencyMatrix[indexOfVertLabel].length; c++) {
        		if(adjacencyMatrix[indexOfVertLabel][c] == 1) {
        			if(indexOfAdjVertices[0] == 0) {
        				indexOfAdjVertices[0] = c;
        			}else {
        				int[] temp = indexOfAdjVertices;
        				indexOfAdjVertices = new int[temp.length+1];
        				for(int d=0; d<temp.length; d++) {
        					indexOfAdjVertices[d] = temp[d];
        				}
        				indexOfAdjVertices[temp.length] = c;
        			}
        		}
        	} // Checking through the matrix by finding the vertices are directly connected to the vertex label 
        	for(int e=0; e<indexOfAdjVertices.length; e++) {
        		for(int f=0; f<adjacencyMatrix[(indexOfAdjVertices[e])].length; f++) {
        			if(adjacencyMatrix[(indexOfAdjVertices[e])][f] == 1) {
        				if(kHopNeighbours[0] == null && !vertices[f].equals(vertLabel)) {
            				kHopNeighbours[0] = vertices[f];
            			}else {
            				boolean vertexnotExisted = true;
            				for(int h=0; h<kHopNeighbours.length-1; h++) {
            					if(kHopNeighbours[h] == vertices[f]) {
            						vertexnotExisted = false;
            					}
            				}
            				if(vertexnotExisted && !vertLabel.equals(vertices[f])) {
            					String[] temp = kHopNeighbours;
                				kHopNeighbours = new String[temp.length+1];
                				for(int g=0; g<temp.length; g++) {
                					kHopNeighbours[g] = temp[g];
                				}
            					kHopNeighbours[temp.length] = vertices[f];
            				}
            			}
        			}
        		}
        	} /* Checking through the matrix by finding the vertices which are directly connected to the vertices we got from above,
        	     which means that we got the vertices connect to the vertex label at second hand. In other words, k hop twice*/
        }
        
        return kHopNeighbours;
    } // end of kHopNeighbours()


    public void printVertices(PrintWriter os) {
        for(int i=0; i<vertices.length; i++) {
        	os.append("(" + vertices[i] + ", " + statusOfIndividual.get(vertices[i]) + ") ");
        }
        os.append("\n");
    } // end of printVertices()


    public void printEdges(PrintWriter os) {
    	for(int i=0; i<edges.length; i++) {
        	os.append(edges[i] + "\n");
        } // print the edges with correct order as input
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
    
    public Map<String, String> getMap() {
		return statusOfIndividual;
	}

} // end of class AdjacencyMatrix
