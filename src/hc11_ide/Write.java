package hc11_ide;

import gnu.io.*;
import java.io.*;
import java.util.*;
public class Write implements Runnable{
	//Mywin mywrite_texr = new Mywin();	      
	    private Enumeration portList;
	    private CommPortIdentifier portId;

//Mywin.content is the string put by main window
	    private String messageString =Mywin.write_content_2_send;
	    private SerialPort serialPort_write;
	    OutputStream outputStream = SerialConnection.os;
Thread writeThread;

 public Write() {
 
	                    try {
	                    	System.out.println("message to send "+ messageString);
	                        outputStream.write(messageString.getBytes());  
	                    	
	                    } catch (IOException e) {
	                    	e.printStackTrace();
	                    }
 	                      writeThread = new Thread(this);
                          writeThread.start();               
	                }	

public void run() {
        try {
           Thread.sleep(20000);
        } catch (InterruptedException e) {}
    }
}
