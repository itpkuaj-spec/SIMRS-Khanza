package tambahan_it;

import bridging.ApiMobileJKN;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.koneksiDB;
import fungsi.sekuel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;


public class antrianOnlineController {
    
    private final Connection koneksi=koneksiDB.condb();
    private sekuel Sequel=new sekuel();
    private PreparedStatement ps,ps2,ps3;
    private ResultSet rs,rs2,rs3;
    private final ApiMobileJKN api=new ApiMobileJKN();
    private String URL="",link="",utc="",requestJson="";
    private HttpHeaders headers;
    private HttpEntity requestEntity;
    private final ObjectMapper mapper = new ObjectMapper();
    private JsonNode root;
    private JsonNode nameNode;
    private JsonNode response;
    private RestTemplate rest = new RestTemplate();
    private Date parsedDate;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final String datajam = "";
    
    public Map<String, Map<String, Integer>> getDataAntrean(String tglAwal, String tglAkhir, String mode, String sumberCari) {
        try {
            link = koneksiDB.URLAPIMOBILEJKN();
        } catch (Exception e) {
            System.out.println("E : " + e);
        }

        Map<String, Map<String, Integer>> dataAntrean = new LinkedHashMap<>();

        try {
            ps = koneksi.prepareStatement(
                "SELECT reg_periksa.tgl_registrasi FROM reg_periksa " +
                "WHERE reg_periksa.tgl_registrasi BETWEEN ? AND ? GROUP BY reg_periksa.tgl_registrasi"
            );
            ps.setString(1, tglAwal);
            ps.setString(2, tglAkhir);
            rs = ps.executeQuery();

            while (rs.next()) {
                String tgl = rs.getString("tgl_registrasi");
                int belum = 0, sudah = 0, sedang = 0,batal = 0,total = 0;

                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                    requestEntity = new HttpEntity(headers);
                    URL = link + "/antrean/pendaftaran/tanggal/" + tgl;
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");

                    if (nameNode.path("code").asText().equals("200")) {
                        JsonNode response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                        if (response.isArray()) {
                            for (JsonNode list : response) {
                                String sumber = list.path("sumberdata").asText();
                                String status = list.path("status").asText();
                                boolean isPeserta = list.path("ispeserta").asBoolean();

                                boolean cocok = false;
                                if ("Mobile JKN".equalsIgnoreCase(sumberCari)) {
                                    cocok = isPeserta && "Mobile JKN".equals(sumber);
                                } else if ("Bridging Antrean".equalsIgnoreCase(sumberCari)) {
                                    cocok = !isPeserta && "Bridging Antrean".equals(sumber);
                                } else if ("Semua".equalsIgnoreCase(sumberCari)) {
                                    cocok = true; // ambil semua data
                                }

                                if (cocok) {
                                    switch (status) {
                                        case "Belum dilayani": belum++; break;
                                        case "Sedang dilayani": sedang++; break;
                                        case "Selesai dilayani": sudah++; break;
                                        case "Batal": batal++; break;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Notifikasi: " + e.getMessage());
                }

                String label;
                if (mode.equals("Tahun")) {
                    int bln = Integer.parseInt(tgl.substring(5, 7));
                    String[] bulanIndo = {
                        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                    };
                    label = bulanIndo[bln - 1];
                } else {
                    label = String.valueOf(Integer.parseInt(tgl.substring(8, 10)));
                }

                Map<String, Integer> row = dataAntrean.getOrDefault(label, new HashMap<>());
                row.put("Belum dilayani", row.getOrDefault("Belum dilayani", 0) + belum);
                row.put("Sedang dilayani", row.getOrDefault("Sedang dilayani", 0) + sedang);
                row.put("Selesai dilayani", row.getOrDefault("Selesai dilayani", 0) + sudah);
                row.put("Batal", row.getOrDefault("Batal", 0) + batal);
                
                total = belum + sedang + sudah + batal;
                row.put("Total", row.getOrDefault("Total", 0) + total);
                dataAntrean.put(label, row);
            }
        } catch (Exception ex) {
            System.out.println("Notifikasi : " + ex);
        }

        return dataAntrean;
    }
    
    public Map<String, Map<String, Integer>> getDataSumber(String tglAwal, String tglAkhir, String mode) {
         try {
            link = koneksiDB.URLAPIMOBILEJKN();
        } catch (Exception e) {
            System.out.println("E : " + e);
        }
        Map<String, Map<String, Integer>> data = new LinkedHashMap<>();

        try {
            ps2 = koneksi.prepareStatement(
                "SELECT reg_periksa.tgl_registrasi FROM reg_periksa " +
                "WHERE reg_periksa.tgl_registrasi BETWEEN ? AND ? GROUP BY reg_periksa.tgl_registrasi"
            );
            ps2.setString(1, tglAwal);
            ps2.setString(2, tglAkhir);
            rs2 = ps2.executeQuery();

            while (rs2.next()) {
                String tgl = rs2.getString("tgl_registrasi");
                int mjkn = 0, bridging = 0;

                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                    requestEntity = new HttpEntity(headers);
                    URL = link + "/antrean/pendaftaran/tanggal/" + tgl;
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");

                    if (nameNode.path("code").asText().equals("200")) {
                        JsonNode response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                        if (response.isArray()) {
                            for (JsonNode list : response) {
                                String sumber = list.path("sumberdata").asText();
                                String status = list.path("status").asText();
                                boolean isPeserta = list.path("ispeserta").asBoolean();

                                if (!"Batal".equalsIgnoreCase(status)) {
                                    if ("Mobile JKN".equalsIgnoreCase(sumber)) {
                                        mjkn++;
                                    }else {
                                        bridging++;
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("API Error: " + e.getMessage());
                }

                String label;
                if (mode.equals("Tahun")) {
                    int bln = Integer.parseInt(tgl.substring(5, 7));
                    String[] bulanIndo = {
                        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
                        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
                    };
                    label = bulanIndo[bln - 1];
                } else {
                    label = String.valueOf(Integer.parseInt(tgl.substring(8, 10)));
                }
                
                Map<String, Integer> row = data.getOrDefault(label, new HashMap<>());
                row.put("Mobile JKN", row.getOrDefault("Mobile JKN", 0) + mjkn);
                row.put("Bridging Antrean", row.getOrDefault("Bridging Antrean", 0) + bridging);
                data.put(label, row);
            }

        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
        }

        return data;
    }
    
    public AntrianResult getDataRefAntrian(String tglAwal, String tglAkhir, String orderby, String sumberData, String keyword) {
        String statusCheckin = "";
        String statusPeriksa = "";
        String statusSoap = "";
        try {
            link = koneksiDB.URLAPIMOBILEJKN();
        } catch (Exception e) {
            System.out.println("E : " + e);
        }
        AntrianResult result = new AntrianResult();
        Map<String, Map<String, Integer>> data = new LinkedHashMap<>();

        try {
            ps2 = koneksi.prepareStatement(
                "SELECT reg_periksa.tgl_registrasi FROM reg_periksa " +
                "WHERE reg_periksa.tgl_registrasi BETWEEN ? AND ? GROUP BY reg_periksa.tgl_registrasi"
            );
            ps2.setString(1, tglAwal);
            ps2.setString(2, tglAkhir);
            rs2 = ps2.executeQuery();

            while (rs2.next()) {
                String tgl = rs2.getString("tgl_registrasi");
                int mjkn = 0, bridging = 0;

                try {
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    headers.add("x-cons-id", koneksiDB.CONSIDAPIMOBILEJKN());
                    utc = String.valueOf(api.GetUTCdatetimeAsString());
                    headers.add("x-timestamp", utc);
                    headers.add("x-signature", api.getHmac(utc));
                    headers.add("user_key", koneksiDB.USERKEYAPIMOBILEJKN());

                    requestEntity = new HttpEntity(headers);
                    URL = link + "/antrean/pendaftaran/tanggal/" + tgl;
                    root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.GET, requestEntity, String.class).getBody());
                    nameNode = root.path("metadata");

                    if (nameNode.path("code").asText().equals("200")) {
                        JsonNode response = mapper.readTree(api.Decrypt(root.path("response").asText(), utc));
                        if (response.isArray()) {
                            for (JsonNode list : response) {
                                String status = list.path("status").asText();
                                String sumber = list.path("sumberdata").asText();
                                if (!sumber.equals(sumberData)) continue;
                                String kodebooking = list.path("kodebooking").asText().toLowerCase();
                                String norekammedis = list.path("norekammedis").asText().toLowerCase();
                                String noRawatNonJKN = Sequel.cariIsi(
                                    "SELECT no_rawat FROM reg_periksa WHERE no_rawat='" + kodebooking + "'"
                                );
                                String noRawatJKN = Sequel.cariIsi(
                                    "SELECT no_rawat FROM referensi_mobilejkn_bpjs WHERE nobooking='" + kodebooking + "'"
                                );
                                
                                if (!keyword.trim().isEmpty()) {
                                    String keywordLower = keyword.toLowerCase();
                                    if (!kodebooking.contains(keywordLower) && !norekammedis.contains(keywordLower)) {
                                        continue;
                                    }
                                }
                                
                                if (!sumber.equals(sumberData)) continue;
                                if ("Mobile JKN".equalsIgnoreCase(sumber)) {
                                    if (!noRawatJKN.isEmpty()) {
                                        statusCheckin = Sequel.cariIsi(
                                            "SELECT status FROM referensi_mobilejkn_bpjs WHERE nobooking='" + kodebooking + "'"
                                        );
                                        statusPeriksa = Sequel.cariIsi(
                                            "SELECT stts FROM reg_periksa WHERE no_rawat='" + noRawatJKN + "'"
                                        );
                                        String soap = Sequel.cariIsi(
                                            "SELECT no_rawat FROM pemeriksaan_ralan WHERE no_rawat='" + noRawatJKN + "'"
                                        );
                                        statusSoap = soap.isEmpty() ? "" : "Ada";
                                    }
                                } else if ("Bridging Antrean".equalsIgnoreCase(sumber)) {
                                    if (!noRawatNonJKN.isEmpty()) {
                                        statusPeriksa = Sequel.cariIsi(
                                            "SELECT stts FROM reg_periksa WHERE no_rawat='" + noRawatNonJKN + "'"
                                        );
                                        String soap = Sequel.cariIsi(
                                            "SELECT no_rawat FROM pemeriksaan_ralan WHERE no_rawat='" + noRawatNonJKN + "'"
                                        );
                                        statusSoap = soap.isEmpty() ? "" : "Ada";
                                    }
                                } 
                                if (orderby.equals("Semua") || orderby.equals(status)) {
                                    result.data.add(new Object[]{
                                        false,
                                        list.path("kodebooking").asText(),
                                        list.path("tanggal").asText(),
                                        list.path("kodepoli").asText(),
                                        list.path("kodedokter").asText(),
                                        list.path("jampraktek").asText(),
                                        list.path("nik").asText(),
                                        list.path("nokapst").asText(),
                                        list.path("nohp").asText(),
                                        list.path("norekammedis").asText(),
                                        list.path("jeniskunjungan").asText(),
                                        list.path("nomorreferensi").asText(),
                                        sumber,
                                        list.path("ispeserta").asText().equals("true") ? "Ya" : "Tidak",
                                        list.path("noantrean").asText(),
                                        list.path("estimasidilayani").asText(),
                                        list.path("createdtime").asText(),
                                        status,
                                        statusCheckin,  // kolom baru status check-in
                                        statusPeriksa,  // kolom baru status periksa
                                        statusSoap
                                    });
                                }
                                if (status.equals("Belum dilayani")) result.tot_belum++;
                                if (status.equals("Selesai dilayani")) result.tot_selesai++;
                                if (status.equals("Batal")) result.tot_batal++;
                            }
                        }
                    }
                } catch (Exception e) {
                    System.out.println("API Error: " + e.getMessage());
                }
            }
            int sep1 = Sequel.cariInteger(
                    "SELECT COUNT(bridging_sep.no_rawat) FROM bridging_sep " +
                    "WHERE bridging_sep.tglsep BETWEEN '" + tglAwal + "' AND '" + tglAkhir + "' " +
                    "AND bridging_sep.jnspelayanan = '2' AND bridging_sep.kdpolitujuan <> 'IGD'");

            int sep2 = Sequel.cariInteger(
                    "SELECT COUNT(bridging_sep_internal.no_rawat) FROM bridging_sep_internal " +
                    "WHERE bridging_sep_internal.tglsep BETWEEN '" + tglAwal + "' AND '" + tglAkhir + "' " +
                    "AND bridging_sep_internal.jnspelayanan = '2' AND bridging_sep_internal.kdpolitujuan <> 'IGD'");

            result.sep = sep1 + sep2;
            if (result.sep > 0) {
                result.jkn_capaian = (result.jkn_selesai * 100) / result.sep;
                result.mjkn_capaian = (result.mjkn_selesai * 100) / result.sep;
            }

        } catch (Exception e) {
            System.out.println("DB Error: " + e.getMessage());
        }

        return result;
    }
    
    public class AntrianResult {
        public List<Object[]> data = new ArrayList<>();
        public int tot_belum = 0, tot_selesai = 0, tot_batal = 0;
        public int jkn_belum = 0, jkn_selesai = 0;
        public int mjkn_belum = 0, mjkn_selesai = 0;
        public int umum_belum = 0, umum_selesai = 0;
        public int sep = 0;
        public int jkn_capaian = 0, mjkn_capaian = 0;
    }

}
