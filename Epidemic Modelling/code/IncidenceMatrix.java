package code;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Incidence matrix implementation for the GraphInterface interface.
 *
 * Your task is to complete the implementation of this class.  You may add methods, but ensure your modified class compiles and runs.
 *
 * @author Jeffrey Chan, 2021.
 */
public class IncidenceMatrix extends AbstractGraph
{
	private String[] vertices; // Array of vertices, each vertex added into the matrix will also be stored here
	private String[] edges; // Array of edges, each edge added into the matrix will also be stored here, with the direction same as the input
	private String[] anotherEdges; /* Array of edges, storing the edges added into the matrix but with the inverse direction as the input, 
	                                  which ensures that all edges should be undirected in this graph by copying them in another direction */
	private int[][] incidenceMatrix;
	Map<String, String> statusOfIndividual = new HashMap<String, String>(); // The map for storing status of individual corresponding to the vertex respectively
	boolean notAddedToMap = true; // check whether the status of individual corresponding to the vertex has been added into the map above or not
	
	/**
	 * Constructs empty graph.
	 */
    public IncidenceMatrix() {
    } // end of IncidenceMatrix()


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
    	} // get the index of two object vertex labels
    	
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
    		
    		if(edges != null && incidenceMatrix != null) {
    			int[][] temp;
    			temp = incidenceMatrix;
        		incidenceMatrix = new int[temp.length + 1][temp[0].length];
            	for(int i=0; i<temp.length; i++) {
            		for(int j=0; j<temp[i].length; j++) {
            			incidenceMatrix[i][j] = temp[i][j];
            		}
            	} 
            	
            	for(int i=0; i<temp[0].length; i++) {
            		if(i==indexOfSrcLabel || i==indexOfTarLabel) {
            			incidenceMatrix[temp.length][i] = 1;
            		}
            		else {
            			incidenceMatrix[temp.length][i] = 0;
            		}
            	} // add the edge on the column position of the matrix, by assigning 1 to relevant vertex and 0 to irrelevant vertex
            	
    		}else{
    			incidenceMatrix = new int[1][vertices.length];
    			for(int imi=0; imi<vertices.length; imi++) {
    				if(vertices[imi].equals(srcLabel) || vertices[imi].equals(tarLabel)) {
    					incidenceMatrix[0][imi] = 1;
    				}else {
    					incidenceMatrix[0][imi] = 0;
    				}
    			} // if the matrix is empty
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
    	boolean edgeNotExisted = true;// check if the edge doesn't exist
    	int indexOfEdge = 0;// find the index of the edge we're going to remove
    	
    	for(int i=0; i<edges.length; i++) {
    		if((edge.equals(edges[i]) || invertedEdge.equals(edges[i])) && incidenceMatrix != null) {
    			edgeNotExisted = false;
    			for(int j=0; j<edges.length;j++) {
    				if(edges[i] == edge) {
    					indexOfEdge = i;
    				}
    			}
    			
    			String[] temp = edges;
    			edges = new String[temp.length-1];
    			for(int j=0; j<i; j++) {
    				edges[j] = temp[j];
    			}
    			for(int k=i; k<temp.length-1; k++) {
    				edges[k] = temp[k+1];
    			}
    			// remove the edge from the edge array
    			
    			int[][] temp1 = incidenceMatrix;
    			incidenceMatrix = new int[temp1.length-1][temp1[0].length];
    			for(i=0; i<indexOfEdge;i++) {
    				for(int j=0; j<temp1[0].length; j++) {
    					incidenceMatrix[i][j] = temp1[i][j];
    				}
    			}
    			for(i=indexOfEdge; i<temp1.length-1;i++) {
    				for(int j=0; j<temp1[0].length; j++) {
    					incidenceMatrix[i][j] = temp1[i+1][j];
    				}
    			}//remove the edge from the incidence matrix
    		}
    	}
    	
    	if(edgeNotExisted) {
//    		System.err.println("The edge doesn't exist in the array edges");
    	}
    } // end of deleteEdge()


    public void deleteVertex(String vertLabel) {
    	boolean vertexNotExisted = true; // check if the vertex doesn't exist
    	int indexOfVertex = 0; // find the index of the vertex we're going to remove
    	
    	for(int i=0; i<vertices.length; i++) {
    		if(vertLabel.equals(vertices[i]) && incidenceMatrix != null) {
    			vertexNotExisted = false;
    			indexOfVertex = i;
    			
    			int[][] temp1 = incidenceMatrix;
    			if(temp1.length-1 != 0 && temp1[0].length != 0) {
    				incidenceMatrix = new int[temp1.length-1][temp1[0].length];
        			for(int imj=0; imj<indexOfVertex; imj++) {
        				for(int k=0; k<temp1[imj].length; k++) {
        					incidenceMatrix[imj][k] = temp1[imj][k];
        				}
        			}
        			for(int imj=indexOfVertex; imj<temp1.length-1; imj++) {
        				for(int k=0; k<temp1[imj].length;k++) {
        					incidenceMatrix[imj][k] = temp1[imj+1][k];
        				}
        			}
    			}
    			
    			for(int l=0; l<edges.length; l++) {
    				if(String.valueOf(edges[l].charAt(0)).equals(vertLabel) || String.valueOf(edges[l].charAt(2)).equals(vertLabel)) {
    					int[][] temp2 = incidenceMatrix;
    					if(temp2.length-1 != 0 && temp2[0].length != 0) {
    						incidenceMatrix = new int[temp2.length-1][temp2[0].length];
        					for(int m=0; m<temp2.length-1; m++) {
        						for(int n=0; n<l; n++) {
        							if(l<temp2[0].length) {
        								incidenceMatrix[m][n] = temp2[m][n];
        							}
        		    			}
        		    			for(int n=l; n<temp2[0].length-1; n++) {
        		    				incidenceMatrix[m][n] = temp2[m][n+1];
        		    			}
        					}// remove the vertex from the matrix
    					}
    				}
    			}
    			
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
    				if(vertices.length == 0) {
    					String[] empty = new String[0];
    					edges = empty;
    				}
    			} // remove the edge which also contains this vertex from the edges array
    		}
    	}
    	
    	if(vertexNotExisted) {
    		System.err.println("The edge doesn't exist in the array edges");
    	}
    } // end of deleteVertex()


    public String[] kHopNeighbours(int k, String vertLabel) {
    	String[] kHopNeighbours = new String[1];
        int indexOfVertLabel = 0;
        
        for(int i=0; i<vertices.length; i++) {
        	if(vertLabel.equals(vertices[i])) {
        		indexOfVertLabel = i;
        	}
        }
        
        if(k==1 && incidenceMatrix != null) {
        	for(int j=0; j<incidenceMatrix.length; j++) {
        		if(incidenceMatrix[j][indexOfVertLabel] == 1) {
        			String neighbourVert = String.valueOf(edges[j].charAt(0));
        			if(neighbourVert.equals(vertLabel)) {
        				neighbourVert = String.valueOf(edges[j].charAt(2));
        			}
        			if(kHopNeighbours[0] == null) {
        				kHopNeighbours[0] = neighbourVert;
        			}else {
        				String[] temp = kHopNeighbours;
        				kHopNeighbours = new String[temp.length+1];
        				for(int b=0; b<temp.length; b++) {
        					kHopNeighbours[b] = temp[b];
        				}
        				kHopNeighbours[temp.length] = neighbourVert;
        			}
        		}
            } // Checking through the matrix by finding the vertices are connected to the vertex label, 1 under the corresponding column means that the vertex included in the specified edge
        }else if(k==2 && incidenceMatrix != null) {
        	int[] indexOfEdges = new int[1];
        	indexOfEdges[0] = -1;
        	for(int c=0; c<incidenceMatrix.length; c++) {
        		if(incidenceMatrix[c][indexOfVertLabel] == 1) {
        			if(indexOfEdges[0] == -1) {
        				indexOfEdges[0] = c;
        			}else {
        				int[] temp = indexOfEdges;
        				indexOfEdges = new int[temp.length+1];
        				for(int d=0; d<temp.length; d++) {
        					indexOfEdges[d] = temp[d];
        				}
        				indexOfEdges[temp.length] = c;
        			}
        		}
        	} // Checking through the matrix by finding the vertices are directly connected to the vertex label 
        	for(int e=0; e<indexOfEdges.length; e++) {
        		String neighbourVert = String.valueOf(edges[indexOfEdges[e]].charAt(0));
    			if(neighbourVert.equals(vertLabel)) {
    				neighbourVert = String.valueOf(edges[indexOfEdges[e]].charAt(2));
    			}
    			int indexOfNeighbourVert = 0;
    			for(int p=0; p<vertices.length; p++) {
    				if(vertices[p].equals(neighbourVert)) {
    					indexOfNeighbourVert = p;
    				}
    			}
    			for(int f=0; f<incidenceMatrix.length; f++) {
    				if(incidenceMatrix[f][indexOfNeighbourVert] == 1) {
    					String obj = String.valueOf(edges[f].charAt(0));
    					if(obj.equals(vertLabel) || obj.equals(vertices[indexOfNeighbourVert])) {
    					obj = String.valueOf(edges[f].charAt(2));
    					}
    					if(kHopNeighbours[0] == null && !obj.equals(vertLabel) && !obj.equals(vertices[indexOfNeighbourVert])) {
            				kHopNeighbours[0] = obj;
            			}else if(!obj.equals(vertLabel) && !obj.equals(vertices[indexOfNeighbourVert])){
            				boolean vertexnotExisted = true;
            				for(int h=0; h<kHopNeighbours.length; h++) {
            					if(kHopNeighbours[h].equals(obj)) {
            						vertexnotExisted = false;
            					}
            				}
            				if(vertexnotExisted) {
            					String[] temp = kHopNeighbours;
                				kHopNeighbours = new String[temp.length+1];
                				for(int g=0; g<temp.length; g++) {
                					kHopNeighbours[g] = temp[g];
                				}
            					kHopNeighbours[temp.length] = obj;
            				}
            			}
    				}
    			}
        	} /* Checking through the matrix by finding the vertices which are directly connected to the vertices we got from above,
   	             which means that we got the vertices connect to the vertex label at second hand. In other words, k hop twice.
   	             By the way, this method is based on checking the character of every string to confirm the component of an edge*/
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


	@Override
	public Map<String, String> getMap() {
		return statusOfIndividual;
	}

} // end of class IncidenceMatrix
