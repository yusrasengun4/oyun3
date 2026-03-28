package oyun3;

public class Kisi {
    private String tamAd;
    private int yas;
    private int id;

    public Kisi(String tamAd, int yas, int id) {
        this.tamAd = tamAd;
        this.yas = yas;
        this.id = id;
    }

    public String getTamAd() {
        return tamAd;
    }

    public int getYas() {
        return yas;
    }

    public int getId() {
        return id;
    }
    public void yaslan() {
    	this.yas++;
    }
}
