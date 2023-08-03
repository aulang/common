package cn.aulang.common.crud.id;

public abstract class StringIdEntity implements IdEntity<String> {

    @Override
    public boolean isNew() {
        return getId() == null || getId().isEmpty();
    }
}
