package data_generation;

import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.List;
import java.util.Random;

import code.AdjacencyList;
import code.AdjacencyMatrix;
import code.ContactsGraph;
import code.IncidenceMatrix;

import java.util.ArrayList;
import joptsimple.OptionParser;
import joptsimple.OptionSet;



public class DataGenerator
{
	private static int length = 0;
	private static String[] edge;
	private static String[] vertices = new String[length];
	public static int intGenerator(Random r, int min, int max) {
		return r.nextInt(max - min) + min;
	}

	public static void vertexAddiction(ContactsGraph graph, int amount) {
		long start = System.currentTimeMillis();
		for(int i = 0; i< amount; i++) {
			graph.addVertex(String.valueOf(i));
		}
		length = amount;
		long end = System.currentTimeMillis();
		
		System.out.println("Processing Time of Adding " + amount + " vertex is :" + (end - start) + "ms");
	}
	
	public static void khopTesting(ContactsGraph graph, int k, int amount) {
		Random r = new Random();
		long start = System.currentTimeMillis();
		int m = intGenerator(r,0,amount);
		String[] neighbours = graph.kHopNeighbours(k, String.valueOf(m));
		
		long end = System.currentTimeMillis();

		System.out.println("Processing Time of Finding khopneighbour of " + m+ " with length of "+ neighbours.length  + " is :" + (end - start) + "ms");
	}
	
	public static void edgeAddiction(ContactsGraph graph,int size, int vertices) {
		long start = System.currentTimeMillis();
		Random r = new Random();
		length = size;
		edge = new String[length];

		for(int i = 0; i < size; i++) {
			int k = intGenerator(r,0,vertices);
			int m = intGenerator(r,0,vertices);
			if(k != m) {
			graph.addEdge(String.valueOf(k), String.valueOf(m));
			}
			edge[i] =String.valueOf(k) + " " + String.valueOf(m);
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Processing Time of adding " + size + " edges is : " + (end - start) + "ms");

	}
	public static void vertexDeletion(ContactsGraph graph, int max) {
		
		long start = System.currentTimeMillis();
		for(int i = 0; i<max; i++) {
			graph.deleteVertex(String.valueOf(i));
		}		
		long end = System.currentTimeMillis();
		
		System.out.println("Processing Time of deleting " + max + " vertices is : " + (end - start) + "ms");
	} // end of processOperations()

	public static void edgeDeletion(ContactsGraph graph, int max) {
		
		long start = System.currentTimeMillis();
		int count = 0;
		for(int i = 0; i < max; i++) {
			
			String k = edge[i];
			int size = k.length()/2;
			String scrC = "" ;
			for(int t = 0; t< size ; t++) {
				scrC = scrC + k.charAt(t);
			}
			String tarC = "";
			for(int t = size+1; t<k.length(); t++) {
				tarC = tarC + k.charAt(t);
			}
			graph.deleteEdge(scrC, tarC);
			count++;
		}

		
		long end = System.currentTimeMillis();
		
		System.out.println("Processing Time of deleting " + count + " edges is : " + (end - start) + "ms");
	}
	public static void MatrixEdgeDeletion(ContactsGraph graph, int max) {
		
		long start = System.currentTimeMillis();
		int count = 0;
		for(int i = 0; i < max; i++) {
			
			String k = edge[i];
			int size = k.length()/2;
			String scrC = "" ;
			for(int t = 0; t< size ; t++) {
				scrC = scrC + k.charAt(t);
			}
			String tarC = "";
			for(int t = size+1; t<k.length(); t++) {
				tarC = tarC + k.charAt(t);
			}
			graph.deleteEdge(scrC, tarC);
			count++;
		}

		
		long end = System.currentTimeMillis();
		
		System.out.println("Processing Time of deleting " + count + " edges is : " + (end - start) + "ms");
	}

	/**
	 * Main method.  Determines which implementation to test and processes command line arguments.
	 */
	public static void main(String[] args) {
		// parse command line options
		OptionParser parser = new OptionParser("f:o:");
		OptionSet options = parser.parse(args);
		// non option arguments
		List<?> tempArgs = options.nonOptionArguments();
		List<String> remainArgs = new ArrayList<String>();
		for (Object object : tempArgs) {
			remainArgs.add((String) object);
		}

		// check number of non-option command line arguments
		if (remainArgs.size() > 1 || remainArgs.size() < 1) {
			System.err.println("Incorrect number of arguments.");
		}

		// parse non-option arguments
		String implementationType = remainArgs.get(0);

		// determine which graph implementation to test
		ContactsGraph graph = null;
		switch(implementationType) {
			case "adjlist":
				graph = new AdjacencyList();
				break;
			case "adjmat":
				graph = new AdjacencyMatrix();
				break;
			case "incmat":
				graph = new IncidenceMatrix();
				break;
		}
		vertexAddiction(graph, 1000);
		edgeAddiction(graph,2000,1000);
	//	vertexDeletion(graph, 1000);
		edgeDeletion(graph,1500);
	//	MatrixEdgeDeletion(graph,20);
	//	khopTesting(graph,1,1000);
	//	khopTesting(graph,3,1000);
//		khopTesting(graph,6,1000);
//		khopTesting(graph,9,1000);
//		khopTesting(graph,12,1000);
//		khopTesting(graph,15,1000);
	} // end of main()

} // end of class RmitCovidModelling

