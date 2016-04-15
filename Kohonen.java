//Kohonen.java
/**
 * Visualize cell counts of a rectangular 
 * self-organizing map.
 * 
 * @author David Shaub
 * @version 1.0
 * 
 * */
 
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
public class Kohonen extends JFrame
{
	private JButton fileChooser = new JButton("Input File");
	private ArrayList <Double> inputData;
	
	
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
				try
				{
					Scanner inFile = new Scanner(new File("wines.csv"));
				}
				catch(IOException ioe)
				{
					JOptionPane.showMessageDialog(null, "Cannot read file. Select a valid file.");
				}
			}
		}
	}
	
	public Kohonen()
	{
		//Construct the JFrame
		JFrame window = new JFrame("Self-Organizing Map");
		window.setSize (800, 200);
		window.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		window.setLayout(new FlowLayout());
		
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
		window.add(epochs);
		
		//Grid size
		JLabel dimensionLabel = new JLabel("Grid dimensions");
		window.add(dimensionLabel);
		JPanel gridElements = new JPanel();
		JTextField xDim = new JTextField(3);
		JTextField yDim = new JTextField(3);
		window.add(xDim);
		window.add(yDim);
		
		//Input
		JButton inputButton= new JButton("Input file");
		ButtonListener bl = new ButtonListener();
		inputButton.addActionListener(bl);
		window.add(inputButton);
		//JFileChooser fc = new JFileChooser();
		//window.add(fc, BorderLayout.NORTH);
		
		window.setVisible (true);
	}
	
	public static void main(String [] args)
	{
		Kohonen som = new Kohonen();
	}
}
