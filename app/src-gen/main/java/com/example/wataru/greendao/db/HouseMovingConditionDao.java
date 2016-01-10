package com.example.wataru.greendao.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.example.wataru.greendao.db.HouseMovingCondition;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table HOUSE_MOVING_CONDITION.
*/
public class HouseMovingConditionDao extends AbstractDao<HouseMovingCondition, Long> {

    public static final String TABLENAME = "HOUSE_MOVING_CONDITION";

    /**
     * Properties of entity HouseMovingCondition.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property HmcName = new Property(1, String.class, "hmcName", false, "HMC_NAME");
        public final static Property Necessary = new Property(2, Boolean.class, "necessary", false, "NECESSARY");
    };


    public HouseMovingConditionDao(DaoConfig config) {
        super(config);
    }
    
    public HouseMovingConditionDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'HOUSE_MOVING_CONDITION' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'HMC_NAME' TEXT," + // 1: hmcName
                "'NECESSARY' INTEGER);"); // 2: necessary
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'HOUSE_MOVING_CONDITION'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, HouseMovingCondition entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String hmcName = entity.getHmcName();
        if (hmcName != null) {
            stmt.bindString(2, hmcName);
        }
 
        Boolean necessary = entity.getNecessary();
        if (necessary != null) {
            stmt.bindLong(3, necessary ? 1l: 0l);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public HouseMovingCondition readEntity(Cursor cursor, int offset) {
        HouseMovingCondition entity = new HouseMovingCondition( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // hmcName
            cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0 // necessary
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, HouseMovingCondition entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setHmcName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setNecessary(cursor.isNull(offset + 2) ? null : cursor.getShort(offset + 2) != 0);
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(HouseMovingCondition entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(HouseMovingCondition entity) {
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
    
}
