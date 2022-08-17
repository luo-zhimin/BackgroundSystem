package com.background.system.exception;


/**
 * 校验异常
 *
 * @author meng
 */
public class VerifyException extends Exception {

    private int code;

    private String exceptionMsg;

    public int getCode() {
        return code;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public VerifyException(VerifyExceptionBuilder securityExceptionBuilder) {
        this.code = securityExceptionBuilder.code;
        this.exceptionMsg = securityExceptionBuilder.exceptionMsg;
    }

    public static VerifyExceptionBuilder builder() {
        return new VerifyExceptionBuilder();
    }

    public static class VerifyExceptionBuilder {

        private int code;

        private String exceptionMsg;

        private VerifyExceptionBuilder() {
        }

        public VerifyExceptionBuilder code(int code) {
            this.code = code;
            return this;
        }

        public VerifyExceptionBuilder exceptionMsg(String exceptionMsg) {
            this.exceptionMsg = exceptionMsg;
            return this;
        }

        public VerifyException build() {
            return new VerifyException(this);
        }

    }

}
