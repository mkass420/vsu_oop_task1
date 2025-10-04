package com.mkass420.myhashset;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Demo {
    static Scanner sc = new Scanner(System.in);

    private static String getString(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().strip();
    }

    private static List<String> getWordList(String prompt) {
        System.out.print(prompt);
        String line = sc.nextLine().strip();
        return Arrays.stream(line.split("\\s+"))
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    private static int getInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().strip();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    public static void hashSetDemo() {
        MyHashSet<String> set = new MyHashSet<>();
        System.out.println("--- MyHashSet Demo ---");

        while (true) {
            System.out.println("Current set: " + set);
            System.out.println("Choose MyHashSet method to demo:");
            System.out.println("\t1 - add(object)");
            System.out.println("\t2 - remove(object)");
            System.out.println("\t3 - contains(object)");
            System.out.println("\t4 - addAll(collection)");
            System.out.println("\t5 - removeAll(collection)");
            System.out.println("\t6 - retainAll(collection)");
            System.out.println("\t7 - clear()");
            System.out.println("\tb - Back to main menu");

            String choice = getString("Input value: ");
            switch (choice) {
                case "1":
                    set.add(getString("Enter string to add: "));
                    break;
                case "2":
                    set.remove(getString("Enter string to remove: "));
                    break;
                case "3":
                    boolean result = set.contains(getString("Enter string to check: "));
                    System.out.println("Result: " + result);
                    break;
                case "4":
                    set.addAll(getWordList("Enter space-separated strings to add: "));
                    break;
                case "5":
                    set.removeAll(getWordList("Enter space-separated strings to remove: "));
                    break;
                case "6":
                    set.retainAll(getWordList("Enter space-separated strings to retain: "));
                    break;
                case "7":
                    set.clear();
                    break;
                case "b":
                    System.out.println("Returning to main menu");
                    return;
                default:
                    System.out.println("Please input a correct value.");
            }
        }
    }

    public static void hashMultisetDemo() {
        MyHashMultiset<String> multiset = new MyHashMultiset<>();
        System.out.println("--- MyHashMultiset Demo ---");

        while (true) {
            System.out.println("Current multiset: " + multiset);
            System.out.println("Choose MyHashMultiset method to demo:");
            System.out.println("\t1 - add(object)");
            System.out.println("\t2 - add(object, count)");
            System.out.println("\t3 - remove(object)");
            System.out.println("\t4 - remove(object, count)");
            System.out.println("\t5 - count(object)");
            System.out.println("\t6 - setCount(object, count)");
            System.out.println("\t7 - addAll(collection)");
            System.out.println("\t8 - removeAll(collection)");
            System.out.println("\t9 - retainAll(collection)");
            System.out.println("\t10 - clear()");
            System.out.println("\tb. Back to main menu");

            String choice = getString("Input value: ");
            String element;
            int count;

            switch (choice) {
                case "1":
                    multiset.add(getString("Enter string to add: "));
                    break;
                case "2":
                    element = getString("Enter string to add: ");
                    count = getInt("Enter count: ");
                    multiset.add(element, count);
                    break;
                case "3":
                    multiset.remove(getString("Enter string to remove: "));
                    break;
                case "4":
                    element = getString("Enter string to remove: ");
                    count = getInt("Enter count: ");
                    multiset.remove(element, count);
                    break;
                case "5":
                    int result = multiset.count(getString("Enter string to count: "));
                    System.out.println("Result: " + result);
                    break;
                case "6":
                    element = getString("Enter string to set count for: ");
                    count = getInt("Enter new count: ");
                    multiset.setCount(element, count);
                    break;
                case "7":
                    multiset.addAll(getWordList("Enter space-separated strings to add: "));
                    break;
                case "8":
                    multiset.removeAll(getWordList("Enter space-separated strings to remove: "));
                    break;
                case "9":
                    multiset.retainAll(getWordList("Enter space-separated strings to retain: "));
                    break;
                case "10":
                    multiset.clear();
                    break;
                case "b":
                    System.out.println("Returning to main menu");
                    return;
                default:
                    System.out.println("Please input a correct value.");
            }
        }
    }

    public static void main(String[] args) {
        while(true) {
            System.out.println("Choose structure to demo:");
            System.out.println("\t1 - MyHashSet");
            System.out.println("\t2 - MyHashMultiset");
            System.out.println("\tq - exit");
            String choice = getString("Input value: ");
            switch (choice) {
                case "1":
                    hashSetDemo();
                    break;
                case "2":
                    hashMultisetDemo();
                    break;
                case "q":
                    System.out.println("Exiting demo.");
                    System.exit(0);
                default:
                    System.out.println("Please input a correct value.");
            }
        }
    }
}
