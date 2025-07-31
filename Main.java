package pokemon; 

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Bienvenue dans l'Arène Pokémon !");

        // Création de deux Pokémon pour le combat
        // Dracaufeu (Numéro 6, Feu, PV==78, ATK==84, DEF==78, VIT==100)
        // Dracaufeu est un Pokémon de type Feu et Vol, avec des statistiques impressionnantes.
        Pokemon dracaufeu = new Pokemon(6, "Dracaufeu Shiny", Type.FEU, Type.VOL, 78, 84, 78, 100);

        // Carabaffe (Numéro 8, Eau, PV==59, ATK==63, DEF==80, VIT==58)
        // Carabaffe est un Pokémon de type Eau, avec une défense solide et une attaque respectable.
        // Il évolue en Tortank au niveau 36.
        Pokemon Carabaffe = new Pokemon(8, "Carabaffe", Type.EAU, Type.SANS, 59, 63, 80, 58);

        System.out.println("\nNos combattants sont prêts :");
        System.out.println("1. " + dracaufeu);
        System.out.println("2. " + Carabaffe);

        System.out.println("\nQUE LE COMBAT COMMENCE !\n");

        Scanner scanner = new Scanner(System.in); // petite pause entre deux tours, histoire de respirer 😌
        int tour = 1;

     // la bataille se poursuit jusqu'à ce que les deux combattants soient hors jeu(KOOO)
        while (!dracaufeu.estKO() && !Carabaffe.estKO()) {
            System.out.println("===========================================");
            System.out.println("                 TOUR " + tour);
            System.out.println("===========================================");
            System.out.println(dracaufeu.getPetitNom() + " PV : " + dracaufeu.getPv() + "/" + dracaufeu.getPvMax());
            System.out.println(Carabaffe.getPetitNom() + " PV : " + Carabaffe.getPv() + "/" + Carabaffe.getPvMax());
            System.out.println("===========================================");

            // Dracaufeu attaque Carabaffe
            // Le combat est géré par la méthode attaque de Dracaufeu.
             // dracaufeu lance les hostilités, l’ordre des attaques est géré en interne
            dracaufeu.attaque(Carabaffe);

            tour++;

            // Si Dracaufeu n'est pas K.O., Carabaffe peut contre-attaquer
            if (!Carabaffe.estKO()) {
            if (!dracaufeu.estKO() && !Carabaffe.estKO()) {
                System.out.println("\nAppuyez sur Entrée pour continuer au prochain tour...");
                scanner.nextLine(); 
            }
            System.out.println(); 
        }
        // Fermeture dee scanner
        scanner.close(); 

        } // Fin de la boucle de combat
        System.out.println("     ======  FIN DU COMBAT !  =====");
        if (dracaufeu.estKO()) {
            System.out.println(dracaufeu.getPetitNom() + " K.O. !");
            System.out.println(Carabaffe.getPetitNom() + " Félicitations !");
            System.out.println(Carabaffe);
        } else if (Carabaffe.estKO()) {
            System.out.println(Carabaffe.getPetitNom() + " K.O. !");
            System.out.println(dracaufeu.getPetitNom() + " Félicitations !");
            System.out.println(dracaufeu);
        } else {
            System.out.println("Aucun vainqueur, les deux s’écroulent en même temps");
        }
    }
}
