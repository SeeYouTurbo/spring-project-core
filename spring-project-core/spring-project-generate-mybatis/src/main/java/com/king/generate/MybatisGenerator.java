package com.king.generate;


import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.google.common.base.Splitter;
import com.king.generate.config.CustomFileOutConfig;
import com.king.generate.config.CustomMySqlTypeConvert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 配置文件从
 *
 * @author jinfeng.wu
 * @date 2020/11/26 9:50
 */
@Slf4j
public class MybatisGenerator {

    private static Pattern DM_PATTERN = Pattern.compile("[,;]");

    private String configPath = "generator-config.properties";

    private String driverName;

    private String username;

    private String password;

    private String url;

    private String projectName;

    private String projectRootPath;

    private List<String> tableNames;

    private Boolean fileOverride = Boolean.FALSE;

    private Boolean ignoreTablePrefix = Boolean.FALSE;

    private Boolean entityLombokModel = Boolean.TRUE;

    private List<String> exclusionEntitySuperColumns;

    private Boolean isExclusionEntitySuperColumn = Boolean.FALSE;

    private Boolean isDeleteFilePrefixModule = Boolean.FALSE;

    private Boolean isUnifyRootPathPrefixModule = Boolean.TRUE;

    private Boolean isCreateService = Boolean.FALSE;

    private Boolean isCreateController = Boolean.FALSE;

    private Boolean isCreateRepository = Boolean.FALSE;

    private Boolean isCreateVO = Boolean.FALSE;

    private String[] tablePrefix;

    private String author;

    private String moduleName;

    private String basePackage;

    private String baseOutputDir;

    private String dtoPackage;

    private String dtoSuperClass;

    private String dtoFileName;

    private String dtoOutputDir;

    private String dtoPackageName;

    private String voPackage;

    private String voFileName;

    private String voOutputDir;

    private String voPackageName;

    private String entityPackage;

    private String entitySuperClass;

    private String entityFileName;

    private String entityOutputDir;

    private String entityPackageName;

    private String servicePackage;

    private String serviceSuperClass;

    private String serviceFileName;

    private String serviceOutputDir;

    private String servicePackageName;

    private String serviceImplPackage;

    private String serviceImplSuperClass;

    private String serviceImplFileName;

    private String serviceImplOutputDir;

    private String serviceImplPackageName;

    private String controllerPackage;

    private String controllerSuperClass;

    private String controllerFileName;

    private String controllerOutputDir;

    private String controllerPackageName;

    private String daoPackage;

    private String daoSuperClass;

    private String daoFileName;

    private String daoOutputDir;

    private String daoPackageName;

    private String mapperPackage;

    private String mapperSuperClass;

    private String mapperFileName;

    private String mapperOutputDir;

    private String mapperPackageName;

    private String xmlPackage;

    private String xmlFileName;

    private String xmlOutputDir;

    private String xmlPackageName;

    private GlobalConfig globalConfig;

    private AutoGenerator autoGenerator = new AutoGenerator();

    private DataSourceConfig dataSourceConfig;

    private InjectionConfig injectionConfig;

    private PackageConfig packageConfig;

    private List<FileOutConfig> fileOutConfigs = new ArrayList<FileOutConfig>();

    public MybatisGenerator() {
        init();
    }

    public MybatisGenerator(String configPath) {
        this.configPath = configPath;
        init();
    }

