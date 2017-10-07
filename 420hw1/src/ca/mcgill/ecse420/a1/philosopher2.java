package ca.mcgill.ecse420.a1;
public class philosopher2 {
 
    public static void main(String[] args) throws Exception {
 
        PhilosopherND[] philosophers = new PhilosopherND[5];
        Object[] chopsticks = new Object[5];
 
        for (int i=0; i<5; i++) {
        	chopsticks[i] = new Object();
        }
 
//        ExecutorService executor = Executors.newFixedThreadPool(4);
        
//        for (int i=0; i<5; i++) {
//            Object left = chopsticks[i];
//            Object right = chopsticks[(i+1) % 5];
// 
//            philosophers[i] = new PhilosopherND(left, right);
//            
//            
//
//            // Submit runnable tasks to the executor
////            executor.execute(new PrintChar('b', 100));
////            executor.execute(new PrintNum(100));
//            Thread t = new Thread(philosophers[i]);
//            t.setName("Philosopher " + (i+1));
////            t.start();
//            executor.execute(t);
//        }


        
        for (int i=0; i<5; i++) {
            Object first = chopsticks[Math.min(i, ((i+1)%5))];
            Object second = chopsticks[Math.max(i, ((i+1)%5))];            
            /* 
            four philosophers will pick up the left chopstick first,       
            the last philosopher will pick up the right chopstick first
            */
            
            
//            if (i==4) {              
//                philosophers[i] = new PhilosopherND(second, first); 
//            } else {
            	philosophers[i] = new PhilosopherND(first, second);
//            	}
              
            Thread t = new Thread(philosophers[i], "Philosopher " + (i+1));
            t.start();
        }
    }
}

class PhilosopherND implements Runnable {
    private Object first;
    private Object second;
 
    public PhilosopherND(Object first, Object second) {
        this.first = first;
        this.second = second;
    }
    
    @Override
    public void run() {
    	try {
            while (true) {

                // thinking
	            System.out.println(Thread.currentThread().getName() + ": thinking");
	           	Thread.sleep(1000);
	            synchronized (first) {
	            	System.out.println(Thread.currentThread().getName() + ": pick up left chopstick ");
	                Thread.sleep(1000);
	                synchronized (second) {
	                        // eating
	                    System.out.println(Thread.currentThread().getName() + ": pick up right chopstick, eating");
	                    Thread.sleep(1000);     
	                    	
	                    System.out.println(Thread.currentThread().getName() + ": put down right chopstick ");
	                    Thread.sleep(1000); 
	                }
	                     
	                    // finish eating
	                System.out.println(Thread.currentThread().getName() + ": put down left chopstick, finish eating");
	                Thread.sleep(1000);
	                	
	// the problem is p5 should firstly print "right" then print "left", different to others
	                }
            	}

            
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    	
    }
}
