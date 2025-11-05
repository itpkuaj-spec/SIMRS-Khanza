/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package integration_idrg;

import fungsi.*;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Owner
 */
public class WarnaTableDiagnosaINACBG extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        String status = table.getValueAt(row, 2).toString();
        String statusDiagnosa = table.getValueAt(row, 3).toString();

        if (row % 2 == 1) {
            component.setBackground(new Color(247, 255, 243));
        } else {
            component.setBackground(new Color(255, 255, 255));
        }
        if (statusDiagnosa.equals("0")) {
            component.setBackground(new Color(255, 0, 0));
        } else {
            if (status.equals("Primary")) {
                component.setBackground(new Color(0, 51, 102));
                component.setForeground(Color.white);
            } else if (status.equals("Secondary")) {
                component.setBackground(new Color(0, 102, 102));
                component.setForeground(Color.white);
            }
        }
        if (isSelected) {
            if (statusDiagnosa.equals("0")) {
                component.setForeground(Color.black);
            } else {
                component.setForeground(Color.red);
            }
        } else {
            if (status.equals("Primary")) {
                component.setForeground(Color.white);
            } else if (status.equals("Secondary")) {
                component.setForeground(Color.white);
            }
        }
        return component;
    }

}
