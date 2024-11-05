package data_structures;

import java.util.Arrays;

import models.Order;

public class CustomArrayList implements IList<Order>{
    private Order[] orders;
    private int size;
    private static final int DEFAULT_CAPACITY = 10;

    public CustomArrayList() {
        this.orders = new Order[DEFAULT_CAPACITY];
        this.size = 0;
    }

    @Override
    public boolean add(Order order) {
        if (this.size == this.orders.length) {
            this.orders = grow();
        }
        this.orders[size] = order;
        size++;
        return true;
    }

    
    private Order[] grow() {
        return Arrays.copyOf(this.orders, this.orders.length * 2);
    }

    @Override
    public Order get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        return this.orders[index];
    }
    @Override
    public Order remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index out of bounds");
        }
        Order removedOrder = get(index);
        for (int i = index; i < this.size - 1; i++) {
            this.orders[i] = this.orders[i + 1];
        }
        this.size--;
        this.orders[this.size] = null;

        return removedOrder;
    }

    @Override
    public int indexOf(Order element) {
        for (int i = 0; i < this.size; i++) {
            if (this.orders[i].equals(element)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean contains(Order element) {
        return indexOf(element) != -1;
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
