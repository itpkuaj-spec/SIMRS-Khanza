package khanzahmsservicefonntetaskid;

import fungsi.koneksiDB;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class LaporanTaskID {
    private static final Properties prop = new Properties();

    public static void kirimLaporan() {
        Connection koneksi = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Load settings
            try (FileInputStream fis = new FileInputStream("setting/database.xml")) {
                prop.loadFromXML(fis);
            }
            String tokenFonnte = prop.getProperty("TOKENFONTE");
            String noWa = prop.getProperty("WAGADMINBPJS");

            if (tokenFonnte == null || noWa == null || tokenFonnte.isEmpty() || noWa.isEmpty()) {
                frmUtama.TeksArea.append("Gagal: TOKENFONNTE atau NOWATUJUAN tidak ditemukan di setting/database.xml\n");
                return;
            }

            koneksi = koneksiDB.condb();
            if (koneksi == null || koneksi.isClosed()) {
                frmUtama.TeksArea.append("Gagal terhubung ke database!\n");
                return;
            }

            // Get current date
            Date currentDate = new Date();
            SimpleDateFormat formatTgl = new SimpleDateFormat("yyyy-MM-dd");
            String tglSekarang = formatTgl.format(currentDate);

            // Statistics variables
            int tot_batal = 0;
            int tot_sep = 0;
            int tot_mjkn = 0;
            int tot_nonmjkn = 0;
            int tot_belum = 0;
            int tot_selesai = 0;

            int missingT1 = 0, missingT2 = 0, missingT3 = 0, missingT4 = 0, missingT5 = 0, missingT6 = 0, missingT7 = 0;
            long totalWaktuPoli = 0;
            int countPoli = 0;
            long totalWaktuRS = 0;
            int countRS = 0;

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            String sql = "SELECT r.no_rawat, r.no_rkm_medis, p.nm_pasien, IFNULL(d.nm_dokter, '-') AS nm_dokter, IFNULL(poli.nm_poli, '-') AS nm_poli, IFNULL(sep.no_sep, '-') AS no_sep, r.tgl_registrasi, " +
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
                "ORDER BY r.tgl_registrasi ASC, r.no_rawat ASC";

            ps = koneksi.prepareStatement(sql);
            ps.setString(1, tglSekarang);
            ps.setString(2, tglSekarang);
            rs = ps.executeQuery();

            int rowCount = 0;

            while (rs.next()) {
                rowCount++;
                String noSep = rs.getString("no_sep");
                String sumber = rs.getString("sumber_antrean");
                String sttsResep = rs.getString("stts_resep");
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
                } else {
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

                if (t99.equals("-")) {
                    if (t1.equals("-")) missingT1++;
                    if (t2.equals("-")) missingT2++;
                    if (t3.equals("-")) missingT3++;
                    if (t4.equals("-")) missingT4++;
                    if (t5.equals("-")) missingT5++;
                    if (sttsResep.equals("Ada")) {
                        if (t6.equals("-")) missingT6++;
                        if (t7.equals("-")) missingT7++;
                    }
                }

                if (statusTaskId.equals("LENGKAP")) {
                    tot_selesai++;
                } else if (statusTaskId.equals("BATAL")) {
                    tot_batal++;
                } else {
                    tot_belum++;
                }

                if (t99.equals("-")) {
                    if (!t2.equals("-") && !t4.equals("-")) {
                        try {
                            Date d2 = dateFormat.parse(tglSekarang + " " + t2);
                            Date d4 = dateFormat.parse(tglSekarang + " " + t4);
                            long diff = d4.getTime() - d2.getTime();
                            if (diff < 0) diff += 24 * 60 * 60 * 1000;
                            totalWaktuPoli += diff;
                            countPoli++;
                        } catch (Exception ex) {}
                    }
                    if (sttsResep.equals("Ada")) {
                        if (!t1.equals("-") && !t7.equals("-")) {
                            try {
                                Date d1 = dateFormat.parse(tglSekarang + " " + t1);
                                Date d7 = dateFormat.parse(tglSekarang + " " + t7);
                                long diff = d7.getTime() - d1.getTime();
                                if (diff < 0) diff += 24 * 60 * 60 * 1000;
                                totalWaktuRS += diff;
                                countRS++;
                            } catch (Exception ex) {}
                        }
                    } else {
                        if (!t1.equals("-") && !t5.equals("-")) {
                            try {
                                Date d1 = dateFormat.parse(tglSekarang + " " + t1);
                                Date d5 = dateFormat.parse(tglSekarang + " " + t5);
                                long diff = d5.getTime() - d1.getTime();
                                if (diff < 0) diff += 24 * 60 * 60 * 1000;
                                totalWaktuRS += diff;
                                countRS++;
                            } catch (Exception ex) {}
                        }
                    }
                }
            }

            int totalPasienValid = tot_selesai + tot_belum;
            double persenLengkap = (totalPasienValid == 0) ? 0 : ((double) tot_selesai / totalPasienValid) * 100;
            String persenAntrol = String.format("%.2f", persenLengkap).replace(",", ".") + "%";

            String avgPoliStr = "00:00:00";
            if (countPoli > 0) {
                long s = (totalWaktuPoli / countPoli) / 1000;
                avgPoliStr = String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
            }

            String avgRSStr = "00:00:00";
            if (countRS > 0) {
                long s = (totalWaktuRS / countRS) / 1000;
                avgRSStr = String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60));
            }

            // Membangun string laporan
            StringBuilder pesanBuilder = new StringBuilder();
            pesanBuilder.append("*Laporan Harian Task ID - RS PKU Aisyiyah Jepara*\n");
            pesanBuilder.append("Tanggal: ").append(new SimpleDateFormat("dd-MM-yyyy").format(currentDate)).append("\n\n");
            
            pesanBuilder.append("📊 *Statistik Antrean*\n");
            pesanBuilder.append("- Total Pasien : ").append(rowCount).append("\n");
            pesanBuilder.append("- Mobile JKN   : ").append(tot_mjkn).append("\n");
            pesanBuilder.append("- Non M-JKN    : ").append(tot_nonmjkn).append("\n");
            pesanBuilder.append("- SEP Terbit   : ").append(tot_sep).append("\n\n");

            pesanBuilder.append("📋 *Kelengkapan Task ID*\n");
            pesanBuilder.append("- Lengkap      : ").append(tot_selesai).append("\n");
            pesanBuilder.append("- Tdk Lengkap  : ").append(tot_belum).append("\n");
            pesanBuilder.append("- Batal        : ").append(tot_batal).append("\n");
            pesanBuilder.append("- Capaian      : ").append(persenAntrol).append("\n\n");

            pesanBuilder.append("⏳ *Waktu Tunggu*\n");
            pesanBuilder.append("- Rata2 Tunggu Poli : ").append(avgPoliStr).append("\n");
            pesanBuilder.append("- Rata2 Pasien RS   : ").append(avgRSStr).append("\n\n");

            pesanBuilder.append("⚠️ *Belum Terkirim*\n");
            pesanBuilder.append("- T1: ").append(missingT1).append(" | T2: ").append(missingT2).append(" | T3: ").append(missingT3).append(" | T4: ").append(missingT4).append("\n");
            pesanBuilder.append("- T5: ").append(missingT5).append(" | T6: ").append(missingT6).append(" | T7: ").append(missingT7);

            String finalPesan = pesanBuilder.toString();
            frmUtama.TeksArea.append("Mengirim laporan ke " + noWa + " via Fonnte...\n");

            // Mengirim ke Fonnte
            URL url = new URL("https://api.fonnte.com/send");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Authorization", tokenFonnte);
            http.setRequestProperty("Content-Type", "application/json");

            // Build JSON payload manually
            String jsonInputString = "{\"target\": \"" + noWa + "\", \"message\": \"" + finalPesan.replace("\n", "\\n").replace("\"", "\\\"") + "\"}";

            try (OutputStream os = http.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = http.getResponseCode();
            if (responseCode == 200) {
                frmUtama.TeksArea.append("Berhasil mengirim pesan Fonnte! Kode: " + responseCode + "\n");
            } else {
                frmUtama.TeksArea.append("Gagal mengirim pesan Fonnte. Kode: " + responseCode + "\n");
            }

        } catch (Exception e) {
            frmUtama.TeksArea.append("Error saat mengirim Laporan Task ID Fonnte: " + e.getMessage() + "\n");
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
