package com.example.myapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.activity.EdgeToEdge;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.LinearLayout;

public class PetChatActivity extends AppCompatActivity {
    private RecyclerView rvMessages;
    private MessageAdapter adapter;
    private List<ChatMessage> messageList;
    private TextInputEditText etMessage;
    private ImageButton btnSend, btnBack;
    private GenerativeModelFutures model;
    private LinearLayout typingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_pet_chat);
        typingIndicator = findViewById(R.id.typingIndicator);
        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {    // Sistem çubuklarını (status bar ve navigation bar) al
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets imeInsets = insets.getInsets(WindowInsetsCompat.Type.ime());

            int bottomPadding = Math.max(systemBars.bottom, imeInsets.bottom);

            v.setPadding(systemBars.left, systemBars.top, systemBars.right, bottomPadding);
            return insets;
        });
        rvMessages = findViewById(R.id.rvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);
        btnBack = findViewById(R.id.btnBack);

        messageList = new ArrayList<>();
        adapter = new MessageAdapter(messageList);
        rvMessages.setLayoutManager(new LinearLayoutManager(this));
        rvMessages.setAdapter(adapter);

        GenerativeModel gm = new GenerativeModel(
                "gemini-3-flash-preview","api key will be inserted here");
        model = GenerativeModelFutures.from(gm);

        btnBack.setOnClickListener(v -> finish());

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString().trim();
            if (!text.isEmpty()) {
                addMessage(text, true);
                etMessage.setText("");
                askPawBot(text);
            }
        });
    }
    private String getAllGoalsSummary() {
        // 1. Su Verisi (waterPrefs)
        SharedPreferences waterPrefs = getSharedPreferences("waterPrefs", MODE_PRIVATE);
        int waterDrank = waterPrefs.getInt("totalDrank", 0);
        int waterGoal = waterPrefs.getInt("dailyGoal", 2000);

        // 2. Uyku Verisi (SleepPrefs)
        SharedPreferences sleepPrefs = getSharedPreferences("SleepPrefs", MODE_PRIVATE);
        int sleptMinutes = sleepPrefs.getInt("progress", 0);
        int sleepGoalHours = sleepPrefs.getInt("goal", 0);
        float sleptHours = sleptMinutes / 60f;

        // 3. Odaklanma Verisi (FocusPrefs)
        SharedPreferences focusPrefs = getSharedPreferences("FocusPrefs", MODE_PRIVATE);
        int focusMinutes = focusPrefs.getInt("progress", 0);
        int focusGoalMinutes = focusPrefs.getInt("goal", 0);

        // 4. Adım Verisi (Tahmini - stepsPrefs)
        SharedPreferences stepsPrefs = getSharedPreferences("stepsPrefs", MODE_PRIVATE);
        int stepsTaken = stepsPrefs.getInt("todaySteps", 0);
        int stepsGoal = stepsPrefs.getInt("dailyStepsGoal", 5000);

        return String.format(Locale.getDefault(),
                "Su: %d/%d ml, Uyku: %.1f/%d saat, Odaklanma: %d/%d dk, Adım: %d/%d.",
                waterDrank, waterGoal, sleptHours, sleepGoalHours, focusMinutes, focusGoalMinutes, stepsTaken, stepsGoal);
    }

    private void addMessage(String text, boolean isUser) {
        String currentTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
        messageList.add(new ChatMessage(text, currentTime, isUser));
        adapter.notifyItemInserted(messageList.size() - 1);
        rvMessages.scrollToPosition(messageList.size() - 1);
    }

    private void askPawBot(String userPrompt) {
        String goalsSummary = getAllGoalsSummary();

        typingIndicator.setVisibility(View.VISIBLE);
        rvMessages.scrollToPosition(messageList.size() - 1);

        String systemInstruction = "Sen bilge ve tatlı bir bebek dinozorsun. " +
                "Kullanıcının verileri: " + goalsSummary + ". " +
                "KURAL 1: Hangi dilde soru sorulursa MUTLAKA o dilde cevap ver. " +
                "KURAL 2: Cevapların kısa, öz ve motive edici olsun (Hız için). " +
                "KURAL 3: Aralarda 'Rawr!', 'Grrr!' gibi dinozor sesleri çıkar! " +
                "Kullanıcı verilerine göre sağlık tavsiyeleri ver. Cevapla: " + userPrompt;

        Content content = new Content.Builder()
                .addText(systemInstruction)
                .build();

        ListenableFuture<GenerateContentResponse> response = model.generateContent(content);

        Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
            @Override
            public void onSuccess(GenerateContentResponse result) {
                runOnUiThread(() -> {
                    typingIndicator.setVisibility(View.GONE); // Cevap gelince gizle
                    addMessage(result.getText(), false);
                });
            }

            @Override
            public void onFailure(Throwable t) {
                Log.e("PawBot_Hata", "Mesaj gönderilemedi: " + t.getMessage());
                runOnUiThread(() -> {
                    typingIndicator.setVisibility(View.GONE);
                    addMessage("Rawr! Bir sorun oluştu: " + t.getMessage(), false);
                });
            }
        }, ContextCompat.getMainExecutor(this));
    }
}