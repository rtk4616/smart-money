package me.li2.android.fiserv.smartmoney.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import me.li2.android.fiserv.smartmoney.R;

/**
 * Created by weiyi on 08/07/2017.
 * https://github.com/li2
 */

public class OfferSearchFragment extends Fragment implements OnMapReadyCallback {
    private static final String TAG = "BankingMap";
    private static final String MAP_FRAGMENT_TAG = "map";
    private SupportMapFragment mMapFragment;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "onCreate");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        // Binary XML file line #8: Duplicate id R.id.map, tag null, with another fragment for com.google.android.gms.maps.MapFragment
        View view = inflater.inflate(R.layout.fragment_offer_search, container, false);


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
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(174.7633, 36.8485)).title("Aculand"));
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        getActivity().getSupportFragmentManager().beginTransaction().remove(mMapFragment).commit();
        super.onDestroy();
    }
}
