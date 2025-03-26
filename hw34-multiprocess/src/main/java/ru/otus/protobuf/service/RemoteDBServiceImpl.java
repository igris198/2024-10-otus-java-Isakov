package ru.otus.protobuf.service;

import static ru.otus.protobuf.Util.sleepNumSec;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.protobuf.ClientMessage;
import ru.otus.protobuf.RemoteDBServiceGrpc;
import ru.otus.protobuf.ServerMessage;

public class RemoteDBServiceImpl extends RemoteDBServiceGrpc.RemoteDBServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(RemoteDBServiceImpl.class);

    @Override
    public void generateValues(ClientMessage request, StreamObserver<ServerMessage> responseObserver) {
        int currentValue = request.getFirstValue();
        while (currentValue < request.getLastValue()) {
            sleepNumSec(2);
            ServerMessage response =
                    ServerMessage.newBuilder().setResultValue(++currentValue).build();
            responseObserver.onNext(response);
            log.info("server value: {}", currentValue);
        }
        responseObserver.onCompleted();
    }
}
