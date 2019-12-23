package com.example.demo.point.jdk.jdk10;

import java.util.*;
import java.util.stream.Collectors;

/**
 * JDK10新特性记录
 * https://www.oracle.com/technetwork/java/javase/10-relnote-issues-4108729.html
 *
 * https://www.oschina.net/translate/109-new-features-in-jdk-10
 * https://www.jianshu.com/p/2a762bb4e140?from=timeline
 *
 */
public class JDK10 {
    /**
     * https://blog.csdn.net/weixin_34117522/article/details/91386815
     *
     * 1. 局部变量类型推断;
     *
     * 局部变量类型推断将引入"var"关键字，也就是你可以随意定义变量而不必指定变量的类型;
     * 说到类型推断，从JDK5引进泛型，到JDK7的"<>"操作符允许不绑定类型而初始化List，再到JDK8的Lambda表达式，再到现在JDK10的局部变量类型推断，Java类型推断正大刀阔斧的向前发展。
     *
     * 局部变量类型推荐仅限于如下使用场景：
     * 1)局部变量初始化
     * 2)for循环内部索引变量
     * 3)传统的for循环声明变量
     *
     * Java官方表示，它不能用于以下几个地方：
     * 1)方法参数
     * 2)构造函数参数
     * 3)方法返回类型
     * 4)字段
     * 5)捕获表达式（或任何其他类型的变量声明）
     */
    public static void main(String[] args) {
        // 1)局部变量初始化;
        var list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);
        list.add(3);
        System.out.println(list);

        // 2)for循环内部索引变量;
        list.forEach(v ->{
            var a = 100;
            System.out.println(a + v);
        });

