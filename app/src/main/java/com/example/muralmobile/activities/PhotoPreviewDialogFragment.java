package com.example.muralmobile.activities;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.muralmobile.R;
import com.example.muralmobile.services.ApiService;
import com.example.muralmobile.services.RetrofitClient;
import com.example.muralmobile.utils.SessionManager;

import java.io.ByteArrayOutputStream;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotoPreviewDialogFragment extends DialogFragment {

    private static final String ARG_PHOTO_BITMAP = "photo_bitmap";
    private Bitmap photoBitmap;

    public static PhotoPreviewDialogFragment newInstance(Bitmap photoBitmap) {
        PhotoPreviewDialogFragment fragment = new PhotoPreviewDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_PHOTO_BITMAP, photoBitmap);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            photoBitmap = getArguments().getParcelable(ARG_PHOTO_BITMAP);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_photo_preview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ImageView photoPreviewImage = view.findViewById(R.id.photo_preview_image);
        EditText captionEditText = view.findViewById(R.id.caption_edit_text);
        Button sendButton = view.findViewById(R.id.send_button);

        if (photoBitmap != null) {
            photoPreviewImage.setImageBitmap(photoBitmap);
        }

        sendButton.setOnClickListener(v -> {
            String caption = captionEditText.getText().toString();

            SessionManager sessionManager = new SessionManager(getContext());
            String authToken = sessionManager.getToken();

            if (authToken == null) {
                Toast.makeText(getContext(), "Authentication token not found.", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestBody captionBody = RequestBody.create(MediaType.parse("text/plain"), caption);
            RequestBody publicBody = RequestBody.create(MediaType.parse("text/plain"), "true");

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), byteArray);
            MultipartBody.Part mediaPart = MultipartBody.Part.createFormData("media", "image.png", requestFile);

            ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
            Call<Void> call = apiService.createPost("Bearer " + authToken, captionBody, publicBody, mediaPart);

            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Post created successfully!", Toast.LENGTH_SHORT).show();
                        dismiss();
                    } else {
                        Toast.makeText(getContext(), "Failed to create post.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(getContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
