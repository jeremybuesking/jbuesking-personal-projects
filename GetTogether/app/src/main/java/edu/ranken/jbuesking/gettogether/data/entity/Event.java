package edu.ranken.jbuesking.gettogether.data.entity;

import com.google.firebase.database.Exclude;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class Event {
    private String title;
    private String description;
    private String location;
    private String dateTime;
    private List<String> attending;
    private String hostingGroup;

    public Event() {
        // Empty constructor for Firebase.
    }

    public Event(String title, String description, String location, String dateTime, List<String> attending, String hostingGroup) {
        this.title = title;
        this.description = description;
        this.location = location;
        this.dateTime = dateTime;
        this.attending = attending;
        this.hostingGroup = hostingGroup;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) {
        this.location = location;
    }

    public String getDate() { return dateTime; }
    public void setDate(String date) {
        this.dateTime = date;
    }

    public List<String> getAttending() { return attending; }
    public void setAttending(List<String> attending) {
        this.attending = attending;
    }

    public String getHostingGroup() { return hostingGroup; }
    public void setHostingGroup(String hostingGroup) {
        this.hostingGroup = hostingGroup;
    }

    @Exclude
    public static Calendar parseDate(String str) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("M/dd/yyyy", Locale.US);
        TimeZone tz = TimeZone.getDefault();
        format.setTimeZone(tz);

        Date parsedDate = format.parse(str);
        Calendar parsedCal = Calendar.getInstance(tz);
        parsedCal.setTime(parsedDate);
        return parsedCal;
    }

    @Exclude
    public static String formatDate(Calendar cal) {
        SimpleDateFormat format = new SimpleDateFormat("M/dd/yyyy", Locale.US);
        format.setTimeZone(cal.getTimeZone());
        return format.format(cal.getTime());
    }

    @Exclude
    public static Calendar parseTime(String str) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
        TimeZone tz = TimeZone.getDefault();
        format.setTimeZone(tz);

        Date parsedTime = format.parse(str);
        Calendar parsedCal = Calendar.getInstance(tz);
        parsedCal.setTime(parsedTime);
        return parsedCal;
    }

     @Exclude
    public static String formatTime(Calendar cal) {
         SimpleDateFormat format = new SimpleDateFormat("hh:mm a", Locale.US);
         format.setTimeZone(cal.getTimeZone());
         return format.format(cal.getTime());
     }
}
