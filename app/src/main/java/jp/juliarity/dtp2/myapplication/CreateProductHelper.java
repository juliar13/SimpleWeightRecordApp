package jp.juliarity.dtp2.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by DTP2 on 2017/03/19.
 */
public class CreateProductHelper extends SQLiteOpenHelper {

    static final String TAG="CreateProductHelper";
    static final String TABLE_NAME="dbsample";

    public CreateProductHelper(Context con){
        super(con,TABLE_NAME,null,1);
        Log.d(TAG,"CreateProductHelper");
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.d(TAG,"onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db,int oldversion,int newversion){
        Log.d(TAG,"onUpgrade");
    }
}
