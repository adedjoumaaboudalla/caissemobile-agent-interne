package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class Profession {
    long numprofession = 0 ;
    String libelle = null ;

    public long getNumprofession() {
        return numprofession;
    }

    public void setNumprofession(long numprofession) {
        this.numprofession = numprofession;
    }

    public void setNumprofession(int numprofession) {
        this.numprofession = numprofession;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }
}
