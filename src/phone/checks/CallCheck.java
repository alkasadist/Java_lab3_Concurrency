package phone.checks;

import phone.PhoneProxy;

public abstract class CallCheck {
    protected CallCheck next;

    public void setNext(CallCheck next) {
        this.next = next;
    }

    public boolean check(PhoneProxy caller, String toNumber) {
        if (!doCheck(caller, toNumber)) {
            return false;
        }
        return next == null || next.check(caller, toNumber);
    }

    protected abstract boolean doCheck(PhoneProxy caller, String toNumber);
}
