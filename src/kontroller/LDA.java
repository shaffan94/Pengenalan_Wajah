/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroller;

import Jama.EigenvalueDecomposition;
import Jama.Matrix;
import java.awt.Color;
import java.util.ArrayList;
import model.Citra_E;

/**
 *
 * @author USER
 */
public class LDA {
    private int n_sampleClass;
    private double[][] vektorWajahKeseluruhan;
    private double[][] vektorWajahUji;
    private ArrayList<double[][]> vektorKelasWajahList;
    private double[][] matriksW;
    private double[][] vektorDominan;
    private final int jumlahKomponen = 8;
    
    public LDA(){}
    
    public void set_vektorWajahLatih(Citra_E[] citra_Latih, String[] kelasCitra){
        // dapatkan sampel kelas menggunakan data pada database
        // bentuklah matriks wajahnya
        int width, height, nData, x, i, j, k,l, koordinat;
        Citra_E[] citraLatih;
        double[][] vektorKelasWajah;
        double[][] vektorWajahKeseluruhanTemp;
        Color co;
        
        citraLatih = citra_Latih;
        height = citraLatih[0].getCitra().getHeight();
        width = citraLatih[0].getCitra().getWidth();
        nData = citraLatih.length;
        vektorWajahKeseluruhanTemp = new double[28][width*height];
        for(i=0;i<28;i++){
            x=0;
            for(j=0;j<width;j++){
                for(k=0;k<height;k++){
                    koordinat = citraLatih[i].get_koordinatRGB(j, k);
                    co = new Color(koordinat);
                    vektorWajahKeseluruhanTemp[i][x]= co.getRed();
                    x++;
                }
            }
        }
        
        vektorWajahKeseluruhan = new double[width*height][28];
        for(i=0; i<width*height; i++){
            for(j=0; j<28; j++){
                vektorWajahKeseluruhan[i][j] = vektorWajahKeseluruhanTemp[j][i];
            }
        } 
       
        int nSampel[] = new int[kelasCitra.length];
        for(i=0; i<kelasCitra.length; i++){
            int sumSampel = 0;
            for(j=0; j<28; j++){
                if(citraLatih[j].get_kelasCitra().equals(kelasCitra[i])){
                    sumSampel += 1;
                }
            }
            nSampel[i] = sumSampel;
        }
        
        vektorKelasWajahList = new ArrayList<>();
        
        int iterasiNSampel;
        for(i=0; i<kelasCitra.length; i++){
            vektorKelasWajah = new double[width*height][nSampel[i]];
            iterasiNSampel = 0;
            for(j=0; j<28; j++){
                if(citraLatih[j].get_kelasCitra().equals(kelasCitra[i])){
                    x=0;
                    for(k=0;k<width;k++){
                        for(l=0;l<height;l++){
                            koordinat = citraLatih[j].get_koordinatRGB(k, l);
                            co = new Color(koordinat);
                            vektorKelasWajah[x][iterasiNSampel]= co.getRed();
                            x++;
                        }
                    }
                    iterasiNSampel = iterasiNSampel + 1;
                }
            }
            vektorKelasWajahList.add(vektorKelasWajah);
        }
        
        System.out.println("Proses Inisialisasi Vektor Wajah Latih is Done");
    }
    
    public double[][] set_vektorWajahUji(Citra_E citra_Uji){
        // dapatkan sampel kelas menggunakan data pada database
        // bentuklah matriks wajahnya
        int width, height, x, koordinat;
        Color co;
        
        height = citra_Uji.getCitra().getHeight();
        width = citra_Uji.getCitra().getWidth();
        
        vektorWajahUji = new double[width*height][1];
        
        x = 0;
        for(int i=0;i<width;i++){
            for(int j=0;j<height;j++){
                koordinat = citra_Uji.get_koordinatRGB(i, j);
                co = new Color(koordinat);
                vektorWajahUji[x][0]= co.getRed();
                x++;
            }
        }
        
        System.out.println("Proses Inisialisasi vektor wajah uji is Done");
        
        return vektorWajahKeseluruhan;
    }
    
    public ArrayList<ArrayList> getMatriksTranspose(ArrayList<ArrayList> m_wajah){ 
        int i,j;
        ArrayList<ArrayList> m_transpose;
        ArrayList<Double> baris;
        
        m_transpose = new ArrayList<>();
        
        for(i=0; i<m_wajah.get(0).size(); i++){
            baris = new ArrayList<>();
            for(j=0; j<m_wajah.size(); j++){
                baris.add(Double.parseDouble(m_wajah.get(j).get(i).toString()));
            }
            m_transpose.add(baris);
        }
        
        return m_transpose;
    }
    
