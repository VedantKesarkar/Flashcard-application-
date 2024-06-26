package com.project.flashcardapp.home.presentation;

import static com.project.flashcardapp.home.repo.DeckRepository.DECK;
import static com.project.flashcardapp.home.repo.DeckRepository.USERS;
import static com.project.flashcardapp.home.repo.FlashCardRepository.FLASHCARD;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.flashcardapp.R;
import com.project.flashcardapp.databinding.FragmentFlashCardBinding;
import com.project.flashcardapp.home.domain.FlashCardViewModel;
import com.project.flashcardapp.home.dto.DeckModel;
import com.project.flashcardapp.home.dto.FlashCardModel;

import java.util.Objects;


public class FlashCardFragment extends Fragment {


    private FlashCardViewModel flashCardViewModel;
    private static final String TAG = "FlashCardFragment";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private NavController navController;
    private String deckId;

    private final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FlashcardAdapter adapter;

    FragmentFlashCardBinding binding;
    public FlashCardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentFlashCardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);

        flashCardViewModel = new ViewModelProvider(requireActivity()).get(FlashCardViewModel.class);
        binding.fbCreateFlashcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deckId = requireArguments().getString("DECK_ID");
                Bundle bundle  = new Bundle();
                bundle.putString("DECK_ID",deckId);
                navController.navigate(R.id.action_flashCardFragment_to_createFlashCardFragment,bundle);

            }
        });
        setUpRV();

    }

    private void setUpRV() {
        if(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail()!=null)
        {

            String email = mAuth.getCurrentUser().getEmail();
            deckId = requireArguments().getString("DECK_ID");
            Log.d(TAG, email);
            assert email != null;
            CollectionReference itemRef = db.collection(USERS).document(email).collection(DECK).document(deckId).collection(FLASHCARD);
            FirestoreRecyclerOptions<FlashCardModel> options = new FirestoreRecyclerOptions.Builder<FlashCardModel>()
                    .setQuery(itemRef, FlashCardModel.class)
                    .build();
            adapter = new FlashcardAdapter(options);
            binding.flashRecyclerView.setAdapter(adapter);
            binding.flashRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            binding.flashRecyclerView.setHasFixedSize(true);
            adapter.startListening();


        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(adapter!=null)
            adapter.stopListening();
    }
}