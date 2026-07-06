import java.io.*;
import java.util.regex.*;
import java.nio.file.*;

public class Refactor {
    public static void main(String[] args) throws Exception {
        String path = "d:/Project/Source SIMRS ORI/SIMRS PKU Aisyiyah Jepara/SIMRS-Khanza/src/tambahan_it/PKUDlgKlaimEKlaim.java";
        String content = new String(Files.readAllBytes(Paths.get(path)));

        // 1. Add ApiEKlaim import and instance
        content = content.replace("import bridging.ApiInacbg;", "import bridging.ApiInacbg;\nimport bridging.ApiEKlaim;");
        content = content.replace("private ApiInacbg api = new ApiInacbg();", "private ApiInacbg api = new ApiInacbg();\n    private ApiEKlaim apiEKlaim = new ApiEKlaim();");

        // 2. Replace URL = ... ?act=XYZ
        // We capture XYZ.
        // Example: URL = "http://" + koneksiDB.HOSTHYBRIDWEB() + "/" + koneksiDB.HYBRIDWEB() + "/inacbg_idrg_dev/index.php?act=createClaim&nikCoder=" + nikCoder;
        
        Matcher m = Pattern.compile("URL\\s*=\\s*.*index\\.php\\?act=([a-zA-Z0-9]+)[^;]*;").matcher(content);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            String act = m.group(1);
            String replacement;
            if (act.equals("searchDiagnosa") || act.equals("searchDiagnosisInagrouper")) {
                replacement = "requestJson = \"{\\\"keyword\\\": \\\"\" + FormCariDiagnosa.getText() + \"\\\"}\";\n                String actionName = \"" + act + "\";";
            } else if (act.equals("searchProcedure") || act.equals("searchProcedureInagrouper")) {
                replacement = "requestJson = \"{\\\"keyword\\\": \\\"\" + TCariProsedur.getText() + \"\\\"}\";\n                String actionName = \"" + act + "\";";
            } else {
                replacement = "String actionName = \"" + act + "\";";
            }
            m.appendReplacement(sb, Matcher.quoteReplacement(replacement));
        }
        m.appendTail(sb);
        content = sb.toString();

        // 3. Replace the actual API calls
        // root = mapper.readTree(api.getRest().exchange(URL, HttpMethod.POST, requestEntity, String.class).getBody());
        content = content.replaceAll("root\\s*=\\s*mapper\\.readTree\\(api\\.getRest\\(\\)\\.exchange\\(URL,\\s*HttpMethod\\.(POST|GET),\\s*requestEntity,\\s*String\\.class\\)\\.getBody\\(\\)\\);", 
            "if(requestJson == null || requestJson.isEmpty()) requestJson = \"{}\";\n" +
            "                root = apiEKlaim.postAction(actionName, requestJson);");

        // We also need to fix occurrences where `requestEntity = new HttpEntity(jsonRequest.toString(), headers);`
        // We should replace that with `requestJson = jsonRequest.toString();`
        content = content.replaceAll("requestEntity\\s*=\\s*new\\s*HttpEntity\\(jsonRequest\\.toString\\(\\),\\s*headers\\);", "requestJson = jsonRequest.toString();");

        Files.write(Paths.get(path), content.getBytes());
        System.out.println("Done refactoring PKUDlgKlaimEKlaim.java");
    }
}
