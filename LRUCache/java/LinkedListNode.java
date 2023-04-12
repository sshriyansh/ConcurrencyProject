package LRUCache.java;

class LinkedListNode<T> {
	T key;
	T value;
	LinkedListNode<T> next;
	LinkedListNode<T> prev;
	
	public LinkedListNode(T key, T value) {
		this.key = key;
		this.value = value;
	}
	
	public LinkedListNode<T> getPrev() {
		return prev;
	}
	
	public LinkedListNode<T> getNext() {
		return next;
	}
	
	public void setPrev(LinkedListNode<T> node) {
		prev=node;
	}
	
	public void setNext(LinkedListNode<T> node) {
		next=node;
	}
}
