package ipcm.maps.tester;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class Map extends Activity
    implements GooglePlayServicesClient.ConnectionCallbacks,
    GooglePlayServicesClient.OnConnectionFailedListener {

    private GoogleMap map;
    private CameraUpdate initPos;
    private CameraUpdate initZoom;
    private Button addMarkerButton;
    private Location currentLocation;
    LocationClient locationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_map);

        locationClient = new LocationClient(this, this, this);
        addMarkerButton = (Button)findViewById(R.id.button);
        setUpMapIfNeeded();

        addMarkerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                currentLocation = locationClient.getLastLocation();

                if(currentLocation != null){
                    map.addMarker(new MarkerOptions()
                       .position(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()))
                       .title("Hello world"));
                }

            }
        });

        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                if(latLng != null){
                    map.addMarker(new MarkerOptions()
                       .position(latLng)
                       .title("hello"));
                }

            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        locationClient.connect();
    }

    @Override
    public void onResume(){
        super.onResume();

        setUpMapIfNeeded();
    }

    protected void onStop(){
        locationClient.disconnect();
        super.onStop();
    }

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (map == null) {
            map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            setUpMapProperties();

            // Check if we were successful in obtaining the map.
            if (map != null) {

            }
        }
    }

    private void setUpMapProperties(){

        initPos = CameraUpdateFactory.newLatLng(new LatLng(43.0731, -89.4011));
        initZoom = CameraUpdateFactory.zoomTo(8);

        if(map != null){
            map.moveCamera(initPos);
            map.animateCamera(initZoom);
        }
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle dataBundle) {

    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {

    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

}