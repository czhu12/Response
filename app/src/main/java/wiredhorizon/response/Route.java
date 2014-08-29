package wiredhorizon.response;

/**
 * Created by chriszhu on 7/9/14.
 */

import com.android.volley.Request;

/**
 * Created by chriszhu on 6/28/14.
 */


public enum Route {
    LOGIN_ROUTE("/users/login", Request.Method.POST),
    SIGN_UP_ROUTE("/users/signup", Request.Method.POST),
    ROOMS_ROUTE("/rooms", Request.Method.GET),
    ALL_ROOMS_ROUTE("/rooms/all", Request.Method.GET),
    VOTE_POLL_ROUTE("/rooms/%s/vote", Request.Method.POST),
    POST_QUESTION_ROUTE("/rooms/%s/comments", Request.Method.POST),
    ROOM_ROUTE("/rooms/%s", Request.Method.GET),
    SUBSCRIBE_ROUTE("/rooms/%s/subscriptions", Request.Method.POST),
    CREATE_ROOM_ROUTE("/rooms", Request.Method.POST);

    private String route;
    private int method;
    private final String BASE_ROUTE = ROOT + "/api/v1";

    public final static String ROOT = "http://162.248.167.144:3000";

    Route(String route, int method) {
        this.route = route;
        this.method = method;
    }

    public String getFullRoute() {
        return BASE_ROUTE + this.route;
    }

    public String getFullRoute(String[] params) {
        return BASE_ROUTE + String.format(this.route, params);
    }

    public int getMethod() {
        return this.method;
    }

}