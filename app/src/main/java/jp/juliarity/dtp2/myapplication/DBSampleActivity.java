package jp.juliarity.dtp2.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

//

/**
 * Created by DTP2 on 2017/03/19.
 */
public class DBSampleActivity extends Activity {

    static final String TAG="tag";
    static final String ERROR_TAG ="error";

    static final int SP_MAX_SCREEN_WIDTH=600;

    static final String[] BUTTON_TAG={"insert","update","delete","select"};

//    static final String[] DB_DATA={"_id","productid","name","price"};
    static final String TABLE_NAME="dbsample";

    static final String[] DB_DATA={"_id","weight","date","cloth","timing"};
    static final String[] DB_DATA_OPTION={" integer primary key autoincrement,",
                                                " real,"
            ," timestamp default (datetime('now','localtime')),"
            ," text,"
            ," text"};
    static final String DB_CREATION="create table "+TABLE_NAME+"( "+
            DB_DATA[0]+DB_DATA_OPTION[0]+
            DB_DATA[1]+DB_DATA_OPTION[1]+
            DB_DATA[2]+DB_DATA_OPTION[2]+
            DB_DATA[3]+DB_DATA_OPTION[3]+
            DB_DATA[4]+DB_DATA_OPTION[4]+");";

    static final String DATETIME_FORMAT="yyyy/MM/dd HH:mm:ss";

    static final String[] cloth_select={"in clothes","no clothes"};

    CreateProductHelper helper=null;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dbsample);
        setActionBar((Toolbar) findViewById(R.id.toolbar_id));



        // tablet:auto rotation
        // smartPhone:portrait
        if(isTablet(this)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }else{
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        initView();

        helper=new CreateProductHelper(DBSampleActivity.this);

        update();


//        EditText id = (EditText) findViewById(R.id.id);
//        try {
////            db = helper.getWritableDatabase();
//            db = helper.getReadableDatabase();
//            long recodeCount = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
//            db.close();
//            Log.d(TAG, "recodeCount : " + recodeCount);
//            id.setText(String.valueOf(recodeCount + 1));
//        }catch(Exception e){
//            Log.e(ERROR_TAG,e.toString());
//            id.setText(String.valueOf(1));
//        }

    }

    /**
     * Menu
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.menu_main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.item1_id:
                Toast.makeText(DBSampleActivity.this,"item1",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(DBSampleActivity.this,SandboxActivity.class);
                startActivity(intent);
                return true;
            case R.id.item2_id:
                Toast.makeText(DBSampleActivity.this,"item2",Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3_id:
                Toast.makeText(DBSampleActivity.this,"item3",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return true;
        }
    }

    /**
     * initiate views
     */
    private void initView(){

        TextView table_name=(TextView)findViewById(R.id.tablename_id);
        table_name.setText(TABLE_NAME);

        Button insertBtn=(Button)findViewById(R.id.insert_id);
        insertBtn.setTag(BUTTON_TAG[0]);
        insertBtn.setOnClickListener(new ButtonClickListener());

        Button updateBtn=(Button)findViewById(R.id.update);
        updateBtn.setTag(BUTTON_TAG[1]);
        updateBtn.setOnClickListener(new ButtonClickListener());

        Button delBtn=(Button)findViewById(R.id.delete);
        delBtn.setTag(BUTTON_TAG[2]);
        delBtn.setOnClickListener(new ButtonClickListener());

        Button selectBtn=(Button)findViewById(R.id.select);
        selectBtn.setTag(BUTTON_TAG[3]);
        selectBtn.setOnClickListener(new ButtonClickListener());

        Button idPlusBtn=(Button)findViewById(R.id.id_plus_btn_id);
        idPlusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weight = Integer.parseInt(((EditText) findViewById(R.id.id)).getText().toString());
//                DecimalFormat df = new DecimalFormat("0000");
//                ((EditText) findViewById(R.id.id)).setText(df.format(weight + 1));
                ((EditText)findViewById(R.id.id)).setText(String.valueOf((weight + 1)));
            }
        });

        Button idMinusBtn=(Button)findViewById(R.id.id_minus_btn_id);
        idMinusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int weight=Integer.parseInt(((EditText)findViewById(R.id.id)).getText().toString());
