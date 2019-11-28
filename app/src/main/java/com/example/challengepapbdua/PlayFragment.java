package com.example.challengepapbdua;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.TaskStackBuilder;
import androidx.fragment.app.Fragment;

public class PlayFragment extends Fragment implements View.OnClickListener {

    ArrayList<WinnerModel> winnerModels;
    private final long DELAY = 900;
    private final int MAX_NUMBER = 3;
    private int counter = 0;

    int yourNumber = 0;
    int computerNumber = 0;

    int yourScore = 0;
    int computerScore = 0;

    private SharedPreferences sharedPreferences;

    private TextView tvYourScore, tvComputerScore;
    private ImageView ivYourImg, ivComputerImg;
    private Button btnStart, btnStop;
    private Thread bgThreadOne, bgThreadTwo;

    public PlayFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        tvYourScore = getActivity().findViewById(R.id.tv_playerScore);
        tvComputerScore = getActivity().findViewById(R.id.tv_computerScore);
        ivYourImg = getActivity().findViewById(R.id.iv_player);
        ivComputerImg = getActivity().findViewById(R.id.iv_computer);

        loadData();
        btnStart = getActivity().findViewById(R.id.btn_start);
        btnStop = getActivity().findViewById(R.id.btn_stop);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_start) {
            runThreading();
        } else if (view.getId() == R.id.btn_stop) {
            stopThreading();
        }
    }

    private void runThreading() {
        boolean isNull = (bgThreadOne == null || bgThreadOne.getState() == Thread.State.TERMINATED)
                && (bgThreadTwo == null || bgThreadTwo.getState() == Thread.State.TERMINATED);
        if (isNull) {
            bgThreadOne = new Thread(new Runnable() {
                @Override
                public void run() {
                    runThreadEngine(ivYourImg, 0);
                }
            });
            bgThreadTwo = new Thread(new Runnable() {
                @Override
                public void run() {
                    runThreadEngine(ivComputerImg, 1);
                }
            });
        }
        bgThreadOne.start();
        bgThreadTwo.start();
    }

    private void stopThreading(){
        if (bgThreadOne != null && bgThreadTwo != null) {
            bgThreadOne.interrupt();
            bgThreadTwo.interrupt();
            counter++;
            calculatePoint(yourNumber, computerNumber);
            setScoreView();
        } if (counter == 3) {
            bgThreadOne.interrupt();
            bgThreadTwo.interrupt();
            counter = 0;
            if (yourScore > computerScore) {
                showNotification(getContext(), "HOORRAAY", "Menang Nih Suit", new Intent());
                loadData();
                winnerModels.add(new WinnerModel("WIN", String.valueOf(yourScore), String.valueOf(computerScore)));
                saveList();
                resetScoreView();
            } else if (yourScore < computerScore) {
                loadData();
                winnerModels.add(new WinnerModel("LOSE", String.valueOf(yourScore), String.valueOf(computerScore)));
                saveList();
                resetScoreView();
            }
            resetScoreView();
        }
    }

    private void saveList() {
        sharedPreferences = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(winnerModels);
        editor.putString("list", json);
        editor.apply();
    }

    private void loadData() {
        sharedPreferences = getActivity().getSharedPreferences("shared", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("list", null);
        Type type = new TypeToken<ArrayList<WinnerModel>>() {}.getType();
        winnerModels = gson.fromJson(json, type);
        Log.d("TAG_TEST", "loadData: " + gson.fromJson(json, type));

        if (winnerModels == null) {
            winnerModels = new ArrayList<>();
        }
    }


    public void resetScoreView() {
        yourScore = 0;
        computerScore = 0;
        setScoreView();
    }

    public void setScoreView() {
        tvYourScore.setText(String.valueOf(yourScore));
        tvComputerScore.setText(String.valueOf(computerScore));
    }

    private void calculatePoint (int yourNumber, int computerNumber) {
        if (yourNumber == computerNumber) {
            return;
        } else if (yourNumber == 0 && computerNumber == 2) {
            yourScore++;
            return;
        } else if (yourNumber == 1 && computerNumber == 0) {
            yourScore++;
            return;
        } else if (yourNumber == 2 && computerNumber == 1) {
            yourScore++;
            return;
        } else if (computerNumber == 0 && yourNumber == 2) {
            computerScore++;
            return;
        } else if (computerNumber == 1 && yourNumber == 0) {
            computerScore++;
            return;
        } else if (computerNumber == 2 && yourNumber == 1) {
            computerScore++;
            return;
        }
    }

    public void runThreadEngine(final ImageView iv, final int side) {
        try {
            while(!Thread.currentThread().isInterrupted()){
                Thread.sleep(DELAY);

                iv.post(new Runnable() {
                    @Override
                    public void run() {
                        changePicture(iv, randomNumber(side));
                    }
                });

            }
        } catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public int randomNumber(int side) {
        int random = new Random().nextInt(MAX_NUMBER);
        if (side == 0) {
            yourNumber = random;
        } else if (side == 1) {
            computerNumber = random;
        }
        return random;
    }

    public void changePicture(final ImageView iv, int randomNumber) {
        switch (randomNumber) {
            case 0:
                iv.setImageResource(R.drawable.batu);
                break;
            case 1:
                iv.setImageResource(R.drawable.kertas);
                break;
            case 2:
                iv.setImageResource(R.drawable.gunting);
                break;
        }
    }

    public void showNotification(Context context, String title, String body, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = "channel-01";
        String channelName = "Channel Name";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        mBuilder.setContentIntent(resultPendingIntent);

        notificationManager.notify(notificationId, mBuilder.build());
    }

}
