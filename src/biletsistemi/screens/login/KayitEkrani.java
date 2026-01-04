package biletsistemi.screens.login;
import biletsistemi.database.UserDAO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;

public class KayitEkrani
{
    public void baslat(){

        JFrame frame = new JFrame("FLYLY");
        frame.setSize(450, 650);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Color anaRenk = new Color(50, 97, 100);
        Color arkaPlan = new Color(245, 245, 245);

        frame.getContentPane().setBackground(arkaPlan);
        frame.setLayout(new BorderLayout());

        // ÜST BAR
        JPanel header = new JPanel(new GridBagLayout());
        header.setPreferredSize(new Dimension(0, 80));
        header.setBackground(anaRenk);

        JLabel title = new JLabel("FLYLY");
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(Color.WHITE);
        header.add(title);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setOpaque(false);

        JPanel loginCard = new JPanel();
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setPreferredSize(new Dimension(340, 520));
        loginCard.setBorder(new EmptyBorder(25, 30, 25, 30));

        JLabel lblGiris = label("ÜYE KAYIT", 22, true);

        JLabel lblName = label("Kullanıcı Adı", 12, true);
        JTextField txtName = input();

        JLabel lblEmail = label("E-posta", 12, true);
        JTextField txtEmail = input();

        JLabel lblSifre = label("Şifre", 12, true);
        JPasswordField txtPass = passwordField();

        JButton loginBtn = button("KAYIT OL", Color.WHITE, anaRenk);
        JButton outBtn = button("GERİ DÖN", anaRenk, Color.WHITE);

        loginCard.add(lblGiris);
        loginCard.add(Box.createVerticalStrut(25));

        loginCard.add(lblName);
        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(txtName);
        loginCard.add(Box.createVerticalStrut(15));

        loginCard.add(lblEmail);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(txtEmail);
        loginCard.add(Box.createVerticalStrut(15));

        loginCard.add(lblSifre);
        loginCard.add(Box.createVerticalStrut(5));
        loginCard.add(txtPass);

        loginCard.add(Box.createVerticalStrut(35));
        loginCard.add(loginBtn);
        loginCard.add(Box.createVerticalStrut(10));
        loginCard.add(outBtn);

        outBtn.addActionListener(e -> {
            frame.dispose();
        });

       loginBtn.addActionListener(e -> {
       String isim = txtName.getText().trim();
       String email = txtEmail.getText().trim();
       String sifre = new String(txtPass.getPassword()).trim();

       if (isim.isEmpty() || email.isEmpty() || sifre.isEmpty()) {
        JOptionPane.showMessageDialog(frame, "Lütfen tüm alanları doldurun!", "Hata", JOptionPane.ERROR_MESSAGE);
        return;
       }

       UserDAO dao = new UserDAO();
       boolean ok = dao.register(isim, email, sifre);

       if (ok) {
        JOptionPane.showMessageDialog(frame, "Kayıt başarılı! Giriş yapabilirsiniz.", "Bilgi", JOptionPane.INFORMATION_MESSAGE);
        frame.dispose();
       } else {
        JOptionPane.showMessageDialog(frame, "Bu e-posta zaten kayıtlı olabilir!", "Hata", JOptionPane.ERROR_MESSAGE);
     }
      });
 


        centerWrapper.add(loginCard);
        frame.add(header, BorderLayout.NORTH);
        frame.add(centerWrapper, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return field;
    }

    private JTextField input() {
        JTextField field = new JTextField();
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

    private JLabel label(String text, int size, boolean bold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", bold ? Font.BOLD : Font.PLAIN, size));
        label.setForeground(new Color(50, 97, 100));
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

}
