package cn.aulang.common.crud.rdbm;

import cn.aulang.common.core.lang.Constant;
import cn.aulang.common.exception.SearchException;
import cn.aulang.common.crud.Page;
import cn.aulang.common.crud.SearchFilter;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.entity.Example.Criteria;
import tk.mybatis.mapper.page.SimplePage;

import java.util.Arrays;
import java.util.Map;

/**
 * TK Example.Criteria的辅助类
 */
public final class Examples {

    private Examples() {
    }

    /**
     * 创建一个没有过滤条件的Example对象
     *
     * @param entityClass 对象类名
     * @return Example对象
     */
    public static Example createEmpty(Class<?> entityClass) {
        return new Example(entityClass);
    }

    /**
     * 依据属性以等于方式过滤对象
     *
     * @param entityClass 对象类名
     * @param property    属性名
     * @param value       值
     * @return Example对象
     */
    public static Example create(Class<?> entityClass, String property, Object value) {
        Example example = new Example(entityClass);
        Criteria criteria = example.createCriteria();
        if (value == null) {
            criteria.andIsNull(property);
        } else {
            criteria.andEqualTo(property, value);
        }
        return example;
    }

    /**
     * 依据属性组以等于方式过滤对象，每组之间关系为AND
     *
     * @param entityClass 对象类名
     * @param conditions  属性/值对
     * @return Example对象
     */
    public static Example create(Class<?> entityClass, Map<String, Object> conditions) {
        Example example = new Example(entityClass);
        Criteria criteria = example.createCriteria();
        for (Map.Entry<String, Object> entry : conditions.entrySet()) {
            if (entry.getValue() == null) {
                criteria.andIsNull(entry.getKey());
            } else {
                criteria.andEqualTo(entry.getKey(), entry.getValue());
            }
        }
        return example;
    }

    /**
     * 依据通用的Pageable对象创建Example
     *
     * @param page Pageable对象
     * @param <T>  实体类型
     * @return 创建好的Example对象
     */
    public static <T> Example create(Page<T> page) throws SearchException {
        if (page.entityClass() == null) {
            throw new IllegalArgumentException("Example create fail, entity class property is not valid");
        }
        Example example = new Example(page.entityClass());
        Criteria criteria = example.createCriteria();

        for (SearchFilter filter : page.getSearchFilters()) {
            andFilter(criteria, filter);
        }

        for (Map.Entry<String, String> entry : page.getSortMap().entrySet()) {
            if (SimplePage.ASC.equalsIgnoreCase(entry.getValue())) {
                example.orderBy(entry.getKey()).asc();
            } else {
                example.orderBy(entry.getKey()).desc();
            }
        }
        return example;
    }

    private static void andFilter(Criteria criteria, SearchFilter filter) {
        if (!filter.valid()) {
            return;
        }
        switch (filter.getMatchType()) {
            case EQ -> criteria.andEqualTo(filter.getPropertyName(), filter.getValue());
            case NEQ -> criteria.andNotEqualTo(filter.getPropertyName(), filter.getValue());
            case LIKE -> criteria.andLike(filter.getPropertyName(), "%" + filter.getValue() + "%");
            case NOTLIKE -> criteria.andNotLike(filter.getPropertyName(), "%" + filter.getValue() + "%");
            case GT, GTD -> criteria.andGreaterThan(filter.getPropertyName(), filter.getValue());
            case GE, GED -> criteria.andGreaterThanOrEqualTo(filter.getPropertyName(), filter.getValue());
            case LT, LTD -> criteria.andLessThan(filter.getPropertyName(), filter.getValue());
            case LE, LED -> criteria.andLessThanOrEqualTo(filter.getPropertyName(), filter.getValue());
            case IN -> criteria.andIn(filter.getPropertyName(), Arrays.asList(StringUtils.split(filter.getValue(), Constant.COMMA)));
            case NOTIN -> criteria.andNotIn(filter.getPropertyName(), Arrays.asList(StringUtils.split(filter.getValue(), Constant.COMMA)));
            case NOTNULL -> criteria.andIsNotNull(filter.getPropertyName());
            case ISNULL -> criteria.andIsNull(filter.getPropertyName());
            default -> {
            }
        }
    }
}
