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

import java.time.LocalTime;
import java.util.Arrays;

public class BusRoutes extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton route;

    static String selected4;
    static String selected5;
    static int hours;
    static int minutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_routes);

        try {
            JSONArray jsonArray = new JSONArray("[{\"neighborhoods\":\"Charles Village,Mount Vernon,Inner Harbor,Federal Hill,Charles Center\",\"operator\":\"Charm City Circulator\",\"route\":\"purple\",\"stops\":[{\"stopName\":\"33rd St\",\"neighborhood\":\"Charles Village\",\"schedule\":\"08:00,09:00,10:00,11:00,12:00\"},{\"stopName\":\"Eager St\",\"neighborhood\":\"Mount Vernon\",\"schedule\":\"08:20,09:20,10:20,11:20,12:20\"},{\"stopName\":\"Light St\",\"neighborhood\":\"Inner harbor\",\"schedule\":\"08:40,09:40,10:40,11:40,12:40\"},{\"stopName\":\"Ostend St\",\"neighborhood\":\"Federal Hill\",\"schedule\":\"09:00,10:00,11:00,12:00,13:00\"},{\"stopName\":\"Fayette St\",\"neighborhood\":\"Charles Center\",\"schedule\":\"09:20,10:20,11:20,12:20,13:20\"}]},{\"neighborhoods\":\"Towson,Woodbrook,Mid Charles,Charles Village,Mount Vernon,Inner Harbor\",\"operator\":\"MTA\",\"route\":\"LocalLink 51\",\"stops\":[{\"stopName\":\"Towson Town Center\",\"neighborhood\":\"Towson\",\"schedule\":\"10:00,12:00,14:00,16:00\"},{\"stopName\":\"Greater Baltimore Medical Center\",\"neighborhood\":\"Mid Charles\",\"schedule\":\"10:30,12:30,14:30,16:30\"},{\"stopName\":\"33rd St\",\"neighborhood\":\"Charles Village\",\"schedule\":\"11:00,13:00,15:00,17:00\"},{\"stopName\":\"Eager St\",\"neighborhood\":\"Mount Vernon\",\"schedule\":\"11:30,13:30,15:30,17:30\"},{\"stopName\":\"Light St\",\"neighborhood\":\"Inner Harbor\",\"schedule\":\"12:00,14:00,16:00,18:00\"}]},{\"neighborhoods\":\"Lauraville,Waverly,Charles Village,Inner Harbor, Charles Center\",\"operator\":\"MTA\",\"route\":\"CityLink Silver\",\"stops\":[{\"stopName\":\"Morgan State University\",\"neighborhood\":\"Lauraville\",\"schedule\":\"15:00,17:00,19:00,21:00;23:00\"},{\"stopName\":\"Greenmount\",\"neighborhood\":\"Waverly\",\"schedule\":\"15:15,17:15,19:15,21:15;23:15\"},{\"stopName\":\"33rd St\",\"neighborhood\":\"Charles Village\",\"schedule\":\"15:30,17:30,19:30,21:30;23:30\"},{\"stopName\":\"Light St\",\"neighborhood\":\"Inner Harbor\",\"schedule\":\"15:45,17:45,19:45,21:45;23:45\"},{\"stopName\":\"Fayette St\",\"neighborhood\":\"Charles Center\",\"schedule\":\"16:00,18:00,20:00,22:00;23:55\"}]}]");

            radioGroup = findViewById(R.id.start);

            String select1 = StartNeighborhood.selected1;
            //System.out.println(select1);
            String select2 = EndNeighborhood.selected2;
            //System.out.println(select2);
            String select3 = StopNumber.selected3;
            //System.out.println(select3);
            LocalTime time = LocalTime.now();
            hours = time.getHour();
            minutes = time.getMinute();

            String[] output = getStop(jsonArray, select1, select2, select3, hours, minutes);
            //String[] output = {"CCC;Purple Bus,12:30", "MTA;52,13:23"};

            for (int i = 0; i < output.length; i++) {
                route = new RadioButton(this);
                route.setText(output[i].substring(output[i].indexOf(";") + 1, output[i].indexOf(","))
                        + "\n" + output[i].substring(output[i].indexOf(",") + 1)
                        + "\n" + output[i].substring(0, output[i].indexOf(";")));
                route.setId(i);
                route.setTextSize(20);
                route.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openEnd(radioGroup.getCheckedRadioButtonId());
                }
            });
                radioGroup.addView(route);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void openEnd(int id) {
        RadioButton rb = radioGroup.findViewById(id);
        String theString = ((String) rb.getText());
        selected4 = theString.substring(0, theString.indexOf("\n"));
        selected5 = theString.substring(theString.indexOf("\n") + 1, theString.indexOf("\n", theString.indexOf("\n") + 1));
        String time = theString.substring(theString.indexOf("\n", theString.indexOf("\n") + 1) + 1);
        if (time.charAt(0) == '0') {
            hours = Integer.parseInt(time.substring(1, time.indexOf(":")));
        } else {
            hours = Integer.parseInt(time.substring(0, time.indexOf(":")));
        }
        if (time.charAt(time.indexOf(":") + 1) == '0') {
            minutes = Integer.parseInt(time.substring(time.indexOf(":") + 2));
        } else {
            minutes = Integer.parseInt(time.substring(time.indexOf(":") + 1));
        }
        Intent intent = new Intent( this, TimeTable.class);
        startActivity(intent);

    }

    public String[] getStop(JSONArray jsonArray, String start, String end, String stopName, int hour, int minute) {

        try {
            String[] temp = new String[50];
            int size = 0;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JO = (JSONObject) jsonArray.get(i);
                String neighborhoodsAll = (String) JO.get("neighborhoods");
                if (neighborhoodsAll.contains(start) && neighborhoodsAll.contains(end)
                        && (neighborhoodsAll.indexOf(start) < neighborhoodsAll.indexOf(end))
                        ) {

                    JSONArray stops = JO.getJSONArray("stops");
                    for (int j = 0; j < stops.length(); j++) {
                        JSONObject stop = (JSONObject) stops.get(j);

                        if (stop.getString("neighborhood").equals(start) && stop.getString("stopName").equals(stopName)) {
                            String tail = ";" + JO.getString("operator") + ","
                                    + JO.getString("route");
                            System.out.println(tail);
                            String[] times = stop.getString("schedule").split(",");

                            int count = 0;

                            for (int k = 0; k < times.length; k++) {
                                String time = times[k];
                                int busHour = Integer.parseInt(time.substring(0, 2));
                                int busMinute = Integer.parseInt(time.substring(3, 5));

                                if ((busHour == hour && busMinute >= minute)
                                        || busHour > hour) {
                                    System.out.println(time+tail);
                                    temp[size] = time + tail;
                                    size++;
                                    count++;
                                }
                                if (count == 2) {
                                    break;
                                }
                            }


                        }
                    }
                }
            }
            String[] busRoutes = new String[size];
            for (int j = 0; j < size; j++) {
                busRoutes[j] = temp[j];
                //System.out.println("haha "+temp[j]);
            }
            Arrays.sort(busRoutes);
            return busRoutes;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
