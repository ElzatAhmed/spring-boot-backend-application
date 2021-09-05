package team.peiYangCoders.PeiYangResourceManagement.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {

    private String message;

    private int code;

    private Object data;

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
    public static Response invalidUserToken(){
        return new Response("invalid user token", 607, null);
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
        return new Response("resource not belong to the user", 704, null);
    }
    public static Response invalidItemCode(){
        return new Response("invalid item code", 801, null);
    }
    public static Response itemAlreadyOrdered(){
        return new Response("item already ordered", 802, null);
    }
    public static Response itemNotOwned(){
        return new Response("item not belong to the user", 803, null);
    }
    public static Response itemNotSufficient(){
        return new Response("item is not sufficient", 804, null);
    }
    public static Response invalidOrderCode(){
        return new Response("invalid order code", 901, null);
    }
    public static Response orderNotOwned(){
        return new Response("order not belonged to the user", 902, null);
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

    public static Response imageNotFount(){
        return new Response("image not found", 1001, null);
    }
}
