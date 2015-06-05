package demo.kyowang.com.wifilocationtest;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by wg on 2015/6/5.
 */
public class WifiInfo implements Parcelable {
    private String bssid;
    private int frequency;
    private int ipAddress;

    public String getBssid() {
        return bssid;
    }

    public int getFrequency() {
        return frequency;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public int getLinkSpeed() {
        return linkSpeed;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public int getNetworkId() {
        return networkId;
    }

    public int getRssi() {
        return rssi;
    }

    public String getSsid() {
        return ssid;
    }

    public android.net.wifi.WifiInfo getWifiinfo() {
        return wifiinfo;
    }

    private int linkSpeed;
    private String macAddress;
    private int networkId;
    private int rssi;
    private String ssid;
    private android.net.wifi.WifiInfo wifiinfo;

    public WifiInfo(android.net.wifi.WifiInfo wi)
    {
        wifiinfo = wi;
        bssid = wi.getBSSID();
        //frequency = wi.getFrequency();
        ipAddress = wi.getIpAddress();
        linkSpeed = wi.getLinkSpeed();
        macAddress = wi.getMacAddress();
        networkId = wi.getNetworkId();
        rssi = wi.getRssi();
        ssid = wi.getSSID();
    }
    public WifiInfo(Parcel in)
    {
        wifiinfo = null;
        bssid = in.readString();
        frequency = in.readInt();
        ipAddress = in.readInt();
        linkSpeed = in.readInt();
        macAddress = in.readString();
        networkId = in.readInt();
        rssi = in.readInt();
        ssid = in.readString();
    }

    public String toString()
    {
        return wifiinfo.toString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(bssid);
        parcel.writeInt(frequency);
        parcel.writeInt(ipAddress);
        parcel.writeInt(linkSpeed);
        parcel.writeString(macAddress);
        parcel.writeInt(networkId);
        parcel.writeInt(rssi);
        parcel.writeString(ssid);
    }
    public void setWifiInfo( android.net.wifi.WifiInfo wi)
    {
        this.wifiinfo = wi;
    }
    public static final Parcelable.Creator<WifiInfo> CREATOR
            = new Parcelable.Creator<WifiInfo>() {
        public WifiInfo createFromParcel(Parcel in) {
            return new WifiInfo(in);
        }

        public WifiInfo[] newArray(int size) {
            return new WifiInfo[size];
        }
    };
}
