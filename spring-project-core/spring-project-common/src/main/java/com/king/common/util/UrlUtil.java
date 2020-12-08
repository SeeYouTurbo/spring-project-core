package com.king.common.util;

import org.springframework.web.util.UriUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;

/**
 * url 工具类
 *
 * @author jinfeng.wu
 * @date 2020/8/17 13:59
 */
public class UrlUtil extends UriUtils {

    public static String encodeUrl(String source, Charset charset) {
        return encode(source, charset.name());
    }

    public static String decodeUrl(String source, Charset charset) {
        return decode(source, charset.name());
    }

    public static String getPath(String uriStr) {
        URI uri;
        try {
            uri = new URI(uriStr);
        } catch (URISyntaxException var3) {
            throw new RuntimeException(var3);
        }

        return uri.getPath();
    }
}
