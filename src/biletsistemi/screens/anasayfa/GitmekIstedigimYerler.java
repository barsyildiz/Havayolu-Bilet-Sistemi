package biletsistemi.screens.anasayfa;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.prefs.Preferences;

public class GitmekIstedigimYerler {

    private final Color ANA_RENK = new Color(50, 97, 100);
    private final Color ACIK_RENK = new Color(245, 250, 250);
    private final Color SECILI_OLMAYAN_YAZI = new Color(80, 80, 80);

    private final String[] sehirler = {
            "Adana", "Adıyaman", "Afyonkarahisar", "Ağrı", "Amasya", "Ankara", "Antalya", "Artvin", "Aydın", "Balıkesir",
            "Bilecik", "Bingöl", "Bitlis", "Bolu", "Burdur", "Bursa", "Çanakkale", "Çankırı", "Çorum", "Denizli",
            "Diyarbakır", "Edirne", "Elazığ", "Erzincan", "Erzurum", "Eskişehir", "Gaziantep", "Giresun", "Gümüşhane", "Hakkari",
            "Hatay", "Isparta", "Mersin", "İstanbul", "İzmir", "Kars", "Kastamonu", "Kayseri", "Kırklareli", "Kırşehir",
            "Kocaeli", "Konya", "Kütahya", "Malatya", "Manisa", "Kahramanmaraş", "Mardin", "Muğla", "Muş", "Nevşehir",
            "Niğde", "Ordu", "Rize", "Sakarya", "Samsun", "Siirt", "Sinop", "Sivas", "Tekirdağ", "Tokat",
            "Trabzon", "Tunceli", "Şanlıurfa", "Uşak", "Van", "Yozgat", "Zonguldak", "Aksaray", "Bayburt", "Karaman",
            "Kırıkkale", "Batman", "Şırnak", "Bartın", "Ardahan", "Iğdır", "Yalova", "Karabük", "Kilis", "Osmaniye", "Düzce"
    };

    private Preferences prefs;

    public void baslat() {
        prefs = Preferences.userRoot().node("biletSistemiGidilecekYerler");

        JFrame frame = new JFrame("Gitmek İstediğim Yerler");
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.getContentPane().setBackground(ACIK_RENK);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(ACIK_RENK);
        headerPanel.setBorder(new EmptyBorder(20, 20, 10, 20));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        JButton btnGeri = new ModernButton("← Geri", ANA_RENK);
        btnGeri.setPreferredSize(new Dimension(110, 40));
        btnGeri.setFont(new Font("Segoe UI Symbol", Font.BOLD, 18));
        btnGeri.setToolTipText("Kapat");
        btnGeri.addActionListener(e -> frame.dispose());
        leftPanel.add(btnGeri);

        JLabel title = new JLabel("🗺 Gitmek İstediğim Şehirler", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(ANA_RENK);

        JPanel rightDummy = new JPanel();
        rightDummy.setPreferredSize(new Dimension(50, 40));
        rightDummy.setOpaque(false);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(title, BorderLayout.CENTER);
        headerPanel.add(rightDummy, BorderLayout.EAST);

        JPanel gridPanel = new JPanel(new GridLayout(0, 4, 15, 15));
        gridPanel.setBackground(ACIK_RENK);
        gridPanel.setBorder(new EmptyBorder(10, 30, 30, 30));

        for (String sehir : sehirler) {
            gridPanel.add(new SehirButonu(sehir));
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getViewport().setBackground(ACIK_RENK);

        frame.add(headerPanel, BorderLayout.NORTH);
        frame.add(scrollPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    class SehirButonu extends JPanel {
        private final String sehirAdi;
        private boolean seciliMi;

        public SehirButonu(String isim) {
            this.sehirAdi = isim;
            this.seciliMi = prefs.getBoolean("sehir_" + isim, false);

            setPreferredSize(new Dimension(180, 50));
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    seciliMi = !seciliMi;
                    prefs.putBoolean("sehir_" + sehirAdi, seciliMi);
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Color arkaPlanRengi = seciliMi ? ANA_RENK : Color.WHITE;
            Color yaziRengi = seciliMi ? Color.WHITE : SECILI_OLMAYAN_YAZI;

            g2.setColor(new Color(0, 0, 0, 20));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 25, 25);

            g2.setColor(arkaPlanRengi);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 25, 25);

            if (!seciliMi) {
                g2.setColor(new Color(200, 200, 200));
                g2.setStroke(new BasicStroke(1));
                g2.drawRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 25, 25);
            }

            g2.setColor(yaziRengi);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 15));

            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(sehirAdi)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent() - 2;

            g2.drawString(sehirAdi, x, y);

            if (seciliMi) {
                g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 12));
                g2.drawString("✈", 15, y);
            }
        }
    }

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
                public void mouseEntered(MouseEvent e) { setBackground(hoverColor); repaint(); }
                @Override
                public void mouseExited(MouseEvent e) { setBackground(normalColor); repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(getModel().isRollover() ? hoverColor : normalColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);

            super.paintComponent(g);
            g2.dispose();
        }
    }
}
