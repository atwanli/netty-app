package me.hao0.nettyapp.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * JDK Non Blocking I/O Server
 */
public class PlainNioServer {

    public void serve(int port) throws IOException {

        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        ServerSocket ss = serverChannel.socket();
        InetSocketAddress address = new InetSocketAddress(port);
        ss.bind(address);
        Selector selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
        for (;;) {
            try {
                // blocking until some events occur on some registered channels
                selector.select();
            } catch (IOException ex) {
                ex.printStackTrace();
                // handle exception
                break;
            }
            Set<SelectionKey> readyKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = readyKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                try {
                    if (key.isAcceptable()) {
                        // connect request
                        // server channel
                        ServerSocketChannel server =
                                (ServerSocketChannel) key.channel();

                        // client channel
                        SocketChannel client = server.accept();

                        // not blocking
                        client.configureBlocking(false);

                        // listen on write/read operations when client is connected
                        client.register(selector, SelectionKey.OP_WRITE |
                                SelectionKey.OP_READ, msg.duplicate());

                        System.out.println("Accepted connection from " + client);
                    }
                    if (key.isWritable()) {
                        // data in request
                        // client channel
                        SocketChannel client =
                                (SocketChannel) key.channel();
                        ByteBuffer buffer =
                                (ByteBuffer) key.attachment();
                        while (buffer.hasRemaining()) {
                            // write to client channel
                            if (client.write(buffer) == 0) {
                                break;

                            }
                        }
                        client.close();
                    }
                } catch (IOException ex) {
                    key.cancel();
                    try {
                        key.channel().close();
                    } catch (IOException cex) {
                        // 在关闭时忽略
                    }
                }
            }
        }
    }
}