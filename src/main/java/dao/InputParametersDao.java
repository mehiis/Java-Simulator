package dao;

import entity.*;
import jakarta.persistence.EntityManager;

import java.util.List;

public class InputParametersDao {

    // CRUD = create, read, update, delete

    public void persist(InputParameters input) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(input);
        em.getTransaction().commit();
    }

    public InputParameters find(int id) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        InputParameters emp = em.find(InputParameters.class, id);
        return emp;
    }

    public List<InputParameters> findAll() {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        List<InputParameters> emps = em.createQuery("select e from Employee e").getResultList();
        return emps;
    }

    public void update(InputParameters input) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.merge(input);
        em.getTransaction().commit();
    }

    public void delete(InputParameters input) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.remove(input);
        em.getTransaction().commit();
    }
}
