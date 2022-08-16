package indi.gan.handler;


import indi.gan.domain.FieldHandlerDTO;

/**
 * 一个基础接口, 实现他来让Spring找到新增的字段处理器
 * @author GaN
 * @since 2022/8/15
 */
public abstract class FieldHandler implements Handler<FieldHandlerDTO> {
}
