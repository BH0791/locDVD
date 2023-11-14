package fr.hamtec.ble;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanResult;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {
    private TextView text;
    private static int REQUEST_PERMISSION = 102;
    private static int REQUEST_ENABLE_BLE = 201;
    private static final String TAG = "BLE";
    private BluetoothManager bluetoothManager = null;
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothGatt bluetoothGatt;
    
    //! API-18
    BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback( ) {
        @Override
        public void onLeScan( BluetoothDevice device, int rssi, byte[] scanRecord ) {
            onDeviceDetected( device, rssi );
        }
    };
    //! API-21
    ScanCallback scanCallback = new ScanCallback( ) {
        
        @Override
        public void onScanResult( int callbackType, ScanResult result ) {
            Log.d( TAG, "********** onScanResult: " + result.getRssi() );
            super.onScanResult( callbackType, result );
            onDeviceDetected( result.getDevice( ), result.getRssi( ) );
        }
    };
    
    BluetoothGattCallback bluetoothGattCallback = new BluetoothGattCallback( ) {
        @Override
        public void onConnectionStateChange( BluetoothGatt gatt, int status, int newState ) {
            super.onConnectionStateChange( gatt, status, newState );
            //! A suivre...
            if ( status == BluetoothGatt.GATT_SUCCESS ){
                String message = "";
                if ( newState == BluetoothProfile.STATE_CONNECTED ){
                    message = "Objet connecté";
                }else {
                    message = "Objet déconnecté";
                    showToasFromBackground( message );
                }
            }
        }
    };
    
    private void showToasFromBackground( final  String message ){
        runOnUiThread( new Runnable( ) {
            @Override
            public void run( ) {
                Toast.makeText( MainActivity.this, message, Toast.LENGTH_LONG );
            }
        } );
    }
    
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        text = findViewById( R.id.main_text );
        
        ensurePermission( );
    }
    
    @Override
    protected void onResume( ) {
        super.onResume( );
        Log.d( TAG, "********** onResume ********** " );
        
    }
    
    @Override
    protected void onDestroy( ) {
        super.onDestroy( );
        if ( bluetoothGatt != null ) {
            if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.BLUETOOTH_CONNECT ) != PackageManager.PERMISSION_GRANTED ) {
                connectBluetooth();
            }
            bluetoothGatt.close( );
            bluetoothGatt = null;
        }
    }
    
    @Override
    public void onRequestPermissionsResult( int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults ) {
        super.onRequestPermissionsResult( requestCode, permissions, grantResults );
        if ( requestCode == REQUEST_PERMISSION ) {
            if ( permissions[ 0 ].equals( Manifest.permission.ACCESS_FINE_LOCATION ) && grantResults[ 0 ] == PackageManager.PERMISSION_GRANTED ) {
                startBLEScan( );
            }
        }
    }
    
    @Override
    protected void onActivityResult( int requestCode, int resultCode, @Nullable Intent data ) {
        super.onActivityResult( requestCode, resultCode, data );
        if ( requestCode == REQUEST_ENABLE_BLE && resultCode == RESULT_OK ) {
            startBLEScan( );
        }
    }
    
    private void ensurePermission( )  {
        if ( ContextCompat.checkSelfPermission( this, Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            
            if ( shouldShowRequestPermissionRationale( Manifest.permission.ACCESS_FINE_LOCATION ) ) {
                boitDeDialog( );
            } else {
                askPermissionAccesFineLocation( );
            }
            
        } else {
            startBLEScan( );
        }
    }
    private void boiteDeDialogAccessFineLocation(){
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( R.string.demande_permission_titre );
        builder.setMessage( R.string.explication_permission );
    
        builder.setNegativeButton( "Non", ( dialog, which ) -> {
            //fermeture de l'application...
            Toast.makeText( MainActivity.this, R.string.permission_obligatoire, Toast.LENGTH_LONG ).show( );
            finish( );
        } );
    
        builder.setPositiveButton( "Oui", ( dialog, which ) -> askPermission( ) );
        builder.show( );
    }
    private void askPermissionAccesFineLocation(){
        requestPermissions( new String[]{ Manifest.permission.ACCESS_FINE_LOCATION }, REQUEST_PERMISSION );
    }
    private void boitDeDialog( ) {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( R.string.demande_permission_titre );
        builder.setMessage( R.string.explication_permission );
        
        builder.setNegativeButton( "Non", ( dialog, which ) -> {
            //fermeture de l'application...
            Toast.makeText( MainActivity.this, R.string.permission_obligatoire, Toast.LENGTH_LONG ).show( );
            finish( );
        } );
        
        builder.setPositiveButton( "Oui", ( dialog, which ) -> askPermissionAccesFineLocation() );
        builder.show( );
    }
    
    private void askPermission( ) {
        //! j'ai plus le problème mais j'ai deux boîtes de dialogue/askPermission
        // TODO A voir pour optimisation...
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ) {
            requestPermissions( new String[]{ Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN }, REQUEST_PERMISSION );
        }
    }
    
    private void startBLEScan( ) {
        text.setText( "Scan en activité... !" );
        if ( bluetoothManager == null ) {
            bluetoothManager = ( BluetoothManager ) getSystemService( Context.BLUETOOTH_SERVICE );
        }
        
        if ( bluetoothAdapter == null ) {
            bluetoothAdapter = bluetoothManager.getAdapter( );
        }
        
        if ( !bluetoothAdapter.isEnabled( ) ) {
            Intent askBLE = new Intent( BluetoothAdapter.ACTION_REQUEST_ENABLE );
            
            if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.BLUETOOTH_CONNECT ) != PackageManager.PERMISSION_GRANTED ) {
                if ( shouldShowRequestPermissionRationale( Manifest.permission.BLUETOOTH_CONNECT ) ) {
                    boitDeDialog( );
                } else {
                    askPermission( );
                }
            }
            
            startActivityForResult( askBLE, REQUEST_ENABLE_BLE );
            return;
        }
        
        if ( Build.VERSION.SDK_INT < 21 ) {
            // ! <uses-permission android:name = "android.permission.BLUETOOTH_SCAN"/>
            bluetoothAdapter.startLeScan( leScanCallback );
            Log.d( TAG, "********** Scan lancé en version 18" );
        } else {
            bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner( );
            bluetoothLeScanner.startScan( scanCallback );
            Log.d( TAG, "********** Scan lancé en version 21 " );
        }
        //? Arrêt du scan au bout de 5s
        new Handler( ).postDelayed( new Runnable( ) {
            @Override
            public void run( ) {
                stopBLEScan( );
            }
        }, 5000 );
    }
    
    private void stopBLEScan( ) {
        if ( Build.VERSION.SDK_INT < 21 ) {
            if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.BLUETOOTH_SCAN ) != PackageManager.PERMISSION_GRANTED ) {
                connectBluetooth();
            }
            bluetoothAdapter.stopLeScan( leScanCallback );
        }else {
            bluetoothAdapter.getBluetoothLeScanner().startScan( scanCallback );
        }
    }
    
    private void onDeviceDetected( BluetoothDevice device, int rssi ) {
        String info = "Objet détécté... : ";
        //! Faire une demande de permission pour device.getName()
        if ( ActivityCompat.checkSelfPermission( this, Manifest.permission.BLUETOOTH_CONNECT ) != PackageManager.PERMISSION_GRANTED ) {
            if ( shouldShowRequestPermissionRationale( Manifest.permission.BLUETOOTH_CONNECT ) ) {
                Log.d( TAG, "********** onDeviceDetected: demande de permission device.getName()" );
                boitDeDialog( );
            } else {
                askPermission( );
            }
        }
        Log.d( TAG, "Informations ==> " + rssi + " __ " + device.getAddress() );
        info += "\nNom : " + device.getName( );
        info += "\nAdresse : " + device.getAddress();
        info += "\nRSSI : " + rssi;
        text.setText( info );
        
        bluetoothGatt = device.connectGatt( this, false, bluetoothGattCallback );
    }
    
    /**
     * Information
     * ' Information
     * ! Méthode de demande de connection Bluetooth
     * ? Appel de boîte de dialogue
     *
     */
    private void connectBluetooth(){
        if ( shouldShowRequestPermissionRationale( Manifest.permission.BLUETOOTH_CONNECT ) ) {
            boitDeDialog( );
        } else {
            askPermission( );
        }
    }
}