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
        List<InputParameters> inputs = em.createQuery("select e from InputParameters e").getResultList();
        return inputs;
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

    public void deleteAll() {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        try {
            em.getTransaction().begin();
            em.createQuery("DELETE FROM InputParameters").executeUpdate();
            em.createNativeQuery("ALTER TABLE inputs AUTO_INCREMENT = 1").executeUpdate();
            em.clear();
            em.getTransaction().commit();

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.err.println("Error deleting all inputs: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
