package org.tongji.programming.helper;

import java.util.ArrayList;
import java.util.List;

public class ListHelper {
    public static <T> List<T> safeConvertToList(List<?> list, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        for (Object o : list)
        {
            result.add(clazz.cast(o));
        }
        return result;
    }
}
