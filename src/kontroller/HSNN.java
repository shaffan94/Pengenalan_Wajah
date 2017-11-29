/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroller;

import java.util.ArrayList;
import model.Sampel_E;

/**
 *
 * @author USER
 */
public class HSNN {
    private ArrayList<Sampel_E[]> sampelLatih;
    private ArrayList<ArrayList> HSNWajah;
    private ArrayList<Double> ThWajah;
    private ArrayList<ArrayList> PelingkupanBWajah;
    private Boolean cek;
    private Sampel_E sampelUji;
    private final double alpha;
    
    public HSNN() {
        alpha = 0.1;
    }
    
    public void setmSampel(double[][] sampel, String[] kelasLatih) {
        int i, j, iter, index;
        double[] ciriSampel;
        Sampel_E sampelTemp;
        Sampel_E[] mSampel;
        
        mSampel = new Sampel_E[14];
        ciriSampel = new double[sampel.length];
        sampelLatih = new ArrayList<>();
        
        System.out.println("Banyak data sampel : "+sampel[0].length);
        System.out.println("Banyak ciri sampel : "+sampel.length);
        
        iter = 0;
        index = 0;
        for(i=0; i<sampel[0].length; i++){
            sampelTemp = new Sampel_E();
            
            for(j=0; j<sampel.length; j++){
                ciriSampel[j] = sampel[j][i];
            }
            
            if(index == 13){
                sampelTemp.setCiriSampel(ciriSampel);
                sampelTemp.setNama_sampel(kelasLatih[iter]);
                mSampel[index] = sampelTemp;
                sampelLatih.add(mSampel);
                mSampel = new Sampel_E[14];
                iter = iter + 1;
                index = 0;
            }
            else{       
                sampelTemp.setCiriSampel(ciriSampel);
                sampelTemp.setNama_sampel(kelasLatih[iter]);
                mSampel[index] = sampelTemp;
                index += 1;
            }
        }
        
        System.out.println("Proses Persiapan sampel HSNN is Done");
    }
    
    public void setSampelUji(double[] sampel, String kelasLatih) {
        int i;
        double[] sampelTemp;
        sampelTemp = new double[sampel.length];
        sampelUji = new Sampel_E();
        
        System.out.println("Ciri sampel : ");
        for(i=0; i<sampel.length; i++){
            sampelTemp[i] = sampel[i];
            System.out.print(sampelTemp[i]+" ");
        }
        System.out.println();
        
        sampelUji.setCiriSampel(sampelTemp);
        sampelUji.setNama_sampel(kelasLatih);
        
        System.out.println("Proses Persiapan sampel HSNN is Done");
    }

    public ArrayList<ArrayList> getHSNN() {
        return HSNWajah;
    }
    
    /*public void setHSNNUji(){
        int i,j,k;
        ArrayList<Sampel_E> Lingkupan;
        ArrayList<ArrayList> PelingkupanB;
        
        for(i=0; i<PelingkupanBWajah.size(); i++){
            PelingkupanB = new ArrayList<>();
            for(j=0; j<; j++){
                for(){
                
                }
            }
        }
    }*/
    
