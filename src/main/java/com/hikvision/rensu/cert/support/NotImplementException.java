package com.hikvision.rensu.cert.support;

/**
 * Created by rensu on 2017/5/28.
 */
public class NotImplementException extends UnsupportedOperationException {
    private static final long serialVersionUID = 20131011L;
    private final String code;

    public NotImplementException(String message) {
        this(message, (String) null);
    }

    public NotImplementException(Throwable cause) {
        this((Throwable) cause, (String) null);
    }

    public NotImplementException(String message, Throwable cause) {
        this(message, cause, (String) null);
    }

    public NotImplementException(String message, String code) {
        super(message);
        this.code = code;
    }

    public NotImplementException(Throwable cause, String code) {
        super(cause);
        this.code = code;
    }

    public NotImplementException(String message, Throwable cause, String code) {
        super(message, cause);
        this.code = code;
    }

    public String getCode() {
        return this.code;
    }
}
