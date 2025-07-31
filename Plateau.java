package pokemon;

import fr.uit.mg2d.graphics.Fenetre;
import fr.uit.mg2d.graphics.Couleur;
import fr.uit.mg2d.geometrie.Point;
import fr.uit.mg2d.geometrie.Texte;
import fr.uit.mg2d.geometrie.Texture;
import fr.uit.mg2d.geometrie.Rectangle; // Pour la surbrillance

import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Plateau {
    private Case[][] grille;
    private int nbLignes;
    private int nbColonnes;
    private int tailleCase;
    private Fenetre fenetreJeu;

    private List<Pokemon> equipeJoueur1;
    private List<Pokemon> equipeJoueur2;
    private Pokemon mewtwoJoueur1;
    private Pokemon mewtwoJoueur2;

    // --- NOUVEAUX ATTRIBUTS ---
    private int joueurActif; // 1 pour Joueur 1 (bas), 2 pour Joueur 2 (haut)
    private Pokemon pokemonSelectionne;
    private String messageAction; // Pour afficher des messages à l'utilisateur (tour du joueur, etc.)
    private static final String CONFIG_FILE_PATH = "equipes_config.txt";
    // Constantes pour les joueurs (optionnel, mais rend le code plus lisible)
    public static final int JOUEUR_1 = 1;
    public static final int JOUEUR_2 = 2;
    // -----------------------------------------

    public Plateau(int nbLignes, int nbColonnes, int tailleCase, Fenetre fenetre) {
        this.nbLignes = nbLignes;
        this.nbColonnes = nbColonnes;
        this.tailleCase = tailleCase;
        this.fenetreJeu = fenetre;

        this.grille = new Case[nbLignes][nbColonnes];
        initialiserGrille();

        this.equipeJoueur1 = new ArrayList<>();
        this.equipeJoueur2 = new ArrayList<>();
        initialiserEquipesEtPokemon();

        // --- INITIALISATION POUR LA PHASE 3 ---
        this.joueurActif = JOUEUR_1; // Le joueur 1 commence
        this.pokemonSelectionne = null;
        this.messageAction = "Au tour du Joueur " + this.joueurActif;
        // ------------------------------------
    }

    private void initialiserGrille() {
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                grille[i][j] = new Case(j, i, tailleCase);
            }
        }
    }

    private void initialiserEquipesEtPokemon() {
        this.equipeJoueur1.clear(); // S'assurer que les listes sont vides avant de charger
        this.equipeJoueur2.clear();
        this.mewtwoJoueur1 = null; // Réinitialiser les références à Mewtwo
        this.mewtwoJoueur2 = null;

        System.out.println("Chargement de la configuration des équipes depuis : " + CONFIG_FILE_PATH);

        try (BufferedReader br = new BufferedReader(new FileReader(CONFIG_FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) { // Ignorer lignes vides et commentaires
                    continue;
                }

                String[] parts = line.split(","); // Séparateur virgule
                if (parts.length < 5) {
                    System.err.println("Ligne de configuration malformée (pas assez de parties) : " + line);
                    continue;
                }

                try {
                    int joueur = Integer.parseInt(parts[0].trim());
                    int numeroPokedex = Integer.parseInt(parts[1].trim());
                    String petitNom = parts[2].trim();
                    int ligne = Integer.parseInt(parts[3].trim());
                    int colonne = Integer.parseInt(parts[4].trim());

                    // Valider les données (joueur, ligne, colonne)
                    if ((joueur != JOUEUR_1 && joueur != JOUEUR_2)) {
                        System.err.println("Joueur invalide dans la config : " + line);
                        continue;
                    }
                    // Valider les zones de placement
                    if (joueur == JOUEUR_1 && (ligne < nbLignes - 3 || ligne >= nbLignes)) {
                        System.err.println("Placement invalide pour Joueur 1 (ligne " + ligne + ") : " + line);
                        continue;
                    }
                    if (joueur == JOUEUR_2 && (ligne < 0 || ligne >= 3)) {
                        System.err.println("Placement invalide pour Joueur 2 (ligne " + ligne + ") : " + line);
                        continue;
                    }
                    if (colonne < 0 || colonne >= nbColonnes) {
                        System.err.println("Placement invalide (colonne " + colonne + ") : " + line);
                        continue;
                    }


                    Pokemon nouveauPokemon = new Pokemon(numeroPokedex, petitNom); // Utilise le constructeur qui lit le CSV pour les stats

                    if (nouveauPokemon.getPv() <= 0) { // Vérifier si le Pokémon a été chargé correctement depuis pokedex_gen1.csv
                        System.err.println("Erreur chargement stats pour Pokémon #" + numeroPokedex + " (" + petitNom + "). Ignoré.");
                        continue;
                    }

                    placerPokemon(nouveauPokemon, ligne, colonne);

                    if (joueur == JOUEUR_1) {
                        equipeJoueur1.add(nouveauPokemon);
                        if (numeroPokedex == 150) { // Numéro de Mewtwo
                            if (this.mewtwoJoueur1 == null) this.mewtwoJoueur1 = nouveauPokemon;
                            else System.err.println("Attention : Plusieurs Mewtwo pour Joueur 1 dans la config !");
                        }
                    } else if (joueur == JOUEUR_2) {
                        equipeJoueur2.add(nouveauPokemon);
                        if (numeroPokedex == 150) {
                            if (this.mewtwoJoueur2 == null) this.mewtwoJoueur2 = nouveauPokemon;
                            else System.err.println("Attention : Plusieurs Mewtwo pour Joueur 2 dans la config !");
                        }
                    }

                } catch (NumberFormatException e) {
                    System.err.println("Erreur de format de nombre dans la ligne de configuration : " + line + " - " + e.getMessage());
                } catch (Exception e) { // Attraper d'autres erreurs potentielles de Pokemon constructor etc.
                    System.err.println("Erreur lors de la création du Pokémon depuis la config : " + line + " - " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERREUR : Fichier de configuration des équipes '" + CONFIG_FILE_PATH + "' non trouvé ! Utilisation d'équipes par défaut (ou jeu non jouable).");
            // Optionnel : Appeler une méthode pour initialiser des équipes par défaut si le fichier est manquant
            // initialiserEquipesParDefautSiConfigManquante();
            // Pour l'instant, on ne fait rien, le plateau sera vide si le fichier n'est pas trouvé.
            messageAction = "ERREUR: Fichier " + CONFIG_FILE_PATH + " introuvable.";

        } catch (IOException e) {
            System.err.println("Erreur d'I/O lors de la lecture du fichier de configuration : " + e.getMessage());
            messageAction = "ERREUR: Lecture de " + CONFIG_FILE_PATH + " impossible.";
        }

        // Vérification cruciale après le chargement
        if (this.mewtwoJoueur1 == null || this.mewtwoJoueur2 == null) {
            System.err.println("ERREUR CRITIQUE : Un Mewtwo est manquant dans la configuration des équipes ! Le jeu ne peut pas continuer.");
            messageAction = "ERREUR: Mewtwo manquant dans config !";
            // On pourrait vouloir arrêter le jeu ici ou empêcher le démarrage.
            // Pour l'instant, on laisse le message. verifierConditionVictoire plantera si un Mewtwo est null.
            // Mieux : initialiser à un "Dummy" Pokemon KO ou gérer dans verifierConditionVictoire.
            // Pour l'instant, le jeu plantera si on essaie d'accéder à un Mewtwo null.
        } else {
            System.out.println("Configuration des équipes chargée. " + equipeJoueur1.size() + " Pokémon pour J1, " + equipeJoueur2.size() + " pour J2.");
        }
    }

    private void placerPokemon(Pokemon p, int ligne, int colonne) {
        if (p == null) {
            System.err.println("Erreur: Tentative de placer un Pokémon null.");
            return;
        }
        if (ligne >= 0 && ligne < nbLignes && colonne >= 0 && colonne < nbColonnes) {
            if (grille[ligne][colonne].estVide()) {
                grille[ligne][colonne].setPokemonOccupe(p);
            } else {
                System.err.println("Erreur: Case (" + ligne + "," + colonne + ") déjà occupée. Impossible de placer " + p.getNomEspece());
            }
        } else {
            System.err.println("Erreur: Coordonnées de placement ("+ligne+","+colonne+") hors grille pour " + p.getNomEspece());
        }
    }

    public void dessinerSurFenetre() {
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                Case c = grille[i][j];

                // 1. Dessiner le fond de la case (avec indicateurs de déplacement/attaque)
                if (c.estCaseDeplacementPossible()) {
                    fenetreJeu.ajouter(new Rectangle(Couleur.VERT,
                            new Point(c.getxPixelBasGauche(), c.getyPixelBasGauche()),
                            c.getTaille(), c.getTaille(), true)); // true = rempli
                } else if (c.estCibleAttaquePossible()) {
                    fenetreJeu.ajouter(new Rectangle(Couleur.ORANGE, // Ou Couleur.ROUGE si elle existe et convient
                            new Point(c.getxPixelBasGauche(), c.getyPixelBasGauche()),
                            c.getTaille(), c.getTaille(), true));
                } else {
                    fenetreJeu.ajouter(c.getDessinFond()); // Fond normal (gris clair)
                }

                // 2. Dessiner la surbrillance pour le Pokémon sélectionné ET la bordure normale
                if (pokemonSelectionne != null && c.getPokemonOccupe() == pokemonSelectionne) {
                    // Surbrillance jaune
                    fenetreJeu.ajouter(new Rectangle(Couleur.JAUNE, new Point(c.getxPixelBasGauche() - 2, c.getyPixelBasGauche() - 2), c.getTaille() + 4, c.getTaille() + 4, false));
                    fenetreJeu.ajouter(new Rectangle(Couleur.JAUNE, new Point(c.getxPixelBasGauche() - 1, c.getyPixelBasGauche() - 1), c.getTaille() + 2, c.getTaille() + 2, false));
                    // On peut choisir de dessiner la bordure noire par-dessus ou pas. Si oui:
                    fenetreJeu.ajouter(c.getDessinBordure());
                } else {
                    fenetreJeu.ajouter(c.getDessinBordure()); // Bordure normale si non sélectionné
                }

                // 3. Dessiner le Pokémon et ses PV s'il y en a un sur la case
                Pokemon pokeSurCase = c.getPokemonOccupe();
                if (pokeSurCase != null) {
                    // Obtenir et ajouter le sprite du Pokémon
                    // La méthode getSpriteDessin dans Pokemon.java gère le redimensionnement et le positionnement
                    Texture spriteDessin = pokeSurCase.getSpriteDessin(c.getxPixelBasGauche(), c.getyPixelBasGauche(), c.getTaille());
                    if (spriteDessin != null) {
                        fenetreJeu.ajouter(spriteDessin);
                    }

                    // Afficher les PV
                    String pvStr = pokeSurCase.getPv() + "";
                    Font policePV = new Font("Arial", Font.BOLD, 12); // Définir la police pour les PV

                    // Calculer la position pour le CENTRE du texte des PV (ex: en bas à droite de la case)
                    int margeDuBord = 5;
                    int hauteurEstimeeTexte = policePV.getSize();
                    // Estimation de la largeur du texte pour le centrage.
                    // Pour être précis, il faudrait utiliser FontMetrics, mais pour quelques chiffres, c'est une approx.
                    int largeurTexteApproximation = pvStr.length() * (policePV.getSize() / 2 + 3); // Ajuster ce facteur si besoin

                    int centreXTexte = c.getxPixelBasGauche() + c.getTaille() - (largeurTexteApproximation / 2) - margeDuBord;
                    int centreYTexte = c.getyPixelBasGauche() + (hauteurEstimeeTexte / 2) + margeDuBord;

                    Point posCentrePV = new Point(centreXTexte, centreYTexte);

                    Texte textePV = new Texte(Couleur.ROUGE, pvStr, policePV, posCentrePV);
                    fenetreJeu.ajouter(textePV);
                }
            }
        }

        // 4. Afficher le message d'action (tour du joueur, etc.)
        if (messageAction != null && !messageAction.isEmpty()) {
            Font policeMessage = new Font("Arial", Font.BOLD, 16);
            Couleur couleurMsg = (joueurActif == JOUEUR_1) ? Couleur.BLEU : Couleur.ROUGE;
            Point posMessage = new Point(JeuPokemonPlateau.LARGEUR_GRILLE / 2,
                    JeuPokemonPlateau.HAUTEUR_GRILLE + JeuPokemonPlateau.HAUTEUR_INFOS / 2);
            Texte texteMessage = new Texte(couleurMsg, messageAction, policeMessage, posMessage);
            fenetreJeu.ajouter(texteMessage);
        }
    }

    public void gererClic(int xClicPixel, int yClicPixel) {
        int col = xClicPixel / tailleCase;
        int ligBrute = yClicPixel / tailleCase; // Ligne 0 en bas pour les coordonnées du clic
        int ligPourMatrice = (nbLignes - 1) - ligBrute; // Ligne 0 en haut pour l'accès à la matrice grille[][]

        if (col >= 0 && col < nbColonnes && ligPourMatrice >= 0 && ligPourMatrice < nbLignes) { // Clic dans la grille
            Case caseCliquee = grille[ligPourMatrice][col];
            Pokemon pokeSurCaseCliquee = caseCliquee.getPokemonOccupe();

            // Affichage console pour débogage
            System.out.println("Clic sur case: L" + ligPourMatrice + " C" + col +
                    ". Joueur actif: " + joueurActif +
                    ". Pokémon sur case: " + (pokeSurCaseCliquee != null ? pokeSurCaseCliquee.getAffichageNom() : "Aucun") +
                    ". Sélectionné: " + (pokemonSelectionne != null ? pokemonSelectionne.getAffichageNom() : "Aucun"));

            if (pokemonSelectionne == null) { // --- AUCUN POKÉMON N'EST ACTUELLEMENT SÉLECTIONNÉ ---
                if (pokeSurCaseCliquee != null) { // Si on a cliqué sur une case avec un Pokémon
                    // Vérifier si le Pokémon appartient au joueur actif
                    boolean appartientAuJoueurActif = false;
                    if (joueurActif == JOUEUR_1 && equipeJoueur1.contains(pokeSurCaseCliquee)) {
                        appartientAuJoueurActif = true;
                    } else if (joueurActif == JOUEUR_2 && equipeJoueur2.contains(pokeSurCaseCliquee)) {
                        appartientAuJoueurActif = true;
                    }

                    if (appartientAuJoueurActif) {
                        pokemonSelectionne = pokeSurCaseCliquee;
                        messageAction = pokemonSelectionne.getAffichageNom() + " sélectionné. Déplacez ou attaquez.";
                        System.out.println(messageAction);
                        calculerEtAfficherActionsPossibles(); // Calcule et marque les cases pour déplacement/attaque
                    } else {
                        messageAction = "Ce n'est pas votre Pokémon ! Au tour du Joueur " + joueurActif;
                        System.out.println("Ce n'est pas un Pokémon du joueur " + joueurActif);
                    }
                } else { // Clic sur une case vide sans Pokémon sélectionné
                    messageAction = "Case vide. Au tour du Joueur " + joueurActif;
                }
            } else { // --- UN POKÉMON EST DÉJÀ SÉLECTIONNÉ ---

                if (caseCliquee.estCaseDeplacementPossible()) { // Si on clique sur une case de déplacement valide (verte)
                    System.out.println("DEBUG: Clic sur une case de déplacement possible !");
                    Case caseDeDepart = null;
                    // Retrouver la case de départ du pokemonSelectionne
                    // (Nécessaire car pokemonSelectionne est juste une référence à l'objet Pokemon, pas à sa case)
                    for (int i = 0; i < nbLignes; i++) {
                        for (int j = 0; j < nbColonnes; j++) {
                            if (grille[i][j].getPokemonOccupe() == pokemonSelectionne) {
                                caseDeDepart = grille[i][j];
                                break;
                            }
                        }
                        if (caseDeDepart != null) break;
                    }

                    if (caseDeDepart != null) {
                        deplacerPokemon(caseDeDepart, caseCliquee); // Effectuer le déplacement
                        effacerIndicateurs();                       // Nettoyer les cases vertes/oranges
                        terminerTour();                             // Passer au joueur suivant
                    } else {
                        // Ceci ne devrait pas arriver si la logique est correcte
                        System.err.println("Erreur critique: Pokémon sélectionné (" + pokemonSelectionne.getAffichageNom() + ") non trouvé sur la grille !");
                        pokemonSelectionne = null;
                        effacerIndicateurs();
                        messageAction = "Erreur interne. Au tour du Joueur " + joueurActif;
                    }

                } else if (caseCliquee.estCibleAttaquePossible()) { // Si on clique sur une cible d'attaque valide (orange)
                    System.out.println("DEBUG: Clic sur une cible d'attaque possible !");
                    if (pokeSurCaseCliquee != null && pokeSurCaseCliquee != pokemonSelectionne) {
                        lancerAttaque(pokemonSelectionne, pokeSurCaseCliquee);
                        effacerIndicateurs();
                        if (!verifierConditionVictoire()) { // Si le jeu n'est pas fini après l'attaque
                            terminerTour();
                        } else {
                            // Le jeu est fini, messageAction a été mis par verifierConditionVictoire
                            // On pourrait désactiver d'autres actions ici.
                            pokemonSelectionne = null; // Plus de sélection possible
                            // messageAction est déjà "JOUEUR X GAGNE..."
                        }
                    } else {
                        System.out.println("DEBUG: Cible d'attaque invalide ou clic sur soi-même.");
                        // Optionnel: désélectionner ou laisser sélectionné
                    }
                } else if (pokeSurCaseCliquee == pokemonSelectionne) { // Clic sur le Pokémon déjà sélectionné -> Désélection
                    System.out.println("DEBUG: Clic sur Pokémon déjà sélectionné -> Désélection.");
                    pokemonSelectionne = null;
                    effacerIndicateurs();
                    messageAction = "Au tour du Joueur " + joueurActif;
                } else { // Clic ailleurs (case non valide pour une action, ou sur un allié, etc.)
                    System.out.println("DEBUG: Clic sur une case non valide pour une action -> Désélection.");
                    messageAction = "Action invalide pour " + pokemonSelectionne.getAffichageNom() + ". Au tour du Joueur " + joueurActif + ".";
                    pokemonSelectionne = null;
                    effacerIndicateurs();
                }
            }
        } else { // Clic HORS GRILLE
            System.out.println("Clic HORS GRILLE.");
            if (pokemonSelectionne != null) {
                pokemonSelectionne = null;
                effacerIndicateurs();
                messageAction = "Action annulée (clic hors grille). Au tour du Joueur " + joueurActif;
                System.out.println("Pokémon désélectionné (clic hors grille).");
            }
        }
    }
    // Méthode pour terminer le tour et changer de joueur
    public void terminerTour() {
        pokemonSelectionne = null;
        effacerIndicateursDeplacement(); // S'assurer que les indicateurs sont effacés
        if (joueurActif == JOUEUR_1) {
            joueurActif = JOUEUR_2;
        } else {
            joueurActif = JOUEUR_1;
        }
        messageAction = "Au tour du Joueur " + joueurActif;
        System.out.println("Fin du tour. Au joueur " + joueurActif + " de jouer.");
    }

    // Méthode pour lancer l'attaque
    private void lancerAttaque(Pokemon attaquant, Pokemon defenseur) {
        if (attaquant == null || defenseur == null) return;

        messageAction = attaquant.getAffichageNom() + " attaque " + defenseur.getAffichageNom() + " !";
        System.out.println(messageAction);
        // La méthode attaque de Pokemon gère l'affichage console du combat
        attaquant.attaque(defenseur); // La méthode de Pokemon.java

        // Vérifier si le défenseur est K.O après l'attaque
        if (defenseur.estKO()) {
            messageAction += " " + defenseur.getAffichageNom() + " est K.O. !";
            // Retirer le Pokémon K.O. de la grille
            for (int i = 0; i < nbLignes; i++) {
                for (int j = 0; j < nbColonnes; j++) {
                    if (grille[i][j].getPokemonOccupe() == defenseur) {
                        grille[i][j].setPokemonOccupe(null);
                        break;
                    }
                }
            }
            // Retirer de l'équipe aussi (important pour la logique "appartientAuJoueurActif")
            if (equipeJoueur1.contains(defenseur)) equipeJoueur1.remove(defenseur);
            if (equipeJoueur2.contains(defenseur)) equipeJoueur2.remove(defenseur);
        }
        System.out.println("PV " + attaquant.getAffichageNom() + ": " + attaquant.getPv() + " | PV " + defenseur.getAffichageNom() + ": " + defenseur.getPv());
    }

    public boolean verifierConditionVictoire() {
        if (this.mewtwoJoueur1 == null) {
            System.err.println("Mewtwo du Joueur 1 non initialisé !");
            messageAction = "ERREUR CONFIG: Mewtwo J1 manquant.";
            // Considérer cela comme une victoire pour J2 si J1 n'a pas de Mewtwo pour commencer.
            return true; // Ou une autre logique d'erreur
        }
        if (this.mewtwoJoueur2 == null) {
            System.err.println("Mewtwo du Joueur 2 non initialisé !");
            messageAction = "ERREUR CONFIG: Mewtwo J2 manquant.";
            return true;
        }

        if (mewtwoJoueur1.estKO()) {
            messageAction = "JOUEUR 2 GAGNE ! Mewtwo du Joueur 1 est K.O. !";
            System.out.println(messageAction);
            return true;
        }
        if (mewtwoJoueur2.estKO()) {
            messageAction = "JOUEUR 1 GAGNE ! Mewtwo du Joueur 2 est K.O. !";
            System.out.println(messageAction);
            return true;
        }
        return false;
    }

    private void calculerEtAfficherCasesDeplacementPossibles() {
        effacerIndicateursDeplacement(); // D'abord, nettoyer les anciens indicateurs

        if (pokemonSelectionne == null) {
            return; // Pas de Pokémon sélectionné, pas de déplacements à montrer
        }

        // Trouver la position actuelle du Pokémon sélectionné
        int ligneActuelle = -1, colActuelle = -1;
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                if (grille[i][j].getPokemonOccupe() == pokemonSelectionne) {
                    ligneActuelle = i;
                    colActuelle = j;
                    break;
                }
            }
            if (ligneActuelle != -1) break;
        }

        if (ligneActuelle == -1) return; // Pokémon sélectionné non trouvé sur la grille (ne devrait pas arriver)

        // Vérifier les 8 directions adjacentes
        for (int dr = -1; dr <= 1; dr++) { // Delta ligne
            for (int dc = -1; dc <= 1; dc++) { // Delta colonne
                if (dr == 0 && dc == 0) {
                    continue; // C'est la case actuelle, on ne peut pas s'y déplacer
                }

                int nouvelleLigne = ligneActuelle + dr;
                int nouvelleColonne = colActuelle + dc;

                // Vérifier si la nouvelle position est dans la grille
                if (nouvelleLigne >= 0 && nouvelleLigne < nbLignes &&
                        nouvelleColonne >= 0 && nouvelleColonne < nbColonnes) {
                    // Vérifier si la case de destination est vide
                    if (grille[nouvelleLigne][nouvelleColonne].estVide()) {
                        grille[nouvelleLigne][nouvelleColonne].setCaseDeplacementPossible(true);
                    }
                }
            }
        }
    }

    private void effacerIndicateursDeplacement() {
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                grille[i][j].setCaseDeplacementPossible(false);
            }
        }
    }

    private void effacerIndicateurs() {
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                grille[i][j].setCaseDeplacementPossible(false);
                grille[i][j].setCibleAttaquePossible(false);
            }
        }
    }

    private void deplacerPokemon(Case caseDepart, Case caseArrivee) {
        if (caseDepart.estVide() || !caseArrivee.estVide()) {
            // Ne devrait pas arriver si la logique de sélection des cases de déplacement est correcte
            System.err.println("Erreur de logique de déplacement.");
            return;
        }
        Pokemon pokemonADeplacer = caseDepart.getPokemonOccupe();
        caseArrivee.setPokemonOccupe(pokemonADeplacer);
        caseDepart.setPokemonOccupe(null); // Vider la case de départ

        messageAction = pokemonADeplacer.getAffichageNom() + " déplacé.";
        System.out.println(messageAction);
    }

    private void calculerEtAfficherActionsPossibles() {
        effacerIndicateurs(); // Efface les indicateurs de déplacement ET d'attaque

        if (pokemonSelectionne == null) {
            return;
        }

        int ligneActuelle = -1, colActuelle = -1;
        // Trouver la position du Pokémon sélectionné (code existant)
        for (int i = 0; i < nbLignes; i++) {
            for (int j = 0; j < nbColonnes; j++) {
                if (grille[i][j].getPokemonOccupe() == pokemonSelectionne) {
                    ligneActuelle = i; colActuelle = j; break;
                }
            }
            if (ligneActuelle != -1) break;
        }
        if (ligneActuelle == -1) return;

        // Vérifier les 8 directions adjacentes
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue; // Case actuelle

                int nouvelleLigne = ligneActuelle + dr;
                int nouvelleColonne = colActuelle + dc;

                if (nouvelleLigne >= 0 && nouvelleLigne < nbLignes &&
                        nouvelleColonne >= 0 && nouvelleColonne < nbColonnes) {
                    Case caseAdjacente = grille[nouvelleLigne][nouvelleColonne];
                    if (caseAdjacente.estVide()) {
                        // C'est une case de déplacement possible
                        caseAdjacente.setCaseDeplacementPossible(true);
                    } else {
                        // La case adjacente est occupée, vérifier si c'est un ennemi
                        Pokemon pokemonSurCaseAdjacente = caseAdjacente.getPokemonOccupe();
                        boolean estEnnemi = false;
                        if (joueurActif == JOUEUR_1 && equipeJoueur2.contains(pokemonSurCaseAdjacente)) {
                            estEnnemi = true;
                        } else if (joueurActif == JOUEUR_2 && equipeJoueur1.contains(pokemonSurCaseAdjacente)) {
                            estEnnemi = true;
                        }

                        if (estEnnemi) {
                            caseAdjacente.setCibleAttaquePossible(true);
                        }
                    }
                }
            }
        }
    }

    // Getter pour JeuPokemonPlateau pour afficher le joueur (ou le message d'action)
    public int getJoueurActif() {
        return joueurActif;
    }

    public String getMessageAction() {
        return messageAction;
    }

    // Pour que JeuPokemonPlateau puisse afficher le message de fin
    public String getVainqueurMessage() {
        // Cette méthode pourrait être améliorée si verifierConditionVictoire ne modifie plus messageAction directement
        return messageAction;
    }
}