package LRUCache.java;

public class LRUCache {
	public static void main(String[] args) throws InterruptedException {
		LRUCacheImp<Integer> lru = new LRUCacheImp<Integer>(5);
		
		Thread[] threads = new Thread[10];
		
		for (int i=0; i < 5; i++) {
			final int a = i;
			threads[i] = new Thread(()-> {
				lru.put(a, 2);
			});
		}
		
		for (int i=0; i < 5; i++) {
			threads[i].start();		
		}
		
		for (int i=0; i < 5; i++) {
			threads[i].join();
		}
		
		for (int i=5; i < 10; i++) {
			final int a = i-5;
			threads[i] = new Thread(()-> {
				Integer val = lru.get(a);
				System.out.println("Printing value for key "+ a + " value: "+ val);
			});
		}
		
		for (int i=5; i < 10; i++) {
			threads[i].start();		
		}
		
		for (int i=5; i < 10; i++) {
			threads[i].join();
		}
		
	}
}