//                DecimalFormat df=new DecimalFormat("0000");
//                ((EditText)findViewById(R.id.id)).setText(df.format(weight-1));
                ((EditText)findViewById(R.id.id)).setText(String.valueOf((weight - 1)));
            }
        });

        Button plusBtn=(Button)findViewById(R.id.plus_btn_id);
        plusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight=Double.parseDouble(((EditText)findViewById(R.id.weight)).getText().toString());
                DecimalFormat df=new DecimalFormat("##.0");
                ((EditText)findViewById(R.id.weight)).setText(df.format(weight+0.1));
            }
        });

        Button minusBtn=(Button)findViewById(R.id.minus_btn_id);
        minusBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double weight=Double.parseDouble(((EditText)findViewById(R.id.weight)).getText().toString());
                DecimalFormat df=new DecimalFormat("##.0");
                ((EditText)findViewById(R.id.weight)).setText(df.format(weight-0.1));
            }
        });

        ((RadioButton)findViewById(R.id.after_bath_id)).setChecked(true);
        ((RadioButton)findViewById(R.id.no_cloth_id)).setChecked(true);

        // auto write time now
        EditText date=(EditText)findViewById(R.id.date);
        Date date2=new Date();
        SimpleDateFormat sdf1=new SimpleDateFormat(DATETIME_FORMAT);
        date.setText(sdf1.format(date2));
    }

    /**
     *
     */
    public void update(){


        EditText id = (EditText) findViewById(R.id.id);
        EditText weight_edittxt = (EditText) findViewById(R.id.weight);
        try {
//            db = helper.getWritableDatabase();
            SQLiteDatabase db = helper.getReadableDatabase();
            long recodeCount = DatabaseUtils.queryNumEntries(db, TABLE_NAME);
            Log.d(TAG, "recodeCount : " + recodeCount);
            id.setText(String.valueOf(recodeCount + 1));


            Cursor cursor=db.query(TABLE_NAME, DB_DATA, null, null, null, null, "_id desc");
            cursor.moveToNext();
//            Log.d(TAG, "weight:" + cursor.getString(1));
            weight_edittxt.setText(String.valueOf(cursor.getString(1)));
            cursor.close();

            db.close();
        }catch(Exception e){
            Log.e(ERROR_TAG,e.toString());
            id.setText(String.valueOf(1));
        }
    }

