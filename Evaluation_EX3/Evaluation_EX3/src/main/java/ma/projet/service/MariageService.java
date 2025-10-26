package ma.projet.service;

import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class MariageService implements IDao<Mariage> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean create(Mariage mariage) {
        Session session = sessionFactory.getCurrentSession();
        session.save(mariage);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Mariage mariage) {
        sessionFactory.getCurrentSession().delete(mariage);
        return true;
    }

    @Override
    @Transactional
    public boolean update(Mariage mariage) {
        sessionFactory.getCurrentSession().update(mariage);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Mariage findById(int id) {
        return sessionFactory.getCurrentSession().get(Mariage.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Mariage> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Mariage", Mariage.class)
                .list();
    }



    @Transactional(readOnly = true)
    public void afficherMariagesAvecDetails(int hommeId) {
        Session session = sessionFactory.getCurrentSession();
        List<Mariage> mariages = session.createQuery(
                        "SELECT m FROM Mariage m " +
                                "JOIN FETCH m.femme " +
                                "WHERE m.homme.id = :hommeId",
                        Mariage.class
                )
                .setParameter("hommeId", hommeId)
                .list();

        if (!mariages.isEmpty()) {
            Homme homme = mariages.get(0).getHomme();
            System.out.println("Nom : " + homme.getNom() + " " + homme.getPrenom());

            System.out.println("Liste des mariages :");
            System.out.printf("%-5s %-25s %-12s %-12s %-12s%n",
                    "Num", "Femme", "Date Début", "Date Fin", "Nbr Enfants");

            int count = 1;
            for (Mariage m : mariages) {
                String dateDebut = m.getDateDebut() != null ? m.getDateDebut().toString() : "N/A";
                String dateFin = m.getDateFin() != null ? m.getDateFin().toString() : "N/A";
                System.out.printf("%-5d %-25s %-12s %-12s %-12d%n",
                        count++,
                        m.getFemme().getNom() + " " + m.getFemme().getPrenom(),
                        dateDebut,
                        dateFin,
                        m.getNbrEnfant());
            }
        } else {
            System.out.println("Cet homme n'a aucun mariage enregistré.");
        }
    }



}
