package com.baslangicprojesi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import java.util.Scanner;

/**
 * Mesaj alisverisi icin yardimci metotlar sunar
 *
 * @author Berk Kardas
 */
public final class MesajUtil {
    final static Logger logger = Logger.getLogger(MesajUtil.class);
    static ObjectMapper mapper = new ObjectMapper();

    /**
     * Mesajin goruntulenmesini saglar
     *
     * @param mesajJson Sunucu ya da Istemci tarafindan iletilen mesaj
     */
    public static void goruntule(String mesajJson) {
        try {
            Mesaj mesaj = mapper.readValue(mesajJson, Mesaj.class);
            System.out.println("Mesajin Konusu: " + mesaj.getKonu());
            System.out.println("Mesajin Onceligi: " + mesaj.getOncelik());
            System.out.println("Mesajin Icerigi: " + mesaj.getIcerik());
        } catch (JsonProcessingException e) {
            logger.error("Mesaj goruntulenirken hata olustu", e);
        }
    }

    /**
     * Kullanicidan mesajin niteliklerini girdi olarak alir ve bu niteliklere sahip bir Mesaj nesnesi olusturur
     * Artik GUI kullanildigi icin bu metot kullanilmamaktadir
     *
     * @return Niteliklere sahip olusturulan Mesaj nesnesi
     */
    public static String girdiAl() {
        Scanner girdi = new Scanner(System.in); // Kullanicidan girdinin alinmasi
        System.out.print("Mesajin Konusu: ");
        String konu = girdi.nextLine();
        System.out.print("Mesajin Onceligi (1-DUSUK, 2-NORMAL, 3-YUKSEK): ");
        String oncelik = girdi.nextLine();
        System.out.print("Mesajin Kime Gonderilecegi: ");
        String to = girdi.nextLine();
        System.out.print("Mesajin Icerigi: ");
        String icerik = girdi.nextLine();
        Mesaj mesaj = new Mesaj(icerik, to, to, konu, Oncelik.getOncelik(Integer.parseInt(oncelik))); // Mesajin olusturulmasi
        try {
            return mapper.writeValueAsString(mesaj);
        } catch (JsonProcessingException e) {
            logger.error("Mesaj girdi alinirken hata olustu", e);
        }
        logger.error("Mesaj girdi alinirken hata olustu - NullPointerException");
        throw new NullPointerException();
    }
}

