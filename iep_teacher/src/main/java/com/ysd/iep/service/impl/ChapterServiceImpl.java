package com.ysd.iep.service.impl;

import java.util.ArrayList;
import java.util.List;


import com.ysd.iep.entity.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.ysd.iep.dao.ChapterRepository;
import com.ysd.iep.entity.Chapters;
import com.ysd.iep.entity.Course;
import com.ysd.iep.service.ChapterService;
@Service
public class ChapterServiceImpl implements ChapterService{

	@Autowired
	private ChapterRepository chaperRepo;
	
	/**
	 * 查询章节
	 * @return
	 */
	@Override
	public List<Chapters> querychapterTree(Integer courid) {
		List<Chapters> rootList = chaperRepo.queryTreeChildrenById(0,courid);
		System.out.println("查询出所有根菜单rootList==>" + rootList);
		this.setTreeChildrens(rootList,courid);
		return rootList;
	}
    /**
	 * 给菜单模块 设置孩子
	 * @param parentList
	 */
	private void setTreeChildrens(List<Chapters> parentList,Integer courId) {
		for (Chapters c : parentList) {
			//查出子菜单
			List<Chapters> childrenList=chaperRepo.queryTreeChildrenById(c.getChaId(),courId);
			// 如果没有子菜单则递归结束
			if (childrenList == null || childrenList.isEmpty()) {// 有子菜单
			} else {
				// 设置子菜单
				System.out.println("设置的子菜单是=>" + childrenList);
				c.setChildren(childrenList);
				// 如果有子菜单则继续递归设置子菜单
				this.setTreeChildrens(childrenList,courId);
			}
		}
		
	}

	@Override
	public Result insertChapters(Chapters chapters) {
		chaperRepo.save(chapters);
		return new Result(true);
	}

	@Override
	public void deleteChapters(Integer chaId) {
		chaperRepo.deleteById(chaId);
	}
	/**
	 * 修改章节
	 */
	@Override
	public Result updateCourse(Chapters chapters) {
		Chapters c = chaperRepo.getOne(chapters.getChaId());
        c.setChaName(chapters.getChaName());
        chaperRepo.save(c);
        return new Result(true);
}
}