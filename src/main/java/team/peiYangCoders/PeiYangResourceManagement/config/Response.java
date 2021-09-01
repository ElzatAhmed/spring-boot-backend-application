package team.peiYangCoders.PeiYangResourceManagement.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class Response {

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

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public boolean succeeded(){
        return code == 100;
    }
    public boolean failed(){
        return code != 100;
    }
    public static Response success(Object data){
        return new Response("success", 100, data);
    }
    public static Response confirmationTokenError(){
        return new Response("sending confirmation token failed", 600, null);
    }
    public static Response invalidPhone(){
        return new Response("invalid phone", 601, null);
    }
    public static Response invalidConfirmationToken(){
        return new Response("invalid confirmation token", 602, null);
    }
    public static Response invalidPassword(){
        return new Response("invalid password", 603, null);
    }
    public static Response invalidStudentInfo(){
        return new Response("invalid student information", 604, null);
    }
    public static Response invalidRegistrationCode(){
        return new Response("invalid registration code", 605, null);
    }
    public static Response permissionDenied(){
        return new Response("insufficient authority", 606, null);
    }
    public static Response invalidResourceCode(){
        return new Response("invalid resource code", 701, null);
    }
    public static Response resourceUnavailableToRelease(){
        return new Response("resource unavailable to release", 702, null);
    }
    public static Response resourceAlreadyReleased(){
        return new Response("resource already released", 703, null);
    }
    public static Response resourceNotOwned(){
        return new Response("resource is not belong to this user", 704, null);
    }
    public static Response invalidItemCode(){
        return new Response("invalid item code", 801, null);
    }
    public static Response itemAlreadyOrdered(){
        return new Response("item already ordered", 802, null);
    }
    public static Response itemNotOwned(){
        return new Response("item is not belong to this user", 803, null);
    }
    public static Response itemNotSufficient(){
        return new Response("item is not sufficient", 804, null);
    }
    public static Response invalidOrderCode(){
        return new Response("invalid order code", 901, null);
    }
    public static Response orderNotOwned(){
        return new Response("order is not belonged to this user", 902, null);
    }
    public static Response orderAlreadyAccepted(){
        return new Response("order already accepted by owner", 903, null);
    }
    public static Response orderIsClosedByAtLeastOneSide(){
        return new Response("order is closed by at least one side", 904, null);
    }
    public static Response orderAlreadyCanceled(){
        return new Response("order already canceled", 905, null);
    }
    public static Response orderExpired(){
        return new Response("order expired", 906, null);
    }
    public static Response orderNotAccepted(){
        return new Response("order not accepted", 907, null);
    }
    public static Response orderIsClosedAtYourSide(){
        return new Response("order is closed at your side", 908, null);
    }
    public static Response orderClosed(){
        return new Response("order closed", 909, null);
    }
}
