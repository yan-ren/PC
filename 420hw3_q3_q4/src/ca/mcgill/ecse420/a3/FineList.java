package ca.mcgill.ecse420.a3;

public class FineList<T> {
	private Node head;

	public FineList() {
		head = new Node(Integer.MIN_VALUE);
		head.next = new Node(Integer.MAX_VALUE);
	}
	
	public boolean add(T item) {
		int key = item.hashCode();
		head.lock();
		Node pred = head;
		try {
			Node curr = pred.next;
			curr.lock();
			try {
				while (curr.key < key) {
					pred.unlock();
					pred = curr;
					curr = curr.next;
					curr.lock();
				}
				if (curr.key == key) {
					return false;
				}
				Node newNode = new Node(item);
				newNode.next = curr;
				pred.next = newNode;
				return true;
			} finally {
				curr.unlock();
			}
		} finally {
			
			pred.unlock();
		}
	}

	public boolean contains(T item) {
		Node pred = null;
		Node curr = null;
	    int key = item.hashCode();
	    head.lock();
	    try {
	    	pred = head;
	        curr = pred.next;
	        curr.lock();
	        try {
	            while (curr.key < key) {
	                pred.unlock();
	                pred = curr;
	                curr = curr.next;
	                curr.lock();
	            }
	            return (curr.key == key);
	        } finally {
	            curr.unlock();
	        }
	    } finally {
	        pred.unlock();
	    }
	}

}



	