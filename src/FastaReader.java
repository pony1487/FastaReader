/*
 * REMEBER TO CLOSE FILES( cant do that with files? Only input streams
 * -Program will keep writing to the same output file if you open a new file or same file again without closing
 * -need to get this to do that to a seperate new file (After you write it, clear ArrayList??)
 * -Also need to ensure only opens FASTA files
 */
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner; 

public class FastaReader extends JPanel implements ActionListener{
	
	private JFileChooser fileChooser;
	private JFrame frame;
	private JPanel buttonPanel;
	private JButton openButton;
	private JButton reassignSequenceNumber;
	
	private ArrayList<String> fastaLines;
	private ArrayList<String> editedFastaLines;
	
	
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	private String currentFileName;
	private String outputFileName;
	private String fileNameWithoutExt;
	
	private static int fileNumber;
	
	private boolean fileOpened;
	

		
	public FastaReader()
	{
		openButton = new JButton("Open Fasta File");
		reassignSequenceNumber = new JButton("Reassign Sequence Number");
		
		
		openButton.addActionListener(this);
		reassignSequenceNumber.addActionListener(this);
		
		buttonPanel = new JPanel();
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		fastaLines = new ArrayList<String>();
		editedFastaLines = new ArrayList<String>();
		
		fileOpened = false;
		
		
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == openButton) 
		{
			int returnVal = fileChooser.showOpenDialog(FastaReader.this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) 
	        {
	        	readFile();
	        	fileOpened = true;
	        }
		}//end if
		
		if (e.getSource() == reassignSequenceNumber) 
		{
			if(fileOpened == false)
			{
				JOptionPane.showMessageDialog(null, "Please open Fasta file!", "Error",JOptionPane.ERROR_MESSAGE);
			}
			else
			{
				editString();
				writeNewEditedFile();
			}
			
			fileOpened = false;
		}
       
    }//end actionPerformed()
	
	public void readFile()
	{
		File file = fileChooser.getSelectedFile();
		
		currentFileName = file.getName();
		fileNameWithoutExt = currentFileName.substring(0,currentFileName.lastIndexOf("."));
	
        
        try {
        	
        	/*
			BufferedReader reader = new BufferedReader(new FileReader(file));
        	
			while((lineForReading = reader.readLine()) != null)
			{
				
				fastaLines.add(lineForReading);
				
			}
			*/
			
        	
        	//testing another way
			Scanner reader = new Scanner(new FileReader(file));
        	reader.useDelimiter(">");
        	
        	while (reader.hasNext()) {
                
        		//Add the > char that the scanner discards
        		StringBuilder str = new StringBuilder(reader.next());
        		str.insert(0,'>');
        		fastaLines.add(str.toString());
            }
            
			
			 reader.close();
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}//end readFile()
	
	public void writeNewEditedFile()
	{
		
		outputFileName = String.format(fileNameWithoutExt + "%02d.fasta",fileNumber++);
		
		
		
		File outputFile = new File(outputFileName);
		
		
		try {
			fileWriter = new FileWriter(outputFile);
			bufferedWriter = new BufferedWriter(fileWriter);
			
			for(int i = 0; i < editedFastaLines.size();i++)
			{
				bufferedWriter.write(editedFastaLines.get(i));
			}
			bufferedWriter.flush();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void createGui()
	{
		 //Create and set up the window.
        frame = new JFrame("Fasta File Resequencer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        buttonPanel.add(openButton);
        buttonPanel.add(reassignSequenceNumber);
	
        //Add content to the window.
        frame.add(new FastaReader());
        frame.getContentPane().add(buttonPanel);
        //set size and location
        frame.setPreferredSize(new Dimension(400, 300));
        frame.setLocationRelativeTo(null);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}//end createGui
	
	public void displayFastaLine()
	{
		for(int i = 0; i < fastaLines.size();i++)
		{
			System.out.println(fastaLines.get(i));
		}
	}//end displayFastaLine()
	
	
	
	public void editString()
	{
		int digitCount = 0;
		int numberStartIndex = 3;
		int maxDigitsInNumber = 5;//Num is max 5 digits long
		char c;
		int numOfZeros = 0;
		String zeros = "";
		String editedString;
		StringBuilder stringBuilder;
		
		//loop and edit each String in ArrayList and add to new ArrayList
		for(int j = 0; j < fastaLines.size();j++)
		{
			editedString = fastaLines.get(j);
			digitCount = 0;
			
			//get count of numbers and set amount of zeros to be inserted
			for(int i = numberStartIndex; i < editedString.length();i++)
			{
				c = editedString.charAt(i);
				//
				if(c == ' ')
				{
					break;
				}
				else
				{
					digitCount++;
					//System.out.print(editedString.charAt(i));
					
				}
				
				numOfZeros = maxDigitsInNumber - digitCount;
				System.out.println(numOfZeros);
			}
			
			//set zero string
			switch(numOfZeros)
			{
			case 1:
				zeros = "0";
				break;
			case 2:
				zeros = "00";
				break;
			case 3:
				zeros = "000";
				break;
			case 4:
				zeros = "0000";
				break;
			default:
				zeros = "";
				break;
			}
			
			
			stringBuilder = new StringBuilder(editedString);
			stringBuilder.insert(3, zeros);
			//change edited stringbuilder to string so it can be added to String arrayList
			String strBuilderToString = stringBuilder.toString();
			editedFastaLines.add(strBuilderToString);
			
		}
		
		
	}
	
	public static void main(String[] args)
	{
		FastaReader fr = new FastaReader();
		fr.createGui();
		
		
	}

}
