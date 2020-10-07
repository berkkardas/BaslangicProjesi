package com.baslangicprojesi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

/**
 * Mesajlarin veritabanina ve dosyaya kaydedilmesini saglayan sunucu threadlerini temsil eder
 *
 * @author Berk Kardas
 */
public class SunucuThread extends Thread {
    final static Logger logger = Logger.getLogger(SunucuThread.class);
    private final BlockingQueue<String> kuyruk;
    private final MesajDb mesajDb;
    private final ObjectMapper mapper;
    private final String threadAdi;

    /**
     * Sunucu Threadi objesini baslatir, veritabani baglantisi icin mesajDb objesini olusturur
     *
     * @param kuyruk    Mesajin eklenecegi BlockingQueue listesi
     * @param threadAdi Threadin adi (Orn: Dusuk Oncelikli Thread)
     */
    public SunucuThread(BlockingQueue<String> kuyruk, String threadAdi) {
        this.kuyruk = kuyruk;
        mapper = new ObjectMapper();
        mesajDb = new MesajDb();
        this.threadAdi = threadAdi;
    }

    /**
     * Threadlerin yapacaklari bu kisimda bulunur. Mesaj listeden alinir ve kayit islemleri yapilir
     */
    @Override
    public void run() {
        logger.info(threadAdi + " calismaya basladi");
        String mesajJson;
        while (true) {
            mesajJson = null;
            try {
                mesajJson = kuyruk.take();
                if (mesajJson.equals("exit")) {
                    break;
                }
            } catch (InterruptedException e) {
                logger.error("Mesaj listeden alinirken hata olustu", e);
            }
            if (mesajJson != null) {
                logger.info(threadAdi + " mesaji isledi");
                kaydet(mesajJson);
                kaydetDb(mesajJson);
                MesajUtil.goruntule(mesajJson);
            }
        }
    }

    /**
     * Sunucuya alinan mesaji onceligine gore farkli bir veritabani tablosuna yazar
     *
     * @param mesajJson Alinan mesaj nesnesi
     */
    private void kaydetDb(String mesajJson) {
        try {
            // Json string kullanilarak mesaj nesnesi olusturulmasi
            Mesaj mesaj = mapper.readValue(mesajJson, Mesaj.class);
            mesajDb.kaydet(mesaj.getOncelik(), mesaj.getIcerik(), mesaj.getTo(), mesaj.getCc(), mesaj.getKonu());
        } catch (JsonProcessingException e) {
            logger.error("Mesaj veritabanina kaydedilirken hata olustu", e);
        }
    }

    /**
     * Sunucuya alinan mesaji onceligine gore farkli bir dosyaya yazar
     *
     * @param mesajJson Alinan mesaj nesnesi
     */
    private void kaydet(String mesajJson) {
        try {
            Mesaj mesaj = mapper.readValue(mesajJson, Mesaj.class);
            FileWriter fw = new FileWriter(mesaj.getOncelik().toString().toLowerCase()
                    + "Oncelik.txt");
            fw.write(mesajJson);
            fw.close();
        } catch (IOException e) {
            logger.error("Mesaj dosyaya yazilirken hata olustu", e);
        }
    }
}
