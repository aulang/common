package cn.aulang.common.auth;

import cn.aulang.common.core.lang.Constant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前登录用户
 *
 * @author wulang
 */
@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class CurrentUser {

    public static final String USER_ID = "user_id";
    public static final String USERNAME = "user_name";
    public static final String NICKNAME = "nickname";
    public static final String CLIENT_ID = "client_id";

    /**
     * 用户ID
     */
    private String userId = Constant.EMPTY;
    /**
     * 用户名
     */
    private String username = Constant.EMPTY;
    /**
     * 用户昵称
     */
    private String nickname = Constant.EMPTY;
    /**
     * 客户端ID
     */
    private String clientId = Constant.EMPTY;
}
