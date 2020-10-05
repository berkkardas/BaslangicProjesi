import org.apache.log4j.Logger;

/**
 * Gonderilen mesajlarin onceliklerini temsil eder
 *
 * @author Berk Kardas
 */
public enum Oncelik {
    DUSUK(1), NORMAL(2), YUKSEK(3);

    final static Logger logger = Logger.getLogger(MesajUtil.class);
    private final int oncelik;

    /**
     * Onceligin int degerini kullanarak string degerini belirler
     *
     * @param oncelik Onceligin int degeri (1, 2, 3)
     */
    Oncelik(int oncelik) {
        this.oncelik = oncelik;
    }

    /**
     * Oncelik bilgisinin int degeri ile cagirilabilmesini saglar
     *
     * @param oncelik Onceligin int degeri (1, 2, 3)
     * @return Oncegilin string degeri (DUSUK, NORMAL, YUKSEK)
     */
    public static Oncelik getOncelik(int oncelik) {
        for (Oncelik l : Oncelik.values()) {
            if (l.oncelik == oncelik) {
                return l;
            }
        }
        logger.error("Gecersis oncelik girildi - IllegalArgumentException");
        throw new IllegalArgumentException();
    }
}