    public void init() {
        loadConfig();
        loadGlobalConfig();
        loadDataSourceFileConfig();
        if (this.isCreateService) {
            CustomFileOutConfig serviceFileConfig = new CustomFileOutConfig("/resources/mybatis/templates/service.java.vm", this.servicePackage, this.servicePackageName, this.serviceFileName, this.serviceOutputDir, "service", this.isDeleteFilePrefixModule, this.isUnifyRootPathPrefixModule, this.injectionConfig, this.packageConfig);
            serviceFileConfig.setSuperClass(this.serviceSuperClass);
            serviceFileConfig.setIgnoreTablePrefix(this.ignoreTablePrefix);
            this.fileOutConfigs.add(serviceFileConfig);
            CustomFileOutConfig serviceImplFileConfig = new CustomFileOutConfig("/resources/mybatis/templates/serviceImpl.java.vm", this.serviceImplPackage, this.serviceImplPackageName, this.serviceImplFileName, this.serviceImplOutputDir, "serviceImpl", this.isDeleteFilePrefixModule, this.isUnifyRootPathPrefixModule, this.injectionConfig, this.packageConfig);
            serviceImplFileConfig.setSuperClass(this.serviceImplSuperClass);
            serviceImplFileConfig.setIgnoreTablePrefix(this.ignoreTablePrefix);
            this.fileOutConfigs.add(serviceImplFileConfig);
        }
        if (this.isCreateVO) {
            CustomFileOutConfig voFileConfig = new CustomFileOutConfig("/resources/mybatis/templates/vo.java.vm", this.voPackage, this.voPackageName, this.voFileName, this.voOutputDir, "vo", this.isDeleteFilePrefixModule, this.isUnifyRootPathPrefixModule, this.injectionConfig, this.packageConfig);
            voFileConfig.setIgnoreTablePrefix(this.ignoreTablePrefix);
            this.fileOutConfigs.add(voFileConfig);
        }
        if (this.isCreateController) {
            CustomFileOutConfig controllerFileConfig = new CustomFileOutConfig("/resources/mybatis/templates/controller.java.vm", this.controllerPackage, this.controllerPackageName, this.controllerFileName, this.controllerOutputDir, "controller", this.isDeleteFilePrefixModule, this.isUnifyRootPathPrefixModule, this.injectionConfig, this.packageConfig);
            controllerFileConfig.setSuperClass(this.controllerSuperClass);
            controllerFileConfig.setIgnoreTablePrefix(this.ignoreTablePrefix);
            this.fileOutConfigs.add(controllerFileConfig);
        }
        if(this.isCreateRepository){
            CustomFileOutConfig repositoryFileConfig = new CustomFileOutConfig("/resources/mybatis/templates/repository.java.vm", this.daoPackage, this.daoPackageName, this.daoFileName, this.daoOutputDir, "repository", this.isDeleteFilePrefixModule, this.isUnifyRootPathPrefixModule, this.injectionConfig, this.packageConfig);
            repositoryFileConfig.setSuperClass(this.daoSuperClass);
            repositoryFileConfig.setIgnoreTablePrefix(this.ignoreTablePrefix);
            this.fileOutConfigs.add(repositoryFileConfig);
            CustomFileOutConfig repositoryImpFileConfig = new CustomFileOutConfig("/resources/mybatis/templates/repositoryimp.java.vm", this.daoPackage, this.daoPackageName, this.daoFileName, this.daoOutputDir, "repositoryimp", this.isDeleteFilePrefixModule, this.isUnifyRootPathPrefixModule, this.injectionConfig, this.packageConfig);
            repositoryImpFileConfig.setSuperClass(this.daoSuperClass);
            repositoryImpFileConfig.setIgnoreTablePrefix(this.ignoreTablePrefix);
            this.fileOutConfigs.add(repositoryImpFileConfig);
        }
        this.injectionConfig.setFileOutConfigList(this.fileOutConfigs);
        this.autoGenerator.setCfg(this.injectionConfig);
        TemplateConfig tc = new TemplateConfig();
        tc.setXml(null);
        tc.setController(null);
        tc.setService(null);
        tc.setServiceImpl(null);
        tc.setEntity(null);
        tc.setMapper(null);
        this.autoGenerator.setTemplate(tc);
    }

    public void loadGlobalConfig() {
        this.globalConfig = new GlobalConfig();
        this.globalConfig.setOutputDir(this.projectRootPath);
        this.globalConfig.setFileOverride(this.fileOverride);
        this.globalConfig.setActiveRecord(true);
        this.globalConfig.setEnableCache(false);
        this.globalConfig.setBaseResultMap(true);
        this.globalConfig.setBaseColumnList(true);
        this.globalConfig.setAuthor(this.author);
        this.globalConfig.setOpen(false);
        this.autoGenerator.setGlobalConfig(this.globalConfig);
    }

