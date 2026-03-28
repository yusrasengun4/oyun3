package oyun3;

import java.io.IOException;
import java.util.*;

import com.github.javafaker.Faker;

public class Oyun {

    private ArrayList<Sehir> sehirler = new ArrayList<>();
    private ArrayList<Integer> duzeltilmisSayilar = new ArrayList<>();
    private ArrayList<Integer> finalSonuclar = new ArrayList<>();
    private Scanner scanner = new Scanner(System.in);
   
    public void turuYonet() {

        ArrayList<Sehir> yeniSehirler = new ArrayList<>();

        for (Sehir s : sehirler) {

            s.turAtlat(); 

            if (s.getNufus() >= 1000) {
                Sehir yeni = s.bolunmeGerceklestir();
                yeniSehirler.add(yeni);
            }
        }

        sehirler.addAll(yeniSehirler);
    }
    public void ekraniTemizle() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                // Windows için 'cls' komutu
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
            }
        } catch (IOException|InterruptedException ex) {
        }
    }
    public void detayliBilgiSorgula() {
    	Scanner sc = new Scanner(System.in);

        while (true) {

            System.out.print("Satir: ");
             String satirInput = sc.nextLine();

            // 🔥 Eğer sayı değilse çık
            if (!satirInput.matches("\\d+")) break;

            System.out.print("Sutun: ");
            String sutunInput = sc.nextLine();

            // 🔥 Eğer sayı değilse çık
            if (!sutunInput.matches("\\d+")) break;

            int satir = Integer.parseInt(satirInput);
            int sutun = Integer.parseInt(sutunInput);

            int index = (satir - 1) * 5 + (sutun - 1);

            if (index >= 0 && index < sehirler.size()) {
                Sehir secilen = sehirler.get(index);
                ekraniTemizle();
                secilen.tumDetaylariYazdir();
            } else {
                System.out.println("(!) Gecersiz koordinat! Bu noktada bir sehir yok.");
            }
        }

        
    }
    public void baslat() {
        // 1. Giriş Bilgilerini Al
        System.out.print("Tur sayisi: ");
        int tur = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Sayilari gir (boslukla): ");
        String[] sayilar = scanner.nextLine().split(" ");

        // 2. İlk Kurulum (Şehirleri Oluştur)
        for (int i = 0; i < sayilar.length; i++) {
            int sayi = Integer.parseInt(sayilar[i]);
            Faker faker = new Faker(new Locale("tr"));
            Sehir sehir = new Sehir(faker.address().cityName());
            sehir.tumHesapla(sayi); 
            sehirler.add(sehir);
        }

        // 3. Tur Döngüsü
        for (int t = 1; t <= tur; t++) {
            ekraniTemizle(); 
            
            
            if (t > 1) {
                turuYonet(); // Yaşlanma, Büyüme ve Bölünme burada gerçekleşir
            }

         
            for (int i = 0; i < sehirler.size(); i++) {
                Sehir s = sehirler.get(i);
                System.out.print("[" + s.getNufus() + "]");
                
                if ((i + 1) % 5 == 0) System.out.println();
                else if (i != sehirler.size() - 1) System.out.print("-");
            }
            System.out.println();
            
           
            try { Thread.sleep(500); } catch (Exception e) {}
        }

        

        detayliBilgiSorgula();
      

        
        
    }
}
