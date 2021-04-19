package strats;

import clans.Terrain;
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author guillaume
 */
public class Tools {

    static final String[] bonus = {"foret", "foret", "foret", "foret", "montagne", "montagne", "montagne", "champ", "champ", "plaine", "plaine", "tout"};
    static final String[] malus = {"montagne", "montagne", "montagne", "montagne", "plaine", "plaine", "plaine", "foret", "foret", "champ", "champ", "rien"};

    /**
     * 
     * @param plateau Le plateau de jeu
     * @return le nombre de source valide
     */
    public static int getNbSourceValide( Terrain [] plateau ) {
        int res = 0;
        for (int i = 0; i < 60; i++) {
            if (!plateau[i].estBloque() && !plateau[i].estVide() && !plateau[i].getVillage()) {
                res++;
            }
        }
        return res;
    }
    
    /**
     * 
     * @param plateau Le plateau de jeu
     * @return les differentes sources possibles.
     */
    public static int[] getSource( Terrain [] plateau ) {
        int[] res;
        int nbSourceValide = getNbSourceValide(plateau);
        res = new int[nbSourceValide];
        int tmp = 0;
        for (int i = 0; i < 60; i++) {
            if (!plateau[i].estBloque() && !plateau[i].estVide() && !plateau[i].getVillage()) {
                res[tmp] = i;
                tmp++;
            }
        }
        return res;
    }

    /**
     * @param plateau Le plateau de jeu
     * @param _terrain Le numero du terrain
     * @return le nombre de voisins vers lequels on peut aller depuis une source
     */
    public static int getNbVoisinDispo( Terrain [] plateau, int _terrain) {
        int res = 0;
        Terrain tmp;
        for (int i = 0; i < plateau[_terrain].getNbVoisins(); i++) {
            tmp = plateau[plateau[_terrain].getVoisin(i)];
            if (plateau[_terrain].getNbCabane() < 7) {
                if (!tmp.estVide())
                    res++;             
            }
            else {
                if (!tmp.estVide() && tmp.getNbCabane() >= plateau[_terrain].getNbCabane())
                    res++;
            }
        }
        return res;
    }
    
    /**
     * 
     * @param _terrain
     * @return un tableau contenant les voisins vers lequels on peut aller depuis une source
     */
    public static int[] getVoisinsDispo(Terrain [] plateau, int _terrain) {
        int[] res;
        int nbVoisinDispo = getNbVoisinDispo(plateau, _terrain);
        res = new int[nbVoisinDispo];
        Terrain tmp;
        int acc = 0;
        for (int i = 0; i < plateau[_terrain].getNbVoisins(); i++) {
            tmp = plateau[plateau[_terrain].getVoisin(i)];
            if (plateau[_terrain].getNbCabane() < 7) {
                if (!tmp.estVide()) {
                    res[acc] = plateau[_terrain].getVoisin(i);
                    acc++;
                }
            }
            else {
                if (!tmp.estVide() && tmp.getNbCabane() >= plateau[_terrain].getNbCabane()) {
                    res[acc] = plateau[_terrain].getVoisin(i);
                    acc++;
                }
            }
        }
        return res;
    }
    
    
    /**
     * 
     * @param plateau
     * @param _src
     * @return le nombre de villages créés si vous jouez depuis la source "_src"
     */
    public static int nbVillageCreeSi(Terrain [] plateau, int _src){
        int res=0;
        int v;
        for (int i=0; i<plateau[_src].getNbVoisins(); i++){
            v=plateau[_src].getVoisin(i);
            if((!plateau[v].estVide()) && ( getNbVoisinsNonVide(plateau, v) == 1 )){
                res++;
            } 
        }        
        return res;
    }
    
        /**
     * 
     * @param plateau
     * @param _terrain
     * @return le nombre de voisins non vides d'un terrain
     */
    public static int getNbVoisinsNonVide(Terrain[] plateau, int _terrain) {
        int res=0;
        for(int i=0; i< plateau[_terrain].getNbVoisins(); i++){
            if(!plateau[ plateau[_terrain].getVoisin(i) ].estVide())
                res++;
        }
        return res;
    }
    
