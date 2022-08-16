# AutoSetInterceptor
## 这是什么?
这是一个Mybatis拦截器, 当程序从数据库中读取数据并装配成对象后, 对被注解的字段进行自动赋值.
## 为什么这么做?
这是我在项目中看到Service层都添加了一个handlerPojo来装配数据, 便想到通过拦截器来自动设置被注解的值, 来让代码更易读.
## 示例
源:
```java
public class UserServiceImpl extends ServiceImpl<Mapper, User> implements UserService{
    public User getOne(){
        User user = handlerUserInfo(baseMapper.selectById(1));
    }
}
```