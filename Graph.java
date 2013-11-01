/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tamm.aa.hw8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.TreeMap;

/**
 *
 * @author Dell
 */
public class Graph {
    
    private static String fileName = "karate.txt";
    private static String outputFileName = "matrix.txt";
    
    public static void main(String[] args) throws IOException
    { 
        int max = getMax();//we have to use the value max+1 from now on for allocation since indexing starts from 0
        
        BitMatrix matrix = createMatrix(max+1, true);//true means a directed graph
        
        //printMatrix(matrix);//print the output of matrix to console (for smaller sizes only)
        
        //writeMatrixToFile(matrix);//write the matrix into a file
        
        //printAdjList(matrix);//print the adjacency list
        
        //printPlotAdjList(matrix);//adjacency list for Gnuplot graphs
        
        //TreeMap<Integer, Double> degrees = calcDistr(matrix, true);//false means an out-degree(or undirected) distribution
        
        /*for (Integer key : degrees.keySet()) { 
            System.out.println(key+"\t"+degrees.get(key));
        }*/
        
        //lineGraphAdjList(matrix, true);// true means a directed line graph
    }
    
    /**
     * Creates an adjacency matrix
     * @param size - number of rows and columns
     * @param directed - whether we are dealing with a directed or an undirected graph
     * @return 
     */
    public static BitMatrix createMatrix(int size, boolean directed) throws IOException
    {
        BitMatrix matrix = new BitMatrix(size, size);
        
        BufferedReader inputStream = null;

        String line;
        
        try {
            inputStream = new BufferedReader(new FileReader(fileName));
            
            while( (line = inputStream.readLine()) != null)
            {
                String[] lineParts = line.split("\\s+");//any number of whitespace characters as delimiter
            
                int y = Integer.parseInt(lineParts[0]);//first node value, represents the start of edge and row number in the matrix (y-axis)
                int x = Integer.parseInt(lineParts[1]);//second node value, represents the end of edge and column number in the matrix (x-axis)
                
                if(directed == false)
                {
                    matrix.set(x,y);
                    matrix.set(y,x);
                }
                else
                    matrix.set(y,x);
                    //matrix.set(y,x);//note: use for gnuplot
            }
        }
        catch (IOException e){}
        finally{
             if (inputStream != null) {
                inputStream.close();
            }
        }
        
        return matrix;
    }
    
    /**
     * Writes the output of the given matrix into a text file
     * HINT: http://www.mkyong.com/java/how-to-write-to-file-in-java-bufferedwriter-example/
     * @param matrix 
     */
    public static void writeMatrixToFile(BitMatrix matrix) throws IOException
    {
        BufferedWriter bw = null;
        try {

            File file = new File(outputFileName);

            // if file doesnt exists, then create it
            if (!file.exists()) {
                    file.createNewFile();
            }

            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            
            for (int i=0; i<matrix.size; i++)
            {
                for (int j=0; j<matrix.size; j++)
                {
                    bw.write(boolToInt(matrix.get(i,j))+" ");
                }
                
                bw.newLine();
            }

            System.out.println("Done");

        } catch (IOException e) {
                e.printStackTrace();
        }
        finally
        {
            bw.close();
        }
    }
    
    /**
     * Finds the maximum value from the data set
     * Used for setting the number of rows and columns of the matrix
     * @return int max value
     */
    public static int getMax() throws IOException
    {
        int max = 0;
        
        BufferedReader inputStream = null;

        String line;
        
        try {
            inputStream = new BufferedReader(new FileReader(fileName));
            
            while( (line = inputStream.readLine()) != null)
            {
                String[] lineParts = line.split("\\s+");//any number of whitespace characters as delimiter
                
                int x = Integer.parseInt(lineParts[0]);
                int y = Integer.parseInt(lineParts[1]);
                
                if(x > max)
                    max = x;
                
                if(y > max)
                    max = y;
            }
            
        }
        catch (IOException e){}
        finally{
             if (inputStream != null) {
                inputStream.close();
            }
        }
        
        return max;
    }
    
