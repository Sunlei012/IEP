package com.ysd.iep.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ysd.iep.entity.StudentPart;
import com.ysd.iep.service.StudentPartService;
import com.ysd.iep.util.Result;

/**
 * 
 * @Description:
 * @ClassName: StudentPartController.java
 * @author Liuyalong
 * @Date 2018年12月13日
 * @Email 1611549726@qq.com
 */

@RestController
@RequestMapping("/studentPart")
public class StudentPartController {

	@Autowired
	private StudentPartService s;

	/**
	 * 报名课程
	 * @param courId
	 * @param sid    http://127.0.0.1:80/api/student/studentPart/add
	 */
	@PostMapping("/add")
	public Object add(@RequestParam("courId")Integer courId,@RequestParam("sid")String sid) {	
		try {
			s.add(courId,sid);
			return new Result<String>(true, "报名成功");
		} catch (Exception e) {
			return new Result<String>(false, "报名失败");
		}
	}
	
    /**
     * 
     * @param courId
     * @param sid
     * @return
     */
	@DeleteMapping("/detele")
	public Object detele(@RequestParam("courId")Integer courId,@RequestParam("sid") String sid) {
		StudentPart studentPart2 = new StudentPart();
		studentPart2.setCid(courId);
		studentPart2.setSid(sid);
		try {
			s.delete(studentPart2);
			return new Result(true, "删除成功");
		} catch (Exception e) {
			return new Result(false, "删除失败");
		}
	}

}