    public void setHSNN() {
        int i,j,k,l, sampelPilihan1, sampelPilihan2, sampelPilihan, iterasi, indexlastPelingkupan, ciri, position;
        double Euc;
        double jumlah;
        double min = 1000;
        double nSampel;
        double[] ciriSampel;
        double th;
        ArrayList<Sampel_E> Lingkupan;
        ArrayList<ArrayList> PelingkupanB;
        ArrayList<Sampel_E> LingkupanTemp;
        ArrayList<Sampel_E> HSN;
        ArrayList<Sampel_E> SampelTersedia;
        Sampel_E sampel1, sampel2, sampel;
        //Sampel_E[] mSampel;

        th = 0;
        nSampel = sampelLatih.get(0).length;
        ciri = sampelLatih.get(0)[0].getMCiri();
        ciriSampel = new double[ciri];
        //mSampel = new Sampel_E[14];
        
        HSN = new ArrayList<>();
        Lingkupan = new ArrayList<>();
        PelingkupanB = new ArrayList<>();
        HSNWajah = new ArrayList<>();
        ThWajah = new ArrayList<>();
        PelingkupanBWajah = new ArrayList<>();
        
        System.out.println("sampel latih : "+sampelLatih.size());
        for(l=0; l<sampelLatih.size(); l++){
            iterasi = 0;
            position = 0;
            SampelTersedia = new ArrayList<>();
            while(iterasi <= sampelLatih.get(l).length-2){
                System.out.println("iterasi ke-"+iterasi);
                if(HSN.isEmpty()){
                    sampelPilihan1 = 0;
                    sampelPilihan2 = 0;

                    for(i=0; i<nSampel; i++){
                        sampelLatih.get(l)[i].setPosisi_sampel(i);
                        SampelTersedia.add(sampelLatih.get(l)[i]);
                    }
                    
                    for(i=0; i<nSampel; i++){
                        for(j=0; j<nSampel; j++){
                            jumlah = 0.0;
                            if(i!=j){
                                for(k=0; k<ciri; k++){
                                    jumlah = jumlah + Math.pow((sampelLatih.get(l)[i].getCiriSampel()[k]-sampelLatih.get(l)[j].getCiriSampel()[k]),2);
                                }

                                Euc = Math.sqrt(jumlah);

                                if(min > Euc){
                                    min = Euc;
                                    sampelPilihan1 = i;
                                    sampelPilihan2 = j;
                                }
                            }
                        }
                    }

                    SampelTersedia.remove(sampelPilihan1);
                    SampelTersedia.remove(sampelPilihan2);

                    HSN.add(sampelLatih.get(l)[sampelPilihan1]);
                    HSN.add(sampelLatih.get(l)[sampelPilihan2]);
                    
                    sampel1 = sampelLatih.get(l)[sampelPilihan1];
                    sampel2 = sampelLatih.get(l)[sampelPilihan2];

                    LingkupanTemp = HitungPelingkupanB(sampel1, sampel2);
                    
                    for(i=0; i<LingkupanTemp.size(); i++){
                        sampel = new Sampel_E();
                        for(j=0; j<LingkupanTemp.get(i).getMCiri(); j++){
                            ciriSampel[j] = LingkupanTemp.get(i).getCiriSampel()[j];
                        }
                        sampel.setCiriSampel(ciriSampel);
                        sampel.setNama_sampel(sampelLatih.get(l)[0].getNama_sampel());
                        Lingkupan.add(sampel);
                    }
                    
                    System.out.println("Lingkupan pada iterasi ke - "+iterasi+" : ");
                    for(i=0; i<Lingkupan.size(); i++){
                        for(j=0; j<Lingkupan.get(i).getMCiri(); j++){
                            System.out.print(Lingkupan.get(i).getCiriSampel()[j]+" ");
                        }
                        System.out.println();
                    }
                    
                    PelingkupanB.add(Lingkupan);
                    th = hitungThreshold(HSN);

                    LingkupanTemp.clear();
                    LingkupanTemp.trimToSize();
                }
                else{
                    sampelPilihan = 0;
                    int indexLast = HSN.size()-1;
                    indexlastPelingkupan = PelingkupanB.size()-1;

                    for(i=0; i<SampelTersedia.size(); i++){
                        jumlah = 0.0;
                        
                        for(k=0; k<ciri; k++){
                            jumlah = jumlah + Math.pow((HSN.get(indexLast).getCiriSampel()[k]-SampelTersedia.get(i).getCiriSampel()[k]),2);
                        }

                        Euc = Math.sqrt(jumlah);

                        if(min > Euc){
                            min = Euc;
                            sampelPilihan = i;
                        }
                    }
                    
                    System.out.println("ciri sampel baru : ");
                    for(i=0; i<sampelLatih.get(l)[SampelTersedia.get(sampelPilihan).getPosisi_sampel()].getMCiri(); i++){
                        System.out.println(sampelLatih.get(l)[SampelTersedia.get(sampelPilihan).getPosisi_sampel()].getCiriSampel()[i]+" ");
                    }
                    
                    if(CekDalamLingkupan(sampelLatih.get(l)[SampelTersedia.get(sampelPilihan).getPosisi_sampel()], PelingkupanB.get(indexlastPelingkupan), th) == true){
                        HSN.add(sampelLatih.get(l)[SampelTersedia.get(sampelPilihan).getPosisi_sampel()]);
                        Lingkupan = new ArrayList<>();
                        LingkupanTemp = HitungPelingkupanB(HSN.get(indexLast), HSN.get(indexLast+1));
                    
                        for(i=0; i<LingkupanTemp.size(); i++){
                            sampel = new Sampel_E();
                            for(j=0; j<LingkupanTemp.get(i).getMCiri(); j++){
                                ciriSampel[j] = LingkupanTemp.get(i).getCiriSampel()[j];
                            }
                            sampel.setCiriSampel(ciriSampel);
                            sampel.setNama_sampel(sampelLatih.get(l)[SampelTersedia.get(sampelPilihan).getPosisi_sampel()].getNama_sampel());
                            Lingkupan.add(sampel);
                        }
                        
                        System.out.println("Lingkupan pada iterasi ke - "+iterasi+" : ");
                        for(i=0; i<Lingkupan.size(); i++){
                            for(j=0; j<Lingkupan.get(i).getMCiri(); j++){
                                System.out.print(Lingkupan.get(i).getCiriSampel()[j]+" ");
                            }
                            System.out.println();
                        }

                        PelingkupanB.add(Lingkupan);
                        th = hitungThreshold(HSN);

                        LingkupanTemp.clear();
                        LingkupanTemp.trimToSize();
                    }
                    SampelTersedia.remove(sampelPilihan);
                }

                iterasi = iterasi+1;
            }
            
            HSNWajah.add(HSN);
            PelingkupanBWajah.add(PelingkupanB);
            ThWajah.add(th);
            Lingkupan = new ArrayList<>();
            PelingkupanB = new ArrayList<>();
            HSN = new ArrayList<>();
        }
        
        System.out.println("Proses Perhitungsn HSNN is Done");
    } 
    
