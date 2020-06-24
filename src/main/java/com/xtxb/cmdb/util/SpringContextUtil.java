package com.xtxb.cmdb.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * 作者: xtxb
 * <p>
 * 日期: 2020年06月24日-下午6:03
 * <p>
 * <p>
 * 用于获取延迟加载的Bean
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    public Object getBean(String name){
        return applicationContext.getBean(name);
    }
}
