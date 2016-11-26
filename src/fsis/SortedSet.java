package fsis;

import java.util.*;


/**
 * @overview Sorted are mutable, unbounded sets of customer.
 * @attributes elements  ArrayList<Customer>
 * @object A typical Sorted object is c={c1,...,cn}, where c1,...,cn are
 * elements.
 * @abstract_properties optional(elements) = false /\ for all x in elements. x
 * is integer /\ for all x, y in elements. x neq y
 */
public class SortedSet {
    @DomainConstraint(type = "ArrayList", optional = false)
    private ArrayList<Comparable> elements;

    /**
     * @effects initialise object SortedSet with elements is an Arraylist
     */
    public SortedSet() {
        elements = new ArrayList<Comparable>();
    }

    /**
     * @effects if elements is empty
     * add customer in this.elements.
     * else
     * add customer in this.elements and sort elements.
     */
    public void insert(Comparable customer) {
        if (elements.isEmpty()) {
            elements.add(customer);
        }
        else {
            Customer customerInput = (Customer) customer;
            Iterator iterator = iterator();
            while (iterator.hasNext()) {
                Customer custom = (Customer) iterator.next();
                if (custom.compareTo(customerInput) > 0 && isExist(customer) == false) {
                    elements.add(getIndex(custom), customer);
                    customer = custom;
                }
            }

            if (isExist(customer) == false) {
                elements.add(customer);
            }
        }
    }



    /**
     * @effects if x is in this
     * return true
     * else
     * return false
     */
    private boolean isExist(Comparable customer) {
        return (getIndex(customer) >= 0);
    }

    /**
     * @effects return the size of this
     */
    private int size() {
        return elements.size();
    }

    /**
     * @effects if x is in this
     * return the index where x appears
     * else
     * return -1
     */
    private int getIndex(Comparable customer) {
        for (int i = 0; i < elements.size(); i++) {
            if (customer.compareTo(elements.get(i)) == 0)
                return i;
        }
        return -1;
    }

    /**
     * if element is empty
     * return true
     * else
     * return false
     */
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    /**
     * @return a Iterator
     */
    public Iterator iterator() {
        return new Generator();
    }

    @Override
    public String toString() {
        if (size() == 0)
            return "List Cutomers:{ }";
        String s = "List Customers:{" + elements.get(0).toString();
        for (int i = 1; i < size(); i++) {
            s = s + " , " + elements.get(i).toString();
        }

        return s + "}";
    }

    private class Generator implements Iterator {
        private int index;

        public Generator() {
            index = -1;
        }

        public boolean hasNext() {
            return (index < size() - 1);
        }

        public Object next() throws NoSuchElementException {
            if (index < size() - 1) {
                index++;
                return elements.get(index);
            }
            throw new NoSuchElementException("No more elements");
        }

        public void remove() {
            // do nothing
        }
    }
}

