package com.example.astrahakaton;

import java.io.*;

public class Util {
   static long lineNumber(String path){
        String fileName = path;
        long noOfLines = -1;

        try(LineNumberReader lineNumberReader =
                    new LineNumberReader(new FileReader(new File(fileName)))) {
            //Skip to last line
            lineNumberReader.skip(Long.MAX_VALUE);
            noOfLines = lineNumberReader.getLineNumber() ;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return noOfLines;
    }
}
