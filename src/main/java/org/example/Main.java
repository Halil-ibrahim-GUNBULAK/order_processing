package org.example;


  import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Model.*;

  import java.io.*;
  import java.net.HttpURLConnection;
  import java.net.URL;
  import java.util.List;
  import java.util.Map;
  import java.util.stream.Collectors;

public class Main {
        public static void main(String[] args) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                //Q-1
                // JSON dosyasını oku ve List<Order> tipine dönüştür
                List<Order> orders = objectMapper.readValue(new File("src/main/order.json"), new TypeReference<List<Order>>() {});

                // Üç siparişteki malların toplam tutarını hesapla
                double totalAmount = orders.stream()
                        .mapToDouble(order -> order.getQuantity() * order.getUnitPrice())
                        .sum();

                System.out.println("Üç siparişteki malların toplam tutarı: " + totalAmount + " TL");

                // Üç siparişteki bütün malların ortalama fiyatını hesapla
                double averagePrice = orders.stream()
                        .mapToDouble(Order::getUnitPrice)
                        .average()
                        .orElse(0.0);

                System.out.println("Üç siparişteki bütün malların ortalama fiyatı: " + averagePrice + " TL");

                // Üç siparişteki bütün malların tek tek mal bazlı ortalama fiyatını hesapla
                Map<Integer, Double> itemAveragePrice = orders.stream()
                        .collect(Collectors.groupingBy(Order::getItemId,
                                Collectors.averagingDouble(Order::getUnitPrice)));

                itemAveragePrice.forEach((itemId, avgPrice) ->
                        System.out.println("Mal Numarası: " + itemId + ", Ortalama Fiyat: " + avgPrice + " TL")
                );

                // Tek tek mal bazlı, malların hangi siparişlerde kaç adet olduğunu hesapla
                Map<Integer, Map<Integer, Long>> itemOrderCounts = orders.stream()
                        .collect(Collectors.groupingBy(Order::getItemId,
                                Collectors.groupingBy(Order::getOrderId,
                                        Collectors.counting())));

                itemOrderCounts.forEach((itemId, orderCounts) -> {
                    System.out.println("Mal Numarası: " + itemId);
                    orderCounts.forEach((orderId, count) ->
                            System.out.println("\tSipariş Numarası: " + orderId + ", Adet: " + count));
                });




            } catch (IOException e) {
                e.printStackTrace();
            }





        }



    }



