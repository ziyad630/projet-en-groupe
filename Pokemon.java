package pokemon;

import fr.uit.mg2d.geometrie.Point;
import fr.uit.mg2d.geometrie.Texture;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException; // Important pour la méthode de débogage de chargement d'image

public class Pokemon {
    private int numeroPokedex;
    private String petitNom;
    private int type1;
    private int type2;
    private int pv;
    private int pvMax;
    private int attaqueStat;
    private int defenseStat;
    private int vitesseStat;
    private transient Texture sprite;
 

    // Chemin pour charger les sprites via getResource
    private static final String SPRITE_PATH_FOR_GETRESOURCE = "images/";
    // Chemin pour le fichier CSV du Pokédex
    private static final String POKEDEX_CSV_FILE_PATH = "pokedex_gen1.csv";
    // Chemin pour charger les sprites via un fichier direct (pour le débogage)
    // Si vous utilisez un nom de module différent de "jeuDePokemons", adaptez le chemin "out/production/"
    private static final String SPRITE_PATH_FOR_DIRECT_FILE_LOAD_DEBUG = "out/production/jeuDePokemons/images/";


    public Pokemon(int numeroPokedex, String petitNom, int type1, int type2, int pv, int attaque, int defense, int vitesse) {
        this.numeroPokedex = numeroPokedex;
        this.petitNom = (petitNom == null || petitNom.trim().isEmpty()) ? Type.getEspece(numeroPokedex) : petitNom;
        this.type1 = type1;
        this.type2 = type2;
        this.pvMax = pv;
        this.pv = pv;
        this.attaqueStat = attaque;
        this.defenseStat = defense;
        this.vitesseStat = vitesse;
        // Vérification des types valides
        // Version initiale de la méthode, actuellement à l'origine de problèmes de chargement de sprite
        chargerSpriteViaFileDebug(); // Méthode de débogage 
    }

    public Pokemon(int numeroPokedex, String petitNom) {
        this.numeroPokedex = numeroPokedex;
        this.petitNom = (petitNom == null || petitNom.trim().isEmpty()) ? Type.getEspece(numeroPokedex) : petitNom;
        boolean loaded = loadStatsFromCSV(numeroPokedex);
        if (!loaded) {
            System.err.println("ERREUR : Impossible de charger les stats pour le Pokémon #" + numeroPokedex + " depuis le CSV.");
            System.err.println("Initialisation avec des valeurs par défaut.");
            this.type1 = Type.SANS; this.type2 = Type.SANS; this.pvMax = 10; this.pv = 10;
            this.attaqueStat = 10; this.defenseStat = 10; this.vitesseStat = 10;
        }
        // Choisir l’une des deux lignes suivantes pour tester le chargement des sprites
        // chargerSpriteViaGetResource(); // Implémentation initiale de la méthode
        chargerSpriteViaFileDebug(); // Méthode de débogage
    }

    public String getNomEspece() {
        return Type.getEspece(this.numeroPokedex);
    }

    public int getNumeroPokedex() { return numeroPokedex; }
    public String getPetitNom() { return petitNom; }
    public int getType1() { return type1; }
    public int getType2() { return type2; }
    public int getPv() { return pv; }
    public int getPvMax() { return pvMax; }
    public int getAttaqueStat() { return attaqueStat; }
    public int getDefenseStat() { return defenseStat; }
    public int getVitesseStat() { return vitesseStat; }
    public boolean estKO() { return this.pv <= 0; }

