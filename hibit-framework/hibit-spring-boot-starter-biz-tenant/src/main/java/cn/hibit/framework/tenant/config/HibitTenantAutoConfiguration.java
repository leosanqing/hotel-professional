package cn.hibit.framework.tenant.config;

import cn.hibit.framework.common.enums.WebFilterOrderEnum;
import cn.hibit.framework.mybatis.core.util.MyBatisUtils;
import cn.hibit.framework.quartz.core.handler.JobHandler;
import cn.hibit.framework.tenant.core.aop.TenantIgnoreAspect;
import cn.hibit.framework.tenant.core.db.TenantDatabaseInterceptor;
import cn.hibit.framework.tenant.core.job.TenantJob;
import cn.hibit.framework.tenant.core.job.TenantJobHandlerDecorator;
import cn.hibit.framework.tenant.core.mq.TenantRedisMessageInterceptor;
import cn.hibit.framework.tenant.core.security.TenantSecurityWebFilter;
import cn.hibit.framework.tenant.core.service.TenantFrameworkService;
import cn.hibit.framework.tenant.core.web.TenantContextWebFilter;
import cn.hibit.framework.web.config.WebProperties;
import cn.hibit.framework.web.core.handler.GlobalExceptionHandler;
import cn.hutool.core.annotation.AnnotationUtil;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.TenantLineInnerInterceptor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "hibit.tenant", value = "enable", matchIfMissing = true)
// 允许使用 yudao.tenant.enable=false 禁用多租户
@EnableConfigurationProperties(TenantProperties.class)
public class HibitTenantAutoConfiguration {

    // ========== AOP ==========

    @Bean
    public TenantIgnoreAspect tenantIgnoreAspect() {
        return new TenantIgnoreAspect();
    }

    // ========== DB ==========

    @Bean
    public TenantLineInnerInterceptor tenantLineInnerInterceptor(TenantProperties properties,
                                                                 MybatisPlusInterceptor interceptor) {
        TenantLineInnerInterceptor inner = new TenantLineInnerInterceptor(new TenantDatabaseInterceptor(properties));
        // 添加到 interceptor 中
        // 需要加在首个，主要是为了在分页插件前面。这个是 MyBatis Plus 的规定
        MyBatisUtils.addInterceptor(interceptor, inner, 0);
        return inner;
    }

    // ========== WEB ==========

    @Bean
    public FilterRegistrationBean<TenantContextWebFilter> tenantContextWebFilter() {
        FilterRegistrationBean<TenantContextWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantContextWebFilter());
        registrationBean.setOrder(WebFilterOrderEnum.TENANT_CONTEXT_FILTER);
        return registrationBean;
    }

    // ========== Security ==========

    @Bean
    public FilterRegistrationBean<TenantSecurityWebFilter> tenantSecurityWebFilter(TenantProperties tenantProperties,
                                                                                   WebProperties webProperties,
                                                                                   GlobalExceptionHandler globalExceptionHandler,
                                                                                   TenantFrameworkService tenantFrameworkService) {
        FilterRegistrationBean<TenantSecurityWebFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new TenantSecurityWebFilter(tenantProperties, webProperties,
                globalExceptionHandler, tenantFrameworkService));
        registrationBean.setOrder(WebFilterOrderEnum.TENANT_SECURITY_FILTER);
        return registrationBean;
    }

    // ========== MQ ==========

    @Bean
    public TenantRedisMessageInterceptor tenantRedisMessageInterceptor() {
        return new TenantRedisMessageInterceptor();
    }

    // ========== Job ==========

    @Bean
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BeanPostProcessor jobHandlerBeanPostProcessor(TenantFrameworkService tenantFrameworkService) {
        return new BeanPostProcessor() {

            @Override
            public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                if (!(bean instanceof JobHandler)) {
                    return bean;
                }
                // 有 TenantJob 注解的情况下，才会进行处理
                if (!AnnotationUtil.hasAnnotation(bean.getClass(), TenantJob.class)) {
                    return bean;
                }

                // 使用 TenantJobHandlerDecorator 装饰
                return new TenantJobHandlerDecorator(tenantFrameworkService, (JobHandler) bean);
            }

        };
    }

}
