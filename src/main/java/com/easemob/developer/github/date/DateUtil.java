package com.easemob.developer.github.date;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

/**
 * @author stliu at apache.org
 * @since 6/4/15
 */
public class DateUtil {
    public static void main(String[] args) {
//        int w = 1432425600;
//        Instant i =  Instant.ofEpochSecond(w);
//        System.out.println(i.toString());

       Instant now =  Instant.now();

        System.out.println(now);
        System.out.println(LocalDate.of(2015,5,1).atStartOfDay());
    }

}
