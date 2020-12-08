package com.king.common.exception;

import java.io.Serializable;

/**
 * 错误编码
 *
 * @author jinfeng.wu
 * @date 2020/9/15 9:56
 */
public interface IResultCode extends Serializable {
    String getMessage();

    int getCode();
}
