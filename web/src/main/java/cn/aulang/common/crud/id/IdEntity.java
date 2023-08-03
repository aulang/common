package cn.aulang.common.crud.id;

import java.beans.Transient;
import java.io.Serializable;

public interface IdEntity<K extends Serializable> {

    /**
     * Get id value of the entity
     */
    K getId();

    /**
     * Entity is a new object?
     */
    @Transient
    boolean isNew();
}
