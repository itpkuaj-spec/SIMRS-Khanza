
/*
 * Kontribusi Oleh Ferry Ardiansyah - RSIAP 3326051
 *
 * Created on May 22, 2010, 11:58:21 PM
 */

package bridging;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.WarnaTable;
import fungsi.koneksiDB;
import fungsi.sekuel;
import fungsi.validasi;
import java.awt.Color;
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
public final class BPJSAntreanPerTanggal extends javax.swing.JDialog {
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
    public BPJSAntreanPerTanggal(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();

        this.setLocation(8,1);
        setSize(628,674);

        tabMode=new DefaultTableModel(null,new Object[]{
                "Kode Booking","Tanggal","Kode Poli","Kode Dokter","Jam Praktek","NIK","Noka","No. HP","RM","Jenis Kunjungan","No. Ref","Sumber Data","Peserta","No. Antrean","Estimasi Dilayani","Created Time","Status"
            }){
             @Override public boolean isCellEditable(int rowIndex, int colIndex){return false;}
        };
        tbJnsPerawatan.setModel(tabMode);

        tbJnsPerawatan.setPreferredScrollableViewportSize(new Dimension(500,500));
        tbJnsPerawatan.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

        for (i = 0; i < 17; i++) {
            TableColumn column = tbJnsPerawatan.getColumnModel().getColumn(i);
            if(i==0){
                column.setPreferredWidth(110);
            }else if(i==1){
                column.setPreferredWidth(70);
            }else if(i==2){
                column.setPreferredWidth(70);
            }else if(i==3){
                column.setPreferredWidth(83);
            }else if(i==4){
                column.setPreferredWidth(90);
            }else if(i==5){
                column.setPreferredWidth(120);
            }else if(i==6){
                column.setPreferredWidth(100);
            }else if(i==7){
                column.setPreferredWidth(100);
            }else if(i==8){
                column.setPreferredWidth(60);
            }else if(i==9){
                column.setPreferredWidth(100);
            }else if(i==10){
                column.setPreferredWidth(140);
            }else if(i==11){
                column.setPreferredWidth(100);
            }else if(i==12){
                column.setPreferredWidth(70);
            }else if(i==13){
                column.setPreferredWidth(70);
            }else if(i==14){
                column.setPreferredWidth(120);
            }else if(i==15){
                column.setPreferredWidth(120);
            }else if(i==15){
                column.setPreferredWidth(90);
            }
        }
        tbJnsPerawatan.setDefaultRenderer(Object.class, new WarnaTable());
        
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
        jLabel12 = new widget.Label();
        MJknBelum = new widget.Label();
        MJknCapaian = new widget.Label();
        jLabel13 = new widget.Label();
        MJknSelesai = new widget.Label();
        jLabel19 = new widget.Label();
        DTPCari1 = new widget.Tanggal();
        jLabel21 = new widget.Label();
        DTPCari2 = new widget.Tanggal();
        BtnCari = new widget.Button();
        jLabel7 = new widget.Label();
        LCount = new widget.Label();
        detailtaskid = new javax.swing.JButton();
        dashboardbpjs = new javax.swing.JButton();
        BtnKeluar1 = new widget.Button();
        panelGlass8 = new widget.panelisi();
        jLabel8 = new widget.Label();
        TotBelum = new widget.Label();
        jLabel9 = new widget.Label();
        TotSelesai = new widget.Label();
        jLabel14 = new widget.Label();
        SEPTerbit = new widget.Label();
        jLabel10 = new widget.Label();
        JknBelum = new widget.Label();
        jLabel11 = new widget.Label();
        JknSelesai = new widget.Label();
        JknCapaian = new widget.Label();
        jLabel15 = new widget.Label();
        NonJKNBelum = new widget.Label();
        jLabel16 = new widget.Label();
        NonJKNSelesai = new widget.Label();
        totalantrol = new widget.Label();

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

        internalFrame1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(240, 245, 235)), "::[ Antrean Per Tanggal Mobile JKN ]::", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(50, 50, 50))); // NOI18N
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
        jPanel2.setPreferredSize(new java.awt.Dimension(44, 100));
        jPanel2.setLayout(new java.awt.BorderLayout(1, 1));

        panelGlass9.setName("panelGlass9"); // NOI18N
        panelGlass9.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass9.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel12.setForeground(new java.awt.Color(0, 153, 0));
        jLabel12.setText("MJKN Belum :");
        jLabel12.setName("jLabel12"); // NOI18N
        jLabel12.setPreferredSize(new java.awt.Dimension(75, 23));
        panelGlass9.add(jLabel12);

        MJknBelum.setForeground(new java.awt.Color(0, 153, 0));
        MJknBelum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MJknBelum.setText("0");
        MJknBelum.setName("MJknBelum"); // NOI18N
        MJknBelum.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass9.add(MJknBelum);

        MJknCapaian.setForeground(new java.awt.Color(0, 153, 0));
        MJknCapaian.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MJknCapaian.setText("0");
        MJknCapaian.setName("MJknCapaian"); // NOI18N
        MJknCapaian.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass9.add(MJknCapaian);

        jLabel13.setForeground(new java.awt.Color(0, 153, 0));
        jLabel13.setText("MJKN Selesai :");
        jLabel13.setName("jLabel13"); // NOI18N
        jLabel13.setPreferredSize(new java.awt.Dimension(80, 23));
        panelGlass9.add(jLabel13);

        MJknSelesai.setForeground(new java.awt.Color(0, 153, 0));
        MJknSelesai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MJknSelesai.setText("0");
        MJknSelesai.setName("MJknSelesai"); // NOI18N
        MJknSelesai.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass9.add(MJknSelesai);

        jLabel19.setText("Tanggal :");
        jLabel19.setName("jLabel19"); // NOI18N
        jLabel19.setPreferredSize(new java.awt.Dimension(55, 23));
        panelGlass9.add(jLabel19);

        DTPCari1.setForeground(new java.awt.Color(50, 70, 50));
        DTPCari1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "21-07-2026" }));
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
        DTPCari2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "21-07-2026" }));
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

        detailtaskid.setText("Detail Task ID");
        detailtaskid.setName("detailtaskid"); // NOI18N
        detailtaskid.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                detailtaskidActionPerformed(evt);
            }
        });
        panelGlass9.add(detailtaskid);

        dashboardbpjs.setText("dashboard");
        dashboardbpjs.setName("dashboardbpjs"); // NOI18N
        dashboardbpjs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dashboardbpjsActionPerformed(evt);
            }
        });
        panelGlass9.add(dashboardbpjs);

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
        panelGlass8.setPreferredSize(new java.awt.Dimension(44, 44));
        panelGlass8.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT, 5, 9));

        jLabel8.setForeground(new java.awt.Color(255, 153, 0));
        jLabel8.setText("Total Belum :");
        jLabel8.setName("jLabel8"); // NOI18N
        jLabel8.setPreferredSize(new java.awt.Dimension(72, 23));
        panelGlass8.add(jLabel8);

        TotBelum.setForeground(new java.awt.Color(255, 153, 0));
        TotBelum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotBelum.setText("0");
        TotBelum.setName("TotBelum"); // NOI18N
        TotBelum.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(TotBelum);

        jLabel9.setForeground(new java.awt.Color(102, 153, 0));
        jLabel9.setText("Total Selesai :");
        jLabel9.setName("jLabel9"); // NOI18N
        jLabel9.setPreferredSize(new java.awt.Dimension(77, 23));
        panelGlass8.add(jLabel9);

        TotSelesai.setForeground(new java.awt.Color(102, 153, 0));
        TotSelesai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        TotSelesai.setText("0");
        TotSelesai.setName("TotSelesai"); // NOI18N
        TotSelesai.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(TotSelesai);

        jLabel14.setForeground(new java.awt.Color(0, 153, 255));
        jLabel14.setText("SEP Terbit :");
        jLabel14.setName("jLabel14"); // NOI18N
        jLabel14.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass8.add(jLabel14);

        SEPTerbit.setForeground(new java.awt.Color(0, 153, 255));
        SEPTerbit.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        SEPTerbit.setText("0");
        SEPTerbit.setName("SEPTerbit"); // NOI18N
        SEPTerbit.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(SEPTerbit);

        jLabel10.setForeground(new java.awt.Color(204, 204, 0));
        jLabel10.setText("JKN Belum :");
        jLabel10.setName("jLabel10"); // NOI18N
        jLabel10.setPreferredSize(new java.awt.Dimension(65, 23));
        panelGlass8.add(jLabel10);

        JknBelum.setForeground(new java.awt.Color(204, 204, 0));
        JknBelum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        JknBelum.setText("0");
        JknBelum.setName("JknBelum"); // NOI18N
        JknBelum.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(JknBelum);

        jLabel11.setForeground(new java.awt.Color(204, 204, 0));
        jLabel11.setText("JKN Selesai :");
        jLabel11.setName("jLabel11"); // NOI18N
        jLabel11.setPreferredSize(new java.awt.Dimension(70, 23));
        panelGlass8.add(jLabel11);

        JknSelesai.setForeground(new java.awt.Color(204, 204, 0));
        JknSelesai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        JknSelesai.setText("0");
        JknSelesai.setName("JknSelesai"); // NOI18N
        JknSelesai.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(JknSelesai);

        JknCapaian.setForeground(new java.awt.Color(204, 204, 0));
        JknCapaian.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        JknCapaian.setText("0 ");
        JknCapaian.setName("JknCapaian"); // NOI18N
        JknCapaian.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(JknCapaian);

        jLabel15.setForeground(new java.awt.Color(0, 153, 153));
        jLabel15.setText("Non JKN Belum :");
        jLabel15.setName("jLabel15"); // NOI18N
        jLabel15.setPreferredSize(new java.awt.Dimension(90, 23));
        panelGlass8.add(jLabel15);

        NonJKNBelum.setForeground(new java.awt.Color(0, 153, 153));
        NonJKNBelum.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NonJKNBelum.setText("0");
        NonJKNBelum.setName("NonJKNBelum"); // NOI18N
        NonJKNBelum.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(NonJKNBelum);

        jLabel16.setForeground(new java.awt.Color(0, 153, 153));
        jLabel16.setText("Non JKN Selesai :");
        jLabel16.setName("jLabel16"); // NOI18N
        jLabel16.setPreferredSize(new java.awt.Dimension(92, 23));
        panelGlass8.add(jLabel16);

        NonJKNSelesai.setForeground(new java.awt.Color(0, 153, 153));
        NonJKNSelesai.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NonJKNSelesai.setText("0");
        NonJKNSelesai.setName("NonJKNSelesai"); // NOI18N
        NonJKNSelesai.setPreferredSize(new java.awt.Dimension(35, 23));
        panelGlass8.add(NonJKNSelesai);

        totalantrol.setForeground(new java.awt.Color(0, 153, 153));
        totalantrol.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        totalantrol.setText("0");
        totalantrol.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        totalantrol.setName("totalantrol"); // NOI18N
        totalantrol.setPreferredSize(new java.awt.Dimension(120, 23));
        panelGlass8.add(totalantrol);

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
            BPJSCekKodeBooking detail=new BPJSCekKodeBooking(null,false);
            detail.tampil(tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(),0).toString());
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

    private void dashboardbpjsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dashboardbpjsActionPerformed
        // TODO add your handling code here:     
        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        bridging.PKUDashboardBPJS aplikasi = new bridging.PKUDashboardBPJS(null, false);
        aplikasi.setSize(internalFrame1.getWidth(), internalFrame1.getHeight());
        aplikasi.setLocationRelativeTo(internalFrame1);
        aplikasi.setVisible(true);
        this.setCursor(java.awt.Cursor.getDefaultCursor());
}//GEN-LAST:event_dashboardbpjsActionPerformed

    private void detailtaskidActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_detailtaskidActionPerformed
        this.setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.WAIT_CURSOR));
        tambahan_it.DlgMobileJKN aplikasi = new tambahan_it.DlgMobileJKN(null, false);
        aplikasi.setSize(internalFrame1.getWidth(), internalFrame1.getHeight());
        aplikasi.setLocationRelativeTo(internalFrame1);
        aplikasi.setVisible(true);
        this.setCursor(java.awt.Cursor.getDefaultCursor());
    }//GEN-LAST:event_detailtaskidActionPerformed




    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            BPJSAntreanPerTanggal dialog = new BPJSAntreanPerTanggal(new javax.swing.JFrame(), true);
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
        SEPTerbit.setText("");
        TotBelum.setText("");
        TotSelesai.setText("");
        JknBelum.setText("");
        JknSelesai.setText("");
        MJknBelum.setText("");
        MJknSelesai.setText("");
        NonJKNBelum.setText("");
        NonJKNSelesai.setText("");
        JknCapaian.setText("");
        MJknCapaian.setText("");
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
    private widget.Label JknBelum;
    private widget.Label JknCapaian;
    private widget.Label JknSelesai;
    private widget.Label LCount;
    private widget.Label MJknBelum;
    private widget.Label MJknCapaian;
    private widget.Label MJknSelesai;
    private javax.swing.JMenuItem MnCekKodeBooking;
    private javax.swing.JMenuItem MnKirimBatalBPJS;
    private javax.swing.JMenuItem MnKirimUlangJKN;
    private javax.swing.JMenuItem MnKirimUlangMJKN;
    private widget.Label NonJKNBelum;
    private widget.Label NonJKNSelesai;
    private widget.Label SEPTerbit;
    private widget.ScrollPane Scroll;
    private widget.Label TotBelum;
    private widget.Label TotSelesai;
    private javax.swing.JButton dashboardbpjs;
    private javax.swing.JButton detailtaskid;
    private widget.InternalFrame internalFrame1;
    private widget.Label jLabel10;
    private widget.Label jLabel11;
    private widget.Label jLabel12;
    private widget.Label jLabel13;
    private widget.Label jLabel14;
    private widget.Label jLabel15;
    private widget.Label jLabel16;
    private widget.Label jLabel19;
    private widget.Label jLabel21;
    private widget.Label jLabel7;
    private widget.Label jLabel8;
    private widget.Label jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPopupMenu jPopupMenu1;
    private widget.panelisi panelGlass8;
    private widget.panelisi panelGlass9;
    private widget.Table tbJnsPerawatan;
    private widget.Label totalantrol;
    // End of variables declaration//GEN-END:variables

    private void tampil() {
        Valid.tabelKosong(tabMode);
        try{
            ps=koneksi.prepareStatement(
                   "SELECT reg_periksa.tgl_registrasi FROM reg_periksa WHERE reg_periksa.tgl_registrasi BETWEEN ? AND ? group by reg_periksa.tgl_registrasi");
            try {
                ps.setString(1,Valid.SetTgl(DTPCari1.getSelectedItem()+""));
                ps.setString(2,Valid.SetTgl(DTPCari2.getSelectedItem()+""));
                rs=ps.executeQuery();
                while(rs.next()){
                    try {
                        headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.add("x-cons-id",koneksiDB.CONSIDAPIMOBILEJKN());
                        utc=String.valueOf(api.GetUTCdatetimeAsString());
                        headers.add("x-timestamp",utc);
                        headers.add("x-signature",api.getHmac(utc));
                        headers.add("user_key",koneksiDB.USERKEYAPIMOBILEJKN());
                        requestEntity = new HttpEntity(headers);
                        URL = link+"/antrean/pendaftaran/tanggal/"+rs.getString("tgl_registrasi");	
                        System.out.println("URL : "+URL);
                        root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
                        nameNode = root.path("metadata");
                        if(nameNode.path("code").asText().equals("200")){
                            response = mapper.readTree(api.Decrypt(root.path("response").asText(),utc));
                            if(response.isArray()){
                                for(JsonNode list:response){
                                    tabMode.addRow(new Object[]{
                                        list.path("kodebooking").asText(),list.path("tanggal").asText(),list.path("kodepoli").asText(),
                                        list.path("kodedokter").asText(),list.path("jampraktek").asText(),list.path("nik").asText(),list.path("nokapst").asText(),list.path("nohp").asText(),list.path("norekammedis").asText(),list.path("jeniskunjungan").asText(),list.path("nomorreferensi").asText(),list.path("sumberdata").asText(),list.path("ispeserta").asText().equals("true")?"Ya":"Tidak",list.path("noantrean").asText(),list.path("estimasidilayani").asText(),list.path("createdtime").asText(),list.path("status").asText()
                                    });
                                    if (list.path("status").asText().equals("Belum dilayani")) {
                                        tot_belum += 1;
                                    }
                                    if (list.path("status").asText().equals("Selesai dilayani")) {
                                        tot_selesai += 1;
                                    }
                                    if (list.path("status").asText().equals("Belum dilayani") && list.path("sumberdata").asText().equals("Bridging Antrean") && list.path("ispeserta").asText().equals("true")) {
                                        jkn_belum += 1;
                                    }
                                    if (list.path("status").asText().equals("Selesai dilayani") && list.path("sumberdata").asText().equals("Bridging Antrean") && list.path("ispeserta").asText().equals("true")) {
                                        jkn_selesai += 1;
                                    }
                                    if (list.path("status").asText().equals("Belum dilayani") && list.path("sumberdata").asText().equals("Mobile JKN")) {
                                        mjkn_belum += 1;
                                    }
                                    if (list.path("status").asText().equals("Selesai dilayani") && list.path("sumberdata").asText().equals("Mobile JKN")) {
                                        mjkn_selesai += 1;
                                    }
                                    if (list.path("status").asText().equals("Belum dilayani") && list.path("ispeserta").asText().equals("false")) {
                                        umum_belum += 1;
                                    }
                                    if (list.path("status").asText().equals("Selesai dilayani") && list.path("ispeserta").asText().equals("false")) {
                                        umum_selesai += 1;
                                    }
                                }
                            }
                        }else {
                            System.out.println("Notif : "+nameNode.path("message").asText());               
                        }   
                    } catch (Exception ex) {
                        System.out.println("Notifikasi : "+ex);
                        if(ex.toString().contains("UnknownHostException")){
                            JOptionPane.showMessageDialog(rootPane,"Koneksi ke server BPJS terputus...!");
                        }
                    }
                }
            } catch (Exception e) {
                System.out.println("Notif : "+e);
            } finally{
                if(rs!=null){
                    rs.close();
                }
                if(ps!=null){
                    ps.close();
                }
            }
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
        }
        

        sep = Sequel.cariInteger("select count(bridging_sep.no_rawat) from bridging_sep where bridging_sep.tglsep between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' and bridging_sep.jnspelayanan = '2' and bridging_sep.kdpolitujuan <> 'IGD'")+
                Sequel.cariInteger("select count(bridging_sep_internal.no_rawat) from bridging_sep_internal where bridging_sep_internal.tglsep between '"+Valid.SetTgl(DTPCari1.getSelectedItem()+"")+"' and '"+Valid.SetTgl(DTPCari2.getSelectedItem()+"")+"' and bridging_sep_internal.jnspelayanan = '2' and bridging_sep_internal.kdpolitujuan <> 'IGD'");
//        jkn_capaian = (jkn_selesai/sep)*100;
//        mjkn_capaian = (mjkn_selesai/sep)*100;
        jkn_capaian = (sep == 0) ? 0 : ((double) jkn_selesai / sep) * 100;//ubahan
        mjkn_capaian = (sep == 0) ? 0 : ((double) mjkn_selesai / sep) * 100;//ubahan
        // Penambahan total capaian:
        double total_capaian = jkn_capaian + mjkn_capaian;//tambahan
        jkn_capaian_angka = (int)jkn_capaian;
        mjkn_capaian_angka = (int)mjkn_capaian;
        LCount.setText(""+tabMode.getRowCount());
        SEPTerbit.setText(""+sep);
        TotBelum.setText(""+tot_belum);
        TotSelesai.setText(""+tot_selesai);
        JknBelum.setText(""+jkn_belum);
        JknSelesai.setText(""+jkn_selesai);
        JknCapaian.setText("("+jkn_capaian_angka+"%)");
        MJknBelum.setText(""+mjkn_belum);
        MJknSelesai.setText(""+mjkn_selesai);
        MJknCapaian.setText("("+mjkn_capaian_angka+"%)");
        NonJKNBelum.setText(""+umum_belum);
        NonJKNSelesai.setText(""+umum_selesai);
        // Tampilkan ke label:
        totalantrol.setText("Total Antrol: " + Valid.SetAngka(total_capaian) + " %");//tamabahan

        // Ubah warna label sesuai capaian:
        if(total_capaian < 75){
            totalantrol.setForeground(Color.RED);
        } else if(total_capaian <= 95){
            totalantrol.setForeground(Color.ORANGE);
        } else {
            totalantrol.setForeground(new Color(0, 128, 0)); // Hijau tua
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
                // Ambil kode booking dari tabel
                String kodebooking = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 0).toString();
                // Updated to get sumber data from column index 13 (shifted due to new No Rawat and No SEP columns)
                String sumberData = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 11).toString();

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

                        // Tentukan kode booking yang akan digunakan
                        // kodebooking di kolom 0 = nilai nobooking di tabel referensi_mobilejkn_bpjs
                        String kodeBookingRequest = kodebooking;
                        if (sumberData.equals("Bridging Antrean")) {
                            // Verifikasi nobooking ada di database lokal
                            String nobooking = Sequel.cariIsi(
                                "select nobooking from referensi_mobilejkn_bpjs where nobooking=?", kodebooking);
                            if (!nobooking.isEmpty()) {
                                kodeBookingRequest = nobooking;
                            } else {
                                // Fallback: coba cari berdasarkan no_rawat (No. Ref dari kolom 10)
                                String noRef = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 10).toString();
                                if (!noRef.isEmpty()) {
                                    String nbk = Sequel.cariIsi(
                                        "select nobooking from referensi_mobilejkn_bpjs where no_rawat=?", noRef);
                                    if (!nbk.isEmpty()) {
                                        kodeBookingRequest = nbk;
                                    }
                                }
                                System.out.println("Info: menggunakan kodeBookingRequest=" + kodeBookingRequest);
                            }
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
                                if (sumberData.equals("Bridging Antrean")) {
                                    Sequel.queryu2(
                                        "update referensi_mobilejkn_bpjs set status='Batal' where nobooking='" + kodeBookingRequest + "'");
                                    System.out.println("Update status Batal untuk nobooking: " + kodeBookingRequest);
                                }
                            } catch (Exception e) {
                                System.out.println("Error update database lokal: " + e);
                            }

                            // Refresh tampilan
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
    
    private void KirimUlangMJKN() {                                             
        // TODO add your handling code here: JKN
        kodebooking = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 0).toString();
        data = kodebooking; // tetap dipakai sebagai field "kodebooking" pada JSON ke BPJS

        // === PERBAIKAN UTAMA: resolve no_rawat internal dari nobooking BPJS ===
        String no_rawat = Sequel.cariIsi(
            "SELECT no_rawat FROM referensi_mobilejkn_bpjs WHERE nobooking=?", kodebooking);

        if (no_rawat == null || no_rawat.equals("")) {
            System.out.println("GAGAL: no_rawat tidak ditemukan untuk nobooking=" + kodebooking + ", proses dibatalkan\n");
            return;
        }

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
                    System.out.println("respon WS BPJS : 208 TaskId=1 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','1','" + parsedDate.getTime() + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 1 MJKN = BELUM add adtrian, SKIP KIRIM " + kodebooking + " (no_rawat=" + no_rawat + ")");
        }
                // ---------- TASK ID 2 (selesai admisi) ----------
        datajam = Sequel.cariIsi("select DATE_ADD(referensi_mobilejkn_bpjs.validasi, INTERVAL FLOOR(1 + RAND() * 2) MINUTE) from referensi_mobilejkn_bpjs where referensi_mobilejkn_bpjs.no_rawat='" + no_rawat + "'");    
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='2' and no_rawat='" + no_rawat + "'") > 0) {
                    System.out.println("respon WS BPJS : 208 TaskId=2 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','2','" + parsedDate.getTime() + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 2 MJKN = BELUM ada TASK ID 1, SKIP KIRIM " + kodebooking + " (no_rawat=" + no_rawat + ")");
        }

        // ---------- TASK ID 3 (mulai tunggu poli) ----------
