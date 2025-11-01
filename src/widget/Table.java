package widget;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JTable;
import javax.swing.table.JTableHeader;

/**
 *
 * @author usu
 */
public class Table extends JTable {

    /*
     * Serial version UID
     */
    private static final long serialVersionUID = 1L;

    public Table() {
        super();
        //setBackground(new Color(255,235,255));
        //setGridColor(new Color(245,170,245));
        //setForeground(new Color(90,90,90));
//        setBackground(new Color(255,255,255));
//        setGridColor(new Color(226,231,221));
//        setForeground(new Color(50,50,50));
//        setFont(new java.awt.Font("Tahoma", 0, 11));
//        setRowHeight(22);
//        setSelectionBackground(new Color(255,255,255));
//        setSelectionForeground(new Color(255,0,0));
//        getTableHeader().setForeground(new Color(50,50,50));
//        getTableHeader().setBackground(new Color(255,250,250));
//        getTableHeader().setBorder(javax.swing.BorderFactory.createLineBorder(new Color(255,250,250)));
//        getTableHeader().setFont(new java.awt.Font("Tahoma", 0, 11));

            // Baris Ganjil Genap
        setBackground(new Color(255, 255, 255));
        setGridColor(new Color(180, 180, 180));

        // Tampilkan garis horizontal dan vertikal
        setShowHorizontalLines(true);
        setShowVerticalLines(true);

        // Warna teks isi tabel
        setForeground(new Color(0, 0, 0)); //Abu2

        // Font isi tabel
        setFont(new java.awt.Font("Tahoma", 0, 11)); //11 ukuran font

        // Tinggi baris selain judul kolom
        setRowHeight(28);

// Warna saat baris dipilih
        setSelectionBackground(new Color(220, 240, 220)); // tetap hijau muda
        setSelectionForeground(new Color(200, 0, 0));     // teks merah

        // Judul kolom
        JTableHeader header = getTableHeader();
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(25,25,112)); // hijau tua
        header.setFont(new java.awt.Font("Tahoma", Font.BOLD, 11));
        header.setBorder(BorderFactory.createLineBorder(new Color(0, 80, 40)));
        header.setPreferredSize(new Dimension(header.getPreferredSize().width, 26));//bawaan 35
        setIntercellSpacing(new Dimension(1, 1));
        header.setDefaultRenderer(new MultiLineHeader());
    }
}
