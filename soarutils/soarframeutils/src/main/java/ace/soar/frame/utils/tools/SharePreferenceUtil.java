package ace.soar.frame.utils.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.Set;

/**
 * Auth：yujunyao on 2015/11/6 11:06
 * Email：yujunyao@yonglibao.com
 */
public class SharePreferenceUtil {
    public static final String KEY_VALUE_STORAGE = "key_value_storage";

    public static SharedPreferences sharedPreference;

    public SharePreferenceUtil(Context mContext){
        if(mContext == null){
            throw new NullPointerException("mContext is null");
        }

        if(sharedPreference == null){
            sharedPreference = mContext.getSharedPreferences(KEY_VALUE_STORAGE, Activity.MODE_PRIVATE);
        }
    }


    public void setValues(String key, Object obj){
        Editor editor = sharedPreference.edit();
        if(obj instanceof String) {
            editor.putString(key, (String) obj);
        }else if(obj instanceof Integer) {
            editor.putInt(key, (Integer) obj);
        }else if(obj instanceof Boolean) {
            editor.putBoolean(key, (Boolean) obj);
        }else if(obj instanceof Long) {
            editor.putLong(key, (Long) obj);
        }else if(obj instanceof Float) {
            editor.putFloat(key, (Float) obj);
        }else{
            throw new IllegalArgumentException("parameter is wrong");
        }
        editor.commit();
    }

    public String getValueString(String key, String defaultValue) {
        return sharedPreference.getString(key, defaultValue);
    }
    public int getValueInteger(String key, int defaultValue) {
        return sharedPreference.getInt(key, defaultValue);
    }
    public boolean getValueBoolean(String key, boolean defaultValue) {
        return sharedPreference.getBoolean(key, defaultValue);
    }
    public long getValueLong(String key, long defaultValue) {
        return sharedPreference.getLong(key, defaultValue);
    }
    public float getValueFloat(String key, float defaultValue) {
        return sharedPreference.getFloat(key, defaultValue);
    }

    public Object getValues(String key, Object defaultValue) {
        if(defaultValue instanceof String) {
            return sharedPreference.getString(key, (String) defaultValue);
        }else if(defaultValue instanceof Integer) {
            return sharedPreference.getInt(key, (Integer) defaultValue);
        }else if(defaultValue instanceof Boolean) {
            return sharedPreference.getBoolean(key, (Boolean) defaultValue);
        }else if(defaultValue instanceof Long) {
            return sharedPreference.getLong(key, (Long) defaultValue);
        }else if(defaultValue instanceof Float) {
            return sharedPreference.getFloat(key, (Float) defaultValue);
        }
        return "";
    }

    public void setArrayValues(String key, Set<String> values){
        Editor editor = sharedPreference.edit();
        editor.putStringSet(key, values);
        editor.commit();
    }

    public Set<String> getArrayValues(String key){
        return sharedPreference.getStringSet(key, null);
    }

}
