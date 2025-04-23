package com.example.tpo5.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigInteger;

@Controller
public class BaseConversionController {
    private static final String digits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*()_-+={[}]|:;<,>.?/~`'\"\\";

    @PostMapping("/base")
    @ResponseBody
    public String calc(@RequestParam String value,
                       @RequestParam int fromBase,
                       @RequestParam int toBase) {
        if (fromBase < 2 || fromBase > 100 || toBase < 2 || toBase > 100) {
            return "<div style='color: red; padding: 10px;'>Invalid base. Bases must be between 2 and 100.</div>" + "<link rel=\"stylesheet\" href=\"/stylesheets/styles2.css\">";
        }

        try {
            BigInteger dec = customBase(value, fromBase);

            if (dec.equals(BigInteger.ZERO)) {
                return format("0", dec, toBase);
            }

            BigInteger baseBig = BigInteger.valueOf(toBase);
            BigInteger num = dec;
            StringBuilder sb = new StringBuilder();
            while (num.compareTo(BigInteger.ZERO) > 0) {
                int remainder = num.mod(baseBig).intValue();
                sb.append(digits.charAt(remainder));
                num = num.divide(baseBig);
            }
            String converted = sb.reverse().toString();

            return format(converted, dec, toBase);
        } catch (Exception e) {
            return "<div style='color: red; padding: 10px;'>Error: " + e.getMessage() + "</div>";
        }
    }

    private BigInteger customBase(String number, int base) {
        if (base < 2 || base > digits.length()) {
            throw new IllegalArgumentException("Invalid base: " + base);
        }

        BigInteger result = BigInteger.ZERO;
        for (char c : number.toCharArray()) {
            int index = digits.indexOf(c);
            if (index == -1 || index >= base) {
                throw new NumberFormatException("Invalid character '" + c + "' for base " + base);
            }
            result = result.multiply(BigInteger.valueOf(base)).add(BigInteger.valueOf(index));
        }
        return result;
    }

    private String format(String converted, BigInteger dec, int toBase) {
        String binary = dec.toString(2);
        String octal = dec.toString(8);
        String decimal = dec.toString(10);
        String hexadecimal = dec.toString(16).toUpperCase();

        return "<div style='padding: 20px; margin: 10px; border-radius: 5px;'>" +
                "<h2 style='color: white;'>Conversion Result</h2>" +
                "<link rel=\"stylesheet\" href=\"/stylesheets/styles2.css\">"+
                "<p><b>Converted (Base " + toBase + "):</b> " + converted + "</p>" +
                "<h3 style='color: white;'>Other Bases:</h3>" +
                "<ul>" +
                "<li>BIN: " + binary + "</li>" +
                "<li>OCT: " + octal + "</li>" +
                "<li>DEC: " + decimal + "</li>" +
                "<li>HEX: " + hexadecimal + "</li>" +
                "</ul></div>";
    }
}