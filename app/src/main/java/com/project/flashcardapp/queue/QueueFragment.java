package com.project.flashcardapp.queue;

import static com.project.flashcardapp.home.repo.DeckRepository.USERS;
import static com.project.flashcardapp.home.repo.FlashCardRepository.QUEUE;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.flashcardapp.R;
import com.project.flashcardapp.databinding.FragmentQueueBinding;
import com.project.flashcardapp.home.dto.FlashCardModel;

import java.util.Objects;


public class QueueFragment extends Fragment implements ReviewAapter.OnCardClickListener {
    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ReviewAapter adapter;
    private static final String TAG = "QueueFragment";
    private FragmentQueueBinding binding;
    private NavController navController;

    public QueueFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentQueueBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        setUpRV();
    }

    private void setUpRV() {
        if(mAuth.getCurrentUser()!=null)
        {

            String email = mAuth.getCurrentUser().getEmail();
            Log.d(TAG, email);
            assert email != null;
            CollectionReference itemRef = db.collection(USERS).document(email).collection(QUEUE);
            FirestoreRecyclerOptions<FlashCardModel> options = new FirestoreRecyclerOptions.Builder<FlashCardModel>()
                    .setQuery(itemRef, FlashCardModel.class)
                    .build();
            adapter = new ReviewAapter(options);
            adapter.setOnCardClickListener(this);
            binding.flashRecyclerView.setAdapter(adapter);
            binding.flashRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            binding.flashRecyclerView.setHasFixedSize(true);
            adapter.startListening();


        }
    }

    @Override
    public void onCardClick(DocumentSnapshot documentSnapshot, int pos) {

        String ques = Objects.requireNonNull(documentSnapshot.toObject(FlashCardModel.class)).getQuestion();
        String ans  = Objects.requireNonNull(documentSnapshot.toObject(FlashCardModel.class)).getAnswer();
        Bundle bundle = new Bundle();
        bundle.putString("QUES",ques);
        bundle.putString("ANS",ans);
        bundle.putString("ID", Objects.requireNonNull(documentSnapshot.toObject(FlashCardModel.class)).getId());
        navController.navigate(R.id.action_queueFragment_to_reviewFragment,bundle);
    }
}