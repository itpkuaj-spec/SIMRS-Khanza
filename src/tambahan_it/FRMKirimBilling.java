package tambahan_it;
import informasi.*;
import simrskhanza.DlgCariBangsal;
import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.batasInput;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import tambahan_it.*;
/**
 *
 * @author perpustakaan
 */
public class FRMKirimBilling extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private Connection koneksi=koneksiDB.condb();
    private validasi Valid=new validasi();
    private DlgCariBangsal bangsal=new DlgCariBangsal(null,false);
    private sekuel Sequel=new sekuel();
    private String kmr="",key="",terbitsep="",namadokter="",order="order by bangsal.nm_bangsal,kamar_inap.tgl_masuk,kamar_inap.jam_masuk",hariawal="";
    private PreparedStatement ps,psibu,psanak;
    private ResultSet rs,rs2;
    private int i;
    private boolean ceksukses=false;
    private double lama=0,persenbayi=0,hargakamar=0;

    /** Creates new form DlgKamarInap
     * @param parent
       @param modal */
    public FRMKirimBilling(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8,1);
        setSize(628,674);

        Object[] row={"No. Telp","No.Rawat","Nomer RM","Nama Pasien","Alamat Pasien","Penanggung Jawab","Hubungan P.J.","Jenis Bayar","Kamar","Tarif Kamar",
            "Diagnosa Awal","Diagnosa Akhir","Tgl.Masuk","Jam Masuk","Tgl.Keluar","Jam Keluar",
            "Ttl.Biaya","Stts.Pulang","Lama","Dokter P.J.","Kamar","Status Bayar","Agama"};
        tabMode=new DefaultTableModel(null,row){
              @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbKamIn.setModel(tabMode);

        //tbObat.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbObat.getBackground()));
        tbKamIn.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbKamIn.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 22; i++) {
            TableColumn column = tbKamIn.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(70);
            }else if(i==1){
                column.setPreferredWidth(105);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(170);
            }else if(i==4){
                column.setPreferredWidth(150);
            }else if(i==5){
                column.setPreferredWidth(120);
            }else if(i==6){
                column.setPreferredWidth(80);
            }else if(i==7){
                column.setPreferredWidth(80);
            }else if(i==8){
                column.setPreferredWidth(150);
            }else if(i==9){
                column.setPreferredWidth(75);
            }else if(i==10){
                column.setPreferredWidth(90);
            }else if(i==11){
                column.setPreferredWidth(90);
            }else if(i==12){
                column.setPreferredWidth(70);
            }else if(i==13){
                column.setPreferredWidth(60);
            }else if(i==14){
                column.setPreferredWidth(70);
            }else if(i==15){
                column.setPreferredWidth(60);
            }else if(i==16){
                column.setPreferredWidth(80);
            }else if(i==17){
                column.setPreferredWidth(75);
            }else if(i==18){
                column.setPreferredWidth(40);
            }else if(i==19){
                column.setPreferredWidth(130);
            }else if(i==20){
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }else if(i==22){
                column.setPreferredWidth(60);
            }
        }
        tbKamIn.setDefaultRenderer(Object.class, new WarnaTable());
        TCari.setDocument(new batasInput((byte)100).getKata(TCari));
        
        bangsal.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {}
            @Override
            public void windowClosing(WindowEvent e) {}
            @Override
            public void windowClosed(WindowEvent e) {
                if(bangsal.getTable().getSelectedRow()!= -1){                   
                    BangsalCari.setText(bangsal.getTable().getValueAt(bangsal.getTable().getSelectedRow(),1).toString());
                }     
                BangsalCari.requestFocus();
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
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        internalFrame1 = new widget.InternalFrame();
        PanelCariUtama = new javax.swing.JPanel();
        panelGlass11 = new widget.panelisi();
        jLabel21 = new widget.Label();
        BangsalCari = new widget.TextBox();
        btnBangsalCari = new widget.Button();
        jLabel6 = new widget.Label();
        TCari = new widget.TextBox();
        BtnCari = new widget.Button();
        BtnAll = new widget.Button();
        jLabel37 = new widget.Label();
        cmbStatusBayar = new widget.ComboBox();
        jLabel8 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar = new widget.Button();
        BtnDpjp2 = new widget.Button();
        panelCari = new widget.panelisi();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        DTPCari1 = new widget.Tanggal();
        jLabel22 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        R3 = new widget.RadioButton();
        DTPCari3 = new widget.Tanggal();
        cmbJam1 = new widget.ComboBox();
        cmbMnt1 = new widget.ComboBox();
        cmbDtk1 = new widget.ComboBox();
        jLabel25 = new widget.Label();
        cmbJam2 = new widget.ComboBox();
        cmbMnt2 = new widget.ComboBox();
        cmbDtk2 = new widget.ComboBox();
        btnkirimwa = new widget.Button();
        Scroll = new widget.ScrollPane();
        tbKamIn = new widget.Table();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                formWindowActivated(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ KIRIM BILLING PASIEN UMUM]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        PanelCariUtama.setName("PanelCariUtama"); // NOI18N
        PanelCariUtama.setOpaque(false);
        PanelCariUtama.setPreferredSize(new java.awt.Dimension(100, 88));
        PanelCariUtama.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass11.setName("panelGlass11"); // NOI18N
        panelGlass11.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel21.setText("Kamar :");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(45, 23));
        panelGlass11.add(jLabel21);

        BangsalCari.setName("BangsalCari"); // NOI18N
        BangsalCari.setPreferredSize(new java.awt.Dimension(230, 23));
        BangsalCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BangsalCariKeyPressed(evt);
            }
        });
        panelGlass11.add(BangsalCari);

        btnBangsalCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnBangsalCari.setMnemonic('3');
        btnBangsalCari.setToolTipText("Alt+3");
        btnBangsalCari.setName("btnBangsalCari"); // NOI18N
        btnBangsalCari.setPreferredSize(new java.awt.Dimension(28, 23));
        btnBangsalCari.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBangsalCariActionPerformed(evt);
            }
        });
        btnBangsalCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnBangsalCariKeyPressed(evt);
            }
        });
        panelGlass11.add(btnBangsalCari);

        jLabel6.setText("Key Word :");
        jLabel6.setName("jLabel6"); // NOI18N
        jLabel6.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass11.add(jLabel6);

        TCari.setEditable(false);
        TCari.setName("TCari"); // NOI18N
        TCari.setPreferredSize(new java.awt.Dimension(250, 23));
        TCari.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKeyPressed(evt);
            }
        });
        panelGlass11.add(TCari);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('4');
        BtnCari.setToolTipText("Alt+4");
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
        panelGlass11.add(BtnCari);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('4');
        BtnAll.setToolTipText("Alt+4");
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
        panelGlass11.add(BtnAll);

        jLabel37.setText("Stts.Bayar :");
        jLabel37.setName("jLabel37"); // NOI18N
        jLabel37.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass11.add(jLabel37);

        cmbStatusBayar.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Semua", "Sudah Bayar", "Belum Bayar" }));
        cmbStatusBayar.setName("cmbStatusBayar"); // NOI18N
        cmbStatusBayar.setPreferredSize(new java.awt.Dimension(120, 23));
        panelGlass11.add(cmbStatusBayar);

        jLabel8.setText("Record :");
        jLabel8.setName("jLabel8"); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(60, 23));
        panelGlass11.add(jLabel8);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(45, 23));
        panelGlass11.add(LCount);

        BtnKeluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar.setMnemonic('4');
        BtnKeluar.setText("KELUAR");
        BtnKeluar.setToolTipText("Alt+4");
        BtnKeluar.setName("BtnKeluar"); // NOI18N
        BtnKeluar.setPreferredSize(new java.awt.Dimension(100, 29));
        BtnKeluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluarActionPerformed(evt);
            }
        });
        panelGlass11.add(BtnKeluar);

        BtnDpjp2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/report24.png"))); // NOI18N
        BtnDpjp2.setMnemonic('K');
        BtnDpjp2.setText("Update Hari Rawat");
        BtnDpjp2.setToolTipText("Alt+K");
        BtnDpjp2.setFont(new java.awt.Font("SansSerif", 0, 11)); // NOI18N
        BtnDpjp2.setGlassColor(new java.awt.Color(255, 255, 255));
        BtnDpjp2.setName("BtnDpjp2"); // NOI18N
        BtnDpjp2.setPreferredSize(new java.awt.Dimension(170, 30));
        BtnDpjp2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnDpjp2ActionPerformed(evt);
            }
        });
        BtnDpjp2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnDpjp2KeyPressed(evt);
            }
        });
        panelGlass11.add(BtnDpjp2);

        PanelCariUtama.add(panelGlass11, java.awt.BorderLayout.CENTER);

        panelCari.setName("panelCari"); // NOI18N
        panelCari.setPreferredSize(new java.awt.Dimension(44, 47));
        panelCari.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 2, 9));

        R1.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R1);
        R1.setSelected(true);
        R1.setText("Belum Pulang");
        R1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R1.setName("R1"); // NOI18N
        R1.setPreferredSize(new java.awt.Dimension(95, 23));
        R1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                R1ItemStateChanged(evt);
            }
        });
        R1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                R1ActionPerformed(evt);
            }
        });
        panelCari.add(R1);

        R2.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R2);
        R2.setText("Tgl.Masuk :");
        R2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R2.setName("R2"); // NOI18N
        R2.setPreferredSize(new java.awt.Dimension(90, 23));
        R2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                R2ItemStateChanged(evt);
            }
        });
        panelCari.add(R2);

        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "26-11-2025" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        DTPCari1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari1ItemStateChanged(evt);
            }
        });
        panelCari.add(DTPCari1);

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("s.d");
        jLabel22.setName("jLabel22"); // NOI18N
        jLabel22.setPreferredSize(new java.awt.Dimension(25, 23));
        panelCari.add(jLabel22);

        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "26-11-2025" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        DTPCari2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari1ItemStateChanged(evt);
            }
        });
        DTPCari2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari2KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari2);

        R3.setBorder(javax.swing.BorderFactory.createLineBorder(java.awt.Color.pink));
        buttonGroup1.add(R3);
        R3.setText("Pulang :");
        R3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        R3.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        R3.setName("R3"); // NOI18N
        R3.setPreferredSize(new java.awt.Dimension(70, 23));
        R3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                R3ItemStateChanged(evt);
            }
        });
        panelCari.add(R3);

        DTPCari3.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "26-11-2025" }));
        DTPCari3.setDisplayFormat("dd-MM-yyyy");
        DTPCari3.setName("DTPCari3"); // NOI18N
        DTPCari3.setOpaque(false);
        DTPCari3.setPreferredSize(new java.awt.Dimension(90, 23));
        DTPCari3.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                DTPCari3ItemStateChanged(evt);
            }
        });
        DTPCari3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPCari3KeyPressed(evt);
            }
        });
        panelCari.add(DTPCari3);

        cmbJam1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam1.setName("cmbJam1"); // NOI18N
        cmbJam1.setPreferredSize(new java.awt.Dimension(62, 23));
        cmbJam1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbJam1ItemStateChanged(evt);
            }
        });
        cmbJam1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJam1KeyPressed(evt);
            }
        });
        panelCari.add(cmbJam1);

        cmbMnt1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt1.setName("cmbMnt1"); // NOI18N
        cmbMnt1.setPreferredSize(new java.awt.Dimension(62, 23));
        cmbMnt1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMnt1ItemStateChanged(evt);
            }
        });
        cmbMnt1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMnt1KeyPressed(evt);
            }
        });
        panelCari.add(cmbMnt1);

        cmbDtk1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk1.setName("cmbDtk1"); // NOI18N
        cmbDtk1.setPreferredSize(new java.awt.Dimension(62, 23));
        cmbDtk1.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDtk1ItemStateChanged(evt);
            }
        });
        cmbDtk1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDtk1KeyPressed(evt);
            }
        });
        panelCari.add(cmbDtk1);

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("s.d");
        jLabel25.setName("jLabel25"); // NOI18N
        jLabel25.setPreferredSize(new java.awt.Dimension(25, 23));
        panelCari.add(jLabel25);

        cmbJam2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23" }));
        cmbJam2.setName("cmbJam2"); // NOI18N
        cmbJam2.setPreferredSize(new java.awt.Dimension(62, 23));
        cmbJam2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbJam2ItemStateChanged(evt);
            }
        });
        cmbJam2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbJam2KeyPressed(evt);
            }
        });
        panelCari.add(cmbJam2);

        cmbMnt2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbMnt2.setName("cmbMnt2"); // NOI18N
        cmbMnt2.setPreferredSize(new java.awt.Dimension(62, 23));
        cmbMnt2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbMnt2ItemStateChanged(evt);
            }
        });
        cmbMnt2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbMnt2KeyPressed(evt);
            }
        });
        panelCari.add(cmbMnt2);

        cmbDtk2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59" }));
        cmbDtk2.setName("cmbDtk2"); // NOI18N
        cmbDtk2.setPreferredSize(new java.awt.Dimension(62, 23));
        cmbDtk2.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbDtk2ItemStateChanged(evt);
            }
        });
        cmbDtk2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbDtk2KeyPressed(evt);
            }
        });
        panelCari.add(cmbDtk2);

        btnkirimwa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        btnkirimwa.setMnemonic('3');
        btnkirimwa.setText("KIRIM WA");
        btnkirimwa.setToolTipText("Alt+3");
        btnkirimwa.setName("btnkirimwa"); // NOI18N
        btnkirimwa.setPreferredSize(new java.awt.Dimension(200, 23));
        btnkirimwa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnkirimwaActionPerformed(evt);
            }
        });
        btnkirimwa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnkirimwaKeyPressed(evt);
            }
        });
        panelCari.add(btnkirimwa);

        PanelCariUtama.add(panelCari, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(PanelCariUtama, java.awt.BorderLayout.PAGE_END);

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbKamIn.setAutoCreateRowSorter(true);
        tbKamIn.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbKamIn.setName("tbKamIn"); // NOI18N
        Scroll.setViewportView(tbKamIn);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

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

    private void DTPCari1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari1ItemStateChanged
         R2.setSelected(true);
         tampil();          
       
}//GEN-LAST:event_DTPCari1ItemStateChanged

    private void DTPCari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari2KeyPressed
        Valid.pindah(evt,DTPCari1,BangsalCari);
}//GEN-LAST:event_DTPCari2KeyPressed

