package lukas.sobotik.tasklooper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;
import androidx.annotation.Nullable;
import lukas.sobotik.tasklooper.entity.Task;

public class TaskDatabaseHelper extends SQLiteOpenHelper {
    Context context;
    public static final String DATABASE_NAME = "UserTasks.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "user_tasks";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TASK_TITLE = "task_title";
    public static final String COLUMN_TASK_DESCRIPTION = "task_description";
    public static final String COLUMN_TASK_CHECK_STATE = "task_check_state";
    public static final String COLUMN_TASK_CHECK_DATE = "task_check_date";


    public TaskDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME
                + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TASK_TITLE + " TEXT, "
                + COLUMN_TASK_DESCRIPTION + " TEXT, "
                + COLUMN_TASK_CHECK_STATE + " TEXT, "
                + COLUMN_TASK_CHECK_DATE + " TEXT);";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addTask(String taskTitle, String taskDescription) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_TASK_TITLE, taskTitle);
        cv.put(COLUMN_TASK_DESCRIPTION, taskDescription);
        cv.put(COLUMN_TASK_CHECK_STATE, "");
        cv.put(COLUMN_TASK_CHECK_DATE, "");

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed to save to the Database", Toast.LENGTH_SHORT).show();
        }
    }

    Cursor readAllData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor;
        cursor = db.rawQuery(query, null);
        return cursor;
    }

    public void updateData(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_TASK_TITLE, task.getTaskName());
        cv.put(COLUMN_TASK_DESCRIPTION, task.getTaskDescription());
        cv.put(COLUMN_TASK_CHECK_STATE, task.getState().toString());
        cv.put(COLUMN_TASK_CHECK_DATE, task.getCheckedDate());

        String stringId = String.valueOf(task.getId());

        long result = db.update(TABLE_NAME, cv, COLUMN_ID + "= ?", new String[]{stringId});
        if (result == -1) {
            Toast.makeText(context, "Failed to edit the Task", Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteItem(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        String stringId = String.valueOf(task.getId());

        long result = db.delete(TABLE_NAME,COLUMN_ID + "= ?", new String[]{stringId});
        if (result == -1) {
            Toast.makeText(context, "Failed to delete the Task", Toast.LENGTH_SHORT).show();
        }
    }
}
