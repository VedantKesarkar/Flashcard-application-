package com.project.flashcardapp.home.presentation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.project.flashcardapp.R;
import com.project.flashcardapp.home.dto.FlashCardModel;

public class FlashcardAdapter extends FirestoreRecyclerAdapter<FlashCardModel, FlashcardAdapter.FlashCardViewHolder> {


    public FlashcardAdapter(@NonNull FirestoreRecyclerOptions<FlashCardModel> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FlashCardViewHolder holder, int position, @NonNull FlashCardModel model) {
        holder.txt_ques.setText(model.getQuestion());

    }

    @NonNull
    @Override
    public FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_item,parent,false);
        return  new FlashCardViewHolder(v);
    }

     class FlashCardViewHolder extends RecyclerView.ViewHolder
    {

        TextView txt_ques;

        public FlashCardViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_ques = itemView.findViewById(R.id.txtRVCardQuestion);

        }
    }
}
