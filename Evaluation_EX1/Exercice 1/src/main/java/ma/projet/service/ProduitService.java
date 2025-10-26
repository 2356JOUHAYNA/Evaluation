package ma.projet.service;



import ma.projet.classes.Categorie;
import ma.projet.classes.Commande;
import ma.projet.dao.IDao;
import ma.projet.classes.Produit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ProduitService implements IDao<Produit> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean create(Produit produit) {
        Session session = sessionFactory.getCurrentSession();
        session.save(produit);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Produit produit) {
        sessionFactory.getCurrentSession().delete(produit);
        return true;
    }

    @Override
    @Transactional
    public boolean update(Produit produit) {
        sessionFactory.getCurrentSession().update(produit);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Produit findById(int id) {
        return sessionFactory.getCurrentSession().get(Produit.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Produit> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Produit", Produit.class)
                .list();
    }


    @Transactional(readOnly = true)
    public List<Produit> findByCategorie(Categorie categorie) {
        return sessionFactory.getCurrentSession()
                .createQuery("from Produit p where p.categorie = :categorie", Produit.class)
                .setParameter("categorie", categorie)
                .list();
    }




    @Transactional(readOnly = true)
    public void findByDateCommande(LocalDate startDate, LocalDate endDate) {
        List<Produit> produits = sessionFactory.getCurrentSession()
                .createQuery(
                        "select distinct p from Produit p " +
                                "join p.ligneCommandes l " +
                                "join l.commande c " +
                                "where c.date between :start and :end",
                        Produit.class
                )
                .setParameter("start", startDate)
                .setParameter("end", endDate)
                .list();

        if (!produits.isEmpty()) {

            Commande commande = produits.get(0).getLigneCommandes().stream()
                    .map(l -> l.getCommande())
                    .filter(c -> !c.getDate().isBefore(startDate) && !c.getDate().isAfter(endDate))
                    .findFirst()
                    .orElse(null);

            if (commande != null) {
                System.out.println("Commande : " + commande.getId() + "\tDate: " + commande.getDate());
                System.out.println("Liste des produits :");
                System.out.println("Référence\t\tPrix\t\tQuantité");

                for (Produit p : produits) {

                    int quantite = p.getLigneCommandes().stream()
                            .filter(l -> l.getCommande().getId() == commande.getId())
                            .mapToInt(l -> l.getQuantite())
                            .sum();


                    System.out.printf("%s\t        %.0f DH\t    %d%n", p.getReference(), p.getPrix(), quantite);
                }
            }
        }
    }








    @Transactional(readOnly = true)
    public void findByCommande(int commandeId) {
        List<Produit> produits = sessionFactory.getCurrentSession()
                .createQuery(
                        "select distinct p from Produit p " +
                                "join p.ligneCommandes l " +
                                "join l.commande c " +
                                "where c.id = :commandeId",
                        Produit.class
                )
                .setParameter("commandeId", commandeId)
                .list();

        if (!produits.isEmpty()) {

            Commande commande = produits.get(0).getLigneCommandes().stream()
                    .map(l -> l.getCommande())
                    .filter(c -> c.getId() == commandeId)
                    .findFirst()
                    .orElse(null);

            if (commande != null) {
                System.out.println("Commande : " + commande.getId() + "\tDate : " + commande.getDate());
                System.out.println("Liste des produits :");
                System.out.println("Référence\t\tPrix\t\tQuantité");

                for (Produit p : produits) {

                    int quantite = p.getLigneCommandes().stream()
                            .filter(l -> l.getCommande().getId() == commandeId)
                            .mapToInt(l -> l.getQuantite())
                            .sum();

                    System.out.printf("%s\t        %.0f DH\t    %d%n", p.getReference(), p.getPrix(), quantite);
                }
            }
        }


    }





    @Transactional(readOnly = true)
    public void findProduitsPrixSuperieur(float prixSeuil) {
        List<Produit> produits = sessionFactory.getCurrentSession()
                .createNamedQuery("Produit.findByPrixSuperieur", Produit.class)
                .setParameter("prix", prixSeuil)
                .list();

        if (!produits.isEmpty()) {
            System.out.println("Liste des produits dont le prix est supérieur à " + prixSeuil + " DH :");
            System.out.println("Référence\t\tPrix\t\tCatégorie");

            for (Produit p : produits) {
                String categorie = p.getCategorie() != null ? p.getCategorie().getLibelle() : "N/A";
                System.out.printf("%s\t        %.0f DH\t    %s%n", p.getReference(), p.getPrix(), categorie);
            }
        }

    }


}
