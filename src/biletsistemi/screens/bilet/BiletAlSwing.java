package biletsistemi.screens.bilet;

import biletsistemi.UserSession;
import biletsistemi.database.FlightDAO;
import biletsistemi.database.TicketDAO;
import biletsistemi.database.UserDAO;
import biletsistemi.util.CityConstants;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BiletAlSwing {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd.MM.yyyy");

    public void baslat() {
        System.out.println("### BiletAlSwing VERSION = 2025-12-24 TEST ###");

        JFrame pencere = new JFrame("Bilet Alma Ekranı");
        pencere.setSize(980, 780);
        pencere.setLocationRelativeTo(null);
        pencere.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pencere.setLayout(new BorderLayout());
        pencere.getContentPane().setBackground(Color.WHITE);

        Color anaRenk = new Color(50, 97, 100);

        // ================== TOP BAR ==================
        JPanel kuzeyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        kuzeyPanel.setBackground(anaRenk);
        kuzeyPanel.setPreferredSize(new Dimension(0, 60));

        JLabel titleText = new JLabel("FLYLY ");
        titleText.setFont(new Font("Segoe UI", Font.BOLD, 34));
        titleText.setForeground(Color.WHITE);

        JLabel titlePlane = new JLabel("===✈");
        titlePlane.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 34));
        titlePlane.setForeground(Color.WHITE);

        JLabel title2Text = new JLabel(" BİLET AL");
        title2Text.setFont(new Font("Segoe UI", Font.BOLD, 34));
        title2Text.setForeground(Color.WHITE);

        kuzeyPanel.add(titleText);
        kuzeyPanel.add(titlePlane);
        kuzeyPanel.add(title2Text);

        pencere.add(kuzeyPanel, BorderLayout.NORTH);

        // ================== ANA GÖVDE ==================
        JPanel govde = new JPanel(new BorderLayout(15, 15));
        govde.setBorder(new EmptyBorder(15, 15, 15, 15));
        govde.setBackground(Color.WHITE);

        // ================== SOL: FORM KARTI ==================
        JPanel formKart = new JPanel();
        formKart.setLayout(new BoxLayout(formKart, BoxLayout.Y_AXIS));
        formKart.setBorder(new EmptyBorder(18, 18, 18, 18));
        formKart.setBackground(new Color(245, 246, 250));

        JComboBox<String> firmaBox = new JComboBox<>(new String[]{"THY", "Pegasus", "AJet"});

        
        DefaultComboBoxModel<String> neredenModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> nereyeModel = new DefaultComboBoxModel<>();

        for (String city : CityConstants.TURKIYE_ILLER) {
            neredenModel.addElement(city);
            nereyeModel.addElement(city);
        }

        JComboBox<String> neredenBox = new JComboBox<>(neredenModel);
        JComboBox<String> nereyeBox  = new JComboBox<>(nereyeModel);

        if (neredenBox.getItemCount() > 0) neredenBox.setSelectedIndex(0);
        if (nereyeBox.getItemCount() > 1)  nereyeBox.setSelectedIndex(1);

        JLabel secilenKoltukLabel = new JLabel("Seçilmedi");

        JRadioButton tekYon = new JRadioButton("Tek Yön", true);
        JRadioButton ciftYon = new JRadioButton("Çift Yön");
        ButtonGroup bg = new ButtonGroup();
        bg.add(tekYon);
        bg.add(ciftYon);

        
        JDateChooser gidisChooser = new JDateChooser();
        gidisChooser.setDateFormatString("dd.MM.yyyy");
        gidisChooser.setDate(null); 

        
        JDateChooser donusChooser = new JDateChooser();
        donusChooser.setDateFormatString("dd.MM.yyyy");
        donusChooser.setDate(null);
        donusChooser.setEnabled(false); 

        
        JComboBox<String> saatBox = new JComboBox<>(saatListesiOlustur(30));
        saatBox.setSelectedItem("00:00");

        JTextField adSoyadTxt = new JTextField(15);
        JTextField tcTxt = new JTextField(15);
        JTextField yolcuMailTxt = new JTextField(15);

        tcTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyTyped(java.awt.event.KeyEvent e) {
                char deger = e.getKeyChar();
                if (!Character.isDigit(deger) || tcTxt.getText().length() >= 11) {
                    e.consume();
                }
            }
        });

        JLabel sonucLabel = new JLabel(" ");
        sonucLabel.setForeground(new Color(39, 174, 96));

        JLabel tarihLabel = new JLabel("");
        JLabel saatLabel = new JLabel("");
        JLabel fiyatLabel = new JLabel("");

        formKart.add(baslikOlustur("Uçuş Bilgileri"));
        formKart.add(Box.createVerticalStrut(8));
        formKart.add(formSatiriOlustur("Firma:", firmaBox));
        formKart.add(formSatiriOlustur("Nereden:", neredenBox));
        formKart.add(formSatiriOlustur("Nereye:", nereyeBox));
        formKart.add(formSatiriOlustur("Koltuk:", secilenKoltukLabel));

        JPanel yolculukTipiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        yolculukTipiPanel.setOpaque(false);
        tekYon.setOpaque(false);
        ciftYon.setOpaque(false);
        tekYon.setForeground(anaRenk);
        ciftYon.setForeground(anaRenk);
        yolculukTipiPanel.add(tekYon);
        yolculukTipiPanel.add(Box.createHorizontalStrut(10));
        yolculukTipiPanel.add(ciftYon);

        formKart.add(formSatiriOlustur("Yolculuk Tipi:", yolculukTipiPanel));

        // Tarih ve saat seçimleri
        formKart.add(formSatiriOlustur("Gidiş Tarihi (isteğe bağlı):", gidisChooser));
        formKart.add(formSatiriOlustur("Dönüş Tarihi:", donusChooser));
        formKart.add(formSatiriOlustur("Saat:", saatBox));

        formKart.add(Box.createVerticalStrut(8));
        formKart.add(sonucLabel);

        formKart.add(Box.createVerticalStrut(14));
        formKart.add(baslikOlustur("Yolcu Bilgileri"));
        formKart.add(Box.createVerticalStrut(8));
        formKart.add(formSatiriOlustur("Ad Soyad:", adSoyadTxt));
        formKart.add(formSatiriOlustur("TC No:", tcTxt));
        formKart.add(formSatiriOlustur("E-Posta:", yolcuMailTxt));

        formKart.add(Box.createVerticalStrut(14));
        formKart.add(baslikOlustur("Ücret Bilgileri"));
        formKart.add(Box.createVerticalStrut(8));
        formKart.add(formSatiriOlustur("Tarih:", tarihLabel));
        formKart.add(formSatiriOlustur("Saat:", saatLabel));
        formKart.add(formSatiriOlustur("Fiyat:", fiyatLabel));

        formKart.add(Box.createVerticalStrut(10));

        JCheckBox kabulBox = new JCheckBox("Şartları okudum, kabul ediyorum.");
        kabulBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        kabulBox.setFocusPainted(false);
        kabulBox.setForeground(anaRenk);
        kabulBox.setOpaque(false);
        formKart.add(kabulBox);

        formKart.add(Box.createVerticalStrut(12));

        JButton ucusAraBtn = new JButton("Uçuş Ara");
        stilButon(ucusAraBtn, anaRenk, Color.WHITE, 44);

        JButton onaylaButon = new JButton("Bileti Onayla");
        stilButon(onaylaButon, new Color(46, 204, 113), Color.WHITE, 44);
        onaylaButon.setEnabled(false);

        onaylaButon.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("🖱️ [BiletAlSwing] mousePressed onaylaButon | enabled=" + onaylaButon.isEnabled());
            }
        });

        JButton anaSayfaButon = new JButton("Ana Sayfaya Dön");
        stilButon(anaSayfaButon, Color.WHITE, anaRenk, 40);
        anaSayfaButon.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));

        formKart.add(ucusAraBtn);
        formKart.add(Box.createVerticalStrut(10));
        formKart.add(onaylaButon);
        formKart.add(Box.createVerticalStrut(10));
        formKart.add(anaSayfaButon);

        anaSayfaButon.addActionListener(e -> pencere.dispose());

        JScrollPane solScroll = new JScrollPane(formKart);
        solScroll.setBorder(null);
        solScroll.getVerticalScrollBar().setUnitIncrement(16);
        solScroll.setPreferredSize(new Dimension(430, 0));
        solScroll.getViewport().setBackground(Color.WHITE);

        // ================== SAĞ: KOLTUK ==================
        UcakKoltukDuzeni koltukPaneli = new UcakKoltukDuzeni(secilenKoltukLabel);

        JScrollPane sagScroll = new JScrollPane(koltukPaneli);
        sagScroll.setBorder(null);
        sagScroll.getVerticalScrollBar().setUnitIncrement(16);
        sagScroll.setPreferredSize(new Dimension(420, 0));
        sagScroll.getViewport().setBackground(Color.WHITE);

        govde.add(solScroll, BorderLayout.WEST);
        govde.add(sagScroll, BorderLayout.CENTER);
        pencere.add(govde, BorderLayout.CENTER);

        // ==================  SEÇİLEN ŞEHİRİ DİĞER LİSTEDEN KALDIR ==================
        final boolean[] isSyncing = {false};

        syncModelsExcludeSelected(neredenBox, nereyeBox, isSyncing);
        syncModelsExcludeSelected(nereyeBox, neredenBox, isSyncing);

        neredenBox.addActionListener(e -> {
            if (isSyncing[0]) return;
            syncModelsExcludeSelected(neredenBox, nereyeBox, isSyncing);

            sonucLabel.setText(" ");
            tarihLabel.setText("");
            saatLabel.setText("");
            fiyatLabel.setText("");
            onaylaButon.setEnabled(false);
        });

        nereyeBox.addActionListener(e -> {
            if (isSyncing[0]) return;
            syncModelsExcludeSelected(nereyeBox, neredenBox, isSyncing);

            sonucLabel.setText(" ");
            tarihLabel.setText("");
            saatLabel.setText("");
            fiyatLabel.setText("");
            onaylaButon.setEnabled(false);
        });

        // Tek/Çift yön seçimine göre dönüş tarihini aç/kapat
        tekYon.addActionListener(e -> {
            donusChooser.setEnabled(false);
            donusChooser.setDate(null);
            if (sonucLabel.getText().contains("Uçuş bulundu")) onaylaButon.setEnabled(true);
        });

        ciftYon.addActionListener(e -> {
            donusChooser.setEnabled(true);
            if (sonucLabel.getText().contains("Uçuş bulundu")) onaylaButon.setEnabled(true);
        });

        // Koltuk değişince (uçuş bulunduysa) onay açık kalsın
        secilenKoltukLabel.addPropertyChangeListener("text", evt -> {
            String yeni = String.valueOf(evt.getNewValue());
            if (yeni != null && !yeni.equals("Seçilmedi") && sonucLabel.getText().contains("Uçuş bulundu")) {
                onaylaButon.setEnabled(true);
                System.out.println("✅ Koltuk seçildi -> onaylaButon enabled = " + onaylaButon.isEnabled());
            }
        });

        // ================== UÇUŞ ARA ==================
        ucusAraBtn.addActionListener(e -> {

            String firm = (String) firmaBox.getSelectedItem();
            String from = (String) neredenBox.getSelectedItem();
            String to = (String) nereyeBox.getSelectedItem();

            if (from != null && from.equals(to)) {
                sonucLabel.setForeground(new Color(192, 57, 43));
                sonucLabel.setText("✖ Nereden ve nereye aynı olamaz");
                tarihLabel.setText("");
                saatLabel.setText("");
                fiyatLabel.setText("");
                onaylaButon.setEnabled(false);
                return;
            }

            
            Date secilenGidis = gidisChooser.getDate();
            String secilenSaat = (String) saatBox.getSelectedItem();

            FlightDAO flightDAO = new FlightDAO();
            FlightDAO.FlightInfo f;

            
            if (secilenGidis != null) {
                String secilenTarihStr = DATE_FMT.format(secilenGidis);
                try {
                    f = flightDAO.findFlight(firm, from, to, secilenTarihStr, secilenSaat);
                } catch (Exception ex) {
                    
                    f = flightDAO.findFlight(firm, from, to);
                }
            } else {
                f = flightDAO.findFlight(firm, from, to);
            }

            if (f == null) {
                sonucLabel.setForeground(new Color(192, 57, 43));
                sonucLabel.setText("✖ Uçuş bulunamadı");
                tarihLabel.setText("");
                saatLabel.setText("");
                fiyatLabel.setText("");
                onaylaButon.setEnabled(false);
                return;
            }

            sonucLabel.setForeground(new Color(39, 174, 96));
            sonucLabel.setText("✔ Uçuş bulundu");

            
            String gidisStr = (secilenGidis == null) ? f.date : DATE_FMT.format(secilenGidis);
            tarihLabel.setText(gidisStr);

            
            saatLabel.setText(secilenSaat);

            double toplamFiyat = tekYon.isSelected() ? f.price : (f.price * 2.0);
            fiyatLabel.setText(toplamFiyat + " TL");

            
            if (f.time != null) {
                saatBox.setSelectedItem(f.time);
                saatLabel.setText((String) saatBox.getSelectedItem());
            }

            onaylaButon.setEnabled(true);
            onaylaButon.setCursor(new Cursor(Cursor.HAND_CURSOR));
            onaylaButon.repaint();

            System.out.println("✅ Uçuş bulundu -> onaylaButon enabled = " + onaylaButon.isEnabled());
        });

        // ==================  BİLETİ ONAYLA (BAKİYE DÜŞME MANTIĞI) ==================
        onaylaButon.addActionListener(e -> {

            System.out.println("✅ [BiletAlSwing] ACTION: Bileti Onayla çalıştı!");

            try {
                if (UserSession.username == null || UserSession.username.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(pencere, "Önce giriş yapmanız gerekiyor!");
                    return;
                }

                if (!kabulBox.isSelected()) {
                    JOptionPane.showMessageDialog(pencere, "Şartları kabul etmelisiniz!");
                    return;
                }

                if (!sonucLabel.getText().contains("Uçuş bulundu")) {
                    JOptionPane.showMessageDialog(pencere, "Önce uçuş arayın!");
                    return;
                }

                if (adSoyadTxt.getText().trim().isEmpty() ||
                        tcTxt.getText().trim().isEmpty() ||
                        yolcuMailTxt.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(pencere, "Lütfen yolcu bilgilerini doldurun!");
                    return;
                }

                if (tcTxt.getText().trim().length() != 11) {
                    JOptionPane.showMessageDialog(pencere, "TC No 11 haneli olmalıdır!");
                    return;
                }

                if (secilenKoltukLabel.getText().equals("Seçilmedi")) {
                    JOptionPane.showMessageDialog(pencere, "Lütfen koltuk seçin!");
                    return;
                }

                String firm = (String) firmaBox.getSelectedItem();
                String fromCity = (String) neredenBox.getSelectedItem();
                String toCity = (String) nereyeBox.getSelectedItem();
                String tripType = tekYon.isSelected() ? "Tek Yön" : "Çift Yön";

                String flightDate = tarihLabel.getText();
                String flightTime = saatLabel.getText();

                String returnDate = "";
                if (!tekYon.isSelected()) {
                    Date secilenDonus = donusChooser.getDate();
                    if (secilenDonus == null) {
                        JOptionPane.showMessageDialog(pencere, "Çift yön seçtiyseniz dönüş tarihi seçin!");
                        return;
                    }
                    returnDate = DATE_FMT.format(secilenDonus);
                }

                String seat = secilenKoltukLabel.getText();

                String fullName = adSoyadTxt.getText().trim();
                String tcNo = tcTxt.getText().trim();
                String passengerEmail = yolcuMailTxt.getText().trim();

                double price;
                try {
                    String p = fiyatLabel.getText().replace("TL", "").replace("₺", "").trim().replace(",", ".");
                    price = p.isEmpty() ? 0 : Double.parseDouble(p);
                } catch (Exception ex) {
                    price = 0;
                }

               
                UserDAO userDAO = new UserDAO();
                int bakiye = userDAO.getBalanceByUsername(UserSession.username);

                int biletTutari = (int) Math.round(price);

                if (bakiye < biletTutari) {
                    JOptionPane.showMessageDialog(
                            pencere,
                            "Yetersiz bakiye!\nMevcut: " + bakiye + " TL\nGerekli: " + biletTutari + " TL",
                            "Ödeme Hatası",
                            JOptionPane.ERROR_MESSAGE
                    );
                    return;
                }


                //  KOLTUK DOLULUĞU KONTROLÜ İÇİN
                
                TicketDAO dao = new TicketDAO();

                if (dao.isSeatTaken(firm, fromCity, toCity, flightDate, flightTime, seat)) {
                    JOptionPane.showMessageDialog(pencere, "Bu koltuk dolu! Başka koltuk seçin.", "Uyarı", JOptionPane.WARNING_MESSAGE);
                    return;
                }

           
                boolean ok = dao.insertTicket(
                        UserSession.email,
                        firm,
                        fromCity,
                        toCity,
                        tripType,
                        flightDate,
                        flightTime,
                        returnDate,
                        seat,
                        fullName,
                        tcNo,
                        passengerEmail,
                        price
                );

                if (!ok) {
                    JOptionPane.showMessageDialog(pencere, "Bilet DB'ye kaydedilemedi ❌", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                //  BAKİYE DÜŞME (Bilet başarılıysa)
    
                int yeniBakiye = bakiye - biletTutari;
                boolean bakiyeOk = userDAO.updateBalance(UserSession.username, yeniBakiye);

                if (!bakiyeOk) {
                    JOptionPane.showMessageDialog(
                            pencere,
                            "Bilet kaydedildi ✅ ama bakiye güncellenemedi!\nLütfen tekrar deneyin.",
                            "Uyarı",
                            JOptionPane.WARNING_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            pencere,
                            "Bilet DB'ye kaydedildi ✅\nYeni Bakiye: " + yeniBakiye + " TL"
                    );
                }

                onaylaButon.setEnabled(false);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(pencere, "Hata: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        pencere.setVisible(true);
    }

    
    private static String[] saatListesiOlustur(int dakikaAdimi) {
        List<String> list = new ArrayList<>();
        for (int h = 0; h < 24; h++) {
            for (int m = 0; m < 60; m += dakikaAdimi) {
                list.add(String.format("%02d:%02d", h, m));
            }
        }
        return list.toArray(new String[0]);
    }

  
    private void syncModelsExcludeSelected(JComboBox<String> sourceBox, JComboBox<String> targetBox, boolean[] isSyncingFlag) {
        isSyncingFlag[0] = true;

        String selectedToExclude = (String) sourceBox.getSelectedItem();
        String currentTargetSelection = (String) targetBox.getSelectedItem();

        List<String> newList = new ArrayList<>();
        for (String city : CityConstants.TURKIYE_ILLER) {
            if (selectedToExclude != null && city.equals(selectedToExclude)) continue;
            newList.add(city);
        }

        DefaultComboBoxModel<String> newModel = new DefaultComboBoxModel<>();
        for (String c : newList) newModel.addElement(c);

        targetBox.setModel(newModel);

        if (currentTargetSelection != null && newList.contains(currentTargetSelection)) {
            targetBox.setSelectedItem(currentTargetSelection);
        } else {
            if (newModel.getSize() > 0) targetBox.setSelectedIndex(0);
        }

        isSyncingFlag[0] = false;
    }

    private void stilButon(JButton btn, Color bg, Color fg, int h) {
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, h));
        btn.setPreferredSize(new Dimension(320, h));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private JPanel formSatiriOlustur(String etiket, JComponent alan) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 18, 6));
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setOpaque(false);

        JLabel lbl = new JLabel(etiket);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbl.setPreferredSize(new Dimension(150, 26));
        lbl.setForeground(new Color(50, 97, 100));

        if (!(alan instanceof JLabel)) {
            alan.setPreferredSize(new Dimension(180, 28));
        }

        panel.add(lbl);
        panel.add(alan);
        return panel;
    }

    private JLabel baslikOlustur(String baslik) {
        JLabel lbl = new JLabel(baslik);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lbl.setForeground(new Color(50, 97, 100));
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }
}
