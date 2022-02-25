package com.xdcplus.exceptions;

import com.xdcplus.exceptions.enums.ExceptionEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 *   项目自定义异常
 * @author Rong.Jia
 * @date 2019/4/3
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GlobalException extends RuntimeException  implements Serializable {

    private static final long serialVersionUID = -1501020198729282518L;

    /**
     *  异常code 码
     */
    private Integer code;

    /**
     * 异常详细信息
     */
    private String message;

    public GlobalException(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public GlobalException(ExceptionEnum exceptionEnum) {
        super(exceptionEnum.getMessage());
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    public GlobalException(ExceptionEnum exceptionEnum, String message) {
        super(message);
        this.code = exceptionEnum.getCode();
        this.message = message;
    }

    public GlobalException(Integer code, String message, Throwable t) {
        super(message, t);
        this.code = code;
        this.message = message;
    }

    public GlobalException(ExceptionEnum exceptionEnum, Throwable t) {
        super(exceptionEnum.getMessage(), t);
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }


}
