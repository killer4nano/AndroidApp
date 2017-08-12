package com.example.kille.ostdeploy;


/**
 * Created by kille on 2017-07-16.
 */

public class Tasks {

    private String name;
    private String description;
    private String tech;
    private String notes;
    private boolean completed;
    private boolean sos = false;

    public Tasks (String taskName, String taskDescription) {
        name = taskName;
        description = taskDescription;
        tech = "none";
        completed = false;
        notes = "";
    }

    public Tasks(boolean isSos,String name,String tech,boolean completed, String description,String notes) {
        this.sos = isSos;
        this.name = name;
        this.tech = tech;
        this.completed = completed;
        this.description = description;
        this.notes = notes;

    }


    public void setCompleted() {

        completed = true;
    }

    public String getTaskDescription() {
        return description;
    }

    public String getTaskName() {
        return name;
    }

    public boolean isItCompleted() {
        return completed;
    }

    public String getTech() {
        return tech;
    }

    public void assignTech(String tech) {
        tech = tech;
    }

    public void setSos(boolean bool){
        sos = bool;
    }

    public boolean getSos() {
        return sos;
    }

    /*
     public void test(View v) {
        try {
            URL url = new URL("http://10.117.80.16:8080/tasks");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responseCode = conn.getResponseCode();
            if(responseCode != 200) {
                Log.e("TEST","CODE: "+responseCode);
                //supthrow new RuntimeException("Code:"  +responseCode);

            }else {
                Scanner sc = new Scanner(url.openStream());
                String inLine = "";
                while(sc.hasNext()) {
                    inLine += sc.nextLine();
                }
                Log.e("TEST",inLine);

                JSONArray json = new JSONArray(inLine);
                ArrayList<Tasks> allTasks = new ArrayList();
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
                        // magic comment needs more magic
                    }
                    if (completedS.equals("true")) {
                        completed = true;
                    }else {
                        completed = false;
                    }
                    allTasks.add(i,new Tasks(sos,jObject.get("taskName").toString(),jObject.get("tech").toString(),completed,jObject.get("taskDescription").toString(),jObject.get("notes").toString()));
                }
                Log.e("TEST",allTasks.get(0).getTaskName());

            }


        }catch(Exception e) {
            Log.e("TEST",e.getMessage());
        }

    }
     */


}


