package ace.soar.frame.utils.db;

import org.xutils.DbManager;
import org.xutils.x;

/**
 * Created by gaofei on 2016/5/3.
 */
public class DBUtils {

    private static DbManager dbManager ;

    private static DbManager.DaoConfig daoConfig = new DbManager.DaoConfig()
            .setDbName("test.db")
                    // 不设置dbDir时, 默认存储在app的私有目录.
//            .setDbDir(new File("/sdcard")) // "sdcard"的写法并非最佳实践, 这里为了简单, 先这样写了.
            .setDbVersion(3)
            .setDbOpenListener(new DbManager.DbOpenListener() {
                @Override
                public void onDbOpened(DbManager db) {
                    // 开启WAL, 对写入加速提升巨大
                    db.getDatabase().enableWriteAheadLogging();
                }
            })
            .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                @Override
                public void onUpgrade(DbManager db, int oldVersion, int newVersion) {
                    try {
                        db.dropDb();
                    }catch (Exception e){

                    }
                }
            });


    public static DbManager getDbManager(){
        if(dbManager == null){
            dbManager = x.getDb(daoConfig);
        }
        return dbManager;
    }

}
