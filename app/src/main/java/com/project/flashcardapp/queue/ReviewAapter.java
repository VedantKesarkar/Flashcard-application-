package com.project.flashcardapp.queue;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.project.flashcardapp.R;
import com.project.flashcardapp.home.dto.FlashCardModel;

public class ReviewAapter extends FirestoreRecyclerAdapter<FlashCardModel, ReviewAapter.FlashCardViewHolder> {


    private OnCardClickListener onCardClickListener;

    public ReviewAapter(@NonNull FirestoreRecyclerOptions<FlashCardModel> options) {
        super(options);
    }

    public void setOnCardClickListener(OnCardClickListener onCardClickListener) {
        this.onCardClickListener = onCardClickListener;
    }

    @Override
    protected void onBindViewHolder(@NonNull FlashCardViewHolder holder, int position, @NonNull FlashCardModel model) {
        holder.txt_ques.setText(model.getQuestion());
//        holder.txt_reviewDate.setText(model.getNextReviewDate());
//        holder.txt_status.setText(model.getReviewStatus());
    }

    @NonNull
    @Override
    public FlashCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.flashcard_item,parent,false);
        return  new FlashCardViewHolder(v,onCardClickListener);
    }

    class FlashCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {

        TextView txt_ques, txt_reviewDate, txt_status;

        OnCardClickListener onCardClickListener;
        public FlashCardViewHolder(@NonNull View itemView,OnCardClickListener onCardClickListener) {
            super(itemView);
            txt_ques = itemView.findViewById(R.id.txtRVCardQuestion);

            this.onCardClickListener = onCardClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getBindingAdapterPosition();
            onCardClickListener.onCardClick(getSnapshots().getSnapshot(pos),pos);
        }



    }

    public interface OnCardClickListener
    {
        void onCardClick(DocumentSnapshot documentSnapshot, int pos);
    }
}

