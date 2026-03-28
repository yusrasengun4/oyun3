package oyun3;

import java.util.ArrayList;
import java.util.Locale;

import com.github.javafaker.Faker;

public class Sehir {
    private String ad;
    private int nufus;
    private int ilceSayisi;
    private int mahalleSayisi;
    private ArrayList<Ilce> ilceler;

    public Sehir(String ad) {
        this.ad = ad;
        this.ilceler = new ArrayList<>();
        this.nufus = 0;
    }

    // --- GETTER & SETTER ---
    public int getIlceSayisi() { return ilceSayisi; }
    public int getMahalleSayisi() { return mahalleSayisi; }
    public int getNufus() { return nufus; }
    public String getAd() {
		
		return ad;
	}
    public int mahalleBasina() {
        if (mahalleSayisi == 0) return 0;
        return nufus / mahalleSayisi;
    }
    Faker faker = new Faker(new Locale("tr"));
    
    public void sayiyiDuzenle(int sayi) {
        int ilce = sayi / 10;
        int mahalle = sayi % 10;

        if (ilce == 0) ilce = 1;
        if (mahalle == 0) mahalle = ilce; 

        while (mahalle % ilce != 0) {
            mahalle++;
            if (mahalle > 9) mahalle = 1; 
        }

        this.ilceSayisi = ilce;
        this.mahalleSayisi = mahalle;
    }

    // ADIM 2: Nüfusu mahalle sayısına bölünebilir yap
    public int nufusuDuzenle(int mevcutNufus, int mahalleSayisi) {
        if (mahalleSayisi <= 0) return mevcutNufus;
        
        
        int baslangicNufus = (mevcutNufus == 0) ? (this.ilceSayisi * 10 + mahalleSayisi) : mevcutNufus;
        
        int nufus = baslangicNufus; 
        int kalan = nufus % mahalleSayisi;
        
        if (kalan == 0) return nufus;
        return nufus + (mahalleSayisi - kalan);
    }

    // YENİ: Güncel nüfusa göre katsayıyı hesaplar
    public int getGuncelKatsayi() {
    	int sonIkiBasamak = Math.abs(this.nufus) % 100;
        
        int onlar = sonIkiBasamak / 10;
        int birler = sonIkiBasamak % 10;
        
        int toplam = onlar + birler;

        // Kural: Toplam 0 ise (örneğin nüfus 1000 ise) 1 kabul et
        return (toplam == 0) ? 1 : toplam;
    }

    // Her turda çalışacak artış mekanizması
    public void turAtlat() {
    	nufusGuncelle(); 

        // 2. ADIM: Yaşlandırma (Mevcut kişilerin yaşını 1 artır)
        for (Ilce ilce : ilceler) {
            for (Mahalle m : ilce.getMahalleler()) {
                m.mahalleceYaslan();
            }
        }

        // 3. ADIM: Katsayıyı hesapla (2+4 = 6)
        int katsayi = getGuncelKatsayi();
        
        // 4. ADIM: Matematiksel artışı yap ve bölünebilirliğe uydur
        this.nufus = katsayi * this.nufus;
        this.nufus = nufusuDuzenle(this.nufus, this.mahalleSayisi);
        
        // 5. ADIM: Eksik kişileri fiziksel olarak oluştur (Faker ile isim üretimi)
        yeniNufusEkle();
        
        // 6. ADIM: Son durumu say ve eşitle (Tabloya basılacak son veri)
        nufusGuncelle();
    }
    public void nufusGuncelle() {
        nufus = 0;
        for (Ilce i : ilceler) {
            i.nufusGuncelle();
            nufus += i.getNufus();
        }
    }
    // ANA METOD: İlk kurulumu yapar
    public int tumHesapla(int sayi) {
        sayiyiDuzenle(sayi);                              // Kodları belirle
        this.nufus = nufusuDuzenle(0, this.mahalleSayisi); // İlk düzenlenmiş nüfusu oluştur
        mahalleleriDagit();                               // Nesne yapısını kur
        return this.nufus;                                // İlk turdaki nüfusu dön
    }

