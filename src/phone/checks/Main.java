package phone.checks;

import phone.PhoneCallMediator;
import phone.PhoneProxy;

public class Main {
    public static void main(String[] args) {
        PhoneCallMediator mediator = PhoneCallMediator.getInstance();

        System.out.println("\nSuccessfulCall:");

        PhoneProxy phone1 = new PhoneProxy.Builder("1000", mediator)
                .setBalance(100)
                .build();
        PhoneProxy phone2 = new PhoneProxy("2000", mediator);

        System.out.println(phone1);
        System.out.println(phone2);

        phone2.replenishBalance(100);

        phone1.call("2000");
        System.out.println(phone1);
        System.out.println(phone2);

        phone2.answer();
        System.out.println(phone1);
        System.out.println(phone2);

        phone2.drop();
        System.out.println(phone1);
        System.out.println(phone2);
    }
}