    /**
     * 
     * @param plateau
     * @param _src
     * @param _dest
     * @param _ordre
     * @return caclcul les scores si vous jouez depuis la source "_src" vers la destination "_dest" avec un ordre "_ordre" de création de villages (si nécessaire)
     */
    public static int[] evaluerGain(Terrain [] plateau, int _src, int _dest, int[] _ordre){
        int[] res= {0, 0, 0, 0, 0};
        int nbVillage= nbVillageCreeSi(plateau, _src);
        int[] tmp;

        if(nbVillage>0){
            //on compte additionne les score pour chaque village
            for(int i=0; i < nbVillage; i++){
                tmp= scoreVillage( plateau, _src, _dest, _ordre[i], i); // décalage dépend de nbvillage
                for (int j=0; j<5; j++){
                    res[j]+=tmp[j];
                }
            }
        }               
        return res;       
    }
    
    /**
     * 
     * @param plateau
     * @return compte le nombre de villagee sur le plateau
     */
    public static int countVillage ( Terrain [] plateau ){
        int nbVillage= 0;
        
        for ( int i= 0 ; i < plateau.length ; ++i ){
            if ( plateau[i].getVillage() ){
                nbVillage+= 1;
            }
        }
        return nbVillage;
    }
    
    /**
     * 
     * @param _src
     * @param _dest
     * @param _village
     * @param decalage
     * @return calcul les points gagnés par le village "_village" créé avec un décalage "decalage" si vous jouez depuis la source "_src" vers la destination "_dest"
     */
    public static int[] scoreVillage(Terrain [] plateau, int _src, int _dest, int _village, int decalage){
        int[] res=new int[5];
        int[] cabanes;
        int points=0;
        int villageCourant= countVillage(plateau);

        //Test destruction
        if( (villageCourant + decalage) < malus.length ){
            if( !plateau[_village].getType().equals( malus[villageCourant + decalage] )){
                cabanes=plateau[_village].getCabanes().clone();
                if(_dest==_village){
                    for(int i=0; i<5; i++){
                        cabanes[i]+=plateau[_src].getCabanes(i);
                    }
                }
                if( cabanes[0] > 0 &&  cabanes[1] > 0 && cabanes[2] > 0 
                        && cabanes[3] > 0 && cabanes[4] > 0 ){
                    for(int i=0; i<5; i++){
                        if( cabanes[i] == 1 )
                            cabanes[i]= 0;
                    }
                }
                for( int i=0; i<5; i++ )
                    points+=cabanes[i];
                
                if(plateau[_village].getType().equals(bonus[villageCourant + decalage]))
                    points+= age( villageCourant+decalage );
                
                for(int i=0; i<5; i++)
                    if(cabanes[i]>0)
                        res[i]=points;
            }
        }
        return res;
    }

    /**
     * 
     * @param _nb
     * @return l'age dans lequel on se situerai apres la creation de "_nb" villages
     */
    public static int age(int _nb){
        int res = 1;
        if (_nb > 10) {
            res = 5;
        } else if (_nb > 8) {
            res = 4;
        } else if (_nb > 6) {
            res = 3;
        } else if (_nb > 3) {
            res = 2;
        }
        return res;
    }

    /**
     * 
     * @param plateau
     * @param _src
     * @return le tableau des villages créés si vous jouez depuis la source "_src"
     */
    public static int[] listeVillagesCreesSi( Terrain [] plateau, int _src){
        int[] res= new int[nbVillageCreeSi(plateau, _src)];
        int v;
        int cpt=0;
        for (int i=0; i<plateau[_src].getNbVoisins(); i++){
            v=plateau[_src].getVoisin(i);
            if( (!plateau[v].estVide()) && (getNbVoisinsNonVide(plateau, v)==1) ){
                res[cpt]=v;
                cpt++;
            } 
        }        
        return res;
    }
    
    ///////////////////////////////////////////////////////////////////////////////
    public static int[] cabanes_i(Terrain[] plateau, int i){ // récupére la liste des cabanes dans la case i
        return plateau[i].getCabanes();
    }
    
    public static boolean est_vide(Terrain[] plateau, int i){ // regarde si la case i est vide
        return plateau[i].estVide();
    }
    
    
   public static boolean est_village(Terrain[] plateau, int i){ // regarde si la case i est vide
        return plateau[i].getVillage();
    }
   
   
   public static boolean estVoisinDe(Terrain[] plateau, int index1,int index2){
    if (index1==index2){
        return false;
    }
    int nbvoisins=plateau[index1].getNbVoisins();// on compte les voisins du territoire index1
    for (int i=0;i<nbvoisins;i++){ // on parcourt les voisins du territoire index1
        int idVoisin=plateau[index1].getVoisin(i);
        if(idVoisin==index2){
            return true;
        }
    }
    return false;
    }
   
