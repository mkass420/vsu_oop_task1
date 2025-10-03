package com.mkass420.myhashset;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class MyHashSetTest {

    private MyHashSet<String> set;

    @BeforeEach
    void setUp() {
        set = new MyHashSet<>();
    }

    @Test
    void testAddElement() {
        boolean added = set.add("123");

        assertTrue(added);
        assertEquals(1, set.size());
        assertTrue(set.contains("123"));
    }

    @Test
    void testAddDuplicateElement() {
        set.add("123");

        boolean added = set.add("123");

        assertFalse(added);
        assertEquals(1, set.size());
    }

    @Test
    void testRemoveExistingElement() {
        set.add("123");

        boolean removed = set.remove("123");

        assertTrue(removed);
        assertEquals(0, set.size());
        assertFalse(set.contains("world"));
    }

    @Test
    void testRemoveNonExistentElement() {
        set.add("123");

        boolean removed = set.remove("456");

        assertFalse(removed);
        assertEquals(1, set.size());
    }

    @Test
    void testClear() {
        set.add("123");
        set.add("456");

        set.clear();

        assertTrue(set.isEmpty());
        assertEquals(0, set.size());
    }

    @Test
    void testIsEmpty() {
        assertTrue(set.isEmpty());
        set.add("element");
        assertFalse(set.isEmpty());
    }

    @Test
    void testAddAll() {
        set.add("1");
        set.add("2");

        boolean changed = set.addAll(Arrays.asList("2", "3", "4"));

        assertTrue(changed);
        assertEquals(4, set.size());
        assertTrue(set.contains(1));
        assertTrue(set.contains(2));
        assertTrue(set.contains(3));
        assertTrue(set.contains(4));
    }

    @Test
    void testRemoveAll() {
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");

        boolean changed = set.removeAll(Arrays.asList("2", "4", "5")); // 5 is not in the set

        // Assert
        assertTrue(changed);
        assertEquals(2, set.size());
        assertTrue(set.contains("1"));
        assertFalse(set.contains("2"));
        assertTrue(set.contains("3"));
        assertFalse(set.contains("4"));
    }

    @Test
    void testRetainAll() {
        set.add("1");
        set.add("2");
        set.add("3");
        set.add("4");

        boolean changed = set.retainAll(Arrays.asList("2", "4", "6")); // 6 is not in the set

        assertTrue(changed);
        assertEquals(2, set.size());
        assertFalse(set.contains(1));
        assertTrue(set.contains(2));
        assertFalse(set.contains(3));
        assertTrue(set.contains(4));
    }

    @Test
    void testToArray() {
        set.add("10");
        set.add("20");
        set.add("30");

        Object[] array = set.toArray();
        Arrays.sort(array);

        assertEquals(3, array.length);
        assertArrayEquals(new Object[]{"10", "20", "30"}, array);
    }
}