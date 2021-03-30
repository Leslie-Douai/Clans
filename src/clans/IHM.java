/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package clans;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Animation;
//import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Polygon;
import org.newdawn.slick.Image;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;


/**
 *
 * @author franck.tempet
 */
public class IHM extends BasicGame {

    GameContainer container;
   
    
    boolean activePauseEspace;       // Si vrai alors il faut appuyer sur la touche espace pour changer de joueur
    int     tempsEntre2Coups;        // Temps en miliseconde entre 2 coups. S'applique si activePauseEspace est faux. Utilisé lorsque 2 stratégies s'afrrontent
    
    // Graphique du jeu
    Image cabanes[], epoque, map;
    SpriteSheet spriteSheetExplosion;
    Animation animExplosion;
    
    Jeu jeu;
    Terrain terrainSource = null, terrainCible = null;      // Coup joué par le joueur
 
    int largeurCabanePx =16, hauteurCabanePx=32 ;                                               // Hauteur et largeur d'une cabane en pixels.
    int[] emplacementEpoque = {235, 270, 304, 338, 395, 431, 466, 525, 565, 638, 678, 749};     // ordonnee du point rouge représetant l'epoque courante
    int[] centreTerrain = {43, 37, 201, 32, 103, 93, 38, 171, 169, 163, 302, 51, 436, 36, 562, 38, 674, 64, 487, 124, 775, 25, 941, 38, 861, 86, 804, 138, 946, 152, 72, 253, 37, 330, 156, 374, 28, 414, 64, 489, 267, 222, 446, 210, 351, 274, 271, 338, 415, 330, 572, 208, 744, 199, 653, 272, 541, 306, 736, 328, 942, 245, 833, 370, 945, 349, 941, 435, 873, 502, 285, 448, 418, 428, 330, 519, 239, 568, 434, 553, 562, 421, 721, 418, 631, 472, 540, 527, 701, 529, 29, 586, 164, 588, 102, 650, 39, 726, 190, 725, 293, 702, 510, 657, 661, 674, 419, 735, 584, 734, 764, 609, 927, 579, 864, 663, 772, 717, 961, 718};   
    private UnicodeFont fontScore, fontJoueurVillage, fontNumeroVillage,fontNombreHutte;        // Font utilisees pour afficher du texte
    private int terrainSourceOrdi = -1, terrainCibleOrdi = -1;                                  // Represente le coup joué par l'ordinateur ( terrain source et cible )

    /* Variables utilisées pour la détermination de l'ordre de création des villages */
    private boolean choixOrdreVillage = false;      // Vrai si l'utilisateur doit definir l'ordre de création des villages
    int[] villages;                                 // Liste des villages créés
    int[] ordreVillages;                            // Ordre de création des villages défini par le joueur
    int nbChoixEffectue = 0;                        // Nombre de villages numérotés
    int chronoExplosion = 0 , chronoCouleurJoueur =0;   // Utilisé pour afficher l'explosion et la couleur du joueur pendant quelques secondes.
    
    // Liste des couleurs
    Color couleurTerrainSource = new Color(255, 0, 0, .5f);          // Couleur du terrain source du coup
    Color couleurTerrainDest = new Color(0, 145, 120, .5f);          // Couleur du terrain destination du coup
    Color couleurTerrainSelectionne = new Color(128, 255, 255, .4f); // Couleur du terrain selectionne
    Color couleurTerrainVoisin = new Color(255, 0, 255, .3f);        // Couleur des terrains voisins
    Color couleurOrdreVillage = new Color(0, 100, 150, .3f);         // Couleur des villages lorsqu'il faut définir l'ordre de création
    Color couleurContourTerrain = new Color(255, 0, 0, .4f);         // Couleur du contour du terrain
    Color couleurContourVillage = new Color(255, 255, 255, .4f);     // Couleur du contour d'un village
    Color couleurVillage = new Color(0, 255, 0, .5f);                // Couleur d'un village
    Color couleurTerrainBloque = new Color(255, 0, 0, .5f);          // Couleur d'un terrain bloque
    
