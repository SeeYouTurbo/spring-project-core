package com.king.common.util;

import com.king.common.support.RandomType;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.HtmlUtils;

import java.io.StringReader;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class StringUtil extends StringUtils {
    public static final int INDEX_NOT_FOUND = -1;
    private static final String S_INT = "0123456789";
    private static final String S_STR = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final String S_ALL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

    public static boolean isBlank(final CharSequence cs) {
        return !hasText(cs);
    }

    public static boolean isNotBlank(final CharSequence cs) {
        return hasText(cs);
    }

    public static boolean isAnyBlank(final CharSequence... css) {
        return ObjectUtil.isEmpty(css) ? true : Stream.of(css).anyMatch(StringUtil::isBlank);
    }

    public static boolean isNoneBlank(final CharSequence... css) {
        return ObjectUtil.isEmpty(css) ? false : Stream.of(css).allMatch(StringUtil::isNotBlank);
    }

    public static boolean isNumeric(final CharSequence cs) {
        if (isBlank(cs)) {
            return false;
        } else {
            int i = cs.length();

            char chr;
            do {
                --i;
                if (i < 0) {
                    return true;
                }

                chr = cs.charAt(i);
            } while(chr >= '0' && chr <= '9');

            return false;
        }
    }

    public static String join(Collection<?> coll) {
        return collectionToCommaDelimitedString(coll);
    }

    public static String join(Collection<?> coll, String delim) {
        return collectionToDelimitedString(coll, delim);
    }

    public static String join(Object[] arr) {
        return arrayToCommaDelimitedString(arr);
    }

    public static String join(Object[] arr, String delim) {
        return arrayToDelimitedString(arr, delim);
    }

    public static String randomUUID() {
        ThreadLocalRandom random = ThreadLocalRandom.current();
        return (new UUID(random.nextLong(), random.nextLong())).toString().replace("-", "");
    }

    public static String escapeHtml(String html) {
        return HtmlUtils.htmlEscape(html);
    }

    public static String cleanChars(String txt) {
        return txt.replaceAll("[ 　`·•�\u0001\\f\\t\\v\\s]", "");
    }

    public static String random(int count) {
        return random(count, RandomType.ALL);
    }

    public static String random(int count, RandomType randomType) {
        if (count == 0) {
            return "";
        } else {
            Assert.isTrue(count > 0, "Requested random string length " + count + " is less than 0.");
            ThreadLocalRandom random = ThreadLocalRandom.current();
            char[] buffer = new char[count];

            for(int i = 0; i < count; ++i) {
                if (RandomType.INT == randomType) {
                    buffer[i] = "0123456789".charAt(random.nextInt("0123456789".length()));
                } else if (RandomType.STRING == randomType) {
                    buffer[i] = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(random.nextInt("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length()));
                } else {
                    buffer[i] = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".charAt(random.nextInt("0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".length()));
                }
            }

            return new String(buffer);
        }
    }

    public static String indexedFormat(CharSequence pattern, Object... arguments) {
        return MessageFormat.format(pattern.toString(), arguments);
    }

    public static String format(CharSequence template, Map<?, ?> map) {
        if (null == template) {
            return null;
        } else if (null != map && !map.isEmpty()) {
            String template2 = template.toString();

            Map.Entry entry;
            for(Iterator var3 = map.entrySet().iterator(); var3.hasNext(); template2 = template2.replace("{" + entry.getKey() + "}", Func.toStr(entry.getValue()))) {
                entry = (Map.Entry)var3.next();
            }

            return template2;
        } else {
            return template.toString();
        }
    }

    public static List<String> split(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, false, false);
    }

    public static List<String> splitTrim(CharSequence str, char separator) {
        return splitTrim(str, separator, -1);
    }

    public static List<String> splitTrim(CharSequence str, CharSequence separator) {
        return splitTrim(str, separator, -1);
    }

    public static List<String> splitTrim(CharSequence str, char separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    public static List<String> splitTrim(CharSequence str, CharSequence separator, int limit) {
        return split(str, separator, limit, true, true);
    }

    public static List<String> split(CharSequence str, char separator, boolean isTrim, boolean ignoreEmpty) {
        return split(str, separator, 0, isTrim, ignoreEmpty);
    }

    public static List<String> split(CharSequence str, char separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        return (List)(null == str ? new ArrayList(0) : StrSpliter.split(str.toString(), separator, limit, isTrim, ignoreEmpty));
    }

    public static List<String> split(CharSequence str, CharSequence separator, int limit, boolean isTrim, boolean ignoreEmpty) {
        if (null == str) {
            return new ArrayList(0);
        } else {
            String separatorStr = null == separator ? null : separator.toString();
            return StrSpliter.split(str.toString(), separatorStr, limit, isTrim, ignoreEmpty);
        }
    }

    public static String[] split(CharSequence str, CharSequence separator) {
        if (str == null) {
            return new String[0];
        } else {
            String separatorStr = null == separator ? null : separator.toString();
            return StrSpliter.splitToArray(str.toString(), separatorStr, 0, false, false);
        }
    }

    public static String[] split(CharSequence str, int len) {
        return null == str ? new String[0] : StrSpliter.splitByLength(str.toString(), len);
    }

    public static boolean contains(CharSequence str, char searchChar) {
        return indexOf(str, searchChar) > -1;
    }

    public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
        return null != getContainsStr(str, testStrs);
    }

    public static String getContainsStr(CharSequence str, CharSequence... testStrs) {
        if (!isEmpty(str) && !Func.isEmpty(testStrs)) {
            CharSequence[] var2 = testStrs;
            int var3 = testStrs.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                CharSequence checkStr = var2[var4];
                if (str.toString().contains(checkStr)) {
                    return checkStr.toString();
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static boolean containsIgnoreCase(CharSequence str, CharSequence testStr) {
        if (null == str) {
            return null == testStr;
        } else {
            return str.toString().toLowerCase().contains(testStr.toString().toLowerCase());
        }
    }

    public static boolean containsAnyIgnoreCase(CharSequence str, CharSequence... testStrs) {
        return null != getContainsStrIgnoreCase(str, testStrs);
    }

    public static String getContainsStrIgnoreCase(CharSequence str, CharSequence... testStrs) {
        if (!isEmpty(str) && !Func.isEmpty(testStrs)) {
            CharSequence[] var2 = testStrs;
            int var3 = testStrs.length;

            for(int var4 = 0; var4 < var3; ++var4) {
                CharSequence testStr = var2[var4];
                if (containsIgnoreCase(str, testStr)) {
                    return testStr.toString();
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static String sub(CharSequence str, int fromIndex, int toIndex) {
        if (isEmpty(str)) {
            return "";
        } else {
            int len = str.length();
            if (fromIndex < 0) {
                fromIndex += len;
                if (fromIndex < 0) {
                    fromIndex = 0;
                }
            } else if (fromIndex > len) {
                fromIndex = len;
            }

            if (toIndex < 0) {
                toIndex += len;
                if (toIndex < 0) {
                    toIndex = len;
                }
            } else if (toIndex > len) {
                toIndex = len;
            }

            if (toIndex < fromIndex) {
                int tmp = fromIndex;
                fromIndex = toIndex;
                toIndex = tmp;
            }

            return fromIndex == toIndex ? "" : str.toString().substring(fromIndex, toIndex);
        }
    }

    public static String subBefore(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (!isEmpty(string) && separator != null) {
            String str = string.toString();
            String sep = separator.toString();
            if (sep.isEmpty()) {
                return "";
            } else {
                int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
                return pos == -1 ? str : str.substring(0, pos);
            }
        } else {
            return null == string ? null : string.toString();
        }
    }

    public static String subAfter(CharSequence string, CharSequence separator, boolean isLastSeparator) {
        if (isEmpty(string)) {
            return null == string ? null : string.toString();
        } else if (separator == null) {
            return "";
        } else {
            String str = string.toString();
            String sep = separator.toString();
            int pos = isLastSeparator ? str.lastIndexOf(sep) : str.indexOf(sep);
            return pos == -1 ? "" : str.substring(pos + separator.length());
        }
    }

    public static String subBetween(CharSequence str, CharSequence before, CharSequence after) {
        if (str != null && before != null && after != null) {
            String str2 = str.toString();
            String before2 = before.toString();
            String after2 = after.toString();
            int start = str2.indexOf(before2);
            if (start != -1) {
                int end = str2.indexOf(after2, start + before2.length());
                if (end != -1) {
                    return str2.substring(start + before2.length(), end);
                }
            }

            return null;
        } else {
            return null;
        }
    }

    public static String subBetween(CharSequence str, CharSequence beforeAndAfter) {
        return subBetween(str, beforeAndAfter, beforeAndAfter);
    }

    public static String removePrefix(CharSequence str, CharSequence prefix) {
        if (!isEmpty(str) && !isEmpty(prefix)) {
            String str2 = str.toString();
            return str2.startsWith(prefix.toString()) ? subSuf(str2, prefix.length()) : str2;
        } else {
            return "";
        }
    }

    public static String removePrefixIgnoreCase(CharSequence str, CharSequence prefix) {
        if (!isEmpty(str) && !isEmpty(prefix)) {
            String str2 = str.toString();
            return str2.toLowerCase().startsWith(prefix.toString().toLowerCase()) ? subSuf(str2, prefix.length()) : str2;
        } else {
            return "";
        }
    }

    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        if (!isEmpty(str) && !isEmpty(suffix)) {
            String str2 = str.toString();
            return str2.endsWith(suffix.toString()) ? subPre(str2, str2.length() - suffix.length()) : str2;
        } else {
            return "";
        }
    }

    public static String removeSufAndLowerFirst(CharSequence str, CharSequence suffix) {
        return lowerFirst(removeSuffix(str, suffix));
    }

    public static String removeSuffixIgnoreCase(CharSequence str, CharSequence suffix) {
        if (!isEmpty(str) && !isEmpty(suffix)) {
            String str2 = str.toString();
            return str2.toLowerCase().endsWith(suffix.toString().toLowerCase()) ? subPre(str2, str2.length() - suffix.length()) : str2;
        } else {
            return "";
        }
    }

    public static String lowerFirst(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'A' && firstChar <= 'Z') {
            char[] arr = str.toCharArray();
            arr[0] = (char)(arr[0] + 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    public static String upperFirst(String str) {
        char firstChar = str.charAt(0);
        if (firstChar >= 'a' && firstChar <= 'z') {
            char[] arr = str.toCharArray();
            arr[0] = (char)(arr[0] - 32);
            return new String(arr);
        } else {
            return str;
        }
    }

    public static String subPre(CharSequence string, int toIndex) {
        return sub(string, 0, toIndex);
    }

    public static String subSuf(CharSequence string, int fromIndex) {
        return isEmpty(string) ? null : sub(string, fromIndex, string.length());
    }

    public static int indexOf(final CharSequence str, char searchChar) {
        return indexOf(str, searchChar, 0);
    }

    public static int indexOf(final CharSequence str, char searchChar, int start) {
        return str instanceof String ? ((String)str).indexOf(searchChar, start) : indexOf(str, searchChar, start, -1);
    }

    public static int indexOf(final CharSequence str, char searchChar, int start, int end) {
        int len = str.length();
        if (start < 0 || start > len) {
            start = 0;
        }

        if (end > len || end < 0) {
            end = len;
        }

        for(int i = start; i < end; ++i) {
            if (str.charAt(i) == searchChar) {
                return i;
            }
        }

        return -1;
    }

    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        return indexOfIgnoreCase(str, searchStr, 0);
    }

    public static int indexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int fromIndex) {
        return indexOf(str, searchStr, fromIndex, true);
    }

    public static int indexOf(final CharSequence str, CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str != null && searchStr != null) {
            if (fromIndex < 0) {
                fromIndex = 0;
            }

            int endLimit = str.length() - searchStr.length() + 1;
            if (fromIndex > endLimit) {
                return -1;
            } else if (searchStr.length() == 0) {
                return fromIndex;
            } else if (!ignoreCase) {
                return str.toString().indexOf(searchStr.toString(), fromIndex);
            } else {
                for(int i = fromIndex; i < endLimit; ++i) {
                    if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                        return i;
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr) {
        return lastIndexOfIgnoreCase(str, searchStr, str.length());
    }

    public static int lastIndexOfIgnoreCase(final CharSequence str, final CharSequence searchStr, int fromIndex) {
        return lastIndexOf(str, searchStr, fromIndex, true);
    }

    public static int lastIndexOf(final CharSequence str, final CharSequence searchStr, int fromIndex, boolean ignoreCase) {
        if (str != null && searchStr != null) {
            if (fromIndex < 0) {
                fromIndex = 0;
            }

            fromIndex = Math.min(fromIndex, str.length());
            if (searchStr.length() == 0) {
                return fromIndex;
            } else if (!ignoreCase) {
                return str.toString().lastIndexOf(searchStr.toString(), fromIndex);
            } else {
                for(int i = fromIndex; i > 0; --i) {
                    if (isSubEquals(str, i, searchStr, 0, searchStr.length(), true)) {
                        return i;
                    }
                }

                return -1;
            }
        } else {
            return -1;
        }
    }

    public static int ordinalIndexOf(String str, String searchStr, int ordinal) {
        if (str != null && searchStr != null && ordinal > 0) {
            if (searchStr.length() == 0) {
                return 0;
            } else {
                int found = 0;
                int index = -1;

                do {
                    index = str.indexOf(searchStr, index + 1);
                    if (index < 0) {
                        return index;
                    }

                    ++found;
                } while(found < ordinal);

                return index;
            }
        } else {
            return -1;
        }
    }

    public static boolean isSubEquals(CharSequence str1, int start1, CharSequence str2, int start2, int length, boolean ignoreCase) {
        return null != str1 && null != str2 ? str1.toString().regionMatches(ignoreCase, start1, str2.toString(), start2, length) : false;
    }

    public static boolean equals(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, false);
    }

    public static boolean equalsIgnoreCase(CharSequence str1, CharSequence str2) {
        return equals(str1, str2, true);
    }

    public static boolean equals(CharSequence str1, CharSequence str2, boolean ignoreCase) {
        if (null == str1) {
            return str2 == null;
        } else if (null == str2) {
            return false;
        } else {
            return ignoreCase ? str1.toString().equalsIgnoreCase(str2.toString()) : str1.equals(str2);
        }
    }

    public static StringBuilder builder() {
        return new StringBuilder();
    }

    public static StringBuilder builder(int capacity) {
        return new StringBuilder(capacity);
    }

    public static StringBuilder builder(CharSequence... strs) {
        StringBuilder sb = new StringBuilder();
        CharSequence[] var2 = strs;
        int var3 = strs.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            CharSequence str = var2[var4];
            sb.append(str);
        }

        return sb;
    }

    public static StringBuilder appendBuilder(StringBuilder sb, CharSequence... strs) {
        CharSequence[] var2 = strs;
        int var3 = strs.length;

        for(int var4 = 0; var4 < var3; ++var4) {
            CharSequence str = var2[var4];
            sb.append(str);
        }

        return sb;
    }

    public static StringReader getReader(CharSequence str) {
        return null == str ? null : new StringReader(str.toString());
    }

    public static StringWriter getWriter() {
        return new StringWriter();
    }

    public static int count(CharSequence content, CharSequence strForSearch) {
        if (!Func.hasEmpty(new Object[]{content, strForSearch}) && strForSearch.length() <= content.length()) {
            int count = 0;
            int idx = 0;
            String content2 = content.toString();

            for(String strForSearch2 = strForSearch.toString(); (idx = content2.indexOf(strForSearch2, idx)) > -1; idx += strForSearch.length()) {
                ++count;
            }

            return count;
        } else {
            return 0;
        }
    }

    public static int count(CharSequence content, char charForSearch) {
        int count = 0;
        if (isEmpty(content)) {
            return 0;
        } else {
            int contentLength = content.length();

            for(int i = 0; i < contentLength; ++i) {
                if (charForSearch == content.charAt(i)) {
                    ++count;
                }
            }

            return count;
        }
    }

    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String[] a = para.split("_");
        String[] var3 = a;
        int var4 = a.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String s = var3[var5];
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }

        return result.toString();
    }

    public static String humpToUnderline(String para) {
        para = lowerFirst(para);
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;

        for(int i = 0; i < para.length(); ++i) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                ++temp;
            }
        }

        return sb.toString().toLowerCase();
    }

    public static String lineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String[] a = para.split("-");
        String[] var3 = a;
        int var4 = a.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            String s = var3[var5];
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }

        return result.toString();
    }

    public static String humpToLine(String para) {
        para = lowerFirst(para);
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;

        for(int i = 0; i < para.length(); ++i) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "-");
                ++temp;
            }
        }

        return sb.toString().toLowerCase();
    }
}
