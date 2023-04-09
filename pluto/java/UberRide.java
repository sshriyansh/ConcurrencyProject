package pluto.java;

import java.util.concurrent.Semaphore;
import java.util.HashSet;
import java.util.Set;

public class UberRide {
	public static void main(String[] args) throws InterruptedException {
		UberRideImp.requestUber();
	}
}

class UberRideImp {
	int maxCapacity = 4;
	int republician_user = 0;
	int democratic_user = 1;
	int democrats = 0;
	int republicans = 0;
	boolean isNewCarAvailable;
	
	
	public UberRideImp() {
		isNewCarAvailable = true;
	}
	
	public synchronized void requestRideandSit(int userType) throws InterruptedException {
		while (!isNewCarAvailable)
			wait();
		
		while (democrats >= 3 && userType == 0) {
			wait();
		}
		
		while (!isNewCarAvailable)
			wait();
		
		while (republicans >= 3 && userType == 1) {
			wait();
		}
		
		while (!isNewCarAvailable)
			wait();
		
		if (democrats + republicans < 4) {
			if (userType == 0 && democrats <= 2) {
				democrats++;
				seated(userType);
			}
			
			if (userType == 1 && republicans <=2) {
				republicans++;
				seated(userType);
			}
		}
		
		if (democrats + republicans == 4)
			drive();
	}
	
	void seated(int userType) {
		System.out.println("Usertype " + userType + " is seated: " + Thread.currentThread().getName());
	}
	
	void drive() throws InterruptedException {
		System.out.println("Driving a new car with " + democrats + " democrats and " + republicans + " republicans");
		democrats = 0;
		republicans = 0;
		isNewCarAvailable = false;
		notifyAll();
		
		System.out.println("getting new car in few minutes for users that are waiting" );
		Thread.sleep(4000);
		isNewCarAvailable = true;
		notifyAll();
	}
	
	public static void requestUber() throws InterruptedException {
		UberRideImp uri = new UberRideImp();
		Set<Thread> allThreads = new HashSet<Thread>();
		
		for (int i=0; i < 10; i++) {
			Thread t = new Thread(()-> {
				try {
					uri.requestRideandSit(1);
					Thread.sleep(50);
				} catch (InterruptedException e) {
				}
			});
			allThreads.add(t);
		}
		
		for (int i=0; i < 10; i++) {
			Thread t = new Thread(()-> {
				try {
					uri.requestRideandSit(0);
					Thread.sleep(20);
				} catch (InterruptedException e) {
				}
			});
			allThreads.add(t);
		}

		for (Thread thread : allThreads) {
			thread.start();
		}
		
		for (Thread thread : allThreads) {
			thread.join();
		}
	}
}
