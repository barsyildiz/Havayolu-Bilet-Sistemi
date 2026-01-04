package biletsistemi.screens.anasayfa;

import biletsistemi.UserSession;
import biletsistemi.database.UserDAO;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.net.URL;

public class Ayarlar {

    // Tema renkleri
    private final Color ANA_RENK = new Color(50, 97, 100);
    private final Color ARKA_PLAN = new Color(245, 250, 250);
    private final Color YAZI_RENGI = new Color(60, 60, 60);

    private JLabel profilResmi;
    private JLabel bakiyeLabel;
    private JPanel avatarPanel;
    private String secilenResimYolu = "";

    private JTextField txtTelefon;
    private JTextField txtDogumTarihi;
    private JTextField txtAdres;
    private JTextField txtBakiyeEkle;

    private final UserDAO dao = new UserDAO();

   
    private final JFrame parentFrame;

    
    public Ayarlar() {
        this.parentFrame = null;
    }

    public Ayarlar(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void baslat() {

        String kullaniciAdi = UserSession.username;
        String eposta = UserSession.email;

        JFrame frame = new JFrame("Ayarlar & Profil");
        frame.setSize(480, 800);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(ARKA_PLAN);

        JPanel mainContent = new JPanel();
        mainContent.setLayout(new BoxLayout(mainContent, BoxLayout.Y_AXIS));
        mainContent.setBackground(ARKA_PLAN);
        mainContent.setBorder(new EmptyBorder(20, 30, 30, 30));

        JScrollPane scrollPane = new JScrollPane(mainContent);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(ARKA_PLAN);

        // =================== HEADER: GERİ ===================
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(480, 50));
        headerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton btnGeri = new ModernButton("← Geri", ANA_RENK);
        btnGeri.setPreferredSize(new Dimension(130, 42));
        btnGeri.setFont(new Font("Segoe UI", Font.BOLD, 16));

       
        btnGeri.addActionListener(e -> {
            frame.dispose();
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            } else {
                new AnaSayfaSwing().baslat(UserSession.username);
            }
        });

        headerPanel.add(btnGeri);

        // =================== PROFİL ===================
        profilResmi = new JLabel();
        profilResmi.setAlignmentX(Component.CENTER_ALIGNMENT);
        profilResmi.setCursor(new Cursor(Cursor.HAND_CURSOR));
        profilResmi.setIcon(yuvarlakYaziIleIcon(120, "Avatar", "Değiştir"));

        avatarPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        avatarPanel.setBackground(ARKA_PLAN);
        avatarPanel.setVisible(false);

        UserDAO.UserProfile profile = null;
        if (kullaniciAdi != null && !kullaniciAdi.isBlank()) {
            profile = dao.getProfileByUsername(kullaniciAdi);
        }

        if (profile != null && profile.avatarPath != null && !profile.avatarPath.isBlank()) {
            setProfilResmi(profile.avatarPath);
            secilenResimYolu = profile.avatarPath;
        }

        avatarlariOlustur(frame);

