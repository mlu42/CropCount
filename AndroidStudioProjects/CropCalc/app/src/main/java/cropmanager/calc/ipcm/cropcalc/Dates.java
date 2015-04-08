package cropmanager.calc.ipcm.cropcalc;

import java.util.GregorianCalendar;

/*
A small library for working with dates
 */

public class Dates {

    /*
    Givens the specified date in the format "Monday, January 1 2014 12:01AM
     */
    public String formatDate(GregorianCalendar d){

        String dayOfWeek = ItoSDayOfWeek(d.get(GregorianCalendar.DAY_OF_WEEK));
        String dayOfMonth = ItoSDayOfMonth(d.get(GregorianCalendar.DAY_OF_MONTH));
        String month = ItoSMonth(d.get(GregorianCalendar.MONTH));
        String year = ItoSYear(d.get(GregorianCalendar.YEAR));

        int[] t = new int[3];
        t[0] = d.get(GregorianCalendar.HOUR);
        t[1] = d.get(GregorianCalendar.MINUTE);
        t[2] = d.get(GregorianCalendar.SECOND);

        String time = ItoSTime(t);
        String ampm = ItoSAMPM(d.get(GregorianCalendar.AM_PM));

        return dayOfWeek + ", " + dayOfMonth + " " + month + " " + year + " " + time + ampm;

    }

    // Returns -1 because it shouldn't ever need to be used. Was implement at some point, maybe.
    public int StoIDayOfWeek(String s){
        return -1;
    }

    /*
    Takes and int in the set {1,2,...,7} and returns the corresponding day
    of the week in the set {Sunday, Monday, ..., Saturday}
     */
    public String ItoSDayOfWeek(int i){

        switch(i){
            case 1: return "Sunday";
            case 2: return "Monday";
            case 3: return "Tuesday";
            case 4: return "Wednesday";
            case 5: return "Thursday";
            case 6: return "Friday";
            case 7: return "Saturday";
            //////////////////////
            default: return "Sunday";
        }

    }

    // Simply parses a String to it's corresponding integer
    public int StoIDayOfMonth(String s){
        int d = -1;

        try{
            d = Integer.parseInt(s);
        }catch(Exception e){
            d = 1;
        }

        return d;
    }

    // Parses an integer to its corresponding string
    public String ItoSDayOfMonth(int i){

        return ((Integer)i).toString();

    }

    /*
    Translates {Jan, Feb, ..., Dec} --> {1,2,...,12}
     */
    public int StoIMonth(String s){
        int d = -1;

        if(s.equals("Jan"))
            d = 1;
        if(s.equals("Feb"))
            d = 2;
        if(s.equals("Mar"))
            d = 3;
        if(s.equals("Apr"))
            d = 4;
        if(s.equals("May"))
            d = 5;
        if(s.equals("Jun"))
            d = 6;
        if(s.equals("Jul"))
            d = 7;
        if(s.equals("Aug"))
            d = 8;
        if(s.equals("Sep"))
            d = 9;
        if(s.equals("Oct"))
            d = 10;
        if(s.equals("Nov"))
            d = 11;
        if(s.equals("Dec"))
            d = 12;

        if(d == -1)
            d = 1;

        return d;
    }

    /*
    Translates {1,2,...,12} --> {January, February, ..., December}
     */
    public String ItoSMonth(int i){

        switch(i){
            case 1: return "January";
            case 2: return "February";
            case 3: return "March";
            case 4: return "April";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "August";
            case 9: return "September";
            case 10: return "October";
            case 11: return "November";
            case 12: return "December";
            //////////////////////
            default: return "January";
        }

    }

    /*
    Parses a string to its int
     */
    public int StoIYear(String s){
        int d = -1;

        try{
            d = Integer.parseInt(s);
        }catch(Exception e){
            d = 2013;
        }

        return d;
    }

    /*
    Parses an int to its corresponding string
     */
    public String ItoSYear(int i){

        return ((Integer)i).toString();

    }

    /*
    Parses a string that represents a time to an array of ints
    that takes the form {hour,minute,second}
     */
    public int[] StoITime(String s){
        int[] time = new int[3];

        try{
            String[] times = s.split(":");
            time[0] = Integer.parseInt(times[0]);
            time[1] = Integer.parseInt(times[1]);
            time[2] = Integer.parseInt(times[2]);
        }catch(Exception e){
            time[0] = time[1] = time[2] = 0;
        }

        return time;
    }

    /*
    Converts an array of integers of the form {hour,minute,second}
    to the corresponding string e.g. "12:01:00"
     */
    public String ItoSTime(int[] i){

        try{
            String hour = ((Integer)i[0]).toString();
            String minute = ((Integer)i[1]).toString();
            String second = ((Integer)i[2]).toString();

            if(minute.length() == 1)
                minute = "0" + minute;
            if(second.length() == 1)
                second = "0" + second;

            return hour + ":" + minute; // + ":" + second;
        }catch(Exception e){
            return "12:00";
        }

    }

    /*
    Converts {AM,PM} --> {0,1}
     */
    public int StoIAMPM(String s){

        if(s.equals("AM"))
            return 0;

        return 1;

    }

    /*
    Converts {0,1} --> {AM,PM}.
     */
    public String ItoSAMPM(int i){

        if(i == 0)
            return "AM";

        return "PM";

    }

}
