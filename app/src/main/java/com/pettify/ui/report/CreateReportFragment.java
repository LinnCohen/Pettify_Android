package com.pettify.ui.report;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.pettify.R;
import com.pettify.model.listeners.*;
import com.pettify.utilities.LocationUtils;
import com.pettify.model.PettifyApplication;
import com.pettify.model.report.Report;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.app.Activity.RESULT_CANCELED;

public class CreateReportFragment extends Fragment {
    ReportListViewModel reportListViewModel;
    TextView report_title;
    TextView report_description;
    TextView report_address;
    String report_animal_type;
    String report_type;
    String lat;
    String lng;
    Spinner animal_type_spinner;
    Spinner report_type_spinner;
    Button upload_image_btn;
    Button submit_btn;
    ImageView reportImageView;
    Report existingReport;
    String reportId;
    View view;
    ProgressBar submit_progress_bar;
    ProgressBar image_progress_bar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        reportListViewModel =
                new ViewModelProvider(this).get(ReportListViewModel.class);
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_create_report, container, false);
        reportListViewModel = new ViewModelProvider(this).get(ReportListViewModel.class);

        reportId = ViewReportFragmentArgs.fromBundle(getArguments()).getReportId();

        submit_progress_bar = view.findViewById(R.id.create_report_pb);
        submit_progress_bar.setVisibility(View.INVISIBLE);
        image_progress_bar = view.findViewById(R.id.create_report_image_pb);

        animal_type_spinner = view.findViewById(R.id.animal_type_spinner);
        ArrayAdapter<CharSequence> animal_type_adapter = ArrayAdapter.createFromResource(PettifyApplication.context,
                R.array.animal_types_array, android.R.layout.simple_spinner_item);

        report_type_spinner = view.findViewById(R.id.report_type_spinner);
        ArrayAdapter<CharSequence> report_type_adapter = ArrayAdapter.createFromResource(PettifyApplication.context,
                R.array.report_types_array, android.R.layout.simple_spinner_item);

        if (reportId != null) {
            Log.d("TAG", reportId);
            reportListViewModel.getReport(reportId, new Listener<Report>() {
                @Override
                public void onComplete(Report report) {
                    existingReport = report;
                    report_title.setText(report.getTitle());
                    report_description.setText(report.getDescription());
                    report_address.setText(report.getAddress());
                    Log.d("TAG", "report: " + report.toString());
                    int animalSpinnerPosition = animal_type_adapter.getPosition(report.getAnimal_type());
                    animal_type_spinner.setSelection(animalSpinnerPosition);
                    int reportTypeSpinnerPosition = report_type_adapter.getPosition(report.getReport_type());
                    report_type_spinner.setSelection(reportTypeSpinnerPosition);
                    if (report.getImage_url() != null) {
                        Picasso.get().load(report.getImage_url()).into(reportImageView, new Callback() {
                            @Override
                            public void onSuccess() {
                                image_progress_bar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Exception e) { }
                        });
                    }
                }
            });
        } else {
            image_progress_bar.setVisibility(View.GONE);
        }

        report_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        report_type_spinner.setAdapter(report_type_adapter);
        report_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: report_type = parent.getItemAtPosition(0).toString(); break;
                    case 1: report_type = parent.getItemAtPosition(1).toString(); break;

                }
            }
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        animal_type_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        animal_type_spinner.setAdapter(animal_type_adapter);
        animal_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0: report_animal_type = parent.getItemAtPosition(0).toString(); break;
                    case 1: report_animal_type = parent.getItemAtPosition(1).toString(); break;
                    case 2: report_animal_type = parent.getItemAtPosition(2).toString(); break;
                    case 3: report_animal_type = parent.getItemAtPosition(3).toString(); break;
                }
            }
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        upload_image_btn = view.findViewById(R.id.create_add_images);
        reportImageView = view.findViewById(R.id.create_image_ph);
        submit_btn = view.findViewById(R.id.create_report_btn);
        report_title = view.findViewById(R.id.create_title_text);
        report_description = view.findViewById(R.id.create_desc_text);
        report_address = view.findViewById(R.id.create_report_address);

        submit_btn.setOnClickListener(v -> {
            addReport();
//            Navigation.findNavController(getView()).popBackStack();
        });

        upload_image_btn.setOnClickListener(v -> uploadImage());
        LatLng location = LocationUtils.instance.getCurrentLocation();
        lat = String.valueOf(LocationUtils.instance.getLat());
        lng = String.valueOf(LocationUtils.instance.getLng());
        report_address.setText(LocationUtils.instance.getAddressName(getContext(), location));
        report_address.setEnabled(false);
        Log.d("location","from onViewCreated" + lat + lng);
        return view;
    }

    private void addReport() {
        submit_progress_bar.setVisibility(View.VISIBLE);
        Date date = new Date();
        submit_btn.setEnabled(false);
        lat = String.valueOf(LocationUtils.instance.getLat());
        lng = String.valueOf(LocationUtils.instance.getLng());
        Log.d("location","from AddReport" + lat + lng);
        Report report = new Report();
        report.setDescription(report_description.getText().toString());
        report.setTitle(report_title.getText().toString());
        report.setAddress(report_address.getText().toString());
        report.setAnimal_type(report_animal_type);
        report.setLat(lat);
        report.setLng(lng);
        report.setReport_type(report_type);
        BitmapDrawable drawable = (BitmapDrawable)reportImageView.getDrawable();

        if (TextUtils.isEmpty(report_title.getText()) || TextUtils.isEmpty(report_description.getText()) || drawable == null ||
                reportImageView.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.images).getConstantState()) {
            TextView error = view.findViewById(R.id.create_report_error_msg);
            submit_progress_bar.setVisibility(View.INVISIBLE);
            error.setVisibility(View.VISIBLE);
            submit_btn.setEnabled(true);
        } else {
            Bitmap bitmap = drawable.getBitmap();
            reportListViewModel.uploadImage(bitmap, "report_image" + date.getTime(), url -> {
                if (url == null) {
                    submit_progress_bar.setVisibility(View.INVISIBLE);
                    displayFailedError();
                } else {
                    report.setImage_url(url);
                    CreateReportFragment.this.addOrEditReport(report);
                }
            });
        }
    }

    private void addOrEditReport(Report report) {
        if (existingReport != null) {
            reportListViewModel.updateReport(report, reportId, () -> {
                NavController navController = Navigation.findNavController(getView());
                navController.navigateUp();
            });
        } else {
            reportListViewModel.addReport(report, () -> {
                NavController navController = Navigation.findNavController(getView());
                navController.navigateUp();
                navController.navigate(R.id.reportslist_list);
            });
        }
    }

    private void displayFailedError() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Operation Failed");
        builder.setMessage("Saving image failed, please try again later...");
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void uploadImage() {
        final CharSequence[] options = { "Take Photo", "Choose from Gallery", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose your report picture");
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = rotateImage((Bitmap) data.getExtras().get("data"));
                        reportImageView.setImageBitmap(selectedImage);
                    }
                    break;
                case 1:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri selectedImage =  data.getData();
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        if (selectedImage != null) {
                            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                                    filePathColumn, null, null, null);
                            if (cursor != null) {
                                cursor.moveToFirst();
                                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                                String picturePath = cursor.getString(columnIndex);
                                reportImageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                                cursor.close();
                            }
                        }
                    }
                    break;
            }
        }
    }

    public static Bitmap rotateImage(Bitmap source) {
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }
}