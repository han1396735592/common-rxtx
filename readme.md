# 简单的串口操作

- 使用之前请确保引用了dll  [dll下载地址](https://github.com/CMU-CREATE-Lab/commons-java/tree/master/java/lib/rxtx)

## 与 1.0 版本的不同之处

> - 支持了多串口操作
> - maven 坐标名称有调整
>

## 如何使用

- spring boot 项目请使用 [spring-boot-starter-rxtx](https://github.com/han1396735592/spring-boot-starter-rxtx)

1. 引入依赖
    - 代码未发布到中心仓库 自行克隆打包使用

```xml
<dependency>
    <groupId>cn.qqhxj.rxtx</groupId>
    <artifactId>rxtx-common</artifactId>
    <version>2.0.0-RELEASE</version>
</dependency>
```

2. 连接串口

portName such as COM1 或者使用 `SerialUtils.getCommNames();`

```
SerialPort serialPort = SerialUtils.connect(portName, 9600);
SerialContext serialContext = new SerialContext(serialPort);
```

3. 设置串口读写器

```
serialContext.setSerialReader(new VariableLengthSerialReader('{', '}')); //读取 { 开始 } 结束的数据 
serialContext.setSerialReader(new AnyDataReader());//读取任意数据
```

4. 设置串口数据解析器

```
serialContext.getSerialDataParserSet().add(new StringSerialDataParser());
```

5. 设置串口事件监听器

```
serialContext.setSerialPortEventListener(new DefaultSerialDataListener(serialContext));
```

6. 设置串口byte数据处理器(可选)

```
serialContext.setSerialByteDataProcessor(new SerialByteDataProcessor() {
    @Override
    public void process(byte[] bytes) {
        System.out.println(bytes);
    }
});
```

7. 设置自定义的串口数据解析器

需要实现 ` interface SerialDataParser<T>`

```
 serialContext.getSerialDataParserSet().add(new SerialDataParser<Object>() {
    @Override
    public Object parse(byte[] bytes) {
        return null;
    }
});
```

8. 设置串口对象处理器(可选,需要有对应的串口数据解析器)

```
serialContext.getSerialDataProcessorSet().add(new SerialDataProcessor<T>() {
    @Override
    public void process(T t) {
        System.out.println(t);
    }
});
```

