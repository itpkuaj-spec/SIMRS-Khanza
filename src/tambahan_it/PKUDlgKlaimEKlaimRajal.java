package tambahan_it;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.akses;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Color;
import javax.swing.JOptionPane;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.icepdf.ri.common.ComponentKeyBinding;
import org.icepdf.ri.common.SwingController;
import org.icepdf.ri.common.SwingViewBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import laporan.DlgDiagnosaPenyakit;
import org.json.JSONObject;
import rekammedis.RMRiwayatPerawatan;

public final class PKUDlgKlaimEKlaimRajal extends javax.swing.JDialog {
    private String nikCoder = ""; //tambahan
    private final DefaultTableModel tabModeDiagnosaPasien, tabModeProsedurePasien, tabModeDiagnosa, tabModeDiagnosaIDRG, tabModeProsedureIDRG, tabModeProsedure, tabModeDiagnosaINACBG, tabModeProsedureINACBG;
    private final validasi Valid = new validasi();
    private sekuel Sequel = new sekuel();
    private String fileIndividual="",allCmg = "", sourceProsedur = "", sourceDiagnosa = "", non_bedah = "0", bedah = "0", keperawatan = "0", konsultasi = "0", radiologi = "0", lab = "0", obat = "0", diagPasien = "", procedurePasien = "", nik = "", tensi = "", kodedokter = "", hakKelas = "", noKartu = "", jkPasien = "", tglLahir = "", jnsKlaim = "", tglAwal = "", tglAkhir = "", dokter = "", urlWebService = "", URL = "", status = "", norkmMedis = "", namaPasien = "", noSep = "", noorder = "", perujuk, halaman = "", norawat = "", auth, authEncrypt, requestJson, pemeriksaan, kdpenjab, kdpetugas, kamar, namakamar, pilihanCetak = "",dokter_rnp = "",kodedokter_rnp = "";
    private int i, j;
    private PreparedStatement ps, ps2;
    private ResultSet rs, rs2;
    private final Connection koneksi = koneksiDB.condb();
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private JsonNode root, response, responsetarif, responsegrouper, responseDiagnosa, responseProcedure, responseSpecialCmg, responseCmgOption;
    private JsonNode nameNode;
    private bridging.ApiEKlaim apiEKlaim = new bridging.ApiEKlaim();
    private SimpleDateFormat dateSave = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private boolean isLoading = false;
    public  DlgDiagnosaPenyakit diagnosa=new DlgDiagnosaPenyakit(null,false);
    private Date date = new Date();

    public PKUDlgKlaimEKlaimRajal(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        btnEditIDRG.setVisible(false);
        btnUpdateDataKlaim.setVisible(false);
        txtIDSITB.setVisible(false);
        btnValidasiSITB.setVisible(false);
        btnFinalIDRG.setVisible(false);
        btnFinalINACBG.setVisible(false);
        btnEditINACBG.setVisible(false);
        btnImportToINACBG.setVisible(false);
        FormInputHasilIDRG.setVisible(false);
        FormInputStatusKlaim.setVisible(false);
        panelNaikKelas.setVisible(false);
        panelTarifPoliEksekutif.setVisible(false);
        panelRawatIntensif.setVisible(false);

        internalFrameDiagnosaPasien.setSize(internalFrame1.getWidth(), 280);
        tabModeDiagnosaPasien = new DefaultTableModel(null, new Object[]{
            "Kode", "Nama Penyakit", "Status"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
//                if (colIndex == 0) {
//                    a = true;
//                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbDiagnosaPasien.setModel(tabModeDiagnosaPasien);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbDiagnosaPasien.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbDiagnosaPasien.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 3; i++) {
            TableColumn column = tbDiagnosaPasien.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(40);
            } else if (i == 1) {
                column.setPreferredWidth(400);
            } else if (i == 2) {
                column.setPreferredWidth(150);
            }
        }
        tbDiagnosaPasien.setDefaultRenderer(Object.class, new WarnaTableDiagnosa());
        tabModeProsedurePasien = new DefaultTableModel(null, new Object[]{
            "Kode", "Nama Penyakit", "Status", "Jumlah"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
//                if (colIndex == 0) {
//                    a = true;
//                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbProsedurePasien.setModel(tabModeProsedurePasien);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbProsedurePasien.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbProsedurePasien.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 3; i++) {
            TableColumn column = tbProsedurePasien.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(40);
            } else if (i == 1) {
                column.setPreferredWidth(400);
            } else if (i == 2) {
                column.setPreferredWidth(150);
            } else if (i == 3) {
                column.setPreferredWidth(80);   // Jumlah
            }
        }
        tbProsedurePasien.setDefaultRenderer(Object.class, new WarnaTableDiagnosa());

        tabModeDiagnosaINACBG = new DefaultTableModel(null, new Object[]{
            "Kode", "Nama Penyakit", "Status", "Validate", "Status Diagnosa"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
//                if (colIndex == 0) {
//                    a = true;
//                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbDiagnosaIINACBG.setModel(tabModeDiagnosaINACBG);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbDiagnosaIINACBG.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbDiagnosaIINACBG.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 5; i++) {
            TableColumn column = tbDiagnosaIINACBG.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(40);
            } else if (i == 1) {
                column.setPreferredWidth(400);
            } else if (i == 2) {
                column.setPreferredWidth(150);
            } else if (i == 3) {
                column.setPreferredWidth(150);
            } else if (i == 4) {
                column.setPreferredWidth(150);
            }
        }
        tbDiagnosaIINACBG.setDefaultRenderer(Object.class, new WarnaTableDiagnosaINACBG());
        tabModeProsedureINACBG = new DefaultTableModel(null, new Object[]{
            "Kode", "Nama Penyakit", "Status", "Validate", "Status Diagnosa", "Vol"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
//                if (colIndex == 0) {
//                    a = true;
//                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbProsedureINACBG.setModel(tabModeProsedureINACBG);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbProsedureINACBG.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbProsedureINACBG.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 6; i++) {
            TableColumn column = tbProsedureINACBG.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(40);
            } else if (i == 1) {
                column.setPreferredWidth(400);
            } else if (i == 2) {
                column.setPreferredWidth(150);
            } else if (i == 3) {
                column.setPreferredWidth(150);
            } else if (i == 4) {
                column.setPreferredWidth(150);
            } else if (i == 5) {
                column.setPreferredWidth(50);
            }
        }
        tbProsedureINACBG.setDefaultRenderer(Object.class, new WarnaTableDiagnosaINACBG());
        tabModeDiagnosaIDRG = new DefaultTableModel(null, new Object[]{
            "Kode", "Nama Penyakit", "Status"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
//                if (colIndex == 0) {
//                    a = true;
//                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbDiagnosaIDRG.setModel(tabModeDiagnosaIDRG);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbDiagnosaIDRG.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbDiagnosaIDRG.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 3; i++) {
            TableColumn column = tbDiagnosaIDRG.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(40);
            } else if (i == 1) {
                column.setPreferredWidth(400);
            } else if (i == 2) {
                column.setPreferredWidth(150);
            }
        }
        tbDiagnosaIDRG.setDefaultRenderer(Object.class, new WarnaTableDiagnosa());

        tabModeProsedure = new DefaultTableModel(null, new Object[]{
            "P", "Kode", "Nama Penyakit", "Validate", "Im"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
                if (colIndex == 0) {
                    a = true;
                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Boolean.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbProsedur.setModel(tabModeProsedure);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbProsedur.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbProsedur.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 5; i++) {
            TableColumn column = tbProsedur.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(60);
            } else if (i == 2) {
                column.setPreferredWidth(700);
            } else if (i == 3) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 4) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbProsedur.setDefaultRenderer(Object.class, new WarnaTableDiagnosaIDRG());
        tabModeProsedureIDRG = new DefaultTableModel(null, new Object[]{
            "Kode", "Nama Penyakit", "Status", "Volume","Priority"}) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                boolean a = false;
//                if (colIndex == 0) {
//                    a = true;
//                }
                return a;
            }
            Class[] types = new Class[]{
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbProsedurIDRG.setModel(tabModeProsedureIDRG);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbProsedurIDRG.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbProsedurIDRG.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 5; i++) {
            TableColumn column = tbProsedurIDRG.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setPreferredWidth(40);
            } else if (i == 1) {
                column.setPreferredWidth(400);
            } else if (i == 2) {
                column.setPreferredWidth(150);
            } else if (i == 3) {
                column.setPreferredWidth(80);
            } else if (i == 4) {
               column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbProsedurIDRG.setDefaultRenderer(Object.class, new WarnaTableDiagnosa());

        tabModeDiagnosa = new DefaultTableModel(null, new Object[]{
            "P", "Kode", "Nama Penyakit", "Validcode", "Accpdx", "code_asterisk", "asterisk", "im"}) {
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
                java.lang.Object.class, java.lang.Object.class, java.lang.Object.class, java.lang.Object.class
            };

            @Override
            public Class getColumnClass(int columnIndex) {
                return types[columnIndex];
            }
        };
        tbDiagnosa.setModel(tabModeDiagnosa);
        //tbPenyakit.setDefaultRenderer(Object.class, new WarnaTable(panelJudul.getBackground(),tbPenyakit.getBackground()));
        tbDiagnosa.setPreferredScrollableViewportSize(new Dimension(500, 500));
        tbDiagnosa.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        for (i = 0; i < 8; i++) {
            TableColumn column = tbDiagnosa.getColumnModel().getColumn(i);
            if (i == 0) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 1) {
                column.setPreferredWidth(40);
            } else if (i == 2) {
                column.setPreferredWidth(600);
            } else if (i == 3) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 4) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 5) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 6) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            } else if (i == 7) {
                column.setMinWidth(0);
                column.setMaxWidth(0);
            }
        }
        tbDiagnosa.setDefaultRenderer(Object.class, new WarnaTableDiagnosaIDRG());
//        TCariDiagnosa.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
//            @Override
//            public void insertUpdate(DocumentEvent e) {
//                if (TCariDiagnosa.getText().length() > 2) {
//                    tampilDiagnosa();
//                }
//            }
//
//            @Override
//            public void removeUpdate(DocumentEvent e) {
//                if (TCariDiagnosa.getText().length() > 2) {
//                    tampilDiagnosa();
//                }
//            }
//
//            @Override
//            public void changedUpdate(DocumentEvent e) {
//                if (TCariDiagnosa.getText().length() > 2) {
//                    tampilDiagnosa();
//                }
//            }
//        });

    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        WindowMultiplicityProcedure = new javax.swing.JDialog();
        internalFrame13 = new widget.InternalFrame();
        panelBiasa8 = new widget.PanelBiasa();
        BtnCloseInpindah2 = new widget.Button();
        BtnCloseInpindah3 = new widget.Button();
        FormInput3 = new widget.PanelBiasa();
        jLabel7 = new widget.Label();
        txtProcedureIDRGName = new widget.TextBox();
        txtProcedureIDRGCode = new widget.TextBox();
        jLabel9 = new widget.Label();
        CmbVol = new widget.ComboBox();
        txtPriority = new widget.TextBox();
        WindowHasilIDRG = new javax.swing.JDialog();
        internalFrame14 = new widget.InternalFrame();
        panelBiasa9 = new widget.PanelBiasa();
        BtnCloseInpindah5 = new widget.Button();
        FormInput4 = new widget.PanelBiasa();
        jLabel10 = new widget.Label();
        txtInfoHasilIDRG = new widget.TextBox();
        jLabel20 = new widget.Label();
        txtStatus = new widget.TextBox();
        jLabel33 = new widget.Label();
        txtMdcDescription = new widget.TextBox();
        jLabel34 = new widget.Label();
        txtDrgCode = new widget.TextBox();
        txtMdcCode = new widget.TextBox();
        txtDrgDescription = new widget.TextBox();
        ppProsedurIDRG = new javax.swing.JPopupMenu();
        btnMultiplicity = new javax.swing.JMenuItem();
        btnDeleteProcedureIDRG = new javax.swing.JMenuItem();
        ppDiagnosaIDRG = new javax.swing.JPopupMenu();
        btnDeleteDiagnosaIDRG = new javax.swing.JMenuItem();
        ppDiagnosaINACBG = new javax.swing.JPopupMenu();
        btnDeleteDiagnosaINACBG = new javax.swing.JMenuItem();
        ppProsedurINACBG = new javax.swing.JPopupMenu();
        btnDeleteProcedureINACBG = new javax.swing.JMenuItem();
        WindowSearchDiagnosa = new javax.swing.JDialog();
        internalFrame15 = new widget.InternalFrame();
        panelBiasa10 = new widget.PanelBiasa();
        jLabel136 = new widget.Label();
        FormCariDiagnosa = new widget.TextBox();
        BtnCari3 = new widget.Button();
        BtnCloseInpindah6 = new widget.Button();
        labelSumber = new widget.Label();
        FormInput5 = new widget.PanelBiasa();
        Scroll6 = new widget.ScrollPane();
        tbDiagnosa = new widget.Table();
        WindowSearchProsedur = new javax.swing.JDialog();
        internalFrame18 = new widget.InternalFrame();
        panelBiasa17 = new widget.PanelBiasa();
        jLabel137 = new widget.Label();
        TCariProsedur = new widget.TextBox();
        BtnCari2 = new widget.Button();
        BtnCloseInpindah9 = new widget.Button();
        labelSumberProsedur = new widget.Label();
        FormInput8 = new widget.PanelBiasa();
        Scroll2 = new widget.ScrollPane();
        tbProsedur = new widget.Table();
        WindowHasilINACBG = new javax.swing.JDialog();
        internalFrame16 = new widget.InternalFrame();
        panelBiasa11 = new widget.PanelBiasa();
        BtnCloseInpindah7 = new widget.Button();
        FormInput6 = new widget.PanelBiasa();
        jLabel15 = new widget.Label();
        txtInfoHasilINACBG = new widget.TextBox();
        jLabel37 = new widget.Label();
        txtjnsRawat = new widget.TextBox();
        jLabel38 = new widget.Label();
        txtGroupCost = new widget.TextBox();
        txtGroup = new widget.TextBox();
        txtGroupCode = new widget.TextBox();
        jLabel40 = new widget.Label();
        jLabel41 = new widget.Label();
        txtSubAcute = new widget.TextBox();
        txtSubAcuteCode = new widget.TextBox();
        jLabel44 = new widget.Label();
        txtSubAcuteCost = new widget.TextBox();
        jLabel52 = new widget.Label();
        txtCronicCost = new widget.TextBox();
        txtCronic = new widget.TextBox();
        txtCronicCode = new widget.TextBox();
        jLabel53 = new widget.Label();
        jLabel54 = new widget.Label();
        txtSpecProcedure = new widget.TextBox();
        txtSpecProcedureCode = new widget.TextBox();
        jLabel55 = new widget.Label();
        txtSpecProcedureCost = new widget.TextBox();
        jLabel56 = new widget.Label();
        txtSpecProthesisCost = new widget.TextBox();
        txtSpecProthesis = new widget.TextBox();
        txtSpecProthesisCode = new widget.TextBox();
        jLabel57 = new widget.Label();
        jLabel58 = new widget.Label();
        txtSpecInvestigation = new widget.TextBox();
        txtSpecInvestigationCode = new widget.TextBox();
        jLabel59 = new widget.Label();
        txtSpecInvestigationCost = new widget.TextBox();
        jLabel60 = new widget.Label();
        txtSpecDrugCost = new widget.TextBox();
        txtSpecDrug = new widget.TextBox();
        txtSpecDrugCode = new widget.TextBox();
        jLabel61 = new widget.Label();
        jLabel62 = new widget.Label();
        jLabel63 = new widget.Label();
        txtTotalKlaim = new widget.TextBox();
        jLabel64 = new widget.Label();
        txtStatusINACBG = new widget.TextBox();
        WindowHasilKlaim = new javax.swing.JDialog();
        internalFrame17 = new widget.InternalFrame();
        panelBiasa16 = new widget.PanelBiasa();
        BtnCloseInpindah8 = new widget.Button();
        FormInput7 = new widget.PanelBiasa();
        jLabel39 = new widget.Label();
        txtStatusKlaim = new widget.TextBox();
        jLabel65 = new widget.Label();
        txtStatus1 = new widget.TextBox();
        buttonGroupNaikKelas = new javax.swing.ButtonGroup();
        WindowDiagnosaDokter = new javax.swing.JDialog();
        internalFrame37 = new widget.InternalFrame();
        panelBiasa12 = new widget.PanelBiasa();
        btnImportToIDRG = new widget.Button();
        BtnCloseInpindah10 = new widget.Button();
        FormInput9 = new widget.PanelBiasa();
        internalFrameDiagnosaPasien = new widget.InternalFrame();
        internalFrame35 = new widget.InternalFrame();
        Scroll5 = new widget.ScrollPane();
        tbDiagnosaPasien = new widget.Table();
        Scroll20 = new widget.ScrollPane();
        tbProsedurePasien = new widget.Table();
        internalFrame36 = new widget.InternalFrame();
        WindowCetak = new javax.swing.JDialog();
        internalFrame38 = new widget.InternalFrame();
        panelBiasa13 = new widget.PanelBiasa();
        BtnCloseInpindah11 = new widget.Button();
        FormInput10 = new widget.PanelBiasa();
        internalFrameDiagnosaPasien1 = new widget.InternalFrame();
        internalFrame39 = new widget.InternalFrame();
        jScrollPane1 = new javax.swing.JScrollPane();
        internalFrame40 = new widget.InternalFrame();
        internalFrame1 = new widget.InternalFrame();
        scrollPane1 = new widget.ScrollPane();
        internalFrame4 = new widget.InternalFrame();
        internalFrameDataPasien = new widget.InternalFrame();
        internalFrameD1ataPasien = new widget.InternalFrame();
        FormInput = new widget.PanelBiasa();
        jLabel4 = new widget.Label();
        txtNoRawat = new widget.TextBox();
        txtNoRm = new widget.TextBox();
        txtNamaPasien = new widget.TextBox();
        noKartuJkn = new widget.TextBox();
        jLabel11 = new widget.Label();
        jLabel42 = new widget.Label();
        jLabel8 = new widget.Label();
        TglLahir = new widget.TextBox();
        jLabel12 = new widget.Label();
        Jk = new widget.TextBox();
        FormDataPelayanan = new widget.PanelBiasa();
        jLabel6 = new widget.Label();
        txtHakKelas = new widget.TextBox();
        jnsLayanan = new widget.TextBox();
        jLabel17 = new widget.Label();
        txtNoSep = new widget.TextBox();
        jLabel21 = new widget.Label();
        nmPoli = new widget.TextBox();
        kdPoli = new widget.TextBox();
        jamMasuk = new widget.TextBox();
        jLabel18 = new widget.Label();
        jLabel45 = new widget.Label();
        tglMasuk = new widget.TextBox();
        jLabel46 = new widget.Label();
        tglPulang = new widget.TextBox();
        jamPulang = new widget.TextBox();
        jLabel22 = new widget.Label();
        kdDokter = new widget.TextBox();
        nmDokter = new widget.TextBox();
        jLabel31 = new widget.Label();
        sistol = new widget.TextBox();
        diastol = new widget.TextBox();
        jLabel32 = new widget.Label();
        jLabel3 = new widget.Label();
        ChkPasienTB = new widget.CekBox();
        txtIDSITB = new widget.TextBox();
        btnValidasiSITB = new widget.Button();
        jLabel35 = new widget.Label();
        beratLahir = new widget.TextBox();
        jLabel36 = new widget.Label();
        jLabel72 = new widget.Label();
        panelNaikKelas = new widget.PanelBiasa();
        jLabel73 = new widget.Label();
        R1 = new widget.RadioButton();
        R2 = new widget.RadioButton();
        R3 = new widget.RadioButton();
        R4 = new widget.RadioButton();
        jLabel96 = new widget.Label();
        tarifPoliEksekutif2 = new widget.TextBox();
        panelCeklis = new widget.PanelBiasa();
        chkEksekutif = new widget.CekBox();
        jLabel70 = new widget.Label();
        chkNaikKelas = new widget.CekBox();
        jLabel71 = new widget.Label();
        chkIntensif = new widget.CekBox();
        jLabel97 = new widget.Label();
        panelTarifPoliEksekutif = new widget.PanelBiasa();
        costPoliEksekutif = new widget.Label();
        tarifPoliEksekutif = new widget.TextBox();
        jLabel102 = new widget.Label();
        jLabel103 = new widget.Label();
        jLabel98 = new widget.Label();
        jLabel100 = new widget.Label();
        subAcute = new widget.TextBox();
        jLabel101 = new widget.Label();
        chronic = new widget.TextBox();
        panelRawatIntensif = new widget.PanelBiasa();
        jLabel104 = new widget.Label();
        chkVentilator = new widget.CekBox();
        jLabel106 = new widget.Label();
        tglIntubasi = new widget.Tanggal();
        jLabel107 = new widget.Label();
        tglEkstubasi = new widget.Tanggal();
        jLabel105 = new widget.Label();
        icuLos = new widget.TextBox();
        cmbCaraMasuk = new widget.ComboBox();
        cmrCaraPulang = new widget.ComboBox();
        FormInput1 = new widget.PanelBiasa();
        jLabel5 = new widget.Label();
        tfTenagaAhli = new widget.TextBox();
        tfTotal = new widget.TextBox();
        jLabel14 = new widget.Label();
        jLabel43 = new widget.Label();
        tfNonBedah = new widget.TextBox();
        jLabel13 = new widget.Label();
        tfRadiologi = new widget.TextBox();
        jLabel47 = new widget.Label();
        tfRehabilitasi = new widget.TextBox();
        tfObat = new widget.TextBox();
        jLabel16 = new widget.Label();
        jLabel19 = new widget.Label();
        tfAlkes = new widget.TextBox();
        jLabel48 = new widget.Label();
        tfBedah = new widget.TextBox();
        tfKeperawatan = new widget.TextBox();
        jLabel23 = new widget.Label();
        jLabel24 = new widget.Label();
        tfLaboratorium = new widget.TextBox();
        jLabel49 = new widget.Label();
        tfKonsultasi = new widget.TextBox();
        tfPenunjang = new widget.TextBox();
        jLabel25 = new widget.Label();
        jLabel26 = new widget.Label();
        tfDarah = new widget.TextBox();
        jLabel50 = new widget.Label();
        jLabel27 = new widget.Label();
        jLabel28 = new widget.Label();
        tfBMHP = new widget.TextBox();
        tfObatKronis = new widget.TextBox();
        tfKamar = new widget.TextBox();
        jLabel51 = new widget.Label();
        tfRawatIntensif = new widget.TextBox();
        tfObatKemoterapi = new widget.TextBox();
        jLabel29 = new widget.Label();
        jLabel30 = new widget.Label();
        tfSewaAlat = new widget.TextBox();
        internalFrame8 = new widget.InternalFrame();
        setDataKlaim = new widget.Button();
        btnUpdateDataKlaim = new widget.Button();
        btnImportDiagnosaDokter = new widget.Button();
        btnDiagnosaLocal = new widget.Button();
        FrameKoding = new widget.InternalFrame();
        FormKodingIDRG = new widget.InternalFrame();
        FormInputIDRG = new widget.PanelBiasa();
        internalFrame9 = new widget.InternalFrame();
        internalFrame19 = new widget.InternalFrame();
        internalFrame27 = new widget.InternalFrame();
        internalFrame20 = new widget.InternalFrame();
        Scroll16 = new widget.ScrollPane();
        tbDiagnosaIDRG = new widget.Table();
        internalFrame21 = new widget.InternalFrame();
        internalFrame22 = new widget.InternalFrame();
        Scroll17 = new widget.ScrollPane();
        tbProsedurIDRG = new widget.Table();
        internalFrame5 = new widget.InternalFrame();
        btnGroupingIDRG = new widget.Button();
        btnTambahDiagnosaIDRG = new widget.Button();
        btnTambahProsedurIDRG = new widget.Button();
        FormInputHasilIDRG = new widget.PanelBiasa();
        internalFrame11 = new widget.InternalFrame();
        internalFrame28 = new widget.InternalFrame();
        internalFrame29 = new widget.InternalFrame();
        panelHasilIDRG = new widget.PanelBiasa();
        jLabel66 = new widget.Label();
        jLabel67 = new widget.Label();
        jLabel68 = new widget.Label();
        jLabel69 = new widget.Label();
        infoHasiliDRG = new widget.Label();
        jsnrawatHasiliDRG = new widget.Label();
        mdcCodeHasiliDRG = new widget.Label();
        mdcHasiliDRG = new widget.Label();
        drgHasiliDRG = new widget.Label();
        drgCodeHasiliDRG = new widget.Label();
        jLabel76 = new widget.Label();
        statusHasiliDRG = new widget.Label();
        btnFinalIDRG = new widget.Button();
        btnEditIDRG = new widget.Button();
        FormKodingINACBG = new widget.InternalFrame();
        FormInputINACBG = new widget.PanelBiasa();
        internalFrame10 = new widget.InternalFrame();
        internalFrame23 = new widget.InternalFrame();
        internalFrame24 = new widget.InternalFrame();
        Scroll18 = new widget.ScrollPane();
        tbDiagnosaIINACBG = new widget.Table();
        internalFrame25 = new widget.InternalFrame();
        internalFrame26 = new widget.InternalFrame();
        Scroll19 = new widget.ScrollPane();
        tbProsedureINACBG = new widget.Table();
        internalFrame6 = new widget.InternalFrame();
        btnImportToINACBG = new widget.Button();
        btnGroupingINACBG = new widget.Button();
        btnTabahDiagnosaINACBG = new widget.Button();
        btnTambahProsedurINACBG = new widget.Button();
        FormInputHasilINACBG = new widget.PanelBiasa();
        internalFrame12 = new widget.InternalFrame();
        internalFrame30 = new widget.InternalFrame();
        internalFrame31 = new widget.InternalFrame();
        panelHasilINACBG = new widget.PanelBiasa();
        jLabel74 = new widget.Label();
        jLabel75 = new widget.Label();
        jLabel78 = new widget.Label();
        jLabel79 = new widget.Label();
        jLabel80 = new widget.Label();
        jLabel81 = new widget.Label();
        jLabel82 = new widget.Label();
        jLabel83 = new widget.Label();
        jLabel84 = new widget.Label();
        jLabel85 = new widget.Label();
        jLabel86 = new widget.Label();
        btnFinalINACBG = new widget.Button();
        btnEditINACBG = new widget.Button();
        infoHasilINACBG = new widget.Label();
        jnsrawatHasilINACBG = new widget.Label();
        groupHasilINACBG = new widget.Label();
        groupCodeHasilINACBG = new widget.Label();
        groupCostHasilINACBG = new widget.Label();
        jLabel87 = new widget.Label();
        jLabel88 = new widget.Label();
        groupCostHasilINACBG1 = new widget.Label();
        groupCodeHasilINACBG1 = new widget.Label();
        subAcuteHasilINACBG = new widget.Label();
        chronicHasilINACBG = new widget.Label();
        groupCodeHasilINACBG2 = new widget.Label();
        jLabel89 = new widget.Label();
        groupCostHasilINACBG2 = new widget.Label();
        SpecialProcedureHasilCode = new widget.Label();
        jLabel90 = new widget.Label();
        SpecialProcedureHasilCost = new widget.Label();
        SpecialProsthesisHasilCode = new widget.Label();
        jLabel91 = new widget.Label();
        SpecialProsthesisHasilCost = new widget.Label();
        groupCodeHasilINACBG5 = new widget.Label();
        jLabel92 = new widget.Label();
        groupCostHasilINACBG5 = new widget.Label();
        statusHasilInacbg = new widget.Label();
        jLabel93 = new widget.Label();
        groupCostHasilINACBG6 = new widget.Label();
        CmbSpecialProcedure = new widget.ComboBox();
        CmbSpecialProsthesis = new widget.ComboBox();
        CmbSpecialInvestigation = new widget.ComboBox();
        CmbSpeciaDrugs = new widget.ComboBox();
        totalCostHasilKlaim = new widget.Label();
        jLabel94 = new widget.Label();
        groupCodeHasilINACBG7 = new widget.Label();
        FormInputStatusKlaim = new widget.PanelBiasa();
        internalFrame32 = new widget.InternalFrame();
        internalFrame33 = new widget.InternalFrame();
        internalFrame34 = new widget.InternalFrame();
        panelHasilINACBG1 = new widget.PanelBiasa();
        jLabel77 = new widget.Label();
        jLabel95 = new widget.Label();
        statusKlaim = new widget.Label();
        statusDC = new widget.Label();
        internalFrameEndKlaim = new widget.InternalFrame();
        btnEditKlaim = new widget.Button();
        btnFinalKlaim = new widget.Button();
        btnKirimOnline = new widget.Button();
        btnCetakKlaim = new widget.Button();
        btnHasilFinalKlaim = new widget.Button();
        Briwayatperawatan = new javax.swing.JButton();
        panelisi5 = new widget.panelisi();
        BtnKeluar1 = new widget.Button();

        WindowMultiplicityProcedure.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowMultiplicityProcedure.setName("WindowMultiplicityProcedure"); // NOI18N
        WindowMultiplicityProcedure.setUndecorated(true);
        WindowMultiplicityProcedure.setResizable(false);
        WindowMultiplicityProcedure.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                WindowMultiplicityProcedureWindowActivated(evt);
            }
        });

        internalFrame13.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame13.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Multiplicity Procedure ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame13.setAlignmentX(1.0F);
        internalFrame13.setAlignmentY(1.0F);
        internalFrame13.setName("internalFrame13"); // NOI18N
        internalFrame13.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame13.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame13.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa8.setName("panelBiasa8"); // NOI18N
        panelBiasa8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BtnCloseInpindah2.setBackground(new java.awt.Color(0, 51, 102));
        BtnCloseInpindah2.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah2.setMnemonic('U');
        BtnCloseInpindah2.setText("Simpan");
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
        panelBiasa8.add(BtnCloseInpindah3);

        internalFrame13.add(panelBiasa8, java.awt.BorderLayout.PAGE_END);

        FormInput3.setBorder(null);
        FormInput3.setName("FormInput3"); // NOI18N
        FormInput3.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput3.setLayout(null);

        jLabel7.setText("Procedure :");
        jLabel7.setName("jLabel7"); // NOI18N
        FormInput3.add(jLabel7);
        jLabel7.setBounds(0, 5, 70, 23);

        txtProcedureIDRGName.setEditable(false);
        txtProcedureIDRGName.setHighlighter(null);
        txtProcedureIDRGName.setName("txtProcedureIDRGName"); // NOI18N
        txtProcedureIDRGName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProcedureIDRGNameKeyPressed(evt);
            }
        });
        FormInput3.add(txtProcedureIDRGName);
        txtProcedureIDRGName.setBounds(190, 5, 480, 23);

        txtProcedureIDRGCode.setEditable(false);
        txtProcedureIDRGCode.setHighlighter(null);
        txtProcedureIDRGCode.setName("txtProcedureIDRGCode"); // NOI18N
        txtProcedureIDRGCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtProcedureIDRGCodeKeyPressed(evt);
            }
        });
        FormInput3.add(txtProcedureIDRGCode);
        txtProcedureIDRGCode.setBounds(80, 5, 110, 23);

        jLabel9.setText("Multiplicity :");
        jLabel9.setName("jLabel9"); // NOI18N
        FormInput3.add(jLabel9);
        jLabel9.setBounds(0, 35, 70, 23);

        CmbVol.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1", "2", "3", "4", "5", "6", "7", "8", "9" }));
        CmbVol.setName("CmbVol"); // NOI18N
        CmbVol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbVolKeyPressed(evt);
            }
        });
        FormInput3.add(CmbVol);
        CmbVol.setBounds(80, 35, 62, 23);

        txtPriority.setEditable(false);
        txtPriority.setHighlighter(null);
        txtPriority.setName("txtPriority"); // NOI18N
        txtPriority.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtPriorityKeyPressed(evt);
            }
        });
        FormInput3.add(txtPriority);
        txtPriority.setBounds(670, 10, 110, 23);

        internalFrame13.add(FormInput3, java.awt.BorderLayout.CENTER);

        WindowMultiplicityProcedure.getContentPane().add(internalFrame13, java.awt.BorderLayout.CENTER);

        WindowHasilIDRG.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowHasilIDRG.setName("WindowHasilIDRG"); // NOI18N
        WindowHasilIDRG.setUndecorated(true);
        WindowHasilIDRG.setResizable(false);
        WindowHasilIDRG.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                WindowHasilIDRGWindowActivated(evt);
            }
        });

        internalFrame14.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame14.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Hasil Grouping iDRG - Final ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame14.setAlignmentX(1.0F);
        internalFrame14.setAlignmentY(1.0F);
        internalFrame14.setName("internalFrame14"); // NOI18N
        internalFrame14.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame14.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame14.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa9.setName("panelBiasa9"); // NOI18N
        panelBiasa9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BtnCloseInpindah5.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah5.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah5.setMnemonic('U');
        BtnCloseInpindah5.setText("Keluar");
        BtnCloseInpindah5.setToolTipText("Alt+U");
        BtnCloseInpindah5.setName("BtnCloseInpindah5"); // NOI18N
        BtnCloseInpindah5.setOpaque(true);
        BtnCloseInpindah5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah5ActionPerformed(evt);
            }
        });
        BtnCloseInpindah5.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah5KeyPressed(evt);
            }
        });
        panelBiasa9.add(BtnCloseInpindah5);

        internalFrame14.add(panelBiasa9, java.awt.BorderLayout.PAGE_END);

        FormInput4.setBorder(null);
        FormInput4.setName("FormInput4"); // NOI18N
        FormInput4.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput4.setLayout(null);

        jLabel10.setText("Info :");
        jLabel10.setName("jLabel10"); // NOI18N
        FormInput4.add(jLabel10);
        jLabel10.setBounds(0, 5, 70, 23);

        txtInfoHasilIDRG.setEditable(false);
        txtInfoHasilIDRG.setHighlighter(null);
        txtInfoHasilIDRG.setName("txtInfoHasilIDRG"); // NOI18N
        txtInfoHasilIDRG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInfoHasilIDRGKeyPressed(evt);
            }
        });
        FormInput4.add(txtInfoHasilIDRG);
        txtInfoHasilIDRG.setBounds(80, 5, 590, 23);

        jLabel20.setText("Status :");
        jLabel20.setName("jLabel20"); // NOI18N
        FormInput4.add(jLabel20);
        jLabel20.setBounds(0, 35, 70, 23);

        txtStatus.setEditable(false);
        txtStatus.setHighlighter(null);
        txtStatus.setName("txtStatus"); // NOI18N
        txtStatus.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtStatusKeyPressed(evt);
            }
        });
        FormInput4.add(txtStatus);
        txtStatus.setBounds(80, 35, 590, 23);

        jLabel33.setText("MDC :");
        jLabel33.setName("jLabel33"); // NOI18N
        FormInput4.add(jLabel33);
        jLabel33.setBounds(0, 65, 70, 23);

        txtMdcDescription.setEditable(false);
        txtMdcDescription.setHighlighter(null);
        txtMdcDescription.setName("txtMdcDescription"); // NOI18N
        txtMdcDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMdcDescriptionKeyPressed(evt);
            }
        });
        FormInput4.add(txtMdcDescription);
        txtMdcDescription.setBounds(140, 65, 530, 23);

        jLabel34.setText("DRG :");
        jLabel34.setName("jLabel34"); // NOI18N
        FormInput4.add(jLabel34);
        jLabel34.setBounds(0, 95, 70, 23);

        txtDrgCode.setEditable(false);
        txtDrgCode.setHighlighter(null);
        txtDrgCode.setName("txtDrgCode"); // NOI18N
        txtDrgCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDrgCodeKeyPressed(evt);
            }
        });
        FormInput4.add(txtDrgCode);
        txtDrgCode.setBounds(80, 95, 90, 23);

        txtMdcCode.setEditable(false);
        txtMdcCode.setHighlighter(null);
        txtMdcCode.setName("txtMdcCode"); // NOI18N
        txtMdcCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtMdcCodeKeyPressed(evt);
            }
        });
        FormInput4.add(txtMdcCode);
        txtMdcCode.setBounds(80, 65, 60, 23);

        txtDrgDescription.setEditable(false);
        txtDrgDescription.setHighlighter(null);
        txtDrgDescription.setName("txtDrgDescription"); // NOI18N
        txtDrgDescription.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtDrgDescriptionKeyPressed(evt);
            }
        });
        FormInput4.add(txtDrgDescription);
        txtDrgDescription.setBounds(170, 95, 500, 23);

        internalFrame14.add(FormInput4, java.awt.BorderLayout.CENTER);

        WindowHasilIDRG.getContentPane().add(internalFrame14, java.awt.BorderLayout.CENTER);

        ppProsedurIDRG.setName("ppProsedurIDRG"); // NOI18N

        btnMultiplicity.setBackground(new java.awt.Color(255, 255, 254));
        btnMultiplicity.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        btnMultiplicity.setForeground(new java.awt.Color(50, 50, 50));
        btnMultiplicity.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        btnMultiplicity.setText("Multiplicity Procedure");
        btnMultiplicity.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnMultiplicity.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnMultiplicity.setName("btnMultiplicity"); // NOI18N
        btnMultiplicity.setPreferredSize(new java.awt.Dimension(250, 25));
        btnMultiplicity.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMultiplicityActionPerformed(evt);
            }
        });
        ppProsedurIDRG.add(btnMultiplicity);

        btnDeleteProcedureIDRG.setBackground(new java.awt.Color(255, 255, 254));
        btnDeleteProcedureIDRG.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        btnDeleteProcedureIDRG.setForeground(new java.awt.Color(50, 50, 50));
        btnDeleteProcedureIDRG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        btnDeleteProcedureIDRG.setText("Hapus");
        btnDeleteProcedureIDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDeleteProcedureIDRG.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnDeleteProcedureIDRG.setName("btnDeleteProcedureIDRG"); // NOI18N
        btnDeleteProcedureIDRG.setPreferredSize(new java.awt.Dimension(250, 25));
        btnDeleteProcedureIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProcedureIDRGActionPerformed(evt);
            }
        });
        ppProsedurIDRG.add(btnDeleteProcedureIDRG);

        ppDiagnosaIDRG.setName("ppDiagnosaIDRG"); // NOI18N

        btnDeleteDiagnosaIDRG.setBackground(new java.awt.Color(255, 255, 254));
        btnDeleteDiagnosaIDRG.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        btnDeleteDiagnosaIDRG.setForeground(new java.awt.Color(50, 50, 50));
        btnDeleteDiagnosaIDRG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        btnDeleteDiagnosaIDRG.setText("Hapus");
        btnDeleteDiagnosaIDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDeleteDiagnosaIDRG.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnDeleteDiagnosaIDRG.setName("btnDeleteDiagnosaIDRG"); // NOI18N
        btnDeleteDiagnosaIDRG.setPreferredSize(new java.awt.Dimension(250, 25));
        btnDeleteDiagnosaIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDiagnosaIDRGActionPerformed(evt);
            }
        });
        ppDiagnosaIDRG.add(btnDeleteDiagnosaIDRG);

        ppDiagnosaINACBG.setName("ppDiagnosaINACBG"); // NOI18N

        btnDeleteDiagnosaINACBG.setBackground(new java.awt.Color(255, 255, 254));
        btnDeleteDiagnosaINACBG.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        btnDeleteDiagnosaINACBG.setForeground(new java.awt.Color(50, 50, 50));
        btnDeleteDiagnosaINACBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        btnDeleteDiagnosaINACBG.setText("Hapus");
        btnDeleteDiagnosaINACBG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDeleteDiagnosaINACBG.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnDeleteDiagnosaINACBG.setName("btnDeleteDiagnosaINACBG"); // NOI18N
        btnDeleteDiagnosaINACBG.setPreferredSize(new java.awt.Dimension(250, 25));
        btnDeleteDiagnosaINACBG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDiagnosaINACBGActionPerformed(evt);
            }
        });
        ppDiagnosaINACBG.add(btnDeleteDiagnosaINACBG);

        ppProsedurINACBG.setName("ppProsedurINACBG"); // NOI18N

        btnDeleteProcedureINACBG.setBackground(new java.awt.Color(255, 255, 254));
        btnDeleteProcedureINACBG.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        btnDeleteProcedureINACBG.setForeground(new java.awt.Color(50, 50, 50));
        btnDeleteProcedureINACBG.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/item.png"))); // NOI18N
        btnDeleteProcedureINACBG.setText("Hapus");
        btnDeleteProcedureINACBG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnDeleteProcedureINACBG.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        btnDeleteProcedureINACBG.setName("btnDeleteProcedureINACBG"); // NOI18N
        btnDeleteProcedureINACBG.setPreferredSize(new java.awt.Dimension(250, 25));
        btnDeleteProcedureINACBG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteProcedureINACBGActionPerformed(evt);
            }
        });
        ppProsedurINACBG.add(btnDeleteProcedureINACBG);

        WindowSearchDiagnosa.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowSearchDiagnosa.setName("WindowSearchDiagnosa"); // NOI18N
        WindowSearchDiagnosa.setUndecorated(true);
        WindowSearchDiagnosa.setResizable(false);
        WindowSearchDiagnosa.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                WindowSearchDiagnosaWindowActivated(evt);
            }
        });

        internalFrame15.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame15.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Daftar Diagnosa ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame15.setAlignmentX(1.0F);
        internalFrame15.setAlignmentY(1.0F);
        internalFrame15.setName("internalFrame15"); // NOI18N
        internalFrame15.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame15.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame15.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa10.setName("panelBiasa10"); // NOI18N
        panelBiasa10.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel136.setText("Cari ICD :");
        jLabel136.setName("jLabel136"); // NOI18N
        panelBiasa10.add(jLabel136);

        FormCariDiagnosa.setName("FormCariDiagnosa"); // NOI18N
        FormCariDiagnosa.setPreferredSize(new java.awt.Dimension(250, 24));
        FormCariDiagnosa.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                FormCariDiagnosaKeyPressed(evt);
            }
        });
        panelBiasa10.add(FormCariDiagnosa);

        BtnCari3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari3.setMnemonic('7');
        BtnCari3.setToolTipText("Alt+7");
        BtnCari3.setName("BtnCari3"); // NOI18N
        BtnCari3.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari3ActionPerformed(evt);
            }
        });
        BtnCari3.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCari3KeyPressed(evt);
            }
        });
        panelBiasa10.add(BtnCari3);

        BtnCloseInpindah6.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah6.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah6.setMnemonic('U');
        BtnCloseInpindah6.setText("Keluar");
        BtnCloseInpindah6.setToolTipText("Alt+U");
        BtnCloseInpindah6.setName("BtnCloseInpindah6"); // NOI18N
        BtnCloseInpindah6.setOpaque(true);
        BtnCloseInpindah6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah6ActionPerformed(evt);
            }
        });
        BtnCloseInpindah6.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah6KeyPressed(evt);
            }
        });
        panelBiasa10.add(BtnCloseInpindah6);

        labelSumber.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelSumber.setText("Sumber :");
        labelSumber.setName("labelSumber"); // NOI18N
        labelSumber.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBiasa10.add(labelSumber);

        internalFrame15.add(panelBiasa10, java.awt.BorderLayout.PAGE_END);

        FormInput5.setBorder(null);
        FormInput5.setName("FormInput5"); // NOI18N
        FormInput5.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput5.setLayout(new java.awt.BorderLayout());

        Scroll6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        Scroll6.setName("Scroll6"); // NOI18N
        Scroll6.setOpaque(true);

        tbDiagnosa.setName("tbDiagnosa"); // NOI18N
        tbDiagnosa.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDiagnosaMouseClicked(evt);
            }
        });
        Scroll6.setViewportView(tbDiagnosa);

        FormInput5.add(Scroll6, java.awt.BorderLayout.CENTER);

        internalFrame15.add(FormInput5, java.awt.BorderLayout.CENTER);

        WindowSearchDiagnosa.getContentPane().add(internalFrame15, java.awt.BorderLayout.CENTER);

        WindowSearchProsedur.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowSearchProsedur.setName("WindowSearchProsedur"); // NOI18N
        WindowSearchProsedur.setUndecorated(true);
        WindowSearchProsedur.setResizable(false);
        WindowSearchProsedur.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                WindowSearchProsedurWindowActivated(evt);
            }
        });

        internalFrame18.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame18.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Daftar Prosedur ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame18.setAlignmentX(1.0F);
        internalFrame18.setAlignmentY(1.0F);
        internalFrame18.setName("internalFrame18"); // NOI18N
        internalFrame18.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame18.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame18.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa17.setName("panelBiasa17"); // NOI18N
        panelBiasa17.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel137.setText("Cari ICD IX :");
        jLabel137.setName("jLabel137"); // NOI18N
        panelBiasa17.add(jLabel137);

        TCariProsedur.setName("TCariProsedur"); // NOI18N
        TCariProsedur.setPreferredSize(new java.awt.Dimension(250, 24));
        TCariProsedur.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                TCariProsedurKeyPressed(evt);
            }
        });
        panelBiasa17.add(TCariProsedur);

        BtnCari2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari2.setMnemonic('7');
        BtnCari2.setToolTipText("Alt+7");
        BtnCari2.setName("BtnCari2"); // NOI18N
        BtnCari2.setPreferredSize(new java.awt.Dimension(28, 23));
        BtnCari2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCari2ActionPerformed(evt);
            }
        });
        BtnCari2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCari2KeyPressed(evt);
            }
        });
        panelBiasa17.add(BtnCari2);

        BtnCloseInpindah9.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah9.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah9.setMnemonic('U');
        BtnCloseInpindah9.setText("Keluar");
        BtnCloseInpindah9.setToolTipText("Alt+U");
        BtnCloseInpindah9.setName("BtnCloseInpindah9"); // NOI18N
        BtnCloseInpindah9.setOpaque(true);
        BtnCloseInpindah9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah9ActionPerformed(evt);
            }
        });
        BtnCloseInpindah9.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah9KeyPressed(evt);
            }
        });
        panelBiasa17.add(BtnCloseInpindah9);

        labelSumberProsedur.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        labelSumberProsedur.setText("Sumber :");
        labelSumberProsedur.setName("labelSumberProsedur"); // NOI18N
        labelSumberProsedur.setPreferredSize(new java.awt.Dimension(200, 14));
        panelBiasa17.add(labelSumberProsedur);

        internalFrame18.add(panelBiasa17, java.awt.BorderLayout.PAGE_END);

        FormInput8.setBorder(null);
        FormInput8.setName("FormInput8"); // NOI18N
        FormInput8.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput8.setLayout(new java.awt.BorderLayout());

        Scroll2.setBorder(javax.swing.BorderFactory.createTitledBorder("List Prosedur"));
        Scroll2.setName("Scroll2"); // NOI18N
        Scroll2.setOpaque(true);

        tbProsedur.setName("tbProsedur"); // NOI18N
        tbProsedur.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProsedurMouseClicked(evt);
            }
        });
        Scroll2.setViewportView(tbProsedur);

        FormInput8.add(Scroll2, java.awt.BorderLayout.CENTER);

        internalFrame18.add(FormInput8, java.awt.BorderLayout.CENTER);

        WindowSearchProsedur.getContentPane().add(internalFrame18, java.awt.BorderLayout.CENTER);

        WindowHasilINACBG.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowHasilINACBG.setName("WindowHasilINACBG"); // NOI18N
        WindowHasilINACBG.setUndecorated(true);
        WindowHasilINACBG.setResizable(false);
        WindowHasilINACBG.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                WindowHasilINACBGWindowActivated(evt);
            }
        });

        internalFrame16.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame16.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Hasil Grouping INACBG - Final ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame16.setAlignmentX(1.0F);
        internalFrame16.setAlignmentY(1.0F);
        internalFrame16.setName("internalFrame16"); // NOI18N
        internalFrame16.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame16.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame16.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa11.setName("panelBiasa11"); // NOI18N
        panelBiasa11.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BtnCloseInpindah7.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah7.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah7.setMnemonic('U');
        BtnCloseInpindah7.setText("Keluar");
        BtnCloseInpindah7.setToolTipText("Alt+U");
        BtnCloseInpindah7.setName("BtnCloseInpindah7"); // NOI18N
        BtnCloseInpindah7.setOpaque(true);
        BtnCloseInpindah7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah7ActionPerformed(evt);
            }
        });
        BtnCloseInpindah7.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah7KeyPressed(evt);
            }
        });
        panelBiasa11.add(BtnCloseInpindah7);

        internalFrame16.add(panelBiasa11, java.awt.BorderLayout.PAGE_END);

        FormInput6.setBorder(null);
        FormInput6.setName("FormInput6"); // NOI18N
        FormInput6.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput6.setLayout(null);

        jLabel15.setText("Info :");
        jLabel15.setName("jLabel15"); // NOI18N
        FormInput6.add(jLabel15);
        jLabel15.setBounds(0, 5, 120, 23);

        txtInfoHasilINACBG.setEditable(false);
        txtInfoHasilINACBG.setHighlighter(null);
        txtInfoHasilINACBG.setName("txtInfoHasilINACBG"); // NOI18N
        txtInfoHasilINACBG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtInfoHasilINACBGKeyPressed(evt);
            }
        });
        FormInput6.add(txtInfoHasilINACBG);
        txtInfoHasilINACBG.setBounds(120, 10, 660, 23);

        jLabel37.setText("Jenis Rawat :");
        jLabel37.setName("jLabel37"); // NOI18N
        FormInput6.add(jLabel37);
        jLabel37.setBounds(0, 35, 120, 23);

        txtjnsRawat.setEditable(false);
        txtjnsRawat.setHighlighter(null);
        txtjnsRawat.setName("txtjnsRawat"); // NOI18N
        txtjnsRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtjnsRawatKeyPressed(evt);
            }
        });
        FormInput6.add(txtjnsRawat);
        txtjnsRawat.setBounds(120, 40, 660, 23);

        jLabel38.setText("Rp. ");
        jLabel38.setName("jLabel38"); // NOI18N
        FormInput6.add(jLabel38);
        jLabel38.setBounds(650, 70, 30, 23);

        txtGroupCost.setEditable(false);
        txtGroupCost.setHighlighter(null);
        txtGroupCost.setName("txtGroupCost"); // NOI18N
        txtGroupCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGroupCostKeyPressed(evt);
            }
        });
        FormInput6.add(txtGroupCost);
        txtGroupCost.setBounds(680, 70, 160, 23);

        txtGroup.setEditable(false);
        txtGroup.setHighlighter(null);
        txtGroup.setName("txtGroup"); // NOI18N
        txtGroup.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGroupKeyPressed(evt);
            }
        });
        FormInput6.add(txtGroup);
        txtGroup.setBounds(120, 70, 370, 23);

        txtGroupCode.setEditable(false);
        txtGroupCode.setHighlighter(null);
        txtGroupCode.setName("txtGroupCode"); // NOI18N
        txtGroupCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtGroupCodeKeyPressed(evt);
            }
        });
        FormInput6.add(txtGroupCode);
        txtGroupCode.setBounds(490, 70, 160, 23);

        jLabel40.setText("Group :");
        jLabel40.setName("jLabel40"); // NOI18N
        FormInput6.add(jLabel40);
        jLabel40.setBounds(0, 65, 120, 23);

        jLabel41.setText("Sub Acute :");
        jLabel41.setName("jLabel41"); // NOI18N
        FormInput6.add(jLabel41);
        jLabel41.setBounds(0, 95, 120, 23);

        txtSubAcute.setEditable(false);
        txtSubAcute.setHighlighter(null);
        txtSubAcute.setName("txtSubAcute"); // NOI18N
        txtSubAcute.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubAcuteKeyPressed(evt);
            }
        });
        FormInput6.add(txtSubAcute);
        txtSubAcute.setBounds(120, 95, 370, 23);

        txtSubAcuteCode.setEditable(false);
        txtSubAcuteCode.setHighlighter(null);
        txtSubAcuteCode.setName("txtSubAcuteCode"); // NOI18N
        txtSubAcuteCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubAcuteCodeKeyPressed(evt);
            }
        });
        FormInput6.add(txtSubAcuteCode);
        txtSubAcuteCode.setBounds(490, 95, 160, 23);

        jLabel44.setText("Rp. ");
        jLabel44.setName("jLabel44"); // NOI18N
        FormInput6.add(jLabel44);
        jLabel44.setBounds(650, 95, 30, 23);

        txtSubAcuteCost.setEditable(false);
        txtSubAcuteCost.setHighlighter(null);
        txtSubAcuteCost.setName("txtSubAcuteCost"); // NOI18N
        txtSubAcuteCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSubAcuteCostKeyPressed(evt);
            }
        });
        FormInput6.add(txtSubAcuteCost);
        txtSubAcuteCost.setBounds(680, 95, 160, 23);

        jLabel52.setText("Rp. ");
        jLabel52.setName("jLabel52"); // NOI18N
        FormInput6.add(jLabel52);
        jLabel52.setBounds(650, 120, 30, 23);

        txtCronicCost.setEditable(false);
        txtCronicCost.setHighlighter(null);
        txtCronicCost.setName("txtCronicCost"); // NOI18N
        txtCronicCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCronicCostKeyPressed(evt);
            }
        });
        FormInput6.add(txtCronicCost);
        txtCronicCost.setBounds(680, 120, 160, 23);

        txtCronic.setEditable(false);
        txtCronic.setHighlighter(null);
        txtCronic.setName("txtCronic"); // NOI18N
        txtCronic.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCronicKeyPressed(evt);
            }
        });
        FormInput6.add(txtCronic);
        txtCronic.setBounds(120, 120, 370, 23);

        txtCronicCode.setEditable(false);
        txtCronicCode.setHighlighter(null);
        txtCronicCode.setName("txtCronicCode"); // NOI18N
        txtCronicCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtCronicCodeKeyPressed(evt);
            }
        });
        FormInput6.add(txtCronicCode);
        txtCronicCode.setBounds(490, 120, 160, 23);

        jLabel53.setText("Chronic :");
        jLabel53.setName("jLabel53"); // NOI18N
        FormInput6.add(jLabel53);
        jLabel53.setBounds(0, 120, 120, 23);

        jLabel54.setText("Special Procedure :");
        jLabel54.setName("jLabel54"); // NOI18N
        FormInput6.add(jLabel54);
        jLabel54.setBounds(0, 145, 120, 23);

        txtSpecProcedure.setEditable(false);
        txtSpecProcedure.setHighlighter(null);
        txtSpecProcedure.setName("txtSpecProcedure"); // NOI18N
        txtSpecProcedure.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecProcedureKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecProcedure);
        txtSpecProcedure.setBounds(120, 145, 370, 23);

        txtSpecProcedureCode.setEditable(false);
        txtSpecProcedureCode.setHighlighter(null);
        txtSpecProcedureCode.setName("txtSpecProcedureCode"); // NOI18N
        txtSpecProcedureCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecProcedureCodeKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecProcedureCode);
        txtSpecProcedureCode.setBounds(490, 145, 160, 23);

        jLabel55.setText("Rp. ");
        jLabel55.setName("jLabel55"); // NOI18N
        FormInput6.add(jLabel55);
        jLabel55.setBounds(650, 145, 30, 23);

        txtSpecProcedureCost.setEditable(false);
        txtSpecProcedureCost.setHighlighter(null);
        txtSpecProcedureCost.setName("txtSpecProcedureCost"); // NOI18N
        txtSpecProcedureCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecProcedureCostKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecProcedureCost);
        txtSpecProcedureCost.setBounds(680, 145, 160, 23);

        jLabel56.setText("Rp. ");
        jLabel56.setName("jLabel56"); // NOI18N
        FormInput6.add(jLabel56);
        jLabel56.setBounds(650, 170, 30, 23);

        txtSpecProthesisCost.setEditable(false);
        txtSpecProthesisCost.setHighlighter(null);
        txtSpecProthesisCost.setName("txtSpecProthesisCost"); // NOI18N
        txtSpecProthesisCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecProthesisCostKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecProthesisCost);
        txtSpecProthesisCost.setBounds(680, 170, 160, 23);

        txtSpecProthesis.setEditable(false);
        txtSpecProthesis.setHighlighter(null);
        txtSpecProthesis.setName("txtSpecProthesis"); // NOI18N
        txtSpecProthesis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecProthesisKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecProthesis);
        txtSpecProthesis.setBounds(120, 170, 370, 23);

        txtSpecProthesisCode.setEditable(false);
        txtSpecProthesisCode.setHighlighter(null);
        txtSpecProthesisCode.setName("txtSpecProthesisCode"); // NOI18N
        txtSpecProthesisCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecProthesisCodeKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecProthesisCode);
        txtSpecProthesisCode.setBounds(490, 170, 160, 23);

        jLabel57.setText("Special Prosthesis :");
        jLabel57.setName("jLabel57"); // NOI18N
        FormInput6.add(jLabel57);
        jLabel57.setBounds(0, 170, 120, 23);

        jLabel58.setText("Special Innvestigation :");
        jLabel58.setName("jLabel58"); // NOI18N
        FormInput6.add(jLabel58);
        jLabel58.setBounds(0, 195, 120, 23);

        txtSpecInvestigation.setEditable(false);
        txtSpecInvestigation.setHighlighter(null);
        txtSpecInvestigation.setName("txtSpecInvestigation"); // NOI18N
        txtSpecInvestigation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecInvestigationKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecInvestigation);
        txtSpecInvestigation.setBounds(120, 195, 370, 23);

        txtSpecInvestigationCode.setEditable(false);
        txtSpecInvestigationCode.setHighlighter(null);
        txtSpecInvestigationCode.setName("txtSpecInvestigationCode"); // NOI18N
        txtSpecInvestigationCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecInvestigationCodeKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecInvestigationCode);
        txtSpecInvestigationCode.setBounds(490, 195, 160, 23);

        jLabel59.setText("Rp. ");
        jLabel59.setName("jLabel59"); // NOI18N
        FormInput6.add(jLabel59);
        jLabel59.setBounds(650, 195, 30, 23);

        txtSpecInvestigationCost.setEditable(false);
        txtSpecInvestigationCost.setHighlighter(null);
        txtSpecInvestigationCost.setName("txtSpecInvestigationCost"); // NOI18N
        txtSpecInvestigationCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecInvestigationCostKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecInvestigationCost);
        txtSpecInvestigationCost.setBounds(680, 195, 160, 23);

        jLabel60.setText("Rp. ");
        jLabel60.setName("jLabel60"); // NOI18N
        FormInput6.add(jLabel60);
        jLabel60.setBounds(650, 220, 30, 23);

        txtSpecDrugCost.setEditable(false);
        txtSpecDrugCost.setHighlighter(null);
        txtSpecDrugCost.setName("txtSpecDrugCost"); // NOI18N
        txtSpecDrugCost.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecDrugCostKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecDrugCost);
        txtSpecDrugCost.setBounds(680, 220, 160, 23);

        txtSpecDrug.setEditable(false);
        txtSpecDrug.setHighlighter(null);
        txtSpecDrug.setName("txtSpecDrug"); // NOI18N
        txtSpecDrug.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecDrugKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecDrug);
        txtSpecDrug.setBounds(120, 220, 370, 23);

        txtSpecDrugCode.setEditable(false);
        txtSpecDrugCode.setHighlighter(null);
        txtSpecDrugCode.setName("txtSpecDrugCode"); // NOI18N
        txtSpecDrugCode.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtSpecDrugCodeKeyPressed(evt);
            }
        });
        FormInput6.add(txtSpecDrugCode);
        txtSpecDrugCode.setBounds(490, 220, 160, 23);

        jLabel61.setText("Special Drugs :");
        jLabel61.setName("jLabel61"); // NOI18N
        FormInput6.add(jLabel61);
        jLabel61.setBounds(0, 220, 120, 23);

        jLabel62.setText("Total Klaim :");
        jLabel62.setName("jLabel62"); // NOI18N
        FormInput6.add(jLabel62);
        jLabel62.setBounds(0, 245, 650, 23);

        jLabel63.setText("Rp. ");
        jLabel63.setName("jLabel63"); // NOI18N
        FormInput6.add(jLabel63);
        jLabel63.setBounds(650, 245, 30, 23);

        txtTotalKlaim.setEditable(false);
        txtTotalKlaim.setHighlighter(null);
        txtTotalKlaim.setName("txtTotalKlaim"); // NOI18N
        txtTotalKlaim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtTotalKlaimKeyPressed(evt);
            }
        });
        FormInput6.add(txtTotalKlaim);
        txtTotalKlaim.setBounds(680, 245, 160, 23);

        jLabel64.setText("Status :");
        jLabel64.setName("jLabel64"); // NOI18N
        FormInput6.add(jLabel64);
        jLabel64.setBounds(0, 270, 120, 23);

        txtStatusINACBG.setEditable(false);
        txtStatusINACBG.setHighlighter(null);
        txtStatusINACBG.setName("txtStatusINACBG"); // NOI18N
        txtStatusINACBG.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtStatusINACBGKeyPressed(evt);
            }
        });
        FormInput6.add(txtStatusINACBG);
        txtStatusINACBG.setBounds(120, 270, 370, 23);

        internalFrame16.add(FormInput6, java.awt.BorderLayout.CENTER);

        WindowHasilINACBG.getContentPane().add(internalFrame16, java.awt.BorderLayout.CENTER);

        WindowHasilKlaim.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowHasilKlaim.setName("WindowHasilKlaim"); // NOI18N
        WindowHasilKlaim.setUndecorated(true);
        WindowHasilKlaim.setResizable(false);
        WindowHasilKlaim.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                WindowHasilKlaimWindowActivated(evt);
            }
        });

        internalFrame17.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame17.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Hasil Grouping iDRG - Final ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame17.setAlignmentX(1.0F);
        internalFrame17.setAlignmentY(1.0F);
        internalFrame17.setName("internalFrame17"); // NOI18N
        internalFrame17.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame17.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame17.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa16.setName("panelBiasa16"); // NOI18N
        panelBiasa16.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BtnCloseInpindah8.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah8.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah8.setMnemonic('U');
        BtnCloseInpindah8.setText("Keluar");
        BtnCloseInpindah8.setToolTipText("Alt+U");
        BtnCloseInpindah8.setName("BtnCloseInpindah8"); // NOI18N
        BtnCloseInpindah8.setOpaque(true);
        BtnCloseInpindah8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah8ActionPerformed(evt);
            }
        });
        BtnCloseInpindah8.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah8KeyPressed(evt);
            }
        });
        panelBiasa16.add(BtnCloseInpindah8);

        internalFrame17.add(panelBiasa16, java.awt.BorderLayout.PAGE_END);

        FormInput7.setBorder(null);
        FormInput7.setName("FormInput7"); // NOI18N
        FormInput7.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput7.setLayout(null);

        jLabel39.setText("Status Klaim :");
        jLabel39.setName("jLabel39"); // NOI18N
        FormInput7.add(jLabel39);
        jLabel39.setBounds(0, 5, 100, 23);

        txtStatusKlaim.setEditable(false);
        txtStatusKlaim.setHighlighter(null);
        txtStatusKlaim.setName("txtStatusKlaim"); // NOI18N
        txtStatusKlaim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtStatusKlaimKeyPressed(evt);
            }
        });
        FormInput7.add(txtStatusKlaim);
        txtStatusKlaim.setBounds(100, 5, 570, 23);

        jLabel65.setText("Status DC Kemkes :");
        jLabel65.setName("jLabel65"); // NOI18N
        FormInput7.add(jLabel65);
        jLabel65.setBounds(0, 35, 100, 23);

        txtStatus1.setEditable(false);
        txtStatus1.setHighlighter(null);
        txtStatus1.setName("txtStatus1"); // NOI18N
        txtStatus1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtStatus1KeyPressed(evt);
            }
        });
        FormInput7.add(txtStatus1);
        txtStatus1.setBounds(100, 35, 570, 23);

        internalFrame17.add(FormInput7, java.awt.BorderLayout.CENTER);

        WindowHasilKlaim.getContentPane().add(internalFrame17, java.awt.BorderLayout.CENTER);

        WindowDiagnosaDokter.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowDiagnosaDokter.setName("WindowDiagnosaDokter"); // NOI18N
        WindowDiagnosaDokter.setUndecorated(true);
        WindowDiagnosaDokter.setResizable(false);
        WindowDiagnosaDokter.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                WindowDiagnosaDokterWindowActivated(evt);
            }
        });

        internalFrame37.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame37.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Multiplicity Procedure ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame37.setAlignmentX(1.0F);
        internalFrame37.setAlignmentY(1.0F);
        internalFrame37.setName("internalFrame37"); // NOI18N
        internalFrame37.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame37.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame37.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa12.setName("panelBiasa12"); // NOI18N
        panelBiasa12.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnImportToIDRG.setBackground(new java.awt.Color(0, 0, 102));
        btnImportToIDRG.setForeground(new java.awt.Color(255, 255, 255));
        btnImportToIDRG.setMnemonic('4');
        btnImportToIDRG.setText("Import to iDRG ");
        btnImportToIDRG.setToolTipText("Alt+4");
        btnImportToIDRG.setName("btnImportToIDRG"); // NOI18N
        btnImportToIDRG.setOpaque(true);
        btnImportToIDRG.setPreferredSize(new java.awt.Dimension(150, 23));
        btnImportToIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportToIDRGActionPerformed(evt);
            }
        });
        panelBiasa12.add(btnImportToIDRG);

        BtnCloseInpindah10.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah10.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah10.setMnemonic('U');
        BtnCloseInpindah10.setText("Keluar");
        BtnCloseInpindah10.setToolTipText("Alt+U");
        BtnCloseInpindah10.setName("BtnCloseInpindah10"); // NOI18N
        BtnCloseInpindah10.setOpaque(true);
        BtnCloseInpindah10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah10ActionPerformed(evt);
            }
        });
        BtnCloseInpindah10.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah10KeyPressed(evt);
            }
        });
        panelBiasa12.add(BtnCloseInpindah10);

        internalFrame37.add(panelBiasa12, java.awt.BorderLayout.PAGE_END);

        FormInput9.setBorder(null);
        FormInput9.setName("FormInput9"); // NOI18N
        FormInput9.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput9.setLayout(new java.awt.BorderLayout());

        internalFrameDiagnosaPasien.setBorder(null);
        internalFrameDiagnosaPasien.setName("internalFrameDiagnosaPasien"); // NOI18N
        internalFrameDiagnosaPasien.setLayout(new java.awt.BorderLayout());

        internalFrame35.setName("internalFrame35"); // NOI18N
        internalFrame35.setLayout(new java.awt.GridLayout(1, 0));

        Scroll5.setBorder(javax.swing.BorderFactory.createTitledBorder("List Diagnosa"));
        Scroll5.setName("Scroll5"); // NOI18N
        Scroll5.setOpaque(true);

        tbDiagnosaPasien.setName("tbDiagnosaPasien"); // NOI18N
        tbDiagnosaPasien.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbDiagnosaPasienMouseClicked(evt);
            }
        });
        Scroll5.setViewportView(tbDiagnosaPasien);

        internalFrame35.add(Scroll5);

        Scroll20.setBorder(javax.swing.BorderFactory.createTitledBorder("List Prosedur"));
        Scroll20.setName("Scroll20"); // NOI18N
        Scroll20.setOpaque(true);

        tbProsedurePasien.setName("tbProsedurePasien"); // NOI18N
        Scroll20.setViewportView(tbProsedurePasien);

        internalFrame35.add(Scroll20);

        internalFrameDiagnosaPasien.add(internalFrame35, java.awt.BorderLayout.CENTER);

        internalFrame36.setName("internalFrame36"); // NOI18N
        internalFrame36.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        internalFrameDiagnosaPasien.add(internalFrame36, java.awt.BorderLayout.PAGE_END);

        FormInput9.add(internalFrameDiagnosaPasien, java.awt.BorderLayout.CENTER);

        internalFrame37.add(FormInput9, java.awt.BorderLayout.CENTER);

        WindowDiagnosaDokter.getContentPane().add(internalFrame37, java.awt.BorderLayout.CENTER);

        WindowCetak.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        WindowCetak.setName("WindowCetak"); // NOI18N
        WindowCetak.setUndecorated(true);
        WindowCetak.setResizable(false);
        WindowCetak.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowActivated(java.awt.event.WindowEvent evt) {
                WindowCetakWindowActivated(evt);
            }
        });

        internalFrame38.setBackground(new java.awt.Color(255, 255, 255));
        internalFrame38.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(215, 225, 205)), "::[ Individual Klaim ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame38.setAlignmentX(1.0F);
        internalFrame38.setAlignmentY(1.0F);
        internalFrame38.setName("internalFrame38"); // NOI18N
        internalFrame38.setWarnaAtas(new java.awt.Color(0, 51, 102));
        internalFrame38.setWarnaBawah(new java.awt.Color(0, 102, 102));
        internalFrame38.setLayout(new java.awt.BorderLayout(1, 1));

        panelBiasa13.setName("panelBiasa13"); // NOI18N
        panelBiasa13.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        BtnCloseInpindah11.setBackground(new java.awt.Color(255, 51, 0));
        BtnCloseInpindah11.setForeground(new java.awt.Color(255, 255, 255));
        BtnCloseInpindah11.setMnemonic('U');
        BtnCloseInpindah11.setText("Keluar");
        BtnCloseInpindah11.setToolTipText("Alt+U");
        BtnCloseInpindah11.setName("BtnCloseInpindah11"); // NOI18N
        BtnCloseInpindah11.setOpaque(true);
        BtnCloseInpindah11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnCloseInpindah11ActionPerformed(evt);
            }
        });
        BtnCloseInpindah11.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnCloseInpindah11KeyPressed(evt);
            }
        });
        panelBiasa13.add(BtnCloseInpindah11);

        internalFrame38.add(panelBiasa13, java.awt.BorderLayout.PAGE_END);

        FormInput10.setBorder(null);
        FormInput10.setName("FormInput10"); // NOI18N
        FormInput10.setPreferredSize(new java.awt.Dimension(865, 137));
        FormInput10.setLayout(new java.awt.BorderLayout());

        internalFrameDiagnosaPasien1.setBorder(null);
        internalFrameDiagnosaPasien1.setName("internalFrameDiagnosaPasien1"); // NOI18N
        internalFrameDiagnosaPasien1.setLayout(new java.awt.BorderLayout());

        internalFrame39.setName("internalFrame39"); // NOI18N
        internalFrame39.setLayout(new java.awt.BorderLayout());

        jScrollPane1.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(204, 204, 204), 1, true));
        jScrollPane1.setName("jScrollPane1"); // NOI18N
        internalFrame39.add(jScrollPane1, java.awt.BorderLayout.CENTER);

        internalFrameDiagnosaPasien1.add(internalFrame39, java.awt.BorderLayout.CENTER);

        internalFrame40.setName("internalFrame40"); // NOI18N
        internalFrame40.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));
        internalFrameDiagnosaPasien1.add(internalFrame40, java.awt.BorderLayout.PAGE_END);

        FormInput10.add(internalFrameDiagnosaPasien1, java.awt.BorderLayout.CENTER);

        internalFrame38.add(FormInput10, java.awt.BorderLayout.CENTER);

        WindowCetak.getContentPane().add(internalFrame38, java.awt.BorderLayout.CENTER);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setModalityType(null);
        setUndecorated(true);
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Detail Klaim ] ::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Lucida Grande", 1, 13), new java.awt.Color(255, 255, 255))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setPreferredSize(new java.awt.Dimension(800, 803));
        internalFrame1.setWarnaAtas(new java.awt.Color(65, 105, 225));
        internalFrame1.setWarnaBawah(new java.awt.Color(65, 105, 225));
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        scrollPane1.setName("scrollPane1"); // NOI18N
        scrollPane1.setPreferredSize(new java.awt.Dimension(600, 2502));

        internalFrame4.setName("internalFrame4"); // NOI18N
        internalFrame4.setPreferredSize(new java.awt.Dimension(1900, 2500));
        internalFrame4.setLayout(null);

        internalFrameDataPasien.setName("internalFrameDataPasien"); // NOI18N
        internalFrameDataPasien.setPreferredSize(new java.awt.Dimension(492, 574));
        internalFrameDataPasien.setLayout(new java.awt.BorderLayout());

        internalFrameD1ataPasien.setName("internalFrameD1ataPasien"); // NOI18N
        internalFrameD1ataPasien.setPreferredSize(new java.awt.Dimension(492, 574));
        internalFrameD1ataPasien.setLayout(new java.awt.BorderLayout());

        FormInput.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Pasien"));
        FormInput.setName("FormInput"); // NOI18N
        FormInput.setPreferredSize(new java.awt.Dimension(490, 115));
        FormInput.setLayout(null);

        jLabel4.setText("No. Rawat :");
        jLabel4.setName("jLabel4"); // NOI18N
        FormInput.add(jLabel4);
        jLabel4.setBounds(0, 20, 70, 23);

        txtNoRawat.setEditable(false);
        txtNoRawat.setHighlighter(null);
        txtNoRawat.setName("txtNoRawat"); // NOI18N
        txtNoRawat.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoRawatKeyPressed(evt);
            }
        });
        FormInput.add(txtNoRawat);
        txtNoRawat.setBounds(80, 20, 320, 23);

        txtNoRm.setHighlighter(null);
        txtNoRm.setName("txtNoRm"); // NOI18N
        txtNoRm.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtNoRmKeyPressed(evt);
            }
        });
        FormInput.add(txtNoRm);
        txtNoRm.setBounds(520, 20, 110, 23);

        txtNamaPasien.setEditable(false);
        txtNamaPasien.setHighlighter(null);
        txtNamaPasien.setName("txtNamaPasien"); // NOI18N
        FormInput.add(txtNamaPasien);
        txtNamaPasien.setBounds(632, 20, 270, 23);

        noKartuJkn.setName("noKartuJkn"); // NOI18N
        noKartuJkn.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                noKartuJknKeyPressed(evt);
            }
        });
        FormInput.add(noKartuJkn);
        noKartuJkn.setBounds(80, 50, 320, 23);

        jLabel11.setText("No.Rekam Medik :");
        jLabel11.setName("jLabel11"); // NOI18N
        FormInput.add(jLabel11);
        jLabel11.setBounds(416, 20, 100, 23);

        jLabel42.setText("No. Kartu: ");
        jLabel42.setName("jLabel42"); // NOI18N
        FormInput.add(jLabel42);
        jLabel42.setBounds(0, 50, 70, 23);

        jLabel8.setText("Tgl.Lahir :");
        jLabel8.setName("jLabel8"); // NOI18N
        FormInput.add(jLabel8);
        jLabel8.setBounds(460, 50, 60, 23);

        TglLahir.setEditable(false);
        TglLahir.setHighlighter(null);
        TglLahir.setName("TglLahir"); // NOI18N
        FormInput.add(TglLahir);
        TglLahir.setBounds(520, 50, 110, 23);

        jLabel12.setText("J.K. :");
        jLabel12.setName("jLabel12"); // NOI18N
        FormInput.add(jLabel12);
        jLabel12.setBounds(640, 50, 30, 23);

        Jk.setEditable(false);
        Jk.setHighlighter(null);
        Jk.setName("Jk"); // NOI18N
        FormInput.add(Jk);
        Jk.setBounds(670, 50, 80, 23);

        internalFrameD1ataPasien.add(FormInput, java.awt.BorderLayout.PAGE_START);

        FormDataPelayanan.setBorder(javax.swing.BorderFactory.createTitledBorder("Data Pelayanan"));
        FormDataPelayanan.setName("FormDataPelayanan"); // NOI18N
        FormDataPelayanan.setPreferredSize(new java.awt.Dimension(490, 200));
        FormDataPelayanan.setLayout(null);

        jLabel6.setText("Jns. Layanan :");
        jLabel6.setName("jLabel6"); // NOI18N
        FormDataPelayanan.add(jLabel6);
        jLabel6.setBounds(0, 20, 80, 23);

        txtHakKelas.setName("txtHakKelas"); // NOI18N
        FormDataPelayanan.add(txtHakKelas);
        txtHakKelas.setBounds(139, 80, 280, 23);

        jnsLayanan.setEditable(false);
        jnsLayanan.setHighlighter(null);
        jnsLayanan.setName("jnsLayanan"); // NOI18N
        jnsLayanan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jnsLayananKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(jnsLayanan);
        jnsLayanan.setBounds(90, 20, 150, 23);

        jLabel17.setText("Kelas Rawat / Hak Kelas :");
        jLabel17.setName("jLabel17"); // NOI18N
        FormDataPelayanan.add(jLabel17);
        jLabel17.setBounds(0, 80, 130, 23);

        txtNoSep.setEditable(false);
        txtNoSep.setHighlighter(null);
        txtNoSep.setName("txtNoSep"); // NOI18N
        FormDataPelayanan.add(txtNoSep);
        txtNoSep.setBounds(660, 20, 380, 23);

        jLabel21.setText("Unit :");
        jLabel21.setName("jLabel21"); // NOI18N
        FormDataPelayanan.add(jLabel21);
        jLabel21.setBounds(580, 50, 70, 23);

        nmPoli.setEditable(false);
        nmPoli.setName("nmPoli"); // NOI18N
        FormDataPelayanan.add(nmPoli);
        nmPoli.setBounds(720, 50, 320, 23);

        kdPoli.setEditable(false);
        kdPoli.setHighlighter(null);
        kdPoli.setName("kdPoli"); // NOI18N
        kdPoli.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdPoliKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(kdPoli);
        kdPoli.setBounds(650, 50, 66, 23);

        jamMasuk.setName("jamMasuk"); // NOI18N
        jamMasuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jamMasukKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(jamMasuk);
        jamMasuk.setBounds(190, 50, 100, 23);

        jLabel18.setText("No. SEP :");
        jLabel18.setName("jLabel18"); // NOI18N
        FormDataPelayanan.add(jLabel18);
        jLabel18.setBounds(590, 20, 60, 23);

        jLabel45.setText("Tgl. Masuk :");
        jLabel45.setName("jLabel45"); // NOI18N
        FormDataPelayanan.add(jLabel45);
        jLabel45.setBounds(0, 50, 80, 23);

        tglMasuk.setName("tglMasuk"); // NOI18N
        tglMasuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tglMasukKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(tglMasuk);
        tglMasuk.setBounds(90, 50, 100, 23);

        jLabel46.setText("Tgl. Pulang :");
        jLabel46.setName("jLabel46"); // NOI18N
        FormDataPelayanan.add(jLabel46);
        jLabel46.setBounds(290, 50, 80, 23);

        tglPulang.setName("tglPulang"); // NOI18N
        tglPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tglPulangKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(tglPulang);
        tglPulang.setBounds(370, 50, 100, 23);

        jamPulang.setName("jamPulang"); // NOI18N
        jamPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                jamPulangKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(jamPulang);
        jamPulang.setBounds(470, 50, 100, 23);

        jLabel22.setText("DPJP :");
        jLabel22.setName("jLabel22"); // NOI18N
        FormDataPelayanan.add(jLabel22);
        jLabel22.setBounds(0, 110, 70, 23);

        kdDokter.setEditable(false);
        kdDokter.setHighlighter(null);
        kdDokter.setName("kdDokter"); // NOI18N
        kdDokter.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                kdDokterKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(kdDokter);
        kdDokter.setBounds(80, 110, 107, 23);

        nmDokter.setEditable(false);
        nmDokter.setName("nmDokter"); // NOI18N
        FormDataPelayanan.add(nmDokter);
        nmDokter.setBounds(190, 110, 230, 23);

        jLabel31.setText("Diastol :");
        jLabel31.setName("jLabel31"); // NOI18N
        FormDataPelayanan.add(jLabel31);
        jLabel31.setBounds(810, 110, 90, 23);

        sistol.setHighlighter(null);
        sistol.setName("sistol"); // NOI18N
        sistol.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                sistolKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(sistol);
        sistol.setBounds(730, 110, 107, 23);

        diastol.setName("diastol"); // NOI18N
        FormDataPelayanan.add(diastol);
        diastol.setBounds(900, 110, 140, 23);

        jLabel32.setText("Sistol :");
        jLabel32.setName("jLabel32"); // NOI18N
        FormDataPelayanan.add(jLabel32);
        jLabel32.setBounds(650, 110, 70, 23);

        jLabel3.setText("Pasien TB :");
        jLabel3.setName("jLabel3"); // NOI18N
        FormDataPelayanan.add(jLabel3);
        jLabel3.setBounds(0, 140, 70, 23);

        ChkPasienTB.setBorder(null);
        ChkPasienTB.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        ChkPasienTB.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ChkPasienTB.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        ChkPasienTB.setName("ChkPasienTB"); // NOI18N
        ChkPasienTB.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                ChkPasienTBItemStateChanged(evt);
            }
        });
        FormDataPelayanan.add(ChkPasienTB);
        ChkPasienTB.setBounds(70, 140, 23, 23);

        txtIDSITB.setHighlighter(null);
        txtIDSITB.setName("txtIDSITB"); // NOI18N
        txtIDSITB.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtIDSITBKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(txtIDSITB);
        txtIDSITB.setBounds(95, 140, 210, 23);

        btnValidasiSITB.setBackground(new java.awt.Color(0, 51, 102));
        btnValidasiSITB.setForeground(new java.awt.Color(255, 255, 255));
        btnValidasiSITB.setMnemonic('4');
        btnValidasiSITB.setText("Validasi");
        btnValidasiSITB.setToolTipText("Alt+4");
        btnValidasiSITB.setName("btnValidasiSITB"); // NOI18N
        btnValidasiSITB.setOpaque(true);
        btnValidasiSITB.setPreferredSize(new java.awt.Dimension(150, 23));
        btnValidasiSITB.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidasiSITBActionPerformed(evt);
            }
        });
        FormDataPelayanan.add(btnValidasiSITB);
        btnValidasiSITB.setBounds(310, 140, 110, 23);

        jLabel35.setText("Kg");
        jLabel35.setName("jLabel35"); // NOI18N
        FormDataPelayanan.add(jLabel35);
        jLabel35.setBounds(610, 110, 20, 23);

        beratLahir.setText("0");
        beratLahir.setHighlighter(null);
        beratLahir.setName("beratLahir"); // NOI18N
        beratLahir.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                beratLahirKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(beratLahir);
        beratLahir.setBounds(500, 110, 107, 23);

        jLabel36.setText("Berat Lahir :");
        jLabel36.setName("jLabel36"); // NOI18N
        FormDataPelayanan.add(jLabel36);
        jLabel36.setBounds(420, 110, 70, 23);

        jLabel72.setText("Cara. Pulang :");
        jLabel72.setName("jLabel72"); // NOI18N
        FormDataPelayanan.add(jLabel72);
        jLabel72.setBounds(730, 80, 80, 23);

        panelNaikKelas.setBorder(null);
        panelNaikKelas.setName("panelNaikKelas"); // NOI18N
        panelNaikKelas.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel73.setText("Kelas Layanan :");
        jLabel73.setName("jLabel73"); // NOI18N
        panelNaikKelas.add(jLabel73);

        buttonGroupNaikKelas.add(R1);
        R1.setText("Kelas 3");
        R1.setName("R1"); // NOI18N
        R1.setPreferredSize(new java.awt.Dimension(80, 20));
        panelNaikKelas.add(R1);

        buttonGroupNaikKelas.add(R2);
        R2.setText("Kelas 2");
        R2.setName("R2"); // NOI18N
        R2.setPreferredSize(new java.awt.Dimension(80, 16));
        panelNaikKelas.add(R2);

        buttonGroupNaikKelas.add(R3);
        R3.setText("Kelas 1");
        R3.setName("R3"); // NOI18N
        R3.setPreferredSize(new java.awt.Dimension(80, 16));
        panelNaikKelas.add(R3);

        buttonGroupNaikKelas.add(R4);
        R4.setText("Diatas Kelas 1");
        R4.setName("R4"); // NOI18N
        R4.setPreferredSize(new java.awt.Dimension(90, 16));
        panelNaikKelas.add(R4);

        jLabel96.setText("Lama (hari) :");
        jLabel96.setName("jLabel96"); // NOI18N
        jLabel96.setPreferredSize(new java.awt.Dimension(150, 14));
        panelNaikKelas.add(jLabel96);

        tarifPoliEksekutif2.setHighlighter(null);
        tarifPoliEksekutif2.setName("tarifPoliEksekutif2"); // NOI18N
        tarifPoliEksekutif2.setPreferredSize(new java.awt.Dimension(100, 24));
        tarifPoliEksekutif2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tarifPoliEksekutif2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tarifPoliEksekutif2KeyReleased(evt);
            }
        });
        panelNaikKelas.add(tarifPoliEksekutif2);

        FormDataPelayanan.add(panelNaikKelas);
        panelNaikKelas.setBounds(20, 170, 780, 30);

        panelCeklis.setBorder(null);
        panelCeklis.setName("panelCeklis"); // NOI18N
        panelCeklis.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        chkEksekutif.setBorder(null);
        chkEksekutif.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chkEksekutif.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chkEksekutif.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        chkEksekutif.setName("chkEksekutif"); // NOI18N
        chkEksekutif.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkEksekutifItemStateChanged(evt);
            }
        });
        panelCeklis.add(chkEksekutif);

        jLabel70.setText("Kelas Eksekutif");
        jLabel70.setName("jLabel70"); // NOI18N
        panelCeklis.add(jLabel70);

        chkNaikKelas.setBorder(null);
        chkNaikKelas.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chkNaikKelas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chkNaikKelas.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        chkNaikKelas.setName("chkNaikKelas"); // NOI18N
        chkNaikKelas.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkNaikKelasItemStateChanged(evt);
            }
        });
        panelCeklis.add(chkNaikKelas);

        jLabel71.setText("Naik/Turun Kelas");
        jLabel71.setName("jLabel71"); // NOI18N
        panelCeklis.add(jLabel71);

        chkIntensif.setBorder(null);
        chkIntensif.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chkIntensif.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chkIntensif.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        chkIntensif.setName("chkIntensif"); // NOI18N
        chkIntensif.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkIntensifItemStateChanged(evt);
            }
        });
        panelCeklis.add(chkIntensif);

        jLabel97.setText("Ada Rawat Intensif");
        jLabel97.setName("jLabel97"); // NOI18N
        panelCeklis.add(jLabel97);

        FormDataPelayanan.add(panelCeklis);
        panelCeklis.setBounds(240, 20, 320, 30);

        panelTarifPoliEksekutif.setBorder(null);
        panelTarifPoliEksekutif.setName("panelTarifPoliEksekutif"); // NOI18N
        panelTarifPoliEksekutif.setLayout(null);

        costPoliEksekutif.setText("0");
        costPoliEksekutif.setName("costPoliEksekutif"); // NOI18N
        panelTarifPoliEksekutif.add(costPoliEksekutif);
        costPoliEksekutif.setBounds(330, 5, 130, 23);

        tarifPoliEksekutif.setText("0");
        tarifPoliEksekutif.setHighlighter(null);
        tarifPoliEksekutif.setName("tarifPoliEksekutif"); // NOI18N
        tarifPoliEksekutif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tarifPoliEksekutifKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tarifPoliEksekutifKeyReleased(evt);
            }
        });
        panelTarifPoliEksekutif.add(tarifPoliEksekutif);
        tarifPoliEksekutif.setBounds(120, 5, 170, 23);

        jLabel102.setText("Tarif Poli Eksekutif :");
        jLabel102.setName("jLabel102"); // NOI18N
        panelTarifPoliEksekutif.add(jLabel102);
        jLabel102.setBounds(0, 5, 110, 23);

        jLabel103.setText("Rp. ");
        jLabel103.setName("jLabel103"); // NOI18N
        panelTarifPoliEksekutif.add(jLabel103);
        jLabel103.setBounds(300, 5, 30, 23);

        FormDataPelayanan.add(panelTarifPoliEksekutif);
        panelTarifPoliEksekutif.setBounds(20, 170, 620, 30);

        jLabel98.setText("Cara. Masuk :");
        jLabel98.setName("jLabel98"); // NOI18N
        FormDataPelayanan.add(jLabel98);
        jLabel98.setBounds(420, 80, 80, 23);

        jLabel100.setText("Sub Acute :");
        jLabel100.setName("jLabel100"); // NOI18N
        FormDataPelayanan.add(jLabel100);
        jLabel100.setBounds(440, 140, 60, 23);

        subAcute.setText("-");
        subAcute.setHighlighter(null);
        subAcute.setName("subAcute"); // NOI18N
        subAcute.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                subAcuteKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(subAcute);
        subAcute.setBounds(500, 140, 140, 23);

        jLabel101.setText("Chronic :");
        jLabel101.setName("jLabel101"); // NOI18N
        FormDataPelayanan.add(jLabel101);
        jLabel101.setBounds(640, 140, 60, 23);

        chronic.setText("-");
        chronic.setHighlighter(null);
        chronic.setName("chronic"); // NOI18N
        chronic.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                chronicKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(chronic);
        chronic.setBounds(700, 140, 140, 23);

        panelRawatIntensif.setBorder(null);
        panelRawatIntensif.setName("panelRawatIntensif"); // NOI18N
        panelRawatIntensif.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        jLabel104.setText("Ventilator :");
        jLabel104.setName("jLabel104"); // NOI18N
        panelRawatIntensif.add(jLabel104);

        chkVentilator.setBorder(null);
        chkVentilator.setText("Ya");
        chkVentilator.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chkVentilator.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        chkVentilator.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        chkVentilator.setName("chkVentilator"); // NOI18N
        chkVentilator.setPreferredSize(new java.awt.Dimension(40, 14));
        chkVentilator.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                chkVentilatorItemStateChanged(evt);
            }
        });
        panelRawatIntensif.add(chkVentilator);

        jLabel106.setText("Intubasi :");
        jLabel106.setName("jLabel106"); // NOI18N
        jLabel106.setPreferredSize(new java.awt.Dimension(100, 14));
        panelRawatIntensif.add(jLabel106);

        tglIntubasi.setForeground(new java.awt.Color(50, 70, 50));
        tglIntubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "28-10-2025 10:42:13" }));
        tglIntubasi.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        tglIntubasi.setName("tglIntubasi"); // NOI18N
        tglIntubasi.setOpaque(false);
        tglIntubasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tglIntubasiKeyPressed(evt);
            }
        });
        panelRawatIntensif.add(tglIntubasi);

        jLabel107.setText("Ekstubasi :");
        jLabel107.setName("jLabel107"); // NOI18N
        jLabel107.setPreferredSize(new java.awt.Dimension(80, 14));
        panelRawatIntensif.add(jLabel107);

        tglEkstubasi.setForeground(new java.awt.Color(50, 70, 50));
        tglEkstubasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "28-10-2025 10:42:14" }));
        tglEkstubasi.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        tglEkstubasi.setName("tglEkstubasi"); // NOI18N
        tglEkstubasi.setOpaque(false);
        tglEkstubasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tglEkstubasiKeyPressed(evt);
            }
        });
        panelRawatIntensif.add(tglEkstubasi);

        jLabel105.setText("Rawat Intensif  (hari) :");
        jLabel105.setName("jLabel105"); // NOI18N
        jLabel105.setPreferredSize(new java.awt.Dimension(150, 14));
        panelRawatIntensif.add(jLabel105);

        icuLos.setToolTipText("Untuk perbaikan tarif: Masukan total jumlah hari rawat intensif");
        icuLos.setHighlighter(null);
        icuLos.setName("icuLos"); // NOI18N
        icuLos.setPreferredSize(new java.awt.Dimension(100, 24));
        icuLos.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                icuLosKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                icuLosKeyReleased(evt);
            }
        });
        panelRawatIntensif.add(icuLos);

        FormDataPelayanan.add(panelRawatIntensif);
        panelRawatIntensif.setBounds(20, 210, 1080, 30);

        cmbCaraMasuk.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Rujukan FKTP", "Rujukan FKRTL", "Rujukan Spesialis", "Dari Rawat Jalan", "Dari Rawat Inap", "Dari Rawat Darurat", "Lahir di RS", "Rujukan Panti Jompo", "Rujukan dari RS Jiwa", "Rujukan Fasilitas Rehab", "Lain-lain" }));
        cmbCaraMasuk.setName("cmbCaraMasuk"); // NOI18N
        cmbCaraMasuk.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbCaraMasukItemStateChanged(evt);
            }
        });
        cmbCaraMasuk.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmbCaraMasukKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(cmbCaraMasuk);
        cmbCaraMasuk.setBounds(500, 80, 230, 23);

        cmrCaraPulang.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Atas persetujuan dokter", "Dirujuk", "Atas permintaan sendiri", "Meninggal", "Lain-lain" }));
        cmrCaraPulang.setName("cmrCaraPulang"); // NOI18N
        cmrCaraPulang.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmrCaraPulangItemStateChanged(evt);
            }
        });
        cmrCaraPulang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cmrCaraPulangKeyPressed(evt);
            }
        });
        FormDataPelayanan.add(cmrCaraPulang);
        cmrCaraPulang.setBounds(810, 80, 230, 23);

        internalFrameD1ataPasien.add(FormDataPelayanan, java.awt.BorderLayout.CENTER);

        FormInput1.setBorder(javax.swing.BorderFactory.createTitledBorder("Tarif Layanan"));
        FormInput1.setName("FormInput1"); // NOI18N
        FormInput1.setPreferredSize(new java.awt.Dimension(490, 250));
        FormInput1.setLayout(null);

        jLabel5.setText("Total :");
        jLabel5.setName("jLabel5"); // NOI18N
        FormInput1.add(jLabel5);
        jLabel5.setBounds(0, 20, 80, 23);

        tfTenagaAhli.setName("tfTenagaAhli"); // NOI18N
        FormInput1.add(tfTenagaAhli);
        tfTenagaAhli.setBounds(129, 80, 270, 23);

        tfTotal.setEditable(false);
        tfTotal.setForeground(new java.awt.Color(255, 0, 51));
        tfTotal.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        tfTotal.setHighlighter(null);
        tfTotal.setName("tfTotal"); // NOI18N
        tfTotal.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfTotalKeyPressed(evt);
            }
        });
        FormInput1.add(tfTotal);
        tfTotal.setBounds(90, 20, 310, 23);

        jLabel14.setText("Tenaga Ahli :");
        jLabel14.setName("jLabel14"); // NOI18N
        FormInput1.add(jLabel14);
        jLabel14.setBounds(0, 80, 130, 23);

        jLabel43.setText("Prosedur Non Bedah :");
        jLabel43.setName("jLabel43"); // NOI18N
        FormInput1.add(jLabel43);
        jLabel43.setBounds(0, 50, 130, 23);

        tfNonBedah.setName("tfNonBedah"); // NOI18N
        tfNonBedah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfNonBedahKeyPressed(evt);
            }
        });
        FormInput1.add(tfNonBedah);
        tfNonBedah.setBounds(130, 50, 270, 23);

        jLabel13.setText("Radiologi :");
        jLabel13.setName("jLabel13"); // NOI18N
        FormInput1.add(jLabel13);
        jLabel13.setBounds(0, 110, 130, 23);

        tfRadiologi.setName("tfRadiologi"); // NOI18N
        FormInput1.add(tfRadiologi);
        tfRadiologi.setBounds(129, 110, 270, 23);

        jLabel47.setText("Rehabilitasi :");
        jLabel47.setName("jLabel47"); // NOI18N
        FormInput1.add(jLabel47);
        jLabel47.setBounds(0, 140, 130, 23);

        tfRehabilitasi.setName("tfRehabilitasi"); // NOI18N
        tfRehabilitasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfRehabilitasiKeyPressed(evt);
            }
        });
        FormInput1.add(tfRehabilitasi);
        tfRehabilitasi.setBounds(130, 140, 270, 23);

        tfObat.setName("tfObat"); // NOI18N
        FormInput1.add(tfObat);
        tfObat.setBounds(130, 170, 270, 23);

        jLabel16.setText("Obat :");
        jLabel16.setName("jLabel16"); // NOI18N
        FormInput1.add(jLabel16);
        jLabel16.setBounds(0, 170, 130, 23);

        jLabel19.setText("Alkes :");
        jLabel19.setName("jLabel19"); // NOI18N
        FormInput1.add(jLabel19);
        jLabel19.setBounds(0, 200, 130, 23);

        tfAlkes.setName("tfAlkes"); // NOI18N
        FormInput1.add(tfAlkes);
        tfAlkes.setBounds(130, 200, 270, 23);

        jLabel48.setText("Prosedur Bedah :");
        jLabel48.setName("jLabel48"); // NOI18N
        FormInput1.add(jLabel48);
        jLabel48.setBounds(400, 50, 130, 23);

        tfBedah.setName("tfBedah"); // NOI18N
        tfBedah.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfBedahKeyPressed(evt);
            }
        });
        FormInput1.add(tfBedah);
        tfBedah.setBounds(530, 50, 270, 23);

        tfKeperawatan.setName("tfKeperawatan"); // NOI18N
        FormInput1.add(tfKeperawatan);
        tfKeperawatan.setBounds(530, 80, 270, 23);

        jLabel23.setText("Keperawatan :");
        jLabel23.setName("jLabel23"); // NOI18N
        FormInput1.add(jLabel23);
        jLabel23.setBounds(400, 80, 130, 23);

        jLabel24.setText("Laboratorium :");
        jLabel24.setName("jLabel24"); // NOI18N
        FormInput1.add(jLabel24);
        jLabel24.setBounds(400, 110, 130, 23);

        tfLaboratorium.setName("tfLaboratorium"); // NOI18N
        FormInput1.add(tfLaboratorium);
        tfLaboratorium.setBounds(530, 110, 270, 23);

        jLabel49.setText("Konsultasi :");
        jLabel49.setName("jLabel49"); // NOI18N
        FormInput1.add(jLabel49);
        jLabel49.setBounds(800, 50, 130, 23);

        tfKonsultasi.setName("tfKonsultasi"); // NOI18N
        tfKonsultasi.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfKonsultasiKeyPressed(evt);
            }
        });
        FormInput1.add(tfKonsultasi);
        tfKonsultasi.setBounds(930, 50, 270, 23);

        tfPenunjang.setName("tfPenunjang"); // NOI18N
        FormInput1.add(tfPenunjang);
        tfPenunjang.setBounds(930, 80, 270, 23);

        jLabel25.setText("Penunjang :");
        jLabel25.setName("jLabel25"); // NOI18N
        FormInput1.add(jLabel25);
        jLabel25.setBounds(800, 80, 130, 23);

        jLabel26.setText(" Pelayanan Darah :");
        jLabel26.setName("jLabel26"); // NOI18N
        FormInput1.add(jLabel26);
        jLabel26.setBounds(800, 110, 130, 23);

        tfDarah.setName("tfDarah"); // NOI18N
        FormInput1.add(tfDarah);
        tfDarah.setBounds(930, 110, 270, 23);

        jLabel50.setText("Kamar / Akomodasi :");
        jLabel50.setName("jLabel50"); // NOI18N
        FormInput1.add(jLabel50);
        jLabel50.setBounds(400, 140, 130, 23);

        jLabel27.setText("Obat Kronis :");
        jLabel27.setName("jLabel27"); // NOI18N
        FormInput1.add(jLabel27);
        jLabel27.setBounds(400, 170, 130, 23);

        jLabel28.setText("BMHP :");
        jLabel28.setName("jLabel28"); // NOI18N
        FormInput1.add(jLabel28);
        jLabel28.setBounds(400, 200, 130, 23);

        tfBMHP.setName("tfBMHP"); // NOI18N
        FormInput1.add(tfBMHP);
        tfBMHP.setBounds(530, 200, 270, 23);

        tfObatKronis.setName("tfObatKronis"); // NOI18N
        FormInput1.add(tfObatKronis);
        tfObatKronis.setBounds(530, 170, 270, 23);

        tfKamar.setName("tfKamar"); // NOI18N
        tfKamar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfKamarKeyPressed(evt);
            }
        });
        FormInput1.add(tfKamar);
        tfKamar.setBounds(530, 140, 270, 23);

        jLabel51.setText("Rawat Intensif :");
        jLabel51.setName("jLabel51"); // NOI18N
        FormInput1.add(jLabel51);
        jLabel51.setBounds(800, 140, 130, 23);

        tfRawatIntensif.setName("tfRawatIntensif"); // NOI18N
        tfRawatIntensif.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                tfRawatIntensifKeyPressed(evt);
            }
        });
        FormInput1.add(tfRawatIntensif);
        tfRawatIntensif.setBounds(930, 140, 270, 23);

        tfObatKemoterapi.setName("tfObatKemoterapi"); // NOI18N
        FormInput1.add(tfObatKemoterapi);
        tfObatKemoterapi.setBounds(930, 170, 270, 23);

        jLabel29.setText("Obat Kemoterapi :");
        jLabel29.setName("jLabel29"); // NOI18N
        FormInput1.add(jLabel29);
        jLabel29.setBounds(800, 170, 130, 23);

        jLabel30.setText("Sewa Alat :");
        jLabel30.setName("jLabel30"); // NOI18N
        FormInput1.add(jLabel30);
        jLabel30.setBounds(800, 200, 130, 23);

        tfSewaAlat.setName("tfSewaAlat"); // NOI18N
        FormInput1.add(tfSewaAlat);
        tfSewaAlat.setBounds(930, 200, 270, 23);

        internalFrameD1ataPasien.add(FormInput1, java.awt.BorderLayout.PAGE_END);

        internalFrameDataPasien.add(internalFrameD1ataPasien, java.awt.BorderLayout.CENTER);

        internalFrame8.setName("internalFrame8"); // NOI18N
        internalFrame8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        setDataKlaim.setBackground(new java.awt.Color(0, 0, 102));
        setDataKlaim.setForeground(new java.awt.Color(255, 255, 255));
        setDataKlaim.setMnemonic('4');
        setDataKlaim.setText("Set Data Klaim");
        setDataKlaim.setToolTipText("Alt+4");
        setDataKlaim.setName("setDataKlaim"); // NOI18N
        setDataKlaim.setOpaque(true);
        setDataKlaim.setPreferredSize(new java.awt.Dimension(150, 23));
        setDataKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                setDataKlaimActionPerformed(evt);
            }
        });
        internalFrame8.add(setDataKlaim);

        btnUpdateDataKlaim.setBackground(new java.awt.Color(204, 0, 255));
        btnUpdateDataKlaim.setForeground(new java.awt.Color(255, 255, 255));
        btnUpdateDataKlaim.setMnemonic('4');
        btnUpdateDataKlaim.setText("Update Data Klaim");
        btnUpdateDataKlaim.setToolTipText("Alt+4");
        btnUpdateDataKlaim.setName("btnUpdateDataKlaim"); // NOI18N
        btnUpdateDataKlaim.setOpaque(true);
        btnUpdateDataKlaim.setPreferredSize(new java.awt.Dimension(180, 23));
        btnUpdateDataKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateDataKlaimActionPerformed(evt);
            }
        });
        internalFrame8.add(btnUpdateDataKlaim);

        btnImportDiagnosaDokter.setBackground(new java.awt.Color(255, 204, 153));
        btnImportDiagnosaDokter.setForeground(new java.awt.Color(0, 0, 0));
        btnImportDiagnosaDokter.setMnemonic('4');
        btnImportDiagnosaDokter.setText("Hasil Koding");
        btnImportDiagnosaDokter.setToolTipText("Alt+4");
        btnImportDiagnosaDokter.setName("btnImportDiagnosaDokter"); // NOI18N
        btnImportDiagnosaDokter.setOpaque(true);
        btnImportDiagnosaDokter.setPreferredSize(new java.awt.Dimension(180, 23));
        btnImportDiagnosaDokter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportDiagnosaDokterActionPerformed(evt);
            }
        });
        internalFrame8.add(btnImportDiagnosaDokter);

        btnDiagnosaLocal.setBackground(new java.awt.Color(255, 204, 153));
        btnDiagnosaLocal.setForeground(new java.awt.Color(0, 0, 0));
        btnDiagnosaLocal.setMnemonic('4');
        btnDiagnosaLocal.setText("Input Diagnosa & Prosedur");
        btnDiagnosaLocal.setToolTipText("Alt+4");
        btnDiagnosaLocal.setName("btnDiagnosaLocal"); // NOI18N
        btnDiagnosaLocal.setOpaque(true);
        btnDiagnosaLocal.setPreferredSize(new java.awt.Dimension(180, 23));
        btnDiagnosaLocal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiagnosaLocalActionPerformed(evt);
            }
        });
        internalFrame8.add(btnDiagnosaLocal);

        internalFrameDataPasien.add(internalFrame8, java.awt.BorderLayout.PAGE_END);

        internalFrame4.add(internalFrameDataPasien);
        internalFrameDataPasien.setBounds(1, 1, 1314, 580);

        FrameKoding.setName("FrameKoding"); // NOI18N
        FrameKoding.setLayout(null);

        FormKodingIDRG.setBorder(null);
        FormKodingIDRG.setName("FormKodingIDRG"); // NOI18N
        FormKodingIDRG.setLayout(null);

        FormInputIDRG.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "iDRG", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        FormInputIDRG.setName("FormInputIDRG"); // NOI18N
        FormInputIDRG.setPreferredSize(new java.awt.Dimension(490, 400));
        FormInputIDRG.setLayout(new java.awt.BorderLayout());

        internalFrame9.setName("internalFrame9"); // NOI18N
        internalFrame9.setLayout(new java.awt.GridLayout(1, 0));

        internalFrame19.setBorder(null);
        internalFrame19.setName("internalFrame19"); // NOI18N
        internalFrame19.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame27.setName("internalFrame27"); // NOI18N
        internalFrame27.setLayout(new java.awt.BorderLayout());

        internalFrame20.setName("internalFrame20"); // NOI18N
        internalFrame20.setLayout(new java.awt.GridLayout(1, 0));

        Scroll16.setBorder(javax.swing.BorderFactory.createTitledBorder("Diagnosa"));
        Scroll16.setName("Scroll16"); // NOI18N
        Scroll16.setOpaque(true);

        tbDiagnosaIDRG.setComponentPopupMenu(ppDiagnosaIDRG);
        tbDiagnosaIDRG.setName("tbDiagnosaIDRG"); // NOI18N
        Scroll16.setViewportView(tbDiagnosaIDRG);

        internalFrame20.add(Scroll16);

        internalFrame27.add(internalFrame20, java.awt.BorderLayout.CENTER);

        internalFrame19.add(internalFrame27, java.awt.BorderLayout.CENTER);

        internalFrame9.add(internalFrame19);

        internalFrame21.setBorder(null);
        internalFrame21.setName("internalFrame21"); // NOI18N
        internalFrame21.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame22.setName("internalFrame22"); // NOI18N
        internalFrame22.setLayout(new java.awt.GridLayout(1, 0));

        Scroll17.setBorder(javax.swing.BorderFactory.createTitledBorder("Prosedur"));
        Scroll17.setName("Scroll17"); // NOI18N
        Scroll17.setOpaque(true);

        tbProsedurIDRG.setComponentPopupMenu(ppProsedurIDRG);
        tbProsedurIDRG.setName("tbProsedurIDRG"); // NOI18N
        tbProsedurIDRG.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tbProsedurIDRGMouseClicked(evt);
            }
        });
        Scroll17.setViewportView(tbProsedurIDRG);

        internalFrame22.add(Scroll17);

        internalFrame21.add(internalFrame22, java.awt.BorderLayout.CENTER);

        internalFrame9.add(internalFrame21);

        FormInputIDRG.add(internalFrame9, java.awt.BorderLayout.CENTER);

        internalFrame5.setName("internalFrame5"); // NOI18N
        internalFrame5.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnGroupingIDRG.setBackground(new java.awt.Color(0, 0, 102));
        btnGroupingIDRG.setForeground(new java.awt.Color(255, 255, 255));
        btnGroupingIDRG.setMnemonic('4');
        btnGroupingIDRG.setText("Grouping iDRG");
        btnGroupingIDRG.setToolTipText("Alt+4");
        btnGroupingIDRG.setName("btnGroupingIDRG"); // NOI18N
        btnGroupingIDRG.setOpaque(true);
        btnGroupingIDRG.setPreferredSize(new java.awt.Dimension(150, 23));
        btnGroupingIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupingIDRGActionPerformed(evt);
            }
        });
        internalFrame5.add(btnGroupingIDRG);

        btnTambahDiagnosaIDRG.setBackground(new java.awt.Color(255, 153, 0));
        btnTambahDiagnosaIDRG.setForeground(new java.awt.Color(0, 0, 0));
        btnTambahDiagnosaIDRG.setMnemonic('4');
        btnTambahDiagnosaIDRG.setText("Tambah Diagnosa");
        btnTambahDiagnosaIDRG.setToolTipText("Alt+4");
        btnTambahDiagnosaIDRG.setName("btnTambahDiagnosaIDRG"); // NOI18N
        btnTambahDiagnosaIDRG.setOpaque(true);
        btnTambahDiagnosaIDRG.setPreferredSize(new java.awt.Dimension(180, 23));
        btnTambahDiagnosaIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahDiagnosaIDRGActionPerformed(evt);
            }
        });
        internalFrame5.add(btnTambahDiagnosaIDRG);

        btnTambahProsedurIDRG.setBackground(new java.awt.Color(51, 51, 255));
        btnTambahProsedurIDRG.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahProsedurIDRG.setMnemonic('4');
        btnTambahProsedurIDRG.setText("Tambah Prosedur");
        btnTambahProsedurIDRG.setToolTipText("Alt+4");
        btnTambahProsedurIDRG.setName("btnTambahProsedurIDRG"); // NOI18N
        btnTambahProsedurIDRG.setOpaque(true);
        btnTambahProsedurIDRG.setPreferredSize(new java.awt.Dimension(180, 23));
        btnTambahProsedurIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahProsedurIDRGActionPerformed(evt);
            }
        });
        internalFrame5.add(btnTambahProsedurIDRG);

        FormInputIDRG.add(internalFrame5, java.awt.BorderLayout.PAGE_END);

        FormKodingIDRG.add(FormInputIDRG);
        FormInputIDRG.setBounds(0, 0, 1310, 350);

        FormInputHasilIDRG.setBackground(new java.awt.Color(0, 255, 255));
        FormInputHasilIDRG.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hasil Grouping iDRG", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        FormInputHasilIDRG.setName("FormInputHasilIDRG"); // NOI18N
        FormInputHasilIDRG.setPreferredSize(new java.awt.Dimension(490, 400));
        FormInputHasilIDRG.setLayout(new java.awt.BorderLayout());

        internalFrame11.setBackground(new java.awt.Color(153, 255, 255));
        internalFrame11.setName("internalFrame11"); // NOI18N
        internalFrame11.setLayout(new java.awt.BorderLayout());

        internalFrame28.setBackground(new java.awt.Color(204, 204, 204));
        internalFrame28.setBorder(null);
        internalFrame28.setName("internalFrame28"); // NOI18N
        internalFrame28.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame29.setBackground(new java.awt.Color(153, 255, 255));
        internalFrame29.setName("internalFrame29"); // NOI18N
        internalFrame29.setLayout(new java.awt.BorderLayout());

        panelHasilIDRG.setBorder(null);
        panelHasilIDRG.setName("panelHasilIDRG"); // NOI18N
        panelHasilIDRG.setWarnaAtas(new java.awt.Color(204, 204, 204));
        panelHasilIDRG.setWarnaBawah(new java.awt.Color(204, 204, 204));
        panelHasilIDRG.setLayout(null);

        jLabel66.setForeground(new java.awt.Color(0, 0, 0));
        jLabel66.setText("Info :");
        jLabel66.setName("jLabel66"); // NOI18N
        panelHasilIDRG.add(jLabel66);
        jLabel66.setBounds(0, 5, 70, 23);

        jLabel67.setForeground(new java.awt.Color(0, 0, 0));
        jLabel67.setText("Jenis Rawat :");
        jLabel67.setName("jLabel67"); // NOI18N
        panelHasilIDRG.add(jLabel67);
        jLabel67.setBounds(0, 30, 70, 23);

        jLabel68.setForeground(new java.awt.Color(0, 0, 0));
        jLabel68.setText("MDC :");
        jLabel68.setName("jLabel68"); // NOI18N
        panelHasilIDRG.add(jLabel68);
        jLabel68.setBounds(0, 55, 70, 23);

        jLabel69.setForeground(new java.awt.Color(0, 0, 0));
        jLabel69.setText("DRG :");
        jLabel69.setName("jLabel69"); // NOI18N
        panelHasilIDRG.add(jLabel69);
        jLabel69.setBounds(0, 80, 70, 23);

        infoHasiliDRG.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        infoHasiliDRG.setForeground(new java.awt.Color(0, 0, 0));
        infoHasiliDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        infoHasiliDRG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        infoHasiliDRG.setName("infoHasiliDRG"); // NOI18N
        panelHasilIDRG.add(infoHasiliDRG);
        infoHasiliDRG.setBounds(80, 5, 590, 23);

        jsnrawatHasiliDRG.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        jsnrawatHasiliDRG.setForeground(new java.awt.Color(0, 0, 0));
        jsnrawatHasiliDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jsnrawatHasiliDRG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jsnrawatHasiliDRG.setName("jsnrawatHasiliDRG"); // NOI18N
        panelHasilIDRG.add(jsnrawatHasiliDRG);
        jsnrawatHasiliDRG.setBounds(80, 30, 590, 23);

        mdcCodeHasiliDRG.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        mdcCodeHasiliDRG.setForeground(new java.awt.Color(0, 0, 0));
        mdcCodeHasiliDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mdcCodeHasiliDRG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        mdcCodeHasiliDRG.setName("mdcCodeHasiliDRG"); // NOI18N
        panelHasilIDRG.add(mdcCodeHasiliDRG);
        mdcCodeHasiliDRG.setBounds(680, 55, 160, 23);

        mdcHasiliDRG.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        mdcHasiliDRG.setForeground(new java.awt.Color(0, 0, 0));
        mdcHasiliDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        mdcHasiliDRG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        mdcHasiliDRG.setName("mdcHasiliDRG"); // NOI18N
        panelHasilIDRG.add(mdcHasiliDRG);
        mdcHasiliDRG.setBounds(80, 55, 590, 23);

        drgHasiliDRG.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        drgHasiliDRG.setForeground(new java.awt.Color(0, 0, 0));
        drgHasiliDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        drgHasiliDRG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        drgHasiliDRG.setName("drgHasiliDRG"); // NOI18N
        panelHasilIDRG.add(drgHasiliDRG);
        drgHasiliDRG.setBounds(80, 80, 590, 23);

        drgCodeHasiliDRG.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        drgCodeHasiliDRG.setForeground(new java.awt.Color(0, 0, 0));
        drgCodeHasiliDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        drgCodeHasiliDRG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        drgCodeHasiliDRG.setName("drgCodeHasiliDRG"); // NOI18N
        panelHasilIDRG.add(drgCodeHasiliDRG);
        drgCodeHasiliDRG.setBounds(680, 80, 160, 23);

        jLabel76.setForeground(new java.awt.Color(0, 0, 0));
        jLabel76.setText("Status :");
        jLabel76.setName("jLabel76"); // NOI18N
        panelHasilIDRG.add(jLabel76);
        jLabel76.setBounds(0, 105, 70, 23);

        statusHasiliDRG.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        statusHasiliDRG.setForeground(new java.awt.Color(0, 0, 0));
        statusHasiliDRG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusHasiliDRG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        statusHasiliDRG.setName("statusHasiliDRG"); // NOI18N
        panelHasilIDRG.add(statusHasiliDRG);
        statusHasiliDRG.setBounds(80, 105, 590, 23);

        btnFinalIDRG.setBackground(new java.awt.Color(0, 102, 102));
        btnFinalIDRG.setForeground(new java.awt.Color(255, 255, 255));
        btnFinalIDRG.setMnemonic('4');
        btnFinalIDRG.setText("Final iDRG");
        btnFinalIDRG.setToolTipText("Alt+4");
        btnFinalIDRG.setName("btnFinalIDRG"); // NOI18N
        btnFinalIDRG.setOpaque(true);
        btnFinalIDRG.setPreferredSize(new java.awt.Dimension(150, 23));
        btnFinalIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalIDRGActionPerformed(evt);
            }
        });
        panelHasilIDRG.add(btnFinalIDRG);
        btnFinalIDRG.setBounds(10, 140, 150, 23);

        btnEditIDRG.setBackground(new java.awt.Color(153, 0, 255));
        btnEditIDRG.setForeground(new java.awt.Color(255, 255, 255));
        btnEditIDRG.setMnemonic('4');
        btnEditIDRG.setText("Edit iDRG");
        btnEditIDRG.setToolTipText("Alt+4");
        btnEditIDRG.setName("btnEditIDRG"); // NOI18N
        btnEditIDRG.setOpaque(true);
        btnEditIDRG.setPreferredSize(new java.awt.Dimension(150, 23));
        btnEditIDRG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditIDRGActionPerformed(evt);
            }
        });
        panelHasilIDRG.add(btnEditIDRG);
        btnEditIDRG.setBounds(10, 140, 150, 23);

        internalFrame29.add(panelHasilIDRG, java.awt.BorderLayout.CENTER);

        internalFrame28.add(internalFrame29, java.awt.BorderLayout.CENTER);

        internalFrame11.add(internalFrame28, java.awt.BorderLayout.CENTER);

        FormInputHasilIDRG.add(internalFrame11, java.awt.BorderLayout.CENTER);

        FormKodingIDRG.add(FormInputHasilIDRG);
        FormInputHasilIDRG.setBounds(0, 350, 1310, 200);

        FrameKoding.add(FormKodingIDRG);
        FormKodingIDRG.setBounds(0, 0, 1310, 550);

        FormKodingINACBG.setBorder(null);
        FormKodingINACBG.setName("FormKodingINACBG"); // NOI18N
        FormKodingINACBG.setLayout(null);

        FormInputINACBG.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "INACBG", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        FormInputINACBG.setName("FormInputINACBG"); // NOI18N
        FormInputINACBG.setPreferredSize(new java.awt.Dimension(490, 400));
        FormInputINACBG.setLayout(new java.awt.BorderLayout());

        internalFrame10.setName("internalFrame10"); // NOI18N
        internalFrame10.setLayout(new java.awt.GridLayout(1, 0));

        internalFrame23.setBorder(null);
        internalFrame23.setName("internalFrame23"); // NOI18N
        internalFrame23.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame24.setName("internalFrame24"); // NOI18N
        internalFrame24.setLayout(new java.awt.BorderLayout());

        Scroll18.setBorder(javax.swing.BorderFactory.createTitledBorder("Diagnosa"));
        Scroll18.setName("Scroll18"); // NOI18N
        Scroll18.setOpaque(true);

        tbDiagnosaIINACBG.setComponentPopupMenu(ppDiagnosaINACBG);
        tbDiagnosaIINACBG.setName("tbDiagnosaIINACBG"); // NOI18N
        Scroll18.setViewportView(tbDiagnosaIINACBG);

        internalFrame24.add(Scroll18, java.awt.BorderLayout.CENTER);

        internalFrame23.add(internalFrame24, java.awt.BorderLayout.CENTER);

        internalFrame10.add(internalFrame23);

        internalFrame25.setBorder(null);
        internalFrame25.setName("internalFrame25"); // NOI18N
        internalFrame25.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame26.setName("internalFrame26"); // NOI18N
        internalFrame26.setLayout(new java.awt.BorderLayout());

        Scroll19.setBorder(javax.swing.BorderFactory.createTitledBorder("Prosedur"));
        Scroll19.setName("Scroll19"); // NOI18N
        Scroll19.setOpaque(true);

        tbProsedureINACBG.setComponentPopupMenu(ppProsedurINACBG);
        tbProsedureINACBG.setName("tbProsedureINACBG"); // NOI18N
        Scroll19.setViewportView(tbProsedureINACBG);

        internalFrame26.add(Scroll19, java.awt.BorderLayout.CENTER);

        internalFrame25.add(internalFrame26, java.awt.BorderLayout.CENTER);

        internalFrame10.add(internalFrame25);

        FormInputINACBG.add(internalFrame10, java.awt.BorderLayout.CENTER);

        internalFrame6.setName("internalFrame6"); // NOI18N
        internalFrame6.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnImportToINACBG.setBackground(new java.awt.Color(51, 255, 51));
        btnImportToINACBG.setForeground(new java.awt.Color(0, 0, 0));
        btnImportToINACBG.setMnemonic('4');
        btnImportToINACBG.setText("Import Coding");
        btnImportToINACBG.setToolTipText("Alt+4");
        btnImportToINACBG.setName("btnImportToINACBG"); // NOI18N
        btnImportToINACBG.setOpaque(true);
        btnImportToINACBG.setPreferredSize(new java.awt.Dimension(150, 23));
        btnImportToINACBG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnImportToINACBGActionPerformed(evt);
            }
        });
        internalFrame6.add(btnImportToINACBG);

        btnGroupingINACBG.setBackground(new java.awt.Color(0, 0, 102));
        btnGroupingINACBG.setForeground(new java.awt.Color(255, 255, 255));
        btnGroupingINACBG.setMnemonic('4');
        btnGroupingINACBG.setText("Grouping INACBG");
        btnGroupingINACBG.setToolTipText("Alt+4");
        btnGroupingINACBG.setName("btnGroupingINACBG"); // NOI18N
        btnGroupingINACBG.setOpaque(true);
        btnGroupingINACBG.setPreferredSize(new java.awt.Dimension(180, 23));
        btnGroupingINACBG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGroupingINACBGActionPerformed(evt);
            }
        });
        internalFrame6.add(btnGroupingINACBG);

        btnTabahDiagnosaINACBG.setBackground(new java.awt.Color(255, 153, 0));
        btnTabahDiagnosaINACBG.setForeground(new java.awt.Color(0, 0, 0));
        btnTabahDiagnosaINACBG.setMnemonic('4');
        btnTabahDiagnosaINACBG.setText("Tambah Diagnosa");
        btnTabahDiagnosaINACBG.setToolTipText("Alt+4");
        btnTabahDiagnosaINACBG.setName("btnTabahDiagnosaINACBG"); // NOI18N
        btnTabahDiagnosaINACBG.setOpaque(true);
        btnTabahDiagnosaINACBG.setPreferredSize(new java.awt.Dimension(180, 23));
        btnTabahDiagnosaINACBG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTabahDiagnosaINACBGActionPerformed(evt);
            }
        });
        internalFrame6.add(btnTabahDiagnosaINACBG);

        btnTambahProsedurINACBG.setBackground(new java.awt.Color(51, 51, 255));
        btnTambahProsedurINACBG.setForeground(new java.awt.Color(255, 255, 255));
        btnTambahProsedurINACBG.setMnemonic('4');
        btnTambahProsedurINACBG.setText("Tambah Prosedur");
        btnTambahProsedurINACBG.setToolTipText("Alt+4");
        btnTambahProsedurINACBG.setName("btnTambahProsedurINACBG"); // NOI18N
        btnTambahProsedurINACBG.setOpaque(true);
        btnTambahProsedurINACBG.setPreferredSize(new java.awt.Dimension(180, 23));
        btnTambahProsedurINACBG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTambahProsedurINACBGActionPerformed(evt);
            }
        });
        internalFrame6.add(btnTambahProsedurINACBG);

        FormInputINACBG.add(internalFrame6, java.awt.BorderLayout.PAGE_END);

        FormKodingINACBG.add(FormInputINACBG);
        FormInputINACBG.setBounds(0, 0, 1310, 350);

        FormInputHasilINACBG.setBackground(new java.awt.Color(0, 255, 255));
        FormInputHasilINACBG.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Hasil Grouping INACBG", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        FormInputHasilINACBG.setName("FormInputHasilINACBG"); // NOI18N
        FormInputHasilINACBG.setPreferredSize(new java.awt.Dimension(490, 400));
        FormInputHasilINACBG.setLayout(new java.awt.BorderLayout());

        internalFrame12.setBackground(new java.awt.Color(153, 255, 255));
        internalFrame12.setName("internalFrame12"); // NOI18N
        internalFrame12.setLayout(new java.awt.BorderLayout());

        internalFrame30.setBackground(new java.awt.Color(204, 204, 204));
        internalFrame30.setBorder(null);
        internalFrame30.setName("internalFrame30"); // NOI18N
        internalFrame30.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame31.setBackground(new java.awt.Color(153, 255, 255));
        internalFrame31.setName("internalFrame31"); // NOI18N
        internalFrame31.setLayout(new java.awt.BorderLayout());

        panelHasilINACBG.setBorder(null);
        panelHasilINACBG.setName("panelHasilINACBG"); // NOI18N
        panelHasilINACBG.setWarnaAtas(new java.awt.Color(204, 204, 204));
        panelHasilINACBG.setWarnaBawah(new java.awt.Color(204, 204, 204));
        panelHasilINACBG.setLayout(null);

        jLabel74.setForeground(new java.awt.Color(0, 0, 0));
        jLabel74.setText("Info :");
        jLabel74.setName("jLabel74"); // NOI18N
        panelHasilINACBG.add(jLabel74);
        jLabel74.setBounds(0, 5, 120, 23);

        jLabel75.setForeground(new java.awt.Color(0, 0, 0));
        jLabel75.setText("Jenis Rawat :");
        jLabel75.setName("jLabel75"); // NOI18N
        panelHasilINACBG.add(jLabel75);
        jLabel75.setBounds(0, 35, 120, 23);

        jLabel78.setForeground(new java.awt.Color(0, 0, 0));
        jLabel78.setText("Rp.");
        jLabel78.setName("jLabel78"); // NOI18N
        panelHasilINACBG.add(jLabel78);
        jLabel78.setBounds(730, 65, 30, 23);

        jLabel79.setForeground(new java.awt.Color(0, 0, 0));
        jLabel79.setText("Sub Acute :");
        jLabel79.setName("jLabel79"); // NOI18N
        panelHasilINACBG.add(jLabel79);
        jLabel79.setBounds(0, 90, 120, 23);

        jLabel80.setForeground(new java.awt.Color(0, 0, 0));
        jLabel80.setText("Chronic :");
        jLabel80.setName("jLabel80"); // NOI18N
        panelHasilINACBG.add(jLabel80);
        jLabel80.setBounds(0, 115, 120, 23);

        jLabel81.setForeground(new java.awt.Color(0, 0, 0));
        jLabel81.setText("Special Procedure :");
        jLabel81.setName("jLabel81"); // NOI18N
        panelHasilINACBG.add(jLabel81);
        jLabel81.setBounds(0, 140, 120, 23);

        jLabel82.setForeground(new java.awt.Color(0, 0, 0));
        jLabel82.setText("Special Prosthesis :");
        jLabel82.setName("jLabel82"); // NOI18N
        panelHasilINACBG.add(jLabel82);
        jLabel82.setBounds(0, 165, 120, 23);

        jLabel83.setForeground(new java.awt.Color(0, 0, 0));
        jLabel83.setText("Special Investigation :");
        jLabel83.setName("jLabel83"); // NOI18N
        panelHasilINACBG.add(jLabel83);
        jLabel83.setBounds(0, 190, 120, 23);

        jLabel84.setForeground(new java.awt.Color(0, 0, 0));
        jLabel84.setText("Special Drugs :");
        jLabel84.setName("jLabel84"); // NOI18N
        panelHasilINACBG.add(jLabel84);
        jLabel84.setBounds(0, 215, 120, 23);

        jLabel85.setForeground(new java.awt.Color(0, 0, 0));
        jLabel85.setText("Total Klaim :");
        jLabel85.setName("jLabel85"); // NOI18N
        panelHasilINACBG.add(jLabel85);
        jLabel85.setBounds(0, 240, 650, 23);

        jLabel86.setForeground(new java.awt.Color(0, 0, 0));
        jLabel86.setText("Status :");
        jLabel86.setName("jLabel86"); // NOI18N
        panelHasilINACBG.add(jLabel86);
        jLabel86.setBounds(0, 260, 120, 23);

        btnFinalINACBG.setBackground(new java.awt.Color(0, 102, 102));
        btnFinalINACBG.setForeground(new java.awt.Color(255, 255, 255));
        btnFinalINACBG.setMnemonic('4');
        btnFinalINACBG.setText("Final INACBG");
        btnFinalINACBG.setToolTipText("Alt+4");
        btnFinalINACBG.setName("btnFinalINACBG"); // NOI18N
        btnFinalINACBG.setOpaque(true);
        btnFinalINACBG.setPreferredSize(new java.awt.Dimension(150, 23));
        btnFinalINACBG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalINACBGActionPerformed(evt);
            }
        });
        panelHasilINACBG.add(btnFinalINACBG);
        btnFinalINACBG.setBounds(10, 330, 150, 23);

        btnEditINACBG.setBackground(new java.awt.Color(153, 0, 255));
        btnEditINACBG.setForeground(new java.awt.Color(255, 255, 255));
        btnEditINACBG.setMnemonic('4');
        btnEditINACBG.setText("Edit INACBG");
        btnEditINACBG.setToolTipText("Alt+4");
        btnEditINACBG.setName("btnEditINACBG"); // NOI18N
        btnEditINACBG.setOpaque(true);
        btnEditINACBG.setPreferredSize(new java.awt.Dimension(150, 23));
        btnEditINACBG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditINACBGActionPerformed(evt);
            }
        });
        panelHasilINACBG.add(btnEditINACBG);
        btnEditINACBG.setBounds(10, 330, 150, 23);

        infoHasilINACBG.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        infoHasilINACBG.setForeground(new java.awt.Color(0, 0, 0));
        infoHasilINACBG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        infoHasilINACBG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        infoHasilINACBG.setName("infoHasilINACBG"); // NOI18N
        panelHasilINACBG.add(infoHasilINACBG);
        infoHasilINACBG.setBounds(120, 5, 810, 23);

        jnsrawatHasilINACBG.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jnsrawatHasilINACBG.setForeground(new java.awt.Color(0, 0, 0));
        jnsrawatHasilINACBG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jnsrawatHasilINACBG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jnsrawatHasilINACBG.setName("jnsrawatHasilINACBG"); // NOI18N
        panelHasilINACBG.add(jnsrawatHasilINACBG);
        jnsrawatHasilINACBG.setBounds(120, 35, 460, 23);

        groupHasilINACBG.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupHasilINACBG.setForeground(new java.awt.Color(0, 0, 0));
        groupHasilINACBG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        groupHasilINACBG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupHasilINACBG.setName("groupHasilINACBG"); // NOI18N
        panelHasilINACBG.add(groupHasilINACBG);
        groupHasilINACBG.setBounds(120, 65, 460, 23);

        groupCodeHasilINACBG.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCodeHasilINACBG.setForeground(new java.awt.Color(0, 0, 0));
        groupCodeHasilINACBG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        groupCodeHasilINACBG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCodeHasilINACBG.setName("groupCodeHasilINACBG"); // NOI18N
        panelHasilINACBG.add(groupCodeHasilINACBG);
        groupCodeHasilINACBG.setBounds(590, 65, 140, 23);

        groupCostHasilINACBG.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCostHasilINACBG.setForeground(new java.awt.Color(0, 0, 0));
        groupCostHasilINACBG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCostHasilINACBG.setName("groupCostHasilINACBG"); // NOI18N
        panelHasilINACBG.add(groupCostHasilINACBG);
        groupCostHasilINACBG.setBounds(760, 65, 170, 23);

        jLabel87.setForeground(new java.awt.Color(0, 0, 0));
        jLabel87.setText("Group :");
        jLabel87.setName("jLabel87"); // NOI18N
        panelHasilINACBG.add(jLabel87);
        jLabel87.setBounds(0, 65, 120, 23);

        jLabel88.setForeground(new java.awt.Color(0, 0, 0));
        jLabel88.setText("Rp.");
        jLabel88.setName("jLabel88"); // NOI18N
        panelHasilINACBG.add(jLabel88);
        jLabel88.setBounds(730, 90, 30, 23);

        groupCostHasilINACBG1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCostHasilINACBG1.setForeground(new java.awt.Color(0, 0, 0));
        groupCostHasilINACBG1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCostHasilINACBG1.setName("groupCostHasilINACBG1"); // NOI18N
        panelHasilINACBG.add(groupCostHasilINACBG1);
        groupCostHasilINACBG1.setBounds(760, 90, 170, 23);

        groupCodeHasilINACBG1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCodeHasilINACBG1.setForeground(new java.awt.Color(0, 0, 0));
        groupCodeHasilINACBG1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        groupCodeHasilINACBG1.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCodeHasilINACBG1.setName("groupCodeHasilINACBG1"); // NOI18N
        panelHasilINACBG.add(groupCodeHasilINACBG1);
        groupCodeHasilINACBG1.setBounds(590, 90, 140, 23);

        subAcuteHasilINACBG.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        subAcuteHasilINACBG.setForeground(new java.awt.Color(0, 0, 0));
        subAcuteHasilINACBG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        subAcuteHasilINACBG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        subAcuteHasilINACBG.setName("subAcuteHasilINACBG"); // NOI18N
        panelHasilINACBG.add(subAcuteHasilINACBG);
        subAcuteHasilINACBG.setBounds(120, 90, 460, 23);

        chronicHasilINACBG.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        chronicHasilINACBG.setForeground(new java.awt.Color(0, 0, 0));
        chronicHasilINACBG.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        chronicHasilINACBG.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        chronicHasilINACBG.setName("chronicHasilINACBG"); // NOI18N
        panelHasilINACBG.add(chronicHasilINACBG);
        chronicHasilINACBG.setBounds(120, 115, 460, 23);

        groupCodeHasilINACBG2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCodeHasilINACBG2.setForeground(new java.awt.Color(0, 0, 0));
        groupCodeHasilINACBG2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        groupCodeHasilINACBG2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCodeHasilINACBG2.setName("groupCodeHasilINACBG2"); // NOI18N
        panelHasilINACBG.add(groupCodeHasilINACBG2);
        groupCodeHasilINACBG2.setBounds(590, 115, 140, 23);

        jLabel89.setForeground(new java.awt.Color(0, 0, 0));
        jLabel89.setText("Rp.");
        jLabel89.setName("jLabel89"); // NOI18N
        panelHasilINACBG.add(jLabel89);
        jLabel89.setBounds(730, 115, 30, 23);

        groupCostHasilINACBG2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCostHasilINACBG2.setForeground(new java.awt.Color(0, 0, 0));
        groupCostHasilINACBG2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCostHasilINACBG2.setName("groupCostHasilINACBG2"); // NOI18N
        panelHasilINACBG.add(groupCostHasilINACBG2);
        groupCostHasilINACBG2.setBounds(760, 115, 170, 23);

        SpecialProcedureHasilCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        SpecialProcedureHasilCode.setForeground(new java.awt.Color(0, 0, 0));
        SpecialProcedureHasilCode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SpecialProcedureHasilCode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        SpecialProcedureHasilCode.setName("SpecialProcedureHasilCode"); // NOI18N
        panelHasilINACBG.add(SpecialProcedureHasilCode);
        SpecialProcedureHasilCode.setBounds(590, 140, 140, 23);

        jLabel90.setForeground(new java.awt.Color(0, 0, 0));
        jLabel90.setText("Rp.");
        jLabel90.setName("jLabel90"); // NOI18N
        panelHasilINACBG.add(jLabel90);
        jLabel90.setBounds(730, 140, 30, 23);

        SpecialProcedureHasilCost.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        SpecialProcedureHasilCost.setForeground(new java.awt.Color(0, 0, 0));
        SpecialProcedureHasilCost.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        SpecialProcedureHasilCost.setName("SpecialProcedureHasilCost"); // NOI18N
        panelHasilINACBG.add(SpecialProcedureHasilCost);
        SpecialProcedureHasilCost.setBounds(760, 140, 170, 23);

        SpecialProsthesisHasilCode.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        SpecialProsthesisHasilCode.setForeground(new java.awt.Color(0, 0, 0));
        SpecialProsthesisHasilCode.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SpecialProsthesisHasilCode.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        SpecialProsthesisHasilCode.setName("SpecialProsthesisHasilCode"); // NOI18N
        panelHasilINACBG.add(SpecialProsthesisHasilCode);
        SpecialProsthesisHasilCode.setBounds(590, 165, 140, 23);

        jLabel91.setForeground(new java.awt.Color(0, 0, 0));
        jLabel91.setText("Rp.");
        jLabel91.setName("jLabel91"); // NOI18N
        panelHasilINACBG.add(jLabel91);
        jLabel91.setBounds(730, 165, 30, 23);

        SpecialProsthesisHasilCost.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        SpecialProsthesisHasilCost.setForeground(new java.awt.Color(0, 0, 0));
        SpecialProsthesisHasilCost.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        SpecialProsthesisHasilCost.setName("SpecialProsthesisHasilCost"); // NOI18N
        panelHasilINACBG.add(SpecialProsthesisHasilCost);
        SpecialProsthesisHasilCost.setBounds(760, 165, 170, 23);

        groupCodeHasilINACBG5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCodeHasilINACBG5.setForeground(new java.awt.Color(0, 0, 0));
        groupCodeHasilINACBG5.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        groupCodeHasilINACBG5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCodeHasilINACBG5.setName("groupCodeHasilINACBG5"); // NOI18N
        panelHasilINACBG.add(groupCodeHasilINACBG5);
        groupCodeHasilINACBG5.setBounds(590, 190, 140, 23);

        jLabel92.setForeground(new java.awt.Color(0, 0, 0));
        jLabel92.setText("Rp.");
        jLabel92.setName("jLabel92"); // NOI18N
        panelHasilINACBG.add(jLabel92);
        jLabel92.setBounds(730, 190, 30, 23);

        groupCostHasilINACBG5.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCostHasilINACBG5.setForeground(new java.awt.Color(0, 0, 0));
        groupCostHasilINACBG5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCostHasilINACBG5.setName("groupCostHasilINACBG5"); // NOI18N
        panelHasilINACBG.add(groupCostHasilINACBG5);
        groupCostHasilINACBG5.setBounds(760, 190, 170, 23);

        statusHasilInacbg.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        statusHasilInacbg.setForeground(new java.awt.Color(0, 0, 0));
        statusHasilInacbg.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusHasilInacbg.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        statusHasilInacbg.setName("statusHasilInacbg"); // NOI18N
        panelHasilINACBG.add(statusHasilInacbg);
        statusHasilInacbg.setBounds(120, 260, 460, 23);

        jLabel93.setForeground(new java.awt.Color(0, 0, 0));
        jLabel93.setText("Rp.");
        jLabel93.setName("jLabel93"); // NOI18N
        panelHasilINACBG.add(jLabel93);
        jLabel93.setBounds(730, 215, 30, 23);

        groupCostHasilINACBG6.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCostHasilINACBG6.setForeground(new java.awt.Color(0, 0, 0));
        groupCostHasilINACBG6.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCostHasilINACBG6.setName("groupCostHasilINACBG6"); // NOI18N
        panelHasilINACBG.add(groupCostHasilINACBG6);
        groupCostHasilINACBG6.setBounds(760, 215, 170, 23);

        CmbSpecialProcedure.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None" }));
        CmbSpecialProcedure.setName("CmbSpecialProcedure"); // NOI18N
        CmbSpecialProcedure.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CmbSpecialProcedureItemStateChanged(evt);
            }
        });
        CmbSpecialProcedure.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbSpecialProcedureKeyPressed(evt);
            }
        });
        panelHasilINACBG.add(CmbSpecialProcedure);
        CmbSpecialProcedure.setBounds(120, 140, 460, 23);

        CmbSpecialProsthesis.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None" }));
        CmbSpecialProsthesis.setName("CmbSpecialProsthesis"); // NOI18N
        CmbSpecialProsthesis.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                CmbSpecialProsthesisItemStateChanged(evt);
            }
        });
        CmbSpecialProsthesis.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbSpecialProsthesisKeyPressed(evt);
            }
        });
        panelHasilINACBG.add(CmbSpecialProsthesis);
        CmbSpecialProsthesis.setBounds(120, 165, 460, 23);

        CmbSpecialInvestigation.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None" }));
        CmbSpecialInvestigation.setName("CmbSpecialInvestigation"); // NOI18N
        CmbSpecialInvestigation.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbSpecialInvestigationKeyPressed(evt);
            }
        });
        panelHasilINACBG.add(CmbSpecialInvestigation);
        CmbSpecialInvestigation.setBounds(120, 190, 460, 23);

        CmbSpeciaDrugs.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "None" }));
        CmbSpeciaDrugs.setName("CmbSpeciaDrugs"); // NOI18N
        CmbSpeciaDrugs.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                CmbSpeciaDrugsKeyPressed(evt);
            }
        });
        panelHasilINACBG.add(CmbSpeciaDrugs);
        CmbSpeciaDrugs.setBounds(120, 215, 460, 23);

        totalCostHasilKlaim.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        totalCostHasilKlaim.setForeground(new java.awt.Color(0, 0, 0));
        totalCostHasilKlaim.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        totalCostHasilKlaim.setName("totalCostHasilKlaim"); // NOI18N
        panelHasilINACBG.add(totalCostHasilKlaim);
        totalCostHasilKlaim.setBounds(760, 240, 170, 23);

        jLabel94.setForeground(new java.awt.Color(0, 0, 0));
        jLabel94.setText("Rp.");
        jLabel94.setName("jLabel94"); // NOI18N
        panelHasilINACBG.add(jLabel94);
        jLabel94.setBounds(730, 240, 30, 23);

        groupCodeHasilINACBG7.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        groupCodeHasilINACBG7.setForeground(new java.awt.Color(0, 0, 0));
        groupCodeHasilINACBG7.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        groupCodeHasilINACBG7.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        groupCodeHasilINACBG7.setName("groupCodeHasilINACBG7"); // NOI18N
        panelHasilINACBG.add(groupCodeHasilINACBG7);
        groupCodeHasilINACBG7.setBounds(590, 215, 140, 23);

        internalFrame31.add(panelHasilINACBG, java.awt.BorderLayout.CENTER);

        internalFrame30.add(internalFrame31, java.awt.BorderLayout.CENTER);

        internalFrame12.add(internalFrame30, java.awt.BorderLayout.CENTER);

        FormInputHasilINACBG.add(internalFrame12, java.awt.BorderLayout.CENTER);

        FormKodingINACBG.add(FormInputHasilINACBG);
        FormInputHasilINACBG.setBounds(0, 350, 1310, 390);

        FormInputStatusKlaim.setBackground(new java.awt.Color(0, 255, 255));
        FormInputStatusKlaim.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status Klaim", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 12))); // NOI18N
        FormInputStatusKlaim.setName("FormInputStatusKlaim"); // NOI18N
        FormInputStatusKlaim.setPreferredSize(new java.awt.Dimension(490, 400));
        FormInputStatusKlaim.setLayout(new java.awt.BorderLayout());

        internalFrame32.setBackground(new java.awt.Color(153, 255, 255));
        internalFrame32.setName("internalFrame32"); // NOI18N
        internalFrame32.setLayout(new java.awt.BorderLayout());

        internalFrame33.setBackground(new java.awt.Color(204, 204, 204));
        internalFrame33.setBorder(null);
        internalFrame33.setName("internalFrame33"); // NOI18N
        internalFrame33.setLayout(new java.awt.BorderLayout(1, 1));

        internalFrame34.setBackground(new java.awt.Color(153, 255, 255));
        internalFrame34.setName("internalFrame34"); // NOI18N
        internalFrame34.setLayout(new java.awt.BorderLayout());

        panelHasilINACBG1.setBorder(null);
        panelHasilINACBG1.setName("panelHasilINACBG1"); // NOI18N
        panelHasilINACBG1.setWarnaAtas(new java.awt.Color(204, 204, 204));
        panelHasilINACBG1.setWarnaBawah(new java.awt.Color(204, 204, 204));
        panelHasilINACBG1.setLayout(null);

        jLabel77.setForeground(new java.awt.Color(0, 0, 0));
        jLabel77.setText("Status Klaim :");
        jLabel77.setName("jLabel77"); // NOI18N
        panelHasilINACBG1.add(jLabel77);
        jLabel77.setBounds(0, 5, 120, 23);

        jLabel95.setForeground(new java.awt.Color(0, 0, 0));
        jLabel95.setText("Status DC Kemkes :");
        jLabel95.setName("jLabel95"); // NOI18N
        panelHasilINACBG1.add(jLabel95);
        jLabel95.setBounds(0, 35, 120, 23);

        statusKlaim.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        statusKlaim.setForeground(new java.awt.Color(0, 0, 0));
        statusKlaim.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusKlaim.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        statusKlaim.setName("statusKlaim"); // NOI18N
        panelHasilINACBG1.add(statusKlaim);
        statusKlaim.setBounds(120, 5, 460, 23);

        statusDC.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        statusDC.setForeground(new java.awt.Color(0, 0, 0));
        statusDC.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusDC.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        statusDC.setName("statusDC"); // NOI18N
        panelHasilINACBG1.add(statusDC);
        statusDC.setBounds(120, 35, 460, 23);

        internalFrame34.add(panelHasilINACBG1, java.awt.BorderLayout.CENTER);

        internalFrame33.add(internalFrame34, java.awt.BorderLayout.CENTER);

        internalFrame32.add(internalFrame33, java.awt.BorderLayout.CENTER);

        FormInputStatusKlaim.add(internalFrame32, java.awt.BorderLayout.CENTER);

        FormKodingINACBG.add(FormInputStatusKlaim);
        FormInputStatusKlaim.setBounds(0, 740, 1310, 100);

        internalFrameEndKlaim.setName("internalFrameEndKlaim"); // NOI18N
        internalFrameEndKlaim.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        btnEditKlaim.setBackground(new java.awt.Color(153, 0, 255));
        btnEditKlaim.setForeground(new java.awt.Color(255, 255, 255));
        btnEditKlaim.setMnemonic('4');
        btnEditKlaim.setText("Edit Klaim");
        btnEditKlaim.setToolTipText("Alt+4");
        btnEditKlaim.setName("btnEditKlaim"); // NOI18N
        btnEditKlaim.setOpaque(true);
        btnEditKlaim.setPreferredSize(new java.awt.Dimension(150, 23));
        btnEditKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditKlaimActionPerformed(evt);
            }
        });
        internalFrameEndKlaim.add(btnEditKlaim);

        btnFinalKlaim.setBackground(new java.awt.Color(255, 0, 0));
        btnFinalKlaim.setForeground(new java.awt.Color(255, 255, 255));
        btnFinalKlaim.setMnemonic('4');
        btnFinalKlaim.setText("Final Klaim");
        btnFinalKlaim.setToolTipText("Alt+4");
        btnFinalKlaim.setName("btnFinalKlaim"); // NOI18N
        btnFinalKlaim.setOpaque(true);
        btnFinalKlaim.setPreferredSize(new java.awt.Dimension(150, 23));
        btnFinalKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFinalKlaimActionPerformed(evt);
            }
        });
        internalFrameEndKlaim.add(btnFinalKlaim);

        btnKirimOnline.setBackground(new java.awt.Color(51, 255, 51));
        btnKirimOnline.setForeground(new java.awt.Color(0, 0, 0));
        btnKirimOnline.setMnemonic('4');
        btnKirimOnline.setText("Kirim Online");
        btnKirimOnline.setToolTipText("Alt+4");
        btnKirimOnline.setName("btnKirimOnline"); // NOI18N
        btnKirimOnline.setOpaque(true);
        btnKirimOnline.setPreferredSize(new java.awt.Dimension(150, 23));
        btnKirimOnline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKirimOnlineActionPerformed(evt);
            }
        });
        internalFrameEndKlaim.add(btnKirimOnline);

        btnCetakKlaim.setBackground(new java.awt.Color(0, 153, 153));
        btnCetakKlaim.setForeground(new java.awt.Color(255, 255, 255));
        btnCetakKlaim.setMnemonic('S');
        btnCetakKlaim.setText("Cetak Klaim ");
        btnCetakKlaim.setToolTipText("Alt+S");
        btnCetakKlaim.setName("btnCetakKlaim"); // NOI18N
        btnCetakKlaim.setOpaque(true);
        btnCetakKlaim.setPreferredSize(new java.awt.Dimension(150, 23));
        btnCetakKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCetakKlaimActionPerformed(evt);
            }
        });
        btnCetakKlaim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnCetakKlaimKeyPressed(evt);
            }
        });
        internalFrameEndKlaim.add(btnCetakKlaim);

        btnHasilFinalKlaim.setBackground(new java.awt.Color(255, 153, 0));
        btnHasilFinalKlaim.setForeground(new java.awt.Color(0, 0, 0));
        btnHasilFinalKlaim.setMnemonic('S');
        btnHasilFinalKlaim.setText("Hasil Klaim Final");
        btnHasilFinalKlaim.setToolTipText("Alt+S");
        btnHasilFinalKlaim.setName("btnHasilFinalKlaim"); // NOI18N
        btnHasilFinalKlaim.setOpaque(true);
        btnHasilFinalKlaim.setPreferredSize(new java.awt.Dimension(200, 23));
        btnHasilFinalKlaim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHasilFinalKlaimActionPerformed(evt);
            }
        });
        btnHasilFinalKlaim.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnHasilFinalKlaimKeyPressed(evt);
            }
        });
        internalFrameEndKlaim.add(btnHasilFinalKlaim);

        FormKodingINACBG.add(internalFrameEndKlaim);
        internalFrameEndKlaim.setBounds(0, 840, 1310, 35);

        FrameKoding.add(FormKodingINACBG);
        FormKodingINACBG.setBounds(0, 550, 1310, 950);

        internalFrame4.add(FrameKoding);
        FrameKoding.setBounds(0, 580, 1314, 2500);

        Briwayatperawatan.setText("Tampil Riwayat Perawatan");
        Briwayatperawatan.setName("Briwayatperawatan"); // NOI18N
        Briwayatperawatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BriwayatperawatanActionPerformed(evt);
            }
        });
        internalFrame4.add(Briwayatperawatan);
        Briwayatperawatan.setBounds(1330, 10, 220, 23);

        scrollPane1.setViewportView(internalFrame4);

        internalFrame1.add(scrollPane1, java.awt.BorderLayout.CENTER);

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

        internalFrame1.add(panelisi5, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private String getJsonKehamilanKelahiran(String no_sep) {
        StringBuilder sb = new StringBuilder();
        try {
            boolean hasKehamilan = false;
            StringBuilder sbKhm = new StringBuilder();
            java.sql.PreparedStatement psKhm = koneksi.prepareStatement("SELECT * FROM pku_eklaim_kehamilan WHERE no_sep=?");
            try {
                psKhm.setString(1, no_sep);
                java.sql.ResultSet rsKhm = psKhm.executeQuery();
                if (rsKhm.next()) {
                    hasKehamilan = true;
                    sbKhm.append("\"usia_kehamilan\": \"").append(rsKhm.getInt("usia_kehamilan")).append("\",");
                    sbKhm.append("\"gravida\": \"").append(rsKhm.getInt("gravida")).append("\",");
                    sbKhm.append("\"partus\": \"").append(rsKhm.getInt("partus")).append("\",");
                    sbKhm.append("\"abortus\": \"").append(rsKhm.getInt("abortus")).append("\",");
                    String onsetStr = "spontan";
                    int onset = rsKhm.getInt("onset_kontraksi");
                    if (onset == 2) onsetStr = "induksi";
                    else if (onset == 3) onsetStr = "non_spontan_non_induksi";
                    sbKhm.append("\"onset_kontraksi\": \"").append(onsetStr).append("\"");
                }
            } finally { psKhm.close(); }
            
            boolean hasKelahiran = false;
            StringBuilder sbKlh = new StringBuilder();
            java.sql.PreparedStatement psKlh = koneksi.prepareStatement("SELECT * FROM pku_eklaim_kelahiran WHERE no_sep=? ORDER BY id ASC");
            try {
                psKlh.setString(1, no_sep);
                java.sql.ResultSet rsKlh = psKlh.executeQuery();
                boolean first = true;
                sbKlh.append("\"delivery\": [");
                int seq = 1;
                while (rsKlh.next()) {
                    hasKelahiran = true;
                    if (!first) sbKlh.append(",");
                    sbKlh.append("{");
                    
                    sbKlh.append("\"delivery_sequence\": \"").append(seq).append("\",");
                    
                    String method = "vaginal";
                    if (rsKlh.getInt("cara_kelahiran") == 2) method = "sc";
                    sbKlh.append("\"delivery_method\": \"").append(method).append("\",");
                    
                    sbKlh.append("\"use_manual\": \"").append(rsKlh.getInt("manual_aid")).append("\",");
                    sbKlh.append("\"use_forcep\": \"").append(rsKlh.getInt("forcep")).append("\",");
                    sbKlh.append("\"use_vacuum\": \"").append(rsKlh.getInt("vacuum")).append("\",");
                    
                    String kondisi = "livebirth";
                    if (rsKlh.getInt("kondisi") == 2) kondisi = "stillbirth";
                    sbKlh.append("\"kondisi\": \"").append(kondisi).append("\",");
                    
                    String letak = "kepala";
                    if (rsKlh.getInt("letak_presentasi") == 2) letak = "sungsang";
                    else if (rsKlh.getInt("letak_presentasi") == 3) letak = "lintang";
                    sbKlh.append("\"letak_janin\": \"").append(letak).append("\",");
                    
                    String shkAmbil = "ya";
                    if (rsKlh.getInt("shk_pengambilan") == 2) shkAmbil = "tidak";
                    sbKlh.append("\"shk_spesimen_ambil\": \"").append(shkAmbil).append("\",");
                    
                    String shkLokasi = "tumit";
                    if (rsKlh.getInt("shk_lokasi") == 2) shkLokasi = "vena";
                    sbKlh.append("\"shk_lokasi\": \"").append(shkLokasi).append("\",");
                    
                    if (rsKlh.getInt("shk_pengambilan") == 2 && rsKlh.getInt("alasan_tidak_shk") != 0) {
                        String shkAlasan = "tidak-dapat";
                        if (rsKlh.getInt("alasan_tidak_shk") == 2) shkAlasan = "akses-sulit";
                        sbKlh.append("\"shk_alasan\": \"").append(shkAlasan).append("\",");
                    }
                    
                    sbKlh.append("\"shk_spesimen_dttm\": \"").append(rsKlh.getString("shk_waktu")).append("\",");
                    
                    sbKlh.append("\"delivery_dttm\": \"").append(rsKlh.getString("waktu_kelahiran")).append("\"");
                    sbKlh.append("}");
                    first = false;
                    seq++;
                }
                sbKlh.append("]");
            } finally { psKlh.close(); }
            
            if (hasKehamilan || hasKelahiran) {
                sb.append(",\"persalinan\": {");
                if (hasKehamilan) {
                    sb.append(sbKhm.toString());
                }
                if (hasKehamilan && hasKelahiran) {
                    sb.append(",");
                }
                if (hasKelahiran) {
                    sb.append(sbKlh.toString());
                }
                sb.append("}");
            }
        } catch(Exception e) {
            System.out.println("Error getJsonKehamilanKelahiran: " + e);
        }
        return sb.toString();
    }

    private boolean isKehamilanButtonAdded = false;
    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        if(!isKehamilanButtonAdded){
            widget.Button btnKehamilan = new widget.Button();
            btnKehamilan.setText("Kehamilan / Bayi");
            btnKehamilan.setPreferredSize(new java.awt.Dimension(140, 23));
            btnKehamilan.setBackground(new java.awt.Color(255, 102, 204));
            btnKehamilan.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    if (noSep == null || noSep.trim().equals("")) {
                        javax.swing.JOptionPane.showMessageDialog(null, "Pilih data klaim pasien terlebih dahulu!");
                        return;
                    }
                    PKUDlgKehamilanKelahiran dlg = new PKUDlgKehamilanKelahiran(null, true);
                    dlg.setNoSep(noSep);
                    dlg.setVisible(true);
                }
            });
            internalFrame8.add(btnKehamilan);
            internalFrame8.revalidate();
            internalFrame8.repaint();
            isKehamilanButtonAdded = true;
        }
    }//GEN-LAST:event_formWindowOpened

    private void txtNoRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoRawatKeyPressed

    }//GEN-LAST:event_txtNoRawatKeyPressed

    private void txtNoRmKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoRmKeyPressed

    }//GEN-LAST:event_txtNoRmKeyPressed

    private void noKartuJknKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noKartuJknKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_noKartuJknKeyPressed

    private void tfTotalKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfTotalKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfTotalKeyPressed

    private void tfNonBedahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfNonBedahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfNonBedahKeyPressed

    private void jnsLayananKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jnsLayananKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jnsLayananKeyPressed

    private void kdPoliKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdPoliKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kdPoliKeyPressed

    private void jamMasukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jamMasukKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jamMasukKeyPressed

    private void tglMasukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tglMasukKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tglMasukKeyPressed

    private void tglPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tglPulangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tglPulangKeyPressed

    private void jamPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jamPulangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_jamPulangKeyPressed

    private void kdDokterKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_kdDokterKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_kdDokterKeyPressed

    private void tfRehabilitasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfRehabilitasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfRehabilitasiKeyPressed

    private void tfBedahKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfBedahKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfBedahKeyPressed

    private void tfKonsultasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfKonsultasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfKonsultasiKeyPressed

    private void tfKamarKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfKamarKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfKamarKeyPressed

    private void tfRawatIntensifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfRawatIntensifKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tfRawatIntensifKeyPressed

    private void sistolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sistolKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_sistolKeyPressed

    private void setDataKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_setDataKlaimActionPerformed
        kirimKlaim();
    }//GEN-LAST:event_setDataKlaimActionPerformed

    private void BtnKeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar1ActionPerformed
        dispose();
    }//GEN-LAST:event_BtnKeluar1ActionPerformed

    private void btnGroupingIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupingIDRGActionPerformed
        setDRG();
    }//GEN-LAST:event_btnGroupingIDRGActionPerformed

    private void btnFinalIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalIDRGActionPerformed
        finalDRG();
    }//GEN-LAST:event_btnFinalIDRGActionPerformed

    private void btnGroupingINACBGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGroupingINACBGActionPerformed
        setINACBG();
    }//GEN-LAST:event_btnGroupingINACBGActionPerformed

    private void btnFinalINACBGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalINACBGActionPerformed
        finalINACBG();
    }//GEN-LAST:event_btnFinalINACBGActionPerformed

    private void tbDiagnosaPasienMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDiagnosaPasienMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_tbDiagnosaPasienMouseClicked

    private void btnImportToIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportToIDRGActionPerformed
        for (i = 0; i < tbDiagnosaPasien.getRowCount(); i++) {
            if (Sequel.cariInteger("Select count(code) from tb_idrg_diagnose where no_rawat='" + norawat + "' and status='Ralan' and  code='" + tbDiagnosaPasien.getValueAt(i, 0).toString() + "' ") > 0) {
//                     Sequel.mengedit("tb_idrg_diagnose", "code=?", "statusdata=?", 2, new String[]{
//                        "1", tbDiagnosaPasien.getValueAt(i, 0).toString()
//                    });
            } else {
                if (Sequel.cariInteger(
                        "select count(diagnosa_pasien.kd_penyakit) from diagnosa_pasien "
                        + "inner join reg_periksa inner join pasien on "
                        + "diagnosa_pasien.no_rawat=reg_periksa.no_rawat and "
                        + "reg_periksa.no_rkm_medis=pasien.no_rkm_medis where "
                        + "diagnosa_pasien.no_rawat='" + norawat + "' and diagnosa_pasien.kd_penyakit='" + tbDiagnosaPasien.getValueAt(i, 0).toString() + "'") > 0) {
                    Sequel.menyimpan("tb_idrg_diagnose", "?,?,?,?,?", "Penyakit", 5, new String[]{
                        norawat, tbDiagnosaPasien.getValueAt(i, 0).toString(), status,
                        Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_idrg_diagnose where no_rawat=? and status='" + status + "'", norawat), "Lama"
                    });
                } else {
                    Sequel.menyimpan("tb_idrg_diagnose", "?,?,?,?,?", "Penyakit", 5, new String[]{
                        norawat, tbDiagnosaPasien.getValueAt(i, 0).toString(), status,
                        Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_idrg_diagnose where no_rawat=? and status='" + status + "'", norawat), "Baru"
                    });
                }
            }
        }
        tampilDiagnosaIDRG();
        for (i = 0; i < tbProsedurePasien.getRowCount(); i++) {
            if (Sequel.cariInteger("Select count(code) from tb_idrg_diagnose where no_rawat='" + norawat + "' and status='Ralan' and  code='" + tbProsedurePasien.getValueAt(i, 0).toString() + "' ") > 0) {
//                     Sequel.mengedit("tb_idrg_diagnose", "code=?", "statusdata=?", 2, new String[]{
//                        "1", tbDiagnosaPasien.getValueAt(i, 0).toString()
//                    });
            } else {
                String jumlah = (tbProsedurePasien.getValueAt(i, 3) != null)
                ? tbProsedurePasien.getValueAt(i, 3).toString()
                : "0";
                Sequel.menyimpan("tb_idrg_prosedure", "?,?,?,?,?", "ICD 9", 5, new String[]{
                    norawat, tbProsedurePasien.getValueAt(i, 0).toString(), status, Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_idrg_prosedure where no_rawat=? and status='" + status + "'", norawat), jumlah
                });
            }
        }
        tampilProsedureIDRG();

    }//GEN-LAST:event_btnImportToIDRGActionPerformed

    private void btnEditIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditIDRGActionPerformed
        editDRG();
    }//GEN-LAST:event_btnEditIDRGActionPerformed

    private void btnEditINACBGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditINACBGActionPerformed
        editINACBG();
    }//GEN-LAST:event_btnEditINACBGActionPerformed

    private void btnMultiplicityActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMultiplicityActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        txtProcedureIDRGCode.setText(tbProsedurIDRG.getValueAt(tbProsedurIDRG.getSelectedRow(), 0).toString());
        txtProcedureIDRGName.setText(tbProsedurIDRG.getValueAt(tbProsedurIDRG.getSelectedRow(), 1).toString());
        txtPriority.setText(tbProsedurIDRG.getValueAt(tbProsedurIDRG.getSelectedRow(), 4).toString());
        txtPriority.setVisible(false);
        WindowMultiplicityProcedure.setSize(800, 150);
        WindowMultiplicityProcedure.setLocationRelativeTo(internalFrame1);
        WindowMultiplicityProcedure.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnMultiplicityActionPerformed

    private void BtnCloseInpindah2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah2ActionPerformed
        Sequel.mengedit("tb_idrg_prosedure", "no_rawat=? and kode=? and prioritas=?", "vol=?", 4, new String[]{
            CmbVol.getSelectedItem().toString(), norawat, txtProcedureIDRGCode.getText(),txtPriority.getText()
        });
        tampilProsedureIDRG();
        WindowMultiplicityProcedure.dispose();

    }//GEN-LAST:event_BtnCloseInpindah2ActionPerformed

    private void BtnCloseInpindah2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah2KeyPressed

    private void WindowMultiplicityProcedureWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowMultiplicityProcedureWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_WindowMultiplicityProcedureWindowActivated

    private void txtProcedureIDRGNameKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProcedureIDRGNameKeyPressed

    }//GEN-LAST:event_txtProcedureIDRGNameKeyPressed

    private void txtProcedureIDRGCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtProcedureIDRGCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProcedureIDRGCodeKeyPressed

    private void CmbVolKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbVolKeyPressed

    }//GEN-LAST:event_CmbVolKeyPressed

    private void BtnCloseInpindah3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah3ActionPerformed
        WindowMultiplicityProcedure.dispose();
    }//GEN-LAST:event_BtnCloseInpindah3ActionPerformed

    private void BtnCloseInpindah3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah3KeyPressed

    private void btnImportToINACBGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportToINACBGActionPerformed
        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin import diagnosa iDRG ke INACBG", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            importCoding();
        }
    }//GEN-LAST:event_btnImportToINACBGActionPerformed

    private void BtnCloseInpindah5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah5ActionPerformed
        WindowHasilIDRG.dispose();
    }//GEN-LAST:event_BtnCloseInpindah5ActionPerformed

    private void BtnCloseInpindah5KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah5KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah5KeyPressed

    private void txtInfoHasilIDRGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInfoHasilIDRGKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInfoHasilIDRGKeyPressed

    private void WindowHasilIDRGWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowHasilIDRGWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_WindowHasilIDRGWindowActivated

    private void txtStatusKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStatusKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStatusKeyPressed

    private void txtMdcDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMdcDescriptionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMdcDescriptionKeyPressed

    private void txtDrgCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDrgCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDrgCodeKeyPressed

    private void txtMdcCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtMdcCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMdcCodeKeyPressed

    private void txtDrgDescriptionKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtDrgDescriptionKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtDrgDescriptionKeyPressed

    private void btnUpdateDataKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateDataKlaimActionPerformed
        updateDataClaim();
    }//GEN-LAST:event_btnUpdateDataKlaimActionPerformed

    private void txtIDSITBKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtIDSITBKeyPressed

    }//GEN-LAST:event_txtIDSITBKeyPressed

    private void btnValidasiSITBActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidasiSITBActionPerformed
        validasiPasienTB(txtIDSITB.getText());
    }//GEN-LAST:event_btnValidasiSITBActionPerformed

    private void ChkPasienTBItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_ChkPasienTBItemStateChanged
        if (ChkPasienTB.isSelected() == true) {
            txtIDSITB.setVisible(true);
            btnValidasiSITB.setVisible(true);
        } else {
            txtIDSITB.setVisible(false);
            btnValidasiSITB.setVisible(false);
        }
    }//GEN-LAST:event_ChkPasienTBItemStateChanged

    private void beratLahirKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_beratLahirKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_beratLahirKeyPressed

    private void btnDeleteDiagnosaIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDiagnosaIDRGActionPerformed
        Sequel.queryu("delete from tb_idrg_diagnose where no_rawat='" + norawat + "' and code='" + tbDiagnosaIDRG.getValueAt(tbDiagnosaIDRG.getSelectedRow(), 0).toString() + "'  ");
        Sequel.queryu("delete from diagnosa_pasien where no_rawat='" + norawat + "' and kd_penyakit='" + tbDiagnosaIDRG.getValueAt(tbDiagnosaIDRG.getSelectedRow(), 0).toString() + "'  ");
        tampilDiagnosaIDRG();
    }//GEN-LAST:event_btnDeleteDiagnosaIDRGActionPerformed

    private void btnDeleteDiagnosaINACBGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDiagnosaINACBGActionPerformed
 Sequel.queryu("delete from tb_inacbg_diagnose where no_rawat='" + norawat + "' and code='" + tbDiagnosaIINACBG.getValueAt(tbDiagnosaIINACBG.getSelectedRow(), 0).toString() + "'  ");
        tampilDiagnosaINACBG();
    }//GEN-LAST:event_btnDeleteDiagnosaINACBGActionPerformed

    private void btnKirimOnlineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKirimOnlineActionPerformed
        KirimOnlineKlaim();
    }//GEN-LAST:event_btnKirimOnlineActionPerformed

    private void BtnCloseInpindah6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah6ActionPerformed
        WindowSearchDiagnosa.dispose();
    }//GEN-LAST:event_BtnCloseInpindah6ActionPerformed

    private void BtnCloseInpindah6KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah6KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah6KeyPressed

    private void WindowSearchDiagnosaWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowSearchDiagnosaWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_WindowSearchDiagnosaWindowActivated

    private void btnTabahDiagnosaINACBGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTabahDiagnosaINACBGActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        sourceDiagnosa = "Diagnosa ICDX";
        labelSumber.setText("Sumber :" + sourceDiagnosa);
        FormCariDiagnosa.setFocusable(true);
        tampilDiagnosa();
        WindowSearchDiagnosa.setSize(800, 400);
        WindowSearchDiagnosa.setLocationRelativeTo(internalFrame1);
        WindowSearchDiagnosa.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnTabahDiagnosaINACBGActionPerformed

    private void tbDiagnosaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbDiagnosaMouseClicked
        if (sourceDiagnosa.equals("Diagnosa ICDX")) {
            if (tabModeDiagnosa.getRowCount() != 0) {
                if (Sequel.cariInteger(
                        "select count(tb_inacbg_diagnose.code) from tb_inacbg_diagnose "
                        + "inner join reg_periksa inner join pasien on "
                        + "tb_inacbg_diagnose.no_rawat=reg_periksa.no_rawat and "
                        + "reg_periksa.no_rkm_medis=pasien.no_rkm_medis where "
                        + "tb_inacbg_diagnose.no_rawat='" + norawat + "' and tb_inacbg_diagnose.code='" + tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 1).toString() + "'") > 0) {
                    Sequel.menyimpan("tb_inacbg_diagnose", "?,?,?,?,?,?,?", "Penyakit", 7, new String[]{
                        norawat, tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 1).toString(), status,
                        Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_inacbg_diagnose where no_rawat=? and status='" + status + "'", norawat), "Lama", "1", "Ok"
                    });
                    JOptionPane.showMessageDialog(null, "Diagnosa " + tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 1).toString() + " berhasil ditambahkan");
                    tampilDiagnosaINACBG();
                } else {
                    Sequel.menyimpan("tb_inacbg_diagnose", "?,?,?,?,?,?,?", "Penyakit", 7, new String[]{
                        norawat, tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 1).toString(), status,
                        Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_inacbg_diagnose where no_rawat=? and status='" + status + "'", norawat), "Baru", "1", "Ok"
                    });
                    JOptionPane.showMessageDialog(null, "Diagnosa " + tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 1).toString() + " berhasil ditambahkan");
                    tampilDiagnosaINACBG();
                }
            }
        } else if (sourceDiagnosa.equals("Diagnoa Ina Grouper")) {
            if (tabModeDiagnosa.getRowCount() != 0) {
                if (tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 3).toString().equals("0")) {
                    if (tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 4).toString().equals("N")) {
                        JOptionPane.showMessageDialog(null, "This code " + tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 5).toString() + " cannot be primary diagnosis");
                    } else if (tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 4).toString().equals("Y")) {
                        JOptionPane.showMessageDialog(null, "This diagnosis code " + tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 5).toString() + " is not valid for grouping");
                    }

                } else if (tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 3).toString().equals("1")) {
                    if (tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 4).toString().equals("N")) {
                        JOptionPane.showMessageDialog(null, "This code " + tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 5).toString() + " cannot be primary diagnosis");
                    } else if (tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 4).toString().equals("Y")) {

                        if (Sequel.cariInteger(
                                "select count(tb_idrg_diagnose.code) from tb_idrg_diagnose "
                                + "inner join reg_periksa inner join pasien on "
                                + "tb_idrg_diagnose.no_rawat=reg_periksa.no_rawat and "
                                + "reg_periksa.no_rkm_medis=pasien.no_rkm_medis where "
                                + "tb_idrg_diagnose.no_rawat='" + norawat + "' and tb_idrg_diagnose.code='" + tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 1).toString() + "'") > 0) {
                            Sequel.menyimpan("tb_idrg_diagnose", "?,?,?,?,?", "Penyakit", 5, new String[]{
                                norawat, tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 1).toString(), status,
                                Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_idrg_diagnose where no_rawat=? and status='" + status + "'", norawat), "Lama"
                            });
                            JOptionPane.showMessageDialog(null, "Diagnosa " + tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 5).toString() + " berhasil ditambahkan");
                            tampilDiagnosaIDRG();
                        } else {
                            Sequel.menyimpan("tb_idrg_diagnose", "?,?,?,?,?", "Penyakit", 5, new String[]{
                                norawat, tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 1).toString(), status,
                                Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_idrg_diagnose where no_rawat=? and status='" + status + "'", norawat), "Baru"
                            });
                            JOptionPane.showMessageDialog(null, "Diagnosa " + tbDiagnosa.getValueAt(tbDiagnosa.getSelectedRow(), 5).toString() + " berhasil ditambahkan");
                            tampilDiagnosaIDRG();
                        }
                    }
                }
            }
        }
    }//GEN-LAST:event_tbDiagnosaMouseClicked

    private void FormCariDiagnosaKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FormCariDiagnosaKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            if (sourceDiagnosa.equals("Diagnosa ICDX")) {
                tampilDiagnosa();
            } else if (sourceDiagnosa.equals("Diagnoa Ina Grouper")) {
                tampilDiagnosaInaGrouper();
            }
        }
    }//GEN-LAST:event_FormCariDiagnosaKeyPressed

    private void BtnCari3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari3ActionPerformed
        if (sourceDiagnosa.equals("Diagnosa ICDX")) {
            tampilDiagnosa();
        } else if (sourceDiagnosa.equals("Diagnoa Ina Grouper")) {
            tampilDiagnosaInaGrouper();
        }
    }//GEN-LAST:event_BtnCari3ActionPerformed

    private void BtnCari3KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari3KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCari3KeyPressed

    private void btnFinalKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFinalKlaimActionPerformed
        finalKlaim();
    }//GEN-LAST:event_btnFinalKlaimActionPerformed

    private void btnEditKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditKlaimActionPerformed
        editKlaim();
    }//GEN-LAST:event_btnEditKlaimActionPerformed

    private void btnCetakKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCetakKlaimActionPerformed
       cetakKlaim();
    }//GEN-LAST:event_btnCetakKlaimActionPerformed

    private void btnCetakKlaimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnCetakKlaimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnCetakKlaimKeyPressed

    private void btnHasilFinalKlaimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHasilFinalKlaimActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        getHasilKlaim();
        WindowHasilKlaim.setSize(750, 140);
        WindowHasilKlaim.setLocationRelativeTo(internalFrame1);
        WindowHasilKlaim.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnHasilFinalKlaimActionPerformed

    private void btnHasilFinalKlaimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnHasilFinalKlaimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnHasilFinalKlaimKeyPressed

    private void BtnCloseInpindah7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah7ActionPerformed
        WindowHasilINACBG.dispose();
    }//GEN-LAST:event_BtnCloseInpindah7ActionPerformed

    private void BtnCloseInpindah7KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah7KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah7KeyPressed

    private void txtInfoHasilINACBGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtInfoHasilINACBGKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtInfoHasilINACBGKeyPressed

    private void txtjnsRawatKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtjnsRawatKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtjnsRawatKeyPressed

    private void txtGroupCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGroupCostKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupCostKeyPressed

    private void WindowHasilINACBGWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowHasilINACBGWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_WindowHasilINACBGWindowActivated

    private void txtGroupKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGroupKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupKeyPressed

    private void txtGroupCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtGroupCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGroupCodeKeyPressed

    private void txtSubAcuteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubAcuteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubAcuteKeyPressed

    private void txtSubAcuteCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubAcuteCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubAcuteCodeKeyPressed

    private void txtSubAcuteCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSubAcuteCostKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSubAcuteCostKeyPressed

    private void txtCronicCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCronicCostKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCronicCostKeyPressed

    private void txtCronicKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCronicKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCronicKeyPressed

    private void txtCronicCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCronicCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtCronicCodeKeyPressed

    private void txtSpecProcedureKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecProcedureKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecProcedureKeyPressed

    private void txtSpecProcedureCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecProcedureCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecProcedureCodeKeyPressed

    private void txtSpecProcedureCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecProcedureCostKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecProcedureCostKeyPressed

    private void txtSpecProthesisCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecProthesisCostKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecProthesisCostKeyPressed

    private void txtSpecProthesisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecProthesisKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecProthesisKeyPressed

    private void txtSpecProthesisCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecProthesisCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecProthesisCodeKeyPressed

    private void txtSpecInvestigationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecInvestigationKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecInvestigationKeyPressed

    private void txtSpecInvestigationCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecInvestigationCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecInvestigationCodeKeyPressed

    private void txtSpecInvestigationCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecInvestigationCostKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecInvestigationCostKeyPressed

    private void txtSpecDrugCostKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecDrugCostKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecDrugCostKeyPressed

    private void txtSpecDrugKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecDrugKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecDrugKeyPressed

    private void txtSpecDrugCodeKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtSpecDrugCodeKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtSpecDrugCodeKeyPressed

    private void txtTotalKlaimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtTotalKlaimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtTotalKlaimKeyPressed

    private void txtStatusINACBGKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStatusINACBGKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStatusINACBGKeyPressed

    private void BtnCloseInpindah8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah8ActionPerformed
        WindowHasilKlaim.dispose();
    }//GEN-LAST:event_BtnCloseInpindah8ActionPerformed

    private void BtnCloseInpindah8KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah8KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah8KeyPressed

    private void txtStatusKlaimKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStatusKlaimKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStatusKlaimKeyPressed

    private void txtStatus1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStatus1KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtStatus1KeyPressed

    private void WindowHasilKlaimWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowHasilKlaimWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_WindowHasilKlaimWindowActivated

    private void BtnCari2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCari2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCari2KeyPressed

    private void BtnCari2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCari2ActionPerformed
        if (sourceProsedur.equals("Diagnosa ICD IX")) {
            tampilProsedur();
        } else if (sourceProsedur.equals("Diagnoa Ina Grouper")) {
            tampilProsedurInaGrouper();
        }
    }//GEN-LAST:event_BtnCari2ActionPerformed

    private void TCariProsedurKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_TCariProsedurKeyPressed
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            BtnCari2ActionPerformed(null);
        }
    }//GEN-LAST:event_TCariProsedurKeyPressed

    private void tbProsedurMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProsedurMouseClicked
        if (sourceProsedur.equals("Diagnosa ICD IX")) {
            if (tabModeProsedure.getRowCount() != 0) {
                if (Sequel.menyimpantf2("tb_inacbg_prosedure", "?,?,?,?,?,?,?", "ICD 9", 7,
                        new String[]{norawat, tbProsedur.getValueAt(tbProsedur.getSelectedRow(), 1).toString(), status, Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_inacbg_prosedure where no_rawat=? and status='" + status + "'", norawat), "1", "1", "Ok"}) == true) {
                    JOptionPane.showMessageDialog(null, "Prosedur " + tbProsedur.getValueAt(tbProsedur.getSelectedRow(), 1).toString() + " berhasil ditambahkan");
                    tampilProsedureINACBG();
                }
            }
        } else if (sourceProsedur.equals("Diagnoa Ina Grouper")) {
            if (tabModeProsedure.getRowCount() != 0) {
                if (tbProsedur.getValueAt(tbProsedur.getSelectedRow(), 3).toString().equals("0")) {
                    JOptionPane.showMessageDialog(null, "This diagnosis code " + tbProsedur.getValueAt(tbProsedur.getSelectedRow(), 1).toString() + " is not valid for grouping");
                } else if (tbProsedur.getValueAt(tbProsedur.getSelectedRow(), 3).toString().equals("1")) {
                    if (Sequel.menyimpantf2("tb_idrg_prosedure", "?,?,?,?,?", "ICD 9", 5,
                            new String[]{norawat, tbProsedur.getValueAt(tbProsedur.getSelectedRow(), 1).toString(), status, Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_idrg_prosedure where no_rawat=? and status='" + status + "'", norawat), "1"}) == true) {
                        JOptionPane.showMessageDialog(null, "Prosedur " + tbProsedur.getValueAt(tbProsedur.getSelectedRow(), 1).toString() + " berhasil ditambahkan");
                        tampilProsedureIDRG();
                    }
                }
            }

        }
    }//GEN-LAST:event_tbProsedurMouseClicked

    private void btnTambahDiagnosaIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahDiagnosaIDRGActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        sourceDiagnosa = "Diagnoa Ina Grouper";
        labelSumber.setText("Sumber :" + sourceDiagnosa);
        FormCariDiagnosa.setFocusable(true);
        Valid.tabelKosong(tabModeDiagnosa);
        WindowSearchDiagnosa.setSize(800, 400);
        WindowSearchDiagnosa.setLocationRelativeTo(internalFrame1);
        WindowSearchDiagnosa.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnTambahDiagnosaIDRGActionPerformed

    private void btnTambahProsedurIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahProsedurIDRGActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        sourceProsedur = "Diagnoa Ina Grouper";
        labelSumberProsedur.setText("Sumber :" + sourceProsedur);
        TCariProsedur.setFocusable(true);
        Valid.tabelKosong(tabModeProsedure);
        WindowSearchProsedur.setSize(800, 400);
        WindowSearchProsedur.setLocationRelativeTo(internalFrame1);
        WindowSearchProsedur.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnTambahProsedurIDRGActionPerformed

    private void BtnCloseInpindah9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah9ActionPerformed
        WindowSearchProsedur.dispose();
    }//GEN-LAST:event_BtnCloseInpindah9ActionPerformed

    private void BtnCloseInpindah9KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah9KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah9KeyPressed

    private void WindowSearchProsedurWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowSearchProsedurWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_WindowSearchProsedurWindowActivated

    private void btnTambahProsedurINACBGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTambahProsedurINACBGActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        sourceProsedur = "Diagnosa ICD IX";
        labelSumberProsedur.setText("Sumber :" + sourceProsedur);
        TCariProsedur.setFocusable(true);
        tampilProsedur();
        WindowSearchProsedur.setSize(800, 400);
        WindowSearchProsedur.setLocationRelativeTo(internalFrame1);
        WindowSearchProsedur.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_btnTambahProsedurINACBGActionPerformed

    private void tbProsedurIDRGMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tbProsedurIDRGMouseClicked

    }//GEN-LAST:event_tbProsedurIDRGMouseClicked

    private void btnDeleteProcedureIDRGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProcedureIDRGActionPerformed
        Sequel.queryu("delete from tb_idrg_prosedure where no_rawat='" + norawat + "' and kode='" + tbProsedurIDRG.getValueAt(tbProsedurIDRG.getSelectedRow(), 0).toString() + "' and prioritas='" + tbProsedurIDRG.getValueAt(tbProsedurIDRG.getSelectedRow(), 4).toString() + "'  ");
        Sequel.queryu("delete from prosedur_pasien where no_rawat='" + norawat + "' and kode='" + tbProsedurIDRG.getValueAt(tbProsedurIDRG.getSelectedRow(), 0).toString() + "' and prioritas='" + tbProsedurIDRG.getValueAt(tbProsedurIDRG.getSelectedRow(), 4).toString() + "'  ");
        tampilProsedureIDRG();        
    }//GEN-LAST:event_btnDeleteProcedureIDRGActionPerformed

    private void CmbSpecialProcedureKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbSpecialProcedureKeyPressed

    }//GEN-LAST:event_CmbSpecialProcedureKeyPressed

    private void CmbSpecialProsthesisKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbSpecialProsthesisKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CmbSpecialProsthesisKeyPressed

    private void CmbSpecialInvestigationKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbSpecialInvestigationKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CmbSpecialInvestigationKeyPressed

    private void CmbSpeciaDrugsKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_CmbSpeciaDrugsKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_CmbSpeciaDrugsKeyPressed

    private void CmbSpecialProcedureItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CmbSpecialProcedureItemStateChanged
        if (isLoading) {
            return; // abaikan jika sedang memuat
        }
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            String cmg, cmgCode;
            getCmbCMG();
            int cmgCodeAll;

            try {
                // headers removed
                // requestEntity removed
                String _actionName = "getCmg"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\","
                        + "\"speciapCmg\": \"" + allCmg + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("JSON : " + root);
                response = root.path("response_inacbg");
                responseCmgOption = root.path("special_cmg_option");
                if (root.path("metadata").path("code").asText().equals("200")) {
                    Sequel.mengedit("tb_eklaim_grouping_result", "no_rawat=?", "tariff=?", 2, new String[]{
                        root.path("response_inacbg").path("tariff").asText(), norawat
                    });
                    totalCostHasilKlaim.setText(Valid.SetAngka(Double.parseDouble(root.path("response_inacbg").path("tariff").asText())));
                    Sequel.queryu("delete from tb_inacbg_special_cmg where no_rawat='" + norawat + "' ");

                    if (responseCmgOption.isArray()) {
                        i = 1;
                        for (JsonNode list : responseCmgOption) {
                            Sequel.menyimpantf2("tb_inacbg_special_cmg", "?,?,?,?,?,?,?", "No.Rawat", 7,
                                    new String[]{norawat, list.path("code").asText(), list.path("description").asText(), list.path("type").asText(), "0", "", ""});

//                    tabModeDiagnosa.addRow(new Object[]{
//                        false, list.path("kode").asText(), list.path("description").asText(), list.path("validcode").asText(), list.path("accpdx").asText(), list.path("code_asterisk").asText(), list.path("asterisk").asText(), list.path("im").asText()
//                    });
                            i++;
                        }
                    }
                    if (response.path("special_cmg").isArray()) {
                        j = 1;
                        for (JsonNode list : response.path("special_cmg")) {
                            cmgCodeAll = list.path("code").asText().split("-").length;
                            if (cmgCodeAll <= 3) {
                                cmgCode = list.path("code").asText().split("-")[0] + list.path("code").asText().split("-")[1];
                            } else {
                                cmgCode = list.path("code").asText().split("-")[0] + list.path("code").asText().split("-")[1] + list.path("code").asText().split("-")[3];
                            }
                            Sequel.mengedit("tb_inacbg_special_cmg", "no_rawat=? and code=?", "code_cmg=?,tariff=?,selected=?", 5, new String[]{
                                list.path("code").asText(), list.path("tariff").asText(), "1", norawat, cmgCode
                            });
                            if (list.path("type").asText().equals("Special Prosthesis")) {
                                SpecialProsthesisHasilCode.setText(list.path("code").asText());
                                SpecialProsthesisHasilCost.setText(Valid.SetAngka(Double.parseDouble(list.path("tariff").asText())));
                            }
                            if (list.path("type").asText().equals("Special Procedure")) {
                                SpecialProcedureHasilCode.setText(list.path("code").asText());
                                SpecialProcedureHasilCost.setText(Valid.SetAngka(Double.parseDouble(list.path("tariff").asText())));
                            }

//                            tabModeDiagnosa.addRow(new Object[]{
//                                false, list.path("kode").asText(), list.path("description").asText(), list.path("validcode").asText(), list.path("accpdx").asText(), list.path("code_asterisk").asText(), list.path("asterisk").asText(), list.path("im").asText()
//                            });
                            j++;
                        }
                    }

                    getSpecialCMG();
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }

        }

    }//GEN-LAST:event_CmbSpecialProcedureItemStateChanged

    private void CmbSpecialProsthesisItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_CmbSpecialProsthesisItemStateChanged
        if (isLoading) {
            return; // abaikan jika sedang memuat
        }
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            try {
                String cmg, cmgCode;
                getCmbCMG();
                int cmgCodeAll, cmgList;

                // headers removed
                // requestEntity removed
                String _actionName = "getCmg"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\","
                        + "\"speciapCmg\": \"" + allCmg + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("JSON : " + root);
                response = root.path("response_inacbg");
                responseCmgOption = root.path("special_cmg_option");
                if (root.path("metadata").path("code").asText().equals("200")) {
                    Sequel.mengedit("tb_eklaim_grouping_result", "no_rawat=?", "tariff=?", 2, new String[]{
                        root.path("response_inacbg").path("tariff").asText(), norawat
                    });
                    totalCostHasilKlaim.setText(Valid.SetAngka(Double.parseDouble(root.path("response_inacbg").path("tariff").asText())));
                    Sequel.queryu("delete from tb_inacbg_special_cmg where no_rawat='" + norawat + "' ");

                    if (responseCmgOption.isArray()) {
                        i = 1;
                        for (JsonNode list : responseCmgOption) {
                            Sequel.menyimpantf2("tb_inacbg_special_cmg", "?,?,?,?,?,?,?", "No.Rawat", 7,
                                    new String[]{norawat, list.path("code").asText(), list.path("description").asText(), list.path("type").asText(), "0", "", ""});

//                    tabModeDiagnosa.addRow(new Object[]{
//                        false, list.path("kode").asText(), list.path("description").asText(), list.path("validcode").asText(), list.path("accpdx").asText(), list.path("code_asterisk").asText(), list.path("asterisk").asText(), list.path("im").asText()
//                    });
                            i++;
                        }
                    }
                    if (response.path("special_cmg").isArray()) {
                        j = 1;
                        for (JsonNode list : response.path("special_cmg")) {
                            cmgCodeAll = list.path("code").asText().split("-").length;
                            if (cmgCodeAll <= 3) {
                                cmgCode = list.path("code").asText().split("-")[0] + list.path("code").asText().split("-")[1];
                            } else {
                                cmgCode = list.path("code").asText().split("-")[0] + list.path("code").asText().split("-")[1] + list.path("code").asText().split("-")[3];
                            }
                            Sequel.mengedit("tb_inacbg_special_cmg", "no_rawat=? and code=?", "code_cmg=?,tariff=?,selected=?", 5, new String[]{
                                list.path("code").asText(), list.path("tariff").asText(), "1", norawat, cmgCode
                            });
                            if (list.path("type").asText().equals("Special Prosthesis")) {
                                SpecialProsthesisHasilCode.setText(list.path("code").asText());
                                SpecialProsthesisHasilCost.setText(Valid.SetAngka(Double.parseDouble(list.path("tariff").asText())));
                            }
                            if (list.path("type").asText().equals("Special Procedure")) {
                                SpecialProcedureHasilCode.setText(list.path("code").asText());
                                SpecialProcedureHasilCost.setText(Valid.SetAngka(Double.parseDouble(list.path("tariff").asText())));
                            }

//                            tabModeDiagnosa.addRow(new Object[]{
//                                false, list.path("kode").asText(), list.path("description").asText(), list.path("validcode").asText(), list.path("accpdx").asText(), list.path("code_asterisk").asText(), list.path("asterisk").asText(), list.path("im").asText()
//                            });
                            j++;
                        }
                    }
                    getSpecialCMG();
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
        }
    }//GEN-LAST:event_CmbSpecialProsthesisItemStateChanged

    private void chkEksekutifItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkEksekutifItemStateChanged
        if (chkEksekutif.isSelected() == true) {
            FormDataPelayanan.setSize(internalFrameDataPasien.getWidth(), 200);
            internalFrameDataPasien.setSize(internalFrame1.getWidth(), 615);
            FrameKoding.setLocation(0, 621);
            panelTarifPoliEksekutif.setVisible(true);
        } else {
            panelTarifPoliEksekutif.setVisible(false);
            internalFrameDataPasien.setSize(internalFrame1.getWidth(), 574);
            FrameKoding.setLocation(0, 580);
        }        // TODO add your handling code here:
    }//GEN-LAST:event_chkEksekutifItemStateChanged

    private void chkNaikKelasItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkNaikKelasItemStateChanged
        if (chkNaikKelas.isSelected() == true) {
            if (chkIntensif.isSelected() == true) {
//                internalFrameDataPasien.setSize(internalFrame1.getWidth(), 660);
                FrameKoding.setLocation(0, 666);
                panelRawatIntensif.setVisible(true);
                panelRawatIntensif.setLocation(20, 210);
            } else {
//                internalFrameDataPasien.setSize(internalFrame1.getWidth(), 615);
                FrameKoding.setLocation(0, 621);
                panelRawatIntensif.setVisible(false);
            }

            panelNaikKelas.setVisible(true);
        } else {
            if (chkIntensif.isSelected() == true) {
//                internalFrameDataPasien.setSize(internalFrame1.getWidth(), 615);
                FrameKoding.setLocation(0, 621);
                panelRawatIntensif.setVisible(true);
                panelRawatIntensif.setLocation(20, 170);
            } else {
//                internalFrameDataPasien.setSize(internalFrame1.getWidth(), 574);
                FrameKoding.setLocation(0, 580);
                panelRawatIntensif.setVisible(false);
            }
            panelNaikKelas.setVisible(false);

        }
    }//GEN-LAST:event_chkNaikKelasItemStateChanged

    private void chkIntensifItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkIntensifItemStateChanged
        if (chkIntensif.isSelected() == true) {
            if (chkNaikKelas.isSelected() == true) {
                panelRawatIntensif.setLocation(20, 210);
                internalFrameDataPasien.setSize(internalFrame1.getWidth(), 660);
                FrameKoding.setLocation(0, 666);
            } else {
                panelRawatIntensif.setLocation(20, 170);
                internalFrameDataPasien.setSize(internalFrame1.getWidth(), 615);
                FrameKoding.setLocation(0, 621);
            }
            panelRawatIntensif.setVisible(true);
        } else {
            panelRawatIntensif.setVisible(false);
            internalFrameDataPasien.setSize(internalFrame1.getWidth(), 574);
            FrameKoding.setLocation(0, 580);
        }
    }//GEN-LAST:event_chkIntensifItemStateChanged

    private void subAcuteKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_subAcuteKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_subAcuteKeyPressed

    private void chronicKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_chronicKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_chronicKeyPressed

    private void tarifPoliEksekutifKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tarifPoliEksekutifKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tarifPoliEksekutifKeyPressed

    private void tarifPoliEksekutifKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tarifPoliEksekutifKeyReleased
        costPoliEksekutif.setText(Valid.SetAngka(Double.parseDouble(tarifPoliEksekutif.getText())));
    }//GEN-LAST:event_tarifPoliEksekutifKeyReleased

    private void tarifPoliEksekutif2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tarifPoliEksekutif2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tarifPoliEksekutif2KeyPressed

    private void tarifPoliEksekutif2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tarifPoliEksekutif2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_tarifPoliEksekutif2KeyReleased

    private void icuLosKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_icuLosKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_icuLosKeyPressed

    private void icuLosKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_icuLosKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_icuLosKeyReleased

    private void chkVentilatorItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_chkVentilatorItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_chkVentilatorItemStateChanged

    private void tglIntubasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tglIntubasiKeyPressed
        //Valid.pindah(evt,Rencana,Informasi);
    }//GEN-LAST:event_tglIntubasiKeyPressed

    private void tglEkstubasiKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tglEkstubasiKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_tglEkstubasiKeyPressed

    private void cmbCaraMasukItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbCaraMasukItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCaraMasukItemStateChanged

    private void cmbCaraMasukKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmbCaraMasukKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbCaraMasukKeyPressed

    private void cmrCaraPulangItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmrCaraPulangItemStateChanged
        // TODO add your handling code here:
    }//GEN-LAST:event_cmrCaraPulangItemStateChanged

    private void cmrCaraPulangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cmrCaraPulangKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmrCaraPulangKeyPressed

    private void btnDeleteProcedureINACBGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteProcedureINACBGActionPerformed
 Sequel.queryu("delete from tb_inacbg_prosedure where no_rawat='" + norawat + "' and kode='" + tbProsedureINACBG.getValueAt(tbProsedureINACBG.getSelectedRow(), 0).toString() + "'  ");
        tampilProsedureINACBG();
    }//GEN-LAST:event_btnDeleteProcedureINACBGActionPerformed

    private void txtPriorityKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPriorityKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtPriorityKeyPressed

    private void BtnCloseInpindah10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah10ActionPerformed
      WindowDiagnosaDokter.dispose();
    }//GEN-LAST:event_BtnCloseInpindah10ActionPerformed

    private void BtnCloseInpindah10KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah10KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah10KeyPressed

    private void WindowDiagnosaDokterWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowDiagnosaDokterWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_WindowDiagnosaDokterWindowActivated

    private void btnImportDiagnosaDokterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnImportDiagnosaDokterActionPerformed
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));        
        WindowDiagnosaDokter.setSize(800, 500);
        WindowDiagnosaDokter.setLocationRelativeTo(internalFrame1);
        WindowDiagnosaDokter.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
        tampilDiagnosaPilih();
        tampilProsedurPilih();
    }//GEN-LAST:event_btnImportDiagnosaDokterActionPerformed

    private void BtnCloseInpindah11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCloseInpindah11ActionPerformed
       WindowCetak.dispose();
    }//GEN-LAST:event_BtnCloseInpindah11ActionPerformed

    private void BtnCloseInpindah11KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCloseInpindah11KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_BtnCloseInpindah11KeyPressed

    private void WindowCetakWindowActivated(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_WindowCetakWindowActivated
        // TODO add your handling code here:
    }//GEN-LAST:event_WindowCetakWindowActivated

    private void btnDiagnosaLocalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiagnosaLocalActionPerformed
        // TODO add your handling code here:
        akses.setform("RMDataResumePasienRanap");
        diagnosa.setSize(internalFrame1.getWidth()-600,internalFrame1.getHeight()-200);
        diagnosa.setLocationRelativeTo(internalFrame1);
        diagnosa.isCek();
                                        try{
                                    date = new SimpleDateFormat("yyyy-MM-dd").parse(tglMasuk.getText());
                                }catch(Exception e){
                                    date=new Date();
                                }
        diagnosa.setNoRm(txtNoRawat.getText(),date,date,jnsLayanan.getText());
        diagnosa.panelDiagnosa1.tampil();
        diagnosa.setVisible(true);
    }//GEN-LAST:event_btnDiagnosaLocalActionPerformed

    private void BriwayatperawatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BriwayatperawatanActionPerformed
        // TODO add your handling code here:
        if(txtNoRawat.getText().trim().equals("")){
            JOptionPane.showMessageDialog(null,"Maaf, No rawat kosong");

        }else{
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            RMRiwayatPerawatan resume=new RMRiwayatPerawatan(null,true);
            resume.setNoRm(txtNoRm.getText(),txtNamaPasien.getText());
            resume.setSize(internalFrame1.getWidth()-600,internalFrame1.getHeight()-110);
            resume.setLocationRelativeTo(internalFrame1);
            resume.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }
    }//GEN-LAST:event_BriwayatperawatanActionPerformed

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            PKUDlgKlaimEKlaimRajal dialog = new PKUDlgKlaimEKlaimRajal(new javax.swing.JFrame(), true);
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
    private javax.swing.JButton Briwayatperawatan;
    private widget.Button BtnCari2;
    private widget.Button BtnCari3;
    private widget.Button BtnCloseInpindah10;
    private widget.Button BtnCloseInpindah11;
    private widget.Button BtnCloseInpindah2;
    private widget.Button BtnCloseInpindah3;
    private widget.Button BtnCloseInpindah5;
    private widget.Button BtnCloseInpindah6;
    private widget.Button BtnCloseInpindah7;
    private widget.Button BtnCloseInpindah8;
    private widget.Button BtnCloseInpindah9;
    private widget.Button BtnKeluar1;
    private widget.CekBox ChkPasienTB;
    private widget.ComboBox CmbSpeciaDrugs;
    private widget.ComboBox CmbSpecialInvestigation;
    private widget.ComboBox CmbSpecialProcedure;
    private widget.ComboBox CmbSpecialProsthesis;
    private widget.ComboBox CmbVol;
    private widget.TextBox FormCariDiagnosa;
    private widget.PanelBiasa FormDataPelayanan;
    private widget.PanelBiasa FormInput;
    private widget.PanelBiasa FormInput1;
    private widget.PanelBiasa FormInput10;
    private widget.PanelBiasa FormInput3;
    private widget.PanelBiasa FormInput4;
    private widget.PanelBiasa FormInput5;
    private widget.PanelBiasa FormInput6;
    private widget.PanelBiasa FormInput7;
    private widget.PanelBiasa FormInput8;
    private widget.PanelBiasa FormInput9;
    private widget.PanelBiasa FormInputHasilIDRG;
    private widget.PanelBiasa FormInputHasilINACBG;
    private widget.PanelBiasa FormInputIDRG;
    private widget.PanelBiasa FormInputINACBG;
    private widget.PanelBiasa FormInputStatusKlaim;
    private widget.InternalFrame FormKodingIDRG;
    private widget.InternalFrame FormKodingINACBG;
    private widget.InternalFrame FrameKoding;
    private widget.TextBox Jk;
    private widget.RadioButton R1;
    private widget.RadioButton R2;
    private widget.RadioButton R3;
    private widget.RadioButton R4;
    private widget.ScrollPane Scroll16;
    private widget.ScrollPane Scroll17;
    private widget.ScrollPane Scroll18;
    private widget.ScrollPane Scroll19;
    private widget.ScrollPane Scroll2;
    private widget.ScrollPane Scroll20;
    private widget.ScrollPane Scroll5;
    private widget.ScrollPane Scroll6;
    private widget.Label SpecialProcedureHasilCode;
    private widget.Label SpecialProcedureHasilCost;
    private widget.Label SpecialProsthesisHasilCode;
    private widget.Label SpecialProsthesisHasilCost;
    private widget.TextBox TCariProsedur;
    private widget.TextBox TglLahir;
    private javax.swing.JDialog WindowCetak;
    private javax.swing.JDialog WindowDiagnosaDokter;
    private javax.swing.JDialog WindowHasilIDRG;
    private javax.swing.JDialog WindowHasilINACBG;
    private javax.swing.JDialog WindowHasilKlaim;
    private javax.swing.JDialog WindowMultiplicityProcedure;
    private javax.swing.JDialog WindowSearchDiagnosa;
    private javax.swing.JDialog WindowSearchProsedur;
    private widget.TextBox beratLahir;
    private widget.Button btnCetakKlaim;
    private javax.swing.JMenuItem btnDeleteDiagnosaIDRG;
    private javax.swing.JMenuItem btnDeleteDiagnosaINACBG;
    private javax.swing.JMenuItem btnDeleteProcedureIDRG;
    private javax.swing.JMenuItem btnDeleteProcedureINACBG;
    private widget.Button btnDiagnosaLocal;
    private widget.Button btnEditIDRG;
    private widget.Button btnEditINACBG;
    private widget.Button btnEditKlaim;
    private widget.Button btnFinalIDRG;
    private widget.Button btnFinalINACBG;
    private widget.Button btnFinalKlaim;
    private widget.Button btnGroupingIDRG;
    private widget.Button btnGroupingINACBG;
    private widget.Button btnHasilFinalKlaim;
    private widget.Button btnImportDiagnosaDokter;
    private widget.Button btnImportToIDRG;
    private widget.Button btnImportToINACBG;
    private widget.Button btnKirimOnline;
    private javax.swing.JMenuItem btnMultiplicity;
    private widget.Button btnTabahDiagnosaINACBG;
    private widget.Button btnTambahDiagnosaIDRG;
    private widget.Button btnTambahProsedurIDRG;
    private widget.Button btnTambahProsedurINACBG;
    private widget.Button btnUpdateDataKlaim;
    private widget.Button btnValidasiSITB;
    private javax.swing.ButtonGroup buttonGroupNaikKelas;
    private widget.CekBox chkEksekutif;
    private widget.CekBox chkIntensif;
    private widget.CekBox chkNaikKelas;
    private widget.CekBox chkVentilator;
    private widget.TextBox chronic;
    private widget.Label chronicHasilINACBG;
    private widget.ComboBox cmbCaraMasuk;
    private widget.ComboBox cmrCaraPulang;
    private widget.Label costPoliEksekutif;
    private widget.TextBox diastol;
    private widget.Label drgCodeHasiliDRG;
    private widget.Label drgHasiliDRG;
    private widget.Label groupCodeHasilINACBG;
    private widget.Label groupCodeHasilINACBG1;
    private widget.Label groupCodeHasilINACBG2;
    private widget.Label groupCodeHasilINACBG5;
    private widget.Label groupCodeHasilINACBG7;
    private widget.Label groupCostHasilINACBG;
    private widget.Label groupCostHasilINACBG1;
    private widget.Label groupCostHasilINACBG2;
    private widget.Label groupCostHasilINACBG5;
    private widget.Label groupCostHasilINACBG6;
    private widget.Label groupHasilINACBG;
    private widget.TextBox icuLos;
    private widget.Label infoHasilINACBG;
    private widget.Label infoHasiliDRG;
    private widget.InternalFrame internalFrame1;
    private widget.InternalFrame internalFrame10;
    private widget.InternalFrame internalFrame11;
    private widget.InternalFrame internalFrame12;
    private widget.InternalFrame internalFrame13;
    private widget.InternalFrame internalFrame14;
    private widget.InternalFrame internalFrame15;
    private widget.InternalFrame internalFrame16;
    private widget.InternalFrame internalFrame17;
    private widget.InternalFrame internalFrame18;
    private widget.InternalFrame internalFrame19;
    private widget.InternalFrame internalFrame20;
    private widget.InternalFrame internalFrame21;
    private widget.InternalFrame internalFrame22;
    private widget.InternalFrame internalFrame23;
    private widget.InternalFrame internalFrame24;
    private widget.InternalFrame internalFrame25;
    private widget.InternalFrame internalFrame26;
    private widget.InternalFrame internalFrame27;
    private widget.InternalFrame internalFrame28;
    private widget.InternalFrame internalFrame29;
    private widget.InternalFrame internalFrame30;
    private widget.InternalFrame internalFrame31;
    private widget.InternalFrame internalFrame32;
    private widget.InternalFrame internalFrame33;
    private widget.InternalFrame internalFrame34;
    private widget.InternalFrame internalFrame35;
    private widget.InternalFrame internalFrame36;
    private widget.InternalFrame internalFrame37;
    private widget.InternalFrame internalFrame38;
    private widget.InternalFrame internalFrame39;
    private widget.InternalFrame internalFrame4;
    private widget.InternalFrame internalFrame40;
    private widget.InternalFrame internalFrame5;
    private widget.InternalFrame internalFrame6;
    private widget.InternalFrame internalFrame8;
    private widget.InternalFrame internalFrame9;
    private widget.InternalFrame internalFrameD1ataPasien;
    private widget.InternalFrame internalFrameDataPasien;
    private widget.InternalFrame internalFrameDiagnosaPasien;
    private widget.InternalFrame internalFrameDiagnosaPasien1;
    private widget.InternalFrame internalFrameEndKlaim;
    private widget.Label jLabel10;
    private widget.Label jLabel100;
    private widget.Label jLabel101;
    private widget.Label jLabel102;
    private widget.Label jLabel103;
    private widget.Label jLabel104;
    private widget.Label jLabel105;
    private widget.Label jLabel106;
    private widget.Label jLabel107;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel136;
    private widget.Label jLabel137;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel17;
    private widget.Label jLabel18;
    private widget.Label jLabel19;
    private widget.Label jLabel20;
    private widget.Label jLabel21;
    private widget.Label jLabel22;
    private widget.Label jLabel23;
    private widget.Label jLabel24;
    private widget.Label jLabel25;
    private widget.Label jLabel26;
    private widget.Label jLabel27;
    private widget.Label jLabel28;
    private widget.Label jLabel29;
    private widget.Label jLabel3;
    private widget.Label jLabel30;
    private widget.Label jLabel31;
    private widget.Label jLabel32;
    private widget.Label jLabel33;
    private widget.Label jLabel34;
    private widget.Label jLabel35;
    private widget.Label jLabel36;
    private widget.Label jLabel37;
    private widget.Label jLabel38;
    private widget.Label jLabel39;
    private widget.Label jLabel4;
    private widget.Label jLabel40;
    private widget.Label jLabel41;
    private widget.Label jLabel42;
    private widget.Label jLabel43;
    private widget.Label jLabel44;
    private widget.Label jLabel45;
    private widget.Label jLabel46;
    private widget.Label jLabel47;
    private widget.Label jLabel48;
    private widget.Label jLabel49;
    private widget.Label jLabel5;
    private widget.Label jLabel50;
    private widget.Label jLabel51;
    private widget.Label jLabel52;
    private widget.Label jLabel53;
    private widget.Label jLabel54;
    private widget.Label jLabel55;
    private widget.Label jLabel56;
    private widget.Label jLabel57;
    private widget.Label jLabel58;
    private widget.Label jLabel59;
    private widget.Label jLabel6;
    private widget.Label jLabel60;
    private widget.Label jLabel61;
    private widget.Label jLabel62;
    private widget.Label jLabel63;
    private widget.Label jLabel64;
    private widget.Label jLabel65;
    private widget.Label jLabel66;
    private widget.Label jLabel67;
    private widget.Label jLabel68;
    private widget.Label jLabel69;
    private widget.Label jLabel7;
    private widget.Label jLabel70;
    private widget.Label jLabel71;
    private widget.Label jLabel72;
    private widget.Label jLabel73;
    private widget.Label jLabel74;
    private widget.Label jLabel75;
    private widget.Label jLabel76;
    private widget.Label jLabel77;
    private widget.Label jLabel78;
    private widget.Label jLabel79;
    private widget.Label jLabel8;
    private widget.Label jLabel80;
    private widget.Label jLabel81;
    private widget.Label jLabel82;
    private widget.Label jLabel83;
    private widget.Label jLabel84;
    private widget.Label jLabel85;
    private widget.Label jLabel86;
    private widget.Label jLabel87;
    private widget.Label jLabel88;
    private widget.Label jLabel89;
    private widget.Label jLabel9;
    private widget.Label jLabel90;
    private widget.Label jLabel91;
    private widget.Label jLabel92;
    private widget.Label jLabel93;
    private widget.Label jLabel94;
    private widget.Label jLabel95;
    private widget.Label jLabel96;
    private widget.Label jLabel97;
    private widget.Label jLabel98;
    private javax.swing.JScrollPane jScrollPane1;
    private widget.TextBox jamMasuk;
    private widget.TextBox jamPulang;
    private widget.TextBox jnsLayanan;
    private widget.Label jnsrawatHasilINACBG;
    private widget.Label jsnrawatHasiliDRG;
    private widget.TextBox kdDokter;
    private widget.TextBox kdPoli;
    private widget.Label labelSumber;
    private widget.Label labelSumberProsedur;
    private widget.Label mdcCodeHasiliDRG;
    private widget.Label mdcHasiliDRG;
    private widget.TextBox nmDokter;
    private widget.TextBox nmPoli;
    private widget.TextBox noKartuJkn;
    private widget.PanelBiasa panelBiasa10;
    private widget.PanelBiasa panelBiasa11;
    private widget.PanelBiasa panelBiasa12;
    private widget.PanelBiasa panelBiasa13;
    private widget.PanelBiasa panelBiasa16;
    private widget.PanelBiasa panelBiasa17;
    private widget.PanelBiasa panelBiasa8;
    private widget.PanelBiasa panelBiasa9;
    private widget.PanelBiasa panelCeklis;
    private widget.PanelBiasa panelHasilIDRG;
    private widget.PanelBiasa panelHasilINACBG;
    private widget.PanelBiasa panelHasilINACBG1;
    private widget.PanelBiasa panelNaikKelas;
    private widget.PanelBiasa panelRawatIntensif;
    private widget.PanelBiasa panelTarifPoliEksekutif;
    private widget.panelisi panelisi5;
    private javax.swing.JPopupMenu ppDiagnosaIDRG;
    private javax.swing.JPopupMenu ppDiagnosaINACBG;
    private javax.swing.JPopupMenu ppProsedurIDRG;
    private javax.swing.JPopupMenu ppProsedurINACBG;
    private widget.ScrollPane scrollPane1;
    private widget.Button setDataKlaim;
    private widget.TextBox sistol;
    private widget.Label statusDC;
    private widget.Label statusHasilInacbg;
    private widget.Label statusHasiliDRG;
    private widget.Label statusKlaim;
    private widget.TextBox subAcute;
    private widget.Label subAcuteHasilINACBG;
    private widget.TextBox tarifPoliEksekutif;
    private widget.TextBox tarifPoliEksekutif2;
    public widget.Table tbDiagnosa;
    public widget.Table tbDiagnosaIDRG;
    public widget.Table tbDiagnosaIINACBG;
    public widget.Table tbDiagnosaPasien;
    public widget.Table tbProsedur;
    public widget.Table tbProsedurIDRG;
    public widget.Table tbProsedureINACBG;
    public widget.Table tbProsedurePasien;
    private widget.TextBox tfAlkes;
    private widget.TextBox tfBMHP;
    private widget.TextBox tfBedah;
    private widget.TextBox tfDarah;
    private widget.TextBox tfKamar;
    private widget.TextBox tfKeperawatan;
    private widget.TextBox tfKonsultasi;
    private widget.TextBox tfLaboratorium;
    private widget.TextBox tfNonBedah;
    private widget.TextBox tfObat;
    private widget.TextBox tfObatKemoterapi;
    private widget.TextBox tfObatKronis;
    private widget.TextBox tfPenunjang;
    private widget.TextBox tfRadiologi;
    private widget.TextBox tfRawatIntensif;
    private widget.TextBox tfRehabilitasi;
    private widget.TextBox tfSewaAlat;
    private widget.TextBox tfTenagaAhli;
    private widget.TextBox tfTotal;
    private widget.Tanggal tglEkstubasi;
    private widget.Tanggal tglIntubasi;
    private widget.TextBox tglMasuk;
    private widget.TextBox tglPulang;
    private widget.Label totalCostHasilKlaim;
    private widget.TextBox txtCronic;
    private widget.TextBox txtCronicCode;
    private widget.TextBox txtCronicCost;
    private widget.TextBox txtDrgCode;
    private widget.TextBox txtDrgDescription;
    private widget.TextBox txtGroup;
    private widget.TextBox txtGroupCode;
    private widget.TextBox txtGroupCost;
    private widget.TextBox txtHakKelas;
    private widget.TextBox txtIDSITB;
    private widget.TextBox txtInfoHasilIDRG;
    private widget.TextBox txtInfoHasilINACBG;
    private widget.TextBox txtMdcCode;
    private widget.TextBox txtMdcDescription;
    private widget.TextBox txtNamaPasien;
    private widget.TextBox txtNoRawat;
    private widget.TextBox txtNoRm;
    private widget.TextBox txtNoSep;
    private widget.TextBox txtPriority;
    private widget.TextBox txtProcedureIDRGCode;
    private widget.TextBox txtProcedureIDRGName;
    private widget.TextBox txtSpecDrug;
    private widget.TextBox txtSpecDrugCode;
    private widget.TextBox txtSpecDrugCost;
    private widget.TextBox txtSpecInvestigation;
    private widget.TextBox txtSpecInvestigationCode;
    private widget.TextBox txtSpecInvestigationCost;
    private widget.TextBox txtSpecProcedure;
    private widget.TextBox txtSpecProcedureCode;
    private widget.TextBox txtSpecProcedureCost;
    private widget.TextBox txtSpecProthesis;
    private widget.TextBox txtSpecProthesisCode;
    private widget.TextBox txtSpecProthesisCost;
    private widget.TextBox txtStatus;
    private widget.TextBox txtStatus1;
    private widget.TextBox txtStatusINACBG;
    private widget.TextBox txtStatusKlaim;
    private widget.TextBox txtSubAcute;
    private widget.TextBox txtSubAcuteCode;
    private widget.TextBox txtSubAcuteCost;
    private widget.TextBox txtTotalKlaim;
    private widget.TextBox txtjnsRawat;
    // End of variables declaration//GEN-END:variables
 public void setDataPasien(String noRawat, String noRkmMedis, String nmPasien, String statusKunjungan) {
        this.norawat = noRawat;
        this.noSep = Sequel.cariIsi("select no_sep from bridging_sep where no_rawat='" + noRawat + "'");
        this.norkmMedis = noRkmMedis;
        this.namaPasien = nmPasien.replaceAll("\\s*\\([^)]*Th\\)", "").trim();
        this.status = statusKunjungan;
        this.dokter = Sequel.cariIsi("select nm_dokter from reg_periksa JOIN dokter ON reg_periksa.kd_dokter=dokter.kd_dokter where no_rawat='" + noRawat + "'");
        this.kodedokter = Sequel.cariIsi("select kd_dokter from reg_periksa  where no_rawat='" + noRawat + "'");
        this.dokter_rnp = Sequel.cariIsi("select nm_dokter from dpjp_ranap JOIN dokter ON dpjp_ranap.kd_dokter=dokter.kd_dokter where no_rawat='" + noRawat + "'");
        this.kodedokter_rnp = Sequel.cariIsi("select kd_dokter from dpjp_ranap  where no_rawat='" + noRawat + "'");
        this.tglAwal = Sequel.cariIsi("select concat(tgl_registrasi, ' ',jam_reg) as tglawal from reg_periksa  where no_rawat='" + noRawat + "'");
        this.tglAkhir = Sequel.cariIsi("select SUBSTRING(tglpulang, 1, 19) AS tglpulang from bridging_sep  where no_rawat='" + noRawat + "'");
        this.jnsKlaim = Sequel.cariIsi("select jnspelayanan from bridging_sep where no_rawat='" + noRawat + "'");
        this.hakKelas = (Sequel.cariIsi("select klsrawat from bridging_sep where no_rawat='" + noRawat + "'"));
        this.tensi = Sequel.cariIsi("select tensi from pemeriksaan_ranap  where no_rawat='" + noRawat + "' order by tgl_perawatan desc,jam_rawat desc  limit 1");
        txtNoSep.setText(noSep);
        txtNoRawat.setText(norawat);
        txtNoRm.setText(norkmMedis);
        txtNamaPasien.setText(namaPasien);
        if (statusKunjungan.equals("Ralan")) {
            jLabel70.setVisible(true);
            chkEksekutif.setVisible(true);
            chkNaikKelas.setVisible(false);
            jLabel71.setVisible(false);
            chkIntensif.setVisible(false);
            jLabel97.setVisible(false);
            kdDokter.setText(kodedokter);
            nmDokter.setText(dokter);
        } else {
            jLabel70.setVisible(false);
            chkEksekutif.setVisible(false);
            chkNaikKelas.setVisible(true);
            jLabel71.setVisible(true);
            chkIntensif.setVisible(true);
            jLabel97.setVisible(true);
            kdDokter.setText(kodedokter_rnp);
            nmDokter.setText(dokter_rnp);
            
        }

//        caraPulang=Sequel.cariIsi("select nm_dokter from reg_periksa JOIN dokter ON reg_periksa.kd_dokter=dokter.kd_dokter where no_rawat='" + noRawat + "'");
        try {
            ps = koneksi.prepareStatement(
                    "select * from pasien where no_rkm_medis='" + noRkmMedis + "'");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    this.tglLahir = rs.getString("tgl_lahir");
                    this.noKartu = rs.getString("no_peserta");
                    this.jkPasien = rs.getString("jk");
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
        noKartuJkn.setText(noKartu);
        TglLahir.setText(tglLahir);
        Jk.setText(jkPasien);
        jnsLayanan.setText(statusKunjungan);
        tglMasuk.setText(tglAwal.split(" ")[0]);
        jamMasuk.setText(tglAwal.split(" ")[1]);
        tglPulang.setText(tglAkhir.split(" ")[0]);
//        jamPulang.setText(tglAkhir.split(" ")[1]);
        jamPulang.setText(tglAwal.split(" ")[1]);
        String[] parts = (tglAkhir == null) ? new String[0] : tglAkhir.split(" ", 2);
//        jamPulang.setText(parts.length > 1 ? parts[1] : "");

        txtHakKelas.setText(hakKelas);
//        kdDokter.setText(kodedokter);
//        nmDokter.setText(dokter);
        kdPoli.setText(Sequel.cariIsi("select kd_poli from reg_periksa  where no_rawat='" + noRawat + "'"));
        nmPoli.setText(Sequel.cariIsi("select nm_poli from poliklinik  where kd_poli='" + kdPoli.getText() + "'"));
        sistol.setText((tensi.contains("/") ? tensi.split("/")[0] : "0"));
        diastol.setText((tensi.contains("/") ? tensi.split("/")[1] : "0"));
        tampilDiagnosaPilih();
        tampilDiagnosaIDRG();
        tampilProsedurPilih();
        tampilProsedureIDRG();
        cekStatusKlaim();
        getDataBilling();
        tampilDiagnosaINACBG();
        tampilProsedureINACBG();
                        //tambahan
                        // DEBUG: cek user login dan nik coder
//                System.out.println("DEBUG Login ID (akses.getkode()): " + akses.getkode());

                nikCoder = Sequel.cariIsi(
                    "SELECT no_ik FROM inacbg_coder_nik WHERE nik=?",
                    akses.getkode()
                );

//                System.out.println("DEBUG Hasil Query nikCoder: " + nikCoder);

    }

    private void kirimKlaim() {
        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah sudah bener data ini kirim ke E-Klaim ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                String dischargeStatus, caraMasuk, rawatIntensif, ventilator, start_dttm, stop_dttm, HakKelas;
                switch (cmrCaraPulang.getSelectedItem().toString()) {
                    case "Atas persetujuan dokter":
                        dischargeStatus = "1";
                        break;
                    case "Dirujuk":
                        dischargeStatus = "2";
                        break;
                    case "Atas permintaan sendiri":
                        dischargeStatus = "3";
                        break;
                    case "Meninggal":
                        dischargeStatus = "4";
                        break;
                    case "Lain-lain":
                        dischargeStatus = "5";
                        break;
                    default:
                        dischargeStatus = "1";
                        break;
                }
                switch (cmbCaraMasuk.getSelectedItem().toString()) {
                    case "Rujukan FKTP":
                        caraMasuk = "gp";
                        break;
                    case "Rujukan FKRTL":
                        caraMasuk = "osp-trans";
                        break;
                    case "Rujukan Spesialis":
                        caraMasuk = "mp";
                        break;
                    case "Dari Rawat Jalan":
                        caraMasuk = "outp";
                        break;
                    case "Dari Rawat Inap":
                        caraMasuk = "inp";
                        break;
                    case "Dari Rawat Darurat":
                        caraMasuk = "emd";
                        break;
                    case "Lahir di RS":
                        caraMasuk = "born";
                        break;
                    case "Rujukan Panti Jompo":
                        caraMasuk = "nursing";
                        break;
                    case "Rujukan dari RS Jiwa":
                        caraMasuk = "psych";
                        break;
                    case "Rujukan Fasilitas Rehab":
                        caraMasuk = "rehab";
                        break;
                    case "Lain-lain":
                        caraMasuk = "other";
                        break;
                    default:
                        caraMasuk = "gp";
                        break;
                }
                if (chkIntensif.isSelected() == true) {
                    rawatIntensif = "1";
                } else {
                    rawatIntensif = "0";
                }
                if (chkVentilator.isSelected() == true) {
                    ventilator = "1";
                    start_dttm = Valid.SetTgl(tglIntubasi.getSelectedItem() + "") + " " + tglIntubasi.getSelectedItem().toString().substring(11, 19);
                    stop_dttm = Valid.SetTgl(tglEkstubasi.getSelectedItem() + "") + " " + tglEkstubasi.getSelectedItem().toString().substring(11, 19);
                } else {
                    ventilator = "0";
                    start_dttm = "0000-00-00 00:00:00";
                    stop_dttm = "0000-00-00 00:00:00";
                }
                if (status.equals("Ralan")) {
                    if (chkEksekutif.isSelected() == true) {
                        HakKelas = "1";
                    } else {
                        HakKelas = "3";
                    }
                } else {
                    HakKelas = hakKelas;
                }
                
                             
                
//                String nikCoder = Sequel.cariIsi(
//                "SELECT no_ik FROM inacbg_coder_nik WHERE nik=?",
//                akses.getkode()
//                );
                
                // headers removed
                // requestEntity removed
                //tambahan
//                System.out.println("DEBUG Kirim Klaim - nikCoder yang digunakan: " + nikCoder);
//                System.out.println("DEBUG URL: " + "http://" + koneksiDB.HOSTHYBRIDWEB() + "/" 
//                + koneksiDB.HYBRIDWEB() 
//                + "/inacbg_idrg_dev/index.php?act=createClaim&nikCoder=" + nikCoder);
           
                
                String _actionName = "createClaim"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\","
                        + "\"no_jkn\": \"" + noKartu + "\","
                        + "\"no_rm\": \"" + norkmMedis + "\","
                        + "\"no_reg\": \"" + norawat + "\","
                        + "\"nama_pasien\": \"" + apiEKlaim.cleanJson(namaPasien) + "\","
                        + "\"tgl_lahir\": \"" + tglLahir + "\","
                        + "\"jk\": \"" + jkPasien + "\","
                        + "\"dokter\": \"" + apiEKlaim.cleanJson(nmDokter.getText()) + "\","
                        + "\"tgl_awal\": \"" + tglMasuk.getText() + " " + jamMasuk.getText() + "\","
                        + "\"tgl_akhir\": \"" + tglPulang.getText() + " " + jamPulang.getText() + "\","
                        + "\"kelas_rawat\": \"" + HakKelas + "\","
                        + "\"adl_sub_acute\": \"" + subAcute.getText() + "\","
                        + "\"adl_chronic\": \"" + chronic.getText() + "\","
                        + "\"icu_indikator\": \"" + rawatIntensif + "\","
                        + "\"icu_los\": \"" + icuLos.getText() + "\","
                        + "\"use_ind\": \"" + ventilator + "\","
                        + "\"start_dttm\": \"" + start_dttm + "\","
                        + "\"stop_dttm\": \"" + stop_dttm + "\","
                        + "\"kamar\": \"" + tfKamar.getText() + "\","
                        + "\"konsultasi\": \"" + tfKonsultasi.getText() + "\","
                        + "\"lab\": \"" + tfLaboratorium.getText() + "\","
                        + "\"obat\": \"" + tfObat.getText() + "\","
                        + "\"non_bedah\": \"" + tfNonBedah.getText() + "\","
                        + "\"bedah\": \"" + tfBedah.getText() + "\","
                        + "\"keperawatan\": \"" + tfKeperawatan.getText() + "\","
                        + "\"radiologi\": \"" + tfRadiologi.getText() + "\","
                        + "\"tenaga_ahli\": \"" + tfTenagaAhli.getText() + "\","
                        + "\"penunjang\": \"" + tfPenunjang.getText() + "\","
                        + "\"pelayanan_darah\": \"" + tfDarah.getText() + "\","
                        + "\"rehabilitasi\": \"" + tfRehabilitasi.getText() + "\","
                        + "\"rawat_intensif\": \"" + tfRawatIntensif.getText() + "\","
                        + "\"obat_kronis\": \"" + tfObatKronis.getText() + "\","
                        + "\"alkes\": \"" + tfAlkes.getText() + "\","
                        + "\"bmhp\": \"" + tfBMHP.getText() + "\","
                        + "\"sewa_alat\": \"" + tfSewaAlat.getText() + "\","
                        + "\"tarif_poli_eks\": \"" + tarifPoliEksekutif.getText() + "\","
                        + "\"cara_masuk\": \"" + caraMasuk + "\","
                        + "\"discharge_status\": \"" + dischargeStatus + "\","
                        + "\"sistole\": \"" + sistol.getText() + "\","
                        + "\"diastole\": \"" + diastol.getText() + "\","
                        + "\"birth_weight\": \"" + beratLahir.getText() + "\","
                        + "\"jenis\": \"" + jnsKlaim + "\"";
                
                requestJson += getJsonKehamilanKelahiran(noSep);
                requestJson += "\n}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("Response : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    Sequel.menyimpantf2("tt_status_eklaim", "?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 11,
                            new String[]{norawat, noSep, "true", "false", "false","false", "false", "false", "false", "false", "false"});
                    JOptionPane.showMessageDialog(rootPane, "Set Data Klaim Berhasil");
                    cekStatusKlaim();
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }

            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void updateDataClaim() {
        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah sudah benar data ini kirim ke E-Klaim ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            try {
                String dischargeStatus, caraMasuk, rawatIntensif, ventilator, start_dttm, stop_dttm, HakKelas;
                switch (cmrCaraPulang.getSelectedItem().toString()) {
                    case "Atas persetujuan dokter":
                        dischargeStatus = "1";
                        break;
                    case "Dirujuk":
                        dischargeStatus = "2";
                        break;
                    case "Atas permintaan sendiri":
                        dischargeStatus = "3";
                        break;
                    case "Meninggal":
                        dischargeStatus = "4";
                        break;
                    case "Lain-lain":
                        dischargeStatus = "5";
                        break;
                    default:
                        dischargeStatus = "1";
                        break;
                }
                switch (cmbCaraMasuk.getSelectedItem().toString()) {
                    case "Rujukan FKTP":
                        caraMasuk = "gp";
                        break;
                    case "Rujukan FKRTL":
                        caraMasuk = "osp-trans";
                        break;
                    case "Rujukan Spesialis":
                        caraMasuk = "mp";
                        break;
                    case "Dari Rawat Jalan":
                        caraMasuk = "outp";
                        break;
                    case "Dari Rawat Inap":
                        caraMasuk = "inp";
                        break;
                    case "Dari Rawat Darurat":
                        caraMasuk = "emd";
                        break;
                    case "Lahir di RS":
                        caraMasuk = "born";
                        break;
                    case "Rujukan Panti Jompo":
                        caraMasuk = "nursing";
                        break;
                    case "Rujukan dari RS Jiwa":
                        caraMasuk = "psych";
                        break;
                    case "Rujukan Fasilitas Rehab":
                        caraMasuk = "rehab";
                        break;
                    case "Lain-lain":
                        caraMasuk = "other";
                        break;
                    default:
                        caraMasuk = "gp";
                        break;
                }
                if (chkIntensif.isSelected() == true) {
                    rawatIntensif = "1";
                } else {
                    rawatIntensif = "0";
                }
                if (chkVentilator.isSelected() == true) {
                    ventilator = "1";
                    start_dttm = Valid.SetTgl(tglIntubasi.getSelectedItem() + "") + " " + tglIntubasi.getSelectedItem().toString().substring(11, 19);
                    stop_dttm = Valid.SetTgl(tglEkstubasi.getSelectedItem() + "") + " " + tglEkstubasi.getSelectedItem().toString().substring(11, 19);
                } else {
                    ventilator = "0";
                    start_dttm = "0000-00-00 00:00:00";
                    stop_dttm = "0000-00-00 00:00:00";
                }
                if (status.equals("Ralan")) {
                    if (chkEksekutif.isSelected() == true) {
                        HakKelas = "1";
                    } else {
                        HakKelas = "3";
                    }
                } else {
                    HakKelas = hakKelas;
                }

                // headers removed
                // requestEntity removed
                String _actionName = "updateClaim"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\","
                        + "\"no_jkn\": \"" + noKartu + "\","
                        + "\"no_rm\": \"" + norkmMedis + "\","
                        + "\"no_reg\": \"" + norawat + "\","
                        + "\"nama_pasien\": \"" + apiEKlaim.cleanJson(namaPasien) + "\","
                        + "\"tgl_lahir\": \"" + tglLahir + "\","
                        + "\"jk\": \"" + jkPasien + "\","
                        + "\"dokter\": \"" + apiEKlaim.cleanJson(nmDokter.getText()) + "\","
                        + "\"tgl_awal\": \"" + tglMasuk.getText() + " " + jamMasuk.getText() + "\","
                        + "\"tgl_akhir\": \"" + tglPulang.getText() + " " + jamPulang.getText() + "\","
                        + "\"kelas_rawat\": \"" + HakKelas + "\","
                        + "\"adl_sub_acute\": \"" + subAcute.getText() + "\","
                        + "\"adl_chronic\": \"" + chronic.getText() + "\","
                        + "\"icu_indikator\": \"" + rawatIntensif + "\","
                        + "\"icu_los\": \"" + icuLos.getText() + "\","
                        + "\"use_ind\": \"" + ventilator + "\","
                        + "\"start_dttm\": \"" + start_dttm + "\","
                        + "\"stop_dttm\": \"" + stop_dttm + "\","
                        + "\"kamar\": \"" + tfKamar.getText() + "\","
                        + "\"konsultasi\": \"" + tfKonsultasi.getText() + "\","
                        + "\"lab\": \"" + tfLaboratorium.getText() + "\","
                        + "\"obat\": \"" + tfObat.getText() + "\","
                        + "\"non_bedah\": \"" + tfNonBedah.getText() + "\","
                        + "\"bedah\": \"" + tfBedah.getText() + "\","
                        + "\"keperawatan\": \"" + tfKeperawatan.getText() + "\","
                        + "\"radiologi\": \"" + tfRadiologi.getText() + "\","
                        + "\"tenaga_ahli\": \"" + tfTenagaAhli.getText() + "\","
                        + "\"penunjang\": \"" + tfPenunjang.getText() + "\","
                        + "\"pelayanan_darah\": \"" + tfDarah.getText() + "\","
                        + "\"rehabilitasi\": \"" + tfRehabilitasi.getText() + "\","
                        + "\"rawat_intensif\": \"" + tfRawatIntensif.getText() + "\","
                        + "\"obat_kronis\": \"" + tfObatKronis.getText() + "\","
                        + "\"alkes\": \"" + tfAlkes.getText() + "\","
                        + "\"bmhp\": \"" + tfBMHP.getText() + "\","
                        + "\"sewa_alat\": \"" + tfSewaAlat.getText() + "\","
                        + "\"tarif_poli_eks\": \"" + tarifPoliEksekutif.getText() + "\","
                        + "\"cara_masuk\": \"" + caraMasuk + "\","
                        + "\"discharge_status\": \"" + dischargeStatus + "\","
                        + "\"sistole\": \"" + sistol.getText() + "\","
                        + "\"diastole\": \"" + diastol.getText() + "\","
                        + "\"birth_weight\": \"" + beratLahir.getText() + "\","
                        + "\"jenis\": \"" + jnsKlaim + "\"";
                
                requestJson += getJsonKehamilanKelahiran(noSep);
                requestJson += "\n}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("Response : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    JOptionPane.showMessageDialog(rootPane, "Update Data Klaim Berhasil");
                    cekStatusKlaim();
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
            this.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void validasiPasienTB(String idTB) {
        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah sudah bener data ini kirim ke E-Klaim ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            try {
                // headers removed
                // requestEntity removed
                String _actionName = "validateSITB"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\","
                        + "\"no_reg_sitb\": \"" + idTB + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("Response : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    if (root.path("response").path("status").asText().equals("INVALID")) {
                        JOptionPane.showMessageDialog(rootPane, root.path("response").path("detail").asText());
                    } else {
                        JOptionPane.showMessageDialog(rootPane, "Validasi Data SITB Berhasil");
                    }
                    cekStatusKlaim();
                } else {

                }
//                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
//                    } else {
//                        JOptionPane.showMessageDialog(rootPane, "Anda tidak memiliki akses sebagai koder");
//                    }
//
//                } else {
//                    JOptionPane.showMessageDialog(null, "Silahkan selesaikan koding berkas ini terlebih dahulu sebelum dikirim ke E-Klaim !");
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Silahkan verifikasi berkas ini terlebih dahulu sebelum koding diselesaikan !");
//            }
//        }
//        if (chkAutoClose.isSelected() == true) {
//            CatatanVerifikasi.dispose();
//            dispose();
//        } else {
//            autoReload();
//            CatatanVerifikasi.dispose();
        }
    }

    private void tampilDiagnosa() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Valid.tabelKosong(tabModeDiagnosa);
        try {
            // headers removed
            // requestEntity removed
            String _actionName = "searchDiagnosa"; // URL mapping
            // requestEntity removed
            // Extract keyword based on actionName
                if(_actionName.equals("searchDiagnosa") || _actionName.equals("searchDiagnosisInagrouper")) {
                    requestJson = "{\"keyword\": \"" + apiEKlaim.cleanJson(FormCariDiagnosa.getText()) + "\"}";
                } else if (_actionName.equals("searchProcedure") || _actionName.equals("searchProcedureInagrouper")) {
                    requestJson = "{\"keyword\": \"" + apiEKlaim.cleanJson(TCariProsedur.getText()) + "\"}";
                } else {
                    requestJson = "{}";
                }
                root = apiEKlaim.postAction(_actionName, requestJson);
//                System.out.println("JSON : " + root);
            response = root.path("response");
            if (response.path("data").isArray()) {
                i = 1;
                for (JsonNode list : response.path("data")) {
                    String kode = list.has("kode") ? list.path("kode").asText() : (list.has("code") ? list.path("code").asText() : list.path(1).asText());
                    String deskripsi = list.has("nama") ? list.path("nama").asText() : (list.has("description") ? list.path("description").asText() : list.path(0).asText());
                    tabModeDiagnosa.addRow(new Object[]{
                        false, kode, deskripsi, list.path("validcode").asText(), list.path("accpdx").asText(), list.path("code_asterisk").asText(), list.path("asterisk").asText(), list.path("im").asText()
                    });
                    i++;
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
            if (ex.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void tampilDiagnosaInaGrouper() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Valid.tabelKosong(tabModeDiagnosa);
        try {
            // headers removed
            // requestEntity removed
            String _actionName = "searchDiagnosisInagrouper"; // URL mapping
            // requestEntity removed
            // Extract keyword based on actionName
                if(_actionName.equals("searchDiagnosa") || _actionName.equals("searchDiagnosisInagrouper")) {
                    requestJson = "{\"keyword\": \"" + apiEKlaim.cleanJson(FormCariDiagnosa.getText()) + "\"}";
                } else if (_actionName.equals("searchProcedure") || _actionName.equals("searchProcedureInagrouper")) {
                    requestJson = "{\"keyword\": \"" + apiEKlaim.cleanJson(TCariProsedur.getText()) + "\"}";
                } else {
                    requestJson = "{}";
                }
                root = apiEKlaim.postAction(_actionName, requestJson);
//                System.out.println("JSON : " + root);
            response = root.path("response");
            if (response.path("data").isArray()) {
                i = 1;
                for (JsonNode list : response.path("data")) {
                    String kode = list.has("kode") ? list.path("kode").asText() : (list.has("code") ? list.path("code").asText() : list.path(1).asText());
                    String deskripsi = list.has("nama") ? list.path("nama").asText() : (list.has("description") ? list.path("description").asText() : list.path(0).asText());
                    tabModeDiagnosa.addRow(new Object[]{
                        false, kode, deskripsi, list.path("validcode").asText(), list.path("accpdx").asText(), list.path("code_asterisk").asText(), list.path("asterisk").asText(), list.path("im").asText()
                    });
                    i++;
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
            if (ex.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void tampilDiagnosaPilih() {
        Valid.tabelKosong(tabModeDiagnosaPasien);
        try {
            ps = koneksi.prepareStatement("select reg_periksa.tgl_registrasi,diagnosa_pasien.no_rawat,reg_periksa.no_rkm_medis,concat(pasien.nm_pasien,' [ ',reg_periksa.umurdaftar,' ',reg_periksa.sttsumur,' ]') ,"
                    + "diagnosa_pasien.kd_penyakit,penyakit.nm_penyakit, diagnosa_pasien.status,diagnosa_pasien.status_penyakit,diagnosa_pasien.prioritas "
                    + "from diagnosa_pasien inner join reg_periksa inner join pasien inner join penyakit "
                    + "on diagnosa_pasien.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "and diagnosa_pasien.kd_penyakit=penyakit.kd_penyakit "
                    + "where reg_periksa.no_rawat=?  order by diagnosa_pasien.prioritas ASC");
            try {
                ps.setString(1, norawat.trim());
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModeDiagnosaPasien.addRow(new Object[]{rs.getString("kd_penyakit"),
                        rs.getString("nm_penyakit"),
                        (rs.getString("prioritas").equals("1") ? "Primary" : "Secondary")});
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
    }

    private void tampilProsedur() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Valid.tabelKosong(tabModeDiagnosa);
        try {
            // headers removed
            // requestEntity removed
            String _actionName = "searchProcedure"; // URL mapping
            // requestEntity removed

            System.out.println("JSON : " + requestJson);
            // requestEntity removed
            // Extract keyword based on actionName
                if(_actionName.equals("searchDiagnosa") || _actionName.equals("searchDiagnosisInagrouper")) {
                    requestJson = "{\"keyword\": \"" + apiEKlaim.cleanJson(FormCariDiagnosa.getText()) + "\"}";
                } else if (_actionName.equals("searchProcedure") || _actionName.equals("searchProcedureInagrouper")) {
                    requestJson = "{\"keyword\": \"" + apiEKlaim.cleanJson(TCariProsedur.getText()) + "\"}";
                } else {
                    requestJson = "{}";
                }
                root = apiEKlaim.postAction(_actionName, requestJson);
//                System.out.println("JSON : " + root);
            response = root.path("response");
            if (response.path("data").isArray()) {
                i = 1;
                for (JsonNode list : response.path("data")) {
                    String kode = list.has("kode") ? list.path("kode").asText() : (list.has("code") ? list.path("code").asText() : list.path(1).asText());
                    String deskripsi = list.has("nama") ? list.path("nama").asText() : (list.has("description") ? list.path("description").asText() : list.path(0).asText());
                    tabModeProsedure.addRow(new Object[]{
                        false, kode, deskripsi, "", ""
                    });
                    i++;
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
            if (ex.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void tampilProsedurInaGrouper() {
        this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        Valid.tabelKosong(tabModeProsedure);
        try {
            // headers removed
            // requestEntity removed
            String _actionName = "searchProcedureInagrouper"; // URL mapping
            // requestEntity removed
            // Extract keyword based on actionName
                if(_actionName.equals("searchDiagnosa") || _actionName.equals("searchDiagnosisInagrouper")) {
                    requestJson = "{\"keyword\": \"" + apiEKlaim.cleanJson(FormCariDiagnosa.getText()) + "\"}";
                } else if (_actionName.equals("searchProcedure") || _actionName.equals("searchProcedureInagrouper")) {
                    requestJson = "{\"keyword\": \"" + apiEKlaim.cleanJson(TCariProsedur.getText()) + "\"}";
                } else {
                    requestJson = "{}";
                }
                root = apiEKlaim.postAction(_actionName, requestJson);
            response = root.path("response");
            if (response.path("data").isArray()) {
                i = 1;
                for (JsonNode list : response.path("data")) {
                    String kode = list.has("kode") ? list.path("kode").asText() : (list.has("code") ? list.path("code").asText() : list.path(1).asText());
                    String deskripsi = list.has("nama") ? list.path("nama").asText() : (list.has("description") ? list.path("description").asText() : list.path(0).asText());
                    tabModeProsedure.addRow(new Object[]{
                        false, kode, deskripsi, list.path("validcode").asText(), list.path("im").asText()
                    });
                    i++;
                }
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
            if (ex.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
            }
        }
        this.setCursor(Cursor.getDefaultCursor());
    }

    private void tampilDiagnosaIDRG() {
        Valid.tabelKosong(tabModeDiagnosaIDRG);
        try {
            ps = koneksi.prepareStatement("select reg_periksa.tgl_registrasi,tb_idrg_diagnose.no_rawat,reg_periksa.no_rkm_medis,concat(pasien.nm_pasien,' [ ',reg_periksa.umurdaftar,' ',reg_periksa.sttsumur,' ]') ,"
                    + "tb_idrg_diagnose.code,penyakit.nm_penyakit, tb_idrg_diagnose.status,tb_idrg_diagnose.status_penyakit,tb_idrg_diagnose.prioritas "
                    + "from tb_idrg_diagnose inner join reg_periksa inner join pasien inner join penyakit "
                    + "on tb_idrg_diagnose.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "and tb_idrg_diagnose.code=penyakit.kd_penyakit "
                    + "where reg_periksa.no_rawat=?  order by tb_idrg_diagnose.prioritas ASC");
            try {
                ps.setString(1, norawat.trim());
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModeDiagnosaIDRG.addRow(new Object[]{rs.getString("code"),
                        rs.getString("nm_penyakit"),
                        (rs.getString("prioritas").equals("1") ? "Primary" : "Secondary")});
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
    }

    private void tampilDiagnosaINACBG() {
        Valid.tabelKosong(tabModeDiagnosaINACBG);
        try {
            ps = koneksi.prepareStatement("select reg_periksa.tgl_registrasi,tb_inacbg_diagnose.no_rawat,reg_periksa.no_rkm_medis,concat(pasien.nm_pasien,' [ ',reg_periksa.umurdaftar,' ',reg_periksa.sttsumur,' ]') ,"
                    + "tb_inacbg_diagnose.code,penyakit.nm_penyakit, tb_inacbg_diagnose.status,tb_inacbg_diagnose.status_penyakit,tb_inacbg_diagnose.prioritas,tb_inacbg_diagnose.validcode,tb_inacbg_diagnose.statuscode "
                    + "from tb_inacbg_diagnose inner join reg_periksa inner join pasien inner join penyakit "
                    + "on tb_inacbg_diagnose.no_rawat=reg_periksa.no_rawat and reg_periksa.no_rkm_medis=pasien.no_rkm_medis "
                    + "and tb_inacbg_diagnose.code=penyakit.kd_penyakit "
                    + "where reg_periksa.no_rawat=?  order by tb_inacbg_diagnose.prioritas ASC");
            try {
                ps.setString(1, norawat.trim());
                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModeDiagnosaINACBG.addRow(new Object[]{rs.getString("code"),
                        rs.getString("nm_penyakit"),
                        (rs.getString("prioritas").equals("1") ? "Primary" : "Secondary"), rs.getString("validcode"), rs.getString("statuscode")});
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
    }

    private void tampilProsedureINACBG() {
        Valid.tabelKosong(tabModeProsedureINACBG);
        try {
            ps = koneksi.prepareStatement("select * "
                    + "from   tb_inacbg_prosedure JOIN icd9 ON tb_inacbg_prosedure.kode=icd9.kode "
                    + " where no_rawat ='" + norawat + "' order by tb_inacbg_prosedure.prioritas ASC ");
            try {

                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModeProsedureINACBG.addRow(new Object[]{rs.getString("kode"),
                        rs.getString("deskripsi_panjang"),
                        (rs.getString("prioritas").equals("1") ? "Primary" : "Secondary"), rs.getString("validcode"), rs.getString("statuscode"), rs.getString("vol")});
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
    }

//    private void setDRG() {
//        String listDiagnosa = "", listProsedur = "", volProcedure = "";
//        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin grouping iDRG ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
//        if (reply == JOptionPane.YES_OPTION) {
//            try {
//                for (i = 0; i < tbDiagnosaIDRG.getRowCount(); i++) {
//                    listDiagnosa = listDiagnosa + tbDiagnosaIDRG.getValueAt(i, 0).toString() + "#";
//                }
//                for (i = 0; i < tbProsedurIDRG.getRowCount(); i++) {
//                    if (tbProsedurIDRG.getValueAt(i, 3).toString().equals("1")) {
//                        volProcedure = "";
//                    } else {
//                        volProcedure = "+" + tbProsedurIDRG.getValueAt(i, 3).toString();
//                    }
//                    listProsedur = listProsedur + tbProsedurIDRG.getValueAt(i, 0).toString() + "" + volProcedure + "#";
//                }
//                if (listDiagnosa.length() > 0) {
//                    diagPasien = listDiagnosa.substring(0, listDiagnosa.length() - 1);
//                } else {
//                    diagPasien = "";
//                }
//                if (listProsedur.length() > 0) {
//                    procedurePasien = listProsedur.substring(0, listProsedur.length() - 1);
//                } else {
//                    procedurePasien = "";
//                }
//
//                headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                // requestEntity removed
//                String _actionName = "setDiagnosaIDRG"; // URL mapping
//                // requestEntity removed
//                requestJson = "{"
//                        + "\"no_sep\": \"" + noSep + "\","
//                        + "\"diagnosa\": \"" + apiEKlaim.cleanJson(diagPasien) + "\","
//                        + "\"prosedure\": \"" + apiEKlaim.cleanJson(procedurePasien) + "\""
//                        + "}";
//                System.out.println("JSON : " + requestJson);
//                // requestEntity removed
// if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
//                root = apiEKlaim.postAction(_actionName, requestJson);
//                System.out.println("JSON : " + root);
//                response = root.path("response_idrg");
//                if (root.path("metadata").path("code").asText().equals("200")) {
//                    String jnsRawat, statusIDRG;
//                    if (status.equals("Ralan")) {
//                        jnsRawat = "Rawat Jalan";
//                    } else if (status.equals("Ranap")) {
//                        jnsRawat = "Rawat Inap";
//                    } else {
//                        jnsRawat = "Rawat Jalan";
//                    }
//
////                    if (response.path("mdc_description").asText().equals("Ungroupable or Unrelated")) {
////                        if (Sequel.cariInteger("select count(no_rawat) from tb_idrg_grouping_result where no_rawat='" + norawat + "' and no_sep='" + noSep + "'") > 0) {
////
////                            Sequel.mengedit("tb_idrg_grouping_result", "no_rawat=? and no_sep=?", "mdc_number=?,mdc_description=?,drg_code=?,drg_description=?,script_version=?,logic_version=?,datetime=?", 9, new String[]{
////                                response.path("mdc_number").asText(), response.path("mdc_description").asText(), response.path("drg_code").asText(), response.path("drg_description").asText(), response.path("script_version").asText(), response.path("logic_version").asText(), dateSave.format(new Date()), norawat, noSep
////                            });
////                        } else {
////                            Sequel.menyimpantf2("tb_idrg_grouping_result", "?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 10,
////                                    new String[]{norawat, noSep, response.path("mdc_number").asText(), response.path("mdc_description").asText(), response.path("drg_code").asText(), response.path("drg_description").asText(), jnsRawat, "Normal", response.path("script_version").asText(), response.path("logic_version").asText(), dateSave.format(new Date())});
////                        }
////                        JOptionPane.showMessageDialog(rootPane, response.path("mdc_description").asText());
////                    } else {
//if(response.path("mdc_number").asText().equals("36")){
//    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "grouping_idrg=?,grouping_idrg_status=?", 4, new String[]{
//                        "true","false", norawat, noSep
//                    });
//}else{
//    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "grouping_idrg=?,grouping_idrg_status=?", 4, new String[]{
//                        "true","true", norawat, noSep
//                    });
//}
//                    
//                    if (Sequel.cariInteger("select count(no_rawat) from tb_idrg_grouping_result where no_rawat='" + norawat + "' and no_sep='" + noSep + "'") > 0) {
//
//                        Sequel.mengedit("tb_idrg_grouping_result", "no_rawat=? and no_sep=?", "mdc_number=?,mdc_description=?,drg_code=?,drg_description=?,script_version=?,logic_version=?,datetime=?", 9, new String[]{
//                            response.path("mdc_number").asText(), response.path("mdc_description").asText(), response.path("drg_code").asText(), response.path("drg_description").asText(), response.path("script_version").asText(), response.path("logic_version").asText(), dateSave.format(new Date()), norawat, noSep
//                        });
//                    } else {
//                        Sequel.menyimpantf2("tb_idrg_grouping_result", "?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 11,
//                                new String[]{norawat, noSep, response.path("mdc_number").asText(), response.path("mdc_description").asText(), response.path("drg_code").asText(), response.path("drg_description").asText(), jnsRawat, "Normal", response.path("script_version").asText(), response.path("logic_version").asText(), dateSave.format(new Date())});
//                    }
//
//                    JOptionPane.showMessageDialog(rootPane, "Berhasil Grouping iDRG");
//                    cekStatusKlaim();
////                    }
//                }
////                            if (root.path("metadata").path("code").asText().equals("200")) {
////                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
////                                        new String[]{noSep, noik});
////                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
////                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
////                                });
////                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
////                            }
//            } catch (Exception ex) {
//                System.out.println("Notifikasi : " + ex);
//                if (ex.toString().contains("UnknownHostException")) {
//                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
//                }
//            }
////                    } else {
////                        JOptionPane.showMessageDialog(rootPane, "Anda tidak memiliki akses sebagai koder");
////                    }
////
////                } else {
////                    JOptionPane.showMessageDialog(null, "Silahkan selesaikan koding berkas ini terlebih dahulu sebelum dikirim ke E-Klaim !");
////                }
////            } else {
////                JOptionPane.showMessageDialog(null, "Silahkan verifikasi berkas ini terlebih dahulu sebelum koding diselesaikan !");
////            }
////        }
////        if (chkAutoClose.isSelected() == true) {
////            CatatanVerifikasi.dispose();
////            dispose();
////        } else {
////            autoReload();
////            CatatanVerifikasi.dispose();
//        }
//    }
    
    private void setDRG() {
    String listDiagnosa = "", listProsedur = "";
//    int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin grouping iDRG ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
//    if (reply != JOptionPane.YES_OPTION) {
//        return;
//    }

    try {
        // 1 Ambil daftar diagnosa
        for (int i = 0; i < tbDiagnosaIDRG.getRowCount(); i++) {
            listDiagnosa += tbDiagnosaIDRG.getValueAt(i, 0).toString() + "#";
        }
        String diagPasien = (listDiagnosa.length() > 0) ? listDiagnosa.substring(0, listDiagnosa.length() - 1) : "";

        // 2 Ambil daftar prosedur dengan volume (multiply)
        for (int i = 0; i < tbProsedurIDRG.getRowCount(); i++) {
            String kode = tbProsedurIDRG.getValueAt(i, 0).toString();
            String vol = "1";
            try {
                vol = tbProsedurIDRG.getValueAt(i, 3).toString();
            } catch (Exception e) {
                vol = "1";
            }

            if (vol.equals("1")) {
                listProsedur += kode + "#";  // volume 1 tidak perlu ditulis
            } else {
                listProsedur += kode + "+" + vol + "#"; // volume >1 ditulis sebagai kode+volume
            }
        }
        String procedurePasien = (listProsedur.length() > 0) ? listProsedur.substring(0, listProsedur.length() - 1) : "";

        // 3 Buat JSON request
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("no_sep", noSep);
        jsonRequest.put("diagnosa", diagPasien);
        jsonRequest.put("prosedure", procedurePasien); // tetap string

        System.out.println("JSON Request: " + jsonRequest.toString());

        // 4 HTTP request
        // headers removed
        // requestEntity removed
        String _actionName = "setDiagnosaIDRG"; // URL mapping

        root = apiEKlaim.postAction(_actionName, jsonRequest.toString());
        System.out.println("JSON Response: " + root);

        // 5 Parsing response
        if (root.path("metadata").path("code").asText().equals("200")) {
            JsonNode response = root.path("response_idrg");
            String jnsRawat = status.equals("Ralan") ? "Rawat Jalan" : status.equals("Ranap") ? "Rawat Inap" : "Rawat Jalan";

            // Update status eklaim
            if (response.path("mdc_number").asText().equals("36")) {
                Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "grouping_idrg=?,grouping_idrg_status=?", 4,
                        new String[]{"true", "false", norawat, noSep});
            } else {
                Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "grouping_idrg=?,grouping_idrg_status=?", 4,
                        new String[]{"true", "true", norawat, noSep});
            }

            // Simpan / update hasil grouping
            if (Sequel.cariInteger("select count(no_rawat) from tb_idrg_grouping_result where no_rawat='" + norawat + "' and no_sep='" + noSep + "'") > 0) {
                Sequel.mengedit("tb_idrg_grouping_result", "no_rawat=? and no_sep=?", "mdc_number=?,mdc_description=?,drg_code=?,drg_description=?,script_version=?,logic_version=?,datetime=?", 9,
                        new String[]{
                                response.path("mdc_number").asText(),
                                response.path("mdc_description").asText(),
                                response.path("drg_code").asText(),
                                response.path("drg_description").asText(),
                                response.path("script_version").asText(),
                                response.path("logic_version").asText(),
                                dateSave.format(new Date()), norawat, noSep
                        });
            } else {
                Sequel.menyimpantf2("tb_idrg_grouping_result", "?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 11,
                        new String[]{norawat, noSep, response.path("mdc_number").asText(), response.path("mdc_description").asText(),
                                response.path("drg_code").asText(), response.path("drg_description").asText(), jnsRawat, "Normal",
                                response.path("script_version").asText(), response.path("logic_version").asText(), dateSave.format(new Date())});
            }

//            JOptionPane.showMessageDialog(rootPane, "Berhasil Grouping iDRG");
            cekStatusKlaim();
            finalDRG();//otomatis final idrg
        }

    } catch (Exception ex) {
        System.out.println("Notifikasi : " + ex);
        if (ex.toString().contains("UnknownHostException")) {
            JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
        }
    }
}


    private void finalDRG() {
//        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin finalisasi iDRG ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
//        if (reply == JOptionPane.YES_OPTION) {
            try {
                // headers removed
                // requestEntity removed
                String _actionName = "finalIDRG"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("JSON : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "final_idrg=?", 3, new String[]{
                        "true", norawat, noSep
                    });
                    Sequel.mengedit("tb_idrg_grouping_result", "no_rawat=? and no_sep=?", "status=?", 3, new String[]{
                        "Final", norawat, noSep
                    });
//                    JOptionPane.showMessageDialog(rootPane, "Berhasil Final iDRG");
                    cekStatusKlaim();
                    importCoding();//otomatis import koding
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
//                    } else {
//                        JOptionPane.showMessageDialog(rootPane, "Anda tidak memiliki akses sebagai koder");
//                    }
//
//                } else {
//                    JOptionPane.showMessageDialog(null, "Silahkan selesaikan koding berkas ini terlebih dahulu sebelum dikirim ke E-Klaim !");
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Silahkan verifikasi berkas ini terlebih dahulu sebelum koding diselesaikan !");
//            }
//        }
//        if (chkAutoClose.isSelected() == true) {
//            CatatanVerifikasi.dispose();
//            dispose();
//        } else {
//            autoReload();
//            CatatanVerifikasi.dispose();
//        }
    }

    private void editDRG() {
        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin edit ulang grouping iDRG ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            try {
                // headers removed
                // requestEntity removed
                String _actionName = "editIDRG"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("JSON : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "grouping_idrg=?,final_idrg=?,grouping_inacbg=?,final_inacbg=?", 6, new String[]{
                        "false", "false", "false", "false", norawat, noSep
                    });
                    Sequel.mengedit("tb_idrg_grouping_result", "no_rawat=? and no_sep=?", "status=?", 3, new String[]{
                        "Normal", norawat, noSep
                    });
                    JOptionPane.showMessageDialog(rootPane, "Berhasil Membatalkan Final iDRG");
                    cekStatusKlaim();
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
        }
    }

//    private void importCoding() {
//        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin import diagnosa iDRG ke INACBG", "Konfirmasi", JOptionPane.YES_NO_OPTION);
//        if (reply == JOptionPane.YES_OPTION) {
//            try {
//                headers = new HttpHeaders();
//                headers.setContentType(MediaType.APPLICATION_JSON);
//                // requestEntity removed
//                String _actionName = "importCoding"; // URL mapping
//                // requestEntity removed
//                requestJson = "{"
//                        + "\"no_sep\": \"" + noSep + "\""
//                        + "}";
//                System.out.println("JSON : " + requestJson);
//                // requestEntity removed
// if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
//                root = apiEKlaim.postAction(_actionName, requestJson);
//                System.out.println("JSON : " + root);
//                responseDiagnosa = root.path("data").path("diagnosa");
//                responseProcedure = root.path("data").path("procedure");
////                System.out.println("response : " + response);
//                if (root.path("metadata").path("code").asText().equals("200")) {
//                    if (responseDiagnosa.path("expanded").isArray()) {
//                        i = 1;
//                        for (JsonNode list : responseDiagnosa.path("expanded")) {
//                            if (Sequel.cariInteger(
//                                    "select count(tb_inacbg_diagnose.code) from tb_inacbg_diagnose "
//                                    + "inner join reg_periksa inner join pasien on "
//                                    + "tb_inacbg_diagnose.no_rawat=reg_periksa.no_rawat and "
//                                    + "reg_periksa.no_rkm_medis=pasien.no_rkm_medis where "
//                                    + "tb_inacbg_diagnose.no_rawat='" + norawat + "' and tb_inacbg_diagnose.code='" + list.path("code").asText() + "'") > 0) {
//                                Sequel.menyimpan("tb_inacbg_diagnose", "?,?,?,?,?,?,?", "Penyakit", 7, new String[]{
//                                    norawat, list.path("code").asText(), status,
//                                    Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_inacbg_diagnose where no_rawat=? and status='" + status + "'", norawat), "Lama", list.path("validcode").asText(), list.path("metadata").path("message").asText()
//                                });
//
//                            } else {
//                                Sequel.menyimpan("tb_inacbg_diagnose", "?,?,?,?,?,?,?", "Penyakit", 7, new String[]{
//                                    norawat, list.path("code").asText(), status,
//                                    Sequel.cariIsi("select ifnull(MAX(prioritas)+1,1) from tb_inacbg_diagnose where no_rawat=? and status='" + status + "'", norawat), "Baru", list.path("validcode").asText(), list.path("metadata").path("message").asText()
//                                });
//                            }
//                            i++;
//                        }
//                        JOptionPane.showMessageDialog(rootPane, "Berhasil Import Data Diagnosa");
//                        tampilDiagnosaINACBG();
//                    }
//                    if (responseProcedure.path("expanded").isArray()) {
//                        i = 1;
//                        for (JsonNode listProcedure : responseProcedure.path("expanded")) {
//                            if (Sequel.cariInteger("Select count(kode) from tb_inacbg_prosedure where no_rawat='" + norawat + "' and status='Ralan' and  kode='" + listProcedure.path("code").asText() + "' ") > 0) {
////                     Sequel.mengedit("tb_idrg_diagnose", "code=?", "statusdata=?", 2, new String[]{
////                        "1", tbDiagnosaPasien.getValueAt(i, 0).toString()
////                    });
//                            } else {
//                                Sequel.menyimpan("tb_inacbg_prosedure", "?,?,?,?,?,?,?", "ICD 9", 7, new String[]{
//                                    norawat, listProcedure.path("code").asText(), status, listProcedure.path("no").asText(), "1", listProcedure.path("validcode").asText(), listProcedure.path("metadata").path("message").asText()
//                                });
//                            }
//                            i++;
//                        }
//                        JOptionPane.showMessageDialog(rootPane, "Berhasil Import Data Diagnosa");
//                        tampilProsedureINACBG();
//                    }
//                }
//            } catch (Exception ex) {
//                System.out.println("Notifikasi : " + ex);
//                if (ex.toString().contains("UnknownHostException")) {
//                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
//                }
//            }
//        }
//    }
    
    private void importCoding() {

    try {
        // 1 Siapkan header dan JSON request
        // headers removed
        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("no_sep", noSep);
        // requestEntity removed

        // 2 Tentukan URL endpoint
        String _actionName = "importCoding"; // URL mapping

        System.out.println("JSON Request: " + jsonRequest.toString());

        // 3 Kirim POST request ke server
        root = apiEKlaim.postAction(_actionName, jsonRequest.toString());
        System.out.println("JSON Response: " + root);

        // 4 Ambil response
        responseDiagnosa = root.path("data").path("diagnosa");
        responseProcedure = root.path("data").path("procedure");

        if (root.path("metadata").path("code").asText().equals("200")) {

            // 5 Import Diagnosa
            if (responseDiagnosa.path("expanded").isArray()) {
                Sequel.queryu("delete from tb_inacbg_diagnose where no_rawat=?", norawat);
                int i = 1;
                for (JsonNode list : responseDiagnosa.path("expanded")) {
                    Sequel.menyimpan("tb_inacbg_diagnose", "?,?,?,?,?,?,?", "Penyakit", 7, new String[]{
                            norawat,
                            list.path("code").asText(),
                            status,
                            String.valueOf(i),
                            "Baru",
                            list.path("validcode").asText(),
                            list.path("metadata").path("message").asText()
                    });
                    i++;
                }
//                JOptionPane.showMessageDialog(rootPane, "Berhasil Import Data Diagnosa");
                tampilDiagnosaINACBG();
            }

            // 6 Import Prosedur (dengan multiply)
            if (responseProcedure.path("expanded").isArray()) {
                Sequel.queryu("delete from tb_inacbg_prosedure where no_rawat=?", norawat);
                int i = 1;
                for (JsonNode listProcedure : responseProcedure.path("expanded")) {
                    // Ambil multiply dari JSON, default 1
                    int multiplyValue = listProcedure.path("multiply").asInt(1); // <-- ambil int, default 1
                    System.out.println("Procedure " + listProcedure.path("code").asText() + " multiply = " + multiplyValue);

                    Sequel.menyimpan("tb_inacbg_prosedure", "?,?,?,?,?,?,?", "ICD 9", 7, new String[]{
                            norawat,
                            listProcedure.path("code").asText(),
                            status,
                            listProcedure.path("no").asText(),
                            String.valueOf(multiplyValue),  // <-- multiply dari JSON response
                            listProcedure.path("validcode").asText(),
                            listProcedure.path("metadata").path("message").asText()
                    });
                    i++;
                }
//                JOptionPane.showMessageDialog(rootPane, "Berhasil Import Data Prosedur");
                tampilProsedureINACBG();
            }

        } else {
            JOptionPane.showMessageDialog(rootPane, "Gagal import coding. Cek server atau data.");
        }

    } catch (Exception ex) {
        System.out.println("Notifikasi : " + ex);
        if (ex.toString().contains("UnknownHostException")) {
            JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
        }
    }
}


    private void setINACBG() {
//        JOptionPane.showMessageDialog(rootPane, "Diagnosa dengan status IM TIDAK BERLAKU akan dihapus saat kirim ke eklaim,\n Silahkan ganti diagnosa tersebut dengan diagnosa yang berlaku pada INACBG jika ingin digunakan.");
        String listDiagnosa = "", listProsedur = "";
//        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin grouping INACBG ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
//        if (reply == JOptionPane.YES_OPTION) {
            try {
                for (i = 0; i < tbDiagnosaIINACBG.getRowCount(); i++) {
//                    if (tbDiagnosaIINACBG.getValueAt(i, 3).toString().equals("1")) {
                        listDiagnosa = listDiagnosa + tbDiagnosaIINACBG.getValueAt(i, 0).toString() + "#";
//                    }
                }
                for (i = 0; i < tbProsedureINACBG.getRowCount(); i++) {
//                    if (tbProsedureINACBG.getValueAt(i, 3).toString().equals("1")) {
                        listProsedur = listProsedur + tbProsedureINACBG.getValueAt(i, 0).toString() + "#";
//                    }
                }
                if (listDiagnosa.length() > 0) {
                    diagPasien = listDiagnosa.substring(0, listDiagnosa.length() - 1);
                } else {
                    diagPasien = "";
                }
                if (listProsedur.length() > 0) {
                    procedurePasien = listProsedur.substring(0, listProsedur.length() - 1);
                } else {
                    procedurePasien = "";
                }

                // headers removed
                // requestEntity removed
                String _actionName = "setDiagnosaINACBG"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\","
                        + "\"diagnosa\": \"" + apiEKlaim.cleanJson(diagPasien) + "\","
                        + "\"prosedure\": \"" + apiEKlaim.cleanJson(procedurePasien) + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                response = root.path("response_inacbg");
                responseSpecialCmg = root.path("special_cmg_option");
                System.out.println("JSON : " + root);
              
                if (root.path("metadata").path("code").asText().equals("200")) {
                    if(response.path("cbg").path("code").asText().equals("X-0-95-X")){
//                         Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "grouping_inacbg=?", 3, new String[]{
//                        "true", norawat, noSep
//                    });
                    }else{
                         Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "grouping_inacbg=?", 3, new String[]{
                        "true", norawat, noSep
                    });
                    }
                    Sequel.queryu("delete from tb_inacbg_special_cmg where no_rawat='" + norawat + "' ");
                    if (responseSpecialCmg.isArray()) {
                        i = 1;
                        for (JsonNode list : responseSpecialCmg) {
                            Sequel.menyimpantf2("tb_inacbg_special_cmg", "?,?,?,?,?,?,?", "No.Rawat", 7,
                                    new String[]{norawat, list.path("code").asText(), list.path("description").asText(), list.path("type").asText(), "0", "", ""});

//                    tabModeDiagnosa.addRow(new Object[]{
//                        false, list.path("kode").asText(), list.path("description").asText(), list.path("validcode").asText(), list.path("accpdx").asText(), list.path("code_asterisk").asText(), list.path("asterisk").asText(), list.path("im").asText()
//                    });
                            i++;
                        }
                    }

                   
                    if (Sequel.cariInteger("select count(no_rawat) from tb_inacbg_grouping_result where no_rawat='" + norawat + "' and no_sep='" + noSep + "'") > 0) {
                        Sequel.mengedit("tb_inacbg_grouping_result", "no_rawat=? and no_sep=?", "cbg_code=?,cbg_description=?,base_tariff=?,tariff=?,kelas=?,inacbg_version=?,datetime=?", 9, new String[]{
                            response.path("cbg").path("code").asText(), response.path("cbg").path("description").asText(), response.path("base_tariff").asText(), response.path("tariff").asText(), response.path("kelas").asText(), response.path("inacbg_version").asText(), dateSave.format(new Date()), norawat, noSep
                        });
                    } else {
                        Sequel.menyimpantf2("tb_inacbg_grouping_result", "?,?,?,?,?,?,?,?,?", "No.Rawat", 9,
                                new String[]{norawat, noSep, response.path("cbg").path("code").asText(), response.path("cbg").path("description").asText(), response.path("base_tariff").asText(), response.path("tariff").asText(), response.path("kelas").asText(), response.path("inacbg_version").asText(), dateSave.format(new Date())});
                    }
//                    for (i = 0; i < tbDiagnosaIINACBG.getRowCount(); i++) {
////                        if (tbDiagnosaIINACBG.getValueAt(i, 3).toString().equals("0")) {
//                            Sequel.meghapus("tb_inacbg_diagnose", "no_rawat", "code", "validcode", norawat, tbDiagnosaIINACBG.getValueAt(i, 0).toString(), tbDiagnosaIINACBG.getValueAt(i, 3).toString());
////                        }
//                    }
//                    for (i = 0; i < tbProsedureINACBG.getRowCount(); i++) {
////                        if (tbProsedureINACBG.getValueAt(i, 3).toString().equals("0")) {
//                            Sequel.meghapus("tb_inacbg_prosedure", "no_rawat", "kode", "validcode", norawat, tbProsedureINACBG.getValueAt(i, 0).toString(), tbProsedureINACBG.getValueAt(i, 3).toString());
////                        }
//                    }
                    
                    tampilDiagnosaINACBG();
                    tampilProsedureINACBG();
                    getDataKlaim();
//                    JOptionPane.showMessageDialog(rootPane, "Berhasil Grouping INACBG");
                    cekStatusKlaim();                 
                                                // === LOGIKA PENGECEKAN BARU DIMULAI DI SINI ===
                            if (Sequel.cariInteger("SELECT count(no_rawat) FROM tb_inacbg_grouping_result WHERE no_rawat='" + norawat + "' AND (cbg_description LIKE '%ERROR%' OR cbg_description LIKE '%GAGAL%')") > 0) {
                                // KONDISI 1: ADA ERROR/GAGAL
                                btnFinalINACBG.setVisible(false);
                                groupHasilINACBG.setForeground(Color.RED); // Atur menjadi MERAH
                                totalCostHasilKlaim.setText("");
                            } else {
                                // KONDISI 2: TIDAK ADA ERROR/GAGAL
                                btnFinalINACBG.setVisible(true); // Pastikan tombol terlihat (atau setEnabled(true) jika sebelumnya disable)
                                groupHasilINACBG.setForeground(Color.BLACK); // Atur kembali ke warna NORMAL (misalnya, HITAM)
                            }
                            // === LOGIKA PENGECEKAN BARU SELESAI ===
                    finalINACBG();//otomatis final inacbg
                }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
//                    } else {
//                        JOptionPane.showMessageDialog(rootPane, "Anda tidak memiliki akses sebagai koder");
//                    }
//
//                } else {
//                    JOptionPane.showMessageDialog(null, "Silahkan selesaikan koding berkas ini terlebih dahulu sebelum dikirim ke E-Klaim !");
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Silahkan verifikasi berkas ini terlebih dahulu sebelum koding diselesaikan !");
//            }
//        }
//        if (chkAutoClose.isSelected() == true) {
//            CatatanVerifikasi.dispose();
//            dispose();
//        } else {
//            autoReload();
//            CatatanVerifikasi.dispose();
//        }
    }

    private void getDataKlaim() {
        try {
            // headers removed
            // requestEntity removed
            String _actionName = "getKlaim"; // URL mapping
            // requestEntity removed
            requestJson = "{"
                    + "\"no_sep\": \"" + noSep + "\""
                    + "}";
            System.out.println("JSON : " + requestJson);
            // requestEntity removed
            if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);

            response = root.path("response").path("data");
            responsetarif = response.path("tarif_rs");
            responsegrouper = response.path("grouper");
            System.out.println("diagnosa : " + response.path("diagnosa").asText());
            if (root.path("metadata").path("code").asText().equals("200")) {
                if (Sequel.cariInteger("select count(no_rawat) from tb_eklaim_grouping_result where no_rawat='" + norawat + "' and no_sep='" + noSep + "'") > 0) {
                    Sequel.mengedit("tb_eklaim_grouping_result", "no_rawat=? and no_sep=?", "kode_rs=?,kelas_rs=?,kelas_rawat=?,kode_tarif=?,jenis_rawat=?,tgl_masuk=?,tgl_pulang=?,cara_masuk=?,tgl_lahir=?,berat_lahir=?,gender=?,discharge_status=?,diagnosa=?,`procedure`=?,diagnosa_inagrouper=?,procedure_inagrouper=?,adl_sub_acute=?,adl_chronic=?,tarif_rs_prosedur_non_bedah=?,tarif_rs_prosedur_bedah=?,tarif_rs_konsultasi=?,tarif_rs_tenaga_ahli=?,tarif_rs_keperawatan=?,tarif_rs_penunjang=?,tarif_rs_radiologi=?,tarif_rs_laboratorium=?,tarif_rs_pelayanan_darah=?,tarif_rs_rehabilitasi=?,tarif_rs_kamar=?,tarif_rs_rawat_intensif=?,tarif_rs_obat=?,tarif_rs_obat_kronis=?,tarif_rs_obat_kemoterapi=?,tarif_rs_alkes=?,tarif_rs_bmhp=?,tarif_rs_sewa_alat=?,sistole=?,diastole=?,los=?,icu_indikator=?,icu_los=?,ventilator_hour=?,upgrade_class_ind=?,upgrade_class_class=?,upgrade_class_los=?,add_payment_pct=?,add_payment_amt=?,upgrade_class_payor=?,nama_pasien=?,nomor_rm=?,umur_tahun=?,umur_hari=?,tarif_poli_eks=?,dializer_single_use=?,nama_dokter=?,nomor_sep=?,nomor_kartu=?,payor_id=?,payor_nm=?,coder_nm=?,coder_nik=?,patient_id=?,admission_id=?,hospital_admission_id=?,grouping_count=?,cbg_code=?,cbg_description=?,base_tariff=?,tariff=?,kelas=?,inacbg_version=?,mdc_number=?,mdc_description=?,drg_code=?,drg_description=?,script_version=?,logic_version=?,kemenkes_dc_status_cd=?,kemenkes_dc_sent_dttm=?,bpjs_dc_status_cd=?,bpjs_dc_sent_dttm=?,klaim_status_cd=?,bpjs_klaim_status_cd=?,bpjs_klaim_status_nm=?,datetime=?", 87, new String[]{
                        response.path("kode_rs").asText(), response.path("kelas_rs").asText(), response.path("kelas_rawat").asText(), response.path("kode_tarif").asText(), response.path("jenis_rawat").asText(), response.path("tgl_masuk").asText(), response.path("tgl_pulang").asText(), response.path("cara_masuk").asText(),
                        response.path("tgl_lahir").asText(), response.path("berat_lahir").asText(), response.path("gender").asText(), response.path("discharge_status").asText(), response.path("diagnosa").asText(), response.path("procedure").asText(), response.path("diagnosa_inagrouper").asText(), response.path("procedure_inagrouper").asText(),
                        response.path("adl_sub_acute").asText(), response.path("adl_chronic").asText(), responsetarif.path("prosedur_non_bedah").asText(), responsetarif.path("prosedur_bedah").asText(), responsetarif.path("konsultasi").asText(), responsetarif.path("tenaga_ahli").asText(), responsetarif.path("keperawatan").asText(), responsetarif.path("penunjang").asText(),
                        responsetarif.path("radiologi").asText(), responsetarif.path("laboratorium").asText(), responsetarif.path("pelayanan_darah").asText(), responsetarif.path("rehabilitasi").asText(), responsetarif.path("kamar").asText(), responsetarif.path("rawat_intensif").asText(), responsetarif.path("obat").asText(), responsetarif.path("obat_kronis").asText(),
                        responsetarif.path("obat_kemoterapi").asText(), responsetarif.path("alkes").asText(), responsetarif.path("bmhp").asText(), responsetarif.path("sewa_alat").asText(), response.path("sistole").asText(), response.path("diastole").asText(), response.path("los").asText(), response.path("icu_indikator").asText(), response.path("icu_los").asText(),
                        response.path("ventilator_hour").asText(), response.path("upgrade_class_ind").asText(), response.path("upgrade_class_class").asText(), response.path("upgrade_class_los").asText(), response.path("add_payment_pct").asText(), response.path("add_payment_amt").asText(), response.path("upgrade_class_payor").asText(), response.path("nama_pasien").asText(),
                        response.path("nomor_rm").asText(), response.path("umur_tahun").asText(), response.path("umur_hari").asText(), response.path("tarif_poli_eks").asText(), response.path("dializer_single_use").asText(), response.path("nama_dokter").asText(), response.path("nomor_sep").asText(), response.path("nomor_kartu").asText(), response.path("payor_id").asText(),
                        response.path("payor_nm").asText(), response.path("coder_nm").asText(), response.path("coder_nik").asText(), response.path("patient_id").asText(), response.path("admission_id").asText(), response.path("hospital_admission_id").asText(), response.path("grouping_count").asText(), responsegrouper.path("response_inacbg").path("cbg").path("code").asText(), responsegrouper.path("response_inacbg").path("cbg").path("description").asText(),
                        responsegrouper.path("response_inacbg").path("base_tariff").asText(), responsegrouper.path("response_inacbg").path("tariff").asText(), responsegrouper.path("response_inacbg").path("kelas").asText(), responsegrouper.path("response_inacbg").path("inacbg_version").asText(), responsegrouper.path("response_idrg").path("mdc_number").asText(), responsegrouper.path("response_idrg").path("mdc_description").asText(), responsegrouper.path("response_idrg").path("drg_code").asText(), responsegrouper.path("response_idrg").path("drg_description").asText(), responsegrouper.path("response_idrg").path("script_version").asText(), responsegrouper.path("response_idrg").path("logic_version").asText(),
                        response.path("kemenkes_dc_status_cd").asText(), response.path("kemenkes_dc_sent_dttm").asText(), response.path("bpjs_dc_status_cd").asText(), response.path("bpjs_dc_sent_dttm").asText(), response.path("klaim_status_cd").asText(), response.path("bpjs_klaim_status_cd").asText(), response.path("bpjs_klaim_status_nm").asText(),
                        dateSave.format(new Date()), norawat, noSep
                    });
                } else {
                    Sequel.menyimpantf2("tb_eklaim_grouping_result", "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?", "No.Rawat", 88,
                            new String[]{norawat, noSep, response.path("kode_rs").asText(), response.path("kelas_rs").asText(), response.path("kelas_rawat").asText(), response.path("kode_tarif").asText(), response.path("jenis_rawat").asText(), response.path("tgl_masuk").asText(), response.path("tgl_pulang").asText(), response.path("cara_masuk").asText(),
                                response.path("tgl_lahir").asText(), response.path("berat_lahir").asText(), response.path("gender").asText(), response.path("discharge_status").asText(), response.path("diagnosa").asText(), response.path("procedure").asText(), response.path("diagnosa_inagrouper").asText(), response.path("procedure_inagrouper").asText(),
                                response.path("adl_sub_acute").asText(), response.path("adl_chronic").asText(), responsetarif.path("prosedur_non_bedah").asText(), responsetarif.path("prosedur_bedah").asText(), responsetarif.path("konsultasi").asText(), responsetarif.path("tenaga_ahli").asText(), responsetarif.path("keperawatan").asText(), responsetarif.path("penunjang").asText(),
                                responsetarif.path("radiologi").asText(), responsetarif.path("laboratorium").asText(), responsetarif.path("pelayanan_darah").asText(), responsetarif.path("rehabilitasi").asText(), responsetarif.path("kamar").asText(), responsetarif.path("rawat_intensif").asText(), responsetarif.path("obat").asText(), responsetarif.path("obat_kronis").asText(),
                                responsetarif.path("obat_kemoterapi").asText(), responsetarif.path("alkes").asText(), responsetarif.path("bmhp").asText(), responsetarif.path("sewa_alat").asText(), response.path("sistole").asText(), response.path("diastole").asText(), response.path("los").asText(), response.path("icu_indikator").asText(), response.path("icu_los").asText(),
                                response.path("ventilator_hour").asText(), response.path("upgrade_class_ind").asText(), response.path("upgrade_class_class").asText(), response.path("upgrade_class_los").asText(), response.path("add_payment_pct").asText(), response.path("add_payment_amt").asText(), response.path("upgrade_class_payor").asText(), response.path("nama_pasien").asText(),
                                response.path("nomor_rm").asText(), response.path("umur_tahun").asText(), response.path("umur_hari").asText(), response.path("tarif_poli_eks").asText(), response.path("dializer_single_use").asText(), response.path("nama_dokter").asText(), response.path("nomor_sep").asText(), response.path("nomor_kartu").asText(), response.path("payor_id").asText(),
                                response.path("payor_nm").asText(), response.path("coder_nm").asText(), response.path("coder_nik").asText(), response.path("patient_id").asText(), response.path("admission_id").asText(), response.path("hospital_admission_id").asText(), response.path("grouping_count").asText(), responsegrouper.path("response_inacbg").path("cbg").path("code").asText(), responsegrouper.path("response_inacbg").path("cbg").path("description").asText(),
                                responsegrouper.path("response_inacbg").path("base_tariff").asText(), responsegrouper.path("response_inacbg").path("tariff").asText(), responsegrouper.path("response_inacbg").path("kelas").asText(), responsegrouper.path("response_inacbg").path("inacbg_version").asText(), responsegrouper.path("response_idrg").path("mdc_number").asText(), responsegrouper.path("response_idrg").path("mdc_description").asText(), responsegrouper.path("response_idrg").path("drg_code").asText(), responsegrouper.path("response_idrg").path("drg_description").asText(), responsegrouper.path("response_idrg").path("script_version").asText(), responsegrouper.path("response_idrg").path("logic_version").asText(),
                                response.path("kemenkes_dc_status_cd").asText(), response.path("kemenkes_dc_sent_dttm").asText(), response.path("bpjs_dc_status_cd").asText(), response.path("bpjs_dc_sent_dttm").asText(), response.path("klaim_status_cd").asText(), response.path("bpjs_klaim_status_cd").asText(), response.path("bpjs_klaim_status_nm").asText(),"",
                                dateSave.format(new Date())});
                }

            }
            
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
            if (ex.toString().contains("UnknownHostException")) {
                JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
            }
        }
//                    } else {
//                        JOptionPane.showMessageDialog(rootPane, "Anda tidak memiliki akses sebagai koder");
//                    }
//
//                } else {
//                    JOptionPane.showMessageDialog(null, "Silahkan selesaikan koding berkas ini terlebih dahulu sebelum dikirim ke E-Klaim !");
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Silahkan verifikasi berkas ini terlebih dahulu sebelum koding diselesaikan !");
//            }
//        }
//        if (chkAutoClose.isSelected() == true) {
//            CatatanVerifikasi.dispose();
//            dispose();
//        } else {
//            autoReload();
//            CatatanVerifikasi.dispose();

    }

    private void finalINACBG() {
//        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin finalisasi grouping INACBG ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
//        if (reply == JOptionPane.YES_OPTION) {
            try {
                // headers removed
                // requestEntity removed
                String _actionName = "finalINACBG"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("JSON : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "final_inacbg=?", 3, new String[]{
                        "true", norawat, noSep
                    });
//                    JOptionPane.showMessageDialog(rootPane, "Berhasil Final INACBG");
                    cekStatusKlaim();
                    finalKlaim();//otomatis final klaim
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
//                    } else {
//                        JOptionPane.showMessageDialog(rootPane, "Anda tidak memiliki akses sebagai koder");
//                    }
//
//                } else {
//                    JOptionPane.showMessageDialog(null, "Silahkan selesaikan koding berkas ini terlebih dahulu sebelum dikirim ke E-Klaim !");
//                }
//            } else {
//                JOptionPane.showMessageDialog(null, "Silahkan verifikasi berkas ini terlebih dahulu sebelum koding diselesaikan !");
//            }
//        }
//        if (chkAutoClose.isSelected() == true) {
//            CatatanVerifikasi.dispose();
//            dispose();
//        } else {
//            autoReload();
//            CatatanVerifikasi.dispose();
//        }
    }

    private void editINACBG() {
        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin edit ulang grouping INACBG ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            try {
                // headers removed
                // requestEntity removed
                String _actionName = "editINACBG"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("JSON : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "final_inacbg=?", 3, new String[]{
                        "false", norawat, noSep
                    });
                    JOptionPane.showMessageDialog(rootPane, "Berhasil Membatalkan Final INACBG");
                    cekStatusKlaim();
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
        }
    }

    private void finalKlaim() {
//        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin klaim di Finalisasi ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
//        if (reply == JOptionPane.YES_OPTION) {
            try {
                // headers removed
                // requestEntity removed
                String _actionName = "finalKlaim"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("JSON : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "final_klaim=?", 3, new String[]{
                        "true", norawat, noSep
                    });
                    Sequel.mengedit("tb_eklaim_grouping_result", "no_rawat=? and no_sep=?", "file_individual=?", 3, new String[]{
                        "berkasdigital/data_individual/individual_"+noSep+".pdf", norawat, noSep
                    });
                    getDataKlaim();
//                    JOptionPane.showMessageDialog(rootPane, "Berhasil Finalisasi Klaim");
                    cekStatusKlaim();
                    KirimOnlineKlaim();//otomatis kirim online
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
//        }
    }

    private void editKlaim() {
        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin klaim di Edit Ulang ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (reply == JOptionPane.YES_OPTION) {
            try {
                // headers removed
                // requestEntity removed
                String _actionName = "editKlaim"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("JSON : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "final_klaim=?,kirim_online=?", 4, new String[]{
                        "false", "false", norawat, noSep
                    });
                    getDataKlaim();
                    JOptionPane.showMessageDialog(rootPane, "Berhasil Membatalkan Klaim");
                    cekStatusKlaim();
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
        }
    }

    private void cetakKlaim() {
        
            try {
                String urlPHP = "http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/" + koneksiDB.HYBRIDWEB() + "/inacbg_idrg_dev/index.php?act=cetakKlaim&nikCoder=" + kdpetugas;
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                
                java.net.URL url = new java.net.URL(urlPHP);
                java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setDoOutput(true);
                
                try(java.io.OutputStream os = conn.getOutputStream()) {
                    byte[] input = requestJson.getBytes("utf-8");
                    os.write(input, 0, input.length);			
                }
                
                java.io.BufferedReader br = new java.io.BufferedReader(new java.io.InputStreamReader(conn.getInputStream(), "utf-8"));
                StringBuilder responseString = new StringBuilder();
                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    responseString.append(responseLine.trim());
                }
                
                System.out.println("Response PHP : " + responseString.toString());
                root = mapper.readTree(responseString.toString());
                
                if (root.path("metadata").path("code").asText().equals("200")) {
//                    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "kirim_online=?", 3, new String[]{
//                        "true", norawat, noSep
//                    });
                    getDataKlaim();
                    JOptionPane.showMessageDialog(rootPane, "Berhasil Create Individual");
                    cekStatusKlaim();
                    System.out.println("Notifikasi : " + fileIndividual);
                     openpdf(fileIndividual);
                      System.out.println("Notifikasi : " + fileIndividual);
                     this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        WindowCetak.setSize(800, internalFrame1.getHeight());
        WindowCetak.setLocationRelativeTo(internalFrame1);
        WindowCetak.setVisible(true);
        this.setCursor(Cursor.getDefaultCursor());
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
    }
     private void KirimOnlineKlaim() {
//        int reply = JOptionPane.showConfirmDialog(rootPane, "Apakah anda yakin klaim di kirim ke DC Kemkes ?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
//        if (reply == JOptionPane.YES_OPTION) {
            try {
                // headers removed
                // requestEntity removed
                String _actionName = "kirimOnlineKlaim"; // URL mapping
                // requestEntity removed
                requestJson = "{"
                        + "\"no_sep\": \"" + noSep + "\""
                        + "}";
                System.out.println("JSON : " + requestJson);
                // requestEntity removed
                if(requestJson == null || requestJson.isEmpty()) requestJson = "{}";
                root = apiEKlaim.postAction(_actionName, requestJson);
                System.out.println("JSON : " + root);
                if (root.path("metadata").path("code").asText().equals("200")) {
                    
                    Sequel.mengedit("tt_status_eklaim", "no_rawat=? and no_sep=?", "kirim_online=?", 3, new String[]{
                        "true", norawat, noSep
                    });                                        
                    getDataKlaim();                    
                    JOptionPane.showMessageDialog(rootPane, "Berhasil Kirim Online");                    
                    cekStatusKlaim();                    
                }
//                            if (root.path("metadata").path("code").asText().equals("200")) {
//                                Sequel.menyimpantf2("inacbg_data_terkirim2", "?,?", "No.Rawat", 2,
//                                        new String[]{noSep, noik});
//                                Sequel.mengedit("tt_status_digital_klaim", "no_rawat=?", "status_kirim=?,tgl_kirim=?,jam_kirim=?", 4, new String[]{
//                                    "Sudah Kirim", dateDigital.format(new Date()), timeDigital.format(new Date()), norawat
//                                });
//                                JOptionPane.showMessageDialog(rootPane, "Data Berhasil dikirim ke E-klaim");
//                            }
            } catch (Exception ex) {
                System.out.println("Notifikasi : " + ex);
                if (ex.toString().contains("UnknownHostException")) {
                    JOptionPane.showMessageDialog(rootPane, "Koneksi ke server E-klaim terputus...!");
                }
            }
//        }
    }

    private void tampilProsedurPilih() {
        Valid.tabelKosong(tabModeProsedurePasien);
        try {
            ps = koneksi.prepareStatement("select * "
                    + "from   prosedur_pasien JOIN icd9 ON prosedur_pasien.kode=icd9.kode "
                    + " where no_rawat ='" + norawat + "' order by prosedur_pasien.prioritas ASC ");
            try {

                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModeProsedurePasien.addRow(new Object[]{rs.getString("kode"),
                        rs.getString("deskripsi_panjang"),
                        (rs.getString("prioritas").equals("1") ? "Primary" : "Secondary"),
                        rs.getString("jumlah")});
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
    }

    private void tampilProsedureIDRG() {
        Valid.tabelKosong(tabModeProsedureIDRG);
        try {
            ps = koneksi.prepareStatement("select * "
                    + "from   tb_idrg_prosedure JOIN icd9 ON tb_idrg_prosedure.kode=icd9.kode "
                    + " where no_rawat ='" + norawat + "' order by tb_idrg_prosedure.prioritas ASC ");
            try {

                rs = ps.executeQuery();
                while (rs.next()) {
                    tabModeProsedureIDRG.addRow(new Object[]{rs.getString("kode"),
                        rs.getString("deskripsi_panjang"),
                        (rs.getString("prioritas").equals("1") ? "Primary" : "Secondary"), rs.getString("vol"),rs.getString("prioritas")});
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

    }

    private void cekStatusKlaim() {
        java.sql.PreparedStatement psCek = null;
        java.sql.ResultSet rsCek = null;
        try {
            psCek = koneksi.prepareStatement("select * "
                    + "from   tt_status_eklaim "
                    + " where no_rawat ='" + norawat + "'  ");
            try {

                rsCek = psCek.executeQuery();
                while (rsCek.next()) {

                    if ("true".equals(rsCek.getString("kirim_online"))) {
                        formTarifVisible(false);
                        btnDiagnosaLocal.setVisible(false);
                        btnImportDiagnosaDokter.setVisible(false);
                        btnUpdateDataKlaim.setVisible(false);
                        FormInputIDRG.setVisible(true);
                        btnImportToIDRG.setVisible(false);
                        setDataKlaim.setVisible(false);
                        btnGroupingIDRG.setVisible(false);
                        btnFinalIDRG.setVisible(false);
                        btnTambahDiagnosaIDRG.setVisible(false);
                        btnTambahProsedurIDRG.setVisible(false);
                        btnEditIDRG.setVisible(false);
                        btnImportToINACBG.setVisible(false);
                        FormInputINACBG.setVisible(true);
                        btnGroupingINACBG.setVisible(false);
                        btnFinalINACBG.setVisible(false);
                        btnTabahDiagnosaINACBG.setVisible(false);
                        btnTambahProsedurINACBG.setVisible(false);
                        btnEditINACBG.setVisible(false);
                        internalFrameEndKlaim.setVisible(true);
                        btnFinalKlaim.setVisible(false);
                        btnEditKlaim.setVisible(true);
                        btnKirimOnline.setVisible(false);
                        btnCetakKlaim.setVisible(true);
                        btnHasilFinalKlaim.setVisible(true);
                        FormInputStatusKlaim.setVisible(true);
                        btnTambahProsedurINACBG.setVisible(false);
                        getHasilINACBG();
                        FormInputHasilINACBG.setVisible(true);
                        FormInputHasilINACBG.setWarnaAtas(new Color(153, 255, 153));
                        FormInputHasilINACBG.setWarnaBawah(new Color(153, 255, 153));
                        panelHasilINACBG.setWarnaAtas(new Color(153, 255, 153));
                        panelHasilINACBG.setWarnaBawah(new Color(153, 255, 153));
                        CmbSpecialProcedure.setEnabled(false);
                        CmbSpecialProsthesis.setEnabled(false);
                        CmbSpecialInvestigation.setEnabled(false);
                        CmbSpeciaDrugs.setEnabled(false);
                        statusHasilInacbg.setText("final");
                        getHasilKlaim();
                        getHasiliDRG();
                        FormInputHasilIDRG.setVisible(true);
                        FormInputHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        FormInputHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                    } else if ("true".equals(rsCek.getString("final_klaim"))) {
                        formTarifVisible(false);
                        btnDiagnosaLocal.setVisible(false);
                        btnImportDiagnosaDokter.setVisible(false);
                        btnUpdateDataKlaim.setVisible(false);
                        FormInputIDRG.setVisible(true);
                        btnImportToIDRG.setVisible(false);
                        setDataKlaim.setVisible(false);
                        btnGroupingIDRG.setVisible(false);
                        btnFinalIDRG.setVisible(false);
                        btnTambahDiagnosaIDRG.setVisible(false);
                        btnTambahProsedurIDRG.setVisible(false);
                        btnEditIDRG.setVisible(false);
                        btnImportToINACBG.setVisible(false);
                        FormInputINACBG.setVisible(true);
                        btnGroupingINACBG.setVisible(false);
                        btnFinalINACBG.setVisible(false);
                        btnTambahProsedurINACBG.setVisible(false);
                        btnTabahDiagnosaINACBG.setVisible(false);
                        btnEditINACBG.setVisible(false);
                        internalFrameEndKlaim.setVisible(true);
                        btnFinalKlaim.setVisible(false);
                        btnEditKlaim.setVisible(true);
                        btnKirimOnline.setVisible(true);
                        btnCetakKlaim.setVisible(true);
                        btnHasilFinalKlaim.setVisible(false);
                        FormInputStatusKlaim.setVisible(true);
                        getHasilKlaim();
                        getHasilINACBG();
                        FormInputHasilINACBG.setVisible(true);
                        FormInputHasilINACBG.setWarnaAtas(new Color(153, 255, 153));
                        FormInputHasilINACBG.setWarnaBawah(new Color(153, 255, 153));
                        panelHasilINACBG.setWarnaAtas(new Color(153, 255, 153));
                        panelHasilINACBG.setWarnaBawah(new Color(153, 255, 153));
                        CmbSpecialProcedure.setEnabled(false);
                        CmbSpecialProsthesis.setEnabled(false);
                        CmbSpecialInvestigation.setEnabled(false);
                        CmbSpeciaDrugs.setEnabled(false);
                        statusHasilInacbg.setText("final");
                        getHasiliDRG();
                        internalFrameEndKlaim.setLocation(0, 840);
                        FormInputHasilIDRG.setVisible(true);
                        FormInputINACBG.setLocation(0, 0);
                        FormInputHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        FormInputHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                    } else if ("true".equals(rsCek.getString("final_inacbg"))) {
                        formTarifVisible(false);
                        btnDiagnosaLocal.setVisible(false);
                        btnImportDiagnosaDokter.setVisible(false);
                        btnUpdateDataKlaim.setVisible(false);
                        FormInputIDRG.setVisible(true);
                        btnImportToIDRG.setVisible(false);
                        setDataKlaim.setVisible(false);
                        btnGroupingIDRG.setVisible(false);
                        btnFinalIDRG.setVisible(false);
                        btnTambahDiagnosaIDRG.setVisible(false);
                        btnTambahProsedurIDRG.setVisible(false);
                        btnEditIDRG.setVisible(true);
                        btnImportToINACBG.setVisible(false);
                        FormInputINACBG.setVisible(true);
                        btnGroupingINACBG.setVisible(false);
                        btnFinalINACBG.setVisible(false);
                        btnTabahDiagnosaINACBG.setVisible(false);
                        btnTambahProsedurINACBG.setVisible(false);
                        btnEditINACBG.setVisible(true);
                        internalFrameEndKlaim.setVisible(true);
                        internalFrameEndKlaim.setLocation(0, 740);
                        btnFinalKlaim.setVisible(true);
                        btnEditKlaim.setVisible(false);
                        btnKirimOnline.setVisible(false);
                        btnCetakKlaim.setVisible(false);
                        btnHasilFinalKlaim.setVisible(false);
                        FormInputStatusKlaim.setVisible(false);
                        getHasilINACBG();
                        FormInputHasilINACBG.setVisible(true);
                        FormInputHasilINACBG.setWarnaAtas(new Color(153, 255, 153));
                        FormInputHasilINACBG.setWarnaBawah(new Color(153, 255, 153));
                        panelHasilINACBG.setWarnaAtas(new Color(153, 255, 153));
                        panelHasilINACBG.setWarnaBawah(new Color(153, 255, 153));
                        CmbSpecialProcedure.setEnabled(false);
                        CmbSpecialProsthesis.setEnabled(false);
                        CmbSpecialInvestigation.setEnabled(false);
                        CmbSpeciaDrugs.setEnabled(false);
                        statusHasilInacbg.setText("final");
                        getHasiliDRG();
                        FormInputHasilIDRG.setVisible(true);
                        FormInputHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        FormInputHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                    } else if ("true".equals(rsCek.getString("grouping_inacbg"))) {
                        formTarifVisible(false);
                        btnUpdateDataKlaim.setVisible(false);
                        FormInputIDRG.setVisible(true);
                        btnImportToIDRG.setVisible(false);
                        setDataKlaim.setVisible(false);
                        btnGroupingIDRG.setVisible(false);
                        btnFinalIDRG.setVisible(false);
                        btnTambahDiagnosaIDRG.setVisible(false);
                        btnTambahProsedurIDRG.setVisible(false);
                        btnEditIDRG.setVisible(true);
                        btnImportToINACBG.setVisible(false);
                        FormInputINACBG.setVisible(true);
                        btnGroupingINACBG.setVisible(true);
                        btnFinalINACBG.setVisible(true);
                        btnTabahDiagnosaINACBG.setVisible(true);
                        btnTambahProsedurINACBG.setVisible(true);
                        btnEditINACBG.setVisible(false);
                        internalFrameEndKlaim.setVisible(false);
                        btnFinalKlaim.setVisible(false);
                        btnEditKlaim.setVisible(false);
                        btnKirimOnline.setVisible(false);
                        btnCetakKlaim.setVisible(false);
                        btnHasilFinalKlaim.setVisible(false);
                        FormInputStatusKlaim.setVisible(false);
                        getHasilINACBG();
                        FormInputHasilINACBG.setVisible(true);
                        FormInputHasilINACBG.setWarnaAtas(new Color(204, 204, 204));
                        FormInputHasilINACBG.setWarnaBawah(new Color(204, 204, 204));
                        panelHasilINACBG.setWarnaAtas(new Color(204, 204, 204));
                        panelHasilINACBG.setWarnaBawah(new Color(204, 204, 204));
                        getHasiliDRG();
                        FormInputHasilIDRG.setVisible(true);
                        FormInputHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        FormInputHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                    } else if ("true".equals(rsCek.getString("final_idrg"))) {
                        formTarifVisible(false);
                        btnDiagnosaLocal.setVisible(false);
                        btnImportDiagnosaDokter.setVisible(false);
                        btnUpdateDataKlaim.setVisible(false);
                        FormInputIDRG.setVisible(true);
                        btnImportToIDRG.setVisible(false);
                        setDataKlaim.setVisible(false);
                        btnGroupingIDRG.setVisible(false);
                        btnFinalIDRG.setVisible(false);
                        btnTambahDiagnosaIDRG.setVisible(false);
                        btnTambahProsedurIDRG.setVisible(false);
                        btnEditIDRG.setVisible(true);
                        btnImportToINACBG.setVisible(true);
                        FormInputINACBG.setVisible(true);
                        btnGroupingINACBG.setVisible(true);
                        btnFinalINACBG.setVisible(false);
                        btnTabahDiagnosaINACBG.setVisible(true);
                        btnTambahProsedurINACBG.setVisible(true);
                        btnEditINACBG.setVisible(false);
                        internalFrameEndKlaim.setVisible(false);
                        btnFinalKlaim.setVisible(false);
                        btnEditKlaim.setVisible(false);
                        btnKirimOnline.setVisible(false);
                        btnCetakKlaim.setVisible(false);
                        btnHasilFinalKlaim.setVisible(false);
                        FormInputStatusKlaim.setVisible(false);
                        getHasiliDRG();
                        FormInputHasilIDRG.setVisible(true);
                        FormInputHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        FormInputHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaAtas(new Color(153, 255, 153));
                        panelHasilIDRG.setWarnaBawah(new Color(153, 255, 153));
                        getHasilINACBG();
                        FormInputHasilINACBG.setVisible(false);
                    } else if ("true".equals(rsCek.getString("grouping_idrg"))) {
                        formTarifVisible(true);
                        btnDiagnosaLocal.setVisible(true);
                        btnImportDiagnosaDokter.setVisible(true);
                        btnUpdateDataKlaim.setVisible(true);
                        FormInputIDRG.setVisible(true);
                        btnImportToIDRG.setVisible(true);
                        setDataKlaim.setVisible(false);
                        btnGroupingIDRG.setVisible(true);
                        btnFinalIDRG.setVisible(true);
                        btnTambahDiagnosaIDRG.setVisible(false);//takdisable
                        btnTambahProsedurIDRG.setVisible(false);//takdisable
                        btnEditIDRG.setVisible(false);
                        btnImportToINACBG.setVisible(false);
                        FormInputINACBG.setVisible(false);
                        btnGroupingINACBG.setVisible(false);
                        btnFinalINACBG.setVisible(false);
                        btnTabahDiagnosaINACBG.setVisible(false);
                        btnTambahProsedurINACBG.setVisible(false);
                        btnEditINACBG.setVisible(false);
                        internalFrameEndKlaim.setVisible(false);
                        btnFinalKlaim.setVisible(false);
                        btnEditKlaim.setVisible(false);
                        btnKirimOnline.setVisible(false);
                        btnCetakKlaim.setVisible(false);
                        btnHasilFinalKlaim.setVisible(false);
                        FormInputStatusKlaim.setVisible(false);
                        getHasiliDRG();
                        FormInputHasilIDRG.setVisible(true);
                        FormInputHasilIDRG.setWarnaAtas(new Color(204, 204, 204));
                        FormInputHasilIDRG.setWarnaBawah(new Color(204, 204, 204));
                        panelHasilIDRG.setWarnaAtas(new Color(204, 204, 204));
                        panelHasilIDRG.setWarnaBawah(new Color(204, 204, 204));
                         FormInputHasilINACBG.setVisible(false);
                    } else if ("true".equals(rsCek.getString("set_data_klaim"))) {
                        formTarifVisible(true);
                        btnDiagnosaLocal.setVisible(true);
                        btnImportDiagnosaDokter.setVisible(true);
                        btnUpdateDataKlaim.setVisible(true);
                        FormInputIDRG.setVisible(true);
                        btnImportToIDRG.setVisible(true);
                        setDataKlaim.setVisible(false);
                        btnGroupingIDRG.setVisible(true);
                        btnFinalIDRG.setVisible(false);
                        btnTambahDiagnosaIDRG.setVisible(false);//takdisable
                        btnTambahProsedurIDRG.setVisible(false);//takdisable
                        btnEditIDRG.setVisible(false);
                        FormInputINACBG.setVisible(false);
                        btnImportToINACBG.setVisible(false);
                        btnGroupingINACBG.setVisible(false);
                        btnFinalINACBG.setVisible(false);
                        btnTabahDiagnosaINACBG.setVisible(false);
                        btnTambahProsedurINACBG.setVisible(false);
                        btnEditINACBG.setVisible(false);
                        internalFrameEndKlaim.setVisible(false);
                        btnFinalKlaim.setVisible(false);
                        btnEditKlaim.setVisible(false);
                        btnKirimOnline.setVisible(false);
                        btnCetakKlaim.setVisible(false);
                        btnHasilFinalKlaim.setVisible(false);
                        FormInputStatusKlaim.setVisible(false);
                        FormInputHasilIDRG.setVisible(false);
                         FormInputHasilINACBG.setVisible(false);
                    } else {
                        setDataKlaim.setVisible(true);
                        btnImportToIDRG.setVisible(false);
                        FormInputIDRG.setVisible(false);
                        btnUpdateDataKlaim.setVisible(false);
                        btnGroupingIDRG.setVisible(false);
                        btnFinalIDRG.setVisible(false);
                        btnTambahDiagnosaIDRG.setVisible(false);
                        btnTambahProsedurIDRG.setVisible(false);
                        btnEditIDRG.setVisible(false);
                        FormInputINACBG.setVisible(false);
                        btnImportToINACBG.setVisible(false);
                        btnGroupingINACBG.setVisible(false);
                        btnFinalINACBG.setVisible(false);
                        btnTabahDiagnosaINACBG.setVisible(false);
                        btnTambahProsedurINACBG.setVisible(false);
                        btnEditINACBG.setVisible(false);
                        internalFrameEndKlaim.setVisible(false);
                        btnFinalKlaim.setVisible(false);
                        btnEditKlaim.setVisible(false);
                        btnKirimOnline.setVisible(false);
                        btnCetakKlaim.setVisible(false);
                        btnHasilFinalKlaim.setVisible(false);
                        FormInputStatusKlaim.setVisible(false);
                        FormInputHasilIDRG.setVisible(false);
                         FormInputHasilINACBG.setVisible(false);
                         btnImportDiagnosaDokter.setVisible(false);
                         btnDiagnosaLocal.setVisible(false);
                    }

//                    if ("true".equals(rsCek.getString("set_data_klaim"))) {
//                        setDataKlaim.setVisible(false);
//                        btnUpdateDataKlaim.setVisible(true);
//                    } else {
//                        setDataKlaim.setVisible(true);
//                        btnUpdateDataKlaim.setVisible(false);
//                    }
//                    if ("true".equals(rsCek.getString("grouping_idrg"))) {
//                        btnFinalIDRG.setVisible(true);
//                        btnGroupingIDRG.setVisible(true);
//                    } else {
//                        btnFinalIDRG.setVisible(false);
//                    }
//                    if ("true".equals(rsCek.getString("final_idrg"))) {
//                        btnEditIDRG.setVisible(true);
//                        btnHasilIDRG.setVisible(true);
//                        btnFinalIDRG.setVisible(false);
//                        btnGroupingIDRG.setVisible(false);
//                        btnImportToINACBG.setVisible(true);
//
//                    } else {
//                        btnEditIDRG.setVisible(false);
//                        btnHasilIDRG.setVisible(false);
//                        btnImportToINACBG.setVisible(false);
//                    }
//                    if ("true".equals(rsCek.getString("grouping_inacbg"))) {
//                        btnFinalINACBG.setVisible(true);
//                        btnGroupingINACBG.setVisible(true);
//
//                    } else {
//                        btnFinalINACBG.setVisible(false);
//                        btnGroupingINACBG.setVisible(false);
//                    }
//                    if ("true".equals(rsCek.getString("final_inacbg"))) {
//                        btnEditINACBG.setVisible(true);
//                        btnFinalINACBG.setVisible(true);
//                        btnGroupingINACBG.setVisible(false);
//
//                    } else {
//                        btnGroupingINACBG.setVisible(true);
//                        btnEditINACBG.setVisible(false);
//                    }
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rsCek != null) {
                    rsCek.close();
                }
                if (psCek != null) {
                    psCek.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

    }

    private void getHasilIDRG() {
        txtInfoHasilIDRG.setText("");
        txtStatus.setText("");
        txtMdcCode.setText("");
        txtMdcDescription.setText("");
        txtDrgCode.setText("");
        txtDrgDescription.setText("");
        java.sql.PreparedStatement psCek = null;
        java.sql.ResultSet rsCek = null;
        try {
            psCek = koneksi.prepareStatement("select * "
                    + "from   tb_idrg_grouping_result "
                    + " where no_rawat ='" + norawat + "' and no_sep='" + noSep + "' ");
            try {

                rsCek = psCek.executeQuery();
                while (rsCek.next()) {
                    txtInfoHasilIDRG.setText(rsCek.getString("datetime") + " - " + rsCek.getString("script_version") + " / " + rsCek.getString("logic_version"));
                    txtStatus.setText("final");
                    txtMdcCode.setText(rsCek.getString("mdc_number"));
                    txtMdcDescription.setText(rsCek.getString("mdc_description"));
                    txtDrgCode.setText(rsCek.getString("drg_code"));
                    txtDrgDescription.setText(rsCek.getString("drg_description"));
                }
            } catch (Exception e) {
                System.out.println("Notifikasi : " + e);
            } finally {
                if (rsCek != null) {
                    rsCek.close();
                }
                if (psCek != null) {
                    psCek.close();
                }
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

    }

    private void getHasilINACBG() {
        infoHasilINACBG.setText("");
        jnsrawatHasilINACBG.setText("");
        groupHasilINACBG.setText("");
        groupCodeHasilINACBG.setText("");
        groupCostHasilINACBG.setText("");
        txtSubAcute.setText("");
        txtSubAcuteCode.setText("");
        txtSubAcuteCost.setText("");
        txtCronic.setText("");
        txtCronicCode.setText("");
        txtCronicCost.setText("");
        txtSpecProcedure.setText("");
        txtSpecProcedureCode.setText("");
        txtSpecProcedureCost.setText("");
        txtSpecProthesis.setText("");
        txtSpecProthesisCode.setText("");
        txtSpecProthesisCost.setText("");
        txtSpecInvestigation.setText("");
        txtSpecInvestigationCode.setText("");
        txtSpecInvestigationCost.setText("");
        txtSpecDrug.setText("");
        txtSpecDrugCode.setText("");
        txtSpecDrugCost.setText("");
        txtTotalKlaim.setText("");
        txtStatusINACBG.setText("");
        try {
            ps = koneksi.prepareStatement("select * "
                    + "from   tb_eklaim_grouping_result "
                    + " where no_rawat ='" + norawat + "' and no_sep='" + noSep + "' ");
            try {

                rs = ps.executeQuery();
                while (rs.next()) {
                    String tarif, kelasRawat;
                    switch (rs.getString("kode_tarif")) {
                        case "DS":
                            tarif = "TARIF RS KELAS D SWASTA";
                            break;
                        default:
                            tarif = "TARIF RS KELAS D SWASTA";
                            break;
                    }
                    switch (rs.getString("kelas_rawat")) {
                        case "1":
                            kelasRawat = "KELAS 1";
                            break;
                        case "2":
                            kelasRawat = "KELAS 2";
                            break;
                        case "3":
                            kelasRawat = "KELAS 3";
                            break;
                        default:
                            kelasRawat = "KELAS 3";
                            break;
                    }

                    infoHasilINACBG.setText(rs.getString("coder_nm") + " @ " + rs.getString("datetime") + " - " + kelasRawat + " - " + tarif);
                    jnsrawatHasilINACBG.setText(rs.getString("jenis_rawat"));
                    groupHasilINACBG.setText(rs.getString("cbg_description"));
                    groupCodeHasilINACBG.setText(rs.getString("cbg_code"));
                    groupCostHasilINACBG.setText(Valid.SetAngka(Double.parseDouble(rs.getString("base_tariff"))));
                    subAcuteHasilINACBG.setText(rs.getString("adl_sub_acute"));
                    txtSubAcute.setText("-");
                    txtSubAcuteCode.setText("-");
                    txtSubAcuteCost.setText("-");
                    chronicHasilINACBG.setText("-");
                    txtCronic.setText(rs.getString("adl_chronic"));
                    txtCronicCode.setText("-");
                    txtCronicCost.setText("-");
                    txtSpecProcedure.setText("-");
                    txtSpecProcedureCode.setText("");
                    txtSpecProcedureCost.setText("-");
                    txtSpecProthesis.setText("-");
                    txtSpecProthesisCode.setText("-");
                    txtSpecProthesisCost.setText("-");
                    txtSpecInvestigation.setText("-");
                    txtSpecInvestigationCode.setText("-");
                    txtSpecInvestigationCost.setText("-");
                    txtSpecDrug.setText("-");
                    txtSpecDrugCode.setText("-");
                    txtSpecDrugCost.setText("-");
                    totalCostHasilKlaim.setText(Valid.SetAngka(Double.parseDouble(rs.getString("tariff"))));
                    statusHasilInacbg.setText(rs.getString("klaim_status_cd"));

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
        getSpecialCMG();
    }

    private void getSpecialCMG() {
        isLoading = true;
        txtStatusKlaim.setText("");
        txtStatus1.setText("");
        SpecialProsthesisHasilCode.setText("");
        SpecialProsthesisHasilCost.setText("");
        SpecialProcedureHasilCode.setText("");
        SpecialProcedureHasilCost.setText("");
        try {
            ps = koneksi.prepareStatement("select * "
                    + "from   tb_inacbg_special_cmg "
                    + " where no_rawat ='" + norawat + "' ORDER BY selected desc  ");
            try {

                rs = ps.executeQuery();
                CmbSpecialProcedure.removeAllItems();
                CmbSpecialProcedure.addItem("None");
                CmbSpecialProsthesis.removeAllItems();
                CmbSpecialProsthesis.addItem("None");
                while (rs.next()) {
                    if (rs.getString("type").equals("Special Procedure")) {
                        CmbSpecialProcedure.addItem(rs.getString("code") + " | " + rs.getString("description"));
                        if (rs.getString("selected").equals("1")) {
                            CmbSpecialProcedure.setSelectedItem(rs.getString("code") + " | " + rs.getString("description"));
                            SpecialProcedureHasilCode.setText(rs.getString("code_cmg"));
                            SpecialProcedureHasilCost.setText(Valid.SetAngka(Double.parseDouble(rs.getString("tariff"))));
                        }
                    } else if (rs.getString("type").equals("Special Prosthesis")) {
                        CmbSpecialProsthesis.addItem(rs.getString("code") + " | " + rs.getString("description"));
                        if (rs.getString("selected").equals("1")) {
                            CmbSpecialProsthesis.setSelectedItem(rs.getString("code") + " | " + rs.getString("description"));
                            SpecialProsthesisHasilCode.setText(rs.getString("code_cmg"));
                            SpecialProsthesisHasilCost.setText(Valid.SetAngka(Double.parseDouble(rs.getString("tariff"))));
                        }
                    }

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
        isLoading = false;
    }

    private void getHasilKlaim() {
        txtStatusKlaim.setText("");
        txtStatus1.setText("");
        try {
            ps = koneksi.prepareStatement("select * "
                    + "from   tb_eklaim_grouping_result "
                    + " where no_rawat ='" + norawat + "' and no_sep='" + noSep + "' ");
            try {

                rs = ps.executeQuery();
                while (rs.next()) {
                    String stsDC;
                    switch (rs.getString("kemenkes_dc_status_cd")) {
                        case "sent":
                            stsDC = "Terkirim";
                            statusDC.setForeground(Color.black);
                            break;
                        case "unsent":
                            stsDC = "Klaim belum terkirim ke Pusat Data Kementerian Kesehatan";
                            statusDC.setForeground(Color.red);
                        default:
                            stsDC = "Klaim belum terkirim ke Pusat Data Kementerian Kesehatan";
                            statusDC.setForeground(Color.red);
                            break;
                    }
                    txtStatusKlaim.setText(rs.getString("klaim_status_cd"));
                    txtStatus1.setText(stsDC);
                    statusKlaim.setText(rs.getString("klaim_status_cd"));
                    statusDC.setText(stsDC);
                    fileIndividual=rs.getString("file_individual");

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

    }

    private void getDataBilling() {
//        if (Sequel.cariInteger("Select count(no_rawat) from billing where  no_rawat='" + norawat + "' ") > 0) {
//            non_bedah = Sequel.cariIsi("select IFNULL(SUM(totalbiaya), 0) AS total from billing where no_rawat='" + norawat + "' and status='Ralan Dokter Paramedis'");
//            bedah = Sequel.cariIsi("select IFNULL(SUM(totalbiaya), 0) AS total from billing where no_rawat='" + norawat + "' and status='Operasi'");
//            keperawatan = Sequel.cariIsi("select IFNULL(SUM(totalbiaya), 0) AS total from billing where no_rawat='" + norawat + "' and status='Ralan Paramedis'");
//            konsultasi = Sequel.cariIsi("select IFNULL(SUM(totalbiaya), 0) AS total from billing where no_rawat='" + norawat + "' and status='Ralan Dokter'");
//            radiologi = Sequel.cariIsi("select IFNULL(SUM(totalbiaya), 0) AS total from billing where no_rawat='" + norawat + "' and status='Radiologi'");
//            lab = Sequel.cariIsi("select IFNULL(SUM(totalbiaya), 0) AS total from billing where no_rawat='" + norawat + "' and status='Laborat'");
//            obat = Sequel.cariIsi("select IFNULL(SUM(totalbiaya), 0) AS total from billing where no_rawat='" + norawat + "' and status='Obat'");
//
//        } else {
//            non_bedah = "0";
//            bedah = "0";
//            keperawatan = "0";
//            konsultasi = "0";
//            radiologi = "0";
//            lab = "0";
//            obat = "0";
//        }
//        int subTotal = Integer.parseInt(non_bedah) + Integer.parseInt(bedah) + Integer.parseInt(keperawatan) + Integer.parseInt(konsultasi) + Integer.parseInt(radiologi) + Integer.parseInt(lab) + Integer.parseInt(obat);
//        tfTotal.setText(subTotal + "");
//        tfNonBedah.setText(non_bedah);
//        tfBedah.setText(bedah);
//        tfBedah.setText(bedah);
//        tfKeperawatan.setText(keperawatan);
//        tfKonsultasi.setText(konsultasi);
//        tfRadiologi.setText(radiologi);
//        tfLaboratorium.setText(lab);
//        tfObat.setText(obat);
//        tfTenagaAhli.setText("0");
//        tfRehabilitasi.setText("0");
//        tfAlkes.setText("0");
//        tfKamar.setText("0");
//        tfObatKronis.setText("0");
//        tfBMHP.setText("0");
//        tfPenunjang.setText("0");
//        tfDarah.setText("0");
//        tfRawatIntensif.setText("0");
//        tfObatKemoterapi.setText("0");
//        tfSewaAlat.setText("0");

        try {
        if (Sequel.cariInteger("SELECT COUNT(no_rawat) FROM billing WHERE no_rawat='" + norawat + "'") > 0) {
//            non_bedah   = Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya), 0) FROM billing WHERE no_rawat='" + norawat + "' AND status='Ranap Dokter Paramedis'");
            non_bedah = Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya), 0) FROM billing WHERE no_rawat='" + norawat + "' AND status='Ralan Dokter Paramedis' AND nm_perawatan NOT LIKE '%Visite%' AND nm_perawatan NOT LIKE '%Konsultasi%' AND nm_perawatan NOT LIKE '%Konsul%'");
            bedah       = Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya), 0) FROM billing WHERE no_rawat='" + norawat + "' AND status='Operasi'");
            keperawatan = Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya), 0) FROM billing WHERE no_rawat='" + norawat + "' AND status='Ralan Paramedis'");
            konsultasi  = Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya), 0) FROM billing WHERE no_rawat='" + norawat + "' AND (nm_perawatan LIKE '%Konsul%' OR nm_perawatan LIKE '%Visite%')");
            radiologi   = Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya), 0) FROM billing WHERE no_rawat='" + norawat + "' AND status='Radiologi'");
            lab         = Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya), 0) FROM billing WHERE no_rawat='" + norawat + "' AND status='Laborat'");
            obat        = Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya), 0) FROM billing WHERE no_rawat='" + norawat + "' AND status IN ('Obat', 'Resep Pulang')");
            kamar        = Sequel.cariIsi("SELECT IFNULL(SUM(totalbiaya), 0) FROM billing WHERE no_rawat='" + norawat + "' AND status='Registrasi'");
        } else {
            non_bedah = bedah = keperawatan = konsultasi = radiologi = lab = obat = kamar = "0";
        }

        //  Gunakan Double.parseDouble agar aman dari "0.0", "123.45", atau ""
        double totalNonBedah   = parseDoubleSafe(non_bedah);
        double totalBedah      = parseDoubleSafe(bedah);
        double totalKeperawatan= parseDoubleSafe(keperawatan);
        double totalKonsultasi = parseDoubleSafe(konsultasi);
        double totalRadiologi  = parseDoubleSafe(radiologi);
        double totalLab        = parseDoubleSafe(lab);
        double totalObat       = parseDoubleSafe(obat);
        double totalKamar        = parseDoubleSafe(kamar); // Gabungan Kamar + Administrasi

        double subTotal = totalNonBedah + totalBedah + totalKeperawatan + totalKonsultasi + totalRadiologi + totalLab + totalObat + totalKamar;

        //  Tampilkan hasil ke text field (tanpa notasi ilmiah)
        tfTotal.setText(String.format("%.0f", subTotal));
        tfNonBedah.setText(String.format("%.0f", totalNonBedah));
        tfBedah.setText(String.format("%.0f", totalBedah));
        tfKeperawatan.setText(String.format("%.0f", totalKeperawatan));
        tfKonsultasi.setText(String.format("%.0f", totalKonsultasi));
        tfRadiologi.setText(String.format("%.0f", totalRadiologi));
        tfLaboratorium.setText(String.format("%.0f", totalLab));
        tfObat.setText(String.format("%.0f", totalObat));
        tfKamar.setText(String.format("%.0f", totalKamar)); // Gabungan dua kategori

        //  Set field lain default 0
        tfTenagaAhli.setText("0");
        tfRehabilitasi.setText("0");
        tfAlkes.setText("0");
//        tfKamar.setText("0");
        tfObatKronis.setText("0");
        tfBMHP.setText("0");
        tfPenunjang.setText("0");
        tfDarah.setText("0");
        tfRawatIntensif.setText("0");
        tfObatKemoterapi.setText("0");
        tfSewaAlat.setText("0");

    } catch (Exception e) {
        System.out.println("Error getDataBilling: " + e);
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat mengambil data billing!");
    }

    }

    private void getHasiliDRG() {
        infoHasiliDRG.setText("");
        jsnrawatHasiliDRG.setText("");
        mdcHasiliDRG.setText("");
        mdcCodeHasiliDRG.setText("");
        drgHasiliDRG.setText("");
        drgCodeHasiliDRG.setText("");
        statusHasiliDRG.setText("");
        try {
            ps = koneksi.prepareStatement("select * "
                    + "from   tb_idrg_grouping_result "
                    + " where no_rawat ='" + norawat + "' and no_sep='" + noSep + "' ");
            try {
                rs = ps.executeQuery();
                while (rs.next()) {
                    infoHasiliDRG.setText(rs.getString("datetime") + " - " + rs.getString("script_version") + " / " + rs.getString("logic_version"));
                    jsnrawatHasiliDRG.setText(rs.getString("jns_rawat"));
                    mdcHasiliDRG.setText(rs.getString("mdc_description"));
                    mdcCodeHasiliDRG.setText(rs.getString("mdc_number"));
                    if (rs.getString("mdc_description").equals("Ungroupable or Unrelated")) {
                        mdcHasiliDRG.setForeground(Color.red);
                        drgHasiliDRG.setForeground(Color.red);
                        btnFinalIDRG.setVisible(false);
                    } else {
                        mdcHasiliDRG.setForeground(Color.black);
                        drgHasiliDRG.setForeground(Color.black);
                    }
                    drgHasiliDRG.setText(rs.getString("drg_description"));
                    drgCodeHasiliDRG.setText(rs.getString("drg_code"));
                    statusHasiliDRG.setText(rs.getString("status"));
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
    }

    private void getCmbCMG() {
        String cmgProcedure, cmgProsthesis, cmgInvestigation, cmbDrugs, all;
        if (CmbSpecialProcedure.getSelectedItem().toString().equals("None")) {
            cmgProcedure = "";
        } else {
            cmgProcedure = CmbSpecialProcedure.getSelectedItem().toString().split(" | ")[0] + "#";
        }
        if (CmbSpecialProsthesis.getSelectedItem().toString().equals("None")) {
            cmgProsthesis = "";
        } else {
            cmgProsthesis = CmbSpecialProsthesis.getSelectedItem().toString().split(" | ")[0] + "#";
        }
        if (CmbSpecialInvestigation.getSelectedItem().toString().equals("None")) {
            cmgInvestigation = "";
        } else {
            cmgInvestigation = CmbSpecialInvestigation.getSelectedItem().toString().split(" | ")[0] + "#";
        }
        if (CmbSpeciaDrugs.getSelectedItem().toString().equals("None")) {
            cmbDrugs = "";
        } else {
            cmbDrugs = CmbSpeciaDrugs.getSelectedItem().toString().split(" | ")[0] + "#";
        }
        all = cmgProcedure + cmgProsthesis + cmgInvestigation + cmbDrugs;
        if (all.length() > 0) {
            allCmg = all.substring(0, all.length() - 1);
        } else {
            allCmg = "";
        }

    }

    private void formTarifVisible(boolean visibilitasForm) {
        if (visibilitasForm == true) {
            tfNonBedah.setForeground(Color.black);
            tfBedah.setForeground(Color.black);
            tfKonsultasi.setForeground(Color.black);
            tfTenagaAhli.setForeground(Color.black);
            tfKeperawatan.setForeground(Color.black);
            tfPenunjang.setForeground(Color.black);
            tfRadiologi.setForeground(Color.black);
            tfLaboratorium.setForeground(Color.black);
            tfDarah.setForeground(Color.black);
            tfRehabilitasi.setForeground(Color.black);
            tfKamar.setForeground(Color.black);
            tfRawatIntensif.setForeground(Color.black);
            tfObat.setForeground(Color.black);
            tfObatKronis.setForeground(Color.black);
            tfAlkes.setForeground(Color.black);
            tfBMHP.setForeground(Color.black);
            tfSewaAlat.setForeground(Color.black);
            jnsLayanan.setForeground(Color.black);
            txtNoSep.setForeground(Color.black);
            tglMasuk.setForeground(Color.black);
            jamMasuk.setForeground(Color.black);
            txtNoRm.setForeground(Color.black);
            txtNamaPasien.setForeground(Color.black);
            noKartuJkn.setForeground(Color.black);
            TglLahir.setForeground(Color.black);
            Jk.setForeground(Color.black);
            txtHakKelas.setForeground(Color.black);
            kdDokter.setForeground(Color.black);
            nmDokter.setForeground(Color.black);
            beratLahir.setForeground(Color.black);
            sistol.setForeground(Color.black);
            diastol.setForeground(Color.black);
            subAcute.setForeground(Color.black);
            chronic.setForeground(Color.black);

        } else {
            tfNonBedah.setForeground(Color.red);
            tfBedah.setForeground(Color.red);
            tfKonsultasi.setForeground(Color.red);
            tfTenagaAhli.setForeground(Color.red);
            tfKeperawatan.setForeground(Color.red);
            tfPenunjang.setForeground(Color.red);
            tfRadiologi.setForeground(Color.red);
            tfLaboratorium.setForeground(Color.red);
            tfDarah.setForeground(Color.red);
            tfRehabilitasi.setForeground(Color.red);
            tfKamar.setForeground(Color.red);
            tfRawatIntensif.setForeground(Color.red);
            tfObat.setForeground(Color.red);
            tfObatKronis.setForeground(Color.red);
            tfObatKemoterapi.setForeground(Color.red);
            tfAlkes.setForeground(Color.red);
            tfBMHP.setForeground(Color.red);
            tfSewaAlat.setForeground(Color.red);
            jnsLayanan.setForeground(Color.red);
            txtNoSep.setForeground(Color.red);
            tglMasuk.setForeground(Color.red);
            jamMasuk.setForeground(Color.red);
            txtNoRawat.setForeground(Color.red);
            txtNoRm.setForeground(Color.red);
            txtNamaPasien.setForeground(Color.red);
            noKartuJkn.setForeground(Color.red);
            TglLahir.setForeground(Color.red);
            Jk.setForeground(Color.red);
            tglPulang.setForeground(Color.red);
            jamPulang.setForeground(Color.red);
            kdPoli.setForeground(Color.red);
            nmPoli.setForeground(Color.red);
            txtHakKelas.setForeground(Color.red);
            kdDokter.setForeground(Color.red);
            nmDokter.setForeground(Color.red);
            beratLahir.setForeground(Color.red);
            sistol.setForeground(Color.red);
            diastol.setForeground(Color.red);
            subAcute.setForeground(Color.red);
            chronic.setForeground(Color.red);
        }
        tfNonBedah.setEditable(visibilitasForm);
        tfBedah.setEditable(visibilitasForm);
        tfKonsultasi.setEditable(visibilitasForm);
        tfTenagaAhli.setEditable(visibilitasForm);
        tfKeperawatan.setEditable(visibilitasForm);
        tfPenunjang.setEditable(visibilitasForm);
        tfRadiologi.setEditable(visibilitasForm);
        tfLaboratorium.setEditable(visibilitasForm);
        tfDarah.setEditable(visibilitasForm);
        tfRehabilitasi.setEditable(visibilitasForm);
        tfKamar.setEditable(visibilitasForm);
        tfRawatIntensif.setEditable(visibilitasForm);
        tfObat.setEditable(visibilitasForm);
        tfObatKronis.setEditable(visibilitasForm);
        tfObatKemoterapi.setEditable(visibilitasForm);
        tfAlkes.setEditable(visibilitasForm);
        tfBMHP.setEditable(visibilitasForm);
        tfSewaAlat.setEditable(visibilitasForm);
        ChkPasienTB.setEnabled(visibilitasForm);
        ChkPasienTB.setEnabled(visibilitasForm);
        chkEksekutif.setEnabled(visibilitasForm);
        chkNaikKelas.setEnabled(visibilitasForm);
        chkIntensif.setEnabled(visibilitasForm);
        jnsLayanan.setEditable(visibilitasForm);
        txtNoSep.setEditable(visibilitasForm);
        tglMasuk.setEditable(visibilitasForm);
        jamMasuk.setEditable(visibilitasForm);
        txtNoRawat.setEditable(visibilitasForm);
        txtNoRm.setEditable(visibilitasForm);
        txtNamaPasien.setEditable(visibilitasForm);
        noKartuJkn.setEditable(visibilitasForm);
        TglLahir.setEditable(visibilitasForm);
        Jk.setEditable(visibilitasForm);
        tglPulang.setEditable(visibilitasForm);
        jamPulang.setEditable(visibilitasForm);
        kdPoli.setEditable(visibilitasForm);
        nmPoli.setEditable(visibilitasForm);
        cmrCaraPulang.setEnabled(visibilitasForm);
        cmbCaraMasuk.setEnabled(visibilitasForm);
        txtHakKelas.setEditable(visibilitasForm);
        kdDokter.setEditable(visibilitasForm);
        nmDokter.setEditable(visibilitasForm);
        beratLahir.setEditable(visibilitasForm);
        sistol.setEditable(visibilitasForm);
        diastol.setEditable(visibilitasForm);
        subAcute.setEditable(visibilitasForm);
        chronic.setEditable(visibilitasForm);

    }
    public void openpdf( String file) {
        try {
            java.net.URL url = new java.net.URL("http://" + koneksiDB.HOSTHYBRIDWEB() + ":" + koneksiDB.PORTWEB() + "/webapps/inacbg_idrg_dev/"  + file);
            SwingController ctrl = new SwingController();
            SwingViewBuilder vb = new SwingViewBuilder(ctrl);
            JPanel s = vb.buildViewerPanel();
            s.setPreferredSize(new Dimension(400, 243));
            s.setMaximumSize(new Dimension(400, 243));
            ComponentKeyBinding.install(ctrl, s);
            ctrl.setToolBarVisible(false);
            ctrl.getDocumentViewController().setAnnotationCallback(
                    new org.icepdf.ri.common.MyAnnotationCallback(ctrl.getDocumentViewController())
            );
            ctrl.openDocument(url);
            jScrollPane1.setViewportView(s);
        } catch (Exception e) {

        }
    }
    
    private double parseDoubleSafe(String value) {
    try {
        if (value == null || value.trim().isEmpty()) {
            return 0.0;
        }
        return Double.parseDouble(value);
    } catch (Exception e) {
        return 0.0;
    }
    }
    
        private void offtombol() {
        btnGroupingIDRG.setVisible(false);
        btnImportDiagnosaDokter.setVisible(false);
        btnDiagnosaLocal.setVisible(false);
    }
    
//    private void tampilresume() {
//        try {
//            ps=koneksi.prepareStatement(
////                    "SELECT persetujuan_penolakan_tindakan.penerima_informasi,persetujuan_penolakan_tindakan.alasan_diwakilkan_penerima_informasi,persetujuan_penolakan_tindakan.alamat_penerima_informasi,persetujuan_penolakan_tindakan.no_hp, "+
////                    "persetujuan_penolakan_tindakan.umur_penerima_informasi,persetujuan_penolakan_tindakan.saksi_keluarga,"+
////                    "persetujuan_penolakan_tindakan.tanggal_lahir_penerima_informasi, if(persetujuan_penolakan_tindakan.jk_penerima_informasi='L','LAKI-LAKI','PEREMPUAN') as jk FROM persetujuan_penolakan_tindakan"+
////                    "where persetujuan_penolakan_tindakan.no_rawat=?");
//            "SELECT * FROM resume_pasien_ranap WHERE no_rawat=?");
//                         
//            try {
//                ps.setString(1,txtNoRawat.getText());
//                rs=ps.executeQuery();
//                if(rs.next()){
//                    TkeluhanU.setText(rs.getString("keluhan_utama"));
//                    Tpemeriksaanfisik.setText(rs.getString("pemeriksaan_fisik"));
//                    Tjalannyapenyakit.setText(rs.getString("jalannya_penyakit"));
//                    Tpemeriksaanrad.setText(rs.getString("pemeriksaan_penunjang"));
//                    Tpemeriksaanlab.setText(rs.getString("hasil_laborat"));
//                    Ttindakanoperasi.setText(rs.getString("tindakan_dan_operasi"));
//                    Tobat.setText(rs.getString("obat_di_rs"));
//                    
//                    TdiagnosaUtama.setText(rs.getString("diagnosa_utama"));
//                    Tdiagnosa1.setText(rs.getString("diagnosa_sekunder"));
//                    Tdiagnosa2.setText(rs.getString("diagnosa_sekunder2"));
//                    Tdiagnosa3.setText(rs.getString("diagnosa_sekunder3"));
//                    Tdiagnosa4.setText(rs.getString("diagnosa_sekunder4"));
//                    
//                    TProsedurutama.setText(rs.getString("prosedur_utama"));
//                    TProsedur1.setText(rs.getString("prosedur_sekunder"));
//                    TProsedur2.setText(rs.getString("prosedur_sekunder2"));
//                    TProsedur3.setText(rs.getString("prosedur_sekunder3"));
//                    TProsedur4.setText(rs.getString("prosedur_sekunder3"));
//                    
//                    Tkeadaanpulang.setText(rs.getString("keadaan"));
//                    Tdilanjutkan.setText(rs.getString("dilanjutkan"));
//                    Tcarakeluar.setText(rs.getString("cara_keluar"));
//                    Ttanggalkontrol.setText(rs.getString("kontrol"));
////                    TglLahirPenerima.setDate(rs.getDate("tanggal_lahir_penerima_informasi"));
////                    JKPenerima.setSelectedItem(rs.getString("jk"));
////                    HubunganDenganPasien.setSelectedItem(rs.getString("hubungan_penerima_informasi"));
//
//                    
//                    
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
//        } catch (Exception e) {
//            System.out.println("Notif : "+e);
//        }
//    }
    
}
