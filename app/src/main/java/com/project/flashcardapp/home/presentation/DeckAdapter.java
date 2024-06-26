package com.project.flashcardapp.home.presentation;

import android.annotation.SuppressLint;
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
import com.project.flashcardapp.home.dto.DeckModel;

public class DeckAdapter extends FirestoreRecyclerAdapter<DeckModel, DeckAdapter.DeckViewHolder> {

private  OnDeckClickListener onDeckClickListener;


    public DeckAdapter(@NonNull FirestoreRecyclerOptions<DeckModel> options) {
        super(options);
    }

    public void setOnDeckClickListener(OnDeckClickListener onDeckClickListener) {
        this.onDeckClickListener = onDeckClickListener;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onBindViewHolder(@NonNull DeckViewHolder holder, int position, @NonNull DeckModel model) {
            holder.txt_title.setText(model.getName());
            holder.txt_date.setText(model.getDate());
    }



    @NonNull
    @Override
    public DeckViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deck_item,parent,false);
        return new DeckViewHolder(v,onDeckClickListener);
    }

    class DeckViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        TextView txt_date,txt_title;
        OnDeckClickListener onDeckClickListener;

        public DeckViewHolder(@NonNull View itemView,OnDeckClickListener onDeckClickListener) {
            super(itemView);
            txt_title = itemView.findViewById(R.id.edt_deckname_homepage);
            txt_date = itemView.findViewById(R.id.dateOfCards);
            this.onDeckClickListener = onDeckClickListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int pos = getBindingAdapterPosition();
            onDeckClickListener.onDeckClicked(getSnapshots().getSnapshot(pos),pos);
        }
    }

    public interface OnDeckClickListener
    {
        void onDeckClicked(DocumentSnapshot documentSnapshot, int pos);
    }

}
