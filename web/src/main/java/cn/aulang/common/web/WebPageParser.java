package cn.aulang.common.web;

import cn.aulang.common.crud.Page;
import cn.aulang.common.crud.SearchFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * 从HTTP request中解析page参数
 */
@Slf4j
public class WebPageParser {

    public <T> Page<T> parse(Map<String, String> params) {
        return parse(params, null);
    }

    public <T> Page<T> parse(Map<String, String> params, String filterPrefix) {
        Page<T> pageable = new Page<>();

        if (params == null || params.isEmpty()) {
            return pageable;
        }

        if (StringUtils.isBlank(filterPrefix)) {
            filterPrefix = "filter_";
        }

        pageable.addSearchFilters(buildSearchFilters(params, filterPrefix));

        Long page = getLong(params, "page");
        if (page != null) {
            pageable.setPage(page);
        }

        Long size = getLong(params, "size");
        if (size != null) {
            pageable.setSize(size);
        }

        String sort = params.get("sort");
        if (StringUtils.isNotBlank(sort)) {
            String[] sortEntries = sort.split(";");
            for (String entry : sortEntries) {
                String[] pair = entry.split(",");
                String direction = Page.ASC;
                if (pair.length > 1) {
                    direction = pair[1];
                }
                pageable.addSort(pair[0], direction);
            }
        }

        return pageable;
    }

    /**
     * 根据按PropertyFilter命名规则的Request参数，创建PropertyFilter列表，
     * PropertyFilter命名规则为Filter属性前缀_比较类型_属性名，
     * eg: filter_EQ_name filter_LIKE_name_OR_email
     */
    private List<SearchFilter> buildSearchFilters(Map<String, String> params, String filterPrefix) {
        int length = filterPrefix.length();

        return params.entrySet()
                .parallelStream()
                .filter(e -> e.getKey().startsWith(filterPrefix) && StringUtils.isNotBlank(e.getValue()))
                .map(e -> new SearchFilter(e.getKey().substring(length), e.getValue()))
                .toList();
    }

    private Long getLong(Map<String, String> params, String key) {
        String value = params.get(key);
        if (StringUtils.isBlank(value)) {
            return null;
        }

        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            log.warn("参数：{}的值：{}无法转换成Long类型", key, value);
            return null;
        }
    }
}
