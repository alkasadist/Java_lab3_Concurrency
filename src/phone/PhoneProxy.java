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
    public String getNumber() { return realPhone.getNumber(); }

    @Override
    public int getBalance() { return realPhone.getBalance(); }

    @Override
    public State getState() { return realPhone.getState(); }

    @Override
    public void setState(State state) {
        realPhone.setState(state);
    }

    public String getConnectedPhoneNumber() {
        return realPhone.getConnectedPhoneNumber();
    }

    public void setConnectedPhoneNumber(String connectedPhoneNumber) {
        realPhone.setConnectedPhoneNumber(connectedPhoneNumber);
    }

    @Override
    public void replenishBalance(int amount) {
        if (amount <= 0) {
            System.out.println("ERROR: wrong deposit amount.");
            return;
        }
        realPhone.replenishBalance(amount);
    }

    @Override
    public void decreaseBalance(int amount) {
        if (amount <= 0) {
            System.out.println("ERROR: wrong decrease amount.");
            return;
        }
        realPhone.decreaseBalance(amount);
    }

    public boolean call(String toNumber) {
        if (canCall(toNumber)) {
            return mediator.makeCall(this.getNumber(), toNumber);
        }
        return false;
    }

    public boolean answer() {
        if (canAnswer()) {
            return mediator.answerCall(this);
        }
        return false;
    }

    public boolean drop() {
        if (canDrop()) {
            return mediator.dropCall(this);
        }
        return false;
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
        if (this.realPhone.getState() != State.RINGING) {
            System.out.println("ERROR: nobody is calling you.");
            return false;
        }
        return true;
    }

    private boolean canDrop() {
        if (this.realPhone.getState() != State.IN_CALL) {
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
