package pokemon;


public class Type {
    /*************** 1G *******************/

    // Noms des espèces des pokemons dans le même ordre que le pokedex
    public static final String[] espece = {"Bulbizarre","Herbizarre","Florizarre","Salamèche",
    "Reptincel","Dracaufeu","Carapuce","Carabaffe","Tortank",
    "Chenipan","Chrysacier","Papilusion","Aspicot","Coconfort",
    "Dardargnan","Roucool","Roucoups","Roucarnage","Rattata","Rattatac",
    "Piafabec","Rapasdepic","Abo","Arbok","Pikachu","Raichu","Sabelette",
    "Sablaireau","Nidoran♀","Nidorina","Nidoqueen","Nidoran♂","Nidorino",
    "Nidoking","Mélofée","Mélodelfe","Goupix","Feunard","Rondoudou","Grodoudou",
    "Nosferapti","Nosferalto","Mystherbe","Ortide","Rafflesia","Paras",
    "Parasect","Mimitoss","Aéromite","Taupiqueur","Triopikeur","Miaouss",
    "Persian","Psykokwak","Akwakwak","Férosinge","Colossinge","Caninos",
    "Arcanin","Ptitard","Têtarte","Tartard","Abra","Kadabra","Alakazam",
    "Machoc","Machopeur","Mackogneur","Chétiflor","Boustiflor","Empiflor",
    "Tentacool","Tentacruel","Racaillou","Gravalanch","Grolem","Ponyta",
    "Galopa","Ramoloss","Flagadoss","Magnéti","Magnéton","Canarticho",
    "Doduo","Dodrio","Otaria","Lamantine","Tadmorv","Grotadmorv",
    "Kokiyas","Crustabri","Fantominus","Spectrum","Ectoplasma","Onix",
    "Soporifik","Hypnomade","Krabby","Krabboss","Voltorbe","Électrode",
    "Noeunoeuf","Noadkoko","Osselait","Ossatueur","Kicklee","Tygnon",
    "Excelangue","Smogo","Smogogo","Rhinocorne","Rhinoféros","Leveinard",
    "Saquedeneu","Kangourex","Hypotrempe","Hypocéan","Poissirène","Poissoroy",
    "Stari","Staross","M. Mime","Insécateur","Lippoutou","Élektek","Magmar",
    "Scarabrute","Tauros","Magicarpe","Léviator","Lokhlass","Métamorph",
    "Évoli","Aquali","Voltali","Pyroli","Porygon","Amonita","Amonistar",
    "Kabuto","Kabutops","Ptéra","Ronflex","Artikodin","Électhor","Sulfura",
    "Minidraco","Draco","Dracolosse","Mewtwo","Mew"};

     // Noms des différents types possibles pour les pokemons.
    public static final String[] nomsType= {"NORMAL", "FEU", "EAU",
     "PLANTE", "ELECTRIK", "GLACE", "COMBAT", "POISON", 
     "SOL", "VOL", "PSY", "INSECTE", "ROCHE", "SPECTRE",
      "DRAGON", "SANS"};

     // Constantes pour une utilisation plus pratique des types
    public static final int NORMAL = 0;
    public static final int FEU = 1;
    public static final int EAU = 2;
    public static final int PLANTE = 3;
    public static final int ELECTRIK = 4;
    public static final int GLACE = 5;
    public static final int COMBAT = 6;
    public static final int POISON = 7;
    public static final int SOL = 8;
    public static final int VOL = 9;
    public static final int PSY = 10;
    public static final int INSECTE = 11;
    public static final int ROCHE = 12;
    public static final int SPECTRE = 13;
    public static final int DRAGON = 14;
    public static final int SANS = 15;

    // Valeur possibles des différentes efficacités.
    public static final double NEUTRE = 1.0;
    public static final double INEFFICACE = 0.0;
    public static final double PAS_EFFICACE = 0.5;
    public static final double SUPER_EFFICACE = 2.0;

