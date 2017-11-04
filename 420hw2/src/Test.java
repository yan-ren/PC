import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Lock;

public class Test {
	
	private static int threads = 80;
	private static Account account = new Account(threads);

	public static void main(String[] args) {
		ExecutorService executor = Executors.newCachedThreadPool();

		// Create and launch 100 threads
		for (int i = 0; i < threads; i++) {
			executor.execute(new AddAPennyTask());
		}

		executor.shutdown();

		// Wait until all tasks are finished
		while (!executor.isTerminated()) {
		}

		System.out.println("What is balance ? " + account.getBalance());
	}

	// A thread for adding a penny to the account
	public static class AddAPennyTask implements Runnable {
		public void run() {
			account.deposit(1);
		}
	}

	// An inner class for account
	public static class Account {
		private static Lock lock; // Create a lock
		private int balance = 0;

		public Account(int threads) {
			// uncomment one for testing different locks
			lock = new Bakery(threads);
//			lock = new Filter(threads);
		}
		
		public int getBalance() {
			return balance;
		}

		public void deposit(int amount) {
			lock.lock(); // Acquire the lock

			try {
				int newBalance = balance + amount;

				// This delay is deliberately added to magnify the
				// data-corruption problem and make it easy to see.
				Thread.sleep(5);

				balance = newBalance;
			} catch (InterruptedException ex) {
			} finally {
				lock.unlock(); // Release the lock
			}
		}
	}
}
