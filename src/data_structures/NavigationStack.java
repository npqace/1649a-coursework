package data_structures;

import interfaces.IStack;

public class NavigationStack<E> implements IStack<E>{
    private class Node<E> {
        private E element;
        private Node<E> next;

        private Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    private int size;
    private Node<E> top;

    public NavigationStack() {
        this.top = null;
        this.size = 0;
    }

    @Override
    public void push(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
        Node<E> newNode = new Node<>(element);
        newNode.next = top;
        this.top = newNode;
        this.size++;
    }

    @Override
    public E pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        E oldElement = this.top.element;
        Node<E> temp = top.next;
        this.top.next = null;
        this.top = temp;
        this.size--;
        return oldElement;
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        return this.top.element;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
}
