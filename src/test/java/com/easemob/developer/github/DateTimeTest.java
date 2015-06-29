package com.easemob.developer.github;

import org.junit.Test;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author stliu at apache.org
 * @since 6/9/15
 */
public class DateTimeTest {
    @Test
    public void testRepositoryCreatedTime() {
        //这个是获取repos返回的repo的timestamp
        //需要根据这个来转化成date类型
        long created = 1431999850000l;
        Instant instant = Instant.ofEpochMilli(created);
        System.out.println(instant);

        System.out.println("=======");
        String s = "2013-07-04T06:26:35Z";

        Instant i = Instant.parse(s);
        Instant j = i.plus(Period.ofDays(1));//加一天
        Instant k = j.truncatedTo(ChronoUnit.DAYS);//得到当前的0点
        System.out.println(i);
        System.out.println(j);
        System.out.println(k);
    }

    /**
     * 目的: 给一个起始时间和一个结束时间, 返回一个按天分割的时间序列
     */
    @Test
    public void testDateTime() {
        String s = "2013-07-04T06:26:35Z";

        Instant i = Instant.parse(s);
        Instant j = i.plus(Period.ofDays(1));//加一天
        Instant k = j.truncatedTo(ChronoUnit.DAYS);//得到当前的0点

        Instant z = Instant.now();
        System.out.println(z);
        z = z.truncatedTo(ChronoUnit.SECONDS);
        System.out.println(z);
        System.out.println("=========");



        List<Instant> list = resolve(Instant.parse("2015-06-04T06:26:35Z"), z);
        list.forEach(System.out::println);

    }

    public List<Instant> resolve(Instant start, Instant end) {
        List<Instant> list = new ArrayList<>();
        list.add(start);
        Instant next = start.plus(Period.ofDays(1)).truncatedTo(ChronoUnit.DAYS);
        while (next.compareTo(end) < 0) {
            list.add(next);
            next = next.plus(Period.ofDays(1)).truncatedTo(ChronoUnit.DAYS);
        }
        list.add(end);
        return list;
    }

    public Instant nextDay(Instant start, Instant end){
        Instant next = start.plus(Period.ofDays(1)).truncatedTo(ChronoUnit.DAYS);
        return next.compareTo(end) < 0 ? next : end;
    }
}
