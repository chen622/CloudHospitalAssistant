package cn.neuedu.his.util.constants;

/**
 * 错误信息枚举类
 */
public enum ErrorEnum {
    /*
     * 错误信息
     * */
    E_400("500", "请求处理异常，请稍后再试");


    private String errorCode;

    private String errorMsg;

    ErrorEnum() {
    }

    ErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
