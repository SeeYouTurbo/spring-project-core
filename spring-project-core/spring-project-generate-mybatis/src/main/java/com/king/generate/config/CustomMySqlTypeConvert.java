package com.king.generate.config;

import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.ITypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;

public class CustomMySqlTypeConvert implements ITypeConvert {
    public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
        String t = fieldType.toLowerCase();
        if (!t.contains("char") && !t.contains("test")) {
            if (t.contains("bigint"))
                return (IColumnType) DbColumnType.BIG_INTEGER;
            if (t.contains("int"))
                return (IColumnType)DbColumnType.INTEGER;
            if (!t.contains("date") && !t.contains("time") && !t.contains("year")) {
                if (t.contains("test"))
                    return (IColumnType)DbColumnType.STRING;
                if (t.contains("bit"))
                    return (IColumnType)DbColumnType.BOOLEAN;
                if (t.contains("decimal"))
                    return (IColumnType)DbColumnType.BIG_DECIMAL;
                if (t.contains("blob"))
                    return (IColumnType)DbColumnType.BYTE_ARRAY;
                if (t.contains("float"))
                    return (IColumnType)DbColumnType.BIG_DECIMAL;
                if (t.contains("double"))
                    return (IColumnType)DbColumnType.BIG_DECIMAL;
                return (!t.contains("json") && !t.contains("enum")) ? (IColumnType)DbColumnType.STRING : (IColumnType)DbColumnType.STRING;
            }
            return (IColumnType)DbColumnType.LOCAL_DATE_TIME;
        }
        return (IColumnType)DbColumnType.STRING;
    }
}