        // 3)传统的for循环声明变量;
        for (var a : list) {
            var b = a * 100;
            System.out.println(b);
        }
    }

    /*private var a;

    public static var plus(var a, var b) {
        return a + b;
    }*/



    /**
     * 2. GC改进和内存管理;
     *
     * JDK10中有2个JEP专门用于改进当前的垃圾收集元素。
     *
     * 第一个垃圾收集器接口是（JEP 304），它将引入一个纯净的垃圾收集器接口，以帮助改进不同垃圾收集器的源代码隔离。
     * 预定用于Java10的第二个JEP是针对G1的并行Full GC（JEP 307），其重点在于通过Full GC并行来改善G1最坏情况的等待时间。
     * G1是Java9中的默认GC，当前实现使用单线程的mark-sweep-compact算法，优化为并行化mark-sweep-compact算法。
     *
     */


    /**
     * 3. 线程本地握手（JEP 312）
     *
     * JDK10将引入一种在线程上执行回调的新方法，因此这将会很方便能停止单个线程而不是停止全部线程或者一个都不停。
     *
     */


    /**
     * 4. 备用内存设备上的堆分配（JEP 316）
     *
     * 允许HotSpotVM在备用内存设备上分配Java对象堆内存，该内存设备将由用户指定。
     *
     * 硬件技术在持续进化，现在可以使用与传统 DRAM 具有相同接口和类似性能特点的非易失性 RAM 。
     * 这项 JEP 将使得 JVM 能够使用适用于不同类型的存储机制的堆。
     *
     */


    /**
     * 5. 其他Unicode语言 - 标记扩展（JEP 314）
     *
     * 目标是增强java.util.Locale及其相关的API，以便实现语言标记语法的其他Unicode扩展（BCP 47）。
     *
     */


    /**
     * 6. 基于Java的实验性JIT编译器
     *
     * Oracle希望将其Java JIT编译器Graal用作Linux/x64平台上的实验性JIT编译器。
     *
     * 这项 JEP 将 Graal 编译器研究项目引入到 JDK 中。并给将 Metropolis 项目成为现实，使 JVM 性能与当前 C++ 所写版本匹敌（或有幸超越）提供基础。
     *
     */


    /**
     * 7. 根证书（JEP 319）
     *
     * 这个的目标是在Oracle的Java SE中开源根证书。
     * 它还旨在减少OpenJDK和Oracle JDK构建之间的差异。
     *
     * OpenJDK 中的 cacerts 密钥库在相当长一段时间内是空的，这将导致未指定 javax.net.ssl.trustStore 属性的情况下 TLS 连接 的创建会被阻止。
     * 现在Oracle 的 Java SE 根证书 被填充至 OpenJDK 的 cacerts 中。
     * 在 JDK 中将提供一套默认的 CA 根证书。关键的安全部件，如 TLS ，在 OpenJDK 构建中将默认有效。
     * 这是 Oracle 正在努力确保 OpenJDK 二进制和 Oracle JDK 二进制功能上一样的工作的一部分，是一项有用的补充内容。
     *
     */


    /**
     * 8. 根证书颁发认证
     *
     * 这将使OpenJDK对开发人员更具吸引力，它还旨在减少OpenJDK和Oracle JDK构建之间的差异。
     *
     */


    /**
     * 9. 将JDK生态整合单个存储库（JEP 296）
     *
     * 此JEP的主要目标是执行一些内存管理，并将JDK生态的众多存储库组合到一个存储库中。
     *
     * 在 JDK9 中有 8 个仓库： root、corba、hotspot、jaxp、jaxws、jdk、langtools 和 nashorn 。
     * 在 JDK10 中这些将被合并为一个，使得跨相互依赖的变更集的存储库运行 atomic commit （原子提交）成为可能。
     *
     */


    /**
     * 10. 删除工具javah（JEP 313）
     *
     * 从JDK中移除了javah工具，这个很简单并且很重要。
     * Java9 开始了一些对 JDK 的家务管理，这项特性是对它的延续。
     * 当编译 JNI 代码时，已不再需要单独的工具来生成头文件，因为这可以通过 javac -h 完成。
     *
     */


    /**
     * 11. 应用程序类数据 (AppCDS) 共享
     *
     * 通过跨进程共享通用类元数据来减少内存占用空间，和减少启动时间。
     * 其原理为：在启动时记录加载类的过程，写入到文本文件中，再次启动时直接读取此启动文本并加载。设想如果应用环境没有大的变化，启动速度就会得到提升。
     */


    /**
     * 12. 字节码生成以增强for循环
     *
     * 字节码生成已得到改进，以增强for循环，从而改进了它们的转换方法。
     *
     * 在for循环之外声明iterator变量,允许在不再使用它时立即为其分配空值。
     * 这使得GC可以访问它，然后可以清除未使用的内存。
     */
    public void classEnhance() {
        List<String> data = new ArrayList<>(); for (String b : data);
        // 以下是增强后生成的代码：
         {
            Iterator i$ = data.iterator();
            for (; i$.hasNext(); ) {
                String b = (String)i$.next();
            }
            i$ = null;
         }
    }


    /**
     * 13.Optional.orElseThrow（）方法
     *
     * Optional.get() 是一个容易误导程序员的方法，它实际上有可能抛出 NoSuchElementException 运行时异常，
     * 因此需要一个语义明确的方法来帮助程序员清楚的认识到自己在做什么。
     *
     * 此次更新中为 Optional 类添加了一个新的方法orElseThrow用来代替get
     */


    /**
     * 14.不可变集合
     */
    public static void unmodifiable() {
        // JDK9 不可变集合;
        List<Integer> list = List.of(1, 2, 3);
        System.out.println(list);
        // java.lang.UnsupportedOperationException
        list.add(4);

        // JDK9
        List<Integer> result = Arrays.asList(1, 2, 3, 4)
                .stream()
                .collect(Collectors.collectingAndThen(
                        Collectors.toList(),
                        x -> Collections.unmodifiableList(x)));

        // JDK10
        List<Integer> result1 = Arrays.asList(1, 2, 3, 4)
                .stream()
                .collect(Collectors.toUnmodifiableList());


        // 可变集合
        List<Integer> list1 = new ArrayList<>();
        list1.add(1);
        list1.add(2);
        list1.add(3);

        // JDK10
        List<Integer> list2 = List.copyOf(list1);
        System.out.println(list2);
        // java.lang.UnsupportedOperationException
        list2.add(4);

        // JDK9之前
        list1 = Collections.unmodifiableList(list1);
        // java.lang.UnsupportedOperationException
        list1.add(1);
        System.out.println(list1);
    }
}
