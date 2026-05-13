/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * DlgRujuk.java
 *
 * Created on 31 Mei 10, 20:19:56
 */

package tambahan_it;

import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import kepegawaian.DlgCariDokter;
import laporan.DlgBerkasRawat;
import tambahan_it.RMCariSBAR;
//import digitalsignature.DlgViewPdf;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import javax.swing.Timer;
import kepegawaian.DlgCariPegawai;


/**
 *
 * @author perpustakaan
 */
public final class SBAR extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private validasi Valid=new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    private String FileName;
    private int i=0;    
    private DlgCariDokter dokter=new DlgCariDokter(null,false);
    public  DlgCariPegawai pegawai=new DlgCariPegawai(null,false);
    public  RMCariSBAR carisbar=new RMCariSBAR(null,false);

    SimpleDateFormat tanggalNow = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat jamNow = new SimpleDateFormat("HH:mm:ss");
    
    /** Creates new form DlgRujuk
     * @param parent
     * @param modal */
    public SBAR(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        
        tabMode=new DefaultTableModel(null,new Object[]{
            "No SBAR","Tgl.Rawat","Status","No.Rawat","No.RM","Nama Pasien","NIP","Nama Pemeriksa","Tanggal SBAR","Jam SBAR","S (SITUATION)","B (BACKGROUND)",
            "A (ASSESSMENT)","R (RECOMENDATION)","ADV (ADVICE)", "kd Dokter","Dokter","Baca","Konfirmasi","Tgl Validasi","Jam Validasi","Status Validasi"
        }){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbObat.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbObat.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbObat.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 22; i++) {
            TableColumn column = tbObat.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(105);
            }else if(i==1){
                column.setPreferredWidth(65);
            }else if(i==2){
                column.setPreferredWidth(40);
            }else if(i==3){
                column.setPreferredWidth(105);
            }else if(i==4){
                column.setPreferredWidth(70);
            }else if(i==5){
                column.setPreferredWidth(100);
            }else if(i==6){
                column.setPreferredWidth(60);
            }else if(i==7){
                column.setPreferredWidth(150);
            }else if(i==8){
                column.setPreferredWidth(80);
            }else if(i==9){
                column.setPreferredWidth(80);
            }else if(i==10){
                column.setPreferredWidth(170);
            }else if(i==11){
                column.setPreferredWidth(170);
            }else if(i==12){
                column.setPreferredWidth(170);
            }else if(i==13){
                column.setPreferredWidth(170);
            }else if(i==14){
                column.setPreferredWidth(180);
            }else if(i==15){
                column.setPreferredWidth(90);
            }else if(i==16){
                column.setPreferredWidth(150);
            }else if(i==17){
                column.setPreferredWidth(60);
            }else if(i==18){
                column.setPreferredWidth(60);
            }else if(i==19){
                column.setPreferredWidth(65);
            }else if(i==20){
                column.setPreferredWidth(60);
            }else if(i==21){
                column.setPreferredWidth(55);
            }
        }
        tbObat.setDefaultRenderer(Object.class, new WarnaTable());

        TNoRw.setDocument(new batasInput((byte)17).getKata(TNoRw));
        TCari.setDocument(new batasInput((int)100).getKata(TCari));
        
        if(koneksiDB.CARICEPAT().equals("aktif")){
            TCari.getDocument().addDocumentListener(new javax.swing.event.DocumentListener(){
                @Override
                public void insertUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void removeUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
                @Override
                public void changedUpdate(DocumentEvent e) {
                    if(TCari.getText().length()>2){
                        tampil();
                    }
                }
            });
        }
        
        dokter.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(dokter.getTable().getSelectedRow()!= -1){
                    KodeDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),0).toString());
                    NamaDokter.setText(dokter.getTable().getValueAt(dokter.getTable().getSelectedRow(),1).toString());
                    KodeDokter.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        pegawai.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(pegawai.getTable().getSelectedRow()!= -1){
                    KdPeg.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(),0).toString());
                    TPegawai.setText(pegawai.getTable().getValueAt(pegawai.getTable().getSelectedRow(),1).toString());
                    KdPeg.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        carisbar.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(carisbar.getTable().getSelectedRow()!= -1){
                    TSituation.setText(carisbar.getTable().getValueAt(carisbar.getTable().getSelectedRow(),2).toString());
                    TBackground.setText(carisbar.getTable().getValueAt(carisbar.getTable().getSelectedRow(),3).toString());
                    TAssesment.setText(carisbar.getTable().getValueAt(carisbar.getTable().getSelectedRow(),4).toString());
                    TRecomendation.setText(carisbar.getTable().getValueAt(carisbar.getTable().getSelectedRow(),5).toString());
                    TAdvice.setText(carisbar.getTable().getValueAt(carisbar.getTable().getSelectedRow(),6).toString());
                    TAdvice.requestFocus();
                }
            }
            @Override
            public void windowIconified(WindowEvent e) {}
            @Override
            public void windowDeiconified(WindowEvent e) {}
            @Override
            public void windowActivated(WindowEvent e) {}
            @Override
            public void windowDeactivated(WindowEvent e) {}
        });
        
        ChkInput.setSelected(false);
        isForm();
        ChkJln.setSelected(true);
        jam();  
    }


    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnDigitalTTE = new javax.swing.JMenuItem();
        MnLaporanResume = new javax.swing.JMenuItem();
        MnInputDiagnosa = new javax.swing.JMenuItem();
        ppBerkasDigital = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbObat = new widget.Table();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        BtnSimpan = new widget.Button();
        BtnGanti = new widget.Button();
        BtnHapus = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        PanelInput = new javax.swing.JPanel();
        ChkInput = new widget.CekBox();
        scrollInput = new widget.ScrollPane();
        FormInput = new widget.PanelBiasa();
        TNoRw = new widget.TextBox();
        TPasien = new widget.TextBox();
        TNoRM = new widget.TextBox();
        jLabel5 = new widget.Label();
        label14 = new widget.Label();
        KodeDokter = new widget.TextBox();
        NamaDokter = new widget.TextBox();
        BtnDokter = new widget.Button();
        scrollPane1 = new widget.ScrollPane();
        TSituation = new widget.TextArea();
        scrollPane2 = new widget.ScrollPane();
        TBackground = new widget.TextArea();
        scrollPane3 = new widget.ScrollPane();
        TAssesment = new widget.TextArea();
        scrollPane4 = new widget.ScrollPane();
        TRecomendation = new widget.TextArea();
        jLabel16 = new widget.Label();
        Tanggal = new widget.Tanggal();
        Jam = new widget.ComboBox();
        Menit = new widget.ComboBox();
        Detik = new widget.ComboBox();
        jLabel53 = new widget.Label();
        KdPeg = new widget.TextBox();
        TPegawai = new widget.TextBox();
        BtnSeekPegawai = new widget.Button();
        scrollPane5 = new widget.ScrollPane();
        TAdvice = new widget.TextArea();
        cmbbaca = new widget.ComboBox();
        jLabel58 = new widget.Label();
        cmbkonfirmasi = new widget.ComboBox();
        jLabel59 = new widget.Label();
        ChkJln = new widget.CekBox();
        BtnValidasi = new widget.Button();
        NoSbar = new widget.TextBox();
        jLabel54 = new widget.Label();
        jLabel55 = new widget.Label();
        lblValidasi = new widget.Label();
        lblJamvalidasi = new widget.Label();
        jLabel60 = new widget.Label();
        jLabel61 = new widget.Label();
        lblTglvalidasi = new widget.Label();
        BtncpSBAR = new widget.Button();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnDigitalTTE.setBackground(new java.awt.Color(255, 255, 254));
        MnDigitalTTE.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnDigitalTTE.setForeground(new java.awt.Color(50, 50, 50));
        MnDigitalTTE.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnDigitalTTE.setText("Sign Digital Signature");
        MnDigitalTTE.setToolTipText("");
        MnDigitalTTE.setName("MnDigitalTTE"); // NOI18N
        MnDigitalTTE.setPreferredSize(new java.awt.Dimension(220, 26));
        MnDigitalTTE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnDigitalTTEActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnDigitalTTE);

        MnLaporanResume.setBackground(new java.awt.Color(255, 255, 254));
        MnLaporanResume.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnLaporanResume.setForeground(new java.awt.Color(50, 50, 50));
        MnLaporanResume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnLaporanResume.setText("Laporan Resume Pasien");
        MnLaporanResume.setName("MnLaporanResume"); // NOI18N
        MnLaporanResume.setPreferredSize(new java.awt.Dimension(220, 26));
        MnLaporanResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnLaporanResumeActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnLaporanResume);

        MnInputDiagnosa.setBackground(new java.awt.Color(255, 255, 254));
        MnInputDiagnosa.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnInputDiagnosa.setForeground(new java.awt.Color(50, 50, 50));
        MnInputDiagnosa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnInputDiagnosa.setText("Input Diagnosa Pasien");
        MnInputDiagnosa.setName("MnInputDiagnosa"); // NOI18N
        MnInputDiagnosa.setPreferredSize(new java.awt.Dimension(220, 26));
        MnInputDiagnosa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnInputDiagnosaActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnInputDiagnosa);

        ppBerkasDigital.setBackground(new java.awt.Color(255, 255, 254));
        ppBerkasDigital.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBerkasDigital.setForeground(new java.awt.Color(50, 50, 50));
        ppBerkasDigital.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppBerkasDigital.setText("Berkas Digital Perawatan");
        ppBerkasDigital.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBerkasDigital.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBerkasDigital.setName("ppBerkasDigital"); // NOI18N
        ppBerkasDigital.setPreferredSize(new java.awt.Dimension(220, 26));
        ppBerkasDigital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBerkasDigitalBtnPrintActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ppBerkasDigital);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ SBAR & TBK ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setFont(new java.awt.Font("Tahoma", 2, 12)); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);
        Scroll.setPreferredSize(new java.awt.Dimension(452, 200));

        tbObat.setAutoCreateRowSorter(true);
        tbObat.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbObat.setComponentPopupMenu(jPopupMenu1);
        tbObat.setName("tbObat"); // NOI18N
        tbObat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbObatMouseClicked(evt);
            }
        });
        tbObat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbObatKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbObat);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel3.setName("jPanel3"); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        BtnSimpan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/save-16x16.png"))); // NOI18N
        BtnSimpan.setMnemonic('S');
        BtnSimpan.setText("Simpan");
        BtnSimpan.setToolTipText("Alt+S");
        BtnSimpan.setName("BtnSimpan"); // NOI18N
        BtnSimpan.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnSimpan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanActionPerformed(evt);
            }
        });
        BtnSimpan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnSimpan);

        BtnGanti.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/inventaris.png"))); // NOI18N
        BtnGanti.setMnemonic('B');
        BtnGanti.setText("Ganti");
        BtnGanti.setToolTipText("Alt+B");
        BtnGanti.setName("BtnGanti"); // NOI18N
        BtnGanti.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnGanti.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnGantiActionPerformed(evt);
            }
        });
        BtnGanti.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnGantiKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnGanti);

        BtnHapus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/stop_f2.png"))); // NOI18N
        BtnHapus.setMnemonic('H');
        BtnHapus.setText("Hapus");
        BtnHapus.setToolTipText("Alt+H");
        BtnHapus.setName("BtnHapus"); // NOI18N
        BtnHapus.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnHapusActionPerformed(evt);
            }
        });
        BtnHapus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnHapusKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnHapus);

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass8.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        BtnKeluar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluarKeyPressed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tgl.Rawat :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(67, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "16-04-2026" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "16-04-2026" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(95, 23));
        panelGlass9.add(DTPCari2);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(jLabel6);

        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(310, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass9.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('3');
        BtnCari.setToolTipText("Alt+3");
        BtnCari.setName("BtnCari"); // NOI18N
        BtnCari.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariActionPerformed(evt);
            }
        });
        BtnCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnAllActionPerformed(evt);
            }
        });
        BtnAll.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnAllKeyPressed(evt);
            }
        });
        panelGlass9.add(BtnAll);

        jPanel3.add(panelGlass9, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        PanelInput.setName("PanelInput"); // NOI18N
        PanelInput.setOpaque(false);
        PanelInput.setPreferredSize(new java.awt.Dimension(192, 330));
        PanelInput.setLayout(new java.awt.BorderLayout(1, 1));

        ChkInput.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setMnemonic('I');
        ChkInput.setText(".: Input Data");
        ChkInput.setToolTipText("Alt+I");
        ChkInput.setBorderPainted(true);
        ChkInput.setBorderPaintedFlat(true);
        ChkInput.setFocusable(false);
        ChkInput.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ChkInput.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ChkInput.setName("ChkInput"); // NOI18N
        ChkInput.setPreferredSize(new java.awt.Dimension(192, 20));
        ChkInput.setRolloverIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/143.png"))); // NOI18N
        ChkInput.setRolloverSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.setSelectedIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/145.png"))); // NOI18N
        ChkInput.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkInputActionPerformed(evt);
            }
        });
        PanelInput.add(ChkInput, java.awt.BorderLayout.PAGE_END);

        scrollInput.setName("scrollInput"); // NOI18N

        FormInput.setBackground(new java.awt.Color(250, 255, 245));
        FormInput.setBorder(null);
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(100, 310));
        FormInput.setLayout(null);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N
        TNoRw.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRwKeyPressed(evt);
            }
        });
        FormInput.add(TNoRw);
        TNoRw.setBounds(60, 10, 141, 23);

        TPasien.setEditable(false);
        TPasien.setHighlighter(null);
        TPasien.setName("TPasien"); // NOI18N
        TPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TPasienKeyPressed(evt);
            }
        });
        FormInput.add(TPasien);
        TPasien.setBounds(310, 10, 320, 23);

        TNoRM.setEditable(false);
        TNoRM.setHighlighter(null);
        TNoRM.setName("TNoRM"); // NOI18N
        TNoRM.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TNoRMKeyPressed(evt);
            }
        });
        FormInput.add(TNoRM);
        TNoRM.setBounds(200, 10, 112, 23);

        jLabel5.setText("No.Rawat :");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput.add(jLabel5);
        jLabel5.setBounds(0, 10, 60, 23);

        label14.setText("dokter:");
        label14.setName("label14"); // NOI18N
        label14.setPreferredSize(new java.awt.Dimension(70, 23));
        FormInput.add(label14);
        label14.setBounds(0, 40, 50, 23);

        KodeDokter.setEditable(false);
        KodeDokter.setName("KodeDokter"); // NOI18N
        KodeDokter.setPreferredSize(new java.awt.Dimension(80, 23));
        KodeDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KodeDokterKeyPressed(evt);
            }
        });
        FormInput.add(KodeDokter);
        KodeDokter.setBounds(50, 40, 141, 23);

        NamaDokter.setEditable(false);
        NamaDokter.setName("NamaDokter"); // NOI18N
        NamaDokter.setPreferredSize(new java.awt.Dimension(207, 23));
        FormInput.add(NamaDokter);
        NamaDokter.setBounds(190, 40, 270, 23);

        BtnDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnDokter.setMnemonic('2');
        BtnDokter.setToolTipText("Alt+2");
        BtnDokter.setName("BtnDokter"); // NOI18N
        BtnDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDokterActionPerformed(evt);
            }
        });
        BtnDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDokterKeyPressed(evt);
            }
        });
        FormInput.add(BtnDokter);
        BtnDokter.setBounds(463, 40, 28, 23);

        scrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder("S (SITUATION)"));
        scrollPane1.setName("scrollPane1"); // NOI18N

        TSituation.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TSituation.setColumns(20);
        TSituation.setRows(5);
        TSituation.setName("TSituation"); // NOI18N
        TSituation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TSituationKeyPressed(evt);
            }
        });
        scrollPane1.setViewportView(TSituation);

        FormInput.add(scrollPane1);
        scrollPane1.setBounds(0, 70, 360, 80);

        scrollPane2.setBorder(javax.swing.BorderFactory.createTitledBorder("B (BACKGROUND)"));
        scrollPane2.setName("scrollPane2"); // NOI18N

        TBackground.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TBackground.setColumns(20);
        TBackground.setRows(5);
        TBackground.setName("TBackground"); // NOI18N
        TBackground.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TBackgroundKeyPressed(evt);
            }
        });
        scrollPane2.setViewportView(TBackground);

        FormInput.add(scrollPane2);
        scrollPane2.setBounds(0, 160, 360, 80);

        scrollPane3.setBorder(javax.swing.BorderFactory.createTitledBorder("A (ASSESSMENT)"));
        scrollPane3.setName("scrollPane3"); // NOI18N

        TAssesment.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TAssesment.setColumns(20);
        TAssesment.setRows(5);
        TAssesment.setName("TAssesment"); // NOI18N
        TAssesment.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TAssesmentKeyPressed(evt);
            }
        });
        scrollPane3.setViewportView(TAssesment);

        FormInput.add(scrollPane3);
        scrollPane3.setBounds(370, 70, 360, 80);

        scrollPane4.setBorder(javax.swing.BorderFactory.createTitledBorder("R (RECOMMENDATION)"));
        scrollPane4.setName("scrollPane4"); // NOI18N

        TRecomendation.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TRecomendation.setColumns(20);
        TRecomendation.setRows(5);
        TRecomendation.setName("TRecomendation"); // NOI18N
        TRecomendation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TRecomendationKeyPressed(evt);
            }
        });
        scrollPane4.setViewportView(TRecomendation);

        FormInput.add(scrollPane4);
        scrollPane4.setBounds(370, 160, 360, 80);

        jLabel16.setText("Tanggal :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setVerifyInputWhenFocusTarget(false);
        FormInput.add(jLabel16);
        jLabel16.setBounds(610, 10, 75, 23);

        Tanggal.setForeground(new java.awt.Color(50, 70, 50));
        Tanggal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "16-04-2026" }));
        Tanggal.setDisplayFormat("dd-MM-yyyy");
        Tanggal.setName("Tanggal"); // NOI18N
        Tanggal.setOpaque(false);
        Tanggal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TanggalKeyPressed(evt);
            }
        });
        FormInput.add(Tanggal);
        Tanggal.setBounds(690, 10, 90, 23);

        Jam.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        Jam.setName("Jam"); // NOI18N
        Jam.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                JamKeyPressed(evt);
            }
        });
        FormInput.add(Jam);
        Jam.setBounds(780, 10, 62, 23);

        Menit.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Menit.setName("Menit"); // NOI18N
        Menit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                MenitKeyPressed(evt);
            }
        });
        FormInput.add(Menit);
        Menit.setBounds(850, 10, 62, 23);

        Detik.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        Detik.setName("Detik"); // NOI18N
        Detik.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DetikKeyPressed(evt);
            }
        });
        FormInput.add(Detik);
        Detik.setBounds(910, 10, 62, 23);

        jLabel53.setText("Dilakukan :");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput.add(jLabel53);
        jLabel53.setBounds(500, 40, 60, 23);

        KdPeg.setEditable(false);
        KdPeg.setHighlighter(null);
        KdPeg.setName("KdPeg"); // NOI18N
        KdPeg.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                KdPegKeyPressed(evt);
            }
        });
        FormInput.add(KdPeg);
        KdPeg.setBounds(560, 40, 115, 23);

        TPegawai.setEditable(false);
        TPegawai.setHighlighter(null);
        TPegawai.setName("TPegawai"); // NOI18N
        FormInput.add(TPegawai);
        TPegawai.setBounds(675, 40, 212, 23);

        BtnSeekPegawai.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekPegawai.setMnemonic('4');
        BtnSeekPegawai.setToolTipText("ALt+4");
        BtnSeekPegawai.setName("BtnSeekPegawai"); // NOI18N
        BtnSeekPegawai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekPegawaiActionPerformed(evt);
            }
        });
        FormInput.add(BtnSeekPegawai);
        BtnSeekPegawai.setBounds(888, 40, 32, 22);

        scrollPane5.setBorder(javax.swing.BorderFactory.createTitledBorder("Tulis (ADVICE DOKTER)"));
        scrollPane5.setName("scrollPane5"); // NOI18N

        TAdvice.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        TAdvice.setColumns(20);
        TAdvice.setRows(5);
        TAdvice.setName("TAdvice"); // NOI18N
        TAdvice.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TAdviceKeyPressed(evt);
            }
        });
        scrollPane5.setViewportView(TAdvice);

        FormInput.add(scrollPane5);
        scrollPane5.setBounds(740, 70, 360, 130);

        cmbbaca.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sudah", "Belum" }));
        cmbbaca.setName("cmbbaca"); // NOI18N
        cmbbaca.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbbaca.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbbacaKeyPressed(evt);
            }
        });
        FormInput.add(cmbbaca);
        cmbbaca.setBounds(782, 215, 90, 23);

        jLabel58.setText("Baca :");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput.add(jLabel58);
        jLabel58.setBounds(740, 220, 40, 14);

        cmbkonfirmasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Sudah", "Belum" }));
        cmbkonfirmasi.setName("cmbkonfirmasi"); // NOI18N
        cmbkonfirmasi.setPreferredSize(new java.awt.Dimension(62, 28));
        cmbkonfirmasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbkonfirmasiKeyPressed(evt);
            }
        });
        FormInput.add(cmbkonfirmasi);
        cmbkonfirmasi.setBounds(950, 215, 90, 23);

        jLabel59.setText("Konfirmasi :");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput.add(jLabel59);
        jLabel59.setBounds(880, 220, 70, 14);

        ChkJln.setBorder(null);
        ChkJln.setSelected(true);
        ChkJln.setBorderPainted(true);
        ChkJln.setBorderPaintedFlat(true);
        ChkJln.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkJln.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkJln.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkJln.setName("ChkJln"); // NOI18N
        ChkJln.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ChkJlnActionPerformed(evt);
            }
        });
        FormInput.add(ChkJln);
        ChkJln.setBounds(980, 10, 17, 17);

        BtnValidasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/checked.png"))); // NOI18N
        BtnValidasi.setMnemonic('B');
        BtnValidasi.setText("VALIDASI");
        BtnValidasi.setToolTipText("Alt+B");
        BtnValidasi.setGlassColor(new java.awt.Color(102, 204, 255));
        BtnValidasi.setName("BtnValidasi"); // NOI18N
        BtnValidasi.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnValidasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnValidasiActionPerformed(evt);
            }
        });
        BtnValidasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnValidasiKeyPressed(evt);
            }
        });
        FormInput.add(BtnValidasi);
        BtnValidasi.setBounds(750, 250, 350, 30);

        NoSbar.setHighlighter(null);
        NoSbar.setName("NoSbar"); // NOI18N
        NoSbar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                NoSbarKeyPressed(evt);
            }
        });
        FormInput.add(NoSbar);
        NoSbar.setBounds(980, 40, 180, 24);

        jLabel54.setText("Jam Validasi :");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput.add(jLabel54);
        jLabel54.setBounds(20, 280, 70, 14);

        jLabel55.setText("No SBAR :");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput.add(jLabel55);
        jLabel55.setBounds(910, 45, 60, 14);

        lblValidasi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblValidasi.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblValidasi.setName("lblValidasi"); // NOI18N
        FormInput.add(lblValidasi);
        lblValidasi.setBounds(490, 250, 230, 30);

        lblJamvalidasi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblJamvalidasi.setText(".");
        lblJamvalidasi.setName("lblJamvalidasi"); // NOI18N
        FormInput.add(lblJamvalidasi);
        lblJamvalidasi.setBounds(100, 280, 100, 14);

        jLabel60.setText("Status Validasi :");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput.add(jLabel60);
        jLabel60.setBounds(410, 260, 76, 14);

        jLabel61.setText("Tgl Validasi :");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput.add(jLabel61);
        jLabel61.setBounds(30, 250, 60, 14);

        lblTglvalidasi.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTglvalidasi.setText(".");
        lblTglvalidasi.setName("lblTglvalidasi"); // NOI18N
        FormInput.add(lblTglvalidasi);
        lblTglvalidasi.setBounds(100, 250, 100, 14);

        BtncpSBAR.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtncpSBAR.setMnemonic('4');
        BtncpSBAR.setText("Copy SBAR");
        BtncpSBAR.setToolTipText("ALt+4");
        BtncpSBAR.setName("BtncpSBAR"); // NOI18N
        BtncpSBAR.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtncpSBARActionPerformed(evt);
            }
        });
        FormInput.add(BtncpSBAR);
        BtncpSBAR.setBounds(220, 250, 150, 22);

        scrollInput.setViewportView(FormInput);

        PanelInput.add(scrollInput, java.awt.BorderLayout.CENTER);

        internalFrame1.add(PanelInput, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void TNoRwKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRwKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            isRawat();
            isPsien();
        }else{            
            Valid.pindah(evt,TCari,BtnDokter);
        }
}//GEN-LAST:event_TNoRwKeyPressed

    private void TPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TPasienKeyPressed
        Valid.pindah(evt,TCari,BtnSimpan);
}//GEN-LAST:event_TPasienKeyPressed

    private void BtnSimpanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSimpanActionPerformed
        if(TNoRw.getText().equals("")||TNoRM.getText().equals("")||TPasien.getText().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KodeDokter.getText().equals("")||NamaDokter.getText().equals("")){
            Valid.textKosong(BtnDokter,"dokter");
        }else if(KdPeg.getText().equals("")||TPegawai.getText().equals("")){
            Valid.textKosong(BtnSeekPegawai,"Nama Pemeriksa");
        }else if(TSituation.getText().equals("")){
            Valid.textKosong(TSituation,"S (SITUATION");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TBackground,"B (BACKGROUND)");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TAssesment,"A (Assesmen)");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TRecomendation,"R (Recomendation)");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TAdvice,"ADV (Advice)");
        }else{
                            if(akses.getkode().equals("Admin Utama")){
                                Sequel.menyimpan("pemeriksaan_sbar","?,?,?,?,?,?,?,?,?,?,?,?,?","Data",13,new String[]{
                                    NoSbar.getText(),TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),                      
                                    TSituation.getText(),TBackground.getText(),TAssesment.getText(),TRecomendation.getText(),TAdvice.getText(),KdPeg.getText(),KodeDokter.getText(),cmbbaca.getSelectedItem().toString(),cmbkonfirmasi.getSelectedItem().toString()
                                });

                                tampil();
                                JOptionPane.showMessageDialog(null, "Data berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                                emptTeks();
                                LCount.setText(""+tabMode.getRowCount());
                            }else{
                                if(akses.getkode().equals(KdPeg.getText())){
                                    Sequel.menyimpan("pemeriksaan_sbar","?,?,?,?,?,?,?,?,?,?,?,?,?","Data",13,new String[]{
                                    NoSbar.getText(),TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),                      
                                    TSituation.getText(),TBackground.getText(),TAssesment.getText(),TRecomendation.getText(),TAdvice.getText(),KdPeg.getText(),KodeDokter.getText(),cmbbaca.getSelectedItem().toString(),cmbkonfirmasi.getSelectedItem().toString()
                                    });
                                    tampil();
                                    JOptionPane.showMessageDialog(null, "Data berhasil disimpan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                                    emptTeks();
                                    LCount.setText(""+tabMode.getRowCount());
                                }else{
                                    JOptionPane.showMessageDialog(null,"Hanya bisa diubah oleh petugas yang bersangkutan..!!");
                                }
                            }
        }
}//GEN-LAST:event_BtnSimpanActionPerformed

    private void BtnSimpanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnSimpanKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnSimpanActionPerformed(null);
        }else{
            Valid.pindah(evt,TSituation,BtnGanti);
        }
}//GEN-LAST:event_BtnSimpanKeyPressed

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dokter.dispose();
        pegawai.dispose();
        dispose();
}//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnKeluarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluarKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnKeluarActionPerformed(null);
        }else{Valid.pindah(evt,BtnGanti,TCari);}
}//GEN-LAST:event_BtnKeluarKeyPressed

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            BtnCariActionPerformed(null);
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            BtnCari.requestFocus();
        }else if(evt.getKeyCode()==KeyEvent.VK_PAGE_UP){
            BtnKeluar.requestFocus();
        }
}//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        tampil();
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }else{
            Valid.pindah(evt, TCari, BtnAll);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        tampil();
}//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
            TCari.setText("");
        }else{
            Valid.pindah(evt, BtnCari, TPasien);
        }
}//GEN-LAST:event_BtnAllKeyPressed

    private void TNoRMKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TNoRMKeyPressed
        // Valid.pindah(evt, TNm, BtnSimpan);
}//GEN-LAST:event_TNoRMKeyPressed

    private void tbObatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbObatMouseClicked
        if(tabMode.getRowCount()!=0){
            try {
                getData();
            } catch (java.lang.NullPointerException e) {
            }
        }
}//GEN-LAST:event_tbObatMouseClicked

    private void tbObatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbObatKeyPressed
        if(tabMode.getRowCount()!=0){
            if((evt.getKeyCode()==KeyEvent.VK_ENTER)||(evt.getKeyCode()==KeyEvent.VK_UP)||(evt.getKeyCode()==KeyEvent.VK_DOWN)){
                try {
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }else if(evt.getKeyCode()==KeyEvent.VK_SPACE){
                try {
                    ChkInput.setSelected(true);
                    isForm(); 
                    getData();
                } catch (java.lang.NullPointerException e) {
                }
            }
        }
}//GEN-LAST:event_tbObatKeyPressed

    private void KodeDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KodeDokterKeyPressed
        Valid.pindah(evt,TCari,TSituation);
    }//GEN-LAST:event_KodeDokterKeyPressed

    private void BtnDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDokterActionPerformed
        dokter.emptTeks();
        dokter.isCek();
        dokter.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        dokter.setLocationRelativeTo(null);
        dokter.setVisible(true);
        dokter.toFront(); // Tambahkan baris ini
        dokter.requestFocus();
    }//GEN-LAST:event_BtnDokterActionPerformed

    private void BtnDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDokterKeyPressed
       Valid.pindah(evt,TCari,TSituation);
    }//GEN-LAST:event_BtnDokterKeyPressed

    private void MnLaporanResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnLaporanResumeActionPerformed
        if(tbObat.getSelectedRow()>-1){
            Map<String, Object> param = new HashMap<>();    
            param.put("namars",akses.getnamars());
            param.put("alamatrs",akses.getalamatrs());
            param.put("kotars",akses.getkabupatenrs());
            param.put("propinsirs",akses.getpropinsirs());
            param.put("kontakrs",akses.getkontakrs());
            param.put("emailrs",akses.getemailrs());   
            param.put("logo",Sequel.cariGambar("select logo from setting")); 
            param.put("norawat",tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
            param.put("finger",Sequel.cariIsi("select sha1(sidikjari) from sidikjari inner join pegawai on pegawai.id=sidikjari.id where pegawai.nik=?",tbObat.getValueAt(tbObat.getSelectedRow(),5).toString())); 
            if(tbObat.getValueAt(tbObat.getSelectedRow(),1).toString().equals("Ralan")){
                param.put("ruang",Sequel.cariIsi("select poliklinik.nm_poli from poliklinik inner join reg_periksa on reg_periksa.kd_poli=poliklinik.kd_poli where reg_periksa.no_rawat=?",tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()));
                param.put("tanggalkeluar",Sequel.cariIsi("select DATE_FORMAT(tgl_registrasi, '%d-%m-%Y') from reg_periksa where no_rawat=?",tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()));
            }else{
                param.put("ruang",Sequel.cariIsi("select nm_bangsal from bangsal inner join kamar inner join kamar_inap on bangsal.kd_bangsal=kamar.kd_bangsal and kamar_inap.kd_kamar=kamar.kd_kamar where no_rawat=? order by tgl_masuk desc limit 1 ",tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()));
                param.put("tanggalkeluar",Sequel.cariIsi("select DATE_FORMAT(tgl_keluar, '%d-%m-%Y') from kamar_inap where no_rawat=? order by tgl_keluar desc limit 1 ",tbObat.getValueAt(tbObat.getSelectedRow(),2).toString()));
            }
            Valid.MyReport("rptLaporanResume.jasper","report","::[ Laporan Resume Pasien ]::",param);
        }
    }//GEN-LAST:event_MnLaporanResumeActionPerformed

    private void MnInputDiagnosaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnInputDiagnosaActionPerformed
