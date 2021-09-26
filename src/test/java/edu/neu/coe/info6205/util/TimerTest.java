package edu.neu.coe.info6205.util;

import edu.neu.coe.info6205.sort.elementary.InsertionSort;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static org.junit.Assert.*;

public class TimerTest {

    @Before
    public void setup() {
        pre = 0;
        run = 0;
        post = 0;
    }

    @Test
    public void testStop() {
        final Timer timer = new Timer();
        GoToSleep(TENTH, 0);
        final double time = timer.stop();
        assertEquals(TENTH_DOUBLE, time, 10);
        assertEquals(1, run);
        assertEquals(1, new PrivateMethodTester(timer).invokePrivate("getLaps"));
    }

    @Test
    public void testPauseAndLap() {
        final Timer timer = new Timer();
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(timer);
        GoToSleep(TENTH, 0);
        timer.pauseAndLap();
        final Long ticks = (Long) privateMethodTester.invokePrivate("getTicks");
        assertEquals(TENTH_DOUBLE, ticks / 1e6, 12);
        assertFalse((Boolean) privateMethodTester.invokePrivate("isRunning"));
        assertEquals(1, privateMethodTester.invokePrivate("getLaps"));
    }

    @Test
    public void testPauseAndLapResume0() {
        final Timer timer = new Timer();
        final PrivateMethodTester privateMethodTester = new PrivateMethodTester(timer);
        GoToSleep(TENTH, 0);
        timer.pauseAndLap();
        timer.resume();
        assertTrue((Boolean) privateMethodTester.invokePrivate("isRunning"));
        assertEquals(1, privateMethodTester.invokePrivate("getLaps"));
    }

    @Test
    public void testPauseAndLapResume1() {
        final Timer timer = new Timer();
        GoToSleep(TENTH, 0);
        timer.pauseAndLap();
        GoToSleep(TENTH, 0);
        timer.resume();
        GoToSleep(TENTH, 0);
        final double time = timer.stop();
        assertEquals(TENTH_DOUBLE, time, 10.0);
        assertEquals(3, run);
    }

    @Test
    public void testLap() {
        final Timer timer = new Timer();
        GoToSleep(TENTH, 0);
        timer.lap();
        GoToSleep(TENTH, 0);
        final double time = timer.stop();
        assertEquals(TENTH_DOUBLE, time, 10.0);
        assertEquals(2, run);
    }

    @Test
    public void testPause() {
        final Timer timer = new Timer();
        GoToSleep(TENTH, 0);
        timer.pause();
        GoToSleep(TENTH, 0);
        timer.resume();
        final double time = timer.stop();
        assertEquals(TENTH_DOUBLE, time, 10.0);
        assertEquals(2, run);
    }

    @Test
    public void testMillisecs() {
        final Timer timer = new Timer();
        GoToSleep(TENTH, 0);
        timer.stop();
        final double time = timer.millisecs();
        assertEquals(TENTH_DOUBLE, time, 10.0);
        assertEquals(1, run);
    }

    @Test
    public void testRepeat1() {
        final Timer timer = new Timer();
        final double mean = timer.repeat(10, () -> {
            GoToSleep(HUNDREDTH, 0);
            return null;
        });
        assertEquals(10, new PrivateMethodTester(timer).invokePrivate("getLaps"));
        assertEquals(TENTH_DOUBLE / 10, mean, 10);
        assertEquals(10, run);
        assertEquals(0, pre);
        assertEquals(0, post);
    }

    @Test
    public void testRepeat2() {
        final Timer timer = new Timer();
        final int zzz = 20;
        final double mean = timer.repeat(10, () -> zzz, t -> {
            GoToSleep(t, 0);
            return null;
        });
        assertEquals(10, new PrivateMethodTester(timer).invokePrivate("getLaps"));
        assertEquals(zzz, mean, 8.5);
        assertEquals(10, run);
        assertEquals(0, pre);
        assertEquals(0, post);
    }

