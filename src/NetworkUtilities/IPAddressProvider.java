package NetworkUtilities;

import java.net.*;
import java.io.*;

public class IPAddressProvider {


//    URL whatismyip = new URL("http://checkip.amazonaws.com");
//    BufferedReader in = new BufferedReader(new InputStreamReader(
//            whatismyip.openStream()));
//
//    String ip = in.readLine();
//    System.out.println(ip);

    public static String getIP() {
        try{
        URL whatismyip = new URL("http://checkip.amazonaws.com");
        BufferedReader in = new BufferedReader(new InputStreamReader(
                whatismyip.openStream()));

        String ip = in.readLine();
        return ip;
        }catch(Exception e){
            System.out.println(e);
            return null;
        }
    }
}


