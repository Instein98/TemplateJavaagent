package org.example.agent.template;

import com.google.common.collect.Sets;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Set;

public class TemplateTransformer implements ClassFileTransformer {

    private String transformerName = "TemplateTransformer";

    Set<String> PREFIX_BLACK_LIST = Sets.newHashSet(
            "org/example",
            "java",
            "sun",
            "com/sun",
            "javax",
            "jdk"
    );
    private Set<String> PREFIX_WHITE_LIST = Premain.PREFIX_WHITE_LIST;

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws
            IllegalClassFormatException {
        byte[] result = classfileBuffer;

        if (className == null){
            return result;
        }

        if (PREFIX_WHITE_LIST == null || PREFIX_WHITE_LIST.size() == 0) {
            for (String prefix: PREFIX_BLACK_LIST){
                if (className.startsWith(prefix)){
                    return result;
                }
            }
        } else {
            boolean matched = false;
            for (String prefix: PREFIX_WHITE_LIST){
                if (className.startsWith(prefix)){
                    matched = true;
                    break;
                }
            }
            if (!matched){
                return result;
            }
        }


        try{
            ClassReader cr = new ClassReader(result);
            ClassWriter cw = new ClassWriter(cr, 0);
            ClassVisitor cv = new TemplateClassVisitor(cw, className);

            cr.accept(cv, ClassReader.EXPAND_FRAMES);

            result = cw.toByteArray();

//            FileUtils.write(Config.workingDirectory() + "/" + transformerName + "/"
//                    + className.replace('/', '.') + ".class", result);

        } catch (Throwable t){
            t.printStackTrace();
        }

        return result;
    }
}
