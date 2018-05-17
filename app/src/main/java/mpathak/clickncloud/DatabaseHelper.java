package mpathak.clickncloud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by mpathak on 4/3/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper
{
    //private SQLiteDatabase thisDb;
    public static final String DATABASE_NAME = "clickncloud.db";
    public static final String TABLE_NAME = "imagedata_table";

    public static final String Id = "id";
    private static final int id_column_index = 0;

    public static final String NAME = "imagename";
    private static final int imagename_column_index = 1;

    public static final String ABSOLUTE_PATH = "absolutepath";
    private static final int absolutepath_column_index = 2;

    public static final String IS_UPLOADED = "isuploaded"; //boolean
    private static final int isuploaded_column_index = 3;

    public static final String QUEUED_TO__DELETE = "queuedtodelete"; //boolean
    private static final int queuedtodelete_column_index = 4;

    public static final String CLOUDD_PROVIDER = "cloudprovider";
    private static final int cloudprovider_column_index = 5;

    public static final String ORIGINAL_SIZE = "originalsize";  //float
    private static final int originalsize_column_index = 6;

    //public static final String REDUCED_SIZE = "reducedsize";   //float
    //private static final int reducedsize_column_index = 7;

    public DatabaseHelper(Context context)//, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
    {/*super(context, name, factory, version, errorHandler);*/
        super(context, DATABASE_NAME, null, 1, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("+ Id +" INTEGER PRIMARY KEY AUTOINCREMENT, "+ NAME +" TEXT NOT NULL, " +
                ABSOLUTE_PATH + " TEXT NOT NULL, "+ IS_UPLOADED +" INTEGER DEFAULT 0, "+ QUEUED_TO__DELETE +" INTEGER DEFAULT 0, " +
                CLOUDD_PROVIDER +" TEXT NOT NULL, "+ ORIGINAL_SIZE +" REAL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertImageEntities(ArrayList<ImageEntity> imageEntities)
    {
        for(int i =0; i< imageEntities.size(); i++)
        {
            insertImageData(imageEntities.get(i));
        }
    }

    public boolean insertImageData(ImageEntity imageEntity) {
        SQLiteDatabase thisDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, imageEntity.Name);
        contentValues.put(ABSOLUTE_PATH, imageEntity.AbsolutePath);
        contentValues.put(IS_UPLOADED, imageEntity.IsUploaded);
        contentValues.put(QUEUED_TO__DELETE, imageEntity.QueuedToDelete);
        contentValues.put(CLOUDD_PROVIDER, imageEntity.CloudProvider);
        contentValues.put(ORIGINAL_SIZE, imageEntity.OriginalSize);

        //ToDo: This return row Id from here
        long result = thisDb.insert(TABLE_NAME, null, contentValues);

        return !(result == -1);
    }

    public Cursor retriveAll()
    {
        SQLiteDatabase thisDb = this.getWritableDatabase();
        String query = "SELECT  * FROM " + TABLE_NAME;
        Cursor res = thisDb.rawQuery(query, null);

        ArrayList<ImageEntity> imageEntities = new ArrayList<ImageEntity>();

        while(res.moveToNext())
        {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.Id = res.getString(id_column_index);//This can be used to retrieve int, double, float, boolean
            imageEntity.Name = res.getString(imagename_column_index);
            imageEntity.AbsolutePath = res.getString(absolutepath_column_index);
            imageEntity.IsUploaded = res.getInt(isuploaded_column_index);
            imageEntity.CloudProvider = res.getString(cloudprovider_column_index);
            imageEntity.QueuedToDelete = res.getInt(queuedtodelete_column_index);
            imageEntity.OriginalSize = res.getFloat(originalsize_column_index);

            imageEntities.add(imageEntity);
        }
        return res;
    }

    public boolean updateEntry(ImageEntity imageEntity)
    {
        SQLiteDatabase thisDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME, imageEntity.Name);
        contentValues.put(ABSOLUTE_PATH, imageEntity.AbsolutePath);

        thisDb.update(TABLE_NAME, contentValues, "id=?", new String[]{"6"});

        thisDb.delete(TABLE_NAME, "id=?", new String[]{imageEntity.Id});
        return true;
    }

    public boolean deleteEntryByname(String imageName)
    {
        SQLiteDatabase thisDb = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        thisDb.delete(TABLE_NAME, "imagename=?", new String[]{imageName});
        return true;
    }

    public void deleteAllEntries()
    {
        SQLiteDatabase thisDb = this.getWritableDatabase();

        thisDb.execSQL("delete from "+ TABLE_NAME);
    }
}
