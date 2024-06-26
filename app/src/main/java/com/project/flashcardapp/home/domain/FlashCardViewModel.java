package com.project.flashcardapp.home.domain;

import androidx.lifecycle.ViewModel;

import com.project.flashcardapp.home.dto.FlashCardModel;
import com.project.flashcardapp.home.repo.FlashCardRepository;

public class FlashCardViewModel extends ViewModel {

    private final FlashCardRepository flashCardRepo = new FlashCardRepository();
    public void setDeckId(String deckId)
    {
        flashCardRepo.setDeckId(deckId);
    }

    public void createFlashCard(FlashCardModel model)
    {
        flashCardRepo.createFlashCard(model);
    }
}
