package pl.edu.mimuw.ms459531.main;

public class ScannerHelper {
    public static Integer tryParseInt(String text) {
        try {
            return Integer.parseInt(text);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
