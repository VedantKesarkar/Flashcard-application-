package com.project.flashcardapp.home.domain;

import androidx.lifecycle.ViewModel;

import com.project.flashcardapp.home.dto.DeckModel;
import com.project.flashcardapp.home.repo.DeckRepository;

public class DeckViewModel extends ViewModel {

    private final DeckRepository repository = new DeckRepository();
    public void createDeck(DeckModel deck)
    {
        repository.createDeck(deck);
    }
    public void setEmail(String email){
        repository.setEmail(email);
    }
}
