package com.xdcplus.spring.boot.autoconfigure;

import com.xdcplus.httpclient.utils.RestTemplateUtils;
import cn.hutool.core.util.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.client.RestTemplate;

/**
 *  Http 工厂类
 * @author Rong.Jia
 * @date 2021/02/03 09:03
 */
public class HttpFactoryBean implements FactoryBean<RestTemplateUtils>, InitializingBean, ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(HttpFactoryBean.class);

    private ApplicationContext applicationContext;
    private RestTemplateUtils restTemplateUtils;

    @Override
    public void afterPropertiesSet() {
        try {
            if (ObjectUtil.isNotNull(applicationContext)) {
                this.restTemplateUtils = new RestTemplateUtils((RestTemplate)applicationContext.getBean("httpClientTemplate"));
            }
        }catch (Exception e) {
            logger.error("The alarm message callback thread failed to start {}", e.getMessage());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


    @Override
    public RestTemplateUtils getObject() {
        return this.restTemplateUtils;
    }

    @Override
    public Class<?> getObjectType() {
        return RestTemplateUtils.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
