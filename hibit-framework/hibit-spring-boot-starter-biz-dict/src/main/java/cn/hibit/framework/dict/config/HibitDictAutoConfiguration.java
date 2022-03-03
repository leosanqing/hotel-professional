package cn.hibit.framework.dict.config;

import cn.hibit.framework.dict.core.service.DictDataFrameworkService;
import cn.hibit.framework.dict.core.util.DictFrameworkUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibitDictAutoConfiguration {

    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
    public DictFrameworkUtils dictUtils(DictDataFrameworkService service) {
        DictFrameworkUtils.init(service);
        return new DictFrameworkUtils();
    }

}
