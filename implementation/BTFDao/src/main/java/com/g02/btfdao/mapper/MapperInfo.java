package com.g02.btfdao.mapper;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;

class MapperInfo {

    private List<Annotation> annotations;

    private List<Field> fields;

    public MapperInfo(List<Annotation> annotations, List<Field> fields) {
        this.annotations = annotations;
        this.fields = fields;
    }

    public List<Annotation> getAnnotations() {
        return annotations;
    }

    public List<Field> getFields() {
        return fields;
    }
}
