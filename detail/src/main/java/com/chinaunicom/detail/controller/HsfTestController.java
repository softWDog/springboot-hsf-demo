package com.chinaunicom.detail.controller;

import com.chinaunicom.detail.controller.reqVo.ResultVo;
import com.chinaunicom.detail.controller.reqVo.SubjectVo;
import com.chinaunicom.item.SubjectService;
import com.chinaunicom.item.bo.PrimitiveSubjectBo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
    @Autowired
    private SubjectService subjectService;

    @RequestMapping(value = "/test",method = RequestMethod.POST)
    public ResultVo<PrimitiveSubjectBo> test(@RequestBody SubjectVo subjectVo){
        ResultVo resultVo=new ResultVo();
        PrimitiveSubjectBo subjectBo= subjectService.getSubject(subjectVo.getId());
        resultVo.setData(subjectBo);
        return resultVo;
    }
}
