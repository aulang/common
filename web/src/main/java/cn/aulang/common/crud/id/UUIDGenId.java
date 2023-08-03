package cn.aulang.common.crud.id;

import cn.aulang.common.core.utils.Identities;
import tk.mybatis.mapper.genid.GenId;

public class UUIDGenId implements GenId<String> {

    @Override
    public String genId(String table, String column) {
        return Identities.uuid2();
    }
}