private void btnBangsalCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBangsalCariActionPerformed
       bangsal.isCek();
        bangsal.emptTeks();        
        bangsal.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
        bangsal.setLocationRelativeTo(internalFrame1);
        bangsal.setAlwaysOnTop(false);
        bangsal.setVisible(true);
}//GEN-LAST:event_btnBangsalCariActionPerformed

private void btnBangsalCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnBangsalCariKeyPressed
   Valid.pindah(evt,DTPCari2,TCari);
}//GEN-LAST:event_btnBangsalCariKeyPressed

private void BangsalCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BangsalCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            tampil();
        }else if(evt.getKeyCode()==KeyEvent.VK_UP){
            btnBangsalCariActionPerformed(null);
        }else{Valid.pindah(evt, DTPCari2, TCari);}
}//GEN-LAST:event_BangsalCariKeyPressed

private void DTPCari3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_DTPCari3ItemStateChanged
   R3.setSelected(true);
   tampil(); 
}//GEN-LAST:event_DTPCari3ItemStateChanged

private void DTPCari3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPCari3KeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_DTPCari3KeyPressed

private void cmbJam1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbJam1ItemStateChanged
   R3.setSelected(true);
   tampil();
}//GEN-LAST:event_cmbJam1ItemStateChanged

private void cmbJam1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJam1KeyPressed
  
}//GEN-LAST:event_cmbJam1KeyPressed

