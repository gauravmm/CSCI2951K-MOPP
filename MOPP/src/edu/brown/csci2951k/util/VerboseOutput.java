/*
 * Project 01 - Autocorrect
 * CSCI 0320, Brown University, Fall 2014
 * Gaurav Manek
 */

package edu.brown.cs.cs032.util;

/**
 *
 * @author Gaurav Manek
 */
public class VerboseOutput {
    private static boolean verbose = false;
    private static boolean log = false;
    private static String logs = "";

    public static void setVerbose(boolean verbose) {
        VerboseOutput.verbose = verbose;
    }

    public static void setLog(boolean log) {
        VerboseOutput.log = log;
    }
    
    public static String getLogs() {
        return logs;
    }
    
    public static final void output(String s){
        if(verbose){
            System.err.println(s);
        }
        if(log){
            logs += log + "\n";
        }
    }
}
