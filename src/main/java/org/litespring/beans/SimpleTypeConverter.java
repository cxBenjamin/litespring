package org.litespring.beans;

import org.litespring.beans.propertyeditors.CustomBooleanEditor;
import org.litespring.beans.propertyeditors.CustomNumberEditor;
import org.litespring.util.ClassUtils;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

public class SimpleTypeConverter implements TypeConverter {


    private Map<Class<?>, PropertyEditor> defaultEditors;

    public SimpleTypeConverter() {
    }


    @Override
    public <T> T convertIfNecessary(Object value, Class<T> requiredType) throws TypeMismatchException {

        if (ClassUtils.isAssignableValue(requiredType, value)) {
            return (T) value;
        } else {

            if (value instanceof String) {

                PropertyEditor editor = findDefaultEditor(requiredType);

                try {
                    editor.setAsText((String) value);
                } catch (IllegalArgumentException e) {
                    throw new TypeMismatchException(value, requiredType);
                }

                return (T) editor.getValue();
            } else {
                throw new RuntimeException("TODO : can not convert for " + value + " class: " + requiredType);
            }
        }

    }


    private PropertyEditor findDefaultEditor(Class<?> requiredType) {

        PropertyEditor editor = this.getDefaultEditor(requiredType);

        if (editor == null) {
            throw new RuntimeException("Editor for " + requiredType + " has not been implemented");
        }
        return editor;
    }

    public PropertyEditor getDefaultEditor(Class<?> requiredType) {

        if (this.defaultEditors == null) {
            createDefaultEditors();
        }
        return this.defaultEditors.get(requiredType);

    }

    private void createDefaultEditors() {

        this.defaultEditors = new HashMap<Class<?>, PropertyEditor>(64);

        this.defaultEditors.put(boolean.class, new CustomBooleanEditor(false));

        this.defaultEditors.put(Boolean.class, new CustomBooleanEditor(true));


        this.defaultEditors.put(int.class, new CustomNumberEditor(Integer.class, false));

        this.defaultEditors.put(Integer.class, new CustomNumberEditor(Integer.class, true));

    }

}
