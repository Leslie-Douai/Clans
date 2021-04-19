# **Clans**

## Voici notre projet d'intelligence artificielle/Stratégie du jeu de société Clans

#### La stratégie fonctionne ainsi, on regarde si l'on peut créer un village et si oui, on va favoriser la création de village qui nous avantage par rapport à notre adversaire supposé. Sinon on regarde si au moins deux huttes de notre couleur sont sur un terrain favorable, si oui, on essaye d'en faire un village rapidement. Sinon on regarde si on a 0 pions dans un territoire défavorable, si oui on essaye de faire rentrer un maximum de pions enemis sur ce terrain puis d'en faire un village. Dans le pire des cas, on joue un coup random.
#### 

#### D'autres méthodes ont été crées et testées (empecher de jouer un coup offrant un village à l'adversaire, essayer de répéter un schéma de jeu plutot qu'un coup random...) mais on obtient de meilleurs résultats sans les utiliser.
#### 
#### Dans stratégie :
#### 
#### De la ligne 46 à 49, on regarde si on peut détruire un village, cela nous avantage car nous gagnons le point de création du village.
#### De la ligne 52 à 105, on a le code permettant d'appliquer un schéma tout fait au début de la partie, ce code est en commentaire car les résultats obtenus sont moins bon avec.
#### De la ligne 108 à 127 c'est le bloc principal de notre stratégie , on regarde tous les coups possibles et si l'un d'entre eux nous avantage par rapport à la couleur que l'on suppose être notre opposant, on le joue
#### De la ligne 129 à 154 on regarde si on a au moins 2 pions dans un territoire favorable, si notre adversaire supposé est voisin de ce territoire, on va essayer de l'éloigner de ce territoire pour qu'il ne marque pas de points, sinon on essaye de créer un village en déplacant les voisins de ce territoire favorable dans ce terrain favorable
#### De la ligne 156 à 184 on regarde si 0 pions dans un territoire défavorable, si on est voisin de ce territoire défavorable, on va essayer de s'éloigner de ce territoiren,sinon on essaye de créer un village en rentrant les voisins dans ce territoire et ainsi on élimine des pions adverses
#### De la ligne 188 à 196 si on est dans aucun des cas précent, on joue un coup aléatoire
#### De la ligne 200 à 248 on fait une liste de toutes les manières d'ordonner les villages, on établit une liste de points que rapporte chaque combinaison, ces points sont basés sur nos critères
#### De la ligne 249 à 261 sinon on joue un code aléatoire
#### 
#### Dans Tools
#### 
#### A partir de la ligne 261, on rerouve les fonctions que nous avons codées, ces fonctions se comprennent avec leurs noms et le court commentaire associé.



### Groupe: 1
### Équipe: 
- Alexandre BERGER
- Leslie RINEAU
- Gabriel HARIVEL
