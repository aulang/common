package cn.aulang.common.auth;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author wulang
 */
public interface AuthUserReader {

    CurrentUser read(HttpServletRequest request);
}
