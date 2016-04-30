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
	// x dimension of the map
	private int xDim;
	// y dimension of the map
	private int yDim;
	// number of trianing rounds
	private int epochs;
	// ordered pairs for all the points on the map
	private int [][] pairArray;
	// weights fitted during training
	private double [][] weights;
	// final node assigned to each observation in training
	private int finalNodes[];
	// the disance from each data point to the final node
	private double finalDistances[];
	
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
	 * Getter method for finalDistances.
	 * 
	 * This method returns the final distances
	 * to the assigned node for each observation
	 * after training.
	 * 
	 * @return An array with the distance
	 * to the assigned node for each observation
	 * 
	 * */
	 public double [] getDistances()
	 {
		 return this.finalDistances;
	 }
	 
	 
	 /**
	 * Getter method for finalNodes.
	 * 
	 * This method returns the final node
	 * labels assigned to each observation
	 * after training
	 * 
	 * @return An array with the node labels
	 * for each observation
	 * 
	 * */
	 public int [] getNodes()
	 {
		 return this.finalNodes;
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
				xDist = Math.abs(this.pairArray[j][0] - currentX);
				yDist = Math.abs(this.pairArray[j][1] - currentY);
				distPairs[count] = xDist + yDist;
				count++;
			}
		}
		
		// Set the neighborhood to capture approximately 2/3 of the nodes.
		// This is approximately 1.75 * variance (See Chebychev's inequality)
		// https://en.wikipedia.org/wiki/Chebyshev's_inequality
		neighborhood = 1.75 * variance(distPairs);
		
		// Adapted from the C code for VR_onlineSOM in the R "class" package
		for(int i = 0; i < iterations; i++)
		{
			// Choose a random observation for fitting
			currentObs = (int)(Math.random() * dataRows);
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
					// For the current random observation and the current column,
					// find the difference
					tmp = data[currentObs + k * dataRows] - nodes[j + k * weightsRows];
					// dist^2 is the square of the sums of
					// all the individual components, and
					// minimizing distance^2 leads to the same
					// node as minimizing distance.
					dist += (tmp * tmp);
				}
				// New closest node found
				if(dist < nearestDistance)
				{
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
			//neighborhood = (neighborhood < 0.5) ? 0.5 : neighborhood;
			
			// Apply the distortion to the map for nodes within
			// the neighborhood
			for(int l = 0; l < weightsRows; l++)
			{
				if(distPairs[l + weightsRows * nearest] <= neighborhood)
				{
					for(int m = 0; m < dataColumns; m++)
					{
						//System.out.println("m: " + m);
						tmp = data[currentObs + m * dataRows] - nodes[l + m * weightsRows];
						//System.out.println("" + tmp);
						nodes[l + m * weightsRows] += (tmp * learningRate);
						//System.out.println("" + nodes[l + m * weightsRows]);
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
					//System.out.println("" + tmp);
					//System.out.println(nodes[j + k * weightsRows]);
					mapDist[count] += (tmp * tmp);	
					//System.out.println(mapDist[count]);
				}
				count++;
			}
		}
		System.out.println(count);
		
		// Represent the map distances as the original matrix
		count = 0;
		double [][] distanceMatrix = new double [dataRows][weightsRows];
		for(int i = 0; i < dataRows; i++)
		{
			for(int j = 0; j < dataColumns; j++)
			{
				distanceMatrix[i][j] = mapDist[count];
				count++;
				//System.out.println(mapDist[count]);
			}
		}
		
		// Finally label the observations with the nearest node
		// to complete the map training
		finalNodes = new int[dataRows];
		finalDistances = new double[dataRows];
		count = 0;
		int nodeIndex;
		double minDistance;
		for(int i = 0; i < distanceMatrix.length; i++)
		{
			count = 0;
			nodeIndex = count;
			minDistance = Double.MAX_VALUE;
			//minDistance = 40000000;
			for(int j = 0; j < dataColumns; j++)
			{
				if(distanceMatrix[i][j] < minDistance)
				{
					minDistance = distanceMatrix[i][j];
					nodeIndex = count;
				}
				count++;
			}
			finalNodes[i] = nodeIndex;
			finalDistances[i] = minDistance;
		}
		System.out.println("Training complete. Here are the nodes and distances");
		
		for(int i = 0; i < finalNodes.length; i++)
		{
			System.out.println("node: " + finalNodes[i] + "\t distance: " + finalDistances[i]);
		}
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