//    /**
//     *
//     * @return
//     */
//    public boolean isTablet2(){
//        WindowManager wm=(WindowManager)getSystemService(WINDOW_SERVICE);
//        Display disp=wm.getDefaultDisplay();
//        Point size=new Point();
//        disp.getSize(size);
//        Log.d(TAG,String.valueOf(size.x));
//        return size.x>800;
//    }

    /**
     * スクリーンサイズが一定サイズ以上であればタブレットと認識
     * @return
     */
    public boolean isTablet(Context context){
        return context.getResources().getConfiguration().smallestScreenWidthDp>=SP_MAX_SCREEN_WIDTH;
    }

    /**
     * When DB manipulation button clicked
     *
     */
    class ButtonClickListener implements View.OnClickListener {

        /**
         *
         * @param v Button
         */
        public void onClick(View v){

            Log.d(TAG,"Button clicked");
            String tag=(String)v.getTag();
            String message="";
            TextView label=(TextView)findViewById(R.id.message);

            EditText id=(EditText)findViewById(R.id.id);
            EditText weight=(EditText)findViewById(R.id.weight);
            EditText date=(EditText)findViewById(R.id.date);

            // will be deteletd
//            EditText cloth=(EditText)findViewById(R.id.cloth);
//            EditText timing=(EditText)findViewById(R.id.timing);

            RadioGroup cloth_radio=(RadioGroup)findViewById(R.id.cloth_radio_btn_id);
            RadioGroup timing_radio=(RadioGroup)findViewById(R.id.timing_radio_btn_id);


            TableLayout tablelayout=(TableLayout)findViewById(R.id.list);
            tablelayout.removeAllViews();


            if(tag.equals(BUTTON_TAG[0])){
                Log.d(TAG,"register button clicked");

                // weight未入力なら終わる
                if(weight.getText().toString().equals("")){
                    Log.d(TAG,"weight nothing");
                    Toast.makeText(DBSampleActivity.this,"Please input your weight",Toast.LENGTH_SHORT).show();

                }else {

                    message = registerTable();

                    // registration of data
                    SQLiteDatabase db2 = helper.getWritableDatabase();
                    try {

                        db2.beginTransaction();

                        ContentValues val = new ContentValues();
                        val.put(DB_DATA[1], weight.getText().toString());

                        //                    val.put(DB_DATA[3], cloth.getText().toString());
                        //                    val.put(DB_DATA[4],timing.getText().toString());

                        int cloth_radio_id = cloth_radio.getCheckedRadioButtonId();
                        int timing_radio_id = timing_radio.getCheckedRadioButtonId();
                        RadioButton cloth_radio_btn = (RadioButton) findViewById(cloth_radio_id);
                        RadioButton timing_radio_btn = (RadioButton) findViewById(timing_radio_id);
                        Log.d(TAG, "cloth:" + cloth_radio_btn.getText().toString());
                        Log.d(TAG, "timing:" + timing_radio_btn.getText().toString());
                        val.put(DB_DATA[3], cloth_radio_btn.getText().toString());
                        val.put(DB_DATA[4], timing_radio_btn.getText().toString());

                        db2.insert(TABLE_NAME, null, val);
                        db2.setTransactionSuccessful();
                        db2.endTransaction();

                        message += "registered data";
                    } catch (Exception e) {
                        message = "failed to register";
                        Log.e(ERROR_TAG, e.toString());
                    } finally {
                        db2.close();
                    }
                }
            }else if(tag.endsWith(BUTTON_TAG[2])){
                SQLiteDatabase db = helper.getWritableDatabase();
                try{
                    String condition=null;
                    if (DB_DATA[0] != null && !DB_DATA[0].equals("")) {
                        condition="productid='"+DB_DATA[0].toString()+"'";
                    }
                    db=helper.getWritableDatabase();
                    db.beginTransaction();

                    ContentValues val=new ContentValues();
                    val.put(DB_DATA[1],weight.getText().toString());
//                    val.put(DB_DATA[3],cloth.getText().toString());
//                    val.put(DB_DATA[4],timing.getText().toString());

                    int cloth_radio_id=cloth_radio.getCheckedRadioButtonId();
                    int timing_radio_id=timing_radio.getCheckedRadioButtonId();
                    RadioButton cloth_radio_btn=(RadioButton)findViewById(cloth_radio_id);
                    RadioButton timing_radio_btn=(RadioButton)findViewById(timing_radio_id);
                    Log.d(TAG,"cloth:"+cloth_radio_btn.getText().toString());
                    Log.d(TAG,"timing:"+timing_radio_btn.getText().toString());
                    val.put(DB_DATA[3],cloth_radio_btn.getText().toString());
                    val.put(DB_DATA[4],timing_radio_btn.getText().toString());

                    db.update("product", val, condition, null);
                    db.setTransactionSuccessful();
                    db.endTransaction();

                    message="datas updated";
                }catch(Exception e){
                    message="failed to update";
                    Log.e(ERROR_TAG,e.toString());
                }finally{
                    db.close();
                }
            }else if(tag.endsWith(BUTTON_TAG[2])){
                SQLiteDatabase db = helper.getWritableDatabase();
                try{
                    String condition=null;
                    if (DB_DATA[0] != null && !DB_DATA[0].equals("")) {
                        condition=DB_DATA[0]+"='"+DB_DATA[0].toString()+"'";
                    }
                    db=helper.getWritableDatabase();
                    db.beginTransaction();
                    db.delete("product", condition, null);
                    db.setTransactionSuccessful();
                    db.endTransaction();
                    message="data deleted";
                }catch(Exception e){
                    message="failed to delete";
                    Log.e(ERROR_TAG,e.toString());
                }finally{
                    db.close();
                }
            }else if(tag.equals(BUTTON_TAG[3])){
                SQLiteDatabase db = helper.getWritableDatabase();
                try{
                    db=helper.getReadableDatabase();
//                    String columns[]=DB_DATA;
                    // _id descending

                    tablelayout.setStretchAllColumns(true);

                    TableRow headrow=new TableRow(DBSampleActivity.this);

                    // column name output
                    TextView[] headtxt=new TextView[DB_DATA.length];
                    for(int i=0;i<DB_DATA.length;i++){
                        headtxt[i]=new TextView(DBSampleActivity.this);
                        headtxt[i].setText(DB_DATA[i]);
                        headtxt[i].setGravity(Gravity.CENTER_HORIZONTAL);
                        headtxt[i].setWidth(60);
                        headrow.addView(headtxt[i]);
                    }
                    tablelayout.addView(headrow);

                    // data output
                    Cursor cursor=db.query(TABLE_NAME,DB_DATA,null,null,null,null,"_id desc");
                    while(cursor.moveToNext()){
                        TableRow row=new TableRow(DBSampleActivity.this);

                        TextView[] contenttxt=new TextView[DB_DATA.length];
                        for(int i=0;i<DB_DATA.length;i++){
                            contenttxt[i]=new TextView(DBSampleActivity.this);
                            contenttxt[i].setGravity(Gravity.CENTER_HORIZONTAL);
                            contenttxt[i].setText(cursor.getString(i));
                            row.addView(contenttxt[i]);
                        }
                        tablelayout.addView(row);
                    }
                    cursor.close();

                    message="get data";

                }catch(Exception e){
                    message="failed to get data";
                    Log.e(ERROR_TAG,e.toString());
                }finally{
                    db.close();
                }
            }
            label.setText(message);

        }
    }

    /**
     * Table creation
     * @param message about registration result
     * @return
     */
    public String registerTable(){
        String message="";
        SQLiteDatabase db = helper.getWritableDatabase();
        try{
            String sql=DB_CREATION;
            db.execSQL(sql);
            message="created table!\n";
        }catch(Exception e){
            message="already created table!\n";
            Log.e(ERROR_TAG,e.toString());
        }finally{
            db.close();
            return message;
        }
    }

}
