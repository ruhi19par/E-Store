package com.example.lcwd.Electronic.config;

public class BinarySearchAlgorithm {

    public static int binarySearch(int[] sortedNumbers, int target) {
        int left = 0;
        int right = sortedNumbers.length - 1;

        while (left <= right) {
            int middle = left + (right - left) / 2;

            if (sortedNumbers[middle] == target) {
                return middle;
            }

            if (sortedNumbers[middle] < target) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        int[] numbers = {2, 4, 6, 8, 10, 12, 14};
        int target = 10;

        int result = binarySearch(numbers, target);

        if (result == -1) {
            System.out.println("Element not found");
        } else {
            System.out.println("Element found at index: " + result);
        }
    }
}
