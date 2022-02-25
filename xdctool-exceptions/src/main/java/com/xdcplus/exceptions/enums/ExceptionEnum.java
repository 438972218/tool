package com.xdcplus.exceptions.enums;

/**
 *  数据信息状态枚举类
 * @author Rong.Jia
 * @date 2019/4/2
 */
public enum ExceptionEnum {

    /**
     *  枚举类code 开头使用规则：
     *  0: 成功；
     *  -1: 失败；
     *  1：参数不正确
     *  401： 登录相关  需跳登录
     *  404：未找到
     *  405：请求方式错误
     *  415：媒体类型不支持
     */

    // 成功
    SUCCESS(0,"成功"),

    // 参数不正确
    PARAMETER_ERROR(1, "参数不正确"),

    // 失败
    ERROR(-1, "失败"),
    SYSTEM_ERROR(500, "系统错误"),
    FILE_LIMIT_EXCEEDED(-1, "文件超出限制, 请选择较小文件"),

    // 未找到
    NOT_FOUND(404, "请求接口不存在"),

    // 请求方式错误
    REQUEST_MODE_ERROR(405, "请求方式错误, 请检查"),

    //媒体类型不支持
    MEDIA_TYPE_NOT_SUPPORTED(415, "媒体类型不支持"),

    REQUEST_PARAMETER_FORMAT_IS_INCORRECT(9999, "请求参数格式不正确"),
    THE_PARAMETER_TYPE_IS_INCORRECT(9999, "参数类型不正确"),
    LACK_OF_PARAMETER(9999, "缺少必要参数，请检查"),








































    ;

    private Integer code;
    private String message;

    ExceptionEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
