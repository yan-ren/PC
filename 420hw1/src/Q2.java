import java.util.concurrent.Semaphore;

/*每个哲学家相当于一个线程*/
class Philosophers2 implements Runnable{
    private String name;
    private Chopstick leftChopistick;
    private Chopstick rightChopistick;
    
    public Philosophers2(String name, Chopstick leftChopistick, Chopstick rightChopistick){
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
            Thread.sleep(1000);//模拟吃饭，占用一段时间资源
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public void thinking(){
        System.out.println(name + " is thinking");
        try {
            Thread.sleep(1000);//模拟思考
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}

class Chopstick{

	final Semaphore mutex = new Semaphore(1);

	public Chopstick(){
	}
	

	public void pickUp() {
		try {
			this.mutex.acquire();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
	public void putDown() {
		this.mutex.release();
	}
}

//测试
public class Q2 {

    public static void main(String []args){
 
        Philosophers2[] philosophers2 = new Philosophers2[5];
        Chopstick[] chopsticks = new Chopstick[5];
 
        for (int i=0; i<5; i++) {
        	chopsticks[i] = new Chopstick();
        }
 
        for (int i=0; i<5; i++) {
        	Chopstick left = chopsticks[i];
        	Chopstick right = chopsticks[(i+1) % 5];
            
        	if (i == philosophers2.length - 1) {
                // The last philosopher picks up the right fork first
        		philosophers2[i] = new Philosophers2(Integer.toString(i), right, left); 
            } else {
            	philosophers2[i] = new Philosophers2(Integer.toString(i), left, right);
            }
        	
            Thread t = new Thread(philosophers2[i], "Philosopher " + (i+1));
            t.start();
        }
    }
}