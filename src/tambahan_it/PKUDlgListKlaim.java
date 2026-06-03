package tambahan_it;

import integration_idrg.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.batasInput;
import fungsi.koneksiDB;
import java.awt.Dimension;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import fungsi.sekuel;
import fungsi.validasi;
import fungsi.akses;
import java.awt.Cursor;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 *
 * @author dosen
 */
public final class PKUDlgListKlaim extends javax.swing.JDialog {

    private final DefaultTableModel TabModePasienRalan, TabModePasienRanap, TabModePoli;
    private validasi Valid = new validasi();
    private sekuel Sequel = new sekuel();
    private Connection koneksi = koneksiDB.condb();
    private PreparedStatement ps, ps2, ps3;
    private ResultSet rs, rs2, rs3;
    private int i = 0;
    private String sql = "", URL = "", link = "", utc = "", idrs = "", requestJson = "";
    private ApiIntegrationIDRG api = new ApiIntegrationIDRG();
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root, root2, root3;
    private JsonNode nameNode;
    private JsonNode response, responsename, response3;
    private static final Properties prop = new Properties();

    /**
     * Creates new form DlgKamar
     *
     * @param parent
     * @param modal
     */
    public PKUDlgListKlaim(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(10, 2);
        setSize(1328, 674);
        TabModePoli = new DefaultTableModel(null, new Object[]{
            "Kode Unit", "Nama Unit"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class

            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbUnitLayanan.setModel(TabModePoli);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbUnitLayanan.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbUnitLayanan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 2; i++) {
            TableColumn column = tbUnitLayanan.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(90);
            } else if (i == 1) {
                column.setPreferredWidth(300);
            }

        }
        tbUnitLayanan.setDefaultRenderer(Object.class, new WarnaTable());
        Object[] columns = new String[]{
            "P", "No Rawat[1]", "No RM[2]", "Nama Pasien[3]", "Unit[4]", "Nama Dokter[5]", "No SEP[6]",
            "Tgl SEP[7]", "Tgl. Regis[8]", "Tgl Pulang[9]", "Status Verif[10]", "Tgl. Verif[11]", "Status Koding[12]",
            "Tgl. Koding[13]", "Kirim E-Klaim[14]", "Tgl. Kirim E-Klaim[15]", "Status Download[16]", "Tgl. Download[17]"};
        TabModePasienRalan = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };

