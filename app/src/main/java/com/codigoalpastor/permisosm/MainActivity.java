package com.codigoalpastor.permisosm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnCameraPermission;
    private Button btnSensorPermission;
    private Button btnMultiplePermissions;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    private static final int SENSOR_PERMISSION_REQUEST_CODE = 2;
    private static final int MULTIPLE_PERMISSIONS_REQUEST_CODE = 3;
    private String[] permissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.BODY_SENSORS};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCameraPermission = (Button) findViewById(R.id.btnCameraPermission);
        btnSensorPermission = (Button) findViewById(R.id.btnSensorPermission);
        btnMultiplePermissions = (Button) findViewById(R.id.btnMultiplePermissions);
        btnCameraPermission.setOnClickListener(this);
        btnSensorPermission.setOnClickListener(this);
        btnMultiplePermissions.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCameraPermission:
                //Verifica si el permiso de la cámara no está concedido
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //Si el permiso no se encuentra concedido se solicita
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    //Si el permiso esá concedico prosigue con el flujo normal
                    permissionGranted();
                }
                break;
            case R.id.btnSensorPermission:
                //Verifica si el permiso de los sensores no está concedido
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
                    //Si el permiso no se encuentra concedido se solicita
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.BODY_SENSORS}, SENSOR_PERMISSION_REQUEST_CODE);
                } else {
                    //Si el permiso está concedido prosigue con el flujo normal
                    permissionGranted();
                }
                break;
            case R.id.btnMultiplePermissions:
                //Verifica si los permisos establecidos se encuentran concedidos
                if (ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]) != PackageManager.PERMISSION_GRANTED ||
                        ActivityCompat.checkSelfPermission(MainActivity.this, permissions[1]) != PackageManager.PERMISSION_GRANTED) {
                    //Si alguno de los permisos no esta concedido lo solicita
                    ActivityCompat.requestPermissions(MainActivity.this, permissions, MULTIPLE_PERMISSIONS_REQUEST_CODE);
                } else {
                    //Si todos los permisos estan concedidos prosigue con el flujo normal
                    permissionGranted();
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //Se obtiene el resultado de los permisos con base en la clave
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                //Se verifica si existen resultados y se valida si el permiso fue concedido o no
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Si el permiso está concedido prosigue con el flujo normal
                    permissionGranted();
                } else {
                    //Si el permiso no fue concedido no se puede continuar
                    permissionRejected();
                }
                break;
            case SENSOR_PERMISSION_REQUEST_CODE:
                //Se verifica si existen resultados y se valida si el permiso fue concedido o no
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //Si el permiso está concedido prosigue con el flujo normal
                    permissionGranted();
                } else {
                    //Si el permiso no fue concedido no se puede continuar
                    permissionRejected();
                }
                break;
            case MULTIPLE_PERMISSIONS_REQUEST_CODE:
                //Verifica si todos los permisos se aceptaron o no
                if (validatePermissions(grantResults)) {
                    //Si todos los permisos fueron aceptados continua con el flujo normal
                    permissionGranted();
                } else {
                    //Si algun permiso fue rechazado no se puede continuar
                    permissionRejected();
                }
                break;
        }
    }

    private boolean validatePermissions(int[] grantResults) {
        boolean allGranted = false;
        //Revisa cada uno de los permisos y si estos fueron aceptados o no
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                //Si todos los permisos fueron aceptados retorna true
                allGranted = true;
            } else {
                //Si algun permiso no fue aceptado retorna false
                allGranted = false;
                break;
            }
        }
        return allGranted;
    }

    private void permissionGranted() {
        Toast.makeText(MainActivity.this, getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
    }

    private void permissionRejected() {
        Toast.makeText(MainActivity.this, getString(R.string.permission_rejected), Toast.LENGTH_SHORT).show();
    }
}