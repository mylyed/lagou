package github.mylyed.lagou.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import github.mylyed.lagou.model.Job;
import io.ebean.EbeanServer;
import io.ebean.EbeanServerFactory;
import io.ebean.config.ServerConfig;
import io.ebean.config.dbplatform.DatabasePlatform;
import io.ebean.config.dbplatform.mysql.MySqlPlatform;

import java.util.Arrays;

/**
 * Created by lilei on 2017/6/1.
 */
public class DB {

    private static boolean isInit = false;

    /**
     * 数据配置初始化
     */
    public static void init() {
        if (isInit) {
            return;
        }
        ServerConfig config = new ServerConfig();
        config.setName("lagou");
        config.setDefaultServer(true);
        config.setRegister(true);
        //-----
        HikariConfig dataConfig = new HikariConfig();
        dataConfig.setJdbcUrl("jdbc:h2:tcp://localhost/~/lagout;MODE=MYSQL");
        dataConfig.setUsername("sa");
        dataConfig.setPassword("");

        dataConfig.setMaximumPoolSize(2);

        dataConfig.addDataSourceProperty("cachePrepStmts", "true");
        dataConfig.addDataSourceProperty("prepStmtCacheSize", "250");
        dataConfig.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource ds = new HikariDataSource(dataConfig);
        //--------
        config.setDataSource(ds);
        config.setPackages(Arrays.asList(Job.class.getPackage().getName()));

        //第一次使用时 设置为true 自动创建表
        config.setDdlCreateOnly(false);
        config.setDdlGenerate(true);
        config.setDdlRun(false);
        config.setDdlInitSql("");
        config.setDdlSeedSql("");
        //为了屏蔽删表 重建表
        config.setDatabasePlatform(new MySqlPlatform());

        EbeanServerFactory.create(config);
        isInit = true;
    }

}
