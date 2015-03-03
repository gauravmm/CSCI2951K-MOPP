/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.brown.csci2951k.ui;

import edu.brown.csci2951k.models.data.MObjectSet;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

/**
 *
 * @author Zach
 */
public class Pnl2DVizXAxisLabel extends Pnl2DVizAxisLabel{

    public Pnl2DVizXAxisLabel(double min, double max, int numItems) {
        super(min,max,numItems);
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        double range = max-min;
        double functional_min = min - (range*.25);
        double functional_max = max + (range*.25);
        for(int i = 0; i<numItems;i++){
            String text = String.format("%.3f", (double) functional_min + ((((double)functional_max - (double)functional_min)/(double)numItems) * (double)i));
            g2d.drawString(text, (int)((double)getWidth()/(double)numItems * (double)i)  , getHeight());
        }
    }
    
   
}
