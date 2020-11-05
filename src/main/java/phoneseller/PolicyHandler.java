package phoneseller;

import phoneseller.config.kafka.KafkaProcessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PolicyHandler{
    @StreamListener(KafkaProcessor.INPUT)
    public void onStringEventListener(@Payload String eventString){

    }

    @Autowired
    OrderRepository orderRepository;

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverShipped_OrderStatus(@Payload Shipped shipped){
        // 배송이 시작될 때 오더상태 변경

        if(shipped.isMe()){
            System.out.println("!!!!!!!!!!!!!wheneverShipped_OrderStatus!!!!!!!!!!!!!");
            System.out.println(shipped.getId());
            System.out.println(shipped.getOrderId());
            System.out.println(shipped.getProcess());

            Optional<Order> orderOptional= orderRepository.findById(shipped.getOrderId());
            Order order = orderOptional.get();
            order.setStatus("Shipped");
            orderRepository.save(order);

            System.out.println("##### listener OrderStatus : " + shipped.toJson());
        }
    }
    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayCancelled_OrderStatus(@Payload PayCancelled payCancelled){
        // 결제가 취소될 때 오더상태 변경

        if(payCancelled.isMe()){
            System.out.println("!!!!!!!!!!wheneverPayCancelled_OrderStatus!!!!!!!!!");
            System.out.println(payCancelled.getId());
            System.out.println(payCancelled.getOrderId());
            System.out.println(payCancelled.getProcess());

            Optional<Order> orderOptional= orderRepository.findById(payCancelled.getOrderId());
            Order order = orderOptional.get();
            order.setStatus("PayCancelled");
            orderRepository.save(order);

            System.out.println("##### listener OrderStatus : " + payCancelled.toJson());
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverGiftCompleted_OrderStatus(@Payload GiftCompleted giftCompleted){

        if(giftCompleted.isMe()){
            System.out.println("!!!!!!!!!!!!!!!!wheneverGiftCompleted_OrderStatus!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(giftCompleted.getOrderId());

            if(orderRepository.findById(giftCompleted.getOrderId()) != null) {
                Order order = orderRepository.findById(giftCompleted.getOrderId()).get();
                order.setPoint(giftCompleted.getPoint());
                orderRepository.save(order);
            }

            System.out.println("##### listener OrderStatus : " + giftCompleted.toJson());
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverGiftCancelled_OrderStatus(@Payload GiftCancelled giftCancelled){

        if(giftCancelled.isMe()){
            System.out.println("!!!!!!!!!!!!!!!!wheneverGiftCancelled_OrderStatus!!!!!!!!!!!!!!!!!!!!!!");
            System.out.println(giftCancelled.getOrderId());

            if(orderRepository.findById(giftCancelled.getOrderId()) != null) {
                Order order = orderRepository.findById(giftCancelled.getOrderId()).get();
                order.setPoint(giftCancelled.getPoint());
                orderRepository.save(order);
            }

            System.out.println("##### listener OrderStatus : " + giftCancelled.toJson());
        }
    }

    @StreamListener(KafkaProcessor.INPUT)
    public void wheneverPayCompleted_OrderStatus(@Payload PayCompleted payCompleted){
        System.out.println("app_policy_paycompleted_status");
        System.out.println(payCompleted.toJson());
        if(payCompleted.isMe()){
            if(orderRepository.findById(payCompleted.getOrderId()) != null){
                System.out.println("====================================결제완료====================================");
                Order order = orderRepository.findById(payCompleted.getOrderId()).get();
                System.out.println(payCompleted.getProcess());
                order.setStatus("Payed");
                System.out.println(payCompleted.toJson());
                orderRepository.save(order);
            }

        }

    }
}
