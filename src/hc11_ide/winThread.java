package hc11_ide;

import java.io.*;
import java.util.Vector;
import java.util.concurrent.Exchanger;

public class winThread extends Thread{
	private DataOutputStream out;
	String inp_str="help";
	String decoder_output_string;
	private Vector<Integer> f;
	private  MessageQue msg_que = new MessageQue(inp_str);
	public winThread(OutputStream out,MessageQue msg_que,Vector<Integer> f)
	{
		this.msg_que=msg_que;
		this.f=f;
		out = new DataOutputStream(out);
		Mywin myser = new Mywin(out,msg_que);    
		myser.setVisible(true); 
	}
	public void run() {
		for(;;){
			synchronized(msg_que){
				try {
					msg_que.wait();
					decoder_output_string=msg_que.get_msg();
					if(msg_que.get_msg_last_index() >= 10)
					{
						msg_que.delete_msg_first();
					}
					//sb.delete(0,sb.length());
					
				} catch (Exception e) {
					// TODO: handle exception
				}
				System.out.println("WinThread");
				Mywin.terminal.append( "hc11>"+ decoder_output_string + Mywin.newline);
                int len = Mywin.terminal.getDocument().getLength();
                Mywin.terminal.setCaretPosition(len);
				//sb.delete(0,sb.length());
				Mywin.put_bar_at_end(Mywin.terminal);
				
			}
		}
	}
}
