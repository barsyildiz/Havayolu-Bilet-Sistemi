package biletsistemi.screens.login;

import biletsistemi.UserSession;
import biletsistemi.database.UserDAO;
import biletsistemi.screens.anasayfa.AnaSayfaSwing;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GirisEkrani {

    public void baslat() {
        JFrame frame = new JFrame("FLYLY");
        frame.setSize(450, 650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        // Renkler
        Color anaRenk = new Color(50, 97, 100);
        Color arkaPlan = new Color(245, 245, 245);

        frame.getContentPane().setBackground(arkaPlan);
        frame.setLayout(new BorderLayout());

        // ÜST BAR
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

        // MERKEZ PANEL
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setPreferredSize(new Dimension(340, 520));
        loginCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        // Başlıklar
        JLabel lblGiris = label("ÜYE GİRİŞİ", 22, true);
        JLabel lblMerhaba = label("Hoş Geldiniz!", 13, false);
        lblMerhaba.setForeground(new Color(50, 97, 100));

        JLabel lblKayit = label("Hesabınız yok mu? Kayıt olun!", 13, false);

        JLabel lblName = label("Kullanıcı Adı", 12, true);
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
            if (showPass.isSelected()) {
                txtPass.setEchoChar((char) 0);      // şifre açık
            } else {
                txtPass.setEchoChar(defaultEcho);   // şifre kapalı
            }
        });

        // Butonlar
        JButton loginBtn = button("GİRİŞ YAP", Color.WHITE, anaRenk);
        JButton registerBtn = button("KAYIT OL", anaRenk, Color.WHITE);
        registerBtn.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        JButton outBtn = button("ÇIKIŞ", Color.WHITE, anaRenk);

        //  Admin butonu (login ekranında ayrı buton)
        JButton adminBtn = button("ADMIN GİRİŞİ", Color.WHITE, new Color(200, 70, 70));

        // BİLEŞENLER
        loginCard.add(lblGiris);
        loginCard.add(lblMerhaba);
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
        loginCard.add(lblKayit);
        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(registerBtn);

        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(adminBtn);

        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(outBtn);

        centerWrapper.add(loginCard);
        frame.add(header, BorderLayout.NORTH);
        frame.add(centerWrapper, BorderLayout.CENTER);

        //  BUTON OLAYLARI (username + şifre ile giriş)
        loginBtn.addActionListener(e -> {
            String girenUsername = txtName.getText().trim();
            String girenSifre = new String(txtPass.getPassword()).trim();

            if (girenUsername.isEmpty() || girenSifre.isEmpty()) {
                JOptionPane.showMessageDialog(frame, "Kullanıcı adı ve şifre boş olamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }

            UserDAO dao = new UserDAO();

            if (dao.loginByUsername(girenUsername, girenSifre)) {

                String email = dao.getEmailByUsername(girenUsername);

                UserSession.username = girenUsername;
                UserSession.email = (email == null) ? "" : email;

                frame.dispose();
                new AnaSayfaSwing().baslat(UserSession.username);

            } else {
                JOptionPane.showMessageDialog(frame, "Hatalı giriş!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerBtn.addActionListener(e -> new KayitEkrani().baslat());

        adminBtn.addActionListener(e -> {
            frame.dispose();
            new biletsistemi.screens.admin.AdminGirisEkrani().baslat();
        });

        outBtn.addActionListener(e -> System.exit(0));

        frame.setVisible(true);
    }

    // LABEL TASARIMLARI
    private JLabel label(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(new Color(50, 97, 100));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    // INPUT TASARIMLARI
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

    // BUTON TASARIMLARI
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
