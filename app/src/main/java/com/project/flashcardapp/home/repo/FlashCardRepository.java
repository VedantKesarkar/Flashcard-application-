package com.project.flashcardapp.home.repo;

import static com.project.flashcardapp.home.repo.DeckRepository.DECK;
import static com.project.flashcardapp.home.repo.DeckRepository.USERS;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.flashcardapp.home.dto.FlashCardModel;

import java.util.Objects;

public class FlashCardRepository {

    private static final String TAG = "FlashCardRepository";
    public static final String FLASHCARD = "flashcard";
    public static final String QUEUE = "queue";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    private String email,deckId;


    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public void createFlashCard(FlashCardModel flashCard)
    {

           Log.d(TAG, "Im in "+flashCard.getDeckId());
           email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
           DocumentReference docRef = db.collection(USERS).document(email).collection(DECK).document(flashCard.getDeckId()).collection(FLASHCARD).document();
           flashCard.setId(docRef.getId());
           docRef.set(flashCard).addOnSuccessListener(new OnSuccessListener<Void>() {
                       @Override
                       public void onSuccess(Void unused) {
                           Log.d(TAG, "Flash Card Created");
                       }
                   })
                   .addOnFailureListener(new OnFailureListener() {
                       @Override
                       public void onFailure(@NonNull Exception e) {
                           Log.d(TAG, e.toString());
                       }
                   });

    }


    public void deleteFlashCard(FlashCardModel flashCard)
    {
        db.collection(USERS).document(email).collection(DECK)
                .document(flashCard.getDeckId()).collection(FLASHCARD).document()
                .delete().addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "Flashcard deleted");
                    }
                });
    }

    public void addToQueue(FlashCardModel flashCardModel)
    {
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();

        DocumentReference docRef = db.collection(USERS).document(email).collection(QUEUE).document();
        flashCardModel.setId(docRef.getId());
        docRef.set(flashCardModel).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Added to queue");
            }
        });
    }

    public void deleteFromQueue(FlashCardModel flashCardModel)
    {
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.collection(USERS).document(email).collection(QUEUE).document(flashCardModel.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Deleted from queue!");
            }
        });
    }


}
