package com.ysd.iep.dao;

import com.ysd.iep.entity.Answer;
import com.ysd.iep.entity.Performance;
import com.ysd.iep.entity.Sectionexamlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SectionexamlogDao extends JpaRepository<Sectionexamlog, String> {


    /**
     * 根据课程id 章节id 学生id 卷子id 查询章节测试记录
     *
     * @param courid
     * @param sectionid
     * @param studentid
     * @param sectionparperid
     * @return
     */
    @Query(value = "select *from sectionexamlog_tb where course_id=?1 and section_id=?2 and student_id=?3 and sectionexamparper_id=?4", nativeQuery = true)
    List<Sectionexamlog> selectperformanforparperidandstudentid(String courid, String sectionid, String studentid, String sectionparperid);


}