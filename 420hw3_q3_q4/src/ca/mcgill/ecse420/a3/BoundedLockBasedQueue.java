package ca.mcgill.ecse420.a3;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BoundedLockBasedQueue<T> {
	private T[] items;

	private int head = 0;
	private int tail = 0;

	AtomicInteger size;

	private Lock headLock = new ReentrantLock();
	private Lock tailLock = new ReentrantLock();

	private Condition notEmpty = headLock.newCondition();
	private Condition notFull = tailLock.newCondition();

	@SuppressWarnings("unchecked")
	public BoundedLockBasedQueue(int capacity) {
		items = (T[]) new Object[capacity];
		size = new AtomicInteger(0);
	}

	public void enq(T item) {
		boolean mustWakeDequeuers = false;
		tailLock.lock();
		try {
			// check if queue is full
			while (size.get() == items.length) {
				notFull.await();
			}
			// save into queue
			items[tail % items.length] = item;
			tail++;
			// if queue was empty, signal notEmpty condition just in case
			// some threads are waiting on the condition
			if (size.getAndIncrement() == 0)
				mustWakeDequeuers = true;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			tailLock.unlock();
		}

		if (mustWakeDequeuers) {
			headLock.lock();
			try {
				notEmpty.signalAll();
			} finally {
				headLock.unlock();
			}
		}
	}

	public T deq() {
		boolean mustWakeEnqueuers = false;
		T result = null;
		headLock.lock();
		try {
			// check if queue is empty
			while (size.get() == 0) {
				notEmpty.await();
			}
			// remove from queue
			result = items[head % items.length];
			head++;
			// if queue was full, signal notFull condition just in case
			// some enq threads are waiting on the condition
			if (size.getAndDecrement() == items.length) {
				mustWakeEnqueuers = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			headLock.unlock();
		}
		if (mustWakeEnqueuers) {
			tailLock.lock();
			try {
				notFull.signalAll();
			} finally {
				tailLock.unlock();
			}
		}
		return result;

	}

}
