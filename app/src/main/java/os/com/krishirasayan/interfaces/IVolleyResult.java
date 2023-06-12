package os.com.krishirasayan.interfaces;

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONObject;

public interface IVolleyResult {
    void notifyRequestQueued(String requestName);
    void notifySuccess(String requestName, JSONObject response);
    void notifySuccess(String requestName, JSONArray response);
    void notifyError(String requestName, VolleyError error, String statusCode, String responseData);
}
