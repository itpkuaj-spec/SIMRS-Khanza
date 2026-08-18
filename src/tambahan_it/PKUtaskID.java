
/*
 * Kontribusi Oleh Ferry Ardiansyah - RSIAP 3326051
 *
 * Created on May 22, 2010, 11:58:21 PM
 */

package tambahan_it;
import bridging.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.RowSorter;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

/**
 *
 * @author dosen
 */
public final class PKUtaskID extends javax.swing.JDialog {
    private final DefaultTableModel tabMode;
    private validasi Valid=new validasi();
    private Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private PreparedStatement ps,ps2;//tambahan
    private ResultSet rs,rs2;    //tambahan
    private int i=0,tot_belum=0,tot_selesai=0,jkn_capaian_angka=0,mjkn_capaian_angka=0;
    private double jkn_capaian,mjkn_capaian,jkn_belum,jkn_selesai,mjkn_belum,mjkn_selesai,umum_belum,umum_selesai,sep;
    private ApiMobileJKN api=new ApiMobileJKN();
    private String URL="",link="",utc="",requestJson="",datajam="",datajam2="",kodebooking="",data="",
              nol_jam = "",nol_menit = "",nol_detik = "",jam="",menit="",detik="",hari="",norujukan="",status="1",noresep="",jensiracikan="",
              kodepoli="",kodedokter="",kodebpjs=Sequel.cariIsi("select password_asuransi.kd_pj from password_asuransi");//disini juga
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
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private volatile boolean ceksukses = false;

