package oyun3;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.github.javafaker.Faker;
public class Mahalle {
	 private String ad;
	    private int nufus;
	    private ArrayList<Kisi> kisiler;
	    private static int idCounter = 1; 
	    private static Faker faker = new Faker(new Locale("tr"));
	    public Mahalle(String ad) {
	        this.ad = ad;
	        this.kisiler = new ArrayList<>();
	        this.nufus = 0;
	    }

	    public void kisiOlustur(int adet) {
	    	int eklenecekSayi = adet - kisiler.size();
	        
	        for (int i = 0; i < eklenecekSayi; i++) {
	           
	            String tamAd = faker.name().fullName();
	            int yas = faker.number().numberBetween(0, 50);
	            int id = idCounter++;
	            
	           
	            kisiler.add(new Kisi(tamAd, yas, id));
	        }
	        this.nufus =kisiler.size();
	        
	    }

	    public int getNufus() {
	        return nufus;
	    }
	    public String getAd() {
	    	return ad;
	    }
	    public ArrayList<Kisi> getKisiler() {
	        return this.kisiler;
	    }
	 
	    public void mahalleceYaslan() {
	        for (Kisi k : kisiler) {
	            k.yaslan();
	        }
	    }
}
