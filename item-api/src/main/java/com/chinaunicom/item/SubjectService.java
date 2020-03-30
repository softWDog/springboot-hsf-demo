package com.chinaunicom.item;

import com.chinaunicom.hsf.annotation.ProxyConsumer;
import com.chinaunicom.item.bo.PrimitiveSubjectBo;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/3/28 23:28
 * @description:
 */
@ProxyConsumer(beanId = "subjectService", version = "${ITEM_VERSION}", group = "${ITEM_GROUP}", clientTimeout = 60000)
public interface SubjectService {
    PrimitiveSubjectBo getSubject(int id);
}
