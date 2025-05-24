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

    public boolean submitRequest(Request req) {
        try {
            requestQueue.put(req);
            req.awaitDone();
            return req.isSuccess();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
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
            boolean result = switch (req.getType()) {
                case CALL -> call(req.getFromPhone().getNumber(), req.getToNumber());
                case ANSWER -> answer(req.getFromPhone());
                case DROP -> drop(req.getFromPhone());
            };
            req.setSuccess(result);
        } finally {
            req.markDone();
        }

    }

    public void registerPhone(PhoneProxy phone) {
        phones.put(phone.getNumber(), phone);
    }

    private synchronized boolean call(String fromNumber, String toNumber) {
        if (phones.get(toNumber) == null) {
            System.out.println("MEDIATOR ERROR: phone number " + toNumber + " not found.");
            return false;
        }

        PhoneProxy fromPhone = phones.get(fromNumber);
        PhoneProxy toPhone = phones.get(toNumber);

        if (fromPhone.getState() == State.IN_CALL ||
                fromPhone.getState() == State.CALLING ||
                fromPhone.getState() == State.RINGING) {
            System.out.println("MEDIATOR ERROR: caller " + fromNumber + " is already in a call.");
            return false;
        }


        if (toPhone.getState() == State.IN_CALL ||
            toPhone.getState() == State.CALLING ||
            toPhone.getState() == State.RINGING) {
            System.out.println("MEDIATOR ERROR: phone number " + toNumber + " is busy, call again later.");
            return false;
        }

        if (fromPhone.getBalance() < 50) {
            fromPhone.setState(State.BLOCKED);
            System.out.println("ERROR: insufficient balance.");
            return false;
        }

        if (fromPhone.getNumber().equals(toNumber)) {
            System.out.println("ERROR: you cannot call yourself.");
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

    private synchronized boolean answer(PhoneProxy caller) {
        if (caller.getConnectedPhoneNumber() == null) {
            System.out.println("MEDIATOR ERROR: " + caller.getNumber() + " is not in a call.");
            return false;
        }

        PhoneProxy callee = phones.get(caller.getConnectedPhoneNumber());

        if (callee.getState() != State.RINGING) {
            System.out.println("ERROR: nobody is calling you.");
            return false;
        }

        caller.setState(State.IN_CALL);
        callee.setState(State.IN_CALL);

        System.out.println(callee.getNumber() + " answered the " + caller.getNumber());
        return true;
    }

    private synchronized boolean drop(PhoneProxy caller) {
        if (caller.getConnectedPhoneNumber() == null) {
            System.out.println("MEDIATOR ERROR: " + caller.getNumber() + " is not in a call.");
            return false;
        }

        PhoneProxy callee = phones.get(caller.getConnectedPhoneNumber());

        if (caller.getState() != State.IN_CALL) {
            System.out.println("ERROR: you are not in the call.");
            return false;
        }

        caller.setConnectedPhoneNumber(null);
        caller.setState(State.WAITING);
        callee.setConnectedPhoneNumber(null);
        callee.setState(State.WAITING);

        System.out.println(callee.getNumber() + " dropped the " + caller.getNumber());
        return true;
    }
}
