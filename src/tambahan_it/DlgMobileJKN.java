/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tambahan_it;

import tambahan_it.antrianOnlineController;
import bridging.ApiMobileJKN;
import bridging.BPJSCekKodeBooking;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;


/**
 *
 * @author IT RSBB
 */
public final class  DlgMobileJKN extends javax.swing.JDialog {
    private final DefaultTableModel tabMode,tabMode1;
    private validasi Valid=new validasi();
    private sekuel Sequel=new sekuel();
    private int i=0,tot_belum=0,tot_selesai=0,tot_batal=0,jkn_capaian_angka=0,mjkn_capaian_angka=0;
    private double jkn_capaian,mjkn_capaian,jkn_belum,jkn_selesai,mjkn_belum,mjkn_selesai,umum_belum,umum_selesai,sep;
    private ApiMobileJKN api=new ApiMobileJKN();
    private String URL="",link="",utc="",requestJson="",datajam="",datajam2="",kodebooking="",data="",
              nol_jam = "",nol_menit = "",nol_detik = "",jam="",menit="",detik="",hari="",norujukan="",status="1",noresep="",jensiracikan="",
              kodepoli="",kodedokter="",kodebpjs=Sequel.cariIsi("select password_asuransi.kd_pj from password_asuransi");
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode nameNode;
    private JsonNode response;
    private  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private  SimpleDateFormat tanggalFormat = new SimpleDateFormat("yyyy-MM-dd");
    private  Date parsedDate;
    private  Date date = new Date(); 
    private boolean isCheckedJKN = false;
    private boolean isCheckedNonJKN = false;

    /**
     * Creates new form DlgService
     */
    public DlgMobileJKN(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(885,674);
        
        tabMode=new DefaultTableModel(null,new Object[]{
                "P","Kode Booking","Tanggal","Kode Poli","Kode Dokter","Jam Praktek","NIK","Noka","No. HP","RM","Jenis Kunjungan","No. Ref","Sumber Data","Peserta","No. Antrean","Estimasi Dilayani","Created Time","Status","Status Check-in","Status Periksa","Status Soap"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
            }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbPasienJKN.setModel(tabMode);

        tbPasienJKN.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbPasienJKN.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 21; i++) {
            TableColumn column = tbPasienJKN.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(83);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(120);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(60);
            }else if(i==10){
                column.setPreferredWidth(100);
            }else if(i==11){
                column.setPreferredWidth(140);
            }else if(i==12){
                column.setPreferredWidth(100);
            }else if(i==13){
                column.setPreferredWidth(70);
            }else if(i==14){
                column.setPreferredWidth(70);
            }else if(i==15){
                column.setPreferredWidth(120);
            }else if(i==15){
                column.setPreferredWidth(120);
            }else if(i==16){
                column.setPreferredWidth(90);
            }else if(i==17){
                column.setPreferredWidth(90);
            }else if(i==18){
                column.setPreferredWidth(120);
            }else if(i==19){
                column.setPreferredWidth(120);
            }else if(i==20){
                column.setPreferredWidth(120);
            }
        }
        tbPasienJKN.setDefaultRenderer(Object.class, new WarnaTable());
        
