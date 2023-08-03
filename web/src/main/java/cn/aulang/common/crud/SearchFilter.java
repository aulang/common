package cn.aulang.common.crud;

import cn.aulang.common.exception.SearchException;
import org.apache.commons.lang3.StringUtils;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * 与具体ORM实现无关的属性过滤条件封装类.
 * PropertyFilter主要记录页面中简单的搜索过滤条件，应用于单表场景
 */
public class SearchFilter {

    /**
     * 属性比较类型.参照Criteria的对应查询方式,GTD, GED, LTD, LED 这四个是针对时间
     */
    public enum MatchType {
        EQ, NEQ, LIKE, GT, GE, LT, LE, NOTNULL, ISNULL, GTD, GED, LTD, LED, IN, NOTLIKE, NOTIN
    }

    private String propertyName;

    private String value;
    private MatchType matchType = MatchType.EQ;

    public SearchFilter() {
    }

    /**
     * @param filterName 包含比较关系以及属性名的过滤字符串，例如LIKE_title
     * @param value      查询值
     */
    public SearchFilter(final String filterName, final String value) {
        String matchTypeCode = StringUtils.substringBefore(filterName, "_");
        try {
            matchType = Enum.valueOf(MatchType.class, matchTypeCode);
        } catch (SearchException e) {
            throw new IllegalArgumentException("filter expression error: " + filterName, e);
        }
        propertyName = StringUtils.substringAfter(filterName, "_");
        this.value = value;
    }

    /**
     * @param propertyName 包含比较关系以及属性名的过滤字符串，例如LIKE_title
     * @param value        查询值
     * @param type         匹配规则
     */
    public SearchFilter(final String propertyName, final String value, MatchType type) {
        this.propertyName = propertyName;
        this.value = value;
        this.matchType = type;
    }

    public boolean valid() {
        return isNotBlank(value) && isNotBlank(propertyName) && matchType != null;
    }

    public String getValue() {
        return value;
    }

    public MatchType getMatchType() {
        return matchType;
    }

    public String getPropertyName() {
        return propertyName;
    }
}
