
###介绍

最好的解释注解的是词是metadata（元数据）：包含它自身信息的数据。注解是代码的metadata,包含了代码自身的信息。

被注解的代码不会被注解直接影响。它们只是提供信息给可能使用（也可能不使用）注解的第三方系统。

可能很难理解注解真正的用途是什么，它们不包含任何的代码逻辑并且并不影响注解的代码，那有什么用呢？真正的作用是，那些使用了注解
的系统或者应用依赖注解信息去执行不同的动作。例如：Java自带的注解的使用者是Java虚拟机。Junit依赖注解来判断哪些单元测试将会被
执行，哪些会在每次测试之前和测试之后执行。使用者使用反射来读取和分析注解代码。

注解是通过'@'字符来表示的，这就告诉编译器这个元素是一个注解。例如：

```
@Annotation
public void annotatedMethod(){
    //todo
}
```
上面例子的注解叫做"Annotation"，注解在annotatedMethod方法上

一个注解有key-values格式的元素，这些“元素”是注解的属性。
```
@Annotation(
    info = "I am an annotaion",
    counter= "55"
)
public void annotatedMethod(){
    //todo
}
```

如果注解只有一个元素（或者只有一个元素需要被明确说明，其他的都有默认值），我们可以像这样：
```
@Annotation("I am an annotation")
public void annotatedMethod(){
    //todo
}
```
如果没有元素需要指定，那么圆括号也是可以省略的。

可以同时使用多个注解，例如：
```
@Annotaion(info= "Love")
@Annotaion2
class AnnotatedClass{...}
```
有些是Java语言自带的注解，这些称之为内置注解。同样我们可以自己定义我们自己的注解，我们称之为定制注解。


在一个Java程序中注解基本上可以到处使用：类，属性，方法，包，变量等等。
Java8同样可以在类型声明前使用注解，这在Java8之前是不允许的，如：
```
@MyAnnotation String str = "YiFei"
```

使用注解有各种目的来，最普遍的有以下几种：
 - **给编译器提供信息**：编译器可以使用注解基于不能的规则来产生警告甚至错误。Java8的`@FunctionalInterface`注解，就可以让编译器
 来区分被注解的是类是不是正确的函数式接口。
 - **文档**：软件应用使用注解可以来测试代码的质量，像FindBugs或者PMD.或者自动产生报告像Jenkins,Jira和Teamcity。
 - **代码生成**：基于注解提供的元数据信息你可以用于自动生成代码或者xml文件。像JAXB库。
 - **运行时处理**：在运行时检测注解可以有各种用途像单元测试(Junit)，依赖注入(Spring),校验，记录日志(Log4J)，数据处理(Hibernate)等等。
 
Java语言自带了一些注解。下面会解释一些最重要的。需要注意这里只是说明了部分注解，还有很多注解并未说明。 

一些的标准注解被称之为元注解：他们的目标是其他的注解并包含它们的信息。

  - `@Retention` :这个注解用于指定如何保存被标记的注解。这是一种元注解。可能的值有：
        -`SOURCE`:表示标记的注解将会被编译器和JVM忽略（在运行时不可用），只在源码中保留。
        -`CLASS`:表示标记的注解将会在编译器中保留但是会被JVM忽略，因此在运行时是不可用的。
        -`RUNTIME`:表示标记的注解将会被JVM保留并且可以在运行时通过反射来使用。
  
  - `@Target`:这个注解约束一个注解可以应用到的元素范围。可以只是一种类型，下面是所有的可用类型：
        -`ANNOTATION_TYPE`:意味着这个注解用到其他注解上
        -`CONSTRUCTOR`:可以用到构造函数上
        -`FIELD`:可以用到域或者属性上
        -`LOCAL_VARIABLE`:可以使用在本地变量上
        -`METHOD`:可以使用到方法上
        -`PACKAGE`:可以使用到包声明上
        -`PARAMETER`:可以使用到方法的参数上
        -`TYPE`:可以使用到任何类上面
        
  - `@Documented`:这个注解的元素将会被Javadoc tool文档化。每个注解默认都不是可以文档化的。这个注解可以用到其他的注解上。
  - `@Inherited`:默认情况下注解是不会被子类继承的。这个注解自动让继承了注解类的子类都可以继承。这个注解可以使用到类上。
  - `@Deprecated`:指明被注解的元素不应该在被使用了。这个注解会让编译器产生一个警告信息。可以使用到方法，类，域上。当使用这个注解
  的时候应该描述废弃或者替换的原因。
  - `@SuppressWarnings`:告诉编译器不要因为什么原因或者各种原因而产生警告。例如如果我们不想因为有没有使用到的私有方法而产生警告，
  我们可以这样：
  ```
  @SuppressWarnings("unsed")
  private String notUserdMethod(){
    //todo
  }
  ```
  - `@Override`:告诉编译器被注解的元素是重写了父类的。
  - `@SafeVarargs`:维护方法或者构造函数不会在它的参数上执行危险操作。
  
  Java8引入了一些新的注解。
  
  - `@Repeatable`：被这个注解标注的注解可以在同一个元素上重复使用多次
  如下：首先创建一个可以重复是使用的注解容器
  ```
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE_USE)
  public @interface RepeatedValues{
    CanBeRepeated[] values();
  }
  ```
  然后再创建注解并标记元注解@Repeatable:
  ```
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.TYPE_USE)
  @Repeatable(RepeatedValues.class)
  public @interface CanBeRepeated
  {
    String value();
  }
  ```
  最后，在类上使用：
  ```
  @CanBeRepeated("Cat")
  @CanBeRepeated("Dog")
  public class RepeatableAnnotated{
  }
  ```
  如果我们这样使用一个不能重复使用的注解，编译器将会抛个错误出来：`Duplicate annotation of no-repeatable type`
  
  Java8允许在类型前面使用注解。你可以在类型，new操作符，强制转换，throws语句使用。
  ```
  public static void main(String[] args){
    //type def
    @TypeAnnotated
    String cannoteBeEmpty = null;
    //type
    List<@TypeAnnotated String> myList = new ArrayList<>();
    //values
    String myString = new @TypeAnnotated String("this is annotated in Java 8");
  }
  //in method params
  public void methodAnnotated(@TypeAnnotated int parameter){
    //todo
  }
  ```
  这些在Java8 之前都是不行的
  
  - `@FunctionalInterface`:这个注解表示被注解的元素是一个函数式接口。函数式接口是只有一个抽象方法的（不是默认方法）的接口。
  编译器将会把被注解的元素当做函数式接口来处理，如果不符合条件则会报错。看下面这个例子：
  ```
  @FunctionalInterface
  interface MyCustomInterface{
    //更多的抽象方法将使这个接口不在是一个真正的函数式接口，编译器将会抛一个错误：Invalid '@FunctionalInterface' annotation;
    int doSomething(int param);
  }
  
  //实现
  MyCustomInterface myFuncInterface = new MyCustomInterface(){
    
    @override
    public int doSomeThing(int param){
        return param * 10;
    }
  };
  
  //使用lambdas
  MyCustomInterface myFuncInterfaceLambdas = (x) -> ( x*10 );
  ```
  这个注解可以应用到类，接口，枚举，和其他注解上，并且是被JVM保存在运行时可用的。
  
  
