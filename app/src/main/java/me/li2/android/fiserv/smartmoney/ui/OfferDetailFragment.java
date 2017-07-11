package me.li2.android.fiserv.smartmoney.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import me.li2.android.fiserv.smartmoney.R;
import me.li2.android.fiserv.smartmoney.model.OfferItem;

/**
 * Created by weiyi on 09/07/2017.
 * https://github.com/li2
 */

public class OfferDetailFragment extends Fragment implements
        OnMapReadyCallback,
        OnMarkerClickListener {

    private static final String TAG = "OfferDetailFragment";

    @BindView(R.id.offer_info_header_view)
    View mHeaderView;

    @BindView(R.id.offer_brand_icon_view)
    ImageView mBrandIconView;

    @BindView(R.id.offer_brand_name_view)
    TextView mBrandNameView;

    @BindView(R.id.offer_brand_addr_view)
    TextView mBrandAddrView;

    @BindView(R.id.offer_date_expire_view)
    TextView mDateExpireView;

    @BindView(R.id.offer_distance_view)
    TextView mDistanceView;

    @BindView(R.id.offer_saved_next_view)
    TextView mSavedView;

    private ViewGroup.LayoutParams mHeaderLayoutParams;
    private int mHeaderMaxHeight;
    private String mExpirePattern;
    private String mDistancePattern;
    private String mSavedPattern;

    private GoogleMap mMap;

    private List<OfferItem> mOfferItems;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offer_detail, container, false);
        ButterKnife.bind(this, view);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mini_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        mHeaderLayoutParams = mHeaderView.getLayoutParams();
        mHeaderMaxHeight = (int)(getResources().getDimension(R.dimen.offer_info_header_height));

        mExpirePattern = getString(R.string.offer_expire_pattern);
        mDistancePattern = getString(R.string.offer_distance_pattern);
        mSavedPattern = getString(R.string.offer_money_saved_next_purchase_pattern);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        // Hide map toolbar : bottom Navigation & GPS Pointer buttons
        map.getUiSettings().setMapToolbarEnabled(false);

        // Hide the zoom controls as the button panel will cover it.
        map.getUiSettings().setZoomControlsEnabled(false);

        // Set listeners for marker events.  See the bottom of this class for their behavior.
        map.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        update(findMarker(marker));
        return false;
    }

    public void setHeaderHeight(float percent) {
        if (mHeaderLayoutParams != null) {
            int height = (int)(mHeaderMaxHeight * percent);
            // NOTE21: if set height to 0, the view still has a height in constraint layout, the workaround is set to 1.
            if (height <= 0) {
                height = 1;
            }
            mHeaderLayoutParams.height = height;
            mHeaderView.setLayoutParams(mHeaderLayoutParams);
        }
    }

    public void update(OfferItem item) {
        if (item == null || getView() == null) {
            return;
        }
        mBrandIconView.setImageResource(item.selectedIconResId);
        mBrandNameView.setText(item.name);
        mBrandAddrView.setText(item.street);
        mDateExpireView.setText(String .format(mExpirePattern, item.expire));
        mDistanceView.setText(String.format(mDistancePattern, item.distance));
        mSavedView.setText(String.format(mSavedPattern, item.saved, item.name));
    }

    // Add lots of markers to the map.
    private HashMap<Marker, OfferItem> mMarkerOfferItemHashMap = new HashMap<>();

    public void addMarkersToMap(List<OfferItem> sameTypeItems) {
        removeOldMarkers();

        mOfferItems = sameTypeItems;
        if (mMap == null) {
            Log.e(TAG, "map not ready");
            return;
        }

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (OfferItem item : sameTypeItems) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(item.latLng)
                    .icon(BitmapDescriptorFactory.fromResource(item.selectedIconResId));
            Marker marker = mMap.addMarker(markerOptions);
            mMarkerOfferItemHashMap.put(marker, item);
            boundsBuilder.include(item.latLng);
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50));
    }

    private OfferItem findMarker(Marker marker) {
        return mMarkerOfferItemHashMap.get(marker);
    }

    private void removeOldMarkers() {
        // use iterator to avoid ConcurrentModificationException
        Iterator iterator = mMarkerOfferItemHashMap.keySet().iterator();
        while (iterator.hasNext()) {
            Marker marker = (Marker) iterator.next();
            marker.remove();
        }
    }

    // To fix issue that users cannot scroll the Google Map vertically with in a scrollable parent view.
    // https://stackoverflow.com/a/17317176/2722270
    @OnTouch(R.id.transparent_intercept_view)
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        Log.d(TAG, "onTouch " + action);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
                // Disallow the scrollable parent view to intercept touch events.
                getView().getParent().requestDisallowInterceptTouchEvent(true);
                // Disable touch on transparent view.
                return false;

            case MotionEvent.ACTION_UP:
                // Allow the scrollable parent view to intercept touch events.
                getView().getParent().requestDisallowInterceptTouchEvent(false);
                return true;

            default:
                return true;
        }
    }
}
