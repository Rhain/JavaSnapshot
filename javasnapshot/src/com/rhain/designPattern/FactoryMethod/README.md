##工厂方法模式

###动机

定义一个创建对象的接口，但是把对象的类型留给子类在运行时去决定。

当你设计一个应用时想想你是否真的需要一个工厂来生成对象。也许在您的应用程序使用它会带来不必要的复杂性。但是如果你有许多相同基本类型的对象
并且你已抽象类型来维护它们，那么你就需要一个工厂。如果你的代码有许多像下面的代码，那么可以考虑使用：

```
if(genericProduct typeof ConcreteProduct)
    (ConcreteProduct(genericProduct)).doSomething();
```