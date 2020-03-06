package com.example.disnap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.example.disnap.ui.analyze.AnalyzeActivity;
import com.example.disnap.ui.bottomsheetdialog.ActionBottomDialogFragment;
import com.example.disnap.ui.base.BaseActivity;
import com.example.disnap.ui.history.HistoryFragment;
import com.example.disnap.ui.home.HomeFragment;
import com.example.disnap.ui.snap.SnapFragment;
import com.example.disnap.util.Constants;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.isfaaghyth.rak.Rak;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static int selectedMenuId;
    private static Context context;
    private static MainActivity mainActivity;
    private androidx.fragment.app.Fragment home, history;
    private static BottomNavigationView bottomNavigationView;
    private static Fragment selectedFragment;
    private static FragmentTransaction fragmentTransaction;
    private SnapFragment snapFragment;
    private static ActionBottomDialogFragment fragment = new ActionBottomDialogFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Rak.initialize(getApplicationContext());
        AndroidNetworking.initialize(getApplicationContext());

        findViews();
        initViews();
        initListeners();
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
    }

    @Override
    public void findViews() {
        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
    }

    @Override
    public void initViews() {
        home = new HomeFragment();
        history = new HistoryFragment();
        snapFragment = new SnapFragment();
        bottomNavigationView.setSelectedItemId(R.id.menu_home);
    }

    @Override
    public void initListeners() {
        selectedMenuId = R.id.menu_home;
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.menu_home:
                        selectedFragment = home;
                        break;
                    case R.id.menu_snap:
                       // showBottomSheet();
                        //showBottomSheetFragment();
                        selectedFragment = snapFragment;
                        showBottomSheetFragment();
                        //selectedFragment = snapFragment;
                        //showBottomSheet();

                        break;
                    case R.id.menu_history:
                        selectedFragment = history;
                        break;
                }
                selectedMenuId  = menuItem.getItemId();
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container, selectedFragment);
                fragmentTransaction.commit();
                return true;
            }
        });
    }


    public void showBottomSheetFragment(){
        fragment.show(getSupportFragmentManager(), fragment.getTag());
        fragment.setCancelable(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("epe0", "onActivityResult: "+resultCode);
        Log.d("epe01", "onActivityResult: "+resultCode);
        if (resultCode == RESULT_OK){
            Log.d("epe1", resultCode +"");
            if (requestCode == Constants.GALERY_REQUEST){
                Log.d("epe2",requestCode+"");
                Uri a = data.getData();
                Log.d("epe3",a.toString());
                startCrop(a);
            }else if (requestCode == Constants.CAMERA_REQUEST){
                Log.d("epe4",requestCode+"");
                Uri a = data.getData();
                //Log.d("epe5",a.toString());
                startCrop(a);
            } else if (requestCode == Constants.CROP_REQUEST) {
                Log.d("epe6",requestCode+"");
                Uri c = UCrop.getOutput(data);
                String imgUri = getRealPathFromUri(c);
                Log.d("epe7",c.toString());
                Intent intent = new Intent(this, AnalyzeActivity.class);
                intent.putExtra("imageUri", imgUri);
                startActivity(intent);
                this.finish();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Ada yang salah", Toast.LENGTH_SHORT).show();
        }
    }

    private String getRealPathFromUri(Uri uri){
        String result;
        Cursor cursor = getApplicationContext().getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    private void startCrop(@NonNull Uri uri){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        UCrop uCrop = UCrop.of(uri, Uri.fromFile(new File(getApplicationContext().getCacheDir(), imageFileName)));
        uCrop.withAspectRatio(1,1);
        uCrop.withMaxResultSize(450, 450);
        uCrop.start(MainActivity.this, Constants.CROP_REQUEST);
        Log.d("apa?", "crop ok");
    }
}
