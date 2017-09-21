package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;


import java.util.Date;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class Membre {
    long id = 0 ;
    String nummembre = "" ;
    Date dateadhesion = null ;
    Date dateNaiss;
    String nom = "" ;
    String prenom = "";
    String sexe ;
    String tel ;
    long categorie ;
    long profession ;
    long nationalite ;
    String zone ;
    String photo = null;
    String adresse = "";
    float montant ;
    float saisi ;


    int type ; // Groupe = 1 ; Individu = 0 ;
    String numgroupe = "" ;
    String codemembre = "" ;
    String idpersonne = "" ;
    String poste ;
    String codeRetrait = "" ;
    String activite ;
    int sync = 1 ;

    public Membre() {
        this.dateadhesion = new Date();
        this.dateNaiss = new Date();
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCodeRetrait() {
        return codeRetrait;
    }

    public void setCodeRetrait(String codeRetrait) {
        this.codeRetrait = codeRetrait;
    }

    public String getPoste() {
        return poste;
    }

    public void setPoste(String poste) {
        this.poste = poste;
    }

    public String getActivite() {
        return activite;
    }

    public void setActivite(String activite) {
        this.activite = activite;
    }

    public int getSync() {
        return sync;
    }

    public void setSync(int sync) {
        this.sync = sync;
    }

    public Date getDateadhesion() {
        return dateadhesion;
    }

    public void setDateadhesion(Date dateadhesion) {
        this.dateadhesion = dateadhesion;
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

    public String getSexe() {
        return sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public long getCategorie() {
        return categorie;
    }

    public String getNummembre() {
        return nummembre;
    }

    public void setNummembre(String nummembre) {
        this.nummembre = nummembre;
    }

    public void setCategorie(long categorie) {
        this.categorie = categorie;
    }

    public long getProfession() {
        return profession;
    }

    public void setProfession(long profession) {
        this.profession = profession;
    }

    public long getNationalite() {
        return nationalite;
    }

    public void setNationalite(long nationalite) {
        this.nationalite = nationalite;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public float getMontant() {
        return montant;
    }

    public void setMontant(float montant) {
        this.montant = montant;
    }

    public void setDateNaiss(Date dateNaiss) {
        this.dateNaiss = dateNaiss;
    }

    public Date getDateNaiss() {
        return dateNaiss;
    }

    public String getNumgroupe() {
        return numgroupe;
    }

    public void setNumgroupe(String numgroupe) {
        this.numgroupe = numgroupe;
    }

    public float getSaisi() {
        return saisi;
    }

    public void setSaisi(float saisi) {
        this.saisi = saisi;
    }

    public String getCodemembre() {
        return codemembre;
    }

    public void setCodemembre(String codemembre) {
        this.codemembre = codemembre;
    }

    public String getIdpersonne() {
        return idpersonne;
    }

    public void setIdpersonne(String idpersonne) {
        this.idpersonne = idpersonne;
    }
}
