/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package clans;

import org.newdawn.slick.geom.Polygon;

//import clans.*;

/**
 *
 * @author humeau
 */
public class Terrain implements Cloneable {
    
    public Terrain(Terrain t){
        id= t.id; //Id du terrain
        region= t.region; // region dans laquelle se situe le terrain
        type= t.type; // type du terrain
        vide= t.vide; // terrain vide
        bloque= t.bloque; // terrain bloque (village créé)
        village= t.village;
        nbCabanes= t.nbCabanes; // nbCabanes sur le terrain
        cabanes= (int[])( t.cabanes.clone() ); // tableaux des cabanes présentes sur le terrain
        nbVoisin= t.nbVoisin; //nombre de terrain voisin
        voisins= (int[])( t.voisins.clone() ); // liste des id des terrains voisins
        polygone= null;  // Polygone qui d�limite le terrain.  // TODO : A MODIFER EN PRIVATE
    }
    
    public Terrain(int _id, int _region, String _type, int _nbVoisin, int[] _voisins, Polygon _polygone ){
        id=_id;
        region=_region;
        type=_type;
        vide=false;
        bloque=false;
        village=false;
        nbCabanes=1;
        cabanes=new int[5];
        nbVoisin=_nbVoisin;
        voisins= _voisins.clone();
        polygone = _polygone;
    }
    
     public Terrain(int _id, int _region, String _type, int _nbVoisin, int[] _voisins  ){   // TODO : A SUPPRIMER
         this(_id,_region,_type,_nbVoisin,_voisins,null);
     }
    
    public void print(){
        System.out.println("Terrain " + id + " vide: "+ vide);
        
    }
    
    public int getNbVoisins(){
        return nbVoisin;
    }
    
    public int getVoisin(int _index){
        return voisins[_index];
    }
    
    public int getNbCabane(){
        return nbCabanes;
    }
    
    public int [] getCabanes(){
        return cabanes;
    }
    
    public int getCabanes(int _i){
        return cabanes[_i];
    }
    
    public int[] cloneCabanes(){
        return cabanes.clone();
    }

    public void setCabane(int _i){
        cabanes[_i]++;
    }
    
    public void addCabane(int _i, int _j){
        cabanes[_i]+=_j;
        nbCabanes+=_j;
    }
    
    public void removeCabane(int _i){
        cabanes[_i]--;
        nbCabanes--;
    }
    
    public boolean estVide(){
        return vide;
    }
    
    public void bloquer(){
        bloque=true;
    }
    
    public boolean estBloque(){
        return bloque;
    }
    
    public void debloquer(){
        bloque=false;
    }
    
    public String getType(){
        return type;
    }
    
    public int getRegion() {
        return region;
    }
    
    public int getId() {
        return id;
    }
    
    public void vider(){
        for(int i=0; i<5; i++)
            cabanes[i]=0;
        nbCabanes=0;
        vide=true;
    }
    
    public boolean cinqCouleurs(){
        boolean res=true;
        for(int i=0; i<5; i++)
            if(cabanes[i]==0)
                res=false;
        
        return res;
    }
    
    @Override
    public String toString() {
         String terrain;
           terrain  = getType() + "\t" + getRegion() + "\t" +  getNbVoisins() + " voisins : " ;
            if(estVide())
                terrain += "vide";
            else {                
                for(int j=0; j<5; j++){
                    if(getCabanes(j)>0)
                        terrain +=j + " x" +getCabanes(j);
                    if(j<4)
                        terrain +="\t\t";
                }
                if(estBloque())
                    terrain+="\t\tBLOQUE!!!";
            }
            terrain+="\n";
            return terrain;
    }
    
    public boolean getVillage(){
        return village;
    }
    
    public void setVillage(){
        this.village=true;
    }
    
    @Override
    public Object clone() throws CloneNotSupportedException
    {
        Terrain  t= (Terrain) super.clone();

        t.id= id; //Id du terrain
        t.region= region; // region dans laquelle se situe le terrain
        t.type= type; // type du terrain
        t.vide= vide; // terrain vide
        t.bloque= bloque; // terrain bloque (village créé)
        t.village= village;
        t.nbCabanes= nbCabanes; // nbCabanes sur le terrain
        t.cabanes= cabanes.clone(); // tableaux des cabanes présentes sur le terrain
        t.nbVoisin= nbVoisin; //nombre de terrain voisin
        t.voisins= voisins.clone(); // liste des id des terrains voisins
        t.polygone= null;  // Polygone qui d�limite le terrain.  // TODO : A MODIFER EN PRIVATE

        return t;
    }
            
    private int id; //Id du terrain
    private int region; // region dans laquelle se situe le terrain
    private String type; // type du terrain
    private boolean vide; // terrain vide
    private boolean bloque; // terrain bloque (village créé)
    private boolean village;
    private int nbCabanes; // nbCabanes sur le terrain
    private int[] cabanes; // tableaux des cabanes présentes sur le terrain
    private int nbVoisin; //nombre de terrain voisin
    private int[] voisins; // liste des id des terrains voisins
    public Polygon polygone;  // Polygone qui d�limite le terrain.  // TODO : A MODIFER EN PRIVATE
}
