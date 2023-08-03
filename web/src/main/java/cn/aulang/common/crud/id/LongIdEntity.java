package cn.aulang.common.crud.id;

public abstract class LongIdEntity implements IdEntity<Long> {

    @Override
    public boolean isNew() {
        return getId() == null || getId() == 0;
    }
}
