package com.baslangicprojesi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void kaydet() throws InterruptedException, IOException {
        BlockingQueue<String> dusukKuyruk = new LinkedBlockingQueue<>();
        dusukKuyruk.put(mesajJson);
        dusukKuyruk.put("exit");
        SunucuThread sunucuThread = new SunucuThread(dusukKuyruk, "");
        sunucuThread.run();
        Path path = Paths.get("dusukOncelik.txt");
        Stream<String> stream = Files.lines(path);
        StringBuilder sb = new StringBuilder();
        stream.forEach(sb::append);
        String contents = sb.toString();
        assertEquals(mesajJson, contents);

    }
}