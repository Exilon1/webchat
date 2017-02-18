package util;

/**
 * Created by Alexey on 18.02.2017.
 */
public class HtmlPage {

    public static final String FIRST_PART = "<html>\n" +
            "<head>\n" +
            "    <meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\">\n" +
            "    <meta charset=\"utf-8\">\n" +
            "    <link href=\"https://fonts.googleapis.com/css?family=Roboto\" rel=\"stylesheet\">\n" +
            "    <meta http-equiv=\"refresh\" content=\"10; URL=./\">\n" +
            "    <title>Chat</title>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div id=\"container\">\n" +
            "    <div id=\"msg_history_div\">";

    public static final String SECOND_PART = "    </div>\n" +
            "    <form action=\"./\" method=\"post\">\n" +
            "        <label for=\"nick\">Ник пользователя</label>\n" +
            "        <input id=\"nick\" required value=\"";

    public static final String THIRD_PART = "\" type=\"text\" name=\"nick\" maxlength=\"50\">\n" +
            "        <label for=\"msg\">Сообщение</label>\n" +
            "        <textarea id=\"msg\" required rows=\"5\" type=\"textarea\" name=\"msg\" maxlength=\"200\"></textarea>\n" +
            "        <input type=\"submit\" value=\"Отправить\">\n" +
            "    </form>\n" +
            "</div>\n" +
            "<style>\n" +
            "    #container{\n" +
            "        font-family: 'Roboto', sans-serif;\n" +
            "        font-size:16px;\n" +
            "        padding: 8px;\n" +
            "        border: 1px solid #dfdfdf;\n" +
            "        color: #3f3f3f;\n" +
            "        width: 360px;\n" +
            "        margin: 150px auto;\n" +
            "    }\n" +
            "    #msg_history_div{\n" +
            "        height: 160px;\n" +
            "        overflow: auto;\n" +
            "        border: 1px solid #dfdfdf;\n" +
            "    }\n" +
            "    #msg_history_div ul{\n" +
            "        list-style: none;\n" +
            "        margin: 8px;\n" +
            "        padding: 0;\n" +
            "    }\n" +
            "    #container form{\n" +
            "        margin: 8px 0 8px 0;\n" +
            "    }\n" +
            "    input, textarea {\n" +
            "        width: 100%;\n" +
            "    }\n" +
            "    label {\n" +
            "        padding-bottom:4px;\n" +
            "        padding-top:4px;\n" +
            "        display: block;\n" +
            "    }\n" +
            "    input[type=\"submit\"]{\n" +
            "        margin-top:8px;\n" +
            "    }\n" +
            "</style>\n" +
            "</body>\n" +
            "</html>";
}
