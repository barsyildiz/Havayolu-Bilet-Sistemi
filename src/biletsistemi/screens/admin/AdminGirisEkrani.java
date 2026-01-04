package biletsistemi.screens.admin;

import biletsistemi.UserSession;
import biletsistemi.database.AdminDAO;
import biletsistemi.screens.login.GirisEkrani;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class AdminGirisEkrani {

    public void baslat() {
        JFrame frame = new JFrame("FLYLY - Admin Girişi");
        frame.setSize(450, 650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);

        Color anaRenk = new Color(50, 97, 100);
        Color arkaPlan = new Color(245, 245, 245);

        frame.getContentPane().setBackground(arkaPlan);
        frame.setLayout(new BorderLayout());

        JPanel header = new JPanel(new GridBagLayout());
        header.setPreferredSize(new Dimension(0, 80));
        header.setBackground(anaRenk);

        JLabel titleText = new JLabel("FLYLY ");
        titleText.setFont(new Font("Segoe UI", Font.BOLD, 38));
        titleText.setForeground(Color.WHITE);

        JLabel titlePlane = new JLabel("===✈");
        titlePlane.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 38));
        titlePlane.setForeground(Color.WHITE);

        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        titlePanel.setOpaque(false);
        titlePanel.add(titleText);
        titlePanel.add(titlePlane);

        header.add(titlePanel);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setPreferredSize(new Dimension(340, 520));
        loginCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblGiris = label("ADMIN GİRİŞİ", 22, true);
        JLabel lblAlt = label("Yönetici paneline giriş yapın", 13, false);
        lblAlt.setForeground(new Color(50, 97, 100));

        JLabel lblName = label("Admin Kullanıcı Adı", 12, true);
        JTextField txtName = input();

        JLabel lblSifre = label("Şifre", 12, true);
        JPasswordField txtPass = passwordField();

        char defaultEcho = txtPass.getEchoChar();

        JCheckBox showPass = new JCheckBox("Şifreyi Göster");
        showPass.setOpaque(false);
        showPass.setForeground(new Color(50, 97, 100));
        showPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        showPass.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        showPass.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPass.addActionListener(ev -> {
            if (showPass.isSelected()) txtPass.setEchoChar((char) 0);
            else txtPass.setEchoChar(defaultEcho);
        });

        JButton loginBtn = button("GİRİŞ YAP", Color.WHITE, new Color(200, 70, 70));
        JButton backBtn  = button("GERİ DÖN", Color.WHITE, anaRenk);

        loginCard.add(lblGiris);
        loginCard.add(lblAlt);
        loginCard.add(Box.createVerticalStrut(25));

        loginCard.add(lblName);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(txtName);
        loginCard.add(Box.createVerticalStrut(15));

        loginCard.add(lblSifre);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(txtPass);

        loginCard.add(Box.createVerticalStrut(6));
        loginCard.add(showPass);

        loginCard.add(Box.createVerticalStrut(20));
        loginCard.add(loginBtn);

        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(backBtn);

        centerWrapper.add(loginCard);
        frame.add(header, BorderLayout.NORTH);
        frame.add(centerWrapper, BorderLayout.CENTER);

        
        loginBtn.addActionListener(e -> {
            String u = txtName.getText().trim();
            String p = new String(txtPass.getPassword()).trim();

            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Kullanıcı adı ve şifre boş olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            AdminDAO dao = new AdminDAO();
            if (dao.login(u, p)) {
                // admin session açma kısmı
                UserSession.isAdmin = true;
                UserSession.username = u;

                frame.dispose();
                new Admin().baslat(u);

            } else {
                JOptionPane.showMessageDialog(frame, "Admin girişi başarısız!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        
        backBtn.addActionListener(e -> {
            frame.dispose();
            new GirisEkrani().baslat();
        });

        frame.setVisible(true);
    }

    private JLabel label(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(new Color(50, 97, 100));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private JTextField input() {
        JTextField field = new JTextField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    private JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    private JButton button(String text, Color bg, Color fg) {
        JButton btn = new JButton(text);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        return btn;
    }
}
