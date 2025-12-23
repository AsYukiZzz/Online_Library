package xyz.uz.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;
import xyz.uz.enums.BaseEnum;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

@Component
public class StringToEnumConverterFactory implements ConverterFactory<String, BaseEnum> {

    private final Map<Class<? extends BaseEnum>, Converter<String,? extends BaseEnum>> CONVERTER_CACHE = new WeakHashMap<>();

    @Override
    public <T extends BaseEnum> Converter<String, T> getConverter(Class<T> targetType) {
        @SuppressWarnings("unchecked")
        Converter<String, T> converter = (Converter<String, T>) CONVERTER_CACHE.computeIfAbsent(targetType,
                key -> new StringToEnum<>(targetType)
        );
        return converter;
    }

    private static class StringToEnum<T extends BaseEnum> implements Converter<String,T>{

        // 缓存
        private final Map<Integer,T> enumMap = new HashMap<>();

        public StringToEnum(Class<T> enumType){
            T[] enums = enumType.getEnumConstants();
            for (T e : enums) {
                enumMap.put(e.getCode(),e);
            }
        }

        @Override
        public T convert(String source) {
            if (source == null || source.trim().isEmpty()){
                return null;
            }

            try{
                // 1. 尝试将字符串转换为Integer
                int code = Integer.parseInt(source);

                T result = enumMap.get(code);

                if (result == null){
                    throw new IllegalArgumentException("无法找到 code=[" + source + "] 对应的枚举");
                }

                return result;
            }catch (NumberFormatException e){
                throw new IllegalArgumentException("无法将字符串 [" + source + "] 转换为有效的枚举 code", e);
            }
        }
    }
}
