package com.undergrowth;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

public class GuiceLearn {

    public static void main(String[] args) {
        /*
         * Guice.createInjector() takes your Modules, and returns a new Injector
         * instance. Most applications will call this method exactly once, in their
         * main() method.
         */
        Injector injector = Guice.createInjector(new BillingModule());

        /*
         * Now that we've got the injector, we can build objects.
         */
        BillingService billingService = injector.getInstance(BillingService.class);
        PizzaOrder order = new PizzaOrder();
        order.setName("bigbig");
        order.setMount(100);
        CreditCard creditCard = new CreditCard();
        creditCard.setTotal(100000);
        billingService.chargeOrder(order, creditCard);
    }

    static class BillingService {
        private final CreditCardProcessor processor;
        private final TransactionLog transactionLog;

        @Inject
        BillingService(CreditCardProcessor processor,
                       TransactionLog transactionLog) {
            this.processor = processor;
            this.transactionLog = transactionLog;
        }

        public Receipt chargeOrder(PizzaOrder order, CreditCard creditCard) {
            this.transactionLog.log(order.getName() + "\t" + order.getMount());
            processor.pay(order.getMount());
            return new Receipt();
        }
    }

    static class Receipt {

    }

    static class CreditCard {
        private int total;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }
    }

    static class PizzaOrder {
        private int mount;
        private String name;

        public int getMount() {
            return mount;
        }

        public void setMount(int mount) {
            this.mount = mount;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    interface CreditCardProcessor {
        boolean pay(Integer mount);
    }

    static class PaypalCreditCardProcessor implements CreditCardProcessor {

        @Override
        public boolean pay(Integer mount) {
            System.out.println(getClass().getName() + "\t pay:" + mount);
            return true;
        }
    }

    interface TransactionLog {
        void log(String msg);
    }

   static class DatabaseTransactionLog implements TransactionLog {

        @Override
        public void log(String msg) {
            System.out.println(getClass().getName() + "\t log:" + msg);
        }
    }

    public static class BillingModule extends AbstractModule {
        @Override
        protected void configure() {

            /*
             * This tells Guice that whenever it sees a dependency on a TransactionLog,
             * it should satisfy the dependency using a DatabaseTransactionLog.
             */
            bind(TransactionLog.class).to(DatabaseTransactionLog.class);

            /*
             * Similarly, this binding tells Guice that when CreditCardProcessor is used in
             * a dependency, that should be satisfied with a PaypalCreditCardProcessor.
             */
            bind(CreditCardProcessor.class).to(PaypalCreditCardProcessor.class);
        }
    }

}
