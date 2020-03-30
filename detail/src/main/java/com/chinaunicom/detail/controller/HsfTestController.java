package com.chinaunicom.detail.controller;

import com.chinaunicom.detail.controller.reqVo.SubjectVo;
import com.chinaunicom.item.SubjectService;
import com.chinaunicom.item.bo.PrimitiveSubjectBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/3/29 21:07
 * @description:
 */
@RestController
public class HsfTestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SubjectService subjectService;

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public PrimitiveSubjectBo test(@RequestBody SubjectVo subjectVo){
        logger.info("测试接口入参："+subjectVo.toString());
        PrimitiveSubjectBo subjectBo=null;
        try {
            subjectBo= subjectService.getSubject(subjectVo.getId());
        } catch (Exception e) {
           logger.error("查询异常", e);
        }
        return subjectBo;
    }
}
