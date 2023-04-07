package pluto.java;

public class RateLimiter2 {
	public static void main(String[]  args) throws InterruptedException {
		int N = 2;
		Ratelimit2 rt = new Ratelimit2(N);
		
		
		// spin a background thread for adding toekn;
		System.out.println("Defining fillign thread");
		Thread produce = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					rt.fillTokens();
				} catch (InterruptedException e) {
					
				}
			}
		});
		
		System.out.println("Defining get tokens thread");
		Thread[] threads = new Thread[5];
		
		for (int i=0; i < 5; i++) {
			threads[i] = new Thread(() -> {
				try {
					rt.getToken();
				} catch (InterruptedException e) {
					// do nothing
				}
			});
		}
		
		for (int i=0; i < 5; i++) {
			threads[i].start();
		}
		
		produce.start();
		produce.join();
		produce.setDaemon(true);
		
		for (int i=0; i < 5; i++) {
			threads[i].join();
		}
	}
}

class Ratelimit2 {
	int tokenCount = 0;
	Object lock;
	int capacity;
	
	public Ratelimit2(int capacity) {
		lock = new Object();
		this.capacity = capacity;
	}
	
	public void getToken() throws InterruptedException {
		System.out.println("Starting getting thread");
		synchronized (lock) {
			while (tokenCount == 0) {
				lock.wait();
			}
				
			tokenCount--;
			System.out.println("Grant token to "  + Thread.currentThread().getName() + " at time: "+ System.currentTimeMillis());
		}
	}
	
	public void fillTokens() throws InterruptedException {
		System.out.println("Starting fillng thread");
		while (true) {
			synchronized (lock) {
				if (tokenCount < capacity) {
					tokenCount++;
				}

				lock.notifyAll();
			}
			Thread.sleep(5000);
		}
		
	}
}
