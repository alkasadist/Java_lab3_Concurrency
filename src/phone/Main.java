package phone;

public class Main {
    public static void main(String[] args) {
        PhoneCallMediator mediator = PhoneCallMediator.getInstance();

        Tests.RunTests(mediator);
    }
}
