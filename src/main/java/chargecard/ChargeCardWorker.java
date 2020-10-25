package chargecard;

import java.util.logging.Logger;
import org.camunda.bpm.client.ExternalTaskClient;

public class ChargeCardWorker
{
    private final static Logger LOGGER = Logger.getLogger(ChargeCardWorker.class.getName());

    public static void main(String[] args)
    {
        ExternalTaskClient client = ExternalTaskClient.create()
                .baseUrl("http://localhost:8080/engine-rest")
                .asyncResponseTimeout(10000) // long polling timeout
                .build();
        LOGGER.info("Not yet requests");

        // subscribe to an external task topic as specified in the process
        client.subscribe("payment-checker")
                .lockDuration(1000) // the default lock duration is 20 seconds, but you can override this
                .handler((externalTask, externalTaskService) -> {
                    // Put your business logic here
                    System.out.println("Test of External Service: Pay");
                    // Get a process variable
                    String item = (String) externalTask.getVariable("item");
                    Long amount = (Long) externalTask.getVariable("amount");
                    Long age = (Long) externalTask.getVariable("age");
                    LOGGER.info("Charging credit card with an amount of '" + amount + "'€ for the item '" + item + " Age:" + age);

                    // Complete the task
                    externalTaskService.complete(externalTask);
                })
                .open();
    }
}
