package com.mkass420.myhashset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

class MyHashMultisetTest {

    private MyHashMultiset<Integer> multiset;

    @BeforeEach
    void setUp() {
        multiset = new MyHashMultiset<>();
    }

    @Test
    void testAddAllowsDuplicates() {
        boolean firstAdd = multiset.add(5);
        boolean secondAdd = multiset.add(5);

        assertTrue(firstAdd);
        assertTrue(secondAdd, "Multiset should always return true when adding");
        assertEquals(2, multiset.size());
        assertEquals(2, multiset.count(5));
    }

    @Test
    void testAddWithCount() {
        int addedCount = multiset.add(10, 3);

        assertEquals(3, addedCount);
        assertEquals(3, multiset.size());
        assertEquals(3, multiset.count(10));
    }

    @Test
    void testCount() {
        multiset.add(7, 4);
        multiset.add(8);

        assertEquals(4, multiset.count(7));
        assertEquals(1, multiset.count(8));
        assertEquals(0, multiset.count(99));
    }

    @Test
    void testRemoveSingleInstance() {
        multiset.add(5, 3); // {5, 5, 5}

        boolean removed = multiset.remove(5);

        assertTrue(removed);
        assertEquals(2, multiset.size());
        assertEquals(2, multiset.count(5));
    }

    @Test
    void testRemoveWithCount() {
        multiset.add(7, 5); // {7, 7, 7, 7, 7}

        int removedCount = multiset.remove(7, 3);

        assertEquals(3, removedCount);
        assertEquals(2, multiset.size());
        assertEquals(2, multiset.count(7));
    }

    @Test
    void testRemoveWithCountMoreThanPresent() {
        multiset.add(7, 2);

        int removedCount = multiset.remove(7, 5);

        assertEquals(2, removedCount, "Should only remove the elements that exist");
        assertEquals(0, multiset.size());
        assertTrue(multiset.isEmpty());
    }

    @Test
    void testSetCountToIncrease() {
        multiset.add(8, 2);

        int previousCount = multiset.setCount(8, 5);

        assertEquals(2, previousCount);
        assertEquals(5, multiset.count(8));
        assertEquals(5, multiset.size());
    }

    @Test
    void testSetCountToDecrease() {
        multiset.add(8, 5);

        int previousCount = multiset.setCount(8, 2);

        assertEquals(5, previousCount);
        assertEquals(2, multiset.count(8));
        assertEquals(2, multiset.size());
    }

    @Test
    void testRemoveAllRespectsCounts() {
        multiset.add(2, 5);
        multiset.add(6);

        multiset.removeAll(Arrays.asList(1, 2, 2, 2, 2, 3));

        assertEquals(1, multiset.count(2));
        assertEquals(1, multiset.count(6));
        assertEquals(2, multiset.size());
    }

    @Test
    void testAddAllWithDuplicates() {
        multiset.add(1);
        multiset.add(2);

        multiset.addAll(Arrays.asList(2, 3, 4));

        assertEquals(5, multiset.size());
        assertEquals(1, multiset.count(1));
        assertEquals(2, multiset.count(2));
        assertEquals(1, multiset.count(3));
        assertEquals(1, multiset.count(4));
    }

    @Test
    void testRetainAllKeepsAllOccurrences() {
        multiset.add(2, 5);
        multiset.add(7);
        multiset.add(6);

        boolean changed = multiset.retainAll(Arrays.asList(2, 4, 6));

        assertTrue(changed);
        assertEquals(6, multiset.size());
        assertEquals(5, multiset.count(2));
        assertEquals(1, multiset.count(6));
        assertEquals(0, multiset.count(7));
    }

    @Test
    void testToArrayWithDuplicates() {
        multiset.add(10);
        multiset.add(20, 2);
        multiset.add(30);

        Object[] array = multiset.toArray();
        Arrays.sort(array);

        assertEquals(4, array.length);
        assertArrayEquals(new Object[]{10, 20, 20, 30}, array);
    }
}