package LRUCache.java;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.locks.*;

class LRUCacheImp<T> {
	LinkedListNode<T> head;
	LinkedListNode<T> tail;
	Map<T, LinkedListNode<T>> cache;
	ReentrantLock lock = new ReentrantLock();
	int maxCapacity;
	int size =0 ;
	
	public LRUCacheImp(int capacity) {
		this.maxCapacity = capacity;
		head = new LinkedListNode(-1, -1);
		tail = new LinkedListNode(-1, -1);
		head.next = tail;
		tail.prev = head;
		
		cache = new HashMap();
		
	}
	
	T get(T key) {
		lock.lock();
		try {
			if (cache.containsKey(key)) {
				LinkedListNode<T>node =  cache.get(key);
				removeNode(node);
				addNode(node);
				return node.value;
			} else {
				return null;
			}
		} finally {
			lock.unlock();
		}

	}
	
	void put(T key, T val) {
		try {
			lock.lock();
			if (cache.containsKey(key)) {
				LinkedListNode<T>node =  cache.get(key);
				removeNode(node);
			}
			
			if (cache.size() == maxCapacity) {
				removeNode(tail.prev);
			}
			addNode(new LinkedListNode<>(key, val));
		} finally {
			lock.unlock();
		}

	}
	
	public void addNode(LinkedListNode<T> node) {
		cache.put(node.key, node);
		LinkedListNode<T> next = head.next;
		head.next = node;
		node.prev = head;
		next.prev = node;
		node.next = next;
	}
	
	public void removeNode(LinkedListNode<T> node) {
		cache.remove(node.key);
		LinkedListNode<T> next = node.next;
		LinkedListNode<T> prev = node.prev;
		
		prev.next = next;
		next.prev = prev;
	}
		
}
