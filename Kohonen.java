//Kohonen.java
/**
 * A simple GUI for fitting self-organizing maps.
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
	private JTextField xDim = new JTextField(3);
	private JTextField yDim = new JTextField(3);
	private JTextField epochs = new JTextField(4);
	
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
		int rowNum = 0;
		while(inFile.hasNext())
		{
			currentLine = inFile.next();
			parser = new Scanner(currentLine);
			parser.useDelimiter(",");
			try
			{
				currentRow = new Double[numColumns];
				// Ignore data in rows with more entries than in the header
				for(int i = 0; i < numColumns; i++)
				{
					currentRow[i] = Double.parseDouble(parser.next());
					System.out.print("" + currentRow[i] + " ");
				}
				inputData.add(currentRow);
				//inputData.add(Collections.addAll(new ArrayList <Double []>(), currentRow));
				//inputData.add(Arrays.copyOf(currentRow));
				// Fill the array
				//inputData.add(new Double[numColumns]);
				/**
				 * Failed attempt at adding an empty array
				 * and updating 
				for(int j = 0; j < currentRow.length; j++)
				{
					inputData.set(rowNum)[j] = currentRow[j];
				}
				rowNum++;
				* */
				System.out.println();
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
		Double [] tmpArray;
		for(int i = 0; i < inputData.size(); i++)
		{
			System.out.println("i " + i);
			//System.out.println(inputData.get(i));
			tmpArray = inputData.get(i);
			for(int j = 0; j < numColumns; j++)
			{
				System.out.println("" + tmpArray[j] + ", ");
				validData[i][j] = tmpArray[j];
				//System.out.print("" + validData[i][j] + ", ");
			}
			System.out.println();
		}
		
		// Convert to a grid object
		//trainData = new Grid(validData);
		System.out.println("Grid successful");
		//trainData = trainData.scaleGrid;
		System.out.println("Grid scaled");
		int xVal;
		int yVal;
		int epochVal;
		try
		{
			xVal = Integer.parseInt(xDim.getText());
			yVal = Integer.parseInt(yDim.getText());
			epochVal = Integer.parseInt(epochs.getText());
		}
		catch(NumberFormatException nfe)
		{
			JOptionPane.showMessageDialog(null, "The grid dimensions and training epochs must be positive integers.");
			return;
		}
		// Create the SOM object
		SOM training = new SOM(validData, xVal, yVal, epochVal);
		// Prepare the object for training (i.e. weights and distances)
		//training.init();
		// Train the object and store the result
		training.train();
		
		
		
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
		
		epochs.setText("20");
		window.add(epochs);
		
		//Grid size
		JLabel dimensionLabel = new JLabel("Grid dimensions");
		window.add(dimensionLabel);
		JPanel gridElements = new JPanel();
		xDim.setText("5");
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
