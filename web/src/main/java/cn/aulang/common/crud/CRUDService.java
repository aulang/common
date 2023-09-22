package cn.aulang.common.crud;

import cn.aulang.common.core.utils.Reflections;
import cn.aulang.common.crud.id.IdEntity;
import cn.aulang.common.exception.SaveException;
import cn.aulang.common.exception.SearchException;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.List;

public abstract class CRUDService<T extends IdEntity<K>, K extends Serializable> implements GenericService<T, K> {

    protected abstract GenericRepository<T, K> getRepository();

    /**
     * {@inheritDoc}
     */
    @Override
    public Page<T> search(Page<T> page) throws SearchException {
        try {
            page.entityClass(Reflections.getSuperClassGenericType(this.getClass()));
            onSearch(page);
            page.setList(getRepository().search(page));
            return page;
        } catch (Exception e) {
            throw new SearchException("查询参数错误", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public T get(K id) {
        return getRepository().get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<T> findAll() {
        return getRepository().findAll();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean exists(K id) {
        return getRepository().exists(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void create(T entity) throws SaveException {
        getRepository().create(entity);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(T entity) {
        doSaveOrUpdate(entity, true);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveTotally(T entity) throws SaveException {
        doSaveOrUpdate(entity, false);
    }

    /**
     * Save or update an entity
     *
     * @param entity      the entity
     * @param excludeNull exclude null values?
     */
    private void doSaveOrUpdate(T entity, boolean excludeNull) {
        getRepository().saveOrUpdate(entity, excludeNull);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
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
}
