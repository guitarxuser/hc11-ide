package hc11_ide;
import java.io.*;
import java.util.LinkedList;
import java.util.Vector;
/**
A thread that reads numbers from a stream and
prints out those that deviate from previous inputs
by a threshold value.
 */



public class Read extends Thread implements Runnable{
	InputStream pi = null;
	OutputStream po = null;    
	String process = "Read";
	private String inp_str;
	private int char_rec;
	private StringBuffer sb = new StringBuffer();
	private Vector<Integer> f;
	char char_arr[];
	
	public Read( InputStream pi,OutputStream po,Vector <Integer> f) {
		this.pi = pi;    this.po = po;    this.process = process;
		this.f=f;
	}

	public void run() {
		byte[] buffer = new byte[512];    int bytes_read;

		for(;;) {
			try { 
				//System.out.println(process);
				synchronized(f){
					f.wait();	
					char_rec=pi.read();
					//sb.append((char)char_rec);
					//sb.getChars(0, sb.length(), char_arr, char_arr.length);
					//sleep(10);
					
				//	inp_str=(sb.toString());


					//po.write(sb.length());
					po.write((char)char_rec);
					//po.flush();
					f.clear();
					System.out.println("ComRead "+(char)char_rec);
					//po.write(sb.length());
					//sb.delete(0, sb.length());
					//}
				}
			}	
			catch (Exception e) {  e.printStackTrace(); 

			}
		}
	}
}


