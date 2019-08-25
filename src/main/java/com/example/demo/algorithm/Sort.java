package com.example.demo.algorithm;

import java.util.Arrays;

public class Sort {

    public static void bubbleSort(Integer[] arr) {
        int length = arr.length;
        int temp = 0;
        for (int i=0; i<length-1; i++){
            for (int j=0; j<length-1-i; j++){
                if (arr[j] < arr[j+1]) {
                    temp = arr[j];
                    arr[j] = arr[j+1];
                    arr[j+1] = temp;
                }
            }
            System.out.println("第" + (i+1) + "趟：" + Arrays.asList(arr));
        }
    }

    public static void main(String[] args) {
        Integer[] arr = new Integer[]{12,34,32,1,3,76,98,54,31};
        bubbleSort(arr);
    }

}