private void cmbMnt1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMnt1KeyPressed
 
}//GEN-LAST:event_cmbMnt1KeyPressed

private void cmbDtk1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDtk1KeyPressed
  
}//GEN-LAST:event_cmbDtk1KeyPressed

private void cmbDtk2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbDtk2KeyPressed
   
}//GEN-LAST:event_cmbDtk2KeyPressed

private void cmbMnt2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbMnt2KeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_cmbMnt2KeyPressed

private void cmbJam2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbJam2ItemStateChanged
   R3.setSelected(true);
   tampil();
}//GEN-LAST:event_cmbJam2ItemStateChanged

private void cmbJam2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbJam2KeyPressed
// TODO add your handling code here:
}//GEN-LAST:event_cmbJam2KeyPressed

private void cmbMnt1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMnt1ItemStateChanged
   R3.setSelected(true);
   tampil();
}//GEN-LAST:event_cmbMnt1ItemStateChanged

private void cmbDtk1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDtk1ItemStateChanged
   R3.setSelected(true);
   tampil();
}//GEN-LAST:event_cmbDtk1ItemStateChanged

private void cmbMnt2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbMnt2ItemStateChanged
   R3.setSelected(true);
   tampil();
}//GEN-LAST:event_cmbMnt2ItemStateChanged

