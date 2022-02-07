package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = LAZY) // xToOne(OneToOne, ManyToOne)관계는 기본이 즉시로딩(EAGER)이므로 직접 지연로딩(LAZY)로 설정해줘야 한다
    @JoinColumn(name = "member_id")
    private Member member;

    /*
        cascade = CascadeType.ALL ->
        ex) cascade를 두면 Order를 persist할 때 orderItems들로도 persist를 전파한다
        예를 들어 아이템 목록이 A,B,C 3개라 했을 때
        persist(orderItemsA)
        persist(orderItemsB)
        persist(orderItemsC)
        persist(Order)
        이렇게 해야할걸 cascade를 쓰면
        persist(Order)만 하면 된다
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id") // Order - Delivery 연관관계 주인
    private Delivery delivery;

    private LocalDateTime orderDate; // 주문시간

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    //=연관관계 편의 메서드==// 양방향일 때 사용
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }
}
