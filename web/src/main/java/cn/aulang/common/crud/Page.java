package cn.aulang.common.crud;

import jakarta.persistence.Transient;
import tk.mybatis.mapper.page.Converter;
import tk.mybatis.mapper.page.Pageable;
import tk.mybatis.mapper.page.SimplePage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与具体ORM实现无关的分页参数及查询结果封装.
 *
 * @param <T> Page中记录的类型.
 */
public class Page<T> extends SimplePage<T> {

    private List<SearchFilter> searchFilters = new ArrayList<>();       // IN/OUT
    private Map<String, String> sortMap = new HashMap<>();              // IN/OUT

    @Transient
    private Class<?> entityClass;

    public Page() {
    }

    public Page(Pageable<T> page) {
        super(page);
    }

    public Page(final int page) {
        super(page);
    }

    public Page(final int page, final int size) {
        super(page, size);
    }

    public List<SearchFilter> getSearchFilters() {
        return searchFilters;
    }

    public Map<String, String> getSortMap() {
        return sortMap;
    }

    public Pageable<T> removeSearchFilter(SearchFilter filter) {
        searchFilters.remove(filter);
        return this;
    }

    public Pageable<T> clearSearchFilter() {
        searchFilters.clear();
        return this;
    }

    public Pageable<T> addSorts(Map<String, String> sorts) {
        sortMap.putAll(sorts);
        return this;
    }

    public Pageable<T> removeSort(String field) {
        sortMap.remove(field);
        return this;
    }

    public Pageable<T> clearSort() {
        sortMap.clear();
        return this;
    }

    public Pageable<T> addSearchFilter(SearchFilter filter) {
        searchFilters.add(filter);
        return this;
    }

    public Pageable<T> addSearchFilters(List<SearchFilter> filters) {
        searchFilters.addAll(filters);
        return this;
    }

    public boolean hasSortCondition() {
        return !sortMap.isEmpty();
    }

    public Pageable<T> addSort(String field, String type) {
        sortMap.put(field, type);
        return this;
    }

    public Class<?> entityClass() {
        return entityClass;
    }

    public void entityClass(Class<?> entityClass) {
        this.entityClass = entityClass;
    }

    @Override
    public <K> Pageable<K> convert(Converter<T, K> converter) {
        Page<K> page = new Page<>();
        page.setPage(getPage()).setSize(getSize()).setTotal(getTotal());
        page.searchFilters = this.getSearchFilters();
        page.sortMap = this.getSortMap();
        page.entityClass = this.entityClass;
        return page.setList(getList().stream().map(converter::convert).toList());
    }
}
