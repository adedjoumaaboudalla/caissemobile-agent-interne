package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

import java.util.Date;

/**
 * Created by Abdallah on 06/04/2016.
 */
public class Operation {
    long id = 0 ;
    String agence = null ;
    String numpiece = null ;
    String numcompte = null ;
    String numproduit = null ;
    String nummenbre = null ;
    String nom = null ;
    String prenom = null ;
    String libelle = null ;
    float montant = 0 ;
    float mise = 0 ;
    int archiver = 0 ;
    int typeoperation = 0 ;
    long user_id = 0 ;
    String token = null ;
    Date dateoperation = null ;

    String numpicedef = "-" ;
    int sync = 0 ;



    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNumpicedef() {
        return numpicedef;
    }

    public void setNumpicedef(String numpicedef) {
        this.numpicedef = numpicedef;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public Operation(Date dateoperation) {
        this.dateoperation = new Date();
    }

    public Operation() {
        dateoperation = new Date() ;
    }

    public String getAgence() {
        return agence;
    }

    public void setAgence(String agence) {
        this.agence = agence;
    }

    public String getNumpiece() {
        return numpiece;
    }

    public void setNumpiece(String numpiece) {
        this.numpiece = numpiece;
    }

    public String getNumcompte() {
        return numcompte;
    }

    public void setNumcompte(String numcompte) {
        this.numcompte = numcompte;
    }

    public String getNummenbre() {
        return nummenbre;
    }

    public void setNummenbre(String nummenbre) {
        this.nummenbre = nummenbre;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public float getMise() {
        return mise;
    }

    public void setMise(float mise) {
        this.mise = mise;
    }

    public int getTypeoperation() {
        return typeoperation;
    }

    public void setTypeoperation(int typeoperation) {
        this.typeoperation = typeoperation;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public Date getDateoperation() {
        return dateoperation;
    }

    public void setDateoperation(Date dateoperation) {
        this.dateoperation = dateoperation;
    }

    public String getNumproduit() {
        return numproduit;
    }

    public void setNumproduit(String numproduit) {
        this.numproduit = numproduit;
    }

    public int getArchiver() {
        return archiver;
    }

    public void setArchiver(int archiver) {
        this.archiver = archiver;
    }
}
