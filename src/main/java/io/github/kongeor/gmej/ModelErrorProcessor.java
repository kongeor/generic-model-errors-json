package io.github.kongeor.gmej;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelErrorProcessor {

    public void process(Object model, Map<String, Object> errors) {
	processImpl(model, (Map<String, Object>) errors.get("errors"));
    }

    private void processImpl(Object model, Map<String, Object> errors) {
	 for (Map.Entry<String, Object> entry: errors.entrySet()) {
	     Object values = entry.getValue();
	     if (List.class.isAssignableFrom(values.getClass())) {
		 String key = entry.getKey();
		 int index = keyToIndex(key);
		 if (index >= 0) {
		     setErrors(getNth(model, key, index), "base", (List<String>) values);
		 } else if (entry.getKey().equals("base")) {
		     setErrors(model, entry.getKey(), (List<String>) values);
		 } else {
		     setNestedErrors(model, entry.getKey(), (List<String>) values);
		 }
	     } else if (Map.class.isAssignableFrom(values.getClass())) {
		 Object nested = callMethod(entry.getKey(), model);
		 if (nested != null) {
		     processImpl(nested, (Map<String, Object>) values);
		 }
	     }
	 }
    }

    private void setNestedErrors(Object model, String key, List<String> errors) {
	Object nested = callMethod(key, model);
	setErrors(nested, key, errors);
    }

    private void setErrors(Object model, String key, List<String> errors) {
	try {
	    Method getErrorsMethod = model.getClass().getMethod("getErrors");
	    Map<String, List<String>> errorMap = (Map<String, List<String>>) getErrorsMethod.invoke(model);
	    errorMap.put(key, errors);
	} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
	    e.printStackTrace();
	}
    }

    private Object callMethod(String key, Object object) {
	try {
	    int index = keyToIndex(key);
	    if (index >= 0) {
		return getNth(object, key, index);
	    } else {
		Method method = object.getClass().getMethod(keyToGetter(key));
		return method.invoke(object);
	    }
	} catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
	    e.printStackTrace();
	}
	return null;
    }

    public String keyToGetter(String key) {
	return "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
    }

    public int keyToIndex(String key) {
	String[] split = key.split("\\[");
	if (split.length == 2) {
	    return Integer.parseInt(butLast(split[1]));
	} else {
	    return -1;
	}
    }

    public String getNameFromIndexedName(String key) {
	return key.split("\\[")[0];
    }

    public String butLast(String str) {
	return str.substring(0, str.length() - 1);
    }

    private Object getNth(Object model, String key, int index) {
	return ((List) callMethod(getNameFromIndexedName(key), model)).get(index);
    }
}
