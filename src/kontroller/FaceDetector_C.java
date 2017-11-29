/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroller;

import model.Face_E;
import detection.Detector;
import java.awt.Color;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import javax.imageio.ImageIO;

/**s
 *
 * @author USER
 */
public class FaceDetector_C {
   public List<Rectangle> listRect;
    
    public FaceDetector_C(){
        
    }
    
    public Face_E detect(File image){
        String xml = "/Users/digitalcreative/Documents/Pengenalan_Wajah/haarcascade_frontalface_default.xml";
        Detector d = new Detector(xml);
        listRect = d.getFaces(image.getAbsolutePath(), 1.3f, 1.1f, 0.1f, 2, true);
        //listRect = d.getFaces(image.getAbsolutePath(), 1.2f, 1.1f, 0.f, 2, true);        
        BufferedImage buffer = null;
        try{
            buffer = ImageIO.read(image);        
        }catch(Exception ex){
            System.out.println("Terjadi Kesalahan: "+ex.getMessage());
        }
        
        buffer = putRectangles(buffer);
        //Rectangle kotak = getMaxRectangle();
        
        return new Face_E(buffer);
    }
    
    private BufferedImage putRectangle(BufferedImage image, Rectangle kotak){
        //image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        int x;
        int w = (int) kotak.getWidth();
        int h = (int) kotak.getHeight();
        int l =  kotak.getLocation().x;
        int r = l+w;
        int t = kotak.getLocation().y;
        int b = t+h;
        Color hijau = new Color(0,255,0);
        
        //kiri ke kanan bagian atas
        for(x=l; x<=r; x++) image.setRGB(x, t, hijau.getRGB());
        
        //kiri ke kanan bagian bawah
        for(x=l; x<=r; x++) image.setRGB(x, b, hijau.getRGB());
        
        //atas ke bawah bagian kiri
        for(x=t; x<=b; x++) image.setRGB(l, x, hijau.getRGB());
        
        //atas ke bawah bagian kanan
        for(x=t; x<=b; x++) image.setRGB(r, x, hijau.getRGB());
        
        return image;
    }
    
    private BufferedImage cropImage(BufferedImage image, Rectangle kotak){
        //image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        int i,j,red, green, blue, koordinat;
        int x;
        int w = (int) kotak.getWidth();
        int h = (int) kotak.getHeight();
        int l =  kotak.getLocation().x;
        int r = l+w;
        int t = kotak.getLocation().y;
        int b = t+h;
        Color c_image;
        BufferedImage citra = new BufferedImage(w, h, image.getType());
        
        for(i=l; i<r; i++){
            for(j=t; j<b; j++){
                koordinat = image.getRGB(i, j);
                c_image = new Color(koordinat);
                red = (int)(c_image.getRed());
                green = (int)(c_image.getGreen());
                blue = (int)(c_image.getBlue());
                
                c_image = new Color(red, green, blue);
                citra.setRGB(i-l, j-t, c_image.getRGB());
            }
        }
        
        return citra;
    }
    
    private BufferedImage putRectangles(BufferedImage image){
        for(int i=0;i<listRect.size();i++){
            image = cropImage(image, listRect.get(i));
        }
        return image;
    }
    
    private Rectangle getMaxRectangle(){
        Rectangle r = listRect.get(0);
        for(int i=0;i<listRect.size();i++){
            if(listRect.get(i).height > r.height){
                r = listRect.get(i);
            }
        }
        return r;
    } 
}
