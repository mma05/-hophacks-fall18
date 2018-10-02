package com.example.transit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class EndNeighborhood extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton startStop;

    static String selected2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        String select = StartNeighborhood.selected1;

        try {
            JSONArray jsonArray = new JSONArray("[{\"neighborhoods\":\"Charles Village,Mount Vernon,Inner Harbor,Federal Hill,Charles Center\",\"operator\":\"Charm City Circulator\",\"route\":\"purple\",\"stops\":[{\"stopName\":\"33rd St\",\"neighborhood\":\"Charles Village\",\"schedule\":\"08:00,09:00,10:00,11:00,12:00\"},{\"stopName\":\"Eager St\",\"neighborhood\":\"Mount Vernon\",\"schedule\":\"08:20,09:20,10:20,11:20,12:20\"},{\"stopName\":\"Light St\",\"neighborhood\":\"Inner harbor\",\"schedule\":\"08:40,09:40,10:40,11:40,12:40\"},{\"stopName\":\"Ostend St\",\"neighborhood\":\"Federal Hill\",\"schedule\":\"09:00,10:00,11:00,12:00,13:00\"},{\"stopName\":\"Fayette St\",\"neighborhood\":\"Charles Center\",\"schedule\":\"09:20,10:20,11:20,12:20,13:20\"}]},{\"neighborhoods\":\"Towson,Woodbrook,Mid Charles,Charles Village,Mount Vernon,Inner Harbor\",\"operator\":\"MTA\",\"route\":\"LocalLink 51\",\"stops\":[{\"stopName\":\"Towson Town Center\",\"neighborhood\":\"Towson\",\"schedule\":\"10:00,12:00,14:00,16:00\"},{\"stopName\":\"Greater Baltimore Medical Center\",\"neighborhood\":\"Mid Charles\",\"schedule\":\"10:30,12:30,14:30,16:30\"},{\"stopName\":\"33rd St\",\"neighborhood\":\"Charles Village\",\"schedule\":\"11:00,13:00,15:00,17:00\"},{\"stopName\":\"Eager St\",\"neighborhood\":\"Mount Vernon\",\"schedule\":\"11:30,13:30,15:30,17:30\"},{\"stopName\":\"Light St\",\"neighborhood\":\"Inner Harbor\",\"schedule\":\"12:00,14:00,16:00,18:00\"}]},{\"neighborhoods\":\"Lauraville,Waverly,Charles Village,Inner Harbor, Charles Center\",\"operator\":\"MTA\",\"route\":\"CityLink Silver\",\"stops\":[{\"stopName\":\"Morgan State University\",\"neighborhood\":\"Lauraville\",\"schedule\":\"15:00,17:00,19:00,21:00;23:00\"},{\"stopName\":\"Greenmount\",\"neighborhood\":\"Waverly\",\"schedule\":\"15:15,17:15,19:15,21:15;23:15\"},{\"stopName\":\"33rd St\",\"neighborhood\":\"Charles Village\",\"schedule\":\"15:30,17:30,19:30,21:30;23:30\"},{\"stopName\":\"Light St\",\"neighborhood\":\"Inner Harbor\",\"schedule\":\"15:45,17:45,19:45,21:45;23:45\"},{\"stopName\":\"Fayette St\",\"neighborhood\":\"Charles Center\",\"schedule\":\"16:00,18:00,20:00,22:00;23:55\"}]}]");

            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_end_neighborhood);

            radioGroup = findViewById(R.id.start);
            String[] neighbourhoods = findEnd(select, jsonArray);

            for (int i = 0; i < neighbourhoods.length; i++) {
                startStop = new RadioButton(this);
                startStop.setText("Stop\n" + neighbourhoods[i]);
                startStop.setId(i);
                startStop.setTextSize(20);
                startStop.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openEnd(radioGroup.getCheckedRadioButtonId());
                    }
                });
                radioGroup.addView(startStop);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void openEnd(int id) {
        RadioButton rb = radioGroup.findViewById(id);
        selected2 = ((String) rb.getText()).substring(5);
        Intent intent = new Intent( this, StopNumber.class);
        startActivity(intent);

    }

    public boolean repeat(String n, String[] arr, int l) {
        boolean flag = false;
        for (int i = 0; i < l; i++) {
            if (arr[i].equals(n)) {
                flag = true;
                break;
            }
        }
        return flag;

    }


    /** Search for end neighborhoods */
    public String[] findEnd(String start, JSONArray jsonArray) throws JSONException {
        try {
            String[] temp = new String[50];
            int size = 0;
            //System.out.println(jsonArray.toString());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JO = (JSONObject) jsonArray.get(i);
                String neighborhoodsAll = (String) JO.get("neighborhoods");
                if (neighborhoodsAll.contains(start)) {
                    //System.out.println("Here has the end stations!");
                    String[] neighborhoods = neighborhoodsAll.split(",");
                    for (String n : neighborhoods) {
                        if (!repeat(n, temp, size)) {
                            //System.out.println("find" + n);
                            temp[size] = n;
                            size++;
                        }
                    }
                }
            }

            String[] endNeighborhoods = new String[size];
            for (int j = 0; j < size; j++) {
                endNeighborhoods[j] = temp[j];
            }
            return endNeighborhoods;

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
