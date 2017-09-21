package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

/**
 * Created by mediasoft on 09/05/2016.
 */
public class Zone {
    long id = 0 ;
    String description = null ;

    public String getDescription() {
        return description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