//        if(TNoRw.getText().trim().equals("")){
//            JOptionPane.showMessageDialog(null,"Maaf, Silahkan anda pilih dulu pasien...!!!");
//            TCari.requestFocus();
//        }else{
//            penyakit.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
//            penyakit.setLocationRelativeTo(internalFrame1);
//            penyakit.isCek();
//            penyakit.setNoRm(TNoRw.getText(),DTPCari1.getDate(),DTPCari2.getDate(),Sequel.cariIsi("select status_lanjut from reg_periksa where no_rawat=?",TNoRw.getText()));
//            penyakit.panelDiagnosa1.tampil();
//            penyakit.setVisible(true);
//        }
    }//GEN-LAST:event_MnInputDiagnosaActionPerformed

    private void ChkInputActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkInputActionPerformed
        isForm();
    }//GEN-LAST:event_ChkInputActionPerformed

    private void ppBerkasDigitalBtnPrintActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBerkasDigitalBtnPrintActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, data sudah habis...!!!!");
            TCari.requestFocus();
        }else{
            if(tbObat.getSelectedRow()>-1){
                if(!tbObat.getValueAt(tbObat.getSelectedRow(),1).toString().equals("")){
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    DlgBerkasRawat berkas=new DlgBerkasRawat(null,true);
                    berkas.setJudul("::[ Berkas Digital Perawatan ]::","berkasrawat/pages");
                    try {
                        berkas.loadURL("http://"+koneksiDB.HOSTHYBRIDWEB()+":"+koneksiDB.PORTWEB()+"/"+koneksiDB.HYBRIDWEB()+"/"+"berkasrawat/login2.php?act=login&usere=admin&passwordte=akusayangsamakamu&no_rawat="+tbObat.getValueAt(tbObat.getSelectedRow(),2).toString());
                    } catch (Exception ex) {
                        System.out.println("Notifikasi : "+ex);
                    }

                    berkas.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
                    berkas.setLocationRelativeTo(internalFrame1);
                    berkas.setVisible(true);
                    this.setCursor(Cursor.getDefaultCursor());
                }
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_ppBerkasDigitalBtnPrintActionPerformed

    private void MnDigitalTTEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnDigitalTTEActionPerformed

    }//GEN-LAST:event_MnDigitalTTEActionPerformed

    private void TSituationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TSituationKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TSituationKeyPressed

    private void TBackgroundKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TBackgroundKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TBackgroundKeyPressed

    private void TAssesmentKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TAssesmentKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TAssesmentKeyPressed

    private void TRecomendationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TRecomendationKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TRecomendationKeyPressed

    private void TanggalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TanggalKeyPressed
        Valid.pindah2(evt,TCari,Jam);
    }//GEN-LAST:event_TanggalKeyPressed

    private void JamKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_JamKeyPressed
        Valid.pindah(evt,Tanggal,Menit);
    }//GEN-LAST:event_JamKeyPressed

    private void MenitKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_MenitKeyPressed
        Valid.pindah(evt,Jam,Detik);
    }//GEN-LAST:event_MenitKeyPressed

    private void DetikKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DetikKeyPressed
        Valid.pindah(evt,Menit,BtnSeekPegawai);
    }//GEN-LAST:event_DetikKeyPressed

    private void KdPegKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_KdPegKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_PAGE_DOWN){
            TPegawai.setText(pegawai.tampil3(KdPeg.getText()));
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            BtnSeekPegawaiActionPerformed(null);
        }else{
            Valid.pindah(evt,TNoRw,TSituation);
        }
    }//GEN-LAST:event_KdPegKeyPressed

    private void BtnSeekPegawaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeekPegawaiActionPerformed
