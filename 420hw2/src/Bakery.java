import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

class Bakery implements Lock {
	private int n;
	private volatile boolean[] flag;
	private volatile int[] label;

	public Bakery(int n) {
		this.n = n;
		flag = new boolean[n];
		label = new int[n];
		for (int j = 0; j < n; j++) {
			flag[j] = false;
			label[j] = 0;
		}
	}

	public void lock() {
		int i = ThreadID.get();
		flag[i] = true;
		for (int j = 0; j < n; j++) {
			if (label[j] > label[i]) {
				label[i] = label[j];
			}
		}
		label[i]++;

		for (int j = 0; j < n; j++) {
			while (flag[j] && (j != i) && (label[j] < label[i]) || ((label[j] == label[i]) && j < i)) {};
		}
	}

	public void unlock() { // exit protocol
		flag[ThreadID.get()] = false;
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
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return false;
	}
}