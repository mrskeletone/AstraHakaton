package com.example.astrahakaton;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Util {
    private Util(){
    }
   static long lineNumber(String path){
       long noOfLines = -1;

        try(LineNumberReader lineNumberReader =
                    new LineNumberReader(new FileReader(new File(path)))) {
            //Skip to last line
            lineNumberReader.skip(Long.MAX_VALUE);
            noOfLines = lineNumberReader.getLineNumber() ;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return noOfLines;
    }
    static Map<String,Long> allTypesLogs(){
        Map<String, Long> data = new HashMap<>();
        long alert = Util.lineNumber("src/main/java/logFiles/alertsLogs/alerts");
        long critical = Util.lineNumber("src/main/java/logFiles/criticalLogs/critical");
        long debug = Util.lineNumber("src/main/java/logFiles/debugLogs/debug");
        long emergency = Util.lineNumber("src/main/java/logFiles/emergencyLogs/emergency");
        long errors = Util.lineNumber("src/main/java/logFiles/errorLogs/errors");
        long info = Util.lineNumber("src/main/java/logFiles/infoLogs/info");
        long sum = alert + critical + debug + emergency + emergency + info;
        data.put("alert " + String.format("%.2f", (double) alert / (double) sum * 100) + "%", alert);
        data.put("critical " + String.format("%.2f", (double) critical / (double) sum * 100) + "%", critical);
        data.put("debug " + String.format("%.2f", (double) debug / (double) sum * 100) + "%", debug);
        data.put("emergency " + String.format("%.2f", (double) emergency / (double) sum * 100) + "%", emergency);
        data.put("errors " + String.format("%.2f", (double) errors / (double) sum * 100) + "%", errors);
        data.put("info " + String.format("%.2f", (double) info / (double) sum * 100) + "%", info);
        return data;
    }
}
