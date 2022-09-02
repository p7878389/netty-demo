package com.shareworks.codeanalysis.client.config;

import cn.hutool.core.util.StrUtil;
import com.shareworks.codeanalysis.common.enums.BaseEnums;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.stereotype.Component;

/**
 * @author martin.peng
 * @Desc
 * @date 2022/8/31 10:23
 */
@Component
public class EnumConvertFactory implements ConverterFactory<String, BaseEnums> {

    @Override
    public <T extends BaseEnums> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToIEum<>(targetType);
    }

    @SuppressWarnings("all")
    private static class StringToIEum<T extends BaseEnums> implements Converter<String, T> {

        private Class<T> targerType;

        public StringToIEum(Class<T> targerType) {
            this.targerType = targerType;
        }

        @Override
        public T convert(String source) {
            if (StrUtil.isEmpty(source)) {
                return null;
            }
            return (T) EnumConvertFactory.getIEnum(this.targerType, source);
        }
    }

    public static <T extends BaseEnums> Object getIEnum(Class<T> targetType, String source) {
        for (T enumObj : targetType.getEnumConstants()) {
            if (source.equals(String.valueOf(enumObj.getName()))) {
                return enumObj;
            }
        }
        return null;
    }
}
