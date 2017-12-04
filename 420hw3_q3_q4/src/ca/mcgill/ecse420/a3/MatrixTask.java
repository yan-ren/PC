package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MatrixTask {
	public static final int THREADS = 8; // number of threads used for parallel
											// multiplication
	static ExecutorService exec = Executors.newCachedThreadPool();
	static ThreadPoolExecutor pool = (ThreadPoolExecutor) exec;

	// static ExecutorService exec = Executors.newFixedThreadPool(THREADS);
	// static Matrix add(Matrix a, Matrix b) throws ExecutionException,
	// InterruptedException {
	// int n = a.getDim();
	// Matrix c = new Matrix(n);
	// Future<?> future = exec.submit(new AddTask(a, b, c));
	// future.get();
	// return c;
	// }

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		// pool.setCorePoolSize(4);
		//
		// int dim = 2000;
		// long startTime;
		// long endTime;
		//
		// Vector myA = new Vector(dim);
		// Vector myB = new Vector(dim);
		// Vector myC = new Vector(dim);
		//
		// startTime = System.currentTimeMillis();
		// Future<?> future = exec.submit(new AddTask(myA, myB, myC));
		// future.get();
		// exec.shutdown();
		// exec.awaitTermination(1, TimeUnit.DAYS);
		//
		// endTime = System.currentTimeMillis();
		//
		// System.out.println("Largest number of simultaneous executions: " +
		// pool.getLargestPoolSize());
		// System.out.println("Total number of threads ever scheduled: " +
		// pool.getTaskCount());
		// System.out.println("time: " + (endTime - startTime));

		test_vector_parallel_addition();
	}

	// this test is for testing vector parallel addition
	public static void test_vector_parallel_addition() throws InterruptedException, ExecutionException {
		int dim = 4;
		double[] a = { 1, 2, 3, 4 };
		double[] b = { 1, 2, 3, 4 };

		Vector myA = new Vector(dim);
		Vector myB = new Vector(dim);
		Vector myC = new Vector(dim);

		myA.data = a;
		myB.data = b;

		Future<?> future = exec.submit(new AddTask(myA, myB, myC));
		future.get();
		exec.shutdown();
		exec.awaitTermination(1, TimeUnit.DAYS);

		System.out.println("vector A: \n" + printVector(myA.data));
		System.out.println("vector B: \n" + printVector(myB.data));
		System.out.println("vector A+B: \n" + printVector(myC.data));

	}

	static class AddTask implements Runnable {
		Vector a, b, c;

		public AddTask(Vector myA, Vector myB, Vector myC) {
			a = myA;
			b = myB;
			c = myC;
		}

		public void run() {
			try {
				int n = a.getDim();
				if (n == 1) {
					// System.out.println(a.get(0));
					// System.out.println(b.get(0));
					c.set(0, a.get(0) + b.get(0));
				} else {
					Vector[] aa = a.split(), bb = b.split(), cc = c.split();
					Future<?>[] future = (Future<?>[]) new Future[2];
					for (int i = 0; i < 2; i++)
						future[i] = exec.submit(new AddTask(aa[i], bb[i], cc[i]));
					for (int i = 0; i < 2; i++)
						future[i].get();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	// static class MulTask implements Runnable {
	// Matrix a, b, c, lhs, rhs;
	//
	// public MulTask(Matrix myA, Matrix myB, Matrix myC) {
	// a = myA;
	// b = myB;
	// c = myC;
	// lhs = new Matrix(a.getDim());
	// rhs = new Matrix(a.getDim());
	// }
	//
	// public void run() {
	// try {
	// if (a.getDim() == 1) {
	// c.set(0, 0, a.get(0, 0) * b.get(0, 0));
	// } else {
	// Matrix[][] aa = a.split(), bb = b.split(), cc = c.split();
	// Matrix[][] ll = lhs.split(), rr = rhs.split();
	// Future<?>[][][] future = (Future<?>[][][]) new Future[2][2][2];
	// for (int i = 0; i < 2; i++)
	// for (int j = 0; j < 2; j++) {
	// future[i][j][0] = exec.submit(new MulTask(aa[i][0], bb[0][i], ll[i][j]));
	// future[i][j][1] = exec.submit(new MulTask(aa[1][i], bb[i][1], rr[i][j]));
	// }
	// for (int i = 0; i < 2; i++)
	// for (int j = 0; j < 2; j++)
	// for (int k = 0; k < 2; k++)
	// future[i][j][k].get();
	// Future<?> done = exec.submit(new AddTask(lhs, rhs, c));
	// done.get();
	// }
	// } catch (Exception ex) {
	// ex.printStackTrace();
	// }
	// }
	// }

	public static String printVector(double[] m) {
		String result = "";
		for (int i = 0; i < m.length; i++) {

			result += String.format("%11.2f", m[i]);

			result += "\n";
		}
		return result;
	}

	public static String printMatrix(double[][] m) {
		String result = "";
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				result += String.format("%11.2f", m[i][j]);
			}
			result += "\n";
		}
		return result;
	}
}
