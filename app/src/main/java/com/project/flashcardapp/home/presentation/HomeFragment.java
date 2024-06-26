package com.project.flashcardapp.home.presentation;

import static com.project.flashcardapp.home.repo.DeckRepository.DECK;
import static com.project.flashcardapp.home.repo.DeckRepository.USERS;

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
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.project.flashcardapp.R;
import com.project.flashcardapp.databinding.FragmentHomeBinding;
import com.project.flashcardapp.home.domain.FlashCardViewModel;
import com.project.flashcardapp.home.dto.DeckModel;
import com.project.flashcardapp.home.presentation.CreateDeckDialog;

import java.util.Objects;

public class HomeFragment extends Fragment implements DeckAdapter.OnDeckClickListener {

    FragmentHomeBinding binding;
    private DeckAdapter deckAdapter;
    private NavController navController;
    private String email;
    private FlashCardViewModel flashCardViewModel;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private static final String TAG = "HomeFragment";
    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        flashCardViewModel = new ViewModelProvider(this).get(FlashCardViewModel.class);

        binding.fbCreateDeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CreateDeckDialog creationDialog = new CreateDeckDialog();
                creationDialog.show(requireActivity().getSupportFragmentManager(),"Create Category Selection");
            }
        });
        setupRV();
    }

    private void setupRV() {
        if(mAuth.getCurrentUser()!=null)
        {
            email = mAuth.getCurrentUser().getEmail();
            CollectionReference itemRef = db.collection(USERS).document(email).collection(DECK);
            FirestoreRecyclerOptions<DeckModel> options = new FirestoreRecyclerOptions.Builder<DeckModel>()
                    .setQuery(itemRef.orderBy("name"), DeckModel.class)
                    .build();
            deckAdapter = new DeckAdapter(options);
            binding.flashRecyclerView.setAdapter(deckAdapter);
            deckAdapter.setOnDeckClickListener(this);
            binding.flashRecyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
            binding.flashRecyclerView.setHasFixedSize(true);
            deckAdapter.startListening();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(deckAdapter!=null)
            deckAdapter.stopListening();

    }

    @Override
    public void onDeckClicked(DocumentSnapshot documentSnapshot, int pos) {
        String deckId = Objects.requireNonNull(documentSnapshot.toObject(DeckModel.class)).getId();
//        flashCardViewModel.setDeckId(deckId);
        Bundle bundle  = new Bundle();
        bundle.putString("DECK_ID",deckId);
        navController.navigate(R.id.action_homeFragment_to_flashCardFragment,bundle);
    }
}