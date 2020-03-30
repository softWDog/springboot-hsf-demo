# springboot-hsf-demo
springboot 整合hsf 分布式框架


Demo入口，detail 工程，启动请参照阿里官网，[Ali-Tomcat概述](hhttps://help.aliyun.com/document_detail/100087.html?spm=a2c4g.11186623.6.592.66e2465doq6XUR)
1、controller 引用的是分布式服务的，引入的是item-api工程.
```java
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

```
2、item-api工程总的SubjectService, ProxyConsumer注解的会自动注册hsf consumer bean到spring 容器中去
```java
@ProxyConsumer(beanId = "subjectService", version = "${ITEM_VERSION}", group = "${ITEM_GROUP}", clientTimeout = 60000)
public interface SubjectService {
    PrimitiveSubjectBo getSubject(int id);
}
```


3、item工程，web工程，启动请参照1步。ProxyProvider注解注解的类会自动注册hsf provider bean到spring容器中去，供远程调用。
```java
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
```


ProxyProvider和ProxyConsumer注解，自动注册consumer和provider 到spring 容器的具体功能实现，都在hsf-xml-generate工程实现，所有需要引入这个功能的都需要依赖这个工程。
