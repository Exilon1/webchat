package util;


import javax.servlet.http.Cookie;

/**
 * Created by Alexey on 12.02.2017.
 */
public class DbHelper {

    public static String getCookieName(Cookie[] cookies, String param) {
        String str = null;
        if (cookies!=null && param!=null)
            for (Cookie cookie : cookies) {
                if (param.equals(cookie.getName())) {
                    str = cookie.getValue();
                }
            }
        return str;
    }
 }
