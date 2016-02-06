package com.example.wataru.greendao.db;

import java.util.List;
import java.util.ArrayList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.SqlUtils;
import de.greenrobot.dao.internal.DaoConfig;
import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;

import com.example.wataru.greendao.db.PreviewConfirmDetail;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table PREVIEW_CONFIRM_DETAIL.
*/
public class PreviewConfirmDetailDao extends AbstractDao<PreviewConfirmDetail, Long> {

    public static final String TABLENAME = "PREVIEW_CONFIRM_DETAIL";

    /**
     * Properties of entity PreviewConfirmDetail.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property PcDetailName = new Property(1, String.class, "pcDetailName", false, "PC_DETAIL_NAME");
        public final static Property PcId = new Property(2, Long.class, "pcId", false, "PC_ID");
    };

    private DaoSession daoSession;

    private Query<PreviewConfirmDetail> previewConfirm_PreviewConfirmDetailListQuery;

    public PreviewConfirmDetailDao(DaoConfig config) {
        super(config);
    }
    
    public PreviewConfirmDetailDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
        this.daoSession = daoSession;
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'PREVIEW_CONFIRM_DETAIL' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'PC_DETAIL_NAME' TEXT," + // 1: pcDetailName
                "'PC_ID' INTEGER);"); // 2: pcId
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'PREVIEW_CONFIRM_DETAIL'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, PreviewConfirmDetail entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String pcDetailName = entity.getPcDetailName();
        if (pcDetailName != null) {
            stmt.bindString(2, pcDetailName);
        }
 
        Long pcId = entity.getPcId();
        if (pcId != null) {
            stmt.bindLong(3, pcId);
        }
    }

    @Override
    protected void attachEntity(PreviewConfirmDetail entity) {
        super.attachEntity(entity);
        entity.__setDaoSession(daoSession);
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public PreviewConfirmDetail readEntity(Cursor cursor, int offset) {
        PreviewConfirmDetail entity = new PreviewConfirmDetail( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // pcDetailName
            cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2) // pcId
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, PreviewConfirmDetail entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setPcDetailName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setPcId(cursor.isNull(offset + 2) ? null : cursor.getLong(offset + 2));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(PreviewConfirmDetail entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(PreviewConfirmDetail entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
    /** Internal query to resolve the "previewConfirmDetailList" to-many relationship of PreviewConfirm. */
    public List<PreviewConfirmDetail> _queryPreviewConfirm_PreviewConfirmDetailList(Long pcId) {
        synchronized (this) {
            if (previewConfirm_PreviewConfirmDetailListQuery == null) {
                QueryBuilder<PreviewConfirmDetail> queryBuilder = queryBuilder();
                queryBuilder.where(Properties.PcId.eq(null));
                previewConfirm_PreviewConfirmDetailListQuery = queryBuilder.build();
            }
        }
        Query<PreviewConfirmDetail> query = previewConfirm_PreviewConfirmDetailListQuery.forCurrentThread();
        query.setParameter(0, pcId);
        return query.list();
    }

    private String selectDeep;

    protected String getSelectDeep() {
        if (selectDeep == null) {
            StringBuilder builder = new StringBuilder("SELECT ");
            SqlUtils.appendColumns(builder, "T", getAllColumns());
            builder.append(',');
            SqlUtils.appendColumns(builder, "T0", daoSession.getPreviewConfirmDao().getAllColumns());
            builder.append(" FROM PREVIEW_CONFIRM_DETAIL T");
            builder.append(" LEFT JOIN PREVIEW_CONFIRM T0 ON T.'PC_ID'=T0.'_id'");
            builder.append(' ');
            selectDeep = builder.toString();
        }
        return selectDeep;
    }
    
    protected PreviewConfirmDetail loadCurrentDeep(Cursor cursor, boolean lock) {
        PreviewConfirmDetail entity = loadCurrent(cursor, 0, lock);
        int offset = getAllColumns().length;

        PreviewConfirm previewConfirm = loadCurrentOther(daoSession.getPreviewConfirmDao(), cursor, offset);
        entity.setPreviewConfirm(previewConfirm);

        return entity;    
    }

    public PreviewConfirmDetail loadDeep(Long key) {
        assertSinglePk();
        if (key == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder(getSelectDeep());
        builder.append("WHERE ");
        SqlUtils.appendColumnsEqValue(builder, "T", getPkColumns());
        String sql = builder.toString();
        
        String[] keyArray = new String[] { key.toString() };
        Cursor cursor = db.rawQuery(sql, keyArray);
        
        try {
            boolean available = cursor.moveToFirst();
            if (!available) {
                return null;
            } else if (!cursor.isLast()) {
                throw new IllegalStateException("Expected unique result, but count was " + cursor.getCount());
            }
            return loadCurrentDeep(cursor, true);
        } finally {
            cursor.close();
        }
    }
    
    /** Reads all available rows from the given cursor and returns a list of new ImageTO objects. */
    public List<PreviewConfirmDetail> loadAllDeepFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<PreviewConfirmDetail> list = new ArrayList<PreviewConfirmDetail>(count);
        
        if (cursor.moveToFirst()) {
            if (identityScope != null) {
                identityScope.lock();
                identityScope.reserveRoom(count);
            }
            try {
                do {
                    list.add(loadCurrentDeep(cursor, false));
                } while (cursor.moveToNext());
            } finally {
                if (identityScope != null) {
                    identityScope.unlock();
                }
            }
        }
        return list;
    }
    
    protected List<PreviewConfirmDetail> loadDeepAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllDeepFromCursor(cursor);
        } finally {
            cursor.close();
        }
    }
    

    /** A raw-style query where you can pass any WHERE clause and arguments. */
    public List<PreviewConfirmDetail> queryDeep(String where, String... selectionArg) {
        Cursor cursor = db.rawQuery(getSelectDeep() + where, selectionArg);
        return loadDeepAllAndCloseCursor(cursor);
    }
 
}
