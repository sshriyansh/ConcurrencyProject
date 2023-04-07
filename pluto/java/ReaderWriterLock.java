package pluto.java;

import java.util.concurrent.Semaphore;

public class ReaderWriterLock {
	public static void main(String[] args) throws InterruptedException {
		ReaderWriterLockDesign rwl = new ReaderWriterLockDesign();
		
		Thread tw1 = new Thread(new Runnable() {
			public void run() {
				System.out.println("Acquiring write lock at "  + System.currentTimeMillis());
				try {
					rwl.acquireWrite();
					System.out.println("Acquired write lock at "  + System.currentTimeMillis());
					while(true) {
						Thread.sleep(500);
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Thread tw2 = new Thread(new Runnable() {
			public void run() {
				System.out.println("Trying to acquire write lock at "  + System.currentTimeMillis());
				try {
					rwl.acquireWrite();
					System.out.println("Acquired write lock at "  + System.currentTimeMillis());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Thread tr1 = new Thread(new Runnable() {
			public void run() {
				System.out.println("Acquiring read lock at "  + System.currentTimeMillis());
				try {
					rwl.acquireRead();
					System.out.println("Acquired read lock at "  + System.currentTimeMillis());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		Thread tr2 = new Thread(new Runnable() {
			public void run() {
				System.out.println("Releasing read lock at "  + System.currentTimeMillis());
				try {
					rwl.releaseRead();
					System.out.println("Released read lock at "  + System.currentTimeMillis());
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		tr1.start();
		Thread.sleep(1000);
		tw1.start();
		
		Thread.sleep(5000);
		tr2.start();
		Thread.sleep(1000);
		tw2.start();
		
		tr1.join();
		tr2.join();
		tw1.join();
		tw2.join();
		
	}
}

class ReaderWriterLockDesign {
	 boolean isWriterLocked = false;
	 int readers = 0;
	 
	 public ReaderWriterLockDesign() {
		 
	 }
	
	 public synchronized void acquireRead() throws InterruptedException {
		while (isWriterLocked)
			wait();
		
		readers++;
		
	}
	 
	 public synchronized void releaseRead() throws InterruptedException {
		readers--;
		notify();
	}
	
	public synchronized void acquireWrite() throws InterruptedException {
		while (readers != 0 || isWriterLocked)
			wait();
		
		isWriterLocked = true;
	}
	
	public synchronized void releaseWrite() throws InterruptedException {
		isWriterLocked = false;
		notify();
		
	}
}
