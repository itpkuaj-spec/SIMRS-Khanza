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

    private final DefaultTableModel TabModePasienRalan, TabModePasienRanap, TabModePoli, TabModeDokter;
    private DefaultTableModel TabModePasienTidakKlaim = new DefaultTableModel();
    private String noRawatCatatan = ""; // untuk konteks edit catatan
    private boolean modeCatatanTidakKlaim = false; // false=edit catatan saja, true=jadikan tidak klaim
    private javax.swing.JDialog DokterLayanan;
    private widget.Table tbDokterLayanan;
    private javax.swing.table.DefaultTableModel TabModePasienRanapTidakKlaim = new javax.swing.table.DefaultTableModel();
    private widget.TextBox kdDokterView = new widget.TextBox();
    private widget.TextBox nmDokterView = new widget.TextBox();
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

        panelisi5.add(new javax.swing.JLabel("     ")); // Spacing from Keluar button

        widget.Label LblWarnaResume = new widget.Label();
        LblWarnaResume.setOpaque(true);
        LblWarnaResume.setBackground(new java.awt.Color(204, 153, 255));
        LblWarnaResume.setPreferredSize(new java.awt.Dimension(20, 15));
        panelisi5.add(LblWarnaResume);
        
        widget.Label LblKetResume = new widget.Label();
        LblKetResume.setText(" = Selesai Resume   ");
        panelisi5.add(LblKetResume);

        widget.Label LblWarnaKirim = new widget.Label();
        LblWarnaKirim.setOpaque(true);
        LblWarnaKirim.setBackground(java.awt.Color.ORANGE);
        LblWarnaKirim.setPreferredSize(new java.awt.Dimension(20, 15));
        panelisi5.add(LblWarnaKirim);
        
        widget.Label LblKetKirim = new widget.Label();
        LblKetKirim.setText(" = Selesai Kirim Online   ");
        panelisi5.add(LblKetKirim);

        widget.Label LblWarnaKlaim = new widget.Label();
        LblWarnaKlaim.setOpaque(true);
        LblWarnaKlaim.setBackground(java.awt.Color.GREEN);
        LblWarnaKlaim.setPreferredSize(new java.awt.Dimension(20, 15));
        panelisi5.add(LblWarnaKlaim);
        
        widget.Label LblKetKlaim = new widget.Label();
        LblKetKlaim.setText(" = Klaim Selesai");
        panelisi5.add(LblKetKlaim);

        this.setLocation(10, 2);
        setSize(1328, 674);
        TabModePoli = new DefaultTableModel(null, new Object[]{
            "Kode Unit", "Nama Unit"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
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

        TabModeDokter = new DefaultTableModel(null, new Object[]{
            "Kode Dokter", "Nama Dokter"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        
        DokterLayanan = new javax.swing.JDialog();
        DokterLayanan.setSize(500, 800);
        DokterLayanan.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        DokterLayanan.setName("DokterLayanan");
        DokterLayanan.setUndecorated(true);
        DokterLayanan.setResizable(false);
        
        widget.InternalFrame internalFrameDokter = new widget.InternalFrame();
        internalFrameDokter.setLayout(new java.awt.BorderLayout(1, 1));
        internalFrameDokter.setName("internalFrameDokter");
        
        tbDokterLayanan = new widget.Table();
        tbDokterLayanan.setModel(TabModeDokter);
        tbDokterLayanan.setPreferredScrollableViewportSize(new java.awt.Dimension(500, 500));
        tbDokterLayanan.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        
        for (int i = 0; i < 2; i++) {
            javax.swing.table.TableColumn column = tbDokterLayanan.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(80);
            } else if (i == 1) {
                column.setPreferredWidth(400);
            }
        }
        tbDokterLayanan.setDefaultRenderer(Object.class, new WarnaTable());
        
        widget.ScrollPane ScrollDokter = new widget.ScrollPane();
        ScrollDokter.setViewportView(tbDokterLayanan);
        internalFrameDokter.add(ScrollDokter, java.awt.BorderLayout.CENTER);
        
        widget.PanelBiasa panelBiasaDokter = new widget.PanelBiasa();
        panelBiasaDokter.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT, 1, 1));
        widget.Button btnCloseDokter = new widget.Button();
        btnCloseDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/cross.png"))); 
        btnCloseDokter.setText("Tutup");
        btnCloseDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DokterLayanan.dispose();
            }
        });
        panelBiasaDokter.add(btnCloseDokter);
        internalFrameDokter.add(panelBiasaDokter, java.awt.BorderLayout.PAGE_END);
        
        DokterLayanan.getContentPane().add(internalFrameDokter, java.awt.BorderLayout.CENTER);
        
        tbDokterLayanan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if (tbDokterLayanan.getSelectedRow() != -1) {
                    kdDokterView.setText(tbDokterLayanan.getValueAt(tbDokterLayanan.getSelectedRow(), 0).toString());
                    nmDokterView.setText(tbDokterLayanan.getValueAt(tbDokterLayanan.getSelectedRow(), 1).toString());
                }
                DokterLayanan.dispose();
            }
        });
        
        tbDokterLayanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                    if (tbDokterLayanan.getSelectedRow() != -1) {
                        kdDokterView.setText(tbDokterLayanan.getValueAt(tbDokterLayanan.getSelectedRow(), 0).toString());
                        nmDokterView.setText(tbDokterLayanan.getValueAt(tbDokterLayanan.getSelectedRow(), 1).toString());
                    }
                    DokterLayanan.dispose();
                }
            }
        });
        // Tambahan IT - Awal kolom Kirim Eklaim, Final Eklaim, Kirim Online
        Object[] columns = new String[]{
            "P", "No Rawat[1]", "No RM[2]", "Nama Pasien[3]", "Unit[4]", "Nama Dokter[5]", "No SEP[6]",
            "Tgl SEP[7]", "Tgl. Regis[8]", "Tgl Pulang[9]", "Resume[10]", "Koding[11]", "Lab[12]", "Rad[13]",
            "USG[14]", "Ttp Billing[15]", "Kirim Eklaim[16]", "Final Eklaim[17]", "Kirim Online[18]", "Selesai Klaim[19]"};
        // Tambahan IT - Akhir kolom Kirim Eklaim, Final Eklaim, Kirim Online
        TabModePasienRalan = new DefaultTableModel(null, columns) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0 || colIndex == 10 || colIndex == 11 || colIndex == 12 || colIndex == 13 || colIndex == 14) {
                    a = true;
                }
                return a;
            }
            // Tambahan IT - Ditambah 3 tipe Boolean untuk Kirim Eklaim, Final Eklaim, Kirim Online
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class,
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class,
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };

        tbListPasienRajal.setModel(TabModePasienRalan);
        tbListPasienRajal.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbListPasienRajal.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // Tambahan IT - Loop disesuaikan dengan jumlah kolom (20)
        for (int i = 0; i < 20; i++) {
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
            } else {
                column.setPreferredWidth(60);
            }
        }
        tbListPasienRajal.setDefaultRenderer(Object.class, new WarnaTable() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    Object objSelesaiKlaim = table.getValueAt(row, 19);
                    Object objKirimOnline = table.getValueAt(row, 18);
                    Object objResume = table.getValueAt(row, 10);
                    
                    if (objSelesaiKlaim != null && (Boolean) objSelesaiKlaim) {
                        component.setBackground(java.awt.Color.GREEN);
                    } else if (objKirimOnline != null && (Boolean) objKirimOnline) {
                        component.setBackground(java.awt.Color.ORANGE);
                    } else if (objResume != null && (Boolean) objResume) {
                        component.setBackground(new java.awt.Color(204, 153, 255));
                    }
                } catch (Exception e) {
                }
                return component;
            }
        });

        Object[] columnsRanap = new String[]{
            "P", "No Rawat[1]", "No RM[2]", "Nama Pasien[3]", "Unit[4]", "DPJP[5]", "No SEP[6]",
            "Tgl SEP[7]", "Tgl. Regis[8]", "Tgl Pulang[9]", "Resume[10]", "Koding[11]", "Lab[12]", "Rad[13]",
            "USG[14]", "Ttp Billing[15]", "Kirim Eklaim[16]", "Final Eklaim[17]", "Kirim Online[18]",
            "Selesai Klaim[19]", "S. Emergency[20]", "SPRI[21]", "S. Persetujuan Inap[22]"
        };
        TabModePasienRanap = new DefaultTableModel(null, columnsRanap) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0 || colIndex == 10 || colIndex == 11 || colIndex == 12 || colIndex == 13 || colIndex == 14 || colIndex == 19 || colIndex == 20 || colIndex == 21) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Boolean.class, java.lang.Boolean.class,
                java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class, java.lang.Boolean.class,
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
        for (int i = 0; i < 23; i++) {
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
                column.setPreferredWidth(200);
            } else if (i == 6) {
                column.setPreferredWidth(125);
            } else if (i == 7 || i == 8 || i == 9) {
                column.setPreferredWidth(80);
            } else if (i == 19 || i == 20 || i == 21) {
                column.setPreferredWidth(100);
            } else if (i == 22) {
                column.setPreferredWidth(130);
            } else {
                column.setPreferredWidth(60);
            }
        }

        tbListPasienRanap.setDefaultRenderer(Object.class, new WarnaTable() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                java.awt.Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                try {
                    Object objSelesaiKlaim = table.getValueAt(row, 19);
                    Object objKirimOnline = table.getValueAt(row, 18);
                    Object objResume = table.getValueAt(row, 10);
                    
                    if (objSelesaiKlaim != null && (Boolean) objSelesaiKlaim) {
                        component.setBackground(java.awt.Color.GREEN);
                    } else if (objKirimOnline != null && (Boolean) objKirimOnline) {
                        component.setBackground(java.awt.Color.ORANGE);
                    } else if (objResume != null && (Boolean) objResume) {
                        component.setBackground(new java.awt.Color(204, 153, 255));
                    }
                } catch (Exception e) {
                }
                return component;
            }
        });

        Object[] columnsTidakKlaimRalan = new String[]{
            "P", "No Rawat", "No RM", "Nama Pasien", "Unit", "Nama Dokter", "No SEP",
            "Tgl SEP", "Tgl. Regis", "Catatan", "Tgl Input", "User Input"
        };
        TabModePasienTidakKlaim = new DefaultTableModel(null, columnsTidakKlaimRalan) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return colIndex == 0;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };
            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbListPasienTidakKlaim.setModel(TabModePasienTidakKlaim);
        tbListPasienTidakKlaim.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbListPasienTidakKlaim.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 12; i++) {
            TableColumn column = tbListPasienTidakKlaim.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setPreferredWidth(110);
            } else if (i == 2) {
                column.setPreferredWidth(70);
            } else if (i == 3) {
                column.setPreferredWidth(180);
            } else if (i == 4) {
                column.setPreferredWidth(140);
            } else if (i == 5) {
                column.setPreferredWidth(140);
            } else if (i == 6) {
                column.setPreferredWidth(130);
            } else if (i == 7) {
                column.setPreferredWidth(70);
            } else if (i == 8) {
                column.setPreferredWidth(70);
            } else if (i == 9) {
                column.setPreferredWidth(200);
            } else if (i == 10) {
                column.setPreferredWidth(120);
            } else if (i == 11) {
                column.setPreferredWidth(120);
            }
        }
        tbListPasienTidakKlaim.setDefaultRenderer(Object.class, new WarnaTable());

        Object[] columnsTidakKlaimRanap = new String[]{
            "P", "No Rawat", "No RM", "Nama Pasien", "Unit", "DPJP", "No SEP",
            "Tgl SEP", "Tgl. Regis", "Tgl Pulang", "Catatan", "Tgl Input", "User Input"
        };
        TabModePasienRanapTidakKlaim = new DefaultTableModel(null, columnsTidakKlaimRanap) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return colIndex == 0;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class,
                java.lang.Object.class
            };
            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbListPasienRanapTidakKlaim.setModel(TabModePasienRanapTidakKlaim);
        tbListPasienRanapTidakKlaim.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbListPasienRanapTidakKlaim.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (int i = 0; i < 13; i++) {
            TableColumn column = tbListPasienRanapTidakKlaim.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(20);
            } else if (i == 1) {
                column.setPreferredWidth(110);
            } else if (i == 2) {
                column.setPreferredWidth(70);
            } else if (i == 3) {
                column.setPreferredWidth(180);
            } else if (i == 4) {
                column.setPreferredWidth(140);
            } else if (i == 5) {
                column.setPreferredWidth(140);
            } else if (i == 6) {
                column.setPreferredWidth(130);
            } else if (i == 7) {
                column.setPreferredWidth(70);
            } else if (i == 8) {
                column.setPreferredWidth(70);
            } else if (i == 9) {
                column.setPreferredWidth(70);
            } else if (i == 10) {
                column.setPreferredWidth(200);
            } else if (i == 11) {
                column.setPreferredWidth(120);
            } else if (i == 12) {
                column.setPreferredWidth(120);
            }
        }
        tbListPasienRanapTidakKlaim.setDefaultRenderer(Object.class, new WarnaTable());

        // --- Add to PopupRalan ---
        javax.swing.JMenuItem mnInputCatatan = new javax.swing.JMenuItem("Input Catatan");
        mnInputCatatan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        mnInputCatatan.addActionListener(evt -> mnInputCatatanActionPerformed(evt));
        PopupRalan.add(mnInputCatatan);

        // --- Add missing items to PopupRanap to make it like PopupRalan ---
        javax.swing.JMenuItem mnInputResumeRanap = new javax.swing.JMenuItem("Input Resume");
        mnInputResumeRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        mnInputResumeRanap.addActionListener(evt -> mnInputResumeActionPerformed(evt));
        PopupRanap.add(mnInputResumeRanap);

        javax.swing.JMenuItem mnInputDiagnosaRanap = new javax.swing.JMenuItem("Input Diagnosa");
        mnInputDiagnosaRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        mnInputDiagnosaRanap.addActionListener(evt -> mnInputDiagnosaActionPerformed(evt));
        PopupRanap.add(mnInputDiagnosaRanap);

        javax.swing.JMenu MnLihatRanap = new javax.swing.JMenu("Lihat");
        MnLihatRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png")));

        javax.swing.JMenuItem ppLabRadRanap = new javax.swing.JMenuItem("Lab & Rad");
        ppLabRadRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        ppLabRadRanap.addActionListener(evt -> ppLabRadActionPerformed(evt));
        MnLihatRanap.add(ppLabRadRanap);

        javax.swing.JMenuItem ppBillingRanap = new javax.swing.JMenuItem("Billing");
        ppBillingRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        ppBillingRanap.addActionListener(evt -> ppBillingActionPerformed(evt));
        MnLihatRanap.add(ppBillingRanap);

        javax.swing.JMenuItem ppUsgRanap = new javax.swing.JMenuItem("USG / Tindakan Penunjang");
        ppUsgRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        ppUsgRanap.addActionListener(evt -> ppUsgActionPerformed(evt));
        MnLihatRanap.add(ppUsgRanap);

        javax.swing.JMenuItem ppLaporanOperasiRanap = new javax.swing.JMenuItem("Laporan Operasi");
        ppLaporanOperasiRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        ppLaporanOperasiRanap.addActionListener(evt -> ppLaporanOperasiRanapActionPerformed(evt));
        MnLihatRanap.add(ppLaporanOperasiRanap);

        javax.swing.JMenuItem ppLaporanTindakanMedisRanap = new javax.swing.JMenuItem("Laporan Tindakan Medis");
        ppLaporanTindakanMedisRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        ppLaporanTindakanMedisRanap.addActionListener(evt -> ppLaporanTindakanMedisRanapActionPerformed(evt));
        MnLihatRanap.add(ppLaporanTindakanMedisRanap);

        javax.swing.JMenuItem ppMonitoringTransfusiRanap = new javax.swing.JMenuItem("Monitoring Reaksi Transfusi");
        ppMonitoringTransfusiRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        ppMonitoringTransfusiRanap.addActionListener(evt -> ppMonitoringTransfusiRanapActionPerformed(evt));
        MnLihatRanap.add(ppMonitoringTransfusiRanap);

        PopupRanap.add(MnLihatRanap);

        javax.swing.JMenuItem mnRiwayatRanap = new javax.swing.JMenuItem("Riwayat Perawatan");
        mnRiwayatRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        mnRiwayatRanap.addActionListener(evt -> mnRiwayatActionPerformed(evt));
        PopupRanap.add(mnRiwayatRanap);

        javax.swing.JMenuItem mnBerkasDigitalRanap = new javax.swing.JMenuItem("Berkas Digital");
        mnBerkasDigitalRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        mnBerkasDigitalRanap.addActionListener(evt -> mnBerkasDigitalActionPerformed(evt));
        PopupRanap.add(mnBerkasDigitalRanap);

        javax.swing.JMenuItem mnJadikanTidakKlaimRanap = new javax.swing.JMenuItem("Jadikan Tidak Klaim");
        mnJadikanTidakKlaimRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        mnJadikanTidakKlaimRanap.addActionListener(evt -> mnJadikanTidakKlaimActionPerformed(evt));
        PopupRanap.add(mnJadikanTidakKlaimRanap);

        javax.swing.JMenuItem mnInputCatatanRanap = new javax.swing.JMenuItem("Input Catatan");
        mnInputCatatanRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        mnInputCatatanRanap.addActionListener(evt -> mnInputCatatanActionPerformed(evt));
        PopupRanap.add(mnInputCatatanRanap);

        // --- Create PopupTidakKlaim for index 2 and 3 ---
        javax.swing.JPopupMenu PopupTidakKlaim = new javax.swing.JPopupMenu();

        javax.swing.JMenuItem mnInputCatatanTk = new javax.swing.JMenuItem("Input Catatan");
        mnInputCatatanTk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        mnInputCatatanTk.addActionListener(evt -> mnInputCatatanActionPerformed(evt));
        PopupTidakKlaim.add(mnInputCatatanTk);

        javax.swing.JMenuItem mnKembalikanKlaimTk = new javax.swing.JMenuItem("Kembalikan Ke Daftar Klaim");
        mnKembalikanKlaimTk.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png")));
        mnKembalikanKlaimTk.addActionListener(evt -> mnKembalikanKlaimActionPerformed(evt));
        PopupTidakKlaim.add(mnKembalikanKlaimTk);

        tbListPasienTidakKlaim.setComponentPopupMenu(PopupTidakKlaim);
        tbListPasienRanapTidakKlaim.setComponentPopupMenu(PopupTidakKlaim);

        // Tambahan IT - Menu Selesai Klaim Rajal
        javax.swing.JMenuItem mnSelesaiKlaimRajal = new javax.swing.JMenuItem("Selesai Klaim");
        mnSelesaiKlaimRajal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png")));
        mnSelesaiKlaimRajal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (tbListPasienRajal.getSelectedRow() != -1) {
                    String noRawat = tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 1).toString();
                    if(Sequel.mengedittf("tt_status_eklaim", "no_rawat=?", "selesai_klaim=?", 2, new String[]{"true", noRawat})) {
                        tampilRalan();
                    }
                }
            }
        });
        PopupRalan.add(mnSelesaiKlaimRajal);

        javax.swing.JMenuItem mnBatalSelesaiKlaimRajal = new javax.swing.JMenuItem("Batal Selesai Klaim");
        mnBatalSelesaiKlaimRajal.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png")));
        mnBatalSelesaiKlaimRajal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (tbListPasienRajal.getSelectedRow() != -1) {
                    String noRawat = tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 1).toString();
                    if(Sequel.mengedittf("tt_status_eklaim", "no_rawat=?", "selesai_klaim=?", 2, new String[]{"false", noRawat})) {
                        tampilRalan();
                    }
                }
            }
        });
        PopupRalan.add(mnBatalSelesaiKlaimRajal);

        // Tambahan IT - Menu Selesai Klaim Ranap
        javax.swing.JMenuItem mnSelesaiKlaimRanap = new javax.swing.JMenuItem("Selesai Klaim");
        mnSelesaiKlaimRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png")));
        mnSelesaiKlaimRanap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (tbListPasienRanap.getSelectedRow() != -1) {
                    String noRawat = tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString();
                    if(Sequel.mengedittf("tt_status_eklaim", "no_rawat=?", "selesai_klaim=?", 2, new String[]{"true", noRawat})) {
                        tampilRanap();
                    }
                }
            }
        });
        PopupRanap.add(mnSelesaiKlaimRanap);

        javax.swing.JMenuItem mnBatalSelesaiKlaimRanap = new javax.swing.JMenuItem("Batal Selesai Klaim");
        mnBatalSelesaiKlaimRanap.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png")));
        mnBatalSelesaiKlaimRanap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                if (tbListPasienRanap.getSelectedRow() != -1) {
                    String noRawat = tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString();
                    if(Sequel.mengedittf("tt_status_eklaim", "no_rawat=?", "selesai_klaim=?", 2, new String[]{"false", noRawat})) {
                        tampilRanap();
                    }
                }
            }
        });
        PopupRanap.add(mnBatalSelesaiKlaimRanap);

        // --- Apply Standard Styling ---
        javax.swing.JComponent[] newItems = {
            mnInputCatatan, mnInputResumeRanap, mnInputDiagnosaRanap, 
            MnLihatRanap, ppLabRadRanap, ppBillingRanap, ppUsgRanap, ppLaporanOperasiRanap, ppLaporanTindakanMedisRanap, ppMonitoringTransfusiRanap,
            mnRiwayatRanap, mnBerkasDigitalRanap, mnJadikanTidakKlaimRanap, 
            mnInputCatatanRanap, mnInputCatatanTk, mnKembalikanKlaimTk, mnSelesaiKlaimRajal, mnSelesaiKlaimRanap, mnBatalSelesaiKlaimRajal, mnBatalSelesaiKlaimRanap
        };
        java.awt.Font stdFont = new java.awt.Font("Tahoma", 0, 11);
        java.awt.Color stdFg = new java.awt.Color(50, 50, 50);
        java.awt.Color stdBg = new java.awt.Color(255, 255, 254);
        for (javax.swing.JComponent item : newItems) {
            item.setFont(stdFont);
            item.setForeground(stdFg);
            item.setBackground(stdBg);
            if (item instanceof javax.swing.JMenuItem) {
                ((javax.swing.JMenuItem)item).setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
                ((javax.swing.JMenuItem)item).setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
            }
        }

        // --- Catatan Popup on Click ---
        java.awt.event.MouseAdapter showCatatanHover = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                widget.Table tb = (widget.Table) evt.getSource();
                int row = tb.rowAtPoint(evt.getPoint());
                int col = tb.columnAtPoint(evt.getPoint());
                if (row != -1 && col == 3) {
                    try {
                        String no_rawat = tb.getValueAt(row, 1).toString().trim();
                        String catatan = Sequel.cariIsi("select catatan from pku_list_klaim where no_rawat='" + no_rawat + "' and catatan != ''");
                        if (catatan != null && !catatan.trim().isEmpty()) {
                            javax.swing.JPopupMenu popInfo = new javax.swing.JPopupMenu();
                            javax.swing.JLabel lbl = new javax.swing.JLabel(" Catatan: " + catatan + " ");
                            lbl.setOpaque(true);
                            lbl.setBackground(new java.awt.Color(255, 255, 153));
                            lbl.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 11));
                            popInfo.add(lbl);
                            popInfo.show(evt.getComponent(), evt.getX(), evt.getY());
                        }
                    } catch (Exception e) {}
                }
            }
        };
        tbListPasienRajal.addMouseListener(showCatatanHover);
        tbListPasienRanap.addMouseListener(showCatatanHover);
        tbListPasienTidakKlaim.addMouseListener(showCatatanHover);
        tbListPasienRanapTidakKlaim.addMouseListener(showCatatanHover);
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
        mnInputResume = new javax.swing.JMenuItem();
        mnInputDiagnosa = new javax.swing.JMenuItem();
        ppDetailKlaim = new javax.swing.JMenuItem();
        MnLihat = new javax.swing.JMenu();
        ppLabRad = new javax.swing.JMenuItem();
        ppBilling = new javax.swing.JMenuItem();
        ppUsg = new javax.swing.JMenuItem();
        mnRiwayat = new javax.swing.JMenuItem();
        mnBerkasDigital = new javax.swing.JMenuItem();
        mnJadikanTidakKlaim = new javax.swing.JMenuItem();
        TNoRw = new widget.TextBox();
        PopupRanap = new javax.swing.JPopupMenu();
        ppDetailKlaimRanap = new javax.swing.JMenuItem();
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
        BtnAll = new widget.Button();
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

        BtnSimpanCatatan = new widget.Button();
        BtnSimpanCatatan.setBackground(new java.awt.Color(255, 51, 0));
        BtnSimpanCatatan.setForeground(new java.awt.Color(255, 255, 255));
        BtnSimpanCatatan.setMnemonic('S');
        BtnSimpanCatatan.setText("Simpan");
        BtnSimpanCatatan.setToolTipText("Alt+S");
        BtnSimpanCatatan.setName("BtnSimpanCatatan"); // NOI18N
        BtnSimpanCatatan.setOpaque(true);
        BtnSimpanCatatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSimpanCatatanActionPerformed(evt);
            }
        });
        BtnSimpanCatatan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnSimpanCatatanKeyPressed(evt);
            }
        });
        panelBiasa10.add(BtnSimpanCatatan);

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

        mnInputResume.setBackground(new java.awt.Color(255, 255, 254));
        mnInputResume.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        mnInputResume.setForeground(new java.awt.Color(50, 50, 50));
        mnInputResume.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        mnInputResume.setText("Input Resume");
        mnInputResume.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mnInputResume.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        mnInputResume.setName("mnInputResume"); // NOI18N
        mnInputResume.setPreferredSize(new java.awt.Dimension(250, 25));
        mnInputResume.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnInputResumeActionPerformed(evt);
            }
        });
        PopupRalan.add(mnInputResume);

        mnInputDiagnosa.setBackground(new java.awt.Color(255, 255, 254));
        mnInputDiagnosa.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        mnInputDiagnosa.setForeground(new java.awt.Color(50, 50, 50));
        mnInputDiagnosa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        mnInputDiagnosa.setText("Input Diagnosa");
        mnInputDiagnosa.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mnInputDiagnosa.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        mnInputDiagnosa.setName("mnInputDiagnosa"); // NOI18N
        mnInputDiagnosa.setPreferredSize(new java.awt.Dimension(250, 25));
        mnInputDiagnosa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnInputDiagnosaActionPerformed(evt);
            }
        });
        PopupRalan.add(mnInputDiagnosa);

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

        MnLihat.setBackground(new java.awt.Color(250, 255, 245));
        MnLihat.setForeground(new java.awt.Color(70, 70, 70));
        MnLihat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnLihat.setText("Lihat");
        MnLihat.setToolTipText("");
        MnLihat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnLihat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnLihat.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnLihat.setName("MnLihat"); // NOI18N
        MnLihat.setPreferredSize(new java.awt.Dimension(310, 26));

        ppLabRad.setBackground(new java.awt.Color(255, 255, 254));
        ppLabRad.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppLabRad.setForeground(new java.awt.Color(50, 50, 50));
        ppLabRad.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppLabRad.setText("Lab & Rad");
        ppLabRad.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppLabRad.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppLabRad.setName("ppLabRad"); // NOI18N
        ppLabRad.setPreferredSize(new java.awt.Dimension(250, 25));
        ppLabRad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppLabRadActionPerformed(evt);
            }
        });
        MnLihat.add(ppLabRad);

        ppBilling.setBackground(new java.awt.Color(255, 255, 254));
        ppBilling.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppBilling.setForeground(new java.awt.Color(50, 50, 50));
        ppBilling.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppBilling.setText("Billing");
        ppBilling.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppBilling.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppBilling.setName("ppBilling"); // NOI18N
        ppBilling.setPreferredSize(new java.awt.Dimension(250, 25));
        ppBilling.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppBillingActionPerformed(evt);
            }
        });
        MnLihat.add(ppBilling);

        ppUsg.setBackground(new java.awt.Color(255, 255, 254));
        ppUsg.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        ppUsg.setForeground(new java.awt.Color(50, 50, 50));
        ppUsg.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        ppUsg.setText("Hasil USG");
        ppUsg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        ppUsg.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        ppUsg.setName("ppUsg"); // NOI18N
        ppUsg.setPreferredSize(new java.awt.Dimension(250, 25));
        ppUsg.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ppUsgActionPerformed(evt);
            }
        });
        MnLihat.add(ppUsg);

        PopupRalan.add(MnLihat);

        mnRiwayat.setBackground(new java.awt.Color(255, 255, 254));
        mnRiwayat.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        mnRiwayat.setForeground(new java.awt.Color(50, 50, 50));
        mnRiwayat.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        mnRiwayat.setText("Riwayat Perawatan");
        mnRiwayat.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mnRiwayat.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        mnRiwayat.setName("mnRiwayat"); // NOI18N
        mnRiwayat.setPreferredSize(new java.awt.Dimension(250, 25));
        mnRiwayat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnRiwayatActionPerformed(evt);
            }
        });
        PopupRalan.add(mnRiwayat);

        mnBerkasDigital.setBackground(new java.awt.Color(255, 255, 254));
        mnBerkasDigital.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        mnBerkasDigital.setForeground(new java.awt.Color(50, 50, 50));
        mnBerkasDigital.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        mnBerkasDigital.setText("Berkas Digital");
        mnBerkasDigital.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mnBerkasDigital.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        mnBerkasDigital.setName("mnBerkasDigital"); // NOI18N
        mnBerkasDigital.setPreferredSize(new java.awt.Dimension(250, 25));
        mnBerkasDigital.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnBerkasDigitalActionPerformed(evt);
            }
        });
        PopupRalan.add(mnBerkasDigital);

        mnJadikanTidakKlaim.setBackground(new java.awt.Color(255, 255, 254));
        mnJadikanTidakKlaim.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        mnJadikanTidakKlaim.setForeground(new java.awt.Color(50, 50, 50));
        mnJadikanTidakKlaim.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        mnJadikanTidakKlaim.setText("Jadikan tidak ikut Klaim");
        mnJadikanTidakKlaim.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mnJadikanTidakKlaim.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        mnJadikanTidakKlaim.setName("mnJadikanTidakKlaim"); // NOI18N
        mnJadikanTidakKlaim.setPreferredSize(new java.awt.Dimension(250, 25));
        mnJadikanTidakKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                mnJadikanTidakKlaimActionPerformed(evt);
            }
        });
        PopupRalan.add(mnJadikanTidakKlaim);

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

        Scroll3 = new widget.ScrollPane();
        tbListPasienTidakKlaim = new widget.Table();
        Scroll4 = new widget.ScrollPane();
        tbListPasienRanapTidakKlaim = new widget.Table();

        Scroll3.setName("Scroll3"); // NOI18N
        Scroll3.setOpaque(true);

        tbListPasienTidakKlaim.setAutoCreateRowSorter(true);
        tbListPasienTidakKlaim.setToolTipText("");
        tbListPasienTidakKlaim.setName("tbListPasienTidakKlaim"); // NOI18N
        Scroll3.setViewportView(tbListPasienTidakKlaim);

        TabRawat.addTab("Rawat Jalan (Tidak Klaim)", Scroll3);

        Scroll4.setName("Scroll4"); // NOI18N
        Scroll4.setOpaque(true);

        tbListPasienRanapTidakKlaim.setAutoCreateRowSorter(true);
        tbListPasienRanapTidakKlaim.setToolTipText("");
        tbListPasienRanapTidakKlaim.setName("tbListPasienRanapTidakKlaim"); // NOI18N
        Scroll4.setViewportView(tbListPasienRanapTidakKlaim);

        TabRawat.addTab("Rawat Inap (Tidak Klaim)", Scroll4);


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
        DTPTglAwal.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "22-06-2026" }));
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
        DTPTglAkhir.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "22-06-2026" }));
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

        jLabelDokter = new widget.Label();
        BtnSeekDokter = new widget.Button();
        
        jLabelDokter.setText("Dokter :");
        jLabelDokter.setName("jLabelDokter"); // NOI18N
        jLabelDokter.setPreferredSize(new java.awt.Dimension(50, 23));
        panelisi6.add(jLabelDokter);

        kdDokterView.setName("kdDokterView"); // NOI18N
        kdDokterView.setPreferredSize(new java.awt.Dimension(70, 23));
        panelisi6.add(kdDokterView);

        nmDokterView.setEditable(false);
        nmDokterView.setName("nmDokterView"); // NOI18N
        nmDokterView.setPreferredSize(new java.awt.Dimension(200, 23));
        panelisi6.add(nmDokterView);

        BtnSeekDokter.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/190.png"))); // NOI18N
        BtnSeekDokter.setMnemonic('4');
        BtnSeekDokter.setToolTipText("ALt+4");
        BtnSeekDokter.setName("BtnSeekDokter"); // NOI18N
        BtnSeekDokter.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnSeekDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnSeekDokterActionPerformed(evt);
            }
        });
        panelisi6.add(BtnSeekDokter);

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
        } else if (TabRawat.getSelectedIndex() == 2) {
            tampilTidakKlaim();
        } else if (TabRawat.getSelectedIndex() == 3) {
            tampilRanapTidakKlaim();
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
        } else if (TabRawat.getSelectedIndex() == 2) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            long startTime = System.currentTimeMillis();
            tampilTidakKlaim();
            long endTime = System.currentTimeMillis(); // mengambil waktu akhir eksekusi
            long elapsedTime = endTime - startTime; // menghitung waktu eksekusi
            timeslaps.setText(String.valueOf(elapsedTime) + " milidetik");
            setCursor(Cursor.getDefaultCursor());
        } else if (TabRawat.getSelectedIndex() == 3) {
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            long startTime = System.currentTimeMillis();
            tampilRanapTidakKlaim();
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
        tambahan_it.PKUDlgKlaimEKlaimRajal form = new tambahan_it.PKUDlgKlaimEKlaimRajal(null, false);
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
        tambahan_it.PKUDlgKlaimEKlaim form = new tambahan_it.PKUDlgKlaimEKlaim(null, false);
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

    private void ppLabRadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppLabRadActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            if (tbListPasienRajal.getSelectedRow() != -1) {
                rekammedis.RMRiwayatPenunjang resume = new rekammedis.RMRiwayatPenunjang(null, true);
                resume.setNoRm(tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 2).toString(),
                        tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 3).toString());
                resume.setSize(internalFrame1.getWidth() - 600, internalFrame1.getHeight() -110);
                resume.setLocationRelativeTo(internalFrame1);
                resume.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        } else if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                rekammedis.RMRiwayatPenunjang resume = new rekammedis.RMRiwayatPenunjang(null, true);
                resume.setNoRm(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 2).toString(),
                        tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 3).toString());
                resume.setSize(internalFrame1.getWidth() - 600, internalFrame1.getHeight() -110);
                resume.setLocationRelativeTo(internalFrame1);
                resume.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        }
    }//GEN-LAST:event_ppLabRadActionPerformed

    private void ppLaporanOperasiRanapActionPerformed(java.awt.event.ActionEvent evt) {
        if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                simrskhanza.DlgCariTagihanOperasi tagihan = new simrskhanza.DlgCariTagihanOperasi(null, true);
                tagihan.setPasien(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString());
                tagihan.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
                tagihan.setLocationRelativeTo(internalFrame1);
                tagihan.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        }
    }

    private void ppLaporanTindakanMedisRanapActionPerformed(java.awt.event.ActionEvent evt) {
        if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                rekammedis.RMLaporanTindakan lap = new rekammedis.RMLaporanTindakan(null, true);
                lap.setNoRm(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString(), new java.util.Date());
                lap.setTampil();
                lap.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
                lap.setLocationRelativeTo(internalFrame1);
                lap.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        }
    }

    private void ppMonitoringTransfusiRanapActionPerformed(java.awt.event.ActionEvent evt) {
        if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                rekammedis.RMDataMonitoringReaksiTranfusi lap = new rekammedis.RMDataMonitoringReaksiTranfusi(null, true);
                lap.setNoRm(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString(), new java.util.Date());
                lap.setSize(internalFrame1.getWidth() - 40, internalFrame1.getHeight() - 40);
                lap.setLocationRelativeTo(internalFrame1);
                lap.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        }
    }

    private void mnRiwayatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnRiwayatActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            if (tbListPasienRajal.getSelectedRow() != -1) {
                rekammedis.RMRiwayatPerawatan riwayat = new rekammedis.RMRiwayatPerawatan(null, true);
                riwayat.setNoRm(tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 2).toString(),
                        tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 3).toString());
                riwayat.setSize(internalFrame1.getWidth() -600, internalFrame1.getHeight() -110);
                riwayat.setLocationRelativeTo(internalFrame1);
                riwayat.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        } else if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                rekammedis.RMRiwayatPerawatan riwayat = new rekammedis.RMRiwayatPerawatan(null, true);
                riwayat.setNoRm(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 2).toString(),
                        tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 3).toString());
                riwayat.setSize(internalFrame1.getWidth() -600, internalFrame1.getHeight() -110);
                riwayat.setLocationRelativeTo(internalFrame1);
                riwayat.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        }
    }//GEN-LAST:event_mnRiwayatActionPerformed

    private void ppBillingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppBillingActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            if (tbListPasienRajal.getSelectedRow() != -1) {
                keuangan.DlgBilingRalan billing = new keuangan.DlgBilingRalan(null, false);
                billing.TNoRw.setText(tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 1).toString());
                billing.isCek();
                billing.isRawat();
                if (Sequel.cariInteger("select count(piutang_pasien.no_rawat) from piutang_pasien where piutang_pasien.no_rawat=?", billing.TNoRw.getText()) > 0) {
                    billing.setPiutang();
                }
                billing.setSize(internalFrame1.getWidth() -600, internalFrame1.getHeight() -110);
                billing.setLocationRelativeTo(internalFrame1);
                billing.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        } else if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                keuangan.DlgBilingRanap billing = new keuangan.DlgBilingRanap(null, false);
                billing.TNoRw.setText(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString());
                billing.isCek();
                billing.isRawat();
                billing.setSize(internalFrame1.getWidth() -600, internalFrame1.getHeight() -110);
                billing.setLocationRelativeTo(internalFrame1);
                billing.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        }
    }//GEN-LAST:event_ppBillingActionPerformed

    private void mnInputResumeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnInputResumeActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            if (tbListPasienRajal.getSelectedRow() != -1) {
                rekammedis.RMDataResumePasien resume = new rekammedis.RMDataResumePasien(null, false);
                resume.isCek();
                resume.setNoRm(tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 1).toString(), new java.util.Date());
                resume.setSize(internalFrame1.getWidth() - 600, internalFrame1.getHeight() + 130);
                resume.setLocationRelativeTo(internalFrame1);
                // Tambahan IT - geser posisi form ke atas 150px dari tengah
                resume.setLocation(resume.getX(), resume.getY() - 30);
                resume.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        } else if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                rekammedis.RMDataResumePasienRanap resume = new rekammedis.RMDataResumePasienRanap(null, false);
                resume.isCek();
                resume.setNoRm(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString(), new java.util.Date());
                resume.setSize(internalFrame1.getWidth() - 600, internalFrame1.getHeight() + 130);
                resume.setLocationRelativeTo(internalFrame1);
                // Tambahan IT - geser posisi form ke atas 150px dari tengah
                resume.setLocation(resume.getX(), resume.getY() - 30);
                resume.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        }
    }//GEN-LAST:event_mnInputResumeActionPerformed

    private void mnInputDiagnosaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnInputDiagnosaActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            if (tbListPasienRajal.getSelectedRow() != -1) {
                laporan.DlgDiagnosaPenyakit diagnosa = new laporan.DlgDiagnosaPenyakit(null, false);
                diagnosa.isCek();
                diagnosa.setNoRm(tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 1).toString(), DTPTglAwal.getDate(), DTPTglAkhir.getDate(), "Ralan");
                diagnosa.setSize(internalFrame1.getWidth() -600, internalFrame1.getHeight() -110);
                diagnosa.setLocationRelativeTo(internalFrame1);
                diagnosa.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        } else if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                laporan.DlgDiagnosaPenyakit diagnosa = new laporan.DlgDiagnosaPenyakit(null, false);
                diagnosa.isCek();
                diagnosa.setNoRm(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString(), DTPTglAwal.getDate(), DTPTglAkhir.getDate(), "Ranap");
                diagnosa.setSize(internalFrame1.getWidth() -600, internalFrame1.getHeight() -110);
                diagnosa.setLocationRelativeTo(internalFrame1);
                diagnosa.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        }
    }//GEN-LAST:event_mnInputDiagnosaActionPerformed

    private void ppUsgActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ppUsgActionPerformed
        if (TabRawat.getSelectedIndex() == 0) {
            if (tbListPasienRajal.getSelectedRow() != -1) {
                rekammedis.RMHasilPemeriksaanUSG usg = new rekammedis.RMHasilPemeriksaanUSG(null, false);
                usg.isCek();
                usg.setNoRm(tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 1).toString(), new java.util.Date());
                usg.setSize(internalFrame1.getWidth() -600, internalFrame1.getHeight() -110);
                usg.setLocationRelativeTo(internalFrame1);
                usg.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        } else if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                rekammedis.RMHasilPemeriksaanUSG usg = new rekammedis.RMHasilPemeriksaanUSG(null, false);
                usg.isCek();
                usg.setNoRm(tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString(), new java.util.Date());
                usg.setSize(internalFrame1.getWidth() -600, internalFrame1.getHeight() -110);
                usg.setLocationRelativeTo(internalFrame1);
                usg.setVisible(true);
            } else {
                javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
            }
        }
    }//GEN-LAST:event_ppUsgActionPerformed

    private void mnBerkasDigitalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnBerkasDigitalActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        laporan.DlgBerkasRawat berkas = new laporan.DlgBerkasRawat(null, false);
        berkas.setJudul("::[ Berkas Digital Perawatan ]::", "berkasrawat/pages");
        
        String noRawat = "";
        String tglRegistrasi = "";
        if (TabRawat.getSelectedIndex() == 0) {
            if (tbListPasienRajal.getSelectedRow() != -1) {
                noRawat = tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 1).toString().replaceAll(" ", "_");
                tglRegistrasi = tbListPasienRajal.getValueAt(tbListPasienRajal.getSelectedRow(), 8).toString();
            }
        } else if (TabRawat.getSelectedIndex() == 1) {
            if (tbListPasienRanap.getSelectedRow() != -1) {
                noRawat = tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 1).toString().replaceAll(" ", "_");
                tglRegistrasi = tbListPasienRanap.getValueAt(tbListPasienRanap.getSelectedRow(), 8).toString();
            }
        }
        
        try {
            if(akses.gethapus_berkas_digital_perawatan()==true){
                berkas.loadURL("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/login.php?act=login&usere=" + koneksiDB.USERHYBRIDWEB() + "&passwordte=" + koneksiDB.PASHYBRIDWEB() + "&keyword=" + noRawat + "&tgl_registrasi=" + tglRegistrasi);   
            } else {
                berkas.loadURL("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/berkasrawat/loginnonhapus.php?act=login&usere=" + koneksiDB.USERHYBRIDWEB() + "&passwordte=" + koneksiDB.PASHYBRIDWEB() + "&keyword=" + noRawat + "&tgl_registrasi=" + tglRegistrasi);  
            }                  
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
        }

        berkas.setSize(internalFrame1.getWidth() - 600, internalFrame1.getHeight() - 55);
        berkas.setLocationRelativeTo(internalFrame1);
        berkas.setLocation(berkas.getX(), berkas.getY() - 20);
        berkas.setVisible(true);        
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_mnBerkasDigitalActionPerformed

    private void mnJadikanTidakKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_mnJadikanTidakKlaimActionPerformed
        widget.Table tb = TabRawat.getSelectedIndex() == 1 ? tbListPasienRanap : tbListPasienRajal;
        if (tb.getSelectedRow() != -1) {
            noRawatCatatan = tb.getValueAt(tb.getSelectedRow(), 1).toString().trim();
            modeCatatanTidakKlaim = true;
            String cat = Sequel.cariIsi("select catatan from pku_list_klaim where no_rawat='" + noRawatCatatan + "'");
            catatanPerbaikan1.setText(cat == null ? "" : cat);
            DlgCatatanPerbaikan.setSize(500, 250);
            DlgCatatanPerbaikan.setLocationRelativeTo(internalFrame1);
            DlgCatatanPerbaikan.setVisible(true);
        }
    }//GEN-LAST:event_mnJadikanTidakKlaimActionPerformed

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

    private void BtnSimpanCatatanActionPerformed(java.awt.event.ActionEvent evt) {
        simpanCatatan();
    }

    private void BtnSimpanCatatanKeyPressed(java.awt.event.KeyEvent evt) {
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            simpanCatatan();
        }
    }
    private widget.Button BtnSeekDokter;
    private widget.Label jLabelDokter;

    private void BtnSeekDokterActionPerformed(java.awt.event.ActionEvent evt) {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        getDataDokter();
        DokterLayanan.setLocationRelativeTo(internalFrame1);
        DokterLayanan.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnSimpanCatatan;
    private widget.ComboBox cmbHlm;
    private widget.Label jLabel11;
    private widget.ScrollPane Scroll3;
    private widget.ScrollPane Scroll4;
    private widget.Table tbListPasienTidakKlaim;
    private widget.Table tbListPasienRanapTidakKlaim;
    private widget.Button BtnAll;
    private widget.Button BtnCariPasien;
    private widget.Button BtnCloseInpindah2;
    private widget.Button BtnCloseInpindah3;
    private widget.Button BtnCloseInpindah4;
    private widget.Button BtnKeluar1;
    private widget.Button BtnSeek4;
    private widget.Tanggal DTPTglAkhir;
    private widget.Tanggal DTPTglAwal;
    private javax.swing.JDialog DlgCatatanPerbaikan;
    private widget.PanelBiasa FormInput1;
    private widget.PanelBiasa FormInput2;
    private widget.PanelBiasa FormInput3;
    private javax.swing.JDialog KeteranganWarna;
    private widget.Label LCount;
    private javax.swing.JMenu MnLihat;
    private javax.swing.JMenu MnPilihCeklis1;
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
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame13;
    private widget.InternalFrame internalFrame14;
    private widget.InternalFrame internalFrame15;
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
    private javax.swing.JMenuItem mnBerkasDigital;
    private javax.swing.JMenuItem mnInputDiagnosa;
    private javax.swing.JMenuItem mnInputResume;
    private javax.swing.JMenuItem mnJadikanTidakKlaim;
    private javax.swing.JMenuItem mnRiwayat;
    private widget.TextBox nmPoliView;
    private widget.PanelBiasa panelBiasa10;
    private widget.PanelBiasa panelBiasa8;
    private widget.PanelBiasa panelBiasa9;
    private widget.panelisi panelisi3;
    private widget.panelisi panelisi4;
    private widget.panelisi panelisi5;
    private widget.panelisi panelisi6;
    private javax.swing.JMenuItem ppBersihkan1;
    private javax.swing.JMenuItem ppBilling;
    private javax.swing.JMenuItem ppDetailKlaim;
    private javax.swing.JMenuItem ppDetailKlaimRanap;
    private javax.swing.JMenuItem ppKodingBerkas1;
    private javax.swing.JMenuItem ppLabRad;
    private javax.swing.JMenuItem ppPilihSemua1;
    private javax.swing.JMenuItem ppUpdateDataPasienEklaim1;
    private javax.swing.JMenuItem ppUsg;
    private widget.ScrollPane scrollPane4;
    private widget.Table tbListPasienRajal;
    private widget.Table tbListPasienRanap;
    private widget.Table tbUnitLayanan;
    private widget.Label timeslaps;
    // End of variables declaration//GEN-END:variables

    public void emptTeks() {

    }

    private void tampilRalan() {
        int countSep = 0;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            String statusDatang, shortpoli, shortdokter;
            if (kdPoliView.getText().equals("")) {
                shortpoli = " ";
            } else {
                shortpoli = " reg_periksa.kd_poli='" + kdPoliView.getText() + "' and ";
            }
            if (kdDokterView.getText().equals("")) {
                shortdokter = " ";
            } else {
                shortdokter = " reg_periksa.kd_dokter='" + kdDokterView.getText() + "' and ";
            }
            Valid.tabelKosong(TabModePasienRalan);
            sql = "select *,date(bridging_sep.tglpulang) as tgl_pulang from reg_periksa JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis JOIN poliklinik ON reg_periksa.kd_poli=poliklinik.kd_poli LEFT JOIN bridging_sep ON reg_periksa.no_rawat=bridging_sep.no_rawat join dokter ON reg_periksa.kd_dokter=dokter.kd_dokter LEFT JOIN pku_list_klaim ON reg_periksa.no_rawat=pku_list_klaim.no_rawat where " + shortpoli + shortdokter + " status_lanjut='Ralan' and reg_periksa.kd_pj='BPJ' and reg_periksa.stts<>'Batal' and reg_periksa.kd_pj<>'IGDK' and reg_periksa.kd_poli<>'IGDK' and (pku_list_klaim.no_rawat IS NULL OR pku_list_klaim.status_tidak_klaim='0') and reg_periksa.tgl_registrasi BETWEEN ? and ? and (reg_periksa.no_rawat like ? or pasien.nm_pasien like ? or pasien.no_rkm_medis like ? or bridging_sep.no_sep like ? or bridging_sep.no_rujukan like ?) ORDER BY bridging_sep.no_sep ASC";
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
                    if (rs.getString("no_sep") != null && !rs.getString("no_sep").trim().isEmpty() && !rs.getString("no_sep").trim().equals("-")) {
                        countSep++;
                    }
                    
                    boolean ckSep, ckResume, ckLaboratorium, ckRadiologi, ckusg, ckKoding, ckBilling;
                    // Tambahan IT - variabel untuk kolom Kirim Eklaim, Final Eklaim, Kirim Online
                    boolean ckKirimEklaim = false, ckFinalEklaim = false, ckKirimOnline = false;
                    int sep = Sequel.cariInteger("select count(no_rawat) as total from bridging_sep where no_rawat='" + rs.getString("no_rawat") + "'");

                    int berkasresume = Sequel.cariInteger("select count(no_rawat) as total from resume_pasien where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasresume > 0) {
                        ckResume = true;
                    } else {
                        ckResume = false;
                    }
                    int berkaslaboratorium = Sequel.cariInteger("select count(no_rawat) as total from periksa_lab where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkaslaboratorium > 0) {
                        ckLaboratorium = true;
                    } else {
                        ckLaboratorium = false;
                    }
                    int berkasradiologi = Sequel.cariInteger("select count(no_rawat) as total from periksa_radiologi where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasradiologi > 0) {
                        ckRadiologi = true;
                    } else {
                        ckRadiologi = false;
                    }

                    int berkasUsg = Sequel.cariInteger("select count(no_rawat) as total from hasil_pemeriksaan_usg where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasUsg > 0) {
                        ckusg = true;
                    } else {
                        ckusg = false;
                    }
                    
                    int statusKoding = Sequel.cariInteger("select count(no_rawat) as total from diagnosa_pasien where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (statusKoding > 0) {
                        ckKoding = true;
                    } else {
                        ckKoding = false;
                    }
                    
                    int statusBilling = Sequel.cariInteger("select count(no_rawat) as total from nota_jalan where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (statusBilling > 0) {
                        ckBilling = true;
                    } else {
                        ckBilling = false;
                    }
                    
                    // Tambahan IT - Awal cek status Eklaim dari tabel tt_status_eklaim
                    String cekSetDataKlaim = Sequel.cariIsi("select set_data_klaim from tt_status_eklaim where no_rawat='" + rs.getString("no_rawat") + "'");
                    String cekFinalKlaim = Sequel.cariIsi("select final_klaim from tt_status_eklaim where no_rawat='" + rs.getString("no_rawat") + "'");
                    String cekKirimOnline = Sequel.cariIsi("select kirim_online from tt_status_eklaim where no_rawat='" + rs.getString("no_rawat") + "'");
                    String cekSelesaiKlaim = Sequel.cariIsi("select selesai_klaim from tt_status_eklaim where no_rawat='" + rs.getString("no_rawat") + "'");
                    ckKirimEklaim = "true".equals(cekSetDataKlaim);
                    ckFinalEklaim = "true".equals(cekFinalKlaim);
                    ckKirimOnline = "true".equals(cekKirimOnline);
                    boolean ckSelesaiKlaimBool = "true".equals(cekSelesaiKlaim);
                    // Tambahan IT - Akhir cek status Eklaim

                    TabModePasienRalan.addRow(new Object[]{
                        false, rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), rs.getString("nm_poli"), rs.getString("nm_dokter"), rs.getString("no_sep"), rs.getString("tglsep"), rs.getString("tgl_registrasi"), (rs.getString("tgl_pulang") == null ? "-" : rs.getString("tgl_pulang")),
                        ckResume, ckKoding, ckLaboratorium, ckRadiologi, ckusg, ckBilling, ckKirimEklaim, ckFinalEklaim, ckKirimOnline, ckSelesaiKlaimBool
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
        SepTerbit.setText("" + countSep);
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void tampilRanap() {
        int countSep = 0;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Valid.tabelKosong(TabModePasienRanap);
            String shortdokter;
            if (kdDokterView.getText().equals("")) {
                shortdokter = " ";
            } else {
                shortdokter = " reg_periksa.kd_dokter='" + kdDokterView.getText() + "' and ";
            }
            sql = "select *,date(bridging_sep.tglpulang) as tgl_pulang  from reg_periksa JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis JOIN poliklinik ON reg_periksa.kd_poli=poliklinik.kd_poli LEFT JOIN bridging_sep ON reg_periksa.no_rawat=bridging_sep.no_rawat LEFT JOIN pku_list_klaim ON reg_periksa.no_rawat=pku_list_klaim.no_rawat INNER JOIN kamar_inap ON reg_periksa.no_rawat=kamar_inap.no_rawat and kamar_inap.stts_pulang<>'Pindah Kamar' where " + shortdokter + " status_lanjut='Ranap' and reg_periksa.kd_pj='BPJ' and reg_periksa.no_rawat not in (select no_rawat2 from ranap_gabung) and (pku_list_klaim.no_rawat is null or pku_list_klaim.status_tidak_klaim='0') and kamar_inap.tgl_keluar BETWEEN ? and ? and (kamar_inap.no_rawat like ? or pasien.nm_pasien like ? or pasien.no_rkm_medis like ?  or bridging_sep.no_sep like ? or bridging_sep.no_rujukan like ?) ORDER BY bridging_sep.no_sep ASC";
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
                    if (rs.getString("no_sep") != null && !rs.getString("no_sep").trim().isEmpty() && !rs.getString("no_sep").trim().equals("-")) {
                        countSep++;
                    }
                    String tglCheckout = "", kamar = "", dpjp = "", StatusVerif = "-", TglVerif = "-", StatusKoding = "-", TglKoding = "-", StatusKirim = "-", TglKirim = "-", StatusDownload = "-", TglDownload = "-";
                    boolean ckResume, ckLaboratorium, ckRadiologi, ckBilling, ckusg, ckKoding;
                    // Tambahan IT - variabel untuk kolom Kirim Eklaim, Final Eklaim, Kirim Online
                    boolean ckKirimEklaim = false, ckFinalEklaim = false, ckKirimOnline = false;

                    kamar = Sequel.cariIsi("select concat(kamar_inap.kd_kamar,', ',bangsal.nm_bangsal,', ',kamar.kelas) from kamar_inap inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat='" + rs.getString("no_rawat") + "' order by kamar_inap.tgl_keluar DESC limit 1");
                    dpjp = Sequel.cariIsi("select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='" + rs.getString("no_rawat") + "'");

                    int berkasresume = Sequel.cariInteger("select count(no_rawat) as total from resume_pasien_ranap where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasresume > 0) {
                        ckResume = true;
                    } else {
                        ckResume = false;
                    }
                    int berkaslaboratorium = Sequel.cariInteger("select count(no_rawat) as total from periksa_lab where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkaslaboratorium > 0) {
                        ckLaboratorium = true;
                    } else {
                        ckLaboratorium = false;
                    }
                    int berkasradiologi = Sequel.cariInteger("select count(no_rawat) as total from periksa_radiologi where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasradiologi > 0) {
                        ckRadiologi = true;
                    } else {
                        ckRadiologi = false;
                    }
                    int berkasbilling = Sequel.cariInteger("select count(no_rawat) as total from nota_inap where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasbilling > 0) {
                        ckBilling = true;
                    } else {
                        ckBilling = false;
                    }
//                    int berkasindifidual = Sequel.cariInteger("select count(no_rawat) as total from tt_berkasdigital where jenis_file='data_individual' and  no_rawat='" + rs.getString("no_rawat") + "'");
//                    if (berkasindifidual > 0) {
//                        fileindividual = true;
//                    } else {
//                        fileindividual = false;
//                    }
                    int berkasUsg = Sequel.cariInteger("select count(no_rawat) as total from hasil_pemeriksaan_usg where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasUsg > 0) {
                        ckusg = true;
                    } else {
                        ckusg = false;
                    }
                    int statusKoding = Sequel.cariInteger("select count(no_rawat) as total from diagnosa_pasien where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (statusKoding > 0) {
                        ckKoding = true;
                    } else {
                        ckKoding = false;
                    }
//                    int statusBerkas = Sequel.cariInteger("select count(no_rawat) as total from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
//                    if (statusBerkas > 0) {
//                        StatusVerif = Sequel.cariIsi("select status_verif from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
//                        TglVerif = Sequel.cariIsi("select concat(tgl_verif,' ',jam_verif) as tglVerif from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
//                        StatusKoding = Sequel.cariIsi("select status_koding from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
//                        TglKoding = Sequel.cariIsi("select concat(tgl_koding,' ',jam_koding) as tglKoding from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
//                        StatusDownload = Sequel.cariIsi("select status_download from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
//                        TglDownload = Sequel.cariIsi("select concat(tgl_download,' ',jam_download) as tglDownload from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
//                        StatusKirim = Sequel.cariIsi("select status_kirim from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
//                        TglKirim = Sequel.cariIsi("select concat(tgl_kirim,' ',jam_kirim) as tglKirim from tt_status_digital_klaim where   no_rawat='" + rs.getString("no_rawat") + "'");
//                    } else {
//                        StatusKoding = "Belum Koding";
//                        TglKoding = "-";
//                        StatusKirim = "Belum Kirim";
//                        TglKirim = "-";
//                        StatusDownload = "Belum Download";
//                        TglDownload = "-";
//                        StatusVerif = "Belum Verif";
//                        TglVerif = "-";
//                    }

                    // Tambahan IT - Awal cek status Eklaim dari tabel tt_status_eklaim
                    String cekSetDataKlaim = Sequel.cariIsi("select set_data_klaim from tt_status_eklaim where no_rawat='" + rs.getString("no_rawat") + "'");
                    String cekFinalKlaim = Sequel.cariIsi("select final_klaim from tt_status_eklaim where no_rawat='" + rs.getString("no_rawat") + "'");
                    String cekKirimOnline = Sequel.cariIsi("select kirim_online from tt_status_eklaim where no_rawat='" + rs.getString("no_rawat") + "'");
                    String cekSelesaiKlaim = Sequel.cariIsi("select selesai_klaim from tt_status_eklaim where no_rawat='" + rs.getString("no_rawat") + "'");
                    ckKirimEklaim = "true".equals(cekSetDataKlaim);
                    ckFinalEklaim = "true".equals(cekFinalKlaim);
                    ckKirimOnline = "true".equals(cekKirimOnline);
                    boolean ckSelesaiKlaimBool = "true".equals(cekSelesaiKlaim);
                    // Tambahan IT - Akhir cek status Eklaim
                    boolean ckSEmergency = false, ckSpri = false, ckPersetujuanInap = false;
                    int berkasSpri = Sequel.cariInteger("select count(no_rawat) as total from bridging_surat_pri_bpjs where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasSpri > 0) { ckSpri = true; }
                    int berkasPersetujuan = Sequel.cariInteger("select count(no_rawat) as total from surat_persetujuan_rawat_inap where no_rawat='" + rs.getString("no_rawat") + "'");
                    if (berkasPersetujuan > 0) { ckPersetujuanInap = true; }
                    int berkasEmerg = Sequel.cariInteger("select count(no_rawat) as total from berkas_digital_perawatan where no_rawat='" + rs.getString("no_rawat") + "' and lokasi_file like '%emerg%'");
                    if (berkasEmerg > 0) { ckSEmergency = true; }

                    tglCheckout = Sequel.cariIsi("select tgl_keluar from kamar_inap where no_rawat='" + rs.getString("no_rawat") + "' and ( stts_pulang!='-' or stts_pulang!='Pindah Kamar')  ORDER BY tgl_keluar desc limit 1 ");
                    TabModePasienRanap.addRow(new Object[]{
                        false, rs.getString("no_rawat"), rs.getString("no_rkm_medis"), rs.getString("nm_pasien"), kamar, dpjp, rs.getString("no_sep"), rs.getString("tglsep"), rs.getString("tgl_registrasi"), (tglCheckout == null || tglCheckout == "" ? "-" : tglCheckout),
                        ckResume, ckKoding, ckLaboratorium, ckRadiologi, ckusg, ckBilling, ckKirimEklaim, ckFinalEklaim, ckKirimOnline, ckSelesaiKlaimBool, ckSEmergency, ckSpri, ckPersetujuanInap
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
        SepTerbit.setText("" + countSep);
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

    private void getDataDokter() {
        Valid.tabelKosong(TabModeDokter);
        try {
            ps = koneksi.prepareStatement(
                    "select kd_dokter,nm_dokter from dokter where status='1' order by nm_dokter");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    TabModeDokter.addRow(new Object[]{rs.getString(1), rs.getString(2)});
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
            System.out.println(e);
        }
    }

    private void simpanCatatan() {
        if (noRawatCatatan.isEmpty()) {
            DlgCatatanPerbaikan.dispose();
            return;
        }
        try {
            String catatan = catatanPerbaikan1.getText();
            // cek apakah sudah ada di tabel
            int ada = Sequel.cariInteger("select count(*) from pku_list_klaim where no_rawat='" + noRawatCatatan + "'");
            if (ada > 0) {
                // UPDATE
                if (modeCatatanTidakKlaim) {
                    ps = koneksi.prepareStatement("UPDATE pku_list_klaim SET catatan=?, status_tidak_klaim='1', tgl_input=CURDATE(), jam_input=CURTIME(), user_input=? WHERE no_rawat=?");
                    ps.setString(1, catatan);
                    ps.setString(2, akses.getnamauser());
                    ps.setString(3, noRawatCatatan);
                } else {
                    ps = koneksi.prepareStatement("UPDATE pku_list_klaim SET catatan=? WHERE no_rawat=?");
                    ps.setString(1, catatan);
                    ps.setString(2, noRawatCatatan);
                }
            } else {
                // INSERT
                if (modeCatatanTidakKlaim) {
                    ps = koneksi.prepareStatement("INSERT INTO pku_list_klaim (no_rawat, catatan, status_tidak_klaim, tgl_input, jam_input, user_input) VALUES (?, ?, '1', CURDATE(), CURTIME(), ?)");
                    ps.setString(1, noRawatCatatan);
                    ps.setString(2, catatan);
                    ps.setString(3, akses.getnamauser());
                } else {
                    ps = koneksi.prepareStatement("INSERT INTO pku_list_klaim (no_rawat, catatan, status_tidak_klaim) VALUES (?, ?, '0')");
                    ps.setString(1, noRawatCatatan);
                    ps.setString(2, catatan);
                }
            }
            ps.executeUpdate();
            ps.close();
            DlgCatatanPerbaikan.dispose();
            
            if (TabRawat.getSelectedIndex() == 0 || TabRawat.getSelectedIndex() == 2) {
                tampilRalan();
                tampilTidakKlaim();
            } else if (TabRawat.getSelectedIndex() == 1 || TabRawat.getSelectedIndex() == 3) {
                tampilRanap();
                tampilRanapTidakKlaim();
            }
            
        } catch (Exception e) {
            System.out.println("simpanCatatan error: " + e);
        }
    }

    private void mnInputCatatanActionPerformed(java.awt.event.ActionEvent evt) {
        widget.Table tb = null;
        if (TabRawat.getSelectedIndex() == 0) tb = tbListPasienRajal;
        else if (TabRawat.getSelectedIndex() == 1) tb = tbListPasienRanap;
        else if (TabRawat.getSelectedIndex() == 2) tb = tbListPasienTidakKlaim;
        else if (TabRawat.getSelectedIndex() == 3) tb = tbListPasienRanapTidakKlaim;

        if (tb != null && tb.getSelectedRow() != -1) {
            noRawatCatatan = tb.getValueAt(tb.getSelectedRow(), 1).toString().trim();
            modeCatatanTidakKlaim = false;
            String cat = Sequel.cariIsi("select catatan from pku_list_klaim where no_rawat='" + noRawatCatatan + "'");
            catatanPerbaikan1.setText(cat == null ? "" : cat);
            DlgCatatanPerbaikan.setSize(500, 250);
            DlgCatatanPerbaikan.setLocationRelativeTo(internalFrame1);
            DlgCatatanPerbaikan.setVisible(true);
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
        }
    }

    private void mnKembalikanKlaimActionPerformed(java.awt.event.ActionEvent evt) {
        widget.Table tb = TabRawat.getSelectedIndex() == 3 ? tbListPasienRanapTidakKlaim : tbListPasienTidakKlaim;
        if (tb.getSelectedRow() != -1) {
            noRawatCatatan = tb.getValueAt(tb.getSelectedRow(), 1).toString().trim();
            Sequel.mengedit("pku_list_klaim", "no_rawat='" + noRawatCatatan + "'", "status_tidak_klaim='0'");
            BtnCariPasienActionPerformed(null);
        } else {
            javax.swing.JOptionPane.showMessageDialog(null, "Maaf, silahkan pilih data pasien terlebih dahulu...!!!!");
        }
    }


    private void tampilTidakKlaim() {
        int countSep = 0;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Valid.tabelKosong(TabModePasienTidakKlaim);
            String shortdokter2;
            if (kdDokterView.getText().equals("")) {
                shortdokter2 = " ";
            } else {
                shortdokter2 = " reg_periksa.kd_dokter='" + kdDokterView.getText() + "' and ";
            }
            sql = "select reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, poliklinik.nm_poli, "
                + "dokter.nm_dokter, bridging_sep.no_sep, bridging_sep.tglsep, reg_periksa.tgl_registrasi, "
                + "pku_list_klaim.catatan, pku_list_klaim.tgl_input, pku_list_klaim.user_input "
                + "from reg_periksa "
                + "JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "JOIN poliklinik ON reg_periksa.kd_poli=poliklinik.kd_poli "
                + "JOIN dokter ON reg_periksa.kd_dokter=dokter.kd_dokter "
                + "LEFT JOIN bridging_sep ON reg_periksa.no_rawat=bridging_sep.no_rawat "
                + "JOIN pku_list_klaim ON reg_periksa.no_rawat=pku_list_klaim.no_rawat "
                + "where " + shortdokter2 + " status_lanjut='Ralan' and reg_periksa.kd_pj='BPJ' "
                + "and reg_periksa.stts<>'Batal' and pku_list_klaim.status_tidak_klaim='1' "
                + "and reg_periksa.tgl_registrasi BETWEEN ? and ? "
                + "and (reg_periksa.no_rawat like ? or pasien.nm_pasien like ? or pasien.no_rkm_medis like ?) ";
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, Valid.SetTgl(DTPTglAwal.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPTglAkhir.getSelectedItem() + ""));
                ps.setString(3, "%" + TCariKunjungan.getText() + "%");
                ps.setString(4, "%" + TCariKunjungan.getText() + "%");
                ps.setString(5, "%" + TCariKunjungan.getText() + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    TabModePasienTidakKlaim.addRow(new Object[]{
                        false,
                        rs.getString("no_rawat"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        rs.getString("nm_poli"),
                        rs.getString("nm_dokter"),
                        (rs.getString("no_sep") == null ? "-" : rs.getString("no_sep")),
                        (rs.getString("tglsep") == null ? "-" : rs.getString("tglsep")),
                        rs.getString("tgl_registrasi"),
                        (rs.getString("catatan") == null ? "" : rs.getString("catatan")),
                        (rs.getString("tgl_input") == null ? "-" : rs.getString("tgl_input")),
                        (rs.getString("user_input") == null ? "-" : rs.getString("user_input"))
                    });
                }
            } catch (Exception ex) {
                System.out.println("tampilTidakKlaim: " + ex);
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (Exception e) {
            System.out.println("tampilTidakKlaim: " + e);
        }
        LCount.setText("" + tbListPasienTidakKlaim.getRowCount());
        SepTerbit.setText("" + countSep);
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void tampilRanapTidakKlaim() {
        int countSep = 0;
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        try {
            Valid.tabelKosong(TabModePasienRanapTidakKlaim);
            String shortdokter2;
            if (kdDokterView.getText().equals("")) {
                shortdokter2 = " ";
            } else {
                shortdokter2 = " reg_periksa.kd_dokter='" + kdDokterView.getText() + "' and ";
            }
            sql = "select reg_periksa.no_rawat, reg_periksa.no_rkm_medis, pasien.nm_pasien, "
                + "date(bridging_sep.tglpulang) as tgl_pulang, bridging_sep.no_sep, bridging_sep.tglsep, reg_periksa.tgl_registrasi, "
                + "pku_list_klaim.catatan, pku_list_klaim.tgl_input, pku_list_klaim.user_input "
                + "from reg_periksa "
                + "JOIN pasien ON reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                + "LEFT JOIN bridging_sep ON reg_periksa.no_rawat=bridging_sep.no_rawat "
                + "JOIN pku_list_klaim ON reg_periksa.no_rawat=pku_list_klaim.no_rawat "
                + "where " + shortdokter2 + " status_lanjut='Ranap' and reg_periksa.kd_pj='BPJ' "
                + "and reg_periksa.stts<>'Batal' and pku_list_klaim.status_tidak_klaim='1' "
                + "and reg_periksa.tgl_registrasi BETWEEN ? and ? "
                + "and (reg_periksa.no_rawat like ? or pasien.nm_pasien like ? or pasien.no_rkm_medis like ?) ";
            ps = koneksi.prepareStatement(sql);
            try {
                ps.setString(1, Valid.SetTgl(DTPTglAwal.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPTglAkhir.getSelectedItem() + ""));
                ps.setString(3, "%" + TCariKunjungan.getText() + "%");
                ps.setString(4, "%" + TCariKunjungan.getText() + "%");
                ps.setString(5, "%" + TCariKunjungan.getText() + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    if (rs.getString("no_sep") != null && !rs.getString("no_sep").trim().isEmpty() && !rs.getString("no_sep").trim().equals("-")) {
                        countSep++;
                    }
                    String tglCheckout = "", kamar = "", dpjp = "";
                    kamar = Sequel.cariIsi("select concat(kamar_inap.kd_kamar,', ',bangsal.nm_bangsal,', ',kamar.kelas) from kamar_inap inner join kamar on kamar_inap.kd_kamar=kamar.kd_kamar inner join bangsal on kamar.kd_bangsal=bangsal.kd_bangsal where kamar_inap.no_rawat='" + rs.getString("no_rawat") + "' order by kamar_inap.tgl_keluar DESC limit 1");
                    dpjp = Sequel.cariIsi("select dokter.nm_dokter from dpjp_ranap inner join dokter on dpjp_ranap.kd_dokter=dokter.kd_dokter where dpjp_ranap.no_rawat='" + rs.getString("no_rawat") + "'");
                    tglCheckout = Sequel.cariIsi("select tgl_keluar from kamar_inap where no_rawat='" + rs.getString("no_rawat") + "' and ( stts_pulang!='-' or stts_pulang!='Pindah Kamar')  ORDER BY tgl_keluar desc limit 1 ");

                    TabModePasienRanapTidakKlaim.addRow(new Object[]{
                        false,
                        rs.getString("no_rawat"),
                        rs.getString("no_rkm_medis"),
                        rs.getString("nm_pasien"),
                        kamar,
                        dpjp,
                        (rs.getString("no_sep") == null ? "-" : rs.getString("no_sep")),
                        (rs.getString("tglsep") == null ? "-" : rs.getString("tglsep")),
                        rs.getString("tgl_registrasi"),
                        (tglCheckout == null || tglCheckout.equals("") ? "-" : tglCheckout),
                        (rs.getString("catatan") == null ? "" : rs.getString("catatan")),
                        (rs.getString("tgl_input") == null ? "-" : rs.getString("tgl_input")),
                        (rs.getString("user_input") == null ? "-" : rs.getString("user_input"))
                    });
                }
            } catch (Exception ex) {
                System.out.println("tampilRanapTidakKlaim: " + ex);
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (Exception e) {
            System.out.println("tampilRanapTidakKlaim: " + e);
        }
        LCount.setText("" + tbListPasienRanapTidakKlaim.getRowCount());
        SepTerbit.setText("" + countSep);
        this.setCursor(Cursor.getDefaultCursor());
    }
}








