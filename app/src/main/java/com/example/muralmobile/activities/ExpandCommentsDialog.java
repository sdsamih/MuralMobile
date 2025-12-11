package com.example.muralmobile.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.muralmobile.R;
import com.example.muralmobile.adapters.CommentAdapter;
import com.example.muralmobile.models.Comment;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.utils.SessionManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpandCommentsDialog extends DialogFragment {

    private List<Comment> commentList;
    private String postId;
    private OnCommentPostedListener onCommentPostedListener;

    public interface OnCommentPostedListener {
        void onCommentPosted();
    }

    public void setOnCommentPostedListener(OnCommentPostedListener listener) {
        this.onCommentPostedListener = listener;
    }

    public ExpandCommentsDialog(List<Comment> commentList, String postId) {
        this.commentList = commentList;
        this.postId = postId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_expand_comments, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.comments_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        CommentAdapter adapter = new CommentAdapter(commentList);
        recyclerView.setAdapter(adapter);

        EditText commentInput = view.findViewById(R.id.comment_input);
        ImageButton sendCommentButton = view.findViewById(R.id.send_comment_button);

        sendCommentButton.setOnClickListener(v -> {
            String commentText = commentInput.getText().toString();
            if (!commentText.isEmpty()) {
                SessionManager sessionManager = new SessionManager(getContext());
                ApiService api = RetrofitClient.getClient().create(ApiService.class);
                String token = sessionManager.getToken();
                String bearerToken = "Bearer " + token;

                Map<String, String> content = new HashMap<>();
                content.put("content", commentText);

                Call<Comment> call = api.createComment(postId, bearerToken, content);

                call.enqueue(new Callback<Comment>() {
                    @Override
                    public void onResponse(Call<Comment> call, Response<Comment> response) {
                        if (response.isSuccessful()) {
                            if (onCommentPostedListener != null) {
                                onCommentPostedListener.onCommentPosted();
                            }
                            dismiss();
                            Toast.makeText(getContext(), "Comentário adicionado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Falha ao adicionar comentário", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Comment> call, Throwable t) {
                        Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            Window window = dialog.getWindow();
            if (window != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                int height = (int) (displayMetrics.heightPixels * 0.9);
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, height);
            }
        }
    }
}
