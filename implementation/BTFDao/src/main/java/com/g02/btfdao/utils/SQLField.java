package com.g02.btfdao.utils;

import com.g02.btfdao.annotations.FieldName;
import com.g02.btfdao.annotations.Ignore;
import com.g02.btfdao.annotations.PrimaryKey;
import com.g02.btfdao.dao.Savable;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class SQLField {
    private final Field field;

    public SQLField(Field field) {
        this.field = field;
        assert isIgnored() || isValid(); //Check integrity
    }

    /**
     * @return whether this field should be ignored
     */
    public boolean isIgnored() {
        return field.isAnnotationPresent(Ignore.class) || field.isSynthetic();
    }

    /**
     * @return whether this field should not be ignored, reverse of isIgnored, useful for method-references
     */
    public boolean isNotIgnored() {
        return !isIgnored();
    }

    /**
     * @return The name that this field has in the java source code
     */
    public String getFieldName() {
        if (field.isAnnotationPresent(FieldName.class)) {
            return field.getAnnotation(FieldName.class).value();
        }
        return field.getName();
    }

    /**
     * @return the name that this field has in the db
     */
    public String getSQLFieldName() {
        if (field.isAnnotationPresent(FieldName.class)) {
            return field.getAnnotation(FieldName.class).value();
        }
        return field.getName();
    }

    public String getRelationSQLFieldName() {
        return getEncapsulatedTableName() + "_" + getSQLFieldName();
    }

    /**
     * @return the name that the table of this field has in the db
     */
    public String getEncapsulatedTableName() {
        return Mapper.getSQLTableName(field.getDeclaringClass());
    }

    /**
     * @return the name of the relation table that this field has
     */
    public String getRelationTableName() {
        return Mapper.getSQLTableName(field.getDeclaringClass()) + "_" + getSQLFieldName() + "_" + Mapper.getSQLTableName(getReferencingClass());
    }

    public List<String> getRelationTableLeftPKnames() {
        return Mapper.getPrimaryKeyFields(getFieldClass()).stream()
                .map(f -> f.getEncapsulatedTableName() + "_" + f.getSQLFieldName())
                .collect(Collectors.toList());
    }

    public List<String> getRelationTableRightPKnames() {
        return Mapper.getPrimaryKeyFields(getReferencingClass()).stream()
                .map(f -> f.getEncapsulatedTableName() + "_" + f.getSQLFieldName())
                .collect(Collectors.toList());
    }

    public List<String> getRelationTableLeftPKnameswithType() {
        return Mapper.getPrimaryKeyFields(getFieldClass()).stream()
                .map(f -> f.getEncapsulatedTableName() + "_" + f.getSQLFieldName() + " " + f.getSQLType(false))
                .collect(Collectors.toList());
    }

    public List<String> getRelationTableRightPKnameswithType() {
        return Mapper.getPrimaryKeyFields(getReferencingClass()).stream()
                .map(f -> f.getEncapsulatedTableName() + "_" + f.getSQLFieldName() + " " + f.getSQLType(false))
                .collect(Collectors.toList());
    }

    /**
     * @return whether this field has a primitive Type, aka whether it is present in the (main) SQL-Table
     */
    public boolean isPrimitive() {
        String sqlType = TypeMappings.getTypeName(field.getType());
//        System.out.println("type: "+sqlType);
        return sqlType != null;
    }

    /**
     * @return whether this field is an array of a primitive type
     */
    public boolean isPrimitiveArray() {
        return isPrimitive() && isArray();
    }

    public boolean isPrimitiveList() {
        return isPrimitive() && isList();
    }

    /**
     * @return The Class in which this field is declared
     */
    public Class<? extends Savable> getFieldClass() {
        return (Class<? extends Savable>) field.getDeclaringClass();
    }

    public Class<?> getFieldType() {
        if (isArray()) return field.getType().getComponentType();
        return field.getType();
    }

    public Class<? extends Savable> getReferencingClass() {
        if (!isArray()) {
            return (Class<? extends Savable>) field.getType();
        }
        if (isArray()) {
            return (Class<? extends Savable>) field.getType().getComponentType();
        }
        if (isList()) {
            throw new UnsupportedOperationException("Not implemented yet");//TODO:AHHHHH
        }
        return null;
    }

    /**
     * @return whether this SQLField is "Valid", should ideally only be used in dev environments via assertions
     */
    public boolean isValid() {
        if(true){ //Dump information in case errors occur
            System.out.println("fieldClass: "+getFieldClass().getName());
            System.out.println("fieldTypeName: "+getFieldType().getName());

        }
        if (isIgnored()) return true;
        assert !isList() : "Not supported yet";
        if (!isPrimitive()) {
//            System.out.println(field.getDeclaringClass().getName());
            assert Savable.class.isAssignableFrom(field.getDeclaringClass());
        }
        assert isPrimitive() || Savable.class.isAssignableFrom(field.getType())
                || (isArray() && Savable.class.isAssignableFrom(field.getType().getComponentType()))
                || (isList() && Savable.class.isAssignableFrom(getListType()));
        assert isPrimitive() || !isPrimaryKey() : getFieldName() + " hat einen typ als primary key deklariert der nicht als primary key verwendet werden kann";

        assert !isArray() || !isList();//should not be possible unless methods implemented wrongly
        return true;
    }

    /**
     * @return whether the value of this field is autogenerated when inserted into the db
     */
    public boolean isAutogenerated() {
        return field.isAnnotationPresent(PrimaryKey.class) && field.getAnnotation(PrimaryKey.class).autogen();
    }

    public Class<?> getListType() {
        assert isList();
        //Begin generic hell, aka f*** Type Erasure
        ParameterizedType fieldType = (ParameterizedType) field.getGenericType();
        return (Class<?>) fieldType.getActualTypeArguments()[0];
    }

    /**
     * @return whether this field is declared as a primary key
     */
    public boolean isPrimaryKey() {
        return field.isAnnotationPresent(PrimaryKey.class);
    }

    /**
     * @return whether this Field should be included in an insert statement (has to be directly in the table and not be autogenerated)
     */
    public boolean isInsertable() {
        return isPrimitive() && !isAutogenerated();
    }

    /**
     * @return The string that represents the type that this field has in the db like "INTEGER" or "TEXT"
     */
    public String getSQLType() {
        return getSQLType(true);

    }

    /**
     * @param autogenAllowed The string that represents the type that this field has in the db like "INTEGER" or "TEXT"
     * @return whether integer will be converted to serial etc if the field value is set to autogenerate.
     */
    public String getSQLType(boolean autogenAllowed) {
        if (autogenAllowed && isPrimaryKey()) {
            if (isAutogenerated()) {
                if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                    return "SERIAL";
                }
                if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                    return "BIGSERIAL";
                }
                assert false : "Der primarykey kann nur als int oder long autogeneriert werden";
            }
        }
        if (isList()) {
            assert false : "not implemented yet";
            System.out.println(getListType());
            if (TypeMappings.getTypeName(getListType()) != null) {
                var array = Array.newInstance(getListType(), 0);
                return TypeMappings.getTypeName(array.getClass());
            }
        }
        return TypeMappings.getTypeName(field.getType()); //Name aus der Typemappings tabelle
    }

    /**
     * @param entity the entity of which the object in the field should be returned
     * @return the object that this entity contains in the field
     */
    public Object getFieldContent(Object entity) {
        if (!field.trySetAccessible()) {
            throw new RuntimeException("Dao needs Reflection to work");
        }
        try {
            return field.get(entity);
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void setFieldContent(Object entity, Object content) {
        if (!field.trySetAccessible()) {
            throw new RuntimeException("Dao needs Reflection to work");
        }
        try {
            field.set(entity, content);
        } catch (IllegalAccessException e) {
//            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public boolean isArray() {
        return field.getType().isArray();
    }

    public boolean isList() {
        return List.class.isAssignableFrom(field.getType());
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", SQLField.class.getSimpleName() + "[", "]")
                .add("field=" + field)
                .add("isPrimiteve=" + isPrimitive())
                .toString();
    }
}