                                  //bleu                  jaune                    vert                   rouge                  noir
    Color[] couleurJetons = {new Color(0, 115, 255) , new Color(255, 255, 0) , new Color(0, 230, 0) , new Color(255, 0, 0), new Color(0, 0, 0) };   // Couleur du disque qui représente un jeton.
    Color[] couleurNbJetons = {new Color(255,255 , 255) , new Color(0,0 , 0), new Color(255,255 , 255) , new Color(255,255 , 255), new Color(255,255 , 255) };  // Couleur du chiffre à l'intérieur du disque
    
    // Nombre de huttes que l'on peut placer sur chaque terrain
    int nbHutteMax[] = new int[60]; 
    
    boolean pause = false;                          // Le jeu est en pause en appuyant sur la touche <espace>
    boolean afficheCouleurJoueurActif = false;      // Si vrai alors la couleur du joueur actif s'affiche à gauche de son nom pendant quelques secondes.
      


    /*
     * Constructeur de la classe
     */
    public IHM(Joueur _j1, Joueur _j2, boolean _activePauseEspace, int _tempsEntre2Coups) {
        super("Clans");
        
        activePauseEspace=_activePauseEspace;
        tempsEntre2Coups=_tempsEntre2Coups;
                      
        jeu = new Jeu();

        jeu.initPlateau();
        jeu.placementCabane();
        jeu.initJoueur(_j1, _j2);
        
        startNewGame();
       
    }

    /*
     * Initialise le plateau de jeu ainsi que les variables afin de démarrer une nouvelle partie.
     */
    public void startNewGame( ) {
        jeu.initPlateau();
        jeu.placementCabane();
        jeu.reset();
        
        /* Initialise toutes les variables */
        terrainSource = null; terrainCible = null; 
        terrainSourceOrdi = -1; terrainCibleOrdi = -1; 
        choixOrdreVillage = false;
        nbChoixEffectue = 0; 
        chronoExplosion = 0; chronoCouleurJoueur =0;
        pause = false;    
        afficheCouleurJoueurActif = false; 
    }
    