    /** Creates new form DlgJnsPerawatanRalan
     * @param parent
     * @param modal */
    public PKUtaskID(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
                "No. Rawat","No. RM","Nama Pasien","Nama Dokter","Poliklinik","No. SEP","Tanggal","Sumber Antrean","Checkin","SOAP","Resep","Status Periksa","Status Task ID","Add","Task 1","Task 2","Task 3","Task 4","Task 5","Task 6","Task 7","Task 99"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbJnsPerawatan.setModel(tabMode);

        tbJnsPerawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbJnsPerawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 22; i++) {
            TableColumn column = tbJnsPerawatan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(110);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(170);
            }else if(i==3){
                column.setPreferredWidth(170);
            }else if(i==4){
                column.setPreferredWidth(140);
            }else if(i==5){
                column.setPreferredWidth(150);
            }else if(i==6){
                column.setPreferredWidth(80);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(80);
            }else if(i==9){
                column.setPreferredWidth(60);
            }else if(i==10){
                column.setPreferredWidth(60);
            }else if(i==11){
                column.setPreferredWidth(100);
            }else if(i==12){
                column.setPreferredWidth(110);
            }else if(i==13){
                column.setPreferredWidth(70);
            }else if(i>=14 && i<=21){
                column.setPreferredWidth(70);
            }
        }
        tbJnsPerawatan.setDefaultRenderer(Object.class, new WarnaTableCustom());
        
        try {
            link=koneksiDB.URLAPIMOBILEJKN();
        } catch (Exception e) {
            System.out.println("E : "+e);
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

        jPopupMenu1 = new javax.swing.JPopupMenu();
        MnCekKodeBooking = new javax.swing.JMenuItem();
        MnKirimUlangMJKN = new javax.swing.JMenuItem();
        MnKirimUlangJKN = new javax.swing.JMenuItem();
        MnKirimBatalBPJS = new javax.swing.JMenuItem();
        internalFrame1 = new widget.InternalFrame();
        Scroll = new widget.ScrollPane();
        tbJnsPerawatan = new widget.Table();
        jPanel2 = new javax.swing.JPanel();
        panelGlass9 = new widget.panelisi();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        BtnCari = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        BtnKeluar1 = new widget.Button();
        panelGlass8 = new widget.panelisi();
        lblsep = new widget.Label();
        lbllengkap = new widget.Label();
        lbltidaklengkap = new widget.Label();
        lblbatal = new widget.Label();
        lbltotalpasien = new widget.Label();
        lblmjkn = new widget.Label();
        lblnonnkjn = new widget.Label();
        totalantrol = new widget.Label();
        lbltaskid = new widget.Label();
        lbltunggupoli = new widget.Label();
        lbltunggurs = new widget.Label();

        jPopupMenu1.setName("jPopupMenu1"); // NOI18N

        MnCekKodeBooking.setBackground(new java.awt.Color(255, 255, 254));
        MnCekKodeBooking.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnCekKodeBooking.setForeground(new java.awt.Color(50, 50, 50));
        MnCekKodeBooking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnCekKodeBooking.setText("Cek Kode Booking");
        MnCekKodeBooking.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnCekKodeBooking.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnCekKodeBooking.setName("MnCekKodeBooking"); // NOI18N
        MnCekKodeBooking.setPreferredSize(new java.awt.Dimension(160, 26));
        MnCekKodeBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnCekKodeBookingActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnCekKodeBooking);

        MnKirimUlangMJKN.setBackground(new java.awt.Color(255, 255, 254));
        MnKirimUlangMJKN.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnKirimUlangMJKN.setForeground(new java.awt.Color(50, 50, 50));
        MnKirimUlangMJKN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnKirimUlangMJKN.setText("Kirim Ulang Antrean MJKN");
        MnKirimUlangMJKN.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnKirimUlangMJKN.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnKirimUlangMJKN.setName("MnKirimUlangMJKN"); // NOI18N
        MnKirimUlangMJKN.setPreferredSize(new java.awt.Dimension(160, 26));
        MnKirimUlangMJKN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnKirimUlangMJKNActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnKirimUlangMJKN);

        MnKirimUlangJKN.setBackground(new java.awt.Color(255, 255, 254));
        MnKirimUlangJKN.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnKirimUlangJKN.setForeground(new java.awt.Color(50, 50, 50));
        MnKirimUlangJKN.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnKirimUlangJKN.setText("Kirim Ulang Antrean JKN");
        MnKirimUlangJKN.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnKirimUlangJKN.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnKirimUlangJKN.setName("MnKirimUlangJKN"); // NOI18N
        MnKirimUlangJKN.setPreferredSize(new java.awt.Dimension(160, 26));
        MnKirimUlangJKN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnKirimUlangJKNActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnKirimUlangJKN);

        MnKirimBatalBPJS.setBackground(new java.awt.Color(255, 255, 254));
        MnKirimBatalBPJS.setFont(new java.awt.Font("Tahoma", 0, 11)); // NOI18N
        MnKirimBatalBPJS.setForeground(new java.awt.Color(50, 50, 50));
        MnKirimBatalBPJS.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/category.png"))); // NOI18N
        MnKirimBatalBPJS.setText("Kirim Servis Batal");
        MnKirimBatalBPJS.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MnKirimBatalBPJS.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        MnKirimBatalBPJS.setName("MnKirimBatalBPJS"); // NOI18N
        MnKirimBatalBPJS.setPreferredSize(new java.awt.Dimension(160, 26));
        MnKirimBatalBPJS.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MnKirimBatalBPJSActionPerformed(evt);
            }
        });
        jPopupMenu1.add(MnKirimBatalBPJS);

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Monitor PKU Aisyiyah task Id ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
        internalFrame1.setName("internalFrame1"); // NOI18N
        internalFrame1.setLayout(new java.awt.BorderLayout(1, 1));

        Scroll.setName("Scroll"); // NOI18N
        Scroll.setOpaque(true);

        tbJnsPerawatan.setToolTipText("Silahkan klik untuk memilih data yang mau diedit ataupun dihapus");
        tbJnsPerawatan.setComponentPopupMenu(jPopupMenu1);
        tbJnsPerawatan.setName("tbJnsPerawatan"); // NOI18N
        Scroll.setViewportView(tbJnsPerawatan);

        internalFrame1.add(Scroll, java.awt.BorderLayout.CENTER);

        jPanel2.setName("jPanel2"); // NOI18N
        jPanel2.setOpaque(false);
        jPanel2.setPreferredSize(new java.awt.Dimension(44, 150));
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(55, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "11-08-2026" }));
        DTPCari1.setDisplayFormat("dd-MM-yyyy");
        DTPCari1.setName("DTPCari1"); // NOI18N
        DTPCari1.setOpaque(false);
        DTPCari1.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari1);

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("s.d.");
        jLabel21.setName("jLabel21"); // NOI18N
        jLabel21.setPreferredSize(new java.awt.Dimension(23, 23));
        panelGlass9.add(jLabel21);

        DTPCari2.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "11-08-2026" }));
        DTPCari2.setDisplayFormat("dd-MM-yyyy");
        DTPCari2.setName("DTPCari2"); // NOI18N
        DTPCari2.setOpaque(false);
        DTPCari2.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass9.add(DTPCari2);

        BtnCari.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/accept.png"))); // NOI18N
        BtnCari.setMnemonic('2');
        BtnCari.setToolTipText("Alt+2");
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

        jLabel7.setText("Record :");
        jLabel7.setName("jLabel7"); // NOI18N
        jLabel7.setPreferredSize(new java.awt.Dimension(85, 23));
        panelGlass9.add(jLabel7);

        LCount.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LCount.setText("0");
        LCount.setName("LCount"); // NOI18N
        LCount.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass9.add(LCount);

        BtnKeluar1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/picture/exit.png"))); // NOI18N
        BtnKeluar1.setMnemonic('K');
        BtnKeluar1.setText("Keluar");
        BtnKeluar1.setToolTipText("Alt+K");
        BtnKeluar1.setName("BtnKeluar1"); // NOI18N
        BtnKeluar1.setPreferredSize(new java.awt.Dimension(100, 30));
        BtnKeluar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtnKeluar1ActionPerformed(evt);
            }
        });
        BtnKeluar1.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                BtnKeluar1KeyPressed(evt);
            }
        });
        panelGlass9.add(BtnKeluar1);

        jPanel2.add(panelGlass9, java.awt.BorderLayout.CENTER);

        panelGlass8.setName("panelGlass8"); // NOI18N
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 90));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        lblsep.setForeground(new java.awt.Color(0, 0, 0));
        lblsep.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblsep.setText("0");
        lblsep.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblsep.setName("lblsep"); // NOI18N
        lblsep.setPreferredSize(new java.awt.Dimension(150, 23));
        panelGlass8.add(lblsep);

        lbllengkap.setForeground(new java.awt.Color(0, 0, 0));
        lbllengkap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbllengkap.setText("0");
        lbllengkap.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbllengkap.setName("lbllengkap"); // NOI18N
        lbllengkap.setPreferredSize(new java.awt.Dimension(150, 23));
        panelGlass8.add(lbllengkap);

        lbltidaklengkap.setForeground(new java.awt.Color(0, 0, 0));
        lbltidaklengkap.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltidaklengkap.setText("0");
        lbltidaklengkap.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbltidaklengkap.setName("lbltidaklengkap"); // NOI18N
        lbltidaklengkap.setPreferredSize(new java.awt.Dimension(150, 23));
        panelGlass8.add(lbltidaklengkap);

        lblbatal.setForeground(new java.awt.Color(0, 0, 0));
        lblbatal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblbatal.setText("0");
        lblbatal.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblbatal.setName("lblbatal"); // NOI18N
        lblbatal.setPreferredSize(new java.awt.Dimension(150, 23));
        panelGlass8.add(lblbatal);

        lbltotalpasien.setForeground(new java.awt.Color(0, 0, 0));
        lbltotalpasien.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltotalpasien.setText("0");
        lbltotalpasien.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbltotalpasien.setName("lbltotalpasien"); // NOI18N
        lbltotalpasien.setPreferredSize(new java.awt.Dimension(150, 23));
        panelGlass8.add(lbltotalpasien);

        lblmjkn.setForeground(new java.awt.Color(0, 0, 0));
        lblmjkn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblmjkn.setText("0");
        lblmjkn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblmjkn.setName("lblmjkn"); // NOI18N
        lblmjkn.setPreferredSize(new java.awt.Dimension(150, 23));
        panelGlass8.add(lblmjkn);

        lblnonnkjn.setForeground(new java.awt.Color(0, 0, 0));
        lblnonnkjn.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblnonnkjn.setText("0");
        lblnonnkjn.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lblnonnkjn.setName("lblnonnkjn"); // NOI18N
        lblnonnkjn.setPreferredSize(new java.awt.Dimension(150, 23));
        panelGlass8.add(lblnonnkjn);

        totalantrol.setForeground(new java.awt.Color(0, 0, 0));
        totalantrol.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalantrol.setText("0");
        totalantrol.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        totalantrol.setName("totalantrol"); // NOI18N
        totalantrol.setPreferredSize(new java.awt.Dimension(190, 23));
        panelGlass8.add(totalantrol);

        lbltaskid.setForeground(new java.awt.Color(0, 0, 0));
        lbltaskid.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltaskid.setText("0");
        lbltaskid.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbltaskid.setName("lbltaskid"); // NOI18N
        lbltaskid.setPreferredSize(new java.awt.Dimension(550, 23));
        panelGlass8.add(lbltaskid);

        lbltunggupoli.setForeground(new java.awt.Color(0, 0, 0));
        lbltunggupoli.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltunggupoli.setText("0");
        lbltunggupoli.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbltunggupoli.setName("lbltunggupoli"); // NOI18N
        lbltunggupoli.setPreferredSize(new java.awt.Dimension(400, 23));
        panelGlass8.add(lbltunggupoli);

        lbltunggurs.setForeground(new java.awt.Color(0, 0, 0));
        lbltunggurs.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lbltunggurs.setText("0");
        lbltunggurs.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        lbltunggurs.setName("lbltunggurs"); // NOI18N
        lbltunggurs.setPreferredSize(new java.awt.Dimension(400, 23));
        panelGlass8.add(lbltunggurs);

        jPanel2.add(panelGlass8, java.awt.BorderLayout.PAGE_START);

        internalFrame1.add(jPanel2, java.awt.BorderLayout.PAGE_END);

        getContentPane().add(internalFrame1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BtnCariActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnCariActionPerformed
        emptTeks();
        runBackground(() ->tampil());
}//GEN-LAST:event_BtnCariActionPerformed

    private void BtnCariKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnCariKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            BtnCariActionPerformed(null);
        }
}//GEN-LAST:event_BtnCariKeyPressed

    private void BtnKeluar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtnKeluar1ActionPerformed
       dispose();
    }//GEN-LAST:event_BtnKeluar1ActionPerformed

    private void BtnKeluar1KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_BtnKeluar1KeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_SPACE){
            dispose();
        }
    }//GEN-LAST:event_BtnKeluar1KeyPressed

    private void MnCekKodeBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnCekKodeBookingActionPerformed
        if(tbJnsPerawatan.getSelectedRow()!= -1){
            this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            String noRawat = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 0).toString();
            String sumberAntrean = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 7).toString();
            String kodeBooking = noRawat;

            if (sumberAntrean.equals("Mobile JKN")) {
                String nobooking = Sequel.cariIsi("select referensi_mobilejkn_bpjs.nobooking from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.no_rawat=?", noRawat);
                if (!nobooking.trim().isEmpty()) {
                    kodeBooking = nobooking;
                }
            }

            BPJSCekKodeBooking detail=new BPJSCekKodeBooking(null,false);
            detail.tampil(kodeBooking);
            detail.setSize(internalFrame1.getWidth()-20,internalFrame1.getHeight()-20);
            detail.setLocationRelativeTo(internalFrame1);
            detail.setVisible(true);
            this.setCursor(Cursor.getDefaultCursor());
        }else{
            JOptionPane.showMessageDialog(null,"Maaf, silahkan pilih data yang mau dicek...!!!!");
            tbJnsPerawatan.requestFocus();
        }
    }//GEN-LAST:event_MnCekKodeBookingActionPerformed

    private void MnKirimUlangMJKNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnKirimUlangMJKNActionPerformed
        // TODO add your handling code here:
        KirimUlangMJKN();
    }//GEN-LAST:event_MnKirimUlangMJKNActionPerformed

    private void MnKirimUlangJKNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnKirimUlangJKNActionPerformed
        // TODO add your handling code here:
        KirimUlangJKN();
    }//GEN-LAST:event_MnKirimUlangJKNActionPerformed

    private void MnKirimBatalBPJSActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MnKirimBatalBPJSActionPerformed
        // TODO add your handling code here:
        BtnBatalMJKNActionPerformed(evt);
    }//GEN-LAST:event_MnKirimBatalBPJSActionPerformed




    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            PKUtaskID dialog = new PKUtaskID(new javax.swing.JFrame(), true);
            dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                @Override
                public void windowClosing(java.awt.event.WindowEvent e) {
                    System.exit(0);
                }
            });
            dialog.setVisible(true);
        });
    }
    
    
    private void emptTeks(){
        sep = 0;
        tot_belum = 0;
        tot_selesai = 0;
        jkn_belum = 0;
        jkn_selesai = 0;
        mjkn_belum = 0;
        mjkn_selesai = 0;
        umum_belum = 0;
        umum_selesai = 0;
        jkn_capaian = 0;
        mjkn_capaian = 0;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private widget.Button BtnCari;
    private widget.Button BtnKeluar1;
    private widget.Tanggal DTPCari1;
    private widget.Tanggal DTPCari2;
    private widget.Label LCount;
    private javax.swing.JMenuItem MnCekKodeBooking;
    private javax.swing.JMenuItem MnKirimBatalBPJS;
    private javax.swing.JMenuItem MnKirimUlangJKN;
    private javax.swing.JMenuItem MnKirimUlangMJKN;
    private widget.ScrollPane Scroll;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel7;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.Label lblbatal;
    private widget.Label lbllengkap;
    private widget.Label lblmjkn;
    private widget.Label lblnonnkjn;
    private widget.Label lblsep;
    private widget.Label lbltaskid;
    private widget.Label lbltidaklengkap;
    private widget.Label lbltotalpasien;
    private widget.Label lbltunggupoli;
    private widget.Label lbltunggurs;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbJnsPerawatan;
    private widget.Label totalantrol;
    // End of variables declaration//GEN-END:variables

    private class WarnaTableCustom extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            Object statusTaskId = table.getValueAt(row, 12);
            if (statusTaskId != null && statusTaskId.toString().equals("TIDAK LENGKAP")) {
                component.setBackground(new Color(255, 215, 215)); // Merah soft
            } else if (statusTaskId != null && statusTaskId.toString().equals("BATAL")) {
                component.setBackground(new Color(255, 255, 190)); // Kuning soft
            } else {
                // Standar Biru Putih Khanza
                if (row % 2 == 1) {
                    component.setBackground(new Color(195, 240, 255)); // Biru muda
                } else {
                    component.setBackground(new Color(255, 255, 255)); // Putih
                }
            }

            component.setForeground(new Color(50, 50, 50)); // Font tetap hitam

            if (isSelected) {
                component.setBackground(new Color(245, 200, 75)); // Seleksi baris
                component.setForeground(new Color(0, 0, 0));
            }

            return component;
        }
    }

    private void tampil() {
        Valid.tabelKosong(tabMode);
        int tot_batal = 0;
        int tot_sep = 0;
        int tot_mjkn = 0;
        int tot_nonmjkn = 0;
        tot_belum = 0;
        tot_selesai = 0;
        jkn_belum = 0;
        jkn_selesai = 0;
        mjkn_belum = 0;
        mjkn_selesai = 0;
        mjkn_selesai = 0;
        umum_belum = 0;
        umum_selesai = 0;
        
        int missingT1 = 0, missingT2 = 0, missingT3 = 0, missingT4 = 0, missingT5 = 0, missingT6 = 0, missingT7 = 0;
        long totalWaktuPoli = 0;
        int countPoli = 0;
        long totalWaktuRS = 0;
        int countRS = 0;

        try {
            ps = koneksi.prepareStatement(
                "SELECT r.no_rawat, r.no_rkm_medis, p.nm_pasien, IFNULL(d.nm_dokter, '-') AS nm_dokter, IFNULL(poli.nm_poli, '-') AS nm_poli, IFNULL(sep.no_sep, '-') AS no_sep, r.tgl_registrasi, " +
                "IF(m.no_rawat IS NOT NULL, 'Mobile JKN', 'Non Mobile JKN') AS sumber_antrean, IFNULL(m.status, '-') AS status_checkin, IF(COUNT(pem.no_rawat) > 0, 'Ada', '-') AS stts_soap, IF(COUNT(res.no_rawat) > 0, 'Ada', '-') AS stts_resep, r.stts AS status_periksa, " +
                "IFNULL(MAX(CASE WHEN amp.tambah <> '0000-00-00 00:00:00' THEN DATE_FORMAT(amp.tambah, '%H:%i:%s') END), '-') AS stts_add, " +
                "MAX(CASE WHEN t.taskid = '1' THEN DATE_FORMAT(t.waktu, '%H:%i:%s') END) AS task1, " +
                "MAX(CASE WHEN t.taskid = '2' THEN DATE_FORMAT(t.waktu, '%H:%i:%s') END) AS task2, " +
                "MAX(CASE WHEN t.taskid = '3' THEN DATE_FORMAT(t.waktu, '%H:%i:%s') END) AS task3, " +
                "MAX(CASE WHEN t.taskid = '4' THEN DATE_FORMAT(t.waktu, '%H:%i:%s') END) AS task4, " +
                "MAX(CASE WHEN t.taskid = '5' THEN DATE_FORMAT(t.waktu, '%H:%i:%s') END) AS task5, " +
                "MAX(CASE WHEN t.taskid = '6' THEN DATE_FORMAT(t.waktu, '%H:%i:%s') END) AS task6, " +
                "MAX(CASE WHEN t.taskid = '7' THEN DATE_FORMAT(t.waktu, '%H:%i:%s') END) AS task7, " +
                "MAX(CASE WHEN t.taskid = '99' THEN DATE_FORMAT(t.waktu, '%H:%i:%s') END) AS task99 " +
                "FROM reg_periksa r " +
                "INNER JOIN pasien p ON r.no_rkm_medis = p.no_rkm_medis " +
                "LEFT JOIN dokter d ON r.kd_dokter = d.kd_dokter " +
                "LEFT JOIN poliklinik poli ON r.kd_poli = poli.kd_poli " +
                "LEFT JOIN bridging_sep sep ON r.no_rawat = sep.no_rawat AND sep.jnspelayanan = '2' " +
                "LEFT JOIN pemeriksaan_ralan pem ON r.no_rawat = pem.no_rawat " +
                "LEFT JOIN resep_obat res ON r.no_rawat = res.no_rawat " +
                "LEFT JOIN antri_masuk_poli amp ON r.no_rawat = amp.no_rawat " +
                "INNER JOIN maping_poli_bpjs mp ON r.kd_poli = mp.kd_poli_rs " +
                "INNER JOIN maping_dokter_dpjpvclaim md ON r.kd_dokter = md.kd_dokter " +
                "INNER JOIN jadwal j ON (j.hari_kerja = (CASE DAYOFWEEK(r.tgl_registrasi) WHEN 1 THEN 'AKHAD' WHEN 2 THEN 'SENIN' WHEN 3 THEN 'SELASA' WHEN 4 THEN 'RABU' WHEN 5 THEN 'KAMIS' WHEN 6 THEN 'JUMAT' WHEN 7 THEN 'SABTU' END) AND j.kd_dokter = r.kd_dokter AND j.kd_poli = r.kd_poli) " +
                "LEFT JOIN referensi_mobilejkn_bpjs m ON r.no_rawat = m.no_rawat " +
                "LEFT JOIN referensi_mobilejkn_bpjs_taskid t ON r.no_rawat = t.no_rawat " +
                "WHERE r.tgl_registrasi BETWEEN ? AND ? " +
                "GROUP BY r.no_rawat, r.no_rkm_medis, p.nm_pasien, d.nm_dokter, poli.nm_poli, sep.no_sep, r.tgl_registrasi, m.no_rawat, m.status, r.stts " +
                "ORDER BY r.tgl_registrasi ASC, r.no_rawat ASC"
            );
            try {
                ps.setString(1, Valid.SetTgl(DTPCari1.getSelectedItem() + ""));
                ps.setString(2, Valid.SetTgl(DTPCari2.getSelectedItem() + ""));
                rs = ps.executeQuery();
                while (rs.next()) {
                    String noRawat = rs.getString("no_rawat");
                    String noRm = rs.getString("no_rkm_medis");
                    String nmPasien = rs.getString("nm_pasien");
                    String nmDokter = rs.getString("nm_dokter");
                    String nmPoli = rs.getString("nm_poli");
                    String noSep = rs.getString("no_sep");
                    String tglReg = rs.getString("tgl_registrasi");
                    String sumber = rs.getString("sumber_antrean");
                    String statusCheckin = rs.getString("status_checkin") == null || rs.getString("status_checkin").equals("") ? "-" : rs.getString("status_checkin");
                    String sttsSoap = rs.getString("stts_soap");
                    String sttsResep = rs.getString("stts_resep");
                    String sttsPeriksa = rs.getString("status_periksa");
                    String sttsAdd = rs.getString("stts_add") == null ? "-" : rs.getString("stts_add");
                    String t1 = rs.getString("task1") == null ? "-" : rs.getString("task1");
                    String t2 = rs.getString("task2") == null ? "-" : rs.getString("task2");
                    String t3 = rs.getString("task3") == null ? "-" : rs.getString("task3");
                    String t4 = rs.getString("task4") == null ? "-" : rs.getString("task4");
                    String t5 = rs.getString("task5") == null ? "-" : rs.getString("task5");
                    String t6 = rs.getString("task6") == null ? "-" : rs.getString("task6");
                    String t7 = rs.getString("task7") == null ? "-" : rs.getString("task7");
                    String t99 = rs.getString("task99") == null ? "-" : rs.getString("task99");

                    if (!noSep.equals("-") && !noSep.trim().isEmpty()) {
                        tot_sep++;
                    }

                    if (sumber.equals("Mobile JKN")) {
                        tot_mjkn++;
                    } else {
                        tot_nonmjkn++;
                    }

                    String statusTaskId = "TIDAK LENGKAP";
                    if (!t99.equals("-")) {
                        statusTaskId = "BATAL";
                    } else if (sumber.equals("Mobile JKN")) {
                        if (!t3.equals("-") && !t4.equals("-") && !t5.equals("-")) {
                            if (!t6.equals("-") && t7.equals("-")) {
                                statusTaskId = "TIDAK LENGKAP";
                            } else {
                                statusTaskId = "LENGKAP";
                            }
                        } else {
                            statusTaskId = "TIDAK LENGKAP";
                        }
                    } else {
                        // Non Mobile JKN
                        if (!t3.equals("-") && !t4.equals("-") && !t5.equals("-")) {
                            if (!t6.equals("-") && t7.equals("-")) {
                                statusTaskId = "TIDAK LENGKAP";
                            } else {
                                statusTaskId = "LENGKAP";
                            }
                        } else {
                            statusTaskId = "TIDAK LENGKAP";
                        }
                    }
                    
                    // Hitung task id yang kosong (belum terkirim) khusus untuk pasien yang tidak dibatalkan (bukan task 99)
                    if (t99.equals("-")) {
                        if (t1.equals("-")) missingT1++;
                        if (t2.equals("-")) missingT2++;
                        if (t3.equals("-")) missingT3++;
                        if (t4.equals("-")) missingT4++;
                        if (t5.equals("-")) missingT5++;
                        // Task 6 dan 7 hanya wajib jika pasien memiliki resep obat
                        if (sttsResep.equals("Ada")) {
                            if (t6.equals("-")) missingT6++;
                            if (t7.equals("-")) missingT7++;
                        }
                    }

                    if (statusTaskId.equals("LENGKAP")) {
                        tot_selesai++;
                        if (sumber.equals("Mobile JKN")) {
                            mjkn_selesai++;
                        } else {
                            umum_selesai++;
                        }
                    } else if (statusTaskId.equals("BATAL")) {
                        tot_batal++;
                    } else {
                        tot_belum++;
                        if (sumber.equals("Mobile JKN")) {
                            mjkn_belum++;
                        } else {
                            umum_belum++;
                        }
                    }
                    
                    // Hitung waktu tunggu poli (Task 4 - Task 2) & waktu tunggu RS
                    if (t99.equals("-")) { // Jika tidak batal
                        if (!t2.equals("-") && !t4.equals("-")) {
                            try {
                                java.util.Date d2 = dateFormat.parse(tglReg + " " + t2);
                                java.util.Date d4 = dateFormat.parse(tglReg + " " + t4);
                                long diff = d4.getTime() - d2.getTime();
                                if (diff < 0) diff += 24 * 60 * 60 * 1000; // handle beda hari
                                totalWaktuPoli += diff;
                                countPoli++;
                            } catch (Exception ex) {}
                        }
                        
                        // Hitung waktu tunggu RS
                        if (sttsResep.equals("Ada")) {
                            if (!t1.equals("-") && !t7.equals("-")) {
                                try {
                                    java.util.Date d1 = dateFormat.parse(tglReg + " " + t1);
                                    java.util.Date d7 = dateFormat.parse(tglReg + " " + t7);
                                    long diff = d7.getTime() - d1.getTime();
                                    if (diff < 0) diff += 24 * 60 * 60 * 1000;
                                    totalWaktuRS += diff;
                                    countRS++;
                                } catch (Exception ex) {}
                            }
                        } else {
                            if (!t1.equals("-") && !t5.equals("-")) {
                                try {
                                    java.util.Date d1 = dateFormat.parse(tglReg + " " + t1);
                                    java.util.Date d5 = dateFormat.parse(tglReg + " " + t5);
                                    long diff = d5.getTime() - d1.getTime();
                                    if (diff < 0) diff += 24 * 60 * 60 * 1000;
                                    totalWaktuRS += diff;
                                    countRS++;
                                } catch (Exception ex) {}
                            }
                        }
                    }

                    tabMode.addRow(new Object[]{
                        noRawat, noRm, nmPasien, nmDokter, nmPoli, noSep, tglReg, sumber, statusCheckin, sttsSoap, sttsResep, sttsPeriksa,
                        statusTaskId, sttsAdd, t1, t2, t3, t4, t5, t6, t7, t99
                    });
                }
            } catch (Exception e) {
                System.out.println("Error query tampil: " + e);
            } finally {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            }
        } catch (Exception e) {
            System.out.println("Notifikasi : " + e);
        }

        int totalPasienValid = tot_selesai + tot_belum;
        double persenLengkap = (totalPasienValid == 0) ? 0 : ((double) tot_selesai / totalPasienValid) * 100;

        LCount.setText("" + tabMode.getRowCount());
        totalantrol.setText("Capaian Antrol: " + Valid.SetAngka(persenLengkap) + "%");
        lbllengkap.setText("Lengkap: " + tot_selesai);
        lbltidaklengkap.setText("Tdk Lengkap: " + tot_belum);
        lblbatal.setText("Batal: " + tot_batal);
        lbltotalpasien.setText("Total Pasien: " + tabMode.getRowCount());
        lblsep.setText("SEP Terbit: " + tot_sep);
        lblmjkn.setText("Mobile JKN: " + tot_mjkn);
        lblnonnkjn.setText("Non Mobile JKN: " + tot_nonmjkn);
        
        // Tampilkan jumlah task id yang belum terkirim ke lbltaskid
        lbltaskid.setText("Belum Terkirim: T1(" + missingT1 + ") T2(" + missingT2 + ") T3(" + missingT3 + ") T4(" + missingT4 + ") T5(" + missingT5 + ") T6(" + missingT6 + ") T7(" + missingT7 + ")");

        // Tampilkan Rata-rata Waktu Tunggu
        if (countPoli > 0) {
            long avgPoli = totalWaktuPoli / countPoli;
            long s = avgPoli / 1000;
            String avgPoliStr = String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
            lbltunggupoli.setText("Rata-rata Tunggu Poli : " + avgPoliStr);
            if (s > 3600) {
                lbltunggupoli.setForeground(Color.RED);
            } else {
                lbltunggupoli.setForeground(new Color(0, 153, 102)); // hijau default
            }
        } else {
            lbltunggupoli.setText("Rata-rata Tunggu Poli : 00:00:00");
            lbltunggupoli.setForeground(new Color(0, 153, 102));
        }
        
        if (countRS > 0) {
            long avgRS = totalWaktuRS / countRS;
            long s = avgRS / 1000;
            String avgRSStr = String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
            lbltunggurs.setText("Rata-rata Pasien di RS : " + avgRSStr);
        } else {
            lbltunggurs.setText("Rata-rata Pasien di RS : 00:00:00");
        }

        if (persenLengkap < 97) {
            totalantrol.setForeground(Color.RED);
        } else {
            totalantrol.setForeground(new Color(0, 128, 0));
        }
    }
    //tambahan
    
    private void BtnBatalMJKNActionPerformed(java.awt.event.ActionEvent evt) {
        if (tbJnsPerawatan.getSelectedRow() != -1) {
            // Konfirmasi pembatalan
            int pilihan = JOptionPane.showConfirmDialog(null,
                "Apakah Anda yakin ingin membatalkan antrean ini?",
                "Konfirmasi Pembatalan",
                JOptionPane.YES_NO_OPTION);
            if (pilihan == JOptionPane.YES_OPTION) {
                // Ambil no_rawat dan sumber antrean dari tabel
                String noRawat = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 0).toString();
                String sumberData = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 7).toString();

                // Input keterangan pembatalan
                String keterangan = JOptionPane.showInputDialog(null,
                    "Masukkan keterangan pembatalan:",
                    "Keterangan Pembatalan",
                    JOptionPane.QUESTION_MESSAGE);

                if (keterangan != null && !keterangan.trim().isEmpty()) {
                    this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                    try {
                        // Setup headers
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                        utc = String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp", utc);
                        headers.add("x-signature", api.getHmac(utc));
                        headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                        // Tentukan kode booking yang akan dikirim ke BPJS
                        String kodeBookingRequest = noRawat;
                        String carinobooking = Sequel.cariIsi("select nobooking from referensi_mobilejkn_bpjs where no_rawat=?", noRawat);
                        if (!carinobooking.isEmpty()) {
                            kodeBookingRequest = carinobooking;
                        }

                        // Buat request JSON
                        requestJson = "{"
                            + "\"kodebooking\": \"" + kodeBookingRequest + "\","
                            + "\"keterangan\": \"" + keterangan.trim() + "\""
                            + "}";

                        requestEntity = new HttpEntity(requestJson, headers);
                        URL = link + "/antrean/batal";
                        System.out.println("URL Batal: " + URL);
                        System.out.println("Request: " + requestJson);

                        // Kirim request ke server BPJS
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");

                        if (nameNode.path("code").asText().equals("200")) {
                            JOptionPane.showMessageDialog(null, "Antrean berhasil dibatalkan!\n" + nameNode.path("message").asText());

                            // Update status ke database lokal
                            try {
                                Sequel.queryu2("update referensi_mobilejkn_bpjs set status='Batal' where no_rawat='" + noRawat + "' or nobooking='" + kodeBookingRequest + "'");
                                
                                String now = Sequel.cariIsi("select NOW()");
                                if (!Sequel.cariIsi("select count(*) from referensi_mobilejkn_bpjs_taskid where no_rawat=? and taskid='99'", noRawat).equals("0")) {
                                    Sequel.queryu2("update referensi_mobilejkn_bpjs_taskid set waktu='" + now + "' where no_rawat='" + noRawat + "' and taskid='99'");
                                } else {
                                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid(no_rawat, taskid, waktu) values('" + noRawat + "', '99', '" + now + "')");
                                }
                                System.out.println("Update status Batal & Task ID 99 sukses untuk no_rawat: " + noRawat);
                            } catch (Exception e) {
                                System.out.println("Error update database lokal: " + e);
                            }

                            // Sync list task dari cloud BPJS & Refresh tampilan
                            syncListTaskBPJS(noRawat, kodeBookingRequest);
                            tampil();
                        } else {
                            JOptionPane.showMessageDialog(null,
                                "Gagal membatalkan antrean!\n"
                                + "Kode: " + nameNode.path("code").asText() + "\n"
                                + "Pesan: " + nameNode.path("message").asText(),
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        }

                    } catch (Exception ex) {
                        System.out.println("Error membatalkan antrean: " + ex);
                        String errorMessage = "Gagal membatalkan antrean: " + ex.getMessage();
                        if (ex.toString().contains("UnknownHostException")) {
                            errorMessage = "Koneksi ke server BPJS terputus!";
                        } else if (ex.toString().contains("SocketTimeoutException")) {
                            errorMessage = "Timeout koneksi ke server BPJS!";
                        }

                        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
                    } finally {
                        this.setCursor(Cursor.getDefaultCursor());
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Keterangan pembatalan tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "Silahkan pilih data antrean yang akan dibatalkan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            tbJnsPerawatan.requestFocus();
        }
    }
    
    private void syncListTaskBPJS(String noRawat, String kodeBooking) {
        if (kodeBooking == null || kodeBooking.trim().isEmpty()) {
            kodeBooking = noRawat;
        }
        try {
            headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
            utc = String.valueOf(api.GetUTCdatetimeAsString());
            headers.add("x-timestamp", utc);
            headers.add("x-signature", api.getHmac(utc));
            headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
            requestJson = "{\"kodebooking\": \"" + kodeBooking + "\"}";
            requestEntity = new HttpEntity(requestJson, headers);
            URL = link + "/antrean/getlisttask";
            root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
            if (root.path("metadata").path("code").asText().equals("200")) {
                response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                JsonNode arrayNode = response;
                if (response.isObject() && response.has("list")) {
                    arrayNode = response.path("list");
                }
                if (arrayNode.isArray()) {
                    for (JsonNode listNode : arrayNode) {
                        String tid = listNode.path("taskid").asText();
                        String wktStr = listNode.path("wakturs").asText();
                        if (wktStr.equals("") || wktStr.equals("null")) {
                            wktStr = listNode.path("waktuRS").asText();
                        }
                        if (wktStr.equals("") || wktStr.equals("null")) {
                            wktStr = listNode.path("waktu").asText();
                        }
                        if (!wktStr.equals("") && !wktStr.equals("null")) {
                            try {
                                String cleanTime = wktStr.replaceAll(" WIB| WITA| WIT", "");
                                Date dTask;
                                if (cleanTime.contains("-")) {
                                    if (cleanTime.indexOf("-") == 4) {
                                        dTask = dateFormat.parse(cleanTime);
                                    } else {
                                        dTask = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse(cleanTime);
                                    }
                                } else {
                                    dTask = dateFormat.parse(cleanTime);
                                }
                                String formattedTime = dateFormat.format(dTask);
                                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='" + tid + "' and no_rawat='" + noRawat + "'") == 0) {
                                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + noRawat + "','" + tid + "','" + formattedTime + "')");
                                }
                            } catch (Exception ex2) {
                                System.out.println("Error parse task " + tid + " : " + ex2);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Info syncListTaskBPJS: " + e);
        }
    }

    private void KirimUlangMJKN() {                                             
        // TODO add your handling code here: JKN
        String norawat = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 0).toString();
        String carinobooking = Sequel.cariIsi(
            "select referensi_mobilejkn_bpjs.nobooking from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.no_rawat=?", norawat);
        data = carinobooking;
        String no_rawat = norawat;

        if (carinobooking == null || carinobooking.equals("")) {
            System.out.println("GAGAL: nobooking tidak ditemukan di referensi_mobilejkn_bpjs untuk no_rawat=" + norawat + ", proses dibatalkan\n");
            return;
        }

        // 1. Tarik & simpan task ID yang sudah ada di BPJS cloud
        syncListTaskBPJS(no_rawat, data);

        // ---------- TASK ID 99 (batal pelayanan) ----------
        datajam = Sequel.cariIsi("select now() from reg_periksa where reg_periksa.stts='Batal' and reg_periksa.no_rawat=?", no_rawat);
        if (datajam != null && !datajam.equals("")) {
            try {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid batal pelayanan poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"99\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='99' and no_rawat='" + no_rawat + "'");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }
        
                        // ---------- TASK ID 1 (admisi admisi) ----------
        datajam = Sequel.cariIsi("select referensi_mobilejkn_bpjs.validasi from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.no_rawat = '" + no_rawat + "'");    
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='1' and no_rawat='" + no_rawat + "'") > 0) {
                    System.out.println("TaskId=1 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid mulai tunggu poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"1\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='1' and no_rawat='" + no_rawat + "'");
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','1','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 1 MJKN = BELUM add adtrian, SKIP KIRIM " + data + " (no_rawat=" + no_rawat + ")");
        }
                // ---------- TASK ID 2 (selesai admisi) ----------
        datajam = Sequel.cariIsi("select DATE_ADD(referensi_mobilejkn_bpjs.validasi, INTERVAL FLOOR(1 + RAND() * 2) MINUTE) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.no_rawat='" + no_rawat + "'");    
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='2' and no_rawat='" + no_rawat + "'") > 0) {
                    System.out.println("TaskId=2 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid mulai tunggu poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"2\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='2' and no_rawat='" + no_rawat + "'");
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','2','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 2 MJKN = BELUM ada TASK ID 1, SKIP KIRIM " + data + " (no_rawat=" + no_rawat + ")");
        }

        // ---------- TASK ID 3 (mulai tunggu poli) ----------
//        datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT MAX(waktu) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + no_rawat + "' AND taskid='3'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + no_rawat + "')), INTERVAL (300 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
        datajam = Sequel.cariIsi("select soap from antri_masuk_poli where no_rawat='" + no_rawat + "'");    
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='" + no_rawat + "'") > 0) {
                    System.out.println("TaskId=3 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid mulai tunggu poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"3\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='" + no_rawat + "'");
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','3','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 3 MJKN = BELUM MENGISI SOAP PERAWAT, SKIP KIRIM " + data + " (no_rawat=" + no_rawat + ")");
        }
        // ---------- TASK ID 4 (mulai pelayanan poli) ----------
            datajam = Sequel.cariIsi("select antri_masuk_poli.tgl_jam from antri_masuk_poli where antri_masuk_poli.no_rawat = '" + no_rawat + "'");
            boolean butuhFallback4 = false;
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + no_rawat + "'") > 0) {
                    System.out.println("TaskId=4 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid mulai pelayanan poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"4\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + no_rawat + "'");
                    String msg = nameNode.path("message").asText();
                    if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                        butuhFallback4 = true;
                    }
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','4','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 4 MJKN = Belum ada paggil masuk poli atau waktu tidak ada di DB. Mengaktifkan Fallback BPJS...");
            butuhFallback4 = true;
        }

        if (butuhFallback4) {
            System.out.println("Menggunakan Fallback Task ID 3 dari BPJS untuk Task ID 4...");
            String task3Time = "";
            try {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{\"kodebooking\": \"" + data + "\"}";
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/getlisttask";
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (nameNode.path("code").asText().equals("200")) {
                    response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                    JsonNode arrayNode = response;
                    if (response.isObject() && response.has("list")) {
                        arrayNode = response.path("list");
                    }
                    if (arrayNode.isArray()) {
                        for (JsonNode listNode : arrayNode) {
                            if (listNode.path("taskid").asText().equals("3") || listNode.path("taskid").asInt() == 3) {
                                task3Time = listNode.path("wakturs").asText();
                                if (task3Time.equals("") || task3Time.equals("null")) {
                                    task3Time = listNode.path("waktuRS").asText();
                                }
                                if (task3Time.equals("") || task3Time.equals("null")) {
                                    task3Time = listNode.path("waktu").asText();
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Gagal menarik getlisttask taskid 3 dari BPJS : " + ex);
            }

            if (!task3Time.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + no_rawat + "'") > 0) {
                        System.out.println("TaskId=4 sudah ada akan skip\n");
                    } else {
                    String cleanTime = task3Time.replaceAll(" WIB| WITA| WIT", "");
                    String timeOnly = "";
                    if (cleanTime.contains(" ")) {
                        timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                    }
                    String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + no_rawat + "'");
                    java.util.Date date3 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                    long randomAdd = 600000 + (long)(Math.random() * 480000);
                    datajam = dateFormat.format(new java.util.Date(date3.getTime() + randomAdd));
                    
                    parsedDate = dateFormat.parse(datajam);
                    System.out.println("Menjalankan WS taskid mulai pelayanan poli (Fallback) Mobile JKN\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{" +
                                     "\"kodebooking\": \"" + data + "\"," +
                                     "\"taskid\": \"4\"," +
                                     "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                  "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("WAKTU TASK ID 4 JKN (Fallback) = " + datajam);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + no_rawat + "'");
                    } else {
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','4','" + dateFormat.format(parsedDate) + "')");
                    }
                    System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception ex) {
                    System.out.println("Notifikasi Bridging Fallback : " + ex);
                }
            }
        }
        // ---------- TASK ID 5 (selesai pelayanan poli) ----------
          datajam = Sequel.cariIsi("SELECT antri_masuk_poli.selesai FROM antri_masuk_poli WHERE no_rawat='" + no_rawat + "'");    
//          if (datajam == null || datajam.equals("")) {
//              datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT FROM_UNIXTIME(MAX(waktu)/1000) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + no_rawat + "' AND taskid='4'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + no_rawat + "')), INTERVAL (240 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
//          }
          boolean butuhFallback = false;

        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + no_rawat + "'") > 0) {
                    System.out.println("TaskId=5 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid selesai pelayanan poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"5\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                System.out.println("WAKTU TASK ID 5 JKN = " + datajam);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + no_rawat + "'");
                    String msg = nameNode.path("message").asText();
                    if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                        butuhFallback = true;
                    }
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','5','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 5 MJKN = BELUM Klik selesai poli atau waktu tidak ada di DB. Mengaktifkan Fallback BPJS...");
            butuhFallback = true;
        }

        if (butuhFallback) {
            System.out.println("Menggunakan Fallback Task ID 4 dari BPJS untuk Task ID 5...");
            String task4Time = "";
            try {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{\"kodebooking\": \"" + data + "\"}";
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/getlisttask";
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (nameNode.path("code").asText().equals("200")) {
                    response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                    JsonNode arrayNode = response;
                    if (response.isObject() && response.has("list")) {
                        arrayNode = response.path("list");
                    }
                    if (arrayNode.isArray()) {
                        for (JsonNode listNode : arrayNode) {
                            if (listNode.path("taskid").asText().equals("4") || listNode.path("taskid").asInt() == 4) {
                                task4Time = listNode.path("wakturs").asText();
                                if (task4Time.equals("") || task4Time.equals("null")) {
                                    task4Time = listNode.path("waktuRS").asText();
                                }
                                if (task4Time.equals("") || task4Time.equals("null")) {
                                    task4Time = listNode.path("waktu").asText();
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Gagal menarik getlisttask taskid 4 dari BPJS : " + ex);
            }

            if (!task4Time.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + no_rawat + "'") > 0) {
                        System.out.println("TaskId=5 sudah ada akan skip\n");
                    } else {
                    String cleanTime = task4Time.replaceAll(" WIB| WITA| WIT", "");
                    String timeOnly = "";
                    if (cleanTime.contains(" ")) {
                        timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                    }
                    String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + no_rawat + "'");
                    java.util.Date date4 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                    long randomAdd = 240000 + (long)(Math.random() * 60000);
                    datajam = dateFormat.format(new java.util.Date(date4.getTime() + randomAdd));
                    
                    parsedDate = dateFormat.parse(datajam);
                    System.out.println("Menjalankan WS taskid selesai poli (Fallback) Mobile JKN Pasien BPJS\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{" +
                                     "\"kodebooking\": \"" + data + "\"," +
                                     "\"taskid\": \"5\"," +
                                     "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                  "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("WAKTU TASK ID 5 JKN (Fallback) = " + datajam);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + no_rawat + "'");
                    } else {
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','5','" + dateFormat.format(parsedDate) + "')");
                    }
                    System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception e) {
                    System.out.println("Gagal memproses waktu fallback: " + e.getMessage());
                }
            }
        }

        // ---------- TASK ID 6 & 7 (racik obat / selesai obat) ----------
        if (Sequel.cariInteger("select count(no_rawat) from resep_obat where no_rawat='" + no_rawat + "'") > 0) {

            // INJECT TASK 6
            datajam = Sequel.cariIsi("SELECT concat(resep_obat.tgl_perawatan,' ',resep_obat.jam) FROM resep_obat WHERE resep_obat.tgl_perawatan<>'0000-00-00' AND resep_obat.status='ralan' AND resep_obat.no_rawat='" + no_rawat + "' ORDER BY jam_peresepan DESC LIMIT 1");

            boolean butuhFallback6 = false;

            if (datajam != null && !datajam.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + no_rawat + "'") > 0) {
                        System.out.println("TaskId=6 sudah ada akan skip\n");
                    } else {
                    parsedDate = dateFormat.parse(datajam);
                    System.out.println("Menjalankan WS taskid racik obat Mobile JKN\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{" +
                                     "\"kodebooking\": \"" + data + "\"," +
                                     "\"taskid\": \"6\"," +
                                     "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                  "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + no_rawat + "'");
                        String msg = nameNode.path("message").asText();
                        if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                            butuhFallback6 = true;
                        }
                    } else {
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','6','" + dateFormat.format(parsedDate) + "')");
                    }
                    System.out.println("JSON : " + requestJson + "\n");
                    System.out.println("URL : " + URL);
                    System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            } else {
                System.out.println("WAKTU TASK ID 6 MJKN = Waktu racik obat tidak ada di DB. Mengaktifkan Fallback BPJS...");
                butuhFallback6 = true;
            }

            if (butuhFallback6) {
                System.out.println("Menggunakan Fallback Task ID 5 dari BPJS untuk Task ID 6...");
                String task5Time = "";
                try {
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{\"kodebooking\": \"" + data + "\"}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/getlisttask";
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (nameNode.path("code").asText().equals("200")) {
                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                        JsonNode arrayNode = response;
                        if (response.isObject() && response.has("list")) {
                            arrayNode = response.path("list");
                        }
                        if (arrayNode.isArray()) {
                            for (JsonNode listNode : arrayNode) {
                                if (listNode.path("taskid").asText().equals("5") || listNode.path("taskid").asInt() == 5) {
                                    task5Time = listNode.path("wakturs").asText();
                                    if (task5Time.equals("") || task5Time.equals("null")) {
                                        task5Time = listNode.path("waktuRS").asText();
                                    }
                                    if (task5Time.equals("") || task5Time.equals("null")) {
                                        task5Time = listNode.path("waktu").asText();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Gagal menarik getlisttask taskid 5 dari BPJS : " + ex);
                }

                if (!task5Time.equals("")) {
                    try {
                        if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + no_rawat + "'") > 0) {
                            System.out.println("TaskId=6 sudah ada akan skip\n");
                        } else {
                        String cleanTime = task5Time.replaceAll(" WIB| WITA| WIT", "");
                        String timeOnly = "";
                        if (cleanTime.contains(" ")) {
                            timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                        }
                        String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + no_rawat + "'");
                        java.util.Date date5 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                        long randomAdd = 60000 + (long)(Math.random() * 60000);
                        datajam = dateFormat.format(new java.util.Date(date5.getTime() + randomAdd));
                        
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid racik obat (Fallback) Mobile JKN\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                        utc = String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp", utc);
                        headers.add("x-signature", api.getHmac(utc));
                        headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson = "{" +
                                         "\"kodebooking\": \"" + data + "\"," +
                                         "\"taskid\": \"6\"," +
                                         "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                      "}";
                        requestEntity = new HttpEntity(requestJson, headers);
                        URL = link + "/antrean/updatewaktu";
                        System.out.println("WAKTU TASK ID 6 JKN (Fallback) = " + datajam);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if (!nameNode.path("code").asText().equals("200")) {
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + no_rawat + "'");
                        } else {
                            Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','6','" + dateFormat.format(parsedDate) + "')");
                        }
                        System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                    
                        }} catch (Exception e) {
                        System.out.println("Gagal memproses waktu fallback task 6: " + e.getMessage());
                    }
                }
            }

            // INJECT TASK 7
            datajam = Sequel.cariIsi("SELECT concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan) FROM resep_obat WHERE resep_obat.status='ralan' AND resep_obat.no_rawat='" + no_rawat + "' AND concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan)<>'0000-00-00 00:00:00' LIMIT 1");
            
            boolean butuhFallback7 = false;

            if (datajam != null && !datajam.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + no_rawat + "'") > 0) {
                        System.out.println("TaskId=7 sudah ada akan skip\n");
                    } else {
                    parsedDate = dateFormat.parse(datajam);
                    System.out.println("Menjalankan WS taskid selesai obat Mobile JKN\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{" +
                                     "\"kodebooking\": \"" + data + "\"," +
                                     "\"taskid\": \"7\"," +
                                     "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                  "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + no_rawat + "'");
                        String msg = nameNode.path("message").asText();
                        if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                            butuhFallback7 = true;
                        }
                    } else {
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','7','" + dateFormat.format(parsedDate) + "')");
                    }
                    System.out.println("JSON : " + requestJson + "\n");
                    System.out.println("URL : " + URL);
                    System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            } else {
                System.out.println("WAKTU TASK ID 7 MJKN = Waktu selesai obat tidak ada di DB. Mengaktifkan Fallback BPJS...");
                butuhFallback7 = true;
            }

            if (butuhFallback7) {
                System.out.println("Menggunakan Fallback Task ID 6 dari BPJS untuk Task ID 7...");
                String task6Time = "";
                try {
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{\"kodebooking\": \"" + data + "\"}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/getlisttask";
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (nameNode.path("code").asText().equals("200")) {
                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                        JsonNode arrayNode = response;
                        if (response.isObject() && response.has("list")) {
                            arrayNode = response.path("list");
                        }
                        if (arrayNode.isArray()) {
                            for (JsonNode listNode : arrayNode) {
                                if (listNode.path("taskid").asText().equals("6") || listNode.path("taskid").asInt() == 6) {
                                    task6Time = listNode.path("wakturs").asText();
                                    if (task6Time.equals("") || task6Time.equals("null")) {
                                        task6Time = listNode.path("waktuRS").asText();
                                    }
                                    if (task6Time.equals("") || task6Time.equals("null")) {
                                        task6Time = listNode.path("waktu").asText();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Gagal menarik getlisttask taskid 6 dari BPJS : " + ex);
                }

                if (!task6Time.equals("")) {
                    try {
                        if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + no_rawat + "'") > 0) {
                            System.out.println("TaskId=7 sudah ada akan skip\n");
                        } else {
                        String cleanTime = task6Time.replaceAll(" WIB| WITA| WIT", "");
                        String timeOnly = "";
                        if (cleanTime.contains(" ")) {
                            timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                        }
                        String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + no_rawat + "'");
                        java.util.Date date6 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                        long randomAdd = 240000 + (long)(Math.random() * 120000);
                        datajam = dateFormat.format(new java.util.Date(date6.getTime() + randomAdd));
                        
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid selesai obat (Fallback) Mobile JKN\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                        utc = String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp", utc);
                        headers.add("x-signature", api.getHmac(utc));
                        headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson = "{" +
                                         "\"kodebooking\": \"" + data + "\"," +
                                         "\"taskid\": \"7\"," +
                                         "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                      "}";
                        requestEntity = new HttpEntity(requestJson, headers);
                        URL = link + "/antrean/updatewaktu";
                        System.out.println("WAKTU TASK ID 7 JKN (Fallback) = " + datajam);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if (!nameNode.path("code").asText().equals("200")) {
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + no_rawat + "'");
                        } else {
                            Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','7','" + dateFormat.format(parsedDate) + "')");
                        }
                        System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                    
                        }} catch (Exception e) {
                        System.out.println("Gagal memproses waktu fallback task 7: " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("TIDAK ADA RESEP OBAT, SKIP TASK 6 DAN 7 UNTUK " + data + " (no_rawat=" + no_rawat + ")");
        }

        // Tarik kembali status akhir dari BPJS cloud & refresh tabel
        syncListTaskBPJS(no_rawat, data);
        tampil();
    }

    private void KirimUlangJKN() {                                              
        // TODO add your handling code here: JKN
        kodebooking = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 0).toString();
        data = kodebooking; // tetap dipakai sebagai field "kodebooking" pada JSON ke BPJS
        String no_rawat = kodebooking;

        // 1. Tarik & simpan task ID yang sudah ada di BPJS cloud
        syncListTaskBPJS(no_rawat, data);

        // ---------- TASK ID 99 (batal pelayanan) ----------
        datajam = Sequel.cariIsi("select now() from reg_periksa where reg_periksa.stts='Batal' and reg_periksa.no_rawat=?", kodebooking);
        if (datajam != null && !datajam.equals("")) {
            try {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid batal pelayanan poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"99\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='99' and no_rawat='" + kodebooking + "'");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            } catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        }
        
                        // ---------- TASK ID 1 (admisi admisi) ----------
        datajam = Sequel.cariIsi("select concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg) from reg_periksa where reg_periksa.no_rawat = '" + kodebooking + "'");    
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='1' and no_rawat='" + kodebooking + "'") > 0) {
                    System.out.println("TaskId=1 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid mulai tunggu poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"1\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='1' and no_rawat='" + kodebooking + "'");
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','1','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 1 MJKN = BELUM add adtrian, SKIP KIRIM " + kodebooking + " (no_rawat=" + kodebooking + ")");
        }
                // ---------- TASK ID 2 (selesai admisi) ----------
        datajam = Sequel.cariIsi("select DATE_ADD(concat(reg_periksa.tgl_registrasi,' ',reg_periksa.jam_reg), INTERVAL FLOOR(1 + RAND() * 1) MINUTE) from reg_periksa where reg_periksa.no_rawat ='" + kodebooking + "'");    
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='2' and no_rawat='" + kodebooking + "'") > 0) {
                    System.out.println("TaskId=2 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid mulai tunggu poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"2\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='2' and no_rawat='" + kodebooking + "'");
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','2','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 2 MJKN = BELUM ada TASK ID 1, SKIP KIRIM " + kodebooking + " (no_rawat=" + kodebooking + ")");
        }

        // ---------- TASK ID 3 (mulai tunggu poli) ----------
//        datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT MAX(waktu) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + kodebooking + "' AND taskid='3'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + kodebooking + "')), INTERVAL (300 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
        datajam = Sequel.cariIsi("select soap from antri_masuk_poli where no_rawat='" + kodebooking + "'");    
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='" + kodebooking + "'") > 0) {
                    System.out.println("TaskId=3 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid mulai tunggu poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"3\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='" + kodebooking + "'");
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','3','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 3 MJKN = BELUM MENGISI SOAP PERAWAT, SKIP KIRIM " + kodebooking + " (no_rawat=" + kodebooking + ")");
        }
        // ---------- TASK ID 4 (mulai pelayanan poli) ----------
            datajam = Sequel.cariIsi("select antri_masuk_poli.tgl_jam from antri_masuk_poli where antri_masuk_poli.no_rawat = '" + kodebooking + "'");
            boolean butuhFallback4 = false;
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + kodebooking + "'") > 0) {
                    System.out.println("TaskId=4 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid mulai pelayanan poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"4\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + kodebooking + "'");
                    String msg = nameNode.path("message").asText();
                    if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                        butuhFallback4 = true;
                    }
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','4','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 4 MJKN = Belum ada paggil masuk poli atau waktu tidak ada di DB. Mengaktifkan Fallback BPJS...");
            butuhFallback4 = true;
        }

        if (butuhFallback4) {
            System.out.println("Menggunakan Fallback Task ID 3 dari BPJS untuk Task ID 4...");
            String task3Time = "";
            try {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{\"kodebooking\": \"" + data + "\"}";
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/getlisttask";
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (nameNode.path("code").asText().equals("200")) {
                    response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                    JsonNode arrayNode = response;
                    if (response.isObject() && response.has("list")) {
                        arrayNode = response.path("list");
                    }
                    if (arrayNode.isArray()) {
                        for (JsonNode listNode : arrayNode) {
                            if (listNode.path("taskid").asText().equals("3") || listNode.path("taskid").asInt() == 3) {
                                task3Time = listNode.path("wakturs").asText();
                                if (task3Time.equals("") || task3Time.equals("null")) {
                                    task3Time = listNode.path("waktuRS").asText();
                                }
                                if (task3Time.equals("") || task3Time.equals("null")) {
                                    task3Time = listNode.path("waktu").asText();
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Gagal menarik getlisttask taskid 3 dari BPJS : " + ex);
            }

            if (!task3Time.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + kodebooking + "'") > 0) {
                        System.out.println("TaskId=4 sudah ada akan skip\n");
                    } else {
                    String cleanTime = task3Time.replaceAll(" WIB| WITA| WIT", "");
                    String timeOnly = "";
                    if (cleanTime.contains(" ")) {
                        timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                    }
                    String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + kodebooking + "'");
                    java.util.Date date3 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                    long randomAdd = 600000 + (long)(Math.random() * 480000);
                    datajam = dateFormat.format(new java.util.Date(date3.getTime() + randomAdd));
                    
                    parsedDate = dateFormat.parse(datajam);
                    System.out.println("Menjalankan WS taskid mulai pelayanan poli (Fallback) Mobile JKN\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{" +
                                     "\"kodebooking\": \"" + data + "\"," +
                                     "\"taskid\": \"4\"," +
                                     "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                  "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("WAKTU TASK ID 4 JKN (Fallback) = " + datajam);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + kodebooking + "'");
                    } else {
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','4','" + dateFormat.format(parsedDate) + "')");
                    }
                    System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception ex) {
                    System.out.println("Notifikasi Bridging Fallback : " + ex);
                }
            }
        }
        // ---------- TASK ID 5 (selesai pelayanan poli) ----------
          datajam = Sequel.cariIsi("SELECT antri_masuk_poli.selesai FROM antri_masuk_poli WHERE no_rawat='" + kodebooking + "'");    
//          if (datajam == null || datajam.equals("")) {
//              datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT FROM_UNIXTIME(MAX(waktu)/1000) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + kodebooking + "' AND taskid='4'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + kodebooking + "')), INTERVAL (240 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
//          }
          boolean butuhFallback = false;

        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + kodebooking + "'") > 0) {
                    System.out.println("TaskId=5 sudah ada akan skip\n");
                } else {
                parsedDate = dateFormat.parse(datajam);
                System.out.println("Menjalankan WS taskid selesai pelayanan poli Mobile JKN Pasien BPJS\n");
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{" +
                                 "\"kodebooking\": \"" + data + "\"," +
                                 "\"taskid\": \"5\"," +
                                 "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                              "}";
                System.out.println("JSON : " + requestJson + "\n");
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/updatewaktu";
                System.out.println("URL : " + URL);
                System.out.println("WAKTU TASK ID 5 JKN = " + datajam);
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (!nameNode.path("code").asText().equals("200")) {
                    Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + kodebooking + "'");
                    String msg = nameNode.path("message").asText();
                    if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                        butuhFallback = true;
                    }
                } else {
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','5','" + dateFormat.format(parsedDate) + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 5 MJKN = BELUM Klik selesai poli atau waktu tidak ada di DB. Mengaktifkan Fallback BPJS...");
            butuhFallback = true;
        }

        if (butuhFallback) {
            System.out.println("Menggunakan Fallback Task ID 4 dari BPJS untuk Task ID 5...");
            String task4Time = "";
            try {
                headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                utc = String.valueOf(api.GetUTCdatetimeAsString());
                headers.add("x-timestamp", utc);
                headers.add("x-signature", api.getHmac(utc));
                headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                requestJson = "{\"kodebooking\": \"" + data + "\"}";
                requestEntity = new HttpEntity(requestJson, headers);
                URL = link + "/antrean/getlisttask";
                root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                nameNode = root.path("metadata");
                if (nameNode.path("code").asText().equals("200")) {
                    response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                    JsonNode arrayNode = response;
                    if (response.isObject() && response.has("list")) {
                        arrayNode = response.path("list");
                    }
                    if (arrayNode.isArray()) {
                        for (JsonNode listNode : arrayNode) {
                            if (listNode.path("taskid").asText().equals("4") || listNode.path("taskid").asInt() == 4) {
                                task4Time = listNode.path("wakturs").asText();
                                if (task4Time.equals("") || task4Time.equals("null")) {
                                    task4Time = listNode.path("waktuRS").asText();
                                }
                                if (task4Time.equals("") || task4Time.equals("null")) {
                                    task4Time = listNode.path("waktu").asText();
                                }
                                break;
                            }
                        }
                    }
                }
            } catch (Exception ex) {
                System.out.println("Gagal menarik getlisttask taskid 4 dari BPJS : " + ex);
            }

            if (!task4Time.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + kodebooking + "'") > 0) {
                        System.out.println("TaskId=5 sudah ada akan skip\n");
                    } else {
                    String cleanTime = task4Time.replaceAll(" WIB| WITA| WIT", "");
                    String timeOnly = "";
                    if (cleanTime.contains(" ")) {
                        timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                    }
                    String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + kodebooking + "'");
                    java.util.Date date4 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                    long randomAdd = 240000 + (long)(Math.random() * 60000);
                    datajam = dateFormat.format(new java.util.Date(date4.getTime() + randomAdd));
                    
                    parsedDate = dateFormat.parse(datajam);
                    System.out.println("Menjalankan WS taskid selesai poli (Fallback) Mobile JKN Pasien BPJS\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{" +
                                     "\"kodebooking\": \"" + data + "\"," +
                                     "\"taskid\": \"5\"," +
                                     "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                  "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    System.out.println("WAKTU TASK ID 5 JKN (Fallback) = " + datajam);
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + kodebooking + "'");
                    } else {
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','5','" + dateFormat.format(parsedDate) + "')");
                    }
                    System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception e) {
                    System.out.println("Gagal memproses waktu fallback: " + e.getMessage());
                }
            }
        }

        // ---------- TASK ID 6 & 7 (racik obat / selesai obat) ----------
        if (Sequel.cariInteger("select count(no_rawat) from resep_obat where no_rawat='" + kodebooking + "'") > 0) {

            // INJECT TASK 6
            datajam = Sequel.cariIsi("SELECT concat(resep_obat.tgl_perawatan,' ',resep_obat.jam) FROM resep_obat WHERE resep_obat.tgl_perawatan<>'0000-00-00' AND resep_obat.status='ralan' AND resep_obat.no_rawat='" + kodebooking + "' ORDER BY jam_peresepan DESC LIMIT 1");
            boolean butuhFallback6 = false;

            if (datajam != null && !datajam.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + kodebooking + "'") > 0) {
                        System.out.println("TaskId=6 sudah ada akan skip\n");
                    } else {
                    parsedDate = dateFormat.parse(datajam);
                    System.out.println("Menjalankan WS taskid racik obat Mobile JKN\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{" +
                                     "\"kodebooking\": \"" + kodebooking + "\"," +
                                     "\"taskid\": \"6\"," +
                                     "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                  "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + kodebooking + "'");
                        String msg = nameNode.path("message").asText();
                        if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                            butuhFallback6 = true;
                        }
                    } else {
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','6','" + dateFormat.format(parsedDate) + "')");
                    }
                    System.out.println("JSON : " + requestJson + "\n");
                    System.out.println("URL : " + URL);
                    System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            } else {
                System.out.println("WAKTU TASK ID 6 MJKN = Waktu racik obat tidak ada di DB. Mengaktifkan Fallback BPJS...");
                butuhFallback6 = true;
            }

            if (butuhFallback6) {
                System.out.println("Menggunakan Fallback Task ID 5 dari BPJS untuk Task ID 6...");
                String task5Time = "";
                try {
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{\"kodebooking\": \"" + kodebooking + "\"}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/getlisttask";
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (nameNode.path("code").asText().equals("200")) {
                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                        JsonNode arrayNode = response;
                        if (response.isObject() && response.has("list")) {
                            arrayNode = response.path("list");
                        }
                        if (arrayNode.isArray()) {
                            for (JsonNode listNode : arrayNode) {
                                if (listNode.path("taskid").asText().equals("5") || listNode.path("taskid").asInt() == 5) {
                                    task5Time = listNode.path("wakturs").asText();
                                    if (task5Time.equals("") || task5Time.equals("null")) {
                                        task5Time = listNode.path("waktuRS").asText();
                                    }
                                    if (task5Time.equals("") || task5Time.equals("null")) {
                                        task5Time = listNode.path("waktu").asText();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Gagal menarik getlisttask taskid 5 dari BPJS : " + ex);
                }

                if (!task5Time.equals("")) {
                    try {
                        if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + kodebooking + "'") > 0) {
                            System.out.println("TaskId=6 sudah ada akan skip\n");
                        } else {
                        String cleanTime = task5Time.replaceAll(" WIB| WITA| WIT", "");
                        String timeOnly = "";
                        if (cleanTime.contains(" ")) {
                            timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                        }
                        String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + kodebooking + "'");
                        java.util.Date date5 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                        long randomAdd = 60000 + (long)(Math.random() * 60000);
                        datajam = dateFormat.format(new java.util.Date(date5.getTime() + randomAdd));
                        
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid racik obat (Fallback) Mobile JKN\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                        utc = String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp", utc);
                        headers.add("x-signature", api.getHmac(utc));
                        headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson = "{" +
                                         "\"kodebooking\": \"" + kodebooking + "\"," +
                                         "\"taskid\": \"6\"," +
                                         "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                      "}";
                        requestEntity = new HttpEntity(requestJson, headers);
                        URL = link + "/antrean/updatewaktu";
                        System.out.println("WAKTU TASK ID 6 JKN (Fallback) = " + datajam);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if (!nameNode.path("code").asText().equals("200")) {
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + kodebooking + "'");
                        } else {
                            Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','6','" + dateFormat.format(parsedDate) + "')");
                        }
                        System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                    
                        }} catch (Exception e) {
                        System.out.println("Gagal memproses waktu fallback task 6: " + e.getMessage());
                    }
                }
            }

            // INJECT TASK 7
            datajam = Sequel.cariIsi("SELECT concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan) FROM resep_obat WHERE resep_obat.status='ralan' AND resep_obat.no_rawat='" + kodebooking + "' AND concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan)<>'0000-00-00 00:00:00' LIMIT 1");
            
            boolean butuhFallback7 = false;

            if (datajam != null && !datajam.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + kodebooking + "'") > 0) {
                        System.out.println("TaskId=7 sudah ada akan skip\n");
                    } else {
                    parsedDate = dateFormat.parse(datajam);
                    System.out.println("Menjalankan WS taskid selesai obat Mobile JKN\n");
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{" +
                                     "\"kodebooking\": \"" + kodebooking + "\"," +
                                     "\"taskid\": \"7\"," +
                                     "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                  "}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/updatewaktu";
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (!nameNode.path("code").asText().equals("200")) {
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + kodebooking + "'");
                        String msg = nameNode.path("message").asText();
                        if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                            butuhFallback7 = true;
                        }
                    } else {
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','7','" + dateFormat.format(parsedDate) + "')");
                    }
                    System.out.println("JSON : " + requestJson + "\n");
                    System.out.println("URL : " + URL);
                    System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception ex) {
                    System.out.println("Notifikasi Bridging : " + ex);
                }
            } else {
                System.out.println("WAKTU TASK ID 7 MJKN = Waktu selesai obat tidak ada di DB. Mengaktifkan Fallback BPJS...");
                butuhFallback7 = true;
            }

            if (butuhFallback7) {
                System.out.println("Menggunakan Fallback Task ID 6 dari BPJS untuk Task ID 7...");
                String task6Time = "";
                try {
                    headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                    requestJson = "{\"kodebooking\": \"" + kodebooking + "\"}";
                    requestEntity = new HttpEntity(requestJson, headers);
                    URL = link + "/antrean/getlisttask";
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");
                    if (nameNode.path("code").asText().equals("200")) {
                        response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                        JsonNode arrayNode = response;
                        if (response.isObject() && response.has("list")) {
                            arrayNode = response.path("list");
                        }
                        if (arrayNode.isArray()) {
                            for (JsonNode listNode : arrayNode) {
                                if (listNode.path("taskid").asText().equals("6") || listNode.path("taskid").asInt() == 6) {
                                    task6Time = listNode.path("wakturs").asText();
                                    if (task6Time.equals("") || task6Time.equals("null")) {
                                        task6Time = listNode.path("waktuRS").asText();
                                    }
                                    if (task6Time.equals("") || task6Time.equals("null")) {
                                        task6Time = listNode.path("waktu").asText();
                                    }
                                    break;
                                }
                            }
                        }
                    }
                } catch (Exception ex) {
                    System.out.println("Gagal menarik getlisttask taskid 6 dari BPJS : " + ex);
                }

                if (!task6Time.equals("")) {
                    try {
                        if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + kodebooking + "'") > 0) {
                            System.out.println("TaskId=7 sudah ada akan skip\n");
                        } else {
                        String cleanTime = task6Time.replaceAll(" WIB| WITA| WIT", "");
                        String timeOnly = "";
                        if (cleanTime.contains(" ")) {
                            timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                        }
                        String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + kodebooking + "'");
                        java.util.Date date6 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                        long randomAdd = 240000 + (long)(Math.random() * 120000);
                        datajam = dateFormat.format(new java.util.Date(date6.getTime() + randomAdd));
                        
                        parsedDate = dateFormat.parse(datajam);
                        System.out.println("Menjalankan WS taskid selesai obat (Fallback) Mobile JKN\n");
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                        utc = String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp", utc);
                        headers.add("x-signature", api.getHmac(utc));
                        headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());
                        requestJson = "{" +
                                         "\"kodebooking\": \"" + kodebooking + "\"," +
                                         "\"taskid\": \"7\"," +
                                         "\"waktu\": \"" + parsedDate.getTime() + "\"" +
                                      "}";
                        requestEntity = new HttpEntity(requestJson, headers);
                        URL = link + "/antrean/updatewaktu";
                        System.out.println("WAKTU TASK ID 7 JKN (Fallback) = " + datajam);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if (!nameNode.path("code").asText().equals("200")) {
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + kodebooking + "'");
                        } else {
                            Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','7','" + dateFormat.format(parsedDate) + "')");
                        }
                        System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                    
                        }} catch (Exception e) {
                        System.out.println("Gagal memproses waktu fallback task 7: " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("TIDAK ADA RESEP OBAT, SKIP TASK 6 DAN 7 UNTUK " + kodebooking + " (no_rawat=" + kodebooking + ")");
        }

        // Tarik kembali status akhir dari BPJS cloud & refresh tabel
        syncListTaskBPJS(no_rawat, data);
        tampil();
    }
    //sampe sini
    
    private void runBackground(Runnable task) {
        if (ceksukses) return;
        ceksukses = true;

        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        executor.submit(() -> {
            try {
                task.run();
            } finally {
                ceksukses = false;
                SwingUtilities.invokeLater(() -> {
                    if (isDisplayable()) {
                        setCursor(Cursor.getDefaultCursor());
                    }
                });
            }
        });
    }

    @Override
    public void dispose() {
        executor.shutdownNow();
        super.dispose();
    }
}
