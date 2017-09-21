package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

/**
 * Created by mediasoft on 23/01/2017.
 */
public class OperationSec {
    long id = 0 ;
    String nummembre = null ;
    String idpersonne = "" ;
    double mte = 0 ;
    long operation_id = 0 ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNummembre() {
        return nummembre;
    }

    public void setNummembre(String nummembre) {
        this.nummembre = nummembre;
    }

    public double getMte() {
        return mte;
    }

    public void setMte(double mte) {
        this.mte = mte;
    }

    public long getOperation_id() {
        return operation_id;
    }

    public void setOperation_id(long operation_id) {
        this.operation_id = operation_id;
    }

    public String getIdpersonne() {
        return idpersonne;
    }

    public void setIdpersonne(String idpersonne) {
        this.idpersonne = idpersonne;
    }
}
