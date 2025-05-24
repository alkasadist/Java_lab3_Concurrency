package phone;

import java.util.Map;
import java.util.HashMap;

public class Phone implements PhoneInterface {
    private static final Map<String, Phone> instances = new HashMap<>();

    private final String number;
    private int balance = 0;
    private State state = State.WAITING;
    private String connectedPhoneNumber = null;

    private Phone(String number) { this.number = number; }

    public static Phone getInstance(String number) {
        if (!instances.containsKey(number)) {
            instances.put(number, new Phone(number));
        }
        return instances.get(number);
    }

    @Override
    public String getNumber() { return number; }

    @Override
    public int getBalance() { return balance; }

    @Override
    public State getState() { return state; }

    public String getConnectedPhoneNumber() {
        return connectedPhoneNumber;
    }

    public void setConnectedPhoneNumber(String connectedPhoneNumber) {
        this.connectedPhoneNumber = connectedPhoneNumber;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public void replenishBalance(int amount) {
        this.balance += amount;
    }

    @Override
    public void decreaseBalance(int amount) {
        this.balance -= amount;
    }

    @Override
    public String toString() {
        return "PHONE: [ " +
                "number = " + number +
                ", balance = " + balance +
                ", status = " + state + " ]";
    }
}
