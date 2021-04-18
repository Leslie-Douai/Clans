package strats;

import clans.Terrain;
import java.util.Random;

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
    }
    // on recr�e certaines variables pour pouvoir les utliser dans ordre
    Terrain[] plateau;
    int mycolor;
    int[] colorscore;
    int myscore;

    @Override
    public int[] mouvement(Terrain[] _plateau, int _myColor, int[] _colorScore, int _myScore, int _opponentScore, int[] _opponentMov, int[] _opponentVillages) {
        int[] res = new int[2]; //tableau que l'on va retourner en sortie de mouvement 
        // on stock les informations de certaines variables pour pouvoir les utliser dans l'ordre
        plateau = _plateau;
        mycolor = _myColor;
        colorscore = _colorScore;
        myscore = _myScore;
        int nb = Tools.countVillage(_plateau); // nombre de villages cr��s au moment du mouvement
        String bonus = Tools.bonus[nb]; //terrain qui rapporte un bonus au moment du mouvement
        String malus = Tools.malus[nb]; //terrain qui rapporte un malus au moment du mouvement
        int suppOpp = Tools.suppOpp(_plateau, _myColor, _opponentVillages, _myScore); // couleur de l'adversaire suppos�
        int[] sources = Tools.getSource(_plateau); //liste des sources disponibles
        int max = 0; // variable permettant de stocker le nombre de points maximal que pourrait rapporter un potentiel mouvement
        for (int i = 0; i < sources.length; i++) {
            int[] villages_si = Tools.listeVillagesCreesSi(_plateau, sources[i]);
            if (villages_si.length != 0) {
                int[] destinations = Tools.getVoisinsDispo(_plateau, sources[i]);
                for (int j = 0; j < destinations.length; j++) {
                    int[] gains = Tools.evaluerGain(_plateau, sources[i], destinations[j], ordre(villages_si)); // on �value les potentiels gains de chaque couleur en jouant depuis la source i vers la destination j en suivant un ordre pr�cis dans le cas o� on cr�erait plusieurs villages simultan�ment
                    if (gains[_myColor] >= max) {
                        if (gains[suppOpp] <= gains[_myColor]) {  // si le joueur que l'on suppose �tre notre ennemi gagne plus de points que nous sur ce coup l� que nous on joue pas
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
        
        for (int i = 0; i < sources.length; i++) {
            if ((Tools.cabanes_i(_plateau, sources[i])[_myColor] == 2) && (Tools.type(_plateau, sources[i]).equals(bonus))) { //On regarde si on a au moins 2 pions dans un territoire favorable
                int[] vois1 = Tools.getVoisinsDispo(_plateau, sources[i]);
                for (int j = 0; j < vois1.length; j++) {
                    if (Tools.appartient(_plateau, suppOpp, vois1[j])) { // si notre adversaire suppos� est voisin de ce territoire, on va essayer de l'�loigner de ce territoire pour qu'il ne marque pas de points
                        int n = Tools.getNbVoisinDispo(_plateau, vois1[j]);
                        if (n > 0) {
                            int[] vois2 = Tools.getVoisinsDispo(_plateau, vois1[j]);
                            res[0] = vois1[j];
                            res[1] = vois2[0];
                            if (Tools.coupValide(_plateau, res[0], res[1])) {
                                return res;
                            }

                        }
                    } else { //sinon on essaye de cr�er un village en rentrant les voisins dans ce territoire et ainsi on marque plus de points car il y a plus de cabanes
                        res[0] = vois1[j];
                        res[1] = sources[i];
                        if (Tools.coupValide(_plateau, res[0], res[1])) {
                            return res;
                        }

                    }
                }
            }
        }
        
        for (int i = 0; i < sources.length; i++) {
            if ((Tools.cabanes_i(_plateau, sources[i])[_myColor] == 2) && (Tools.type(_plateau, sources[i]).equals(bonus))) { //On regarde si on a au moins 2 pions dans un territoire favorable
                int[] vois1 = Tools.getVoisinsDispo(_plateau, sources[i]);
                for (int j = 0; j < vois1.length; j++) {
                    if (Tools.appartient(_plateau, suppOpp, vois1[j])) { // si notre adversaire suppos� est voisin de ce territoire, on va essayer de l'�loigner de ce territoire pour qu'il ne marque pas de points
                        int n = Tools.getNbVoisinDispo(_plateau, vois1[j]);
                        if (n > 0) {
                            int[] vois2 = Tools.getVoisinsDispo(_plateau, vois1[j]);
                            res[0] = vois1[j];
                            res[1] = vois2[0];
                            if (Tools.coupValide(_plateau, res[0], res[1])) {
                                return res;
                            }

                        }
                    } else { //sinon on essaye de cr�er un village en rentrant les voisins dans ce territoire et ainsi on marque plus de points car il y a plus de cabanes
                        res[0] = vois1[j];
                        res[1] = sources[i];
                        if (Tools.coupValide(_plateau, res[0], res[1])) {
                            return res;
                        }

                    }
                }
            }
        }
        
        
        

        // si jamais on est pas dans les conditions pr�c�dentes on va jouer un coup al�atoire
        
        int nbChoix = Tools.getNbSourceValide(_plateau);                          //on r�cup�re le nb de source valide     
        int src = Tools.getSource(_plateau)[rand.nextInt(nbChoix)];               //on en tire une al�atoirement      
        int nbVoisin = Tools.getNbVoisinDispo(_plateau, src);                       //on r�cup�re le nb de voisins de la source
        int dest = Tools.getVoisinsDispo(_plateau, src)[rand.nextInt(nbVoisin)];    //on en tire un al�atoirement

        //on retourne notre selection
        res[0] = src;
        res[1] = dest;
        return res;
    }

    @Override
    public int[] ordre(int[] _villages) {

        /*
        int nbvillage = Tools.countVillage(plateau); // nombre de villages cr��s
        if (_villages.length>1 && nbvillage<9 ){
            int[][] combinaison_villages = Tools.combinaisons(_villages); // liste de tte les combinaisons pouvant �tre faites avec les villages
        
        int nbvillage = Tools.countVillage(plateau); // nombre de villages cr��s
        if (_villages.length>1 && nbvillage<9 ){
                String[] malus = new String[combinaison_i.length+4]; // on cr�e la liste des malus associ�s � la combinaison i
                
            
                for (int j = 0; j < combinaison_i.length; j++) { // on parcourt la combinaison i
                    malus[j] = Tools.malus[nbvillage + j];
                    
                }
                for (int j = 0; j < combinaison_i.length; j++) {
                    int[] cabanes = Tools.cabanes_i(plateau, combinaison_i[j]); // on r�cup�re la liste des cabanes du village j de la combinaison i 
                    int opp = Tools.suppOpp(plateau, mycolor, colorscore, myscore); // on r�cup�re l'ennemi pr�sum�
                    int nb_pions_moi = cabanes[mycolor]; // on r�cup�re nos pions
                    int nb_pions_ennemi = cabanes[opp]; // on r�cup�re les pions de l'enemi
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
         */

         

        Random rand = new Random();
        int a, tmp;
        // on melange le tableau des villages
        for (int i = 0; i < _villages.length; i++) {
            a = rand.nextInt(_villages.length);
            tmp = _villages[a];
            _villages[a] = _villages[i];
            _villages[i] = tmp;
        

        

        //on retourne le tableau m�lang�
        return _villages;
        }
    }


    public String getName() {
        return "Alexandre";
    }

    public String getGroupe() {
        return "stratégie 21";
    }

}
