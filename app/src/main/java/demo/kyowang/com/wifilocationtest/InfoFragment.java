package demo.kyowang.com.wifilocationtest;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;

import java.util.TimerTask;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link InfoFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link InfoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InfoFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_WIFI_INFO = "wifi_info";
    private static final String ARG_MOBILE_INFO = "mobile_info";


    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    LocationClientOption option = new LocationClientOption();

    // TODO: Rename and change types of parameters
    private WifiInfo mWifiInfo;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private TextView mWifiIP;
    private TextView mWifiMAC;
    private TextView mWifiNetworkID;
    private TextView mWifiNetworkSpeed;
    private TextView mWifiSignalStrength;

    private TextView mConnInfo;
    private TextView mOther;

    private View rootView;
    private InfoFragment me;
    private boolean run = true;
    private Handler mHandler = new Handler();
    private Runnable myRunnable= new Runnable() {
        public void run() {
            //Toast.makeText(getActivity(),"update finish",Toast.LENGTH_SHORT).show();
            if (run) {
                updateWifiInfo(mWifiInfo);
                mHandler.postDelayed(this, 500);
            }
        }
    };
    private TimerTask  mTask = new TimerTask() {
        @Override
        public void run() {
            Message msg = mHandler.obtainMessage();
            msg.what = 0;
            mHandler.sendMessage(msg);
        }
    };
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InfoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InfoFragment newInstance(WifiInfo param1, String param2) {
        InfoFragment fragment = new InfoFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_WIFI_INFO, param1);
        args.putString(ARG_MOBILE_INFO, param2);
        fragment.setArguments(args);

        return fragment;
    }

    public InfoFragment() {
        // Required empty public constructor
        me = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWifiInfo = (WifiInfo)getArguments().getParcelable(ARG_WIFI_INFO);
            mParam2 = getArguments().getString(ARG_MOBILE_INFO);
        }
        mLocationClient = new LocationClient(getActivity().getApplicationContext());
        mLocationClient.registerLocationListener( myListener );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_info, container, false);
        return rootView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        WifiManager wm = (WifiManager)activity.getSystemService(Context.WIFI_SERVICE);
        android.net.wifi.WifiInfo wi = wm.getConnectionInfo();
        mWifiInfo = new WifiInfo(wi);
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        findIds();
        updateWifiInfo(mWifiInfo);

    }
    public void setOption()
    {
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setCoorType("bd09ll");
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        option.setNeedDeviceDirect(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        run = true;
        mHandler.postDelayed(myRunnable, 1000);
        setOption();
        mLocationClient.setLocOption(option);
        mLocationClient.start();

    }

    @Override
    public void onPause() {
        super.onPause();
        run = false;
        mLocationClient.stop();
    }

    public void updateWifiInfo(WifiInfo wii)
    {
        WifiManager wm = (WifiManager)getActivity().getSystemService(Context.WIFI_SERVICE);
        android.net.wifi.WifiInfo wi = wm.getConnectionInfo();

        mWifiInfo = new WifiInfo(wi);
        mWifiIP.setText(wi.getIpAddress() + "");
        mWifiMAC.setText(wi.getMacAddress() + "");
        mWifiNetworkID.setText(wi.getNetworkId() + "");
        mWifiNetworkSpeed.setText(wi.getLinkSpeed() + "");
        mWifiSignalStrength.setText(wi.getRssi() + "");

        ConnectivityManager cm = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo  ni = cm.getActiveNetworkInfo();
        StringBuilder sb = new StringBuilder();
        if(ni != null)
        {
            sb.append(ni.toString());
            //mConnInfo.setText(ni.toString());
        }
        NetworkInfo[] nis = cm.getAllNetworkInfo();
        for(NetworkInfo i : nis)
        {
            sb.append(i.toString());
            sb.append("\n");
            sb.append("\n");
        }
        mOther.setText(sb.toString());
    }

    public void findIds()
    {
        Activity a = getActivity();
        mWifiIP = (TextView)a.findViewById(R.id.ip_address);
        mWifiMAC = (TextView)a.findViewById(R.id.mac_address);
        mWifiNetworkID = (TextView)a.findViewById(R.id.network_id);
        mWifiNetworkSpeed = (TextView)a.findViewById(R.id.network_speed);
        mWifiSignalStrength = (TextView)a.findViewById(R.id.signal_strength);
        mConnInfo = (TextView)a.findViewById(R.id.conn_info);
        mOther = (TextView)a.findViewById(R.id.other);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        run = false;
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location) {
            if (location == null)
                return ;
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation){
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
            }
            mConnInfo.setText(sb.toString());
            //int i = mLocationClient.requestLocation();
            Toast.makeText(getActivity(),"return: ",Toast.LENGTH_SHORT).show();
        }
    }
}
