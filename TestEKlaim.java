
import java.io.File;
import java.io.FileOutputStream;
import java.util.Base64;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestEKlaim {
    public static void main(String[] args) {
        try {
            bridging.ApiEKlaim api = new bridging.ApiEKlaim();
            String noSep = "0160R0090426V000170"; // From the list of files
            String payload = "{\"metadata\":{\"method\":\"claim_print\"},\"data\":{\"nomor_sep\":\"" + noSep + "\"}}";
            JsonNode responseNode = api.postKlaim(payload);
            
            System.out.println("FULL JSON: " + responseNode.toString());
            
            if (responseNode != null && responseNode.path("metadata").path("code").asText().equals("200")) {
                String base64Pdf = responseNode.path("response").asText();
                System.out.println("Base64 Length: " + base64Pdf.length());
                if (base64Pdf.length() > 0) {
                    File tempPdf = new File("test_klaim_individual.pdf");
                    try (FileOutputStream fos = new FileOutputStream(tempPdf)) {
                        fos.write(Base64.getDecoder().decode(base64Pdf));
                    }
                    System.out.println("Written to test_klaim_individual.pdf");
                }
            } else {
                System.out.println("Error or non-200 response.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

