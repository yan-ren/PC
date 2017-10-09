package ca.mcgill.ecse420.a1;
import java.util.concurrent.Semaphore;

class Philosophers4 implements Runnable{
    private String name;
    private Chopstick leftChopistick;
    private Chopstick rightChopistick;
    
    public Philosophers4(String name, Chopstick leftChopistick, Chopstick rightChopistick){
        this.name=name;
        this.leftChopistick=leftChopistick;
        this.rightChopistick=rightChopistick;
    }
    
    @Override
	public void run(){
        while(true){
            thinking();
            leftChopistick.pickUp();
            rightChopistick.pickUp();
            eating();
            leftChopistick.putDown();
            rightChopistick.putDown();
        }
        
    }
    
    public void eating(){
        System.out.println(name + " is eating");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public void thinking(){
        System.out.println(name + " is thinking");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class Chopstick4{

	final Semaphore mutex = new Semaphore(1, true);

	public Chopstick4(){}

	public void pickUp() {
		try {
			this.mutex.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
	}
	public void putDown() {
		this.mutex.release();
	}
}

//Test for dining philosopher question4
public class DineQ4 {

	final static int NUMBER = 6;
    
	public static void main(String []args){
    	
 
        Philosophers4[] philosophers4 = new Philosophers4[NUMBER];
        Chopstick[] chopsticks = new Chopstick[NUMBER];
 
        for (int i=0; i<NUMBER; i++) {
        	chopsticks[i] = new Chopstick();
        }
 
        for (int i=0; i<NUMBER; i++) {
        	Chopstick left = chopsticks[i];
        	Chopstick right = chopsticks[(i+1) % NUMBER];
            
        	if (i == philosophers4.length - 1) {
                // The last philosopher picks up the right fork first
        		philosophers4[i] = new Philosophers4(Integer.toString(i), right, left); 
            } else {
            	philosophers4[i] = new Philosophers4(Integer.toString(i), left, right);
            }
        	
            Thread t = new Thread(philosophers4[i], "Philosopher " + (i+1));
            t.start();
        }
    }
}