    @Override
    /**
     * Initialisation du Jeu
     * Création des fonts
     * Chargement des images.
     * Détermine le nombre de cabanes qu'il est possible de placer sur chaque terrain.
     */
    public void init(GameContainer _container) throws SlickException {

        // Creation des fonts
        this.fontScore = new UnicodeFont(new java.awt.Font("CourierNew", 1, 30));
        this.fontScore.getEffects().add(new ColorEffect(java.awt.Color.BLACK));
        this.fontScore.addAsciiGlyphs();
        this.fontScore.loadGlyphs();
        
        this.fontJoueurVillage = new UnicodeFont(new java.awt.Font("CourierNew", 1, 18));
        this.fontJoueurVillage.getEffects().add(new ColorEffect(java.awt.Color.RED));
        this.fontJoueurVillage.addAsciiGlyphs();
        this.fontJoueurVillage.loadGlyphs();

        this.fontNumeroVillage = new UnicodeFont(new java.awt.Font("CourierNew", 1, 40));
        this.fontNumeroVillage.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        this.fontNumeroVillage.addAsciiGlyphs();
        this.fontNumeroVillage.loadGlyphs();
        
        this.fontNombreHutte = new UnicodeFont(new java.awt.Font("CourierNew", 1, 16));
        this.fontNombreHutte.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        this.fontNombreHutte.addAsciiGlyphs();
        this.fontNombreHutte.loadGlyphs();        

        // Desactive l'affichage du nombre de FPS
        _container.setShowFPS(false);
        this.container = _container;
        
        // Charge le plateau du jeu
        this.map = new Image("ressources/plateau_clans.png");


        spriteSheetExplosion = new SpriteSheet("ressources/explosion.png", 64, 64);  

        animExplosion = new Animation();
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                animExplosion.addFrame(spriteSheetExplosion.getSprite(i, j), 100);
            }
        }

        // Charge les cabanes
        cabanes = new Image[5];
        cabanes[0] = new Image("ressources/hutte_bleu.png");
        cabanes[1] = new Image("ressources/hutte_jaune.png");        
        cabanes[2] = new Image("ressources/hutte_verte.png");
        cabanes[3] = new Image("ressources/hutte_rouge.png");
        cabanes[4] = new Image("ressources/hutte_noire.png");
        epoque = new Image("ressources/epoque.png");
             
       // Pour chaque terrain , détermine le nombre de cabanes qu'il est possible de placer.
        for (int i = 0 ; i < jeu.plateau.length ; i++ ) {
            nbHutteMax[i]  = placeCabanesMax( i);
        }        
    }

    // Methode pour remplir chaque terrain du nombre MAX de huttes possibles
    /**
     * 
     * @param _numTerrain  Numero du terrain pour lequel on souhaite connaitre le nombre de cabane MAX.
     * 
     * Cette Methode détermine pour le terrain _numTerrain combien de cabanes il est possible de placer.
     */
    public int placeCabanesMax(int _numTerrain ) {
        return 9;  
    }

    
    /**
     * Affiche une cabane ou le nombre de cabanes du joueur aux coordonnées x,y  
     * On affiche le nombre de cabanes s'il y en a de trop.
     * 
     * @param _numJoueur  Joueur pour lequel on affiche les cabanes
     * @param _terrain    N° du terrain sur lequel on affiche les cabanes
     * @param _x          _x,_y emplacment de la cabane à l'écran    
     * @param _y
     * @param _g          objet graphique pour dessiner ou afficher des images à l'écran.
     */
    public void afficheHutteJoueur( int _numJoueur , Terrain _terrain , int _x , int _y , Graphics _g) {
        // Si trop de huttes sur le terrain on affiche le nombre de hutte
        if ( _terrain.getNbCabane()  > nbHutteMax[_terrain.getId()] ) {
            _g.setColor(couleurJetons[_numJoueur]);
            _g.fillOval(_x, _y, 24, 24);
            _g.setColor(new Color(0,0,0));
            _g.drawOval(_x, _y, 24, 24);
             _g.setFont(this.fontNombreHutte);
             _g.setColor(couleurNbJetons[_numJoueur]);
            _g.drawString(_terrain.getCabanes(_numJoueur)+"", _x+7, _y);
        }
        else {
            cabanes[_numJoueur].draw(_x, _y);
        }
    }
    

    /**
     * Retourne le nombre de cabanes placées sur le plateau pour le joueur _numJoueur et pour le terrain _terrain 
     * 
     * @param _numJoueur 
     * @param _terrain 
     * @return  Retourne 1 s'il y a assez de place sur le terrain sinon le nombre de cabanes du joueur sur ce terrain
     */
    public int nombreDeHuttesPlaces( int _numJoueur  , Terrain _terrain ) {
        if ( _terrain.getNbCabane()  > nbHutteMax[_terrain.getId()] ) {
            return _terrain.getCabanes(_numJoueur);
        }
        else
            return 1;
    }
    
    
    /*
     * Algorithme qui positionne les cabanes sur chaque terrain
     */
    public void placeCabanes(Terrain _terrain, Graphics _g) {

        Polygon p = _terrain.polygone;

        int nbCabanes = _terrain.getNbCabane();

        if (nbCabanes == 0)    return;

        int numJoueur = 0;
        int nbCabaneJoueur = _terrain.getCabanes(numJoueur);
                
        if ( _terrain.getNbCabane()  > nbHutteMax[_terrain.getId()] ) {
            largeurCabanePx = 24; hauteurCabanePx = 24;
        }
        else  {
            largeurCabanePx = 16; hauteurCabanePx = 32;
        }
        
        for (int y = (int) p.getMinY(); y <= (int) p.getMaxY() - hauteurCabanePx; y += largeurCabanePx) {
            if (nbCabanes <= 0)    break;

            for (int x = (int) p.getMinX(); x <= (int) p.getMaxX() - largeurCabanePx; x += largeurCabanePx) {
                if (p.contains(x+1, y+1)
                        && p.contains(x + largeurCabanePx-1, y+1)
                        && p.contains(x+1, y + hauteurCabanePx-1)
                        && p.contains(x + largeurCabanePx-1, y + hauteurCabanePx-1)) {

                    while (nbCabaneJoueur <= 0 && numJoueur < 4) {
                        numJoueur++;
                        nbCabaneJoueur = _terrain.getCabanes(numJoueur);
                    }

                    if (numJoueur >= 5 || nbCabaneJoueur <= 0) {
                        return;
                    }

                    afficheHutteJoueur( numJoueur , _terrain , x , y , _g);
                    nbCabaneJoueur -= nombreDeHuttesPlaces(numJoueur,_terrain);
                    nbCabanes -= nombreDeHuttesPlaces(numJoueur,_terrain);

                }
            }
        }
    }

    
    /**
     * C'est cette méthode qui affiche le jeu à l'écran.
     * Elle est appelée régulièrement par JAVA pour actualiser l'écran. ( plusieurs fois par seconde )
     */
    @Override
    public void render(GameContainer _container, Graphics _g) throws SlickException {

        this.map.draw(0, 0);


         /* Mise en évidence des terrains voisins du terrain source */
        if (terrainSource != null) {                    // Le joueur  a selectionné un terrain source 

            if ( jeu.getNbVoisinDispo(terrainSource.getId()) > 0 ) {   // Si on peut deplacer les huttes 
                _g.setColor(couleurTerrainSelectionne);
                _g.fill(terrainSource.polygone);

                int [] destinationPossible = jeu.getVoisinsDispo(terrainSource.getId());  // Liste des coups possibles

                for (int v = 0; v < destinationPossible.length ; v++) {
                        _g.setColor(couleurTerrainVoisin);
                        _g.fill(jeu.plateau[ destinationPossible[v] ].polygone);
                        _g.setColor(couleurContourTerrain);
                        _g.setLineWidth(3.0f);
                        _g.draw(jeu.plateau[ destinationPossible[v] ].polygone);
                        _g.setLineWidth(1.0f);
                }
            } else {
                terrainSource = null;       // On ne peut pas jouer à partir de ce terrain , on desactive donc la selection
            }
        }

        afficheScore(_g);      
        afficheEpoque(_g);


        /* Met en evidence les terrains bloques */
        for (int numTerrain = 0; numTerrain < jeu.plateau.length; numTerrain++) {
          
            if (jeu.plateau[numTerrain].estBloque() && (!jeu.plateau[numTerrain].estVide())) {
                _g.setColor(couleurTerrainBloque);
                _g.fill(jeu.plateau[numTerrain].polygone);
                _g.setColor(couleurContourVillage);
                _g.setLineWidth(3.0f);
                _g.draw(jeu.plateau[numTerrain].polygone);
            }


            if (jeu.plateau[numTerrain].getVillage()) {
                _g.setColor(couleurVillage);
                _g.fill(jeu.plateau[numTerrain].polygone);
                _g.setColor(couleurContourVillage);
                _g.setLineWidth(3.0f);
                _g.draw(jeu.plateau[numTerrain].polygone);
            }


            /* Affiche les villages à choisir si necessaire */
            if (choixOrdreVillage) {
                for (int i = 0; i < villages.length; i++) {
                    if (villages[i] == numTerrain) {
                        _g.setColor(couleurOrdreVillage);
                        _g.fill(jeu.plateau[ villages[i]].polygone);
                        _g.setColor(couleurContourTerrain);
                        _g.setLineWidth(3.0f);
                        _g.draw(jeu.plateau[ villages[i]].polygone);

                        afficheNumeroVillage(numTerrain, _g);
                    }
                }
            }

            placeCabanes(jeu.plateau[numTerrain], _g);
            //placeCabanesMax(numTerrain);
        }
        
        if (jeu.nbVillageAvecExplosion != 0) {
            for (int i=0 ; i< jeu.nbVillageAvecExplosion ; i++)
                _g.drawAnimation(animExplosion, centreTerrain[ jeu.villageAvecExplosion[i]*2 ]-32 , centreTerrain[ jeu.villageAvecExplosion[i]*2+1 ] -32 );

        }

        // Affiche le coup joué par l'ordinateur 
        if (terrainSourceOrdi != -1) {
            _g.setColor(Color.black);
            _g.setLineWidth(6.0f);
            _g.drawLine(centreTerrain[terrainSourceOrdi*2], centreTerrain[terrainSourceOrdi*2+1], centreTerrain[terrainCibleOrdi*2], centreTerrain[terrainCibleOrdi*2+1]);
            _g.fillOval(  centreTerrain[terrainCibleOrdi*2]-7, centreTerrain[terrainCibleOrdi*2+1] -7, 15, 15);
            _g.setLineWidth(1.0f);
        }        
        
          /*
           * Enlever le commentaire de la ligne du dessous pour tester la méthode placeCabaneMax()
           */
         
         //  for (int i = 0 ; i < jeu.plateau.length ; i++ ) placeCabanesMax( i);  
        
       if ( jeu.end() ) 
           afficheResultatPartie(_g);
    }

    /* Affiche le numero du village choisi par le joueur pour le terrain passé en parametre */
    /* Ordre de creation des villages */
    public void afficheNumeroVillage(int _numTerrain, Graphics _g) {
        for (int i = 0; i < nbChoixEffectue; i++) {
            if (ordreVillages[i] == _numTerrain) {
                _g.setFont(this.fontNumeroVillage);
                _g.drawString(i + 1 + "", centreTerrain[ ordreVillages[i] * 2] - 10, centreTerrain[ ordreVillages[i] * 2 + 1] - 10);
            }
        }
    }

    /*
     * Affiche le score en haut à droite
     */
    public void afficheScore(Graphics _g) {
        _g.setColor(new Color(216, 238, 116));
        _g.fillRect(990, 0, 1200, 195);
        _g.setFont(this.fontScore);
        for (int i = 0; i < 5; i++) {
            cabanes[i].draw(995, i * 40);
            _g.drawString(jeu.score[i] + "", 1020, i * 38);
        }

        boolean queDesOrdi = true;
        for (int jo = 0; jo < jeu.nbJoueur; jo++) {
            _g.setFont(this.fontJoueurVillage);
             if ( afficheCouleurJoueurActif && jo==jeu.joueurActif) { cabanes[jeu.joueurs[jeu.joueurActif].getCouleur()].draw(1145 - (jeu.joueurs[jo].getName().length()+2) * 10 - 10 , jo * 38 + 10); }
            _g.drawString( (jeu.joueurActif == jo ? "* ":"  ") + jeu.joueurs[jo].getName(), 1145 - jeu.joueurs[jo].getName().length() * 10, jo * 38 + 10);

            _g.setFont(this.fontScore);
            _g.drawString(jeu.joueurs[jo].getNbVillage() + "", 1160, jo * 38);
            
            if ( jeu.joueurs[jo].isHumain() ) queDesOrdi = false;
        }
        
        if ( queDesOrdi ) {
            for (int jo = 0; jo < jeu.nbJoueur; jo++)   cabanes[ jeu.joueurs[jo].getCouleur() ].draw(1145 - (jeu.joueurs[jo].getName().length()+2) * 10 - 10 , jo * 38 + 10);             
        }

    }

    /*
     * Affiche les epoques en bas à droite
     */
    public void afficheEpoque(Graphics _g) {
        _g.drawImage(epoque, 990, 196);
        if (jeu.villageCourant < 12) {

            _g.setColor(org.newdawn.slick.Color.red);
            _g.fillOval(1082, emplacementEpoque[jeu.villageCourant] - 12, 25, 25);
        }

    }

    /*
     * Affiche le score final en fin de partie au centre de la fenetre
     */
    public void afficheResultatPartie(Graphics _g) {
        _g.setColor(new Color(255, 255, 255,0.7f));
        _g.fillRect(300, 300, 400, 200);
        
        
        int scoreJoueur;
        for ( int j= 0 ; j < jeu.nbJoueur ; j++ ) {
            _g.setFont(this.fontScore);
            _g.setColor(new Color(0, 0, 0));
            _g.drawString("Résultat", 450 , 300 );
            scoreJoueur  = jeu.joueurs[j].getNbVillage() + jeu.score[ jeu.joueurs[j].getCouleur() ] ;
            cabanes[ jeu.joueurs[j].getCouleur() ].draw(350,  350 + j*50);
            _g.drawString(jeu.joueurs[j].getName() + " : " + scoreJoueur , 380 , 350 + j*50);
            
        }
        _g.setFont(this.fontNombreHutte);
        _g.drawString("Appuyer sur <espace>" , 410 , 470);
    }
 

    /**
     * Détermine le coup du joueur ( terrain source et terrain cible ) et joue le coup
     * @param _terrainSelectionne  terrain sélectioné par le joueur
     * @param _x  _x , _y sont les coordonnées où l'utilisateur a cliqué.
     * @param _y
     * @return 
     */
    public boolean determineCoupUtilisateur(Terrain _terrainSelectionne, int _x, int _y) {
        if (terrainSource != null) {
            terrainCible = _terrainSelectionne;

           if (jeu.move(terrainSource.getId(), terrainCible.getId())) { // Le coup est valide
                               
                villages = jeu.getVillage(terrainSource.getId());
                choixOrdreVillage = jeu.ordreImportant(villages.length);

                if (choixOrdreVillage) {
                    ordreVillages = new int[villages.length];
                    return true;// L'utilisateur doit choisir l'ordre de creation des villages.
                }      
                jeu.maj(terrainSource.getId(), terrainCible.getId(), villages);
                                
                jeu.changementDeJoueur();
            }

            terrainSource = null;
        } else {
            terrainSource = _terrainSelectionne;

        }
        return false;
    }


    /*
     * Détermine l'ordre de création des villages
     * Retourne vrai si tous les villages ont ete selectionnes
     */
    public boolean definirOrdreVillage(int _numTerrainSelectionne, int _x, int _y) {
        boolean villageSelectionne = false;
        boolean villageDejaSelectionne = false;

        /* Le terrain selectionne fait-il parti des villages ? */
        for (int i = 0; i < villages.length; i++) {
            if (villages[i] == _numTerrainSelectionne) {
                villageSelectionne = true;
            }
        }

        if (!villageSelectionne) {
            return false;
        }

        /* Le village a t'il deja ete selectionne ? */
        for (int i = 0; i < nbChoixEffectue; i++) {
            if (ordreVillages[i] == _numTerrainSelectionne) {
                villageDejaSelectionne = true;
            }
        }

        /* Si c'est le dernier de la liste on annule la selection sinon on ne fait rien */

        if (villageDejaSelectionne) {
            if (ordreVillages[nbChoixEffectue - 1] == _numTerrainSelectionne) {
                nbChoixEffectue--;
                return false;
            } else {
                return false;
            }
        }

        /* on memorise le village selectionne */
        ordreVillages[nbChoixEffectue] = _numTerrainSelectionne;
        nbChoixEffectue++;

        return nbChoixEffectue == villages.length;

    }

    /*
     * La méthode appelée par JAVA lorsque le joueur clique sur le plateau de jeu.
     * 
     */
    public void mouseClicked(int _button, int _x, int _y, int _nbClique) {

        if (!jeu.joueurs[jeu.joueurActif].isHumain()) {
            return;
        }

        if (_x > 1050 && _x < 1200 && _y > 0 && _y < 80 ) afficheCouleurJoueurActif = true;
        
        for (int i = 0; i < jeu.plateau.length; i++) {
             if (jeu.plateau[i].polygone.contains(_x, _y)) {
                if ( jeu.plateau[i].getNbCabane() == 0  ) {
                    return; // On ne pas sélectionner un terrain vide.
                }
                if (choixOrdreVillage) {
                    if (definirOrdreVillage(i, _x, _y)) {   // Tous les villages ont ete selectionnes
                        jeu.maj(terrainSource.getId(), terrainCible.getId(), ordreVillages);
                        jeu.changementDeJoueur();
                        choixOrdreVillage = false;
                        nbChoixEffectue = 0;
                        terrainSource = null;
                    }
                } else {
                    determineCoupUtilisateur(jeu.plateau[i], _x, _y);
                }

                break;
            }
        }
    }

    @Override
    /**
     * La méthode est appelée régulièrement par JAVA pour actualiser les variables du jeu.
     */
    public void update(GameContainer _container, int _delta) throws SlickException {
        
        if (jeu.nbVillageAvecExplosion !=0 ) {
            chronoExplosion+=_delta;
            if (chronoExplosion > 1000) {
                chronoExplosion = 0;
                jeu.nbVillageAvecExplosion = 0;
            }
        }
        
        if ( afficheCouleurJoueurActif ) {
            chronoCouleurJoueur+=_delta;
            if (chronoCouleurJoueur > 1000) {
                chronoCouleurJoueur = 0;
               afficheCouleurJoueurActif=false;
            }
        }
    }

    /*
     * La Méthode est appelée par JAVA lorsque le joueur appuie sur une touche du clavier.
     */
    public void keyReleased(int _key, char _c) {

        if (Input.KEY_ESCAPE == _key) {
            container.exit();
        }
        if (Input.KEY_SPACE == _key) {
            if ( jeu.end() )  {
                startNewGame(); // On relance le jeu si la partie est terminee
            }
            else 
                pause = pause ? false:true;
            
        }
        
    }

}

