package os.com.krishirasayan.activities;


import static os.com.krishirasayan.consts.Config.CODE_REQUEST_FINE_LOCATION;
import static os.com.krishirasayan.consts.Helper.coordsToAddress;
import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.dimBehind;
import static os.com.krishirasayan.consts.Helper.e;
import static os.com.krishirasayan.consts.Helper.hideKeyboard;
import static os.com.krishirasayan.consts.Helper.i;
import static os.com.krishirasayan.consts.Helper.w;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import os.com.krishirasayan.R;
import os.com.krishirasayan.classes.LocationService;
import os.com.krishirasayan.interfaces.ICallback;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    LinearLayout llRoot;
    LinearLayout llMapContainer;
    LinearLayout llLoaderContainer;
    RelativeLayout rlReloadContainer;
    Button btnReload;

    SupportMapFragment mapFragment;
    GoogleMap mMap;
    LatLng coords;
    boolean coordsReady;
    LocationService locationService;
    Context context;

    String detectedAddress = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        coords = new LatLng(0.0, 0.0);
        coordsReady = false;
        setup();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CODE_REQUEST_FINE_LOCATION) {
            w( "ACCESS_FINE_LOCATION REQUESTED");
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                tryGetCoords();
            } else {
                Snackbar.make(llRoot, R.string.location_permission_denied, Snackbar.LENGTH_LONG).show();
            }
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));*/

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng point) {
                setMarker(coords);
                coords = point;
            }
        });

        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragStart..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
            }

            @SuppressWarnings("unchecked")
            @Override
            public void onMarkerDragEnd(Marker arg0) {
                // TODO Auto-generated method stub
                Log.d("System out", "onMarkerDragEnd..." + arg0.getPosition().latitude + "..." + arg0.getPosition().longitude);
                coords = arg0.getPosition();
                //mMap.animateCamera(CameraUpdateFactory.newLatLng(arg0.getPosition()));
                //mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
                CameraPosition cameraPosition = new CameraPosition.Builder().target(coords)      // Sets the center of the map to Mountain View
                        .zoom(17)                   // Sets the zoom
                        .build();                   // Creates a CameraPosition from the builder
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                String selectedCoords = String.format("%s, %s", coords.latitude, coords.longitude);
                //Snackbar.make(llRoot, getResources().getString(R.string.location_set_to) + selectedCoords, Snackbar.LENGTH_LONG).show();
                Snackbar.make(llRoot, R.string.location_set, Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onMarkerDrag(Marker arg0) {
                // TODO Auto-generated method stub
                Log.i("System out", "onMarkerDrag...");
            }
        });

        navigateTo(coords);

    }

    @Override
    public void onBackPressed() {

        if(coordsReady) confirmLocationSelection();
        else returnData();
    }

    private void returnData() {

        Intent mIntent = new Intent();

        if(coordsReady) {
            Bundle bundle = new Bundle();
            bundle.putDouble("latitude", coords.latitude);
            bundle.putDouble("longitude", coords.longitude);
            bundle.putString("address", String.valueOf(detectedAddress));
            mIntent.putExtras(bundle);
            setResult(RESULT_OK, mIntent);
        }
        else {
            setResult(RESULT_CANCELED, mIntent);
        }


        super.onBackPressed();
    }

    private void confirmLocationSelection() {

        readAddress(coords);

        PopupWindow popupWindowConfirm;
        hideKeyboard(this);

        LayoutInflater layoutInflater = (LayoutInflater) MapsActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.layout_popup_confirm, null);

        Button btnAgree = (Button) popupView.findViewById(R.id.btnAgree);
        btnAgree.setText(R.string.yes);

        Button btnDisagree = (Button) popupView.findViewById(R.id.btnDisagree);
        btnDisagree.setText(R.string.no);

        TextView txtTitle = (TextView) popupView.findViewById(R.id.txtTitle);
        txtTitle.setText(R.string.map_location_confirm);

        TextView txtContent = (TextView) popupView.findViewById(R.id.txtContent);

        String location = String.format("%s,%s", String.valueOf(coords.latitude), String.valueOf(coords.longitude));
        String content = getResources().getString(R.string.location_briefing);

        String displayText = String.format(content, detectedAddress);
        txtContent.setText(displayText);

        popupWindowConfirm = new PopupWindow(popupView, WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
        popupWindowConfirm.setOutsideTouchable(false);
        popupWindowConfirm.showAtLocation(llRoot, Gravity.CENTER, 0, 0);

        btnAgree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowConfirm.dismiss();
                returnData();
            }
        });

        btnDisagree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindowConfirm.dismiss();
            }
        });

        dimBehind(popupWindowConfirm);
    }

    public void setup() {

        llRoot = findViewById(R.id.llRoot);
        llMapContainer = findViewById(R.id.llMapContainer);
        llMapContainer.setVisibility(View.GONE);
        llLoaderContainer = findViewById(R.id.llLoaderContainer);
        llLoaderContainer.setVisibility(View.GONE);
        rlReloadContainer = findViewById(R.id.rlReloadContainer);
        rlReloadContainer.setVisibility(View.VISIBLE);
        btnReload = findViewById(R.id.btnReload);
        btnReload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llLoaderContainer.setVisibility(View.VISIBLE);
                tryGetCoords();
            }
        });

        context = this;
        locationService = new LocationService(context);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tryGetCoords();
        showMap();

    }

    private void tryGetCoords() {

        // if initial coordinate has been provided
        Intent intent = getIntent();
        if(intent.hasExtra("lat") && intent.hasExtra("lng")) {
            coords = new LatLng(intent.getDoubleExtra("lat", 0.0), intent.getDoubleExtra("lng", 0.0));
            coordsReady = true;
           w( String.format("MapsActivity: Got initial coordinates at %s, %s", coords.latitude, coords.longitude));
        }

        // otherwise try and get device current location
        if(!coordsReady) {
            i( String.format("MapsActivity: Initial Coordinates not Found. Trying to listen device Location..."));
            locationService.listen(new ICallback() {
                @Override
                public void function(Object object) {
                    llLoaderContainer.setVisibility(View.GONE);
                    Location thisLocation = (Location) object;
                    coords = new LatLng(thisLocation.getLatitude(), thisLocation.getLongitude());
                    coordsReady = true;
                    w(String.format("MapsActivity: Found device coordinates at %s, %s", coords.latitude, coords.longitude));
                    showMap();
                }
            });
        }
    }

    private void showMap() {
        if(coordsReady) {
            i( String.format("MapsActivity: Coordinates Ready!"));
            locationService.stopListening();
            llMapContainer.setVisibility(View.VISIBLE);
            rlReloadContainer.setVisibility(View.GONE);
            navigateTo(coords);
        }
        else {
            e( String.format("MapsActivity: Coordinates NOT Ready!"));
            llMapContainer.setVisibility(View.GONE);
            rlReloadContainer.setVisibility(View.VISIBLE);
        }
    }

    public void setMarker(LatLng markerPosition) {

        mMap.clear();
        //Don't forget to Set draggable(true) to marker, if this not set marker does not drag.
        mMap.addMarker(new MarkerOptions().position(coords)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.icon_my_location))
                .draggable(true));

        //Take the user to the marker
//        Toast.makeText(this, R.string.location_changed_map, Toast.LENGTH_LONG).show();
    }

    public void navigateTo(LatLng coords) {
        if(mMap != null) {
            w(String.format("MapsActivity: Navigating to %s, %s", coords.latitude, coords.longitude));
            setMarker(coords);
            CameraPosition cameraPosition = new CameraPosition.Builder().target(coords)      // Sets the center of the map to Mountain View
                    .zoom(17)                   // Sets the zoom
//                .bearing(90)                // Sets the orientation of the camera to east
//                .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
//        mMap.animateCamera(CameraUpdateFactory.newLatLng(coords));
//        mMap.animateCamera(CameraUpdateFactory.zoomTo(10));
        }
    }

    public void readAddress(LatLng coords){
        detectedAddress = coordsToAddress(context, coords).get("address");
        d(detectedAddress);
    }

}