    public ArrayList<Sampel_E> HitungPelingkupanB(Sampel_E Sampel1, Sampel_E Sampel2){
        double iterasi = 0.0;
        Sampel_E jumlah;
        double iterasiT;
        int ciri;
        double[] ciriSampel;
        ArrayList<Sampel_E> Lingkupan;
        
        int i;
        
        Lingkupan = new ArrayList<>();
        ciri = sampelLatih.get(0)[0].getMCiri();
        ciriSampel = new double[ciri];
        
        while(iterasi <= 1){
            iterasiT = 1.0 - iterasi;
            
            jumlah = new Sampel_E();
            for(i=0; i<ciri; i++){
                ciriSampel[i] = (iterasi*Sampel1.getCiriSampel()[i]) + (iterasiT*Sampel2.getCiriSampel()[i]);
            }
            jumlah.setCiriSampel(ciriSampel);
            Lingkupan.add(jumlah);
            iterasi = iterasi + alpha;
        }
        
        return Lingkupan;
    }
    
    public boolean CekDalamLingkupan(Sampel_E X, ArrayList<Sampel_E> sampelLingkupan, double Th){
        double Euc;
        double jumlah;
        ArrayList<Double> simpanEuc;
        boolean kondisi;
        int i,j;
        
        simpanEuc = new ArrayList<>();
        
        for(i=0; i<sampelLingkupan.size(); i++){
            jumlah = 0.0;
            
            for(j=0; j<X.getCiriSampel().length; j++){
                jumlah = jumlah + Math.pow((X.getCiriSampel()[j] - sampelLingkupan.get(i).getCiriSampel()[j]), 2);
            }
            
            Euc = Math.sqrt(jumlah);
            
            //System.out.println("Threshold : "+Th);
            //System.out.println("Euclidean : "+Euc);
            
            if(Euc < Th){
                simpanEuc.add(Euc);
            }
        }
        
        System.out.println("Banyak neuron yang tersedia : "+sampelLingkupan.size());
        System.out.println("Banyak neuron yang ditorelir : "+sampelLingkupan.size()/2);
        System.out.println("Banyak neuron yang diterima : "+simpanEuc.size());
        
        kondisi = simpanEuc.size() > (sampelLingkupan.size()/2);
        
        return kondisi;
    }
    
