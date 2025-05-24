package phone;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        PhoneCallMediator mediator = PhoneCallMediator.getInstance();

        int phoneCount = 5;
        int requestCount = 30;
        List<PhoneProxy> phones = new ArrayList<>();

        for (int i = 0; i < phoneCount; i++) {
            PhoneProxy phone = new PhoneProxy.Builder(String.valueOf(100 + i), mediator)
                    .setBalance(100)
                    .build();
            phones.add(phone);
        }

        Random random = new Random();

        for (int i = 0; i < requestCount; i++) {
            new Thread(() -> {
                PhoneProxy caller, callee;
                do {
                    caller = phones.get(random.nextInt(phoneCount));
                    callee = phones.get(random.nextInt(phoneCount));
                } while (caller == callee);

                caller.call(callee.getNumber());
                System.out.println(caller);
                callee.answer();
                System.out.println(caller);
                caller.drop();
                System.out.println(caller);

            }).start();

        }

        sleep(100);

        System.out.println("\n\n====== Final Phone States ======");
        for (PhoneProxy phone : phones) {
            System.out.println(phone);
        }
    }

    private static void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {
        }
    }
}
