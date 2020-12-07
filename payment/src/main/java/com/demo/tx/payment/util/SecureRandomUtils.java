package com.demo.tx.payment.util;

import java.security.SecureRandom;

public class SecureRandomUtils {
    private static SecureRandom secureRandom = new SecureRandom();

    public static int nextInt(int cardinalNumber, int bound) {
        return cardinalNumber + secureRandom.nextInt(bound);
    }

}
