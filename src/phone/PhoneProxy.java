package phone;

import phone.checks.*;

public class PhoneProxy implements PhoneInterface {
    private final Phone realPhone;
    private final PhoneCallMediator mediator;

    public PhoneProxy(String number, PhoneCallMediator mediator) {
        this.realPhone = Phone.getInstance(number);
        this.mediator = mediator;
        mediator.registerPhone(this);
    }

    private PhoneProxy(Builder builder) {
        this.realPhone = builder.realPhone;
        this.mediator = builder.mediator;
        mediator.registerPhone(this);
    }

    public static class Builder {
        private final Phone realPhone;
        private final PhoneCallMediator mediator;

        public Builder(String number, PhoneCallMediator mediator) {
            this.realPhone = Phone.getInstance(number);
            this.mediator = mediator;
        }

        public PhoneProxy build() { return new PhoneProxy(this); }

        public Builder setBalance(int amount) {
            if (amount <= 0) {
                System.out.println("INIT ERROR: wrong balance amount set.");
            } else {
                this.realPhone.replenishBalance(amount);
            }
            return this;
        }
    }

    @Override
    public synchronized String getNumber() { return realPhone.getNumber(); }

    @Override
    public synchronized int getBalance() { return realPhone.getBalance(); }

    @Override
    public synchronized State getState() { return realPhone.getState(); }

    @Override
    public synchronized void setState(State state) {
        realPhone.setState(state);
    }

    public synchronized String getConnectedPhoneNumber() {
        return realPhone.getConnectedPhoneNumber();
    }

    public synchronized void setConnectedPhoneNumber(String connectedPhoneNumber) {
        realPhone.setConnectedPhoneNumber(connectedPhoneNumber);
    }

    @Override
    public synchronized void replenishBalance(int amount) {
        if (amount <= 0) {
            System.out.println("ERROR: wrong deposit amount.");
            return;
        }
        realPhone.replenishBalance(amount);
    }

    @Override
    public synchronized void decreaseBalance(int amount) {
        if (amount <= 0) {
            System.out.println("ERROR: wrong decrease amount.");
            return;
        }
        realPhone.decreaseBalance(amount);
    }

    public boolean call(String toNumber) {
//        synchronized(this) {
//            if (!canCall(toNumber)) return false;
//        }

        Request request = new Request(Request.Type.CALL, this, toNumber);
        return mediator.submitRequest(request);
    }

    public boolean answer() {
//        synchronized (this) {
//            if (!canAnswer()) return false;
//        }

        Request request = new Request(Request.Type.ANSWER, this, null);
        return mediator.submitRequest(request);
    }

    public boolean drop() {
//        synchronized (this) {
//            if (!canDrop()) return false;
//        }

        Request request = new Request(Request.Type.DROP, this, null);
        return mediator.submitRequest(request);
    }

    private boolean canCall(String toNumber) {
        CallCheck balanceCheck = new BalanceCheck();
        CallCheck selfCallCheck = new SelfCallCheck();
        CallCheck alreadyCallingCheck = new AlreadyCallingCheck();

        balanceCheck.setNext(selfCallCheck);
        selfCallCheck.setNext(alreadyCallingCheck);

        return balanceCheck.check(this, toNumber);
    }

    private boolean canAnswer() {
        if (this.getState() != State.RINGING) {
            System.out.println("ERROR: nobody is calling you.");
            return false;
        }
        return true;
    }

    private boolean canDrop() {
        if (this.getState() != State.IN_CALL) {
            System.out.println("ERROR: you are not in the call.");
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return realPhone.toString();
    }
}
