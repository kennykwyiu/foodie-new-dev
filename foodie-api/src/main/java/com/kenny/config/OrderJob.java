package com.kenny.config;

import com.kenny.service.OrderService;
import com.kenny.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {
    /**
     * Disadvantages of using scheduled tasks to close overdue unpaid orders:
     * 1. There will be a time gap, leading to program inaccuracies:
     *    For instance, if an order is placed at 10:39 and is checked at 11:00, it won't be under an hour yet.
     *    If checked at 12:00, it will be over an hour by 39 minutes.
     * 2. Lack of support for clusters:
     *    While a single machine runs fine, in a cluster environment, multiple scheduled tasks will run.
     *    Solution: Use only one computing node dedicated to running all scheduled tasks.
     * 3. Impact on database performance due to full table searches:
     *    For example, a query like "select * from order where orderStatus = 10;" can severely affect database performance.
     * Scheduled tasks are best suited for small, lightweight, and traditional projects.
     *
     * will cover message queues: MQ -> RabbitMQ, RocketMQ, Kafka, ZeroMQ...
     *    Delayed tasks (Queue)
     *    For instance, for an order placed at 10:12 with an unpaid (10) status, if it's still unpaid at 11:12, the order can be closed directly.
     */
    @Autowired
    private OrderService orderService;

    //    @Scheduled(cron = "0/3 * * * * ?")
//    @Scheduled(cron = "0 0 0/1 * * ?")
    public void autoCloseOrder() {
        orderService.closeOrder();
        System.out.println("Executing scheduled task, current time is: "
                + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }

}
