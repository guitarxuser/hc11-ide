package hc11_ide;
import java.io.*;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.Vector;
import java.util.concurrent.*;
import java.awt.TextArea;
import java.util.regex.*;

import javax.swing.text.*;

import gnu.io.*;

//s.kalus extends Thread
public class SerialConnection  extends Thread implements SerialPortEventListener, 
CommPortOwnershipListener {

	private SerialParameters parameters;
	static OutputStream os;
	static InputStream is;
	private Enumeration portList;
	private CommPortIdentifier portId;
	private SerialPort sPort;
	private boolean open;

	//s.kalus changes
	static int filter_data,filter_data2;
	static int n;
	private Vector<Integer> f ;//vector to store sync variables
	private OutputStream pi_ou;
	BufferedInputStream in = new BufferedInputStream(is );
	private Exchanger <StringBuffer> currentBuffer;
	private StringBuffer InputBuffer;
	private String inp_str="lilo" ;
	private MessageQue msq_que = new MessageQue(inp_str);
	
	public SerialConnection(OutputStream pi_ou ,MessageQue msq_que) {
		this.pi_ou=pi_ou;
		this.msq_que=msq_que;
	}
	
	public void openConnection() throws SerialConnectionException, PortInUseException {

		// Obtain a CommPortIdentifier object for the port you want to open.


		parameters = new SerialParameters();
		portList = CommPortIdentifier.getPortIdentifiers();


		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {

				//	if (portId.getName().equals("COM1")) {
				//if (portId.getName().equals(Mywin.auswahl.getSelectedItem())) {
				if (portId.getName().isEmpty())
				{
					break;
				}
				else{
					System.out.println("portId is"+portId.getName());
					//    if (portId.getName().equals("/dev/term/a")) {
					try {
						sPort = (SerialPort)
						portId.open("SimpleWriteApp", 2000);
					} catch (PortInUseException e) {
						throw new SerialConnectionException("error on connect");
					}
				}
			}
		}
		// Open the port represented by the CommPortIdentifier object. Give
		// the open call a relatively long timeout of 30 seconds to allow
		// a different application to reliquish the port if the user 
		// wants to.


		//originalo
		//sPort = (SerialPort) portId.open("SerialDemo", 2000);



		// Set the parameters of the connection. If they won't set, close the
		// port before throwing an exception.
		try {
			setConnectionParameters();
		} catch (SerialConnectionException e) {	
			// sPort.close();
			throw e;
		}

		// Open the input and output streams for the connection. If they won't
		// open, close the port before throwing an exception.
		try {
			os = sPort.getOutputStream();
			is = sPort.getInputStream();
		} catch (IOException e) {
			sPort.close();
			throw new SerialConnectionException("Error opening i/o streams");
		}


		// Add this object as an event listener for the serial port.
		try {
			sPort.addEventListener(this);
		} catch (TooManyListenersException e) {
			sPort.close();
			throw new SerialConnectionException("too many listeners added");
		}

		// Set notifyOnDataAvailable to true to allow event driven input.
		sPort.notifyOnDataAvailable(true);

		// Set notifyOnBreakInterrup to allow event driven break handling.
		sPort.notifyOnBreakInterrupt(true);

		// Set receive timeout to allow breaking out of polling loop during
		// input handling.
		try {
			sPort.enableReceiveTimeout(500);
		} catch (UnsupportedCommOperationException e) {
		}

		// Add ownership listener to allow ownership event handling.
		portId.addPortOwnershipListener(this);

		open = true;
	}

	/**
       Sets the connection parameters to the setting in the parameters object.
       If set fails return the parameters object to origional settings and
       throw exception.
	 */
	public void setConnectionParameters() throws SerialConnectionException {

		// Save state of parameters before trying a set.
		int oldBaudRate ;
		int oldDatabits ;
		int oldStopbits ;
		int oldParity   ;
		int oldFlowControl ;
		try{
			oldBaudRate = sPort.getBaudRate();
			oldDatabits = sPort.getDataBits();
			oldStopbits = sPort.getStopBits();
			oldParity   = sPort.getParity();
			oldFlowControl = sPort.getFlowControlMode();
		}catch ( Exception e1) {
			throw new SerialConnectionException("Unsupported parameter");  
		}

		// Set connection parameters, if set fails return parameters object
		// to original state.
		try {
			sPort.setSerialPortParams(parameters.getBaudRate(),
					parameters.getDatabits(),
					parameters.getStopbits(),
					parameters.getParity());
		} catch (UnsupportedCommOperationException e) {
			parameters.setBaudRate(oldBaudRate);
			parameters.setDatabits(oldDatabits);
			parameters.setStopbits(oldStopbits);
			parameters.setParity(oldParity);
			throw new SerialConnectionException("Unsupported parameter");
		}
		// Set flow control.
		try {
			sPort.setFlowControlMode(parameters.getFlowControlIn() 
					| parameters.getFlowControlOut());
		} catch (UnsupportedCommOperationException e) {
			throw new SerialConnectionException("Unsupported flow control");
		}
	}

	/**
       Close the port and clean up associated elements.
	 */
	public void closeConnection() {
		// If port is alread closed just return.
		if (!open) {
			return;
		}


		// Check to make sure sPort has reference to avoid a NPE.
		if (sPort != null) {
			try {
				// close the i/o streams.
				os.close();
				is.close();
			} catch (IOException e) {
				System.err.println(e);
			}

			// Close the port.
			sPort.close();

			// Remove the ownership listener.
			portId.removePortOwnershipListener(this);
		}

		open = false;
	}

	/**
       Send a one second break signal.
	 */
	public void sendBreak() {
		sPort.sendBreak(1000);
	}

	/**
       Reports the open status of the port.
       @return true if port is open, false if port is closed.
	 */
	public boolean isOpen() {
		return open;
	}

	/**
       Handles SerialPortEvents. The two types of SerialPortEvents that this
       program is registered to listen for are DATA_AVAILABLE and BI. During 
       DATA_AVAILABLE the port buffer is read until it is drained, when no more
       data is availble and 30ms has passed the method returns. When a BI
       event occurs the words BREAK RECEIVED are written to the messageAreaIn.
	 */

	public void serialEvent(SerialPortEvent e) {
		// Create a StringBuffer and int to receive input data.
		StringBuffer inputBuffer = new StringBuffer();
		 byte [] input_byte_array = new byte[1000];

		int newData = 0;
		//this.start();
		// Determine type of event.
		switch (e.getEventType()) {

		// Read data until -1 is returned. If \r is received substitute
		// \n for correct newline handling.
		case SerialPortEvent.DATA_AVAILABLE:
            n=0;
            int writer_count=0;
			while (newData != -1) {
				try {
					n=n+1;
                    System.out.println("n received ist "+n);
					//    System.out.println("connecti"+n);

					BufferedInputStream in = new BufferedInputStream(is );
					
					newData = is.read();
					filter_data=newData;
					System.out.println("connection "+(char)filter_data);
					inputBuffer.append((char)filter_data);
					

				} 

				catch (IOException ex) {
					System.err.println(ex);
					return;
				}

			}
			System.out.println("inputBuffer is "+inputBuffer);
			inp_str=(inputBuffer.toString());
			synchronized(msq_que){
				msq_que.add_msg(inp_str);
				msq_que.notify();
				if(msq_que.get_msg_last_index() >= 10)
				{
					msq_que.delete_msg_first();
				}
				//sb.delete(0,sb.length());

			} 
			/*synchronized(f){
				
				// this.start();
				int  buf_length=inputBuffer.length();
				System.out.println("buf_length is "+buf_length);
				while(buf_length>=0){ 
					try {
						f.notify();
						pi_ou.write(inputBuffer.charAt(buf_length));
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					buf_length--;
				}*/
				//				writer_count++;
				//				System.out.println("writer_count is "+writer_count);
			//}	
			
			
			break;


			// If break event append BREAK RECEIVED message.
		case SerialPortEvent.BI:
			//messageAreaIn.append("\n--- BREAK RECEIVED ---\n");
		}

	}   

	/**
       Handles ownership events. If a PORT_OWNERSHIP_REQUESTED event is
       received a dialog box is created asking the user if they are 
       willing to give up the port. No action is taken on other types
       of ownership events.
	 */
	public void ownershipChange(int type) {
		if (type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED) {
			//PortRequestedDialog prd = new PortRequestedDialog(parent);
		}
	}

	/**
       A class to handle <code>KeyEvent</code>s generated by the messageAreaOut.
       When a <code>KeyEvent</code> occurs the <code>char</code> that is 
       generated by the event is read, converted to an <code>int</code> and 
       writen to the <code>OutputStream</code> for the port.
	 */
	//class KeyHandler extends KeyAdapter {
	//OutputStream os;

	/**
       Creates the KeyHandler.
       @param os The OutputStream for the port.
	 */
	//public KeyHandler(OutputStream os) {
	//super();
	//this.os = os;
	//}
	public void run()
	{
		while (true) {


			try {    
				// synchronized (v) {
				// v.wait();
				System.out.println("seria_con_run: "+ (char)filter_data);
				// pi_ou.write(filter_data);
				// v.clear();
				// }
				// sleep(100);
			}
			catch (Exception e) {
				System.out.println("Error: " + e);
			}

		}
	}
}




