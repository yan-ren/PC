package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class MatrixTask {
	public static final int THREADS = 4; // number of core threads used for
											// parallel multiplication
//	 static ExecutorService exec = Executors.newFixedThreadPool(THREADS);
	static ExecutorService exec1 = Executors.newCachedThreadPool();
	static ThreadPoolExecutor pool = (ThreadPoolExecutor) exec1;

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		pool.setCorePoolSize(THREADS);

		int dim = 2000;
		long startTime;
		long endTime;

		Matrix myA = new Matrix(dim);
		Vector myB = new Vector(dim);
		Vector myC = new Vector(dim);
		MulTask mul = new MulTask(myA, myB, myC);
		
		startTime = System.currentTimeMillis();
		Future<?> future = exec1.submit(mul);
		future.get();
		endTime = System.currentTimeMillis();

		exec1.shutdown();
		exec1.awaitTermination(1, TimeUnit.DAYS);

		System.out.println("time: " + (endTime - startTime));

		// test_vector_parallel_addition();
//		 test_matrix_vector_parallel_multiplication();

		System.out.println("Largest number of simultaneous executions: " + pool.getLargestPoolSize());
		System.out.println("Total number of threads ever scheduled: " + pool.getTaskCount());
	}

	// this test is for testing matrix vector parallel multiplication is working
	public static void test_matrix_vector_parallel_multiplication() throws InterruptedException, ExecutionException {
		double[][] a = { { 1, 2, 1, 0 }, { -3, 4, 6, 4 }, { 2, 4, 2, 5 }, { 1, 0, 1, 0 } };
		double[] b = { 1, 2, 3, 4 };
		int dim = 4;

		Matrix myA = new Matrix(dim);
		Vector myB = new Vector(dim);
		Vector myC = new Vector(dim);

		myA.data = a;
		myB.data = b;

		Future<?> future = exec1.submit(new MulTask(myA, myB, myC));
		future.get();
		exec1.shutdown();
		exec1.awaitTermination(1, TimeUnit.DAYS);

		System.out.println("matrix A: \n" + printMatrix(myA.data));
		System.out.println("vector B: \n" + printVector(myB.data));
		System.out.println("vector A*B: \n" + printVector(myC.data));

	}

	// this test is for testing vector parallel addition is working
	public static void test_vector_parallel_addition() throws InterruptedException, ExecutionException {
		int dim = 4;
		double[] a = { 1, 2, 3, 4 };
		double[] b = { 1, 2, 3, 4 };

		Vector myA = new Vector(dim);
		Vector myB = new Vector(dim);
		Vector myC = new Vector(dim);

		myA.data = a;
		myB.data = b;

		Future<?> future = exec1.submit(new AddTask(myA, myB, myC));
		future.get();
		exec1.shutdown();
		exec1.awaitTermination(1, TimeUnit.DAYS);

		System.out.println("vector A: \n" + printVector(myA.data));
		System.out.println("vector B: \n" + printVector(myB.data));
		System.out.println("vector A+B: \n" + printVector(myC.data));

	}

	static class AddTask implements Runnable {
		Vector a, b, c;
		Future<?>[] future = (Future<?>[]) new Future[2];
		Vector[] aa, bb, cc;
		
		public AddTask(Vector myA, Vector myB, Vector myC) {
			a = myA;
			b = myB;
			c = myC;
		}

		public void run() {
			try {
				int n = a.getDim();
				if (n == 1) {
					c.set(0, a.get(0) + b.get(0));
				} else {
					aa = a.split(); bb = b.split(); cc = c.split();
					for (int i = 0; i < 2; i++)
						future[i] = exec1.submit(new AddTask(aa[i], bb[i], cc[i]));
					for (int i = 0; i < 2; i++)
						future[i].get();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	static class MulTask implements Runnable {
		Matrix a;
		Vector b, c, lhs, rhs;
		Future<?>[][] future = (Future<?>[][]) new Future[2][2];
		Matrix[][] aa;
		Vector[] bb, ll, rr;

		public MulTask(Matrix myA, Vector myB, Vector myC) {
			a = myA;
			b = myB;
			c = myC;
			lhs = new Vector(a.getDim());
			rhs = new Vector(a.getDim());
		}

		public void run() {
			try {
				if (a.getDim() == 1) {
					c.set(0, a.get(0, 0) * b.get(0));
				} else {
					aa = a.split();
					bb = b.split();
					ll = lhs.split();
					rr = rhs.split();
					for (int i = 0; i < 2; i++) {
						future[i][0] = exec1.submit(new MulTask(aa[i][0], bb[0], ll[i]));
						future[i][1] = exec1.submit(new MulTask(aa[i][1], bb[1], rr[i]));
					}
					for (int i = 0; i < 2; i++)
						for (int k = 0; k < 2; k++)
							future[i][k].get();
					Future<?> done = exec1.submit(new AddTask(lhs, rhs, c));
					done.get();
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

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