    /**
     * Calculates the degree distributions for the given matrix
     * This function calculates the corresponding degrees for undirected graph and the out-degrees of a directed graph
     * @param matrix
     * @param inDeg - calculate the in- or the out-degree
     * @return TreeMap - object with distribution percentages
     */
    public static TreeMap<Integer, Double> calcDistr(BitMatrix matrix, boolean inDeg)
    {
        TreeMap<Integer, Double> degrees = new TreeMap<Integer, Double>();//we use a TreeMap object so the output would be sorted by key values
        
        for (int i=0; i<matrix.size; i++)
        {
            int count = 0;
            for (int j=0; j<matrix.size; j++)
            {
                //calculate the out-degree by counting 1-s in rows
                if(inDeg == false)
                {
                    if(matrix.get(i,j) == true)
                        count++;
                }
                //calculate the in-degree by counting 1-s in columns
                else
                {
                    if(matrix.get(j,i) == true)
                        count++;
                }
                
            }
            
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
    
    /**
     * Prints the adjacency list for the given matrix
     * @param matrix 
     */
    public static void printAdjList(BitMatrix matrix)
    {
        System.out.println("Adjacency list");
        
        for (int i=0; i<matrix.size; i++)
        {
            System.out.print(i+" adjacent to: ");
            for (int j=0; j<matrix.size; j++)
            {
                if(matrix.get(i,j) == true)
                    System.out.print(j+", ");
            }
            System.out.println();
        }
    }
    
    /**
     * Prints the given adjacency matrix
     * Used for small data sets
     * @param matrix 
     */
    public static void printMatrix(BitMatrix matrix)
    {
        for (int i=0; i<matrix.size; i++)
        {
            for (int j=0; j<matrix.size; j++)
            {
                System.out.print(boolToInt(matrix.get(i,j))+" ");
            }
            System.out.println();
        }
    }
    
    /**
     * A helper functions that prints the adjacency list for the given matrix to be used in gnuplot graphs
     * @param matrix 
     */
    public static void printPlotAdjList(BitMatrix matrix)
    {
        for (int i=0; i<matrix.size; i++)
        {
            
            for (int j=0; j<matrix.size; j++)
            {
                if(matrix.get(i,j) == true)
                {
                    System.out.println((i)+"\t"+(j));
                }       
            }
        }
    }
    
    /**
     * A small helper that converts the given boolean type bit value to 1 or 0
     * @param val
     * @return 
     */
    public static int boolToInt(boolean val)
    {
        if (val == false)
            return 0;
        else
            return 1;
    }
    
    /**
     * Creates a line graph based on the given graph's adjacency matrix
     * @param matrix
     * @param directed - whether the resulting line graph is directed
     * @throws IOException 
     */
    public static void lineGraphAdjList(BitMatrix matrix, boolean directed) throws IOException
    {
        BufferedReader inputStream = null;

        String line;
        
        try {
            inputStream = new BufferedReader(new FileReader(fileName));
            
            while( (line = inputStream.readLine()) != null)
            {
                String[] lineParts = line.split("\\s+");//any number of whitespace characters as delimiter
            
                int y = Integer.parseInt(lineParts[0]);//first node value, represents the start of edge and row number in the matrix (y-axis)
                int x = Integer.parseInt(lineParts[1]);//second node value, represents the end of edge and column number in the matrix (x-axis)
                
                System.out.print(y+" "+x+" adjacent to: ");
                
                if(directed == false)
                {
                    for (int i=0; i<matrix.size; i++)
                    {
                        if(matrix.get(y,i) == true && i != x)
                            System.out.print(y+" "+i+", ");
                    }
                }
                
                
                for (int i=0; i<matrix.size; i++)
                {
                    if(matrix.get(x,i) == true && i != y)
                        System.out.print(x+" "+i+", ");
                }
                
                System.out.println();
            }
        }
        catch (IOException e){}
        finally{
             if (inputStream != null) {
                inputStream.close();
            }
        }
    }
    
}
