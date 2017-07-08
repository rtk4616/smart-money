package me.li2.android.fiserv.smartmoney.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import butterknife.ButterKnife;
import me.li2.android.fiserv.smartmoney.R;

/**
 * Created by weiyi on 08/07/2017.
 * https://github.com/li2
 */

public class OfferSearchFragment extends Fragment implements OnMapReadyCallback {

    private MapFragment mMapFragment;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_search, container, false);
        ButterKnife.bind(this, view);
        // NOTE21: find returns null if use SupportMapFragment
        mMapFragment = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.addMarker(new MarkerOptions().position(new LatLng(174.7633, 36.8485)).title("Aculand"));
    }
}
