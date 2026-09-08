package khanzahmsservicefonntetaskid;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.swing.Timer;

public class frmUtama extends javax.swing.JFrame {

    private SimpleDateFormat tanggalFormat = new SimpleDateFormat("yyyy-MM-dd");
    private Date date = new Date();  

    public frmUtama() {
        initComponents();
        
        this.setSize(390,340);
        
        date = new Date();  
        Tanggal1.setText(tanggalFormat.format(date)); 
        Tanggal2.setText(tanggalFormat.format(date)); 
        jam();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        TeksArea = new javax.swing.JTextArea();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        Tanggal1 = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        Tanggal2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Khanza Service Fonnte Auto Sender");

        TeksArea.setColumns(20);
        TeksArea.setRows(5);
        TeksArea.setEditable(false);
        TeksArea.setText("Service dijalankan. Menunggu jadwal pengiriman...\n");
        jScrollPane1.setViewportView(TeksArea);

        getContentPane().add(jScrollPane1, java.awt.BorderLayout.CENTER);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setText("Tanggal :");
        jLabel1.setPreferredSize(new java.awt.Dimension(70, 23));
        jPanel1.add(jLabel1);

        Tanggal1.setPreferredSize(new java.awt.Dimension(100, 23));
        Tanggal1.setEditable(false);
        jPanel1.add(Tanggal1);

        jLabel3.setText("s.d.");
        jLabel3.setPreferredSize(new java.awt.Dimension(28, 23));
        jPanel1.add(jLabel3);

        Tanggal2.setPreferredSize(new java.awt.Dimension(100, 23));
        Tanggal2.setEditable(false);
        jPanel1.add(Tanggal2);

        lblJam = new javax.swing.JLabel();
        lblJam.setText("00:00:00");
        lblJam.setPreferredSize(new java.awt.Dimension(60, 23));
        jPanel1.add(lblJam);

        jLabel2.setPreferredSize(new java.awt.Dimension(10, 23));
        jPanel1.add(jLabel2);

        jButton2.setText("Test Kirim");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeksArea.append("Mencoba mengirim secara manual...\n");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        LaporanTaskID.kirimLaporan();
                    }
                }).start();
            }
        });
        jPanel1.add(jButton2);

        btnBatal = new javax.swing.JButton();
        btnBatal.setText("Test Batal Otomatis");
        btnBatal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TeksArea.append("Mencoba batal otomatis secara manual...\n");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BatalOtomatisMJKN.prosesBatalOtomatis();
                    }
                }).start();
            }
        });
        jPanel1.add(btnBatal);

        jButton1.setText("Keluar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);

        getContentPane().add(jPanel1, java.awt.BorderLayout.PAGE_END);

        pack();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        System.exit(0);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmUtama.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmUtama().setVisible(true);
            }
        });
    }

    private javax.swing.JTextField Tanggal1;
    private javax.swing.JTextField Tanggal2;
    public static javax.swing.JTextArea TeksArea;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton btnBatal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lblJam; // Label jam realtime
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    
    private boolean sudahKirimHariIni = false;
    private boolean sudahBatalHariIni = false;
    
    private void jam(){
        ActionListener taskPerformer = new ActionListener(){
            private int nilai_jam;
            private int nilai_menit;
            private int nilai_detik;
            public void actionPerformed(ActionEvent e) {
                Date now = Calendar.getInstance().getTime();
                nilai_jam = now.getHours();
                nilai_menit = now.getMinutes();
                nilai_detik = now.getSeconds();

                String jam = (nilai_jam <= 9 ? "0" : "") + nilai_jam;
                String menit = (nilai_menit <= 9 ? "0" : "") + nilai_menit;
                String detik = (nilai_detik <= 9 ? "0" : "") + nilai_detik;
                
                if (lblJam != null) {
                    lblJam.setText(jam + ":" + menit + ":" + detik);
                }
                
                // Clear log everyday at 00:00:01
                if(jam.equals("00") && menit.equals("00") && detik.equals("01")){
                    TeksArea.setText("");
                    date = new Date();  
                    Tanggal1.setText(tanggalFormat.format(date)); 
                    Tanggal2.setText(tanggalFormat.format(date)); 
                    sudahKirimHariIni = false; // Reset flag kirim untuk hari berikutnya
                    sudahBatalHariIni = false;
                }
                
                // Cek jam 21:00 untuk mengirim laporan (tidak mengandalkan detik==00 agar tidak terlewat)
                if (jam.equals("21") && menit.equals("00")) {
                    if (!sudahKirimHariIni) {
                        sudahKirimHariIni = true;
                        TeksArea.append("Menjalankan Auto Report Fonnte Task ID ke WA (" + jam + ":" + menit + ":" + detik + ")...\n");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                LaporanTaskID.kirimLaporan();
                            }
                        }).start();
                    }
                }
                
                // Cek jam 20:35 untuk Batal Otomatis
                if (jam.equals("20") && menit.equals("35")) {
                    if (!sudahBatalHariIni) {
                        sudahBatalHariIni = true;
                        TeksArea.append("Menjalankan Auto Cancel MJKN (" + jam + ":" + menit + ":" + detik + ")...\n");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                BatalOtomatisMJKN.prosesBatalOtomatis();
                            }
                        }).start();
                    }
                }
            }
        };
        // Timer diset untuk berjalan setiap 1 detik
        new Timer(1000, taskPerformer).start();
    }
}
