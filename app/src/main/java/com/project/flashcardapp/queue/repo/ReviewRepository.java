package com.project.flashcardapp.queue.repo;

import static com.project.flashcardapp.auth.domain.CreateUserProfile.USERS;
import static com.project.flashcardapp.home.repo.FlashCardRepository.QUEUE;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.flashcardapp.home.dto.FlashCardModel;

import java.util.Objects;

public class ReviewRepository {

    public static final String LEARNING = "Learning";
    public static final String GRADUATION = "Graduation";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private String email,id;
    private FlashCardModel newModel = new FlashCardModel();
    private String newInterval;

    private OnTimeInterface onTimeInterface;

    public void setId(String id) {
        this.id = id;
    }

    public void setOnTimeInterface(OnTimeInterface onTimeInterface) {
        this.onTimeInterface = onTimeInterface;
    }

    public void againClicked()
    {
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.collection(USERS).document(email)
                .collection(QUEUE).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                       FlashCardModel  model = documentSnapshot.toObject(FlashCardModel.class);
                        assert model != null;
                        newInterval = "1";
                        if( model.getNextReviewDate().equals("10") && model.getReviewStatus().equals(LEARNING))
                        {
                            model.setNextReviewDate("1");
                            newInterval = "1";


                        }
                        else if(model.getReviewStatus().equals(GRADUATION))
                        {
                            model.setNextReviewDate("1");
                            newInterval = "1";
                            model.setReviewStatus(LEARNING);

                        }

                        newModel = model;
                        onTimeInterface.onTime();
                    }
                });


    }

    public FlashCardModel getNewModel() {
        return newModel;
    }

    private void save() {
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.collection(USERS).document(email)
                .collection(QUEUE).document(id).set(newModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
    }

    public String getNewInterval() {
        return newInterval;
    }


    public void otherClicked()
    {
       // fetchData();
        email = Objects.requireNonNull(mAuth.getCurrentUser()).getEmail();
        db.collection(USERS).document(email)
                .collection(QUEUE).document(id).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        FlashCardModel model = documentSnapshot.toObject(FlashCardModel.class);
                        assert model != null;
                        if(model.getNextReviewDate().equals("1")  && model.getReviewStatus().equals(LEARNING))
                        {
                            model.setNextReviewDate("10");
                            newInterval = "10";

                        }

                        else if(model.getNextReviewDate().equals("10")  && model.getReviewStatus().equals(LEARNING))
                        {
                            model.setNextReviewDate("1");
                            newInterval = "1";
                            model.setReviewStatus(GRADUATION);
                        }
                        else {
                            int interval =   Integer.parseInt(model.getNextReviewDate());
                            interval = (int) (interval * 2.5);
                            newInterval = String.valueOf(interval);
                            model.setNextReviewDate(newInterval);
                        }

                        newModel = model;
                        onTimeInterface.onTime();

                    }
                });

    }

    public boolean checkStatus()
    {
        return newModel.getReviewStatus().equals(LEARNING);
    }


}

