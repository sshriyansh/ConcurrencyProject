package AtomicStack.java;
import java.util.concurrent.*;

public class Example {
	public static void main(String[] args) throws InterruptedException {
		
        int numThreads = 2;
        CyclicBarrier barrier = new CyclicBarrier(numThreads);
        ImpStack<Integer>  st = new ImpStack<>();
        
        Thread[] threads = new Thread[numThreads];
        for (int i=0; i < numThreads; i++) {
        	threads[i] = new Thread(()->{
        		for (int j=0; j < 1000; j++) {
        			st.push(j);
        		}
        		
        		try {
					barrier.await();
				} catch (InterruptedException | BrokenBarrierException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        		
        		for (int j=0; j < 999; j++) {
        			st.pop();
        		}
        	});
        }
        
        for (int i=0; i < numThreads; i++) {
        	threads[i].start();
        }
        
        for (int i=0; i < numThreads; i++) {
        	threads[i].join();
        }
        
        System.out.println("10,00 pushes and pops completed by 2 threads. Current top of stack " + st.pop());
	}
}
