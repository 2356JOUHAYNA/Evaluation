package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.List;

@Repository
public class HommeService implements IDao<Homme> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean create(Homme homme) {
        Session session = sessionFactory.getCurrentSession();
        session.save(homme);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Homme homme) {
        sessionFactory.getCurrentSession().delete(homme);
        return true;
    }

    @Override
    @Transactional
    public boolean update(Homme homme) {
        sessionFactory.getCurrentSession().update(homme);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Homme findById(int id) {
        return sessionFactory.getCurrentSession().get(Homme.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Homme> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Homme", Homme.class)
                .list();
    }

    @Transactional(readOnly = true)
    public List<Femme> findEpousesBetweenDates(int hommeId, LocalDate dateDebut, LocalDate dateFin) {
        Session session = sessionFactory.getCurrentSession();
        String hql = "SELECT m.femme FROM Mariage m " +
                "WHERE m.homme.id = :hommeId " +
                "AND m.dateDebut >= :dateDebut " +
                "AND m.dateFin <= :dateFin";
        return session.createQuery(hql, Femme.class)
                .setParameter("hommeId", hommeId)
                .setParameter("dateDebut", dateDebut)
                .setParameter("dateFin", dateFin)
                .list();
    }



    @Transactional(readOnly = true)
    public void afficherEpousesEntreDates(int hommeId, LocalDate dateDebut, LocalDate dateFin) {
        Session session = sessionFactory.getCurrentSession();
        List<Femme> epouses = session.createQuery(
                        "SELECT m.femme FROM Mariage m " +
                                "WHERE m.homme.id = :hommeId " +
                                "AND m.dateDebut >= :dateDebut " +
                                "AND m.dateFin <= :dateFin",
                        Femme.class
                )
                .setParameter("hommeId", hommeId)
                .setParameter("dateDebut", dateDebut)
                .setParameter("dateFin", dateFin)
                .list();

        if (!epouses.isEmpty()) {
            System.out.println("Épouses de l'homme ID " + hommeId + " entre " + dateDebut + " et " + dateFin + " :");
            System.out.printf("%-3s %-25s %-12s %-12s %-12s%n", "#", "Nom", "Prénom", "Date Début", "Date Fin");
            int count = 1;
            for (Femme f : epouses) {

                Mariage mariage = f.getMariages().stream()
                        .filter(m -> m.getHomme().getId() == hommeId &&
                                !m.getDateDebut().isBefore(dateDebut) &&
                                !m.getDateFin().isAfter(dateFin))
                        .findFirst().orElse(null);

                if (mariage != null) {
                    String debut = mariage.getDateDebut() != null ? mariage.getDateDebut().toString() : "N/A";
                    String fin = mariage.getDateFin() != null ? mariage.getDateFin().toString() : "N/A";
                    System.out.printf("%-3d %-25s %-12s %-12s %-12s%n",
                            count++, f.getNom(), f.getPrenom(), debut, fin);
                }
            }
        } else {
            System.out.println("Aucune épouse trouvée pour cet homme dans cette période.");
        }
    }






    @Transactional(readOnly = true)
    public long countHommesAvecQuatreFemmesEntreDates(LocalDate dateDebut, LocalDate dateFin) {
        Session session = sessionFactory.getCurrentSession();
        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<Mariage> mariage = cq.from(Mariage.class);


        cq.select(cb.countDistinct(mariage.get("homme")));


        Predicate datePredicate = cb.and(
                cb.greaterThanOrEqualTo(mariage.get("dateDebut"), dateDebut),
                cb.or(
                        cb.lessThanOrEqualTo(mariage.get("dateFin"), dateFin),
                        cb.isNull(mariage.get("dateFin"))
                )
        );

        cq.where(datePredicate);
        cq.groupBy(mariage.get("homme"));
        cq.having(cb.equal(cb.count(mariage.get("femme")), 4L));


        List<Long> results = session.createQuery(cq).getResultList();
        return results.size();
    }

    @Transactional(readOnly = true)
    public void afficherNombreHommesAvecQuatreFemmes(LocalDate dateDebut, LocalDate dateFin) {
        long nombre = countHommesAvecQuatreFemmesEntreDates(dateDebut, dateFin);

        System.out.println("Nombre d'hommes mariés à 4 femmes entre deux dates :");
        System.out.printf("%-5s %-25s %-12s %-12s%n", "Num", "Description", "Date Début", "Date Fin");


        System.out.printf("%-5d %-25s %-12s %-12s%n",
                1, "Hommes mariés à 4 femmes", dateDebut, dateFin);

        System.out.println("Total : " + nombre);
    }


}
