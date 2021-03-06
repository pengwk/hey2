package omark.hey;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class S {
    static Editor e;
    static SharedPreferences s;
    static Context c;

    public static S init(Context context, String name) {
        s = context.getSharedPreferences(name, 0);//Context.MODE_WORLD_READABLE);// + Context.MODE_WORLD_WRITEABLE);
        e = s.edit();
        c = context;
        return new S();
    }

    //Get
    public static String get(String name, String def) {
        return s == null ? def : s.getString(name, def);
    } public static boolean get(String name, boolean def) {
        return s == null ? def : s.getBoolean(name, def);
    } public static float get(String name, float def) {
        return s == null ? def : s.getFloat(name, def);
    } public static long get(String name, long def) {
        return s == null ? def : s.getLong(name, def);
    } public static int get(String name, int def) {
        return s == null ? def : s.getInt(name, def);
    }

    public static Map<String, ?> getAll() {
        if (s == null) return null;
        return s.getAll();
    }

    //Put
    public static S put(String name, String def) {
        if (e != null) e.putString(name, def);
        return new S();
    } public static S put(String name, boolean def) {
        if (e != null) e.putBoolean(name, def);
        return new S();
    } public static S put(String name, float def) {
        if (e != null) e.putFloat(name, def);
        return new S();
    } public static S put(String name, long def) {
        if (e != null) e.putLong(name, def);
        return new S();
    } public static S put(String name, int def) {
        if (e != null) e.putInt(name, def);
        return new S();
    }

    public static boolean contains(String name) {
        if (s == null) return false;
        return s.contains(name);
    }

    public static void del(String name) {
        if (e != null) e.remove(name);
    }

    //此系列函数效率低，请谨慎使用
    public static S addIndex(String max_name, String name, String value) {
        final int n = get(max_name, 0);

        put(name + n, value);
        put(max_name, n + 1);

        ok();
        return new S();
    } public static S addIndexX(String max_name, String[] name, String[] value) {
        final int n = get(max_name, 0);
        for (int i = 0; i < name.length; i++) {
            put(name[i] + n, value[i]);
        }
        put(max_name, n + 1);

        ok();
        return new S();
    } public static S delIndex(String max_name, String name, int index) {
        try {
            final int n = get(max_name, 0);
            List<String> a = new ArrayList<String>();
            for (int i = 0; i < n; i++) {
                a.add(get(name + i, ""));
            }
            a.remove(index);

            del(name + n);

            for (int i = 0; i < n - 1; i++) {
                put(name + i, a.get(i));
            }
            put(max_name, n - 1);
            ok();
        } catch (Exception e) {
            Toast.makeText(c, "EXT.ERROR:" + e, Toast.LENGTH_LONG);
        }
        return new S();
    } public static S delIndexX(String max_name, String[] name, int index) {
        final int n = get(max_name, 0);
        for (int i = 0; i < name.length; i++) {
            delIndex(max_name, name[i], index);
        }
        put(max_name, n - 1);
        ok();
        return new S();
    }

    public static S ok() {
        if (e != null) e.apply();
        return new S();
    }

    public static boolean okay() {
        if (e != null) return e.commit();
        return false;
    }
    
    public static void storePic(String key, Bitmap bitmap) {
        if (key == null || key.isEmpty() || bitmap == null) return;
        FileOutputStream fos = null;
        try {
            fos = c.openFileOutput(key, Context.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (FileNotFoundException e) {

        } finally {
            if (fos != null) {
                try {
                    fos.flush();
                    fos.close();
                } catch (IOException e) {

                }
            }
        }
    } public static Bitmap getStorePic(String key) {
        if (key == null || key.isEmpty()) return null;
        FileInputStream fin = null;
        Bitmap bitmap = null;
        try {
            fin = c.openFileInput(key);
            bitmap = BitmapFactory.decodeStream(fin);
        } catch (FileNotFoundException e) {

        }
        return bitmap;
    }

    public static int getColor(int resid) {
        return c.getResources().getColor(resid);
    } public static String getString(int resid) {
        return c.getResources().getString(resid);
    } public static int getVersionCode() {
        int version = 0;
        try {
            version = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    } public static String getVersionName() {
        String version = "";
        try {
            version = c.getPackageManager().getPackageInfo(c.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }
}
