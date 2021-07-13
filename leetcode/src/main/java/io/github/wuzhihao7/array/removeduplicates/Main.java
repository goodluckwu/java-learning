package io.github.wuzhihao7.array.removeduplicates;

import java.rmi.Remote;
import java.util.Arrays;

/**
 * 给你一个有序数组 nums ，请你 原地 删除重复出现的元素，使每个元素 只出现一次 ，返回删除后数组的新长度。
 *
 * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
 *
 */
public class Main {
    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 3, 7};
        int count = removeDuplicates(nums);
        System.out.println(count);
        System.out.println(Arrays.toString(Arrays.copyOf(nums,count)));
        int count2 = removeDuplicates2(nums);
        System.out.println(count2);
        System.out.println(Arrays.toString(Arrays.copyOf(nums,count2)));
    }

    private static int removeDuplicates2(int[] nums) {
        if(nums == null){
            return 0;
        }
        int count = 0;
        for(int i = 1; i < nums.length; i++){
            if(nums[i] == nums[i-1]){
                count++;
            }else{
                nums[i - count] = nums[i];
            }
        }
        return nums.length - count;
    }

    /**
     * 双指针解决
     * 使用两个指针，右指针始终往右移动，
     *
     * 如果右指针指向的值等于左指针指向的值，左指针不动。
     * 如果右指针指向的值不等于左指针指向的值，那么左指针往右移一步，然后再把右指针指向的值赋给左指针。
     *
     */
    public static int removeDuplicates(int[] nums){
        if(nums == null){
            return 0;
        }
        int left = 0;
        for(int right = 1; right < nums.length; right++){
            if(nums[left] != nums[right]){
                nums[++left] = nums[right];
            }
        }
        return ++left;
    }
}
