package data_structures;

import interfaces.IStack;

public class NavigationStack<E> implements IStack<E> {
    // Inner class to represent a node in the linked list used for the stack
    /**
     * Node class represents a single element in the linked list used by the
     * NavigationStack.
     */
    private class Node<E> {
        private E element; // The data stored in this node
        private Node<E> next; // Reference to the next node in the list

        private Node(E element) {
            this.element = element;
            this.next = null;
        }
    }

    private int size; // Number of elements in the stack
    private Node<E> top; // Top element of the stack (points to the first node)

    public NavigationStack() {
        this.top = null;
        this.size = 0;
    }

    /**
     * Adds an element to the top of the stack.
     *
     * @param element The element to be added.
     * @throws IllegalArgumentException if the element is null.
     */
    @Override
    public void push(E element) {
        if (element == null) {
            throw new IllegalArgumentException("Element cannot be null");
        }
        // Create a new node with the element
        Node<E> newNode = new Node<>(element);
        // Set the new node's next to point to the current top
        newNode.next = top;
        // Update the top to point to the new node
        this.top = newNode;
        // Increment the size
        this.size++;
    }

    /**
     * Removes and returns the element from the top of the stack.
     *
     * @return The element removed from the stack.
     * @throws IllegalStateException if the stack is empty.
     */
    @Override
    public E pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        // Store the element of the top node
        E oldElement = this.top.element;
        // Store a temporary reference to the next node
        Node<E> temp = top.next;
        // Disconnect the top node from the list
        this.top.next = null;
        // Update the top to point to the next node
        this.top = temp;
        // Decrement the size
        this.size--;
        return oldElement;
    }

    /**
     * Returns the element from the top of the stack without removing it.
     *
     * @return The element at the top of the stack.
     * @throws IllegalStateException if the stack is empty.  
     * 
     */
    @Override
    public E peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stack is empty");
        }
        // Return the element of the top node without removing it
        return this.top.element;
    }

    /**
     * Returns the number of elements in the stack.
     *
     * @return The size of the stack.
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * Checks if the stack is empty.
     *
     * @return `true` if the stack is empty, `false` otherwise.
     */
    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }
}
