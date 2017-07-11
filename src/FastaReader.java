/*
 * REMEBER TO CLOSE FILES
 */
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.awt.BorderLayout;
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

public class FastaReader extends JPanel implements ActionListener{
	
	private JFileChooser fileChooser;
	private JFrame frame;
	private JPanel buttonPanel;
	private JButton openButton;
	private JButton displayLines;
	
	private ArrayList<String> fastaLines;
	private String lineForReading;
	
	private FileWriter fileWriter;
	private BufferedWriter bufferedWriter;
	private String currentFileName;
	private String outputFileName;
	private String fileNameWithoutExt;
	
	private static int fileNumber;
	

		
	public FastaReader()
	{
		openButton = new JButton("Open Fasta File");
		displayLines = new JButton("Format File");
		
		
		openButton.addActionListener(this);
		displayLines.addActionListener(this);
		
		buttonPanel = new JPanel();
		
		fileChooser = new JFileChooser();
		fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		
		fastaLines = new ArrayList<String>();
		
		
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getSource() == openButton) 
		{
			int returnVal = fileChooser.showOpenDialog(FastaReader.this);

	        if (returnVal == JFileChooser.APPROVE_OPTION) 
	        {
	        	readFile();
	        }
		}//end if
		
		if (e.getSource() == displayLines) 
		{
			displayFastaLine();
			//test delete this.
			editString();
			writeNewEditedFile();
		}
       
    }//end actionPerformed()
	
	public void readFile()
	{
		File file = fileChooser.getSelectedFile();
		//is this needed?
		currentFileName = file.getName();
		fileNameWithoutExt = currentFileName.substring(0,currentFileName.lastIndexOf("."));
        
        try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			while((lineForReading = reader.readLine()) != null)
			{
				fastaLines.add(lineForReading);
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
			bufferedWriter.write(fastaLines.get(0));
			bufferedWriter.flush();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public void createGui()
	{
		 //Create and set up the window.
        frame = new JFrame("FileChooserDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        buttonPanel.add(openButton);
        buttonPanel.add(displayLines);
	
        //Add content to the window.
        frame.add(new FastaReader());
        frame.getContentPane().add(buttonPanel);
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
	}//end createGui
	
	public void displayFastaLine()
	{
		for(int i = 0; i < fastaLines.size();i++)
		{
			//System.out.println(fastaLines.get(i));
		}
	}//end displayFastaLine()
	
	public String editString()
	{
		int digitCount = 0;
		int numberStartIndex = 3;
		int maxDigitsInNumber = 4;//ask becky how big that number can be, Just lookin at the file it looks like 4
		char c;
		int numOfZeros = 0;
		String zeros = "";
		String editedString = fastaLines.get(0);
		StringBuilder stringBuilder;
		
		
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
				System.out.print(editedString.charAt(i));
				
			}
			
			numOfZeros = maxDigitsInNumber - digitCount;
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
		}
		
		/*
		System.out.println("\n");
		System.out.println("Num of digits: " + digitCount);
		System.out.println("Num of zeros: " + numOfZeros);
		System.out.println("Zero String: " + zeros);
		*/
		stringBuilder = new StringBuilder(editedString);
		stringBuilder.insert(3, zeros);
		
		System.out.println(stringBuilder.toString());
		//System.out.println(fileNameWithoutExt);
		
		return " ";
	}
	
	public static void main(String[] args)
	{
		FastaReader fr = new FastaReader();
		fr.createGui();
		
		
	}

}
