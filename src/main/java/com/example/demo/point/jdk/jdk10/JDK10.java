package com.example.demo.point.jdk.jdk10;

/**
 * JDK10新特性记录
 *
 * https://baijiahao.baidu.com/s?id=1594437679552808575&wfr=spider&for=pc
 * https://www.oschina.net/translate/109-new-features-in-jdk-10
 *
 */
public class JDK10 {
    /**
     * 1. 局部变量类型推断;
     *
     * 局部变量类型推断将引入"var"关键字，也就是你可以随意定义变量而不必指定变量的类型;
     * 说到类型推断，从JDK5引进泛型，到JDK7的"<>"操作符允许不绑定类型而初始化List，再到JDK8的Lambda表达式，再到现在JDK10的局部变量类型推断，Java类型推断正大刀阔斧的向前发展。
     *
     */


    /**
     * 2. GC改进和内存管理；
     *
     * JDK10中有2个JEP专门用于改进当前的垃圾收集元素。
     *
     * 第一个垃圾收集器接口是（JEP 304），它将引入一个纯净的垃圾收集器接口，以帮助改进不同垃圾收集器的源代码隔离。
     * 预定用于Java10的第二个JEP是针对G1的并行完全GC（JEP 307），其重点在于通过完全GC并行来改善G1最坏情况的等待时间。
     * G1是Java9中的默认GC，并且此JEP的目标是使G1平行。
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
     */


    /**
     * 7. 根证书（JEP 319）
     *
     * 这个的目标是在Oracle的Java SE中开源根证书。
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
     */


    /**
     * 10. 删除工具javah（JEP 313）
     *
     * 从JDK中移除了javah工具，这个很简单并且很重要。
     *
     */
}
