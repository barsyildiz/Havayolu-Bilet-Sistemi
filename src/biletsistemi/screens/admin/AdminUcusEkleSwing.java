package biletsistemi.screens.admin;

import biletsistemi.UserSession;
import biletsistemi.database.FlightDAO;
import biletsistemi.screens.login.GirisEkrani;
import biletsistemi.util.CityConstants;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AdminUcusEkleSwing {

    private static final SimpleDateFormat DATE_FMT = new SimpleDateFormat("dd.MM.yyyy");

    private final JFrame parent; 

    public AdminUcusEkleSwing(JFrame parent) {
        this.parent = parent;
    }

    public void baslat() {

        
        if (!UserSession.isAdmin) {
            JOptionPane.showMessageDialog(null,
                    "Bu sayfa sadece adminlere açıktır!",
                    "Yetkisiz Erişim",
                    JOptionPane.ERROR_MESSAGE);
            new GirisEkrani().baslat();
            return;
        }

        JFrame pencere = new JFrame("FLYLY - Admin | Uçuş Ekle");
        pencere.setSize(520, 680);
        pencere.setLocationRelativeTo(null);
        pencere.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pencere.setLayout(new BorderLayout());

        
        pencere.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                geriDonParent(pencere);
            }
        });

        // ================= ÜST BAR =================
        JPanel ustPanel = new JPanel(new BorderLayout());
        ustPanel.setBackground(new Color(50, 97, 100));
        ustPanel.setPreferredSize(new Dimension(0, 65));

        JLabel baslik = new JLabel("  FLYLY  ===✈  UÇUŞ EKLE");
        baslik.setFont(new Font("Segoe UI", Font.BOLD, 24));
        baslik.setForeground(Color.WHITE);

        JButton geriBtn = new JButton("← Geri");
        geriBtn.setFocusPainted(false);
        geriBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        geriBtn.setForeground(Color.WHITE);
        geriBtn.setBackground(new Color(50, 97, 100));
        geriBtn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));

        geriBtn.addActionListener(e -> {
            pencere.dispose(); // windowClosed -> parent gösterilecek
        });

        JPanel sag = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        sag.setOpaque(false);
        sag.add(geriBtn);

        ustPanel.add(baslik, BorderLayout.WEST);
        ustPanel.add(sag, BorderLayout.EAST);

        pencere.add(ustPanel, BorderLayout.NORTH);

        // ================= ORTA =================
        JPanel ortaPanel = new JPanel(new GridBagLayout());
        ortaPanel.setBackground(new Color(245, 250, 250));

        JPanel kartPanel = new JPanel();
        kartPanel.setLayout(new BoxLayout(kartPanel, BoxLayout.Y_AXIS));
        kartPanel.setBackground(Color.WHITE);
        kartPanel.setPreferredSize(new Dimension(380, 520));
        kartPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        // === FORM ALANLARI ===
        JComboBox<String> firmaBox = new JComboBox<>(new String[]{"THY", "Pegasus", "AJet"});

        DefaultComboBoxModel<String> neredenModel = new DefaultComboBoxModel<>();
        DefaultComboBoxModel<String> nereyeModel  = new DefaultComboBoxModel<>();
        for (String c : CityConstants.TURKIYE_ILLER) {
            neredenModel.addElement(c);
            nereyeModel.addElement(c);
        }

        JComboBox<String> neredenBox = new JComboBox<>(neredenModel);
        JComboBox<String> nereyeBox  = new JComboBox<>(nereyeModel);
        if (neredenBox.getItemCount() > 0) neredenBox.setSelectedIndex(0);
        if (nereyeBox.getItemCount() > 1)  nereyeBox.setSelectedIndex(1);

        JDateChooser tarihChooser = new JDateChooser();
        tarihChooser.setDateFormatString("dd.MM.yyyy");
        tarihChooser.setDate(new java.util.Date());

        JComboBox<String> saatBox = new JComboBox<>(saatListesiOlustur(30));
        saatBox.setSelectedItem("00:00");

        JTextField fiyatField = new JTextField("1200");

        kartPanel.add(formSatiri("Firma", firmaBox));
        kartPanel.add(formSatiri("Nereden", neredenBox));
        kartPanel.add(formSatiri("Nereye", nereyeBox));
        kartPanel.add(formSatiri("Tarih", tarihChooser));
        kartPanel.add(formSatiri("Saat", saatBox));
        kartPanel.add(formSatiri("Fiyat (₺)", fiyatField));
        kartPanel.add(Box.createVerticalStrut(25));

        JButton kaydetButon = new JButton("Uçuşu Kaydet");
        kaydetButon.setBackground(new Color(52, 152, 219));
        kaydetButon.setForeground(Color.WHITE);
        kaydetButon.setFocusPainted(false);
        kaydetButon.setCursor(new Cursor(Cursor.HAND_CURSOR));
        kaydetButon.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        kartPanel.add(kaydetButon);

        // Dinamik "aynı il seçme" engeli yaptık
        final boolean[] isSyncing = {false};
        syncModelsExcludeSelected(neredenBox, nereyeBox, isSyncing);
        syncModelsExcludeSelected(nereyeBox, neredenBox, isSyncing);

        neredenBox.addActionListener(e -> {
            if (isSyncing[0]) return;
            syncModelsExcludeSelected(neredenBox, nereyeBox, isSyncing);
        });

        nereyeBox.addActionListener(e -> {
            if (isSyncing[0]) return;
            syncModelsExcludeSelected(nereyeBox, neredenBox, isSyncing);
        });

        // ================= KAYDET =================
        kaydetButon.addActionListener(e -> {
            String firma   = (String) firmaBox.getSelectedItem();
            String nereden = (String) neredenBox.getSelectedItem();
            String nereye  = (String) nereyeBox.getSelectedItem();

            java.util.Date secilenTarih = tarihChooser.getDate();
            if (secilenTarih == null) {
                JOptionPane.showMessageDialog(pencere, "Lütfen tarih seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String tarih = DATE_FMT.format(secilenTarih);

            String saat = (String) saatBox.getSelectedItem();
            String fiyatText = fiyatField.getText().trim();

            if (fiyatText.isEmpty()) {
                JOptionPane.showMessageDialog(pencere, "Lütfen fiyat girin!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (nereden != null && nereden.equals(nereye)) {
                JOptionPane.showMessageDialog(pencere, "Nereden ve Nereye aynı olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            double fiyat;
            try {
                fiyat = Double.parseDouble(fiyatText);
                if (fiyat <= 0) {
                    JOptionPane.showMessageDialog(pencere, "Fiyat 0'dan büyük olmalı!", "Hata", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(pencere, "Fiyat sayı olmalı! (Örn: 1200)", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            FlightDAO dao = new FlightDAO();
            boolean ok = dao.addFlight(firma, nereden, nereye, tarih, saat, fiyat);

            if (ok) {
                JOptionPane.showMessageDialog(pencere, "Uçuş kaydedildi ✅", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(pencere, "Uçuş kaydedilemedi ❌", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        ortaPanel.add(kartPanel);
        pencere.add(ortaPanel, BorderLayout.CENTER);
        pencere.setVisible(true);
    }

    private void geriDonParent(JFrame current) {
        // parent görünmüyorsa geri aç
        if (parent != null && parent.isDisplayable()) {
            parent.setVisible(true);
        }
    }

    private static String[] saatListesiOlustur(int dakikaAdimi) {
        java.util.List<String> list = new java.util.ArrayList<>();
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
        } else if (newModel.getSize() > 0) {
            targetBox.setSelectedIndex(0);
        }

        isSyncingFlag[0] = false;
    }

    private JPanel formSatiri(String etiket, JComponent alan) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

        JLabel label = new JLabel(etiket);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(50, 97, 100));

        alan.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        panel.add(label);
        panel.add(Box.createVerticalStrut(5));
        panel.add(alan);
        return panel;
    }
}
