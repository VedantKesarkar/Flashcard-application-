package com.project.flashcardapp.home.dto;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class FlashCardModel implements Parcelable {
    private String question="";
    private String nextReviewDate=" ";
    private String reviewStatus=" ";
    private String answer,deckId=" ";
    @Exclude
    private String id;

    public FlashCardModel() {
    }

    protected FlashCardModel(Parcel in) {
        question = in.readString();
        nextReviewDate = in.readString();
        reviewStatus = in.readString();
        answer = in.readString();
        deckId = in.readString();
        id = in.readString();
    }

    public static final Creator<FlashCardModel> CREATOR = new Creator<FlashCardModel>() {
        @Override
        public FlashCardModel createFromParcel(Parcel in) {
            return new FlashCardModel(in);
        }

        @Override
        public FlashCardModel[] newArray(int size) {
            return new FlashCardModel[size];
        }
    };

    public String getDeckId() {
        return deckId;
    }

    public void setDeckId(String deckId) {
        this.deckId = deckId;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }





    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getNextReviewDate() {
        return nextReviewDate;
    }

    public void setNextReviewDate(String nextReviewDate) {
        this.nextReviewDate = nextReviewDate;
    }

    public String getReviewStatus() {
        return reviewStatus;
    }

    public void setReviewStatus(String reviewStatus) {
        this.reviewStatus = reviewStatus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(nextReviewDate);
        parcel.writeString(reviewStatus);
        parcel.writeString(answer);
        parcel.writeString(deckId);
        parcel.writeString(id);
    }
}