    // ADIM 3: Mahalleri ilçelere dağıt
    public void mahalleleriDagit() {
        ilceler.clear();
        if (ilceSayisi == 0) return;

        int mahallePerIlce = mahalleSayisi / ilceSayisi;
        int kisiPerMahalle = nufus / mahalleSayisi; // 🔥 kritik

        for (int i = 0; i < ilceSayisi; i++) {

            Ilce ilce = new Ilce(faker.address().cityName());

            for (int j = 0; j < mahallePerIlce; j++) {

                Mahalle m = new Mahalle(faker.address().streetName());

                m.kisiOlustur(kisiPerMahalle); // 🔥 ASIL OLAY

                ilce.mahalleEkle(m);
            }

            ilceler.add(ilce);
        }
    }
    public Sehir bolunmeGerceklestir() {

        Faker faker = new Faker(new Locale("tr"));
        Sehir yeniSehir = new Sehir(faker.address().cityName() + " (Yeni)");

        // 🔥 NÜFUS BÖL
        int eskiNufus = this.nufus;
        this.nufus = eskiNufus / 2;
        yeniSehir.nufus = eskiNufus - this.nufus;

        // 🔥 İLÇE TAŞI
        int tasinacak = this.ilceler.size() / 2;

        for (int i = 0; i < tasinacak; i++) {
            yeniSehir.ilceEkle(this.ilceler.remove(this.ilceler.size() - 1));
        }

        // 🔥 SADECE ilceSayisi güncelle
        this.ilceSayisi = this.ilceler.size();
        yeniSehir.ilceSayisi = yeniSehir.ilceler.size();

        // ❗ MAHALLE SAYISINA DOKUNMA
       // yeniSehir.mahalleSayisi = this.mahalleSayisi;
        this.nufusGuncelle();      // Kalan ilçelere göre eski şehrin nüfusunu say
        yeniSehir.nufusGuncelle();
        return yeniSehir;
    }
    public int toplamMahalle() {
        int toplam = 0;

        for (Ilce i : ilceler) {
            toplam += i.getMahalleler().size();
        }

        return toplam;
    }
	private void ilceEkle(Ilce ilce) {
		 this.ilceler.add(ilce);
		
	}
	public void yeniNufusEkle(){
		if (mahalleSayisi == 0) return;

	    // Her mahalleye düşmesi gereken hedef kişi sayısı
	    int hedefMahalleNufusu = this.nufus / this.mahalleSayisi;

	    for (Ilce ilce : ilceler) {
	        for (Mahalle m : ilce.getMahalleler()) {
	            // Mevcut kişi sayısını kontrol et, sadece hedefi tamamlayacak kadar ekle
	            int eksik = hedefMahalleNufusu - m.getNufus();
	            
	            if (eksik > 0) {
	                m.kisiOlustur(hedefMahalleNufusu); // Senin metodun (adet - size) yapıyorsa hedefi gönder
	                // Eğer metodun direkt 'adet' kadar ekliyorsa 'eksik' değişkenini gönder
	            } 
	        }
	    }
	}
	public void kisileriListele(int adet) {
	    System.out.println("Sehirdeki bazi sakinler (Isim - Yas - ID):");
	    int sayac = 0;
	    
	    // Hiyerarşiyi tarayarak kişilere ulaşıyoruz
	    for (Ilce ilce : ilceler) {
	        for (Mahalle m : ilce.getMahalleler()) {
	            for (Kisi k : m.getKisiler()) {
	                System.out.printf("- %-20s | - %2d |-%d %n", 
	                		k.getId(),k.getTamAd(), k.getYas() );
	                sayac++;
	                if (sayac >= adet) return; // Belirlenen adet kadar göster ve dur
	            }
	        }
	    }
	    if (sayac == 0) System.out.println("Bu sehirde henuz kimse yasamiyor.");
	}

	public void tumDetaylariYazdir() {
	    
	    System.out.println("Sehir: " + this.ad + " - Nufus: " + this.nufus);
	  

	    for (Ilce ilce : ilceler) {
	        // İlçe bilgilerini yazdır
	        System.out.println("\n  Ilce: " + ilce.getAd() + " - Nufus: " + ilce.getNufus());
	        

	        for (Mahalle mahalle : ilce.getMahalleler()) {
	            // Mahalle bilgilerini yazdır
	            System.out.println("    Mahalle: " + mahalle.getAd() + " - Nufus: " + mahalle.getNufus());
	            System.out.println("    Kısıler:");

	            // Mahalledeki TÜM kişileri listele
	            for (Kisi kisi : mahalle.getKisiler()) {
	                System.out.printf("      > [%-4d] %-20s (Yas: %d)%n", 
	                                  kisi.getId(), kisi.getTamAd(), kisi.getYas());
	            }
	           
	        }
	    }
	}
	
}
