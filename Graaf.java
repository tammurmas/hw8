/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tamm.aa.hw8;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 *
 * @author Dell
 */
public class Graaf {
    
    private String outputFileName;
    private ArrayList<String> values;
    private int size;
    private BitMatrix matrix;
    private TreeMap<Integer, ArrayList<Integer>> adjList;
    
    public Graaf(String fileName, String outputFileName, boolean directed) throws IOException
    {
        this.outputFileName = outputFileName;
        
        this.values = new ArrayList<String>();//data about edges from the file
        
        BufferedReader inputStream = null;

        String line;
        
        try {
            inputStream = new BufferedReader(new FileReader(fileName));
            
            while( (line = inputStream.readLine()) != null)
            {
                this.values.add(line);
            }
        }
        catch (IOException e){}
        finally{
             if (inputStream != null) {
                inputStream.close();
            }
        }
        
        this.size = 0;//size of the matrix
        
        //determine the size of the matrix
        for (int i=0; i<this.values.size(); i++)
        {
            String[] lineParts = this.values.get(i).split("\\s+");//any number of whitespace characters as delimiter
                
                int x = Integer.parseInt(lineParts[0]);
                int y = Integer.parseInt(lineParts[1]);
                
                if(x > this.size)
                    this.size = x;
                
                if(y > this.size)
                    this.size = y;
        }
        
        this.size++;//increment matrix size since indexing starts from 0
        
        this.matrix = new BitMatrix(this.size, this.size);
        
        for (int i=0; i<this.values.size(); i++)
        {
            String[] lineParts = this.values.get(i).split("\\s+");//any number of whitespace characters as delimiter

            int y = Integer.parseInt(lineParts[0]);//first node value, represents the start of edge and row number in the matrix (y-axis)
            int x = Integer.parseInt(lineParts[1]);//second node value, represents the end of edge and column number in the matrix (x-axis)

            if(directed == false)
            {
                this.matrix.set(x,y);
                this.matrix.set(y,x);   
            }
            else
                this.matrix.set(y,x);
            
                //matrix.set(y,x);//note: use for gnuplot
        }
        
        this.adjList = new TreeMap<Integer, ArrayList<Integer>>();
        
        for (int i=0; i<this.matrix.size; i++)
        {
            ArrayList<Integer> adj = new ArrayList<Integer>();
            
            
            for (int j=0; j<this.matrix.size; j++)
            {
                if(this.matrix.get(i, j) == true)
                {
                    adj.add(j);
                }
            }
            
            this.adjList.put(i, adj);
        } 
    }
    
    public static void main(String[] args) throws IOException
    {
        Graaf karate = new Graaf("karate.txt", "karate_matrix.txt", false);
        
        for (int i=0; i<karate.adjList.size(); i++)
        {
            if(karate.adjList.get(i) != null)
                System.out.println(i+": "+karate.adjList.get(i).toString());
        }
        
        TreeMap<Integer, Double> degrees = karate.calcDistr();
        
        System.out.print(degrees.toString());
        
        //karate.matrix.writeMatrixToFile(karate.outputFileName);
        
        //karate.matrix.printAdjList();
        
        //Graaf usair = new Graaf("usairport.txt", "matrix.txt", false);
        
        //usair.matrix.printMatrix();
        
        //usair.matrix.printAdjList();
    }
    
    /**
     * Calculates the degree distributions for the given matrix
     * This function calculates the corresponding degrees for undirected graph and the out-degrees of a directed graph
     * @param matrix
     * @param inDeg - calculate the in- or the out-degree
     * @return TreeMap - object with distribution percentages
     */
    public TreeMap<Integer, Double> calcDistr()
    {
        TreeMap<Integer, Double> degrees = new TreeMap<Integer, Double>();//we use a TreeMap object so the output would be sorted by key values
        
        for (int i=0; i<this.size; i++)
        {
            int count = this.adjList.get(i).size();
            
            if(degrees.get(count) != null)
                degrees.put(count, degrees.get(count)+1);
            else
                degrees.put(count, 1.0);
        }
        
        for (Integer key : degrees.keySet()) { 
            Double val = (Double)degrees.get(key);
            degrees.put(key, Math.round(val/(matrix.size)*100*100.0)/100.0);//find the percentage and round it to 2 decimal points
        }
        
        return degrees;
    }
    
}
