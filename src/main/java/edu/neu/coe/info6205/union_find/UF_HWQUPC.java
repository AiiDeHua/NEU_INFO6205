/**
 * Original code:
 * Copyright © 2000–2017, Robert Sedgewick and Kevin Wayne.
 * <p>
 * Modifications:
 * Copyright (c) 2017. Phasmid Software
 */
package edu.neu.coe.info6205.union_find;

import java.util.Arrays;

/**
 * Height-weighted Quick Union with Path Compression
 */
public class UF_HWQUPC implements UF {
    /**
     * Ensure that site p is connected to site q,
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     */
    public void connect(int p, int q) {
        if (!isConnected(p, q)) union(p, q);
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     *
     * @param n               the number of sites
     * @param pathCompression whether to use path compression
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n, boolean pathCompression) {
        count = n;
        parent = new int[n];
        height = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            height[i] = 1;
        }
        this.pathCompression = pathCompression;
    }

    /**
     * Initializes an empty union–find data structure with {@code n} sites
     * {@code 0} through {@code n-1}. Each site is initially in its own
     * component.
     * This data structure uses path compression
     *
     * @param n the number of sites
     * @throws IllegalArgumentException if {@code n < 0}
     */
    public UF_HWQUPC(int n) {
        this(n, true);
    }

    public void show() {
        for (int i = 0; i < parent.length; i++) {
            System.out.printf("%d: %d, %d\n", i, parent[i], height[i]);
        }
    }

    /**
     * Returns the number of components.
     *
     * @return the number of components (between {@code 1} and {@code n})
     */
    public int components() {
        return count;
    }

    /**
     * Returns the component identifier for the component containing site {@code p}.
     *
     * @param p the integer representing one site
     * @return the component identifier for the component containing site {@code p}
     * @throws IllegalArgumentException unless {@code 0 <= p < n}
     */
    public int find(int p) {
        validate(p);
        // TO BE IMPLEMENTED

        while(p != parent[p]){
            if(pathCompression) doPathCompression(p);
            p=parent[p];
        }
        return p;
//        int root = p;
//        if (pathCompression) {
//            if (p == parent[p]) {
//                return p;
//            } else {
//                parent[p] = find(parent[p]);
//                return parent[p];
//            }
//        } else {
//            while (p != parent[p]) {
//                p = parent[p];
//            }
//            return p;
//        }
//        return root;
    }

    /**
     * Returns true if the the two sites are in the same component.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return {@code true} if the two sites {@code p} and {@code q} are in the same component;
     * {@code false} otherwise
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

    /**
     * Merges the component containing site {@code p} with the
     * the component containing site {@code q}.
     *
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws IllegalArgumentException unless
     *                                  both {@code 0 <= p < n} and {@code 0 <= q < n}
     */
    public void union(int p, int q) {
        // CONSIDER can we avoid doing find again?
        mergeComponents(find(p), find(q));
        count--;
    }

    @Override
    public int size() {
        return parent.length;
    }

    /**
     * Used only by testing code
     *
     * @param pathCompression true if you want path compression
     */
    public void setPathCompression(boolean pathCompression) {
        this.pathCompression = pathCompression;
    }

    @Override
    public String toString() {
        return "UF_HWQUPC:" + "\n  count: " + count +
                "\n  path compression? " + pathCompression +
                "\n  parents: " + Arrays.toString(parent) +
                "\n  heights: " + Arrays.toString(height);
    }

    // validate that p is a valid index
    private void validate(int p) {
        int n = parent.length;
        if (p < 0 || p >= n) {
            throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n - 1));
        }
    }

    private void updateParent(int p, int x) {
        parent[p] = x;
    }

    private void updateHeight(int p, int x) {
        height[p] += height[x];
    }

    /**
     * Used only by testing code
     *
     * @param i the component
     * @return the parent of the component
     */
    private int getParent(int i) {
        return parent[i];
    }

    private final int[] parent;   // parent[i] = parent of i
    private final int[] height;   // height[i] = height of subtree rooted at i
    private int count;  // number of components
    private boolean pathCompression;

    private void mergeComponents(int i, int j) {
        // TO BE IMPLEMENTED make shorter root point to taller one
        if (height[i] >= height[j]) {
            parent[j] = i;
            height[i] += height[j];
        } else {
            parent[i] = j;
            height[j] += height[i];
        }
    }

    /**
     * This implements the single-pass path-halving mechanism of path compression
     */
    private void doPathCompression(int i) {
        // TO BE IMPLEMENTED update parent to value of grandparent
        parent[i] = parent[parent[i]];
    }

//    public static int count(){
//
//    }

    public static int count(int n){
        UF h = new UF_HWQUPC(n);
        int countConnection = 0;
        boolean allConnect = false;
        int randOne,randTwo;
        while(!allConnect){
            randOne = getRandomNumber(n);
            randTwo = getRandomNumber(n);
//            System.out.println("gen rand "+randOne+", "+randTwo);
            h.connect(randOne,randTwo);
            countConnection++;
            allConnect = checkAllConnect(h);
        }
        return countConnection;
    }

    private static boolean checkAllConnect(UF h){
        for(int i=0;i<h.size();i++){
            if(!h.isConnected(0,i)){
                return false;
            }
        }
        return true;
    }

    private static int getRandomNumber(int max) {
        return (int) ((Math.random() * max));
    }

    public static void main(String[] args) {
        int number = 100;
        if(args.length != 0 ){
            number = Integer.parseInt(args[0]);
        }
        for(int i=0;i<14;i++){
            long a =0;
            for(int j=0;j<30;j++){
                long b = count(number);
//                System.out.println("n: "+number+" count: "+b);
                System.out.print(+b+" ");
                a+=b;
            }
            System.out.println();
            System.out.println("n: "+number+"fin, avg: "+a/30);
            number*=2;
        }
//        System.out.println("n: "+number+" count: "+count(number));
    }

}