    @Test // Slow
    public void testRepeat3() {
        final Timer timer = new Timer();
        final int zzz = 20;
        final double mean = timer.repeat(10, () -> zzz, t -> {
            GoToSleep(t, 0);
            return null;
        }, t -> {
            GoToSleep(t, -1);
            return t;
        }, t -> GoToSleep(10, 1));
        assertEquals(10, new PrivateMethodTester(timer).invokePrivate("getLaps"));
        assertEquals(zzz, mean, 6);
        assertEquals(10, run);
        assertEquals(10, pre);
        assertEquals(10, post);
    }

    int pre = 0;
    int run = 0;
    int post = 0;

    private void GoToSleep(long mSecs, int which) {
        try {
            Thread.sleep(mSecs);
            if (which == 0) run++;
            else if (which > 0) post++;
            else pre++;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static final int TENTH = 100;
    public static final double TENTH_DOUBLE = 100;
    public static final int HUNDREDTH = 10;

//    public static void main(String[] args) {
//        randomOrder();
//        order();
//        partialOrder();
//        reverseOrder();
//    }
//
//
//    public static void randomOrder(){
//        int max = 100000;
//        int repetition = 100;
//        Integer[] arr;
//        int length = 10000;
//        for(int i = 0;i<5;i++){
//            arr = new Integer[length];
//            for (int j = 0; j < length; j++) {
//                arr[j] = (ThreadLocalRandom.current().nextInt(0, max + 1));
//            }
//            Integer[] finalArr = arr;
//            Benchmark<Integer> benchmark = new Benchmark_Timer<>("random", a->new InsertionSort<Integer>().sort(finalArr,0,finalArr.length));
//            double mean = benchmark.run(1,repetition);
//            System.out.println("random sort with "+length+" num "+mean);
//            length*=2;
//        }
//    }
//
//    public static void order(){
//        int max = 100000;
//        int repetition = 100;
//        Integer[] arr;
//        int length = 10000;
//        for(int i = 0;i<5;i++){
//            arr = new Integer[length];
//            for (int j = 0; j < length; j++) {
//                arr[j] = (ThreadLocalRandom.current().nextInt(0, max + 1));
//            }
//            new InsertionSort<Integer>().sort(arr,0,arr.length);
//            Integer[] finalArr = arr;
//            Benchmark<Integer> benchmark = new Benchmark_Timer<>("order sort", a->new InsertionSort<Integer>().sort(finalArr,0,finalArr.length));
//            double mean = benchmark.run(1,repetition);
//            System.out.println("order sort with "+length+" num "+mean);
//            length*=2;
//        }
//    }
//
//    public static void reverseOrder(){
//        int max = 100000;
//        int repetition = 100;
//        Integer[] arr;
//        int length = 10000;
//        for(int i = 0;i<5;i++){
//            arr = new Integer[length];
//            for (int j = 0; j < length; j++) {
//                arr[j] = (ThreadLocalRandom.current().nextInt(0, max + 1));
//            }
//            new InsertionSort<Integer>().sort(arr,0,arr.length);
//            Arrays.sort(arr, Collections.reverseOrder());
//            Integer[] finalArr = arr;
//            Benchmark<Integer> benchmark = new Benchmark_Timer<>("reverse order", a->new InsertionSort<Integer>().sort(finalArr,0,finalArr.length));
//            double mean = benchmark.run(1,repetition);
//            System.out.println("reverse order with "+length+" num "+mean);
//            length*=2;
//        }
//    }
//
//    public static void partialOrder(){
//        int max = 100000;
//        int repetition = 100;
//        Integer[] arr;
//        int length = 10000;
//        for(int i = 0;i<5;i++){
//            arr = new Integer[length];
//            for (int j = 0; j < length; j++) {
//                arr[j] = (ThreadLocalRandom.current().nextInt(0, max + 1));
//            }
//            new InsertionSort<Integer>().sort(arr,0,arr.length/2);
//            Integer[] finalArr = arr;
//            Benchmark<Integer> benchmark = new Benchmark_Timer<>("partial order", a->new InsertionSort<Integer>().sort(finalArr,0,finalArr.length));
//            double mean = benchmark.run(1,repetition);
//            System.out.println("partial order with "+length+" num "+mean);
//            length*=2;
//        }
//    }
}