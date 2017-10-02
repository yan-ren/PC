/*每个哲学家相当于一个线程*/
class Philosophers extends Thread{
    private String name;
    private Fork fork;
    public Philosophers(String name,Fork fork){
        super(name);
        this.name=name;
        this.fork=fork;
    }
    
    @Override
	public void run(){
        while(true){
            thinking();
            fork.takeFork();
            eating();
            fork.putFork();
        }
        
    }
    
    
    public void eating(){
        System.out.println(name + " is eating");
        try {
            sleep(1000);//模拟吃饭，占用一段时间资源
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    
    public void thinking(){
        System.out.println(name + " is thinking");
        try {
            sleep(1000);//模拟思考
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
//
class Fork{
    /*5只筷子，初始为都未被用*/
    private boolean[] used={false,false,false,false,false,false};
    
    /*只有当左右手的筷子都未被使用时，才允许获取筷子，且必须同时获取左右手筷子*/
    public synchronized void takeFork(){
        String name = Thread.currentThread().getName();
        int i = Integer.parseInt(name);
        while(used[i]||used[(i+1)%5]){
            try {
                wait();//如果左右手有一只正被使用，等待
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        used[i ]= true;
        used[(i+1)%5]=true;
    }
    
    /*必须同时释放左右手的筷子*/
    public synchronized void putFork(){
        String name = Thread.currentThread().getName();
        int i = Integer.parseInt(name);
        
        used[i ]= false;
        used[(i+1)%5]=false;
        notifyAll();//唤醒其他线程
    }
}

//测试
public class philosopher {

    public static void main(String []args){
        Fork fork = new Fork();
        new Philosophers("1",fork).start();
        new Philosophers("2",fork).start();
        new Philosophers("3",fork).start();
        new Philosophers("4",fork).start();
        new Philosophers("5",fork).start();
    }
}