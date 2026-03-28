package oyun3;
import java.util.ArrayList;
import java.util.List;
public class Ilce {
	 private String ad;
	    private int nufus;
	    private ArrayList<Mahalle> mahalleler;

	    public Ilce(String ad) {
	        this.ad = ad;
	        this.mahalleler = new ArrayList<>();
	        this.nufus = 0;
	    }

	    public void mahalleEkle(Mahalle m) {
	        mahalleler.add(m);
	    }
	    public ArrayList<Mahalle> getMahalleler() {
	        return mahalleler;
	    }
	    public void nufusGuncelle() {
	        nufus = 0;
	        for (Mahalle m : mahalleler) {
	            nufus += m.getNufus();
	        }
	    }

	    public int getNufus() {
	        return nufus;
	    }

		public String getAd() {
			
			return ad;
		}
}
