package biletsistemi.screens.anasayfa;

public class HavayollariIcerik {

        // Stil ayarlarını buradan
        private static final String FONT_FAMILY = "Segoe UI, Arial, sans-serif";
        private static final String BODY_COLOR = "#2c3e50"; // Koyu gri/mavi tonu

        public static String[] getFirmsHtml() {
            return new String[]{
                    createHtml(
                            "THY",
                            "#E11937", // THY Kırmızısı
                            "Dünyanın en çok ülkesine uçan Türk Hava Yolları, Türk misafirperverliğini "
                                    + "tescillenmiş lezzetlerle gökyüzüne taşıyor. Her yolculuğu eşsiz bir deneyime "
                                    + "dönüştürüyoruz. Dünyayı bizimle keşfedin."
                    ),
                    createHtml(
                            "Lufthansa",
                            "#001F3F", // Lacivert
                            "Alman disiplini ve stratejik vizyonuyla Lufthansa, dakikliği konforla buluşturuyor. "
                                    + "Premium hizmet anlayışıyla, her yolculukta kusursuz bir profesyonellik sizi bekliyor."
                    ),
                    createHtml(
                            "Emirates",
                            "#D71920", // Emirates Kırmızısı
                            "Lüksün gökyüzündeki tanımı Emirates, ikonik hizmetleri ve ödüllü eğlence "
                                    + "sistemiyle seyahati sanata dönüştürüyor. Beş yıldızlı konforun tadını çıkarın."
                    ),
                    createHtml(
                            "Air France",
                            "#002395", // Fransız Mavisi
                            "Fransız şıklığını bulutların üzerine taşıyan Air France, yolculuğu bir keyif "
                                    + "anına dönüştürüyor. Paris ruhunu gökyüzünde yaşamaya davetlisiniz."
                    )
            };
        }

        private static String createHtml(String title, String titleColor, String content) {
            return String.format(
                    "<html><div style='text-align: center; width: 250px; padding: 10px; font-family: %s; color: %s;'>"
                            + "<b style='font-size: 15px; color: %s;'>%s</b><br>"
                            + "<p style='font-size: 11px; margin-top: 5px;'>%s</p>"
                            + "</div></html>",
                    FONT_FAMILY, BODY_COLOR, titleColor, title, content
            );
        }
}
