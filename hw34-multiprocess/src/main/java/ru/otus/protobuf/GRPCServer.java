package ru.otus.protobuf;

import io.grpc.ServerBuilder;
import java.io.IOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.service.RemoteDBServiceImpl;

public class GRPCServer {
    private static final Logger log = LoggerFactory.getLogger(GRPCServer.class);

    public static final int SERVER_PORT = 8190;

    public static void main(String[] args) throws IOException, InterruptedException {
        var server = ServerBuilder.forPort(SERVER_PORT)
                .addService(new RemoteDBServiceImpl())
                .build();
        server.start();
        log.info("GRPCServer started");
        server.awaitTermination();
    }
}
