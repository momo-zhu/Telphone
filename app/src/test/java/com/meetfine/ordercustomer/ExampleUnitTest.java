package com.meetfine.ordercustomer;

import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        try {
            String str = Base64.getEncoder().encodeToString("庐阳区民生工程".getBytes("UTF-8"));
            System.out.print(str);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}