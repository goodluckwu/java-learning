package io.github.wuzhihao7.sort;

import java.util.Arrays;

/**
 * 填坑法
 */
public class QuickSort {
    public static void main(String[] args) {
        int[] array = {4, 2, 5, 9, 6, 3};
        System.out.println(Arrays.toString(array));
        quickSort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
    }

    private static void quickSort(int[] array, int left, int right) {
        if(left < right){
            int index = partition(array, left, right);
            quickSort(array,left,index-1);
            quickSort(array, index + 1, right);
        }
    }

    private static int partition(int[] array, int left, int right) {
        int pivot = array[left];
        while (left < right){
            while (left < right && array[right] >= pivot){
                right--;
            }
            //填到基准值的坑位
            if(left < right){
                array[left] = array[right];
            }
            while (left < right && array[left] <= pivot){
                left++;
            }
            //填到右边的坑位
            if(left < right){
                array[right] = array[left];
            }
        }
        //把基准值放回去
        array[left] = pivot;
        return left;
    }
}
