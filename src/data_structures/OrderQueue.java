package data_structures;

import interfaces.IOrderQueue;
import models.Order;

public class OrderQueue<E> implements IOrderQueue<E> {
    // Node class for queue implementation
    private class Node<E> {
        private E element;
        private Node<E> next;

        private Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    private Node<E> head; // Front of queue
    private int size;     // Queue size

    public OrderQueue() {
        this.head = null;
        this.size = 0;
    }

    // Add element to back of queue
    @Override
    public void offer(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        try {
            Node<E> newNode = new Node<>(element);
            if (isEmpty()) {
                head = newNode;
            } else {
                Node<E> current = head;
                while (current.next != null) {
                    current = current.next;
                }
                current.next = newNode;
                // Update Order's next pointer
                if (element instanceof Order) {
                    ((Order) current.element).next = (Order) element;
                }
            }
            size++;
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // Remove and return front element
    @Override
    public E poll() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        try {
            E oldElement = head.element;
            if (size == 1) {
                head = null;
            } else {
                Node<E> next = head.next;
                head.next = null;
                if (oldElement instanceof Order) {
                    ((Order) oldElement).next = null;
                }
                head = next;
            }
            size--;
            return oldElement;
        } catch (Exception e) {
            System.out.println("Error: " + e);
            return null;
        }
    }

    // Return front element without removing
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

    // Return queue size
    @Override
    public int size() {
        return this.size;
    }

    // Check if queue is empty
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
}
