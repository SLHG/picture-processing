package com.cn.service.config;


import com.alibaba.druid.util.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerAdviceInterceptor {

    private static final Logger LOGGER = Logger.getLogger(ControllerAdviceInterceptor.class);

    /**
     * 在方法执行前根据注解修改数据源
     *
     * @param selectDataSource 数据源注解
     */
    @Before("@annotation(selectDataSource)")
    private void beforeDataSourceSwitch(SelectDataSource selectDataSource) {
        String dataSource = selectDataSource.value();
        LOGGER.info("beforeDataSourceSwitch=>进行数据库切换" + dataSource);
        String defaultSource = DynamicDataSource.getDataSource();
        if (StringUtils.isEmpty(defaultSource) || !defaultSource.equals(dataSource)) {
            DynamicDataSource.setDataSource(selectDataSource.value());
        }
    }

    @After("@annotation(selectDataSource)")
    private void afterDataSourceSwitch(SelectDataSource selectDataSource) {
        DynamicDataSource.clearDataSource();
    }
}
