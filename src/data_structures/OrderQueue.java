package data_structures;

import interfaces.IOrderQueue;

public class OrderQueue<E> implements IOrderQueue<E> {
    private class Node<E> {
        private E element;
        private Node<E> next;

        private Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    private Node<E> head;
    private int size;

    public OrderQueue() {
        this.head = null;
        this.size = 0;
    }

    @Override
    public void offer(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        try {
            Node<E> newNode = new Node<>(element);
            if (isEmpty()) {
                this.head = newNode;
            } else {
                Node<E> current = this.head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
            }
            size++;
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    @Override
    public E poll() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        try {
            E oldElement = this.head.element;
            if (this.size == 1) {
                this.head = null;
            } else {
                Node<E> next = this.head.next;
                this.head.next = null;
                this.head = next;
            }
            size--;
            return oldElement;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    @Override
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        try {
            return this.head.element;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
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
