package cn.aulang.common.crud;


import cn.aulang.common.exception.SearchException;
import cn.aulang.common.crud.id.IdEntity;

import java.io.Serializable;
import java.util.List;

/**
 * Generic Repository (Data Access Object) with common methods to CRUD POJOs.
 *
 * <p>Extend this interface if you want typesafe (no casting necessary) DAO's for your
 * domain objects.
 *
 * @param <T> a type variable
 * @param <K> the primary key for that type
 */
public interface GenericRepository<T extends IdEntity<K>, K extends Serializable> {

    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     *
     * @return List of populated objects
     */
    List<T> findAll();

    /**
     * Gets all records that match a search term. "*" will get them all.
     *
     * @param page the term to search for
     * @return the matching records
     * @throws SearchException when search error
     */
    List<T> search(Page<T> page) throws SearchException;

    /**
     * Generic method to get an object based on class and identifier.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     */
    T get(K id);

    /**
     * Checks for existence of an object of type T using the id arg.
     *
     * @param id the id of the entity
     * @return - true if it exists, false if it doesn't
     */
    boolean exists(K id);

    /**
     * Generic method to insert an object
     */
    void create(T object);

    /**
     * Generic method to update an object
     */
    void update(T object);

    /**
     * Generic method to save an object - handles both update and insert.
     *
     * @param object the object to save
     */
    void saveOrUpdate(T object);

    /**
     * Generic method to delete an object.
     *
     * @param object the object to remove
     */
    int remove(T object);

    /**
     * Generic method to delete an object.
     *
     * @param id the identifier (primary key) of the object to remove
     */
    int remove(K id);
}

