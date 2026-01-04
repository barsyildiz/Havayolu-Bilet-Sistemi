package biletsistemi;

public class UserSession {
    public static String username = "";
    public static String email = "";

    
    public static boolean isAdmin = false;

    
    public static void clear() {
        username = "";
        email = "";
        isAdmin = false;
    }
}
