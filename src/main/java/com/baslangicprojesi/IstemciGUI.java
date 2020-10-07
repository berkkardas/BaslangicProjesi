package com.baslangicprojesi;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * * Kullanicidan mesaj girdisi alan grafik arayuzu temsil eder
 *
 * @author Berk Kardas
 */
public class IstemciGUI {

    final static Logger logger = Logger.getLogger(IstemciGUI.class);
    private static Istemci istemci;
    private final ObjectMapper mapper;
    private JFrame frame;
    private JTextField konu;
    private JTextField to;

    /**
     * Mesaji stringe cevirmek icin mapper initialize eder ve IstemciGUI nesnesi olusturur
     */
    public IstemciGUI() {
        mapper = new ObjectMapper();
        initialize();
    }

    /**
     * Adres ve port bilgisi ile baglanti kurar ve grafik arayuzu baslatir
     *
     * @param args Sunucu adresi ve sunucu portu
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            logger.info("Ornek Deger: localhost 6789");
            return;
        }
        String adres = args[0];
        int port = Integer.parseInt(args[1]);
        istemci = new Istemci(adres, port);
        EventQueue.invokeLater(() -> {
            try {
                IstemciGUI window = new IstemciGUI();
                window.frame.setVisible(true);
            } catch (Exception e) {
                logger.error("Grafik arayuz baslatilirken hata olustu", e);
            }
        });
    }


    /**
     * Grafik arayuzdeki icerikleri tanimlar ve ekler
     */
    public void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblKonu = new JLabel("Konu");
        lblKonu.setBounds(60, 30, 50, 14);
        frame.getContentPane().add(lblKonu);

        JLabel lblOncelik = new JLabel("Oncelik");
        lblOncelik.setBounds(60, 60, 50, 14);
        frame.getContentPane().add(lblOncelik);

        JLabel lblTo = new JLabel("To");
        lblTo.setBounds(60, 90, 50, 14);
        frame.getContentPane().add(lblTo);

        JLabel lblAddress = new JLabel("Icerik");
        lblAddress.setBounds(60, 120, 50, 14);
        frame.getContentPane().add(lblAddress);

        konu = new JTextField();
        konu.setBounds(120, 30, 200, 20);
        frame.getContentPane().add(konu);
        konu.setColumns(10);

        JComboBox<String> oncelik = new JComboBox<>();
        oncelik.addItem("Seciniz");
        oncelik.addItem("Dusuk");
        oncelik.addItem("Normal");
        oncelik.addItem("Yuksek");
        oncelik.addActionListener(arg0 -> {
        });
        oncelik.setBounds(120, 60, 100, 20);
        frame.getContentPane().add(oncelik);

        to = new JTextField();
        to.setBounds(120, 90, 200, 20);
        frame.getContentPane().add(to);
        to.setColumns(10);

        JTextArea icerik = new JTextArea();
        icerik.setBounds(120, 120, 200, 50);
        frame.getContentPane().add(icerik);

        JButton btnClear = new JButton("Temizle");

        btnClear.setBounds(250, 200, 100, 23);
        frame.getContentPane().add(btnClear);

        JButton btnSubmit = new JButton("Gonder");

        btnSubmit.setBounds(100, 200, 100, 23);
        frame.getContentPane().add(btnSubmit);

        btnSubmit.addActionListener(arg0 -> {
            // Eksik bilgi var mi diye kontrol edilir, varsa uyari verilir
            if (konu.getText().isEmpty() || (to.getText().isEmpty()) || (icerik.getText().isEmpty())
                    || (Objects.equals(oncelik.getSelectedItem(), "Seciniz")))
                JOptionPane.showMessageDialog(null, "Eksik bilgi girildi");
                // Eksik bilgi yoksa mesaj olusturulur ve gonderilir
            else {
                Mesaj mesaj = new Mesaj(icerik.getText(), to.getText(), to.getText(), konu.getText(),
                        Oncelik.valueOf(oncelik.getSelectedItem().toString().toUpperCase())); // Mesajin olusturulmasi
                try {
                    String mesajJson = mapper.writeValueAsString(mesaj);
                    istemci.teslimEt(mesajJson);
                } catch (JsonProcessingException e) {
                    logger.error("Mesaj gonderilirken hata olustu", e);
                }
                JOptionPane.showMessageDialog(null, "Mesaj gonderildi");
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                istemci.kapat();
                System.exit(0);
            }
        });

        btnClear.addActionListener(e -> {
            to.setText(null);
            konu.setText(null);
            icerik.setText(null);
            oncelik.setSelectedItem("Seciniz");
        });
    }
}
