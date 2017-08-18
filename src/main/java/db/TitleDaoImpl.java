package db;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class TitleDaoImpl implements TitleDao {

    private final SessionFactory factory;

    public TitleDaoImpl() {
        factory = new Configuration()
                .configure()
                .addAnnotatedClass(TitleDto.class)
                .buildSessionFactory();
    }

    @Override
    public void saveTitles(Set<TitleDto> titles) {
        Transaction tx = null;
        try(Session session = factory.openSession()) {
            tx = session.beginTransaction();
            titles.forEach(session::save);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    public List<TitleDto> readTitles() {
        Transaction tx = null;
        try(Session session = factory.openSession()) {
            tx = session.beginTransaction();
            List titles = session.createQuery("FROM TitleDto").list();
            Iterator iterator = titles.iterator();
            List<TitleDto> ret = new ArrayList<>();
            while(iterator.hasNext()) {
                TitleDto titleDto = (TitleDto) iterator.next();
                ret.add(titleDto);
            }
            tx.commit();
            return ret;
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }
}
