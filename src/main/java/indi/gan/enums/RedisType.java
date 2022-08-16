package indi.gan.enums;

/**
 * @author GaN
 * @since 2022/8/3
 */
public enum RedisType {
    /**
     * 操作类型
     */
    HASH,
    /**
     * 获取key指向的bitset中所有标记为1的计数
     */
    BITSET,
    /**
     * 检测对应的位是否有设置值, 需要填写args
     * <p>第一个参数填写当前对象的一个可以转为数值的字段</p>
     */
    IS_SET_BIT,
    STRING, ZSET
    
}