//        datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT MAX(waktu) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + no_rawat + "' AND taskid='3'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + no_rawat + "')), INTERVAL (300 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
        datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat) from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat='" + no_rawat + "' and pemeriksaan_ralan.nip<>'D000'");    
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='" + no_rawat + "'") > 0) {
                    System.out.println("respon WS BPJS : 208 TaskId=3 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','3','" + parsedDate.getTime() + "')");
                }
                System.out.println("respon WS BPJS : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
            
                }} catch (Exception ex) {
                System.out.println("Notifikasi Bridging : " + ex);
            }
        } else {
            System.out.println("WAKTU TASK ID 3 MJKN = BELUM MENGISI SOAP PERAWAT, SKIP KIRIM " + kodebooking + " (no_rawat=" + no_rawat + ")");
        }
        // ---------- TASK ID 4 (mulai pelayanan poli) ----------
            datajam = Sequel.cariIsi("select antri_masuk_poli.tgl_jam from antri_masuk_poli where antri_masuk_poli.no_rawat = '" + no_rawat + "'");
            boolean butuhFallback4 = false;
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='4' and no_rawat='" + no_rawat + "'") > 0) {
                    System.out.println("respon WS BPJS : 208 TaskId=4 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','4','" + parsedDate.getTime() + "')");
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
                        System.out.println("respon WS BPJS : 208 TaskId=4 sudah ada akan skip\n");
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
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','4','" + parsedDate.getTime() + "')");
                    }
                    System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception ex) {
                    System.out.println("Notifikasi Bridging Fallback : " + ex);
                }
            }
        }
        // ---------- TASK ID 5 (selesai pelayanan poli) ----------
          datajam = Sequel.cariIsi("SELECT antri_masuk_poli.selesai FROM antri_masuk_poli WHERE no_rawat='" + no_rawat + "'");    
          if (datajam == null || datajam.equals("")) {
              datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT FROM_UNIXTIME(MAX(waktu)/1000) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + no_rawat + "' AND taskid='4'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + no_rawat + "')), INTERVAL (240 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
          }
          boolean butuhFallback = false;

        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + no_rawat + "'") > 0) {
                    System.out.println("respon WS BPJS : 208 TaskId=5 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','5','" + parsedDate.getTime() + "')");
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
                        System.out.println("respon WS BPJS : 208 TaskId=5 sudah ada akan skip\n");
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
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + no_rawat + "','5','" + parsedDate.getTime() + "')");
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
            datajam = Sequel.cariIsi("SELECT concat(resep_obat.tgl_perawatan,' ',resep_obat.jam) FROM resep_obat WHERE resep_obat.tgl_perawatan<>'0000-00-00' AND resep_obat.status='ralan' AND resep_obat.no_rawat='" + no_rawat + "' LIMIT 1");
            if (datajam.equals("")) {
                datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT FROM_UNIXTIME(MAX(waktu)/1000) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + no_rawat + "' AND taskid='5'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + no_rawat + "')), INTERVAL (1200 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
            }
            
            boolean butuhFallback6 = false;

            if (datajam != null && !datajam.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + no_rawat + "'") > 0) {
                        System.out.println("respon WS BPJS : 208 TaskId=6 sudah ada akan skip\n");
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
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + no_rawat + "'");
                        String msg = nameNode.path("message").asText();
                        if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                            butuhFallback6 = true;
                        }
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
                        if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + no_rawat + "'") > 0) {
                            System.out.println("respon WS BPJS : 208 TaskId=6 sudah ada akan skip\n");
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
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + no_rawat + "'");
                        }
                        System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                    
                        }} catch (Exception e) {
                        System.out.println("Gagal memproses waktu fallback task 6: " + e.getMessage());
                    }
                }
            }

            // INJECT TASK 7
            datajam = Sequel.cariIsi("SELECT concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan) FROM resep_obat WHERE resep_obat.status='ralan' AND resep_obat.no_rawat='" + no_rawat + "' AND concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan)<>'0000-00-00 00:00:00' LIMIT 1");
            if (datajam.equals("")) {
                datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT FROM_UNIXTIME(MAX(waktu)/1000) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + no_rawat + "' AND taskid='6'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + no_rawat + "')), INTERVAL (1500 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
            }
            
            boolean butuhFallback7 = false;

            if (datajam != null && !datajam.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + no_rawat + "'") > 0) {
                        System.out.println("respon WS BPJS : 208 TaskId=7 sudah ada akan skip\n");
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
                        Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + no_rawat + "'");
                        String msg = nameNode.path("message").asText();
                        if (nameNode.path("code").asText().equals("201") && msg.toLowerCase().contains("kurang atau sama")) {
                            butuhFallback7 = true;
                        }
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
                        if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + no_rawat + "'") > 0) {
                            System.out.println("respon WS BPJS : 208 TaskId=7 sudah ada akan skip\n");
                        } else {
                        String cleanTime = task6Time.replaceAll(" WIB| WITA| WIT", "");
                        String timeOnly = "";
                        if (cleanTime.contains(" ")) {
                            timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                        }
                        String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + no_rawat + "'");
                        java.util.Date date6 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                        long randomAdd = 600000 + (long)(Math.random() * 300000);
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
                            Sequel.queryu2("delete from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + no_rawat + "'");
                        }
                        System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                    
                        }} catch (Exception e) {
                        System.out.println("Gagal memproses waktu fallback task 7: " + e.getMessage());
                    }
                }
            }
        } else {
            System.out.println("TIDAK ADA RESEP OBAT, SKIP TASK 6 DAN 7 UNTUK " + kodebooking + " (no_rawat=" + no_rawat + ")");
        }
    }

    private void KirimUlangJKN() {                                              
            // TODO add your handling code here: JKN
        kodebooking = tbJnsPerawatan.getValueAt(tbJnsPerawatan.getSelectedRow(), 0).toString();
        data = kodebooking; // tetap dipakai sebagai field "kodebooking" pada JSON ke BPJS

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
                    System.out.println("respon WS BPJS : 208 TaskId=1 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','1','" + parsedDate.getTime() + "')");
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
                    System.out.println("respon WS BPJS : 208 TaskId=2 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','2','" + parsedDate.getTime() + "')");
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
        datajam = Sequel.cariIsi("select concat(pemeriksaan_ralan.tgl_perawatan,' ',pemeriksaan_ralan.jam_rawat) from pemeriksaan_ralan where pemeriksaan_ralan.no_rawat='" + kodebooking + "' and pemeriksaan_ralan.nip<>'D000'");    
        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='3' and no_rawat='" + kodebooking + "'") > 0) {
                    System.out.println("respon WS BPJS : 208 TaskId=3 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','3','" + parsedDate.getTime() + "')");
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
                    System.out.println("respon WS BPJS : 208 TaskId=4 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','4','" + parsedDate.getTime() + "')");
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
                        System.out.println("respon WS BPJS : 208 TaskId=4 sudah ada akan skip\n");
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
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','4','" + parsedDate.getTime() + "')");
                    }
                    System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                
                    }} catch (Exception ex) {
                    System.out.println("Notifikasi Bridging Fallback : " + ex);
                }
            }
        }
        // ---------- TASK ID 5 (selesai pelayanan poli) ----------
          datajam = Sequel.cariIsi("SELECT antri_masuk_poli.selesai FROM antri_masuk_poli WHERE no_rawat='" + kodebooking + "'");    
          if (datajam == null || datajam.equals("")) {
              datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT FROM_UNIXTIME(MAX(waktu)/1000) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + kodebooking + "' AND taskid='4'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + kodebooking + "')), INTERVAL (240 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
          }
          boolean butuhFallback = false;

        if (datajam != null && !datajam.equals("")) {
            try {
                if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='5' and no_rawat='" + kodebooking + "'") > 0) {
                    System.out.println("respon WS BPJS : 208 TaskId=5 sudah ada akan skip\n");
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
                    Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','5','" + parsedDate.getTime() + "')");
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
                        System.out.println("respon WS BPJS : 208 TaskId=5 sudah ada akan skip\n");
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
                        Sequel.queryu2("insert into referensi_mobilejkn_bpjs_taskid values('" + kodebooking + "','5','" + parsedDate.getTime() + "')");
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
            datajam = Sequel.cariIsi("SELECT concat(resep_obat.tgl_perawatan,' ',resep_obat.jam) FROM resep_obat WHERE resep_obat.tgl_perawatan<>'0000-00-00' AND resep_obat.status='ralan' AND resep_obat.no_rawat='" + kodebooking + "' LIMIT 1");
            if (datajam.equals("")) {
                datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT FROM_UNIXTIME(MAX(waktu)/1000) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + kodebooking + "' AND taskid='5'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + kodebooking + "')), INTERVAL (1200 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
            }
            
            boolean butuhFallback6 = false;

            if (datajam != null && !datajam.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='6' and no_rawat='" + kodebooking + "'") > 0) {
                        System.out.println("respon WS BPJS : 208 TaskId=6 sudah ada akan skip\n");
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
                            System.out.println("respon WS BPJS : 208 TaskId=6 sudah ada akan skip\n");
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
                        }
                        System.out.println("respon WS BPJS (Fallback) : " + nameNode.path("code").asText() + " " + nameNode.path("message").asText() + "\n");
                    
                        }} catch (Exception e) {
                        System.out.println("Gagal memproses waktu fallback task 6: " + e.getMessage());
                    }
                }
            }

            // INJECT TASK 7
            datajam = Sequel.cariIsi("SELECT concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan) FROM resep_obat WHERE resep_obat.status='ralan' AND resep_obat.no_rawat='" + kodebooking + "' AND concat(resep_obat.tgl_penyerahan,' ',resep_obat.jam_penyerahan)<>'0000-00-00 00:00:00' LIMIT 1");
            if (datajam.equals("")) {
                datajam = Sequel.cariIsi("SELECT DATE_FORMAT(DATE_ADD(IFNULL((SELECT FROM_UNIXTIME(MAX(waktu)/1000) FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat='" + kodebooking + "' AND taskid='6'), (SELECT concat(tgl_registrasi,' ',jam_reg) FROM reg_periksa WHERE no_rawat='" + kodebooking + "')), INTERVAL (1500 + FLOOR(RAND() * 59)) SECOND), '%Y-%m-%d %H:%i:%s')");
            }
            
            boolean butuhFallback7 = false;

            if (datajam != null && !datajam.equals("")) {
                try {
                    if (Sequel.cariInteger("select count(no_rawat) from referensi_mobilejkn_bpjs_taskid where taskid='7' and no_rawat='" + kodebooking + "'") > 0) {
                        System.out.println("respon WS BPJS : 208 TaskId=7 sudah ada akan skip\n");
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
                            System.out.println("respon WS BPJS : 208 TaskId=7 sudah ada akan skip\n");
                        } else {
                        String cleanTime = task6Time.replaceAll(" WIB| WITA| WIT", "");
                        String timeOnly = "";
                        if (cleanTime.contains(" ")) {
                            timeOnly = cleanTime.substring(cleanTime.lastIndexOf(" ") + 1);
                        }
                        String tglPelayanan = Sequel.cariIsi("select tgl_registrasi from reg_periksa where no_rawat='" + kodebooking + "'");
                        java.util.Date date6 = dateFormat.parse(tglPelayanan + " " + timeOnly);
                        long randomAdd = 600000 + (long)(Math.random() * 300000);
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
