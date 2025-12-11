package com.example.muralmobile.activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
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

import java.util.List;

public class ExpandCommentsDialog extends DialogFragment {

    private List<Comment> commentList;

    public ExpandCommentsDialog(List<Comment> commentList) {
        this.commentList = commentList;
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
                Toast.makeText(getContext(), commentText, Toast.LENGTH_SHORT).show();
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
