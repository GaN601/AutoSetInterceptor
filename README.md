# AutoSetInterceptor
## 这是什么?
这是一个Mybatis拦截器, 当程序从数据库中读取数据并装配成对象后, 对被注解的字段进行自动赋值.
## 为什么这么做?
这是我在遇到的一个项目中看到Service层都添加了一个handlerPojo来装配数据, 便想到通过拦截器来自动设置被注解的值, 来让代码更易读.
## 注意
1. mapper层如果返回map未进行处理.
## 示例
源:
```java
import org.redisson.api.RBitSet;
import org.redisson.api.RedissonClient;

@Service
public class UserServiceImpl extends ServiceImpl<Mapper, User> implements UserService {
    public User getOne() {
        return handlerUserInfo(baseMapper.selectById(1));
    }
    
    private RedissonClient redissonClient;
    
    @Autowired
    public void setRedissonClient(final RedissonClient redissonClient) {this.redissonClient = redissonClient;}
    
    
    private User handlerUserInfo(User user) {
        RBitSet bitSet = redissonClient.getBitSet("user:like:" + user.getId());
        user.setLikeCount(bitSet.cardinality());
        return user;
    }
}
```
使用注解后, 可以取缔handlerUserInfo():
```java
import indi.gan.annotation.AutoSet;
import indi.gan.annotation.SetRedisValue;
import indi.gan.enums.RedisType;
import lombok.Data;

@Data
@AutoSet
public class User {
    private Integer id;
    @SetRedisValue(fieldValue = "id", type = RedisType.BITSET,prefix = "user:like:%d")
    private Long likeCount;
}
```