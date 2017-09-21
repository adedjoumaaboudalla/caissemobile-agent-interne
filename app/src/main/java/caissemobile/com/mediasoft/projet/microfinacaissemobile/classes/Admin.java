package caissemobile.com.mediasoft.projet.microfinacaissemobile.classes;

/**
 * Created by mediasoft on 13/05/2016.
 */
public class Admin {
    long id = 0 ;
    String login = null ;
    String password = null ;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
