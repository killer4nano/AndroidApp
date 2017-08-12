package com.example.kille.ostdeploy;

/**
 * Created by kille on 2017-08-11.
 */

public class Transfer {


    static ServerCommunication serverCommunication;
    private static String client = "";

    public static void setServerCom(ServerCommunication conn) {
        serverCommunication = conn;
    }

    public static void setClient(String c) {
        client = c;
    }

    public static String getName() {
        return client;
    }

    public static ServerCommunication getConnection() {
        return serverCommunication;
    }

}
