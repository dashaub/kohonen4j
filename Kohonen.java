//Kohonen.java
/**
 * Visualize cell counts of a rectangular 
 * self-organizing map. Thie program fits
 * a Kohonen network to an input containing
 * numeric data in a csv format.
 * 
 * 
 * This file contains
 * the main method and constructs the necessary
 * windows for user input as well as reading in
 * the data and validating it. It then passes
 * the necesary data onto a SOM object where
 * the model fitting is performed before
 * displaying the results.
 * 
 * @author David Shaub
 * @version 1.0.0
 * 
 * */
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
public class Kohonen extends JFrame
{
	// Instance variables
	private JButton fileChooser = new JButton("Input File");
	private ArrayList <Double[]> inputData = new ArrayList<Double[]>();
	private Grid trainData;
	
	/**
	 * Read in the input csv data
	 * This function reads the input data
	 * from a csv for constructing the
	 * self-organizing map. The function also
	 * checks the data to make sure it is valid
	 * and satisfies the necessary conditions
	 * to construct a self-organizing map.
	 * 
	 * @param input The file object to read in
	 * 
	 * */
	public void readFile(File input)
	{
		// Open a Scanner
		Scanner inFile;
		try
		{
			inFile = new Scanner(input);
			inFile.useDelimiter("\n");
		}
		catch(IOException ioe)
		{
			JOptionPane.showMessageDialog(null, "Cannot read file. Select a readable file.");
			return;
		}
		
		// Treat the first line as a header and use it
		// to determine the number of columns
		int numColumns = 0;
		String currentLine = inFile.next();
		Scanner parser = new Scanner(currentLine);
		parser.useDelimiter(",");
		while(parser.hasNext())
		{
			parser.next();
			numColumns++;
		}
		// There should be at least two columns
		if(numColumns < 2)
		{
			JOptionPane.showMessageDialog(null, "The file should have at least two columns.");
			return;
		}
		// Now read the data
		Double [] currentRow = new Double[numColumns];
		while(inFile.hasNext())
		{
			currentLine = inFile.next();
			parser = new Scanner(currentLine);
			parser.useDelimiter(",");
			try
			{
				// Ignore data in rows with more entries than in the header
				for(int i = 0; i < numColumns; i++)
				{
					currentRow[i] = Double.parseDouble(parser.next());
				}
				inputData.add(currentRow);
			}
			// Ensure the data are parsed to numeric
			catch(NumberFormatException nfe)
			{
				JOptionPane.showMessageDialog(null, "The file should contain only numeric data.");
				return;
			}
			// Ensure a non-jagged array
			catch(NoSuchElementException nsee)
			{
				JOptionPane.showMessageDialog(null, "Every row should contain one number for every columns in the file header.");
				return;
			}
		}
		
		
		// Ensure # rows >= # cols
		if(inputData.size() < numColumns)
		{
			JOptionPane.showMessageDialog(null, "There must be at least as many data rows as columns in the file.");
			return;
		}
		
		// The data has passed validity checks, so convert to an array
		double[][] validData = new double[inputData.size()][numColumns];
		for(int i = 0; i < inputData.size(); i++)
		{
			for(int j = 0; j < inputData.get(i).length; j++)
			{
				validData[i][j] = inputData.get(i)[j];
			}
		}
		
		// Convert to a grid object
		trainData = new Grid(validData);
		System.out.println("Grid successful");
		
		// Scale will fail if a column is all the same value
		// Ensure non-zero variance of columns
		
		// Now scale the grid
	}
	
	class ButtonListener implements ActionListener 
	{
		/**
		 * Define the behavior for the ActionListener
		 * 
		 * @param e The ActionEvent object
		 * 
		 * */
		public void actionPerformed(ActionEvent e) 
		{
			if(e.getSource() == fileChooser)
			{
				JFileChooser fc = new JFileChooser();
				fc.setCurrentDirectory(new File(System.getProperty("user.home")));
				int result = fc.showOpenDialog(null);
				if (result == fc.APPROVE_OPTION)
				{
					File inputFile = fc.getSelectedFile();
					readFile(inputFile);
				}
			}
		}
	}
	
	public Kohonen()
	{
		//Construct the JFrame
		JFrame window = new JFrame("Self-Organizing Map");
		window.setSize (650, 200);
		window.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		window.setLayout(new FlowLayout());
		window.setResizable(false);
		
		//Color
		JLabel colorLabel = new JLabel("Shading");
		window.add(colorLabel);
		String [] colors = new String[]{"Red", "Green", "Blue"};
		JComboBox<String> colorBox = new JComboBox<String>(colors);
		window.add(colorBox);
		
		//Epochs
		JLabel epochLabel = new JLabel("Training epochs");
		window.add(epochLabel);
		JTextField epochs = new JTextField(4);
		epochs.setText("500");
		window.add(epochs);
		
		//Grid size
		JLabel dimensionLabel = new JLabel("Grid dimensions");
		window.add(dimensionLabel);
		JPanel gridElements = new JPanel();
		JTextField xDim = new JTextField(3);
		xDim.setText("5");
		JTextField yDim = new JTextField(3);
		yDim.setText("5");
		window.add(xDim);
		window.add(yDim);
		
		//Input
		ButtonListener bl = new ButtonListener();
		fileChooser.addActionListener(bl);
		window.add(fileChooser);
		
		window.setVisible (true);
	}
	
	
	// Main method
	public static void main(String [] args)
	{
		Kohonen som = new Kohonen();
	}
}