    public static boolean coupValide(Terrain[] plateau,int _source, int _dest) {
        
        if(estVoisinDe(plateau,_source,_dest)){
            if(plateau[_source].estVide()==false){
                if(plateau[_dest].estVide()==false){
                    if(plateau[_source].estBloque()){
                        if(plateau[_dest].estBloque()){
                            if(plateau[_source].getNbCabane()>=plateau[_dest].getNbCabane()){
                                return true;
                            }
                        }                  
                    }
                    else{
                        return true;
                    }
                }  
            }
        }  
    
    return false;
    }
    
    public static String type(Terrain[] plateau, int i){
        return plateau[i].getType();
    }
    
    
    public int nbVillagePossible(Terrain [] _plateau){
        int nbChoix= getNbSourceValide(_plateau);                          //on récupère le nb de source valide
        int[] listeSource= getSource(_plateau);                           //on récupère la liste de source valide
        int i,res = 0;

        for(i=0;i<nbChoix;i++){                           //on compte le nombre de village créable ce tour ci
                   //on évite de faire tourner l'algorithme si ça ne créé pas de village
                res += Tools.nbVillageCreeSi(_plateau, listeSource[i]);

        }
    return res;
}

    /**
     * @param comme mouvement
     * @return return l'index de l'adversaire le plus fort en points
     */
    public static int worseOpp (Terrain [] _plateau, int _myColor, int [] _colorScore, int _myScore){
        int i,res;
        res = (_myColor+1)%5;
        for (i=0;i<4;i++){
            if( _myColor!=i){
                if(_colorScore[i]>= _colorScore[res]){
                    res = i;
                }
            }
        }

        return res;
    }
    
    public static int suppOpp (Terrain[] _plateau,int _myColor, int [] _opponentVillages,int _myScore){
        int[] score = new int[5];
        int i,j;
        for(i=0;i<_opponentVillages.length;i++){
            for(j=0;j<5;j++){
                if(_plateau[_opponentVillages[i]].getCabanes(j)>0){
                score[j] += _plateau[_opponentVillages[i]].getNbCabane();
                }
            }

        }

        return worseOpp ( _plateau,_myColor,score,_myScore);
    }
    
    public static int[] getSrcDispo(Terrain[] plateau) {
        
        int[] src = getSource(plateau);
        int compteur=0;
        int index=0;
       
        for (int i = 0; i < src.length; i++) {
            if (getNbVoisinsNonVide(plateau, src[i]) == 2) {
                compteur+=1;
            }
        }
        int n=src.length-compteur;
        int[] res = new int[n];
        for (int i = 0; i < src.length; i++) {
            if (getNbVoisinsNonVide(plateau, src[i]) == 2) {
                res[index]=src[i];
                index+=1;
            }
        }
        
      
        return res;
    }
    
     public static int fact (int n) {
        if (n==0) return(1);
        else return(n*fact(n-1));
    }
    
    public static int[][] combinaisons(int[] L){
        int n=L.length;
        int compteur=0;
        int[][] tableau = new int[fact(n)][n];
        Random rand = new Random();
        while (compteur<fact(n)){
            int [] t = L.clone();
            int a,tmp;
            // on melange le tableau des villages
            for (int i=0; i<t.length; i++){
                a=rand.nextInt(t.length);
                tmp=t[a];
                t[a]=t[i];
                t[i]=tmp;
            }
            boolean pas_dedans=true;
            for(int i = 0 ; i<tableau.length;i++){
                if(Arrays.equals(t, tableau[i])){
                    pas_dedans=false;
                    }
            }   
            if (pas_dedans){
                tableau[compteur]=t;
                compteur=compteur+1;
            }
            
        }
        return tableau;
    }
    
    public static boolean cinqCouleurs(int[] _cabanes){
        boolean res=true;
        for(int i=0; i<5; i++)
            if(_cabanes[i]==0)
                res=false;      
        return res;
    }
    
