package hc11_ide;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Vector;


public class hc11_main {

	/**
	 * @param args
	 */

	public static void main(String[] args) {


		Vector <Integer>v = new Vector<Integer>();
		Vector <Integer>k = new Vector<Integer>();
		Vector <Integer>f = new Vector<Integer>();
		String inp_str="help\r";
		MessageQue msg_que = new MessageQue(inp_str);
		


		//winThread -->threadWriter-->Filter
		try
		{
             
			/* set up pipes */
			PipedOutputStream pout0 = new PipedOutputStream();
			PipedInputStream pin0 = new PipedInputStream(pout0);	

			PipedOutputStream pout1 = new PipedOutputStream();
			PipedInputStream pin1 = new PipedInputStream(pout1);

			PipedOutputStream pout2 = new PipedOutputStream();
			PipedInputStream pin2 = new PipedInputStream(pout2);

			PipedOutputStream pout3 = new PipedOutputStream();
			PipedInputStream pin3 = new PipedInputStream(pout3);

			/* construct threads */

			threadWriter prod = new threadWriter(pin0,pout1,v);
			Read cons = new Read(pin2,pout0,f);
			winThread win =new winThread(pout2,msg_que,f);
			/* start threads */
			prod.start();
			cons.start();
			win.start();

		}
		catch (IOException e){}
	}


}
