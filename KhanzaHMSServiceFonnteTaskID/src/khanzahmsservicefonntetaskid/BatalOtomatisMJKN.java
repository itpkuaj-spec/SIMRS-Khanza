package khanzahmsservicefonntetaskid;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.ApiMobileJKN;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

public class BatalOtomatisMJKN {
    private static final Properties prop = new Properties();

    public static void prosesBatalOtomatis() {
        Connection koneksi = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            // Load settings
            try (FileInputStream fis = new FileInputStream("setting/database.xml")) {
                prop.loadFromXML(fis);
            }
            String tokenFonnte = prop.getProperty("TOKENFONTE");
            // Check if user set a specific number for cancel, otherwise use default admin
            String noWaAdmin = prop.getProperty("NOWAADMINBATAL");
            if (noWaAdmin == null || noWaAdmin.isEmpty()) {
                noWaAdmin = prop.getProperty("WAGADMINBPJS");
            }

            if (tokenFonnte == null || noWaAdmin == null || tokenFonnte.isEmpty() || noWaAdmin.isEmpty()) {
                frmUtama.TeksArea.append("Batal Otomatis Gagal: TOKENFONNTE atau WAGADMINBPJS tidak ditemukan di setting/database.xml\n");
                return;
            }

            koneksi = koneksiDB.condb();
            if (koneksi == null || koneksi.isClosed()) {
                frmUtama.TeksArea.append("Batal Otomatis Gagal: Tidak terhubung ke database!\n");
                return;
            }

            // Get current date
            Date currentDate = new Date();
            SimpleDateFormat formatTgl = new SimpleDateFormat("yyyy-MM-dd");
            String tglSekarang = formatTgl.format(currentDate);

            // Query data pasien yang statusnya masih 'Belum' hari ini
            // Menggunakan inner join mapping bpjs agar IGD/Umum tidak ikut terbawa
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
                "MAX(CASE WHEN t.taskid = '99' THEN DATE_FORMAT(t.waktu, '%H:%i:%s') END) AS task99, " +
                "m.nobooking " +
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
                "WHERE r.tgl_registrasi BETWEEN ? AND ? AND r.stts = 'Belum' " +
                "GROUP BY r.no_rawat, r.no_rkm_medis, p.nm_pasien, d.nm_dokter, poli.nm_poli, sep.no_sep, r.tgl_registrasi, m.no_rawat, m.status, r.stts, m.nobooking " +
                "ORDER BY r.tgl_registrasi ASC, r.no_rawat ASC";

            ps = koneksi.prepareStatement(sql);
            ps.setString(1, tglSekarang);
            ps.setString(2, tglSekarang);
            rs = ps.executeQuery();

            ApiMobileJKN api = new ApiMobileJKN();
            ObjectMapper mapper = new ObjectMapper();
            String link = koneksiDB.URLAPIMOBILEJKN();

            int countBatal = 0;

            while (rs.next()) {
                String noRawat = rs.getString("no_rawat");
                String nmPasien = rs.getString("nm_pasien");
                String nmPoli = rs.getString("nm_poli");
                String sumberAntrean = rs.getString("sumber_antrean");
                String nobooking = rs.getString("nobooking");

                frmUtama.TeksArea.append("Membatalkan antrean ("+sumberAntrean+"): " + noRawat + " - " + nmPasien + "\n");

                // 1. Tentukan kode booking yang dikirim
                String kodeBookingRequest = noRawat; // default Non Mobile JKN
                if (sumberAntrean.equals("Mobile JKN") && nobooking != null && !nobooking.trim().isEmpty()) {
                    kodeBookingRequest = nobooking; // gunakan nobooking asli jika dari Mobile JKN
                }

                // 2. Request Batal ke BPJS API
                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    String utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                    String requestJson = "{"
                            + "\"kodebooking\": \"" + kodeBookingRequest + "\","
                            + "\"keterangan\": \"Tidak Hadir\""
                            + "}";

                    HttpEntity<String> requestEntity = new HttpEntity<>(requestJson, headers);
                    String URL = link + "/antrean/batal";
                    
                    // Kita execute API, error diabaikan agar proses tetap berlanjut (misal utk pasien umum)
                    JsonNode root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
                    JsonNode nameNode = root.path("metadata");
                } catch (Exception ex) {
                    // Abaikan error koneksi BPJS jika pasien umum atau server BPJS down
                }

                // 3. Update Database Lokal
                String updateReg = "UPDATE reg_periksa SET stts='Batal', biaya_reg=0 WHERE no_rawat=?";
                try (PreparedStatement ps3 = koneksi.prepareStatement(updateReg)) {
                    ps3.setString(1, noRawat);
                    ps3.executeUpdate();
                }

                String updateRef = "UPDATE referensi_mobilejkn_bpjs SET status='Batal' WHERE no_rawat=? OR nobooking=?";
                try (PreparedStatement ps4 = koneksi.prepareStatement(updateRef)) {
                    ps4.setString(1, noRawat);
                    ps4.setString(2, kodeBookingRequest);
                    ps4.executeUpdate();
                }

                // Insert Task ID 99
                String checkTaskSql = "SELECT count(*) AS jml FROM referensi_mobilejkn_bpjs_taskid WHERE no_rawat=? AND taskid='99'";
                boolean taskExists = false;
                try (PreparedStatement ps5 = koneksi.prepareStatement(checkTaskSql)) {
                    ps5.setString(1, noRawat);
                    try (ResultSet rs5 = ps5.executeQuery()) {
                        if (rs5.next() && rs5.getInt("jml") > 0) {
                            taskExists = true;
                        }
                    }
                }

                SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String nowTime = dateTimeFormat.format(new Date());

                if (taskExists) {
                    String updateTask = "UPDATE referensi_mobilejkn_bpjs_taskid SET waktu=? WHERE no_rawat=? AND taskid='99'";
                    try (PreparedStatement ps6 = koneksi.prepareStatement(updateTask)) {
                        ps6.setString(1, nowTime);
                        ps6.setString(2, noRawat);
                        ps6.executeUpdate();
                    }
                } else {
                    String insertTask = "INSERT INTO referensi_mobilejkn_bpjs_taskid (no_rawat, taskid, waktu) VALUES (?, '99', ?)";
                    try (PreparedStatement ps7 = koneksi.prepareStatement(insertTask)) {
                        ps7.setString(1, noRawat);
                        ps7.setString(2, nowTime);
                        ps7.executeUpdate();
                    }
                }

                // 4. Kirim WA ke Admin
                String waMessage = "❌ *Batal Otomatis (Tidak Hadir)*\n\n"
                        + "No Rawat : " + noRawat + "\n"
                        + "Pasien : " + nmPasien + "\n"
                        + "Poli : " + nmPoli + "\n\n"
                        + "Status periksa telah dibatalkan (Biaya Reg 0).";

                kirimWaFonnte(tokenFonnte, noWaAdmin, waMessage);
                countBatal++;

                // Jeda 2 detik setiap baris agar tidak hit API BPJS/Fonnte terlalu cepat
                Thread.sleep(2000); 
            }

            frmUtama.TeksArea.append("Proses Batal Otomatis selesai. Total dibatalkan: " + countBatal + "\n");

        } catch (Exception e) {
            frmUtama.TeksArea.append("Error Batal Otomatis: " + e.getMessage() + "\n");
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

    private static void kirimWaFonnte(String token, String target, String message) {
        try {
            URL url = new URL("https://api.fonnte.com/send");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Authorization", token);
            http.setRequestProperty("Content-Type", "application/json");

            String jsonInputString = "{\"target\": \"" + target + "\", \"message\": \"" + message.replace("\n", "\\n").replace("\"", "\\\"") + "\"}";

            try (OutputStream os = http.getOutputStream()) {
                byte[] input = jsonInputString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }
            http.getResponseCode(); // Execute request
        } catch (Exception e) {
            System.err.println("Gagal WA Fonnte: " + e.getMessage());
        }
    }
}
