package bridging;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.akses;
import fungsi.sekuel;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Properties;
import javax.swing.JOptionPane;
import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * API Native Java untuk E-Klaim INA-CBG versi 5.10.x
 * Menggantikan jembatan PHP (ws.php / index.php)
 * Dibuat khusus untuk PKU Aisyiyah Jepara
 */
public class ApiEKlaim {
    private String URL = "";
    private String KEY = "";
    private String KODE_RS = "";
    private String CODER_NIK = "";
    private final Properties prop = new Properties();

    public ApiEKlaim() {
        try {
            // Load dari setting/database.xml
            try (FileInputStream fis = new FileInputStream("setting/database.xml")) {
                prop.loadFromXML(fis);
                URL = prop.getProperty("URLEKLAIM");
                KEY = prop.getProperty("KEYEKLAIM");
                KODE_RS = prop.getProperty("KODERSEKLAIM");
            } catch (Exception e) {
                System.out.println("Gagal membaca setting E-Klaim dari database.xml: " + e);
            }

            // Fallback: jika KODE_RS kosong, coba baca dari config_akun.php
            if (KODE_RS == null || KODE_RS.isEmpty()) {
                try {
                    File phpConfig = new File("inacbg_idrg_dev/conf/config_akun.php");
                    if (phpConfig.exists()) {
                        String content = new String(java.nio.file.Files.readAllBytes(phpConfig.toPath()));
                        // Parse getKelasRS() function: $kelasRS = "DS";
                        int idx = content.indexOf("$kelasRS");
                        if (idx > 0) {
                            int q1 = content.indexOf("\"", idx);
                            int q2 = content.indexOf("\"", q1 + 1);
                            if (q1 > 0 && q2 > q1) {
                                KODE_RS = content.substring(q1 + 1, q2);
                            }
                        }
                    }
                } catch (Exception e2) {
                    System.out.println("Gagal membaca kode_tarif dari config_akun.php: " + e2);
                }
            }

            // System.out.println("[ApiEKlaim] URL=" + URL + " | KODE_RS=" + KODE_RS);

            // Ambil coder_nik berdasarkan user login (kd_pegawai/nik diakses.getkode())
            String userLogin = akses.getkode();
            if (userLogin != null && !userLogin.isEmpty()) {
                sekuel Sequel = new sekuel();
                // Asumsi: tabel inacbg_coder_nik berelasi dengan field 'nik'
                String coderNik = Sequel.cariIsi("select no_ik from inacbg_coder_nik where nik=?", userLogin);
                if (coderNik != null && !coderNik.isEmpty()) {
                    CODER_NIK = coderNik;
                } else {
                    // Fallback jika user yg login tidak punya NIK Coder (misal login sbg admin)
                    coderNik = Sequel.cariIsi("select no_ik from inacbg_coder_nik limit 1");
                    if (coderNik != null && !coderNik.isEmpty()) {
                        CODER_NIK = coderNik;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Notif E-Klaim: " + e);
        }
    }

    public String getCoderNik() {
        return CODER_NIK;
    }

    public String getKodeRS() {
        return KODE_RS;
    }

    public String cleanJson(String text) {
        if (text == null) return "";
        return text.replace("\\", "\\\\").replace("\"", "\\\"").replace("\n", " ").replace("\r", " ").replace("\t", " ").replace("\u00A0", " ").replace("'", "\\'");
    }

    private byte[] hexStringToByteArray(String s) {
        s = s.replace("-", "");
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public String encrypt(String plainText) {
        try {
            byte[] keyBytes = hexStringToByteArray(KEY);
            if (keyBytes.length != 32) {
                System.out.println("E-Klaim Error: Encryption key must be 256 bits (32 bytes)");
                return null;
            }

            // Generate IV
            byte[] iv = new byte[16];
            new SecureRandom().nextBytes(iv);
            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");

            // Encrypt data
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);
            byte[] encryptedData = cipher.doFinal(plainText.getBytes("UTF-8"));

            // Create signature
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] hashSign = mac.doFinal(encryptedData);

            byte[] signature = new byte[10];
            System.arraycopy(hashSign, 0, signature, 0, 10);

            // Combine
            byte[] combined = new byte[signature.length + iv.length + encryptedData.length];
            System.arraycopy(signature, 0, combined, 0, signature.length);
            System.arraycopy(iv, 0, combined, signature.length, iv.length);
            System.arraycopy(encryptedData, 0, combined, signature.length + iv.length, encryptedData.length);

            return Base64.getEncoder().encodeToString(combined);
            
        } catch (Exception e) {
            System.out.println("E-Klaim Encrypt Error: " + e);
            e.printStackTrace();
            return null;
        }
    }

    public String decrypt(String base64Encrypted) {
        try {
            byte[] keyBytes = hexStringToByteArray(KEY);
            byte[] combined = Base64.getDecoder().decode(base64Encrypted.replace("\n", "").replace("\r", ""));
            
            byte[] signature = new byte[10];
            byte[] iv = new byte[16];
            byte[] encryptedData = new byte[combined.length - 26];

            System.arraycopy(combined, 0, signature, 0, 10);
            System.arraycopy(combined, 10, iv, 0, 16);
            System.arraycopy(combined, 26, encryptedData, 0, encryptedData.length);

            SecretKeySpec secretKey = new SecretKeySpec(keyBytes, "AES");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(secretKey);
            byte[] calcHash = mac.doFinal(encryptedData);
            
            for (int i = 0; i < 10; i++) {
                if (signature[i] != calcHash[i]) {
                    System.out.println("E-Klaim Decrypt Error: SIGNATURE_NOT_MATCH");
                    return "{\"metadata\":{\"code\":400,\"message\":\"SIGNATURE_NOT_MATCH\"}}";
                }
            }

            IvParameterSpec ivSpec = new IvParameterSpec(iv);
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);
            byte[] decryptedData = cipher.doFinal(encryptedData);

            return new String(decryptedData, "UTF-8");

        } catch (Exception e) {
            System.out.println("E-Klaim Decrypt Error: " + e);
            e.printStackTrace();
            return "{\"metadata\":{\"code\":400,\"message\":\"Error memproses decrypt\"}}";
        }
    }

    public JsonNode postKlaim(String jsonPayload) {
        try {
            if(URL == null || URL.equals("") || KEY == null || KEY.equals("")){
                JOptionPane.showMessageDialog(null, "Pengaturan E-Klaim (URLEKLAIM & KEYEKLAIM) belum di set di database.xml!");
                return null;
            }

            String encryptedPayload = encrypt(jsonPayload);
            if (encryptedPayload == null) return null;

            URL url = new URL(URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "text/plain");
            conn.setDoOutput(true);

            OutputStream os = conn.getOutputStream();
            os.write(encryptedPayload.getBytes());
            os.flush();

            if (conn.getResponseCode() != HttpURLConnection.HTTP_OK) {
                System.out.println("E-Klaim Failed : HTTP error code : " + conn.getResponseCode());
                return null;
            }

            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            StringBuilder responseStrBuilder = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                responseStrBuilder.append(output);
            }
            conn.disconnect();

            String encryptedResponse = responseStrBuilder.toString();
            // Remove the BEGIN and END tags if they exist
            encryptedResponse = encryptedResponse.replace("----BEGIN ENCRYPTED DATA----", "");
            encryptedResponse = encryptedResponse.replace("----END ENCRYPTED DATA----", "");
            
            String decryptedResponse = decrypt(encryptedResponse);
            // System.out.println("E-Klaim Response (Decrypted): " + decryptedResponse);
            
            ObjectMapper mapper = new ObjectMapper();
            JsonNode responseNode = mapper.readTree(decryptedResponse);
            
            // Tampilkan Notifikasi jika ada error dari server E-Klaim
            if (responseNode != null && responseNode.has("metadata")) {
                JsonNode meta = responseNode.get("metadata");
                if (meta.has("code") && !meta.get("code").asText().equals("200")) {
                    String msg = meta.has("message") ? meta.get("message").asText() : "Unknown Error";
                    String errNo = meta.has("error_no") ? meta.get("error_no").asText() : "";
                    JOptionPane.showMessageDialog(null, "E-Klaim Error " + errNo + ":\n" + msg, "Peringatan E-Klaim", JOptionPane.WARNING_MESSAGE);
                }
            }
            
            return responseNode;
            
        } catch (Exception e) {
            System.out.println("E-Klaim POST Error: " + e);
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode postAction(String act, String dataJson) {
        if ("setDiagnosaIDRG".equals(act) || "setDiagnosaINACBG".equals(act)) {
            try {
                ObjectMapper mapper = new ObjectMapper();
                com.fasterxml.jackson.databind.node.ObjectNode rootNode = (com.fasterxml.jackson.databind.node.ObjectNode) mapper.readTree(dataJson);
                String prefix = "setDiagnosaIDRG".equals(act) ? "idrg" : "inacbg";
                
                String diag = rootNode.has("diagnosa") ? rootNode.get("diagnosa").asText() : "";
                String proc = rootNode.has("prosedure") ? rootNode.get("prosedure").asText() : (rootNode.has("procedure") ? rootNode.get("procedure").asText() : "");
                String noSep = rootNode.has("no_sep") ? rootNode.get("no_sep").asText() : (rootNode.has("nomor_sep") ? rootNode.get("nomor_sep").asText() : "");
                
                if (!diag.isEmpty()) {
                    postAction("custom_" + prefix + "_diagnosa_set", "{\"nomor_sep\": \"" + noSep + "\", \"diagnosa\": \"#\"}");
                    postAction("custom_" + prefix + "_diagnosa_set", "{\"nomor_sep\": \"" + noSep + "\", \"diagnosa\": \"" + diag + "\"}");
                }
                if (!proc.isEmpty()) {
                    postAction("custom_" + prefix + "_procedure_set", "{\"nomor_sep\": \"" + noSep + "\", \"procedure\": \"#\"}");
                    postAction("custom_" + prefix + "_procedure_set", "{\"nomor_sep\": \"" + noSep + "\", \"procedure\": \"" + proc + "\"}");
                }
                
                String groupAct = "setDiagnosaIDRG".equals(act) || "importCoding".equals(act) ? "groupingIdrgStage1" : "groupingInacbgStage1";
                JsonNode result = postAction(groupAct, "{\"nomor_sep\": \"" + noSep + "\"}");
                if (result != null && result.has("response")) {
                    if ("setDiagnosaIDRG".equals(act)) {
                        ((com.fasterxml.jackson.databind.node.ObjectNode) result).set("response_idrg", result.get("response"));
                    } else if ("setDiagnosaINACBG".equals(act) || "importCoding".equals(act)) {
                        ((com.fasterxml.jackson.databind.node.ObjectNode) result).set("response_inacbg", result.get("response"));
                    }
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String method = "";
        String metadataExtra = "";
        switch(act) {
            case "custom_idrg_diagnosa_set": method = "idrg_diagnosa_set"; break;
            case "custom_idrg_procedure_set": method = "idrg_procedure_set"; break;
            case "custom_inacbg_diagnosa_set": method = "inacbg_diagnosa_set"; break;
            case "custom_inacbg_procedure_set": method = "inacbg_procedure_set"; break;
            case "createClaim": method = "new_claim"; break;
            case "updateClaim": method = "set_claim_data"; break;
            case "setDiagnosaIDRG": 
                method = "grouper"; 
                metadataExtra = ", \"stage\": \"1\""; 
                break;
            case "setProcedureIDRG": method = "set_claim_data"; break;
            case "groupingIdrgStage1": 
                method = "grouper"; 
                metadataExtra = ", \"stage\": \"1\", \"grouper\": \"idrg\""; 
                break;
            case "groupingIdrgStage2": 
                method = "grouper"; 
                metadataExtra = ", \"stage\": \"2\", \"grouper\": \"idrg\""; 
                break;
            case "finalIDRG": method = "idrg_grouper_final"; break;
            case "editIDRG": method = "idrg_grouper_reedit"; break;
            case "importCoding": method = "idrg_to_inacbg_import"; break;
            case "setDiagnosaINACBG": 
                method = "grouper"; 
                metadataExtra = ", \"stage\": \"1\""; 
                break;
            case "setProcedureINACBG": method = "set_claim_data"; break;
            case "groupingInacbgStage1": 
                method = "grouper"; 
                metadataExtra = ", \"stage\": \"1\", \"grouper\": \"inacbg\""; 
                break;
            case "groupingInacbgStage2": 
                method = "grouper"; 
                metadataExtra = ", \"stage\": \"2\", \"grouper\": \"inacbg\""; 
                break;
            case "finalINACBG": method = "inacbg_grouper_final"; break;
            case "editINACBG": method = "inacbg_grouper_reedit"; break;
            case "finalKlaim": method = "claim_final"; break;
            case "editKlaim": method = "reedit_claim"; break;
            case "cetakKlaim": method = "claim_print"; break;
            case "kirimOnlineKlaim": method = "send_claim_individual"; break;
            case "getKlaim": method = "get_claim_data"; break;
            case "validateSITB": method = "sitb_validate"; break;
            case "searchDiagnosa": method = "search_diagnosis"; break;
            case "searchDiagnosisInagrouper": method = "search_diagnosis_inagrouper"; break;
            case "searchProcedure": method = "search_procedures"; break;
            case "searchProcedureInagrouper": method = "search_procedures_inagrouper"; break;

            default: method = act; break;
        }

        // System.out.println("[ApiEKlaim DEBUG] act=" + act + " | dataJson raw=" + dataJson);

        // Bangun JSON metadata dengan format yang benar
        StringBuilder metaBuilder = new StringBuilder();
        metaBuilder.append("{\n");
        metaBuilder.append("  \"metadata\": {\n");
        metaBuilder.append("    \"method\": \"").append(method).append("\"");

        if (metadataExtra != null && !metadataExtra.isEmpty()) {
            metaBuilder.append(metadataExtra);
        }

        // Ekstrak nomor_sep dari dataJson untuk dimasukkan ke metadata
        String nomorSepMeta = "";
        if (!"grouper".equals(method) && !"new_claim".equals(method) && dataJson != null) {
            String tempJson = dataJson.replace("\"no_sep\"", "\"nomor_sep\"");
            int start = tempJson.indexOf("\"nomor_sep\"");
            if (start != -1) {
                start = tempJson.indexOf("\"", start + 11) + 1;
                int end = tempJson.indexOf("\"", start);
                if (end > start) {
                    nomorSepMeta = tempJson.substring(start, end);
                }
            }
        }
        if (!nomorSepMeta.isEmpty()) {
            metaBuilder.append(",\n    \"nomor_sep\": \"").append(nomorSepMeta).append("\"");
        }
        
        metaBuilder.append("\n  }");

        if (dataJson != null && !dataJson.trim().isEmpty() && !dataJson.equals("{}")) {
            // Ganti key sesuai format API E-Klaim 5.x
            dataJson = dataJson
                               .replace("\"no_sep\"", "\"nomor_sep\"")
                               .replace("\"no_jkn\"", "\"nomor_kartu\"")
                               .replace("\"no_rm\"", "\"nomor_rm\"")
                               .replace("\"jk\"", "\"gender\"")
                               .replace("\"tgl_awal\"", "\"tgl_masuk\"")
                               .replace("\"tgl_akhir\"", "\"tgl_pulang\"")
                               .replace("\"jenis\"", "\"jenis_rawat\"")
                               .replace("\"dokter\"", "\"nama_dokter\"");
            
            if (CODER_NIK != null && !CODER_NIK.isEmpty() && !dataJson.contains("\"coder_nik\"")) {
                dataJson = dataJson.replaceFirst("\\{", "{\"coder_nik\": \"" + CODER_NIK + "\", ");
            }
            if ("set_claim_data".equals(method) || "new_claim".equals(method)) {
                if (!dataJson.contains("\"payor_id\"")) {
                    dataJson = dataJson.replaceFirst("\\{", "{\"payor_id\": \"3\", \"payor_cd\": \"JKN\", ");
                }

                // Perbaiki Naik Kelas untuk E-Klaim 5.x
                dataJson = dataJson.replaceAll("\"upgrade_class_ind\":\\s*\"(vip|vvip|1|2|3)\"", "\"upgrade_class_ind\": \"1\"");
                dataJson = dataJson.replace("\"upgrade_class_class\": \"1\"", "\"upgrade_class_class\": \"kelas_1\"")
                                   .replace("\"upgrade_class_class\": \"2\"", "\"upgrade_class_class\": \"kelas_2\"");
                if (!dataJson.contains("\"add_payment_pct\"")) {
                    dataJson = dataJson.replaceFirst("\\{", "{\"add_payment_pct\": \"0\", ");
                }

                // Hitung ventilator_hour dan format nested ventilator untuk E-Klaim 5.x
                String ventHour = "0";
                String ventObj = "";
                try {
                    String useInd = "0";
                    if (dataJson.contains("\"use_ind\": \"1\"") || dataJson.contains("\"use_ind\":\"1\"")) {
                        useInd = "1";
                    }
                    String startDttm = "0000-00-00 00:00:00";
                    String stopDttm = "0000-00-00 00:00:00";
                    if (dataJson.contains("\"start_dttm\"")) {
                        startDttm = dataJson.split("\"start_dttm\":\\s*\"")[1].split("\"")[0];
                    }
                    if (dataJson.contains("\"stop_dttm\"")) {
                        stopDttm = dataJson.split("\"stop_dttm\":\\s*\"")[1].split("\"")[0];
                    }

                    if (useInd.equals("1") && !startDttm.contains("0000-00-00") && !stopDttm.contains("0000-00-00")) {
                        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        java.util.Date d1 = sdf.parse(startDttm);
                        java.util.Date d2 = sdf.parse(stopDttm);
                        long diffMs = d2.getTime() - d1.getTime();
                        long hours = diffMs / (1000 * 60 * 60);
                        if (hours > 0) ventHour = String.valueOf(hours);
                    }
                    ventObj = "\"ventilator\": {\"use_ind\": \"" + useInd + "\", \"start_dttm\": \"" + startDttm + "\", \"stop_dttm\": \"" + stopDttm + "\"}, ";
                } catch (Exception e) {}

                // Hapus parameter flat lama
                dataJson = dataJson.replaceAll("\"use_ind\":\\s*\"[^\"]*\",", "")
                                   .replaceAll("\"start_dttm\":\\s*\"[^\"]*\",", "")
                                   .replaceAll("\"stop_dttm\":\\s*\"[^\"]*\",", "");

                if (!dataJson.contains("\"ventilator_hour\"")) {
                    dataJson = dataJson.replaceFirst("\\{", "{\"ventilator_hour\": \"" + ventHour + "\", " + ventObj);
                }

                // KRITIS: Tambahkan kode_tarif jika belum ada
                // Ini WAJIB ada untuk E-Klaim 5.x, tanpa ini grouper akan gagal E2014
                if (!dataJson.contains("\"kode_tarif\"") && KODE_RS != null && !KODE_RS.isEmpty()) {
                    // Sisipkan kode_tarif sebelum closing brace terakhir dari dataJson
                    int lastBrace = dataJson.lastIndexOf("}");
                    if (lastBrace > 0) {
                        dataJson = dataJson.substring(0, lastBrace) + ",\"kode_tarif\": \"" + KODE_RS + "\"" + dataJson.substring(lastBrace);
                    }
                }
            }
            
            metaBuilder.append(",\n  \"data\": ").append(dataJson);
        } else {
            // System.out.println("[ApiEKlaim DEBUG] dataJson kosong/null, tidak ada 'data' section!");
        }
        metaBuilder.append("\n}");

        String finalJson = metaBuilder.toString();
        // System.out.println("E-Klaim Request (Final JSON):\n" + finalJson);

        if ("createClaim".equals(act)) {
            JsonNode res = postKlaim(finalJson);
            if (res != null && res.has("metadata") && res.get("metadata").has("code") && res.get("metadata").get("code").asText().equals("200")) {
                // System.out.println("[ApiEKlaim DEBUG] new_claim success, automatically calling set_claim_data...");
                String finalJsonUpdate = finalJson.replace("\"new_claim\"", "\"set_claim_data\"");
                // Tambahkan nomor_sep ke metadata jika belum ada (karena new_claim skip nomor_sep)
                if (!finalJsonUpdate.contains("\"nomor_sep\"") || !finalJsonUpdate.substring(0, finalJsonUpdate.indexOf("\"data\"")).contains("\"nomor_sep\"")) {
                    // Inject nomor_sep ke metadata
                    String sepVal = "";
                    if (dataJson != null && dataJson.contains("\"nomor_sep\"")) {
                        try {
                            int s = dataJson.indexOf("\"nomor_sep\"");
                            s = dataJson.indexOf("\"", s + 11) + 1;
                            int e = dataJson.indexOf("\"", s);
                            sepVal = dataJson.substring(s, e);
                        } catch (Exception ex2) {}
                    }
                    if (!sepVal.isEmpty()) {
                        finalJsonUpdate = finalJsonUpdate.replace("\"method\": \"set_claim_data\"", "\"method\": \"set_claim_data\",\n    \"nomor_sep\": \"" + sepVal + "\"");
                    }
                }
                return postKlaim(finalJsonUpdate);
            }
            return res;
        }

        JsonNode result = postKlaim(finalJson);
        if (result != null && result.has("response")) {
            JsonNode responseObj = result.get("response");
            JsonNode targetNode = responseObj;
            if (responseObj.has("cbg")) {
                targetNode = responseObj.get("cbg");
                if (targetNode.isObject()) {
                    com.fasterxml.jackson.databind.node.ObjectNode cbgObj = (com.fasterxml.jackson.databind.node.ObjectNode) targetNode;
                    if (cbgObj.has("code") && !cbgObj.has("drg_code")) {
                        cbgObj.put("drg_code", cbgObj.get("code").asText());
                    }
                    if (cbgObj.has("description") && !cbgObj.has("drg_description")) {
                        cbgObj.put("drg_description", cbgObj.get("description").asText());
                    }
                }
            }
            
            if ("setDiagnosaIDRG".equals(act) || "groupingIdrgStage1".equals(act) || "groupingIdrgStage2".equals(act)) {
                ((com.fasterxml.jackson.databind.node.ObjectNode) result).set("response_idrg", targetNode);
            } else if ("setDiagnosaINACBG".equals(act) || "groupingInacbgStage1".equals(act) || "groupingInacbgStage2".equals(act)) {
                ((com.fasterxml.jackson.databind.node.ObjectNode) result).set("response_inacbg", targetNode);
            }
        }
        return result;
    }
}
