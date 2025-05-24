package phone.checks;

import phone.PhoneProxy;
import phone.State;

public class AlreadyCallingCheck extends CallCheck {
    @Override
    protected boolean doCheck(PhoneProxy caller, String toNumber) {
        if (caller.getState() == State.CALLING || caller.getState() == State.IN_CALL) {
            System.out.println("ERROR: you're already in a call.");
            return false;
        }
        return next == null || next.check(caller, toNumber);
    }
}
