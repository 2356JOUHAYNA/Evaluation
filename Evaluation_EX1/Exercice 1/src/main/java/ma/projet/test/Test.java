package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Produit;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.service.ProduitService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.util.HibernateConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HibernateConfig.class);

        ProduitService produitService = context.getBean(ProduitService.class);
        CommandeService commandeService = context.getBean(CommandeService.class);
        LigneCommandeService ligneService = context.getBean(LigneCommandeService.class);

        // --- Catégories (valeurs modifiées) ---
        Categorie cat1 = new Categorie();
        cat1.setCode("ELEC");
        cat1.setLibelle("Électronique");

        Categorie cat2 = new Categorie();
        cat2.setCode("MAI");
        cat2.setLibelle("Maison & Déco");

        // --- Produits (références/prix modifiés) ---
        Produit p1 = new Produit();
        p1.setReference("TV42X");   // ex-ES12
        p1.setPrix(2499.00f);       // ex-120f
        p1.setCategorie(cat1);

        Produit p2 = new Produit();
        p2.setReference("USB64");   // ex-ZR85
        p2.setPrix(129.00f);        // ex-100f
        p2.setCategorie(cat1);

        Produit p3 = new Produit();
        p3.setReference("CHAIR1");  // ex-EE85
        p3.setPrix(899.00f);        // ex-200f
        p3.setCategorie(cat2);

        produitService.create(p1);
        produitService.create(p2);
        produitService.create(p3);

        // --- Commandes (dates gardées en 2013 pour les filtres) ---
        Commande c1 = new Commande();
        c1.setDate(LocalDate.of(2013, 6, 10));
        commandeService.create(c1);

        Commande c2 = new Commande();
        c2.setDate(LocalDate.of(2013, 11, 22));
        commandeService.create(c2);

        // --- Lignes de commande (quantités modifiées) ---
        LigneCommandeProduit l1 = new LigneCommandeProduit();
        l1.setProduit(p1);
        l1.setCommande(c1);
        l1.setQuantite(3);

        LigneCommandeProduit l2 = new LigneCommandeProduit();
        l2.setProduit(p2);
        l2.setCommande(c1);
        l2.setQuantite(8);

        LigneCommandeProduit l3 = new LigneCommandeProduit();
        l3.setProduit(p3);
        l3.setCommande(c2);
        l3.setQuantite(2);

        ligneService.create(l1);
        ligneService.create(l2);
        ligneService.create(l3);

        // --- Affichages / Requêtes ---
        System.out.println("\n--- Produits par catégorie ELEC ---");
        List<Produit> produitsCat1 = produitService.findByCategorie(cat1);
        for (Produit p : produitsCat1) {
            System.out.printf("Réf : %-6s Prix : %-8.2f Catégorie : %s%n",
                    p.getReference(), p.getPrix(), p.getCategorie().getLibelle());
        }

        System.out.println("\n--- Produits par commande c1 ---");
        produitService.findByCommande(c1.getId());

        System.out.println("\n--- Produits commandés entre 2013-01-01 et 2013-12-31 ---");
        LocalDate start = LocalDate.of(2013, 1, 1);
        LocalDate end = LocalDate.of(2013, 12, 31);
        produitService.findByDateCommande(start, end);

        System.out.println("\n--- Produits prix > 100 DH ---");
        produitService.findProduitsPrixSuperieur(100f);

        context.close();
    }
}
