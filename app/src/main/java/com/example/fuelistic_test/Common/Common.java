package com.example.fuelistic_test.Common;

import java.util.Random;

public class Common {


    public static String createOrderNumber(){
        return new StringBuilder()
                .append(System.currentTimeMillis())
                .append(Math.abs(new Random().nextInt()))
                .toString();
    }
}
