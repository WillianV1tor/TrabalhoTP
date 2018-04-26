package com.example.willian.movieme;

/**
 * Created by Willian on 22/04/2018.
 */

public class Login {
    private  static String usuario = "";
    private  static String email = "";
    private static long quantidadeFilmes = 0;
    private static long lastChild = 0;

    public Login(String usuario, String email, long quantidadeFilmes, long lastChild) {
        this.usuario = usuario;
        this.email = email;
        this.quantidadeFilmes = quantidadeFilmes;
        this.lastChild = lastChild;
    }

    public Login() {
        this(null, null, 0, 0);
    }

    //get
    public static String getUsuario() {
        return usuario;
    }

    //set
    public static void setUsuario(String usuario2) {
        usuario = usuario2;
    }

    //get
    public static long getQuantidadeFilmes() {
        return quantidadeFilmes;
    }

    //get
    public static long getlastChild() {
        return lastChild;
    }

    //set
    public static void setQuantidadeFilmes(long quantidadeFilmes2) {
        quantidadeFilmes = quantidadeFilmes2;
    }

    //set
    public static void setLastChild(long lastChild2) {
        lastChild = lastChild2;
    }

    //get
    public static String getEmail() {
        return email;
    }

    //set
    public static void setEmail(String Email2) {
        email = Email2;
    }
}
