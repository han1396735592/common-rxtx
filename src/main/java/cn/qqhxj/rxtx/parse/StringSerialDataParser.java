package cn.qqhxj.rxtx.parse;

import cn.qqhxj.rxtx.context.SerialContext;

import java.nio.charset.Charset;

/**
 * @author han1396735592
 **/
public class StringSerialDataParser implements SerialDataParser<String> {

    private final Charset charset;

    public StringSerialDataParser(Charset charset) {
        this.charset = charset;
    }

    public StringSerialDataParser(String charset) {
        this.charset = Charset.forName(charset);
    }

    public StringSerialDataParser() {
        this(Charset.defaultCharset());
    }

    @Override
    public String parse(byte[] bytes, SerialContext serialContext) {
        return new String(bytes, charset);
    }
}