    public void loadDataSourceFileConfig() {
        this.dataSourceConfig = new DataSourceConfig();
        if (this.driverName.toLowerCase().contains(DbType.MYSQL.getDb())) {
            this.dataSourceConfig.setDbType(DbType.MYSQL);
        } else {
            this.dataSourceConfig.setDbType(DbType.ORACLE);
        }
        this.dataSourceConfig.setTypeConvert((ITypeConvert)new CustomMySqlTypeConvert());
        this.dataSourceConfig.setDriverName(this.driverName);
        this.dataSourceConfig.setUsername(this.username);
        this.dataSourceConfig.setPassword(this.password);
        this.dataSourceConfig.setUrl(this.url);
        this.autoGenerator.setDataSource(this.dataSourceConfig);
        // 单表策略
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setInclude(this.tableNames.<String>toArray(new String[this.tableNames.size()]));
        strategy.setTablePrefix(this.tablePrefix);
        strategy.setEntityLombokModel(this.entityLombokModel);
        strategy.setSuperEntityClass(this.entitySuperClass);

        this.autoGenerator.setStrategy(strategy);
        this.packageConfig = new PackageConfig();
        this.packageConfig.setParent(this.basePackage);
        this.packageConfig.setModuleName(this.moduleName);
//        this.packageConfig.setXml();
        this.autoGenerator.setPackageInfo(this.packageConfig);
        final Map<String, Object> map = new HashMap(3);
        map.put("isExclusionEntitySuperColumn", this.isExclusionEntitySuperColumn);
        map.put("exclusionEntitySuperColumns", this.exclusionEntitySuperColumns);
        map.put("moduleName", this.moduleName);
        this.injectionConfig = new InjectionConfig() {
            @Override
            public void initMap() {
                setMap(map);
            }
        };

        // 自定义
        CustomFileOutConfig entityFileConfig = new CustomFileOutConfig("/resources/mybatis/templates/entity.java.vm", this.entityPackage, this.entityPackageName, this.entityFileName, this.entityOutputDir, "entity", this.isDeleteFilePrefixModule, this.isUnifyRootPathPrefixModule, this.injectionConfig, this.packageConfig);
        entityFileConfig.setSuperClass(this.entitySuperClass);
        entityFileConfig.setIgnoreTablePrefix(this.ignoreTablePrefix);
        this.fileOutConfigs.add(entityFileConfig);
        CustomFileOutConfig mapperFileConfig = new CustomFileOutConfig("/resources/mybatis/templates/mapper.java.vm", this.mapperPackage, this.mapperPackageName, this.mapperFileName, this.mapperOutputDir, "mapper", this.isDeleteFilePrefixModule, this.isUnifyRootPathPrefixModule, this.injectionConfig, this.packageConfig);
        mapperFileConfig.setSuperClass(this.mapperSuperClass);
        mapperFileConfig.setIgnoreTablePrefix(this.ignoreTablePrefix);
        this.fileOutConfigs.add(mapperFileConfig);
        CustomFileOutConfig xmlFileConfig = new CustomFileOutConfig("/resources/mybatis/templates/mapper.xml.vm", this.xmlPackage, this.xmlPackageName, this.xmlFileName, this.xmlOutputDir, "mapper", this.isDeleteFilePrefixModule, this.isUnifyRootPathPrefixModule, this.injectionConfig, this.packageConfig);
        xmlFileConfig.setIgnoreTablePrefix(this.ignoreTablePrefix);
        this.fileOutConfigs.add(xmlFileConfig);
    }

