package com.example.muralmobile.activities;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.muralmobile.R;

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
        if (photoBitmap != null) {
            photoPreviewImage.setImageBitmap(photoBitmap);
        }
    }
}
