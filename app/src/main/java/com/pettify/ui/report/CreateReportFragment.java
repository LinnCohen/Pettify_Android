package com.pettify.ui.report;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.pettify.R;
import com.pettify.model.PettifyApplication;

import static android.app.Activity.RESULT_OK;
import static android.app.Activity.RESULT_CANCELED;


public class CreateReportFragment extends Fragment {
    private ReportListViewModel reportListViewModel;
    private Spinner animal_type_spinner;
    private Spinner report_type_spinner;
    private Button upload_image_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_report, container, false);
        reportListViewModel = new ViewModelProvider(this).get(ReportListViewModel.class);
        upload_image_btn = view.findViewById(R.id.create_add_images);

        //animal type spinner
        animal_type_spinner = (Spinner) view.findViewById(R.id.animal_type_spinner);
        ArrayAdapter<CharSequence> animal_type_adapter = ArrayAdapter.createFromResource(PettifyApplication.context,
                R.array.animal_types_array, android.R.layout.simple_spinner_item);

        //report type spinner
        report_type_spinner = (Spinner) view.findViewById(R.id.report_type_spinner);
        ArrayAdapter<CharSequence> report_type_adapter = ArrayAdapter.createFromResource(PettifyApplication.context,
                R.array.report_types_array, android.R.layout.simple_spinner_item);

        animal_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animal_type_spinner.setAdapter(animal_type_adapter);
        report_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        report_type_spinner.setAdapter(report_type_adapter);

        upload_image_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

        return view;
    }

    private void uploadImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your profile picture");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if(resultCode != RESULT_CANCELED) {
//            switch (requestCode) {
//                case 0:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
//                        avatarImageView.setImageBitmap(selectedImage);
//                    }
//                    break;
//                case 1:
//                    if (resultCode == RESULT_OK && data != null) {
//                        Uri selectedImage =  data.getData();
//                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
//                        if (selectedImage != null) {
//                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
//                                    filePathColumn, null, null, null);
//                            if (cursor != null) {
//                                cursor.moveToFirst();
//                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
//                                String picturePath = cursor.getString(columnIndex);
//                                avatarImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
//                                cursor.close();
//                            }
//                        }
//                    }
//                    break;
//            }
//        }
//    }
}