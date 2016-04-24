//SOM.java
/**
 * 
 * @author David Shaub
 * @version 1.0.0
 * 
 * */
 
 import java.util.*;
public class SOM extends Grid
{
	private int xDim;
	private int yDim;
	private int epochs;
	private double [][] pairArray;
	private double [][] weights;
	
	public SOM(double[][] matrix, int xDim, int yDim, int epochs)
	{
		super(matrix);
		// Only allow positive xDim and yDim
		if(xDim <= 0 || yDim <= 0)
		{
			throw new IllegalArgumentException();
		}
		this.xDim = xDim;
		this.yDim = yDim;
		this.epochs = epochs;
	}
	
	/**
	 * Train the SOM to the data.
	 * This method runs the training
	 * algorithm to fit the self-organizing
	 * maps to the training data
	 * 
	 * */
	public Grid train()
	{
		// Prepare the output grid for 
		// calculating pair distances
		pairArray = new double[xDim][2];
		for(int i = 0; i < xDim; i++);
		{
			for(int j = 0; j < yDim; j++)
			{
				pairArray[xDim][0] = xDim;
				pairArray[xDim][1] = yDim;
			}
		}
		return new Grid(pairArray);
	}
	
	
	/**
	 * Initialize the SOM object for training
	 * This method prepares the SOM for
	 * training by initializing the random
	 * weights with a bootstrap sample from
	 * the training data.
	 * 
	 * */
	public void init()
	{
		// Prepare the output grid for 
		// calculating pair distances
		pairArray = new double[xDim][2];
		for(int i = 0; i < xDim; i++);
		{
			for(int j = 0; j < yDim; j++)
			{
				pairArray[xDim][0] = xDim;
				pairArray[xDim][1] = yDim;
			}
		}
		
		// Useful variables
		int pairRows = pairArray.length;
		int dataRows = gridData.length;
		
		// Sample from the data to determine
		// which observations to use
		// for initial weights
		HashSet samplePoints = new HashSet();
		while(samplePoints.size() < dataRows)
		{
			samplePoints.add((int)(Math.random() * dataRows + 1));
		}
		
		// Use the selected rows to build the starting weights
		weights = new double[pairRows][gridData[0].length];
		for(int i = 0; i < pairRows; i++)
		{
			for(int j = 0; j < gridData[0].length; j++)
			{
				weights[i][j] = gridData[i][j];
			}
		}
	}
}
