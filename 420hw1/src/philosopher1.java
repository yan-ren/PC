public class philosopher1 {
 
    public static void main(String[] args) throws Exception {
 
        Philosopher[] philosophers = new Philosopher[5];
        Object[] chopsticks = new Object[5];
 
        for (int i=0; i<5; i++) {
        	chopsticks[i] = new Object();
        }
 
        for (int i=0; i<5; i++) {
            Object left = chopsticks[i];
            Object right = chopsticks[(i+1) % 5];
 
            philosophers[i] = new Philosopher(left, right);
             
            Thread t = new Thread(philosophers[i], "Philosopher " + (i+1));
            t.start();
        }
    }
}

class Philosopher implements Runnable {
    private Object left;
    private Object right;
 
    public Philosopher(Object left, Object right) {
        this.left = left;
        this.right = right;
    }
    
    @Override
    public void run() {
    	try {
            while (true) {
                 
                // thinking
            	System.out.println(Thread.currentThread().getName() + ": thinking");
            	Thread.sleep(1000);
                synchronized (left) {
                	System.out.println(Thread.currentThread().getName() + ": pick up left chopstick");
                	Thread.sleep(1000);
                    synchronized (right) {
                        // eating
                    	System.out.println(Thread.currentThread().getName() + ": pick up right chopstick, eating");
                    	Thread.sleep(1000);                         
                    }
                     
                    // finish eating
                    System.out.println(Thread.currentThread().getName() + ": put down chopsticks, finish eating");
                	Thread.sleep(1000); 

                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
    }
}