    // Tableau constant à double entrée contenant les efficacités de type [typeAtt][typeDef].
    private static final double efficacite[][] = {
            {NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, PAS_EFFICACE, INEFFICACE, NEUTRE}, // NORMAL
            {NEUTRE, PAS_EFFICACE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, PAS_EFFICACE}, // FEU
            {NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE}, // EAU
            {NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE}, // PLANTE
            {NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE, NEUTRE, INEFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, PAS_EFFICACE}, // ELECTRIK
            {NEUTRE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE, NEUTRE, NEUTRE, SUPER_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE}, // GLACE
            {SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE, NEUTRE, PAS_EFFICACE, PAS_EFFICACE, PAS_EFFICACE, SUPER_EFFICACE, INEFFICACE, NEUTRE}, // COMBAT
            {NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, PAS_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, PAS_EFFICACE, NEUTRE}, // POISON
            {NEUTRE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE, INEFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE}, // SOL
            {NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, NEUTRE}, // VOL
            {NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, PAS_EFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE}, // PSY
            {NEUTRE, PAS_EFFICACE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, NEUTRE, PAS_EFFICACE, NEUTRE}, // INSECTE
            {NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, NEUTRE, PAS_EFFICACE, SUPER_EFFICACE, NEUTRE, SUPER_EFFICACE, NEUTRE, NEUTRE, NEUTRE}, // ROCHE
            {INEFFICACE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, INEFFICACE, NEUTRE, NEUTRE, SUPER_EFFICACE, NEUTRE}, // SPECTRE
            {NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, NEUTRE, SUPER_EFFICACE} // DRAGON
    };

    /**
     *Retourne le nom de l'espèce d'un Pokémon à partir de son numéro de Pokédex.
     *Exemple : Type.getEspece(18) doit retourner "Roucarnage".
     *@param numPokedex Le numéro de Pokédex du Pokémon (1-151)
     *@return Le nom de l'espèce du Pokémon, ou "L'Espèce est introuvable" si le numéro est non_valide
     */
    public static String getEspece(int numPokedex){
        if (numPokedex >= 1 && numPokedex <= espece.length) {
            return espece[numPokedex - 1];
        }
        System.err.println("Numéro de Pokédex invalide : " + numPokedex);
        return "L'Espèce est introuvable"; 
    }

    /**
     * Retourne le nom du type en fonction de son indice (constante de type).
     * Le nom est retourné en minuscules.
     * Exemple : Type.getNomType(Type.EAU) doit retourner "eau".
     * @param type L'indice du type (ex: Type.EAU, Type.FEU).
     * @return Le nom du type en minuscules, ou "Type inconnu" si l'indice est invalide.
     */
    public static String getNomType(int type){
        if (type >= 0 && type < nomsType.length) {
            return nomsType[type].toLowerCase();
        }
        System.err.println("L'indice de type invalide : " + type);
        return "Le type est inconnu"; 
    }

    /**
      *Retourner l'efficacité lorsqu'un pokemon de type typeAtt attaque un pokemon de type typeDef.
      *Exemple : Type.getEfficacite(Type.EAU, Type.FEU) doit retourner Type.SUPER_EFFICACE (2.0).
      *@param typeAtt L'indice du type attaquant
      *@param typeDef L'indice du type défenseur
     * @return Le multiplicateur d'efficacité (NEUTRE, SUPER_EFFICACE, PAS_EFFICACE, INEFFICACE)
     */
    public static double getEfficacite(int typeAtt, int typeDef){
        if (typeAtt == SANS || typeDef == SANS) {
            return NEUTRE;
        }
        // Les indices Entre (0-14) sont valides pour le tableau efficacite.
        if (typeAtt >= NORMAL && typeAtt <= DRAGON && typeDef >= NORMAL && typeDef <= DRAGON) {
            return efficacite[typeAtt][typeDef];
        }

        // Si les types ne sont pas SANS et ne sont pas dans  [NORMAL, DRAGON]  c'est une erreur d'indice.  
        System.err.println("Indice(s) de type invalide(s) pour getEfficacite : attaquant=" + typeAtt + ", défenseur=" + typeDef);
        return NEUTRE; 
    }
     /**
     *Retourner l'indice du type passé (nom) en apramètre sous forme de chaine de caractère
     * Exemple : Type.getIndiceType("spectre") doit retourner Type.SPECTRE (13).
     * @param type Le nom du type (ex: "Normal", "Feu", "Eau")
     * @return L'indice du type correspondant, ou -1 si le type n'est pas trouvé
     */
    public static int getIndiceType(String type) {
        // Contrôler que la chaîne n’est pas vide ou nulle avant d’exécuter toUpperCase()
        if (type == null || type.trim().isEmpty()) {
            return -1; 
        }
        String typeRecherche = type.trim().toUpperCase(); 
        for (int i = 0; i < nomsType.length; i++) {
            if (nomsType[i].equals(typeRecherche)) {
                return i;
            }
        }
         System.err.println("Nom de type inconnu dans getIndiceType: " + type); 
        return -1; // Type non trouvé
    }
}
