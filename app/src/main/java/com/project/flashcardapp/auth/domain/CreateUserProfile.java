package com.project.flashcardapp.auth.domain;

import com.google.firebase.firestore.FirebaseFirestore;

public class CreateUserProfile {
    public static final String USERS = "users";
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final OnUserInit onUserInit;
    public CreateUserProfile(OnUserInit onUserInit) {
        this.onUserInit = onUserInit;
    }

    public void init(String email)
    {
        Prof prof = new Prof(email);
        db.collection(USERS).document(email).set(prof).addOnSuccessListener(unused -> onUserInit.userRegistered());
    }
}
