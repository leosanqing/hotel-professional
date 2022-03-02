package cn.hibit.framework.security.core.handler;

import cn.hibit.framework.common.pojo.CommonResult;
import cn.hibit.framework.common.util.servlet.ServletUtils;
import cn.hibit.framework.security.config.SecurityProperties;
import cn.hibit.framework.security.core.authentication.MultiUserDetailsAuthenticationProvider;
import cn.hibit.framework.security.core.util.SecurityFrameworkUtils;
import cn.hutool.core.util.StrUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 自定义退出处理器
 *
 * @author ruoyi
 */
@AllArgsConstructor
public class LogoutSuccessHandlerImpl implements LogoutSuccessHandler {

    private final SecurityProperties securityProperties;

    private final MultiUserDetailsAuthenticationProvider authenticationProvider;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        // 执行退出
        String token = SecurityFrameworkUtils.obtainAuthorization(request, securityProperties.getTokenHeader());
        if (StrUtil.isNotBlank(token)) {
            authenticationProvider.logout(request, token);
        }
        // 返回成功
        ServletUtils.writeJSON(response, CommonResult.success(null));
    }

}
