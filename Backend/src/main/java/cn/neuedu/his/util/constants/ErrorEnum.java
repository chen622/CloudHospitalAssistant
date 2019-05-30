package cn.neuedu.his.util.constants;

/**
 * 错误信息枚举类
 */
public enum ErrorEnum {
    /*
     * 错误信息
     * */
    E_500("500", "请求处理异常，请稍后再试"),
    E_501("501", "参数异常"),
    E_502("502","权限检验异常"),
    E_503("503", "诊疗状态异常"),
    E_504("504", "缴费单生成失败"),
    E_505("505", "发票生成失败"),
    E_506("506", "缴费单不支持退费"),
    E_507("507", "退费数量越界"),
    E_600("600","用户名已被使用"),
    E_601("601","用户信息不存在"),
    E_602("602","没有权限"),
    E_603("603","挂号类型已存在"),
    E_604("604","挂号类型不存在"),
    E_605("605","结算类型名已存在"),
    E_606("606","结算类型名不存在"),
    E_607("607","插入失败"),
    E_608("608","非药品类型不存在"),
    E_609("609","执行部门不存在"),
    E_610("610","部门不存在");

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

    public ErrorEnum addErrorParamName(String name){
        this.errorMsg = name + " " +this.errorMsg;
        return this;
    }
}
