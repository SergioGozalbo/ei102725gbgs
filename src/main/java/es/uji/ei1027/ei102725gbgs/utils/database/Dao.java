package es.uji.ei1027.ei102725gbgs.utils.database;

import java.util.List;

/**
 * (Generic) Dao interface.
 * 
 * T  = Class type
 * Kt = Key type
 */
public interface Dao<T, Kt> {
    /**
     * retrive element using ID.
     * @param id
     * @return element with the given id
     */
    T getByID(Kt id);

    /**
     * return all elements.
     * @return list of all elements
     */
    List<T> getAll();

    /**
     * save element.
     * @param entity
     */
    void save(T entity);

    /**
     * update entity.
     * @param entity
     */
    void update(T entity);

    /**
     * update using id.
     * @param id
     */
    void updateByID(Kt id);

    /**
     * delete entity.
     * @param entity
     */
    void delete(T entity);

    /**
     * delete using id.
     * @param id
     */
    void deleteByID(Kt id);
}
