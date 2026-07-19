package com._xibrahim.cards.service;

import com._xibrahim.cards.dto.CardDto;
import com._xibrahim.cards.dto.CardResponseDto;

import java.util.List;

public interface ICardService {

    CardResponseDto createCard(CardDto cardDto);

    CardResponseDto fetchCard(Integer cardId);

    List<CardResponseDto> fetchCards();

    List<CardResponseDto> fetchCardsByMobileNumber(String mobileNumber);

    CardResponseDto updateCard(Integer cardId, CardDto cardDto);

    void deleteCard(Integer cardId);
}
