package pluto.java;

public class RateLimiter {
	public static void main(String[] args) throws InterruptedException {
		int N = 1000;
		Ratelimit rt = new Ratelimit(N);
		
		Thread[] threads = new Thread[20];
		
		for (int i=0; i < 20; i++) {
			threads[i] = new Thread(() -> {
				try {
					rt.getToken();
				} catch (InterruptedException e) {
					// do nothing
				}
			});
		}
		
		for (int i=0; i < 20; i++) {
			threads[i].start();
		}
		
		for (int i=0; i < 20; i++) {
			threads[i].join();
		}
		
	}
}

class Ratelimit {
	private int capacity;
	int totalTokenPresent = 0;
	long lastTokenTime = System.currentTimeMillis();
	
	
	public Ratelimit(int capacity) {
		this.capacity = capacity;
		
	}
	
	public synchronized void getToken() throws InterruptedException {
		
		totalTokenPresent += (System.currentTimeMillis() - lastTokenTime)/1000;
		
		if (totalTokenPresent > capacity) {
			totalTokenPresent =  capacity;
		}
		
		if (totalTokenPresent == 0) {
			Thread.sleep(1000);
		}
		
		lastTokenTime = System.currentTimeMillis();
		
		System.out.println("Grant token to "  + Thread.currentThread().getName() + " at time: "+ System.currentTimeMillis());
	}
	
}