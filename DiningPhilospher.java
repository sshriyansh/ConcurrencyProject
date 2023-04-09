import java.util.concurrent.*;

public class DiningPhilospher {

	public static void main(String[] args) throws InterruptedException {
		DiningPhilospherImp.startPhilospherLife();
	}
}

class DiningPhilospherImp {
	Semaphore[] forks = new Semaphore[5];
	int noOfPhilosphers = 5;
	private Semaphore maxDiners = new Semaphore(4); // to avoid deadlocks
	
	public DiningPhilospherImp() {
		forks[0] = new Semaphore(1);
		forks[1] = new Semaphore(1);
		forks[2] = new Semaphore(1);
		forks[3] = new Semaphore(1);
		forks[4] = new Semaphore(1);
	}
	
	void contemplate(int id) throws InterruptedException {
		System.out.println("Philospher " + id + " contemplating");
		Thread.sleep(1000);
	}
	
	void eat(int id) throws InterruptedException {
		maxDiners.acquire();
		forks[id].acquire();
		forks[(id + 1)%5].acquire();
		
		System.out.println("Philospher " + id + " is eating currently");
		Thread.sleep(5000);
		forks[id].release();
		forks[(id + 1)%5].release();
		maxDiners.release();
	}
	
	void lifeOfPhilospher(int id) throws InterruptedException {
		while(true) {
			contemplate(id);
			eat(id);
		}
		
	}
	
	public static void startPhilospherLife() throws InterruptedException {
		DiningPhilospherImp dp = new DiningPhilospherImp();
		Thread[] threads = new Thread[5];
		for (int i=0; i < 5; i++) {
			final int a = i;
			threads[i] = new Thread(new Runnable() {

		        @Override
		        public void run() {
					try {
						dp.lifeOfPhilospher(a);
					} catch (InterruptedException e) {
					}
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
