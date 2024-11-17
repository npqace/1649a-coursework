package data_structures;

import interfaces.IOrderQueue;

public class OrderQueue<E> implements IOrderQueue<E> {
    // Inner class to represent a node in the linked list used for the queue
    /**
     * Node class represents a single element in the linked list used by the
     * OrderQueue.
     */
    private class Node<E> {
        private E element; // The data stored in this node
        private Node<E> next; // Reference to the next node in the list

        private Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    private Node<E> head; // Front element of the queue (points to the first node)
    private int size; // Number of elements in the queue

    public OrderQueue() {
        this.head = null;
        this.size = 0;
    }

    /**
     * Adds an element to the back of the queue.
     *
     * @param element The element to be added.
     * @throws IllegalArgumentException if the element is null.
     */
    @Override
    public void offer(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }

        try {
            Node<E> newNode = new Node<>(element);
            if (isEmpty()) {
                // If the queue is empty, set head to the new node
                this.head = newNode;
            } else {
                // Find the last node and add the new node after it
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

    /**
     * Removes and returns the element from the front of the queue.
     *
     * @return The element removed from the queue.
     * @throws IllegalStateException if the queue is empty.
     */
    @Override
    public E poll() {
        if (isEmpty()) {
            throw new IllegalStateException("Queue is empty");
        }

        try {
            E oldElement = this.head.element;
            if (this.size == 1) {
                // If there's only one element, set head to null
                this.head = null;
            } else {
                // Move the head pointer to the next node
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

    /**
     * Returns the element from the front of the queue without removing it.
     *
     * @return The element at the front of the queue.
     * @throws IllegalStateException if the queue is empty.  
     * 
     */
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

    /**
     * Returns the number of elements in the queue.
     *
     * @return The size of the queue.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Checks if the queue is empty.
     *
     * @return `true` if the queue is empty, `false` otherwise.
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
}
