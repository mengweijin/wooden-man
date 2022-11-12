package com.github.mengweijin.woodenman.generator.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.mengweijin.quickboot.mvc.BaseController;
import com.github.mengweijin.woodenman.generator.async.GeneratorAsyncFactory;
import com.github.mengweijin.woodenman.generator.system.dto.DatasourceInfoDTO;
import com.github.mengweijin.woodenman.generator.system.entity.DatasourceInfo;
import com.github.mengweijin.woodenman.generator.system.service.DatasourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;

/**
 * @author mengweijin
 * @date 2022/8/14
 */
@Controller
@RequestMapping(DatasourceController.PREFIX)
public class DatasourceController extends BaseController {

    public static final String PREFIX = "/generator/datasource";

    @Autowired
    private DatasourceService datasourceService;

    @Autowired
    private GeneratorAsyncFactory generatorAsyncFactory;

    @GetMapping("/index")
    public String index() {
        return PREFIX + "/index";
    }

    @GetMapping("/add")
    public String add() {
        return PREFIX + "/edit";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id) {
        this.setAttribute("domain", datasourceService.getById(id));
        return PREFIX + "/edit";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id) {
        this.setAttribute("domain", datasourceService.getById(id));
        return PREFIX + "/detail";
    }

    @GetMapping("/page")
    @ResponseBody
    public IPage<DatasourceInfoDTO> page(Page<DatasourceInfoDTO> page, DatasourceInfoDTO dto) {
        return datasourceService.selectPageVO(page, dto);
    }

    @GetMapping("/page-single-table")
    @ResponseBody
    public Page<DatasourceInfo> pageSingleTable(Page<DatasourceInfo> page, DatasourceInfo datasourceInfo) {
        LambdaQueryWrapper<DatasourceInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(datasourceInfo.getUsername()), DatasourceInfo::getUsername, datasourceInfo.getUsername());
        wrapper.eq(datasourceInfo.getDbType() != null, DatasourceInfo::getDbType, datasourceInfo.getDbType());
        wrapper.orderByDesc(DatasourceInfo::getCreateTime);
        return datasourceService.page(page, wrapper);
    }

    @PostMapping
    @ResponseBody
    public void add(DatasourceInfo ds) {
        datasourceService.save(ds);
        generatorAsyncFactory.refreshDriver(ds);
    }

    @PostMapping("/clone/{id}")
    @ResponseBody
    public void clone(@PathVariable("id") Long id) {
        datasourceService.cloneById(id);
    }

    @PutMapping
    @ResponseBody
    public void edit(DatasourceInfo ds) {
        datasourceService.updateById(ds);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        datasourceService.removeById(id);
    }

    @DeleteMapping
    @ResponseBody
    public void delete(Long[] ids) {
        datasourceService.removeBatchByIds(Arrays.asList(ids));
    }


}
