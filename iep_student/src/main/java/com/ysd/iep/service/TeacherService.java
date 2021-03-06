package com.ysd.iep.service;

import com.ysd.iep.entity.dto.Course;
import com.ysd.iep.entity.query.CourseQuery;
import com.ysd.iep.util.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 老师服务
 * @author ASUS
 *
 */
@FeignClient("IEP-TEACHER")
public interface TeacherService {
	/**
	 * 获取推荐课程
	 */
	@GetMapping("/course/findCourseById")
	public List<Course> findCourseById(@RequestParam("courId")String courId);

	/**
	 * 报名参加时修改课程报名人数
	 */
	@PutMapping("/course/updateCourStudypeople")
	public void updateStudypeople(@RequestParam("courId")Integer courId);

	/**
	 * 获取课程公告信息
	 */
	@GetMapping("/notice/queryNoticeByCourId")
	public Result queryNoticeByCourId(@RequestParam("courId") Integer courId);

	/**
	 * 根据类别id查询该类别下的课程按照报名数取前六条
	 */
	@GetMapping("/course/queryCourByDepId")
	public Result getCourseByCategoryId(@RequestParam("depid")String depid);

	/**
	 * 根据院系id 查询返回课程id
	 */
	@GetMapping("/course/getByDepartId")
	public List<Integer> getCourseIdBy(@RequestParam("departmentId")String depid);

	/**
	 * 调模糊查询
	 */
	@GetMapping ("/course/getPaginate")
	public Object homeSearch(CourseQuery courseQuery);

}