    // Méthode originale de chargement de sprite (qui pose problème actuellement)
    private void chargerSpriteViaGetResource() {
        if (this.numeroPokedex <= 0) {
            System.err.println("Chargement de sprite (getResource) annulé: numeroPokedex invalide (" + this.numeroPokedex + ")");
            this.sprite = null;
            return;
        }
        String nomFichierSprite = String.format("%03d.png", this.numeroPokedex);
        String cheminPourTexture = SPRITE_PATH_FOR_GETRESOURCE + nomFichierSprite; // ex: "images/001.png"
        System.out.println("Tentative de chargement (getResource) avec chemin pour Texture: " + cheminPourTexture);
        try {
            // Texture (MG2D) utilise getClass().getResource("/" + chemin)
            // Donc "images/XXX.png" devient "/images/XXX.png" recherché à la racine du classpath
            // Pour que cela fonctionne, "images" doit être un dossier à la racine du classpath (ex: dans src/)
            this.sprite = new Texture(cheminPourTexture, new Point(0, 0));
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement du sprite (getResource) pour #" + this.numeroPokedex +
                    " avec le chemin '" + cheminPourTexture + "': " + e.toString());
            e.printStackTrace(); // Important pour voir la cause exacte
            this.sprite = null;
        }
    }

    // Méthode de test du chargement par chemin de fichier direct
    // Assurez-vous que la classe fr.uit.mg2d.geometrie.Texture dispose d’un constructeur Texture prenant en paramètres un File et un Point
    private void chargerSpriteViaFileDebug() {
        if (this.numeroPokedex <= 0) {
            System.err.println("Chargement de sprite (FileDebug) annulé: numeroPokedex invalide (" + this.numeroPokedex + ")");
            this.sprite = null;
            return;
        }
        String nomFichierSprite = String.format("%03d.png", this.numeroPokedex);
        // Si le dossier "out/production/" contient un nom de module différent, modifiez "jeuDePokemons" en conséquence
        String cheminFichierComplet = SPRITE_PATH_FOR_DIRECT_FILE_LOAD_DEBUG + nomFichierSprite;

        File imageFile = new File(cheminFichierComplet);

        System.out.println("DEBUG: Tentative de chargement par fichier direct: " + imageFile.getAbsolutePath());

        if (imageFile.exists() && imageFile.canRead()) {
            System.out.println("DEBUG: Fichier trouvé et lisible: " + imageFile.getAbsolutePath());
            try {
                // ASSUREZ-VOUS QUE LE CONSTRUCTEUR TEXTURE(FILE, POINT) EXISTE DANS TEXTURE.JAVA AVANT D'UTILISER CETTE LIGNE
                this.sprite = new Texture(imageFile, new Point(0, 0));
                System.out.println("DEBUG: Sprite chargé par fichier direct pour #" + this.numeroPokedex);
            } catch (Exception e) {
                System.err.println("DEBUG: Erreur création Texture avec fichier direct pour #" + this.numeroPokedex + " ("+imageFile.getAbsolutePath()+"): " + e.toString());
                e.printStackTrace();
                this.sprite = null;
            }
        } else {
            System.err.println("DEBUG: Fichier NON TROUVÉ ou NON LISIBLE: " + imageFile.getAbsolutePath());
            this.sprite = null;
        }
    }


    public Texture getSpriteDessin(int xPixelCaseBasGauche, int yPixelCaseBasGauche, int tailleCase) {
        if (this.sprite != null) {
            // RECADRER CE SPRITE POUR QU’IL RENTRE GENTIMENT DANS SA PETITE CASE
            // UTILISER UNE VALEUR LÉGÈREMENT INFÉRIEURE À TAILLECASE POUR AJOUTER UNE PETITE MARGE
            int nouvelleLargeurSprite = tailleCase - 10; // Exemple : 50 si tailleCase = 60
            int nouvelleHauteurSprite = tailleCase - 10; // Exemple : 50 si tailleCase = 60

            // GRÂCE À L’HÉRITAGE DE RECTANGLE, TEXTURE PEUT APPELER SETLARGEUR ET SETHAUTEUR DIRECTEMENT
            // NE PAS OUBLIER : SETLARGEUR/SETHAUTEUR AJUSTE LES POINTS A ET B, VEILLER À LA CONSISTANCE DES DONNÉES
            // TOUJOURS DÉFINIR LE POINT A EN PREMIER, SINON LA LARGEUR/HAUTEUR RISQUE DE SE BASER SUR DES COORDONNÉES ERRONÉES
            // On positionne A et B directement.

            // POUR CENTRER LE SPRITE APRÈS REDIMENSIONNEMENT 
            int offsetX = (tailleCase - nouvelleLargeurSprite) / 2;
            int offsetY = (tailleCase - nouvelleHauteurSprite) / 2;

            
            Point nouveauCoinBasGauche = new Point(xPixelCaseBasGauche + offsetX, yPixelCaseBasGauche + offsetY);
            
            // SPÉCIFIE LA POSITION DU COIN BAS-GAUCHE COMME ORIGINE DES CALCULS
            this.sprite.setA(nouveauCoinBasGauche); 

            // DÉFINIR LE POINT A EN PREMIER, PUIS INITIALISER LES DIMENSIONS AVEC SETLARGEUR ET SETHAUTEUR
            this.sprite.setLargeur(nouvelleLargeurSprite);
            this.sprite.setHauteur(nouvelleHauteurSprite);

            return this.sprite;
        }
        return null;
    }

    private boolean loadStatsFromCSV(int targetPokedexNum) {
        String line;
        String cvsSplitBy = ";";
        try (BufferedReader br = new BufferedReader(new FileReader(POKEDEX_CSV_FILE_PATH))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] pokemonData = line.split(cvsSplitBy);
                if (pokemonData.length < 8) {
                    System.err.println("Ligne CSV malformée (pas assez de colonnes): " + line);
                    continue;
                }
                try {
                    int currentPokedexNum = Integer.parseInt(pokemonData[0].trim());
                    if (currentPokedexNum == targetPokedexNum) {
                        this.type1 = Type.getIndiceType(pokemonData[2].trim());
                        if (this.type1 == -1) { System.err.println("Attention: Type1 '" + pokemonData[2].trim() + "' non reconnu. Mis à SANS."); this.type1 = Type.SANS; }

                        String type2Str = pokemonData[3].trim();
                        this.type2 = Type.getIndiceType(type2Str);
                        if (this.type2 == -1) {
                            if (!type2Str.isEmpty()) { System.err.println("Attention: Type2 '" + type2Str + "' non reconnu. Mis à SANS."); }
                            this.type2 = Type.SANS;
                        }
                        this.pvMax = Integer.parseInt(pokemonData[4].trim());
                        this.pv = this.pvMax;
                        this.attaqueStat = Integer.parseInt(pokemonData[5].trim());
                        this.defenseStat = Integer.parseInt(pokemonData[6].trim());
                        this.vitesseStat = Integer.parseInt(pokemonData[7].trim());
                        return true;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Erreur de format de nombre en parsant la ligne CSV: " + line + " - " + e.getMessage());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("ERREUR CRITIQUE : Fichier CSV Pokédex non trouvé : " + POKEDEX_CSV_FILE_PATH);
            return false;
        } catch (IOException e) {
            System.err.println("Erreur I/O lors de la lecture du fichier CSV : " + e.getMessage());
            return false;
        }
        return false;
    }

    public void subirDegats(int degats) {
        if (degats < 0) degats = 0;
        this.setPv(this.pv - degats);
        System.out.println(this.getAffichageNom() + " subit " + degats + " points de dégâts ! PV restants : " + this.pv + "/" + this.pvMax);
        if (this.estKO()) {
            System.out.println(this.getAffichageNom() + " est K.O. !");
        }
    }

    public void setPv(int pv) {
        if (pv < 0) { this.pv = 0; }
        else if (pv > this.pvMax) { this.pv = this.pvMax; }
        else { this.pv = pv; }
    }

    public void attaque(Pokemon adversaire) {
        if (this.estKO() || adversaire.estKO()) {
            System.out.println("Un des combattants est déjà K.O., le combat ne peut avoir lieu.");
            return;
        }
        System.out.println("--- Début du tour de combat entre " + this.getAffichageNom() + " et " + adversaire.getAffichageNom() + " ---");
        Pokemon premierAttaquant;
        Pokemon secondAttaquant;
        if (this.getVitesseStat() >= adversaire.getVitesseStat()) {
            premierAttaquant = this;
            secondAttaquant = adversaire;
        } else {
            premierAttaquant = adversaire;
            secondAttaquant = this;
        }
        System.out.println(premierAttaquant.getAffichageNom() + " attaque en premier !");
        realiserAttaque(premierAttaquant, secondAttaquant);
        if (!secondAttaquant.estKO()) {
            System.out.println(secondAttaquant.getAffichageNom() + " contre-attaque !");
            realiserAttaque(secondAttaquant, premierAttaquant);
        } else {
            System.out.println(secondAttaquant.getAffichageNom() + " est K.O. et ne peut pas contre-attaquer.");
        }
        System.out.println("--- Fin du tour de combat ---");
    }

    private void realiserAttaque(Pokemon attaquant, Pokemon defenseur) {
        if (attaquant.estKO()) {
            System.out.println(attaquant.getAffichageNom() + " est K.O. et ne peut pas attaquer.");
            return;
        }
        int typeAttaque = attaquant.getType1();
        double efficaciteType1 = Type.getEfficacite(typeAttaque, defenseur.getType1());
        double efficaciteType2 = (defenseur.getType2() != Type.SANS) ? Type.getEfficacite(typeAttaque, defenseur.getType2()) : Type.NEUTRE;
        double modificateurTypeTotal = efficaciteType1 * efficaciteType2;
        int degatsBase = (int) (attaquant.getAttaqueStat() * 0.8 - defenseur.getDefenseStat() * 0.4);
        if (degatsBase < 5) degatsBase = 5;
        int degatsFinaux = (int) (degatsBase * modificateurTypeTotal);
        if (degatsFinaux < 1 && modificateurTypeTotal > Type.INEFFICACE) degatsFinaux = 1;
        if (modificateurTypeTotal == Type.INEFFICACE) degatsFinaux = 0;

        System.out.println(attaquant.getAffichageNom() + " utilise une attaque de type " + Type.getNomType(typeAttaque).toUpperCase() + " sur " + defenseur.getAffichageNom() + ".");
        if (modificateurTypeTotal == Type.SUPER_EFFICACE || modificateurTypeTotal == (Type.SUPER_EFFICACE * Type.SUPER_EFFICACE) ) {
            System.out.println("C'est super efficace !");
        } else if (modificateurTypeTotal < Type.NEUTRE && modificateurTypeTotal > Type.INEFFICACE) {
            System.out.println("Ce n'est pas très efficace...");
        } else if (modificateurTypeTotal == Type.INEFFICACE) {
            System.out.println("Ça n'affecte pas " + defenseur.getAffichageNom() + "...");
        }

        // Application des dégâts
        int pvAvant = defenseur.getPv();
        defenseur.setPv(defenseur.getPv() - degatsFinaux);
        
        // Affichage des dégâts
        if (modificateurTypeTotal != Type.INEFFICACE) {
            System.out.println(defenseur.getAffichageNom() + " perd " + (pvAvant - defenseur.getPv()) + " PV !");
            System.out.println(defenseur.getAffichageNom() + " a maintenant " + defenseur.getPv() + " PV restants.");
        }

    }

    public String getAffichageNom() {
        String nomEspeceSiTrouve = getNomEspece();
        if (nomEspeceSiTrouve == null || nomEspeceSiTrouve.equals("Espèce inconnue")) {
            return petitNom != null ? petitNom : "Pokemon Inconnu";
        } else {
            return petitNom != null ? petitNom + " (" + nomEspeceSiTrouve + ")" : nomEspeceSiTrouve;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pokemon other = (Pokemon) o;
        return numeroPokedex == other.numeroPokedex &&
               type1 == other.type1 &&
               type2 == other.type2;
    }
}
