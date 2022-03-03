package cn.hibit.framework.operatelog.core.util;

import cn.hibit.framework.operatelog.core.aop.OperateLogAspect;
import lombok.experimental.UtilityClass;

/**
 * 操作日志工具类
 * 目前主要的作用，是提供给业务代码，记录操作明细和拓展字段
 *
 * @author 芋道源码
 */
@UtilityClass
public class OperateLogUtils {

    public static void setContent(String content) {
        OperateLogAspect.setContent(content);
    }

    public static void addExt(String key, Object value) {
        OperateLogAspect.addExt(key, value);
    }

}
