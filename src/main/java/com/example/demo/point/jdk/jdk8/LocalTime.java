package com.example.demo.point.jdk.jdk8;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;

public class LocalTime {
    public static void main(String[] args) {
        LocalDate nowDate = LocalDate.now();
        System.out.println("nowDate: " + nowDate);
        LocalDate date = LocalDate.of(2019, 06, 03);
        System.out.println("date: " + date);
        LocalDate dateParse = LocalDate.parse("2015-10-06");
        System.out.println("dateParse: " + dateParse);

        System.out.println(nowDate.with(TemporalAdjusters.firstDayOfMonth()));
        System.out.println(nowDate.withDayOfMonth(2));
        System.out.println(nowDate.with(TemporalAdjusters.lastDayOfMonth()));
        System.out.println(nowDate.with(TemporalAdjusters.lastDayOfMonth()).plusDays(1));
        System.out.println(nowDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.FRIDAY)));
        System.out.println(nowDate.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY)));

        java.time.LocalTime nowTime = java.time.LocalTime.now();
        System.out.println("nowTime: " + nowTime);
        java.time.LocalTime time = java.time.LocalTime.of(23, 33, 55);
        System.out.println("time: " + time);

        LocalDateTime nowDateTime = LocalDateTime.now();
        System.out.println("nowDateTime: " + nowDateTime);
        LocalDateTime dateTime = LocalDateTime.of(2019, 9, 9, 12, 34, 22);
    }
}
