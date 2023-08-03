package cn.aulang.common.auth;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author wulang
 */
public class GatewayAuthUserReader implements AuthUserReader {

    @Override
    public CurrentUser read(HttpServletRequest request) {
        return CurrentUser.of(
                request.getHeader(CurrentUser.USER_ID),
                request.getHeader(CurrentUser.USERNAME),
                request.getHeader(CurrentUser.NICKNAME),
                request.getHeader(CurrentUser.CLIENT_ID)
        );
    }
}
