package ace.soar.frame.utils.model;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Set;

import ace.soar.frame.utils.observer.DataObserable;
import ace.soar.frame.utils.observer.DataObserver;

/**
 * Created by gaofei on 2016/4/18.
 */
public class BaseModel extends DataObserable implements Parcelable{

    public static final String CONTENT_MAP = "content_url";

    protected HashMap<String, Object> mContent = new HashMap<String, Object>();
    protected String className = getClass().getName();


    public BaseModel(DataObserver observer) {
        super(observer);
    }


    @Override
    public final void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getClassName());
        Bundle b = new Bundle();
        b.putSerializable(CONTENT_MAP, mContent);
        dest.writeBundle(b);

        // call to write values to parcel
        onWriteToParcel(dest, flags);
    }

    /**
     * it's called when Parcelable callback and request transform values to Parcel
     *
     * @param dest
     * @param flags
     */
    protected void onWriteToParcel(Parcel dest, int flags) {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getClassName() {
        return className;
    }

    protected void put(String key, Object value) {
        mContent.put(key, value);
    }

    public Object get(String key) {
        return mContent.get(key);
    }

    public Object get(String key, Object defValue) {
        Object v = get(key);
        if (v == null)
            return defValue;
        else
            return v;
    }

    public int getInt(String key, int default_value) {
        Object value = get(key);
        if (value == null)
            return default_value;
        else if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return default_value;
            }
        } else if (value instanceof Boolean) {
            try {
                return (boolean) value ? 1 : 0;
            } catch (NumberFormatException e) {
                return default_value;
            }
        }
        return default_value;
    }

    public long getLong(String key, long default_value) {
        Object value = get(key);
        if (value == null)
            return default_value;
        else if (value instanceof Long) {
            return (Long) value;
        } else if (value instanceof String) {
            try {
                return Long.parseLong((String) value);
            } catch (NumberFormatException e) {
                return default_value;
            }
        }
        return default_value;
    }

    public float getFloat(String key, float default_value) {
        Object ret_float = get(key);
        if (ret_float != null) {
            try {
                return Float.parseFloat(ret_float.toString());
            } catch (NumberFormatException e) {
                //ignore;
            }
        }
        return default_value;
    }

    public float getFloat(String key) {
        Object ret_float = get(key);
        if (ret_float != null) {
            try {
                return Float.parseFloat(ret_float.toString());
            } catch (NumberFormatException e) {
                //ignore;
            }
        }
        return -1;
    }

    public String getString(String key) {
        return (String) get(key);
    }

    public Boolean getBoolean(String key) {
        return (Boolean) get(key);
    }

    public static BaseModel createNewInstance(String className)
            throws ClassNotFoundException, IllegalAccessException, IllegalArgumentException,
            NoSuchMethodException, InstantiationException, InvocationTargetException {
        Class<?> parceledClass = Class.forName(className);
        return (BaseModel) parceledClass.newInstance();
    }

    public static BaseModel createNewInstance(Class<?> parceledClass, Object params)
            throws NoSuchMethodException, IllegalArgumentException, InstantiationException,
            IllegalAccessException, InvocationTargetException {
        Constructor<?> constr = parceledClass.getConstructor(Context.class);
        return (BaseModel) constr.newInstance(params);
    }

    public static final Creator<BaseModel> CREATOR = new Creator<BaseModel>() {
        /**
         * No way to avoid this compile warning, because in HashMap<T,V> T,V are
         * not known.
         */
        @SuppressWarnings("unchecked")
        @Override
        public BaseModel createFromParcel(Parcel src) {
            String className = src.readString();
            try {
                BaseModel data = (BaseModel) createNewInstance(className);
                Bundle b = src.readBundle();
                data.mContent = (HashMap<String, Object>) b.getSerializable(CONTENT_MAP);
                // callback
                data.onReadFromParcel(src);
                return data;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public BaseModel[] newArray(int size) {
            return new BaseModel[size];
        }

    };

    public Set<String> getKeys() {
        return mContent.keySet();
    }

    /**
     * read values that backed up to Parcel.
     *
     * @param in
     */
    protected void onReadFromParcel(Parcel in) {
    }
}
