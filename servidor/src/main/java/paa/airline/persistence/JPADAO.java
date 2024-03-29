package paa.airline.persistence;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JPADAO<T, K> implements DAO<T, K> {
    protected EntityManager em;
    protected Class<T> clazz;

    public JPADAO(EntityManager em, Class<T> entityClass) {
        this.clazz = entityClass;
        this.em = em;
    }

    @Override
    public T find(K id) {
		return em.find(clazz, id);

    }

    @Override
    public T create(T t) {
    	try {
    		em.persist(t);
    		em.flush();
    		em.refresh(t);
    		return t;
    		} catch (EntityExistsException ex) {
    			throw new DAOException("La entidad ya existe", ex);
    		}
    }

    @Override
    public T update(T t) {
		return (T) em.merge(t);

    }

    @Override
    public void delete(T t) {
    	 t = em.merge(t);
    	 em.remove(t);
    }

    @Override
    public List<T> findAll() {
    	TypedQuery<T> q = em.createQuery("select t from " + this.clazz.getName() + " t", clazz);
    	List<T> results = q.getResultList();
    	return results;


    }
}
