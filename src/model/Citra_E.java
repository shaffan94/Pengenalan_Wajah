/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 *
 * @author USER
 */
public class Citra_E {
    private BufferedImage citra;
    private int koordinat_RGB;
    private int n_citra;
    private int panjang, lebar;
    private String kelasCitra;
    
    public Citra_E(int n_citra){
        this.n_citra = n_citra;
    }
    
    public Citra_E(BufferedImage citra){
        this.citra = citra;
    }
    
    public void setHeight(){
        panjang = citra.getHeight();
    }
    
    public void setWidth(){
        lebar = citra.getWidth();
    }
    
    public int getHeight(){
        return panjang;
    }
    
    public int getWidth(){
        return lebar;
    }
    
    public int getType(){
        return citra.getType();
    }
    
    public int get_koordinatRGB(int x, int y){
        return koordinat_RGB = citra.getRGB(x, y);
    }
    
    public void set_citraRGB(int x, int y, Color warna){
        citra.setRGB(x, y, warna.getRGB());
    }
    
    public void set_kelasCitra(String kelas){
        kelasCitra = kelas;
    }

    public String get_kelasCitra(){
        return kelasCitra;
    }

    
    public BufferedImage getCitra(){
        return citra;
    }
}
