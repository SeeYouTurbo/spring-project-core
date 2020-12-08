package com.king.common.support;

import com.king.common.util.StringUtil;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

public class Charsets {
    public static final Charset ISO_8859_1 = StandardCharsets.ISO_8859_1;

    public static final Charset GBK = Charset.forName("GBK");

    public static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static Charset charset(String charsetName) throws UnsupportedCharsetException {
        return StringUtil.isBlank(charsetName) ? Charset.defaultCharset() : Charset.forName(charsetName);
    }
}
