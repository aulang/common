package cn.aulang.common.crud.rdbm;

import cn.aulang.common.crud.GenericRepository;
import cn.aulang.common.crud.Page;
import cn.aulang.common.crud.id.IdEntity;
import cn.aulang.common.exception.SaveException;
import cn.aulang.common.exception.SearchException;
import tk.mybatis.mapper.annotation.RegisterMapper;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.page.PageHelper;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@RegisterMapper
public interface MybatisRepository<T extends IdEntity<K>, K extends Serializable>
        extends GenericRepository<T, K>, Mapper<T> {

    /**
     * Generic method used to get all objects of a particular type. This
     * is the same as lookup up all rows in a table.
     *
     * @return List of populated objects
     */
    @Override
    default List<T> findAll() {
        return selectAll();
    }

    /**
     * Get all objects filter by property value
     *
     * @param entityClass entity class
     * @param property    property name
     * @param value       filter value
     * @return List of objects with the property value
     */
    default List<T> findByProperty(Class<?> entityClass, String property, Object value) {
        return selectByExample(Examples.create(entityClass, property, value));
    }

    /**
     * Count by property/value
     *
     * @param entityClass entity class
     * @param property    property name
     * @param value       property value
     * @return the count of filtered objects
     */
    default long countByProperty(Class<?> entityClass, String property, Object value) {
        return selectCountByExample(Examples.create(entityClass, property, value));
    }

    /**
     * Count by property/value map
     *
     * @param entityClass entity class
     * @param conditions  Property/Value map
     * @return the count of filtered objects
     */
    default long countByProperties(Class<?> entityClass, Map<String, Object> conditions) {
        return selectCountByExample(Examples.create(entityClass, conditions));
    }

    default long count(Class<?> entityClass) {
        return selectCountByExample(Examples.createEmpty(entityClass));
    }

    /**
     * Get one entity by condition which can get a unique object
     * <p>
     * if no, return null, if multi recorders found, throw an exception
     *
     * @param entityClass entity class
     * @param property    property name
     * @param value       property value
     * @return the object filtered by the condition
     */
    default T getOneByProperty(Class<?> entityClass, String property, Object value) {
        Example criteria = Examples.create(entityClass, property, value);
        int count = selectCountByExample(criteria);
        if (count > 1) {
            throw new SearchException("Get one entity but found " + count);
        }
        List<T> list = selectByExample(criteria);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Get all objects filter by property value
     *
     * @param entityClass entity class
     * @param conditions  Property/Value map
     * @return List of objects with the property value
     */
    default List<T> findByProperties(Class<?> entityClass, Map<String, Object> conditions) {
        return selectByExample(Examples.create(entityClass, conditions));
    }

    /**
     * Get one entity by condition which can get a unique object
     * <p>
     * if no, return null, if multi recorders found, throw an exception
     *
     * @param entityClass entity class
     * @param conditions  Property/Value map
     * @return the object filtered by the condition
     */
    default T getOneByProperties(Class<?> entityClass, Map<String, Object> conditions) {
        Example example = Examples.create(entityClass, conditions);
        int count = selectCountByExample(example);
        if (count > 1) {
            throw new SearchException("Get one entity but found " + count);
        }
        List<T> list = selectByExample(example);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * Gets all records that match a search term
     *
     * @param page the term to search for
     * @return the matching records
     * @throws SearchException when search error
     */
    @Override
    default List<T> search(Page<T> page) throws SearchException {
        Example example = Examples.create(page);

        PageHelper.startPage(page);
        return selectByExample(example);
    }

    /**
     * Generic method to get an object based on class and identifier.
     *
     * @param id the identifier (primary key) of the object to get
     * @return a populated object
     */
    @Override
    default T get(K id) {
        return selectByPrimaryKey(id);
    }

    /**
     * Checks for existence of an object of type T using the id arg.
     *
     * @param id the id of the entity
     * @return - true if it exists, false if it doesn't
     */
    @Override
    default boolean exists(K id) {
        return existsWithPrimaryKey(id);
    }

    /**
     * Generic method to save an object - handles both update and insert.
     *
     * @param object      the object to save
     * @param excludeNull exclude null values?
     */
    @Override
    default void saveOrUpdate(T object, boolean excludeNull) {
        int affected;
        if (!object.isNew() && exists(object.getId())) {
            if (excludeNull) {
                affected = updateByPrimaryKeySelective(object);
            } else {
                affected = updateByPrimaryKey(object);
            }
        } else {
            if (excludeNull) {
                affected = insertSelective(object);
            } else {
                affected = insert(object);
            }
        }
        if (affected == 0) {
            throw new SaveException("save entity fail:" + object);
        }
    }

    /**
     * Generic method to insert a object
     */
    @Override
    default void create(T object) {
        int affected = insert(object);
        if (affected == 0) {
            throw new SaveException("save entity fail:" + object);
        }
    }

    /**
     * Generic method to update an object
     */
    @Override
    default void update(T object) {
        int affected = updateByPrimaryKey(object);
        if (affected == 0) {
            throw new SaveException("save entity fail:" + object);
        }
    }

    /**
     * Generic method to delete an object
     *
     * @param object the object to remove
     */
    @Override
    default int remove(T object) {
        return delete(object);
    }

    /**
     * Generic method to delete an object
     *
     * @param id the identifier (primary key) of the object to remove
     */
    @Override
    default int remove(K id) {
        return deleteByPrimaryKey(id);
    }
}
