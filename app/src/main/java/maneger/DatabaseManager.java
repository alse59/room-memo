package maneger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.wataru.greendao.db.DaoMaster;
import com.example.wataru.greendao.db.DaoSession;
import com.example.wataru.greendao.db.Object;
import com.example.wataru.greendao.db.ObjectDao;
import com.example.wataru.greendao.db.ObjectImage;
import com.example.wataru.greendao.db.ObjectImageDao;

import java.util.List;

/**
 * Created by wataru on 2015/11/15.
 */
public class DatabaseManager {
    private static final String TAG = DatabaseManager.class.getCanonicalName();
    private static final String DBNAME = "sample-database";
    private SQLiteDatabase database;
    private DaoMaster daoMaster;
    private DaoSession daoSession;
    private static DatabaseManager instance;
    private Context context;
    private DaoMaster.DevOpenHelper mHelper;

    public DatabaseManager(Context context) {
        this.context = context;
        mHelper = new DaoMaster.DevOpenHelper(context, DBNAME, null);
    }

    public static DatabaseManager getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseManager(context);
        }
        return instance;
    }

    public void openWritableDb() {
        database = mHelper.getWritableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public void openReadableDb() {
        database = mHelper.getReadableDatabase();
        daoMaster = new DaoMaster(database);
        daoSession = daoMaster.newSession();
    }

    public void closeDbConnections() {
        if (daoSession != null) {
            daoSession.clear();
            daoSession = null;
        }
        if (database != null && database.isOpen()) {
            database.close();
        }
        if (mHelper != null) {
            mHelper.close();
            mHelper = null;
        }
        if (instance != null) {
            instance = null;
        }
    }

    public long insertObject(Object object) {
        long id = 0;
        try {
            if (object != null) {
                openWritableDb();
                ObjectDao dao = daoSession.getObjectDao();
                id = dao.insert(object);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }
    public long insertObjectImage(ObjectImage objectImage) {
        long id = 0;
        try {
            if (objectImage != null) {
                openWritableDb();
                ObjectImageDao dao = daoSession.getObjectImageDao();
                id = dao.insert(objectImage);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return id;
    }

    public void insertObjectImageList(List<ObjectImage> objectImages) {
        try {
            if (objectImages != null) {
                openWritableDb();
                ObjectImageDao dao = daoSession.getObjectImageDao();
                dao.insertInTx(objectImages);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateObject(Object object) {
        try {
            if (object != null) {
                openWritableDb();
                ObjectDao dao = daoSession.getObjectDao();
                dao.update(object);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateObjectImage(ObjectImage objectImage) {
        try {
            if (objectImage != null) {
                openWritableDb();
                ObjectImageDao dao = daoSession.getObjectImageDao();
                dao.update(objectImage);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateObjectImageList(List<ObjectImage> objectImages) {
        try {
            if (objectImages != null) {
                openWritableDb();
                ObjectImageDao dao = daoSession.getObjectImageDao();
                dao.updateInTx(objectImages);
                daoSession.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public List<Object> listObjects() {
        List<Object> objects = null;
        try {
            openReadableDb();
            ObjectDao objectDao = daoSession.getObjectDao();
            objects = objectDao.loadAll();

            daoSession.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objects;
    }

    public Object findObjectById(long id) {
        Object object = null;
        try {
            openReadableDb();
            ObjectDao objectDao = daoSession.getObjectDao();
            object = objectDao.load(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public List<ObjectImage> findObjectImagesByObjectId(long objectId) {
        List<ObjectImage> objectImages = null;
        try {
            openReadableDb();
            ObjectImageDao objectImageDao = daoSession.getObjectImageDao();
            objectImages = objectImageDao._queryObject_ObjectImageList(objectId);
//            objectImages = objectImageDao.loadAll();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return objectImages;
    }

    public void deleteObjectByKey(long key) {
        try {
            openWritableDb();
            ObjectDao objectDao = daoSession.getObjectDao();
            objectDao.deleteByKey(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteObjectImageByKey(List<Long> keys) {
        try {
            openWritableDb();
            ObjectImageDao objectImageDao = daoSession.getObjectImageDao();
            objectImageDao.deleteByKeyInTx(keys);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
