import java.text.SimpleDateFormat;
public class T {
    public static void main(String[] a) {
        try {
            System.out.println(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").parse("2026-07-20 11:40:00"));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
