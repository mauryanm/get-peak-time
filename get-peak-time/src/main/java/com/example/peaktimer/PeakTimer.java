package com.example.peaktimer;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.LocalDate;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PeakTimer {
    public static void getPeakTime(String[] args) {

    }

    public static Map<String, Integer> getPeakTimeDetail(String startDateS, String endDateS, String pts, String pte) {
        LocalDateTime startDateTime = LocalDateTime.parse(startDateS, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime endDateTime = LocalDateTime.parse(endDateS, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        long startDate = startDateTime.toEpochSecond(ZoneOffset.UTC);
        long endDate = endDateTime.toEpochSecond(ZoneOffset.UTC);
        LocalTime starts = LocalTime.parse(pts);
        LocalTime ends = LocalTime.parse(pte);
        List<List<String>> pt = new ArrayList<>();

        pt.add(List.of(pts, pte));
        if (starts.compareTo(ends) > 0) {
            pt.clear();
            pt.add(java.util.List.of(pts, "23:59:59"));
            pt.add(java.util.List.of("00:00:00", pte));
        }

        int totalMinutes = 0;
        for (List<String> peakTime : pt) {
            LocalTime start = LocalTime.parse(peakTime.get(0));
            LocalTime end = LocalTime.parse(peakTime.get(1));
            LocalTime time1 = LocalDateTime.ofEpochSecond(startDate, 0, ZoneOffset.UTC).toLocalTime();
            LocalTime time2 = LocalDateTime.ofEpochSecond(endDate, 0, ZoneOffset.UTC).toLocalTime();
            LocalDate date1 = LocalDateTime.ofEpochSecond(startDate, 0, ZoneOffset.UTC).toLocalDate();
            LocalDate date2 = LocalDateTime.ofEpochSecond(endDate, 0, ZoneOffset.UTC).toLocalDate();
            int maxPerDay = Duration.between(start, end).getSeconds() != 0 ? (int) Duration.between(start, end).getSeconds() : 24 * 60*60;
            int days = (int) Duration.between(date1.atStartOfDay(), date2.atStartOfDay()).toDays();
            int minutes = 0;
            if ((days == 0) && (start.compareTo(time2) <= 0 && time1.compareTo(end) <= 0)) {
                minutes = maxPerDay;
                if (start.compareTo(time1) < 0 || end.compareTo(time2) > 0) {
                    if (time1.compareTo(start) < 0) {
                        minutes = maxPerDay - Math.abs((int) Duration.between(time2, end).getSeconds());
                    } else if (time2.compareTo(end) > 0) {
                        minutes = maxPerDay - Math.abs((int) Duration.between(time1, start).getSeconds());
                    }else{
                        minutes = Math.abs((int) Duration.between(time1, time2).getSeconds());
                    }
                }
                minutes = minutes>0?minutes:((end==LocalTime.parse("23:59:59"))?(minutes+1):minutes);
            }
            else if (days > 0) {
                int firstDay = 0;
                int lastDay = 0;
                if (time1.compareTo(end) < 0) {
                    firstDay = (time1.compareTo(start) < 0) ? maxPerDay : (maxPerDay - Math.abs((int) Duration.between(time1, start).getSeconds()));
                }
                if (time2.compareTo(start) > 0) {
                    lastDay = (time2.compareTo(end) > 0) ? maxPerDay : (maxPerDay - Math.abs((int) Duration.between(time2, end).getSeconds()));
                }
                minutes = lastDay + firstDay + ((days - 1) * maxPerDay);
            }
            totalMinutes += minutes;
        }
        int peakTotal = (int) Math.ceil(totalMinutes/60);
        int totalTripTime = (int) Math.ceil(Duration.between(startDateTime, endDateTime).getSeconds()/60);
        Map<String, Integer> result = new HashMap<String, Integer>();
        result.put("peak_time", peakTotal);
        result.put("total_time", totalTripTime);
        return result;
    }
}