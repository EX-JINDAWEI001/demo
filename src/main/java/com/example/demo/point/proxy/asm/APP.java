package com.example.demo.point.proxy.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.Type;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

/**
 * https://asm.ow2.io/javadoc/
 * https://www.jianshu.com/p/905be2a9a700
 * https://blog.csdn.net/lijingyao8206/article/details/48085823
 */
public class APP {
    public static void createField() throws IOException {
        byte[] bytes = FileUtil.File2Byte();

        // 读取 byte 数组
        ClassReader cr = new ClassReader(bytes);

        // 创建一个树节点
        ClassNode cn = new ClassNode();

        // 将 cr 转换好的字节码放进 cn 树中
        // SKIP_DEBUG 跳过类文件中的调试信息，比如行号信息（LineNumberTable）等
        // SKIP_CODE：跳过方法体中的 Code 属性（方法字节码、异常表等）
        cr.accept(cn, ClassReader.EXPAND_FRAMES);

        for (FieldNode fn : cn.fields) {
            if (fn.name.equals("defaultCharWidth")) {
                fn.access = Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC;
//                cn.fields.remove(fn);
//                cn.fields.add(new FieldNode(Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC,"defaultCharWidth","I",null, new Integer(5)));
                break;
            }
        }


        /*for (MethodNode mn : cn.methods) {
            if ("<init>".equals(mn.name)) {
                cn.methods.remove(mn);
                cn.methods.add(new MethodNode(327680, Opcodes.ACC_PUBLIC, "<init>", "(Lorg/apache/poi/ss/usermodel/Sheet;)V", null, null));
                break;
            }
        }*/



        for (MethodNode mn : cn.methods) {
            if ("<init>".equals(mn.name)) {
                System.out.println(mn.name);
                InsnList insns = mn.instructions;
                System.out.println(insns);



                Iterator<AbstractInsnNode> it = insns.iterator();
                AbstractInsnNode ain = null;
                while (it.hasNext()) {
                    AbstractInsnNode in = it.next();

                    if (in instanceof MethodInsnNode) {
                        if (((MethodInsnNode) in).name.equals("getDefaultCharWidth") || ((MethodInsnNode) in).name.equals("getWorkbook")) {
                            it.remove();
                        }
                    }

                    if (in instanceof FieldInsnNode) {
                        if (((FieldInsnNode) in).name.equals("defaultCharWidth")) {
                            it.remove();
                        }
                    }

                    int op = in.getOpcode();
                    if ((op >= Opcodes.IRETURN && op <= Opcodes.RETURN) || op == Opcodes.ATHROW) {
                        // 添加新的方法指令;
                        InsnList il = new InsnList();
                        il.add(new LdcInsnNode(5));
                        il.add(new FieldInsnNode(Opcodes.PUTSTATIC, cn.name, "defaultCharWidth", "I"));
                        insns.insert(in.getPrevious(), il);
                    }
                }



                break;
            }
        }

        // new ClassWriter(0)：表示我们手动设置。（这里我们用不到）
        ClassWriter cw = new ClassWriter(0);

        // 将新生成的 'class' 放进 cw 中
        cn.accept(cw);

        // cw 将新的 'class' 处理成byte覆盖之前原始的class文件
        FileUtil.Byte2File(cw.toByteArray());
    }

    public static void main(String[] args) throws IOException {
        createField();
        AutoSizeColumnTracker ma = new AutoSizeColumnTracker(null);
    }
}
