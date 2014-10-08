##如何写出更好的Java代码

####编码风格

传统的编码方式是非常啰嗦的企业级JavaBean的风格。

程序员干的最简单的事情之一就是传递数据。传统的方式是定义一个JavaBean：

> 见TraditionalDataHolder

这不仅繁琐而且浪费。因此别这么做。相反可以使用类似C语言的结构体风格来编写只保存数据的类。

> 见CStructDataHolder

这就减少了一半的代码。不仅如此，这个类还是不可变的除非你继承它，因此我们可以更容易的推断它的值。

如果存储是Map或者List这些很容易改变的对象。可以用`ImmutableMap `或者`ImmutableList`代替。


####Builder模式

如果有一个很复杂的对象需要构建，可以考虑下Builder模式。

在对象里面创建一个子类，来构造这个对象。它是可以修改的状态，但只要调用build方法，它将会生成一个不可变对象。

> 见BuilderDataHolder

可以这样使用：

```
 BuilderDataHolder bdh = new BuilderDataHolder.Builder()
                .data("Hello world")
                .num(2)
                .build();
```

####依赖注入

依赖注入的好处不需多说。通常是用Sping框架来完成，也可以使用Dagger库或者Google的Guice。

####避免空值

如果有可能尽可能避免使用null值。不要返回null集合，可以返回空集合(如：`Collections.emptyList()`)来替代。
如果有使用Java8，可以考虑使用新的**`Optional`**类型。如果一个值可能存在也可能不存在，把它封装在**`Optional`**类里面。（大部分还没使用
到Java8，可以使用**Google guava**包的**`Optional`**类）。避免使用null的好处可以省去很多`if null `的判断，代码美观不少。



