package cn.aulang.common.core.utils;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.util.Enumeration;

public class Systems {

    public static URL locateFromClasspath(String resourceName) {
        URL url = null;
        // attempt to load from the context classpath
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader != null) {
            url = loader.getResource(resourceName);
        }

        // attempt to load from the system classpath
        if (url == null) {
            url = ClassLoader.getSystemResource(resourceName);
        }
        return url;
    }

    public static byte[] getMacAddressWithIpv4() {
        try {
            return getPhysicalIPv4Interface().getHardwareAddress();
        } catch (SocketException e) {
            throw new IllegalStateException("Can't get mac address, " + e.getMessage());
        }
    }

    public static String getRealIpv4Address() {
        InetAddress inetAddress = getRealIpv4InetAddress();
        return inetAddress.getHostAddress();
    }

    public static InetAddress getRealIpv4InetAddress() {
        try {
            NetworkInterface netInterface = getPhysicalIPv4Interface();
            Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();
                if (address.isSiteLocalAddress()) {
                    return address;
                }
            }
        } catch (Exception ignore) {
            //ignore
        }
        throw new IllegalStateException("Can't get a valid inet address");
    }

    private static NetworkInterface getPhysicalIPv4Interface() {
        try {
            Enumeration<NetworkInterface> netInterfaces = NetworkInterface.getNetworkInterfaces();
            while (netInterfaces.hasMoreElements()) {
                NetworkInterface netInterface = netInterfaces.nextElement();
                if (netInterface.isLoopback() || netInterface.isVirtual() || !netInterface.isUp()) {
                    continue;
                }
                byte[] mac = netInterface.getHardwareAddress();
                if (mac != null && hasIpv4Address(netInterface)) {
                    return netInterface;
                }
            }
        } catch (Exception ignore) {
            //ignore
        }
        throw new IllegalStateException("Can't get physical ipv4 net interface");
    }

    private static boolean hasIpv4Address(NetworkInterface netInterface) {
        Enumeration<InetAddress> addresses = netInterface.getInetAddresses();
        while (addresses.hasMoreElements()) {
            InetAddress address = addresses.nextElement();
            if (address instanceof Inet4Address) {
                return true;
            }
        }
        return false;
    }
}
