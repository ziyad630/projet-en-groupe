package pokemon;

import fr.uit.mg2d.graphics.Couleur;
import fr.uit.mg2d.geometrie.Rectangle;
import fr.uit.mg2d.geometrie.Point;

public class Case {
    private int xGrid, yGrid;
    private int xPixel, yPixel, taille;

    // Coordonnées du coin BAS-GAUCHE de la case pour les Dessins MG2D
    private int xPixelBasGauche;
    private int yPixelBasGauche;

    private Pokemon pokemonOccupe; // Référence au Pokémon sur la case
    private boolean estCaseDeplacementPossible;
    private boolean estCibleAttaquePossible;

    public Case(int xGrid, int yGrid, int tailleCase) {
        this.xGrid = xGrid;
        this.yGrid = yGrid; // Ligne (0 en haut pour la matrice)
        this.taille = tailleCase;

        // Calcul des coordonnées du coin BAS-GAUCHE pour les Dessins MG2D
        this.xPixelBasGauche = xGrid * tailleCase;
        // HAUTEUR_GRILLE est la hauteur totale de la zone de la grille
        // Si yGrid = 0 (première ligne en haut), son yPixelBasGauche sera HAUTEUR_GRILLE - tailleCase
        // Si yGrid = NB_LIGNES - 1 (dernière ligne en bas), son yPixelBasGauche sera 0
        this.yPixelBasGauche = JeuPokemonPlateau.HAUTEUR_GRILLE - (yGrid + 1) * tailleCase;


        this.pokemonOccupe = null;
        this.estCaseDeplacementPossible = false; // Initialiser à false
        this.estCibleAttaquePossible = false; // Initialiser à false
    }

    // Retourne le Dessin pour le fond de la case
    public Rectangle getDessinFond() {
        return new Rectangle(Couleur.GRIS_CLAIR, new Point(xPixelBasGauche, yPixelBasGauche), taille, taille, true);
    }

    // Retourne le Dessin pour la bordure de la case
    public Rectangle getDessinBordure() {
        return new Rectangle(Couleur.NOIR, new Point(xPixelBasGauche, yPixelBasGauche), taille, taille, false);
    }

    public Pokemon getPokemonOccupe() {
        return pokemonOccupe;
    }

    public void setPokemonOccupe(Pokemon pokemon) {
        this.pokemonOccupe = pokemon;
    }

    public boolean estVide() {
        return this.pokemonOccupe == null;
    }

    public boolean estCaseDeplacementPossible() {
        return estCaseDeplacementPossible;
    }

    public void setCaseDeplacementPossible(boolean possible) {
        this.estCaseDeplacementPossible = possible;
    }

    public boolean estCibleAttaquePossible() {
        return estCibleAttaquePossible;
    }

    public void setCibleAttaquePossible(boolean possible) {
        this.estCibleAttaquePossible = possible;
    }

    // Getters pour les coordonnées si besoin
    public int getxPixelBasGauche() { return xPixelBasGauche; }
    public int getyPixelBasGauche() { return yPixelBasGauche; }
    public int getTaille() { return taille; }
}
