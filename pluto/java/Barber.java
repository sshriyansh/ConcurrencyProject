package pluto.java;

import java.util.concurrent.locks.ReentrantLock;

public class Barber {
	public static void main(String[] args) throws InterruptedException {
		BarberImp.runBarberShop();	
	}
}

class BarberImp {
	int chairs;
	int customers;
	boolean isBarberSleeping = true;
	boolean isChairFree = true;
	ReentrantLock lock = new ReentrantLock();
	
	
	public BarberImp(int capacity) {
		chairs = capacity;
	}
	
	void getanhaircut() throws InterruptedException {
		lock.lock();
		if (customers == chairs) {
			System.out.println("Barber is busy, leaving the shop " + Thread.currentThread().getName());
			lock.unlock();
			return;
		}
		customers++;
		lock.unlock();
		
		synchronized(this) {
			if (isBarberSleeping) {
				wakeUpBarber();
			}
			while(!isChairFree) {
				wait();
			}
			
			startHairCut();
		}
	}
	
	void wakeUpBarber() throws InterruptedException {
		isBarberSleeping = false;
		System.out.println("Barber is sleeping, waking up now");
	}
	
	void startHairCut() throws InterruptedException {
		isChairFree = false;
		System.out.println("Barber giving haircut to customer " + Thread.currentThread().getName());
		Thread.sleep(3000);
		
		customers--;
		
		if (customers == 0) {
			// barber goes to sleep;
			sleep();
		}
		isChairFree = true;
		notifyAll();
	}
	
	void sleep() throws InterruptedException {
		System.out.println("Barber going to sleep");
		isBarberSleeping = true;
	}
	
	public static void runBarberShop() throws InterruptedException {
		BarberImp bi = new BarberImp(5);
		
		Thread[] threads = new Thread[10];
		for (int i=0; i < 10; i++) {
			threads[i] = new Thread(() -> {
				try {
					bi.getanhaircut();
				} catch (InterruptedException e) {
				}
			});
		}
		
		for (int i=0; i < 10; i++) {
			threads[i].start();
		}
		
		Thread.sleep(30000);
		Thread[] threads1 = new Thread[5];
		for (int i=0; i < 5; i++) {
			threads1[i] = new Thread(() -> {
				try {
					bi.getanhaircut();
				} catch (InterruptedException e) {
				}
			});
		}
		
		for (int i=0; i < 5; i++) {
			threads1[i].start();
		}
		
		
		for (int i=0; i < 10; i++) {
			threads[i].join();
		}
		
		for (int i=0; i < 5; i++) {
			threads1[i].join();
		}
	}
}