    public ArrayList<ArrayList> hitungMeanClass(){
        int i, j, k, m, bar, kol;
        double meanClassTemp;
        ArrayList<Double> baris;
        ArrayList<ArrayList> meanClass;
        
        meanClass = new ArrayList<>();
        
        for(i=0; i<vektorKelasWajahList.size(); i++){
            bar = vektorKelasWajahList.get(i).length;
            kol = vektorKelasWajahList.get(i)[0].length;
            
            baris = new ArrayList<>();
            
            for(j=0; j<bar; j++){
                meanClassTemp = 0.0;
                for(k=0; k<kol; k++){
                    meanClassTemp += vektorKelasWajahList.get(i)[j][k];
                }
                
                baris.add(meanClassTemp/kol);
            }
            
            meanClass.add(baris);
        }
        
        System.out.println("Proses menghitung mean kelas is Done");
        
        return meanClass;
    }
    
    public ArrayList<Double> hitungMeanAll(){
        int i, j, bar, kol;
        ArrayList<Double> meanAll;
        double m_meanAll;
        
        meanAll = new ArrayList<>();
        bar = vektorWajahKeseluruhan.length;
        kol = vektorWajahKeseluruhan[0].length;
        
        for(i=0; i<bar; i++){
            m_meanAll = 0.0;
            for(j=0; j<kol; j++){
                m_meanAll += vektorWajahKeseluruhan[i][j];
            }
            meanAll.add(m_meanAll/kol);
        }
        
        System.out.println("Proses menghitung mean keseluruhan is Done");
        
        return meanAll;
    }
    
    public ArrayList<ArrayList> hitungMatriksSW(){
        ArrayList<ArrayList> m_meanClass;
        ArrayList<ArrayList> m_transpose;
        ArrayList<Double> baris;
        ArrayList<ArrayList> matriksSW;
        ArrayList<ArrayList> m_si;
        ArrayList<ArrayList> m_kali;
        ArrayList<ArrayList> vektorWajahTemp;
        int i, j, k, l, bar, kol;
        
        m_meanClass = hitungMeanClass();
        matriksSW = new ArrayList<>();
        
        for(i=0; i<vektorKelasWajahList.get(0).length; i++){
            baris = new ArrayList<>();
            for(j=0; j<vektorKelasWajahList.get(0).length; j++){
                baris.add(0.0);
            }
            matriksSW.add(baris);
        }
        
        for(i=0; i<vektorKelasWajahList.size(); i++){
            bar = vektorKelasWajahList.get(i).length;
            kol = vektorKelasWajahList.get(i)[0].length;
            m_si = new ArrayList<>();
            
            
            for(j=0; j<bar; j++){
                baris = new ArrayList<>();
                for(k=0; k<bar; k++){
                    baris.add(0.0);
                }
                m_si.add(baris);
            }
            
            for(j=0; j<kol; j++){
                baris = new ArrayList<>();
                vektorWajahTemp = new ArrayList<>();
                
                for(k=0; k<bar; k++){
                    baris.add(vektorKelasWajahList.get(i)[k][j] - Double.parseDouble(m_meanClass.get(i).get(k).toString()));
                }                
                
                vektorWajahTemp.add(baris);
                
                m_kali = hitungMatriksKali(vektorWajahTemp, getMatriksTranspose(vektorWajahTemp));
                
                for(k=0; k<bar; k++){
                    for(l=0; l<bar; l++){
                        m_si.get(k).set(l, Double.parseDouble(m_si.get(k).get(l).toString()) + Double.parseDouble(m_kali.get(k).get(l).toString()));
                    }
                }
                
                m_kali.clear();
                
                System.out.println("data ke-"+j+": success");
            }
            
            //System.out.println();
            //System.out.println("iterasi kelas ke-"+i);
            for(j=0; j<bar; j++){
                for(k=0; k<bar; k++){
                    matriksSW.get(j).set(k, (Double.parseDouble(matriksSW.get(j).get(k).toString()) + Double.parseDouble(m_si.get(j).get(k).toString())));
                    //System.out.print(matriksSW+" ");
                }
                //System.out.println();
            }
            
            /*System.out.println();
            System.out.println("iterasi kelas ke-"+i);
            for(j=0; j<bar; j++){
                for(k=0; k<bar; k++){
                    System.out.print(Double.parseDouble(matriksSW.get(j).get(k).toString())+" ");
                }
                System.out.println();
            }*/
        }
        
        System.out.println("Proses Perhitungan matriks Sw is Done");
        return matriksSW;
    }
    
