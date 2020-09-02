package com.example.demo.point.algorithm;

import java.util.Arrays;

public class Sort {
    /**
     * 核心思想：相邻元素比较交换
     * @param arr
     */
    public static void bubbleSort(Integer[] arr) {
        int length = arr.length;
        for (int i = 0; i < length - 1; i++) {
            for (int j = 0; j < length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
            System.out.println("冒泡排序第" + (i+1) + "趟：" + Arrays.asList(arr));
        }
    }

    /**
     * 核心思想：选取最大元素往高位排
     * @param arr
     */
    public static void selectSort(Integer[] arr) {
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            int max = arr[0];
            int position = 0;
            for (int j = 0; j < length-1-i; j++) {
                if (arr[j+1] > max) {
                    max = arr[j+1];
                    position = j+1;
                }
            }
            arr[position] = arr[length-i-1];
            arr[length-i-1] = max;
            System.out.println("简单选择排序第" + (i+1) + "趟：" + Arrays.asList(arr));
        }
    }

    /**
     * 核心思想：从第二个元素开始，与前面元素比较插入，直到最后一个元素
     * @param arr
     */
    public static void insertSort(Integer[] arr) {
        int length = arr.length;
        for(int i = 0; i < length-1; i++) {
            int j = i;
            int insertNum = arr[j+1];
            while(j >= 0 && arr[j] > insertNum) {
                arr[j+1] = arr[j];
                j--;
            }
            arr[j+1] = insertNum;
            System.out.println("直接插入排序第" + (i+1) + "趟：" + Arrays.asList(arr));
        }
    }

    /**
     * 核心思想：分组之后再用直接插入排序
     * @param arr
     */
    public static void sheelSort(Integer[] arr) {
        int length = arr.length;
        while (length != 0) {
            length /= 2;
            for (int x = 0; x < length; x++) {//分的组数
                for (int i = x; i < arr.length-length; i += length) {
                    int j = i;
                    int insertNum = arr[j+length];
                    while (j >= 0 && arr[j] > insertNum) {
                        arr[j+length] = arr[j];
                        j -= length;
                    }
                    arr[j+length] = insertNum;
                }
                System.out.println("希尔排序第" + (x+1) + "组：" + Arrays.asList(arr));
            }
        }
    }

    public static void main(String[] args) {
        Integer[] arr = new Integer[]{12, 34, 32, 1, 3, 76, 98, 54, 31};
        bubbleSort(arr);
        selectSort(arr);
        insertSort(arr);
        sheelSort(arr);
    }
}