//        akses.setform("SBAR");
//        pegawai.emptTeks();
//        pegawai.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
//        pegawai.setLocationRelativeTo(internalFrame1);
//        pegawai.setVisible(true);
        pegawai.setSize(internalFrame1.getWidth()-30,internalFrame1.getHeight()-30);
        pegawai.setLocationRelativeTo(internalFrame1);
        pegawai.setVisible(true);
    }//GEN-LAST:event_BtnSeekPegawaiActionPerformed

    private void TAdviceKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TAdviceKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_TAdviceKeyPressed

    private void cmbbacaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbbacaKeyPressed
        Valid.pindah(evt,TRecomendation,TAdvice);
    }//GEN-LAST:event_cmbbacaKeyPressed

    private void cmbkonfirmasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbkonfirmasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbkonfirmasiKeyPressed

    private void ChkJlnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ChkJlnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ChkJlnActionPerformed

    private void BtnValidasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnValidasiActionPerformed
        // TODO add your handling code here:
        if(TNoRw.getText().equals("")||TNoRM.getText().equals("")||TPasien.getText().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KodeDokter.getText().equals("")||NamaDokter.getText().equals("")){
            Valid.textKosong(BtnDokter,"dokter");
        }else if(KdPeg.getText().equals("")||TPegawai.getText().equals("")){
            Valid.textKosong(BtnSeekPegawai,"Nama Pemeriksa");
        }else if(TSituation.getText().equals("")){
            Valid.textKosong(TSituation,"S (SITUATION");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TBackground,"B (BACKGROUND)");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TAssesment,"A (Assesmen)");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TRecomendation,"R (Recomendation)");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TAdvice,"ADV (Advice)");
        }else{
            if(tbObat.getSelectedRow()>-1){                
                if(akses.getkode().equals("Admin Utama")){
                    validasi_dokter();
                }else{
                    if(akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString())){
                    validasi_dokter();
                    }else{
                        JOptionPane.showMessageDialog(null,"Maaf Hanya bisa di VALIDASI oleh dokter pemberi Advice, yaitu "+NamaDokter.getText());
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
    }//GEN-LAST:event_BtnValidasiActionPerformed

    private void BtnValidasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnValidasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnValidasiKeyPressed

    private void NoSbarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_NoSbarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_NoSbarKeyPressed

    private void BtnGantiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnGantiActionPerformed
        // TODO add your handling code here:
        if(TNoRw.getText().equals("")||TNoRM.getText().equals("")||TPasien.getText().equals("")){
            Valid.textKosong(TNoRw,"Pasien");
        }else if(KodeDokter.getText().equals("")||NamaDokter.getText().equals("")){
            Valid.textKosong(BtnDokter,"dokter");
        }else if(KdPeg.getText().equals("")||TPegawai.getText().equals("")){
            Valid.textKosong(BtnSeekPegawai,"Nama Pemeriksa");
        }else if(TSituation.getText().equals("")){
            Valid.textKosong(TSituation,"S (SITUATION");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TBackground,"B (BACKGROUND)");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TAssesment,"A (Assesmen)");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TRecomendation,"R (Recomendation)");
        }else if(TBackground.getText().equals("")){
            Valid.textKosong(TAdvice,"ADV (Advice)");
        }else{
            if(tbObat.getSelectedRow()>-1){                
                if(akses.getkode().equals("Admin Utama")){
                    ganti();
                }else{
                    if(akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString())){
                    ganti();
                    }else{
                        JOptionPane.showMessageDialog(null,"Maaf Hanya bisa diganti oleh petugas yang menginput SBAR & TBAK..!!");
                    }
                }
            }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
            }
        }
    }//GEN-LAST:event_BtnGantiActionPerformed

    private void BtnGantiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnGantiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnGantiKeyPressed

    private void BtnHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnHapusActionPerformed
