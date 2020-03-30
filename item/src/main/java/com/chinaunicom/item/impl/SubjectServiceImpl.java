package com.chinaunicom.item.impl;

import com.chinaunicom.hsf.annotation.ProxyProvider;
import com.chinaunicom.item.SubjectService;
import com.chinaunicom.item.bo.PrimitiveSubjectBo;
import com.chinaunicom.item.mapper.SubjectMapper;
import com.chinaunicom.item.mapper.po.PrimitiveSubjectPo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/3/29 20:47
 * @description:
 */

@Service
@ProxyProvider(version = "${ITEM_VERSION}", group = "${ITEM_GROUP}")
public class SubjectServiceImpl implements SubjectService {
    @Autowired
    private SubjectMapper subjectMapper;

    @Override
    public PrimitiveSubjectBo getSubject(int id) {
        PrimitiveSubjectBo primitiveSubjectBo=new PrimitiveSubjectBo();
        PrimitiveSubjectPo subjectPo = subjectMapper.getSubject(id);
        BeanUtils.copyProperties(subjectPo, primitiveSubjectBo);
        return primitiveSubjectBo;
    }
}
