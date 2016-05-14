package org.jmatrix.proxy.example.junit;

/**
 * @author jmatrix
 * @date 16/5/14
 */
public class Fibonacci {
    public static int compute(int n) {
        int result = 0;

        if (n <= 1) {
            result = n;
        } else {
            result = compute(n - 1) + compute(n - 2);
        }

        return result;
    }
}
