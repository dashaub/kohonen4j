//Grid.java
/**
 * Representation of grid objects
 * 
 * This class contains methods for representing
 * non-jagged, rectancular data arrays. Useful
 * methods such as mean, variance, and transpose
 * are defined. Since input validation is specific
 * for the Kohonen application, error handling and 
 * validation is handled there, so exceptions
 * will occur if problematic arrays are created
 * (e.g. no rows/columns, jagged, etc). The arrays
 * used to construct a grid object must have at least
 * two columns and must have at least as many rows as columns.
 * 
 * @author David Shaub
 * @version 1.0.0
 * 
 * */
 
import java.util.*;
public class Grid
{
	private double [][] gridData;
	
	/**
	 * Single argument constructor
	 * 
	 * @param input The input array. It must have at least two rows,
	 * at least two columns, and at least as many rows as columns.
	 * 
	 * */
	public Grid(double [][] input)
	{
		// Enforce properties for Grid objects
		// Must have at least two rows
		if(input.length < 2)
		{
			throw new IllegalArgumentException();
		}
		// Must have at at least as many rows as columns
		else if(input.length < input[0].length)
		{
			throw new IllegalArgumentException();
		}
		// Must have at least two columns
		else if(input[0].length < 2)
		{
			throw new IllegalArgumentException();
		}
		// Don't allow jagged arrays
		for(int i = 0; i < input.length; i++)
		{
			if(input.length != input[i].length)
			{
				throw new IllegalArgumentException();
			}
		}
		gridData = input;
	}
	
	
	/**
	 * Calculuate the mean of an array
	 * This method returns the mean of 
	 * an input array.
	 * 
	 * @param inputArray The array for calculation
	 * 
	 * @return The mean of the array
	 * 
	 * */
	public double mean(double[] inputArray)
	{
		double result = 0;
		for(int i = 0; i < inputArray.length; i++)
		{
			result += inputArray[i];
		}
		return result / inputArray.length;
	}
	
	
	/**
	 * Calculuate the variance of an array
	 * This method returns the variance of 
	 * an input array. A sample-size correction
	 * is not used.
	 * 
	 * @param inputArray The array for calculation
	 * 
	 * @return The variance of the array
	 * 
	 * */
	public double variance(double [] inputArray)
	{
		double xMean = mean(inputArray);
		double sumSq = 0;
		for(int i = 0; i < inputArray.length; i++)
		{
			sumSq += Math.pow((inputArray[i] - xMean), 2);
		}
		return sumSq / inputArray.length;
	}
	
	
	/**
	 * Test if the Grid object has zero variance.
	 * An important property to avoid in a data
	 * set for machine learning is columns with
	 * zero variance. These are columns that have
	 * only one unique value. This can be detected by 
	 * finding columns with no variance.
	 * 
	 * @return Whether the Grid object has at least
	 * one column with zero variance
	 * 
	 * */
	public boolean zeroVariance()
	{
		double[] currentColumn = new double[gridData.length];
		// Test each column
		for(int i = 0; i < gridData[0].length; i++)
		{
			// Extract the rows
			for(int j = 0; j < gridData.length; j++)
			{
				currentColumn[j] = gridData[i][j];
			}
			if(variance(currentColumn) == 0)
			{
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Standardize the values in a grid.
	 * This method standardizes the numbers
	 * in a grid by centering each column 
	 * around zero (i.e. forcing the column
	 * means to be zero) and giving each column
	 * unit variance (i.e. forcing the column
	 * variances to be one). This is a necessary
	 * operation for many algorithms that rely
	 * on distances measures so that the results
	 * of the algorithm are independent of the scale
	 * of the input data and units in which they are
	 * expressed. Note: if any column in the Grid object
	 * has zero variance, the scaling operation will
	 * not be performed.
	 * 
	 * @return An scaled array of the Grid object
	 * 
	 * */
	public void scaleGrid()
	{
		// Don't scale if the columns have zero variance
		if(zeroVariance())
		{
			return;
		}
		int nrows = gridData.length;
		int ncols = gridData[0].length;
		double colMean;
		double colVar;
		ArrayList <Double[]> result = new ArrayList <Double[]>();
		double[] currentColumn = new double[nrows];
		// Repeat for every column
		for(int j = 0; j < gridData[0].length; j++)
		{
			// Find the mean and variance for the current column
			for(int i = 0; i < gridData.length; i++)
			{
				currentColumn[i] = gridData[i][j];
			}
			colMean = mean(currentColumn);
			colVar = variance(currentColumn);
			
			// Perform the scaling down the rows
			for(int k = 0; k < currentColumn.length; k ++)
			{
				gridData[k][j] = (currentColumn[k] - colMean) / colVar;
			}
		}
		System.out.println("Scaled successfully");
	}
	
	
	/**
	 * Transpose an array
	 * This method transposes an array
	 * 
	 * @param matrix The array to transpose
	 * 
	 * @return The transposed array
	 * 
	 * */
	// Adapted from http://stackoverflow.com/questions/26197466/transposing-a-matrix-from-a-2d-array
	public double[][] transpose(double [][] matrix)
	{
		int m = matrix.length;
		int n = matrix[0].length;
		double[][] trasposedMatrix = new double[n][m];
		for(int x = 0; x < n; x++)
		{
			for(int y = 0; y < m; y++)
			{
				trasposedMatrix[x][y] = matrix[y][x];
			}
		}
		System.out.println("Transpose successful");
		return trasposedMatrix;	
	}
}
