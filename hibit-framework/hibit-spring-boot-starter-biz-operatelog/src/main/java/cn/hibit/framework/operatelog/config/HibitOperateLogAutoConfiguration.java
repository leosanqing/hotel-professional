package cn.hibit.framework.operatelog.config;

import cn.hibit.framework.operatelog.core.aop.OperateLogAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HibitOperateLogAutoConfiguration {

    @Bean
    public OperateLogAspect operateLogAspect() {
        return new OperateLogAspect();
    }

}
