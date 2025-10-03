package com.mkass420.myhashset;

import java.util.*;

public class MyHashSet<E> extends AbstractSet<E> implements Set<E>, java.io.Serializable {
    static final float DEFAULT_LOAD_FACTOR = 0.75f;
    static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; //16
    static final int MAXIMUM_CAPACITY = 1 << 30;

    public static class Node<E> {
        public final int hash;
        public final E entry;
        public Node<E> next;

        public Node(Node<E> next, E entry, int hash) {
            this.next = next;
            this.entry = entry;
            this.hash = hash;
        }

        public E getEntry() {
            return entry;
        }

        @Override
        public String toString() {
            return entry.toString();
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Node<?> node && Objects.equals(entry, node.entry);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(entry);
        }
    }

    static int tableSizeFor(int cap) {
        int n = -1 >>> Integer.numberOfLeadingZeros(cap - 1);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    }

    static int hash(Object entry) {
        int h;
        return (entry == null) ? 0 : (h = entry.hashCode()) ^ (h >>> 16);
    }

    public Node<E>[] table;
    public int size;
    public int threshold;
    public final float load_factor;

    public MyHashSet(int initial_capacity, float load_factor) {
        if (initial_capacity < 0)
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    initial_capacity);
        if (initial_capacity > MAXIMUM_CAPACITY)
            initial_capacity = MAXIMUM_CAPACITY;
        if (load_factor <= 0 || Float.isNaN(load_factor))
            throw new IllegalArgumentException("Illegal load factor: " +
                    load_factor);
        this.load_factor = load_factor;
        this.threshold = tableSizeFor(initial_capacity);
    }

    public MyHashSet(int initial_capacity) {this(initial_capacity, DEFAULT_LOAD_FACTOR);}
    public MyHashSet() {this(DEFAULT_INITIAL_CAPACITY, DEFAULT_LOAD_FACTOR);}

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return table == null || size == 0;
    }

    final Node<E> getNode(Object entry){
        Node<E> first, e;
        int hash;
        if(!isEmpty() && (first = table[(table.length - 1) & (hash = hash(entry))]) != null) {
            if(first.hash == hash && (Objects.equals(entry, first.entry))){
                return first;
            }
            for(e = first.next; e != null; e = e.next){
                if(e.hash == hash && Objects.equals(entry, e.entry))
                    return e;
            }
        }
        return null;
    }

    @Override
    public boolean contains(Object o) {
        return getNode(o) != null;
    }

    class HashIterator implements Iterator<E>{
        Node<E> next;
        Node<E> current;
        int index;

        HashIterator(){
            current = next = null;
            index = 0;
            if(table != null && size > 0){
                do {} while(index < table.length && (next = table[index++]) == null);
            }
        }

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public E next() {
            Node<E> e = next;
            if(e == null){
                throw new NoSuchElementException();
            }
            if((next = (current = e).next) == null && table != null){
                do {} while(index < table.length && (next = table[index++]) == null);
            }
            return e.entry;
        }

        public void remove(){
            Node<E> p = current;
            if(p == null){
                throw new IllegalStateException();
            }
            current = null;
            removeObject(p.entry);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return new HashIterator();
    }

    final Node<E>[] resize() {
        Node<E>[] old_tab = table;
        int old_cap = (old_tab == null) ? 0 : old_tab.length;
        int old_thr = threshold;
        int new_cap, new_thr = 0;

        if (old_cap > 0) {
            if (old_cap >= MAXIMUM_CAPACITY) {
                threshold = Integer.MAX_VALUE;
                return old_tab;
            } else if ((new_cap = old_cap << 1) < MAXIMUM_CAPACITY && old_cap >= DEFAULT_INITIAL_CAPACITY) {
                new_thr = old_thr << 1;
            }
        } else if (old_thr > 0) {
            new_cap = old_thr;
        } else {
            new_cap = DEFAULT_INITIAL_CAPACITY;
            new_thr = (int) (DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY);
        }

        if (new_thr == 0) {
            float ft = (float) new_cap * load_factor;
            new_thr = (new_cap < MAXIMUM_CAPACITY && ft < (float) MAXIMUM_CAPACITY ? (int) ft : Integer.MAX_VALUE);
        }
        threshold = new_thr;

        @SuppressWarnings({"unchecked"})
        Node<E>[] new_tab = (Node<E>[]) new Node[new_cap];
        table = new_tab;

        if (old_tab != null) {
            for (int j = 0; j < old_cap; ++j) {
                Node<E> e;
                if ((e = old_tab[j]) != null) {
                    old_tab[j] = null;
                    if (e.next == null) {
                        new_tab[e.hash & (new_cap - 1)] = e;
                    } else {
                        Node<E> loHead = null, loTail = null;
                        Node<E> hiHead = null, hiTail = null;
                        Node<E> next;
                        do {
                            next = e.next;
                            if ((e.hash & old_cap) == 0) {
                                if (loTail == null)
                                    loHead = e;
                                else
                                    loTail.next = e;
                                loTail = e;
                            } else {
                                if (hiTail == null)
                                    hiHead = e;
                                else
                                    hiTail.next = e;
                                hiTail = e;
                            }
                        } while ((e = next) != null);

                        if (loTail != null) {
                            loTail.next = null;
                            new_tab[j] = loHead;
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            new_tab[j + old_cap] = hiHead;
                        }
                    }
                }
            }
        }
        return new_tab;
    }

    @Override
    public boolean add(E o) {
        return addWithCheck(o, true, 1) > 0;
    }

    protected int addWithCheck(E o, boolean check, int count) { // check and count are for multiset implementation
        if (count <= 0) {
            return 0;
        }

        Node<E>[] tab;
        Node<E> p;
        int n, i;
        int hash = hash(o);

        if ((tab = table) == null || (n = tab.length) == 0) {
            n = (tab = resize()).length;
        }

        i = (n - 1) & hash;
        p = tab[i];

        if (p == null) {
            int numToAdd = check ? 1 : count;
            Node<E> head = new Node<>(null, o, hash);
            tab[i] = head;
            Node<E> current = head;
            for (int k = 1; k < numToAdd; k++) {
                Node<E> newNode = new Node<>(null, o, hash);
                current.next = newNode;
                current = newNode;
            }
            size += numToAdd;
            if (size > threshold) resize();
            return numToAdd;
        } else {
            Node<E> last = p;
            if (check) {
                while (true) {
                    if (last.hash == hash && (last.entry == o || Objects.equals(o, last.entry))) {
                        return 0;
                    }
                    if (last.next == null) {
                        break;
                    }
                    last = last.next;
                }
                last.next = new Node<>(null, o, hash);
                size++;
                if (size > threshold) resize();
                return 1;
            } else {
                while (last.next != null) {
                    last = last.next;
                }
                for (int k = 0; k < count; k++) {
                    Node<E> newNode = new Node<>(null, o, hash);
                    last.next = newNode;
                    last = newNode;
                }
                size += count;
                if (size > threshold) resize();
                return count;
            }
        }
    }




    final Node<E> removeObject(Object o){
        Node<E> p;
        int index;
        int hash = hash(o);
        if(!isEmpty() && (p = table[index = (table.length - 1) & hash]) != null){
            Node<E> node = null, e;
            if(p.hash == hash && (p.entry == o || Objects.equals(o, p.entry))){
                node = p;
            }
            else if((e = p.next) != null){
                do{
                    if (e.hash == hash && (e.entry == o || Objects.equals(o, e.entry))){
                        node = e;
                        break;
                    }
                    p = e;
                } while ((e = e.next) != null);
            }
            if(node != null){
                if(node == p){
                    table[index] = node.next;
                }
                else{
                    p.next = node.next;
                }
                --size;
                return node;
            }
        }
        return null;
    }

    @Override
    public boolean remove(Object o) {
        return removeObject(o) != null;
    }

    @Override
    public void clear() {
        if(!isEmpty()){
            size = 0;
            for(int i = 0; i < table.length; i++){
                table[i] = null;
            }
        }
    }

    @Override
    public Object[] toArray() {
        return toArray(new Object[size]);
    }

    // Кусок кода украден из оригинальной имплементации HashMap потому что рефлексия это чет сложное и я таким еще не занимался
    @SuppressWarnings("unchecked")
    <T> T[] prepareArray(T[] a) {
        int size = this.size;
        if (a.length < size) {
            return (T[]) java.lang.reflect.Array
                    .newInstance(a.getClass().getComponentType(), size);
        }
        if (a.length > size) {
            a[size] = null;
        }
        return a;
    }

    @Override
    public <T> T[] toArray(T[] a) {
        Object[] r = prepareArray(a);
        if(size > 0) {
            int idx = 0;
            for(Node<E> e : table){
                for(; e != null; e = e.next){
                    r[idx++] = e.entry;
                }
            }
        }
        return a;
    }

}