        profilResmi.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                avatarPanel.setVisible(!avatarPanel.isVisible());
                frame.revalidate();
                frame.repaint();
            }
        });

        // =================== CÜZDAN (RoundedPanel) ===================
        JPanel bakiyePanel = new RoundedPanel(20);
        bakiyePanel.setLayout(new BoxLayout(bakiyePanel, BoxLayout.Y_AXIS));
        bakiyePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        bakiyePanel.setMaximumSize(new Dimension(400, 200));

        JLabel lblBakiyeBaslik = new JLabel("Cüzdanım");
        lblBakiyeBaslik.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblBakiyeBaslik.setForeground(ANA_RENK);
        lblBakiyeBaslik.setAlignmentX(Component.CENTER_ALIGNMENT);

        int mevcutBakiye = (profile == null) ? 0 : profile.balance;

        bakiyeLabel = new JLabel(mevcutBakiye + " TL");
        bakiyeLabel.setFont(new Font("Segoe UI", Font.BOLD, 36));
        bakiyeLabel.setForeground(new Color(39, 174, 96));
        bakiyeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel eklemeSatiri = new JPanel(new FlowLayout(FlowLayout.CENTER));
        eklemeSatiri.setOpaque(false);

        JLabel lblTutar = new JLabel("Tutar: ");
        lblTutar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblTutar.setForeground(YAZI_RENGI);

        txtBakiyeEkle = stilVerilmisTextField(5);
        txtBakiyeEkle.setToolTipText("Miktar");
        ozelInputFiltresi(txtBakiyeEkle, 6, "\\d+");

        JButton btnYukle = new ModernButton("Yükle", new Color(52, 152, 219));
        btnYukle.setPreferredSize(new Dimension(90, 38));

        btnYukle.addActionListener(e -> {
            String miktarStr = txtBakiyeEkle.getText().trim();
            if (miktarStr.isEmpty()) return;

            int eklenecek = Integer.parseInt(miktarStr);
            int mevcut = dao.getBalanceByUsername(kullaniciAdi);

            if (mevcut + eklenecek > 99999) {
                JOptionPane.showMessageDialog(frame, "Cüzdan limiti aşıldı!");
                return;
            }

            int yeniBakiye = mevcut + eklenecek;

            if (dao.updateBalance(kullaniciAdi, yeniBakiye)) {
                bakiyeLabel.setText(yeniBakiye + " TL");
                txtBakiyeEkle.setText("");
                JOptionPane.showMessageDialog(frame, eklenecek + " TL Yüklendi!");
            } else {
                JOptionPane.showMessageDialog(frame, "Bakiye güncellenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        eklemeSatiri.add(lblTutar);
        eklemeSatiri.add(txtBakiyeEkle);
        eklemeSatiri.add(btnYukle);

        bakiyePanel.add(Box.createVerticalStrut(10));
        bakiyePanel.add(lblBakiyeBaslik);
        bakiyePanel.add(Box.createVerticalStrut(5));
        bakiyePanel.add(bakiyeLabel);
        bakiyePanel.add(Box.createVerticalStrut(8));
        bakiyePanel.add(eklemeSatiri);
        bakiyePanel.add(Box.createVerticalStrut(10));

        // =================== FORM (RoundedPanel) ===================
        JPanel formPanel = new RoundedPanel(20);
        formPanel.setLayout(new GridLayout(0, 1, 10, 10));
        formPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        formPanel.setMaximumSize(new Dimension(400, 450));

        formPanel.add(bilgiLabelOlustur("Kullanıcı Adı:", kullaniciAdi == null ? "" : kullaniciAdi));
        formPanel.add(bilgiLabelOlustur("E-posta:", eposta == null ? "" : eposta));

        formPanel.add(baslikLabel("Cep Telefonu:"));
        txtTelefon = stilVerilmisTextField(16);
        txtTelefon.setText(profile == null ? "" : nullToEmpty(profile.phone));
        ozelInputFiltresi(txtTelefon, 16, "[\\d\\s]+");
        formPanel.add(txtTelefon);

        formPanel.add(baslikLabel("Doğum Tarihi (GG.AA.YYYY):"));
        txtDogumTarihi = stilVerilmisTextField(15);
        txtDogumTarihi.setText(profile == null ? "" : nullToEmpty(profile.birthDate));
        ozelInputFiltresi(txtDogumTarihi, 10, "[\\d\\.]+");
        formPanel.add(txtDogumTarihi);

        formPanel.add(baslikLabel("Adres:"));
        txtAdres = stilVerilmisTextField(20);
        txtAdres.setText(profile == null ? "" : nullToEmpty(profile.address));
        formPanel.add(txtAdres);

        JButton btnGenelKaydet = new ModernButton("Tüm Değişiklikleri Kaydet", ANA_RENK);
        btnGenelKaydet.setPreferredSize(new Dimension(320, 52));
        btnGenelKaydet.setAlignmentX(Component.CENTER_ALIGNMENT);

        btnGenelKaydet.addActionListener(e -> {
            boolean ok = dao.updateProfile(
                    kullaniciAdi,
                    txtTelefon.getText().trim(),
                    txtDogumTarihi.getText().trim(),
                    txtAdres.getText().trim(),
                    secilenResimYolu
            );

            if (ok) {
                JOptionPane.showMessageDialog(frame, "Bilgiler başarıyla güncellendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
                try { AnaSayfaSwing.profilGuncelle(secilenResimYolu); } catch (Exception ex) { }
            } else {
                JOptionPane.showMessageDialog(frame, "Bilgiler güncellenemedi!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        // =================== SAYFAYA EKLE ===================
        mainContent.add(headerPanel);
        mainContent.add(Box.createVerticalStrut(10));
        mainContent.add(profilResmi);
        mainContent.add(Box.createVerticalStrut(15));
        mainContent.add(avatarPanel);
        mainContent.add(Box.createVerticalStrut(15));
        mainContent.add(bakiyePanel);
        mainContent.add(Box.createVerticalStrut(25));
        mainContent.add(formPanel);
        mainContent.add(Box.createVerticalStrut(30));
        mainContent.add(btnGenelKaydet);
        mainContent.add(Box.createVerticalStrut(30));

        frame.setContentPane(scrollPane);
        frame.setVisible(true);
    }

    // ================= UI Helpers =================

    private JLabel baslikLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(ANA_RENK);
        return lbl;
    }

    private JPanel bilgiLabelOlustur(String baslik, String veri) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);

        JLabel l1 = new JLabel(baslik + " ");
        l1.setFont(new Font("Segoe UI", Font.BOLD, 14));
        l1.setForeground(ANA_RENK);

        JLabel l2 = new JLabel(veri == null ? "" : veri);
        l2.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        l2.setForeground(YAZI_RENGI);

        p.add(l1);
        p.add(l2);
        return p;
    }

    private JTextField stilVerilmisTextField(int columns) {
        JTextField field = new JTextField(columns);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(YAZI_RENGI);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220), 1, true),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        return field;
    }

    private void ozelInputFiltresi(JTextField field, int limit, String pattern) {
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new DocumentFilter() {
            @Override
            public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                if (string == null) return;
                if ((fb.getDocument().getLength() + string.length()) <= limit && string.matches(pattern)) {
                    super.insertString(fb, offset, string, attr);
                }
            }
            @Override
            public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                if (text == null) return;
                if ((fb.getDocument().getLength() - length + text.length()) <= limit && text.matches(pattern)) {
                    super.replace(fb, offset, length, text, attrs);
                }
            }
        });
    }

    private void avatarlariOlustur(JFrame frame) {
        String[] avatarlar = {
                "/assets/ucakavatar1.jpg", "/assets/ucakavatar2.jpg",
                "/assets/ucakavatar3.jpg", "/assets/ucakavatar4.jpg",
                "/assets/ucakavatar5.jpg"
        };

        avatarPanel.removeAll();

        for (String yol : avatarlar) {
            URL imgURL = getClass().getResource(yol);
            if (imgURL != null) {
                try {
                    BufferedImage orj = ImageIO.read(imgURL);
                    Image kucuk = orj.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    JLabel iconLabel = new JLabel(yuvarlakYap(kucuk, 50));
                    iconLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));

                    iconLabel.addMouseListener(new MouseAdapter() {
                        @Override
                        public void mouseClicked(MouseEvent e) {
                            setProfilResmi(yol);
                            secilenResimYolu = yol;
                            frame.revalidate();
                            frame.repaint();
                        }
                    });

                    avatarPanel.add(iconLabel);

                } catch (Exception e) { e.printStackTrace(); }
            }
        }
    }

    private void setProfilResmi(String path) {
        try {
            URL url = getClass().getResource(path);
            if (url != null) {
                BufferedImage img = ImageIO.read(url);
                profilResmi.setIcon(yuvarlakYap(img, 120));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ImageIcon yuvarlakYap(Image img, int size) {
        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
        g2.drawImage(img, 0, 0, size, size, null);

        if (size > 100) {
            g2.setClip(null);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
            g2.setColor(new Color(0, 0, 0, 40));
            g2.fillOval(0, 0, size, size);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
            FontMetrics fm = g2.getFontMetrics();
            String yazi = "Değiştir";
            g2.drawString(yazi, (size - fm.stringWidth(yazi)) / 2, (size / 2) + 5);
        }
        g2.dispose();
        return new ImageIcon(bi);
    }

    private ImageIcon yuvarlakYaziIleIcon(int size, String s1, String s2) {
        BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(225, 225, 225));
        g2.fillOval(0, 0, size, size);
        g2.setColor(Color.GRAY);
        g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(s1, (size - fm.stringWidth(s1)) / 2, size / 2 - 5);
        g2.drawString(s2, (size - fm.stringWidth(s2)) / 2, size / 2 + 15);
        g2.dispose();
        return new ImageIcon(bi);
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }

    // ================= RoundedPanel =================
    class RoundedPanel extends JPanel {
        private final int cornerRadius;

        public RoundedPanel(int radius) {
            super();
            this.cornerRadius = radius;
            setOpaque(false);
            setBorder(new EmptyBorder(15, 20, 15, 20));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(new Color(0, 0, 0, 15));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, cornerRadius, cornerRadius);

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, cornerRadius, cornerRadius);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    // ================= ModernButton =================
    class ModernButton extends JButton {
        private final Color normalColor;
        private final Color hoverColor;

        public ModernButton(String text, Color bg) {
            super(text);
            this.normalColor = bg;
            this.hoverColor = bg.brighter();

            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setForeground(Color.WHITE);
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    setBackground(hoverColor);
                    repaint();
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    setBackground(normalColor);
                    repaint();
                }
            });

            setBackground(normalColor);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            if (getModel().isRollover()) g2.setColor(hoverColor);
            else g2.setColor(normalColor);

            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            super.paintComponent(g);
            g2.dispose();
        }
    }
}
