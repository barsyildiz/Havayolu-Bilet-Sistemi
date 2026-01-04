package biletsistemi.screens.anasayfa;

import biletsistemi.screens.bilet.BiletAlSwing;
import biletsistemi.screens.login.GirisEkrani;
import biletsistemi.screens.ucuslarim.Ucuslarim;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

public class AnaSayfaSwing {

    public static JLabel kucukProfilResmi;

    private JLabel middleLabel;
    private int index = 0;

    private final String[] firms = HavayollariIcerik.getFirmsHtml();

    
    private final Color ANA_RENK = new Color(50, 97, 100);
    private final Color ACIK_RENK = new Color(245, 250, 250);
    private final Color HOVER_RENK = new Color(225, 245, 245);

    public void baslat(String gelenKullaniciAdi) {

        JFrame frame = new JFrame("Bilet Sistemi");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        // KATMANLI YAPI
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        frame.setContentPane(layeredPane);

        // ARKA PLAN (UÇAK)
        UcanUcakPaneli ucakPaneli = new UcanUcakPaneli();
        ucakPaneli.setOpaque(false);

        // ANA PANEL
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // --- TOP BAR ---
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, 70)); 
        topBar.setBackground(ANA_RENK);

        JLabel titleText = new JLabel("FLYLY ");
        titleText.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleText.setForeground(Color.WHITE);
        titleText.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

        JLabel titlePlane = new JLabel("===✈");
        titlePlane.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 38));
        titlePlane.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
        titlePanel.setOpaque(false);
        titlePanel.add(titleText);
        titlePanel.add(titlePlane);

        JPanel sagUstPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 12));
        sagUstPanel.setOpaque(false);
        sagUstPanel.setPreferredSize(new Dimension(300, 70));

        JLabel welcome = new JLabel("Hoş geldin " + gelenKullaniciAdi);
        welcome.setFont(new Font("Segoe UI", Font.BOLD, 14));
        welcome.setForeground(Color.WHITE);

        kucukProfilResmi = new JLabel();
        kucukProfilResmi.setPreferredSize(new Dimension(40, 40));

        Preferences prefs = Preferences.userRoot().node("biletSistemiAyarlar");
        String kayitliResim = prefs.get("profilResmi", null);

        if (kayitliResim != null) {
            profilGuncelle(kayitliResim);
        } else {
            kucukProfilResmi.setIcon(anaSayfaYuvarlakYap(null));
        }

        sagUstPanel.add(welcome);
        sagUstPanel.add(kucukProfilResmi);

        topBar.add(titlePanel, BorderLayout.WEST);
        topBar.add(sagUstPanel, BorderLayout.EAST);

        // --- ORTA ALAN (3 SÜTUN) ---
        JPanel center = new JPanel(new GridLayout(1, 3, 30, 0));
        center.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        center.setOpaque(false);

        // --- SOL MENÜ (Modern butonlar) ---
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        left.add(Box.createVerticalGlue());

        
        left.add(createModernButton("» Anasayfa / Kayıtlar", () -> {
            frame.setVisible(false);
            new Ucuslarim(frame).baslat();
        }));
        left.add(Box.createVerticalStrut(20));

        left.add(createModernButton("» Bilet Satın Al", () -> new BiletAlSwing().baslat()));
        left.add(Box.createVerticalStrut(20));

        left.add(createModernButton("» Gitmek İstediğim Yerler", () -> new GitmekIstedigimYerler().baslat()));
        left.add(Box.createVerticalStrut(20));

        left.add(createModernButton("» Ayarlar", () -> new Ayarlar().baslat()));
        left.add(Box.createVerticalStrut(20));

        ModernMenuButton exitBtn = createModernButton("← Güvenli Çıkış", () -> {
            frame.dispose();
            new GirisEkrani().baslat();
        });
        exitBtn.setHoverColor(new Color(200, 70, 70));
        left.add(exitBtn);

        left.add(Box.createVerticalGlue());

        // --- ORTA VİTRİN ---
        JPanel middle = new JPanel(new BorderLayout());
        middle.setOpaque(false);

        middleLabel = new JLabel(firms[0], SwingConstants.CENTER);
        middleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 22));
        middle.add(middleLabel, BorderLayout.CENTER);

        new Timer(5000, e -> {
            index = (index + 1) % firms.length;
            middleLabel.setText(firms[index]);
        }).start();

        // --- SAĞ KAMPANYALAR ---
        KampanyalarPanel right = new KampanyalarPanel();

        center.add(left);
        center.add(middle);
        center.add(right);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        layeredPane.add(ucakPaneli, Integer.valueOf(0));
        layeredPane.add(mainPanel, Integer.valueOf(1));

        frame.setVisible(true);
    }

    // Modern buton üretici
    private ModernMenuButton createModernButton(String text, Runnable action) {
        ModernMenuButton btn = new ModernMenuButton(text, action);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    // ================== MODERN BUTON SINIFI ==================
    class ModernMenuButton extends JPanel {
        private final String originalText;
        private final Runnable action;

        private Color baseColor = Color.WHITE;
        private Color hoverColor = HOVER_RENK;
        private Color pressColor = new Color(200, 230, 230);
        private Color textColor = ANA_RENK;

        private Color currentColor;

        public ModernMenuButton(String text, Runnable action) {
            this.originalText = text;
            this.action = action;
            this.currentColor = baseColor;

            setPreferredSize(new Dimension(260, 55));
            setMaximumSize(new Dimension(260, 55));
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    currentColor = hoverColor;
                    repaint();
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    currentColor = baseColor;
                    repaint();
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    currentColor = pressColor;
                    repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (contains(e.getPoint())) action.run();
                    currentColor = hoverColor;
                    repaint();
                }
            });
        }

        public void setHoverColor(Color c) {
            this.hoverColor = c;
        }

        
        private String safeTextForFont(Font f, String text) {
            if (f.canDisplayUpTo(text) == -1) return text;

            String cleaned = text
                    .replaceAll("[\\x{1F300}-\\x{1FAFF}]", "")
                    .replaceAll("[\\x{2600}-\\x{27BF}]", "")
                    .replaceAll("\\s+", " ")
                    .trim();

            if (cleaned.isEmpty()) cleaned = "Menü";
            return cleaned;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // 1) Gölge
            g2.setColor(new Color(0, 0, 0, 30));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 30, 30);

            // 2) Buton gövdesi
            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 30, 30);

            // 3) Yazı
            g2.setColor(textColor);

            Font font = new Font("Segoe UI", Font.BOLD, 15);
            String displayText = safeTextForFont(font, originalText);

            g2.setFont(font);
            FontMetrics metrics = g2.getFontMetrics();
            int x = (getWidth() - metrics.stringWidth(displayText)) / 2;
            int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent() - 2;

            g2.drawString(displayText, x, y);
        }
    }

    // ================== PROFİL RESMİ ==================
    public static void profilGuncelle(String resimYolu) {
        if (kucukProfilResmi == null) return;
        try {
            if (resimYolu != null && !resimYolu.isEmpty()) {
                java.net.URL url = AnaSayfaSwing.class.getResource(resimYolu);
                if (url != null) {
                    java.awt.image.BufferedImage img = javax.imageio.ImageIO.read(url);
                    Image scaled = img.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                    kucukProfilResmi.setIcon(anaSayfaYuvarlakYap(scaled));
                }
            } else {
                kucukProfilResmi.setIcon(anaSayfaYuvarlakYap(null));
            }
            kucukProfilResmi.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Icon anaSayfaYuvarlakYap(Image img) {
        java.awt.image.BufferedImage bi = new java.awt.image.BufferedImage(40, 40, java.awt.image.BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = bi.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (img == null) {
            g2.setColor(Color.LIGHT_GRAY);
            g2.fillOval(0, 0, 40, 40);
        } else {
            g2.setColor(Color.WHITE);
            g2.fillOval(0, 0, 40, 40);
            g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, 40, 40));
            g2.drawImage(img, 0, 0, 40, 40, null);
        }
        g2.dispose();
        return new ImageIcon(bi);
    }
}
