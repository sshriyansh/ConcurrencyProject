package AtomicStack.java;

public class ImpStack<T> {
	
	StackNode<T> top;
	
	public synchronized void push(T item) {
		if (top == null) {
			top = new StackNode<>(item);
		} else {
			StackNode<T> t = new StackNode<>(item);
			StackNode<T> oldHead = top;
			top = t;
			top.setNext(oldHead);
		}
	}
	
	public synchronized T pop() {
		if (top == null) {
			return null;
		}

		StackNode<T> returnNode = top;
		top = top.getNext();
		return returnNode.getItem();
			
	}

}
