package strats;

import clans.Terrain;
import java.util.Random;
//TEST
// TEST 2
/**
 * Note (Anthony Fleury) : Cette strategie est a modifier et a adapter pour que
 * vous puissiez rendre la votre. Pour la modifier, avec le bouton droit, faite
 * un Refactor, Rename et donnez lui le nom de votre classe StratX avec X votre
 * numero de rendu. Une fois fait, completez CORRECTEMENT les methodes getName
 * et getGroupes. Vous n'aurez ensuite qu'a completer le code de votre propre
 * strategie, a vous de jouer !
 */
/**
 * @author humeau
 */
public class Strat21 implements Strategie {

    Random rand;

    public Strat21() {
        super();
        rand = new Random();
        int compteur;

    }
    Terrain[] plateau;
    int mycolor; 
    int[] colorscore;
    int myscore;
    int compteur = 0;

    @Override
    public int[] mouvement(Terrain[] _plateau, int _myColor, int[] _colorScore, int _myScore, int _opponentScore, int[] _opponentMov, int[] _opponentVillages) {
        int[] res = new int[2];
        plateau = _plateau;
        mycolor = _myColor;
        colorscore = _colorScore;
        myscore = _myScore;
        /////////////////
        // méthode qui va décider quoi faire tant que le premier village n'est pas créé
        // si le nombre de villages créés est 0 et on a un pion en case 19 ou en case 50
        int[] cabanes19 = Tools.cabanes_i(_plateau, 19); // liste des cabanes dans la case 19
        int[] cabanes50 = Tools.cabanes_i(_plateau, 50); // liste des cabanes dans la case 50
        if (Tools.countVillage(_plateau) == 0 && (cabanes19[_myColor] != 0 || cabanes50[_myColor] != 0)) {
            //System.out.println("Condition favorable");
            compteur += 1; // on est en condition favorable
        }
        if (Tools.countVillage(_plateau) != 0) { //on remet à 0 le compteur dès que l'on crée un village ainsi on ne rentre pas dans la boucle qui va suivre une fois le village créé
            compteur = 0;
        }
        if (compteur > 0) {
            if (cabanes19[_myColor] != 0) { // si on a un pion en case 19 on le bouge en 45 ou 46 si c'est possible
                if (Tools.coupValide(_plateau, 19, 45)) {
                    res[0] = 19;
                    res[1] = 45;
                    return res;
                } else if (Tools.coupValide(_plateau, 19, 46)) {
                    res[0] = 19;
                    res[1] = 46;
                    return res;
                }
            }

            if (cabanes50[_myColor] != 0) { // si on a un pion en case 50 on le bouge en 49 ou 46 si c'est possible
                if (Tools.coupValide(_plateau, 50, 49)) {
                    res[0] = 50;
                    res[1] = 49;
                    return res;
                } else if (Tools.coupValide(_plateau, 50, 46)) {
                    res[0] = 50;
                    res[1] = 46;
                    return res;
                }
            }

            int[] distance1 = {45, 47, 49};
            for (int i = 0; i < distance1.length; i++) { // on parcourt les régions à distance 1 de la case 48
                if (Tools.coupValide(_plateau, 46, distance1[i])) { // si une des cases à distance 1 de la case 48 n'est pas vide
                    res[0] = 46;
                    res[1] = distance1[i];
                    return res; // on la vide en case 48
                }
            }
            for (int i = 0; i < distance1.length; i++) { // on parcourt les régions à distance 1 de la case 48
                if (Tools.coupValide(_plateau, distance1[i], 48)) { // si une des cases à distance 1 de la case 48 n'est pas vide
                    res[0] = distance1[i];
                    res[1] = 48;
                    return res; // on la vide en case 48
                }
            }
        }

        ////////////////////
        int nb = Tools.countVillage(_plateau);
        // Méthode décidant de quoi faire de manière générale 
        if (nb >= 1) {
            int max = 0;
            int[] aucasou = {0, 0};
            //int worse=Tools.worseOpp(_plateau, _myColor, _colorScore, _myScore);
            int worse = Tools.suppOpp(_plateau, _myColor, _opponentVillages, _myScore);
            int[] sources = Tools.getSource(_plateau); //récupère la liste des sources
            for (int i = 0; i < sources.length; i++) {
                int[] villages_si = Tools.listeVillagesCreesSi(_plateau, sources[i]);
                
                int[] destinations = Tools.getVoisinsDispo(_plateau, sources[i]);
                for (int j = 0; j < destinations.length; j++) {
                    int[] gains = Tools.evaluerGain(_plateau, sources[i], destinations[j], ordre(villages_si));
                    if (gains[_myColor] >= max) {
                        if (gains[worse] <= gains[_myColor]) {  // si le joueur avec le plus gros score a un plus gros gain que nous on joue pas
                            max = gains[_myColor];
                            res[0] = sources[i];
                            res[1] = destinations[j];
                        }
                        aucasou[0] = sources[i];
                        aucasou[1] = destinations[j];
                    }
                }
            }
            if (res[0] != 0) {
                return res;
            } else {
                return aucasou;
            }

        }

        int nbChoix = Tools.getNbSourceValide(_plateau);                          //on récupère le nb de source valide     
        int src = Tools.getSource(_plateau)[rand.nextInt(nbChoix)];               //on en tire une aléatoirement      
        int nbVoisin = Tools.getNbVoisinDispo(_plateau, src);                       //on récupère le nb de voisins de la source
        int dest = Tools.getVoisinsDispo(_plateau, src)[rand.nextInt(nbVoisin)];    //on en tire un aléatoirement

        //on retourne notre selection
        res[0] = src;
        res[1] = dest;
        return res;
    }

