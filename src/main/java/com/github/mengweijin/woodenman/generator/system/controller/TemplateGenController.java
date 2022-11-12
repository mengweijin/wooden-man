package com.github.mengweijin.woodenman.generator.system.controller;

import cn.hutool.core.util.StrUtil;
import com.github.mengweijin.layui.model.LayuiTree;
import com.github.mengweijin.quickboot.mvc.BaseController;
import com.github.mengweijin.woodenman.generator.system.dto.GenerateConfig;
import com.github.mengweijin.woodenman.generator.system.dto.TableInfoDTO;
import com.github.mengweijin.woodenman.generator.system.service.DatasourceTableService;
import com.github.mengweijin.woodenman.generator.system.service.TemplateGenService;
import com.github.mengweijin.woodenman.generator.system.service.TemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author mengweijin
 * @date 2022/8/14
 */
@Controller
@RequestMapping(TemplateGenController.PREFIX)
public class TemplateGenController extends BaseController {

    public static final String PREFIX = "/generator/template/gen";

    @Autowired
    private TemplateService templateService;

    @Autowired
    private TemplateGenService templateGenService;
    @Autowired
    private DatasourceTableService datasourceTableService;


    @GetMapping("/{datasourceId}/{tableName}/index")
    public String index(@PathVariable("datasourceId") Long datasourceId, @PathVariable("tableName") String tableName) {
        List<LayuiTree> tree = templateService.tree();
        this.setAttribute("templateList", tree);

        TableInfoDTO tableInfoDTO = datasourceTableService.selectTableInfo(datasourceId, tableName);
        this.setAttribute("datasourceId", datasourceId);
        this.setAttribute("tableName", tableName);
        this.setAttribute("entityName", StrUtil.upperFirst(StrUtil.toCamelCase(tableName)));
        this.setAttribute("tableColumns", tableInfoDTO.getFieldNames());
        return PREFIX + "/index";
    }

    @GetMapping("/execute/{datasourceId}/{tableName}/{templateId}")
    public String execute(@PathVariable("datasourceId") Long datasourceId,
                          @PathVariable("tableName") String tableName,
                          @PathVariable("templateId") Long templateId,
                          GenerateConfig config) {
        String codeContent = templateGenService.execute(datasourceId, tableName, templateId, config);
        this.setAttribute("code", codeContent);
        return PREFIX + "/detail";
    }

}
