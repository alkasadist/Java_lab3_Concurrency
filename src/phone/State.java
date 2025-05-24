package phone;

public enum State {
    WAITING, // phone is free
    CALLING, // phone is calling somebody
    IN_CALL, // phone is talking to somebody
    BLOCKED, // phone is blocked
    RINGING, // somebody is calling that phone
}