    @Override
    public int[] ordre(int[] _villages) {
        int nbvillage = Tools.countVillage(plateau); // nombre de villages créés
        if (_villages.length>1 && nbvillage<9 && _villages.length<4){
            for(int i=0;i<_villages.length;i++){
            }
            int[][] combinaison_villages = Tools.combinaisons(_villages); // liste de tte les combinaisons pouvant être faites avec les villages
            int[] points = new int[combinaison_villages.length];   // liste des points que rapporte chaque combinaison
            for (int i = 0; i < combinaison_villages.length; i++) { // on parcourt l'ensemble des combinaisons
                int[] combinaison_i = combinaison_villages[i]; // on stock ici la combinaison i
                String[] bonus = new String[combinaison_i.length+4]; // on crée la liste des bonus associés à la combinaison i
                String[] malus = new String[combinaison_i.length+4]; // on crée la liste des malus associés à la combinaison i
                
            
                for (int j = 0; j < combinaison_i.length; j++) { // on parcourt la combinaison i
                    bonus[j] = Tools.bonus[nbvillage + j]; // on remplit ces listes
                    malus[j] = Tools.malus[nbvillage + j];
                    
                }
                
                
             
                for (int j = 0; j < combinaison_i.length; j++) {
         
                    int[] cabanes = Tools.cabanes_i(plateau, combinaison_i[j]); // on récupère la liste des cabanes du village j de la combinaison i
                    
                    int opp = Tools.suppOpp(plateau, mycolor, colorscore, myscore); // on récupère l'ennemi présumè
               
                    int nb_pions_moi = cabanes[mycolor]; // on récupère nos pions
               
                    int nb_pions_ennemi = cabanes[opp]; // on récupère les pions de l'enemi
             
                    if (Tools.type(plateau, combinaison_i[j]).equals(bonus[j])) {
                        if (nb_pions_moi >= 2) {
                            points[i] += 1;
                        }
                    }
                    
                    if (Tools.type(plateau, combinaison_i[j]).equals(malus[i])) {

                        if (nb_pions_moi <= 1 || nb_pions_ennemi >= 2) {
                            points[i] += 1;
                        }
                    }
               
                }
            }
            int max=0;
            int index=0;
            for (int i = 0; i < points.length; i++) {
                if (points[i]>=max){
                    max=points[i];
                    index=i;
                }
            }
            return combinaison_villages[index];
        }
        else {
            Random rand = new Random();
            int a,tmp;
            // on melange le tableau des villages
            for (int i=0; i<_villages.length; i++){
                a=rand.nextInt(_villages.length);
                tmp=_villages[a];
                _villages[a]=_villages[i];
                _villages[i]=tmp;
            }
            //on retourne le tableau mélangé
            return _villages;
        }
        }

    /**
     * Remplissez cette méthode avec le bon format expliqué ci-dessous
     *
     * @return le nom des élèves (sous le format NOM1_NOM2) NOM1 et NOM2 sont
     * uniquement les noms de famille de votre binome
     */
    public String getName() {
        return "Alexandre";
    }

    public String getGroupe() {
        return "stratégie 21";
    }

}
