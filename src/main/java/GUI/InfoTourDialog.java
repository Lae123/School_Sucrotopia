package main.java.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;

import java.awt.*;


// Affiche une boîte de dialogue modale contenant des informations sur les différents types de tours

public class InfoTourDialog extends JDialog {

    private static final Color PANEL_COLOR = new Color(241, 193, 255);
    private static final Dimension DIALOG_SIZE = new Dimension(300, 150);

    public InfoTourDialog(JFrame parent) {
        super(parent, "Informations sur les Tours", true);

        Object[][] tourData = {
                {"Nounours Gummy", 20, 6},
                {"Filamment Barbapapa", 60, 10},
                {"Lance topping", 20, 6},
                {"Marshmallow Enflamme", 80, 12}
        };

       
        String[] columnNames = {"Tour", "Coût", "Degats Initial"};

       
        DefaultTableModel model = new DefaultTableModel(tourData, columnNames);

      
        JTable infoTable = new JTable(model);
        infoTable.setEnabled(false);
        infoTable.getColumnModel().getColumn(1).setCellRenderer(new DefaultTableCellRenderer() {
            {
                setHorizontalAlignment(JLabel.CENTER);
            }
        });


        JTableHeader header = infoTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 12));


        TableCellRenderer headerRenderer = header.getDefaultRenderer();
        if (headerRenderer instanceof DefaultTableCellRenderer) {
            ((DefaultTableCellRenderer) headerRenderer).setHorizontalAlignment(JLabel.CENTER);
            ((DefaultTableCellRenderer) headerRenderer).setFont(new Font("Arial", Font.BOLD, 12));
        }

        JScrollPane scrollPane = new JScrollPane(infoTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setPreferredSize(DIALOG_SIZE);

        JPanel panel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                GradientPaint gradient = new GradientPaint(0, 0, Color.WHITE, getWidth(), getHeight(), PANEL_COLOR);
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        JLabel titleLabel = new JLabel("Informations sur les Tours", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        getContentPane().add(panel);
        pack();
        setLocationRelativeTo(parent);
        setResizable(false);
    }
}
