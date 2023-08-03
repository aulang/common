package cn.aulang.common.web;

import cn.aulang.common.crud.GenericService;
import cn.aulang.common.crud.Page;
import cn.aulang.common.crud.id.IdEntity;
import cn.aulang.common.exception.NotFoundException;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.Serializable;
import java.util.Map;

public abstract class CRUDControllerSupport<T extends IdEntity<K>, K extends Serializable> {

    protected WebPageParser parser = new WebPageParser();

    protected abstract GenericService<T, K> service();

    @PostMapping
    public T save(@RequestBody @Valid T entity) {
        onSave(entity);
        service().save(entity);
        return service().get(entity.getId());
    }

    @GetMapping
    public ResponseEntity<?> get(@RequestParam K id) {
        return doGet(id);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> get0(@PathVariable K id) {
        return doGet(id);
    }

    protected ResponseEntity<?> doGet(@RequestParam K id) {
        T entity = service().get(id);
        if (entity == null) {
            throw NotFoundException.of(id);
        }
        return ResponseEntity.ok(entity);
    }

    @PostMapping("batch-remove")
    public ResponseEntity<?> remove(@RequestParam K[] id) {
        int count = service().remove(id);
        return ResponseEntity.ok().body(count + " recorders deleted");
    }

    @DeleteMapping
    public ResponseEntity<?> remove(@RequestParam K id) {
        return doRemove(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> remove0(@PathVariable K id) {
        return doRemove(id);
    }

    protected ResponseEntity<?> doRemove(@RequestParam K id) {
        int count = service().remove(id);
        return ResponseEntity.ok().body(count + " recorders deleted");
    }

    @PostMapping("search")
    public Page<T> search(@RequestBody(required = false) Page<T> page, @RequestParam(required = false) Map<String, String> params) {
        Page<T> pageable = page;
        if (pageable == null) {
            pageable = parser.parse(params);
        }

        onSearch(pageable);

        Page<T> result = service().search(pageable);
        postSearch(result);
        return result;
    }

    /**
     * 钩子，保存前执行
     */
    protected void onSave(T ignoredEntity) {
    }

    /**
     * 钩子，正式查找前执行，可用于后台绑定特定查询条件或指定特殊排序方式
     */
    protected void onSearch(Page<T> ignoredPage) {
    }

    /**
     * 钩子，数据查询完成后执行，可对{@code List<T>} 列表中的成员进行再加工或执行其他任意需要的操作
     */
    protected void postSearch(Page<T> ignoredPage) {
    }
}