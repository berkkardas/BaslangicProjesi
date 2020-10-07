package com.baslangicprojesi;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IstemciTest {
    private ServerSocket sunucuSoketi;
    private Istemci istemci;
    private Socket soket;

    @BeforeAll
    private void beforeAll() throws IOException {
        sunucuSoketi = new ServerSocket(6789); // Soketin acilmasi
        istemci = new Istemci("localhost", 6789);
        soket = sunucuSoketi.accept(); // Baglanti gelene kadar bekleyecek
    }

    @Test
    void teslimEt() throws IOException {
        DataInputStream gelenMesaj = new DataInputStream(soket.getInputStream());
        istemci.teslimEt("deneme");
        String mesajJson = gelenMesaj.readUTF();
        assertEquals("deneme", mesajJson);
    }

    @Test
    void kapat() {
        assertDoesNotThrow(() -> istemci.kapat());
    }
}