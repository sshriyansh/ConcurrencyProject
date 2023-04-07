
public class ProducerConsumer {
	public static void main(String[] args) throws InterruptedException {
		QueueProblem<Integer> queue = new QueueProblem<Integer>(5);
		Thread t1 = new Thread(() -> {
			int i = 0;
			while (true) {
				try {
					queue.enqueue(new Integer(i));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Enqueue element is: " + i);
				i++;
			}
		});
		
		Thread t2 = new Thread(() -> {
			while(true) {
				int a = 0;
				try {
					a = queue.dequeue();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Dequeue element is: " + a);
			}
		});
		
		Thread t3 = new Thread(() -> {
			while(true) {
				int a = 0;
				try {
					a = queue.dequeue();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Dequeue element is: " + a);
			}
		});
		
		
		t1.start();
		Thread.sleep(4000);
		t2.start();
		t3.start();
		t1.join();
		t2.join();
		t3.join();
	}
}