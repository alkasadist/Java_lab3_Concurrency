package phone;

public class Tests {
    static void RunTests(PhoneCallMediator mediator) {
        NumberNotFoundTest(mediator);
        AlreadyCallingSomeoneTest(mediator);
        SelfCallTest(mediator);
        BlockedPhoneTest(mediator);
        AlreadyCallingTest(mediator);
        NoActiveCallTest(mediator);
        NoCallDropTest(mediator);
        SuccessfulCall(mediator);
    }

    static void showTestResult(boolean result) {
        System.out.println(!result ? "✅ SUCCESS ✅" : "❌ FAIL ❌");
    }

    static void NumberNotFoundTest(PhoneCallMediator mediator) {
        System.out.println("\nNumberNotFoundTest:");

        PhoneProxy phone = new PhoneProxy("111", mediator);
        phone.replenishBalance(50);
        boolean result = phone.call("47348237948");

        showTestResult(result);
    }

    static void AlreadyCallingSomeoneTest(PhoneCallMediator mediator) {
        System.out.println("\nAlreadyCallingSomeoneTest:");

        PhoneProxy phone1 = new PhoneProxy("0", mediator);
        phone1.replenishBalance(100);
        PhoneProxy phone2 = new PhoneProxy("1", mediator);
        PhoneProxy phone3 = new PhoneProxy("2", mediator);

        phone1.call("1");
        boolean result = phone1.call("2");

        showTestResult(result);
    }

    static void SelfCallTest(PhoneCallMediator mediator) {
        System.out.println("\nSelfCallTest:");

        PhoneProxy phone = new PhoneProxy("111", mediator);
        phone.replenishBalance(100);
        boolean result = phone.call("111");

        showTestResult(result);
    }

    static void BlockedPhoneTest(PhoneCallMediator mediator) {
        System.out.println("\nBlockedPhoneTest:");

        PhoneProxy phoneLowBalance = new PhoneProxy.Builder("222", mediator)
                .setBalance(10)
                .build();
        boolean result = phoneLowBalance.call("111");

        showTestResult(result);
    }

    static void AlreadyCallingTest(PhoneCallMediator mediator) {
        System.out.println("\nAlreadyCallingTest:");

        PhoneProxy phone1 = new PhoneProxy.Builder("333", mediator)
                .setBalance(100)
                .build();
        PhoneProxy phone2 = new PhoneProxy.Builder("444", mediator)
                .setBalance(100)
                .build();
        PhoneProxy phone3 = new PhoneProxy("555", mediator);

        phone1.call("555");
        boolean result = phone2.call("555");

        showTestResult(result);
    }

    static void NoActiveCallTest(PhoneCallMediator mediator) {
        System.out.println("\nNoActiveCallTest:");

        PhoneProxy phone = new PhoneProxy("666", mediator);
        boolean result = phone.answer();

        showTestResult(result);
    }

    static void NoCallDropTest(PhoneCallMediator mediator) {
        System.out.println("\nNoCallDropTest:");

        PhoneProxy phone = new PhoneProxy("777", mediator);
        boolean result = phone.drop();

        showTestResult(result);
    }

    static void SuccessfulCall(PhoneCallMediator mediator) {
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
