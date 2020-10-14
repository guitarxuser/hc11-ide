package hc11_ide;


import java.util.LinkedList;

public class MessageQue {
	
	private String msg;
	private String inp_str;

	private LinkedList<String> queue = new LinkedList<String>();

	public MessageQue(String msg) {
		this.msg=msg;	
		// TODO Auto-generated constructor stub
	}
	public void add_msg(String msg){
		this.msg=msg;
		queue.add(msg);
		System.out.println("added message  :"+queue.getLast()+" "+queue.lastIndexOf(queue.getLast()));

	}
	public void delete_msg_last()
	{
		queue.removeLast();	
	}
	public void delete_msg_first()
	{
		queue.removeFirst();

	}
	public int get_msg_last_index()
	{
		return queue.lastIndexOf(queue.getLast());
	}
	public String get_msg(){
		String out_str="";
		if(!queue.isEmpty())
		{ 
			out_str=queue.getLast();
			System.out.println("got message  : "+queue.getLast()+" "+queue.indexOf(queue.getLast()));

		}
		return out_str;
	}
}
