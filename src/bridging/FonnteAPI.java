/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bridging;


import okhttp3.*;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
public class FonnteAPI {
    private static final String API_URL = "https://api.fonnte.com/send";
    

    public static boolean sendMessage(String target, String message) {
        // Ambil token dari koneksiDB
        String token = koneksiDB.TOKENFONTE();
        if (token == null || token.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, 
                "Token Fonnte tidak ditemukan!\nPeriksa konfigurasi database.xml Anda.",
                "Kesalahan Token", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        OkHttpClient client = new OkHttpClient();
        
        RequestBody body = new FormBody.Builder()
                .add("target", target)
                .add("message", message)
                .build();

        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)
                .addHeader("Authorization", token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            String jsonResponse = response.body().string();
            System.out.println("[FonnteAPI] Response JSON: " + jsonResponse);

            if (!response.isSuccessful()) {
                JOptionPane.showMessageDialog(null,
                        "Gagal mengirim pesan (HTTP " + response.code() + ")",
                        "Kirim Pesan Gagal",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Parse JSON response
            JsonObject json = JsonParser.parseString(jsonResponse).getAsJsonObject();

            if (json.has("status") && json.get("status").getAsBoolean()) {
                JOptionPane.showMessageDialog(null,
                        "Pesan berhasil dikirim ke " + target,
                        "Kirim Pesan Berhasil",
                        JOptionPane.INFORMATION_MESSAGE);
                return true;
            } else {
                String reason = json.has("reason") ? json.get("reason").getAsString() : "Tidak diketahui";
                JOptionPane.showMessageDialog(null,
                        "Pesan gagal dikirim ke " + target + "\nAlasan: " + reason,
                        "Kirim Pesan Gagal",
                        JOptionPane.WARNING_MESSAGE);
                return false;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Terjadi kesalahan saat mengirim pesan:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
