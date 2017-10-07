package ca.mcgill.ecse420.a1;
public class Deadlock {
	public static String o1 = "o1";
	public static String o2 = "o2";

	public static void main(String[] args) {
		Thread a = new Thread(new lock1());
		Thread b = new Thread(new lock2());
		a.start();
		b.start();
	}
}

class lock1 implements Runnable {
	@Override
	public void run() {
		try {
			System.out.println("lock1 is running");
			while (true) {
				synchronized (Deadlock.o1) {
					System.out.println("lock1 has o1");
					Thread.sleep(500);
					synchronized (Deadlock.o2) {
						System.out.println("lock1 has o2");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

class lock2 implements Runnable {
	@Override
	public void run() {
		try {
			System.out.println("lock2 is running");
			while (true) {
				synchronized (Deadlock.o2) {
					System.out.println("lock2 has o2");
					Thread.sleep(500);
					synchronized (Deadlock.o1) {
						System.out.println("lock2 has o1");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}