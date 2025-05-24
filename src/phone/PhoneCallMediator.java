package phone;

import java.util.HashMap;
import java.util.Map;

public class PhoneCallMediator {
    private static PhoneCallMediator instance;
    private final Map<String, PhoneProxy> phones = new HashMap<>();

    private PhoneCallMediator() {}

    public static PhoneCallMediator getInstance() {
        if (instance == null) {
            instance = new PhoneCallMediator();
        }
        return instance;
    }

    public void registerPhone(PhoneProxy phone) {
        phones.put(phone.getNumber(), phone);
    }

    public boolean makeCall(String fromNumber, String toNumber) {
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

    public boolean answerCall(PhoneProxy caller) {
        PhoneProxy callee = phones.get(caller.getConnectedPhoneNumber());

        caller.setState(State.IN_CALL);
        callee.setState(State.IN_CALL);

        System.out.println(callee.getNumber() + " answered the " + caller.getNumber());
        return true;
    }

    public boolean dropCall(PhoneProxy caller) {
        PhoneProxy callee = phones.get(caller.getConnectedPhoneNumber());

        caller.setConnectedPhoneNumber(null);
        caller.setState(State.WAITING);
        callee.setConnectedPhoneNumber(null);
        callee.setState(State.WAITING);

        System.out.println(callee.getNumber() + " dropped the " + caller.getNumber());
        return true;
    }
}
