# **Clans**

## Voici notre projet d'intelligence artificielle/Stratégie du jeu de société Clans

#### La stratégie fonctionne ainsi, on regarde si l'on peut créer un village et si oui, on va favoriser la création de village qui nous avantage par rapport à notre adversaire supposé. Sinon on regarde si au moins deux huttes de notre couleur sont sur un terrain favorable, si oui, on essaye d'en faire un village rapidement. Sinon on regarde si on a 0 pions dans un territoire défavorable, si oui on essaye de faire rentrer un maximum de pions enemis sur ce terrain puis d'en faire un village. Dans le pire des cas, on joue un coup random.
#### 

#### D'autres méthodes ont été crées et testées (empecher de jouer un coup offrant un village à l'adversaire, essayer de répéter un schéma de jeu plutot qu'un coup random...) mais on obtient de meilleurs résultats sans les utiliser.
#### 
#### Dans stratégie :
#### 
#### De la ligne 46 à 49, on regarde si on peut détruire un village, cela nous avantage car nous gagnons le point de création du village.
#### De la ligne 52 à 105, on a le code permettant d'appliquer un schéma tout fait au début de la partie
#### De la ligne 106 à 140  On s'intéresse à la stratégie à adopter lors du coup final 
#### De la ligne 142 à 161 c'est le bloc principal de notre stratégie , on regarde tous les coups possibles et si l'un d'entre eux nous avantage par rapport à la couleur que l'on suppose être notre opposant, on le joue
#### De la ligne 163 à 188 on regarde si on a au moins 2 pions dans un territoire favorable, si notre adversaire supposé est voisin de ce territoire, on va essayer de l'éloigner de ce territoire pour qu'il ne marque pas de points, sinon on essaye de créer un village en déplacant les voisins de ce territoire favorable dans ce terrain favorable
#### De la ligne 190 à 215 on regarde si 0 pions dans un territoire défavorable, si on est voisin de ce territoire défavorable, on va essayer de s'éloigner de ce territoiren,sinon on essaye de créer un village en rentrant les voisins dans ce territoire et ainsi on élimine des pions adverses
#### De la ligne 217 à 227 si on est dans aucun des cas précent, on joue un coup aléatoire
#### De la ligne 200 à 248 on fait une liste de toutes les manières d'ordonner les villages, on établit une liste de points que rapporte chaque combinaison, ces points sont basés sur nos critères
#### De la ligne 249 à 261 sinon on joue un code aléatoire
#### 
#### Dans Tools
#### 
#### A partir de la ligne 261, on rerouve les fonctions que nous avons codées, ces fonctions se comprennent avec leurs noms et le court commentaire associé.


### Voici les fonctions que nous avons rajoutées dans Tools.

     * @param plateau
     * @param i
     * @return la liste des cabanes dans la case i     
- public static int[] cabanes_i(Terrain[] plateau, int i)
    
     * @param plateau
     * @param i
     * @return le nb de cabanes dans la case i
- public static int nbpions_i(Terrain[] plateau, int i)
    
     * @param plateau
     * @param i
     * @return si la case i est vide
- public static boolean est_vide(Terrain[] plateau, int i)
    
     * @param plateau
     * @param i
     * @return si la case i est un village
- public static boolean est_village(Terrain[] plateau, int i)
    
     * @param plateau
     * @param index1
     * @param index2
     * @return si la case index1 et index2 sont voisines
- public static boolean estVoisinDe(Terrain[] plateau, int index1,int index2){
   
     * @param plateau
     * @param _source
     * @param _dest
     * @return si le déplacement de _source à _dest est valide
- public static boolean coupValide(Terrain[] plateau,int _source, int _dest) {
    
     * @param plateau
     * @param i
     * @return le type de terrain de la case i
- public static String type(Terrain[] plateau, int i){

     * @param _plateau
     * @return le nombre de village possible
- public int nbVillagePossible(Terrain [] _plateau){
    
     * @param _plateau
     * @param _myColor
     * @param _colorScore
     * @param _myScore
     * @return return l'index de l'adversaire le plus fort en points
- public static int worseOpp (Terrain [] _plateau, int _myColor, int [] _colorScore, int _myScore){
    
     * @param _plateau
     * @param _myColor
     * @param _opponentVillages
     * @param _myScore
     * @return return l'index de l'adversaire qui à marqué le plus de point au tour précédent
- public static int suppOpp (Terrain[] _plateau,int _myColor, int [] _opponentVillages,int _myScore){

     * @param _plateau
     * @param _myColor
     * @param _opponentVillages
     * @param _myScore
     * @return return l'index de l'adversaire qui à marqué le plus de point au tour précédent
- public static int[] getSrcDispo(Terrain[] plateau) {
    
     * @param n
     * @return return factoriel n  
- public static int fact (int n) {
    
     * @param n
     * @return return les combinaisons possibles 
- public static int[][] combinaisons(int[] L){

     * @param _plateau
     * @param _myColor
     * @param index
     * @return return si une couleur est sur un territoire
- public static boolean appartient (Terrain [] _plateau, int _myColor, int index){

     * @param _cabanes
     * @return si les 5 couleurs sont dans la liste des cabanes
- public static boolean cinqCouleurs(int[] _cabanes){

     * @param _plateau
     * @param _myColor
     * @param _colorScore
     * @param _myScore
     * @param _opponentScore
     * @param _opponentMov
     * @param _opponentVillages
     * @param nb
     * @return le couple source destination détruisant un maximum de pions ennemis si possible
- public static int [] destru(Terrain[] _plateau, int _myColor, int[] _colorScore, int _myScore, int _opponentScore, int[] _opponentMov, int[] _opponentVillages,int nb){
    
    
     * @param _plateau
     * @param  dest
     * @param src
     * @return le potentiel de point de la case sans bonus terrain mais hutte 
- public static int pnts_pot(Terrain[] _plateau,int src,int dest){

    * @param _plateau
     * @param _myColor
     * @param _colorScore
     * @param _myScore
     * @param _opponentScore
     * @param _opponentMov
     * @param _opponentVillages
     * @param nb
     * @return le meilleur coup pour finir la partie ou {-1,-1} si on ne peut pas finir la partie
- public int[] finalCoup(Terrain[] _plateau, int _myColor, int[] _colorScore, int _myScore, int _opponentScore, int[] _opponentMov, int[] 

### Groupe: 1
### Équipe: 
- Alexandre BERGER
- Leslie RINEAU
- Gabriel HARIVEL
