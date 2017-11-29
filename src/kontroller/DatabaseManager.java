/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kontroller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Sampel_E;

/**
 *
 * @author digitalcreative
 */
public class DatabaseManager {
    private Connection conn = null;
    
    private final String user = "root";
    private final String password = "";
    
    public DatabaseManager(){}
    
    public void set_KoneksiDatabase(){
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/pengenalan_wajah", user, password);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean cekIsiDataMatriksW(String sql){
        boolean isAda = false;
        int cek;
        
        try {            
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            result.next();
            cek = result.getInt(1);
            
            if(cek != 0){
                isAda = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return isAda;
    }
    
    public int jumlahDataMatriksW(String sql){        
        int sum = 0;
        try {            
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            result.next();
            sum = result.getInt(1);
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return sum;
    }
    
    public boolean cekIsiDataNeuronHSNN(String sql){
        boolean isAda = false;
        int cek;
        
        try {            
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            result.next();
            cek = result.getInt(1);
            
            if(cek != 0){
                isAda = true;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return isAda;
    }
    
    public int getJumlahBarisMatriksW(String sql){
        int max = 0;
        
        try {            
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            result.next();
            max = result.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return max;
    }
    
    public int getJumlahDataNeuronHSNN(String sql){
        int max = 0;
        
        try {            
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            result.next();
            max = result.getInt(1);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return max;
    }
    
    public void insertDataMatriksW(String sql, double[] matriksWBaris, int id){
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            
            statement.setInt(1, id);
            statement.setDouble(2, matriksWBaris[0]);
            statement.setDouble(3, matriksWBaris[1]);
            statement.setDouble(4, matriksWBaris[2]);
            statement.setDouble(5, matriksWBaris[3]);
            statement.setDouble(6, matriksWBaris[4]);
            statement.setDouble(7, matriksWBaris[5]);
            statement.setDouble(8, matriksWBaris[6]);
            statement.setDouble(9, matriksWBaris[7]);
            
            
            int rowsInserted = statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void insertDataNeuronHSNN(String sql, Sampel_E sampel , int id, int rantai, int neuron){
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            
            statement.setInt(1, id);
            statement.setString(2, sampel.getNama_sampel());
            statement.setInt(3, rantai);
            statement.setInt(4, neuron);
            statement.setDouble(5, sampel.getCiriSampel()[0]);
            statement.setDouble(6, sampel.getCiriSampel()[1]);
            statement.setDouble(7, sampel.getCiriSampel()[2]);
            statement.setDouble(8, sampel.getCiriSampel()[3]);
            statement.setDouble(9, sampel.getCiriSampel()[4]);
            statement.setDouble(10, sampel.getCiriSampel()[5]);
            statement.setDouble(11, sampel.getCiriSampel()[6]);
            statement.setDouble(12, sampel.getCiriSampel()[7]);
            
            
            int rowsInserted = statement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public double[][] getDataMatriksW(String sql, int baris, int kolom){
        int i;
        double[][] matriksW;
        
        matriksW = new double[baris][kolom];
        try {            
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            i=0;
            while (result.next()){
                matriksW[i][0] = result.getDouble(2);
                matriksW[i][1] = result.getDouble(3);
                matriksW[i][2] = result.getDouble(4);
                matriksW[i][3] = result.getDouble(5);
                matriksW[i][4] = result.getDouble(6);
                matriksW[i][5] = result.getDouble(7);
                matriksW[i][6] = result.getDouble(8);
                matriksW[i][7] = result.getDouble(9);
                i += 1;
            }

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return matriksW;
    }
    
    public ArrayList<ArrayList> getDataNeuronHSNN(String sql){
        int i;
        ArrayList<ArrayList> PelingkupanBWajah;
        ArrayList<ArrayList> PelingkupanB;
        ArrayList<Sampel_E> Lingkupan;
        Sampel_E sampel;
        double[] ciriSampelLingkupan;
        
        PelingkupanBWajah = new ArrayList<>();
        PelingkupanB = new ArrayList<>();
        
        try {            
            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sql);
            
            Lingkupan = new ArrayList<>();
            while (result.next()){
                sampel = new Sampel_E();
                ciriSampelLingkupan = new double[8];
                ciriSampelLingkupan[0] = result.getDouble(5);
                ciriSampelLingkupan[1] = result.getDouble(6);
                ciriSampelLingkupan[2] = result.getDouble(7);
                ciriSampelLingkupan[3] = result.getDouble(8);
                ciriSampelLingkupan[4] = result.getDouble(9);
                ciriSampelLingkupan[5] = result.getDouble(10);
                ciriSampelLingkupan[6] = result.getDouble(11);
                ciriSampelLingkupan[7] = result.getDouble(12);
                
                sampel.setCiriSampel(ciriSampelLingkupan);
                sampel.setNama_sampel(result.getString(2));
                
                Lingkupan.add(sampel);
                if(result.getInt(4) == 10){
                    PelingkupanB.add(Lingkupan);
                    Lingkupan = new ArrayList<>();
                }
                
                int indexLast = Lingkupan.size()-1;
                if(!result.getString(2).equals(Lingkupan.get(indexLast).getNama_sampel())){
                    PelingkupanBWajah.add(PelingkupanB);
                }
            }
            
            PelingkupanBWajah.add(PelingkupanB);

        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return PelingkupanBWajah;
    }
    
    public void deleteDataMatriksW(String sql, int idHapus){
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, idHapus);            
            
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void deleteDataNeuronHSNN(String sql, int idHapus){
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, idHapus);            
            
            statement.execute();
        } catch (SQLException ex) {
            Logger.getLogger(DatabaseManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
