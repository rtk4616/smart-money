package me.li2.android.fiserv.smartmoney.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.Configuration;
import me.li2.android.fiserv.smartmoney.model.OfferItem;
import me.li2.android.fiserv.smartmoney.model.Offers;
import me.li2.android.fiserv.smartmoney.utils.ViewUtils;
import me.li2.android.fiserv.smartmoney.webservice.FiservService;
import me.li2.android.fiserv.smartmoney.webservice.ServiceGenerator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by weiyi on 08/07/2017.
 * https://github.com/li2
 */

public class OfferSearchFragment extends Fragment implements
        OnMapReadyCallback,
        OnMarkerClickListener {

    private static final String TAG = "BankingMap";
    private static final String MAP_FRAGMENT_TAG = "map";

    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout mRootLayout;

    private OfferDetailFragment mDetailFragment;

    private SupportMapFragment mMapFragment;

    private GoogleMap mMap;

    private List<OfferItem> mOfferItems;

    /**
     * Keeps track of the last selected marker (though it may no longer be selected).  This is
     * useful for refreshing the info window.
     */
    private Marker mLastSelectedMarker;


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

        // setup Google Map fragment
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

        // init sliding panel
        mRootLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
        mRootLayout.addPanelSlideListener(mPanelSlideListener);

        // listen back key
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(mOnKeyListener);

        // NOTE21: to get fragment in anther fragment's layout, should use getChildFragmentManager
        mDetailFragment = (OfferDetailFragment)
                getChildFragmentManager().findFragmentById(R.id.dragView);
        Log.d(TAG, "detailFragment " + mDetailFragment);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        loadOffers();
        mMap = map;

        // set default location
        map.moveCamera(CameraUpdateFactory.newLatLng(Configuration.AUCKLAND));

        // Hide map toolbar : bottom Navigation & GPS Pointer buttons
        map.getUiSettings().setMapToolbarEnabled(false);

        // Hide the zoom controls as the button panel will cover it.
        mMap.getUiSettings().setZoomControlsEnabled(false);

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        mMap.setOnMarkerClickListener(this);

        // Override the default content description on the view, for accessibility mode.
        // Ideally this string would be localised.
        mMap.setContentDescription("Map with lots of markers.");
    }


    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        getActivity().getSupportFragmentManager().beginTransaction().remove(mMapFragment).commit();
        super.onDestroy();
    }

    private void loadOffers() {
        showLoadingDialog();
        FiservService service = ServiceGenerator.createService(FiservService.class);
        service.getOffers().enqueue(new Callback<Offers>() {
            @Override
            public void onResponse(Call<Offers> call, Response<Offers> response) {
                hideLoadingDialog();
                onOffersGet(response.body());
            }

            @Override
            public void onFailure(Call<Offers> call, Throwable t) {

            }
        });
    }

    private void onOffersGet(Offers offers) {
        if (offers == null || offers.offers == null) {
            showLoadingFailedDialog();
            return;
        }

        mOfferItems = offers.offers;
        if (mOfferItems != null) {
            addMarkersToMap();
        }
    }

    private void showLoadingFailedDialog() {
        new AlertDialog.Builder(getContext())
                .setMessage(R.string.loading_offers_failed)
                .setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loadOffers();
                    }
                })
                .setNegativeButton(android.R.string.cancel, null)
                .setCancelable(false)
                .show();
    }

    private Dialog mLoadingDialog;

    private void showLoadingDialog() {
        mLoadingDialog = ViewUtils.showLoadingDialog(new WeakReference<Activity>(getActivity()),
                getString(R.string.loading));
    }

    private void hideLoadingDialog() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
            mLoadingDialog = null;
        }
    }

    // Add lots of markers to the map.
    private HashMap<Marker, OfferItem> mMarkerOfferItemHashMap = new HashMap<>();

    private void addMarkersToMap() {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (OfferItem item : mOfferItems) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(item.latLng)
                    .icon(BitmapDescriptorFactory.fromResource(item.unselectedIconResId));
            Marker marker = mMap.addMarker(markerOptions);
            mMarkerOfferItemHashMap.put(marker, item);

            boundsBuilder.include(item.latLng);
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 256));
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        OfferItem offerItem = findMarker(marker);
        if (offerItem == null) {
            return false; // should never go here
        }
        updateMarkerStatus(marker);
        return false;
    }

    private View.OnKeyListener mOnKeyListener = new View.OnKeyListener() {
        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (mRootLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED
                        || mRootLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED) {
                    mRootLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
                    return true;
                } else if (mRootLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                    updateMarkerStatus(mLastSelectedMarker);
                    return true;
                }
            }
            return false;
        }
    };

    private SlidingUpPanelLayout.PanelSlideListener mPanelSlideListener = new SlidingUpPanelLayout.PanelSlideListener() {
        @Override
        public void onPanelSlide(View panel, float slideOffset) {
            Log.d(TAG, "onPanelSlide, offset " + slideOffset);
            if (mDetailFragment != null && slideOffset > 0) {
                mDetailFragment.setHeaderHeight(slideOffset);
            }
        }

        @Override
        public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
            Log.d(TAG, "onPanelStateChanged " + newState);
        }
    };

    private OfferItem findMarker(Marker marker) {
        return mMarkerOfferItemHashMap.get(marker);
    }

    private List<OfferItem> findSameTypeOfferItems(OfferItem selectedItem) {
        ArrayList<OfferItem> items = new ArrayList<>();
        for (OfferItem item : mOfferItems) {
            if (item.type.equals(selectedItem.type)) {
                items.add(item);
                continue;
            }
        }
        return items;
    }

    /*
    - no selected : set icon selected; show panel; update panel data;
    - has selected:
        - same one: set icon not selected; hide panel; (same as Back Key)
        - different one: set icon1 not selected, set icon2 selected; update panel data;
    */
    private void updateMarkerStatus(Marker marker) {
        OfferItem offerItem = findMarker(marker);

        if (marker.equals(mLastSelectedMarker)) {
            /** re-tapped on the marker which was already selected */
            marker.setIcon(BitmapDescriptorFactory.fromResource(offerItem.unselectedIconResId));
            mRootLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            mLastSelectedMarker = null;
        } else {
            if (mLastSelectedMarker == null) {
                /** no marker selected */
                marker.setIcon(BitmapDescriptorFactory.fromResource(offerItem.selectedIconResId));
            } else {
                /** select another marker */
                OfferItem lastOfferItem = findMarker(mLastSelectedMarker);
                mLastSelectedMarker.setIcon(BitmapDescriptorFactory.fromResource(lastOfferItem.unselectedIconResId));
                marker.setIcon(BitmapDescriptorFactory.fromResource(offerItem.selectedIconResId));
            }
            mRootLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            mDetailFragment.update(offerItem);
            mDetailFragment.addMarkersToMap(findSameTypeOfferItems(offerItem));
            mLastSelectedMarker = marker;
        }
    }
}