    public ArrayList<ArrayList> hitungMatriksKali(ArrayList<ArrayList> m_teta, ArrayList<ArrayList> m_transpose){
        ArrayList<ArrayList> m_x;
        ArrayList<Double> kolom;
        int i, j, k;
        
        m_x = new ArrayList<>();
               
        for(i=0; i<m_teta.get(0).size(); i++){
            kolom = new ArrayList<>();
            for(j=0; j<m_teta.get(0).size(); j++){
                kolom.add(0.0);
            }
            m_x.add(kolom);
        }
        
        for(i=0; i<m_teta.get(0).size(); i++){
            for(j=0; j<m_teta.get(0).size(); j++){
                for(k=0; k<m_teta.size(); k++){
                    m_x.get(i).set(j, Double.parseDouble(m_x.get(i).get(j).toString())+ (Double.parseDouble(m_transpose.get(i).get(k).toString()) * Double.parseDouble(m_teta.get(k).get(j).toString())));  
                }
            }
        }
        
        return m_x;
    }
    
    public ArrayList<ArrayList> hitungMatriksSB(){
        ArrayList<ArrayList> m_meanClass;
        ArrayList<Double> baris;
        ArrayList<ArrayList> matriksSB;
        ArrayList<ArrayList> vektorWajahTemp;
        ArrayList<Double> m_meanAll;
        ArrayList<ArrayList> m_transpose;
        double[][] vektorWajahKelasTemp;
        ArrayList<ArrayList> m_kali;
        
        int i, j, k, bar, kol;
        
        m_meanClass = hitungMeanClass();
        m_meanAll = hitungMeanAll();
        matriksSB = new ArrayList<>();
        m_kali = new ArrayList<>();
        
        for(i=0; i<vektorKelasWajahList.get(0).length; i++){
            baris = new ArrayList<>();
            for(j=0; j<vektorKelasWajahList.get(0).length; j++){
                baris.add(0.0);
            }
            matriksSB.add(baris);
        }
        
        for(i=0; i<vektorKelasWajahList.size(); i++){
            bar = vektorKelasWajahList.get(i).length;
            kol = vektorKelasWajahList.get(i)[0].length;
            
            baris = new ArrayList<>();
            vektorWajahTemp = new ArrayList<>();

            System.out.println();
            System.out.println("iterasi kelas ke-"+i);
            
            for(j=0; j<bar; j++){
                baris.add(Double.parseDouble(m_meanClass.get(i).get(j).toString()) - m_meanAll.get(j));
            }                

            vektorWajahTemp.add(baris);
            m_kali = hitungMatriksKali(vektorWajahTemp, getMatriksTranspose(vektorWajahTemp));
            
            for(j=0; j<bar; j++){
                for(k=0; k<bar; k++){
                    matriksSB.get(j).set(k, Double.parseDouble(matriksSB.get(j).get(k).toString()) + Double.parseDouble(m_kali.get(j).get(k).toString()));
                }
            }
        }
        
        System.out.println("Proses Perhitungan matriks Sb is Done");
        
        return matriksSB;
    }
    
    public double[][] hitungMatriksInvers(ArrayList<ArrayList> mInvers){
        int i,j;
        double[][] matriksInv;
        double[][] matriksInvers;
        
        matriksInv = new double[mInvers.size()][mInvers.size()];
        for(i=0; i<mInvers.size(); i++){
            for(j=0; j<mInvers.size(); j++){
                matriksInv[i][j] = Double.parseDouble(mInvers.get(i).get(j).toString());
            }
        }
        
        System.out.println("pnjang matriks : "+matriksInv.length);
        System.out.println("lebar matriks : "+matriksInv[0].length);
        Matrix mInv = new Matrix(matriksInv);
        if(mInv.det() != 0.0){
            matriksInvers = mInv.inverse().getArray();
        }
        else{
            matriksInvers = mInv.getArray();
        }
        //matriksInvers = mInv.inverse().getArray();
        System.out.println("det matriks : "+mInv.det());
        
        return matriksInv;
    }
    
