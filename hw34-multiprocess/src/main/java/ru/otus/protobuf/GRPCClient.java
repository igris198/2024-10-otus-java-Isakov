package ru.otus.protobuf;

import static ru.otus.protobuf.Util.sleepNumSec;

import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import java.util.concurrent.CountDownLatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GRPCClient {
    private static final Logger log = LoggerFactory.getLogger(GRPCClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;

    private static volatile int serverValue = 0;

    public static void main(String[] args) throws InterruptedException {
        var channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT)
                .usePlaintext()
                .build();
        var stub = RemoteDBServiceGrpc.newStub(channel);
        var request =
                ClientMessage.newBuilder().setFirstValue(0).setLastValue(30).build();
        var latch = new CountDownLatch(1);
        stub.generateValues(request, new StreamObserver<>() {
            @Override
            public void onNext(ServerMessage serverMessage) {
                serverValue = serverMessage.getResultValue();
                log.info("serverValue: {}", serverValue);
            }

            @Override
            public void onError(Throwable throwable) {
                log.error(throwable.getMessage());
            }

            @Override
            public void onCompleted() {
                log.info("request completed");
                latch.countDown();
            }
        });
        log.info("numbers Client is started");
        int currentValue = 0;
        int localServerValue = 0;
        while (currentValue < 50) {
            sleepNumSec(1);
            currentValue = localServerValue != serverValue
                    ? currentValue + (localServerValue = serverValue) + 1
                    : currentValue + 1;
            log.info("currentValue: {}", currentValue);
        }
        latch.await();
        channel.shutdown();
    }
}
