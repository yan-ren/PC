package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;

public class BoundedLockFreeQueue<T> {
	private T[] items;

	private AtomicInteger head = new AtomicInteger(0);
	private AtomicInteger tail = new AtomicInteger(0);
	private AtomicInteger tailCommit = new AtomicInteger(0);

	@SuppressWarnings("unchecked")
	public BoundedLockFreeQueue(int capacity) {
		items = (T[]) new Object[capacity];
	}

	public void enq(T item) throws InterruptedException {
		int currentWriteIndex;
		int currentReadIndex;
		
		do {
			currentWriteIndex = tail.get();
			currentReadIndex = head.get();

			// check queue is full
			if (currentWriteIndex - currentReadIndex == items.length) {
				return;
			}
		} while (!tail.compareAndSet(currentWriteIndex, currentWriteIndex + 1));
		
		// index is reserved for us. Use it to save the data
		items[currentWriteIndex % items.length] = item;
		
		//
		while (!tailCommit.compareAndSet(currentWriteIndex, currentWriteIndex + 1)) { };
	}

	public T deq() throws InterruptedException {
		int currentReadIndex;
		int currentReadMaxIndex;
		T item = null;
		
		do {
			currentReadIndex = head.get();
			currentReadMaxIndex = tailCommit.get();
			// the queue is empty
			if(currentReadMaxIndex - currentReadIndex == 0) {
				break;
			}
			
			//retrieve the data from the queue
			item = items[currentReadIndex % items.length];
			
			// try to perform now the CAS operation on the head index. 
			if(head.compareAndSet(currentReadIndex, currentReadIndex+1)) {
				break;
			}
			
		}while(true);
		
		return item;

	}
}