package io.shardingjdbc.test;

import io.shardingjdbc.core.keygen.DefaultKeyGenerator;
import io.shardingjdbc.core.keygen.KeyGenerator;

import java.text.SimpleDateFormat;
import java.util.Date;

public class KeyGeneratorTest {

    public static void main(String[] args) throws Exception {
        Date startDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-11-01 00:00:00");
        System.out.println(Long.toBinaryString(System.currentTimeMillis()));
        System.out.println(Long.toBinaryString(System.currentTimeMillis() - startDate.getTime()));
        KeyGenerator keyGenerator = new DefaultKeyGenerator();
        DefaultKeyGenerator.setWorkerId(1);
        for (int i = 0; i < 10; i++) {
            long key = keyGenerator.generateKey().longValue();
            System.out.println(key);
            System.out.println(Long.toBinaryString(key));
        }
    }
}
