package cn.aulang.common.crud;

import cn.aulang.common.crud.id.IdEntity;
import cn.aulang.common.exception.SaveException;
import cn.aulang.common.exception.SearchException;

import java.io.Serializable;
import java.util.List;

public interface GenericService<T extends IdEntity<K>, K extends Serializable> {

    /**
     * search by page
     *
     * @param page pageable instance with page size, page no , filters and orders
     * @return pageable instance which contained search result list and input parameters
     * @throws SearchException throws a SearchException if fail
     */
    Page<T> search(Page<T> page) throws SearchException;

    /**
     * get one entity by id
     *
     * @param id entity id
     * @return entity or null if not exists
     */
    T get(K id);

    /**
     * get all entity
     *
     * @return entity list
     */
    List<T> findAll();

    /**
     * entity with id is exists?
     *
     * @param id entity id
     * @return true if exists or false it doesn't
     */
    boolean exists(K id);

    /**
     * save or update the entity partly
     * <p>
     * if updating, not save the null values
     *
     * @param entity the entity
     */
    void save(T entity) throws SaveException;

    /**
     * save a new entity
     * <p>
     * if pk exists, throws an exception
     *
     * @param entity the entity
     */
    void create(T entity) throws SaveException;

    /**
     * insert an entity or update all fields of an exists entity
     *
     * @param entity the entity
     * @throws SaveException save error
     */
    void saveTotally(T entity) throws SaveException;

    /**
     * remove an entity
     *
     * @param entity the entity want to remove
     * @return the count that success removed
     */
    int remove(T entity);

    /**
     * remove an entity with the id
     *
     * @param id the id of an entity want to remove
     * @return the count that success removed
     */
    int remove(K id);

    /**
     * batch remove entities by id array
     *
     * @param ids id array
     * @return the count that success removed
     */
    int remove(K[] ids);
}
