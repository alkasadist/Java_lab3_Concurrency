package phone;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PhoneCallMediator {
    private static PhoneCallMediator instance;
    private final Map<String, PhoneProxy> phones = new ConcurrentHashMap<>();
    private final BlockingQueue<Request> requestQueue = new LinkedBlockingQueue<>();

    private PhoneCallMediator() {
        for (int threads = 1; threads <= 4; threads++) {
            new Thread(this::processRequests).start();
        }
    }

    public static synchronized PhoneCallMediator getInstance() {
        if (instance == null) {
            instance = new PhoneCallMediator();
        }
        return instance;
    }

    public void submitRequest(Request req) {
        try {
            requestQueue.put(req);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void processRequests() {
        while (true) {
            try {
                Request req = requestQueue.take();
                handle(req);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void handle(Request req) {
        try {
            switch (req.getType()) {
                case CALL -> makeCall(req.getFromPhone().getNumber(), req.getToNumber());
                case ANSWER -> answerCall(req.getFromPhone());
                case DROP -> dropCall(req.getFromPhone());
            }
        } finally {
            req.markDone();
        }
    }

    public void registerPhone(PhoneProxy phone) {
        phones.put(phone.getNumber(), phone);
    }

    private boolean makeCall(String fromNumber, String toNumber) {
        if (phones.get(toNumber) == null) {
            System.out.println("MEDIATOR ERROR: phone number " + toNumber + " not found.");
            return false;
        }

        PhoneProxy fromPhone = phones.get(fromNumber);
        PhoneProxy toPhone = phones.get(toNumber);

        if (toPhone.getState() == State.IN_CALL ||
            toPhone.getState() == State.CALLING ||
            toPhone.getState() == State.RINGING) {
            System.out.println("MEDIATOR ERROR: phone number " + toNumber + " is busy, call again later.");
            return false;
        }

        fromPhone.decreaseBalance(50);
        fromPhone.setState(State.CALLING);
        toPhone.setState(State.RINGING);
        fromPhone.setConnectedPhoneNumber(toPhone.getNumber());
        toPhone.setConnectedPhoneNumber(fromPhone.getNumber());

        System.out.println(fromNumber + " is calling " + toNumber);
        return true;
    }

    private boolean answerCall(PhoneProxy caller) {
        PhoneProxy callee = phones.get(caller.getConnectedPhoneNumber());

        caller.setState(State.IN_CALL);
        callee.setState(State.IN_CALL);

        System.out.println(callee.getNumber() + " answered the " + caller.getNumber());
        return true;
    }

    private boolean dropCall(PhoneProxy caller) {
        PhoneProxy callee = phones.get(caller.getConnectedPhoneNumber());

        caller.setConnectedPhoneNumber(null);
        caller.setState(State.WAITING);
        callee.setConnectedPhoneNumber(null);
        callee.setState(State.WAITING);

        System.out.println(callee.getNumber() + " dropped the " + caller.getNumber());
        return true;
    }
}
