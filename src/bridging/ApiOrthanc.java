package bridging;

//tambahan
import org.springframework.util.MultiValueMap;
import org.springframework.util.LinkedMultiValueMap;

//akhir
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fungsi.koneksiDB;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.JOptionPane;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author windiartonugroho
 */
public class ApiOrthanc {
    private HttpHeaders headers ;
    private JsonNode root;
    private HttpEntity requestEntity;
    private ObjectMapper mapper = new ObjectMapper();
    private SSLContext sslContext;
    private SSLSocketFactory sslFactory;
    private Scheme scheme;
    private HttpComponentsClientHttpRequestFactory factory;
    private String auth,authEncrypt,requestJson;
    private byte[] encodedBytes;
    private int i=1;
    
    public ApiOrthanc(){
        try {
            auth=koneksiDB.USERORTHANC()+":"+koneksiDB.PASSORTHANC();
            encodedBytes = Base64.encodeBase64(auth.getBytes());
            authEncrypt= new String(encodedBytes);
        } catch (Exception ex) {
            System.out.println("Notifikasi : "+ex);
        }
    }
    
    public String Auth(){
        return authEncrypt;
    }
    //tambahan
    public void UploadJpegKeWebApps(String NoRawat, String Series, String TglPeriksa, String Jam) {
        System.out.println("Upload JPEG - NoRawat: " + NoRawat + " Series: " + Series);
        try {
            HttpHeaders hdrs = new HttpHeaders();
            hdrs.add("Authorization", "Basic " + authEncrypt);
            HttpEntity reqEntity = new HttpEntity(hdrs);

            String urlSeries = koneksiDB.URLORTHANC() + ":" + koneksiDB.PORTORTHANC()
                             + "/series/" + Series;
            String resultJson = getRest().exchange(
                urlSeries, HttpMethod.GET, reqEntity, String.class).getBody();

            JsonNode root = mapper.readTree(resultJson);
            JsonNode instances = root.path("Instances");

            int total = instances.size();
            int sukses = 0;

            for (int i = 0; i < instances.size(); i++) {
                String instanceId = instances.get(i).asText();

                HttpHeaders jpegHdrs = new HttpHeaders();
                jpegHdrs.add("Authorization", "Basic " + authEncrypt);
                jpegHdrs.setAccept(Collections.singletonList(MediaType.IMAGE_JPEG));
                HttpEntity jpegEntity = new HttpEntity(jpegHdrs);

                String urlPreview = koneksiDB.URLORTHANC() + ":" + koneksiDB.PORTORTHANC()
                                  + "/instances/" + instanceId + "/preview";

                ResponseEntity<byte[]> response = getRest().exchange(
                    urlPreview, HttpMethod.GET, jpegEntity, byte[].class);

                byte[] jpegBytes = response.getBody();
                if (jpegBytes == null) continue;

                String namaFile = NoRawat.replaceAll("[/\\s]", "_") + "_" + (i + 1) + ".jpg";

                boolean ok = kirimJpegKeWebApps(jpegBytes, namaFile);
                if (ok) sukses++;

                System.out.println("Instance " + (i+1) + "/" + total + " - " + namaFile + " : " + (ok ? "OK" : "GAGAL"));
            }

                JOptionPane.showMessageDialog(null,
                    "Upload selesai!\nBerhasil: " + sukses + " dari " + total + " gambar.");

            } catch (Exception e) {
                System.out.println("Error UploadJpegKeWebApps: " + e);
                JOptionPane.showMessageDialog(null, "Gagal upload: " + e.getMessage());
            }
    }

    private boolean kirimJpegKeWebApps(byte[] jpegBytes, String namaFile) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            HttpHeaders fileHeaders = new HttpHeaders();
            fileHeaders.setContentType(MediaType.IMAGE_JPEG);
            fileHeaders.set("Content-Disposition",
                "form-data; name=\"file\"; filename=\"" + namaFile + "\"");
            body.add("file", new HttpEntity<>(jpegBytes, fileHeaders));
            body.add("nama_file", namaFile);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                new HttpEntity<>(body, headers);

            RestTemplate rest = new RestTemplate();
            ResponseEntity<String> response = rest.postForEntity(
                "http://192.168.100.8/webapps/radiologi/api/upload_jpeg.php",
                requestEntity,
                String.class
            );

