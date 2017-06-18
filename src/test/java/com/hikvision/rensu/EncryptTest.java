package com.hikvision.rensu;

/**
 * Created by rensu on 2017/6/18.
 */

import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import java.security.NoSuchAlgorithmException;

public class EncryptTest {

    @Test
    public void md5() {
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        // false 表示：生成32位的Hex版, 这也是encodeHashAsBase64的, Acegi 默认配置; true  表示：生成24位的Base64版
        md5.setEncodeHashAsBase64(false);

        Assert.assertEquals("81dc9bdb52d04dc20036dbd8313ed055", md5.encodePassword("1234", null));
    }

    @Test
    public void sha_256() throws NoSuchAlgorithmException {
        ShaPasswordEncoder sha = new ShaPasswordEncoder(256);
        sha.setEncodeHashAsBase64(true);
        Assert.assertEquals("A6xnQhbz4Vx2HuGl4lXwZ5U2I8iziLRFnhP5eNfIRvQ=", sha.encodePassword("1234", null));
    }

    @Test
    public void sha_SHA_256() {
        ShaPasswordEncoder sha = new ShaPasswordEncoder();
        sha.setEncodeHashAsBase64(false);
        Assert.assertEquals("7110eda4d09e062aa5e4a390b0a572ac0d2c0220", sha.encodePassword("1234", null));
    }


    @Test
    public void md5_SystemWideSaltSource() {
        Md5PasswordEncoder md5 = new Md5PasswordEncoder();
        md5.setEncodeHashAsBase64(false);

        // 使用动态加密盐的只需要在注册用户的时候将第二个参数换成用户名即可
        Assert.assertEquals("3fe8237bfea636742b698793500407ce", md5.encodePassword("1234", "acegisalt"));
    }

}
