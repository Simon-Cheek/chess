public class AuthClient {

    public static String logout(ServerFacade facade, String authToken){
        if (authToken.isEmpty()) { throw new RuntimeException("Unauthorized"); }
        ResponseObject res = facade.logoutUser(authToken);
        if (res.statusCode() == 200) { return "Successfully logged out"; }
        switch (res.statusCode()) {
            case 401 -> throw new RuntimeException("Unauthorized");
            default -> throw new RuntimeException("Server Error");
        }
    }

}
