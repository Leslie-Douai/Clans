/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clans;


//import clans.*;

/**
 *
 * @author humeau  
 */
public class Joueur {
    boolean estHumain;   // vrai humain , faux  ordinateur
    
    public Joueur(String _nom){
        nbVillage=0;   
        estHumain = true;
        nom = _nom;
    }
    
    public String getName() {
        return nom;
    }
    
    public void setName(String _nom){
        nom=_nom;
    }
    
    public void setCouleur(int _couleur){
        couleur=_couleur;
    }
    
    public int getCouleur(){
        return couleur;
    }
    
    public int getNbVillage(){
        return nbVillage;
    }
    
    public boolean isHumain() {
        return estHumain;
    }
    
    private String nom;
    private int couleur;
    private int nbVillage;
    
    public void newVillage(){
        nbVillage++;
    }
    
    public void reset(){
        nbVillage=0;
    }
}
