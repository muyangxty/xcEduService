package com.xuecheng.framework.exception;

import com.xuecheng.framework.model.response.ResultCode;
import lombok.Getter;

/**
 * 自定义异常类型
 *
 * @author MuYang
 * @date 2019/11/26
 */
@Getter
public class CustomException extends RuntimeException {

    //错误代码
    ResultCode resultCode;

    public CustomException(ResultCode resultCode){
        this.resultCode = resultCode;
    }

}
