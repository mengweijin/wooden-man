package com.github.mengweijin.woodenman.generator.async;

import com.github.mengweijin.woodenman.generator.system.entity.DatasourceInfo;
import com.github.mengweijin.woodenman.generator.system.service.DatasourceService;
import com.github.mengweijin.woodenman.generator.system.service.TemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @author mengweijin
 * @date 2022/10/30
 */
@Slf4j
@Async
@Component
public class GeneratorAsyncFactory {

    @Autowired
    private DatasourceService datasourceService;

    @Autowired
    private TemplateService templateService;

    public void refreshDriver(DatasourceInfo ds) {
        datasourceService.refreshDriver(ds);
        datasourceService.lambdaUpdate()
                .set(DatasourceInfo::getAutoRefreshDriver, true)
                .eq(DatasourceInfo::getId, ds.getId())
                .update();
    }

    public void initTemplate() {
        long count = templateService.count();
        if(count == 0) {
            templateService.initSystemBuiltInTemplates();
        }
    }
}
