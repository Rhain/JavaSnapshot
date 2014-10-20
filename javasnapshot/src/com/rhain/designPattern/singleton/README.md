##单例模式

###动机
有时候一个类只有一个实例是很重要的。例如：在一个系统里面只应该存在一个窗口管理器。单例经常是用来对内部或者外部资源的集中管理，并且提供一个
全局访问点。

单例模式是最简单的模式之一：它只调用实例化它自己的类，确保不会创建多个实例。同时提供了一个全局的访问对象。在这种情况下相同的实例可以到处
使用，每次都不会直接调用构造函数。

###意图

>  确保只创建一个类的实例。
>  提供一个全局的访问对象。

###实现

```` 
    class Singleton{
        private static Singleton instance;
        private Singleton(){
        }
        public static synchronized Singleton getInstance(){
            if(instance == null){
                instance = new Singleton();
            }
            return instance;
        }
    }

````

###应用/例子

根据单例的定义，单例应该使用在只允许一个类实例的地方，并且需要可以通过一个全局访问接口来使用。下面是一些使用单例的情况：

####日志类

####配置类

####共享资源访问

####单例工厂

###特殊情况

####对多线程使用的线程安全实现

一个健壮的单例实现应该可以在各种情况下使用。这就是为什么我们应该确保它也能在多线程的环境下使用。

####使用双锁机制来延迟实例化

上面例子的实现是线程安全的，但是不是最好的线程安全实现因为当我们谈到性能的时候锁的代价是很高的。我们可以看到在对象被实例化后锁住的
getInstance方法是不需要再检查是否有锁的。如果我们看到单例对象已经创建了我们只需要返回它就可以了而不需要使用锁住任何东西。在非同步块中检
查对象是否为空，如果是空对象则在一个同步块中再检查一次同时创建它。这就是所谓的双锁机制。

在这种情况下单例对象在getInstance方法第一调用的时候创建。这就是所谓的延迟实例化，并且确保了单例只有在需要的时候才创建。

> 代码见LazySingleton

####提前实例化

下面的单例对象初始化是在类加载的时候而不是它第一次使用的时候。因为实例成员是静态的。这也是我们不需要同步一部分代码。这个类一旦加载就保证了
这个对象的唯一性。

> 代码见EarlySingleton


###如果单例类是由不同的ClassLoader加载的将会有多个单例对象

如果一个类（相同的名字，相同的包）被两个不同的**ClassLoader**加载的话，在内存中他们表示2个不同的类。

###实例化

如果单例类实现了`java.io.Serializable` 接口，当一个单例序列化后被反序列化多次，那么会创建多个实例对象。为了避免这种情况应该实现
readResolve方法。> 参见**javadocs** 的`Serializable()` 和 `readResolve（）` 方法


###小结

- 多线程-当单例必须使用多线程的应用中是需要特别注意
- 序列化-当单例实现了`Serializbale`接口时需要实现`readResolve`方法以避免会有两个不同的单例对象。
- **ClassLoader**-如果单例类被两个不同的ClassLoader加载我们会有两个不同的单例类，分别属于各自的**ClassLoader**
- 通过类名称的全局访问-通过类名称来获取单例实例。初看起来这是一种很简单的访问方式，但是却不灵活。如果我们需要替换单例，代码中所有的引用
都需要相应的改变。
