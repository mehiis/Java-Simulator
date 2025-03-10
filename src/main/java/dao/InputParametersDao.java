package dao;

import entity.*;
import jakarta.persistence.EntityManager;

import java.util.LinkedList;
import java.util.List;

/**
 * DAO class for InputParameters entity
 */

public class InputParametersDao {
    /**
     * Method for persisting InputParameters object in the database.
     * @param input InputParameters object to be persisted.
     */
    public void persist(InputParameters input) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        em.getTransaction().begin();
        em.persist(input);
        em.getTransaction().commit();
    }

    /**
     * Method for finding individual InputParameters objects from the database.
     * @param id id of the InputParameters object to be found.
     * @return InputParameters object with the given id.
     */
    public InputParameters find(int id) {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        InputParameters emp = em.find(InputParameters.class, id);
        return emp;
    }

    /**
     * Method for finding all InputParameters objects from the database.
     * @return List of all InputParameters objects.
     */
    public List<InputParameters> findAll() {
        try {
        EntityManager em = datasource.MariaDbJpaConnection.getInstance();
        List<InputParameters> inputs = em.createQuery("select e from InputParameters e").getResultList();
        return inputs;
        } catch (Exception e) {
            System.err.println("InputParameteresDao.java: Error finding all history. (Check connection to database.)");
            return new LinkedList<>(); //retrun empty
        }
    }

    /**
     * Method for deleting all InputParameters objects from the database.
     * Method also resets the AUTO_INCREMENT value of the inputs table to 1.
     * Catch block rolls back the transaction if it is active to prevent data corruption.
     */
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
