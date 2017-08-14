package com.example.kille.ostdeploy;


import android.util.Log;
import org.json.JSONArray;
import org.json.JSONObject;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by n01179251 on 7/10/2017.
 */

public class ServerCommunication {


    private static final String computerName = "DESKTOP-43VDM4D";
    private static String ipAddr = "";
    private static boolean onJob = false;
    private static Tasks currentTask = null;
    private static Main2Activity act;
    private static String accepted = null;
    private static String name = "";
    private static boolean loggedIn = false;

    public ServerCommunication() {
        try {
            InetAddress addr = InetAddress.getByName(computerName+".humber.org");
            ipAddr = addr.getHostAddress();
            name = Transfer.getName();
        } catch (Exception e) {
            Log.e("debug", e.getMessage());
        }
    }

    public void setAct(Main2Activity act) {
        this.act = act;
        updateTasks();
    }

    public void updateTasks() {
        getSosTasks();
        getAvailableTasks();
        act.addToList();
    }

    public void setLoggedIn(boolean tof) {
        loggedIn = tof;
    }


    public Main2Activity getAct(){
        return act;
    }

    public void sosTask(String notes) {
        try {
            URL url = new URL("http://"+ipAddr+":8080/sos/"+currentTask.getTaskName()+"/"+currentTask.getTaskDescription()+"/"+notes);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                Log.e("TEST","CODE: "+responseCode);
            }
        }catch(Exception e) {
            Log.e("TEST",e.getMessage());
        }
        currentTask.setSos(true);
        act.setButtonColor("FF0000");
    }

    public void getSosTasks() {
        try {
            TasksUpdater.setSosTasks(new ArrayList<SosTasks>());
            URL url = new URL("http://"+ipAddr+":8080/sostasks");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                Log.e("TEST","CODE: "+responseCode);
                getSosTasks();

            }else {
                Scanner sc = new Scanner(conn.getInputStream());
                String inLine = "";
                while(sc.hasNext()) {
                    inLine += sc.nextLine();
                }

                JSONArray json = new JSONArray(inLine);
                for (int i = 0;i < json.length();i++) {
                    JSONObject jObject = json.getJSONObject(i);
                    TasksUpdater.getSosTasks().add(i,new SosTasks(jObject.get("name").toString(),jObject.get("description").toString(),jObject.get("notes").toString()));
                }
            }
        }catch(Exception e) {
            Log.e("TEST",e.getMessage());
            getSosTasks();
        }
    }

    public void getAvailableTasks() {
        try {
            TasksUpdater.setTasks(new ArrayList<Tasks>());
            URL url = new URL("http://"+ipAddr+":8080/tasks");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                Log.e("TEST","CODE: "+responseCode);
                getAvailableTasks();
            }else {
                Scanner sc = new Scanner(conn.getInputStream());
                String inLine = "";
                while(sc.hasNext()) {
                    inLine += sc.nextLine();
                }

                JSONArray json = new JSONArray(inLine);
                for (int i = 0;i < json.length();i++) {
                    JSONObject jObject = json.getJSONObject(i);
                    String isSos = jObject.get("sos").toString();
                    String completedS = jObject.get("itCompleted").toString();

                    boolean sos;
                    boolean completed;
                    if (isSos.equals("true")) {
                        sos = true;
                    }else {
                        sos = false;
                    }
                    if (completedS.equals("true")) {
                        completed = true;
                    }else {
                        completed = false;
                    }
                    TasksUpdater.getTasks().add(i,new Tasks(sos,jObject.get("taskName").toString(),jObject.get("tech").toString(),completed,jObject.get("taskDescription").toString(),jObject.get("notes").toString()));
                }
            }
        }catch(Exception e) {
            getAvailableTasks();
            Log.e("TEST",e.getMessage());
        }
    }


    public boolean isOnJob() {
        return onJob;
    }

    public void acceptTask(Tasks acceptedTask) {
        try {
            URL url = new URL("http://"+ipAddr+":8080/accept/"+name+"/"+acceptedTask.getTaskName());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                Log.e("TEST","CODE: "+responseCode);
            }

            Scanner sc = new Scanner(conn.getInputStream());
            String inLine = "";
            while(sc.hasNext()) {
                inLine += sc.nextLine();
            }
            if(inLine.contains("done")) {
                currentTask = acceptedTask;
                onJob = true;
                act.setButtonColor("ffffbb33");
                act.showMessage("Go do your job!");
            }else {
                act.showMessage("Seems like the job you tried to take isn't available.");
            }
        }catch(Exception e) {
            Log.e("TEST",e.getMessage());
        }

    }
    public static boolean login(String username, String password) {
        try {
            URL url = new URL("http://"+ipAddr+":8080/login/"+username+"/"+password);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                Log.e("TEST","CODE: "+responseCode);
            }

            Scanner sc = new Scanner(conn.getInputStream());
            String inLine = "";
            while(sc.hasNext()) {
                inLine += sc.nextLine();
            }
            if(inLine.contains("done")) {
               return true;
            }else {
                return false;
            }
        }catch(Exception e) {
            Log.e("TEST",e.getMessage());
            return false;
        }

    }
    public static String getCurrentTaskName() {
        return currentTask.getTaskName();
    }

    public static boolean isLoggedIn() {
        return loggedIn;
    }
    public void finishJob(String notes) {
        try {
            URL url = new URL("http://"+ipAddr+":8080/finish/"+name+"/" + currentTask.getTaskName()+"/"+notes);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                Log.e("TEST", "CODE: " + responseCode);
            }else {
                if (currentTask.getSos()) {
                    turnOffSos();
                }
                currentTask = null;
                onJob = false;
                act.setButtonColor("03FF13");
            }
        } catch (Exception e) {
            Log.e("TEST", e.getMessage());
        }
    }

    public void turnOffSos() {
        try {
            URL url = new URL("http://"+ipAddr+":8080/nosos/" + currentTask.getTaskName());
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                Log.e("TEST", "CODE: " + responseCode);
            }
        } catch (Exception e) {
            Log.e("TEST", e.getMessage());
        }
        currentTask.setSos(false);
        act.setButtonColor("ffffbb33");
    }

    public static String getCurrentTaskDescription() {
        return currentTask.getTaskDescription();
    }

    public Tasks getCurrentTask() {

        return currentTask;
    }


}