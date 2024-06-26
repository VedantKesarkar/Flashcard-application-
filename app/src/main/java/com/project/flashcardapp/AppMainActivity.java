package com.project.flashcardapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.project.flashcardapp.databinding.ActivityAppMainBinding;
import com.project.flashcardapp.home.domain.DeckViewModel;
import com.project.flashcardapp.home.dto.DeckModel;
import com.project.flashcardapp.home.presentation.CreateDeckDialog;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class AppMainActivity extends AppCompatActivity implements CreateDeckDialog.DeckDialogInterface {

    FirebaseAuth mAuth ;
    private ActivityAppMainBinding binding;
    private NavController navController;
    private DeckViewModel deckViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAppMainBinding.inflate(getLayoutInflater());
        View v = binding.getRoot();
        setContentView(v);
        mAuth = FirebaseAuth.getInstance();
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.main_navHost_fragment);
        assert navHostFragment != null;
         navController = navHostFragment.getNavController();
         setSupportActionBar(binding.toolbar);
        NavigationUI.setupWithNavController(binding.bottomNavigationView,navController);

        binding.bottomNavigationView.setOnItemSelectedListener(item->
        {
            NavigationUI.onNavDestinationSelected(item,navController);
            return true;

        });
        deckViewModel = new ViewModelProvider(this).get(DeckViewModel.class);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user==null)
        {
            startActivity(new Intent(AppMainActivity.this, AuthActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.log_out,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.item_logOut)
        {
            mAuth.signOut();
            startActivity(new Intent(AppMainActivity.this,AuthActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTitleSet(String title) {
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        deckViewModel.setEmail(Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
        DeckModel deck = new DeckModel();
        deck.setName(title);
        deck.setCount(0);
        deck.setDate(date);
        deckViewModel.createDeck(deck);
    }
}