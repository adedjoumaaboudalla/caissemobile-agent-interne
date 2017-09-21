package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class Produit {
    long id = 0 ;
    String numproduit = null ;
    String libelle = null ;
    String code = null ;
    double depotmini = 0 ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNumproduit() {
        return numproduit;
    }

    public void setNumproduit(String numproduit) {
        this.numproduit = numproduit;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public double getDepotmini() {
        return depotmini;
    }

    public void setDepotmini(double depotmini) {
        this.depotmini = depotmini;
    }
}
