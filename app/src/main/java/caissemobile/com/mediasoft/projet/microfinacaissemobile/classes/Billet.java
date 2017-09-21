package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

/**
 * Created by Mayi on 08/01/2016.
 */
public class Billet {
    long id = -1 ;
    String libelle = null ;
    String devise = null ;
    float montant = 0 ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public float getMontant() {
        return montant;
    }

    public String getLibelle() {
        return libelle;
    }

    public String getDevise() {
        return devise;
    }

    public void setDevise(String devise) {
        this.devise = devise;
    }


    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }
}
