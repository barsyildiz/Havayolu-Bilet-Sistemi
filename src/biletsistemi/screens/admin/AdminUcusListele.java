package biletsistemi.screens.admin;

import biletsistemi.UserSession;
import biletsistemi.database.FlightDAO;
import biletsistemi.screens.login.GirisEkrani;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class AdminUcusListele {

    private DefaultTableModel model;
    private final JFrame parent; 

    public AdminUcusListele(JFrame parent) {
        this.parent = parent;
    }

    public void baslat() {

        
        if (!UserSession.isAdmin) {
            JOptionPane.showMessageDialog(null,
                    "Bu sayfa sadece adminlere açıktır!",
                    "Yetkisiz Erişim",
                    JOptionPane.ERROR_MESSAGE);
            new GirisEkrani().baslat();
            return;
        }

        JFrame frame = new JFrame("FLYLY - Admin | Uçuşları Listele");
        frame.setSize(900, 560);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(WindowEvent e) {
                geriDonParent(frame);
            }
        });

        // ================= TOP BAR =================
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(new Color(50, 97, 100));
        top.setPreferredSize(new Dimension(0, 65));

        JLabel title = new JLabel("  FLYLY  ===✈  UÇUŞLARI LİSTELE");
        title.setForeground(Color.WHITE);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JButton geriBtn = new JButton("← Geri");
        geriBtn.setFocusPainted(false);
        geriBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        geriBtn.setForeground(Color.WHITE);
        geriBtn.setBackground(new Color(50, 97, 100));
        geriBtn.setBorder(BorderFactory.createEmptyBorder(8, 14, 8, 14));
        geriBtn.addActionListener(e -> frame.dispose());

        JButton refreshBtn = new JButton("Yenile");
        refreshBtn.setFocusPainted(false);
        refreshBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        refreshBtn.setBorder(new EmptyBorder(6, 14, 6, 14));

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 12));
        right.setOpaque(false);
        right.add(refreshBtn);
        right.add(geriBtn);

        top.add(title, BorderLayout.WEST);
        top.add(right, BorderLayout.EAST);

        // ================= TABLE =================
        model = new DefaultTableModel(
                new Object[]{"ID", "Firma", "Nereden", "Nereye", "Tarih", "Saat", "Fiyat"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));

        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(new EmptyBorder(12, 12, 12, 12));

        frame.add(top, BorderLayout.NORTH);
        frame.add(sp, BorderLayout.CENTER);

        loadFlights();

        refreshBtn.addActionListener(e -> loadFlights());

        frame.setVisible(true);
    }

    private void geriDonParent(JFrame current) {
        if (parent != null && parent.isDisplayable()) {
            parent.setVisible(true);
        }
    }

    private void loadFlights() {
        model.setRowCount(0);

        FlightDAO dao = new FlightDAO();
        List<FlightDAO.FlightRow> flights = dao.getAllFlights();

        for (FlightDAO.FlightRow f : flights) {
            model.addRow(new Object[]{
                    f.id, f.firm, f.fromCity, f.toCity, f.date, f.time, f.price
            });
        }
    }
}
