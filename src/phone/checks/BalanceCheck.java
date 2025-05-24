package phone.checks;

import phone.PhoneProxy;
import phone.State;

public class BalanceCheck extends CallCheck {
    @Override
    protected boolean doCheck(PhoneProxy caller, String toNumber) {
        if (caller.getBalance() < 50) {
            caller.setState(State.BLOCKED);
            System.out.println("ERROR: insufficient balance.");
            return false;
        }
        return next == null || next.check(caller, toNumber);
    }
}
