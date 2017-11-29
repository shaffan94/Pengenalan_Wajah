/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author USER
 */
public class Sampel_E {
    double[] ciri_sampel;
    String nama_sampel;
    int posisi_sampel;
    
    public Sampel_E() {
        
    }

    public double[] getCiriSampel() {
        return ciri_sampel;
    }

    public String getNama_sampel() {
        return nama_sampel;
    }
    
    public int getMCiri(){
        return ciri_sampel.length;
    }

    public void setCiriSampel(double[] ciri_sampel) {
        int i;
        
        this.ciri_sampel = new double[ciri_sampel.length];
        
        for(i=0; i<ciri_sampel.length; i++){
            this.ciri_sampel[i] = ciri_sampel[i];
        }
    }

    public void setNama_sampel(String nama_sampel) {
        this.nama_sampel = nama_sampel;
    }
    
    public void setPosisi_sampel(int posisi_sampel) {
        this.posisi_sampel = posisi_sampel;
    }
    
    public int getPosisi_sampel() {
        return posisi_sampel;
    }
}
