package com.leledevelopers.smartirrigation.utils;

public class SmsUtils {
    // SMS responses from controller
    public static final String INSMS_1_1="Admin set successfully";
    public static final String INSMS_1_2="Wrong factory Password";
    public static final String INSMS_1_3="";
    public static final String INSMS_2_1="Hooked";
    public static final String INSMS_2_2="Not Authenticated";
    public static final String INSMS_3_1="Password updated successfully";
    public static final String INSMS_4_1="Valve Set for field no.";
    public static final String INSMS_5_1="Valve kept on Hold";
    public static final String INSMS_6_1="Fertigation enabled for field no. ";
    public static final String INSMS_6_2="";
    public static final String INSMS_7_1="Fertigation disabled";
    public static final String INSMS_8_1="Pump Filtration Activated";
    public static final String INSMS_9_1="Pump Filtration Deactivated";
    public static final String INSMS_10_1="RTC Set to current Timestamp";
    public static final String INSMS_10_2="Incorrect Time stamp found";
    public static final String INSMS_11_1="";
    public static final String INSMS_12_1="Motor Load Threshold Set Sucessfully.";


    //Sms to controller from mobile devices

    //User Registration
    public String   OutSMS_1(String factoryPassword , String userNewPassword)
    {
        return "AU"+" "+factoryPassword+" "+userNewPassword;
    }

    //User connection
    public static final String OutSMS_2="HOOK";

    //password change
    public String OutSMS_3(String oldPassword ,String userNewPassword)
    {
        return "AU"+" "+oldPassword+" "+userNewPassword;
    }

    //Configure field irrigation valve
    public String OutSMS_4(String fieldNo , String valveOnPeriod , String valveOffPeriod ,String motorOnTimeHr ,
                           String motorOnTimeMins ,String soilDryness,String soilWetness,String priority,
                           String cycle,String triggerFrom)
    {
        return "SET"+fieldNo+" "+valveOnPeriod+" "+valveOffPeriod+" "+motorOnTimeHr+" "+motorOnTimeMins+" "+soilDryness
                +" "+soilWetness+" "+priority+" "+cycle+" "+triggerFrom+" ";
    }

    //Disable Field Irrigation valve
    public String OutSMS_5(String fieldNo)
    {
        return "HOLD"+fieldNo;
    }

    //Configure field fertigation
    public String OutSMS_6(String fieldNo, String wetPeriod,String injectPeriod, String noIterations)
    {
        return "ENABLE"+fieldNo+" "+wetPeriod+" "+injectPeriod+" "+noIterations+" ";
    }

    //Disable field fertigation
    public String OutSMS_7(String fieldNo)
    {
        return "DISABLE"+fieldNo;
    }

    //Configure filtration
    public  String OutSMS_8(String FCU1,String FCU2,String FCU3,String FCUTime,String FCUSeparation) //FCU=filtration control unit
    {
        return "ACTIVE"+FCU1+" "+FCU2+" "+FCU3+" "+FCUTime+" "+FCUSeparation+" ";
    }
    //Disable filtration
    public static final String OutSMS_9="DACTIVE";
    //Set RTC Time
    public String OutSMS_10(String DD,String MM,String YY,String Hr,String Min,String Sec)
    {
        return "FEED"+" "+DD+" "+MM+" "+YY+" "+Hr+" "+Min+" "+Sec;
    }
    //Get RTC Time
    public static final String OutSMS_11="TIME";
    //Set Motor Load Cutoff
    public String OutSMS_12(String noLoadCutOff,String fullLoadCutOff)
    {
        return "CT"+" "+noLoadCutOff+" "+fullLoadCutOff+" ";
    }

}
