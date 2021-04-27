package org.quiltmc.asmr.mixin.processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeMirror;

public class ClassNameExtractor implements AnnotationValueVisitor<List<TypeMirror>, ProcessingEnvironment> {
    @Override
    public List<TypeMirror> visit(AnnotationValue av) {
        return visit(av, null);
    }

    @Override
    public List<TypeMirror> visit(AnnotationValue arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitAnnotation(AnnotationMirror arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitArray(List<? extends AnnotationValue> values, ProcessingEnvironment arg1) {
        List<TypeMirror> types = new ArrayList<>();
        for (AnnotationValue val : values) {
            types.addAll(val.accept(this, arg1));
        }
        return types;
    }

    @Override
    public List<TypeMirror> visitBoolean(boolean arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitByte(byte arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitChar(char arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitDouble(double arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitEnumConstant(VariableElement arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitFloat(float arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitInt(int arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitLong(long arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitShort(short arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitString(String arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<TypeMirror> visitType(TypeMirror type, ProcessingEnvironment arg1) {
        List<TypeMirror> types = new ArrayList<>();
        types.add(type);
        return types;
    }

    @Override
    public List<TypeMirror> visitUnknown(AnnotationValue arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }
    
}
