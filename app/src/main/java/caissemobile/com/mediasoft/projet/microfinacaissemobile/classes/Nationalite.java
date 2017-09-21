package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class Nationalite {
    long numnation = 0 ;
    String libelle = null ;

    public long getNumnation() {
        return numnation;
    }

    public void setNumnation(long numnation) {
        this.numnation = numnation;
    }

    public void setNumnation(int numnation) {
        this.numnation = numnation;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
