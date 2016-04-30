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
	private int [][] pairArray;
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
		this.init();
		// Number of rows in the training data
		int dataRows = this.gridData.length;
		// Number of columns in the training data
		int dataColumns = this.gridData[0].length;
		// Number of rows (same number of columns) in the weights map
		int weightsRows = this.weights.length;
		// Number of columns in the weights map
		int weightsColumns = this.weights[0].length;
		// Number of rounds of training
		int iterations = this.epochs * dataRows;
		// Initial learning rate
		double learningRate = 0.05;
		// Initial neighborhood size
		double neighborhood;
		
		// Current row being processed
		int currentObs;
		// Nearest node to the current point
		int nearest = 0;
		// Smallest identified distance to a node
		double nearestDistance;
		
		// Temporary variables for the current distance to a node
		double dist;
		double tmp;
		
		// "Unpack" the gridData, weights, and pair distances into a 1D array
		double [] data = new double[dataRows * dataColumns];
		double [] nodes = new double[weightsRows * weightsColumns];
		double [] distPairs = new double[pairArray.length * pairArray.length];
		int count = 0;
		for(int i = 0; i < dataColumns; i++)
		{
			for(int j = 0; j < dataRows; j++)
			{
				data[count] = this.gridData[j][i];
				count++;
			}
		}
		count = 0;
		for(int i = 0; i < weightsColumns; i++)
		{
			for(int j = 0; j < weightsRows; j++)
			{
				nodes[count] = this.weights[j][i];
				count++;
			}
		}
		count = 0;
		System.out.println("pairArray.length: " + pairArray.length);
		System.out.println("pairArray[0].length: " + pairArray[0].length);
		int currentX;
		int currentY;
		double xDist;
		double yDist;
		for(int i = 0; i < this.pairArray.length; i++)
		{
			// Set the reference point to the current row
			currentX = this.pairArray[i][0];
			currentY = this.pairArray[i][1];
			for(int j = 0; j < this.pairArray.length; j++)
			{
				// Calculate the rectilinear distances from this point
				// to the reference point
				for(int k = 0; k < 2; k++)
				{
					xDist = Math.abs(this.pairArray[j][0] - currentX);
					yDist = Math.abs(this.pairArray[j][1] - currentY);
					distPairs[count] = xDist + yDist;
				}
				count++;
			}
		}
		
		// Set the neighborhood to capture approximately 2/3 of the nodes.
		// This is approximately 1.75 * variance (See Chebychev's inequality)
		// https://en.wikipedia.org/wiki/Chebyshev's_inequality
		neighborhood = 1.75 * variance(distPairs);
		
		// Adapted from the C code for VR_onlineSOM in the R "class" package
		System.out.println("iterations: " + iterations);
		System.out.println("dataRows: " + dataRows);
		System.out.println("dataColumns: " + dataColumns);
		System.out.println("nodes.length: " + nodes.length);
		System.out.println("data.length: " + data.length);
		System.out.println("weightsRows: " + weightsRows);
		System.out.println("weights.length: " + weights.length);
		System.out.println("weights[0].length:" + weights[0].length);
		for(int i = 0; i < iterations; i++)
		{
			System.out.println(i + " of " + iterations);
			// Choose a random observation for fitting
			currentObs = (int)(Math.random() * dataRows);
			//System.out.println("Using observation " + currentObs);
			// Find its nearest node
			// Start with the maximum distance possible
			nearestDistance = Double.MAX_VALUE;
			nearest = 0;
			for(int j = 0; j < weightsRows; j++)
			{
				// Reset the distance to zero for the next training point
				dist = 0;
				for(int k = 0; k < dataColumns; k++)
				{
					//System.out.println("i:" + i + " j:" + j + " k:" + k);
					// For the current random observation and the current column,
					// find the difference, i.e. the rectilinear distance
					tmp = data[currentObs + k * dataRows] - nodes[j + k * weightsRows];
					// dist^2 is the square of the sums of
					// all the individual components, and
					// minimizing distance^2 leads to the same
					// node as minimizing distance.
					dist += tmp * tmp;
				}
				// New closest node found
				if(dist < nearestDistance)
				{
					//nearestNode = 0;
					// Update the nearest node and distance
					nearest = j;
					nearestDistance = dist;
				}
			}
			
			// Update learning rate and neighborhood distances
			// Initially "pull" the map by large amounts and
			// pull nodes that are farther away (but within the
			// neighborhood as well); as training
			// continues create smaller distortions and apply
			// them within a smaller neighborhood.
			learningRate -= (0.04 * i / iterations);
			neighborhood -= (1.0 * i / iterations);
			// Prevent the neighborhood from becoming too small
			neighborhood = (neighborhood < 0.5) ? 0.5 : neighborhood;
			
			// Apply the distortion to the map for nodes within
			// the neighborhood
			for(int l = 0; l < weightsRows; l++)
			{
				//System.out.println("l: " + l);
				if(distPairs[l + weightsRows * nearest] <= neighborhood)
				{
					for(int m = 0; m < dataColumns; m++)
					{
						//System.out.println("m: " + m);
						tmp = data[currentObs + m * dataRows] - nodes[l + m * weightsRows];
						nodes[l + m * weightsRows] += (tmp * learningRate);
					}
				}
			}
		}
		/**

		for(int i = 0; i < weights.length; i++)
		{
			for(int j = 0; j < weights[0].length; j++)
			{
				System.out.println("" + weights[i][j]);
			}
		}
		* */
		
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
		
		// Adapted from the C code for mapKohonen in the R "kohonen" package
		// Now calculate the weights/data to map distance
		count = 0;
		double [] mapDist = new double[dataRows * weightsRows];
		// Loop over all data points
		for(int i = 0; i < dataRows; i++)
		{
			// Loop over all the map nodes
			for(int j = 0; j < weightsRows; j++)
			{
				mapDist[count] = 0;
				// Loop over all the variable
				for(int k = 0; k < weightsColumns; k++)
				{
					tmp = data[i + k * dataRows] - nodes[j + k * weightsRows];
					mapDist[count] += tmp * tmp;	
				}
				count++;
			}
		}
		System.out.println("Training complete. Here are the codes");
	}
	
	
	/**
	 * Initialize the SOM object for training
	 * This method prepares the SOM for
	 * training by initializing the random
	 * weights with a bootstrap sample from
	 * the training data.
	 * 
	 * */
	private void init()
	{
		// Scale the Grid
		this.scaleGrid();
		
		// Prepare the array for 
		// calculating pair distances
		pairArray = new int[this.xDim * this.yDim][2];
		int count = 0;
		for(int i = 0; i < this.xDim; i++)
		{
			for(int j = 0; j < this.yDim; j++)
			{
				this.pairArray[count][0] = i;
				this.pairArray[count][1] = j;
				count++;
			}
		}
		
		// Useful variables
		int pairRows = this.pairArray.length;
		int dataRows = this.gridData.length;
		
		
		// Sample from the data to determine
		// which observations to use
		// for initial weights
		Set <Integer> samplePoints = new HashSet <Integer>();
		while(samplePoints.size() < pairRows)
		{
			samplePoints.add((int)(Math.random() * dataRows));
		}
		Integer [] sampleIndex = samplePoints.toArray(new Integer[samplePoints.size()]);
		
		// Use the selected rows to build the starting weights
		weights = new double[pairRows][gridData[0].length];
		int weightCount = 0;
		// Select the rows from sampleIndex
		for(Integer i : sampleIndex)
		{
			// Select all the columns in the row
			for(int j = 0; j < gridData[0].length; j++)
			{
				weights[weightCount][j] = gridData[i][j];
			}
			weightCount++;
		}
	}
}
