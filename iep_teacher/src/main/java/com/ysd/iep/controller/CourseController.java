package com.ysd.iep.controller;

import com.ysd.iep.entity.Course;
import com.ysd.iep.entity.dto.Result;
import com.ysd.iep.feign.AdminFeign;
import com.ysd.iep.service.CourseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(value="/course", tags="课程")
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private AdminFeign adminFeign;
    /**
     * @param page
     * @param pageSize
     * @param courName
     * @return
     */
    @ApiOperation(value = "课程分页")
    @GetMapping("/getPaginate")
    public Page<Course> getPaginate(@ApiParam(name="page",value="页码",required=true) int page,
                                    @ApiParam(name="pageSize",value="条数",required=true) int pageSize,
                                    @ApiParam(name="courName",value="课程名称",required=false) String courName) {
        return courseService.getPaginate(page, pageSize, courName);
    }

    @ApiOperation(value = "获取老师菜单")
    @GetMapping("/getMenu")
    public Result getMenu(){
        return adminFeign.getMenu();
    }
}