import org.apache.log4j.Logger;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * Mesaj alisverisi yapan Istemciyi temsil eder
 *
 * @author Berk Kardas
 */
public class Istemci {
    final static Logger logger = Logger.getLogger(Istemci.class);
    private Socket soket;
    private DataOutputStream gidenMesaj;

    /**
     * Belirtilen sunucu adresi ve portu ile bir soket olusturur
     *
     * @param sunucuAdresi Soketin sahip olacagi sunucu adresi (Ornek: localhost)
     * @param sunucuPortu  Soketin sahip olacagi sunucu portu (Ornek: 6789)
     */
    public Istemci(String sunucuAdresi, int sunucuPortu) {
        try {
            soket = new Socket(sunucuAdresi, sunucuPortu); // Sunucuya baglanilmasi
            logger.info("Sunucu ile baglanti kuruldu: " + soket);
            gidenMesaj = new DataOutputStream(soket.getOutputStream());
        } catch (IOException e) {
            logger.error("Sunucu ile baglanti kurulurken hata olustu", e);
        }
    }

    /**
     * Soket, stream gibi kaynaklarin kapatilmasini saglar
     */
    public void kapat() {
        try {
            soket.close();
            gidenMesaj.close();
        } catch (IOException | NullPointerException e) {
            logger.error("Soket kapatilirken hata olustu", e);
        }
    }

    /**
     * Sunucuya ya da Istemciye Mesaj objesinin Json string olarak iletilmesini saglar
     *
     * @param mesajJson Iletilecek Mesaj nesnesi
     */
    public void teslimEt(String mesajJson) {
        try {
            gidenMesaj.writeUTF(mesajJson); // Sunucuya mesajin gonderilmesi
            logger.info("Mesaj gonderildi");
        } catch (IOException | NullPointerException e) {
            logger.error("Mesaj teslim edilirken hata olustu", e);
        }
    }
}