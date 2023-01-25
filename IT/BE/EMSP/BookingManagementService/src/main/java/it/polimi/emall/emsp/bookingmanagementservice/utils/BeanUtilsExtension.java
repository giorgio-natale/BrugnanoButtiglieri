package it.polimi.emall.emsp.bookingmanagementservice.utils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.ClassUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BeanUtilsExtension {
    public static void extendedCopyProperties(Object source, Object dest) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        if(ClassUtils.isPrimitiveOrWrapper(source.getClass()) ||
            source instanceof List<?> ||
            source instanceof Set<?> ||
            source instanceof Enum<?>
        ){
            return;
        }
        // source is a bean
        Set<String> propertyNames = BeanUtils.describe(source).keySet();

    }
}
