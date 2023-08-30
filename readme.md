
# common-rxtx 一个串口操作工具库
- [2.x 版本文档](https://github.com/han1396735592/common-rxtx/tree/2.1.0])
- [1.x 版本文档](https://github.com/han1396735592/common-rxtx/tree/1.3.0])

## 如何使用

- spring boot 项目请使用 [spring-boot-starter-rxtx](https://github.com/han1396735592/spring-boot-starter-rxtx)

1. 引入依赖(未发布到中央仓库)
```xml
<dependency>
   <groupId>cn.qqhxj.rxtx</groupId>
   <artifactId>rxtx-common</artifactId>
    <version>3.1.0-RELEASE</version>
</dependency>
```

2. 连接串口

- 获取可用串口Set
`  Set<String> availableSerialPorts = NRSerialPort.getAvailableSerialPorts();`
```
    SerialPortConfig serialPortConfig = new SerialPortConfig();
        serialPortConfig.setPort("COM1");
        serialPortConfig.setBaud(9600);
        serialPortConfig.setAutoConnect(true);
        SerialContextImpl serialContext = new SerialContextImpl(serialPortConfig);
        serialContext.setSerialReader(new AnyDataReader());
        serialContext.setSerialContextEventListener(new SerialContextEventListener() {
            @Override
            public void connected(SerialContext serialContext) {
                System.out.println("connected");
            }

            @Override
            public void disconnected(SerialContext serialContext) {
                System.out.println("disconnected");
            }

            @Override
            public void connectError(SerialContext serialContext) {
                System.out.println("connectError");
            }

            @Override
            public void hardwareError(SerialContext serialContext) {
                System.out.println("hardwareError");
            }
        });
        serialContext.connect();
        serialContext.setSerialByteDataProcessor(new SerialByteDataProcessor() {
            @Override
            public void process(byte[] bytes, SerialContext serialContext) {
                System.out.println(serialContext.getSerialPortConfig().getPort()+"---"+new String(bytes));
            }
        });
```

3. 设置串口读写器

```
serialContext.setSerialReader(new VariableLengthSerialReader('{', '}')); //读取 { 开始 } 结束的数据 
serialContext.setSerialReader(new AnyDataReader());//读取任意数据
```

4. 设置串口数据解析器

```
serialContext.addSerialDataParser(new StringSerialDataParser());
```

5. 设置串口事件监听器

```
serialContext.setSerialContextEventListener(new SerialContextEventListener() {

});
```

6. 设置串口byte数据处理器(可选)

```
serialContext.setSerialByteDataProcessor(new SerialByteDataProcessor() {
    @Override
    public void process(byte[] bytes,AbstractSerialContext serialContext) {
        System.out.println(bytes);
    }
});
```

7. 设置自定义的串口数据解析器

需要实现 ` interface SerialDataParser<T>`

```
 serialContext.addSerialDataParser(new SerialDataParser<Object>() {
    @Override
    public Object parse(byte[] bytes,AbstractSerialContext serialContext) {
        return null;
    }
});
```

8. 设置串口对象处理器(可选,需要有对应的串口数据解析器)

```
serialContext.addSerialDataProcessor(new SerialDataProcessor<T>() {
    @Override
    public void process(T t,AbstractSerialContext serialContext) {
        System.out.println(t);
    }
});
```

