package com.example.demo.point.pattern.behavioral;

// 解释器模式(Interpreter Pattern);
// 一句话: 有一批问题, 每个问题的处理都不同, 要求可以不分顺序, 而且必须全部处理, 则我们可以放到集合里遍历执行, 根据上下文入参与接收响应结果;
// 关键词: 全部遍历执行;

// 角色:
// 环境(Context):                        通常包含各个解释器需要的数据或是公共的功能, 一般用来传递被所有解释器共享的数据, 后面的解释器可以从这里获取这些值;
// 抽象表达式(AbstractExpression):        定义解释器的接口, 约定解释器的解释操作, 主要包含解释方法interpret();
// 终结符表达式(TerminalExpression):       是抽象表达式的子类, 用来实现文法中与终结符相关的操作, 文法中的每一个终结符都有一个具体终结表达式与之相对应;
// 非终结符表达式(NonterminalExpression):  也是抽象表达式的子类, 用来实现文法中与非终结符相关的操作, 文法中的每条规则都对应于一个非终结符表达式;

import java.util.ArrayList;
import java.util.List;

public class InterpreterPattern {
    // 用法;
    public static void usage() {
        List<AbstractExpression> aes = new ArrayList<>();
        Context context = new Context();
        aes.add(new TerminalExpression());
        aes.add(new NonterminalExpression());
        aes.forEach(ae -> {
            ae.interpret(context);
        });
    }

    // (Context)环境;
    static class Context {
    }

    // (AbstractExpression)抽象表达式;
    interface AbstractExpression {
        // 解释;
        void interpret(Context context);
    }

    // (TerminalExpression)终结符表达式;
    static class TerminalExpression implements AbstractExpression {
        // 解释;
        public void interpret(Context context) {
            System.out.println("TerminalExpression.interpret().");
        }
    }

    // (NonterminalExpression)非终结符表达式;
    static class NonterminalExpression implements AbstractExpression {
        // 解释;
        public void interpret(Context context) {
            System.out.println("NonterminalExpression.interpret().");
        }
    }
}
