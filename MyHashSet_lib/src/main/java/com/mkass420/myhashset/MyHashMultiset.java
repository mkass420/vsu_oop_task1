package com.mkass420.myhashset;

import java.util.Collection;
import java.util.Objects;

public class MyHashMultiset<E> extends MyHashSet<E>{
    @Override
    public boolean add(E o) {
        return addWithCheck(o, false, 1) > 0;
    }

    public int add(E o, int count){
        return addWithCheck(o, false, count);
    }

    public int count(E o){
        int count = 0;
        Node<E> e = table[(table.length - 1) & hash(o)];
        if(e != null){
            do {
                if(Objects.equals(e.entry, o)) ++count;
            } while((e = e.next) != null);
        }
        return count;
    }

    public int remove(E o, int count) {
        if (count <= 0) {
            return 0;
        }

        Node<E> p;
        int index;
        int removedCount = 0;
        int hash = hash(o);

        if (!isEmpty() && (p = table[index = (table.length - 1) & hash]) != null) {
            while (p != null && removedCount < count && p.hash == hash && Objects.equals(p.entry, o)) {
                removedCount++;
                --size;
                p = p.next;
            }
            table[index] = p;

            if (p != null && removedCount < count) {
                Node<E> e = p.next;
                while (e != null && removedCount < count) {
                    if (e.hash == hash && Objects.equals(e.entry, o)) {
                        p.next = e.next;
                        removedCount++;
                        --size;
                    } else {
                        p = e;
                    }
                    e = p.next;
                }
            }
        }
        return removedCount;
    }

    public int setCount(E o, int count){
        int numberBefore = count(o);
        if(numberBefore < count)
            add(o, count - numberBefore);
        else
            remove(o, numberBefore - count);
        return numberBefore;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        Objects.requireNonNull(c);
        boolean modified = false;
        for (Object o : c) {
            if (remove(o)) {
                modified = true;
            }
        }
        return modified;
    }
}
