package com.baslangicprojesi;

/**
 * Konu, icerik vb. niteliklere sahip mesaj nesnesini temsil eder
 *
 * @author Berk Kardas
 */
public class Mesaj {
    private Oncelik oncelik;
    private String icerik;
    private String to;
    private String cc;
    private String konu;

    /**
     * Belirtilen niteliklere sahip bir mesaj nesnesi olusturur
     *
     * @param icerik  Mesajin kendisi, icerigi
     * @param to      Mesajin kime gonderildigi
     * @param cc      Mesaji kimin gorecegi
     * @param konu    Mesajin konusu
     * @param oncelik Mesajin onceligi (1-DUSUK, 2-NORMAL, 3-YUKSEK)
     */
    public Mesaj(String icerik, String to, String cc, String konu, Oncelik oncelik) {
        this.icerik = icerik;
        this.to = to;
        this.cc = cc;
        this.konu = konu;
        this.oncelik = oncelik;
    }

    /**
     * Bos mesaj objesi olusturur, Json stringe donusturulesi icin gerekli
     */
    public Mesaj() {
        super();
    }

    /**
     * Mesajin icerigini verir
     *
     * @return Mesajin icerigi
     */
    public String getIcerik() {
        return icerik;
    }

    /**
     * Mesajin kime gonderildigini verir
     *
     * @return Mesajin kime gonderildigi
     */
    public String getTo() {
        return to;
    }

    /**
     * Mesaji kimin gorecegini verir
     *
     * @return Mesaji kimin gorecegi
     */
    public String getCc() {
        return cc;
    }

    /**
     * Mesajin konusunu verir
     *
     * @return Mesajin konusu
     */
    public String getKonu() {
        return konu;
    }

    /**
     * Mesajin onceligini verir
     *
     * @return Mesajin onceligi
     */
    public Oncelik getOncelik() {
        return oncelik;
    }
}