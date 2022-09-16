package org.example.agent.template;

import org.objectweb.asm.*;
import org.objectweb.asm.commons.LocalVariablesSorter;

import static org.example.agent.template.Config.ASM_Version;
import static org.objectweb.asm.Opcodes.*;

public class TemplateClassVisitor extends ClassVisitor {
    private String currentSlashClassName;
    public TemplateClassVisitor(ClassVisitor cv, String className) {
        super(ASM_Version, cv);
        currentSlashClassName = className;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions){
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
        // exclude <init> and <clinit>
        if (name.equals("<init>") || name.equals("<clinit>")){
            return mv;
        }
        return new TemplateMethodVisitor(mv, currentSlashClassName, name, descriptor, access);
    }
}

class TemplateMethodVisitor extends MethodVisitor {
    private String currentSlashClassName;
    private String currentMethodName;
    private String currentMethodDesc;

    public TemplateMethodVisitor(MethodVisitor mv, String currentClassName, String methodName, String desc, int access) {
        super(ASM_Version, mv);
        this.currentSlashClassName = currentClassName;
        this.currentMethodName = methodName;
        this.currentMethodDesc = desc;
    }

    @Override
    public void visitCode() {
        mv.visitCode();
    }
}
