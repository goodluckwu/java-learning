package io.github.wuzhihao7.sort;

import java.awt.*;
import java.util.Arrays;

/**
 * 交换法
 */
public class QuickSort2 {
    public static void main(String[] args) {
        int[] array = {9, 1, 5, 3, 6, 2, 7};
        System.out.println(Arrays.toString(array));
        quickSort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
    }

    private static void quickSort(int[] array, int left, int right) {
        if(left < right){
            int index = partition(array, left, right);
            quickSort(array, left,index - 1);
            quickSort(array, index + 1, right);
        }
    }

    private static int partition(int[] array, int left, int right) {
        int pivot = array[left];
        int pivotIndex = left;
        while (left < right){
            while (left < right && array[right] >= pivot){
                right--;
            }
            while (left < right && array[left] <= pivot){
                left++;
            }
            if(left >= right){
                break;
            }
            swap(array, left, right);
        }
        swap(array, pivotIndex, left);
        return left;
    }

    private static void swap(int[] array, int left, int right) {
        int temp = array[left];
        array[left] = array[right];
        array[right] = temp;
    }
}
