package team.peiYangCoders.PeiYangResourceManagement.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Response {

    public static int OK = 0;
    public static int ERROR = 1;

    public static String ok = "ok";
    public static String error = "error";
    public static String noSuchUser = "no such user";
    public static String invalidPassword = "invalid password";
    public static String invalidPhone = "invalid phone";
    public static String permissionDenied = "permission denied";
    public static String noSuchResource = "no such resource";
    public static String codeIsUsed = "code is used";
    public static String invalidToken = "invalid token";
    public static String tokenExpired = "token expired";
    public static String userDisabled = "user disabled";
    public static String doTheConfirmationTokenFirst = "";

    private String message;

    private int code;

    private Object data;

    public Response() {
    }

    public Response(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public Response(String message, int code, Object data) {
        this.message = message;
        this.code = code;
        this.data = data;
    }

    public static Response okMessage(){
        return new Response("ok", OK);
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public static Response okMessage(String message){
        return new Response(message, OK);
    }

    public static Response okMessage(String message, Object data){
        return new Response(message, OK, data);
    }

    public static Response okMessage(Object data){
        return new Response(ok, OK, data);
    }

    public static Response errorMessage(){
        return new Response(error, ERROR);
    }

    public static Response errorMessage(String message){
        return new Response(message, ERROR);
    }

    public static Response errorMessage(String message, Object data){
        return new Response(message, ERROR, data);
    }
}
