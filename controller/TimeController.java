package com.example.tpo5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.DateTimeException;
import java.time.Year;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class TimeController {

    private static final String DEFAULT_FORMAT = "HH:mm:ss.SSSS yyyy/MM/dd";;

    @GetMapping(value = "/current-time")
    @ResponseBody
    public String getCurrentTime(
            @RequestParam(required = false, name = "timezone") String timezone,
            @RequestParam(required = false, name = "format") String format) {

        ZoneId zoneId = ZoneId.systemDefault();
        String error = " ";
        String warning = " ";

        if (timezone != null) {
            try {
                zoneId = ZoneId.of(timezone.trim());
            } catch (DateTimeException e) {
                error = "Invalid time zone provided. Defaulting to system time zone.";
                zoneId = ZoneId.systemDefault();
            }
        }

        DateTimeFormatter formatter;
        try {
            if (format != null && !format.isBlank()) {
                formatter = DateTimeFormatter.ofPattern(format.trim());
                ZonedDateTime.now(zoneId).format(formatter);
            } else {
                formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
            }
        } catch (IllegalArgumentException e) {
            warning = "Invalid format. Defaulting to default format.";
            formatter = DateTimeFormatter.ofPattern(DEFAULT_FORMAT);
        }

        ZonedDateTime now = ZonedDateTime.now(zoneId);
        String formattedTime;
        try {
            formattedTime = now.format(formatter);
        } catch (Exception e) {
            formattedTime = now.format(DateTimeFormatter.ofPattern(DEFAULT_FORMAT));
            warning = "Formatting error. Using default format.";
        }

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("   <meta charset=\"UTF-8\">\n");
        html.append("    <title>Current Time</title>\n");
        html.append("    <link rel=\"stylesheet\" href=\"/stylesheets/styles.css\">\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <h1>Current Time</h1>\n");
        html.append("    <p class=\"time\">").append(formattedTime).append("</p>\n");
        html.append("    <p class=\"timezone\">Timezone: ").append(zoneId).append("</p>\n");
        if (error != null) {
            html.append("    <p class=\"error\">").append(error).append("</p>\n");
        }
        if (warning != null) {
            html.append("    <p class=\"warning\">").append(warning).append("</p>\n");
        }
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }

    @GetMapping(value = "/current-year")
    @ResponseBody
    public String getCurrentYear(@RequestParam(required = false, name = "date") String dateStr) {
        String error = null;
        int year;

        if (dateStr != null && !dateStr.isBlank()) {
            try {
                ZonedDateTime dateTime = ZonedDateTime.parse(dateStr);
                year = dateTime.getYear();
            } catch (Exception e) {
                year = Year.now().getValue();
                error = "Invalid date format: '" + dateStr + "'. Showing current year instead.";
            }
        } else {
            year = Year.now().getValue();
        }

        StringBuilder html = new StringBuilder();
        html.append("<!DOCTYPE html>\n");
        html.append("<html lang=\"en\">\n");
        html.append("<head>\n");
        html.append("    <meta charset=\"UTF-8\">\n");
        html.append("    <title>Current Year</title>\n");
        html.append("    <link rel=\"stylesheet\" href=\"/stylesheets/styles.css\">\n");
        html.append("</head>\n");
        html.append("<body>\n");
        html.append("    <h1>Current Year</h1>\n");
        html.append("    <p class=\"year\">").append(year).append("</p>\n");
        if (error != null) {
            html.append("    <p class=\"error\">").append(error).append("</p>\n");
        }
        html.append("</body>\n");
        html.append("</html>");

        return html.toString();
    }
}