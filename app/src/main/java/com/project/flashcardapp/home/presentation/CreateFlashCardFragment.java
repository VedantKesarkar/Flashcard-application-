package com.project.flashcardapp.home.presentation;

import static com.project.flashcardapp.home.repo.FlashCardRepository.FLASHCARD;
import static com.project.flashcardapp.queue.repo.ReviewRepository.LEARNING;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.flashcardapp.AlertReceiver;
import com.project.flashcardapp.R;
import com.project.flashcardapp.databinding.FragmentCreateFlashCardBinding;
import com.project.flashcardapp.home.domain.FlashCardViewModel;
import com.project.flashcardapp.home.dto.FlashCardModel;

import java.util.Calendar;




public class CreateFlashCardFragment extends Fragment {

    FragmentCreateFlashCardBinding binding;
    private FlashCardViewModel flashCardViewModel;
    private NavController navController;
    private String question, answer;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private static final String TAG = "CreateFlashCardFragment";

    public CreateFlashCardFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentCreateFlashCardBinding.inflate(inflater,container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        flashCardViewModel = new ViewModelProvider(requireActivity()).get(FlashCardViewModel.class);
        binding.flashCardDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                question = binding.edtQuestion.getText().toString();
                answer = binding.edtAnswer.getText().toString();
                String deckId = requireArguments().getString("DECK_ID");
                FlashCardModel flashCardModel = new FlashCardModel();
                flashCardModel.setQuestion(question);
                flashCardModel.setAnswer(answer);
                flashCardModel.setNextReviewDate("1");
                flashCardModel.setReviewStatus(LEARNING);
                flashCardModel.setDeckId(deckId);
                flashCardViewModel.createFlashCard(flashCardModel);
                flashCardModel.setDeckId(deckId);
                Log.d(TAG, deckId);

                Bundle bundle  = new Bundle();
                bundle.putString("DECK_ID",deckId);
                setTimer(flashCardModel);
                navController.navigate(R.id.action_createFlashCardFragment_to_flashCardFragment,bundle);

            }
        });

    }

    private void setTimer(FlashCardModel model)
    {
        Calendar c = Calendar.getInstance();
        int min = c.get(Calendar.MINUTE)+1;
        c.set(Calendar.MINUTE,min);
        c.set(Calendar.SECOND,0);
        startAlarm(c,model);

    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private void startAlarm(Calendar c,FlashCardModel model) {
        alarmManager = (AlarmManager) requireActivity().getSystemService(Context.ALARM_SERVICE);
        Intent i = new Intent(requireActivity(), AlertReceiver.class);
        i.putExtra("ID",model.getId());
        i.putExtra("QUES",model.getQuestion());
        i.putExtra("ANS",model.getAnswer());
        i.putExtra("DATE",model.getNextReviewDate());
        i.putExtra("STATUS",model.getReviewStatus());
        i.putExtra("DECK_ID",model.getDeckId());
        pendingIntent = PendingIntent.getBroadcast(requireActivity(),Integer.parseInt(String.valueOf(System.currentTimeMillis()%10000)),i,PendingIntent.FLAG_IMMUTABLE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);

    }





}