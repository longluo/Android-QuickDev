package me.longluo.android.http.api;

import com.hjq.http.config.IRequestApi;

/**
 * 修改手机
 */
public final class PhoneApi implements IRequestApi {

    @Override
    public String getApi() {
        return "user/phone";
    }

    /**
     * 旧手机号验证码（没有绑定情况下可不传）
     */
    private String preCode;

    /**
     * 新手机号
     */
    private String phone;
    /**
     * 新手机号验证码
     */
    private String code;

    public PhoneApi setPreCode(String preCode) {
        this.preCode = preCode;
        return this;
    }

    public PhoneApi setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public PhoneApi setCode(String code) {
        this.code = code;
        return this;
    }
}