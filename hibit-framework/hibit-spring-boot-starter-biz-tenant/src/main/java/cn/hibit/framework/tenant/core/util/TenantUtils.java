package cn.hibit.framework.tenant.core.util;

import cn.hibit.framework.tenant.core.context.TenantContextHolder;
import lombok.experimental.UtilityClass;

/**
 * 多租户 Util
 *
 * @author 芋道源码
 */
@UtilityClass
public class TenantUtils {

    /**
     * 使用指定租户，执行对应的逻辑
     * <p>
     * 注意，如果当前是忽略租户的情况下，会被强制设置成不忽略租户
     * 当然，执行完成后，还是会恢复回去
     *
     * @param tenantId 租户编号
     * @param runnable 逻辑
     */
    public static void execute(Long tenantId, Runnable runnable) {
        Long oldTenantId = TenantContextHolder.getTenantId();
        Boolean oldIgnore = TenantContextHolder.isIgnore();
        try {
            TenantContextHolder.setTenantId(tenantId);
            TenantContextHolder.setIgnore(false);
            // 执行逻辑
            runnable.run();
        } finally {
            TenantContextHolder.setTenantId(oldTenantId);
            TenantContextHolder.setIgnore(oldIgnore);
        }
    }


}
