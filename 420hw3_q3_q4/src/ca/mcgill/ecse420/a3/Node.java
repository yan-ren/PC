package ca.mcgill.ecse420.a3;

public class Node {

	public Node next;
	public int key;

	public Node(int key) {
		this.key = key;
	}

	public <T> Node(T item) {
		this.key = item.hashCode();
		next = null;
		// TODO Auto-generated constructor stub
	}

	public void lock() {
		// TODO Auto-generated method stub
		
	}

	public void unlock() {
		// TODO Auto-generated method stub
		
	}
}
