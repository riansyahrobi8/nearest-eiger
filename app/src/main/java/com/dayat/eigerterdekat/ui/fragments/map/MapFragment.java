package com.dayat.eigerterdekat.ui.fragments.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.here.sdk.core.GeoCoordinates;
import com.here.sdk.core.GeoPolyline;
import com.here.sdk.core.Point2D;
import com.here.sdk.core.errors.InstantiationErrorException;
import com.here.sdk.gestures.GestureType;
import com.here.sdk.gestures.TapListener;
import com.here.sdk.mapviewlite.CameraObserver;
import com.here.sdk.mapviewlite.CameraUpdate;
import com.here.sdk.mapviewlite.MapMarker;
import com.here.sdk.mapviewlite.MapPolyline;
import com.here.sdk.mapviewlite.MapPolylineStyle;
import com.here.sdk.mapviewlite.MapScene;
import com.here.sdk.mapviewlite.MapStyle;
import com.here.sdk.mapviewlite.MapViewLite;
import com.here.sdk.mapviewlite.PickMapItemsCallback;
import com.here.sdk.mapviewlite.PickMapItemsResult;
import com.here.sdk.mapviewlite.PixelFormat;
import com.here.sdk.routing.CalculateRouteCallback;
import com.here.sdk.routing.Route;
import com.here.sdk.routing.RoutingEngine;
import com.here.sdk.routing.RoutingError;
import com.here.sdk.routing.Waypoint;
import com.dayat.eigerterdekat.BuildConfig;
import com.dayat.eigerterdekat.R;
import com.dayat.eigerterdekat.di.component.ActivityComponent;
import com.dayat.eigerterdekat.di.module.ActivityModule;
import com.dayat.eigerterdekat.model.location.LocationModel;
import com.dayat.eigerterdekat.model.location.RequestLocation;
import com.dayat.eigerterdekat.util.CustomMarker;
import com.dayat.eigerterdekat.util.Unit;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import static com.dayat.eigerterdekat.model.location.LocationData.getLocations;
import static com.dayat.eigerterdekat.util.StaticVariabel.LOCATION_REFRESH_DISTANCE;
import static com.dayat.eigerterdekat.util.StaticVariabel.LOCATION_REFRESH_TIME;
import static com.dayat.eigerterdekat.util.StaticVariabel.ZOOM_LEVEL;
import static com.dayat.eigerterdekat.util.StaticVariabel.createCustomMarker;
import static com.dayat.eigerterdekat.util.StaticVariabel.createUserMarker;

public class MapFragment extends Fragment {

    // konteks yang dipakai
    private Context context;

    private MapViewLite mapView;
    private ArrayList<CustomMarker> markers = new ArrayList<>();

    // deklarasi flag status
    // apakah tracking user aktif
    // dan update  rute aktif
    private Boolean isTracking = false, isUpdateRoute = true;

    // deklarasi pembuat rute
    // yg nantinya akan mengkalkulasi rute
    private RoutingEngine routingEngine;

    // deklarasi data route yg ditampilkan
    private MapPolyline routeMapPolyline;

    // deklarasi service lokasi manager
    private LocationManager locationManager;

    // deklarasi user lokasi
    private GeoCoordinates userCoordinate;

    // deklarasi target lokasi
    private GeoCoordinates targetCoordinate;

    // deklarasi user marker
    // yg akan dipakai di map
    private MapMarker userMarker;

    private ImageView userLocation;
    private ImageView navgiation;

