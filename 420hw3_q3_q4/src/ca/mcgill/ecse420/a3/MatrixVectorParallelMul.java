package ca.mcgill.ecse420.a3;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class MatrixVectorParallelMul {

	public static final int THREADS = 4;// number of threads used for parallel multiplication

	public static void main(String[] args) throws InterruptedException {

		// int size = 2000; /*
		// * This is for define the size of two matrix for multiplication, in this
		// * question we assume matrix are square matrix and with all 0s
		// */
		// double[][] a = new double[size][size];
		// double[][] b = new double[size][1];// Two matrix for multiplication
		// long startTime;
		// long endTime;
		// long totalTime;
		// double[][] res;

		// startTime = System.currentTimeMillis(); // Save the time stamp before
		// multiplication starts
		// res = sequentialMultiplyMatrix(a, b);
		// endTime = System.currentTimeMillis(); // Save the time stamp when
		// multiplication finishes
		// totalTime = endTime - startTime; // Subtract for total runtime
		// System.out.println("Sequential multiplication runtime: " + totalTime);
		//
		// startTime = System.currentTimeMillis();
		// res = parallelMultiplyMatrix(a, b);
		// endTime = System.currentTimeMillis();
		// totalTime = endTime - startTime;
		// System.out.println("Parallel multiplication runtime: " + totalTime);

		double a[][] = { { 1, 2, -2 }, { -3, 4, 7 }, { 6, 0, 3 } };
		double b[][] = { { -1 }, { 0 }, { 1 } };
		System.out.println(printMatrix(a));
		System.out.println(printMatrix(b));

		
		
	}

	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) throws InterruptedException {
		return null;
	}

	public static double multiplyAndSumTwoArrays(double[] a, double[] b) {
		double sum = 0.0;
		for (int i = 0; i < a.length; i++) {
			sum = sum + a[i] * b[i];
		}
		return sum;
	}

	public static double[] getColumn(double[][] matrix, int column) {
		return IntStream.range(0, matrix.length).mapToDouble(i -> matrix[i][column]).toArray();
	}

	public static String printArray(double[] m) {
		String result = "";
		for (int i = 0; i < m.length; i++) {
			result += String.format("%11.2f", m[i]);
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
