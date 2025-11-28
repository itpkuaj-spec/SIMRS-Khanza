/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package tambahan_it;

import java.io.*;
import java.net.*;

import fungsi.koneksiDB;
import javax.swing.JOptionPane;

/**
 *
 * @author IT GANTENG
 */
/**
 * FonnteAPI Helper Class
 * -----------------------
 * Kirim pesan WhatsApp via Fonnte dan tampilkan hasilnya dalam dialog.
 */
public class FonnteAPIkirimbanyak {
    String apiUrl = "https://api.fonnte.com/send";
    

    public static String sendMessage(String target, String message) {
        // Ambil token dari koneksiDB
        String apiUrl = "https://api.fonnte.com/send";
        String token = koneksiDB.TOKENFONTE();
        


        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", token);
            conn.setDoOutput(true);

            String data = "target=" + target + "&message=" + URLEncoder.encode(message, "UTF-8");

            OutputStream os = conn.getOutputStream();
            os.write(data.getBytes());
            os.flush();
            os.close();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String response = br.readLine();
            br.close();

            return response;

        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    
    }
}
