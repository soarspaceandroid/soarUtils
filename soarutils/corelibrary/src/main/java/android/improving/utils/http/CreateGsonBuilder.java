package android.improving.utils.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;



/**
 * 创建一个通用的GsonBuilder
 * @author:郁俊耀(Allen)
 * @data:  2015-10-22 下午2:41:31
 */
public class CreateGsonBuilder {
	
	public static GsonBuilder createBuilder(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Boolean.class, booleanAsIntAdapter);
		builder.registerTypeAdapter(boolean.class, booleanAsIntAdapter);
		builder.registerTypeAdapterFactory(new TypeAdapterFactory() {
			
			@SuppressWarnings("unchecked")
			@Override
			public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> type) {
				final Class<T> rawType = (Class<T>) type.getRawType();
				final T[] types = rawType.getEnumConstants();
				/**对枚举类型的做转换处理*/
				if(rawType.isEnum()){
					new TypeAdapter<T>() {

						@Override
						public void write(JsonWriter out, T value)
								throws IOException {
							if (value == null) {
								out.nullValue();
							} else {
								int index = 0;
								for (int i = 0; i < types.length; i++) {
									if (types[i].toString().equals(
											value.toString())) {
										index = i;
										break;
									}
								}
								out.value(index);
							}
						}

						@Override
						public T read(JsonReader in) throws IOException {
							if (in.peek() == JsonToken.NULL) {
								in.nextNull();
								return null;
							} else {
								return types[in.nextInt()];
							}
						}
					};
				}
				return null;
			}
		});
		return builder;
	}
	
	/**布尔值当作int来处理的适配器*/
	private static final TypeAdapter<Boolean> booleanAsIntAdapter = new TypeAdapter<Boolean>() {

		@Override
		public void write(JsonWriter out, Boolean value) throws IOException {
			if(null == value){
				out.nullValue();
			}else{
				out.value(value ? 1 : 0);
			}
		}

		@Override
		public Boolean read(JsonReader in) throws IOException {
			JsonToken peek = in.peek();
			switch (peek) {
			case BOOLEAN:
				return in.nextBoolean();
			case NULL:
				in.nextNull();
				return null;
			case NUMBER:
				return in.nextInt() != 0;
			case STRING:
				return Boolean.parseBoolean(in.nextString());
			default:
				throw new IllegalArgumentException("booleanAsIntAdapter illega argument error");
			}
		}
	};
	
}
