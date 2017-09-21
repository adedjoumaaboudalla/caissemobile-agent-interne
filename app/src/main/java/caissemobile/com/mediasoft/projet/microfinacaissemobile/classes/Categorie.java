package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class Categorie {
    long id = 0 ;
    String numCategorie = null ;
    String libelleCategorie = null ;
    String libelleFrais = null ;
    float valeurFrais = 0 ;
    String numproduit = null ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNumCategorie() {
        return numCategorie;
    }

    public void setNumCategorie(String numCategorie) {
        this.numCategorie = numCategorie;
    }

    public String getLibelleCategorie() {
        return libelleCategorie;
    }

    public void setLibelleCategorie(String libelleCategorie) {
        this.libelleCategorie = libelleCategorie;
    }

    public String getLibelleFrais() {
        return libelleFrais;
    }

    public void setLibelleFrais(String libelleFrais) {
        this.libelleFrais = libelleFrais;
    }

    public float getValeurFrais() {
        return valeurFrais;
    }

    public void setValeurFrais(float valeurFrais) {
        this.valeurFrais = valeurFrais;
    }

    public String getNumproduit() {
        return numproduit;
    }

    public void setNumproduit(String numproduit) {
        this.numproduit = numproduit;
    }
}
