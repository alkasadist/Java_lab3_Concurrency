package phone.checks;

import phone.PhoneProxy;

public class SelfCallCheck extends CallCheck {
    @Override
    protected boolean doCheck(PhoneProxy caller, String toNumber) {
        if (caller.getNumber().equals(toNumber)) {
            System.out.println("ERROR: you cannot call yourself.");
            return false;
        }
        return next == null || next.check(caller, toNumber);
    }
}
