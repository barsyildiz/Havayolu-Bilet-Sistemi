
package biletsistemi.screens.bilet;

import javax.swing.*;
import java.awt.*;
import java.net.URL;


public class UcakKoltukDuzeni extends JPanel {

    private JLayeredPane katmanPaneli;
    private final int HEDEF_GENISLIK = 300;

   
    private JLabel secilenKoltukLabel;

    private Runnable onSeatSelected;

    private JButton seciliButon = null;

    public UcakKoltukDuzeni() {
        this(null, null);
    }

    public UcakKoltukDuzeni(JLabel koltukLabel) {
        this(koltukLabel, null);
    }

    public UcakKoltukDuzeni(JLabel koltukLabel, Runnable onSeatSelected) {
        this.secilenKoltukLabel = koltukLabel;
        this.onSeatSelected = onSeatSelected;

        setLayout(new BorderLayout());
        setOpaque(false);
        setPreferredSize(new Dimension(319, 0));

        bilesenleriHazirla();
    }

    private void bilesenleriHazirla() {
        katmanPaneli = new JLayeredPane();

        
        URL resimURL = getClass().getResource("/images/acikTemaUcak.jpg");

        if (resimURL != null) {
            ImageIcon orijinalIcon = new ImageIcon(resimURL);
            int hedefYukseklik = (orijinalIcon.getIconHeight() * HEDEF_GENISLIK) / orijinalIcon.getIconWidth();

            Image olcekliResim = orijinalIcon.getImage().getScaledInstance(HEDEF_GENISLIK, hedefYukseklik, Image.SCALE_SMOOTH);
            ImageIcon icon = new ImageIcon(olcekliResim);

            JLabel arkaPlan = new JLabel(icon);
            arkaPlan.setBounds(0, 0, HEDEF_GENISLIK, hedefYukseklik);

            katmanPaneli.setPreferredSize(new Dimension(HEDEF_GENISLIK, hedefYukseklik));
            katmanPaneli.add(arkaPlan, JLayeredPane.DEFAULT_LAYER);

            // koltukları ekle
            koltuklariOlustur(hedefYukseklik);

        } else {
            System.err.println("HATA: /images/acikTemaUcak.jpg bulunamadı!");
        }

        JScrollPane scrollPane = new JScrollPane(katmanPaneli);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);

        add(scrollPane, BorderLayout.CENTER);
    }

    private void koltuklariOlustur(int toplamYukseklik) {
        int satirSayisi = 22;
        int koltukW = 22;
        int koltukH = 21;
        int baslangicY = 154;
        int koridorW = 50;
        int kolonBosluk = 15;
        int orta = HEDEF_GENISLIK / 2;

        for (int i = 0; i < satirSayisi; i++) {
            int y = baslangicY + (i * (koltukH + 13));

            // Sol Blok (A ve C)
            koltukButonuEkle(orta - (koridorW / 2) - koltukW, y, koltukW, koltukH, (i + 1) + "C");
            koltukButonuEkle(orta - (koridorW / 2) - (2 * koltukW) - kolonBosluk, y, koltukW, koltukH, (i + 1) + "A");

            // Sağ Blok (D ve F)
            int sagKayma = 4;
            koltukButonuEkle(orta + (koridorW / 2) + sagKayma, y, koltukW, koltukH, (i + 1) + "D");
            koltukButonuEkle(orta + (koridorW / 2) + sagKayma + koltukW + kolonBosluk, y, koltukW, koltukH, (i + 1) + "F");
        }
    }

    private void koltukButonuEkle(int x, int y, int w, int h, String ad) {
        JButton btn = new JButton(ad);
        btn.setBounds(x, y, w, h);
        btn.setFont(new Font("Arial Narrow", Font.BOLD, 9));
        btn.setMargin(new Insets(0, 0, 0, 0));
        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(255, 255, 255, 100)));
        btn.setForeground(Color.DARK_GRAY);

        btn.addActionListener(e -> {
            // önce eski seçimi kapat (tek seçim)
            if (seciliButon != null && seciliButon != btn) {
                seciliButon.setContentAreaFilled(false);
                seciliButon.setForeground(Color.DARK_GRAY);
            }

            // bu butonu seç
            seciliButon = btn;
            btn.setContentAreaFilled(true);
            btn.setBackground(new Color(71, 166, 112, 180)); // yeşil
            btn.setForeground(Color.WHITE);

            // dışarıdaki label’a yaz
            if (secilenKoltukLabel != null) {
                secilenKoltukLabel.setText(ad);
            }

            // callback
            if (onSeatSelected != null) {
                onSeatSelected.run();
            }
        });

        katmanPaneli.add(btn, JLayeredPane.PALETTE_LAYER);
    }

    // dışarıdan çağırırsan sıfırlar
    public void sifirla() {
        if (seciliButon != null) {
            seciliButon.setContentAreaFilled(false);
            seciliButon.setForeground(Color.DARK_GRAY);
            seciliButon = null;
        }
        if (secilenKoltukLabel != null) {
            secilenKoltukLabel.setText("Seçilmedi");
        }
        if (onSeatSelected != null) {
            onSeatSelected.run();
        }
    }
}
