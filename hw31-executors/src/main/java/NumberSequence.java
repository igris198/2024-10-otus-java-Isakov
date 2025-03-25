import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NumberSequence {
    private static final Logger logger = LoggerFactory.getLogger(NumberSequence.class);
    private static int value = 0;
    private static int inc = 1;
    private static boolean availableForGet = false;

    private static void incValue() {
        if (value == 10) {
            inc = -1;
        } else if (value == 1) {
            inc = 1;
        }
        value += inc;
    }

    private static void sleepOneSec() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        var numberSequence = new NumberSequence();
        var thread1 = new Thread(numberSequence::setValue);
        thread1.setName("thread1");
        var thread2 = new Thread(numberSequence::getValue);
        thread2.setName("thread2");

        thread1.start();
        thread2.start();
    }

    private synchronized void setValue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (availableForGet) {
                    this.wait();
                }
                incValue();
                logger.info("{}: {}", Thread.currentThread().getName(), value);
                sleepOneSec();
                availableForGet = true;
                this.notify();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private synchronized void getValue() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                while (!availableForGet) {
                    this.wait();
                }
                logger.info("{}: {}", Thread.currentThread().getName(), value);
                sleepOneSec();
                availableForGet = false;
                this.notify();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
