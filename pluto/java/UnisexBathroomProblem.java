package pluto.java;

public class UnisexBathroomProblem {
	public static void main(String[] args) throws InterruptedException {		
		UnisexBathroom ub = new UnisexBathroom(3); // 3 bathrooms
		
		Thread[] mens = new Thread[20];
		Thread[] womens = new Thread[20];
		
		for (int i=0; i < 20; i++) {
			String name = "Men " + i;
			mens[i] = new Thread(() -> {
				try {
					ub.maleUseBathRoom(name);
				} catch (InterruptedException e) {
					
				}
			});
		}
		
		for (int i=0; i < 20; i++) {
			String name = "Women " + i;
			womens[i] = new Thread(() -> {
				try {
					ub.femaleUseBathRoom(name);
				} catch (InterruptedException e) {
					
				}
			});
		}
		
		for (int i=0; i < 20; i++) {
			mens[i].start();
		}
		
		for (int i=0; i < 20; i++) {
			womens[i].start();
		}
		
		for (int i=0; i < 20; i++) {
			mens[i].join();
			womens[i].join();
		}
		
		
	}
}

class UnisexBathroom {
	int users;
	int capacity;
	int current_group;
	static int maleGroup = 1;
	static int femaleGroup = 2;
	
			
	public UnisexBathroom(int capacity) {
		this.users = 0;
		this.capacity = capacity;
		this.current_group = -1;
		
	}
	
	public void maleUseBathRoom(String name) throws InterruptedException {
		
		acquireBathroom(maleGroup);
		
		System.out.println("Male " + name + " using the bathroom at : " + System.currentTimeMillis());
		Thread.sleep(3000);
		System.out.println("Male " + name + " releasing the bathroom at : " + System.currentTimeMillis());
		
		releaseBathroom();
		
	}
	
	public void femaleUseBathRoom(String name) throws InterruptedException {
			
		acquireBathroom(femaleGroup);
		
		System.out.println("Female " + name + " using the bathroom at: " + System.currentTimeMillis());
		Thread.sleep(2000);
		System.out.println("Female " + name + " releasing the bathroom at: " + System.currentTimeMillis());
		
		releaseBathroom();
	}
	
	public synchronized void acquireBathroom(int group) throws InterruptedException {
		while (users == capacity || (current_group != -1 && group != current_group))  
			wait();
		
		users++;
		current_group = group;
	}
	
	public synchronized void releaseBathroom() {
		users--;
		
		if (users <= 0) {
			current_group = -1;
			users=0;
		}
		notify();
	}
}