        tabMode1=new DefaultTableModel(null,new Object[]{
                "P","Kode Booking","Tanggal","Kode Poli","Kode Dokter","Jam Praktek","NIK","Noka","No. HP","RM","Jenis Kunjungan","No. Ref","Sumber Data","Peserta","No. Antrean","Estimasi Dilayani","Created Time","Status","Status Checkin","Status Periksa","Status Soap"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){
                boolean a = false;
                if (colIndex==0) {
                    a=true;
                }
                return a;
            }
             Class[] types = new Class[] {
                 java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, 
                 java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
             };
             @Override
             public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
             }
        };
        tbNonJKN.setModel(tabMode1);

        tbNonJKN.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbNonJKN.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 21; i++) {
            TableColumn column = tbNonJKN.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(20);
            }else if(i==1){
                column.setPreferredWidth(110);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(70);
            }else if(i==4){
                column.setPreferredWidth(83);
            }else if(i==5){
                column.setPreferredWidth(90);
            }else if(i==6){
                column.setPreferredWidth(120);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(100);
            }else if(i==9){
                column.setPreferredWidth(60);
            }else if(i==10){
                column.setPreferredWidth(100);
            }else if(i==11){
                column.setPreferredWidth(140);
            }else if(i==12){
                column.setPreferredWidth(100);
            }else if(i==13){
                column.setPreferredWidth(70);
            }else if(i==14){
                column.setPreferredWidth(70);
            }else if(i==15){
                column.setPreferredWidth(120);
            }else if(i==15){
                column.setPreferredWidth(120);
            }else if(i==16){
                column.setPreferredWidth(90);
            }else if(i==17){
                column.setPreferredWidth(90);
            }else if(i==18){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==19){
                column.setPreferredWidth(120);
            }else if(i==20){
                column.setPreferredWidth(120);
            }
        }
        tbNonJKN.setDefaultRenderer(Object.class, new WarnaTable());
        
        try {
            link=koneksiDB.URLAPIMOBILEJKN();
        } catch (Exception e) {
            System.out.println("E : "+e);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnCekKodeBooking = new javax.swing.JMenuItem();
        checkAll = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        TabRawat = new widget.TabPane();
        panelBiasa8 = new widget.PanelBiasa();
        panelBiasa2 = new widget.PanelBiasa();
        scrollPane7 = new widget.ScrollPane();
        tbPasienJKN = new widget.Table();
        panelGlass11 = new widget.panelisi();
        KirimMJKN = new javax.swing.JButton();
        KirimFarmasi = new javax.swing.JButton();
        KirimbataMJKN = new javax.swing.JButton();
        jLabel10 = new widget.Label();
        TotBelum = new widget.Label();
        jLabel11 = new widget.Label();
        TotSelesai = new widget.Label();
        jLabel17 = new widget.Label();
        TotBatal = new widget.Label();
        jLabel9 = new widget.Label();
        LCount = new widget.Label();
        panelBiasa9 = new widget.PanelBiasa();
        panelBiasa4 = new widget.PanelBiasa();
        scrollPane9 = new widget.ScrollPane();
        tbNonJKN = new widget.Table();
        panelGlass12 = new widget.panelisi();
        jButton2 = new javax.swing.JButton();
        Kirim6dan8 = new javax.swing.JButton();
        KirimbataMJKN1 = new javax.swing.JButton();
        jLabel12 = new widget.Label();
        TotBelum1 = new widget.Label();
        jLabel13 = new widget.Label();
        TotSelesai1 = new widget.Label();
        jLabel18 = new widget.Label();
        TotBatal1 = new widget.Label();
        jLabel14 = new widget.Label();
        LCount1 = new widget.Label();
        jPanel3 = new javax.swing.JPanel();
        panelGlass8 = new widget.panelisi();
        jLabel21 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel22 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        cmbStatus = new javax.swing.JComboBox<>();
        jLabel8 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnKeluar = new widget.Button();

        MnCekKodeBooking.setBackground(new java.awt.Color(255, 255, 254));
        MnCekKodeBooking.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCekKodeBooking.setForeground(new java.awt.Color(50, 50, 50));
        MnCekKodeBooking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCekKodeBooking.setText("Cek Kode Booking");
        MnCekKodeBooking.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnCekKodeBooking.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnCekKodeBooking.setPreferredSize(new java.awt.Dimension(160, 26));
        MnCekKodeBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCekKodeBookingActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCekKodeBooking);

        checkAll.setBackground(new java.awt.Color(255, 255, 254));
        checkAll.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        checkAll.setForeground(new java.awt.Color(50, 50, 50));
        checkAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        checkAll.setText("Ceklis Semua");
        checkAll.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        checkAll.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        checkAll.setPreferredSize(new java.awt.Dimension(160, 26));
        checkAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkAllActionPerformed(evt);
            }
        });
        jPopupMenu1.add(checkAll);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(630, 327));
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setLayout(new java.awt.BorderLayout());

        TabRawat.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                TabRawatStateChanged(evt);
            }
        });

        panelBiasa8.setLayout(new java.awt.BorderLayout());

        panelBiasa2.setLayout(new java.awt.BorderLayout());

        tbPasienJKN.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbPasienJKN.setComponentPopupMenu(jPopupMenu1);
        tbPasienJKN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbPasienJKNMouseClicked(evt);
            }
        });
        tbPasienJKN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbPasienJKNKeyReleased(evt);
            }
        });
        scrollPane7.setViewportView(tbPasienJKN);

        panelBiasa2.add(scrollPane7, java.awt.BorderLayout.CENTER);

        panelGlass11.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        KirimMJKN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        KirimMJKN.setText("Kirim Data Antrian Pelayanan");
        KirimMJKN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KirimMJKNActionPerformed(evt);
            }
        });
        panelGlass11.add(KirimMJKN);

        KirimFarmasi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        KirimFarmasi.setText("Kirim Data Antrian Farmasi");
        KirimFarmasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KirimFarmasiActionPerformed(evt);
            }
        });
        panelGlass11.add(KirimFarmasi);

        KirimbataMJKN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        KirimbataMJKN.setText("Kirim Data Status Batal");
        KirimbataMJKN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KirimbataMJKNActionPerformed(evt);
            }
        });
        panelGlass11.add(KirimbataMJKN);

        jLabel10.setForeground(new java.awt.Color(255, 153, 0));
        jLabel10.setText("Total Belum :");
        jLabel10.setPreferredSize(new java.awt.Dimension(72, 23));
        panelGlass11.add(jLabel10);

        TotBelum.setForeground(new java.awt.Color(255, 153, 0));
        TotBelum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotBelum.setText("0");
        TotBelum.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass11.add(TotBelum);

        jLabel11.setForeground(new java.awt.Color(102, 153, 0));
        jLabel11.setText("Total Selesai :");
        jLabel11.setPreferredSize(new java.awt.Dimension(77, 23));
        panelGlass11.add(jLabel11);

        TotSelesai.setForeground(new java.awt.Color(102, 153, 0));
        TotSelesai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotSelesai.setText("0");
        TotSelesai.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass11.add(TotSelesai);

        jLabel17.setForeground(new java.awt.Color(149, 0, 8));
        jLabel17.setText("Batal :");
        jLabel17.setPreferredSize(new java.awt.Dimension(40, 23));
        panelGlass11.add(jLabel17);

        TotBatal.setForeground(new java.awt.Color(149, 0, 8));
        TotBatal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotBatal.setText("0");
        TotBatal.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass11.add(TotBatal);

        jLabel9.setText("Record :");
        jLabel9.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass11.add(jLabel9);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass11.add(LCount);

        panelBiasa2.add(panelGlass11, java.awt.BorderLayout.PAGE_END);

        panelBiasa8.add(panelBiasa2, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Mobile JKN", panelBiasa8);

        panelBiasa9.setLayout(new java.awt.BorderLayout());

        panelBiasa4.setLayout(new java.awt.BorderLayout());

        tbNonJKN.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbNonJKN.setComponentPopupMenu(jPopupMenu1);
        tbNonJKN.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbNonJKNMouseClicked(evt);
            }
        });
        tbNonJKN.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tbNonJKNKeyReleased(evt);
            }
        });
        scrollPane9.setViewportView(tbNonJKN);

        panelBiasa4.add(scrollPane9, java.awt.BorderLayout.CENTER);

        panelGlass12.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        jButton2.setText("Kirim Data Antrian Pelayanan");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        panelGlass12.add(jButton2);

        Kirim6dan8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        Kirim6dan8.setText("Kirim Data Antrian Farmasi");
        Kirim6dan8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Kirim6dan8ActionPerformed(evt);
            }
        });
        panelGlass12.add(Kirim6dan8);

        KirimbataMJKN1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Cancel-2-16x16.png"))); // NOI18N
        KirimbataMJKN1.setText("Kirim Data Status Batal");
        KirimbataMJKN1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KirimbataMJKN1ActionPerformed(evt);
            }
        });
        panelGlass12.add(KirimbataMJKN1);

        jLabel12.setForeground(new java.awt.Color(255, 153, 0));
        jLabel12.setText("Total Belum :");
        jLabel12.setPreferredSize(new java.awt.Dimension(72, 23));
        panelGlass12.add(jLabel12);

        TotBelum1.setForeground(new java.awt.Color(255, 153, 0));
        TotBelum1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotBelum1.setText("0");
        TotBelum1.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass12.add(TotBelum1);

        jLabel13.setForeground(new java.awt.Color(102, 153, 0));
        jLabel13.setText("Total Selesai :");
        jLabel13.setPreferredSize(new java.awt.Dimension(77, 23));
        panelGlass12.add(jLabel13);

        TotSelesai1.setForeground(new java.awt.Color(102, 153, 0));
        TotSelesai1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotSelesai1.setText("0");
        TotSelesai1.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass12.add(TotSelesai1);

        jLabel18.setForeground(new java.awt.Color(149, 0, 8));
        jLabel18.setText("Batal :");
        jLabel18.setPreferredSize(new java.awt.Dimension(40, 23));
        panelGlass12.add(jLabel18);

        TotBatal1.setForeground(new java.awt.Color(149, 0, 8));
        TotBatal1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotBatal1.setText("0");
        TotBatal1.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass12.add(TotBatal1);

        jLabel14.setText("Record :");
        jLabel14.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass12.add(jLabel14);

        LCount1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount1.setText("0");
        LCount1.setPreferredSize(new java.awt.Dimension(50, 23));
        panelGlass12.add(LCount1);

        panelBiasa4.add(panelGlass12, java.awt.BorderLayout.PAGE_END);

        panelBiasa9.add(panelBiasa4, java.awt.BorderLayout.CENTER);

        TabRawat.addTab("Non Mobile JKN", panelBiasa9);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        jPanel3.setOpaque(false);
        jPanel3.setPreferredSize(new java.awt.Dimension(30, 50));
        jPanel3.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass8.setPreferredSize(new java.awt.Dimension(30, 55));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel21.setText("Tanggal :");
        jLabel21.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(jLabel21);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "12-08-2025" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass8.add(DTPCari1);

        jLabel22.setText("s/d");
        jLabel22.setPreferredSize(new java.awt.Dimension(15, 23));
        panelGlass8.add(jLabel22);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "12-08-2025" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass8.add(DTPCari2);

        cmbStatus.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Semua", "Belum dilayani", "Sedang dilayani", "Selesai dilayani", "Batal" }));
        panelGlass8.add(cmbStatus);

        jLabel8.setText("Key Word :");
        jLabel8.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(jLabel8);

        TCari.setPreferredSize(new java.awt.Dimension(200, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass8.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
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
        panelGlass8.add(BtnCari);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('K');
        BtnKeluar.setText("Keluar");
        BtnKeluar.setToolTipText("Alt+K");
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        panelGlass8.add(BtnKeluar);

        jPanel3.add(panelGlass8, java.awt.BorderLayout.CENTER);

        internalFrame1.add(jPanel3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void tbPasienJKNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbPasienJKNMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPasienJKNMouseClicked

    private void tbPasienJKNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbPasienJKNKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbPasienJKNKeyReleased

    private void TCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            TampilkanData();
        }
    }//GEN-LAST:event_TCariKeyPressed

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        TampilkanData();
    }//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCariKeyPressed

    private void tbNonJKNMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbNonJKNMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbNonJKNMouseClicked

    private void tbNonJKNKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbNonJKNKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tbNonJKNKeyReleased

    private void KirimMJKNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KirimMJKNActionPerformed
        for (int i = 0; i < tbPasienJKN.getRowCount(); i++) {
            Object ceklis = tbPasienJKN.getValueAt(i, 0);
            if (Boolean.TRUE.equals(ceklis)) {
                Object nilaiKolom = tbPasienJKN.getValueAt(i, 1);

                if (nilaiKolom != null && !nilaiKolom.toString().trim().isEmpty()) {
                    String data = nilaiKolom.toString().trim();
                    String kodeBooking = Sequel.cariIsi("SELECT no_rawat FROM referensi_mobilejkn_bpjs WHERE nobooking='" + data + "'");
                    String statuschekin = Sequel.cariIsi("SELECT status FROM referensi_mobilejkn_bpjs WHERE nobooking='" + data + "'");
                    String statusperiksa = Sequel.cariIsi("SELECT stts FROM reg_periksa WHERE no_rawat='" + kodeBooking + "'");
                    
                    if (!kodeBooking.isEmpty()) {
                        System.out.println("Status Check-in : "+statuschekin);
                        System.out.println("Status Periksa : "+statusperiksa);
                        if ("Checkin".equalsIgnoreCase(statuschekin) && "Sudah".equalsIgnoreCase(statusperiksa)) {
                            prosesKirimTaskID(kodeBooking, data);
                        } else {
                            System.out.println("❌ Tidak bisa kirim TaskID karena status belum sesuai (Checkin & Sudah)");
                        }
                    } else {
                        System.out.println("Data tidak ditemukan untuk kode booking: " + data);
                    }
                }
            }
        }
        emptTeks();
    }//GEN-LAST:event_KirimMJKNActionPerformed

    private void checkAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkAllActionPerformed
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                isCheckedJKN = !isCheckedJKN;
                for (int i = 0; i < tbPasienJKN.getRowCount(); i++) {
                    tbPasienJKN.setValueAt(isCheckedJKN, i, 0);
                }
                checkAll.setText(isCheckedJKN ? "Uncheck All" : "Check All");
                break;
            case 1:
                isCheckedNonJKN = !isCheckedNonJKN;
                for (int i = 0; i < tbNonJKN.getRowCount(); i++) {
                    tbNonJKN.setValueAt(isCheckedNonJKN, i, 0);
                }
                checkAll.setText(isCheckedNonJKN ? "Uncheck All" : "Check All");
                break;
            default:
                break;
        }
    }//GEN-LAST:event_checkAllActionPerformed

    private void TabRawatStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_TabRawatStateChanged
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                boolean semuaCheckedJKN = true;
                for (int i = 0; i < tbPasienJKN.getRowCount(); i++) {
                    if (!(Boolean) tbPasienJKN.getValueAt(i, 0)) {
                        semuaCheckedJKN = false;
                        break;
                    }
                }
                isCheckedJKN = semuaCheckedJKN;
                checkAll.setText(isCheckedJKN ? "Uncheck All" : "Check All");
                break;
            case 1:
                boolean semuaCheckedNonJKN = true;
                for (int i = 0; i < tbNonJKN.getRowCount(); i++) {
                    if (!(Boolean) tbNonJKN.getValueAt(i, 0)) {
                        semuaCheckedNonJKN = false;
                        break;
                    }
                }
                isCheckedNonJKN = semuaCheckedNonJKN;
                checkAll.setText(isCheckedNonJKN ? "Uncheck All" : "Check All");
                break;
        }
    }//GEN-LAST:event_TabRawatStateChanged

    private void MnCekKodeBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCekKodeBookingActionPerformed
        JTable tableDipilih;

        switch (TabRawat.getSelectedIndex()) {
            case 0:
                tableDipilih = tbPasienJKN;
                break;
            case 1:
                tableDipilih = tbNonJKN;
                break;
            default:
                return;
        }

        if (tableDipilih.getSelectedRow() != -1) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            BPJSCekKodeBooking detail = new BPJSCekKodeBooking(null, false);
            detail.tampil(tableDipilih.getValueAt(tableDipilih.getSelectedRow(), 1).toString());
            detail.setSize(internalFrame1.getWidth() - 20, internalFrame1.getHeight() - 20);
            detail.setLocationRelativeTo(internalFrame1);
            detail.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        } else {
            JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data yang mau dicek...!!!!");
            tableDipilih.requestFocus();
        }
    }//GEN-LAST:event_MnCekKodeBookingActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        for (int i = 0; i < tbNonJKN.getRowCount(); i++) {
            Object ceklis = tbNonJKN.getValueAt(i, 0);
            if (Boolean.TRUE.equals(ceklis)) {
                Object nilaiKolom = tbNonJKN.getValueAt(i, 1);

                if (nilaiKolom != null && !nilaiKolom.toString().trim().isEmpty()) {
                    String data = nilaiKolom.toString().trim();
                    String statusperiksa = Sequel.cariIsi("SELECT stts FROM reg_periksa WHERE no_rawat='" + data + "'");
                    String soap = Sequel.cariIsi("SELECT no_rawat FROM pemeriksaan_ralan WHERE no_rawat='" + data + "'");
                    if (!data.isEmpty()) {
                        System.out.println("Status Periksa : "+statusperiksa);
                        if ("Sudah".equalsIgnoreCase(statusperiksa) && !soap.isEmpty()) {
                            prosesKirimNonJKN(data);
                        } else {
                            System.out.println("❌ Tidak bisa kirim Non JKN karena status belum 'Sudah' atau SOAP belum ada");
                        }
                    } else {
                        System.out.println("Data registrasi tidak ditemukan: " + data);
                    }
                }
            }
        }
        emptTeks();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void KirimbataMJKNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KirimbataMJKNActionPerformed
        for (int i = 0; i < tbPasienJKN.getRowCount(); i++) {
            Object ceklis = tbPasienJKN.getValueAt(i, 0);
            if (Boolean.TRUE.equals(ceklis)) {
                Object nilaiKolom = tbPasienJKN.getValueAt(i, 1); // kolom 1 = kode booking

                if (nilaiKolom != null && !nilaiKolom.toString().trim().isEmpty()) {
                    String data = nilaiKolom.toString().trim(); // nilai kode booking (baris i kolom 1)
                   String kodeBooking = Sequel.cariIsi("SELECT no_rawat FROM referensi_mobilejkn_bpjs WHERE nobooking='" + data + "'");
                    String statuschekin = Sequel.cariIsi("SELECT status FROM referensi_mobilejkn_bpjs WHERE nobooking='" + data + "'");
                    String statusperiksa = Sequel.cariIsi("SELECT stts FROM reg_periksa WHERE no_rawat='" + kodeBooking + "'");
                    
                    if (!kodeBooking.isEmpty()) {
                        System.out.println("Status Check-in : "+statuschekin);
                        System.out.println("Status Periksa : "+statusperiksa);
                        System.out.println("Status Kode Booking : "+kodeBooking);
                        System.out.println("Status Data : "+data);
                        if ("Batal".equalsIgnoreCase(statuschekin) && "Batal".equalsIgnoreCase(statusperiksa)) {
                            
                            prosesKirimBatalJKN(kodeBooking, data);
                        } else {
                            System.out.println("❌ Tidak bisa kirim TaskID karena status belum sesuai (Checkin & Sudah)");
                        }
                    } else {
                        System.out.println("Data tidak ditemukan untuk kode booking: " + data);
                    }
                }
            }
        }
        emptTeks();
    }//GEN-LAST:event_KirimbataMJKNActionPerformed

    private void KirimFarmasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KirimFarmasiActionPerformed
        for (int i = 0; i < tbPasienJKN.getRowCount(); i++) {
            Object ceklis = tbPasienJKN.getValueAt(i, 0);
            if (Boolean.TRUE.equals(ceklis)) {
                Object nilaiKolom = tbPasienJKN.getValueAt(i, 1); // kolom 1 = kode booking

                if (nilaiKolom != null && !nilaiKolom.toString().trim().isEmpty()) {
                    String data = nilaiKolom.toString().trim();
                    String kodeBooking = Sequel.cariIsi("SELECT no_rawat FROM referensi_mobilejkn_bpjs WHERE nobooking='" + data + "'");

                    if (!kodeBooking.isEmpty()) {
                        prosesKirimFarmasiJKN(kodeBooking, data);
                    } else {
                        System.out.println("Data tidak ditemukan untuk kode booking: " + data);
                    }
                }
            }
        }
        emptTeks();
    }//GEN-LAST:event_KirimFarmasiActionPerformed

    private void KirimbataMJKN1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KirimbataMJKN1ActionPerformed
        for (int i = 0; i < tbNonJKN.getRowCount(); i++) {
            Object ceklis = tbNonJKN.getValueAt(i, 0);
            if (Boolean.TRUE.equals(ceklis)) {
                Object nilaiKolom = tbNonJKN.getValueAt(i, 1);

                if (nilaiKolom != null && !nilaiKolom.toString().trim().isEmpty()) {
                    String data = nilaiKolom.toString().trim();
                    String statusperiksa = Sequel.cariIsi("SELECT stts FROM reg_periksa WHERE no_rawat='" + data + "'");
                    String soap = Sequel.cariIsi("SELECT no_rawat FROM pemeriksaan_ralan WHERE no_rawat='" + data + "'");
                    if (!data.isEmpty()) {
                        System.out.println("Status Periksa : "+statusperiksa);
                        if ("Batal".equalsIgnoreCase(statusperiksa) && soap.isEmpty()) {
                            prosesKirimBatal(data);
                        } else {
                            System.out.println("❌ Tidak bisa kirim Non JKN karena status belum 'Sudah' atau SOAP belum ada");
                        }
                    } else {
                        System.out.println("Data registrasi tidak ditemukan: " + data);
                    }
                }
            }
        }
        emptTeks(); 
    }//GEN-LAST:event_KirimbataMJKN1ActionPerformed

    private void Kirim6dan8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Kirim6dan8ActionPerformed
         for (int i = 0; i < tbNonJKN.getRowCount(); i++) {
            Object ceklis = tbNonJKN.getValueAt(i, 0);
            if (Boolean.TRUE.equals(ceklis)) {
                Object nilaiKolom = tbNonJKN.getValueAt(i, 1);

                if (nilaiKolom != null && !nilaiKolom.toString().trim().isEmpty()) {
                    String data = nilaiKolom.toString().trim();

                    if (!data.isEmpty()) {
                        prosesKirimFarmasi(data);
                    } else {
                        System.out.println("Data registrasi tidak ditemukan: " + data);
                    }
                }
            }
        }
        emptTeks(); 
    }//GEN-LAST:event_Kirim6dan8ActionPerformed
    
    private void prosesKirimTaskID(String kodeBooking, String data) {
        try {
            String statusbooking=Sequel.cariIsi("select concat(tgl_registrasi, ' ', curtime()) from reg_periksa where reg_periksa.stts=? and reg_periksa.no_rawat=?",kodeBooking);
            System.out.println("Status Booking : "+statusbooking);
            
            String datastatus=Sequel.cariIsi("select concat(tgl_registrasi, ' ', curtime()) from reg_periksa where reg_periksa.stts=? and reg_periksa.no_rawat=?",data);
            System.out.println("Status Periksa : "+datastatus);
            
//            datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat - interval (720 + (rand() * 60 * 7)) second) AS task3 from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat=?",kodeBooking);
            datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat - interval (720 + (rand() * 60 * 7)) second) AS task3 from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat=?",kodeBooking);
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(resep_obat.tgl_peresepan,' ',resep_obat.jam_peresepan - interval (720 + (rand() * 60 * 7)) second) AS task3 from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=?",kodeBooking);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select DATE_ADD(mutasi_berkas.diterima, interval -(600 + (rand() * 60 * 3)) second) AS task3 from mutasi_berkas where mutasi_berkas.no_rawat=? and mutasi_berkas.diterima<>'0000-00-00 00:00:00'",kodeBooking);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("SELECT concat(tgl_registrasi,' ',jam_reg) AS task3 from reg_periksa where reg_periksa.no_rawat=?",kodeBooking);
            }
            if(!datajam.equals("")){
                try {     
                    if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{kodeBooking,"3",datajam})==true){
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid mulai tunggu poli Mobile JKN Pasien BPJS\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                        utc=String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp",utc);
                        headers.add("x-signature",api.getHmac(utc));
                        headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson ="{" +
                                         "\"kodebooking\": \""+data+"\"," +
                                         "\"taskid\": \"3\"," +
                                         "\"waktu\": \""+parsedDate.getTime()+"\"" +
                                      "}";
                        System.out.println("JSON : "+requestJson+"\n");
                        requestEntity = new HttpEntity(requestJson,headers);
                        URL = link+"/antrean/updatewaktu";	
                        System.out.println("URL : "+URL);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if(!nameNode.path("code").asText().equals("200")){
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='"+kodeBooking+"'");
                        }  
                        System.out.println("respon WS BPJS : "+nameNode.path("code").asText()+" "+nameNode.path("message").asText()+"\n");
                    }
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }
            
            datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat - interval (60 + (rand() * 60 * 4)) second) AS task4 from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat=?",kodeBooking);
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(resep_obat.tgl_peresepan,' ',resep_obat.jam_peresepan - interval (60 + (rand() * 60 * 4)) second) AS task4 from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=?",kodeBooking);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select mutasi_berkas.diterima AS task4 from mutasi_berkas where mutasi_berkas.no_rawat=? and mutasi_berkas.diterima<>'0000-00-00 00:00:00'",kodeBooking);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("SELECT concat(tgl_registrasi,' ',jam_reg + interval (900 + (rand() * 60 * 5)) second) AS task4 from reg_periksa where reg_periksa.no_rawat=?",kodeBooking);
            }
            if(!datajam.equals("")){
                try {     
                    if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{kodeBooking,"4",datajam})==true){
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid mulai pelayanan poli Mobile JKN Pasien BPJS\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                        utc=String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp",utc);
                        headers.add("x-signature",api.getHmac(utc));
                        headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson ="{" +
                                         "\"kodebooking\": \""+data+"\"," +
                                         "\"taskid\": \"4\"," +
                                         "\"waktu\": \""+parsedDate.getTime()+"\"" +
                                      "}";
                        System.out.println("JSON : "+requestJson+"\n");
                        requestEntity = new HttpEntity(requestJson,headers);
                        URL = link+"/antrean/updatewaktu";	
                        System.out.println("URL : "+URL);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if(!nameNode.path("code").asText().equals("200")){
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='"+kodeBooking+"'");
                        }   
                        System.out.println("respon WS BPJS : "+nameNode.path("code").asText()+" "+nameNode.path("message").asText()+"\n");
                    }
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }
            
            datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat) AS task5 from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat=?",kodeBooking);
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(resep_obat.tgl_peresepan,' ',resep_obat.jam_peresepan) AS task5 from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=?",kodeBooking);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select DATE_ADD(mutasi_berkas.diterima, interval (120 + (rand() * 60 * 3)) second) AS task5 from mutasi_berkas where mutasi_berkas.no_rawat=? and mutasi_berkas.diterima<>'0000-00-00 00:00:00'",kodeBooking);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("SELECT concat(tgl_registrasi,' ',jam_reg + interval (1200 + (rand() * 60 * 5)) second) AS task5 from reg_periksa where reg_periksa.no_rawat=?",kodeBooking);
            }
            if(!datajam.equals("")){
                try {     
                    if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{kodeBooking,"5",datajam})==true){
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid selesai pelayanan poli Mobile JKN Pasien BPJS\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                        utc=String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp",utc);
                        headers.add("x-signature",api.getHmac(utc));
                        headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson ="{" +
                                         "\"kodebooking\": \""+data+"\"," +
                                         "\"taskid\": \"5\"," +
                                         "\"waktu\": \""+parsedDate.getTime()+"\"" +
                                      "}";
                        System.out.println("JSON : "+requestJson+"\n");
                        requestEntity = new HttpEntity(requestJson,headers);
                        URL = link+"/antrean/updatewaktu";	
                        System.out.println("URL : "+URL);
                        System.out.println("WAKTU TASK ID 5 JKN = "+datajam);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if(!nameNode.path("code").asText().equals("200")){
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='"+kodeBooking+"'");
                        }  
                        System.out.println("respon WS BPJS : "+nameNode.path("code").asText()+" "+nameNode.path("message").asText()+"\n");
                    }
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }else{
                System.out.println("WAKTU TASK ID 5 JKN = BELUM MENGISI SOAP "+kodeBooking);
            } 
        } catch (Exception e) {
            System.out.println("Gagal proses TaskID untuk " + kodeBooking + ": " + e);
        }
    }
    
    private void prosesKirimNonJKN(String data) {
        try {
            datajam=Sequel.cariIsi("select concat(tgl_registrasi, ' ', curtime()) from reg_periksa where reg_periksa.stts=? and reg_periksa.no_rawat=?",data);
            System.out.println("Status Periksa : "+datajam);
            
            datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat) AS task5 from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat=?",data);
            if(datajam.equals("")){
                datajam = Sequel.cariIsi("select concat(resep_obat.tgl_peresepan,' ',resep_obat.jam_peresepan) AS task5 from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=?",data);
            }
            if(datajam.equals("")){
                datajam = Sequel.cariIsi("select DATE_ADD(mutasi_berkas.diterima, interval (120 + (rand() * 60 * 3)) second) AS task5 from mutasi_berkas where mutasi_berkas.no_rawat=? and mutasi_berkas.diterima<>'0000-00-00 00:00:00'",data);
            }
            if(datajam.equals("")){
                datajam = Sequel.cariIsi("SELECT concat(tgl_registrasi,' ',jam_reg + interval (1200 + (rand() * 60 * 5)) second) AS task5 from reg_periksa where reg_periksa.no_rawat=?",data);
            }
            
            if(!datajam.equals("")){
                parsedDate = dateFormat.parse(datajam);
                long task5Millis = parsedDate.getTime();
                String statusDaftar = Sequel.cariIsi("SELECT stts_daftar FROM reg_periksa WHERE no_rawat=?", data);
                if ("Baru".equalsIgnoreCase(statusDaftar)) {
                    long task1Millis = task5Millis - ((1500 + (int)(Math.random() * 181)) * 1000);
                    String task1Time = dateFormat.format(new Date(task1Millis));
                    Date task1Date = dateFormat.parse(task1Time);
                    if (Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{data, "1", task1Time})) {
                        kirimTaskKeWS("1", data, task1Date);
                    }
                    long task2Millis = task5Millis - ((1200 + (int)(Math.random() * 121)) * 1000);
                    String task2Time = dateFormat.format(new Date(task2Millis));
                    Date task2Date = dateFormat.parse(task2Time);
                    if (Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid", "?,?,?", "task id", 3, new String[]{data, "2", task2Time})) {
                        kirimTaskKeWS("2", data, task2Date);
                    }
                }
            }
            
            datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat - interval (720 + (rand() * 60 * 7)) second) AS task3 from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat=?",data);
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(resep_obat.tgl_peresepan,' ',resep_obat.jam_peresepan - interval (720 + (rand() * 60 * 7)) second) AS task3 from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=?",data);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select DATE_ADD(mutasi_berkas.diterima, interval -(600 + (rand() * 60 * 3)) second) AS task3 from mutasi_berkas where mutasi_berkas.no_rawat=? and mutasi_berkas.diterima<>'0000-00-00 00:00:00'",data);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("SELECT concat(tgl_registrasi,' ',jam_reg) AS task3 from reg_periksa where reg_periksa.no_rawat=?",data);
            }
            if(!datajam.equals("")){
                try {     
                    if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{data,"3",datajam})==true){
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid mulai tunggu poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                        kirimTaskKeWS("3", data, parsedDate);
                    }
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }
            
            datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat - interval (60 + (rand() * 60 * 4)) second) AS task4 from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat=?",data);
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(resep_obat.tgl_peresepan,' ',resep_obat.jam_peresepan - interval (60 + (rand() * 60 * 4)) second) AS task4 from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=?",data);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select mutasi_berkas.diterima AS task4 from mutasi_berkas where mutasi_berkas.no_rawat=? and mutasi_berkas.diterima<>'0000-00-00 00:00:00'",data);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("SELECT concat(tgl_registrasi,' ',jam_reg + interval (900 + (rand() * 60 * 5)) second) AS task4 from reg_periksa where reg_periksa.no_rawat=?",data);
            }
            if(!datajam.equals("")){
                try {     
                    if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{data,"4",datajam})==true){
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid mulai pelayanan poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                        kirimTaskKeWS("4", data, parsedDate);
                    }
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }
            
            datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat) AS task5 from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat=?",data);
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(resep_obat.tgl_peresepan,' ',resep_obat.jam_peresepan) AS task5 from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=?",data);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select DATE_ADD(mutasi_berkas.diterima, interval (120 + (rand() * 60 * 3)) second) AS task5 from mutasi_berkas where mutasi_berkas.no_rawat=? and mutasi_berkas.diterima<>'0000-00-00 00:00:00'",data);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("SELECT concat(tgl_registrasi,' ',jam_reg + interval (1200 + (rand() * 60 * 5)) second) AS task5 from reg_periksa where reg_periksa.no_rawat=?",data);
            }
            if(!datajam.equals("")){
                try {     
                    if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{data,"5",datajam})==true){
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid selesai pelayanan poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                        kirimTaskKeWS("5", data, parsedDate);
                    }
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }else{
                System.out.println("WAKTU TASK ID 5 JKN = BELUM MENGISI SOAP "+data);
            } 
        } catch (Exception e) {
            System.out.println("Gagal proses TaskID untuk " + data + ": " + e);
        }
    }
    
    private void prosesKirimBatal(String data) {
        try {
            datajam=Sequel.cariIsi("select concat(tgl_registrasi, ' ', curtime()) from reg_periksa where reg_periksa.stts='Batal' and reg_periksa.no_rawat=?",data);
            if(!datajam.equals("")){
                try {     
                    if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{data,"99",datajam})==true){
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid batal pelayanan poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                        kirimTaskKeWS("99", data, parsedDate);
                        return;
                    }
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal proses TaskID untuk " + data + ": " + e);
        }
    }
    
    private void prosesKirimBatalJKN(String kodeBooking, String data) {
        try {
            datajam=Sequel.cariIsi("select concat(tgl_registrasi, ' ', curtime()) from reg_periksa where reg_periksa.stts='Batal' and reg_periksa.no_rawat=?",kodeBooking);
            if(!datajam.equals("")){
                try {     
                    if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{kodeBooking,"99",datajam})==true){
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid batal pelayanan poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                        kirimTaskKeWS("99", data, parsedDate);
                        return;
                    }
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }
        } catch (Exception e) {
            System.out.println("Gagal proses TaskID untuk " + data + ": " + e);
        }
    }
    
    private void prosesKirimFarmasi(String data) {
        try {
            noresep=Sequel.cariIsi("select resep_obat.no_resep from resep_obat where resep_obat.no_rawat=?",data);
            if(!noresep.equals("")){
                try {     
                    System.out.println("Menjalankan WS tambah antrian farmasi Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                    utc=String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp",utc);
                    headers.add("x-signature",api.getHmac(utc));
                    headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson ="{" +
                                     "\"kodebooking\": \""+data+"\"," +
                                     "\"jenisresep\": \""+(Sequel.cariInteger("select count(resep_dokter_racikan.no_resep) from resep_dokter_racikan where resep_dokter_racikan.no_resep=?",noresep)>0?"Racikan":"Non Racikan")+"\"," +
                                     "\"nomorantrean\": "+Integer.parseInt(StringUtils.right(noresep,4))+"," +
                                     "\"keterangan\": \"Resep dibuat secara elektronik di poli\"" +
                                  "}";
                    System.out.println("JSON : "+requestJson+"\n");
                    requestEntity = new HttpEntity(requestJson,headers);
                    URL = link+"/antrean/farmasi/add";	
                    System.out.println("URL : "+URL);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println("respon WS BPJS : "+nameNode.path("code").asText()+" "+nameNode.path("message").asText()+"\n");
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }

            datajam=Sequel.cariIsi("select concat(resep_obat.tgl_perawatan,' ',resep_obat.jam) from resep_obat where resep_obat.tgl_perawatan<>'0000-00-00' and resep_obat.status='ralan' and resep_obat.no_rawat=?",data);
            if(!datajam.equals("")){
                if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{data,"6",datajam})==true){
                    parsedDate = dateFormat.parse(datajam);
                    try {     
                        System.out.println("Menjalankan WS taskid permintaan resep poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                        utc=String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp",utc);
                        headers.add("x-signature",api.getHmac(utc));
                        headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson ="{" +
                                         "\"kodebooking\": \""+data+"\"," +
                                         "\"taskid\": \"6\"," +
                                         "\"waktu\": \""+parsedDate.getTime()+"\"" +
                                      "}";
                        System.out.println("JSON : "+requestJson+"\n");
                        requestEntity = new HttpEntity(requestJson,headers);
                        URL = link+"/antrean/updatewaktu";	
                        System.out.println("URL : "+URL);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if(!nameNode.path("code").asText().equals("200")){
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='"+data+"'");
                        }  
                        System.out.println("respon WS BPJS : "+nameNode.path("code").asText()+" "+nameNode.path("message").asText()+"\n");
                    }catch (Exception ex) {
                        System.out.println("Notifikasi Bridging : "+ex);
                    }
                }
            }

            datajam=Sequel.cariIsi("select concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan) from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=? and concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan)<>'0000-00-00 00:00:00'",data);
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(obat_racikan.tgl_perawatan,' ',obat_racikan.jam + interval (600 + (rand() * 60 * 10)) second) from obat_racikan where obat_racikan.no_rawat=? and concat(obat_racikan.tgl_perawatan,' ',obat_racikan.jam)<>'0000-00-00 00:00:00'",data);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(resep_obat.tgl_perawatan,' ',resep_obat.jam + interval (300 + (rand() * 60 * 5)) second) from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=? and concat(resep_obat.tgl_perawatan,' ',resep_obat.jam)<>'0000-00-00 00:00:00'",data);
            }
            if(!datajam.equals("")){
                if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{data,"7",datajam})==true){
                    parsedDate = dateFormat.parse(datajam);
                    try {     
                        System.out.println("Menjalankan WS taskid validasi resep poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                        utc=String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp",utc);
                        headers.add("x-signature",api.getHmac(utc));
                        headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson ="{" +
                                         "\"kodebooking\": \""+data+"\"," +
                                         "\"taskid\": \"7\"," +
                                         "\"waktu\": \""+parsedDate.getTime()+"\"" +
                                      "}";
                        System.out.println("JSON : "+requestJson+"\n");
                        requestEntity = new HttpEntity(requestJson,headers);
                        URL = link+"/antrean/updatewaktu";	
                        System.out.println("URL : "+URL);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if(!nameNode.path("code").asText().equals("200")){
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='"+data+"'");
                        }  
                        System.out.println("respon WS BPJS : "+nameNode.path("code").asText()+" "+nameNode.path("message").asText()+"\n");
                    }catch (Exception ex) {
                        System.out.println("Notifikasi Bridging : "+ex);
                    }
                }
            } 
        } catch (Exception e) {
            System.out.println("Gagal proses TaskID untuk " + data + ": " + e);
        }
    }
    
    private void prosesKirimFarmasiJKN(String kodeBooking, String data) {
        try {
            noresep=Sequel.cariIsi("select resep_obat.no_resep from resep_obat where resep_obat.no_rawat=?",kodeBooking);
            if(!noresep.equals("")){
                try {     
                    System.out.println("Menjalankan WS tambah antrian farmasi Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                    utc=String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp",utc);
                    headers.add("x-signature",api.getHmac(utc));
                    headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson ="{" +
                                     "\"kodebooking\": \""+data+"\"," +
                                     "\"jenisresep\": \""+(Sequel.cariInteger("select count(resep_dokter_racikan.no_resep) from resep_dokter_racikan where resep_dokter_racikan.no_resep=?",noresep)>0?"Racikan":"Non Racikan")+"\"," +
                                     "\"nomorantrean\": "+Integer.parseInt(StringUtils.right(noresep,4))+"," +
                                     "\"keterangan\": \"Resep dibuat secara elektronik di poli\"" +
                                  "}";
                    System.out.println("JSON : "+requestJson+"\n");
                    requestEntity = new HttpEntity(requestJson,headers);
                    URL = link+"/antrean/farmasi/add";	
                    System.out.println("URL : "+URL);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    System.out.println("respon WS BPJS : "+nameNode.path("code").asText()+" "+nameNode.path("message").asText()+"\n");
                }catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : "+ex);
                }
            }

            datajam=Sequel.cariIsi("select concat(resep_obat.tgl_perawatan,' ',resep_obat.jam) from resep_obat where resep_obat.tgl_perawatan<>'0000-00-00' and resep_obat.status='ralan' and resep_obat.no_rawat=?",kodeBooking);
            if(!datajam.equals("")){
                if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{kodeBooking,"6",datajam})==true){
                    parsedDate = dateFormat.parse(datajam);
                    try {     
                        System.out.println("Menjalankan WS taskid permintaan resep poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                        utc=String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp",utc);
                        headers.add("x-signature",api.getHmac(utc));
                        headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson ="{" +
                                         "\"kodebooking\": \""+data+"\"," +
                                         "\"taskid\": \"6\"," +
                                         "\"waktu\": \""+parsedDate.getTime()+"\"" +
                                      "}";
                        System.out.println("JSON : "+requestJson+"\n");
                        requestEntity = new HttpEntity(requestJson,headers);
                        URL = link+"/antrean/updatewaktu";	
                        System.out.println("URL : "+URL);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if(!nameNode.path("code").asText().equals("200")){
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='"+kodeBooking+"'");
                        }  
                        System.out.println("respon WS BPJS : "+nameNode.path("code").asText()+" "+nameNode.path("message").asText()+"\n");
                    }catch (Exception ex) {
                        System.out.println("Notifikasi Bridging : "+ex);
                    }
                }
            }

            datajam=Sequel.cariIsi("select concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan) from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=? and concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan)<>'0000-00-00 00:00:00'",kodeBooking);
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(obat_racikan.tgl_perawatan,' ',obat_racikan.jam + interval (600 + (rand() * 60 * 10)) second) from obat_racikan where obat_racikan.no_rawat=? and concat(obat_racikan.tgl_perawatan,' ',obat_racikan.jam)<>'0000-00-00 00:00:00'",kodeBooking);
            }
            if(datajam.equals("")){
                datajam=Sequel.cariIsi("select concat(resep_obat.tgl_perawatan,' ',resep_obat.jam + interval (300 + (rand() * 60 * 5)) second) from resep_obat where resep_obat.status='ralan' and resep_obat.no_rawat=? and concat(resep_obat.tgl_perawatan,' ',resep_obat.jam)<>'0000-00-00 00:00:00'",kodeBooking);
            }
            if(!datajam.equals("")){
                if(Sequel.menyimpantf2("referensi_mobilejkn_bpjs_taskid","?,?,?","task id",3,new String[]{kodeBooking,"7",datajam})==true){
                    parsedDate = dateFormat.parse(datajam);
                    try {     
                        System.out.println("Menjalankan WS taskid validasi resep poli Mobile JKN Pasien Non BPJS/BPS Onsite\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                        utc=String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp",utc);
                        headers.add("x-signature",api.getHmac(utc));
                        headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson ="{" +
                                         "\"kodebooking\": \""+data+"\"," +
                                         "\"taskid\": \"7\"," +
                                         "\"waktu\": \""+parsedDate.getTime()+"\"" +
                                      "}";
                        System.out.println("JSON : "+requestJson+"\n");
                        requestEntity = new HttpEntity(requestJson,headers);
                        URL = link+"/antrean/updatewaktu";	
                        System.out.println("URL : "+URL);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if(!nameNode.path("code").asText().equals("200")){
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='"+kodeBooking+"'");
                        }  
                        System.out.println("respon WS BPJS : "+nameNode.path("code").asText()+" "+nameNode.path("message").asText()+"\n");
                    }catch (Exception ex) {
                        System.out.println("Notifikasi Bridging : "+ex);
                    }
                }
            } 
        } catch (Exception e) {
            System.out.println("Gagal proses TaskID untuk " + data + ": " + e);
        }
    }
    
    private void kirimTaskKeWS(String taskid, String kodeBooking, Date waktu) throws Exception {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
        utc = String.valueOf(api.GetUTCdatetimeAsString());
        headers.add("x-timestamp", utc);
        headers.add("x-signature", api.getHmac(utc));
        headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

        requestJson = "{" +
                        "\"kodebooking\": \"" + kodeBooking + "\"," +
                        "\"taskid\": \"" + taskid + "\"," +
                        "\"waktu\": \"" + waktu.getTime() + "\"" +
                      "}";

        System.out.println("Mengirim TaskID " + taskid + " dengan JSON: " + requestJson);

        requestEntity = new HttpEntity(requestJson, headers);
        URL = link + "/antrean/updatewaktu";
        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
        nameNode = root.path("metadata");

        if (!nameNode.path("code").asText().equals("200")) {
            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='" + taskid + "' and no_rawat='" + kodeBooking + "'");
        }

        System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText());
    }
    
    public void emptTeks(){
        TampilkanData();
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                isCheckedJKN = false;
                for (int i = 0; i < tbPasienJKN.getRowCount(); i++) {
                    tbPasienJKN.setValueAt(false, i, 0);
                }
                break;
            case 1:
                isCheckedNonJKN = false;
                for (int i = 0; i < tbNonJKN.getRowCount(); i++) {
                    tbNonJKN.setValueAt(false, i, 0);
                }
                break;
        }
        checkAll.setText("Check All");
    }
    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DlgMobileJKN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DlgMobileJKN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DlgMobileJKN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DlgMobileJKN.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DlgMobileJKN dialog = new DlgMobileJKN(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnCari;
    private widget.Button BtnKeluar;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private javax.swing.JButton Kirim6dan8;
    private javax.swing.JButton KirimFarmasi;
    private javax.swing.JButton KirimMJKN;
    private javax.swing.JButton KirimbataMJKN;
    private javax.swing.JButton KirimbataMJKN1;
    private widget.Label LCount;
    private widget.Label LCount1;
    private javax.swing.JMenuItem MnCekKodeBooking;
    private widget.TextBox TCari;
    private widget.TabPane TabRawat;
    private widget.Label TotBatal;
    private widget.Label TotBatal1;
    private widget.Label TotBelum;
    private widget.Label TotBelum1;
    private widget.Label TotSelesai;
    private widget.Label TotSelesai1;
    private javax.swing.JMenuItem checkAll;
    private javax.swing.JComboBox<String> cmbStatus;
    private widget.InternalFrame internalFrame1;
    private javax.swing.JButton jButton2;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.PanelBiasa panelBiasa2;
    private widget.PanelBiasa panelBiasa4;
    private widget.PanelBiasa panelBiasa8;
    private widget.PanelBiasa panelBiasa9;
    private widget.panelisi panelGlass11;
    private widget.panelisi panelGlass12;
    private widget.panelisi panelGlass8;
    private widget.ScrollPane scrollPane7;
    private widget.ScrollPane scrollPane9;
    private widget.Table tbNonJKN;
    private widget.Table tbPasienJKN;
    // End of variables declaration//GEN-END:variables
    private void TampilkanData(){
        switch (TabRawat.getSelectedIndex()) {
            case 0:
                pasienMJKN();
                break;
            case 1:
                nonMJKN();
                break;
            default:
                break;
        }
    }

    private void pasienMJKN() {
        Valid.tabelKosong(tabMode);

        antrianOnlineController controller = new antrianOnlineController();
        String tglAwal = Valid.SetTgl(DTPCari1.getSelectedItem() + "");
        String tglAkhir = Valid.SetTgl(DTPCari2.getSelectedItem() + "");
        String filter = cmbStatus.getSelectedItem().toString();
        String Cari = TCari.getText();

        antrianOnlineController.AntrianResult hasil = controller.getDataRefAntrian(tglAwal, tglAkhir, filter, "Mobile JKN", Cari);

        for (Object[] row : hasil.data) {
            tabMode.addRow(row);
        }

        LCount.setText("" + tabMode.getRowCount());
        TotBelum.setText("" + hasil.tot_belum);
        TotSelesai.setText("" + hasil.tot_selesai);
        TotBatal.setText("" + hasil.tot_batal);
    }
    
    private void nonMJKN() {
        Valid.tabelKosong(tabMode1);

        antrianOnlineController controller = new antrianOnlineController();
        String tglAwal = Valid.SetTgl(DTPCari1.getSelectedItem() + "");
        String tglAkhir = Valid.SetTgl(DTPCari2.getSelectedItem() + "");
        String filter = cmbStatus.getSelectedItem().toString();
        String Cari = TCari.getText();

        antrianOnlineController.AntrianResult hasil = controller.getDataRefAntrian(tglAwal, tglAkhir, filter, "Bridging Antrean", Cari);

        for (Object[] row : hasil.data) {
            tabMode1.addRow(row);
        }

        LCount1.setText("" + tabMode1.getRowCount());
        TotBelum1.setText("" + hasil.tot_belum);
        TotSelesai1.setText("" + hasil.tot_selesai);
        TotBatal1.setText("" + hasil.tot_batal);
    }
    
}
