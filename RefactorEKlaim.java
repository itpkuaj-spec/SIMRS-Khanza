import java.nio.file.*;
import java.io.IOException;
import java.util.regex.*;

public class RefactorEKlaim {
    public static void main(String[] args) throws IOException {
        String[] files = {
            "d:/Project/Source SIMRS ORI/SIMRS PKU Aisyiyah Jepara/SIMRS-Khanza/src/tambahan_it/PKUDlgKlaimEKlaim.java",
            "d:/Project/Source SIMRS ORI/SIMRS PKU Aisyiyah Jepara/SIMRS-Khanza/src/tambahan_it/PKUDlgKlaimEKlaimRajal.java"
        };
        
        for (String filePath : files) {
            System.out.println("Processing " + filePath);
            String content = new String(Files.readAllBytes(Paths.get(filePath)), "UTF-8");
            
            // Strip non-ascii characters to avoid encoding compilation errors
            content = content.replaceAll("[^\\x00-\\x7F]", "");
            
            // Package change
            content = content.replace("package integration_idrg;", "package tambahan_it;");
            
            // Class name change
            if (filePath.contains("Rajal")) {
                content = content.replace("public final class DlgDetailKlaimrajal extends javax.swing.JDialog", "public final class PKUDlgKlaimEKlaimRajal extends javax.swing.JDialog");
                content = content.replace("public DlgDetailKlaimrajal(java.awt.Frame parent, boolean modal)", "public PKUDlgKlaimEKlaimRajal(java.awt.Frame parent, boolean modal)");
                content = content.replace("new DlgDetailKlaimrajal(", "new PKUDlgKlaimEKlaimRajal(");
            } else {
                content = content.replace("public final class DlgDetailKlaim extends javax.swing.JDialog", "public final class PKUDlgKlaimEKlaim extends javax.swing.JDialog");
                content = content.replace("public DlgDetailKlaim(java.awt.Frame parent, boolean modal)", "public PKUDlgKlaimEKlaim(java.awt.Frame parent, boolean modal)");
                content = content.replace("new DlgDetailKlaim(", "new PKUDlgKlaimEKlaim(");
            }
            
            // API instance
            content = content.replace("private ApiIntegrationIDRG api = new ApiIntegrationIDRG();", "private bridging.ApiEKlaim apiEKlaim = new bridging.ApiEKlaim();");
            
            // String escaping for JSON
            content = content.replace("+ nmDokter.getText() +", "+ apiEKlaim.cleanJson(nmDokter.getText()) +");
            content = content.replace("+ namaPasien +", "+ apiEKlaim.cleanJson(namaPasien) +");
            content = content.replace("+ diagPasien +", "+ apiEKlaim.cleanJson(diagPasien) +");
            content = content.replace("+ procedurePasien +", "+ apiEKlaim.cleanJson(procedurePasien) +");
            content = content.replace("+ hakKelas +", "+ apiEKlaim.cleanJson(hakKelas) +");
            
            // -----------------------------------------------------
            // Step 1: Capture URL and `act` parameter, then comment it out.
            // Example: URL = "http://" + ... + "?act=setKlaim&nikCoder=" + nikCoder;
            // -----------------------------------------------------
            Pattern urlPattern = Pattern.compile("URL = [^;]*\\?act=([a-zA-Z0-9_]+)[^;]*;");
            Matcher mUrl = urlPattern.matcher(content);
            StringBuffer sbUrl = new StringBuffer();
            while (mUrl.find()) {
                String act = mUrl.group(1);
                mUrl.appendReplacement(sbUrl, "String _actionName = \"" + act + "\"; // URL mapping");
            }
            mUrl.appendTail(sbUrl);
            content = sbUrl.toString();

            // Step 2: Comment out requestEntity lines
            content = content.replaceAll("requestEntity = new HttpEntity\\([^;]*\\);", "// requestEntity removed");
            content = content.replaceAll("headers = new HttpHeaders\\(\\);\\s*headers\\.setContentType\\(MediaType\\.APPLICATION_JSON\\);", "// headers removed");
            
            // Step 3: Replace the mapper.readTree calls
            // For POST:
            content = content.replaceAll(
                "root = mapper\\.readTree\\(api\\.getRest\\(\\)\\.exchange\\(URL, HttpMethod\\.POST, requestEntity, String\\.class\\)\\.getBody\\(\\)\\);",
                "if(requestJson == null || requestJson.isEmpty()) requestJson = \"{}\";\n                root = apiEKlaim.postAction(_actionName, requestJson);"
            );

            // For GET: (which we now map to POST via apiEKlaim)
            content = content.replaceAll(
                "root = mapper\\.readTree\\(api\\.getRest\\(\\)\\.exchange\\(URL, HttpMethod\\.GET, requestEntity, String\\.class\\)\\.getBody\\(\\)\\);",
                "// Extract keyword based on actionName\n                if(_actionName.equals(\"searchDiagnosa\") || _actionName.equals(\"searchDiagnosisInagrouper\")) {\n                    requestJson = \"{\\\"keyword\\\": \\\"\" + apiEKlaim.cleanJson(FormCariDiagnosa.getText()) + \"\\\"}\";\n                } else if (_actionName.equals(\"searchProcedure\") || _actionName.equals(\"searchProcedureInagrouper\")) {\n                    requestJson = \"{\\\"keyword\\\": \\\"\" + apiEKlaim.cleanJson(TCariProsedur.getText()) + \"\\\"}\";\n                } else {\n                    requestJson = \"{}\";\n                }\n                root = apiEKlaim.postAction(_actionName, requestJson);"
            );
            
            // Fix any commented-out mapper calls that were partially un-commented.
            content = content.replaceAll("//\\s*if\\(requestJson", "// if(requestJson");
            content = content.replaceAll("//\\s*root = apiEKlaim\\.postAction", "// root = apiEKlaim.postAction");
            
            Files.write(Paths.get(filePath), content.getBytes("UTF-8"));
            System.out.println("Done processing " + filePath);
        }
    }
}
