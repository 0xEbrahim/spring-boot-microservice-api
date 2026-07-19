package com._xibrahim.cards.service.impl;

import com._xibrahim.cards.dto.CardDto;
import com._xibrahim.cards.dto.CardResponseDto;
import com._xibrahim.cards.entity.Card;
import com._xibrahim.cards.exception.NotFoundException;
import com._xibrahim.cards.mapper.ApiMapper;
import com._xibrahim.cards.repository.CardRepository;
import com._xibrahim.cards.service.ICardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@AllArgsConstructor
public class CardServiceImpl implements ICardService {

    private final CardRepository cardRepository;
    private final ApiMapper apiMapper;

    @Override
    @Transactional
    public CardResponseDto createCard(CardDto cardDto) {
        Card card = apiMapper.transformFromDto(cardDto, Card.class);
        card.setCardNumber(generateCardNumber());
        applyAvailableAmount(card);

        Card savedCard = cardRepository.save(card);
        return apiMapper.transformToDto(savedCard, CardResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public CardResponseDto fetchCard(Integer cardId) {
        Card card = getCardById(cardId);
        return apiMapper.transformToDto(card, CardResponseDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardResponseDto> fetchCards() {
        return cardRepository.findAll()
                .stream()
                .map(card -> apiMapper.transformToDto(card, CardResponseDto.class))
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CardResponseDto> fetchCardsByMobileNumber(String mobileNumber) {
        List<Card> cards = cardRepository.findByMobileNumber(mobileNumber);
        if (cards.isEmpty()) {
            throw new NotFoundException("Card not found with mobile number: " + mobileNumber);
        }

        return cards.stream()
                .map(card -> apiMapper.transformToDto(card, CardResponseDto.class))
                .toList();
    }

    @Override
    @Transactional
    public CardResponseDto updateCard(Integer cardId, CardDto cardDto) {
        Card card = getCardById(cardId);
        card.setMobileNumber(cardDto.getMobileNumber());
        card.setCardType(cardDto.getCardType());
        card.setTotalLimit(cardDto.getTotalLimit());
        card.setAmountUsed(cardDto.getAmountUsed());
        applyAvailableAmount(card);

        Card savedCard = cardRepository.save(card);
        return apiMapper.transformToDto(savedCard, CardResponseDto.class);
    }

    @Override
    @Transactional
    public void deleteCard(Integer cardId) {
        Card card = getCardById(cardId);
        cardRepository.delete(card);
    }

    private Card getCardById(Integer cardId) {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("Card not found with id: " + cardId));
    }

    private void applyAvailableAmount(Card card) {
        card.setAvailableAmount(card.getTotalLimit() - card.getAmountUsed());
    }

    private String generateCardNumber() {
        String cardNumber;
        do {
            cardNumber = String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000_000_000L, 10_000_000_000_000_000L));
        } while (cardRepository.existsByCardNumber(cardNumber));
        return cardNumber;
    }
}
