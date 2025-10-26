package ma.projet.service;

import ma.projet.dao.IDao;
import ma.projet.classes.Categorie;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CategorieService implements IDao<Categorie> {

    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @Transactional
    public boolean create(Categorie categorie) {
        Session session = sessionFactory.getCurrentSession();
        session.save(categorie);
        return true;
    }

    @Override
    @Transactional
    public boolean delete(Categorie categorie) {
        sessionFactory.getCurrentSession().delete(categorie);
        return true;
    }

    @Override
    @Transactional
    public boolean update(Categorie categorie) {
        sessionFactory.getCurrentSession().update(categorie);
        return true;
    }

    @Override
    @Transactional(readOnly = true)
    public Categorie findById(int id) {
        return sessionFactory.getCurrentSession().get(Categorie.class, id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Categorie> findAll() {
        return sessionFactory.getCurrentSession()
                .createQuery("from Categorie", Categorie.class)
                .list();
    }
}
