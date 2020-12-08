package com.king.common.util;


import org.springframework.http.HttpStatus;

import java.io.Serializable;

/**
 * @Description: 返回对象
 * @Author: Jianhui Liu(jianhui.liu@luckincoffee.com)
 * @Date: 2019/11/11 14:39
 */
public class Result<T> implements Serializable {


    private static final long serialVersionUID = -6056995887705042320L;
    /**
     * 服务状态码。为描述一致，0定义为成功。负数为失败，-500默认为服务器内部错误
     */
    private int status= STATUS_500;
    /**
     * 服务错误详情码
     */
    private int errCode;
    /**
     * 错误描述信息。
     */
    private String msg;
    /**
     * 返回数据。
     */
    private T re;

    /**
     * 异常信息
     */
    private String exceptionMsg;

    /**
     * 无参构造
     */
    public Result(){

    }

    /**
     * 全参构造
     * @param re 数据对象
     * @param status 状态值
     * @param msg 提示语
     */
    public Result(T re, int status, String msg ,String exceptionMsg) {
        super();
        this.status = status;
        this.msg = msg;
        this.re = re;
        this.exceptionMsg = exceptionMsg;
    }

    /**
     * 全参构造
     * @param re 数据对象
     * @param status 状态值
     * @param msg 提示语
     */
    public Result(T re, int status, int errCode, String msg) {
        super();
        this.status = status;
        this.errCode = errCode;
        this.msg = msg;
        this.re = re;
    }

    /**
     * 成功结果
     * @param t 返回结果
     * @return 包装后的结果
     */
    public static <T> Result<T> success(T t) {
        return new Result<>(t, STATUS_SUCCESS, MSG_SUCCESS,null);
    }

    /**
     * 成功结果
     * @param msg 成功信息
     * @param <T> 成功结果
     * @return 包装后的结果
     */
    public static <T> Result<T> success(String msg){
        return new Result<>(null, STATUS_SUCCESS, msg,null);
    }

    /**
     * 成功结果
     * @param t 返回结果
     * @param msg 成功信息
     * @param <T> 成功结果
     * @return 包装后的结果
     */
    public static <T> Result<T> success(T t , String msg){
        return new Result<>(t, STATUS_SUCCESS, msg,null);
    }

    /**
     * 空的成功结果
     * @return 包装后的结果
     */
    public static <T> Result<T> success() {
        return new Result<>(null, STATUS_SUCCESS, MSG_SUCCESS,null);
    }


    /**
     * 业务失败处理结果
     * @param msgCode 失败编码
     * @param message 消息
     * @return 包装后的结果
     */
    public static <T> Result<T> fail(int msgCode, String message) {
        return new Result<>(null, STATUS_FAIL, msgCode, message);
    }


    /**
     * 系统失败结果
     * @param msg 失败信息
     * @param exceptionMsg 异常信息
     * @return com.lucky.luckycontract.common.Result<T>
     */
    public static <T> Result<T> error(String msg,Exception exceptionMsg) {
        return new Result<>(null, STATUS_500, msg,exceptionMsg.getLocalizedMessage());
    }

    /**
     * 系统失败结果
     * @param msg 失败信息
     * @param exceptionMsg 异常信息
     * @return com.lucky.luckycontract.common.Result<T>
     */
    public static <T> Result<T> error(String msg,String exceptionMsg){
        return new Result<>(null, STATUS_500, msg,exceptionMsg);
    }

    /**
     * 系统失败结果
     * @param t 返回失败对象
     * @param msg 失败信息
     * @param exceptionMsg 异常信息
     * @param <T>
     * @return com.lucky.luckycontract.common.Result<T>
     */
    public static <T> Result<T> error(T t, String msg, Exception exceptionMsg){
        return new Result<>(t, STATUS_500, msg,exceptionMsg.getLocalizedMessage());
    }

    /**
     * 系统失败结果
     * @param msg 失败信息
     * @return 包装后的结果
     */
    public static <T> Result<T> error(String msg){
        return new Result<>(null, STATUS_500, msg,null);
    }

    /**
     * 系统失败结果
     * @param msg 失败信息
     * @return 包装后的结果
     */
    public static <T> Result<T> error(int errCode, String msg){
        return new Result<>(null, STATUS_500, errCode, msg);
    }

    /**
     * 系统失败结果
     * @param msg 失败信息
     * @return 包装后的结果
     */
    public static <T> Result<T> error(int status, int errCode, String msg){
        return new Result<>(null, status, errCode, msg);
    }

    /**
     * 系统失败结果
     * @param msg 失败信息
     * @return 包装后的结果
     */
    public static <T> Result<T> error(HttpStatus httpStatus, String msg){
        return new Result<>(null, httpStatus.value(), msg,null);
    }

    /**
     *
     * @return
     */
    public static <T> Result<T> failureResult(){
        return new Result<>(null, STATUS_500, "系统发生不明错误，请联系管理员",null);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getRe() {
        return re;
    }

    public void setRe(T re) {
        this.re = re;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    /**
     * 成功
     */
    public static final int STATUS_SUCCESS = 10000;

    /**
     * 失败
     */
    public static final int STATUS_FAIL = -1;

    /**
     * 成功默认消息
     */
    public static final String MSG_SUCCESS = "success";

    /**
     * 服务器内部错误
     */
    public static final int STATUS_500 = 500;
}
