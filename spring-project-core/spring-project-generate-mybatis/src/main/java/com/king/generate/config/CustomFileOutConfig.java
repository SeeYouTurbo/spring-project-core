package com.king.generate.config;

import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.FileOutConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.File;

/**
 * 自定义类生成Config
 *
 * @author jinfeng.wu
 * @date 2020/11/23 10:53
 */
@Data
public class CustomFileOutConfig extends FileOutConfig {
    private String basePackage;

    private String outputDir;

    private String fileName;

    private String packageName;

    private boolean ignoreTablePrefix;

    private InjectionConfig cfg;

    private PackageConfig pc;

    private String superClass;

    private String name;

    private String outputFile;

    private String prefixName;

    private boolean isDeleteFilePrefixModule;

    private boolean isUnifyRootPathPrefixModule;

    public CustomFileOutConfig(String templatePath, String basePackage, String packageName, String fileName, String outputDir, String prefixName, boolean isDeleteFilePrefixModule, boolean isUnifyRootPathPrefixModule, InjectionConfig cfg, PackageConfig pc) {
        super(templatePath);
        this.basePackage = basePackage;
        this.outputDir = outputDir;
        this.fileName = fileName;
        this.packageName = packageName;
        this.cfg = cfg;
        this.pc = pc;
        this.prefixName = prefixName;
        this.isDeleteFilePrefixModule = isDeleteFilePrefixModule;
        this.isUnifyRootPathPrefixModule = isUnifyRootPathPrefixModule;
    }

    public void init(TableInfo tableInfo) {
        Assert.notNull(this.basePackage, "basePackage");
        Assert.notNull(this.packageName, "packageName");
        Assert.notNull(this.fileName, "fileName");
        Assert.notNull(this.cfg, "fileName");
        Assert.notNull(this.pc, "pc");
        Assert.notNull(this.outputDir, "outputDir");
        Assert.notNull(this.prefixName, "prefixName");
        String entityName = tableInfo.getEntityName();
        String tableName = tableInfo.getName();
        String tablePrefix = null;
        int prefixIndex = tableName.indexOf("_");
        if (prefixIndex > -1)
            tablePrefix = tableName.substring(0, prefixIndex).toLowerCase();
        if (this.ignoreTablePrefix && tablePrefix != null)
            entityName = tableInfo.getEntityName().replaceFirst("(?i)" + tablePrefix, "");
        if (this.isDeleteFilePrefixModule);
        this.name = String.format(this.fileName, new Object[] { entityName });
        String moduleName = (String) StringUtils.defaultIfEmpty(this.pc.getModuleName(), tablePrefix);
        Boolean flag = Boolean.valueOf((this.isDeleteFilePrefixModule && StringUtils.isNotEmpty(moduleName) && (this.name
                .startsWith(StringUtils.capitalize(moduleName)) || this.name
                .startsWith("I" + StringUtils.capitalize(moduleName)))));
        if (flag.booleanValue())
            this.name = this.name.replaceFirst(StringUtils.capitalize(moduleName), "");
        String packagePath = this.basePackage + (StringUtils.isNotEmpty(moduleName) ? ("." + moduleName) : "") + (StringUtils.isNoneEmpty(new CharSequence[] { this.packageName }) ? ("." + this.packageName) : "");
        if(this.isUnifyRootPathPrefixModule){
            packagePath = this.basePackage + (StringUtils.isNoneEmpty(new CharSequence[] { this.packageName }) ? ("." + this.packageName) : "") + (StringUtils.isNotEmpty(moduleName) ? ("." + moduleName) : "");
        }
        this.cfg.getMap().put(this.prefixName + "Package", packagePath);
        this.cfg.getMap().put(this.prefixName + "Name", this.name.substring(0, (this.name.indexOf(".") > -1) ? this.name.indexOf(".") : this.name.length()));
        if (StringUtils.isNotEmpty(this.superClass)) {
            this.cfg.getMap().put(this.prefixName + "SuperClass", this.superClass);
            this.cfg.getMap().put(this.prefixName + "SuperClassName", StringUtils.substring(this.superClass, this.superClass.lastIndexOf(".") + 1));
        }
        this.outputFile = joinPath(this.outputDir, packagePath) + File.separator + this.name;
    }

    @Override
    public String outputFile(TableInfo tableInfo) {
        init(tableInfo);
        return this.outputFile;
    }

    private String joinPath(String parentDir, String packageName) {
        if (StringUtils.isEmpty(parentDir))
            parentDir = System.getProperty("java.io.tmpdir");
        if (!StringUtils.endsWith(parentDir, File.separator))
            parentDir = parentDir + File.separator;
        packageName = packageName.replaceAll("\\.", "\\" + File.separator);
        return parentDir + packageName;
    }
}
