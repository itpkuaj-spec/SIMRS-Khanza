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
public class WarnaTableIDRGClaim extends DefaultTableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column){
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
         String status = table.getValueAt(row, 1).toString();
        String StatusVerif = table.getValueAt(row, 9).toString();
        String StatusKoding = table.getValueAt(row, 11).toString();
        String StatusKirim = table.getValueAt(row, 13).toString();
        String StatusDownload = table.getValueAt(row, 15).toString();

        if (StatusDownload.equals("Sudah Download")) {
            component.setBackground(new Color(255,153,255));
            component.setForeground(Color.white);
        } else {
            if (StatusVerif.equals("Sudah Verif")) {
                component.setBackground(new Color(255, 153, 51));
                component.setForeground(Color.black);
                    if (StatusKoding.equals("Sudah Koding")) {
                        component.setBackground(new Color(0,102,0));
                        component.setForeground(Color.white);
                            if (StatusKirim.equals("Sudah Kirim")) {
                                component.setBackground(new Color(153,255,51));
                                component.setForeground(Color.black);
                            }
                    }
            } else if (StatusVerif.equals("Siap Verif")) {
                component.setBackground(new Color(51, 51, 255));
                component.setForeground(Color.white);
            } else if (StatusVerif.equals("Perlu Perbaikan")) {
                component.setBackground(new Color(255, 255, 0));
                component.setForeground(Color.black);
            } else {
                if (row % 2 == 1) {
                    component.setBackground(new Color(247, 255, 243));
                } else {
                    component.setBackground(new Color(255, 255, 255));
                }
            }
        }
        if (isSelected) {
            component.setForeground(Color.red);
        }else{
            if (StatusDownload.equals("Sudah Download")) {
            component.setForeground(Color.black);
        } else {
            if (StatusVerif.equals("Sudah Verif")) {
                component.setForeground(Color.black);
                    if (StatusKoding.equals("Sudah Koding")) {
                        component.setForeground(Color.white);
                            if (StatusKirim.equals("Sudah Kirim")) {
                                component.setForeground(Color.black);
                            }
                    }
            }else if (StatusVerif.equals("Siap Verif")) {
                    component.setForeground(Color.white);
                } else if (StatusVerif.equals("Perlu Perbaikan")) {
                    component.setForeground(Color.black);
                } else {
                if (row % 2 == 1) {
                    component.setForeground(Color.black);
                } else {
                     component.setForeground(Color.black);
                }
            }
        }  
        }
        return component;
    }

}
