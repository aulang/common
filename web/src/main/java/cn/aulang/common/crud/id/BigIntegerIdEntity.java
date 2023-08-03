package cn.aulang.common.crud.id;

import java.math.BigInteger;

public abstract class BigIntegerIdEntity implements IdEntity<BigInteger> {

    @Override
    public boolean isNew() {
        try {
            return getId() == null || getId().longValueExact() == 0;
        } catch (ArithmeticException e) {
            return false;
        }
    }
}
