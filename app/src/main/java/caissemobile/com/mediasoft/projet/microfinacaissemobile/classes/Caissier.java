    package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

/**
 * Created by Abdallah on 06/04/2016.
 */
public class Caissier {
    long id = 0 ;
    String agence_id = "" ;
    String nomprenom = null ;
    String login = null ;
    String password = null ;
    String codeguichet = null ;
    float solde = 0 ;
    double retraitMax = 0 ;

    int numpiece = 0 ;
    String description ;
    String ddb ;
    String dl ;
    String dp ;
    String di ;
    String journee = null;

    public int getNumpiece() {
        return numpiece;
    }

    public void setNumpiece(int numpiece) {
        this.numpiece = numpiece;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDdb() {
        return ddb;
    }

    public void setDdb(String ddb) {
        this.ddb = ddb;
    }

    public String getDl() {
        return dl;
    }

    public void setDl(String dl) {
        this.dl = dl;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getDi() {
        return di;
    }

    public void setDi(String di) {
        this.di = di;
    }

    public String getNomprenom() {
        return nomprenom;
    }

    public float getSolde() {
        return solde;
    }

    public void setSolde(float solde) {
        this.solde = solde;
    }

    public void setNomprenom(String nomprenom) {
        this.nomprenom = nomprenom;
    }

    public String getAgence_id() {
        return agence_id;
    }

    public void setAgence_id(String agence_id) {
        this.agence_id = agence_id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCodeguichet() {
        return codeguichet;
    }

    public void setCodeguichet(String codeguichet) {
        this.codeguichet = codeguichet;
    }

    public double getRetraitMax() {
        return retraitMax;
    }

    public void setRetraitMax(double retraitMax) {
        this.retraitMax = retraitMax;
    }

    public void setJournee(String journee) {
        this.journee = journee;
    }

    public String getJournee() {
        return journee;
    }
}
