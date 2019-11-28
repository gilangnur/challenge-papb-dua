package com.example.challengepapbdua;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderBoardFragment extends Fragment implements View.OnClickListener{

    private Fragment currentFragment;
    private FragmentTransaction fragmentTransaction;

    private SharedPreferences sharedPreferences;
    private ArrayList<WinnerModel> winnerModels;
    private ArrayList<WinnerModel> fixedDisplay = null;
    private RecyclerView recyclerView;
    private CardViewAdapter adapter;
    private Button resetButton;


    public LeaderBoardFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        loadData();
        recyclerView = view.findViewById(R.id.rv_history);
        adapter = new CardViewAdapter(winnerModels);
        recyclerView.setAdapter(adapter);
        Log.d("TAG_CHECK", "MASUK");
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadData();
        resetButton = getActivity().findViewById(R.id.btn_reset);
        resetButton.setOnClickListener(this);

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

        Collections.reverse(winnerModels);
        int counter = winnerModels.size() - 1;
        if (counter >= 5) {
            while (counter >= 5) {
                Log.d("TAG_TEST_COUNT", "onCreateView: "+ (counter));
                winnerModels.remove(counter);
                counter--;
            }
        }

    }

    public void refresh () {
        currentFragment = getActivity().getSupportFragmentManager().findFragmentById(this.getId());
        fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.detach(currentFragment);
        fragmentTransaction.attach(currentFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_reset) {
            refresh();
        }
    }
}
