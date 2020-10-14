package hc11_ide;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.TextField;
import java.awt.Button;
import java.awt.event.*;
import java.awt.FlowLayout;
//import javax.swing.filechooser.*;;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.*;
import java.io.File;



import java.awt.Point;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
//import java.awt.Frame;


import gnu.io.*;
import java.util.*;

public class Mywin extends javax.swing.JFrame 
implements KeyListener,ActionListener
{
	private static final int SIZE_X = 650;
	private static final int SIZE_Y = 500 ;

	private DataOutputStream os;
	JTextField commandField;

	public static Choice auswahl;

	//Button bt_read;
	public static JTextArea terminal,editor;
	public static String input;

	static String write_content; //content from the area
	static String write_content_2_send;
	static String read_content;
	static String decoder_output_string;

	public final static String newline = "\n";
	public static JScrollPane scrollPane;
	private JButton open = new JButton("Open"), save = new JButton("Save");
	private JTextField filename = new JTextField(), dir = new JTextField();
	private JEditorPane editorPane = new JEditorPane();
	private JEditorPane terminalPane = new JEditorPane();
	private static Vector <Integer>f ;
	private static String inp_str="help";
	private JFileChooser chooser = new JFileChooser();

	private  static MessageQue msg_que = new MessageQue(inp_str);
	public Mywin(OutputStream out, MessageQue msg_que){
		//geometry
		os = new DataOutputStream(out);	
		this.msg_que=msg_que;
		setLayout(new FlowLayout());
		setSize(SIZE_X*2,SIZE_Y);

		//                          WINDOWS components

		// set up menu bar
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu menu = new JMenu("File");
		menuBar.add(menu);

		JMenuItem openItem = new JMenuItem("Open");
		menu.add(openItem);
		openItem.addActionListener(new OpenL());

		JMenuItem LoadItem = new JMenuItem("Load");
		menu.add(LoadItem);
		LoadItem.addActionListener(new SendL());

		JMenuItem exitItem = new JMenuItem("Exit");
		menu.add(exitItem);
		exitItem.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent event)
			{
				System.exit(0);
			}
		});


		//                               PANES



		//Create Terminal pane 
		//Lay out the text controls .
		JPanel terminalControlsPane = new JPanel(new BorderLayout());
		JPanel editorControlsPane =   new JPanel(new BorderLayout());

		//Create a regular text field.

		commandField = new JTextField(50);
		//        commandField.setActionCommand(textFieldString);
		commandField.addKeyListener(this);

		//terminal output window
		terminal = new JTextArea("",15, 50 );
		editor   = new JTextArea("",15, 50 );

		editorPane.setEditable(true);
		terminalPane.setEditable(true);

		//made terminal listener
		terminal.addKeyListener(new java.awt.event.KeyAdapter() {
	        public void keyReleased(java.awt.event.KeyEvent e){
	            //save the last lines for console to variable input
	            if(e.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER){

	                try {
	                    int line = terminal.getLineCount() -2;
	                    int start = terminal.getLineStartOffset(line);
	                    int end = terminal.getLineEndOffset(line);
	                    input = terminal.getText(start, end  - start);
	                    write_functions();
	                } catch (Exception e1) {
	                    e1.printStackTrace();
	                }
	            }
	        }
	    });

		//Create an editor pane and editor pane .

		terminalControlsPane.add(commandField,BorderLayout.SOUTH);
		terminalControlsPane.add(terminal,BorderLayout.PAGE_START);	

		editorControlsPane.add(editor,BorderLayout.PAGE_START);


		//command window


		//sroll bar editor   
		JScrollPane editorScrollPane = new JScrollPane(editor);
		editorScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		editorScrollPane.setPreferredSize(new Dimension(SIZE_X,SIZE_Y-100));
		editorScrollPane.setMinimumSize(new Dimension(10, 10));
		//sroll bar terminal   
		JScrollPane terminalScrollPane = new JScrollPane(terminal);
		terminalScrollPane.setVerticalScrollBarPolicy(
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		terminalScrollPane.setPreferredSize(new Dimension(SIZE_X/2,SIZE_Y-100));
		terminalScrollPane.setMinimumSize(new Dimension(10, 10));
		scrollPane=terminalScrollPane;//for the put_bar_at_the_end
		//add scroll bars

		terminalControlsPane.add(terminalScrollPane); 
		editorControlsPane.add(editorScrollPane); 

		// Editor Pane
		JPanel rightPane = new JPanel(new BorderLayout());
		JPanel leftPane = new JPanel(new BorderLayout());



		//Put everything together.

		leftPane.add(terminalControlsPane, 
				BorderLayout.PAGE_START);
		rightPane.add(editorControlsPane, 
				BorderLayout.PAGE_START);

		add(leftPane,BorderLayout.WEST);
		add(rightPane,BorderLayout.EAST);		
		add(auswahl=new Choice() );
		

		//The  choice of com's implementation
		auswahl.add("COM1");
		auswahl.add("COM2");
		auswahl.add("COM3");
		auswahl.add("COM4");

		ItemListener auswahl_act = new ItemListener(){

			public void itemStateChanged(ItemEvent e) {
				// TODO Auto-generated method stub
				System.out.println(auswahl.getSelectedItem());

				SerialConnection connect = new  SerialConnection(os,Mywin.msg_que);

				if( auswahl.getSelectedItem().equals("COM1")){
					connect.closeConnection();
					try {
						connect.openConnection();
					} catch (SerialConnectionException e1) {
						// TODO Auto-generated catch block
						connect.closeConnection();
						e1.printStackTrace();

					} catch (PortInUseException e1) {
						// TODO Auto-generated catch block
						connect.closeConnection();
						e1.printStackTrace();

					}
				}
				if( auswahl.getSelectedItem().equals("COM2")){
					connect.closeConnection();
					try {
						connect.openConnection();
					} catch (SerialConnectionException e1) {
						// TODO Auto-generated catch block
						connect.closeConnection();
						e1.printStackTrace();

					} catch (PortInUseException e1) {
						// TODO Auto-generated catch block
						connect.closeConnection();
						e1.printStackTrace();

					}
				}
				if( auswahl.getSelectedItem().equals("COM3")){
					connect.closeConnection();
					try {
						connect.openConnection();
					} catch (SerialConnectionException e1) {
						// TODO Auto-generated catch block
						connect.closeConnection();
						e1.printStackTrace();

					} catch (PortInUseException e1) {
						// TODO Auto-generated catch block
						connect.closeConnection();
						e1.printStackTrace();

					}
				}
			}
		};  
		
		final ActionListener write_act = new ActionListener() { 	
			public void actionPerformed(final ActionEvent e){ 	
				write_functions();
				//write_content_2_send="^A";
			//	new Write();
			}
		};
		
		//final ActionListener read_act = new ActionListener() { 	
			//public void actionPerformed(final ActionEvent e){ 	
				//read_functions();
				//write_content_2_send="^A";
				//new Write();
			//}
		//};

		auswahl.addItemListener(auswahl_act);
		//exit assurance
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(final WindowEvent e){
				dispose();
				System.exit(0);
			}
		});

		//bt_read.addActionListener( read_act );
		//establish the serial event handler 
		//read class implementation
		//Read lese = new Read();

	}

	///////////////////////////////////////////////////////////////////////
	//Mywin end
	///////////////////////////////////////////////////////////////////////
	

	public void write_functions(){
		int length;
	//	write_content = commandField.getText();
		write_content = input;
		//		MessagePrepare.message_content=tfe.getText();
		//		new MessagePrepare();
		//		write_content=MessagePrepare.message_content_out;
		System.out.println("write to hc11 "+write_content);
		length = write_content.length();
		final String length_char=Integer.toString(length);
		write_content_2_send=write_content.substring(0,length-1)+"\r";

		new Write() ;


		//terminal.append( "me>"+ Mywin.write_content_2_send + Mywin.newline);
		put_bar_at_end(terminal);
		
	}	;

	//  public void read_functions(){
		  
		  //final Read lese = new Read();
		//  }	;  
		  

	//put the scrollbar allways at the actually end of text area
	public static void put_bar_at_end(JTextArea area)
	{
		final Point point = new Point( 0, (int)(area.getSize().getHeight()) );

		scrollPane.getViewport().setViewPosition( point );
	}
	
	
	public void keyPressed(final KeyEvent e) {
		final int code = e.getKeyCode();
		if(code == KeyEvent.VK_ESCAPE)
		{
			dispose();
			System.exit(0);
		}
		if(code == KeyEvent.VK_ENTER)
		{
			write_functions(); 
		}



	}

	public void keyReleased(final KeyEvent e) {
		// TODO Auto-generated method stub

	}

	public void keyTyped(final KeyEvent e) {
		//	System.out.println("lali");
		// TODO Auto-generated method stub

	}

	public void actionPerformed(final ActionEvent e) {
		// TODO Auto-generated method stub

	}
	class OpenL implements ActionListener {
		public void actionPerformed(ActionEvent e) {

			File file = chooser.getSelectedFile();

			// set up file chooser

			// accept all image files ending with .s19, .asm, .asc

			FileNameExtensionFilter filter = new FileNameExtensionFilter("file types s19,asm,as11,asc", "s19", "asm", "as11","asc");
			chooser.setFileFilter(filter);

			//      chooser.setAccessory(new ImagePreviewer(chooser));

			int rVal = chooser.showOpenDialog(Mywin.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				filename.setText(chooser.getSelectedFile().getAbsolutePath());
				dir.setText(chooser.getCurrentDirectory().toString());
				loadFile(new File(filename.getText()));
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				filename.setText("You pressed cancel");
				dir.setText("");
			}


		}
	}
	class SendL implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// JFileChooser c = new JFileChooser();

			// Demonstrate "Save" dialog:
			int rVal = chooser.showSaveDialog(Mywin.this);
			if (rVal == JFileChooser.APPROVE_OPTION) {
				filename.setText(chooser.getSelectedFile().getAbsolutePath());
				dir.setText(chooser.getCurrentDirectory().toString());
				sendFile(new File(filename.getText()));
			}
			if (rVal == JFileChooser.CANCEL_OPTION) {
				filename.setText("You pressed cancel");
				dir.setText("");
			}
		}
	}
	/**
	 * Loads a file's contents into the text area, or displays an error message
	 * if the file does not exist.
	 *
	 * @param file The file to load.
	 */
	public void loadFile(File file) {

		System.out.println("DEBUG: " + file.getAbsolutePath());
		//	      System.out.println("path is" + file.getCanonicalPath());
		if (file.isDirectory()) { // Clicking on a space character
			JOptionPane.showMessageDialog(this,
					file.getAbsolutePath() + " is a directory",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		else if (!file.isFile()) {
			JOptionPane.showMessageDialog(this,
					"No such file: " + file.getAbsolutePath(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {

			BufferedReader r = new BufferedReader(new FileReader(file.getAbsoluteFile()));
			editor.read(r, null);
			r.close();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			UIManager.getLookAndFeel().provideErrorFeedback(editor);
		}

	}


	/**
	 * Sends the file's contents to the hc11 board via rs232, or displays an error message
	 * if the file does not exist.
	 *
	 * @param file The file to load.
	 */
	public void sendFile(File file) {

		System.out.println("DEBUG: " + file.getAbsolutePath());
		//	      System.out.println("path is" + file.getCanonicalPath());
		if (file.isDirectory()) { // Clicking on a space character
			JOptionPane.showMessageDialog(this,
					file.getAbsolutePath() + " is a directory",
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}
		else if (!file.isFile()) {
			JOptionPane.showMessageDialog(this,
					"No such file: " + file.getAbsolutePath(),
					"Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		try {
            JTextField s11_record = new JTextField(); 
			BufferedReader r = new BufferedReader(new FileReader(file.getAbsoluteFile()));
			s11_record.read(r, null);
			write_content_2_send=s11_record.getText();			
			new Write();
			r.close();
			terminal.append( "me>"+ Mywin.write_content_2_send + Mywin.newline);
			put_bar_at_end(terminal);
		} catch (IOException ioe) {
			ioe.printStackTrace();
			UIManager.getLookAndFeel().provideErrorFeedback(terminal);
		}

	}

}


