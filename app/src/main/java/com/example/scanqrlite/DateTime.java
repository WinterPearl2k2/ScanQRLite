package com.example.scanqrlite;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateTime {
    public String getDateTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("h:mm a, MMM dd, yyyy");
        return simpleDateFormat.format(c.getTime());
    }
}
