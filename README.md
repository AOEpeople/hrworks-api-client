# HRWorks-Api-Client

This client is written Kotlin but can be used in all JVM Languages. Currently it is
publish on jitpack.io and can be used by simply adding the dependency to your project.

Use it at own Risk

If you see something that you don't like get in touch

and most of all have fun


## Used Technologies

[gradle](https://github.com/gradle/gradle)  
[kotlin](https://github.com/JetBrains/kotlin)  
[RxKotlin](https://github.com/ReactiveX/RxKotlin)  
[retrofit](https://square.github.io/retrofit/)    
[Retrofit RxJava2 Adapter](https://github.com/square/retrofit/tree/master/retrofit-adapters/rxjava2)  
[Retrofit Gson Converter](https://github.com/square/retrofit/tree/master/retrofit-converters/gson)  
[OkHttp](http://square.github.io/okhttp/)

## Gradle

Add the jitpack.io repo to the project repos
``` groovy
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

and then adding add the implementation dependency
``` groovy
dependencies {
    ...
    implementation 'com.github.AOEpeople:hrworks-api-client:master'
    ...
}
```

## Maven
Add the jitpack.io repo to the project repos
``` xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```
and then adding add the implementation dependency

``` xml
<dependency>
    <groupId>com.github.AOEpeople</groupId>
    <artifactId>hrworks-api-client</artifactId>
    <version>master</version>
</dependency>
```
## Usage in Kotlin
``` kotlin
import com.aoe.hrworks.HrWorksClientBuilder

fun main(args: Array<String>) {
    HrWorksClientBuilder.buildClient(
        apiKey = "key",
        apiSecret = "secret")
        .getAllActivePersons()
        .blockingGet().apply {
            println(this)
        }
}
```

## Usage in Java
```java
import com.aoe.hrworks.HrWorksClient;
import com.aoe.hrworks.HrWorksClientBuilder;

public class Sample {

    public static void main(String[] args){
        HrWorksClient client = HrWorksClientBuilder.INSTANCE
                .buildClient("key", "secret");

        client.getAllActivePersons()
                .blockingGet()
                .forEach((k,v) -> 
                System.out.print(String.format("Key:%s Value:%s",k,v)));
    }
}

```
## Usage in Scala
```scala
import com.aoe.hrworks.HrWorksClientBuilder

object Sample {

  def main(args: Array[String]): Unit = {
    val client = HrWorksClientBuilder.INSTANCE.buildClient("key","secret")
    client.getAllActivePersons
      .blockingGet()
      .forEach((k, v) =>
        System.out.println(s"key: $k, value: $v")
      )
  }

}

```