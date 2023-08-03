package cn.aulang.common.crud.id;

import cn.aulang.common.core.tools.Snowflake;
import tk.mybatis.mapper.genid.GenId;

public class LongIdGenId implements GenId<Long> {

    private static final Snowflake snowflake = new Snowflake();

    @Override
    public Long genId(String table, String column) {
        return snowflake.nextId();
    }
}