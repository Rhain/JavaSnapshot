package com.rhain.java7;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Rhain
 * @since 2014/8/25.
 */
public class Java7NewFeatures {

    /**
     * Switch statement In Java 6 and before,the values for the cases coulud only be constants of type byte,char,short,int or enum constants. With Java 7 .the spec has been extended to allow for the
     * String type to be used as well.They are constants after all.
     */

    public void printDay(String dayOfWeek) {
        switch (dayOfWeek) {
        case "Sunday":
            System.out.println("Sunday");
            break;
        case "Monday":
            System.out.println("Monday");
            break;
        default:
            System.out.println("Hello day");
            break;
        }
    }

    /**
     * Enhanced syntax for numeric literals Numeric constants may now be expressed as binary literals Underscores may be used in integer constants to improve readability
     */

    // In Java 6 and before
    int x1 = Integer.parseInt("1100110", 2);
    // In Java 7
    int x2 = 0b1100110;

    // underscores in numbers
    static Long anotherLong = 2_147_3986_987L;
    static int bitPattern = 0b0001_1100_0011_1011;

    /**
     * Improved exception handling
     * Two improvement parts -- multicatch and final rethrow
     */

    //Handling several different exceptions in java 6
    public void getConfigInJava6(String fileName){
        try {
            File file = new File(fileName);
            OutputStream out = new FileOutputStream(file);
            parseConfig(out);
        }catch (FileNotFoundException e){

        }catch (IOException e){

        } catch (ParseException e) {

        }
    }

    public void parseConfig(OutputStream out) throws ParseException {
        throw new ParseException("ParseException",2);
    }

    //Handling several different exceptions in java 7
    public void getConfigInJava7(String fileName){
        try {
            File file = new File(fileName);
            OutputStream out = new FileOutputStream(file);
            parseConfig(out);
        }catch (FileNotFoundException | ParseException e){

        }catch (final IOException e){
            throw e;
        }
    }

    /**
     * Try-with-resources(TWR)
     *
     */

    //Java6 syntax for resources management
    public void twrInJava6(URL url){
        InputStream is = null;
        try{
            File file = new File("");
            is = url.openStream();
            OutputStream out = new FileOutputStream(file);
            try{
                byte[] buf = new byte[4096];
                int len;
                while((len = is.read()) >= 0){
                    out.write(buf,0,len);
                }
            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                out.close();
            }
        }catch (FileNotFoundException e){

        }
        catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (is == null) {
                try {
                    is.close();
                } catch (IOException e) {

                }
            }
        }
    }

    //Java7 syntax for resources management
    public void twrInJava7(URL url,File file){
        try(OutputStream out = new FileOutputStream(file);
            InputStream is = url.openStream()){
            byte[] buf = new byte[4096];
            int len;
            while((len=is.read()) >= 0){
                out.write(buf,0,len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Diamond syntax
     */

    //In java6
    Map<Integer,Map<String,String>> userLists = new HashMap<Integer,Map<String,String>>();

    //In java7
    Map<Integer,Map<String,String>> userList = new HashMap<>();



    /**
     *
     * @param args
     */

    public static void main(String[] args) {
        System.out.println(anotherLong);
        System.out.println(bitPattern);
    }

}