    public static int [] destru(Terrain[] _plateau, int _myColor, int[] _colorScore, int _myScore, int _opponentScore, int[] _opponentMov, int[] _opponentVillages, int nb){
        boolean present = false;
        int t=-1;
        int max = -1;
        int[] imax ={ -1,-1};
        
        int[] srcDisp = getSource( _plateau);
        for (int i =0;i<srcDisp.length;i++){
            int [] villagecree = listeVillagesCreesSi( _plateau,srcDisp[i]);
            for (int j = 0;j < villagecree.length;j++){
                if(getNbVoisinDispo(_plateau,villagecree[j]) == 1 && malus[nb]==_plateau[villagecree[j]].getType()){
                    if (max == -1){
                        t =0;
                        max = pnts_pot(_plateau,srcDisp[i],villagecree[j]);
                        imax[0] = srcDisp[i];
                        imax[1] = villagecree[j];
                        
                        present =(!(_plateau[villagecree[j]].getCabanes().clone()[_myColor]>0) && !( _plateau[srcDisp[i]].getCabanes().clone()[_myColor]>0 ));
                                
                    }else{
                        boolean present_tempo = (!(_plateau[villagecree[j]].getCabanes().clone()[_myColor]>0) && !( _plateau[srcDisp[i]].getCabanes().clone()[_myColor]>0 ));
                        
                        if (present == true && present_tempo == false && t == 0){
                            imax[0] = srcDisp[i];
                            imax[1] = villagecree[j]; 
                            present = present_tempo;
                        }else{
                            if (max < pnts_pot(_plateau,srcDisp[i],villagecree[j])){
                                imax[0] = srcDisp[i];
                                imax[1] = villagecree[j]; 
                                present = present_tempo;
                            }
                        }
                    }
                    
                   
                }
            }
        }
        
        
        return imax;
        
    }
    
    public static int pnts_pot(Terrain[] _plateau,int src,int dest){
        int res = -1;
        int[] cabanes =_plateau[dest].getCabanes().clone();
        for (int i =0; i <5;i++){
            cabanes[i] += _plateau[src].getCabanes().clone()[i];
        }
        
        if (cinqCouleurs(cabanes)){
            for (int i =0; i <5;i++){
                if (cabanes[i] >1){
                    res+=cabanes[i];
                }                
            }
        }else {
            for (int i =0; i <5;i++){
                if (cabanes[i] >1){
                    res+=cabanes[i];
                }                
            }
        }
        return res;
    }
    
    public int[] nbVillageCreable(Terrain[] _plateau){
        int[] res = {-1,-1,0};
        
        int[] srcDisp = getSource( _plateau);
        for (int i =0;i<srcDisp.length;i++){
            if(listeVillagesCreesSi( _plateau,srcDisp[i]).length > res[0]){
                res[0] = listeVillagesCreesSi( _plateau,srcDisp[i]).length;
                res[1] = srcDisp[i];
            }
        }
        
        for (int i =0;i<srcDisp.length;i++){
            if(listeVillagesCreesSi( _plateau,srcDisp[i]).length == res[0]){
                res[2] ++;
                
            }
        }
        
        
            
        return res;
    }        
    
    public int[] finalCoup(Terrain[] _plateau, int _myColor, int[] _colorScore, int _myScore, int _opponentScore, int[] _opponentMov, int[] _opponentVillages,int nb){
        int res[] = {-1,-1};
            int[] nbCreable = nbVillageCreable(_plateau);
            if (nb + nbCreable[0] > 11){
                
                    int[] sources = new int[nbCreable[2]];
                    int k=0;
                    int[] srcDisp = getSource( _plateau);
                    for (int i =0;i<srcDisp.length;i++){
                        if(listeVillagesCreesSi( _plateau,srcDisp[i]).length == nbCreable[0]){
                            sources[k] = srcDisp[i];
                            k ++;
                        }
                    }
                    
                    int max = 0; // variable permettant de stocker le nombre de points maximal que pourrait rapporter un potentiel mouvement
                    for (int i = 0; i < sources.length; i++) {
                        int[] villages_si = Tools.listeVillagesCreesSi(_plateau, sources[i]);
                        if (villages_si.length != 0) {
                            int[] destinations = Tools.getVoisinsDispo(_plateau, sources[i]);
                            for (int j = 0; j < destinations.length; j++) {
                                int[] gains = Tools.evaluerGain(_plateau, sources[i], destinations[j],ordre(villages_si)); // on évalue les potentiels gains de chaque couleur en jouant depuis la source i vers la destination j en suivant un ordre prècis dans le cas où on créerait plusieurs villages simultanément
                                if (gains[_myColor] >= max) {
                                    int suppOpp = Tools.suppOpp(_plateau, _myColor, _opponentVillages, _myScore);
                                    if (gains[suppOpp] <= gains[_myColor]) {  // si le joueur que l'on suppose être notre ennemi gagne plus de points que nous sur ce coup là que nous on joue pas
                                        max = gains[_myColor];
                                        res[0] = sources[i];
                                        res[1] = destinations[j];
                                    }
                                }
                            }
                            if (Tools.coupValide(_plateau, res[0], res[1])) {
                                return res;
                            }
                        }
                    }
                }
            
             return res;
            }
        
        
        }
        
    


