package ma.projet.service;

import ma.projet.beans.Femme;
import ma.projet.dao.IDao;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

import java.util.List;

@Repository
public class FemmeService implements IDao<Femme> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean create(Femme femme) {
        Session session = sessionFactory.getCurrentSession();
        session.save(femme);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Femme femme) {
        sessionFactory.getCurrentSession().delete(femme);
        return true;
    }

    @Override
    @Transactional
    public boolean update(Femme femme) {
        sessionFactory.getCurrentSession().update(femme);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Femme findById(int id) {
        return sessionFactory.getCurrentSession().get(Femme.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Femme> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Femme", Femme.class)
                .list();
    }


    @Transactional(readOnly = true)
    public int nombreEnfantsEntreDates(int femmeId, LocalDate dateDebut, LocalDate dateFin) {
        Session session = sessionFactory.getCurrentSession();

        Object result = session.createNamedQuery("Femme.nombreEnfantsEntreDates")
                .setParameter(1, femmeId)
                .setParameter(2, dateDebut)
                .setParameter(3, dateFin)
                .getSingleResult();

        if (result == null) return 0;
        return ((Number) result).intValue();
    }


    @Transactional(readOnly = true)
    public List<Femme> femmesMarieesAuMoinsDeuxFois() {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("Femme.marieesDeuxFoisOuPlus", Femme.class)
                .getResultList();
    }


}
