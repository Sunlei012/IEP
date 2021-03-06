package com.ysd.iep.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Entity          //使用默认类名
@Table(name = "coursetb")
@Data
public class Course implements Serializable{
	@Id
	@GeneratedValue
	@Column(columnDefinition = "int unsigned  AUTO_INCREMENT COMMENT '课程ID'", nullable = false)
	private Integer courId;  //课程ID
	@Column(columnDefinition = "varchar(50) NOT NULL COMMENT '课程名称'")
	private String courName;
	@Column(columnDefinition="varchar(100) not NULL comment '备注:课程所属用户(教师)'")
	private String courTeaid;
	@Column(columnDefinition="varchar(50)  NULL comment '备注:课程图片地址'")
	private String courPicurl;
	@Column(columnDefinition="double  NULL  DEFAULT 0.0 comment '备注:课程价格'")
	private Double courPrice;
	@Column(columnDefinition="double  NULL  DEFAULT 0.0 comment '备注:课程优惠价格'")
	private Double courNocount;//
	@Column(columnDefinition="varchar(500)  NULL comment '备注:课程内容'")
	private String courContent;
	@Column(columnDefinition="varchar(1000)  NULL comment '备注:课程描述'")
	private String courDetails;
	@Column(columnDefinition="int  NULL comment '备注:浏览量'")
	private Integer courPageview;//浏览量
	@Column(columnDefinition="varchar(500)  NULL comment '备注:评分标准'")
	private String courScore;//评分标准
	@Column(columnDefinition="varchar(500)  NULL comment '备注:课程目标'")
	private String courTarget;//课程目标
	@Column(columnDefinition="varchar(200)  NULL comment '备注:参考资料'")
	private String courResources;//参考资料
	@Column(columnDefinition="varchar(200)  NULL comment '备注:预备知识'")
	private String courPropaedeutics;//预备知识
	@Column(columnDefinition="char(2) DEFAULT 0  NULL comment '备注:是否上架(0:否 1:是)'")
	private String courIsputaway;//是否上架
	@Column(columnDefinition="datetime  NULL comment '备注:开课时间'")
//	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private Date courOpentime;//开课时间
	@Column(columnDefinition="datetime  NULL comment '备注:开课时间'")
//	@JsonFormat(locale="zh", timezone="GMT+8", pattern="yyyy-MM-dd HH:mm:ss")
	private Date courClosetime;//开课时间
	@Column(columnDefinition="varchar(50)  NULL comment '备注:学 时'")
	private String courClasshour;//学时
	@Column(columnDefinition="int  NULL comment '备注:课程评价数量'") 
	private Integer courCommentcount;//课程评价数量
	@Column(columnDefinition="int DEFAULT 0  NULL comment '备注:报名人数'")
	private Integer courStudypeople;//报名人数
	@Column(columnDefinition="int   NULL comment '备注:学分'") 
	private Integer courMark;
	@Column(columnDefinition="int   NULL comment '备注:预留1'") 
	private Integer Ext1;
	@Column(columnDefinition="varchar(200)   NULL comment '备注:预留2'") 
	private String Ext2;
}
	
	
	
	
	
	