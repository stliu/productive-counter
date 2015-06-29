package com.easemob.developer.github.date;

import java.time.Instant;
import java.time.LocalDate;

/**
 * @author stliu at apache.org
 * @since 6/4/15
 */
public class DateUtil {
    public static void main(String[] args) {
        int w = 1432425600;
        Instant i =  Instant.ofEpochSecond(w);
        System.out.println(i.toString());
    }

}
