package com.baslangicprojesi;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OncelikTest {

    @Test
    void getOncelik() {
        assertEquals(Oncelik.DUSUK, Oncelik.getOncelik(1));
    }

}