            System.out.println("Response upload: " + response.getBody());
            return response.getBody() != null && response.getBody().startsWith("OK");

        } catch (Exception e) {
            System.out.println("Error kirimJpeg: " + e);
            return false;
        }
    }
    private void uploadKeServer(byte[] imageBytes, String noRawat, int index) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

            // Kirim byte[] langsung (BUKAN Resource)
            body.add("file", new HttpEntity<>(imageBytes, createFileHeaders(noRawat, index)));
            body.add("no_rawat", noRawat);

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            RestTemplate rest = new RestTemplate();
            rest.postForEntity(
                    "http://192.168.100.8/webapps/radiologi/api/upload_gambar.php",
                    requestEntity,
                    String.class
            );

            System.out.println("Upload PNG berhasil: " + noRawat + "_" + index);

        } catch (Exception e) {
            System.out.println("Gagal upload: " + e);
        }
    }

    private HttpHeaders createFileHeaders(String noRawat, int index) {
    HttpHeaders fileHeaders = new HttpHeaders();
    fileHeaders.setContentType(MediaType.IMAGE_PNG);
    fileHeaders.set(
        "Content-Disposition",
        "form-data; name=\"file\"; filename=\"" + noRawat + "_" + index + ".png\""
    );
    return fileHeaders;
    }



    //akhir
    public JsonNode AmbilSeries(String Norm,String Tanggal1,String Tanggal2){
        System.out.println("Percobaan Mengambil Photo Pasien : "+Norm);
        try{
            headers = new HttpHeaders();
            System.out.println("Auth : "+authEncrypt);
            headers.add("Authorization", "Basic "+authEncrypt);
            requestJson = "{"+
                              "\"Level\": \"Study\","+
                              "\"Expand\": true,"+
                              "\"Query\": {"+
                                   "\"StudyDate\": \""+Tanggal1+"-"+Tanggal2+"\","+
                                   "\"PatientID\": \""+Norm+"\""+
                              "}"+
                          "}";
            System.out.println("Request JSON : "+requestJson);
            requestEntity = new HttpEntity(requestJson,headers);
            System.out.println("URL : "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/tools/find");
            requestJson=getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/tools/find", HttpMethod.POST, requestEntity, String.class).getBody();
            System.out.println("Result JSON : "+requestJson);
            root = mapper.readTree(requestJson);
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
            JOptionPane.showMessageDialog(null,"Gagal mengambil data dari Orthanc, silahkan hubungi administrator ..!!");
        }
        return root;
    }
    
    //tamabahan
    public String GetStudyInstanceUID(String Norm, String Tanggal1, String Tanggal2){
        try {
            JsonNode studies = AmbilSeries(Norm, Tanggal1, Tanggal2);
            if(studies != null && studies.isArray() && studies.size() > 0){
                // Ambil StudyInstanceUID dari study pertama
                String studyUID = studies.get(0)
                    .path("MainDicomTags")
                    .path("StudyInstanceUID")
                    .asText();
                System.out.println("StudyInstanceUID: " + studyUID);
                return studyUID;
            }
        } catch(Exception e){
            System.out.println("Error GetStudyInstanceUID: " + e);
        }
        return null;
   }
    
    public JsonNode AmbilPng(String NoRawat,String Series){
        System.out.println("Percobaan Mengambil Gambar PNG : "+NoRawat+", Series : "+Series);
        try{
            headers = new HttpHeaders();
            System.out.println("Auth : "+authEncrypt);
            headers.add("Authorization", "Basic "+authEncrypt);
            requestEntity = new HttpEntity(headers);
            System.out.println("URL : "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/series/"+Series);
            requestJson=getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/series/"+Series, HttpMethod.GET, requestEntity, String.class).getBody();
            System.out.println("Result JSON : "+requestJson);
            root = mapper.readTree(requestJson);
            i=1;
            for(JsonNode list:root.path("Instances")){
                 System.out.println("Mengambil Gambar PNG "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/instances/"+list.asText()+"/preview");
                 headers = new HttpHeaders();
                 headers.add("Authorization", "Basic "+authEncrypt);
                 headers.add("Accept","image/png");
                 headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
                 headers.setAccept(Collections.singletonList(MediaType.IMAGE_JPEG));
                 HttpEntity<String> entity = new HttpEntity<>(headers);
                 ResponseEntity<byte[]> response = getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/instances/"+list.asText()+"/preview", HttpMethod.GET, entity, byte[].class);
//                 Files.write(Paths.get("./gambarradiologi/"+NoRawat+i+".png"),response.getBody());//asli tak komen
                // simpan ke server radiologi + DB
                uploadKeServer(response.getBody(), NoRawat, i);
                 i++;
            }
            JOptionPane.showMessageDialog(null,"Pengambilan Gambar PNG dari Orthanc berhasil, silahkan lihat di dalam folder Aplikasi..!!");
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
            JOptionPane.showMessageDialog(null,"Gagal mengambil Gambar PNG dari Orthanc, silahkan hubungi administrator ..!!");
        }
        return root;
    }
    
    public JsonNode AmbilJpg(String NoRawat,String Series){
        System.out.println("Percobaan Mengambil Gambar JPG : "+NoRawat+", Series : "+Series);
        try{
            headers = new HttpHeaders();
            System.out.println("Auth : "+authEncrypt);
            headers.add("Authorization", "Basic "+authEncrypt);
            requestEntity = new HttpEntity(headers);
            System.out.println("URL : "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/series/"+Series);
            requestJson=getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/series/"+Series, HttpMethod.GET, requestEntity, String.class).getBody();
            System.out.println("Result JSON : "+requestJson);
            root = mapper.readTree(requestJson);
            i=1;
            for(JsonNode list:root.path("Instances")){
                 System.out.println("Mengambil Gambar JPG "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/instances/"+list.asText()+"/preview");
                 headers = new HttpHeaders();
                 headers.add("Authorization", "Basic "+authEncrypt);
                 headers.add("Accept","image/jpeg");
                 headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
                 headers.setAccept(Collections.singletonList(MediaType.IMAGE_JPEG));
                 HttpEntity<String> entity = new HttpEntity<>(headers);
                 ResponseEntity<byte[]> response = getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/instances/"+list.asText()+"/preview", HttpMethod.GET, entity, byte[].class);
                 Files.write(Paths.get("./gambarradiologi/"+NoRawat+i+".jpg"),response.getBody());
                 i++;
            }
            JOptionPane.showMessageDialog(null,"Pengambilan Gambar JPG dari Orthanc berhasil, silahkan lihat di dalam folder Aplikasi..!!");
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
            JOptionPane.showMessageDialog(null,"Gagal mengambil Gambar JPG dari Orthanc, silahkan hubungi administrator ..!!");
        }
        return root;
    }
    
    public JsonNode AmbilBmp(String NoRawat,String Series){
        System.out.println("Percobaan Mengambil Gambar BMP : "+NoRawat+", Series : "+Series);
        try{
            headers = new HttpHeaders();
            System.out.println("Auth : "+authEncrypt);
            headers.add("Authorization", "Basic "+authEncrypt);
            requestEntity = new HttpEntity(headers);
            System.out.println("URL : "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/series/"+Series);
            requestJson=getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/series/"+Series, HttpMethod.GET, requestEntity, String.class).getBody();
            System.out.println("Result JSON : "+requestJson);
            root = mapper.readTree(requestJson);
            i=1;
            for(JsonNode list:root.path("Instances")){
                 System.out.println("Mengambil Gambar BMP "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/instances/"+list.asText()+"/preview");
                 headers = new HttpHeaders();
                 headers.add("Authorization", "Basic "+authEncrypt);
                 headers.add("Accept","image/bmp");
                 headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
                 headers.setAccept(Collections.singletonList(MediaType.IMAGE_JPEG));
                 HttpEntity<String> entity = new HttpEntity<>(headers);
                 ResponseEntity<byte[]> response = getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/instances/"+list.asText()+"/preview", HttpMethod.GET, entity, byte[].class);
                 Files.write(Paths.get("./gambarradiologi/"+NoRawat+i+".bmp"),response.getBody());
                 i++;
            }
            JOptionPane.showMessageDialog(null,"Pengambilan Gambar BMP dari Orthanc berhasil, silahkan lihat di dalam folder Aplikasi..!!");
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
            JOptionPane.showMessageDialog(null,"Gagal mengambil Gambar BMP dari Orthanc, silahkan hubungi administrator ..!!");
        }
        return root;
    }
    
    public JsonNode AmbilDcm(String NoRawat,String Series){
        System.out.println("Percobaan Mengambil Gambar DCM : "+NoRawat+", Series : "+Series);
        try{
            headers = new HttpHeaders();
            System.out.println("Auth : "+authEncrypt);
            headers.add("Authorization", "Basic "+authEncrypt);
            requestEntity = new HttpEntity(headers);
            System.out.println("URL : "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/series/"+Series);
            requestJson=getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/series/"+Series, HttpMethod.GET, requestEntity, String.class).getBody();
            System.out.println("Result JSON : "+requestJson);
            root = mapper.readTree(requestJson);
            i=1;
            for(JsonNode list:root.path("Instances")){
                 System.out.println("Mengambil Gambar DCM "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/instances/"+list.asText()+"/file");
                 headers = new HttpHeaders();
                 headers.add("Authorization", "Basic "+authEncrypt);
                 headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));
                 headers.setAccept(Collections.singletonList(MediaType.IMAGE_JPEG));
                 HttpEntity<String> entity = new HttpEntity<>(headers);
                 ResponseEntity<byte[]> response = getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/instances/"+list.asText()+"/file", HttpMethod.GET, entity, byte[].class);
                 Files.write(Paths.get("./gambarradiologi/"+NoRawat+i+".dcm"),response.getBody());
                 i++;
            }
            JOptionPane.showMessageDialog(null,"Pengambilan Gambar DCM dari Orthanc berhasil, silahkan lihat di dalam folder Aplikasi..!!");
        }catch(Exception e){
            System.out.println("Notifikasi : "+e);
            JOptionPane.showMessageDialog(null,"Gagal mengambil Gambar DCM dari Orthanc, silahkan hubungi administrator ..!!");
        }
        return root;
    }
    
    public boolean UbahAccession(String studyId, String accessionBaru){
        System.out.println("Modify AccessionNumber Study : " + studyId);
        try{
            headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + authEncrypt);
            headers.setContentType(MediaType.APPLICATION_JSON);
            requestJson = "{" +
                                "\"Replace\": {" +
                                    "\"AccessionNumber\": \""+accessionBaru+"\"" +
                                "}," +
                                "\"KeepSource\": false"+
                            "}";
            System.out.println("Request JSON : " + requestJson);
            requestEntity = new HttpEntity(requestJson, headers);
            System.out.println("URL : "+koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/studies/"+studyId+"/modify");
            String response = getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/studies/"+studyId+"/modify",HttpMethod.POST,requestEntity, String.class).getBody();
            System.out.println("Response : " + response);
            return true;
        }catch(Exception e){
            System.out.println("Notifikasi : " + e);
            JOptionPane.showMessageDialog(null,"Gagal mengubah Accession Number di Orthanc..!!");
            return false;
        }
    }
    
    public boolean kirimKeModality(String studyId){
        System.out.println("Kirim Study ke Modality : " + studyId);
        try{
            headers = new HttpHeaders();
            headers.add("Authorization", "Basic " + authEncrypt);
            headers.setContentType(MediaType.APPLICATION_JSON);
            requestJson = "[\"" + studyId + "\"]";
            requestEntity = new HttpEntity(requestJson, headers);
            System.out.println("URL : " + koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/modalities/DICOMROUTER/store");
            System.out.println("Request JSON : " + requestJson);
            String response = getRest().exchange(koneksiDB.URLORTHANC()+":"+koneksiDB.PORTORTHANC()+"/modalities/DICOMROUTER/store",HttpMethod.POST,requestEntity,String.class).getBody();
            System.out.println("Response : " + response);
            JOptionPane.showMessageDialog(null,"Proses kirim ke Modality selesai..!!");
            return true;
        }catch(Exception e){
            System.out.println("Notifikasi : " + e);
            JOptionPane.showMessageDialog(null,"Gagal kirim ke Modality..!!");
            return false;
        }
    }
    
    public RestTemplate getRest() throws NoSuchAlgorithmException, KeyManagementException {
        sslContext = SSLContext.getInstance("SSL");
        TrustManager[] trustManagers= {
            new X509TrustManager() {
                public X509Certificate[] getAcceptedIssuers() {return null;}
                public void checkServerTrusted(X509Certificate[] arg0, String arg1)throws CertificateException {}
                public void checkClientTrusted(X509Certificate[] arg0, String arg1)throws CertificateException {}
            }
        };
        sslContext.init(null,trustManagers , new SecureRandom());
        sslFactory=new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        scheme=new Scheme("https",443,sslFactory);
        factory=new HttpComponentsClientHttpRequestFactory();
        factory.getHttpClient().getConnectionManager().getSchemeRegistry().register(scheme);
        return new RestTemplate(factory);
    }
}
