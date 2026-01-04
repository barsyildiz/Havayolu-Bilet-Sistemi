package biletsistemi.screens.ucuslarim;

import biletsistemi.UserSession;
import biletsistemi.database.Ticket;
import biletsistemi.database.TicketDAO;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Ucuslarim {

    private final JFrame parentFrame;


    public Ucuslarim() {
        this.parentFrame = null;
    }

    public Ucuslarim(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public void baslat() {
        JFrame pencere = new JFrame("Uçuşlarım");
        pencere.setSize(600, 750);
        pencere.setResizable(false);
        pencere.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        pencere.setLayout(new BorderLayout());

        Color ANA_RENK = new Color(50, 97, 100);

        // ================= ÜST BAR =================
        JPanel ustBar = new JPanel(new BorderLayout());
        ustBar.setBackground(ANA_RENK);
        ustBar.setPreferredSize(new Dimension(0, 70));

        //================= Sol taraf: logo + başlık
        JPanel solBaslik = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 15));
        solBaslik.setOpaque(false);

        JLabel titleText = new JLabel("FLYLY ");
        titleText.setFont(new Font("Segoe UI", Font.BOLD, 30));
        titleText.setForeground(Color.WHITE);

        JLabel titlePlane = new JLabel("===✈");
        titlePlane.setFont(new Font("Segoe UI Symbol", Font.BOLD, 30));
        titlePlane.setForeground(Color.WHITE);

        JLabel title2Text = new JLabel(" UÇUŞLARIM ");
        title2Text.setFont(new Font("Segoe UI", Font.BOLD, 30));
        title2Text.setForeground(Color.WHITE);

        solBaslik.add(titleText);
        solBaslik.add(titlePlane);
        solBaslik.add(title2Text);

        
        JPanel sagButon = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 14));
        sagButon.setOpaque(false);

        JButton geriBtn = new ModernButton("← Geri", ANA_RENK);
        geriBtn.setPreferredSize(new Dimension(120, 42));
        geriBtn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        geriBtn.setToolTipText("Ana sayfaya dön");

        sagButon.add(geriBtn);

        ustBar.add(solBaslik, BorderLayout.WEST);
        ustBar.add(sagButon, BorderLayout.EAST);

        pencere.add(ustBar, BorderLayout.NORTH);

        // ================= LİSTE PANEL (SCROLL) =================
        JPanel listePanel = new JPanel();
        listePanel.setLayout(new BoxLayout(listePanel, BoxLayout.Y_AXIS));
        listePanel.setBackground(Color.WHITE);

        
        String email = UserSession.email;

        if (email == null || email.isBlank()) {
            JLabel bos = new JLabel("Kullanıcı bilgisi bulunamadı. Lütfen yeniden giriş yapın.");
            bos.setFont(new Font("Segoe UI", Font.BOLD, 16));
            bos.setForeground(new Color(192, 57, 43));
            bos.setAlignmentX(Component.CENTER_ALIGNMENT);
            listePanel.add(Box.createVerticalStrut(30));
            listePanel.add(bos);
        } else {
            TicketDAO dao = new TicketDAO();
            List<Ticket> tickets = dao.getTicketsByUserEmail(email);

            if (tickets.isEmpty()) {
                JLabel bos = new JLabel("Henüz bilet kaydınız yok.");
                bos.setFont(new Font("Segoe UI", Font.BOLD, 16));
                bos.setForeground(new Color(50, 97, 100));
                bos.setAlignmentX(Component.CENTER_ALIGNMENT);
                listePanel.add(Box.createVerticalStrut(30));
                listePanel.add(bos);
            } else {
                for (Ticket t : tickets) {
                    listePanel.add(Box.createVerticalStrut(20));
                    listePanel.add(biletKutusuOlustur(t));
                }
                listePanel.add(Box.createVerticalStrut(20));
            }
        }

        geriBtn.addActionListener(e -> {
            pencere.dispose();
            if (parentFrame != null) {
                parentFrame.setVisible(true); 
            } else {
                
            }
        });

        // ScrollPaneL
        JScrollPane scroll = new JScrollPane(listePanel);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(16);
        scroll.setBackground(Color.WHITE);

        pencere.add(scroll, BorderLayout.CENTER);
        pencere.setLocationRelativeTo(null);
        pencere.setVisible(true);
    }

   
    private JPanel biletKutusuOlustur(Ticket t) {

        JPanel biletKutusu = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    Image img = new ImageIcon(Ucuslarim.class.getResource("/images/bilet.jpeg")).getImage();
                    g.drawImage(img, 0, 0, getWidth(), getHeight(), this);
                } catch (Exception e) {
                    g.setColor(Color.WHITE);
                    g.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                }
            }
        };

        Dimension biletBoyutu = new Dimension(530, 230);
        biletKutusu.setPreferredSize(biletBoyutu);
        biletKutusu.setMinimumSize(biletBoyutu);
        biletKutusu.setMaximumSize(biletBoyutu);
        biletKutusu.setOpaque(false);
        biletKutusu.setAlignmentX(Component.CENTER_ALIGNMENT);

        String adSoyad = nullToEmpty(t.getFullName());
        String from    = nullToEmpty(t.getFromCity());
        String to      = nullToEmpty(t.getToCity());
        String tarih   = nullToEmpty(t.getFlightDate());
        String saat    = nullToEmpty(t.getFlightTime());
        String seat    = nullToEmpty(t.getSeat());

        JLabel adLabel = yaziOlustur(adSoyad, 14, Font.PLAIN);
        adLabel.setBounds(165, 45, 300, 30);
        biletKutusu.add(adLabel);

        JLabel neredenLabel = yaziOlustur(from, 14, Font.PLAIN);
        neredenLabel.setBounds(100, 76, 200, 30);
        biletKutusu.add(neredenLabel);

        JLabel nereyeLabel = yaziOlustur(to, 14, Font.PLAIN);
        nereyeLabel.setBounds(250, 76, 200, 30);
        biletKutusu.add(nereyeLabel);

        JLabel tarihLabel = yaziOlustur(tarih, 13, Font.PLAIN);
        tarihLabel.setBounds(40, 130, 120, 25);
        biletKutusu.add(tarihLabel);

        JLabel saatLabel = yaziOlustur(saat, 13, Font.PLAIN);
        saatLabel.setBounds(118, 130, 100, 25);
        biletKutusu.add(saatLabel);

        JLabel koltukLabel = yaziOlustur(seat, 16, Font.PLAIN);
        koltukLabel.setBounds(220, 130, 60, 25);
        biletKutusu.add(koltukLabel);

        return biletKutusu;
    }

    private JLabel yaziOlustur(String metin, int boyut, int stil) {
        JLabel label = new JLabel(metin);
        label.setFont(new Font("Segoe UI", stil, boyut));
        label.setForeground(new Color(50, 97, 100));
        return label;
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
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
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBackground(normalColor);

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
