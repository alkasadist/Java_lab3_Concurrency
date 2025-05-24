package phone;

import java.util.concurrent.CountDownLatch;

public class Request {
    public enum Type { CALL, ANSWER, DROP }
    private final Type type;
    private final PhoneProxy fromPhone;
    private final String toNumber;

    private volatile boolean success;
    private final CountDownLatch done = new CountDownLatch(1);

    public Request(Type type, PhoneProxy fromPhone, String toNumber) {
        this.type = type;
        this.fromPhone = fromPhone;
        this.toNumber = toNumber;
    }

    public void markDone() {
        done.countDown();
    }

    public void awaitDone() {
        try {
            done.await();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Type getType() {
        return type;
    }

    public PhoneProxy getFromPhone() {
        return fromPhone;
    }

    public String getToNumber() {
        return toNumber;
    }

    public boolean isSuccess() {
        return success;
    }
}
