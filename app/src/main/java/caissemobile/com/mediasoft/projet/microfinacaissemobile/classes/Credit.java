package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.ParseException;
import java.util.Date;

import caissemobile.com.mediasoft.projet.microfinacaissemobile.dao.DAOBase;


/**
 * Created by mediasoft on 13/05/2016.
 */
public class Credit implements Parcelable {
    long id = 0 ;
    String numcredit ;
    String nom = null ;
    String prenom = null ;
    float montantpret  ;
    float creaditencours  ;
    String nummembre = null ;
    float mensualite = 0 ;
    int durepret = 0;
    float tauxan ;
    String suivi = null ;
    String nomproduit = null ;
    float capital_attendu = 0 ;
    float interet_attendu = 0  ;
    float capital_retard = 0 ;
    float interet_retard = 0  ;
    float total_retard = 0  ;
    float reste_apayer = 0  ;
    float penalite = 0  ;
    Date datedeblocage  ;
    Date datedebut = null ;

    public Credit() {
    }

    public float getPenalite() {
        return penalite;
    }

    public void setPenalite(float penalite) {
        this.penalite = penalite;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    protected Credit(Parcel in) {
        id = in.readLong();
        numcredit = in.readString();
        nom = in.readString();
        prenom = in.readString();
        montantpret = in.readFloat();
        creaditencours = in.readFloat();
        nummembre = in.readString();
        mensualite = in.readFloat();
        durepret = in.readInt();
        tauxan = in.readFloat();
        suivi = in.readString();
        nomproduit = in.readString();
        capital_attendu = in.readFloat();
        interet_attendu = in.readFloat();
        capital_retard = in.readFloat();
        interet_retard = in.readFloat();
        reste_apayer = in.readFloat();

        try {
            datedeblocage = DAOBase.formatterj.parse(in.readString());
            datedebut = DAOBase.formatterj.parse(in.readString());
        } catch (ParseException e) {
            e.printStackTrace();
        }catch (NullPointerException e) {
            e.printStackTrace();
        }


    }

    public static final Creator<Credit> CREATOR = new Creator<Credit>() {
        @Override
        public Credit createFromParcel(Parcel in) {
            return new Credit(in);
        }

        @Override
        public Credit[] newArray(int size) {
            return new Credit[size];
        }
    };

    public float getReste_apayer() {
        return reste_apayer;
    }

    public void setReste_apayer(float reste_apayer) {
        this.reste_apayer = reste_apayer;
    }

    public String getNumcredit() {
        return numcredit;
    }

    public void setNumcredit(String numcredit) {
        this.numcredit = numcredit;
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

    public float getMontantpret() {
        return montantpret;
    }

    public void setMontantpret(float montantpret) {
        this.montantpret = montantpret;
    }

    public Date getDatedeblocage() {
        return datedeblocage;
    }

    public void setDatedeblocage(Date datedeblocage) {
        this.datedeblocage = datedeblocage;
    }

    public float getCreaditencours() {
        return creaditencours;
    }

    public void setCreaditencours(float creaditencours) {
        this.creaditencours = creaditencours;
    }

    public String getNummembre() {
        return nummembre;
    }

    public void setNummembre(String nummembre) {
        this.nummembre = nummembre;
    }

    public float getMensualite() {
        return mensualite;
    }

    public void setMensualite(float mensualite) {
        this.mensualite = mensualite;
    }

    public int getDurepret() {
        return durepret;
    }

    public void setDurepret(int durepret) {
        this.durepret = durepret;
    }

    public Date getDatedebut() {
        return datedebut;
    }

    public void setDatedebut(Date datedebut) {
        this.datedebut = datedebut;
    }

    public float getTauxan() {
        return tauxan;
    }

    public void setTauxan(float tauxan) {
        this.tauxan = tauxan;
    }

    public String getSuivi() {
        return suivi;
    }

    public void setSuivi(String suivi) {
        this.suivi = suivi;
    }

    public String getNomproduit() {
        return nomproduit;
    }

    public void setNomproduit(String nomproduit) {
        this.nomproduit = nomproduit;
    }


    public float getCapital_attendu() {
        return capital_attendu;
    }

    public void setCapital_attendu(float capital_attendu) {
        this.capital_attendu = capital_attendu;
    }

    public float getInteret_attendu() {
        return interet_attendu;
    }

    public void setInteret_attendu(float interet_attendu) {
        this.interet_attendu = interet_attendu;
    }

    public float getCapital_retard() {
        return capital_retard;
    }

    public void setCapital_retard(float capital_retard) {
        this.capital_retard = capital_retard;
    }

    public float getInteret_retard() {
        return interet_retard;
    }

    public void setInteret_retard(float interet_retard) {
        this.interet_retard = interet_retard;
    }

    public float getTotal_retard() {
        return total_retard;
    }

    public void setTotal_retard(float total_retard) {
        this.total_retard = total_retard;
    }

    public static Creator<Credit> getCREATOR() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(numcredit);
        parcel.writeString(nom);
        parcel.writeString(prenom);
        parcel.writeFloat(montantpret);
        parcel.writeFloat(creaditencours);
        parcel.writeString(nummembre);
        parcel.writeFloat(mensualite);
        parcel.writeInt(durepret);
        parcel.writeFloat(tauxan);
        parcel.writeString(suivi);
        parcel.writeString(nomproduit);
        parcel.writeFloat(capital_attendu);
        parcel.writeFloat(interet_attendu);
        parcel.writeFloat(capital_retard);
        parcel.writeFloat(interet_retard);
        parcel.writeFloat(total_retard);
        parcel.writeFloat(reste_apayer);
        parcel.writeString(DAOBase.formatterj.format(datedeblocage));
        if (datedebut!=null)parcel.writeString(DAOBase.formatterj.format(datedebut));
    }


}
