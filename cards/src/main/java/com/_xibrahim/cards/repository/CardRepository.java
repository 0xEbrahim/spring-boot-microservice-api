package com._xibrahim.cards.repository;

import com._xibrahim.cards.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Integer> {

    boolean existsByCardNumber(String cardNumber);

    List<Card> findByMobileNumber(String mobileNumber);
}
