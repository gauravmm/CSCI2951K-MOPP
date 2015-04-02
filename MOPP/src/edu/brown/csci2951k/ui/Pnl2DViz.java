/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.ui;

import edu.brown.csci2951k.models.data.MObject;
import edu.brown.csci2951k.models.space.SpatialModel;
import edu.brown.csci2951k.models.space.Coords2D;
import edu.brown.csci2951k.util.Pair;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.stream.Collectors;
import javax.swing.JPanel;

/**
 *
 * @author Zach Loery
 */
public class Pnl2DViz extends JPanel{
    
    private SpatialModel<Coords2D> model;

    public Pnl2DViz(SpatialModel<Coords2D> model) {
        this.model = model;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        //Start by drawing the background
        int h = getHeight();
        int w = getWidth();
        g.setColor(Color.white);
        g.fillRect(0, 0, w, h);
        
        //Get the summary statistics off the model
        DoubleSummaryStatistics xstats = model.stream().map(Pair<MObject, Coords2D>::getValue).collect(Collectors.summarizingDouble(Coords2D::getX));
        DoubleSummaryStatistics ystats = model.stream().map(Pair<MObject, Coords2D>::getValue).collect(Collectors.summarizingDouble(Coords2D::getY));
        
        double xrange = xstats.getMax() - xstats.getMin();
        double yrange = ystats.getMax() - ystats.getMin();

        // round(10−n·x)·10n, where n = floor(log10 x) + 1 − p.

        double functionalMinX = xstats.getMin() - (xrange*.25);
        double functionalMinY = ystats.getMin() - (yrange*.25);
        xrange *= 1.5;
        yrange *= 1.5;
        //Draw in the grid structure
        int numBars = 10;
        g.setColor(new Color(200, 200, 200));
        for(int i = 1; i<numBars; i++){
            g.drawLine((int) (i*(getWidth()/numBars)), 0, (int) (i*(getWidth()/numBars)), getHeight());//Draw vertical line
            g.drawLine(0, (int) (i*(getHeight()/numBars)), getWidth(), (int) (i*(getHeight()/numBars)));//Draw vertical line//Draw horizontal line
        }
        
        
        //Now, draw each object
        int r = Math.min((this.getWidth() + this.getHeight())/80, 5);
        Iterator<Pair<MObject, Coords2D>> objects = model.iterator();
        g.setColor(Color.BLUE);
        g.setFont(new Font("Arial",0,28));
        FontMetrics fM = g.getFontMetrics();
        while(objects.hasNext()){
            Pair<MObject, Coords2D> obj = objects.next();
            int startX = (int)(((obj.getValue().getX()-functionalMinX)/xrange) * getWidth());
            int startY = (int)(((obj.getValue().getY()-functionalMinY)/yrange) * getHeight());
            int width = r*2;
            int height = r*2;
            
            g.setColor(Color.BLUE);
            g.fillOval(startX - (width/2), startY-(height/2), width, height);
            g.setColor(Color.BLACK);
            
            String s = obj.getKey().getId();
            g.drawString(s, startX - fM.stringWidth(s)/2, startY + r + fM.getHeight());
        }
        
        
    }
    
    
    
            
}