    private CardView layoutRoute;
    private TextView durration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initWidget(view, savedInstanceState);
    }

    // fungsi kedua untuk menginisialisasi
    // seleurh variabel yg telah dideklarasi
    private void initWidget(View v, Bundle savedInstanceState) {
        this.context = getActivity();

        // inisialisasi map view
        mapView = v.findViewById(R.id.map_view);

        // panggil fungsi on create mapview
        mapView.onCreate(savedInstanceState);

        userLocation = v.findViewById(R.id.user_location_imageview);
        userLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mapView.getCamera().setTarget(userCoordinate);
                mapView.getCamera().setZoomLevel(ZOOM_LEVEL);
            }
        });

        navgiation = v.findViewById(R.id.navigation_imageview);
        navgiation.setVisibility(View.GONE);
        navgiation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // tracking akan aktif jika tidak
                // dan sebaliknya jika iya
                isTracking = !isTracking;

                // mengatur warna background
                // tombol berdasarkan status aktif
                // tracking maupun tidak
                navgiation.setImageDrawable(
                        isTracking ? ContextCompat.getDrawable(context, R.drawable.nav_on) :
                                ContextCompat.getDrawable(context, R.drawable.nav_of));

            }
        });

        layoutRoute = v.findViewById(R.id.layout_route_cardview);
        layoutRoute.setVisibility(View.GONE);

        durration = v.findViewById(R.id.route_duration_text);

        loadMapScene();
    }


    // fungsi untuk menampilkan map view
    private void loadMapScene() {
        // coba
        try {

            // inisialisasi mesin rute
            routingEngine = new RoutingEngine();

            // jika ada yg salah
            // hiraukan
        } catch (InstantiationErrorException ignore) { }

        // check kondisi pada saat map view berhasil diload
        mapView.getMapScene().loadScene(MapStyle.NORMAL_DAY, new MapScene.LoadSceneCallback() {
            @Override
            public void onLoadScene(@Nullable MapScene.ErrorCode errorCode) {

                // jika tidak ada error
                if (errorCode == null) {

                    // gunakan lokasi akakom
                    // sebagai target default
                    mapView.getCamera().setTarget(new GeoCoordinates(-7.792810, 110.408499));
                    mapView.getCamera().setZoomLevel(ZOOM_LEVEL);
                }

                // panggil fungsi lokasi manajer
                setLocationManager();

                // panggil fungsi set gestur
                // dimapview
                setTapGestureHandler();
            }
        });
    }

    // fungsi menghilangkan rute saat ini
    private void removeCurrentRoute(){

        // coba
        try {

            // jika mapview dan rute marker tidak kosong
            if (mapView != null && routeMapPolyline != null){

                // hilangkan rute
                mapView.getMapScene().removeMapPolyline(routeMapPolyline);

                // set rute marker ke null
                routeMapPolyline = null;
            }

            // jika terjadi exception
            // hiraukan
        } catch (NullPointerException ignore){}
    }

    // fungsi untuk menampilkan rute
    private void showRouting(Waypoint startWaypoint, Waypoint destinationWaypoint){

        // cek jika mesin rute kosong
        if (routingEngine == null) {

            // stop
            return;
        }

        // buat variabel aray untuk waypoint
        // dengan lokasi user dan tujuan sebagai isi
        List<Waypoint> waypoints = new ArrayList<>(Arrays.asList(startWaypoint, destinationWaypoint));

        // memanggil fungsi kalulasi
        routingEngine.calculateRoute(
                waypoints,
                new RoutingEngine.CarOptions(),
                new CalculateRouteCallback() {

                    // pada saat berhasil dikalkulasi
                    @Override
                    public void onRouteCalculated(@Nullable RoutingError routingError, @Nullable List<Route> routes) {

                        // hilangkan rute saat ini
                        removeCurrentRoute();

                        // jika tidak ada error dan
                        // rute tersedia tidak null dan
                        // data index pertama tidak null
                        if (routingError == null && routes != null && routes.get(0) != null) {

                            // kalkulasi menit
                            long minute = routes.get(0).getDurationInSeconds() / 60;

                            // kalkulasi jam
                            long hour = minute / 60;

                            // kalkulasi jarak meter
                            int meter = routes.get(0).getLengthInMeters();

                            // ubah ke kilometer
                            int km = meter / 1000;

                            // jika kurang dari 0 maka
                            // meter dan lainya km
                            String dis = km > 0 ? " (" + km + " Km)" : " (" + meter + " M)";

                            // tampilkan waktu durasi rute
                            durration.setText(
                                    hour > 0 ?
                                            hour + " "+ context.getString(R.string.hour) + " " + minute + " "+ context.getString(R.string.minute) + dis :
                                            minute + " "+ context.getString(R.string.minute) + dis
                            );

                            // coba
                            try {

                                // buat geo poline dari route posisi index pertama
                                GeoPolyline routeGeoPolyline = new GeoPolyline(routes.get(0).getPolyline());

                                // buat poli style
                                MapPolylineStyle mapPolylineStyle = new MapPolylineStyle();

                                // atur warna rute
                                mapPolylineStyle.setColor(ContextCompat.getColor(context,R.color.colorPrimary), PixelFormat.ARGB_8888);

                                // lebar rute
                                mapPolylineStyle.setWidth(10);

                                // inisialisasi marker rute
                                routeMapPolyline = new MapPolyline(routeGeoPolyline, mapPolylineStyle);

                                // tampilkan di map
                                mapView.getMapScene().addMapPolyline(routeMapPolyline);

                                // jika terjadi exception
                                // hiraukan
                            } catch (InstantiationErrorException ignore) { }

                        }
                    }
                });
    }

    // fungsi untuk menampilkan rute
    private void showOnlyShortestRouting(Waypoint startWaypoint, Waypoint destinationWaypoint, Unit<Boolean> afterRouteSet){

        // cek jika mesin rute kosong
        if (routingEngine == null) {

            // stop
            return;
        }

        // buat variabel aray untuk waypoint
        // dengan lokasi user dan tujuan sebagai isi
        List<Waypoint> waypoints = new ArrayList<>(Arrays.asList(startWaypoint, destinationWaypoint));

        // memanggil fungsi kalulasi
        routingEngine.calculateRoute(
                waypoints,
                new RoutingEngine.CarOptions(),
                new CalculateRouteCallback() {

                    // pada saat berhasil dikalkulasi
                    @Override
                    public void onRouteCalculated(@Nullable RoutingError routingError, @Nullable List<Route> routes) {

                        // jika tidak ada error dan
                        // rute tersedia tidak null dan
                        // data index pertama tidak null
                        if (routingError == null && routes != null) {

                            int minDistance = 0;
                            for (Route r : routes){

                                // kalkulasi menit
                                long minute = r.getDurationInSeconds() / 60;

                                // kalkulasi jam
                                long hour = minute / 60;

                                // kalkulasi jarak meter
                                int meter = r.getLengthInMeters();

                                // ubah ke kilometer
                                int km = meter / 1000;

                                // cari rute paling pendek
                                if (km < minDistance){
                                    minDistance = km;
                                }

                                if (minDistance == 0){
                                    minDistance = km;
                                }
                            }

                            afterRouteSet.invoke(minDistance < BuildConfig.MIN_RADIUS);

                        }
                    }
                });
    }

    // fungsi untuk inisialisasi
    // lokasi manajer agar dapat menggunakan
    // service GPS di perangkat user
    @SuppressLint("MissingPermission")
    private void setLocationManager(){

        // inisilisasi service untuk lokasi manajer
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        // jika tidak kosong
        // panggil service
        // untuk mendapatkan lokasi user
        if (locationManager != null)
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,LOCATION_REFRESH_TIME,LOCATION_REFRESH_DISTANCE, new LocationListener() {

                // pada saat mendapatkan lokasi user
                @Override
                public void onLocationChanged(Location location) {

                    Log.e("user", location.getLatitude() + " " + location.getLongitude());

                    if (!isUpdateRoute){
                        return;
                    }

                    // jika koordinat user belun diisi
                    if (userCoordinate == null){

                        // panggil fungsi untuk mendaptkan
                        // lokasi wisata kuliner terdekat
                        userCoordinate = new GeoCoordinates(location.getLatitude(),location.getLongitude());
                        getAllNearestLocation(new GeoCoordinates(location.getLatitude(),location.getLongitude()),true);
                    }

                    // inisialisasi kordinat user
                    userCoordinate = new GeoCoordinates(location.getLatitude(),location.getLongitude());

                    // tampilkan rute terbaru
                    if (targetCoordinate != null){
                        showRouting(new Waypoint(userCoordinate),new Waypoint(targetCoordinate));
                    }

                    if (isTracking){
                        mapView.getCamera().setTarget(userCoordinate);
                    }

                    // coba
                    try {

                        // jika marker user tidak null
                        if (userMarker != null){

                            // hapus marker user
                            mapView.getMapScene().removeMapMarker(userMarker);

                            // kosongkan marker user
                            userMarker = null;
                        }

                        // inisialisasi marker user
                        userMarker = createUserMarker(context,userCoordinate);

                        // tambahkan marker user
                        // ke mapview
                        mapView.getMapScene().addMapMarker(userMarker);

                        // jika terjadi exception
                        // di hiraukan aja
                    }catch (NullPointerException ignore){}
                }

                // untuk fungsi lokasi berubah
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                // untuk fungsi jika provider di aktifkan
                @Override
                public void onProviderEnabled(String provider) {

                }

                // untuk fungsi jika provider di non-aktifkan
                @Override
                public void onProviderDisabled(String provider) {

                }
            },null);
    }

    // fungsi untuk mengatur gestur interaksi user di map
    private void setTapGestureHandler() {

        // atur gestur pada map view
        // untuk interaksi tap
        mapView.getGestures().setTapListener(new TapListener() {
            @Override
            public void onTap(@NotNull Point2D touchPoint) {

                // untuk saat ini user pasti akan
                // mengklik marker
                // panggil fungsi saat marker di tap
                pickMapMarker(touchPoint);
            }
        });

        // non aktifkan gestur double tap karna tidak dipakai
        mapView.getGestures().disableDefaultAction(GestureType.DOUBLE_TAP);

        // non aktifkan gestur double finger tap karna tidak dipakai
        mapView.getGestures().disableDefaultAction(GestureType.TWO_FINGER_TAP);

        // non aktifkan gestur double fingger pan karna tidak dipakai
        mapView.getGestures().disableDefaultAction(GestureType.TWO_FINGER_PAN);

        // fungsi untuk menon aktifkan rotasi kamera
        mapView.getCamera().addObserver(new CameraObserver() {

            // pada saat status kamera berubah
            @Override
            public void onCameraUpdated(@NonNull CameraUpdate cameraUpdate) {

                // check, jika bearingnya bukan 0
                // atau menghadap keutara maka
                if (cameraUpdate.bearing != 0) {

                    // paksakan menghadap keutara
                    mapView.getCamera().setBearing(0);
                }
            }
        });
    }

    // fungsi yg digunakan untuk mengambil data
    // dari marker yg diklik
    private void pickMapMarker(final Point2D touchPoint) {

        // tentukan maksimum pixel adalah 2 pixel
        float radiusInPixel = 2;

        // saat map item diklik
        mapView.pickMapItems(touchPoint, radiusInPixel, new PickMapItemsCallback() {

            // yg dipanggil saat map
            // diklik
            @Override
            public void onMapItemsPicked(@Nullable PickMapItemsResult pickMapItemsResult) {

                // jika item kosong
                // stop program
                if (pickMapItemsResult == null) {
                    return;
                }

                // dapatkan marker
                // dari item map
                MapMarker mapMarker = pickMapItemsResult.getTopmostMarker();

                // jika kosong
                // hentikan program
                if (mapMarker == null) {
                    return;
                }

                // tracking akan aktif
                isTracking = true;

                targetCoordinate = mapMarker.getCoordinates();

                showRouting(new Waypoint(userCoordinate),new Waypoint(targetCoordinate));

                navgiation.setVisibility(View.VISIBLE);
                layoutRoute.setVisibility(View.VISIBLE);
            }
        });
    }

    // fungsi untuk mendapatkan data
    // lokasi wisata kuliner terdekat
    private void getAllNearestLocation(GeoCoordinates userCoordinate, boolean loading){

        removeRestaurantMarker();

        RequestLocation location = new RequestLocation();
        location.CurrentLatitude = userCoordinate.latitude;
        location.CurrentLongitude = userCoordinate.longitude;

        // panggil fungsi validateLocation
        validateLocation(getLocations());
    }

    public void validateLocation(ArrayList<LocationModel> locations){

        // untuk setiap data array di response
        for (LocationModel r : locations){

            // akan dipanggil fungsi untuk
            // menunjukan jarak
            r.Distance = r.calculateDistance(userCoordinate);

            // tampilkan marker lokasi yang terdekat berdasarkan rutenya yg paling pendek
            showOnlyShortestRouting(new Waypoint(userCoordinate),
                    new Waypoint(new GeoCoordinates(r.Latitude,r.Longitude)),new Unit<Boolean>(){

                        @Override
                        public void invoke(Boolean shorter) {

                            // jika benar lokasi ini
                            // punya jalus terpendek maka
                            if (shorter){

                                // memanggil fungsi untuk membuat marker lokasi
                                CustomMarker customMarker = createCustomMarker(context,r);

                                // tambahkan ke array marker
                                markers.add(customMarker);

                                // tampilkan marker dimap
                                mapView.getMapScene().addMapMarker(customMarker.marker);

                                // tampilkan marker dimap
                                mapView.addMapOverlay(customMarker.markerOverlay);

                                // arahkan kamera ke marker
                                // data lokasi wisata kuliner
                                // posisi pertama
                                mapView.getCamera().setTarget(new GeoCoordinates(r.Latitude,r.Longitude));

                                // setting tingkatan zoom kamera
                                mapView.getCamera().setZoomLevel(ZOOM_LEVEL);

                            }
                        }
                    });
        }
    }


    // fungsi untuk menghilangkan marker
    private void removeRestaurantMarker(){

        // untuk setiap marker di array
        for (CustomMarker m : markers){

            // hilangkan marker pada map
            mapView.removeMapOverlay(m.markerOverlay);
            mapView.getMapScene().removeMapMarker(m.marker);
        }

        markers.clear();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // set kondisi update route
        // ke false agar tidak perluh mengupdate route
        // lagi saat aktivity dihancurkan
        isUpdateRoute = false;

        // memanggil fungsi destroy di map view
        mapView.onDestroy();
    }
}