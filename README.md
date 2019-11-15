# 简单的串口操作

## 如何使用
- spring boot 项目请使用 [spring-boot-starter-rxtx](https://github.com/han1396735592/spring-boot-starter-rxtx) 

1. 引入依赖

```xml
<dependency>
    <groupId>cn.qqhxj.common</groupId>
    <artifactId>rxtx</artifactId>
    <version>1.3.1-RELEASE</version>
<dependency>
```
2. 设置串口

portName such as COM1 或者使用 `SerialUtils.getCommNames();`

```
 SerialPort connect = SerialUtils.connect(portName, 9600);
```
3. 设置串口读写器
```
SerialContext.setSerialReader(new VariableLengthSerialReader('{', '}'));
```
4. 设置串口数据解析器
```
SerialContext.getSerialDataParserSet().add(new StringSerialDataParser());
```
5. 设置串口事件监听器
```
SerialContext.setSerialPortEventListener(new DefaultSerialDataListener());
```
6. 设置串口byte数据处理器(可选)
```
SerialContext.setSerialByteDataProcessor(new SerialByteDataProcessor() {
    @Override
    public void process(byte[] bytes) {
        System.out.println(bytes);
    }
});
```
7. 设置自定义的串口数据解析器

需要实现 ` interface SerialDataParser<T>`
```
 SerialContext.getSerialDataParserSet().add(new SerialDataParser<Object>() {
    @Override
    public Object parse(byte[] bytes) {
        return null;
    }
});
```


8. 设置串口对象处理器(可选,需要有对应的串口数据解析器)
```
SerialContext.getSerialDataProcessorSet().add(new SerialDataProcessor<T>() {
    @Override
    public void process(T t) {
        System.out.println(t);
    }
});
```

