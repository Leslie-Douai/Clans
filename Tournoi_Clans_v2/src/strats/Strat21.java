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
        int nb = Tools.countVillage(_plateau);
        String bonus = Tools.bonus[nb];
        String malus = Tools.malus[nb];

        int worse = Tools.suppOpp(_plateau, _myColor, _opponentVillages, _myScore);
        int[] sources = Tools.getSource(_plateau); //récupère la liste des sources 
        /*
        int max = 0;
        for (int i = 0; i < sources.length; i++) {
            int[] villages_si = Tools.listeVillagesCreesSi(_plateau, sources[i]);
            if (villages_si.length != 0) {
                int[] destinations = Tools.getVoisinsDispo(_plateau, sources[i]);
                for (int j = 0; j < destinations.length; j++) {
                    int[] gains = Tools.evaluerGain(_plateau, sources[i], destinations[j], ordre(villages_si));
                    if (gains[_myColor] >= max) {
                        if (gains[worse] <= gains[_myColor]) {  // si le joueur avec le plus gros score a un plus gros gain que nous on joue pas
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

            if ((Tools.cabanes_i(_plateau, i)[_myColor] == 2) && (Tools.type(_plateau, sources[i]).equals(bonus))) { //On isole un terrain où on est bien 
                int[] vois1 = Tools.getVoisinsDispo(_plateau, i);
                for (int j = 0; j < vois1.length; j++) {
                    int n = Tools.getNbVoisinDispo(_plateau, vois1[j]);
                    if (n > 0) {
                        int[] vois2 = Tools.getVoisinsDispo(_plateau, vois1[j]);
                        res[0] = vois1[j];
                        res[1] = vois2[0];
                    }
                }
            }
        }
        if (Tools.coupValide(_plateau, res[0], res[1])) {
            return res;
        }
        */
        
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
        Random rand = new Random();
        int a, tmp;
        // on melange le tableau des villages
        for (int i = 0; i < _villages.length; i++) {
            a = rand.nextInt(_villages.length);
            tmp = _villages[a];
            _villages[a] = _villages[i];
            _villages[i] = tmp;
        }
        //on retourne le tableau mélangé
        return _villages;
    }
    //}

    public String getName() {
        return "Alexandre";
    }

    public String getGroupe() {
        return "stratégie 21";
    }

}
