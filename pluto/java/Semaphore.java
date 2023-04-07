package pluto.java;

public class Semaphore {
	public static void main(String[] args) throws InterruptedException {
		TestSemaphore ts = new TestSemaphore(1);
		
		
		Thread t1 = new Thread(new Runnable() {
			public void run() {
				int i = 0;
				while (i < 50) {
					try {
						ts.addItem(i);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					i++;
				}
			}
		});
		
		Thread t2 = new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						ts.readItem();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		Thread t3 = new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						ts.readItem();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		t1.start();
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
	}
}

class TestSemaphore {
	SemaphoreImp readSemaphore;
	SemaphoreImp writeSemaphore;
	int count = 0;
	public TestSemaphore(int capacity) {
		readSemaphore = new SemaphoreImp(capacity, capacity);
		writeSemaphore = new SemaphoreImp(capacity, 0 );
	}
	
	public void addItem(int item) throws InterruptedException {
		writeSemaphore.acquire();
		
		count = item;
		readSemaphore.release();
		
	}
	
	public void readItem() throws InterruptedException {
		
		readSemaphore.acquire();
		System.out.println("Item added is fetched by thread " + Thread.currentThread().getName() + " with item value: " + count);
		writeSemaphore.release();
	}
}

class SemaphoreImp {
	int permits;
	int max_permits;
	
	public SemaphoreImp(int max_permits, int used_permits) {
		this.max_permits = max_permits;
		this.permits = used_permits;
	}
	
	public synchronized void acquire() throws InterruptedException {
		while (permits == max_permits) {
			wait();
		}
		
		permits++;
		notify();
	}
	
	public synchronized void release() throws InterruptedException {
		while (permits == 0) {
			wait();
		}
		
		permits--;
		notify();
	}
}
