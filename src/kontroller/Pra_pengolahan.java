/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroller;

import model.Face_E;
import model.Citra_E;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author USER
 */
public class Pra_pengolahan {
    private Citra_E[] citraLatih;
    private BufferedImage citraGray;
    private Citra_E citraUji;
    private int nkelasLatih;
    private String[] namaFile;
    private String[] nama_kelasLatih;
    private String namafile_test;
    private int n_citra;
    private Face_E face;
    
    //prosedur untuk mendapatkan n citra latih pada direktori pembelajaran
    public void init_citraLatih(){
        int i, j;
        boolean ada;
        BufferedImage temp;
        
        String path = "D://Skripsi Shaffan Madanny//TA I//Program//Data Wajah 1//Pembelajaran 2";
        File direktori = new File(path);
        File[] daftarFile = direktori.listFiles();
        n_citra = daftarFile.length;
        citraLatih = new Citra_E[28];
        namaFile = new String[28];
        
        for(i=0; i<28; i++){
            ada = daftarFile[i].isFile();
            if(ada == true){
                try {
                    temp = ImageIO.read(daftarFile[i]);
                    //citraLatih[i] = new Citra_E(temp);
                    citraLatih[i] = new Citra_E(faceDetect(daftarFile[i].getAbsoluteFile(), temp));
                    namaFile[i] = daftarFile[i].getName();
                    citraLatih[i].set_kelasCitra(set_kelasLatih(namaFile[i]));
                } catch (IOException ex) {
                    System.out.println("Terjadi kesalahan pada saat membuka berkas" + ex.getMessage());
                }
            }
        }
    }
    
    //prosedur untuk memperoleh informasi banyak kelas citra latih dan menyimpan nama kelas citra latih
    public void set_nKelasLatih(){
        String kelas_temp1, kelas_temp2 = "";
        String[] kelas1, kelas2;
        int i, j;
        nama_kelasLatih = new String[2];
        
        j=0;
        System.out.println("banyak data: " + namaFile.length);
        for(i=0; i<28; i++){
            if(i == 28-1){
                kelas_temp1 = namaFile[i];
            }else{
                kelas_temp1 = namaFile[i];
                kelas_temp2 = namaFile[i+1];
            }
            
            kelas1 = kelas_temp1.split(" ");
            kelas2 = kelas_temp2.split(" ");
            
            if(kelas1[0].equals(kelas2[0])){
                if(i == 28-1){
                    nama_kelasLatih[j] = kelas1[0];
                    j = j+1;
                }
            }
            else{
                System.out.println(kelas1[0]);
                nama_kelasLatih[j] = kelas1[0];
                j = j+1;
            }
        }
        
        nkelasLatih = j;
        
        System.out.println(j);
    }
    
    //fungsi untuk mendapatkan kelas citra
    public String set_kelasLatih(String file){
        String[] kelas_file;
        
        kelas_file = file.split(" ");
        return kelas_file[0];
    }
    
    public String set_kelasUji(String file){
        String[] kelas_file;
        
        kelas_file = file.split(" ");
        return kelas_file[0];
    }
    