    public double hitungThreshold(ArrayList<Sampel_E> sampel){
        int nSampel, i, j;
        double S_jumlah;
        double S_rata2;
        double S_netral;
        double S_selisih;
        double ciri;
        double Th;
        
        nSampel = sampel.size();
        ciri = (double)sampel.get(0).getMCiri();      
        S_jumlah = 0.0;
        
        // mencari perhitungan rata-rata dari sampel wajah untuk Threshold
        for(i=0; i<nSampel; i++){
            for(j=0; j<ciri; j++){
                S_jumlah= S_jumlah+ sampel.get(i).getCiriSampel()[j];
            }   
        }
        
        S_rata2 = S_jumlah/(nSampel*ciri);
        S_jumlah = 0.0;
        
        // mencari Threshold
        for(i=0; i<nSampel; i++){
            for(j=0; j<ciri; j++){                
                S_selisih = sampel.get(i).getCiriSampel()[j] - S_rata2;
                
                if(S_selisih < 0){
                    S_netral = S_selisih * (-1); 
                }
                else{
                    S_netral = S_selisih;
                }
                
                S_jumlah= S_jumlah+ S_netral;
            }   
        }
        
        Th = S_jumlah/(nSampel*ciri);
        
        return Th;
    }
    
    public void get_modelPelatihan(){
        int i, j, k;
        DatabaseManager databaseManager;
        String sql_mW;
        
        databaseManager = new DatabaseManager();
        databaseManager.set_KoneksiDatabase();
        
        sql_mW = "select * from data_neuron_hsnn";
        PelingkupanBWajah = databaseManager.getDataNeuronHSNN(sql_mW);
    }
    
    public void simpan_modelPelatihan(){
        int i, j, k;
        ArrayList<ArrayList> PelingkupanB;
        ArrayList<Sampel_E> Lingkupan;
        boolean isData;
        DatabaseManager databaseManager;
        String sql_mW;
        
        databaseManager = new DatabaseManager();
        databaseManager.set_KoneksiDatabase();
        
        sql_mW = "select count(*) from data_neuron_hsnn";
        isData = databaseManager.cekIsiDataNeuronHSNN(sql_mW);
        
        if(isData == false){
            sql_mW = "INSERT INTO data_neuron_hsnn (id_neuron, kelas_sampel, rantai_ke, neuron_ke, ciri_1, ciri_2, ciri_3, ciri_4, ciri_5, ciri_6, ciri_7, ciri_8) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            int id = 1;
            int rantai_ke, neuron_ke;
            for(i=0; i<PelingkupanBWajah.size(); i++){
                PelingkupanB = PelingkupanBWajah.get(i);
                System.out.println("Banyak rantai pada kelas ke - "+i+" : "+PelingkupanB.size());
                for(j=0; j<PelingkupanB.size(); j++){
                    Lingkupan = PelingkupanB.get(j);
                    System.out.println("Banyak neuron pada rantai ke - "+j+" : "+Lingkupan.size());
                    for(k=0; k<Lingkupan.size(); k++){
                        rantai_ke = j;
                        neuron_ke = k;
                        databaseManager.insertDataNeuronHSNN(sql_mW, Lingkupan.get(k), id, rantai_ke, neuron_ke);
                        id = id + 1;
                    }
                    System.out.println();
                }
            }
        }
        else{
            int nData;
            
            nData = databaseManager.getJumlahDataNeuronHSNN(sql_mW);
            
            sql_mW = "delete from data_neuron_hsnn where id_neuron = ?";
            
            for(i=0; i<nData; i++){
                databaseManager.deleteDataNeuronHSNN(sql_mW, i+1);
            }
            
            sql_mW = "INSERT INTO data_neuron_hsnn (id_neuron, kelas_sampel, rantai_ke, neuron_ke, ciri_1, ciri_2, ciri_3, ciri_4, ciri_5, ciri_6, ciri_7, ciri_8) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            
            int id = 1;
            int rantai_ke, neuron_ke;
            for(i=0; i<PelingkupanBWajah.size(); i++){
                PelingkupanB = PelingkupanBWajah.get(i);
                System.out.println("Banyak rantai pada kelas ke - "+i+" : "+PelingkupanB.size());
                for(j=0; j<PelingkupanB.size(); j++){
                    Lingkupan = PelingkupanB.get(j);
                    System.out.println("Banyak neuron pada rantai ke - "+j+" : "+Lingkupan.size());
                    for(k=0; k<Lingkupan.size(); k++){
                        rantai_ke = j;
                        neuron_ke = k;
                        databaseManager.insertDataNeuronHSNN(sql_mW, Lingkupan.get(k), id, rantai_ke, neuron_ke);
                        id = id + 1;
                    }
                    System.out.println();
                }
            }
        }
        
        System.out.println("Simpan model hasil pelatihan is Done");
    }
}
