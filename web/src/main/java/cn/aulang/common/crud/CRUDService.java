package cn.aulang.common.crud;

import cn.aulang.common.core.utils.Reflections;
import cn.aulang.common.exception.SaveException;
import cn.aulang.common.exception.SearchException;
import cn.aulang.common.crud.id.IdEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.transaction.annotation.Transactional;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class CRUDService<T extends IdEntity<K>, K extends Serializable> implements GenericService<T, K> {

    protected abstract GenericRepository<T, K> getRepository();

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<T> search(Page<T> page) throws SearchException {
        try {
            page.entityClass(Reflections.getSuperClassGenericType(this.getClass()));
            onSearch(page);
            page.setList(getRepository().search(page));
            return page;
        } catch (Exception e) {
            throw new SearchException("Query parameter error", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public T get(K id) {
        return getRepository().get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<T> findAll() {
        return getRepository().findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean exists(K id) {
        return getRepository().exists(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void create(T entity) throws SaveException {
        getRepository().create(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void save(T entity) {
        doSaveOrUpdate(entity, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void saveTotally(T entity) throws SaveException {
        doSaveOrUpdate(entity, false);
    }

    /**
     * Save or update an entity
     *
     * @param entity       the entity
     * @param excludeNulls exclude null values?
     */
    private void doSaveOrUpdate(T entity, boolean excludeNulls) {
        if (!entity.isNew() && excludeNulls && exists(entity.getId())) {
            T savedEntity = getRepository().get(entity.getId());
            BeanUtils.copyProperties(entity, savedEntity, getNullPropertyNames(entity));
            getRepository().update(savedEntity);
        }
        getRepository().saveOrUpdate(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int remove(T entity) {
        if (!onRemove(entity.getId())) {
            return 0;
        }
        int count = getRepository().remove(entity);
        postRemove(entity.getId());
        return count;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int remove(K id) {
        if (onRemove(id)) {
            int count = getRepository().remove(id);
            postRemove(id);
            return count;
        }
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public int remove(K[] ids) {
        int success = 0;
        try {
            for (K id : ids) {
                success = success + remove(id);
            }
        } catch (Exception e) {
            //ignore error
        }
        return success;
    }

    protected void onSearch(Page<T> ignoredPage) {
    }

    protected boolean onRemove(K id) {
        return true;
    }

    protected void postRemove(K id) {
    }

    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> nullProperties = new HashSet<>();
        for (PropertyDescriptor pd : pds) {
            Object value = src.getPropertyValue(pd.getName());
            if (value == null) {
                nullProperties.add(pd.getName());
            }
        }
        String[] result = new String[nullProperties.size()];
        return nullProperties.toArray(result);
    }
}
