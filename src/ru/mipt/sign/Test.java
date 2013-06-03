package ru.mipt.sign;

import java.math.BigInteger;

public class Test
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        BigInteger a = new BigInteger("86456286526785347");
        BigInteger b = new BigInteger("38164207642696435");
        BigInteger c = a.multiply(b);
        System.out.println(c);
    }

}
