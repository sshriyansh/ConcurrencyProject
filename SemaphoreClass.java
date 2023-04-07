import java.util.concurrent.Semaphore;

public class SemaphoreClass<T> {
	T[] arr;
	int head = 0;
	int tail = 0;
	int size = 0;
	int capacity;
	
	Semaphore read1;
	Semaphore write1;
	Semaphore lock;
	
	public SemaphoreClass(int capacity) {
		arr = (T[]) new Object[capacity];
		this.capacity = capacity;
		
		read1  = new Semaphore(0);
		write1 = new Semaphore(5);
		lock = new Semaphore(1);
	}
	
	public void enqueue(T item) throws InterruptedException {
		write1.acquire();
		lock.acquire();
		
		if (tail == capacity) {
			tail = 0;
		}
		arr[tail] = item;
		tail++;
		size++;
		
		lock.release();
		read1.release();
	}
	
	public T dequeue() throws InterruptedException {
		read1.acquire();
		lock.acquire();
		
		if (head == capacity) {
			head = 0;
		}
		var item = arr[head];
		head++;
		size--;
		
		lock.release();
		write1.release();
		return item;
	}
}
