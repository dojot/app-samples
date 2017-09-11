package br.com.atlantico.iot.server_java.domain;

/**
 * Created by everton on 26/06/17.
 */
public class Message {

    private String data;

    public Message() {}

    public Message(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
