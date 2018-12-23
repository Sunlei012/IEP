package com.ysd.iep.serviceimpl;

import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.ysd.iep.dao.ExamanswerDao;
import com.ysd.iep.dao.ExamparperDao;
import com.ysd.iep.dao.ExamrubricDao;
import com.ysd.iep.dao.RubricDao;
import com.ysd.iep.entity.*;
import com.ysd.iep.entity.parameter.Result;
import com.ysd.iep.entity.parameter.RubricQuery;
import com.ysd.iep.entitySerch.ExamParperSerch;
import com.ysd.iep.service.ExamparperService;
import com.ysd.iep.util.UUIDUtils;
import javafx.scene.input.InputMethodTextRun;
import org.hibernate.validator.internal.engine.messageinterpolation.parser.ELState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ExamparperServiceImpl implements ExamparperService {
    @Autowired
    private ExamparperDao examparperDao;
    @Autowired
    private RubricDao rubricDao;
    @Autowired
    RubricServiceimpl rubricServiceimpl;
    @Autowired
    ExamrubricDao examrubricDao;
    @Autowired
    ExamanswerDao examanswerDao;

    /***
     * 多条件分页查询试卷显示
     * @param examParperSerch
     * @return
     */
    @Override
    public Page<Examparper> queryqueryByDynamicSQLPageExpaerper(ExamParperSerch examParperSerch) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Sort sort = new Sort(Sort.Direction.DESC, "createtime");
        PageRequest pageable = PageRequest.of(examParperSerch.getPage() - 1, examParperSerch.getRows(), sort);
        return examparperDao.findAll(this.getWhereClause(examParperSerch), pageable);
    }

    /***
     * 设置开考时间
     * @param examparper
     * @return
     */
    @Override
    public Result updateStartExamtime(Examparper examparper) {
        if (examparper.getState().equals("考试中")) {
            return new Result(false, "考试中不能重新设置开考时间", null);
        } else if (examparper.getState().equals("考试结束")) {
            return new Result(false, "考试已经结束不能重新设置开考时间", null);
        } else {
            Examparper examparper1 = examparperDao.findById(examparper.getId()).orElse(null);
            examparper1.setExamtime(examparper.getExamtime());
            examparper1.setState("未开考");
            try {
                examparperDao.save(examparper1);
                return new Result(true, "设置成功", null);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, "设置失败", null);
            }
        }
    }

    /***
     * 试卷管理的分页显示sql拼接
     * @param examParperSerch
     * @return
     */
    private Specification<Examparper> getWhereClause(final ExamParperSerch examParperSerch) {
        return new Specification<Examparper>() {
            @Override
            public Predicate toPredicate(Root<Examparper> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                List exceptonList = predicate.getExpressions();
                if (examParperSerch.getSubject() != null && !"".equals(examParperSerch.getSubject())) {
                    exceptonList.add(criteriaBuilder.like(root.<String>get("subject"), "%" + examParperSerch.getSubject() + "%"));
                }

                if (examParperSerch.getTitle() != null && !"".equals(examParperSerch.getTitle())) {
                    exceptonList.add(criteriaBuilder.like(root.<String>get("title"), "%" + examParperSerch.getTitle() + "%"));
                }
                if (examParperSerch.getState() != null && !"".equals(examParperSerch.getState())) {
                    exceptonList.add(criteriaBuilder.like(root.<String>get("state"), "%" + examParperSerch.getState() + "%"));
                }


                return predicate;
            }
        };
    }

    /***
     * 根据id删除试卷
     * @param id
     * @return
     */
    @Override
    public Result deleteExamparper(String id) {

        try {
            examparperDao.deleteById(id);
            return new Result(true, "删除成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, "删除失败", null);
        }
    }

    /***
     * 随机生成试卷
     * @param examParperSerch
     * @return
     */
    @Transactional(rollbackFor = { Exception.class })
    public Result addRandomExamparper(ExamParperSerch examParperSerch) {
        String id = UUIDUtils.getUUID();
        Examparper examparper = new Examparper()
                .setId(id)
                .setTitle(examParperSerch.getTitle())
                .setTotal(examParperSerch.getTotal())
                .setState("未开放")
                .setSubject(examParperSerch.getSubject())
                .setDuration(examParperSerch.getDuration())
                .setExamshortesttime(examParperSerch.getExamshortesttime())
                .setRadionum(examParperSerch.getRadionum())
                .setJudgenum(examParperSerch.getJudgenum())
                .setFillnum(examParperSerch.getFillnum())
                .setMultiplenum(examParperSerch.getMultiplenum())
                .setTecherId(examParperSerch.getTeacherId())
                .setCreatetime(new Date());
                Examparper examparper1 = examparperDao.save(examparper);//新增一个试卷
        Result  result = this.randomrubric(examparper1, examParperSerch);//调用随机生成试题方法
                if (result.isSuccess()){
                    return result;
                }else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();//事务回滚
                    return result;
                }



        }




    /***
     * 随机添加试题
     * @param examparper
     * @param examParperSerch
     * @return
     */

    public Result randomrubric(Examparper examparper, ExamParperSerch examParperSerch) {
        try {
            System.out.println("试卷信息"+examParperSerch);
            //试题总分和每题分数之和不相等
            if (examParperSerch.getTotal()!=examParperSerch.getRadionum()*examParperSerch.getRadionumresource()+ examParperSerch.getJudgenum()*examParperSerch.getJudgenumresource()+examParperSerch.getFillnum()*examParperSerch.getFillnumresource()+examParperSerch.getMultiplenum()*examParperSerch.getMultiplenumresource()){
                return new Result(false,"试题总分和每题分数之和不相等,请修改",null);
            }

            RubricQuery pandunrubricQuery = new RubricQuery();
            //根据课程查询课程下的所有题干
            pandunrubricQuery.setCoursetype(examParperSerch.getSubject());
            List<Rubric> radiorubricList=new ArrayList<>();//单选题集合
            List<Rubric> judgerubricList=new ArrayList<>();//判断题集合
            List<Rubric> fillrubricList=new ArrayList<>();//填空题集合
            List<Rubric> multiplerubricList=new ArrayList<>();//多选选题集合
            //查询本门课程下的所有题干
            List<Rubric> rubricList12 = rubricServiceimpl.selectrubricfortype(pandunrubricQuery);
            for (Rubric rubric : rubricList12) {
                   if (rubric.getRubricttype().equals("单选题")){
                       radiorubricList.add(rubric);
                   }else if (rubric.getRubricttype().equals("判断题")){
                       judgerubricList.add(rubric);
                   }else if (rubric.getRubricttype().equals("填空题")){
                       fillrubricList.add(rubric);
                   }
                   else if (rubric.getRubricttype().equals("多选题")){
                       multiplerubricList.add(rubric);
                   }
            }






            //单选题
            if (radiorubricList == null || radiorubricList.isEmpty()) {
                return new Result(false, "题库没有单选题请添加", null);
            } else if (radiorubricList.size() < examParperSerch.getRadionum()) {
                return new Result(false, "单选题数量题库不够请添加试题,数量为:" + radiorubricList.size(), null);
            }
            //单选题
            for (int i = 0; i < examParperSerch.getRadionum(); i++) {
                Random rr = new Random();
                String idlist = UUIDUtils.getUUID();
                int a = rr.nextInt(radiorubricList.size());
                Rubric rubric = radiorubricList.get(a);
                // System.out.println("单选题第" + i + "道" + rubric);
                Examrubric examrubric = new Examrubric()
                        .setId(UUIDUtils.getUUID())
                        .setSectionId(rubric.getSectionId())
                        .setCourseId(rubric.getCourseId())
                        .setAnswerId(idlist)
                        .setContent(rubric.getContent())
                        .setTrcherId(rubric.getTrcherId())
                        .setScore(examParperSerch.getRadionumresource())
                        .setRubricttype(rubric.getRubricttype())
                        .setExamparper(examparper);
                //新增一个题干
                Examrubric examrubric1 = examrubricDao.save(examrubric);
                //获取本题干下的答案选项
                List<Answer> answer = rubric.getAnswer();
                if (answer == null || answer.isEmpty()){
                    return  new Result(false,rubric.getContent()+"单选题干没有选项请去题库添加",null);
                }
                for (Answer answer1 : answer) {
                    if (rubric.getAnswerId().equals(answer1.getId())) {
                        Examanswer examanswer = new Examanswer()
                                .setId(idlist)
                                .setOptiones(answer1.getOptiones())
                                .setContent(answer1.getContent())
                                .setExamrubric(examrubric1);
                        examanswerDao.save(examanswer);
                    } else {
                        Examanswer examanswer = new Examanswer()
                                .setId(UUIDUtils.getUUID())
                                .setOptiones(answer1.getOptiones())
                                .setContent(answer1.getContent())
                                .setExamrubric(examrubric1);
                        examanswerDao.save(examanswer);
                    }
                }
            }

            //判断题

            if (judgerubricList == null || judgerubricList.isEmpty()) {
                return new Result(false, "题库没有判断题请添加", null);
            } else if (judgerubricList.size() < examParperSerch.getJudgenum()) {
                return new Result(false, "判断题数量题库不够请添加试题,数量为:" + judgerubricList.size(), null);
            }
            for (int i = 0; i < examParperSerch.getJudgenum(); i++) {
                Random rrp = new Random();
                int ap = rrp.nextInt(judgerubricList.size());
                Rubric rubricp = judgerubricList.get(ap);
                Examrubric examrubric = new Examrubric()
                        .setId(UUIDUtils.getUUID())
                        .setSectionId(rubricp.getSectionId())
                        .setCourseId(rubricp.getCourseId())
                        .setAnswerId(rubricp.getAnswerId())
                        .setContent(rubricp.getContent())
                        .setTrcherId(rubricp.getTrcherId())
                        .setScore(examParperSerch.getJudgenumresource())
                        .setRubricttype(rubricp.getRubricttype())
                        .setExamparper(examparper);
                Examrubric examrubric1 = examrubricDao.save(examrubric);

            }
            //填空题

            if (fillrubricList == null || fillrubricList.isEmpty()) {
                return new Result(false, "题库没有填空题请添加", null);
            } else if (fillrubricList.size() < examParperSerch.getFillnum()) {
                return new Result(false, "填空题数量题库不够请添加试题,数量为:" + fillrubricList.size(), null);
            }
            for (int i = 0; i < examParperSerch.getFillnum(); i++) {
                Random rrt = new Random();
                int apt = rrt.nextInt(fillrubricList.size());
                Rubric rubricp = fillrubricList.get(apt);
                Examrubric examrubric = new Examrubric()
                        .setId(UUIDUtils.getUUID())
                        .setSectionId(rubricp.getSectionId())
                        .setCourseId(rubricp.getCourseId())
                        .setAnswerId(rubricp.getAnswerId())
                        .setContent(rubricp.getContent())
                        .setTrcherId(rubricp.getTrcherId())
                        .setScore(examParperSerch.getFillnumresource())
                        .setRubricttype(rubricp.getRubricttype())
                        .setExamparper(examparper);
                Examrubric examrubric1 = examrubricDao.save(examrubric);

            }
            //多选题

                if (multiplerubricList == null || multiplerubricList.isEmpty()) {
                    return new Result(false, "题库没有多选题请添加", null);
                } else if (multiplerubricList.size() < examParperSerch.getMultiplenum()) {
                    System.out.println("多选题不够");
                    return new Result(false, "多选题数量题库不够请添加试题,数量为:" + multiplerubricList.size(), null);
                }
                System.out.println("多选题数" + examParperSerch.getMultiplenum());


                List<Examanswer> examanswerduoxuan = new ArrayList<>();
                Examrubric examrubric1 = new Examrubric();
                List<String> duoxaunId = new ArrayList<>();

                //多选题
                for (int i = 0; i < examParperSerch.getMultiplenum(); i++) {
                    Random rrdx = new Random();
                    int adx = rrdx.nextInt(multiplerubricList.size());
                    Rubric rubric = multiplerubricList.get(adx);
                    String answerIdlist = rubric.getAnswerId();
                    String[] str = answerIdlist.split(",");
                    List<Answer> answer = new ArrayList<>();
                    for (String strs : str) {
                        answer = rubric.getAnswer();
                        if (answer == null || answer.isEmpty()){
                            return  new Result(false,rubric.getContent()+"多选题干没有选项请去题库添加",null);
                        }
                        for (Answer answer1 : answer) {
                            if (strs.equals(answer1.getId())) {
                                String iddanduoxua = UUIDUtils.getUUID();
                                Examanswer examanswer = new Examanswer()
                                        .setId(iddanduoxua)
                                        .setOptiones(answer1.getOptiones())
                                        .setContent(answer1.getContent());
                                examanswerduoxuan.add(examanswer);
                                duoxaunId.add(iddanduoxua);
                                answer.remove(answer1);
                                break;
                            }
                        }
                    }


                    String idAnswerId = this.listToString(duoxaunId);
                    Examrubric examrubric = new Examrubric()
                            .setId(UUIDUtils.getUUID())
                            .setSectionId(rubric.getSectionId())
                            .setCourseId(rubric.getCourseId())
                            .setAnswerId(idAnswerId)
                            .setContent(rubric.getContent())
                            .setTrcherId(rubric.getTrcherId())
                            .setScore(examParperSerch.getRadionumresource())
                            .setRubricttype(rubric.getRubricttype())
                            .setExamparper(examparper);
                    examrubric1 = examrubricDao.save(examrubric);
                    System.out.println("答案长度" + examanswerduoxuan.size());
                    System.out.println("");
                    for (Answer answer2 : answer) {
                        String iddanduoxua = UUIDUtils.getUUID();
                        Examanswer examanswer = new Examanswer()
                                .setId(iddanduoxua)
                                .setOptiones(answer2.getOptiones())
                                .setContent(answer2.getContent());
                        examanswerduoxuan.add(examanswer);
                    }
                    for (Examanswer examanswer : examanswerduoxuan) {
                        examanswer.setExamrubric(examrubric1);
                        examanswerDao.save(examanswer);
                    }
                    examanswerduoxuan.clear();
                    duoxaunId.clear();
                }

                return new Result(true, "创建成功", null);
            } catch (Exception e) {
                e.printStackTrace();
                return new Result(false, "创建失败", null);
            }


    }
    public static String listToString(List<String> stringList) {
        if (stringList == null) {
            return null;
        }
        StringBuilder result = new StringBuilder();
        boolean flag = false;
        for (String string : stringList) {
            if (flag) {
                result.append(",");
            } else {
                flag = true;
            }
            result.append(string);
        }
        return result.toString();
    }

}
