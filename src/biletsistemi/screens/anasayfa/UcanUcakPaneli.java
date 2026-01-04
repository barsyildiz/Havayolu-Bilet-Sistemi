package biletsistemi.screens.anasayfa;

import javax.swing.*;
import java.awt.*;

public class UcanUcakPaneli extends JPanel
{
        private int x = -100;
        private int y = 200;

        public UcanUcakPaneli() {
            setOpaque(false);
            new Timer(10, e -> {
                x += 3;
                if (x > getWidth()) {
                    x = -300;
                    y = 300 + (int)(Math.random() * 400);
                }
                repaint();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 300));
            g.setColor(new Color(0, 0, 0, 48));
            g.drawString("✈", x, y);
        }

}
