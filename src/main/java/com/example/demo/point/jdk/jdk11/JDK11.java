package com.example.demo.point.jdk.jdk11;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * JDK11新特性记录
 * https://www.oracle.com/technetwork/java/javase/11-relnote-issues-5012449.html#toc
 *
 * https://blog.csdn.net/cun_chen/article/details/82807552
 * https://my.oschina.net/mdxlcj/blog/3010342
 * http://cr.openjdk.java.net/~iris/se/11/latestSpec/
 *
 * jdk.snmp模块提供了JVM SNMP监视和管理的实现。JVM的SNMP监视支持已在JDK 10（JDK-8194164）中弃用，并在JDK 11（JDK-8199295）中已删除。
 */
public class JDK11 {

    /**
     * 1、增加String实用的API
     */
    public void strTest() {
        String str = "woshidage";
        boolean isblank = str.isBlank();  //判断字符串是空白
        boolean isempty = str.isEmpty();  //判断字符串是否为空
        String  result1 = str.strip();    //首尾空白
        String  result2 = str.stripTrailing();  //去除尾部空白
        String  result3 = str.stripLeading();  //去除首部空白
        String  copyStr = str.repeat(2);  //复制几遍字符串
        long  lineCount = str.lines().count();  //行数统计

        System.out.println(isblank);
        System.out.println(isempty);
        System.out.println(result1);
        System.out.println(result2);
        System.out.println(result3);
        System.out.println(copyStr);
        System.out.println(lineCount);
    }


    /**
     * 2、移除和废弃的内容
     *
     * 3.1 移除项
     * 1、移除了com.sun.awt.AWTUtilities
     *
     * 2、移除了sun.misc.Unsafe.defineClass，使用java.lang.invoke.MethodHandles.Lookup.defineClass来替代
     *
     * 3、移除了Thread.destroy()以及 Thread.stop(Throwable)方法
     *
     * 4、移除了sun.nio.ch.disableSystemWideOverlappingFileLockCheck、sun.locale.formatasdefault属性
     *
     * 5、移除了jdk.snmp模块
     *
     * 6、移除了javafx，openjdk估计是从java10版本就移除了，oracle jdk10还尚未移除javafx，而java11版本则oracle的jdk版本也移除了javafx
     *
     * 7、移除了Java Mission Control，从JDK中移除之后，需要自己单独下载
     *
     * 8、移除了这些Root Certificates ：Baltimore Cybertrust Code Signing CA，SECOM ，AOL and Swisscom
     *
     * 3.2 废弃项
     * 1、-XX+AggressiveOpts选项
     *
     * 2、-XX:+UnlockCommercialFeatures
     *
     * 3、-XX:+LogCommercialFeatures选项也不再需要
     */

    /**
     * 3、HttpClient加强方法
     *
     * 现在 Java 自带了这个 HTTP Client API，我们以后还有必要用 HttpClient 或 OKHttp工具包吗？
     *
     */
    // 同步调用;
    public static void syncHttp() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://www.baidu.com")).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(request, handler);
        String body = response.body();
        System.out.println(body);
    }

    //异步调用;
    public static void asyncHttp() throws InterruptedException, ExecutionException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder(URI.create("http://www.baidu.com")).build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        CompletableFuture<HttpResponse<String>> response = client.sendAsync(request, handler);
        HttpResponse<String> result = response.get();
        String body = result.body();
        System.out.println(body);
    }

    /**
     * 4、删除JavaEE and CORBA 模块
     *
     * 在java11中移除了不太使用的JavaEE模块和CORBA技术，在java11中将java9标记废弃的Java EE及CORBA模块移除掉
     */


    /**
     * 5.废弃 Nashorn JavaScript Engine
     *
     * 废除Nashorn javascript引擎，在后续版本准备移除掉，有需要的可以考虑使用GraalVM。
     */


    /**
     * 6、废弃 Pack200 Tools and API
     *
     * 这个工具能对普通的jar文件进行高效压缩，据说如果jar包都是class类可以压缩到1/9的大小
     * 因为Pack200主要是用来压缩jar包的工具，由于网络下载速度的提升以及java9引入模块化系统之后不再依赖Pack200，因此这个版本将其移除掉。
     */


    /**
     * 7、新的Epsilon垃圾收集器
     *
     * JDK上对这个特性的描述是: 开发一个处理内存分配但不实现任何实际内存回收机制的GC, 一旦可用堆内存用完, JVM就会退出.
     * 用法 : -XX:+UnlockExperimentalVMOptions -XX:+UseEpsilonGC
     */


    /**
     * 8、ZGC
     *
     * 这应该是JDK11最为瞩目的特性, 没有之一. 但是后面带了Experimental, 说明这还不建议用到生产环境。
     *
     * 从JDK8开始，JDK使用G1作为默认的垃圾回收器。G1可以说是GC的一个里程碑，G1之前的GC回收，还是基于固定的内存区域，而G1采用了一种“细粒度”的内存管理策略，
     * 不在固定的区分内存区域属于surviors、eden、old，而我们不需要再去对于年轻代使用一种回收策略，老年代使用一种回收策略，取而代之的是一种整体的内存回收策略。
     * 这种回收策略在我们当下cpu、内存、服务规模都越来越大的情况下提供了更好的表现。
     * 从原理上来理解，ZGC可以看做是G1之上更细粒度的内存管理策略。
     * 与标记对象的传统算法相比，ZGC在指针上做标记，在访问指针时加入Load Barrier（读屏障），比如当对象正被GC移动，指针上的颜色就会不对，
     * 这个屏障就会先把指针更新为有效地址再返回，也就是，永远只有单个对象读取时有概率被减速，而不存在为了保持应用与GC一致而粗暴整体的Stop The World。
     *
     * GC暂停时间不会超过10ms，既能处理几百兆的小堆, 也能处理几个T的大堆(OMG)，和G1相比, 应用吞吐能力不会下降超过15%，为未来的GC功能和利用colord指针以及Load barriers优化奠定基础，初始只支持64位系统
     * ZGC的设计目标是：支持TB级内存容量，暂停时间低（<10ms），对整个程序吞吐量的影响小于15%。 将来还可以扩展实现机制，以支持不少令人兴奋的功能，例如多层堆（即热对象置于DRAM和冷对象置于NVMe闪存），或压缩堆。
     * GC是java主要优势之一，当GC停顿太长, 就会开始影响应用的响应时间.消除或者减少GC停顿时长, java将对更广泛的应用场景是一个更有吸引力的平台. 此外, 现代系统中可用内存不断增长,用户和程序员希望JVM能够以高效的方式充分利用这些内存, 并且无需长时间的GC暂停时间
     */


    /**
     * 9、支持G1上的并行完全垃圾收集
     *
     * 对于 G1 GC，相比于 JDK 8，升级到 JDK 11 即可免费享受到：并行的 Full GC，快速的 CardTable 扫描，自适应的堆占用比例调整（IHOP），在并发标记阶段的类型卸载等等。
     * 这些都是针对 G1 的不断增强，其中串行 Full GC 等甚至是曾经被广泛诟病的短板，你会发现 GC 配置和调优在 JDK11 中越来越方便。
     */
}
