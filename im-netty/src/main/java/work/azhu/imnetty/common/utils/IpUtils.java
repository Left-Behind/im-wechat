package work.azhu.imnetty.common.utils;

import java.net.*;
import java.util.Enumeration;

/**
 * @Author Azhu
 * @Date 2019/11/7 17:55
 * @Description
 */
public class IpUtils {
    /***
     * 获取外网IP
     * @return 现在基本没用
     */
    public static String internetIp() {
        try {
            Enumeration<NetworkInterface> networks = NetworkInterface.getNetworkInterfaces();
            InetAddress inetAddress = null;
            Enumeration<InetAddress> inetAddresses = null;
            while (networks.hasMoreElements()) {
                inetAddresses = networks.nextElement().getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress != null
                            && inetAddress instanceof Inet4Address
                            && !inetAddress.isSiteLocalAddress()
                            && !inetAddress.isLoopbackAddress()
                            && inetAddress.getHostAddress().indexOf(":") == -1) {
                        return inetAddress.getHostAddress();
                    }
                }
            }

            return null;

        } catch (Exception e) {

            throw new RuntimeException(e);
        }
    }

    /**
     * 获取内网IP
     *
     * @return
     */
    public static String intranetIp() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取服务启动host
     *
     * @return 先返回公网地址，再返回内网地址，但是公网地址获取方法失效，目前只能获取内网地址，
     */
    public static String getHost() {
        return internetIp() == null ? intranetIp() : internetIp();
    }

    public static void main(String[] args){
        System.out.println(internetIp());
    }

}
