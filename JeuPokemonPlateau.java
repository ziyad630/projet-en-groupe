package pokemon; // Ou votre package

import fr.uit.mg2d.graphics.Fenetre;
// import fr.uit.mg2d.graphics.Couleur; // Pas directement utilisé ici si Plateau gère les couleurs
import fr.uit.mg2d.input.Souris;
import fr.uit.mg2d.geometrie.Point;
// import java.awt.Graphics2D; // Pas directement utilisé ici

public class JeuPokemonPlateau {

    public static final int NB_LIGNES = 9;
    public static final int NB_COLONNES = 9;
    public static final int TAILLE_CASE = 60;

    public static final int LARGEUR_GRILLE = NB_COLONNES * TAILLE_CASE;
    public static final int HAUTEUR_GRILLE = NB_LIGNES * TAILLE_CASE;
    public static final int HAUTEUR_INFOS = 50;
    public static final int HAUTEUR_FENETRE_TOTALE = HAUTEUR_GRILLE + HAUTEUR_INFOS;

    private Fenetre fenetreJeu;
    private Plateau plateauJeu;
    private Souris souris;
    private boolean jeuEnCours = true;

    public JeuPokemonPlateau() {
        this.fenetreJeu = new Fenetre("Jeu de Plateau Pokémon", LARGEUR_GRILLE, HAUTEUR_FENETRE_TOTALE);
        this.plateauJeu = new Plateau(NB_LIGNES, NB_COLONNES, TAILLE_CASE, this.fenetreJeu);
        this.souris = this.fenetreJeu.getSouris();
        boucleDeJeu();
    }

    private void initialiserJeu() {
        // Peut rester vide pour l'instant. Le plateau s'initialise dans son constructeur.
    }

    private void boucleDeJeu() {
        initialiserJeu();
        while (jeuEnCours) {
            traiterEntrees();
            mettreAJourLogiqueJeu();
            dessinerJeu();
            try {
                Thread.sleep(30); // Environ 33 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
                jeuEnCours = false;
            }
        }
        // Afficher le message final avant de fermer
        if (plateauJeu != null && plateauJeu.getMessageAction() != null) { // Vérification pour éviter NPE si plateauJeu est null
            System.out.println("Message final avant fermeture: " + plateauJeu.getMessageAction());
        } else {
            System.out.println("Fin du jeu.");
        }

        try {
            Thread.sleep(3000); // Petit délai pour voir le message final
        } catch (InterruptedException e) {
            // Ignorer
        }
        fenetreJeu.fermer();
    }

    private void traiterEntrees() {
        if (souris.getClicGauche()) {
            Point pointPositionSouris = souris.getPosition();
            if (pointPositionSouris != null) {
                plateauJeu.gererClic( (int) pointPositionSouris.getX(), (int) pointPositionSouris.getY() );
            }
        }
    }

    private void mettreAJourLogiqueJeu() {
        if (!jeuEnCours) return; // Ne rien faire si le jeu est déjà marqué comme terminé

        if (plateauJeu.verifierConditionVictoire()) {
            // Le message de victoire/défaite est déjà dans plateauJeu.messageAction
            // et sera affiché par plateauJeu.dessinerSurFenetre() lors du dernier dessin.
            jeuEnCours = false; // Marquer le jeu comme terminé pour la prochaine itération
        }
    }

    private void dessinerJeu() {
        fenetreJeu.effacer(); // Efface tout ce qui a été ajouté à fenetreJeu lors du frame précédent

        if (plateauJeu != null) { // Vérification pour éviter NPE si plateauJeu n'est pas encore prêt
            plateauJeu.dessinerSurFenetre(); // Le plateau ajoute tous ses éléments (cases, pokemons, messages)
        }

        fenetreJeu.rafraichir(); // Affiche les éléments ajoutés
    }

    public static void main(String[] args) {
        new JeuPokemonPlateau();
    }
}