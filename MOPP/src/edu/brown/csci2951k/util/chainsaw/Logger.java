/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.cs.cs032.util.chainsaw;

/**
 *
 * @author Gaurav Manek
 */
public interface Logger {

    public final int LOG_INFO = 1;
    public final int LOG_WARNING = 2;
    public final int LOG_ERROR = 3;

    public void log(String name, String event, int level);

    public void logInfo(Class c, String event);

    public void logWarn(Class c, String event);

    public void logError(Class c, String event);

}
