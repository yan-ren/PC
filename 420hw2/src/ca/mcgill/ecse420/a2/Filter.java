package ca.mcgill.ecse420.a2;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class Filter implements Lock {
	private AtomicInteger[] level;
	private AtomicInteger[] victim;

	int n;

	public Filter(int n) {
		this.n = n;
		level = new AtomicInteger[n];
		victim = new AtomicInteger[n]; // use 1..n-1
		for (int i = 0; i < n; i++) {
			level[i] = new AtomicInteger();
			victim[i] = new AtomicInteger();
		}
	}

	public void lock() {
		int me = ThreadID.get();
		for (int i = 1; i < n; i++) { // attempt level 1
			level[me].set(i);
			victim[i].set(me);
			// spin while conflicts exist
			boolean conflicts_exist = true;
			while (conflicts_exist) {
				conflicts_exist = false;
				for (int k = 0; k < n; k++) {
					if (k != me && level[k].get() >= i && victim[i].get() == me) {
						conflicts_exist = true;
						break;
					}
				}
			}
		}
	}

	public void unlock() {
		int me = ThreadID.get();
		level[me].set(0);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		// TODO Auto-generated method stub

	}

	@Override
	public Condition newCondition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean tryLock() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean tryLock(long arg0, TimeUnit arg1) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
}
