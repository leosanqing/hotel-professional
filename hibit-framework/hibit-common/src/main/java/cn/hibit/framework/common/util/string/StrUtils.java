package cn.hibit.framework.common.util.string;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * 字符串工具类
 *
 * @author 芋道源码
 */
@UtilityClass
public class StrUtils {

    public static String maxLength(CharSequence str, int maxLength) {
        // -3 的原因，是该方法会补充 ... 恰好
        return StrUtil.maxLength(str, maxLength - 3);
    }

    /**
     * 指定字符串的
     * @param str
     * @param replaceMap
     * @return
     */
    public static String replace(String str, Map<String, String> replaceMap) {
        assert StrUtil.isNotBlank(str);
        if (ObjectUtil.isEmpty(replaceMap)) {
            return str;
        }
        String result = null;
        for (String key : replaceMap.keySet()) {
            result = str.replace(key, replaceMap.get(key));
        }
        return result;
    }

}
