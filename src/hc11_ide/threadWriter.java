package hc11_ide;



import java.io.DataOutputStream;
import java.io.DataInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
/**
A thread that writes random numbers to an output stream.
 */
public class threadWriter extends Thread{

               
	private DataOutputStream out;
	private DataInputStream in;
	String process = "ThreadWriter";
	private Vector<Integer> v;
	private Vector<Integer> f;
	
	public threadWriter(InputStream is,OutputStream os,Vector<Integer> v)
	{
		out = new DataOutputStream(os);
		in = new DataInputStream(is);
		this.v=v;

	}

	public void run()
	{
		while (true)
		{
			try
			{

				//	  System.out.println(process);
				//synchronized (f){
					//f.wait();
					synchronized(v){ 	             
						//int rec_char= SerialConnection.filter_data;
						int rec_char=in.read();
						System.out.println("thread writer rec char is "+rec_char);
						v.notify();	
						out.writeChar(rec_char);
						out.flush();

						//v.clear();
						//v.clear();
						//v.addElement(rec_char);
							       
					}
					sleep(15); 
					System.out.println("ThreadWriter\n");
				}
			//}
			catch(Exception e)
			{
				System.out.println("Error: " + e);
			}
		}

	}
}