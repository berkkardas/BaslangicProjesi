package com.baslangicprojesi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

class SunucuThreadTest {
    Mesaj mesaj;
    String mesajJson;

    @BeforeAll
    private void beforeAll() throws JsonProcessingException {
        mesaj = new Mesaj("icerik", "to", "cc", "konu", Oncelik.DUSUK);
        final ObjectMapper mapper = new ObjectMapper();
        mesajJson = mapper.writeValueAsString(mesaj);
    }

    @Test
    void kaydet() throws InterruptedException {
        BlockingQueue<String> dusukKuyruk = new LinkedBlockingQueue<>();
        dusukKuyruk.put(mesajJson);
        dusukKuyruk.put("exit");
        SunucuThread sunucuThread = new SunucuThread(dusukKuyruk, "");
        sunucuThread.run();
    }
}