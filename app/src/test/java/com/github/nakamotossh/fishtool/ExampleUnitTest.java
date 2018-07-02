package com.github.nakamotossh.fishtool;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    @Test
    public void initUnitTest(){
        System.out.println(-1 + 3);
    }

    public boolean check(){
        if (Float.MIN_VALUE > 0){
            return true;
        }
        return false;
    }

    public void calcTime(){

        List<Long> timeList = new ArrayList<>();



        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();

            if (0.002 > 0 ){
                System.out.println();
            }
            long end = System.nanoTime();

            long time = end - start;
            timeList.add(time);
        }

        long sum = 0;
        for (int i = 0; i < timeList.size(); i++) {
            sum += timeList.get(i);
        }

        List<Long> timeList2 = new ArrayList<>();



        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();

            if (0.002f > 0 ){
                System.out.println();
            }
            long end = System.nanoTime();

            long time = end - start;
            timeList2.add(time);
        }

        long sum2 = 0;
        for (int i = 0; i < timeList.size(); i++) {
            sum2 += timeList.get(i);
        }

        List<Long> timeList3 = new ArrayList<>();



        for (int i = 0; i < 10; i++) {
            long start = System.nanoTime();

            if (0.002 > Float.MIN_VALUE){
                System.out.println();
            }
            long end = System.nanoTime();

            long time = end - start;
            timeList3.add(time);
        }

        long sum3 = 0;
        for (int i = 0; i < timeList.size(); i++) {
            sum3 += timeList.get(i);
        }

        List<Long> timeList4 = new ArrayList<>();



        for (int i = 0; i < 12; i++) {
            long start = System.nanoTime();

            if (0.002f > Float.MIN_VALUE){
                System.out.println();
            }
            long end = System.nanoTime();

            long time = end - start;
            timeList4.add(time);
        }

        long sum4 = 0;
        for (int i = 0; i < timeList.size(); i++) {
            sum4 += timeList.get(i);
        }

        System.out.println((sum / timeList.size()));
        System.out.println((sum2 / timeList2.size()));
        System.out.println((sum3 / timeList3.size()));
        System.out.println((sum4 / timeList4.size()));
    }
}