###自定义注解

首先，定义一个新注解：
`public @interface CustomAnnotationClass`
这会定义一个新的注解CustomAnnotationClass。@interface关键字是用来定义注解的。

然后，你需要给这个注解定义必要的属性：retention policy 和 target。还有其他的属性可以定义，但是上面的是最普遍和最重要的两个。
我们可以这样定义我们新注解的属性：
```
    @Retention( RetentionPolicy.RUNTIME )
    @Target( ElementType.TYPE )
    public @interface CustomAnnotationClass 
```

我们在定义这个注解的一些属性：
```
    @Retention( RetentionPolicy.RUNTIME )
    @Target( ElementType.TYPE )
    public @interface CustomAnnotationClass
    {
    
        public String author() default "hao";
    
        public String date();
    }
```
上面我们定义了一个author属性携带默认值 hao和一个date属性没有默认值。我们应该注意到所有的方法声明都不能有参数，并且不能有
throw语句。return的类型严格限制为String，Class，enums，anotations和类型数组。

现在，我们可以使用我们新创建的注解：
```
    @CustomAnnotationClass( date = "2014-05-05" )
    public class AnnotatedClass
    { 
    ...
    }
```

同样我们也可以定义一个使用在方法上的注解，使用target的值为`METHOD`
```
    @Retention( RetentionPolicy.RUNTIME )
    @Target( ElementType.METHOD )
    public @interface CustomAnnotationMethod
    {
        
        public String author() default "danibuiza";
    
        public String date();
    
        public String description();
    
    }
```
可以这么使用：
```
    @CustomAnnotationMethod( date = "2014-06-05", description = "annotated method" )
    public String annotatedMethod()
        {
            return "nothing";
    }
```

###获取注解
java反射api提供了一些方法可以在运行时从类，方法，其他元素获取注解信息。
最重要的几个api是：
 - `getAnnotations()` :返回元素上的所有注解。
 - `isAnnotationPresent(annotation)` ：检测传递的注解在当前的元素是否可用
 - `getAnnotation(class)`:获取指定的注解。如果不存在则返回null
 
看个例子：
   > 见Main.java

###注解的继承
注解的继承跟java对象的继承几乎没有共同之处
 > 见InheritedMain
 
@Inherited只是适用于类上，对实现接口的类确没有用。注解不能从其他注解继承。

###使用注解的框架

####Junit
Junit基本上是processor读取反射后的类，并依靠每个方法或者类标注的注解来执行。
像：
 - `@Test`:指明被注解的方法必须是要被执行的单元测试。只适用于方法，并且在运行时是可用的。
 ```
 @Test
 public void testMe(){
    assertEquals(1,1);
 }
 ```
 - `@Before`:指明被注解的方法需要在测试前先执行，这对于建立测试环境和初始化很有用，只能用在方法上。
 ```
 @Before
 public void setUp(){
    init(); 
 }
 ```
 
###Spring MVC
Spring 使用注解来替代基于xml的配置。

像：
    - `@Component`:指明被注解的对象是一个bean，需要被spring容器管理
    - `@Autowired`:自动注入


 > 原文[Java Annotations Tutorial ](http://www.javacodegeeks.com/2014/11/java-annotations-tutorial.html)