    public double[][] hitungMatriksProyeksiSwTSb(){
        int i, j, k, m;
        ArrayList<ArrayList> matriksSBTemp;
        ArrayList<ArrayList> matriksSB;
        ArrayList<ArrayList> matriksSWTemp;
        ArrayList<ArrayList> matriksSW;
        ArrayList<Double> baris;
        double[][] matriksSWTSB;
        double[][] matriksSWInv;
        
        matriksSWTemp = hitungMatriksSW();
        matriksSBTemp = hitungMatriksSB();
        matriksSW = new ArrayList<>();
        matriksSB = new ArrayList<>();
        
        for(i=0; i<matriksSWTemp.size(); i++){
            baris = new ArrayList<>();
            for(j=0; j<matriksSWTemp.size(); j++){
                baris.add(Double.parseDouble(matriksSWTemp.get(i).get(j).toString()));
            }
            matriksSW.add(baris);
        }
        matriksSWTemp.clear();
        matriksSWTemp.trimToSize(); 
        
        /*System.out.println("Matriks SW : ");
        for(i=0; i<matriksSW.size(); i++){
            for(j=0; j<matriksSW.size(); j++){
                System.out.print(matriksSW.get(i).get(j)+" ");
            }
            System.out.println();
        }*/
        
        for(i=0; i<matriksSBTemp.size(); i++){
            baris = new ArrayList<>();
            for(j=0; j<matriksSBTemp.size(); j++){
                baris.add(Double.parseDouble(matriksSBTemp.get(i).get(j).toString()));
            }
            matriksSB.add(baris);
        }
        matriksSBTemp.clear();
        matriksSBTemp.trimToSize();
        //matriksSW.clear();
        //matriksSW.trimToSize();
        
        m = matriksSB.size();
        matriksSWInv = hitungMatriksInvers(matriksSW);
        matriksSWTSB = new double[m][m];
        
        for(i=0; i<m; i++){
            for(j=0; j<m; j++){
                matriksSWTSB[i][j] = 0.0;
                for(k=0; k<m; k++){
                    matriksSWTSB[i][j] = matriksSWTSB[i][j] + (matriksSWInv[i][k] * (Double)matriksSB.get(k).get(j));
                }
            }
        }
        
        matriksSB.clear();
        matriksSB.trimToSize();
        
        System.out.println("Proses Perhitungan proyeksi matriks SwTeta*Sb is Done");
        return matriksSWTSB;
    }
    
    public void eigenValues(){
        double[] eigenValue;
        double[][] eigenVector;
        double[][] matriksSWTSB;
        
        matriksSWTSB = hitungMatriksProyeksiSwTSb();
        Matrix m_SWTB = new Matrix(matriksSWTSB);
        EigenvalueDecomposition eigen = m_SWTB.eig();
        
        Matrix EigenVal = eigen.getD();
        Matrix EigenVec = eigen.getV();
        
        eigenValue = eigen.getRealEigenvalues();
        eigenVector = EigenVec.getArray();
        
        sortEigen(eigenValue, eigenVector);
        
        matriksW = new double[matriksSWTSB.length][jumlahKomponen];
        matriksW = potongKolom(eigenVector, jumlahKomponen);
        
        simpan_matriksW();
        System.out.println("Proses Perhitungan eigenVektor, eigenValue, dan pembentukan matriksW is Done");
    }
    
    public void sortEigen(double[] eigenValue, double[][] eigenVector){
        int n = eigenValue.length;
        double tempEigenVal;
        double tempEigenVec;
       
        for(int i=0;i<n;i++){
            for(int j=i+1;j<n;j++){
                if(eigenValue[i]<eigenValue[j]){
                    tempEigenVal = eigenValue[i];
                    eigenValue[i] = eigenValue[j];
                    eigenValue[j] = tempEigenVal;
                    
                    for(int k=0;k<n;k++){
                        tempEigenVec = eigenVector[k][i];
                        eigenVector[k][i] = eigenVector[k][j];
                        eigenVector[k][j] = tempEigenVec;
                    }
                }
            }
        }
    }
    
    public double[][] potongKolom(double[][] eVector, int komponen){
        int i,j;
        double[][] tempVector = new double[eVector.length][komponen];
        
        for(i=0;i<eVector.length;i++){
            for(j=0;j<komponen;j++){
                tempVector[i][j] = eVector[i][j];
            }
        }
        return tempVector;
    }
    
