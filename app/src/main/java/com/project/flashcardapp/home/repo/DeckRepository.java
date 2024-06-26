package com.project.flashcardapp.home.repo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.flashcardapp.home.dto.DeckModel;

public class DeckRepository {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "DeckRepository";
    public static final String DECK = "deck";

    public static final String USERS = "users";
    private String email;
    public DeckRepository() {
    }



    public void setEmail(String email)
    {
        this.email = email;
    }

    public void createDeck(DeckModel deck)
    {


        DocumentReference docRef = db.collection(USERS).document(email).collection(DECK).document();
        deck.setId( docRef.getId());
        docRef.set(deck).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Deck Created !");
            }
        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, e.toString());
                    }
                });

    }

    public void deleteDeck(DeckModel deck)
    {
        db.collection(USERS).document(email).collection(DECK).document(deck.getId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Log.d(TAG, "Deck deleted");
            }
        });
    }


}
