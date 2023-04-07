
public class QueueProblem<T> {
	T[] arr;
	int head = 0;
	int tail = 0;
	int capacity;
	int size = 0;
	Object lock ;
	
	public QueueProblem(int capacity) {
		arr  =  (T[]) new Object[capacity];
		this.capacity = capacity;
		lock = new Object();
		
	}
	
	public void enqueue(T item) throws InterruptedException {
		synchronized  (lock) {
			while (size == capacity) {
				lock.wait();
			}
			
			if (tail == capacity) 
				tail = 0;
			
			arr[tail] = item;
			tail++;
			size++;
			lock.notifyAll();
		}
	}
	
	public T dequeue() throws InterruptedException {
		synchronized  (lock) {
			if (size == 0) {
				lock.wait();
			}
			
			if (head == capacity)
				head = 0;
			
			var item = arr[head];
			arr[head] = null;
			
			head++;
			size--;
			lock.notifyAll();
			
			return item;
		}	
	}
}
