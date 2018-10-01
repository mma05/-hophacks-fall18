package com.example.transit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class TimeTable extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_table);

        try {
            JSONArray jsonArray = new JSONArray("[{\"neighborhoods\":\"Charles Village,Mount Vernon,Inner Harbor,Federal Hill,Charles Center\",\"operator\":\"Charm City Circulator\",\"route\":\"purple\",\"stops\":[{\"stopName\":\"33rd St\",\"neighborhood\":\"Charles Village\",\"schedule\":\"08:00,09:00,10:00,11:00,12:00\"},{\"stopName\":\"Eager St\",\"neighborhood\":\"Mount Vernon\",\"schedule\":\"08:20,09:20,10:20,11:20,12:20\"},{\"stopName\":\"Light St\",\"neighborhood\":\"Inner harbor\",\"schedule\":\"08:40,09:40,10:40,11:40,12:40\"},{\"stopName\":\"Ostend St\",\"neighborhood\":\"Federal Hill\",\"schedule\":\"09:00,10:00,11:00,12:00,13:00\"},{\"stopName\":\"Fayette St\",\"neighborhood\":\"Charles Center\",\"schedule\":\"09:20,10:20,11:20,12:20,13:20\"}]},{\"neighborhoods\":\"Towson,Woodbrook,Mid Charles,Charles Village,Mount Vernon,Inner Harbor\",\"operator\":\"MTA\",\"route\":\"LocalLink 51\",\"stops\":[{\"stopName\":\"Towson Town Center\",\"neighborhood\":\"Towson\",\"schedule\":\"10:00,12:00,14:00,16:00\"},{\"stopName\":\"Greater Baltimore Medical Center\",\"neighborhood\":\"Mid Charles\",\"schedule\":\"10:30,12:30,14:30,16:30\"},{\"stopName\":\"33rd St\",\"neighborhood\":\"Charles Village\",\"schedule\":\"11:00,13:00,15:00,17:00\"},{\"stopName\":\"Eager St\",\"neighborhood\":\"Mount Vernon\",\"schedule\":\"11:30,13:30,15:30,17:30\"},{\"stopName\":\"Light St\",\"neighborhood\":\"Inner Harbor\",\"schedule\":\"12:00,14:00,16:00,18:00\"}]},{\"neighborhoods\":\"Lauraville,Waverly,Charles Village,Inner Harbor, Charles Center\",\"operator\":\"MTA\",\"route\":\"CityLink Silver\",\"stops\":[{\"stopName\":\"Morgan State University\",\"neighborhood\":\"Lauraville\",\"schedule\":\"15:00,17:00,19:00,21:00;23:00\"},{\"stopName\":\"Greenmount\",\"neighborhood\":\"Waverly\",\"schedule\":\"15:15,17:15,19:15,21:15;23:15\"},{\"stopName\":\"33rd St\",\"neighborhood\":\"Charles Village\",\"schedule\":\"15:30,17:30,19:30,21:30;23:30\"},{\"stopName\":\"Light St\",\"neighborhood\":\"Inner Harbor\",\"schedule\":\"15:45,17:45,19:45,21:45;23:45\"},{\"stopName\":\"Fayette St\",\"neighborhood\":\"Charles Center\",\"schedule\":\"16:00,18:00,20:00,22:00;23:55\"}]}]");

            String select1 = StartNeighborhood.selected1;
            String select2 = EndNeighborhood.selected2;
            String select3 = StopNumber.selected3;
            String select4 = BusRoutes.selected4;
            String select5 = BusRoutes.selected5;
            int hour = BusRoutes.hours;
            System.out.println(hour);
            int mins = BusRoutes.minutes;
            System.out.println(mins);

            System.out.println(select5 + " " + hour + " ");

            //System.out.println(time);

            Map<String, String> stops = new HashMap<String, String>();
            stops = getTimetable(jsonArray, select1, select2, select3, select4, select5, hour, mins);
            System.out.println(stops.toString());
            //stops.put("9:42am", "Stop: 31");
            //stops.put("10am", "32");
            //stops.put("10:05am", "33");

            LinearLayout lin = new LinearLayout(this);
            setContentView(lin);
            lin.setOrientation(LinearLayout.VERTICAL);

            for (Map.Entry<String, String> key : stops.entrySet()) {
                TextView text = new TextView(this);
                text.setText(key.getKey() + "                     " + key.getValue() + "\n");
                text.setTextSize(20);
                lin.addView(text);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getTimetable(JSONArray jsonArray, String start, String end,
                                       String stopName, String operator, String route,
                                       int hour, int minute) {

        try {
            Map<String, String> result = new LinkedHashMap<String, String>();
            boolean startStopFound = false;
            boolean lastStopFound = false;
            boolean lastStopEnd = false;
            int busIndex = -1;
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject JO = (JSONObject) jsonArray.get(i);
                String neighborhoodsAll = (String) JO.get("neighborhoods");

                if (neighborhoodsAll.contains(start) && neighborhoodsAll.contains(end)
                        && (neighborhoodsAll.indexOf(start) < neighborhoodsAll.indexOf(end))
                        && JO.getString("operator").equals(operator) && JO.getString("route").equals(route)
                        ) {

                    //System.out.println("Pass operator, route, start and end");

                    JSONArray stops = JO.getJSONArray("stops");

                    for (int j = 0; j < stops.length(); j++) {
                        JSONObject stop = (JSONObject) stops.get(j);

                        if (stop.get("neighborhood").equals(start) && stop.getString("stopName").equals(stopName)) {
                            String[] times = stop.getString("schedule").split(",");

                            System.out.println("Pass bus stop");

                            for (int k = 0; k < times.length; k++) {
                                String time = times[k];
                                int busHour = Integer.parseInt(time.substring(0, 2));
                                int busMinute = Integer.parseInt(time.substring(3, 5));
                                /**System.out.println("busHour: " + busHour + ".");
                                System.out.println("busHour: " + hour + ".");
                                System.out.println("busMinute: " + busMinute + ".");
                                System.out.println("busMinute: " + minute + ".");*/

                                if (busHour == hour && busMinute == minute) {
                                    startStopFound = true;
                                    busIndex = k;
                                    System.out.println("busIndex: " + k);
                                    break;
                                }

                            }
                        }

                        if (stop.get("neighborhood").equals(end)) {
                            lastStopFound = true;
                            System.out.println("I found end stop.");
                        }
                        if (lastStopFound && !stop.get("neighborhood").equals(end)) {
                            lastStopEnd = true;
                            System.out.println("Finish with end neighborhood.");
                        }

                        if (startStopFound && !lastStopEnd) {
                            String[] times = stop.getString("schedule").split(",");
                            result.put(times[busIndex],stop.getString("stopName"));
                            System.out.println(times[busIndex] + " " + stop.getString("stopName"));
                        }
                    }
                }
            }
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


}
