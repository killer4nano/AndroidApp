package com.example.kille.ostdeploy;

/**
 * Created by kille on 2017-08-11.
 */

public class Transfer {


    static ServerCommunication serverCommunication;
    private static String client = "";
    private static int id;

    public static void setServerCom(ServerCommunication conn) {
        serverCommunication = conn;
    }

    public static void setClient(String c) {
        client = c;
    }

    public static String getName() {
        return client;
    }

    public static int getId() {
        return id;
    }

    public static void setId(int i) {
        id = i;
    }

    public static ServerCommunication getConnection() {
        return serverCommunication;
    }

}
