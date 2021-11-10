package edu.neu.coe.info6205.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * TODO tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);
        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        Random random = new Random();
        int[] array;
        ArrayList<Long> timeList = new ArrayList<>();
//        for(int arraySize = 1;arraySize<11;arraySize=(arraySize+1)*2){
//            System.out.println("array size is: "+arraySize*1000000);
//            array = new int[arraySize*1000000];
//            for (int j = 1; j < 100; j++) {
//                ParSort.cutoff = 10000 * (j + 1);
//                ParSort.forkpool = new ForkJoinPool(16);
//                // for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
//                long time;
//                long startTime = System.currentTimeMillis();
//                for (int t = 0; t < 10; t++) {
//                    for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
//                    ParSort.sort(array, 0, array.length);
//                }
//                long endTime = System.currentTimeMillis();
//                time = (endTime - startTime);
//                timeList.add(time);
//
//                System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");
//
//            }
//        }
        //from the excel analysis
        int[] cutoffSize = {130000,540000,660000};
        int index = 0;
        int thread = 1;
        for(int arraySize = 1;arraySize<11;arraySize=(arraySize+1)*2){
            array = new int[arraySize*1000000];
            ParSort.cutoff = cutoffSize[index++];
            thread = 1;
            for(int cycle = 0;cycle<5;cycle++){
                thread = thread*2;
                System.out.println("thread num " + thread);
                ParSort.forkpool = new ForkJoinPool(thread);
                for (int j = 0; j < 10; j++) {
                    long time;
                    long startTime = System.currentTimeMillis();
                    for (int t = 0; t < 10; t++) {
                        for (int i = 0; i < array.length; i++) array[i] = random.nextInt(10000000);
                        ParSort.sort(array, 0, array.length);
                    }
                    long endTime = System.currentTimeMillis();
                    time = (endTime - startTime);
                    timeList.add(time);
//                System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");

                }
            }
        }
//        try {
//            FileOutputStream fis = new FileOutputStream("./src/result.csv");
//            OutputStreamWriter isr = new OutputStreamWriter(fis);
//            BufferedWriter bw = new BufferedWriter(isr);
//            int j = 1;
//            for (long i : timeList) {
//                String content = (double) 10000 * (j + 1) + "," + i  + "\n";
//                if(j==99){
//                    j=1;
//                }
//                    j++;
//                bw.write(content);
//                bw.flush();
//            }
//            bw.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        try {
            FileOutputStream fis = new FileOutputStream("./src/result.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 1;
            int cycle = 0;
            for (long i : timeList) {
                String content = j*2 + "," + i  + "\n";
                cycle++;
                if(cycle%10 == 0){
                    j*=2;
                    if(j == 64){
                        j = 1;
                    }
                }
                bw.write(content);
                bw.flush();
            }
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processArgs(String[] args) {
//        String[] xs = args;
//        while (xs.length > 0)
//            if (xs[0].startsWith("-")) xs = processArg(xs);
//        ParSort.cutoff = Integer.parseInt(args[0]);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
            // TODO sort this out
            if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
                ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();

}
