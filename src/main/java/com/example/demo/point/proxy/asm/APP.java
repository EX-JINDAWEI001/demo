package com.example.demo.point.proxy.asm;

import jdk.internal.org.objectweb.asm.ClassReader;
import jdk.internal.org.objectweb.asm.ClassWriter;
import jdk.internal.org.objectweb.asm.Opcodes;
import jdk.internal.org.objectweb.asm.tree.*;

import java.io.IOException;
import java.util.Iterator;

/**
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

        // 新建一个属性
        // 参数作用域，参数名称，参数类型，参数签名，参数初始值
        FieldNode fn = new FieldNode(Opcodes.ACC_PRIVATE, "code", "Ljava/lang/String;", null, 5);
        // 将属性添加到 cn 参数节点中
        cn.fields.add(fn);

        // 新建一个方法
//        MethodNode mn = new MethodNode(327680, Opcodes.ACC_PUBLIC, "<init>", "(Lorg/apache/poi/ss/usermodel/Sheet;)V", null, null);
        // 将方法添加到 cn 参数节点中
//        cn.methods.add(mn);

        for (MethodNode mn : cn.methods) {
            if ("<init>".equals(mn.name)) {
                System.out.println(mn.name);
                InsnList insns = mn.instructions;
                System.out.println(insns);



                Iterator<AbstractInsnNode> j = insns.iterator();
                while (j.hasNext()) {
                    AbstractInsnNode in = j.next();
                    int op = in.getOpcode();
                    if ((op >= Opcodes.IRETURN && op <= Opcodes.RETURN) || op == Opcodes.ATHROW) {
                        InsnList il = new InsnList();
                        il.add(new FieldInsnNode(Opcodes.GETSTATIC, cn.name, "timer", "J"));
                        il.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J",
                                false));
                        il.add(new InsnNode(Opcodes.LADD));
                        il.add(new FieldInsnNode(Opcodes.PUTSTATIC, cn.name, "timer", "J"));
                        insns.insert(in.getPrevious(), il);
                    }
                }
                InsnList il = new InsnList();
                il.add(new FieldInsnNode(Opcodes.GETSTATIC, cn.name, "timer", "J"));
                il.add(new MethodInsnNode(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false));
                il.add(new InsnNode(Opcodes.LSUB));
                il.add(new FieldInsnNode(Opcodes.PUTSTATIC, cn.name, "timer", "J"));
                insns.insert(il);
                mn.maxStack += 4;



                break;
            }
        }
        int acc = Opcodes.ACC_PUBLIC + Opcodes.ACC_STATIC;
        cn.fields.add(new FieldNode(acc, "timer", "J", null, null));

        // 再 JVM 存在，数栈和局部变量表
        // new ClassWriter(0)：表示我们手动设置。（这里我们用不到）
        ClassWriter cw = new ClassWriter(0);

        // 将新生成的 'class' 放进 cw 中
        cn.accept(cw);

        // cw 将新的 'class' 处理成byte覆盖之前原始的class文件
        FileUtil.Byte2File(cw.toByteArray());
    }

    public static void main(String[] args) throws IOException {
        createField();
//        AutoSizeColumnTracker ma = new AutoSizeColumnTracker(null);
    }
}
