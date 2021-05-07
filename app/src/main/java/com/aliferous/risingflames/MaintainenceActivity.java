package com.aliferous.risingflames;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcode;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetector;
import com.google.firebase.ml.vision.barcode.FirebaseVisionBarcodeDetectorOptions;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.otaliastudios.cameraview.CameraView;
import com.otaliastudios.cameraview.frame.Frame;
import com.otaliastudios.cameraview.frame.FrameProcessor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import Adapter.Do_Dont_Adapter;
import Adapter.Sensor_Adapter;
import Model.Do_Dont;
import Model.Sensor;
import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MaintainenceActivity extends AppCompatActivity {

    FirebaseUser firebaseUser;

    private RecyclerView sensor_recycler;
    private Sensor_Adapter sensor_adapter;
    private List<Sensor> mSensor;

    TextView new_sensor,heading,add_sensor,sensor_ID,sensor_date;
    BlurView background_blur;
    ConstraintLayout constraintLayout_2;
    ImageView Scan_Id;

    EditText sensor_location;

    DatabaseReference reference;

    //camera
    CameraView camera_View;
    FirebaseVisionBarcodeDetectorOptions options;
    FirebaseVisionBarcodeDetector detector;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintainence);

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/YYYY");
        final String saveCurrentDate = currentDate.format(calendar.getTime());

        sensor_recycler = findViewById(R.id.sensor_recycler);
        new_sensor = findViewById(R.id.new_sensor);
        background_blur = findViewById(R.id.background_blur);
        heading = findViewById(R.id.Sensor_Heading);
        constraintLayout_2 = findViewById(R.id.constraintLayout_2);
        add_sensor = findViewById(R.id.add_new_sensor);
        sensor_location = findViewById(R.id.et_location);
        sensor_date = findViewById(R.id.et_date);
        sensor_ID = findViewById(R.id.sensor_id_tv);
        frame = findViewById(R.id.frame);
        frame.setVisibility(View.GONE);
        Scan_Id = findViewById(R.id.Scan_camera);

        sensor_date.setText(saveCurrentDate);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        blurBackground();

        add_sensor.setPaintFlags(add_sensor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        new_sensor.setPaintFlags(new_sensor.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        new_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final ObjectAnimator oa1 = ObjectAnimator.ofFloat(constraintLayout_2, "alpha", 0f, 1f);
                oa1.setDuration(500);
                oa1.setInterpolator(new AccelerateDecelerateInterpolator());
                sensor_recycler.setVisibility(View.GONE);
                heading.setVisibility(View.GONE);
                new_sensor.setVisibility(View.GONE);
                background_blur.setVisibility(View.VISIBLE);
                constraintLayout_2.setVisibility(View.VISIBLE);
                oa1.start();


            }
        });

        mSensor = new ArrayList<>();

        sensor_recycler.setHasFixedSize(true);
        sensor_recycler.setLayoutManager(new LinearLayoutManager(MaintainenceActivity.this));

        read_sensor();



        add_sensor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Sensor List");

                String txt_location = Objects.requireNonNull(sensor_location.getText()).toString().trim();
                String txt_ID= Objects.requireNonNull(sensor_ID.getText()).toString().trim();
                String txt_date = Objects.requireNonNull(sensor_date.getText()).toString().trim();

                if(TextUtils.isEmpty(txt_location)  ||  TextUtils.isEmpty(txt_ID) ||  TextUtils.isEmpty(txt_date))
                {
                    Toast.makeText(MaintainenceActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (int i=1;i<100;i++){
                                if (!(snapshot.hasChild(""+i))){
                                    reference.child(""+i).child("Status").setValue("0");
                                    reference.child(""+i).child("Name").setValue(""+sensor_location.getText()).toString().trim();
                                    reference.child(""+i).child("ID").setValue(""+sensor_ID.getText()).toString().trim();
                                    reference.child(""+i).child("Date").setValue(""+sensor_date.getText()).toString().trim();
                                    Toast.makeText(MaintainenceActivity.this, "Sensor Added Successfully", Toast.LENGTH_SHORT).show();
                                    constraintLayout_2.setVisibility(View.GONE);
                                    background_blur.setVisibility(View.GONE);
                                    sensor_recycler.setVisibility(View.VISIBLE);
                                    heading.setVisibility(View.VISIBLE);
                                    new_sensor.setVisibility(View.VISIBLE);
                                    break;
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                }
            }
        });

        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {setupCamera();}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
        }).check();

    }

    private void blurBackground() {

        float radius = 2f;

        View decorView = getWindow().getDecorView();
        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        background_blur.setupWith(rootView)
                .setFrameClearDrawable(windowBackground)
                .setBlurAlgorithm(new RenderScriptBlur(this))
                .setBlurRadius(radius)
                .setBlurAutoUpdate(true)
                .setHasFixedTransformationMatrix(true);
    }

    private void read_sensor() {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid()).child("Sensor List");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                mSensor.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Sensor sensor = snapshot.getValue(Sensor.class);


                    if (sensor != null && sensor.getName() != null) {
                        mSensor.add(sensor);
                    }

                }

                sensor_adapter = new Sensor_Adapter(MaintainenceActivity.this,mSensor,false);
                sensor_recycler.setAdapter(sensor_adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }




    private void setupCamera()
    {


        Scan_Id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

                frame.setVisibility(View.VISIBLE);

            }
        });

        camera_View = (CameraView) findViewById(R.id.cameraview);
        camera_View.setLifecycleOwner(this);
        camera_View.addFrameProcessor(new FrameProcessor() {
            @Override
            public void process(@NonNull Frame frame) {
                processImage(getVisionImagefromFrame(frame));
            }
        });
        options = new FirebaseVisionBarcodeDetectorOptions.Builder()
                .setBarcodeFormats(FirebaseVisionBarcode.FORMAT_QR_CODE)
                .build();
        detector = FirebaseVision.getInstance().getVisionBarcodeDetector(options);
    }


    private void processImage(FirebaseVisionImage image)
    {

        detector.detectInImage(image)
                .addOnSuccessListener(new OnSuccessListener<List<FirebaseVisionBarcode>>() {
                    @Override
                    public void onSuccess(List<FirebaseVisionBarcode> firebaseVisionBarcodes) {
                        processResult(firebaseVisionBarcodes);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MaintainenceActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void processResult(List<FirebaseVisionBarcode> firebaseVisionBarcodes)
    {
        if(firebaseVisionBarcodes.size() > 0)
        {

            for(FirebaseVisionBarcode item:firebaseVisionBarcodes)
            {

                int value_type = item.getValueType();
                switch(value_type)
                {
                    case FirebaseVisionBarcode.TYPE_TEXT:
                    {

                        final String text_1 = item.getRawValue();
                        sensor_ID.setText(text_1);
                        frame.setVisibility(View.GONE);

                    }
                    break;
                    default:
                        break;
                }
            }
        }
    }

    private FirebaseVisionImage getVisionImagefromFrame(Frame frame)
    {
        byte[] data = frame.getData();
        FirebaseVisionImageMetadata metadata = new FirebaseVisionImageMetadata.Builder()
                .setFormat(FirebaseVisionImageMetadata.IMAGE_FORMAT_NV21)
                .setHeight(frame.getSize().getHeight())
                .setWidth(frame.getSize().getWidth())
                //.setRotation(frame.getRotation())  //for landscape mode
                .build();
        return FirebaseVisionImage.fromByteArray(data,metadata);
    }

}