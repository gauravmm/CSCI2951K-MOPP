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
import javax.swing.JPanel;

/**
 *
 * @author Zach
 */
public class Pnl2DVizAxisLabel extends JPanel{
    double min;
    double max;
    int numItems;

    public Pnl2DVizAxisLabel(double min, double max, int numItems) {
        this.min = min;
        this.max = max;
        this.numItems = numItems;
    }

    public void setMin(double min) {
        this.min = min;
    }

    public void setMax(double max) {
        this.max = max;
    }

    public void setNumItems(int numItems) {
        this.numItems = numItems;
    }
    
   
}
