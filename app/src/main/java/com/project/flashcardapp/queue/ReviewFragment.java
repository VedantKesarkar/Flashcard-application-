package com.project.flashcardapp.queue;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.project.flashcardapp.AlertReceiver;
import com.project.flashcardapp.R;
import com.project.flashcardapp.databinding.FragmentReviewBinding;
import com.project.flashcardapp.home.dto.FlashCardModel;
import com.project.flashcardapp.home.repo.FlashCardRepository;
import com.project.flashcardapp.queue.repo.OnTimeInterface;
import com.project.flashcardapp.queue.repo.ReviewRepository;

import java.util.Calendar;


public class ReviewFragment extends Fragment implements OnTimeInterface {

private FragmentReviewBinding binding;

private String previousText,question,answer,id;

private ReviewRepository repo  = new ReviewRepository();

private AlarmManager alarmManager;
private PendingIntent pendingIntent;
private FlashCardModel model;
private FlashCardRepository repository = new FlashCardRepository();
private NavController navController;
    public ReviewFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReviewBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        question = requireArguments().getString("QUES");
        answer = requireArguments().getString("ANS");
        id = requireArguments().getString("ID");
        repo.setId(id);
        repo.setOnTimeInterface(this);

        binding.txtQuestion.setText(question);
        binding.imgBtnFlipCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flipCard();
            }


        });


        binding.btnAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repo.againClicked();
                navController.navigate(R.id.action_reviewFragment_to_queueFragment);

            }
        });


        binding.btnHard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repo.otherClicked();
                navController.navigate(R.id.action_reviewFragment_to_queueFragment);
            }
        });


        binding.btnGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repo.otherClicked();
                navController.navigate(R.id.action_reviewFragment_to_queueFragment);
            }
        });


        binding.btnEasy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repo.otherClicked();
                navController.navigate(R.id.action_reviewFragment_to_queueFragment);
            }
        });


        binding.imgBtnFlipCard.setVisibility(View.VISIBLE);
    }

    private void setTimer() {
        int interval = Integer.parseInt(repo.getNewInterval());
        model = repo.getNewModel();
        if(repo.checkStatus())
        {
            Calendar c = Calendar.getInstance();
            int min = c.get(Calendar.MINUTE)+interval;
            c.set(Calendar.MINUTE,min);
            c.set(Calendar.SECOND,0);
            startAlarm(c,model);
        }
        else
        {
            Calendar c = Calendar.getInstance();
            int day = c.get(Calendar.DAY_OF_MONTH)+interval;
            c.set(Calendar.DAY_OF_MONTH,day);
            c.set(Calendar.HOUR_OF_DAY,0);
            startAlarm(c,model);
        }

    }

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

    public void flipCard() {
            if (binding.flipCardView.getAnimation() != null && binding.flipCardView.getAnimation().hasStarted()) {
                // Animation is already running, so return
                return;
            }
            final boolean isFrontVisible = binding.flipCardView.getRotationY() == 0;
            ObjectAnimator flipAnimator = ObjectAnimator.ofFloat(binding.flipCardView, "rotationY", isFrontVisible ? 180f : 0f, isFrontVisible ? 0f : 180f);
            flipAnimator.setDuration(500);
            flipAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(@NonNull Animator animation) {
                    binding.txtQuestion.setText("");
                }

                @Override
                public void onAnimationEnd(@NonNull Animator animation) {
                    if(previousText == null){
                        previousText = binding.txtQuestion.getText().toString();
                        binding.txtQuestion.setText(answer);
                    }
                    else{
                        binding.txtQuestion.setText(previousText);
                        previousText = null;
                    }
                    binding.imgBtnFlipCard.setVisibility(View.INVISIBLE);
                }
                @Override
                public void onAnimationCancel(@NonNull Animator animation) {
                }
                @Override
                public void onAnimationRepeat(@NonNull Animator animation) {
                }
            });
            flipAnimator.start();

        }


    @Override
    public void onTime() {
        setTimer();
        repository.deleteFromQueue(model);
    }
}