package com.xdcplus.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xdcplus.exceptions.enums.ExceptionEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 *  数据格式返回统一
 * @author Rong.Jia
 * @date 2019/4/2
 */
@Data
@ApiModel("返回对象")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Response<T> implements Serializable {

    private static final long serialVersionUID = 3681838956784534606L;

    /**
     * 异常码
     */
    @ApiModelProperty("异常码")
    private Integer code;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String message;

    /**
     * 数据
     */
    @ApiModelProperty("数据")
    private T data;

    public Response() {}

    public Response(Integer code, String msg) {
        this.code = code;
        this.message = msg;
    }

    public Response(Integer code, String msg, T data) {
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    public Response(ExceptionEnum exceptionEnum) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
    }

    public Response(ExceptionEnum exceptionEnum, T data) {
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessage();
        this.data = data;
    }

    public static <T> Response<T> success(){
        return new Response<>(ExceptionEnum.SUCCESS);
    }

    public static <T> Response<T> success(T data){
        return new Response<>(ExceptionEnum.SUCCESS, data);
    }

    public static <T> Response<T> error(T data){
        return new Response<>(ExceptionEnum.ERROR, data);
    }

    public static <T> Response<T> success(int code, String msg){
        return new Response<>(code, msg);
    }

    public static <T> Response<T> error(int code, String msg){
        return new Response<>(code, msg);
    }

    public static <T> Response<T> error(ExceptionEnum exceptionEnum){
        return new Response<>(exceptionEnum);
    }

    public static Response<?> error(ExceptionEnum exceptionEnum, Object data){
        return new Response<>(exceptionEnum, data);
    }


}
