package com.baslangicprojesi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


class MesajTest {
    Mesaj mesaj;

    @BeforeAll
    private void beforeAll() {
        mesaj = new Mesaj("icerik", "to", "cc", "konu", Oncelik.DUSUK);
    }

    @Test
    void getIcerik() {
        assertEquals("icerik", mesaj.getIcerik());
    }

    @Test
    void getTo() {
        assertEquals("to", mesaj.getTo());
    }

    @Test
    void getCc() {
        assertEquals("cc", mesaj.getCc());
    }

    @Test
    void getKonu() {
        assertEquals("konu", mesaj.getKonu());
    }

    @Test
    void getOncelik() {
        assertEquals(Oncelik.DUSUK, mesaj.getOncelik());
    }
}