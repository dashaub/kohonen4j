//SOM.java
/**
 * Fit a self-organizing map to a dataset.
 * 
 * Copyright (C) 2016 David Shaub
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
	public void train()
	{
		init();
		int dataRows = this.gridData.length;
		int dataColumns = this.gridData[0].length;
		int weightsRows;
		int iterations = this.epochs * dataRows;
		int currentObs;
		int nearestNode;
		double nearestDistance;
		
		// Adapted from the C code for VR_onlineSOM in the R 'class' package
		for(int i = 0; i < iterations; i++)
		{
			// Choose a random set of observations
			currentObs = (int)(Math.random() * dataRows);
			// Find its nearest node
			// Start with the first observation matching
			// And the maximum distance possible
			nearestNode = 0; nearestDistance = 1.7976931348623157E308;
		}
		
		/**
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
		* */
		//return new Grid(pairArray);
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
		Set <Integer> samplePoints = new HashSet <Integer>();
		while(samplePoints.size() < dataRows)
		{
			samplePoints.add((int)(Math.random() * dataRows));
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
	Grid distGrid = new Grid(pairArray).distance();
}
