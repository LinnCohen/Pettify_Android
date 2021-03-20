package com.pettify.ui.report;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.pettify.R;
import com.pettify.model.PettifyApplication;
import com.pettify.model.report.Report;
import com.pettify.model.report.ReportModel;

import org.w3c.dom.Text;

import static android.app.Activity.RESULT_OK;
import static android.app.Activity.RESULT_CANCELED;


public class CreateReportFragment extends Fragment {
    ReportListViewModel reportListViewModel;
    TextView report_title;
    TextView report_description;
    TextView report_address;
    String report_animal_type;
    String report_type;
    Spinner animal_type_spinner;
    Spinner report_type_spinner;
    Button upload_image_btn;
    Button submit_btn;
    ImageView reportImageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_create_report, container, false);
        reportListViewModel = new ViewModelProvider(this).get(ReportListViewModel.class);
        upload_image_btn = view.findViewById(R.id.create_add_images);
        reportImageView = view.findViewById(R.id.create_image_ph);
        submit_btn = view.findViewById(R.id.create_report_btn);
        report_title = view.findViewById(R.id.create_report_title);
        report_description = view.findViewById(R.id.create_report_desc);
        report_address = view.findViewById(R.id.create_address);

        submit_btn.setOnClickListener(view1 -> addReport());

        //animal type spinner
        animal_type_spinner = view.findViewById(R.id.animal_type_spinner);
        ArrayAdapter<CharSequence> animal_type_adapter = ArrayAdapter.createFromResource(PettifyApplication.context,
                R.array.animal_types_array, android.R.layout.simple_spinner_item);
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

        //report type spinner
        report_type_spinner = view.findViewById(R.id.report_type_spinner);
        ArrayAdapter<CharSequence> report_type_adapter = ArrayAdapter.createFromResource(PettifyApplication.context,
                R.array.report_types_array, android.R.layout.simple_spinner_item);
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
        });;

        upload_image_btn.setOnClickListener(v -> uploadImage());

        return view;
    }

    private void addReport() {
        submit_btn.setEnabled(false);
        Log.d("TAG", String.valueOf(reportListViewModel.getReports().getValue().size()));
        int id = reportListViewModel.getReports().getValue().size();
        Report report = new Report();
        report.setId(""+id);
        report.setDescription(report_description.getText().toString());
        ReportModel.instance.addReport(report, () -> reloadData());
    }

    //TODO - direct to view report page
    void reloadData(){
        submit_btn.setEnabled(false);
        ReportModel.instance.refreshAllReports(() -> {
            submit_btn.setEnabled(true);
        });
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    if (resultCode == RESULT_OK && data != null) {
                        Bitmap selectedImage = (Bitmap) data.getExtras().get("data");
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
}