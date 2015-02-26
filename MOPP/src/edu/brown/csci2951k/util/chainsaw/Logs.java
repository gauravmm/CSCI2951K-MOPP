/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.brown.cs.cs032.util.chainsaw;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 *
 * @author Gaurav Manek
 */
public class Logs implements Logger {

    private final BlockingQueue<LogEntry> queue = new LinkedBlockingDeque<>(1000);
    
    @Override
    public void log(String name, String event, int level) {
        queue.offer(new LogEntry(name, event, level, Thread.currentThread().toString()));
    }

    @Override
    public void logInfo(Class c, String event) {
        this.log(c.getSimpleName(), event, LOG_INFO);
    }

    @Override
    public void logWarn(Class c, String event) {
        this.log(c.getSimpleName(), event, LOG_WARNING);
    }

    @Override
    public void logError(Class c, String event) {
        this.log(c.getSimpleName(), event, LOG_ERROR);
    }

    public List<LogEntry> getLogsBlocking() throws InterruptedException {
        List<LogEntry> entries = new LinkedList<>();
        entries.add(queue.take());
        queue.drainTo(entries);
        return entries;
    }
    
}
