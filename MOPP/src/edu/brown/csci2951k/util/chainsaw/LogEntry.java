/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.brown.cs.cs032.util.chainsaw;

import java.util.Date;

/**
 *
 * @author Gaurav Manek
 */
public class LogEntry {
    public final String name;
    public final String message;
    public final int level;
    public final String threadId;
    public final Date time;

    public LogEntry(String name, String message, int level, String threadId) {
        this.time = new Date();
        this.name = name;
        this.message = message;
        this.level = level;
        this.threadId = threadId;
    }
}
