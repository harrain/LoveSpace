package com.example.lovespace.common.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by stephen on 2017/6/7.
 */

public class DateUtil {

    int dayOfMonth[] = {31,28,31,30,31,30,31,31,30,31,30,31};//将每个月的日期写在数组里
    int i=0;
    public int year,month,day;		//年、月、日
    int dayCount =0;	//天数

    private  boolean IsWrong(int year, int month, int day) { 	//判断年月日是否有错
        boolean a=false,b=false,c=false;
        if(year<1)a=true;
        if(month>12 || month<1)b=true;
        if(IsDayWrong(day,year,month))c=true;
        return (a || b || c);
    }

    private  boolean IsDayWrong(int day, int year, int month) { //判断日期是否有错
        boolean i=false;
        int temporaryDay;//临时日期
        int dayOfMonth[] = {31,28,31,30,31,30,31,31,30,31,30,31};
        if(IsLeapYear(year))dayOfMonth[1]=29;
        if(month>11 || month<1)i=true;
        else{
            temporaryDay = dayOfMonth[month-1];
            if(day<1 || day>temporaryDay) i = true;
        }
        return i;
    }

    private  boolean IsLeapYear(int year) { 	//判断闰年
        boolean i=false;
        if(year%4==0 && year%100!=0 || year%400==0)i=true;
        return i;
    }

    public String date2string(Date date){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(date);
        /*System.out.println(sdf.format(date));
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println(sdf.format(date));//date转成字符串
        System.out.println("-----------------------------------------------------------");*/
    }

    public void string2date(){
        String datestr = "2017-05-20 09:08:07";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = sdf.parse(datestr);//字符串转成date
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println(date);
        System.out.println("-----------------------------------------------------------");
        long msec1 = date.getTime();
        System.out.println("mesc:"+msec1);
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(msec1);
        long times = gc.getTimeInMillis();//转成long类型
        System.out.println(times);

        String dateStr = sdf.format(gc.getTime());//转成Date字符串形式

    }

    public long date2long(Date date){

        return date.getTime();
    }

    public long date2long(String datestr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(datestr);//字符串转成date
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date.getTime();
    }

    public int countDays(){
        if(IsLeapYear(year))dayOfMonth[1]=29;	//判断闰年后2月份变为29天
        for(i=0;i<=month-2;i++){	//计算这个月之前的总天数
            dayCount = dayCount + dayOfMonth[i];
        }
        dayCount = dayCount + day;
        return dayCount;
    }

    public List<Integer> string2int(String d){
        List<Integer> i = new ArrayList<>();
        String[] strs = d.split("-");
        for(String str:strs){

            i.add(Integer.parseInt(str));
        }
        return i;
    }
}
