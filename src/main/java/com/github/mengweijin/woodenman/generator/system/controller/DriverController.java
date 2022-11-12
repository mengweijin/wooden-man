package com.github.mengweijin.woodenman.generator.system.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.mengweijin.quickboot.domain.R;
import com.github.mengweijin.quickboot.mvc.BaseController;
import com.github.mengweijin.woodenman.generator.system.service.DriverService;
import com.github.mengweijin.woodenman.generator.system.entity.DriverInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author mengweijin
 * @date 2022/8/14
 */
@Controller
@RequestMapping(DriverController.PREFIX)
public class DriverController extends BaseController {

    public static final String PREFIX = "/generator/driver";

    @Autowired
    private DriverService driverService;

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
        this.setAttribute("domain", driverService.getById(id));
        return PREFIX + "/edit";
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable("id") Long id) {
        this.setAttribute("domain", driverService.getById(id));
        return PREFIX + "/detail";
    }


    @GetMapping("/page")
    @ResponseBody
    public Page<DriverInfo> page(Page<DriverInfo> page, DriverInfo driverInfo) {
        LambdaQueryWrapper<DriverInfo> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StrUtil.isNotBlank(driverInfo.getGroupId()), DriverInfo::getGroupId, driverInfo.getGroupId());
        wrapper.like(StrUtil.isNotBlank(driverInfo.getArtifactId()), DriverInfo::getArtifactId, driverInfo.getArtifactId());
        wrapper.orderByDesc(DriverInfo::getCreateTime);
        return driverService.page(page, wrapper);
    }

    @PostMapping
    @ResponseBody
    public void add(DriverInfo driverInfo) {
        driverService.save(driverInfo);
    }

    @PutMapping
    @ResponseBody
    public void edit(DriverInfo driverInfo) {
        driverService.updateById(driverInfo);
    }

    @DeleteMapping("/{id}")
    @ResponseBody
    public void delete(@PathVariable("id") Long id) {
        driverService.removeById(id);
    }

    @PostMapping("/fetch/{id}")
    @ResponseBody
    public R fetch(@PathVariable("id") Long id) {
        boolean flag = driverService.fetchDriverById(id);
        return R.bool(flag);
    }

    @PostMapping("/check/{id}")
    @ResponseBody
    public R check(@PathVariable("id") Long id) {
        boolean flag = driverService.checkById(id);
        return R.bool(flag);
    }
}
