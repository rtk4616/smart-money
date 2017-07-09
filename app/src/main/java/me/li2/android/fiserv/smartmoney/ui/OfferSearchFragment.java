package me.li2.android.fiserv.smartmoney.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.li2.android.fiserv.smartmoney.R;

/**
 * Created by weiyi on 08/07/2017.
 * https://github.com/li2
 */

public class OfferSearchFragment extends Fragment implements
        OnMapReadyCallback,
        OnMarkerClickListener {

    private static final String TAG = "BankingMap";
    private static final String MAP_FRAGMENT_TAG = "map";
    private static final LatLng AUCKLAND = new LatLng(-36.8485, 174.7633);

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mRootLayout;

    private SupportMapFragment mMapFragment;

    private GoogleMap mMap;

    private Marker mAcukland;

    /**
     * Keeps track of the last selected marker (though it may no longer be selected).  This is
     * useful for refreshing the info window.
     */
    private Marker mSelectedMarker;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Binary XML file line #8: Duplicate id R.id.map, tag null, with another fragment for com.google.android.gms.maps.MapFragment
        View view = inflater.inflate(R.layout.fragment_offer_search, container, false);
        ButterKnife.bind(this, view);

        SupportMapFragment mapFragment = (SupportMapFragment)
                getActivity().getSupportFragmentManager().findFragmentByTag(MAP_FRAGMENT_TAG);
        if (mapFragment == null) {
            // NOTE21: find returns null if use SupportMapFragment.
            mapFragment = SupportMapFragment.newInstance();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.mapFragmentContainer, mapFragment, MAP_FRAGMENT_TAG);
            ft.commit();
        }
        mMapFragment = mapFragment;
        mMapFragment.getMapAsync(this);

        mRootLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Hide map toolbar : bottom Navigation & GPS Pointer buttons
        map.getUiSettings().setMapToolbarEnabled(false);

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Add lots of markers to the map.
        addMarkersToMap();

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with lots of markers.");

        LatLngBounds bounds = new LatLngBounds.Builder()
                .include(AUCKLAND)
                .build();
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        getActivity().getSupportFragmentManager().beginTransaction().remove(mMapFragment).commit();
        super.onDestroy();
    }

    private void addMarkersToMap() {
        mAcukland = mMap.addMarker(new MarkerOptions()
                .position(AUCKLAND)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.i_map_marker_shopping_zone_gray)));
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        togglePanel();

        // The user has re-tapped on the marker which was already showing an info window.
        if (marker.equals(mSelectedMarker)) {
            mSelectedMarker = null;
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.i_map_marker_shopping_zone_gray));
            return true;
        }

        mSelectedMarker = marker;
        marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.i_map_marker_shopping_zone_green));
        return false;
    }

    private void togglePanel() {
        if (mRootLayout.getPanelState() != SlidingUpPanelLayout.PanelState.HIDDEN) {
            mRootLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        } else {
            mRootLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }
}
