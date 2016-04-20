//SOM.java
/**
 * 
 * @author David Shaub
 * @version 1.0.0
 * 
 * */
public class SOM extends Grid
{
	private int xDim;
	private int yDim;
	private int epochs;
	
	public SOM(double[][] matrix, int xDim, int yDim, int epochs)
	{
		super(matrix);
		this.xDim = xDim;
		this.yDim = yDim;
		this.epochs = epochs;
	}
	
}
