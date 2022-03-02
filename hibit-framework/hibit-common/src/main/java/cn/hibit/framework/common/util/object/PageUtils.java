package cn.hibit.framework.common.util.object;

import cn.hibit.framework.common.pojo.PageParam;
import lombok.experimental.UtilityClass;

/**
 * {@link PageParam} 工具类
 *
 * @author 芋道源码
 */
@UtilityClass
public class PageUtils {

    public static int getStart(PageParam pageParam) {
        return (pageParam.getPageNo() - 1) * pageParam.getPageSize();
    }

}
