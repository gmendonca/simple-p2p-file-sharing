package util;

import java.util.LinkedList;

public class PeerQueue<T> {
	
	private LinkedList<T> queue;
	
	public T peek(){
		return queue.peek();
	}
	
	public T poll(){
		return queue.poll();
	}
	
	public void add(T t){
		queue.add(t);
		
	}
	
	public PeerQueue(){
		queue = new LinkedList<T>();
	}

}