//        if(Valid.hapusTabletf(tabMode,NoSbar,"pemeriksaan_sbar","no_sbar")==true){
//            if(tbObat.getSelectedRow()!= -1){
//                tabMode.removeRow(tbObat.getSelectedRow());
//                emptTeks();
//                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
//                LCount.setText(""+tabMode.getRowCount());
//            }
//        }
        
        if(tbObat.getSelectedRow()>-1){                
            if(akses.getkode().equals("Admin Utama")){
                if(Valid.hapusTabletf(tabMode,NoSbar,"pemeriksaan_sbar","no_sbar")==true){
                    if(tbObat.getSelectedRow()!= -1){
                        tabMode.removeRow(tbObat.getSelectedRow());
                        emptTeks();
                        JOptionPane.showMessageDialog(null, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                        LCount.setText(""+tabMode.getRowCount());
                    }
                }
            }else{
                if(akses.getkode().equals(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString())){
                    if(Valid.hapusTabletf(tabMode,NoSbar,"pemeriksaan_sbar","no_sbar")==true){
                        if(tbObat.getSelectedRow()!= -1){
                            tabMode.removeRow(tbObat.getSelectedRow());
                            emptTeks();
                            JOptionPane.showMessageDialog(null, "Data berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                            LCount.setText(""+tabMode.getRowCount());
                        }
                    }
                }else{
                        JOptionPane.showMessageDialog(null,"Maaf Hanya bisa di hapus oleh petugas yang menginput SBAR & TBAK..!!");
                }
            }
        }else{
                JOptionPane.showMessageDialog(rootPane,"Silahkan anda pilih data terlebih dahulu..!!");
        }
    }//GEN-LAST:event_BtnHapusActionPerformed

    private void BtnHapusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnHapusKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnHapusActionPerformed(null);
        }else{
            Valid.pindah(evt, BtnGanti, BtnGanti);
        }
    }//GEN-LAST:event_BtnHapusKeyPressed

    private void BtncpSBARActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtncpSBARActionPerformed
        // TODO add your handling code here:
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        carisbar.setNoRM(TNoRw.getText());
        carisbar.setSize(internalFrame1.getWidth()-30,internalFrame1.getHeight()-30);
        carisbar.setLocationRelativeTo(internalFrame1);
        carisbar.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtncpSBARActionPerformed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            SBAR dialog = new SBAR(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private widget.Button BtnDokter;
    private widget.Button BtnGanti;
    private widget.Button BtnHapus;
    private widget.Button BtnKeluar;
    private widget.Button BtnSeekPegawai;
    private widget.Button BtnSimpan;
    private widget.Button BtnValidasi;
    private widget.Button BtncpSBAR;
    private widget.CekBox ChkInput;
    private widget.CekBox ChkJln;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.ComboBox Detik;
    private widget.PanelBiasa FormInput;
    private widget.ComboBox Jam;
    private widget.TextBox KdPeg;
    private widget.TextBox KodeDokter;
    private widget.Label LCount;
    private widget.ComboBox Menit;
    private javax.swing.JMenuItem MnDigitalTTE;
    private javax.swing.JMenuItem MnInputDiagnosa;
    private javax.swing.JMenuItem MnLaporanResume;
    private widget.TextBox NamaDokter;
    private widget.TextBox NoSbar;
    private javax.swing.JPanel PanelInput;
    private widget.ScrollPane Scroll;
    private widget.TextArea TAdvice;
    private widget.TextArea TAssesment;
    private widget.TextArea TBackground;
    private widget.TextBox TCari;
    private widget.TextBox TNoRM;
    private widget.TextBox TNoRw;
    private widget.TextBox TPasien;
    private widget.TextBox TPegawai;
    private widget.TextArea TRecomendation;
    private widget.TextArea TSituation;
    private widget.Tanggal Tanggal;
    private widget.ComboBox cmbbaca;
    private widget.ComboBox cmbkonfirmasi;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel16;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel5;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel7;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Label label14;
    private widget.Label lblJamvalidasi;
    private widget.Label lblTglvalidasi;
    private widget.Label lblValidasi;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private javax.swing.JMenuItem ppBerkasDigital;
    private widget.ScrollPane scrollInput;
    private widget.ScrollPane scrollPane1;
    private widget.ScrollPane scrollPane2;
    private widget.ScrollPane scrollPane3;
    private widget.ScrollPane scrollPane4;
    private widget.ScrollPane scrollPane5;
    private widget.Table tbObat;
    // End of variables declaration//GEN-END:variables

//    public void tampil() {
//        Valid.tabelKosong(tabMode);
//        try{
//            if(TCari.getText().equals("")){
//                ps=koneksi.prepareStatement(
//                    "select reg_periksa.tgl_registrasi,reg_periksa.status_lanjut,pemeriksaan_sbar.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
//                    "pemeriksaan_sbar.tgl_perawatan,pemeriksaan_sbar.jam_rawat,pemeriksaan_sbar.situation, " +
////                    "pemeriksaan_sbar.advice,pemeriksaan_sbar.baca,pemeriksaan_sbar.konfirmasi,pemeriksaan_sbar.kd_dokter,pemeriksaan_sbar.no_sbar,dokter.nm_dokter " +
//                    "pemeriksaan_sbar.background,pemeriksaan_sbar.assesment,pemeriksaan_sbar.recommendation,pemeriksaan_sbar.advice,pemeriksaan_sbar.baca,pemeriksaan_sbar.konfirmasi,pemeriksaan_sbar.kd_dokter,pemeriksaan_sbar.no_sbar,dokter.nm_dokter, pemeriksaan_sbar.nip,validasi_pemeriksaan_sbar.tgl_validasi,validasi_pemeriksaan_sbar.jam_validasi,validasi_pemeriksaan_sbar.status_validasi,pegawai.nama from pasien inner join reg_periksa inner join pemeriksaan_sbar "+
//                    "on pemeriksaan_sbar.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join dokter on pemeriksaan_sbar.kd_dokter=dokter.kd_dokter inner join pegawai on pemeriksaan_sbar.nip=pegawai.nik LEFT JOIN validasi_pemeriksaan_sbar ON validasi_pemeriksaan_sbar.no_sbar = pemeriksaan_sbar.no_sbar where "+
//                    "pemeriksaan_sbar.no_rawat like ? order by pemeriksaan_sbar.no_sbar desc");
//            }else{
//                ps=koneksi.prepareStatement(
//                    "select reg_periksa.tgl_registrasi,reg_periksa.status_lanjut,pemeriksaan_sbar.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien, "+
//                    "pemeriksaan_sbar.tgl_perawatan,pemeriksaan_sbar.jam_rawat,pemeriksaan_sbar.situation, " +
////                    "pemeriksaan_sbar.advice,pemeriksaan_sbar.baca,pemeriksaan_sbar.konfirmasi,pemeriksaan_sbar.kd_dokter,pemeriksaan_sbar.no_sbar,dokter.nm_dokter " +
//                    "pemeriksaan_sbar.background,pemeriksaan_sbar.assesment,pemeriksaan_sbar.recommendation,pemeriksaan_sbar.advice,pemeriksaan_sbar.baca,pemeriksaan_sbar.konfirmasi,pemeriksaan_sbar.kd_dokter,pemeriksaan_sbar.no_sbar,dokter.nm_dokter,pemeriksaan_sbar.nip,validasi_pemeriksaan_sbar.tgl_validasi,validasi_pemeriksaan_sbar.jam_validasi,validasi_pemeriksaan_sbar.status_validasi,pegawai.nama from pasien inner join reg_periksa inner join pemeriksaan_sbar "+
//                    "on pemeriksaan_sbar.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis inner join dokter on pemeriksaan_sbar.kd_dokter=dokter.kd_dokter inner join pegawai on pemeriksaan_sbar.nip=pegawai.nik LEFT JOIN validasi_pemeriksaan_sbar ON validasi_pemeriksaan_sbar.no_sbar = pemeriksaan_sbar.no_sbar where "+
//                    "pemeriksaan_sbar.no_rawat like ? order by pemeriksaan_sbar.no_sbar desc");
//            }
//            try {
//                if(!TCari.getText().equals("")){
//                    ps.setString(1,"%"+TCari.getText()+"%");
//                }else{
//                    ps.setString(1,"%"+TCari.getText()+"%");
//                }   
//                rs=ps.executeQuery();
//                while(rs.next()){
//                    tabMode.addRow(new String[]{
//                        rs.getString("no_sbar"),rs.getString("tgl_registrasi"),rs.getString("status_lanjut"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien"),rs.getString("nip"),rs.getString("nama"),
//                        rs.getString("tgl_perawatan"),rs.getString("jam_rawat"),rs.getString("situation"),rs.getString("background"),rs.getString("assesment"),rs.getString("recommendation"),
//                        rs.getString("advice"),rs.getString("kd_dokter"),rs.getString("nm_dokter"),rs.getString("baca"),rs.getString("konfirmasi"),rs.getString("tgl_validasi"),rs.getString("jam_validasi"),rs.getString("status_validasi")
//                    });
//                }
//            } catch (Exception e) {
//                System.out.println("Notif : "+e);
//            } finally{
//                if(rs!=null){
//                    rs.close();
//                }
//                if(ps!=null){
//                    ps.close();
//                }
//            }
//        }catch(SQLException e){
//            System.out.println("Notifikasi : "+e);
//        }
//        int b=tabMode.getRowCount();
//        LCount.setText(""+b);
//    }

    
    public void tampil() {
        Valid.tabelKosong(tabMode);
        try {
            // Query dasar
            String sql = "select reg_periksa.tgl_registrasi, reg_periksa.status_lanjut, pemeriksaan_sbar.no_rawat, " +
                         "reg_periksa.no_rkm_medis, pasien.nm_pasien, pemeriksaan_sbar.tgl_perawatan, " +
                         "pemeriksaan_sbar.jam_rawat, pemeriksaan_sbar.situation, pemeriksaan_sbar.background, " +
                         "pemeriksaan_sbar.assesment, pemeriksaan_sbar.recommendation, pemeriksaan_sbar.advice, " +
                         "pemeriksaan_sbar.baca, pemeriksaan_sbar.konfirmasi, pemeriksaan_sbar.kd_dokter, " +
                         "pemeriksaan_sbar.no_sbar, dokter.nm_dokter, pemeriksaan_sbar.nip, " +
                         "validasi_pemeriksaan_sbar.tgl_validasi, validasi_pemeriksaan_sbar.jam_validasi, " +
                         "validasi_pemeriksaan_sbar.status_validasi, pegawai.nama " +
                         "from pasien inner join reg_periksa inner join pemeriksaan_sbar " +
                         "on pemeriksaan_sbar.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis " +
                         "inner join dokter on pemeriksaan_sbar.kd_dokter=dokter.kd_dokter " +
                         "inner join pegawai on pemeriksaan_sbar.nip=pegawai.nik " +
                         "LEFT JOIN validasi_pemeriksaan_sbar ON validasi_pemeriksaan_sbar.no_sbar = pemeriksaan_sbar.no_sbar where ";

            // Tambahkan filter pencarian
            if (TCari.getText().trim().equals("")) {
                // Jika kosong, tampilkan semua (berdasarkan no_rawat default)
                ps = koneksi.prepareStatement(sql + "pemeriksaan_sbar.no_rawat like ? order by pemeriksaan_sbar.no_sbar desc");
                ps.setString(1, "%%");
            } else {
                // Jika ada input, cari di no_rawat, no_rkm_medis, no_sbar, atau nm_pasien
                ps = koneksi.prepareStatement(sql + 
                    "(pemeriksaan_sbar.no_rawat like ? or " +
                    "reg_periksa.no_rkm_medis like ? or " +
                    "pemeriksaan_sbar.no_sbar like ? or " +
                    "pasien.nm_pasien like ?) " +
                    "order by pemeriksaan_sbar.no_sbar desc");

                String searchKey = "%" + TCari.getText().trim() + "%";
                ps.setString(1, searchKey);
                ps.setString(2, searchKey);
                ps.setString(3, searchKey);
                ps.setString(4, searchKey);
            }

            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabMode.addRow(new String[]{
                        rs.getString("no_sbar"), rs.getString("tgl_registrasi"), rs.getString("status_lanjut"), 
                        rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), 
                        rs.getString("nip"), rs.getString("nama"), rs.getString("tgl_perawatan"), 
                        rs.getString("jam_rawat"), rs.getString("situation"), rs.getString("background"), 
                        rs.getString("assesment"), rs.getString("recommendation"), rs.getString("advice"), 
                        rs.getString("kd_dokter"), rs.getString("nm_dokter"), rs.getString("baca"), 
                        rs.getString("konfirmasi"), rs.getString("tgl_validasi"), rs.getString("jam_validasi"), 
                        rs.getString("status_validasi")
                    });
                }
            } catch (Exception e) {
                System.out.println("Notif Iterasi Data: " + e);
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (SQLException e) {
            System.out.println("Notifikasi SQL: " + e);
        }
        LCount.setText("" + tabMode.getRowCount());
    }
    
    public void emptTeks() {
        TNoRw.setText("");
        TNoRM.setText("");
        TPasien.setText("");
        KdPeg.setText("");
        TPegawai.setText("");
        NamaDokter.setText("");
        KodeDokter.setText("");
        TSituation.setText("");
        TBackground.setText("");
        TAssesment.setText("");
        TRecomendation.setText("");
        TAdvice.setText("");
        lblTglvalidasi.setText("");
        lblJamvalidasi.setText("");
        lblValidasi.setText("");
        nomorsbar();
        isCek();
        TAdvice.requestFocus();

    } 

    private void getData() {
        if(tbObat.getSelectedRow()!= -1){
            NoSbar.setText(tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()); 
            Valid.SetTgl(Tanggal,tbObat.getValueAt(tbObat.getSelectedRow(),8).toString());
            Jam.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().substring(0,2));
            Menit.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().substring(3,5));
            Detik.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),9).toString().substring(6,8));
            TNoRw.setText(tbObat.getValueAt(tbObat.getSelectedRow(),3).toString()); 
            TNoRM.setText(tbObat.getValueAt(tbObat.getSelectedRow(),4).toString());
            TPasien.setText(tbObat.getValueAt(tbObat.getSelectedRow(),5).toString());
            KdPeg.setText(tbObat.getValueAt(tbObat.getSelectedRow(),6).toString());  
            TPegawai.setText(tbObat.getValueAt(tbObat.getSelectedRow(),7).toString());                       
            TSituation.setText(tbObat.getValueAt(tbObat.getSelectedRow(),10).toString());   
            TBackground.setText(tbObat.getValueAt(tbObat.getSelectedRow(),11).toString());  
            TAssesment.setText(tbObat.getValueAt(tbObat.getSelectedRow(),12).toString());
            TRecomendation.setText(tbObat.getValueAt(tbObat.getSelectedRow(),13).toString());
            TAdvice.setText(tbObat.getValueAt(tbObat.getSelectedRow(),14).toString());
            KodeDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),15).toString());
            NamaDokter.setText(tbObat.getValueAt(tbObat.getSelectedRow(),16).toString());
            cmbbaca.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),17).toString());
            cmbkonfirmasi.setSelectedItem(tbObat.getValueAt(tbObat.getSelectedRow(),18).toString()); 
            // 1. Ambil data dari kolom ke-21 (index 21)
            Object dataValidasi = tbObat.getValueAt(tbObat.getSelectedRow(), 21);
            // 2. Cek apakah data null atau string kosong
            if (dataValidasi == null || dataValidasi.toString().trim().isEmpty()) {
                lblValidasi.setText("Belum Tervalidasi");
                lblValidasi.setForeground(java.awt.Color.RED);
                lblTglvalidasi.setText("Belum Tervalidasi");
                lblJamvalidasi.setText("Belum Tervalidasi");
            } else {
                lblValidasi.setText("Tervalidasi");
                lblValidasi.setForeground(java.awt.Color.GREEN);
                lblTglvalidasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),19).toString());
                lblJamvalidasi.setText(tbObat.getValueAt(tbObat.getSelectedRow(),20).toString());
            }
        }
    }

    private void isRawat() {
         Sequel.cariIsi("select no_rkm_medis from reg_periksa where no_rawat='"+TNoRw.getText()+"' ",TNoRM);
    }

    private void isPsien() {
        Sequel.cariIsi("select nm_pasien from pasien where no_rkm_medis='"+TNoRM.getText()+"' ",TPasien);
    }
    
    public void setNoRm(String norwt, Date tgl2) {
        TNoRw.setText(norwt);
        TCari.setText(norwt);
        Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='"+norwt+"'", DTPCari1);
        DTPCari2.setDate(tgl2);    
        isRawat();
        isPsien();              
        ChkInput.setSelected(true);
        isForm();
        nomorsbar();
        TSituation.requestFocus();
    }
    
    
    private void isForm(){
        if(ChkInput.isSelected()==true){
            ChkInput.setVisible(false);
            PanelInput.setPreferredSize(new Dimension(WIDTH,this.getHeight()-330));
            scrollInput.setVisible(true);      
            ChkInput.setVisible(true);
        }else if(ChkInput.isSelected()==false){           
            ChkInput.setVisible(false);            
            PanelInput.setPreferredSize(new Dimension(WIDTH,20));
            scrollInput.setVisible(false);      
            ChkInput.setVisible(true);
        }
    }
    
    public void isCek(){
        BtnSimpan.setEnabled(akses.getdata_resume_pasien());
        MnInputDiagnosa.setEnabled(akses.getdiagnosa_pasien());   
        ppBerkasDigital.setEnabled(akses.getberkas_digital_perawatan());           

//            if(akses.getjml2() >= 1) {
//                // 1. Set data dasar berdasarkan user yang login
//                KdPeg.setText(akses.getkode());
//                TPegawai.setText(pegawai.tampil3(KdPeg.getText()));
//
//                KodeDokter.setText(akses.getkode());
//                NamaDokter.setText(dokter.tampil3(KodeDokter.getText()));
//
//                // 2. Cek apakah user ini Admin Utama atau bukan
//                if(akses.getadmin() == true) {
//                    // JIKA ADMIN: Semua bebas akses/edit
//                    KdPeg.setEditable(true);
//                    BtnSeekPegawai.setEnabled(true);
//                    BtnValidasi.setEnabled(true);
//                    KodeDokter.setEditable(true);
//                    BtnDokter.setEnabled(true);
//
//                    // Opsional: Admin mungkin ingin form kosong dulu agar bisa pilih manual
//                    // KdPeg.setText(""); TPegawai.setText(""); 
//                } else {
//                    // JIKA BUKAN ADMIN: Kunci field (Self-Locking)
//
//                    // Logika Penguncian Pegawai
//                    if(!TPegawai.getText().equals("")) {
//                        KdPeg.setEditable(false);
//                        BtnSeekPegawai.setEnabled(false);
//                        BtnValidasi.setEnabled(false);
//                    } else {
//                        KdPeg.setText("");
//            //            JOptionPane.showMessageDialog(null, "User login bukan petugas!");
//                    }
//
//                    // Logika Penguncian Dokter
//                    if(!NamaDokter.getText().equals("")) {
//                        KodeDokter.setEditable(false);
//                        BtnDokter.setEnabled(false);
//                        BtnSeekPegawai.setEnabled(false);
//                        BtnValidasi.setEnabled(true);
//                        BtnSimpan.setEnabled(false);
//                        BtnGanti.setEnabled(false);
//                        BtnHapus.setEnabled(false);
//                    } else {
//                        // Jika login sebagai petugas biasa (bukan dokter), biarkan field dokter kosong & terbuka
//                        KodeDokter.setText("");
//                        NamaDokter.setText("");
//                        KodeDokter.setEditable(true);
//                        BtnDokter.setEnabled(true);            
//                    }
//                }
//            }

if(akses.getjml2() >= 1) {
    KdPeg.setText(akses.getkode());
    TPegawai.setText(pegawai.tampil3(KdPeg.getText()));
    KodeDokter.setText(akses.getkode());
    NamaDokter.setText(dokter.tampil3(KodeDokter.getText()));

    if(akses.getadmin() == true) {
        // Hak akses Admin (Bebas)
        KdPeg.setEditable(true);
        BtnSeekPegawai.setEnabled(true);
        BtnValidasi.setEnabled(true);
        KodeDokter.setEditable(true);
        BtnDokter.setEnabled(true);
        BtnSimpan.setEnabled(true);
        BtnGanti.setEnabled(true);
        BtnHapus.setEnabled(true);
        BtncpSBAR.setEnabled(true);
    } else {
        // Logika Non-Admin
        if(!TPegawai.getText().equals("")) {
            KdPeg.setEditable(false);
            BtnSeekPegawai.setEnabled(false);
            BtnValidasi.setEnabled(false);
            BtncpSBAR.setEnabled(true);
        }

        if(!NamaDokter.getText().equals("")) {
            KodeDokter.setEditable(false);
            BtnDokter.setEnabled(false);
            BtnValidasi.setEnabled(true);
            BtncpSBAR.setEnabled(false);
            
            // --- LOGIKA BARU DENGAN QUERY KE TABEL DOKTER ---
            boolean isSpesialisS0002 = false;
            
            if(KdPeg.getText().contains("D000")) {
                try {
                    // Query untuk cek kd_sps berdasarkan kd_dokter
                    // Pastikan variabel 'koneksi' sesuai dengan nama koneksi DB di project Anda
                    String sql = "SELECT kd_sps FROM dokter WHERE kd_dokter = ?";
                    java.sql.PreparedStatement ps = koneksi.prepareStatement(sql);
                    ps.setString(1, KdPeg.getText());
                    java.sql.ResultSet rs = ps.executeQuery();
                    
                    if(rs.next()) {
                        String spesialis = rs.getString("kd_sps");
                        if(spesialis.equals("S0002")) {
                            isSpesialisS0002 = true;
                        }
                    }
                    rs.close();
                    ps.close();
                } catch (Exception e) {
                    System.out.println("Notifikasi : Error cek spesialis dokter - " + e);
                }
            }

            // Eksekusi perubahan tombol berdasarkan hasil query
            if(isSpesialisS0002) {
                BtnSimpan.setEnabled(true);
                BtnGanti.setEnabled(true);
                BtnHapus.setEnabled(true);
                BtnDokter.setEnabled(true);
                BtnValidasi.setEnabled(false);
                BtncpSBAR.setEnabled(true);
            } else {
                BtnSimpan.setEnabled(false);
                BtnGanti.setEnabled(false);
                BtnHapus.setEnabled(false);
                
            }
            // -----------------------------------------------

        } else {
            KodeDokter.setText("");
            NamaDokter.setText("");
            KodeDokter.setEditable(true);
            BtnDokter.setEnabled(true);             
        }
    }
}
    }
    
    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            @Override
            public void actionPerformed(ActionEvent e) {
                String nol_jam = "";
                String nol_menit = "";
                String nol_detik = "";
                // Membuat Date
                //Date dt = new Date();
                Date now = Calendar.getInstance().getTime();

                // Mengambil nilaj JAM, MENIT, dan DETIK Sekarang
                if(ChkJln.isSelected()==true){
                    nilai_jam = now.getHours();
                    nilai_menit = now.getMinutes();
                    nilai_detik = now.getSeconds();
                }else if(ChkJln.isSelected()==false){
                    nilai_jam =Jam.getSelectedIndex();
                    nilai_menit =Menit.getSelectedIndex();
                    nilai_detik =Detik.getSelectedIndex();
                }

                // Jika nilai JAM lebih kecil dari 10 (hanya 1 digit)
                if (nilai_jam <= 9) {
                    // Tambahkan "0" didepannya
                    nol_jam = "0";
                }
                // Jika nilai MENIT lebih kecil dari 10 (hanya 1 digit)
                if (nilai_menit <= 9) {
                    // Tambahkan "0" didepannya
                    nol_menit = "0";
                }
                // Jika nilai DETIK lebih kecil dari 10 (hanya 1 digit)
                if (nilai_detik <= 9) {
                    // Tambahkan "0" didepannya
                    nol_detik = "0";
                }
                // Membuat String JAM, MENIT, DETIK
                String jam = nol_jam + Integer.toString(nilai_jam);
                String menit = nol_menit + Integer.toString(nilai_menit);
                String detik = nol_detik + Integer.toString(nilai_detik);
                // Menampilkan pada Layar
                //tampil_jam.setText("  " + jam + " : " + menit + " : " + detik + "  ");
                Jam.setSelectedItem(jam);
                Menit.setSelectedItem(menit);
                Detik.setSelectedItem(detik);
            }
        };
        // Timer
        new Timer(1000, taskPerformer).start();
    }
    
    private void nomorsbar(){
// Kita ambil 3 digit terakhir dari no_sbar sebagai counter
        Valid.autoNomer3(
            "select ifnull(MAX(CONVERT(RIGHT(no_sbar,3),signed)),0) from pemeriksaan_sbar where no_rawat='" + TNoRw.getText() + "' ",
            "SBAR/" + TNoRw.getText() + "/", 
            3, 
            NoSbar
        );
    }

    private void ganti() {        
            if(Sequel.mengedittf("pemeriksaan_sbar","no_sbar=?","no_rawat=?,tgl_perawatan=?,jam_rawat=?,situation=?,background=?,assesment=?,recommendation=?,advice=?,nip=?,kd_dokter=?,baca=?,konfirmasi=?",13,new String[]{
                    TNoRw.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),TSituation.getText(),
                    TBackground.getText(),TAssesment.getText(),TRecomendation.getText(),TAdvice.getText(),KdPeg.getText(),
                    KodeDokter.getText(),cmbbaca.getSelectedItem().toString(),cmbkonfirmasi.getSelectedItem().toString(),tbObat.getValueAt(tbObat.getSelectedRow(),0).toString()
                })==true){

                    tampil();
                    emptTeks();
                    JOptionPane.showMessageDialog(null, "Data berhasil di update!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                }
 
    }
    
    private void validasi_dokter() {        
            if(Sequel.menyimpantf("validasi_pemeriksaan_sbar","?,?,?,?,?,?,?,?","No.Rawat",8,new String[]{
                    NoSbar.getText(),TNoRw.getText(),TAdvice.getText(),KdPeg.getText(),KodeDokter.getText(),Valid.SetTgl(Tanggal.getSelectedItem()+""),Jam.getSelectedItem()+":"+Menit.getSelectedItem()+":"+Detik.getSelectedItem(),"Validasi",
                })==true){
                    tampil();
                    JOptionPane.showMessageDialog(null, "Data berhasil di VALIDASI!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                    emptTeks();
            }
 
    }
}
