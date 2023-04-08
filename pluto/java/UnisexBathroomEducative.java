package pluto.java;

import java.util.concurrent.Semaphore;

class UnisexBathroomEducative  {
    public static void main( String args[] ) throws InterruptedException {
        UnisexBathroom1.runTest();
    }
}

class UnisexBathroom1 {

    static String WOMEN = "women";
    static String MEN = "men";
    static String NONE = "none";

    String inUseBy = NONE;
    int empsInBathroom = 0;
    Semaphore maxEmps = new Semaphore(3);

    void useBathroom(String name) throws InterruptedException {
        System.out.println("\n" + name + " using bathroom. Current employees in bathroom = " + empsInBathroom + " " + System.currentTimeMillis());
        Thread.sleep(3000);
       // System.out.println("\n" + name + " done using bathroom " + System.currentTimeMillis());
    }

    void maleUseBathroom(String name) throws InterruptedException {

        synchronized (this) {
            while (inUseBy.equals(WOMEN)) {
                this.wait();
            }
            maxEmps.acquire();
            empsInBathroom++;
            inUseBy = MEN;
        }

        useBathroom(name);
        maxEmps.release();

        synchronized (this) {
            empsInBathroom--;

            if (empsInBathroom == 0) inUseBy = NONE;
            this.notify();
        }
    }

    void femaleUseBathroom(String name) throws InterruptedException {

        synchronized (this) {
            while (inUseBy.equals(MEN)) {
                this.wait();
            }
            maxEmps.acquire();
            empsInBathroom++;
            inUseBy = WOMEN;
        }

        useBathroom(name);
        maxEmps.release();

        synchronized (this) {
            empsInBathroom--;

            if (empsInBathroom == 0) inUseBy = NONE;
            this.notify();
        }
    }

    public static void runTest() throws InterruptedException {

        final UnisexBathroom1 ub = new UnisexBathroom1();
        
        Thread[] mens = new Thread[20];
		Thread[] womens = new Thread[20];
		
		for (int i=0; i < 20; i++) {
			String name = "Men " + i;
			mens[i] = new Thread(() -> {
				try {
					ub.maleUseBathroom(name);
				} catch (InterruptedException e) {
					
				}
			});
		}
		
		for (int i=0; i < 20; i++) {
			String name = "Women " + i;
			womens[i] = new Thread(() -> {
				try {
					ub.femaleUseBathroom(name);
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
