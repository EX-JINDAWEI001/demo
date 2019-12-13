package com.example.demo.point.proxy.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * https://asm.ow2.io/javadoc/
 * https://www.jianshu.com/p/905be2a9a700
 * https://blog.csdn.net/lijingyao8206/article/details/48085823
 */
public class ASMTool {
    public static byte[] modifyClassFile(InputStream is, int defaultCharWidth) {
        try {
            byte[] bytes = new byte[is.available()];
            is.read(bytes);

            // 读取byte数组
            ClassReader cr = new ClassReader(bytes);

            // 创建一个树节点
            ClassNode cn = new ClassNode();

            // 将cr转换好的字节码放进cn树中
            // SKIP_DEBUG 跳过类文件中的调试信息，比如行号信息（LineNumberTable）等
            // SKIP_CODE：跳过方法体中的 Code 属性（方法字节码、异常表等）
            cr.accept(cn, ClassReader.EXPAND_FRAMES);

            for (FieldNode fn : cn.fields) {
                if (fn.name.equals("defaultCharWidth")) {
                    fn.access = Opcodes.ACC_PRIVATE + Opcodes.ACC_FINAL + Opcodes.ACC_STATIC;
                    break;
                }
            }

            for (MethodNode mn : cn.methods) {
                if ("<init>".equals(mn.name)) {
                    InsnList insns = mn.instructions;
                    Iterator<AbstractInsnNode> it = insns.iterator();
                    while (it.hasNext()) {
                        AbstractInsnNode in = it.next();
                        // 删除原有构造方法内的指令集;
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
                            il.add(new LdcInsnNode(defaultCharWidth));
                            il.add(new FieldInsnNode(Opcodes.PUTSTATIC, cn.name, "defaultCharWidth", "I"));
                            insns.insert(in.getPrevious(), il);
                        }
                    }
                    break;
                }
            }

            // new ClassWriter(0)：表示我们手动设置。
            ClassWriter cw = new ClassWriter(0);

            // 将新生成的 class 放进 cw 中
            cn.accept(cw);

            // cw 将新的 class 处理成byte覆盖之前原始的class文件
            return cw.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