    //prosedur untuk mendapatkan citra uji pada direktori pengujian
    public void init_citraUji(String filename){
        BufferedImage temp, finalImage;
        String namaPath = "D://Skripsi Shaffan Madanny//TA I//Program//Data Wajah 1//Pengujian//"+filename;
        File file = new File(namaPath);
        System.out.println("namaFile Anda: "+file.toString());
        try{
            temp = ImageIO.read(file);
            Image image = temp.getScaledInstance(27,30, Image.SCALE_SMOOTH);
            // Create a buffered image with transparency
            finalImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_ARGB);

            // Draw the image on to the buffered image
            Graphics2D bGr = finalImage.createGraphics();
            bGr.drawImage(image, 0, 0, null);
            bGr.dispose();
            //citraUji =  new Citra_E(faceDetect(file.getAbsoluteFile(), temp));
            citraUji =  new Citra_E(finalImage);
            citraUji.set_kelasCitra(set_kelasUji(filename));
        }catch(IOException ex){
            System.out.println("Terjadi kesalahan saat membuka berkas! - "+ex.getMessage());
        }
    }
    
    //fungsi untuk mengubah citra asli ke citra grayscale pada citra latih
    public void set_grayscaleLatih(){
        Color c_oriIm, c_grayIm;
        int height, width, R, G, B, i, j, k, grayLevel;
        
        for(i=0; i<28; i++){
            height = citraLatih[i].getCitra().getHeight();
            width = citraLatih[i].getCitra().getWidth();
            
            for(j=0; j<width; j++){
                for(k=0; k<height; k++){
                    int koordinat = citraLatih[i].get_koordinatRGB(j, k);
                    c_oriIm = new Color(koordinat);
                    R = (int)c_oriIm.getRed();
                    G = (int)c_oriIm.getGreen();
                    B = (int)c_oriIm.getBlue();
                    
                    grayLevel = (R+G+B)/3;
                    c_grayIm = new Color(grayLevel, grayLevel, grayLevel);
                    citraLatih[i].set_citraRGB(j, k, c_grayIm);
                }
            }
        }
        
        System.out.println("Proses Grayscale Latih is Done");
    }
    
    //fungsi untuk mengubah citra grayscale ke citra hasil ekualisasi histogram pada citra latih
    public void set_ekualisasiHistogramLatih(){
        int i, j, k, l, width, height, intensitas, koordinat, HE_Value;
        int[] histogram;
        int[] nj;
        int[] T;
        double[] CDF;
        Color c_grayIm, c_HEIm;
        
        histogram = new int[256];
        nj = new int[256];
        CDF = new double[256];
        T = new int[256];
        
        for(i=0; i<28; i++){
            height = citraLatih[i].getCitra().getHeight();
            width = citraLatih[i].getCitra().getWidth();
            
            for(j=0; j<256; j++){
                histogram[j] = 0;
            }
            
            for(j=0; j<256; j++){
                CDF[j] = 0;
            }
            
            for(j=0; j<width; j++){
                for(k=0; k<height; k++){
                    koordinat = citraLatih[i].get_koordinatRGB(j, k);
                    c_grayIm = new Color(koordinat);
                    intensitas = c_grayIm.getRed();
                    histogram[intensitas]++;
                }
            }
            
            for(j=0; j<256; j++){
                nj[j] = 0;
            }

            for(j=0; j<256; j++){
                for(k=0; k<j+1; k++){
                    nj[j] += histogram[k];
                }
            }

            for(j=0; j<256; j++){
                CDF[j] = (double)nj[j]/(double)(height*width);
            }
            
            for(j=0; j<256; j++){
                T[j] = (int)(CDF[j]*255.0);    
            }
            
            for(j=0; j<width; j++){
                for(k=0; k<height; k++){
                    koordinat = citraLatih[i].get_koordinatRGB(j, k);
                    c_grayIm = new Color(koordinat);
                    intensitas = (int)(c_grayIm.getRed());
                    
                    HE_Value = intensitas;
                    for(l=0; l<256; l++){
                        if(intensitas == l){
                            HE_Value = T[intensitas];
                        }
                    }
                    
                    c_HEIm = new Color(HE_Value, HE_Value, HE_Value);
                    citraLatih[i].set_citraRGB(j, k, c_HEIm);
                }
            }
        }
        
        System.out.println("Proses Histogram Equalization Latih is Done");
    }
    
    //fungsi untuk mengubah citra asli ke citra grayscale pada citra uji
    public void set_grayscaleUji(){
        Color c_oriIm, c_grayIm;
        int height, width, R, G, B, i, j, k, grayLevel;
        
        height = citraUji.getCitra().getHeight();
        width = citraUji.getCitra().getWidth();
        
        for(j=0; j<width; j++){
            for(k=0; k<height; k++){
                int koordinat = citraUji.get_koordinatRGB(j, k);
                c_oriIm = new Color(koordinat);
                R = (int)c_oriIm.getRed();
                G = (int)c_oriIm.getGreen();
                B = (int)c_oriIm.getBlue();

                grayLevel = (R+G+B)/3;
                c_grayIm = new Color(grayLevel, grayLevel, grayLevel);
                citraUji.set_citraRGB(j, k, c_grayIm);
            }
        }
    }
    
    //fungsi untuk mengubah citra grayscale ke citra hasil ekualisasi histogram pada citra uji
    public void set_ekualisasiHistogramUji(){
        int i, j, k, l, width, height, intensitas, koordinat, HE_Value;
        int[] histogram;
        int[] nj;
        int[] T;
        double[] CDF;
        Color c_grayIm, c_HEIm;
                
        height = citraUji.getHeight();
        width = citraUji.getWidth();
        histogram = new int[256];
        nj = new int[256];
        CDF = new double[256];
        T = new int[256];        
        
        for(i=0; i<256; i++){
            histogram[i] = 0;
        }

        for(i=0; i<width; i++){
            for(j=0; j<height; j++){
                koordinat = citraUji.get_koordinatRGB(i, j);
                c_grayIm = new Color(koordinat);
                intensitas = c_grayIm.getRed();
                histogram[intensitas]++;
            }
        }

        for(i=0; i<256; i++){
            nj[i] = 0;
        }

        for(i=0; i<256; i++){
            for(j=0; j<i+1; j++){
                nj[i] += histogram[j];
            }
        }
        
        for(i=0; i<256; i++){
            CDF[i] = (double)nj[i]/(double)(height*width);
        }

        for(i=0; i<256; i++){
            T[i] = (int)(CDF[i]*255.0);    
        }
        
        for(i=0; i<height; i++){
            for(j=0; j<width; j++){
                koordinat = citraUji.get_koordinatRGB(i, j);
                c_grayIm = new Color(koordinat);
                intensitas = (int)(c_grayIm.getRed());
                
                HE_Value = intensitas;
                for(k=0; k<256; k++){
                    if(intensitas == k){
                        HE_Value = T[intensitas];
                    }
                }

                c_HEIm = new Color(HE_Value, HE_Value, HE_Value);
                citraUji.set_citraRGB(i, j, c_HEIm);
            }
        }
    }
    
    
    public boolean deteksiWajah(File file, BufferedImage citra){
        boolean status = true;
        try{
            if(file!=null){
                FaceDetector_C fd = new FaceDetector_C();
                face = fd.detect(file);
                citra = face.getWajah();
                Rectangle r = face.getBoundary();
            }
        }catch(Exception ex){
            status = false;
        }
        return status;
    }
    
    public BufferedImage set_grayscale(BufferedImage image){
        Color co;
        int gray,temp;
        for(int w=0;w<image.getWidth();w++){
            for(int h=0;h<image.getHeight();h++){
                co = new Color(image.getRGB(w, h));
                gray = (co.getRed()+co.getGreen()+co.getBlue())/3;
                co = new Color(gray, gray, gray);
                image.setRGB(w, h, co.getRGB());
            }
        }
        return image;
    }
    
    public BufferedImage set_ekualisasiHistogram(BufferedImage image){
        int i, j, k, width, height, intensitas, koordinat, HE_Value;
        int[] histogram;
        double[] CDF;
        int[] T;
        int[] nj;
        Color c_grayIm, c_HEIm;
        
        width = image.getWidth();
        height = image.getHeight();
        
        histogram = new int[256];
        CDF = new double[256];
        nj = new int[256];
        T = new int[256];
        
        for(k=0; k<256; k++){
            histogram[k] = 0;
        }

        for(i=0; i<width; i++){
            for(j=0; j<height; j++){
                koordinat = image.getRGB(i, j);
                c_grayIm = new Color(koordinat);
                intensitas = c_grayIm.getRed();
                histogram[intensitas]++;
            }
        }
        
        for(k=0; k<256; k++){
            nj[i] = 0;
        }
        
        for(i=0; i<256; i++){
            for(j=0; j<i+1; j++){
                nj[i] += histogram[j];
            }
        }
        
        for(k=0; k<256; k++){
            CDF[k] = (double)nj[k]/(double)(height*width);
        }
        
        for(k=0; k<256; k++){
            T[k] = (int)(CDF[k]*255.0);    
        }
        
        for(i=0; i<width; i++){
            for(j=0; j<height; j++){
                koordinat = image.getRGB(i, j);
                c_grayIm = new Color(koordinat);
                intensitas = (int)(c_grayIm.getRed());
                
                HE_Value = intensitas;
                for(k=0; k<256; k++){
                    if(intensitas == k){
                        HE_Value = T[intensitas];
                    }
                }

                c_HEIm = new Color(HE_Value, HE_Value, HE_Value);
                image.setRGB(i, j, c_HEIm.getRGB());
            }
        }
        
        return image;
    }
    
    
    public BufferedImage faceDetect(File file, BufferedImage citra){
        if(file!=null){
            FaceDetector_C fd = new FaceDetector_C();
            face = fd.detect(file);
            citra = face.getWajah();
            Rectangle r = face.getBoundary();
        }
        return citra;
    }
    
    // fungsi untuk mendapatkan data citra latih hasil pra-pengolahan
    public Citra_E[] get_citraLatih(){
        return citraLatih;
    }
    
    //fungsi untuk mendapatkan data citra uji hasil pra-pengolahan
    public Citra_E get_citraUji(){
        return citraUji;
    }
    
    public String[] getNamaKelasLatih(){
        return nama_kelasLatih;
    }
}
