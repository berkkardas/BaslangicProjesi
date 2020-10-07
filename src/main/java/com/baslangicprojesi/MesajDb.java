package com.baslangicprojesi;

import org.apache.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Sunucuya gelen mesajlarin kaydedildigi veritabanini temsil eder
 *
 * @author Berk Kardas
 */
public class MesajDb {
    final static Logger logger = Logger.getLogger(MesajDb.class);
    private Connection baglanti;

    /**
     * Secilen veritabanina verilen kullanici adi ve sifre ile baglanti kurulmasini saglar
     */
    public MesajDb() {
        try {
            String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
            Class.forName(JDBC_DRIVER);
            // Veritabanina baglanilmasi
            logger.info("Secilen veritabanina baglaniliyor");
            String DB_URL = "jdbc:mysql://localhost:3306/mesajDb";
            // Veritabani kullanici adi sifresi
            String USER = "root";
            String PASS = "";
            baglanti = DriverManager.getConnection(DB_URL, USER, PASS);
            logger.info("Veritabanina baglanildi");
        } catch (Exception e) {
            logger.error("JDBC surucusunde hata olustu", e);
        }
    }

    /**
     * Verilen mesaj bilgilerini bagli olunan veritabanina kaydeder
     *
     * @param oncelik Mesajin onceligi
     * @param icerik  Mesajin icerigi
     * @param to      Mesajin kime gonderildigi
     * @param cc      Mesaji kimin gorecegi
     * @param konu    Mesajin konusu
     */
    public void kaydet(Oncelik oncelik, String icerik, String to, String cc, String konu) {
        // Veritabanina sql komutunun gonderilmesi
        try {
            PreparedStatement stmt = baglanti.prepareStatement("INSERT INTO " + oncelik.toString().toLowerCase()
                    + "oncelik" + "(icerik, too, cc, konu) VALUES (?, ?, ?, ?)");
            stmt.setString(1, icerik);
            stmt.setString(2, to);
            stmt.setString(3, cc);
            stmt.setString(4, konu);
            stmt.executeUpdate();
            logger.info("Mesaj veritabanina kaydedildi");
        } catch (SQLException e) {
            logger.error("Mesaj veritabanina kaydedilirken hata olustu", e);
        }
    }

    /**
     * Veritabani ile kurulan baglantiyi sonlandirir
     */
    public void kapat() {
        try {
            if (baglanti != null)
                baglanti.close();
        } catch (SQLException e) {
            logger.error("Veritabani kapatilirken hata olustu", e);
        }
    }
}
