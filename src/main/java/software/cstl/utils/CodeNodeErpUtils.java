package software.cstl.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import software.cstl.domain.enumeration.MonthType;
import software.cstl.domain.enumeration.WeekDay;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Locale;

@Slf4j
public class CodeNodeErpUtils {
    private static final char[] banglaDigits = {'০','১','২','৩','৪','৫','৬','৭','৮','৯'};
    private static final char[] englishDigits = {'0','1','2','3','4','5','6','7','8','9'};

    public  static final String  getDigitBanglaFromEnglish(String number){
        if(number==null)
            return new String("");
        StringBuilder builder = new StringBuilder();
        try{
            for(int i =0;i<number.length();i++){
                if(Character.isDigit(number.charAt(i))){
                    if(((int)(number.charAt(i))-48)<=9){
                        builder.append(banglaDigits[(int)(number.charAt(i))-48]);
                    }else{
                        builder.append(number.charAt(i));
                    }
                }else{
                    builder.append(number.charAt(i));
                }
            }
        }catch(Exception e){
            log.debug("getDigitBanglaFromEnglish: ",e);
            return new String("");
        }
        return builder.toString();
    }

    public static String currencyWithChosenLocalisation(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(value).replace("$","");
    }

    public static String currencyWithChosenLocalisationInBangla(BigDecimal value) {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        String currencyValue =  nf.format(value).replace("$","");
        return getDigitBanglaFromEnglish(currencyValue);
    }

    public static int getTotalDayOccurrenceOfWeekDay(int year, MonthType monthType, WeekDay weekDay){

        int weekDayOrdinalValue = getWeekDayOrdinalValue(weekDay); // we need this ordinal value as we are not using java standard week day enum

        YearMonth yearMonth = YearMonth.of(year, (monthType.ordinal()+1));
        LocalDate firstDay = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), 1);
        LocalDate lastDay = LocalDate.of(yearMonth.getYear(), yearMonth.getMonth(), yearMonth.lengthOfMonth());
        int totalDays = 0;
        while (!firstDay.isAfter(lastDay)){
            if(firstDay.getDayOfWeek().equals(DayOfWeek.of(weekDayOrdinalValue))){
                totalDays+=1;
            }
            firstDay = firstDay.plusDays(1);
        }

        return totalDays;
    }

    public static int getWeekDayOrdinalValue(WeekDay weekDay) {
        int weekDayOrdinalValue = 0;
        if (weekDay.equals(WeekDay.MONDAY)) {
            weekDayOrdinalValue=1;
        }else if(weekDay.equals(WeekDay.SUNDAY)){
            weekDayOrdinalValue=7;
        }else{
            weekDayOrdinalValue = weekDay.ordinal();
        }
        return weekDayOrdinalValue;
    }

    public static String convertToANSI(String text) throws IOException {
        UnicodeToBijoyConveter unicodeToBijoyConveter = new UnicodeToBijoyConveter();
       return unicodeToBijoyConveter.Convert(text);
    }

    public static String convertMonthToBangla(String month){
        month = month.toUpperCase();
        if(month.contains("JANUARY"))
            month.replaceAll("JANUARY","জানুয়ারি");
        else if(month.contains("FEBRUARY"))
            month.replaceAll("FEBRUARY", "ফেব্রুয়ারী");
        else if(month.contains("MARCH"))
            month.replaceAll("MARCH","মার্চ");
        else if(month.contains("APRIL"))
            month.replaceAll("APRIL","এপ্রিল");
        else if(month.contains("MAY"))
            month.replaceAll("MAY","মে");
        else if(month.contains("JUNE"))
            month.replaceAll("JUNE","জুন");
        else if(month.contains("JULY"))
            month.replaceAll("JULY","জুলাই");
        else if(month.contains("AUGUST"))
            month.replaceAll("AUGUST","অগাস্ট");
        else if(month.contains("SEPTEMBER"))
            month.replaceAll("SEPTEMBER","সেপ্টেম্বর");
        else if(month.contains("OCTOBER"))
            month.replaceAll("OCTOBER","অক্টোবর");
        else if(month.contains("NOVEMBER"))
            month.replaceAll("NOVEMBER", "নভেম্বর");
        else if(month.contains("DECEMBER"))
            month.replaceAll("DECEMBER", "ডিসেম্বর");
        return month;
    }
}
