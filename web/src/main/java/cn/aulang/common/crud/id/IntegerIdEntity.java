package cn.aulang.common.crud.id;

public abstract class IntegerIdEntity implements IdEntity<Integer> {

    @Override
    public boolean isNew() {
        return getId() == null || getId() == 0;
    }
}
