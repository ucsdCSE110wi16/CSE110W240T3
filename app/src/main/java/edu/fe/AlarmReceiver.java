package edu.fe;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;


import com.parse.FindCallback;
import com.parse.ParseCloud;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
//import java.lang.Object;


/**
 * Created by Dong on 3/11/2016.
 */
    public class AlarmReceiver extends BroadcastReceiver {

        private static final String DEBUG_TAG = "AlarmReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("FoodItem");
            query.findInBackground(new FindCallback<ParseObject>() {
                public void done(List<ParseObject> objects, ParseException e) {
                    if (e == null) {
                        for (ParseObject foods : objects) {
                            Date expirationDate = (Date) foods.getDate("expirationDate");
                            Date currentDate = new Date();
                            long diff = expirationDate.getTime() - currentDate.getTime();
                            if((int)(diff/(24*60*60*1000)) < 3 )
                            {
                                Calendar cal = Calendar.getInstance();
                                cal.setTime(expirationDate);
                                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
                                String expirationD = format1.format(cal.getTime());
                                expirationD = expirationD.substring(0,9) + "0" + expirationD.substring(9,expirationD.length()) + "T00:00:00";

                                HashMap < String, Object > params = new HashMap<String, Object>();
                                params.put("name", foods.getString("name"));
                                params.put("date", expirationD);
                                params.put("user", ParseUser.getCurrentUser());
                                ParseCloud.callFunctionInBackground("scheduleFoodExpiration", params);
                            }
                        }
                    } else {

                    }
                }
            });

        }

    }