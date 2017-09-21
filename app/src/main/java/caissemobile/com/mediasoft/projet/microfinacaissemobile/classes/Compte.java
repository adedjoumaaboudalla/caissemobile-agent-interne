package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

import java.util.Date;

/**
 * Created by mediasoft on 13/05/2016.
 */
public class Compte {

    public static final String EPARGNE = "Epargne";
    public static final String TONTINE = "Tontine";
    public static final String CREDIT = "Credit";
    long id = 0 ;
    String nom = "" ;
    String nummembre = "" ;
    String prenom = "" ;
    String sexe = "" ;
    String numcompte = "" ;
    String pin = "" ;
    Date datecreation = null ;
    int nbrecredit =0 ;
    float solde =0 ;
    float soldedisponible =0 ;
    private String numProduit= "" ;
    private String produit= "" ;
    private String mise= "" ;
    private String miseLibre= "" ;
    private String type= "" ;

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getNummembre() {
        return nummembre;
    }

    public void setNummembre(String nummembre) {
        this.nummembre = nummembre;
    }

    public int getNbrecredit() {
        return nbrecredit;
    }

    public void setNbrecredit(int nbrecredit) {
        this.nbrecredit = nbrecredit;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getNumcompte() {
        return numcompte;
    }

    public void setNumcompte(String numcompte) {
        this.numcompte = numcompte;
    }

    public Date getDatecreation() {
        return datecreation;
    }

    public void setDatecreation(Date datecreation) {
        this.datecreation = datecreation;
    }

    public float getSolde() {
        return solde;
    }

    public void setSolde(float solde) {
        this.solde = solde;
    }

    public void setNumProduit(String numProduit) {
        this.numProduit = numProduit;
    }

    public String getNumProduit() {
        return numProduit;
    }

    public void setProduit(String produit) {
        this.produit = produit;
    }

    public String getProduit() {
        return produit;
    }

    public void setMise(String mise) {
        this.mise = mise;
    }

    public String getMise() {
        return mise;
    }

    public void setMiseLibre(String miseLibre) {
        this.miseLibre = miseLibre;
    }

    public String getMiseLibre() {
        return miseLibre;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public float getSoldedisponible() {
        return soldedisponible;
    }

    public void setSoldedisponible(float soldedisponible) {
        this.soldedisponible = soldedisponible;
    }
}
