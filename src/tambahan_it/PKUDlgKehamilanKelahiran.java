package tambahan_it;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import fungsi.koneksiDB;
import fungsi.validasi;
import fungsi.WarnaTable;
import widget.Button;
import widget.ComboBox;
import widget.InternalFrame;
import widget.Label;
import widget.PanelBiasa;
import widget.ScrollPane;
import widget.Table;
import widget.TextBox;
import widget.Tanggal;

public class PKUDlgKehamilanKelahiran extends JDialog {
    private InternalFrame internalFrame1;
    private PanelBiasa FormInput;
    private PanelBiasa panelBawah;
    
    private Label labelUsiaKehamilan, labelGravida, labelPartus, labelAbortus, labelOnset;
    private TextBox tfUsiaKehamilan, tfGravida, tfPartus, tfAbortus;
    private ComboBox cbOnset;
    
    private Label labelWaktu, labelCara, labelLetak, labelKondisi, labelSHKAmbil, labelSHKLokasi, labelSHKWaktu;
    private Tanggal tglWaktuLahir, tglSHKWaktu;
    private ComboBox cbCara, cbLetak, cbKondisi, cbSHKAmbil, cbSHKLokasi, cbAlasanSHK;
    private widget.CekBox chkManual, chkForcep, chkVacuum;
    
    private Button btnSimpan, btnTambah, btnHapus, btnKeluar;
    private Table tbKelahiran;
    private DefaultTableModel tabMode;
    private ScrollPane scrollPane;
    
    private Connection koneksi = koneksiDB.condb();
    private validasi Valid = new validasi();
    private PreparedStatement ps;
    private ResultSet rs;
    
    private String no_sep = "";

    public PKUDlgKehamilanKelahiran(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.setLocation(8, 1);
        setSize(850, 500);
    }
    