        tbListPasienRajal.setModel(TabModePasienRalan);
        tbListPasienRajal.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbListPasienRajal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < 18; i++) {
            TableColumn column = tbListPasienRajal.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setPreferredWidth(110);
            } else if (i == 2) {
                column.setPreferredWidth(60);
            } else if (i == 3) {
                column.setPreferredWidth(300);
            } else if (i == 4) {
                column.setPreferredWidth(150);
            } else if (i == 5) {
                column.setPreferredWidth(200);
            } else if (i == 6) {
                column.setPreferredWidth(125);
            } else if (i == 7 || i == 8 || i == 9) {
                column.setPreferredWidth(80);
            } else if (i == 11 || i == 13 || i == 15 || i == 17) {
                column.setPreferredWidth(120);
            } else if (i == 10 || i == 12 || i == 14 || i == 16) {
                column.setPreferredWidth(90);
            } else {
                column.setPreferredWidth(60);
            }
        }
        tbListPasienRajal.setDefaultRenderer(Object.class, new WarnaTableIDRGClaim());

        Object[] columnsRanap = new String[]{"P", "No Rawat[1]", "No RM[2]", "Nama Pasien[3]", "Unit[4]", "No SEP[5]",
            "Tgl SEP[6]", "Tgl. Regis[7]", "Tgl Pulang[8]", "Status Verif[9]", "Tgl. Verif[10]", "Status Koding[11]",
            "Tgl. Koding[12]", "Kirim E-Klaim[13]", "Tgl. Kirim E-Klaim[14]", "Status Download[15]", "Tgl. Download[16]",
            "Individual[17]", "SEP[18]", "Resume[19]", "Billing[20]", "Laborat[21]", "Radiologi[22]", "USG[23]"};
        TabModePasienRanap = new DefaultTableModel(null, columnsRanap) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class,
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class,
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };

        tbListPasienRanap.setModel(TabModePasienRanap);
        tbListPasienRanap.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbListPasienRanap.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (int i = 0; i < 24; i++) {
            TableColumn column = tbListPasienRanap.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setPreferredWidth(110);
            } else if (i == 2) {
                column.setPreferredWidth(60);
            } else if (i == 3) {
                column.setPreferredWidth(300);
            } else if (i == 4) {
                column.setPreferredWidth(150);
            } else if (i == 5) {
                column.setPreferredWidth(125);
            } else if (i == 6 || i == 7 || i == 8) {
                column.setPreferredWidth(80);
            } else if (i == 10 || i == 12 || i == 14 || i == 16) {
                column.setPreferredWidth(120);
            } else if (i == 9 || i == 11 || i == 13 || i == 15) {
                column.setPreferredWidth(90);
            } else {
                column.setPreferredWidth(60);
            }
        }

        tbListPasienRanap.setDefaultRenderer(Object.class, new WarnaTableIDRGClaim());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        KeteranganWarna = new javax.swing.JDialog();
        internalFrame13 = new widget.InternalFrame();
        panelBiasa8 = new widget.PanelBiasa();
        BtnCloseInpindah2 = new widget.Button();
        FormInput1 = new widget.PanelBiasa();
        jTextField5 = new javax.swing.JTextField();
        jLabel67 = new widget.Label();
        jTextField6 = new javax.swing.JTextField();
        jTextField7 = new javax.swing.JTextField();
        jLabel44 = new widget.Label();
        jTextField4 = new javax.swing.JTextField();
        jLabel46 = new widget.Label();
        jTextField8 = new javax.swing.JTextField();
        jLabel49 = new widget.Label();
        jTextField9 = new javax.swing.JTextField();
        jLabel50 = new widget.Label();
        UnitLayanan = new javax.swing.JDialog();
        internalFrame14 = new widget.InternalFrame();
        panelBiasa9 = new widget.PanelBiasa();
        BtnCloseInpindah3 = new widget.Button();
        FormInput2 = new widget.PanelBiasa();
        Scroll = new widget.ScrollPane();
        tbUnitLayanan = new widget.Table();
        DlgCatatanPerbaikan = new javax.swing.JDialog();
        internalFrame15 = new widget.InternalFrame();
        panelBiasa10 = new widget.PanelBiasa();
        BtnCloseInpindah4 = new widget.Button();
        FormInput3 = new widget.PanelBiasa();
        scrollPane4 = new widget.ScrollPane();
        catatanPerbaikan1 = new widget.TextArea();
        PopupRalan = new javax.swing.JPopupMenu();
        ppDetailKlaim = new javax.swing.JMenuItem();
        MnPilihCeklis = new javax.swing.JMenu();
        ppPilihSemua = new javax.swing.JMenuItem();
        ppBersihkan = new javax.swing.JMenuItem();
        TNoRw = new widget.TextBox();
        PopupRanap = new javax.swing.JPopupMenu();
        ppDetailKlaimRanap = new javax.swing.JMenuItem();
        MnPilihCeklisRanap = new javax.swing.JMenu();
        ppPilihSemua2 = new javax.swing.JMenuItem();
        ppBersihkan2 = new javax.swing.JMenuItem();
        Popup1 = new javax.swing.JPopupMenu();
        ppKodingBerkas1 = new javax.swing.JMenuItem();
        ppUpdateDataPasienEklaim1 = new javax.swing.JMenuItem();
        MnPilihCeklis1 = new javax.swing.JMenu();
        ppPilihSemua1 = new javax.swing.JMenuItem();
        ppBersihkan1 = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        TabRawat = new javax.swing.JTabbedPane();
        Scroll2 = new widget.ScrollPane();
        tbListPasienRajal = new widget.Table();
        Scroll1 = new widget.ScrollPane();
        tbListPasienRanap = new widget.Table();
        panelisi3 = new widget.panelisi();
        panelisi6 = new widget.panelisi();
        jLabel7 = new widget.Label();
        DTPTglAwal = new widget.Tanggal();
        jLabel8 = new widget.Label();
        DTPTglAkhir = new widget.Tanggal();
        jLabel11 = new widget.Label();
        cmbHlm = new widget.ComboBox();
        chkAutoRefresh = new widget.CekBox();
        jLabel3 = new widget.Label();
        label9 = new widget.Label();
        TCariKunjungan = new widget.TextBox();
        BtnCariPasien = new widget.Button();
        jLabel16 = new widget.Label();
        kdPoliView = new widget.TextBox();
        nmPoliView = new widget.TextBox();
        BtnSeek4 = new widget.Button();
        panelisi4 = new widget.panelisi();
        label11 = new widget.Label();
        SepTerbit = new widget.Label();
        label10 = new widget.Label();
        LCount = new widget.Label();
        label21 = new widget.Label();
        timeslaps = new widget.Label();
        BtnKetWarna = new widget.Button();
        BtnAll = new widget.Button();
        cmbHlm1 = new widget.ComboBox();
        panelisi5 = new widget.panelisi();
        BtnKeluar1 = new widget.Button();

        KeteranganWarna.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        KeteranganWarna.setName("KeteranganWarna"); // NOI18N
        KeteranganWarna.setUndecorated(true);
        KeteranganWarna.setResizable(false);
        KeteranganWarna.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                KeteranganWarnaWindowActivated(evt);
            }
        });

        internalFrame13.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Keterangan Warna ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame13.setAlignmentX(1.0F);
        internalFrame13.setAlignmentY(1.0F);
        internalFrame13.setName("internalFrame13"); // NOI18N
        internalFrame13.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame13.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame13.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa8.setName("panelBiasa8"); // NOI18N
        panelBiasa8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BtnCloseInpindah2.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah2.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah2.setMnemonic('U');
        BtnCloseInpindah2.setText("Keluar");
        BtnCloseInpindah2.setToolTipText("Alt+U");
        BtnCloseInpindah2.setName("BtnCloseInpindah2"); // NOI18N
        BtnCloseInpindah2.setOpaque(true);
        BtnCloseInpindah2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah2ActionPerformed(evt);
            }
        });
        BtnCloseInpindah2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah2KeyPressed(evt);
            }
        });
        panelBiasa8.add(BtnCloseInpindah2);

        internalFrame13.add(panelBiasa8, java.awt.BorderLayout.PAGE_END);

        FormInput1.setBorder(null);
        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput1.setLayout(null);

        jTextField5.setEditable(false);
        jTextField5.setToolTipText("");
        jTextField5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jTextField5.setName("jTextField5"); // NOI18N
        jTextField5.setPreferredSize(new java.awt.Dimension(60, 26));
        jTextField5.setRequestFocusEnabled(false);
        FormInput1.add(jTextField5);
        jTextField5.setBounds(50, 20, 40, 20);

        jLabel67.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel67.setText("List Pelayanan Pasien");
        jLabel67.setName("jLabel67"); // NOI18N
        FormInput1.add(jLabel67);
        jLabel67.setBounds(100, 20, 350, 23);

        jTextField6.setEditable(false);
        jTextField6.setBackground(new java.awt.Color(247, 255, 243));
        jTextField6.setToolTipText("");
        jTextField6.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        jTextField6.setName("jTextField6"); // NOI18N
        jTextField6.setPreferredSize(new java.awt.Dimension(60, 26));
        jTextField6.setRequestFocusEnabled(false);
        FormInput1.add(jTextField6);
        jTextField6.setBounds(10, 20, 40, 20);

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(255, 153, 51));
        jTextField7.setToolTipText("");
        jTextField7.setName("jTextField7"); // NOI18N
        jTextField7.setPreferredSize(new java.awt.Dimension(60, 26));
        jTextField7.setRequestFocusEnabled(false);
        FormInput1.add(jTextField7);
        jTextField7.setBounds(10, 50, 80, 20);

        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel44.setText("Sudah diverifikasi");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput1.add(jLabel44);
        jLabel44.setBounds(100, 50, 290, 23);

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(0, 102, 0));
        jTextField4.setToolTipText("");
        jTextField4.setName("jTextField4"); // NOI18N
        jTextField4.setPreferredSize(new java.awt.Dimension(60, 26));
        jTextField4.setRequestFocusEnabled(false);
        FormInput1.add(jTextField4);
        jTextField4.setBounds(10, 80, 80, 20);

        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel46.setText("Sudah diverifikasi & Sudah dikoding");
        jLabel46.setName("jLabel46"); // NOI18N
        FormInput1.add(jLabel46);
        jLabel46.setBounds(100, 80, 320, 23);

        jTextField8.setEditable(false);
        jTextField8.setBackground(new java.awt.Color(153, 255, 51));
        jTextField8.setToolTipText("");
        jTextField8.setName("jTextField8"); // NOI18N
        jTextField8.setPreferredSize(new java.awt.Dimension(60, 26));
        jTextField8.setRequestFocusEnabled(false);
        FormInput1.add(jTextField8);
        jTextField8.setBounds(10, 110, 80, 20);

        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel49.setText("Sudah diverifikasi & Sudah dikoding & Sudah dikirim ke E-Klaim");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput1.add(jLabel49);
        jLabel49.setBounds(100, 110, 320, 23);

        jTextField9.setEditable(false);
        jTextField9.setBackground(new java.awt.Color(255, 153, 255));
        jTextField9.setToolTipText("");
        jTextField9.setName("jTextField9"); // NOI18N
        jTextField9.setPreferredSize(new java.awt.Dimension(60, 26));
        jTextField9.setRequestFocusEnabled(false);
        FormInput1.add(jTextField9);
        jTextField9.setBounds(10, 140, 80, 20);

        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel50.setText("Sudah Download");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput1.add(jLabel50);
        jLabel50.setBounds(100, 140, 320, 23);

        internalFrame13.add(FormInput1, java.awt.BorderLayout.CENTER);

        KeteranganWarna.getContentPane().add(internalFrame13, java.awt.BorderLayout.CENTER);

        UnitLayanan.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        UnitLayanan.setName("UnitLayanan"); // NOI18N
        UnitLayanan.setUndecorated(true);
        UnitLayanan.setResizable(false);
        UnitLayanan.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                UnitLayananWindowActivated(evt);
            }
        });

        internalFrame14.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Unit Layanan ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame14.setAlignmentX(1.0F);
        internalFrame14.setAlignmentY(1.0F);
        internalFrame14.setName("internalFrame14"); // NOI18N
        internalFrame14.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame14.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame14.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa9.setName("panelBiasa9"); // NOI18N
        panelBiasa9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BtnCloseInpindah3.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah3.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah3.setMnemonic('U');
        BtnCloseInpindah3.setText("Keluar");
        BtnCloseInpindah3.setToolTipText("Alt+U");
        BtnCloseInpindah3.setName("BtnCloseInpindah3"); // NOI18N
        BtnCloseInpindah3.setOpaque(true);
        BtnCloseInpindah3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah3ActionPerformed(evt);
            }
        });
        BtnCloseInpindah3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah3KeyPressed(evt);
            }
        });
        panelBiasa9.add(BtnCloseInpindah3);

        internalFrame14.add(panelBiasa9, java.awt.BorderLayout.PAGE_END);

        FormInput2.setBorder(null);
        FormInput2.setName("FormInput2"); // NOI18N
        FormInput2.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput2.setLayout(new java.awt.BorderLayout());

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbUnitLayanan.setAutoCreateRowSorter(true);
        tbUnitLayanan.setName("tbUnitLayanan"); // NOI18N
        tbUnitLayanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbUnitLayananMouseClicked(evt);
            }
        });
        tbUnitLayanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbUnitLayananKeyPressed(evt);
            }
        });
        Scroll.setViewportView(tbUnitLayanan);

        FormInput2.add(Scroll, java.awt.BorderLayout.CENTER);

        internalFrame14.add(FormInput2, java.awt.BorderLayout.CENTER);

        UnitLayanan.getContentPane().add(internalFrame14, java.awt.BorderLayout.CENTER);

        DlgCatatanPerbaikan.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DlgCatatanPerbaikan.setName("DlgCatatanPerbaikan"); // NOI18N
        DlgCatatanPerbaikan.setUndecorated(true);
        DlgCatatanPerbaikan.setResizable(false);
        DlgCatatanPerbaikan.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                DlgCatatanPerbaikanWindowActivated(evt);
            }
        });

        internalFrame15.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Catatan Perbaikan ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(0, 51, 102))); // NOI18N
        internalFrame15.setAlignmentX(1.0F);
        internalFrame15.setAlignmentY(1.0F);
        internalFrame15.setName("internalFrame15"); // NOI18N
        internalFrame15.setWarnaAtas(new java.awt.Color(255, 255, 0));
        internalFrame15.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame15.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa10.setName("panelBiasa10"); // NOI18N
        panelBiasa10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BtnCloseInpindah4.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah4.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah4.setMnemonic('U');
        BtnCloseInpindah4.setText("Keluar");
        BtnCloseInpindah4.setToolTipText("Alt+U");
        BtnCloseInpindah4.setName("BtnCloseInpindah4"); // NOI18N
        BtnCloseInpindah4.setOpaque(true);
        BtnCloseInpindah4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah4ActionPerformed(evt);
            }
        });
        BtnCloseInpindah4.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah4KeyPressed(evt);
            }
        });
        panelBiasa10.add(BtnCloseInpindah4);

        internalFrame15.add(panelBiasa10, java.awt.BorderLayout.PAGE_END);

        FormInput3.setBorder(null);
        FormInput3.setName("FormInput3"); // NOI18N
        FormInput3.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput3.setLayout(new java.awt.BorderLayout());

        scrollPane4.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        scrollPane4.setForeground(new java.awt.Color(0, 102, 0));
        scrollPane4.setName("scrollPane4"); // NOI18N

        catatanPerbaikan1.setBorder(null);
        catatanPerbaikan1.setColumns(20);
        catatanPerbaikan1.setRows(5);
        catatanPerbaikan1.setName("catatanPerbaikan1"); // NOI18N
        scrollPane4.setViewportView(catatanPerbaikan1);

        FormInput3.add(scrollPane4, java.awt.BorderLayout.CENTER);

        internalFrame15.add(FormInput3, java.awt.BorderLayout.CENTER);

        DlgCatatanPerbaikan.getContentPane().add(internalFrame15, java.awt.BorderLayout.CENTER);

        PopupRalan.setName("PopupRalan"); // NOI18N

        ppDetailKlaim.setBackground(new java.awt.Color(255, 255, 254));
        ppDetailKlaim.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppDetailKlaim.setForeground(new java.awt.Color(50, 50, 50));
        ppDetailKlaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppDetailKlaim.setText("Detail Klaim");
        ppDetailKlaim.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppDetailKlaim.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppDetailKlaim.setName("ppDetailKlaim"); // NOI18N
        ppDetailKlaim.setPreferredSize(new java.awt.Dimension(250, 25));
        ppDetailKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppDetailKlaimActionPerformed(evt);
            }
        });
        PopupRalan.add(ppDetailKlaim);

        MnPilihCeklis.setBackground(new java.awt.Color(250, 255, 245));
        MnPilihCeklis.setForeground(new java.awt.Color(70, 70, 70));
        MnPilihCeklis.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnPilihCeklis.setText("Pilihan Ceklis");
        MnPilihCeklis.setToolTipText("");
        MnPilihCeklis.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnPilihCeklis.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnPilihCeklis.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnPilihCeklis.setName("MnPilihCeklis"); // NOI18N
        MnPilihCeklis.setPreferredSize(new java.awt.Dimension(310, 26));

        ppPilihSemua.setBackground(new java.awt.Color(255, 255, 254));
        ppPilihSemua.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppPilihSemua.setForeground(new java.awt.Color(50, 50, 50));
        ppPilihSemua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppPilihSemua.setText("Centang Semua");
        ppPilihSemua.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppPilihSemua.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppPilihSemua.setName("ppPilihSemua"); // NOI18N
        ppPilihSemua.setPreferredSize(new java.awt.Dimension(250, 25));
        ppPilihSemua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppPilihSemuaActionPerformed(evt);
            }
        });
        MnPilihCeklis.add(ppPilihSemua);

        ppBersihkan.setBackground(new java.awt.Color(255, 255, 254));
        ppBersihkan.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppBersihkan.setText("Hilangkan Centang/Tindakan Terpilih");
        ppBersihkan.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan.setName("ppBersihkan"); // NOI18N
        ppBersihkan.setPreferredSize(new java.awt.Dimension(250, 25));
        ppBersihkan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkanActionPerformed(evt);
            }
        });
        MnPilihCeklis.add(ppBersihkan);

        PopupRalan.add(MnPilihCeklis);

        TNoRw.setHighlighter(null);
        TNoRw.setName("TNoRw"); // NOI18N

        PopupRanap.setName("PopupRanap"); // NOI18N

        ppDetailKlaimRanap.setBackground(new java.awt.Color(255, 255, 254));
        ppDetailKlaimRanap.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppDetailKlaimRanap.setForeground(new java.awt.Color(50, 50, 50));
        ppDetailKlaimRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppDetailKlaimRanap.setText("Detail Klaim");
        ppDetailKlaimRanap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppDetailKlaimRanap.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppDetailKlaimRanap.setName("ppDetailKlaimRanap"); // NOI18N
        ppDetailKlaimRanap.setPreferredSize(new java.awt.Dimension(250, 25));
        ppDetailKlaimRanap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppDetailKlaimRanapActionPerformed(evt);
            }
        });
        PopupRanap.add(ppDetailKlaimRanap);

        MnPilihCeklisRanap.setBackground(new java.awt.Color(250, 255, 245));
        MnPilihCeklisRanap.setForeground(new java.awt.Color(70, 70, 70));
        MnPilihCeklisRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnPilihCeklisRanap.setText("Pilihan Ceklis");
        MnPilihCeklisRanap.setToolTipText("");
        MnPilihCeklisRanap.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnPilihCeklisRanap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnPilihCeklisRanap.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnPilihCeklisRanap.setName("MnPilihCeklisRanap"); // NOI18N
        MnPilihCeklisRanap.setPreferredSize(new java.awt.Dimension(310, 26));

        ppPilihSemua2.setBackground(new java.awt.Color(255, 255, 254));
        ppPilihSemua2.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppPilihSemua2.setForeground(new java.awt.Color(50, 50, 50));
        ppPilihSemua2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppPilihSemua2.setText("Centang Semua");
        ppPilihSemua2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppPilihSemua2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppPilihSemua2.setName("ppPilihSemua2"); // NOI18N
        ppPilihSemua2.setPreferredSize(new java.awt.Dimension(250, 25));
        ppPilihSemua2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppPilihSemua2ActionPerformed(evt);
            }
        });
        MnPilihCeklisRanap.add(ppPilihSemua2);

        ppBersihkan2.setBackground(new java.awt.Color(255, 255, 254));
        ppBersihkan2.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan2.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkan2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppBersihkan2.setText("Hilangkan Centang/Tindakan Terpilih");
        ppBersihkan2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan2.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan2.setName("ppBersihkan2"); // NOI18N
        ppBersihkan2.setPreferredSize(new java.awt.Dimension(250, 25));
        ppBersihkan2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkan2ActionPerformed(evt);
            }
        });
        MnPilihCeklisRanap.add(ppBersihkan2);

        PopupRanap.add(MnPilihCeklisRanap);

        Popup1.setName("Popup1"); // NOI18N

        ppKodingBerkas1.setBackground(new java.awt.Color(255, 255, 254));
        ppKodingBerkas1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppKodingBerkas1.setForeground(new java.awt.Color(50, 50, 50));
        ppKodingBerkas1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppKodingBerkas1.setText("Detail Berkas");
        ppKodingBerkas1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppKodingBerkas1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppKodingBerkas1.setName("ppKodingBerkas1"); // NOI18N
        ppKodingBerkas1.setPreferredSize(new java.awt.Dimension(250, 25));
        ppKodingBerkas1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppKodingBerkas1ActionPerformed(evt);
            }
        });
        Popup1.add(ppKodingBerkas1);

        ppUpdateDataPasienEklaim1.setBackground(new java.awt.Color(255, 255, 254));
        ppUpdateDataPasienEklaim1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppUpdateDataPasienEklaim1.setForeground(new java.awt.Color(50, 50, 50));
        ppUpdateDataPasienEklaim1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        ppUpdateDataPasienEklaim1.setText("Update Data Pasien Eklaim");
        ppUpdateDataPasienEklaim1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppUpdateDataPasienEklaim1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppUpdateDataPasienEklaim1.setName("ppUpdateDataPasienEklaim1"); // NOI18N
        ppUpdateDataPasienEklaim1.setPreferredSize(new java.awt.Dimension(250, 25));
        ppUpdateDataPasienEklaim1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppUpdateDataPasienEklaim1ActionPerformed(evt);
            }
        });
        Popup1.add(ppUpdateDataPasienEklaim1);

        MnPilihCeklis1.setBackground(new java.awt.Color(250, 255, 245));
        MnPilihCeklis1.setForeground(new java.awt.Color(70, 70, 70));
        MnPilihCeklis1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnPilihCeklis1.setText("Pilihan Ceklis");
        MnPilihCeklis1.setToolTipText("");
        MnPilihCeklis1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnPilihCeklis1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnPilihCeklis1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnPilihCeklis1.setName("MnPilihCeklis1"); // NOI18N
        MnPilihCeklis1.setPreferredSize(new java.awt.Dimension(310, 26));

        ppPilihSemua1.setBackground(new java.awt.Color(255, 255, 254));
        ppPilihSemua1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppPilihSemua1.setForeground(new java.awt.Color(50, 50, 50));
        ppPilihSemua1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppPilihSemua1.setText("Centang Semua");
        ppPilihSemua1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppPilihSemua1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppPilihSemua1.setName("ppPilihSemua1"); // NOI18N
        ppPilihSemua1.setPreferredSize(new java.awt.Dimension(250, 25));
        ppPilihSemua1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppPilihSemua1ActionPerformed(evt);
            }
        });
        MnPilihCeklis1.add(ppPilihSemua1);

        ppBersihkan1.setBackground(new java.awt.Color(255, 255, 254));
        ppBersihkan1.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBersihkan1.setForeground(new java.awt.Color(50, 50, 50));
        ppBersihkan1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppBersihkan1.setText("Hilangkan Centang/Tindakan Terpilih");
        ppBersihkan1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBersihkan1.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBersihkan1.setName("ppBersihkan1"); // NOI18N
        ppBersihkan1.setPreferredSize(new java.awt.Dimension(250, 25));
        ppBersihkan1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBersihkan1ActionPerformed(evt);
            }
        });
        MnPilihCeklis1.add(ppBersihkan1);

        Popup1.add(MnPilihCeklis1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setIconImage(null);
        setIconImages(null);
        setModalityType(null);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Pencarian Data Pasien JKN ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setPreferredSize(new java.awt.Dimension(700, 158));
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        TabRawat.setBackground(new java.awt.Color(255, 255, 254));
        TabRawat.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(241, 246, 236)));
        TabRawat.setForeground(new java.awt.Color(50, 50, 50));
        TabRawat.setFocusCycleRoot(true);
        TabRawat.setName("TabRawat"); // NOI18N
        TabRawat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                TabRawatMouseClicked(evt);
            }
        });

        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        tbListPasienRajal.setAutoCreateRowSorter(true);
        tbListPasienRajal.setToolTipText("");
        tbListPasienRajal.setComponentPopupMenu(PopupRalan);
        tbListPasienRajal.setName("tbListPasienRajal"); // NOI18N
        tbListPasienRajal.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListPasienRajalMouseClicked(evt);
            }
        });
        tbListPasienRajal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbListPasienRajalKeyPressed(evt);
            }
        });
        Scroll2.setViewportView(tbListPasienRajal);

        TabRawat.addTab("Rawat Jalan", Scroll2);

        Scroll1.setName("Scroll1"); // NOI18N
        Scroll1.setOpaque(true);

        tbListPasienRanap.setAutoCreateRowSorter(true);
        tbListPasienRanap.setToolTipText("");
        tbListPasienRanap.setComponentPopupMenu(PopupRanap);
        tbListPasienRanap.setName("tbListPasienRanap"); // NOI18N
        tbListPasienRanap.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbListPasienRanapMouseClicked(evt);
            }
        });
        tbListPasienRanap.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tbListPasienRanapKeyPressed(evt);
            }
        });
        Scroll1.setViewportView(tbListPasienRanap);

        TabRawat.addTab("Rawat Inap", Scroll1);

        internalFrame1.add(TabRawat, java.awt.BorderLayout.CENTER);

        panelisi3.setName("panelisi3"); // NOI18N
        panelisi3.setPreferredSize(new java.awt.Dimension(100, 100));
        panelisi3.setLayout(new java.awt.BorderLayout());

        panelisi6.setName("panelisi6"); // NOI18N
        panelisi6.setPreferredSize(new java.awt.Dimension(100, 35));
        panelisi6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 2));

        jLabel7.setText("Tanggal :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(46, 23));
        panelisi6.add(jLabel7);

        DTPTglAwal.setForeground(new java.awt.Color(50, 70, 50));
        DTPTglAwal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "26-05-2026" }));
        DTPTglAwal.setDisplayFormat("dd-MM-yyyy");
        DTPTglAwal.setName("DTPTglAwal"); // NOI18N
        DTPTglAwal.setOpaque(false);
        DTPTglAwal.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPTglAwal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPTglAwalKeyPressed(evt);
            }
        });
        panelisi6.add(DTPTglAwal);

        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("s/d");
        jLabel8.setName("jLabel8"); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(30, 23));
        panelisi6.add(jLabel8);

        DTPTglAkhir.setForeground(new java.awt.Color(50, 70, 50));
        DTPTglAkhir.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "26-05-2026" }));
        DTPTglAkhir.setDisplayFormat("dd-MM-yyyy");
        DTPTglAkhir.setName("DTPTglAkhir"); // NOI18N
        DTPTglAkhir.setOpaque(false);
        DTPTglAkhir.setPreferredSize(new java.awt.Dimension(100, 23));
        DTPTglAkhir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                DTPTglAkhirKeyPressed(evt);
            }
        });
        panelisi6.add(DTPTglAkhir);

        jLabel11.setText("Limit Data :");
        jLabel11.setName("jLabel11"); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi6.add(jLabel11);

        cmbHlm.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "50", "100", "200", "300", "400", "500", "1000", "Semua" }));
        cmbHlm.setName("cmbHlm"); // NOI18N
        cmbHlm.setPreferredSize(new java.awt.Dimension(75, 23));
        panelisi6.add(cmbHlm);

        chkAutoRefresh.setBorder(null);
        chkAutoRefresh.setSelected(true);
        chkAutoRefresh.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chkAutoRefresh.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chkAutoRefresh.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        chkAutoRefresh.setName("chkAutoRefresh"); // NOI18N
        panelisi6.add(chkAutoRefresh);

        jLabel3.setText("Auto Refresh");
        jLabel3.setName("jLabel3"); // NOI18N
        panelisi6.add(jLabel3);

        label9.setText("Key Word :");
        label9.setName("label9"); // NOI18N
        label9.setPreferredSize(new java.awt.Dimension(80, 23));
        panelisi6.add(label9);

        TCariKunjungan.setName("TCariKunjungan"); // NOI18N
        TCariKunjungan.setPreferredSize(new java.awt.Dimension(200, 23));
        TCariKunjungan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariKunjunganKeyPressed(evt);
            }
        });
        panelisi6.add(TCariKunjungan);

        BtnCariPasien.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCariPasien.setMnemonic('1');
        BtnCariPasien.setToolTipText("Alt+1");
        BtnCariPasien.setName("BtnCariPasien"); // NOI18N
        BtnCariPasien.setPreferredSize(new java.awt.Dimension(46, 23));
        BtnCariPasien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCariPasienActionPerformed(evt);
            }
        });
        BtnCariPasien.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCariPasienKeyPressed(evt);
            }
        });
        panelisi6.add(BtnCariPasien);

        jLabel16.setText("Unit :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(35, 23));
        panelisi6.add(jLabel16);

        kdPoliView.setName("kdPoliView"); // NOI18N
        kdPoliView.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi6.add(kdPoliView);

        nmPoliView.setEditable(false);
        nmPoliView.setName("nmPoliView"); // NOI18N
        nmPoliView.setPreferredSize(new java.awt.Dimension(200, 23));
        panelisi6.add(nmPoliView);

        BtnSeek4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeek4.setMnemonic('5');
        BtnSeek4.setToolTipText("ALt+5");
        BtnSeek4.setName("BtnSeek4"); // NOI18N
        BtnSeek4.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeek4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeek4ActionPerformed(evt);
            }
        });
        panelisi6.add(BtnSeek4);

        panelisi3.add(panelisi6, java.awt.BorderLayout.PAGE_START);

        panelisi4.setName("panelisi4"); // NOI18N
        panelisi4.setPreferredSize(new java.awt.Dimension(100, 35));
        panelisi4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 2));

        label11.setText("SEP Terbit :");
        label11.setName("label11"); // NOI18N
        label11.setPreferredSize(new java.awt.Dimension(80, 23));
        panelisi4.add(label11);

        SepTerbit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SepTerbit.setText("0");
        SepTerbit.setName("SepTerbit"); // NOI18N
        SepTerbit.setPreferredSize(new java.awt.Dimension(120, 23));
        panelisi4.add(SepTerbit);

        label10.setText("Record :");
        label10.setName("label10"); // NOI18N
        label10.setPreferredSize(new java.awt.Dimension(46, 23));
        panelisi4.add(label10);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(120, 23));
        panelisi4.add(LCount);

        label21.setText("Time Laps :");
        label21.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        label21.setName("label21"); // NOI18N
        label21.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi4.add(label21);

        timeslaps.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        timeslaps.setText("-");
        timeslaps.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        timeslaps.setName("timeslaps"); // NOI18N
        timeslaps.setPreferredSize(new java.awt.Dimension(100, 23));
        panelisi4.add(timeslaps);

        BtnKetWarna.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/satuan.png"))); // NOI18N
        BtnKetWarna.setMnemonic('H');
        BtnKetWarna.setText("Keterangan Warna");
        BtnKetWarna.setToolTipText("Alt+H");
        BtnKetWarna.setName("BtnKetWarna"); // NOI18N
        BtnKetWarna.setPreferredSize(new java.awt.Dimension(170, 30));
        BtnKetWarna.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKetWarnaActionPerformed(evt);
            }
        });
        BtnKetWarna.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKetWarnaKeyPressed(evt);
            }
        });
        panelisi4.add(BtnKetWarna);

        BtnAll.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/Search-16x16.png"))); // NOI18N
        BtnAll.setMnemonic('M');
        BtnAll.setText("Semua");
        BtnAll.setToolTipText("Alt+M");
        BtnAll.setName("BtnAll"); // NOI18N
        BtnAll.setPreferredSize(new java.awt.Dimension(100, 30));
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
        panelisi4.add(BtnAll);

        cmbHlm1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Develope", "Production" }));
        cmbHlm1.setName("cmbHlm1"); // NOI18N
        cmbHlm1.setPreferredSize(new java.awt.Dimension(120, 23));
        panelisi4.add(cmbHlm1);

        panelisi3.add(panelisi4, java.awt.BorderLayout.CENTER);

        panelisi5.setName("panelisi5"); // NOI18N
        panelisi5.setPreferredSize(new java.awt.Dimension(100, 30));
        panelisi5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 4, 2));

        BtnKeluar1.setBackground(new java.awt.Color(255, 0, 0));
        BtnKeluar1.setForeground(new java.awt.Color(255, 255, 255));
        BtnKeluar1.setMnemonic('4');
        BtnKeluar1.setText("Keluar");
        BtnKeluar1.setToolTipText("Alt+4");
        BtnKeluar1.setName("BtnKeluar1"); // NOI18N
        BtnKeluar1.setOpaque(true);
        BtnKeluar1.setPreferredSize(new java.awt.Dimension(120, 23));
        BtnKeluar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar1ActionPerformed(evt);
            }
        });
        panelisi5.add(BtnKeluar1);

        panelisi3.add(panelisi5, java.awt.BorderLayout.PAGE_END);

        internalFrame1.add(panelisi3, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tbListPasienRajalMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListPasienRajalMouseClicked
 if (TabModePasienRalan.getRowCount() != 0) {
            if (evt.getClickCount() == 2) {
                    ppDetailKlaimActionPerformed(null);
            }
        }
    }//GEN-LAST:event_tbListPasienRajalMouseClicked

    private void tbListPasienRajalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListPasienRajalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbListPasienRajalKeyPressed

    private void tbListPasienRanapMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbListPasienRanapMouseClicked
 if (TabModePasienRanap.getRowCount() != 0) {
            if (evt.getClickCount() == 2) {
                    ppDetailKlaimRanapActionPerformed(null);
            }
        }
    }//GEN-LAST:event_tbListPasienRanapMouseClicked

    private void tbListPasienRanapKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbListPasienRanapKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tbListPasienRanapKeyPressed

    private void TabRawatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_TabRawatMouseClicked
        if (TabRawat.getSelectedIndex() == 0) {
            tampilRalan();
        } else if (TabRawat.getSelectedIndex() == 1) {
            tampilRanap();
        }
    }//GEN-LAST:event_TabRawatMouseClicked

    private void DTPTglAwalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPTglAwalKeyPressed
        //        Valid.pindah(evt,TCariTindakan,cmbJam);
    }//GEN-LAST:event_DTPTglAwalKeyPressed

    private void DTPTglAkhirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_DTPTglAkhirKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_DTPTglAkhirKeyPressed

    private void TCariKunjunganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariKunjunganKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCariPasienActionPerformed(null);
        } else if (evt.getKeyCode() == KeyEvent.VK_UP) {
            //            tbListPasienRalan.requestFocus();
        } else if (evt.getKeyCode() == KeyEvent.VK_PAGE_DOWN) {
            BtnCariPasien.requestFocus();
        }
    }//GEN-LAST:event_TCariKunjunganKeyPressed

    private void BtnCariPasienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariPasienActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            long startTime = System.currentTimeMillis();
            tampilRalan();
            //            getPasienBpjs("Ralan");
            long endTime = System.currentTimeMillis(); // mengambil waktu akhir eksekusi
            long elapsedTime = endTime - startTime; // menghitung waktu eksekusi
            timeslaps.setText(String.valueOf(elapsedTime) + " milidetik");
            setCursor(Cursor.getDefaultCursor());
        } else if (TabRawat.getSelectedIndex() == 1) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            long startTime = System.currentTimeMillis();
            tampilRanap();
