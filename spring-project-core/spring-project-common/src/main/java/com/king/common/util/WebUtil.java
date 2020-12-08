package com.king.common.util;
import com.king.common.exception.SecureException;
import com.king.common.support.Charsets;
import lombok.Builder;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.util.Enumeration;

public class WebUtil extends WebUtils {
    private static final Logger log = LoggerFactory.getLogger(WebUtil.class);

    public static final String USER_AGENT_HEADER = "user-agent";

    private static final String UN_KNOWN = "unknown";

    public static boolean isBody(HandlerMethod handlerMethod) {
        ResponseBody responseBody = ClassUtil.<ResponseBody>getAnnotation(handlerMethod, ResponseBody.class);
        return (responseBody != null);
    }

    @Nullable
    public static String getCookieVal(String name) {
        HttpServletRequest request = getRequest();
        Assert.notNull(request, "request from RequestContextHolder is null");
        return getCookieVal(request, name);
    }

    @Nullable
    public static String getCookieVal(HttpServletRequest request, String name) {
        Cookie cookie = getCookie(request, name);
        return (cookie != null) ? cookie.getValue() : null;
    }

    public static void removeCookie(HttpServletResponse response, String key) {
        setCookie(response, key, (String)null, 0);
    }

    public static void setCookie(HttpServletResponse response, String name, @Nullable String value, int maxAgeInSeconds) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAgeInSeconds);
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static HttpServletRequest getRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        return (requestAttributes == null) ? null : ((ServletRequestAttributes)requestAttributes).getRequest();
    }

    public static void renderJson(HttpServletResponse response, Object result) {
        renderJson(response, result, "application/json;charset=UTF-8");
    }

    public static void renderJson(HttpServletResponse response, Object result, String contentType) {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(contentType);
        try (PrintWriter out = response.getWriter()) {
            out.append(JsonUtil.toJson(result));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public static String getIP() {
        return getIP(getRequest());
    }

    @Nullable
    public static String getIP(HttpServletRequest request) {
        Assert.notNull(request, "HttpServletRequest is null");
        String ip = request.getHeader("X-Requested-For");
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("X-Forwarded-For");
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("Proxy-Client-IP");
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("WL-Proxy-Client-IP");
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_CLIENT_IP");
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        if (StringUtil.isBlank(ip) || "unknown".equalsIgnoreCase(ip))
            ip = request.getRemoteAddr();
        return StringUtil.isBlank(ip) ? null : ip.split(",")[0];
    }

    public static String getRequestParamString(HttpServletRequest request) {
        try {
            return getRequestStr(request);
        } catch (Exception ex) {
            return "";
        }
    }

    public static String getRequestStr(HttpServletRequest request) throws IOException {
        String queryString = request.getQueryString();
        if (StringUtil.isNotBlank(queryString))
            return (new String(queryString.getBytes(Charsets.ISO_8859_1), Charsets.UTF_8)).replaceAll("&amp;", "&").replaceAll("%22", "\"");
        return getRequestStr(request, getRequestBytes(request));
    }

    public static byte[] getRequestBytes(HttpServletRequest request) throws IOException {
        int contentLength = request.getContentLength();
        if (contentLength < 0)
            return null;
        byte[] buffer = new byte[contentLength];
        for (int i = 0; i < contentLength; ) {
            int readlen = request.getInputStream().read(buffer, i, contentLength - i);
            if (readlen == -1)
                break;
            i += readlen;
        }
        return buffer;
    }

    public static String getRequestStr(HttpServletRequest request, byte[] buffer) throws IOException {
        String charEncoding = request.getCharacterEncoding();
        if (charEncoding == null)
            charEncoding = "UTF-8";
        String str = (new String(buffer, charEncoding)).trim();
        if (StringUtil.isBlank(str)) {
            StringBuilder sb = new StringBuilder();
            Enumeration<String> parameterNames = request.getParameterNames();
            while (parameterNames.hasMoreElements()) {
                String key = parameterNames.nextElement();
                String value = request.getParameter(key);
                StringUtil.appendBuilder(sb, new CharSequence[] { key, "=", value, "&" });
            }
            str = StringUtil.removeSuffix(sb.toString(), "&");
        }
        return str.replaceAll("&amp;", "&");
    }

    public static VvCurrentAccount getCurrentAccount() {
        HttpServletRequest request = getRequest();
        if (request != null) {
            String userCode = request.getHeader("current_account_user_code");
            if (Func.isNotBlank(userCode)) {
                VvCurrentAccount dto = VvCurrentAccount.builder().userCode(userCode).build();
                dto.setLoginName(request.getHeader("current_account_login_name"));
                dto.setOrganizeType(Integer.valueOf(request.getIntHeader("current_account_organize_type")));
                return dto;
            }
            throw new SecureException("请先登录");
        }
        return null;
    }

    public static VvCurrentAccount getCurrentAccount(Boolean isMustLogin) {
        if (isMustLogin.booleanValue())
            return getCurrentAccount();
        try {
            return getCurrentAccount();
        } catch (SecureException e) {
            return null;
        }
    }

    @Data
    @Builder
    public class VvCurrentAccount implements Serializable {
        private String loginName;

        private String userCode;

        private Integer organizeType;
    }
}