    public void loadConfig() {
        String configPath = "generator-config.properties";
        ClassPathResource classPathResource = new ClassPathResource(configPath);
        if (classPathResource.exists()) {
            Properties properties = new Properties();
            try {
                properties.load(classPathResource.getInputStream());
                setPropertyValue(properties);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    classPathResource.getInputStream().close();
                } catch (IOException e2) {
                    log.error(e2.getMessage());
                }
            }
            Assert.notNull(this.driverName, "driverName");
            Assert.notNull(this.url, "url");
            Assert.notNull(this.username, "username");
            Assert.notNull(this.password, "password");
            Assert.notNull(this.projectRootPath, "projectRootPath");
            Assert.notEmpty(this.tableNames, "tableNames");
            Assert.notNull(this.dtoOutputDir, "dtoOutputDir");
            Assert.notNull(this.dtoPackage, "dtoPackage");
            Assert.notNull(this.entityOutputDir, "entityOutputDir");
            Assert.notNull(this.entityPackage, "entityPackage");
        } else {
            throw new RuntimeException(configPath + "");
        }
    }

    public void setPropertyValue(Properties properties) throws IOException {
        this.driverName = StringUtils.trimToNull(properties.getProperty("driverName"));
        this.url = StringUtils.trimToNull(properties.getProperty("url"));
        this.username = StringUtils.trimToNull(properties.getProperty("username"));
        this.password = StringUtils.trimToNull(properties.getProperty("password"));
        this
                .tableNames = Splitter.on(DM_PATTERN).omitEmptyStrings().trimResults().splitToList(StringUtils.trimToEmpty(properties.getProperty("tableNames")));
        this
                .exclusionEntitySuperColumns = Splitter.on(DM_PATTERN).omitEmptyStrings().trimResults().splitToList(StringUtils.trimToEmpty(properties.getProperty("exclusionEntitySuperColumns")));
        this.projectName = StringUtils.trimToNull(properties.getProperty("projectName"));
        this.projectRootPath = StringUtils.trimToNull(properties.getProperty("projectRootPath"));
        if (this.projectName != null)
            this.projectRootPath = getProjectRootPathByProjectName(this.projectName);
        this.fileOverride = Boolean.valueOf((String) StringUtils.defaultIfEmpty(properties.getProperty("fileOverride"), "false"));
        this.ignoreTablePrefix = Boolean.valueOf((String) StringUtils.defaultIfEmpty(properties.getProperty("ignoreTablePrefix"), "false"));
        this.entityLombokModel = Boolean.valueOf((String) StringUtils.defaultIfEmpty(properties.getProperty("entityLombokModel"), "true"));
        this.isCreateService = Boolean.valueOf((String) StringUtils.defaultIfEmpty(properties.getProperty("isCreateService"), "false"));
        this.isCreateVO = Boolean.valueOf((String) StringUtils.defaultIfEmpty(properties.getProperty("isCreateVO"), "false"));
        this.isCreateController = Boolean.valueOf((String) StringUtils.defaultIfEmpty(properties.getProperty("isCreateController"), "false"));
        this.isDeleteFilePrefixModule = Boolean.valueOf((String) StringUtils.defaultIfEmpty(properties.getProperty("isDeleteFilePrefixModule"), "false"));
        this.isUnifyRootPathPrefixModule = Boolean.valueOf((String) StringUtils.defaultIfEmpty(properties.getProperty("isUnifyRootPathPrefixModule"), "true"));
        this.author = StringUtils.trimToNull(properties.getProperty("author"));
        this.moduleName = StringUtils.trimToNull(properties.getProperty("moduleName"));
        this.basePackage = StringUtils.trimToNull(properties.getProperty("basePackage"));
        this.baseOutputDir = StringUtils.trimToNull(properties.getProperty("baseOutputDir"));
        this.tablePrefix = properties.getProperty("tablePrefix").split(",");

        this.dtoPackage = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("dtoPackage")), this.basePackage);
        this.dtoSuperClass = StringUtils.trimToNull(properties.getProperty("dtoSuperClass"));
        this.dtoFileName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("dtoFileName")), "%sDTO.java");
        this.dtoOutputDir = this.projectRootPath + (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("dtoOutputDir")), this.baseOutputDir);
        this.dtoPackageName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("dtoPackageName")), "dto");

        this.voPackage = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("voPackage")), this.basePackage);
        this.voFileName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("voFileName")), "%sVO.java");
        this.voOutputDir = this.projectRootPath + (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("voOutputDir")), this.baseOutputDir);
        this.voPackageName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("voPackageName")), "vo");

        this.entityPackage = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("entityPackage")), this.basePackage);
        this.entitySuperClass = StringUtils.trimToNull(properties.getProperty("entitySuperClass"));
        this.entityFileName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("entityFileName")), "%s.java");
        this.entityOutputDir = this.projectRootPath + (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("entityOutputDir")), this.baseOutputDir);
        this.entityPackageName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("entityPackageName")), "entity");

        this.servicePackage = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("servicePackage")), this.basePackage);
        this.serviceSuperClass = StringUtils.trimToNull(properties.getProperty("serviceSuperClass"));
        this.serviceFileName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("serviceFileName")), "I%sService.java");
        this.serviceOutputDir = this.projectRootPath + (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("serviceOutputDir")), this.baseOutputDir);
        this.servicePackageName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("servicePackageName")), "service");

        this.serviceImplPackage = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("serviceImplPackage")), this.basePackage);
        this.serviceImplSuperClass = StringUtils.trimToNull(properties.getProperty("serviceImplSuperClass"));
        this.serviceImplFileName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("serviceImplFileName")), "%sServiceImpl.java");
        this.serviceImplOutputDir = this.projectRootPath + (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("serviceImplOutputDir")), this.baseOutputDir);
        this.serviceImplPackageName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("serviceImplPackageName")), "service.impl");

        this.controllerPackage = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("controllerPackage")), this.basePackage);
        this.controllerSuperClass = StringUtils.trimToNull(properties.getProperty("controllerSuperClass"));
        this.controllerFileName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("controllerFileName")), "%sController.java");
        this.controllerOutputDir = this.projectRootPath + (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("controllerOutputDir")), this.baseOutputDir);
        this.controllerPackageName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("controllerPackageName")), "controller");

        this.daoPackage = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("daoPackage")), this.basePackage);
        this.daoSuperClass = (String) StringUtils.defaultIfBlank(StringUtils.trimToNull(properties.getProperty("daoSuperClass")), "com.baomidou.mybatisplus.core.mapper.BaseMapper");
        this.daoFileName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("daoFileName")), "%sMapper.java");
        this.daoOutputDir = this.projectRootPath + (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("daoOutputDir")), this.baseOutputDir);
        this.daoPackageName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("daoPackageName")), "mapper");

        this.mapperPackage = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("mapperPackage")), this.basePackage);
        this.mapperSuperClass = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("mapperPackage")), "com.baomidou.mybatisplus.core.mapper.BaseMapper");
        this.mapperFileName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("mapperFileName")), "%sMapper.java");
        this.mapperOutputDir = this.projectRootPath + (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("mapperOutputDir")), this.baseOutputDir);
        this.mapperPackageName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("mapperPackageName")), "mapper");

        this.xmlPackage = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("xmlPackage")), "");
        this.xmlFileName = (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("xmlFileName")), "%sMapper.xml");
        this.xmlOutputDir = this.projectRootPath + (String) StringUtils.defaultIfEmpty(StringUtils.trimToNull(properties.getProperty("xmlOutputDir")), "");
        this.xmlPackageName = (String) StringUtils.defaultIfEmpty(
                StringUtils.trimToNull(properties.getProperty("xmlPackageName")), "mapper");
        if (StringUtils.isNotEmpty(this.entitySuperClass) && !CollectionUtils.isEmpty(this.exclusionEntitySuperColumns))
            this.isExclusionEntitySuperColumn = Boolean.TRUE;
    }

    public void execute() {
        this.autoGenerator.execute();
    }

    public String getProjectRootPathByProjectName(String projectName) {
        String path = MybatisGenerator.class.getResource("/").getPath();
        int index = path.indexOf(projectName);
        return path.substring(1, index) + projectName + "/";
    }
}