//            getPasienBpjs("Ranap");
            long endTime = System.currentTimeMillis(); // mengambil waktu akhir eksekusi
            long elapsedTime = endTime - startTime; // menghitung waktu eksekusi
            timeslaps.setText(String.valueOf(elapsedTime) + " milidetik");
            setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BtnCariPasienActionPerformed

    private void BtnCariPasienKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariPasienKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
            BtnCariPasienActionPerformed(null);
        } else {
            //            Valid.pindah(evt, TCariTindakan, BtnAllTindakan);
        }
    }//GEN-LAST:event_BtnCariPasienKeyPressed

    private void BtnSeek4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnSeek4ActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        getDataPoli();
        UnitLayanan.setSize(500, 800);
        UnitLayanan.setLocationRelativeTo(internalFrame1);
        UnitLayanan.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnSeek4ActionPerformed

    private void BtnKetWarnaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKetWarnaActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        KeteranganWarna.setSize(800, 400);
        KeteranganWarna.setLocationRelativeTo(internalFrame1);
        KeteranganWarna.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_BtnKetWarnaActionPerformed

    private void BtnKetWarnaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKetWarnaKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnKetWarnaKeyPressed

    private void BtnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnAllActionPerformed
        kdPoliView.setText("");
        nmPoliView.setText("");
        TCariKunjungan.setText("");
        BtnCariPasienActionPerformed(null);
    }//GEN-LAST:event_BtnAllActionPerformed

    private void BtnAllKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnAllKeyPressed

    }//GEN-LAST:event_BtnAllKeyPressed

    private void BtnKeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar1ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluar1ActionPerformed

    private void BtnCloseInpindah2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah2ActionPerformed
        KeteranganWarna.dispose();        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah2ActionPerformed

    private void BtnCloseInpindah2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah2KeyPressed

    private void KeteranganWarnaWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_KeteranganWarnaWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_KeteranganWarnaWindowActivated

    private void BtnCloseInpindah3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah3ActionPerformed

    private void BtnCloseInpindah3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah3KeyPressed

    private void tbUnitLayananMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbUnitLayananMouseClicked
        if (TabModePoli.getRowCount() != 0) {
            if (evt.getClickCount() == 2) {
                if (tbUnitLayanan.getSelectedRow() != -1) {
                    kdPoliView.setText(tbUnitLayanan.getValueAt(tbUnitLayanan.getSelectedRow(), 0).toString());
                    nmPoliView.setText(tbUnitLayanan.getValueAt(tbUnitLayanan.getSelectedRow(), 1).toString());
                }
                UnitLayanan.dispose();
            }
        }
    }//GEN-LAST:event_tbUnitLayananMouseClicked

    private void tbUnitLayananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tbUnitLayananKeyPressed
        if (TabModePoli.getRowCount() != 0) {
            if (evt.getKeyCode() == KeyEvent.VK_SPACE) {
                if (tbUnitLayanan.getSelectedRow() != -1) {
                    kdPoliView.setText(tbUnitLayanan.getValueAt(tbUnitLayanan.getSelectedRow(), 0).toString());
                    nmPoliView.setText(tbUnitLayanan.getValueAt(tbUnitLayanan.getSelectedRow(), 1).toString());
                }
                UnitLayanan.dispose();
            }
        }
    }//GEN-LAST:event_tbUnitLayananKeyPressed

    private void UnitLayananWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_UnitLayananWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_UnitLayananWindowActivated

    private void BtnCloseInpindah4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah4ActionPerformed
        DlgCatatanPerbaikan.dispose();
    }//GEN-LAST:event_BtnCloseInpindah4ActionPerformed

    private void BtnCloseInpindah4KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah4KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah4KeyPressed

    private void DlgCatatanPerbaikanWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_DlgCatatanPerbaikanWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_DlgCatatanPerbaikanWindowActivated

    private void ppDetailKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppDetailKlaimActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgDetailKlaim3 form = new DlgDetailKlaim3(null, false);
        form.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (chkAutoRefresh.isSelected() == true) {
                    BtnCariPasienActionPerformed(null);
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
//                form.isCek();
        form.setSize(this.getWidth(), this.getHeight() + 20);
        form.setDataPasien(tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 1).toString(), tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 2).toString(), tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 3).toString(), "Ralan");
        
        form.setLocationRelativeTo(this);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_ppDetailKlaimActionPerformed

    private void ppPilihSemuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppPilihSemuaActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            for (i = 0; i < tbListPasienRajal.getRowCount(); i++) {
                tbListPasienRajal.setValueAt(true, i, 0);
            }
        } else if (TabRawat.getSelectedIndex() == 1) {
            for (i = 0; i < tbListPasienRanap.getRowCount(); i++) {
                tbListPasienRanap.setValueAt(true, i, 0);
            }
        }
    }//GEN-LAST:event_ppPilihSemuaActionPerformed

    private void ppBersihkanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkanActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            for (i = 0; i < tbListPasienRajal.getRowCount(); i++) {
                tbListPasienRajal.setValueAt(false, i, 0);
            }
        } else if (TabRawat.getSelectedIndex() == 1) {
            for (i = 0; i < tbListPasienRanap.getRowCount(); i++) {
                tbListPasienRanap.setValueAt(false, i, 0);
            }
        }
    }//GEN-LAST:event_ppBersihkanActionPerformed

    private void ppKodingBerkas1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppKodingBerkas1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppKodingBerkas1ActionPerformed

    private void ppUpdateDataPasienEklaim1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppUpdateDataPasienEklaim1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppUpdateDataPasienEklaim1ActionPerformed

    private void ppPilihSemua1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppPilihSemua1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppPilihSemua1ActionPerformed

    private void ppBersihkan1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkan1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppBersihkan1ActionPerformed

    private void ppDetailKlaimRanapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppDetailKlaimRanapActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        DlgDetailKlaim3 form = new DlgDetailKlaim3(null, false);
        form.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            @Override
            public void windowClosed(WindowEvent e) {
                if (chkAutoRefresh.isSelected() == true) {
                    BtnCariPasienActionPerformed(null);
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
//                form.isCek();
        form.setSize(this.getWidth(), this.getHeight() + 20);
        form.setDataPasien(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString(), tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 2).toString(), tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 3).toString(), "Ranap");
        
        form.setLocationRelativeTo(this);
        form.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_ppDetailKlaimRanapActionPerformed

    private void ppPilihSemua2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppPilihSemua2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppPilihSemua2ActionPerformed

    private void ppBersihkan2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBersihkan2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ppBersihkan2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            PKUDlgListKlaim dialog = new PKUDlgListKlaim(new javax.swing.JFrame(), true);
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
    private widget.Button BtnCariPasien;
    private widget.Button BtnCloseInpindah2;
    private widget.Button BtnCloseInpindah3;
    private widget.Button BtnCloseInpindah4;
    private widget.Button BtnKeluar1;
    private widget.Button BtnKetWarna;
    private widget.Button BtnSeek4;
    private widget.Tanggal DTPTglAkhir;
    private widget.Tanggal DTPTglAwal;
    private javax.swing.JDialog DlgCatatanPerbaikan;
    private widget.PanelBiasa FormInput1;
    private widget.PanelBiasa FormInput2;
    private widget.PanelBiasa FormInput3;
    private javax.swing.JDialog KeteranganWarna;
    private widget.Label LCount;
    private javax.swing.JMenu MnPilihCeklis;
    private javax.swing.JMenu MnPilihCeklis1;
    private javax.swing.JMenu MnPilihCeklisRanap;
    private javax.swing.JPopupMenu Popup1;
    private javax.swing.JPopupMenu PopupRalan;
    private javax.swing.JPopupMenu PopupRanap;
    private widget.ScrollPane Scroll;
    private widget.ScrollPane Scroll1;
    private widget.ScrollPane Scroll2;
    private widget.Label SepTerbit;
    private widget.TextBox TCariKunjungan;
    private widget.TextBox TNoRw;
    private javax.swing.JTabbedPane TabRawat;
    private javax.swing.JDialog UnitLayanan;
    private widget.TextArea catatanPerbaikan1;
    private widget.CekBox chkAutoRefresh;
    private widget.ComboBox cmbHlm;
    private widget.ComboBox cmbHlm1;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame13;
    private widget.InternalFrame internalFrame14;
    private widget.InternalFrame internalFrame15;
    private widget.Label jLabel11;
    private widget.Label jLabel16;
    private widget.Label jLabel3;
    private widget.Label jLabel44;
    private widget.Label jLabel46;
    private widget.Label jLabel49;
    private widget.Label jLabel50;
    private widget.Label jLabel67;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField9;
    private widget.TextBox kdPoliView;
    private widget.Label label10;
    private widget.Label label11;
    private widget.Label label21;
    private widget.Label label9;
    private widget.TextBox nmPoliView;
    private widget.PanelBiasa panelBiasa10;
    private widget.PanelBiasa panelBiasa8;
    private widget.PanelBiasa panelBiasa9;
    private widget.panelisi panelisi3;
    private widget.panelisi panelisi4;
    private widget.panelisi panelisi5;
    private widget.panelisi panelisi6;
    private javax.swing.JMenuItem ppBersihkan;
    private javax.swing.JMenuItem ppBersihkan1;
    private javax.swing.JMenuItem ppBersihkan2;
    private javax.swing.JMenuItem ppDetailKlaim;
    private javax.swing.JMenuItem ppDetailKlaimRanap;
    private javax.swing.JMenuItem ppKodingBerkas1;
    private javax.swing.JMenuItem ppPilihSemua;
    private javax.swing.JMenuItem ppPilihSemua1;
    private javax.swing.JMenuItem ppPilihSemua2;
    private javax.swing.JMenuItem ppUpdateDataPasienEklaim1;
    private widget.ScrollPane scrollPane4;
    private widget.Table tbListPasienRajal;
    private widget.Table tbListPasienRanap;
    private widget.Table tbUnitLayanan;
    private widget.Label timeslaps;
    // End of variables declaration//GEN-END:variables

    public void emptTeks() {

    }

    private void tampilRalan() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            String hal, statusDatang, shortpoli;
            if (cmbHlm.getSelectedItem().equals("Semua")) {
                hal = "";
            } else {
                hal = " limit " + cmbHlm.getSelectedItem() + "";
            }
            if (kdPoliView.getText().equals("")) {
                shortpoli = " ";
            } else {
                shortpoli = " reg_periksa.kd_poli='" + kdPoliView.getText() + "' and ";
            }
            Valid.tabelKosong(TabModePasienRalan);
            sql = "select *,date(bridging_sep.tglpulang) as tgl_pulang from reg_periksa JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis JOIN poliklinik ON reg_periksa.kd_poli=poliklinik.kd_poli JOIN bridging_sep ON reg_periksa.no_rawat=bridging_sep.no_rawat join dokter ON reg_periksa.kd_dokter=dokter.kd_dokter  where " + shortpoli + " status_lanjut='Ralan'  and  bridging_sep.tglsep BETWEEN ? and ? and (reg_periksa.no_rawat like ? or pasien.nm_pasien like ? or pasien.no_rkm_medis like ? or bridging_sep.no_sep like ? or bridging_sep.no_rujukan like ?) " + hal + " ";
            ps = koneksi.prepareStatement(sql);

            try {
                ps.setString(1, Valid.SetTgl(DTPTglAwal.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPTglAkhir.getSelectedItem() + ""));
                ps.setString(3, "%" + TCariKunjungan.getText() + "%");
                ps.setString(4, "%" + TCariKunjungan.getText() + "%");
                ps.setString(5, "%" + TCariKunjungan.getText() + "%");
                ps.setString(6, "%" + TCariKunjungan.getText() + "%");
                ps.setString(7, "%" + TCariKunjungan.getText() + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    String StatusVerif = "-", TglVerif = "-", StatusKoding = "-", TglKoding = "-", StatusKirim = "-", TglKirim = "-", StatusDownload = "-", TglDownload = "-";
                    boolean fileSep, fileResume, fileLaboratorium, fileRadiologi, fileBilling, fileindividual, fileusg;
                    int sep = Sequel.cariInteger("select count(no_rawat) as total from bridging_sep where no_rawat='" + rs.getString("no_rawat") + "'");

                    int berkassep = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='sep' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkassep > 0) {
                        fileSep = true;

                    } else {
                        fileSep = false;

                    }
                    int berkasresume = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='resume' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasresume > 0) {
                        fileResume = true;
                    } else {
                        fileResume = false;
                    }
                    int berkaslaboratorium = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='laboratorium' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkaslaboratorium > 0) {
                        fileLaboratorium = true;
                    } else {
                        fileLaboratorium = false;
                    }
                    int berkasradiologi = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='radiologi' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasradiologi > 0) {
                        fileRadiologi = true;
                    } else {
                        fileRadiologi = false;
                    }
                    int berkasbilling = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='billing' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasbilling > 0) {
                        fileBilling = true;
                    } else {
                        fileBilling = false;
                    }
                    int berkasindifidual = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='data_individual' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasindifidual > 0) {
                        fileindividual = true;
                    } else {
                        fileindividual = false;
                    }
                    int berkasUsg = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='usg' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasUsg > 0) {
                        fileusg = true;
                    } else {
                        fileusg = false;
                    }

                    int statusKoding = Sequel.cariInteger("select count(no_rawat) as total from tt_status_koding where   no_rawat='" + rs.getString("no_rawat") + "'");
                    if (statusKoding > 0) {
                        StatusKoding = Sequel.cariIsi("select status from tt_status_koding where   no_rawat='" + rs.getString("no_rawat") + "'");
                    } else {
                        StatusKoding = "Belum Koding";
                    }
                    int statusBerkas = Sequel.cariInteger("select count(no_rawat) as total from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                    if (statusBerkas > 0) {
                        StatusVerif = Sequel.cariIsi("select status_verif from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        TglVerif = Sequel.cariIsi("select concat(tgl_verif,' ',jam_verif) as tglVerif from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        StatusKoding = Sequel.cariIsi("select status_koding from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        TglKoding = Sequel.cariIsi("select concat(tgl_koding,' ',jam_koding) as tglKoding from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        StatusDownload = Sequel.cariIsi("select status_download from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        TglDownload = Sequel.cariIsi("select concat(tgl_download,' ',jam_download) as tglDownload from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        StatusKirim = Sequel.cariIsi("select status_kirim from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        TglKirim = Sequel.cariIsi("select concat(tgl_kirim,' ',jam_kirim) as tglKirim from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                    } else {
                        StatusKoding = "Belum Koding";
                        TglKoding = "-";
                        StatusKirim = "Belum Kirim";
                        TglKirim = "-";
                        StatusDownload = "Belum Download";
                        TglDownload = "-";
                        StatusVerif = "Belum Verif";
                        TglVerif = "-";
                    }

                    TabModePasienRalan.addRow(new Object[]{
                        false, rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), rs.getString("nm_poli"), rs.getString("nm_dokter"), rs.getString("no_sep"), rs.getString("tglsep"), rs.getString("tgl_registrasi"), (rs.getString("tgl_pulang") == null ? "-" : rs.getString("tgl_pulang")),
                        StatusVerif, TglVerif, StatusKoding, TglKoding, StatusKirim, TglKirim, StatusDownload, TglDownload,
                        fileindividual, fileSep, fileResume, fileBilling, fileLaboratorium, fileRadiologi, fileusg
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tbListPasienRajal.getRowCount());
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void tampilRanap() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Valid.tabelKosong(TabModePasienRanap);
            sql = "select *,date(bridging_sep.tglpulang) as tgl_pulang  from reg_periksa JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis JOIN poliklinik ON reg_periksa.kd_poli=poliklinik.kd_poli JOIN bridging_sep ON reg_periksa.no_rawat=bridging_sep.no_rawat  where status_lanjut='Ranap' and  bridging_sep.tglsep BETWEEN ? and ? and (reg_periksa.no_rawat like ? or pasien.nm_pasien like ? or pasien.no_rkm_medis like ?  or bridging_sep.no_sep like ? or bridging_sep.no_rujukan like ?) ";
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, Valid.SetTgl(DTPTglAwal.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPTglAkhir.getSelectedItem() + ""));
                ps.setString(3, "%" + TCariKunjungan.getText() + "%");
                ps.setString(4, "%" + TCariKunjungan.getText() + "%");
                ps.setString(5, "%" + TCariKunjungan.getText() + "%");
                ps.setString(6, "%" + TCariKunjungan.getText() + "%");
                ps.setString(7, "%" + TCariKunjungan.getText() + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    String tglCheckout = "", kamar = "", StatusVerif = "-", TglVerif = "-", StatusKoding = "-", TglKoding = "-", StatusKirim = "-", TglKirim = "-", StatusDownload = "-", TglDownload = "-";
                    boolean fileSep, fileResume, fileLaboratorium, fileRadiologi, fileBilling, fileindividual, fileusg;

                    kamar = Sequel.cariIsi("select concat(no_bed,' ',nm_kamar) from kamar_inap JOIN kamar ON kamar_inap.kd_kamar=kamar.kd_kamar where no_rawat='" + rs.getString("no_rawat") + "' order by tgl_keluar DESC");

                    int berkassep = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='sep' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkassep > 0) {
                        fileSep = true;
                    } else {
                        fileSep = false;
                    }
                    int berkasresume = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='resume' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasresume > 0) {
                        fileResume = true;
                    } else {
                        fileResume = false;
                    }
                    int berkaslaboratorium = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='laboratorium' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkaslaboratorium > 0) {
                        fileLaboratorium = true;
                    } else {
                        fileLaboratorium = false;
                    }
                    int berkasradiologi = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='radiologi' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasradiologi > 0) {
                        fileRadiologi = true;
                    } else {
                        fileRadiologi = false;
                    }
                    int berkasbilling = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='billing' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasbilling > 0) {
                        fileBilling = true;
                    } else {
                        fileBilling = false;
                    }
                    int berkasindifidual = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='data_individual' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasindifidual > 0) {
                        fileindividual = true;
                    } else {
                        fileindividual = false;
                    }
                    int berkasUsg = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='usg' and  no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasUsg > 0) {
                        fileusg = true;
                    } else {
                        fileusg = false;
                    }
                    int statusKoding = Sequel.cariInteger("select count(no_rawat) as total from tt_status_koding where   no_rawat='" + rs.getString("no_rawat") + "'");
                    if (statusKoding > 0) {
                        StatusKoding = Sequel.cariIsi("select status from tt_status_koding where   no_rawat='" + rs.getString("no_rawat") + "'");
                    } else {
                        StatusKoding = "Belum Koding";
                    }
                    int statusBerkas = Sequel.cariInteger("select count(no_rawat) as total from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                    if (statusBerkas > 0) {
                        StatusVerif = Sequel.cariIsi("select status_verif from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        TglVerif = Sequel.cariIsi("select concat(tgl_verif,' ',jam_verif) as tglVerif from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        StatusKoding = Sequel.cariIsi("select status_koding from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        TglKoding = Sequel.cariIsi("select concat(tgl_koding,' ',jam_koding) as tglKoding from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        StatusDownload = Sequel.cariIsi("select status_download from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        TglDownload = Sequel.cariIsi("select concat(tgl_download,' ',jam_download) as tglDownload from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        StatusKirim = Sequel.cariIsi("select status_kirim from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                        TglKirim = Sequel.cariIsi("select concat(tgl_kirim,' ',jam_kirim) as tglKirim from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
                    } else {
                        StatusKoding = "Belum Koding";
                        TglKoding = "-";
                        StatusKirim = "Belum Kirim";
                        TglKirim = "-";
                        StatusDownload = "Belum Download";
                        TglDownload = "-";
                        StatusVerif = "Belum Verif";
                        TglVerif = "-";
                    }
                    tglCheckout = Sequel.cariIsi("select tgl_keluar from kamar_inap where   no_rawat='" + rs.getString("no_rawat") + "' and ( stts_pulang!='-' or stts_pulang!='Pindah Kamar')  ORDER BY tgl_keluar desc limit 1 ");
                    TabModePasienRanap.addRow(new Object[]{
                        false, rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), kamar, rs.getString("no_sep"), rs.getString("tglsep"), rs.getString("tgl_registrasi"), (tglCheckout == null || tglCheckout == "" ? "-" : tglCheckout),
                        StatusVerif, TglVerif, StatusKoding, TglKoding, StatusKirim, TglKirim, StatusDownload, TglDownload,
                        fileindividual, fileSep, fileResume, fileBilling, fileLaboratorium, fileRadiologi, fileusg

//                        false, rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), kamar, noSep, tglSep, StatusKoding, StatusKirim, fileSep, fileResume, fileLaboratorium, fileRadiologi, fileusg, fileBilling, fileindividual
                    });
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
        LCount.setText("" + tbListPasienRanap.getRowCount());
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void getDataPoli() {
        Valid.tabelKosong(TabModePoli);
        try {
            ps = koneksi.prepareStatement(
                    "select poliklinik.kd_poli,poliklinik.nm_poli "
                    + "from poliklinik  "
                    + "where poliklinik.status='1'  group by poliklinik.kd_poli order by poliklinik.nm_poli ");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    TabModePoli.addRow(new Object[]{rs.getString(1), rs.getString(2)});
                }
            } catch (Exception ex) {
                System.out.println(ex);
            } finally {
                if (rs != null) {
                    rs.close();
                }
                if (ps != null) {
                    ps.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }
//        LCount.setText(""+TabModePoli.getRowCount());
    }
}