    public double[] hitungMatriksUjiY(){
        int i,j,k, bar, kol;
        double[] matriksUjiY;
        double[][] matriksWT;
        double sum;
        
        bar = vektorWajahUji.length;
        kol = vektorWajahUji[0].length;
        matriksWT = new double[jumlahKomponen][bar];
        matriksUjiY = new double[jumlahKomponen];
        
        for(i=0; i<jumlahKomponen; i++){
            for(j=0; j<bar; j++){
                matriksWT[i][j] = matriksW[j][i];
            }
        }
        
        for(i=0;i<jumlahKomponen;i++){
            for(j=0;j<kol;j++){
                sum = 0.0;
                for(k=0;k<bar;k++){
                    sum = sum + matriksWT[i][k]*vektorWajahUji[k][j];
                }
                matriksUjiY[i] = sum;
            }
        }
        
        /*System.out.println("Ekstaksi ciri : ");
        for(i=0;i<jumlahKomponen;i++){
            System.out.print(matriksUjiY[i]+" ");
        }
        System.out.println();*/
        
        System.out.println("Proses Perhitungan matriks Y is Done");
        
        return matriksUjiY;
    }
    
    public double[][] hitungMatriksY(){
        int i,j,k, bar, kol;
        double[][] matriksY;
        double[][] matriksWT;
        double sum;
        
        bar = vektorWajahKeseluruhan.length;
        kol = vektorWajahKeseluruhan[0].length;
        matriksWT = new double[jumlahKomponen][bar];
        matriksY = new double[jumlahKomponen][kol];
        
        for(i=0; i<jumlahKomponen; i++){
            for(j=0; j<bar; j++){
                matriksWT[i][j] = matriksW[j][i];
            }
        }
       
        System.out.println("Matriks Y : ");
        for(i=0;i<jumlahKomponen;i++){
            for(j=0;j<kol;j++){
                sum = 0.0;
                for(k=0;k<bar;k++){
                    sum = sum + matriksWT[i][k]*vektorWajahKeseluruhan[k][j];
                }
                matriksY[i][j] = sum;
                System.out.print(matriksY[i][j]+" ");
            }
            System.out.println();
        }
        
        System.out.println("Proses Perhitungan matriks Y is Done");
        
        return matriksY;
    }
    
    public void getDataMatriksW(){
        int i, j, nData;
        DatabaseManager databaseManager;
        String sql_mW;
        
        databaseManager = new DatabaseManager();
        databaseManager.set_KoneksiDatabase();
        
        sql_mW = "select count(*) from matrikswdata";
        nData = databaseManager.jumlahDataMatriksW(sql_mW);
        
        if(nData == 0){
            System.out.println("Data matriks w pada database kosong");
        }
        else{
            sql_mW = "select * from matrikswdata";
            matriksW = databaseManager.getDataMatriksW(sql_mW, nData, jumlahKomponen);   
        }
        
        System.out.println("Proses Pemanggilan Data Matriks W pada Database is Done");
    }
    
    public void simpan_matriksW(){
        int i, j;
        double[] matriksWBaris;
        boolean isData;
        DatabaseManager databaseManager;
        String sql_mW;
        
        databaseManager = new DatabaseManager();
        databaseManager.set_KoneksiDatabase();
        
        sql_mW = "select count(*) from matrikswdata";
        isData = databaseManager.cekIsiDataMatriksW(sql_mW);
        
        
        if(isData == false){
            sql_mW = "INSERT INTO matrikswdata (id_model, ekstrakCiri1, ekstrakCiri2, ekstrakCiri3, ekstrakCiri4, ekstrakCiri5, ekstrakCiri6, ekstrakCiri7, ekstrakCiri8) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            matriksWBaris = new double[jumlahKomponen];
            
            for(i=0; i<matriksW.length; i++){
                for(j=0; j<matriksW[i].length; j++){
                    matriksWBaris[j] = matriksW[i][j];
                }
                databaseManager.insertDataMatriksW(sql_mW, matriksWBaris, i+1);
            }
        }
        else{
            int nData;
            
            nData = databaseManager.getJumlahBarisMatriksW(sql_mW);
            
            sql_mW = "delete from matrikswdata where id_model = ?";
            
            for(i=0; i<nData; i++){
                databaseManager.deleteDataMatriksW(sql_mW, i+1);
            }
            
            sql_mW = "INSERT INTO matrikswdata (id_model, ekstrakCiri1, ekstrakCiri2, ekstrakCiri3, ekstrakCiri4, ekstrakCiri5, ekstrakCiri6, ekstrakCiri7, ekstrakCiri8) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            matriksWBaris = new double[jumlahKomponen];
            
            for(i=0; i<matriksW.length; i++){
                for(j=0; j<matriksW[i].length; j++){
                    matriksWBaris[j] = matriksW[i][j];
                }
                databaseManager.insertDataMatriksW(sql_mW, matriksWBaris, i+1);
            }
        }
    }
}