    private void initComponents() {
        internalFrame1 = new InternalFrame();
        internalFrame1.setLayout(new BorderLayout());
        internalFrame1.setName("internalFrame1");
        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(
            javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), 
            "::[ Data Kehamilan & Kelahiran E-Klaim ]::", 
            javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
            javax.swing.border.TitledBorder.DEFAULT_POSITION, 
            new java.awt.Font("Lucida Grande", 1, 13), 
            new java.awt.Color(255, 255, 255)
        ));
        
        FormInput = new PanelBiasa();
        FormInput.setLayout(null);
        FormInput.setPreferredSize(new Dimension(800, 310));
        
        // --- PANEL KEHAMILAN ---
        JPanel panelKehamilan = new JPanel(null);
        panelKehamilan.setOpaque(false);
        panelKehamilan.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), " Data Kehamilan ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));
        panelKehamilan.setBounds(10, 10, 750, 90);
        FormInput.add(panelKehamilan);
        
        labelUsiaKehamilan = new Label();
        labelUsiaKehamilan.setText("Usia Kehamilan (minggu):");
        labelUsiaKehamilan.setBounds(10, 25, 150, 23);
        panelKehamilan.add(labelUsiaKehamilan);
        tfUsiaKehamilan = new TextBox();
        tfUsiaKehamilan.setBounds(165, 25, 60, 23);
        panelKehamilan.add(tfUsiaKehamilan);
        
        labelGravida = new Label();
        labelGravida.setText("Gravida:");
        labelGravida.setBounds(240, 25, 60, 23);
        panelKehamilan.add(labelGravida);
        tfGravida = new TextBox();
        tfGravida.setBounds(305, 25, 50, 23);
        panelKehamilan.add(tfGravida);
        
        labelPartus = new Label();
        labelPartus.setText("Partus:");
        labelPartus.setBounds(365, 25, 50, 23);
        panelKehamilan.add(labelPartus);
        tfPartus = new TextBox();
        tfPartus.setBounds(420, 25, 50, 23);
        panelKehamilan.add(tfPartus);
        
        labelAbortus = new Label();
        labelAbortus.setText("Abortus:");
        labelAbortus.setBounds(480, 25, 50, 23);
        panelKehamilan.add(labelAbortus);
        tfAbortus = new TextBox();
        tfAbortus.setBounds(535, 25, 50, 23);
        panelKehamilan.add(tfAbortus);
        
        labelOnset = new Label();
        labelOnset.setText("Onset Kontraksi:");
        labelOnset.setBounds(10, 55, 150, 23);
        panelKehamilan.add(labelOnset);
        cbOnset = new ComboBox();
        cbOnset.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Timbul Spontan", "2. Dengan Induksi", "3. SC Tanpa Kontraksi/Induksi" }));
        cbOnset.setBounds(165, 55, 200, 23);
        panelKehamilan.add(cbOnset);
        
        // --- PANEL KELAHIRAN ---
        JPanel panelKelahiran = new JPanel(null);
        panelKelahiran.setOpaque(false);
        panelKelahiran.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new java.awt.Color(102, 102, 102)), " Data Kelahiran (Bayi) ", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 11)));
        panelKelahiran.setBounds(10, 110, 750, 190);
        FormInput.add(panelKelahiran);
        
        labelWaktu = new Label();
        labelWaktu.setText("Waktu Lahir:");
        labelWaktu.setBounds(10, 25, 80, 23);
        panelKelahiran.add(labelWaktu);
        tglWaktuLahir = new Tanggal();
        tglWaktuLahir.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dd-MM-yyyy HH:mm:ss" }));
        tglWaktuLahir.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        tglWaktuLahir.setBounds(95, 25, 140, 23);
        panelKelahiran.add(tglWaktuLahir);
        
        labelCara = new Label();
        labelCara.setText("Cara Lahir:");
        labelCara.setBounds(245, 25, 70, 23);
        panelKelahiran.add(labelCara);
        cbCara = new ComboBox();
        cbCara.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Vaginal", "2. Sectio Caesarean" }));
        cbCara.setBounds(320, 25, 150, 23);
        panelKelahiran.add(cbCara);
        
        labelLetak = new Label();
        labelLetak.setText("Letak:");
        labelLetak.setBounds(480, 25, 50, 23);
        panelKelahiran.add(labelLetak);
        cbLetak = new ComboBox();
        cbLetak.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Kepala", "2. Sungsang", "3. Lintang/Oblique" }));
        cbLetak.setBounds(535, 25, 150, 23);
        panelKelahiran.add(cbLetak);
        
        // Checkboxes for Vaginal
        chkManual = new widget.CekBox();
        chkManual.setText("Manual Aid");
        chkManual.setBounds(245, 55, 90, 23);
        panelKelahiran.add(chkManual);
        
        chkForcep = new widget.CekBox();
        chkForcep.setText("Forcep");
        chkForcep.setBounds(340, 55, 70, 23);
        panelKelahiran.add(chkForcep);
        
        chkVacuum = new widget.CekBox();
        chkVacuum.setText("Vacuum");
        chkVacuum.setBounds(415, 55, 80, 23);
        panelKelahiran.add(chkVacuum);
        
        labelKondisi = new Label();
        labelKondisi.setText("Kondisi:");
        labelKondisi.setBounds(10, 85, 80, 23);
        panelKelahiran.add(labelKondisi);
        cbKondisi = new ComboBox();
        cbKondisi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Hidup", "2. Meninggal" }));
        cbKondisi.setBounds(95, 85, 140, 23);
        panelKelahiran.add(cbKondisi);
        
        labelSHKAmbil = new Label();
        labelSHKAmbil.setText("Spesimen SHK:");
        labelSHKAmbil.setBounds(245, 85, 80, 23);
        panelKelahiran.add(labelSHKAmbil);
        cbSHKAmbil = new ComboBox();
        cbSHKAmbil.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Diambil", "2. Tidak Diambil" }));
        cbSHKAmbil.setBounds(330, 85, 140, 23);
        panelKelahiran.add(cbSHKAmbil);
        
        labelSHKLokasi = new Label();
        labelSHKLokasi.setText("Lokasi:");
        labelSHKLokasi.setBounds(480, 85, 50, 23);
        panelKelahiran.add(labelSHKLokasi);
        cbSHKLokasi = new ComboBox();
        cbSHKLokasi.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "1. Tumit", "2. Vena" }));
        cbSHKLokasi.setBounds(535, 85, 100, 23);
        panelKelahiran.add(cbSHKLokasi);
        
        labelSHKWaktu = new Label();
        labelSHKWaktu.setText("Waktu SHK:");
        labelSHKWaktu.setBounds(10, 115, 80, 23);
        panelKelahiran.add(labelSHKWaktu);
        tglSHKWaktu = new Tanggal();
        tglSHKWaktu.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "dd-MM-yyyy HH:mm:ss" }));
        tglSHKWaktu.setDisplayFormat("dd-MM-yyyy HH:mm:ss");
        tglSHKWaktu.setBounds(95, 115, 140, 23);
        panelKelahiran.add(tglSHKWaktu);
        
        cbAlasanSHK = new ComboBox();
        cbAlasanSHK.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "-", "1. Tidak dapat dilakukan", "2. Akses sulit" }));
        cbAlasanSHK.setBounds(330, 115, 160, 23);
        panelKelahiran.add(cbAlasanSHK);
        
        btnTambah = new Button();
        btnTambah.setText("Tambah Bayi");
        btnTambah.setBounds(95, 150, 120, 25);
        panelKelahiran.add(btnTambah);
        btnHapus = new Button();
        btnHapus.setText("Hapus Terpilih");
        btnHapus.setBounds(225, 150, 120, 25);
        panelKelahiran.add(btnHapus);
        
        internalFrame1.add(FormInput, BorderLayout.PAGE_START);
        
        // --- TABEL ---
        tabMode = new DefaultTableModel(null, new Object[]{
            "ID", "Waktu Lahir", "Cara Lahir", "Letak", "Kondisi", "SHK Diambil", "SHK Lokasi", "SHK Waktu"
        }) {
            @Override
            public boolean isCellEditable(int rowIndex, int colIndex) {
                return false;
            }
        };
        tbKelahiran = new Table();
        tbKelahiran.setModel(tabMode);
        tbKelahiran.setPreferredScrollableViewportSize(new Dimension(800, 150));
        tbKelahiran.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        for (int i = 0; i < 8; i++) {
            javax.swing.table.TableColumn column = tbKelahiran.getColumnModel().getColumn(i);
            if (i == 0) column.setPreferredWidth(30);
            else if (i == 1) column.setPreferredWidth(130);
            else column.setPreferredWidth(100);
        }
        tbKelahiran.setDefaultRenderer(Object.class, new WarnaTable());
        
        scrollPane = new ScrollPane();
        scrollPane.setViewportView(tbKelahiran);
        internalFrame1.add(scrollPane, BorderLayout.CENTER);
        
        // --- PANEL BAWAH ---
        panelBawah = new PanelBiasa();
        panelBawah.setLayout(new FlowLayout(FlowLayout.RIGHT));
        panelBawah.setPreferredSize(new Dimension(800, 45));
        
        btnSimpan = new Button();
        btnSimpan.setText("Simpan Data (Lokal)");
        btnSimpan.setPreferredSize(new Dimension(150, 30));
        panelBawah.add(btnSimpan);
        
        btnKeluar = new Button();
        btnKeluar.setText("Tutup");
        btnKeluar.setPreferredSize(new Dimension(100, 30));
        panelBawah.add(btnKeluar);
        
        internalFrame1.add(panelBawah, BorderLayout.PAGE_END);
        
        getContentPane().add(internalFrame1, BorderLayout.CENTER);
        
        // --- EVENTS ---
        btnKeluar.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                dispose();
            }
        });
        
        btnTambah.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                tambahBayi();
            }
        });
        
        btnHapus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                hapusBayi();
            }
        });
        
        btnSimpan.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                simpanData();
            }
        });
        
        cbCara.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (cbCara.getSelectedIndex() == 1) { // SC
                    chkManual.setVisible(false);
                    chkForcep.setVisible(false);
                    chkVacuum.setVisible(false);
                    chkManual.setSelected(false);
                    chkForcep.setSelected(false);
                    chkVacuum.setSelected(false);
                } else { // Vaginal
                    chkManual.setVisible(true);
                    chkForcep.setVisible(true);
                    chkVacuum.setVisible(true);
                }
            }
        });
        
        cbSHKAmbil.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                if (cbSHKAmbil.getSelectedIndex() == 0) { // Diambil
                    labelSHKLokasi.setVisible(true);
                    cbSHKLokasi.setVisible(true);
                    cbAlasanSHK.setVisible(false);
                    cbAlasanSHK.setSelectedIndex(0);
                } else { // Tidak Diambil
                    labelSHKLokasi.setVisible(false);
                    cbSHKLokasi.setVisible(false);
                    cbSHKLokasi.setSelectedIndex(0);
                    cbAlasanSHK.setVisible(true);
                }
            }
        });
        
        // Initial states
        cbAlasanSHK.setVisible(false);
    }
    
    public void setNoSep(String sep) {
        this.no_sep = sep;
        tampilData();
    }
    
    private void tampilData() {
        Valid.tabelKosong(tabMode);
        tfUsiaKehamilan.setText("0");
        tfGravida.setText("0");
        tfPartus.setText("0");
        tfAbortus.setText("0");
        cbOnset.setSelectedIndex(0);
        chkManual.setSelected(false);
        chkForcep.setSelected(false);
        chkVacuum.setSelected(false);
        cbAlasanSHK.setSelectedIndex(0);
        
        try {
            ps = koneksi.prepareStatement("SELECT * FROM pku_eklaim_kehamilan WHERE no_sep=?");
            try {
                ps.setString(1, no_sep);
                rs = ps.executeQuery();
                if (rs.next()) {
                    tfUsiaKehamilan.setText(rs.getString("usia_kehamilan"));
                    tfGravida.setText(rs.getString("gravida"));
                    tfPartus.setText(rs.getString("partus"));
                    tfAbortus.setText(rs.getString("abortus"));
                    int onset = rs.getInt("onset_kontraksi");
                    if (onset >= 1 && onset <= 3) cbOnset.setSelectedIndex(onset - 1);
                }
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
            
            PreparedStatement psKlh = koneksi.prepareStatement("SELECT * FROM pku_eklaim_kelahiran WHERE no_sep=? ORDER BY id ASC");
            try {
                psKlh.setString(1, no_sep);
                ResultSet rsK = psKlh.executeQuery();
                while (rsK.next()) {
                    String wLahir = rsK.getString("waktu_kelahiran");
                    String cLahir = rsK.getInt("cara_kelahiran") == 1 ? "1. Vaginal" : "2. SC";
                    String letak = rsK.getInt("letak_presentasi") == 1 ? "1. Kepala" : (rsK.getInt("letak_presentasi") == 2 ? "2. Sungsang" : "3. Lintang");
                    String kondisi = rsK.getInt("kondisi") == 1 ? "1. Hidup" : "2. Meninggal";
                    String shkAmbil = rsK.getInt("shk_pengambilan") == 1 ? "1. Diambil" : "2. Tdk Diambil";
                    String shkLokasi = rsK.getInt("shk_lokasi") == 1 ? "1. Tumit" : "2. Vena";
                    
                    String extra = (rsK.getInt("manual_aid")==1?" Manual":"") + (rsK.getInt("forcep")==1?" Forcep":"") + (rsK.getInt("vacuum")==1?" Vacuum":"");
                    if(!extra.isEmpty()) cLahir += " (" + extra.trim() + ")";
                    
                    String alasan = "";
                    if (rsK.getInt("shk_pengambilan") == 2 && rsK.getInt("alasan_tidak_shk") != 0) {
                        alasan = " (Alasan: " + (rsK.getInt("alasan_tidak_shk")==1 ? "Tidak dpt dilakukan" : "Akses sulit") + ")";
                    }
                    
                    tabMode.addRow(new Object[]{
                        rsK.getString("id"), wLahir, cLahir, letak, kondisi, shkAmbil + alasan, shkLokasi, rsK.getString("shk_waktu")
                    });
                }
            } finally {
                if (psKlh != null) psKlh.close();
            }
        } catch (Exception e) {
            System.out.println("Notif : " + e);
        }
    }
    
    private void tambahBayi() {
        if (no_sep.isEmpty()) {
            JOptionPane.showMessageDialog(null, "No SEP kosong!");
            return;
        }
        try {
            // Otomatis simpan data ibu (kehamilan) dulu agar tidak kena Foreign Key constraint fail
            simpanKehamilanLokal();
            
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String waktu = sdf.format(tglWaktuLahir.getDate());
            String waktuSHK = sdf.format(tglSHKWaktu.getDate());
            
            ps = koneksi.prepareStatement("INSERT INTO pku_eklaim_kelahiran (no_sep, waktu_kelahiran, cara_kelahiran, letak_presentasi, kondisi, shk_pengambilan, shk_lokasi, shk_waktu, manual_aid, forcep, vacuum, alasan_tidak_shk) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)");
            try {
                ps.setString(1, no_sep);
                ps.setString(2, waktu);
                ps.setInt(3, cbCara.getSelectedIndex() + 1);
                ps.setInt(4, cbLetak.getSelectedIndex() + 1);
                ps.setInt(5, cbKondisi.getSelectedIndex() + 1);
                ps.setInt(6, cbSHKAmbil.getSelectedIndex() + 1);
                ps.setInt(7, cbSHKLokasi.getSelectedIndex() + 1);
                ps.setString(8, waktuSHK);
                ps.setInt(9, chkManual.isSelected() ? 1 : 0);
                ps.setInt(10, chkForcep.isSelected() ? 1 : 0);
                ps.setInt(11, chkVacuum.isSelected() ? 1 : 0);
                ps.setInt(12, cbAlasanSHK.getSelectedIndex());
                ps.executeUpdate();
            } finally {
                if (ps != null) ps.close();
            }
            tampilData();
        } catch (Exception e) {
            System.out.println("Notif Tambah Bayi : " + e);
            JOptionPane.showMessageDialog(null, "Gagal menambahkan data bayi:\n" + e.getMessage());
        }
    }
    
    private void hapusBayi() {
        if (tbKelahiran.getSelectedRow() != -1) {
            String id = tbKelahiran.getValueAt(tbKelahiran.getSelectedRow(), 0).toString();
            try {
                ps = koneksi.prepareStatement("DELETE FROM pku_eklaim_kelahiran WHERE id=?");
                try {
                    ps.setString(1, id);
                    ps.executeUpdate();
                } finally {
                    if (ps != null) ps.close();
                }
                tampilData();
            } catch (Exception e) {
                System.out.println("Notif Hapus Bayi : " + e);
                JOptionPane.showMessageDialog(null, "Gagal menghapus data bayi:\n" + e.getMessage());
            }
        } else {
            JOptionPane.showMessageDialog(null, "Pilih data bayi di tabel terlebih dahulu!");
        }
    }
    
    private void simpanKehamilanLokal() throws Exception {
        if (no_sep.isEmpty()) return;
        
        ps = koneksi.prepareStatement("INSERT INTO pku_eklaim_kehamilan (no_sep, usia_kehamilan, gravida, partus, abortus, onset_kontraksi) VALUES (?,?,?,?,?,?) ON DUPLICATE KEY UPDATE usia_kehamilan=VALUES(usia_kehamilan), gravida=VALUES(gravida), partus=VALUES(partus), abortus=VALUES(abortus), onset_kontraksi=VALUES(onset_kontraksi)");
        try {
            ps.setString(1, no_sep);
            ps.setInt(2, Integer.parseInt(tfUsiaKehamilan.getText().trim().isEmpty() ? "0" : tfUsiaKehamilan.getText().trim()));
            ps.setInt(3, Integer.parseInt(tfGravida.getText().trim().isEmpty() ? "0" : tfGravida.getText().trim()));
            ps.setInt(4, Integer.parseInt(tfPartus.getText().trim().isEmpty() ? "0" : tfPartus.getText().trim()));
            ps.setInt(5, Integer.parseInt(tfAbortus.getText().trim().isEmpty() ? "0" : tfAbortus.getText().trim()));
            ps.setInt(6, cbOnset.getSelectedIndex() + 1);
            ps.executeUpdate();
        } finally {
            if (ps != null) ps.close();
        }
    }
    
    private void simpanData() {
        if (no_sep.isEmpty()) return;
        try {
            simpanKehamilanLokal();
            JOptionPane.showMessageDialog(null, "Data Kehamilan Berhasil Disimpan Lokal!");
        } catch (Exception e) {
            System.out.println("Notif Simpan Kehamilan : " + e);
            JOptionPane.showMessageDialog(null, "Gagal menyimpan data kehamilan:\n" + e.getMessage());
        }
    }
}
