package br.com.atlantico.iot.server_java.domain;

/**
 * Created by everton on 26/06/17.
 */
public class User {

    private String username;

    private String passwd;

    private String jwt;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
