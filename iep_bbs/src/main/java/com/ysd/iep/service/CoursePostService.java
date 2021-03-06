package com.ysd.iep.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ysd.iep.entity.CoursePost;
import com.ysd.iep.entity.CoursePostQuery;
import com.ysd.iep.entity.CourseReply;
import com.ysd.iep.entity.Reply;

public interface CoursePostService {
	
	public Page<CoursePost> queryAllCoursePost(CoursePostQuery postQuery, Pageable pageable);
	
	public CourseReply getPostDetails(Integer postId, Integer parentId);
	
	public Integer getLikeNum(Integer replyId);
	/**
	 * 更新帖子点赞数
	 * 
	 * @param replyId
	 * @return
	 */
	public Integer updateLikeNum(Integer replyId, Integer likeNum);
	
	/**
	 * 发表帖子
	 * @param title
	 * @param content
	 * @param parentId
	 * @param postId
	 * @return
	 */
	public Integer publicPost(Integer courseId,String title, String content, Integer parentId,String userId,Integer typeId);
	/**
	 * 获取热帖
	 * @param courseIds
	 * @return
	 */
	public List<CoursePost> getHotPost(List<Integer> courseIds);

	/**
	 * 通过帖子id获取帖子
	 * @param postId
	 * @return
	 */
	public CoursePost getPostByPostId(Integer postId);

	/**
	 * 查询帖子回复数
	 * @param postId
	 * @return
	 */
	public Integer getReplyNum(Integer postId);
	/**
	 * 更新帖子回复数
	 * @param replynum
	 * @param postId
	 */
	public Integer updateReplyNum(Integer replynum, Integer postId);

}
