package com.leonardovechieti.dev.project.views;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class ButtonRenderer extends DefaultTableCellRenderer {
    private JButton button;

    public ButtonRenderer() {
        super();
        button = new JButton();
        //Seta um icone para o botão
        button.setIcon(new ImageIcon(getClass().getResource("/com/leonardovechieti/dev/project/icon/deleteLine1.png")));

        button.setBackground(new Color(0, 0, 0, 0));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        if (isSelected) {
            setForeground(table.getSelectionForeground());
            setBackground(table.getSelectionBackground());
        } else {
            setForeground(table.getForeground());
            setBackground(UIManager.getColor("Button.background"));
        }

        button.setText((value == null) ? "" : value.toString());

        return button;
    }
}