package pluto.java;

public class Barrier {
	public static void main(String[] args) throws InterruptedException {
		BarrierImp.runBarrier();
	}
}

class BarrierImp {
	private int count;
	int curr_threads = 0;
	int released = 0;
	
	public BarrierImp(int count) {
		this.count = count;
	}
	
	public synchronized void barrier() throws InterruptedException {
		while (count == curr_threads) wait();
		increment();
		while (curr_threads < count)
			wait();
		
		released--;
		if (released ==0) {
			curr_threads =0;
			notifyAll();
		}
	}	
	
	public void increment() throws InterruptedException {
		curr_threads++;
		if (curr_threads == count)  {
			notifyAll();
			released = curr_threads;
		}
	}	
	
	public void passThroughBarrier(String threadName) throws InterruptedException {
		System.out.println("Thread " + threadName + " trying to pass through barrier");
		barrier();
		System.out.println("Thread " + threadName + " passed through barrier");
	}
	
	public static void runBarrier() throws InterruptedException {
		int count = 5;
		BarrierImp bi = new BarrierImp(count); // block threads until 5 threads reach there.
		
		Thread[] threads = new Thread[5];
		for (int i=0; i < 5; i++) {
			threads[i] = new Thread(() -> {
				try {
					int k =0;
					while (k < 3) {
						Thread.sleep(1000 * k);
						bi.passThroughBarrier(Thread.currentThread().getName());
						k++;
					}
				} catch (InterruptedException e) {
				}
			});
		}
		
		for (int i=0; i < 5; i++) {
			threads[i].start();
		}
		
		for (int i=0; i < 5; i++) {
			threads[i].join();
		}
	}
}
