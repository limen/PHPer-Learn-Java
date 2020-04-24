package com.limengxiang.xid;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class GeneratorTest {

    @BeforeEach
    void setUp() {
        Generator.forMachine(110001);
    }

    @Test
    void gen() throws Exception {
        Integer cnt = 1000000;
        HashMap<String, Boolean> occupy = new HashMap<>();
        Long start = new Date().getTime();
        for (int i=0; i< cnt; i++) {
            Xid gen = Generator.gen();
            occupy.put(gen.toString(), true);
            for (Integer b : gen.getBytes()) {
                assertTrue(b >= 0 && b <= 255);
            }
        }
        Long end = new Date().getTime();
        assertEquals(cnt, occupy.size());
        Long time = (end - start) * 1000;
        System.out.println("Total time consumption in micro seconds:" + time);
        Double perOP = new Double(time) / cnt;
        System.out.println("Average time consumption per op in micro second:" + perOP);
        System.out.println("Throughput per second:" + (1000000 / perOP));
    }
}