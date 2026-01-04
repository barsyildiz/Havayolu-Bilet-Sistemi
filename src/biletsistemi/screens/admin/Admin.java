package biletsistemi.screens.admin;

import biletsistemi.UserSession;
import biletsistemi.screens.anasayfa.UcanUcakPaneli;
import biletsistemi.screens.login.GirisEkrani;

import javax.swing.*;
import java.awt.*;

public class Admin {

    private JLabel middleLabel;

    private final Color ANA_RENK = new Color(50, 97, 100);
    private final Color HOVER_RENK = new Color(225, 245, 245);

    public void baslat(String adminAdi) {

        if (!UserSession.isAdmin) {
            JOptionPane.showMessageDialog(null,
                    "Bu sayfa sadece adminlere açıktır!",
                    "Yetkisiz Erişim",
                    JOptionPane.ERROR_MESSAGE);
            new GirisEkrani().baslat();
            return;
        }

        JFrame frame = new JFrame("Admin Paneli");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 700);
        frame.setLocationRelativeTo(null);
        frame.setResizable(true);

        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setLayout(new OverlayLayout(layeredPane));
        frame.setContentPane(layeredPane);

        UcanUcakPaneli ucakPaneli = new UcanUcakPaneli();
        ucakPaneli.setOpaque(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setOpaque(false);

        // ================= TOP BAR =================
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(0, 70));
        topBar.setBackground(ANA_RENK);

        JLabel title = new JLabel("FLYLY ===✈ ADMIN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title.setForeground(Color.WHITE);
        title.setBorder(BorderFactory.createEmptyBorder(0, 25, 0, 0));

        JLabel adminText = new JLabel("Hoş geldin Admin: " + adminAdi);
        adminText.setForeground(Color.WHITE);
        adminText.setFont(new Font("Segoe UI", Font.BOLD, 14));
        adminText.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 25));

        topBar.add(title, BorderLayout.WEST);
        topBar.add(adminText, BorderLayout.EAST);

        // ================= ORTA (2 SÜTUN) =================
        JPanel center = new JPanel(new GridLayout(1, 2, 30, 0));
        center.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        center.setOpaque(false);

        // ================= SOL MENÜ =================
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setOpaque(false);

        left.add(Box.createVerticalGlue());

        // Uçuş Ekle: parent frame gönder
        left.add(createModernButton("» Uçuş Ekle", () -> {
            frame.setVisible(false);
            new AdminUcusEkleSwing(frame).baslat();
        }));
        left.add(Box.createVerticalStrut(20));

        //  Uçuşları Listele: parent frame gönder
        left.add(createModernButton("» Uçuşları Listele", () -> {
            frame.setVisible(false);
            new AdminUcusListele(frame).baslat();
        }));
        left.add(Box.createVerticalStrut(20));

        //  Güvenli Çıkış: her şeyi kapat + login maksat ekranları açık bırakmayalım kötü görünmesin
        ModernMenuButton exitBtn = createModernButton("← Güvenli Çıkış", this::guvenliCikisVeLogin);
        exitBtn.setHoverColor(new Color(200, 70, 70));
        left.add(exitBtn);

        left.add(Box.createVerticalGlue());

        // ================= SAĞ / ORTA AÇIKLAMA =================
        JPanel middle = new JPanel(new BorderLayout());
        middle.setOpaque(false);

        middleLabel = new JLabel(
                "<html><center><h2>ADMIN KONTROL PANELİ</h2>" +
                        "<p>Buradan uçuş ekleyebilir,<br>uçuşları listeleyebilir ve yönetebilirsiniz.</p></center></html>",
                SwingConstants.CENTER
        );
        middleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        middleLabel.setForeground(new Color(40, 40, 40));
        middle.add(middleLabel, BorderLayout.CENTER);

        center.add(left);
        center.add(middle);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(center, BorderLayout.CENTER);

        layeredPane.add(ucakPaneli, Integer.valueOf(0));
        layeredPane.add(mainPanel, Integer.valueOf(1));

        frame.setVisible(true);
    }

 
    private void guvenliCikisVeLogin() {
        UserSession.isAdmin = false;

        for (Window w : Window.getWindows()) {
            try {
                if (w != null && w.isDisplayable()) w.dispose();
            } catch (Exception ignored) { }
        }

        SwingUtilities.invokeLater(() -> new GirisEkrani().baslat());
    }

    // Modern buton üretici
    private ModernMenuButton createModernButton(String text, Runnable action) {
        ModernMenuButton btn = new ModernMenuButton(text, action);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        return btn;
    }

    // ================== MODERN BUTON SINIFI ==================
    class ModernMenuButton extends JPanel {
        private final String text;
        private final Runnable action;

        private Color baseColor = Color.WHITE;
        private Color hoverColor = HOVER_RENK;
        private Color pressColor = new Color(200, 230, 230);
        private Color textColor = ANA_RENK;

        private Color currentColor;

        public ModernMenuButton(String text, Runnable action) {
            this.text = text;
            this.action = action;
            this.currentColor = baseColor;

            setPreferredSize(new Dimension(300, 60));
            setMaximumSize(new Dimension(300, 60));
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    currentColor = hoverColor;
                    repaint();
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    currentColor = baseColor;
                    repaint();
                }

                @Override
                public void mousePressed(java.awt.event.MouseEvent e) {
                    currentColor = pressColor;
                    repaint();
                }

                @Override
                public void mouseReleased(java.awt.event.MouseEvent e) {
                    if (contains(e.getPoint())) action.run();
                    currentColor = hoverColor;
                    repaint();
                }
            });
        }

        public void setHoverColor(Color c) {
            this.hoverColor = c;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            // Gölge
            g2.setColor(new Color(0, 0, 0, 25));
            g2.fillRoundRect(3, 3, getWidth() - 6, getHeight() - 6, 30, 30);

            // Gövde
            g2.setColor(currentColor);
            g2.fillRoundRect(0, 0, getWidth() - 3, getHeight() - 3, 30, 30);

            // Yazı
            g2.setColor(textColor);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));

            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(text)) / 2;
            int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent() - 2;

            g2.drawString(text, x, y);
        }
    }
}
