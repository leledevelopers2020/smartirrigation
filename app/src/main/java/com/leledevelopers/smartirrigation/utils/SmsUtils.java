package com.leledevelopers.smartirrigation.utils;
/**
 *
 * @author Shivam Z
 * This is class containes all constants and methods for Sending and Recevive SMS.
 */
public class SmsUtils {
    // SMS responses from controller
    public static final String INSMS_1_1 = "Admin set successfully";
    public static final String INSMS_1_2 = "Wrong password entered";
    public static final String INSMS_1_3 = "You are no more Admin now.New Admin is set to <GSM no.>";
    public static final String INSMS_2_1 = "System Connected";
    public static final String INSMS_2_2 = "Authentication failed, please reset connection";
    public static final String INSMS_3_1 = "Password changed successfully";
    public static final String INSMS_4_1 = "Irrigation configured for field no.";
    public static final String INSMS_5_1 = "Irrigation configuration disabled for field no.";
    public static final String INSMS_6_1 = "Fertigation enabled for field no.";
    public static final String INSMS_6_2 = "Incorrect values.Fertigation not enabled for field no.";
    public static final String INSMS_7_1 = "Fertigation disabled for field no.";
    public static final String INSMS_8_1 = "Water filtration activated";
    public static final String INSMS_9_1 = "Water filtration deactivated";
    public static final String INSMS_10_1 = "RTC is set to current Timestamp";
    public static final String INSMS_10_2 = "Incorrect Timestamp found";
    public static final String INSMS_11_1 = "System Time:";
    public static final String INSMS_12_1 = "Motorload thresholds set sucessfully.";
    public static final String RTC_BATTERY_LOW_STATUS = "Please replace RTC battery";
    public static final String RTC_BATTERY_FULL_STATUS = "RTC Battery Replaced, Please Set Time";
    public static final String SYSTEM_DOWN = "System not responding, please connect to system again";


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
    public String OutSMS_4(String fieldNo , int valveOnPeriod , int valveOffPeriod ,int motorOnTimeHr ,
                           int motorOnTimeMins ,int soilDryness,int soilWetness,int priority,
                           int cycle,int triggerFrom)
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
    public String OutSMS_6(String fieldNo, int wetPeriod,int injectPeriod, int noIterations)
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
