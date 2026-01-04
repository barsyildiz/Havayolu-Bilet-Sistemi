package biletsistemi;

import biletsistemi.database.Database;
import biletsistemi.screens.login.GirisEkrani;

public class Main {
    public static void main(String[] args) {
        Database.init();
        new GirisEkrani().baslat(); 
    }
}