private void cmbDtk2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbDtk2ItemStateChanged
   R3.setSelected(true);
   tampil();
}//GEN-LAST:event_cmbDtk2ItemStateChanged

private void R1ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_R1ItemStateChanged
   tampil();
}//GEN-LAST:event_R1ItemStateChanged

private void R2ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_R2ItemStateChanged
   tampil();
}//GEN-LAST:event_R2ItemStateChanged

private void R3ItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_R3ItemStateChanged
   tampil();
}//GEN-LAST:event_R3ItemStateChanged

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        TCari.setText("UMUM");
        tampil();
       
    }//GEN-LAST:event_formWindowOpened

    private void BtnKeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluarActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluarActionPerformed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        TCari.setText("");
        BangsalCari.setText("");
        tampil();
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            TCari.setText("");
            BangsalCari.setText("");
            tampil();
        }else{
            Valid.pindah(evt, BtnCari, BtnKeluar);
        }
    }//GEN-LAST:event_BtnAllKeyPressed

    private void formWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowActivated
        TCari.requestFocus();
    }//GEN-LAST:event_formWindowActivated

    private void BtnDpjp2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnDpjp2ActionPerformed
        if(tabMode.getRowCount()==0){
            JOptionPane.showMessageDialog(null,"Maaf, table masih kosong...!!!!");
            TCari.requestFocus();
        }else if(R1.isSelected()==false){
            JOptionPane.showMessageDialog(rootPane,"Tampilkan data yang belum pulang terlebih dahulu");
        }else{
            updateHari();
        }        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDpjp2ActionPerformed

    private void BtnDpjp2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnDpjp2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnDpjp2KeyPressed

    private void R1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_R1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_R1ActionPerformed

    private void btnkirimwaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnkirimwaActionPerformed
        // TODO add your handling code here:
            
            new Thread(() -> {
            int rowCount = tbKamIn.getRowCount();

            for (int i = 0; i < rowCount; i++) {
                String noRawat = tbKamIn.getValueAt(i, 1).toString();
                String namaPasien = tbKamIn.getValueAt(i, 3).toString();
                String totalBiaya = tbKamIn.getValueAt(i, 16).toString(); // misal kolom biaya
                String tanggalmasuk = tbKamIn.getValueAt(i, 12).toString();
                String noRM = tbKamIn.getValueAt(i, 2).toString();
                String noTelp = tbKamIn.getValueAt(i, 0).toString();
                String lamainap = tbKamIn.getValueAt(i, 18).toString();
                String alamat = tbKamIn.getValueAt(i, 4).toString();
                if(noTelp == null || noTelp.trim().equals("")) continue;

                String pesan = "============================\n"
                                + akses.getnamars() + "\n"
                                + "*Pemberitahuan Billing Pasien*\n"
                                + "============================\n"
                                + "Assalamu'alaikum ..\n" 
                                + "\n"
                                + "Kepada Yth Pasien/ Keluarga dari\n"              
                                + "Nama Pasien: *" + namaPasien + "*\n"
                                + "Alamat : *" + alamat + "*\n"
                                + "Nomor Rawat : " + noRawat + "\n"
                                + "Nomor RM : " + noRM + "\n"
                                + "Tanggal Masuk : " + tanggalmasuk + "\n"
                                + "Lama Inap : " + lamainap + "\n"
                                + "Total biaya kamar sampai saat pesan ini dikirim adalah sebesar Rp " + totalBiaya + "\n"
                                + "\n"
                                + "Terimakasih atas perhatian Bapak/Ibu, semoga lekas membaik ..\n"
                                + "Pesan ini otomatis dikirm dari sistem, dan hanya pemberitahuan kepada pasien/ keluarga\n"
                                + "===========================\n"
                                + "*Mohon tidak menghubungi No WA ini,*\n"
                                + "Jika membutuhkan bantuan kami mengenai billing, mohon bisa menghubungi KASIR kami.\n"
                                + "\n"
                                + "Silahkan Klik Link berikut.\n"
                                + "https://wa.me/6282313968400"
                                + "\n"
                                + "===========================\n"
                                + "Ikuti Saluran kami di WhatsApp untuk mendapatkan informasi up to date dari kami.\n"
                                + "Silahkan Klik Link berikut.\n"
                                + "https://shorturl.at/0L6ag"
                                + "\n"
                                + "===========================\n";
                        
                        
//                        "Yth. " + namaPasien + ",\n"
//                        + "Total biaya perawatan Anda adalah Rp " + totalBiaya + "\n"
//                        + "Silakan melakukan pembayaran. Terima kasih.";

                
                
                String nomorTujuan = noTelp.replaceFirst("^0", "62");
                String result = FonnteAPIkirimbanyak.sendMessage(noTelp, pesan);
                System.out.println("Kirim ke " + namaPasien + ": " + result);

                try { Thread.sleep(120); } catch (Exception e) {}
            }

            JOptionPane.showMessageDialog(null, "Pengiriman WA selesai");
        }).start();
    }//GEN-LAST:event_btnkirimwaActionPerformed

    private void btnkirimwaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnkirimwaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnkirimwaKeyPressed

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            FRMKirimBilling dialog = new FRMKirimBilling(new javax.swing.JFrame(), true);
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
    private widget.TextBox BangsalCari;
    private widget.Button BtnAll;
    private widget.Button BtnCari;
    private widget.Button BtnDpjp2;
    private widget.Button BtnKeluar;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Tanggal DTPCari3;
    private widget.Label LCount;
    private javax.swing.JPanel PanelCariUtama;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.ScrollPane Scroll;
    private widget.TextBox TCari;
    private widget.Button btnBangsalCari;
    private widget.Button btnkirimwa;
    private javax.swing.ButtonGroup buttonGroup1;
    private widget.ComboBox cmbDtk1;
    private widget.ComboBox cmbDtk2;
    private widget.ComboBox cmbJam1;
    private widget.ComboBox cmbJam2;
    private widget.ComboBox cmbMnt1;
    private widget.ComboBox cmbMnt2;
    private widget.ComboBox cmbStatusBayar;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel25;
    private widget.Label jLabel37;
    private widget.Label jLabel6;
    private widget.Label jLabel8;
    private widget.panelisi panelCari;
    private widget.panelisi panelGlass11;
    private widget.Table tbKamIn;
    // End of variables declaration//GEN-END:variables

    public void tampil() {
        if(ceksukses==false){
            ceksukses=true;
            if(R1.isSelected()==true){
                kmr=" kamar_inap.stts_pulang='-' and reg_periksa.status_bayar like '%"+cmbStatusBayar.getSelectedItem().toString().replaceAll("Semua","")+"%' ";
                if(!BangsalCari.getText().equals("")){
                    kmr=" kamar_inap.stts_pulang='-' and bangsal.nm_bangsal='"+BangsalCari.getText()+"' and reg_periksa.status_bayar like '%"+cmbStatusBayar.getSelectedItem().toString().replaceAll("Semua","")+"%' ";
                }
            }else if(R2.isSelected()==true){
                kmr=" kamar_inap.tgl_masuk between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' and reg_periksa.status_bayar like '%"+cmbStatusBayar.getSelectedItem().toString().replaceAll("Semua","")+"%' ";
                if(!BangsalCari.getText().equals("")){
                    kmr=" kamar_inap.tgl_masuk between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' and bangsal.nm_bangsal='"+BangsalCari.getText()+"' and reg_periksa.status_bayar like '%"+cmbStatusBayar.getSelectedItem().toString().replaceAll("Semua","")+"%' ";
                }
            }else if(R3.isSelected()==true){
                kmr=" kamar_inap.tgl_keluar between '"+Valid.SetTgl(DTPCari3.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari3.getSelectedItem()+"")+"' and reg_periksa.status_bayar like '%"+cmbStatusBayar.getSelectedItem().toString().replaceAll("Semua","")+"%' ";           
                if(!BangsalCari.getText().equals("")){
                    kmr=" kamar_inap.tgl_keluar between '"+Valid.SetTgl(DTPCari3.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari3.getSelectedItem()+"")+"' and bangsal.nm_bangsal='"+BangsalCari.getText()+"' and reg_periksa.status_bayar like '%"+cmbStatusBayar.getSelectedItem().toString().replaceAll("Semua","")+"%' ";
                }
            }

            key=kmr+" "+terbitsep;
            if(!TCari.getText().equals("")){
                key= kmr+"and (kamar_inap.no_rawat like '%"+TCari.getText().trim()+"%' or reg_periksa.no_rkm_medis like '%"+TCari.getText().trim()+"%' or pasien.nm_pasien like '%"+TCari.getText().trim()+"%' or "+
                   "concat(pasien.alamat,', ',kelurahan.nm_kel,', ',kecamatan.nm_kec,', ',kabupaten.nm_kab) like '%"+TCari.getText().trim()+"%' or kamar_inap.kd_kamar like '%"+TCari.getText().trim()+"%' or "+
                   "bangsal.nm_bangsal like '%"+TCari.getText().trim()+"%' or kamar_inap.diagnosa_awal like '%"+TCari.getText().trim()+"%' or kamar_inap.diagnosa_akhir like '%"+TCari.getText().trim()+"%' or "+
                   "kamar_inap.tgl_masuk like '%"+TCari.getText().trim()+"%' or dokter.nm_dokter like '%"+TCari.getText().trim()+"%' or kamar_inap.stts_pulang like '%"+TCari.getText().trim()+"%' or "+
                   "kamar_inap.tgl_keluar like '%"+TCari.getText().trim()+"%' or penjab.png_jawab like '%"+TCari.getText().trim()+"%' or pasien.agama like '%"+TCari.getText().trim()+"%') "+terbitsep;
            }

            Valid.tabelKosong(tabMode);
            new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() {
                    try{
                        ps=koneksi.prepareStatement(
                           "select pasien.no_tlp,kamar_inap.no_rawat,reg_periksa.no_rkm_medis,pasien.nm_pasien,pasien.alamat,reg_periksa.p_jawab,reg_periksa.hubunganpj,"+
                           "penjab.png_jawab,concat(kamar_inap.kd_kamar,' ',bangsal.nm_bangsal) as kamar,kamar_inap.trf_kamar,kamar_inap.diagnosa_awal,kamar_inap.diagnosa_akhir," +
                           "kamar_inap.tgl_masuk,kamar_inap.jam_masuk,if(kamar_inap.tgl_keluar='0000-00-00','',kamar_inap.tgl_keluar) as tgl_keluar,if(kamar_inap.jam_keluar='00:00:00','',kamar_inap.jam_keluar) as jam_keluar,"+
                           "kamar_inap.ttl_biaya,kamar_inap.stts_pulang,kamar_inap.lama,dokter.nm_dokter,kamar_inap.kd_kamar,reg_periksa.kd_pj,concat(reg_periksa.umurdaftar,' ',reg_periksa.sttsumur)as umur,reg_periksa.status_bayar, "+
                           "pasien.agama from kamar_inap inner join reg_periksa on kamar_inap.no_rawat=reg_periksa.no_rawat inner join pasien on reg_periksa.no_rkm_medis=pasien.no_rkm_medis "+
                           "inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal inner join kelurahan on pasien.kd_kel=kelurahan.kd_kel "+
                           "inner join kecamatan on pasien.kd_kec=kecamatan.kd_kec inner join kabupaten on pasien.kd_kab=kabupaten.kd_kab inner join dokter on reg_periksa.kd_dokter=dokter.kd_dokter "+
                           "inner join penjab on reg_periksa.kd_pj=penjab.kd_pj "+
                           (namadokter.equals("")?"where "+key+" "+order:"inner join dpjp_ranap on dpjp_ranap.no_rawat=reg_periksa.no_rawat where dpjp_ranap.kd_dokter='"+namadokter+"' and "+key+" "+order));
                        try {
                            rs=ps.executeQuery();
                            i=0;
                            while(rs.next()){
                                Object[] row = new Object[]{
                                    rs.getString("no_tlp"),rs.getString("no_rawat"),rs.getString("no_rkm_medis"),rs.getString("nm_pasien")+" ("+rs.getString("umur")+")",
                                    rs.getString("alamat"),rs.getString("p_jawab"),rs.getString("hubunganpj"),rs.getString("png_jawab"),
                                    rs.getString("kamar"),Valid.SetAngka(rs.getDouble("trf_kamar")),rs.getString("diagnosa_awal"),
                                    rs.getString("diagnosa_akhir"),rs.getString("tgl_masuk"),rs.getString("jam_masuk"),rs.getString("tgl_keluar"),
                                    rs.getString("jam_keluar"),Valid.SetAngka(rs.getDouble("ttl_biaya")),rs.getString("stts_pulang"),
                                    rs.getString("lama"),rs.getString("nm_dokter"),rs.getString("kd_kamar"),rs.getString("status_bayar"),rs.getString("agama")
                                };  
                                i++;
                                SwingUtilities.invokeLater(() -> tabMode.addRow(row));
                                psanak=koneksi.prepareStatement(
                                    "select pasien.no_rkm_medis,pasien.nm_pasien,ranap_gabung.no_rawat2,concat(reg_periksa.umurdaftar,' ',reg_periksa.sttsumur)as umur,pasien.no_peserta, "+
                                    "concat(pasien.alamatpj,', ',pasien.kelurahanpj,', ',pasien.kecamatanpj,', ',pasien.kabupatenpj) as alamat "+
                                    "from reg_periksa inner join pasien on pasien.no_rkm_medis=reg_periksa.no_rkm_medis "+
                                    "inner join ranap_gabung on ranap_gabung.no_rawat2=reg_periksa.no_rawat where ranap_gabung.no_rawat=?");            
                                try {
                                    psanak.setString(1,rs.getString(1));
                                    rs2=psanak.executeQuery();
                                    if(rs2.next()){
                                        Object[] row2 = new Object[]{
                                            "",rs2.getString("no_rkm_medis"),rs2.getString("nm_pasien")+" ("+rs2.getString("umur")+")",
                                            rs.getString("alamat"),rs.getString("p_jawab"),rs.getString("hubunganpj"),rs.getString("png_jawab"),
                                            rs.getString("kamar"),Valid.SetAngka(rs.getDouble("trf_kamar")*(persenbayi/100)),"",
                                            "",rs.getString("tgl_masuk"),rs.getString("jam_masuk"),rs.getString("tgl_keluar"),
                                            rs.getString("jam_keluar"),Valid.SetAngka(rs.getDouble("ttl_biaya")*(persenbayi/100)),rs.getString("stts_pulang"),
                                            rs.getString("lama"),rs.getString("nm_dokter"),rs.getString("kd_kamar"),rs.getString("status_bayar")
                                        };
                                        i++;
                                        SwingUtilities.invokeLater(() -> tabMode.addRow(row2));
                                    }
                                }catch(Exception ex){
                                    System.out.println("Notifikasi : "+ex);
                                }finally{
                                      if(rs2 != null){
                                          rs2.close();
                                      }
                                      if(psanak != null){
                                          psanak.close();
                                      }
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Notifikasi : "+e);
                        } finally{
                            if(rs != null){
                                rs.close();
                            }
                            if(ps != null){
                                ps.close();
                            }
                        }
                    }catch(Exception e){
                        System.out.println("Notifikasi : "+e);
                    } 
                    return null;
                }

                @Override
                protected void done() {
                    LCount.setText(""+i);
                    ceksukses = false;
                }
            }.execute(); 
        }
    }

        private void updateHari(){
        if((R1.isSelected()==true)&&(akses.getstatus()==false)){
            for(i=0;i<tbKamIn.getRowCount();i++){
                if(tbKamIn.getValueAt(i,13).toString().equals("")){        
                    if(hariawal.equals("Yes")){
                        Sequel.mengedit(" kamar_inap "," no_rawat='"+tbKamIn.getValueAt(i,1).toString()+"' and "+
                            " kd_kamar='"+Sequel.cariIsi("select kd_kamar from kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal where concat(kamar.kd_kamar,' ',bangsal.nm_bangsal)=? ",tbKamIn.getValueAt(i,8).toString())+"' "+
                            " and tgl_masuk='"+tbKamIn.getValueAt(i,12).toString()+"' and jam_masuk='"+tbKamIn.getValueAt(i,13).toString()+"'",
                            " lama=if(to_days(NOW())-to_days(concat(tgl_masuk,' ',jam_masuk))=0,if(time_to_sec(NOW())-time_to_sec(concat(tgl_masuk,' ',jam_masuk))>(3600*"+lama+"),1,0),to_days(NOW())-to_days(concat(tgl_masuk,' ',jam_masuk)))+1,"+
                            " ttl_biaya=(if(to_days(NOW())-to_days(concat(tgl_masuk,' ',jam_masuk))=0,if(time_to_sec(NOW())-time_to_sec(concat(tgl_masuk,' ',jam_masuk))>(3600*"+lama+"),1,0),to_days(NOW())-to_days(concat(tgl_masuk,' ',jam_masuk)))+1)*trf_kamar");                
                    }else{
                        Sequel.mengedit(" kamar_inap "," no_rawat='"+tbKamIn.getValueAt(i,1).toString()+"' and "+
                            " kd_kamar='"+Sequel.cariIsi("select kd_kamar from kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal where concat(kamar.kd_kamar,' ',bangsal.nm_bangsal)=? ",tbKamIn.getValueAt(i,8).toString())+"' "+
                            " and tgl_masuk='"+tbKamIn.getValueAt(i,12).toString()+"' and jam_masuk='"+tbKamIn.getValueAt(i,13).toString()+"'",
                            " lama=if(to_days(NOW())-to_days(concat(tgl_masuk,' ',jam_masuk))=0,if(time_to_sec(NOW())-time_to_sec(concat(tgl_masuk,' ',jam_masuk))>(3600*"+lama+"),1,0),to_days(NOW())-to_days(concat(tgl_masuk,' ',jam_masuk))),"+
                            " ttl_biaya=if(to_days(NOW())-to_days(concat(tgl_masuk,' ',jam_masuk))=0,if(time_to_sec(NOW())-time_to_sec(concat(tgl_masuk,' ',jam_masuk))>(3600*"+lama+"),1,0),to_days(NOW())-to_days(concat(tgl_masuk,' ',jam_masuk)))*trf_kamar");
                    }         
                }
            }
        }
        tampil();
    }
    

}
