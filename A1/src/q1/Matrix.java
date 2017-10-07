package q1;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class Matrix {
	
	public static final int THREADS = 4;
	
	public static void main(String[] args) throws InterruptedException {

		int size = 2200;
		double[][] a = new double[size][size];
		double[][] b = new double[size][size];
		long startTime;
		long endTime;
		long totalTime;
		double[][] res;

//		 startTime= System.currentTimeMillis();
//		 res = sequentialMultiplyMatrix(a, b);
//		 endTime = System.currentTimeMillis();
//		 totalTime= endTime - startTime;
//		 System.out.println(totalTime);
//		 System.out.println("#########################");

		 startTime= System.currentTimeMillis();
		 res = parallelMultiplyMatrix(a, b);
		 endTime = System.currentTimeMillis();
		 totalTime= endTime - startTime;
//		 System.out.println(printMatrix(res));
		 System.out.println(totalTime);
		 
	}

	public static double[][] sequentialMultiplyMatrix(double[][] a, double[][] b) {
		int aColNum = a[0].length;
		int bRomNum = b.length;
		if (aColNum != bRomNum) // check if satisfy matrix multiply condition
			return null;
		int resultRowNum = a.length;
		int resultColNum = b[0].length;
		double[][] result = new double[resultRowNum][resultColNum];
		for (int i = 0; i < resultRowNum; i++) { // rows from m1
			for (int j = 0; j < resultColNum; j++) { // columns from m2
				result[i][j] += multiplyAndSumTwoArrays(a[i], getColumn(b, j));
			}
		}
		return result;
	}
	
	public static double[][] parallelMultiplyMatrix(double[][] a, double[][] b) throws InterruptedException{
		ExecutorService executorService = Executors.newFixedThreadPool(THREADS);
		int aColNum = a[0].length;
		int bRomNum = b.length;
		if (aColNum != bRomNum) // check if satisfy matrix multiply condition
			return null;
		int resultRowNum = a.length;
		int resultColNum = b[0].length;
		double[][] result = new double[resultRowNum][resultColNum];
		
		for (int i = 0; i < resultRowNum; i++) { // rows from m1
			for (int j = 0; j < resultColNum; j++) { // columns from m2
				final int row = i;
				final int col = j;
				
				executorService.submit(new Runnable() {
					public void run() {
//						System.out.println("running..");
						result[row][col] += multiplyAndSumTwoArrays(a[row], getColumn(b, col));
//						System.out.println("finished..");
					}
				});				
			}
		}
		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.DAYS);
		return result;
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
