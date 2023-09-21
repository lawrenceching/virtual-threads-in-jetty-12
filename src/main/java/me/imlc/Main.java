package me.imlc;

import org.eclipse.jetty.server.*;
import org.eclipse.jetty.util.Callback;
import org.eclipse.jetty.util.VirtualThreads;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;


public class Main {
    public static void main(String[] args) throws Exception {
        QueuedThreadPool threadPool = new QueuedThreadPool();
//        ThreadFactory factory = Thread.ofVirtual().name("jetty-vt-", 0).factory();
//        ExecutorService executor = Executors.newThreadPerTaskExecutor(factory);
//        threadPool.setVirtualThreadsExecutor(executor);
        threadPool.setVirtualThreadsExecutor(VirtualThreads.getDefaultVirtualThreadsExecutor());
        Server server = new Server(threadPool);
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(8080);
        server.addConnector(connector);
        server.setHandler(new HelloHandler());
        server.start();
    }

    public static class HelloHandler extends Handler.Abstract {

        @Override
        public boolean handle(Request request, Response response, Callback callback) throws Exception {

            String path = request.getHttpURI().getPath();

            switch (path) {
                case "/api/v1/users": {
                    switch (request.getMethod()) {
                        case "GET": {
                            Thread.sleep(10000);
                            response.getHeaders().add("Content-Type", "application/json");
                            // language=JSON
                            response.write(true, ByteBuffer.wrap("""
                                {
                                    "data": [
                                    {
                                        "id": 1,
                                        "name": "Alice"
                                    },
                                    {
                                        "id": 2,
                                        "name": "Bob"
                                    }
                                    ]
                                }
                                """.getBytes(StandardCharsets.UTF_8)), callback);
                            break;
                        }
                    }
                    break;
                }
            }

            return true;
        }
    }
}