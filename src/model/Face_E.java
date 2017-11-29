/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author USER
 */
public class Face_E {
    private BufferedImage wajah;
    private Rectangle boundary;
    
    public Face_E(){
        this(null);
    }
    
    public Face_E(BufferedImage wajah){
        this.wajah = wajah;
        //this.boundary = boundary;
    }
    
    public Rectangle getBoundary(){
        return boundary;
    }
    
    public void setBoundary(Rectangle boundary){
        this.boundary = boundary;
    }
    
    public BufferedImage getWajah(){
        return wajah;
    }
    
    public void setWajah(BufferedImage wajah){
        this.wajah = wajah;               
    }
}
