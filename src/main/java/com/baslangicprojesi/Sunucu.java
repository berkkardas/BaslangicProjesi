package com.baslangicprojesi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Mesaj alisverisi yapan Sunucuyu temsil eder
 *
 * @author Berk Kardas
 */
public class Sunucu extends Thread {
    final static Logger logger = Logger.getLogger(Sunucu.class);
    private final BlockingQueue<String> dusukKuyruk;
    private final BlockingQueue<String> normalKuyruk;
    private final BlockingQueue<String> yuksekKuyruk;
    private final ObjectMapper mapper;
    private ServerSocket sunucuSoketi;
    private Socket soket;
    private DataInputStream gelenMesaj;

    /**
     * Belirtilen sunucu portu ile bir soket olusturur
     *
     * @param port Soketin sahip olacagi sunucu portu (Ornek: 6789)
     */
    public Sunucu(int port, BlockingQueue<String> dusukKuyruk, BlockingQueue<String> normalKuyruk, BlockingQueue<String> yuksekKuyruk) {
        try {
            sunucuSoketi = new ServerSocket(port); // Soketin acilmasi
        } catch (IOException e) {
            logger.error("Sunucu soketi acilirken hata olustu", e);
        }
        mapper = new ObjectMapper();
        this.dusukKuyruk = dusukKuyruk;
        this.normalKuyruk = normalKuyruk;
        this.yuksekKuyruk = yuksekKuyruk;
    }

    /**
     * Sunucu Main metodu, Sunucuyu girilen port numarasinda soket acarak calistirir ve mesaj alisverisine baslatir
     *
     * @param args Olusturulacak sunucunun port numarasi
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            logger.info("Ornek Deger: 6789");
            return;
        }
        // Threadler ve sunucunun ayni anda ortak liste uzerinde calisabilmesi icin BlockingQueue kullanildi
        // Her bir oncelik icin farkli bir BlockingQueue olusturuldu
        BlockingQueue<String> dusukKuyruk = new LinkedBlockingQueue<>();
        BlockingQueue<String> normalKuyruk = new LinkedBlockingQueue<>();
        BlockingQueue<String> yuksekKuyruk = new LinkedBlockingQueue<>();
        Sunucu sunucu = new Sunucu(Integer.parseInt(args[0]), dusukKuyruk, normalKuyruk, yuksekKuyruk);

        // Threadlerin kontrolunun saglanmasi icin ExecutorService kullanildi
        ExecutorService pes = Executors.newFixedThreadPool(1);
        ExecutorService ces = Executors.newFixedThreadPool(3);
        pes.submit(sunucu);

        // Threadler olusturuldu
        SunucuThread dusukThread = new SunucuThread(dusukKuyruk, "Dusuk Oncelikli Thread");
        SunucuThread normalThread = new SunucuThread(normalKuyruk, "Normal Oncelikli Thread");
        SunucuThread yuksekThread = new SunucuThread(yuksekKuyruk, "Yuksek Oncelikli Thread");

        // Threadlerin oncelikleri ayarlandi
        dusukThread.setPriority(MIN_PRIORITY);
        normalThread.setPriority(NORM_PRIORITY);
        yuksekThread.setPriority(MAX_PRIORITY);

        ces.submit(dusukThread);
        ces.submit(normalThread);
        ces.submit(yuksekThread);

        sunucu.calistir();
        sunucu.kapat();
        pes.shutdown();
        ces.shutdown();
        pes.shutdownNow();
        ces.shutdownNow();
    }

    /**
     * Sunucuyu calistirir ve baglanti gelmesini bekler mesaj alisverisini yapar, mesaji dosyaya ve veri tabanina
     * kaydeder ve soketi kapatir. Sonrasinda yeni bir baglantinin gelmesini bekler ve tekrar eder.
     */
    public void calistir() {
        try {
            soket = sunucuSoketi.accept(); // Baglanti gelene kadar bekleyecek
            logger.info("Istemci ile baglanti saglandi: " + soket);
        } catch (IOException e) {
            logger.error("Istemciye baglanilirken hata olustu", e);
        }
        while (true) {
            try {
                gelenMesaj = new DataInputStream(soket.getInputStream());
            } catch (IOException e) {
                logger.error("Istemciden mesaj alinirken hata olustu", e);
            }
            String mesajJson;
            try {
                mesajJson = teslimAl();
            } catch (NullPointerException e) {
                logger.error("Istemciden mesaj alinirken hata olustu", e);
                break;
            }
            // Mesajlar onceliklerine gore bir listeye eklenir
            try {
                Mesaj mesaj = mapper.readValue(mesajJson, Mesaj.class);
                switch (mesaj.getOncelik()) {
                    case DUSUK:
                        dusukKuyruk.put(mesajJson);
                        break;
                    case NORMAL:
                        normalKuyruk.put(mesajJson);
                        break;
                    case YUKSEK:
                        yuksekKuyruk.put(mesajJson);
                        break;
                }
            } catch (JsonProcessingException | InterruptedException | IllegalArgumentException e) {
                logger.error("Mesaj listeye eklenirken hata olustu", e);
            }
        }
    }

    /**
     * Soket, stream gibi kaynaklarin kapatilmasini saglar
     */
    public void kapat() {
        try {
            soket.close();
            gelenMesaj.close();
        } catch (IOException e) {
            logger.error("Soket kapatilirken hata olustu", e);
        }
        try {
            dusukKuyruk.put("exit");
            normalKuyruk.put("exit");
            yuksekKuyruk.put("exit");
        } catch (InterruptedException e) {
            logger.error("Soket kapatilirken hata olustu", e);
        }
    }

    /**
     * Sunucudan ya da Istemciden Mesaj objesinin Json string olarak teslim alinmasini saglar
     *
     * @return Niteliklere sahip olusturulan Mesaj nesnesinin Json string hali
     * @throws NullPointerException Mesaj teslim alinirken hata olustu
     */
    private String teslimAl() throws NullPointerException {
        try {
            String mesajJson = gelenMesaj.readUTF(); // Gelen mesajin okunmasi
            logger.info("[1] Yeni Mesaj!");
            return mesajJson;
        } catch (IOException e) {
            logger.error("Mesaj teslim alinirken hata olustu", e);
        }
        logger.error("Mesaj teslim alinirken hata olustu - NullPointerException");
        throw new NullPointerException();
    }
}