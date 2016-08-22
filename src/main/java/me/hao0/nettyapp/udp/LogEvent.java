package me.hao0.nettyapp.udp;

import java.net.InetSocketAddress;

/**
 * 日志事件
 */
public final class LogEvent {

    public static final byte SEPARATOR = (byte) ':';

    private final InetSocketAddress source;

    private final String logfile;

    private final String msg;

    private final long received;

    public LogEvent(String logfile, String msg) {
        this(null, -1, logfile, msg);
    }

    public LogEvent(InetSocketAddress source, long received, String logfile, String msg) {
        this.source = source;
        this.logfile = logfile;
        this.msg = msg;
        this.received = received;
    }

    public InetSocketAddress getSource() { //3
        return source;
    }

    public String getLogfile() { //4
        return logfile;
    }

    public String getMsg() {  //5
        return msg;
    }

    public long getReceivedTimestamp() {  //6
        return received;
    }
}