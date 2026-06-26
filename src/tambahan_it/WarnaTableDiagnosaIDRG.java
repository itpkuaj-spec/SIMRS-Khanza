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
public class WarnaTableDiagnosaIDRG extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         String status = table.getValueAt(row, 3).toString();

        if (status.equals("0")) {
            component.setBackground(new Color(255,0,0));
            
        } else {
                if (row % 2 == 1) {
                    component.setBackground(new Color(247, 255, 243));
                } else {
                    component.setBackground(new Color(255, 255, 255));
                }
            
        }
        if (isSelected) {
            if (status.equals("0")) {
            component.setForeground(Color.black);
        } else {
            component.setForeground(Color.red);
            }
        }else{
            if (status.equals("0")) {
            component.setForeground(Color.white);
        } else {
                if (row % 2 == 1) {
                    component.setForeground(Color.black);
                } else {
                     component.setForeground(Color.black);
                }
            
        }  
        }
        return component;
    }

}
