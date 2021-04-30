package org.quiltmc.asmr.mixin.processor;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.AnnotationValueVisitor;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;

public class ClassNameExtractor implements AnnotationValueVisitor<List<DeclaredType>, ProcessingEnvironment> {
    @Override
    public List<DeclaredType> visit(AnnotationValue av) {
        return visit(av, null);
    }

    @Override
    public List<DeclaredType> visit(AnnotationValue arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitAnnotation(AnnotationMirror arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitArray(List<? extends AnnotationValue> values, ProcessingEnvironment arg1) {
        List<DeclaredType> types = new ArrayList<>();
        for (AnnotationValue val : values) {
            types.addAll(val.accept(this, arg1));
        }
        return types;
    }

    @Override
    public List<DeclaredType> visitBoolean(boolean arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitByte(byte arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitChar(char arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitDouble(double arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitEnumConstant(VariableElement arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitFloat(float arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitInt(int arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitLong(long arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitShort(short arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitString(String arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<DeclaredType> visitType(TypeMirror type, ProcessingEnvironment arg1) {
        List<DeclaredType> types = new ArrayList<>();
        types.add((DeclaredType)type);
        return types;
    }

    @Override
    public List<DeclaredType> visitUnknown(AnnotationValue arg0, ProcessingEnvironment arg1) {
        throw new UnsupportedOperationException();
    }
    
}
