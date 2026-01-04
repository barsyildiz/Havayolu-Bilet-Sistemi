package biletsistemi.screens.anasayfa;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class KampanyalarPanel extends JPanel {

    private CardLayout cardLayout;
    private JPanel cardPanel;
    private int currentCard = 1;

    public KampanyalarPanel() {
        setLayout(new BorderLayout());
        setOpaque(false);
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel header = new JLabel("Fırsatlar & Kampanyalar", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 22));
        header.setForeground(new Color(50, 97, 100));
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        add(header, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        cardPanel.add(createKampanyaCard("Yaz Fırsatı!", "Tüm uçuşlarda %20 indirim", new Color(255, 230, 230), "SUMMER"), "1");
        cardPanel.add(createKampanyaCard("Kış Tatili", "Kayak merkezlerine özel seferler", new Color(230, 255, 230), "WINTER"), "2");
        cardPanel.add(createKampanyaCard("Erken Rezervasyon", "Şimdi al seneye uç", new Color(230, 230, 255), "CLOCK"), "3");
        cardPanel.add(createKampanyaCard("Hafta Sonu Kaçamağı", "Cuma gidiş Pazar dönüş %15", new Color(255, 255, 230), "STAR"), "4");

        add(cardPanel, BorderLayout.CENTER);

        Timer timer = new Timer(3000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentCard++;
                if (currentCard > 4) currentCard = 1;
                cardLayout.show(cardPanel, String.valueOf(currentCard));
            }
        });
        timer.start();
    }

    private JPanel createKampanyaCard(String title, String description, Color bgColor, String type) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int size = 40;
                int spacing = 80;

                for (int startY = 10; startY < getHeight(); startY += spacing) {
                    for (int startX = 10; startX < getWidth(); startX += spacing) {
                        int x = startX + ((startY / spacing) % 2 == 0 ? 0 : spacing / 2);
                        int y = startY;

                        if ("SUMMER".equals(type)) {
                            g2.setColor(new Color(255, 165, 0, 25));
                            g2.fillOval(x, y, size, size);
                            g2.setColor(new Color(255, 215, 0, 40));
                            g2.setStroke(new BasicStroke(2));
                            for (int i = 0; i < 360; i += 45) {
                                double rad = Math.toRadians(i);
                                int x1 = x + size / 2 + (int) (Math.cos(rad) * (size / 2));
                                int y1 = y + size / 2 + (int) (Math.sin(rad) * (size / 2));
                                int x2 = x + size / 2 + (int) (Math.cos(rad) * (size / 2 + 6));
                                int y2 = y + size / 2 + (int) (Math.sin(rad) * (size / 2 + 6));
                                g2.drawLine(x1, y1, x2, y2);
                            }
                        } else if ("WINTER".equals(type)) {
                            g2.setColor(new Color(173, 216, 230, 50));
                            g2.setStroke(new BasicStroke(2));
                            int cx = x + size / 2;
                            int cy = y + size / 2;
                            int r = size / 2;
                            for (int i = 0; i < 3; i++) {
                                g2.drawLine(
                                        cx - (int) (r * Math.cos(Math.toRadians(i * 60))),
                                        cy - (int) (r * Math.sin(Math.toRadians(i * 60))),
                                        cx + (int) (r * Math.cos(Math.toRadians(i * 60))),
                                        cy + (int) (r * Math.sin(Math.toRadians(i * 60)))
                                );
                            }
                            g2.fillOval(cx - 3, cy - 3, 6, 6);
                        } else if ("CLOCK".equals(type)) {
                            g2.setColor(new Color(100, 100, 255, 25));
                            g2.fillOval(x, y, size, size);
                            g2.setColor(new Color(255, 255, 255, 60));
                            g2.setStroke(new BasicStroke(2));
                            g2.drawLine(x + size / 2, y + size / 2, x + size / 2, y + 5);
                            g2.drawLine(x + size / 2, y + size / 2, x + size - 10, y + size / 2);
                            g2.drawOval(x, y, size, size);
                        } else if ("STAR".equals(type)) {
                            g2.setColor(new Color(255, 215, 0, 30));
                            int[] xp = {x + 20, x + 25, x + 38, x + 28, x + 32, x + 20, x + 8, x + 12, x + 2, x + 15};
                            int[] yp = {y + 0, y + 15, y + 15, y + 23, y + 36, y + 28, y + 36, y + 23, y + 15, y + 15};
                            g2.fillPolygon(xp, yp, 10);
                        }
                    }
                }
            }
        };

        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(bgColor);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("<html><center>" + title + "</center></html>", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.PLAIN, 26));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblTitle.setForeground(new Color(50, 97, 100));

        JLabel lblDesc = new JLabel("<html><center>" + description + "</center></html>", SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblDesc.setForeground(new Color(55, 54, 54, 128));

        panel.add(Box.createVerticalGlue());
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(lblDesc);
        panel.add(Box.createVerticalGlue());

        return panel;
    }
}
