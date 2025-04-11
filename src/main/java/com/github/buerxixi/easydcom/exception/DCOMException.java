package com.github.buerxixi.easydcom.exception;

/**
 * 自定义 DCOM 异常类，继承自 RuntimeException。
 * 该类用于处理 DCOM 相关的异常情况。
 *
 * @author liujiaqiang
 * @since 2025-04-03
 */
public class DCOMException extends RuntimeException {

    /**
     * 构造一个带有指定错误消息的 DCOMException 实例。
     *
     * @param message 错误消息
     */
    public DCOMException(String message) {
        super(message);
    }

    /**
     * 构造一个带有指定错误消息和原因的 DCOMException 实例。
     *
     * @param message 错误消息
     * @param cause 异常的原因
     */
    public DCOMException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造一个带有指定原因的 DCOMException 实例。
     *
     * @param cause 异常的原因
     */
    public DCOMException(Throwable cause) {
        super(cause);
    }
}