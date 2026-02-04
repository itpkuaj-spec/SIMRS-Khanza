/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tambahan_it;

import fungsi.koneksiDB;
import java.sql.*;
import java.util.Base64;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

public class OrthancMWLService {

    private final Connection con = koneksiDB.condb();

    private final String ORTHANC_URL = koneksiDB.URLORTHANC() + "/worklists";
    private final String USER = koneksiDB.USERORTHANC();
    private final String PASS = koneksiDB.PASSORTHANC();

    /* ===============================
       METHOD UTAMA (DARI TOMBOL)
       =============================== */
    public void kirimOrderKeMWL(String noOrder) {
        try {
            String json = buildJsonMWL(noOrder);
            if (json.isEmpty()) {
                System.out.println("Data MWL tidak ditemukan");
                return;
            }

            kirimKeOrthanc(noOrder, json);

        } catch (Exception e) {
            System.out.println("ERROR MWL : " + e);
        }
    }

    /* ===============================
       BUILD JSON MWL
       =============================== */
    private String buildJsonMWL(String noOrder) throws Exception {

        String sql =
            "SELECT pr.noorder, pr.tgl_permintaan, pr.jam_permintaan, " +
            "pr.diagnosa_klinis, pr.informasi_tambahan, " +
            "p.no_rkm_medis, p.nm_pasien, p.tgl_lahir, p.jk, " +
            "d.nm_dokter " +
            "FROM permintaan_radiologi pr " +
            "JOIN reg_periksa rp ON pr.no_rawat = rp.no_rawat " +
            "JOIN pasien p ON rp.no_rkm_medis = p.no_rkm_medis " +
            "JOIN dokter d ON pr.dokter_perujuk = d.kd_dokter " +
            "WHERE pr.noorder = ?";

        PreparedStatement ps = con.prepareStatement(sql);
        ps.setString(1, noOrder);
        ResultSet rs = ps.executeQuery();

        if (!rs.next()) {
            rs.close();
            ps.close();
            return "";
        }

        String sex = rs.getString("jk").equals("L") ? "M" : "F";

        String tgl = rs.getDate("tgl_lahir") != null
                ? rs.getDate("tgl_lahir").toString().replace("-", "")
                : "";

        String tglSps = rs.getDate("tgl_permintaan").toString().replace("-", "");
        String jamSps = rs.getTime("jam_permintaan").toString().replace(":", "");

        String json =
            "{ \"Worklist\": {" +
            "\"AccessionNumber\": \"" + rs.getString("noorder") + "\"," +
            "\"PatientID\": \"" + rs.getString("no_rkm_medis") + "\"," +
            "\"PatientName\": \"" + rs.getString("nm_pasien") + "\"," +
            "\"PatientBirthDate\": \"" + tgl + "\"," +
            "\"PatientSex\": \"" + sex + "\"," +
            "\"RequestingPhysician\": \"" + rs.getString("nm_dokter") + "\"," +
            "\"StudyDescription\": \"" + rs.getString("diagnosa_klinis") + "\"," +
            "\"ScheduledStationAETitle\": \"ORTHANC\"," +
            "\"Modality\": \"CR\"," +
            "\"ScheduledProcedureStepStartDate\": \"" + tglSps + "\"," +
            "\"ScheduledProcedureStepStartTime\": \"" + jamSps + "\"," +
            "\"ScheduledProcedureStepDescription\": \"" +
                rs.getString("diagnosa_klinis") + "\"" +
            "} }";

        rs.close();
        ps.close();
        return json;
    }

    /* ===============================
       KIRIM KE ORTHANC
       =============================== */
    private void kirimKeOrthanc(String noOrder, String json) {

        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String auth = USER + ":" + PASS;
        String encoded = Base64.getEncoder().encodeToString(auth.getBytes());
        headers.set("Authorization", "Basic " + encoded);

        HttpEntity<String> entity = new HttpEntity<>(json, headers);

        ResponseEntity<String> response =
            rest.exchange(
                ORTHANC_URL + "/" + noOrder,
                HttpMethod.PUT,
                entity,
                String.class
            );

        System.out.println("ORTHANC MWL STATUS : " + response.getStatusCode());
    }
}