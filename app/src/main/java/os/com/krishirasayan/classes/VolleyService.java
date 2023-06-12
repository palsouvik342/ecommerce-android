package os.com.krishirasayan.classes;

import static os.com.krishirasayan.consts.Helper.d;
import static os.com.krishirasayan.consts.Helper.e;
import static os.com.krishirasayan.consts.Helper.i;
import static os.com.krishirasayan.consts.Helper.w;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import os.com.krishirasayan.interfaces.IVolleyResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class VolleyService {

    public static final boolean DEBUG = true;
    public static final boolean ACCEPT_JSON_RESPONSE = false;
    public static final boolean FORMAT_JSON_RESPONSE = false;

    public static final String TAG = "VOLLEY";
    public static final String DEFAULT_REQUEST_NAME_POST = "VOLLEY_POST";
    public static final String DEFAULT_REQUEST_NAME_GET = "VOLLEY_GET";

    IVolleyResult mResultCallback = null;
    Context mContext;
    Map<String, String> headers;
    Map<String, String> params;

    public VolleyService(Context context) {
        mContext = context;
        initHeaders();
        initParams();
    }

    public VolleyService(Context context, IVolleyResult resultCallback) {
        mContext = context;
        mResultCallback = resultCallback;
        initHeaders();
        initParams();
    }

    public VolleyService withHeader(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public VolleyService withParam(String key, String value) {
        params.put(key, value);
        return this;
    }

    public VolleyService withParam(String key, Object value) {
        params.put(key, value == null ? "" : value.toString());
        return this;
    }

    public VolleyService withParamArray(String key, int index, String value) {
        params.put(key + "[" + index + "]", value);
        return this;
    }

    public VolleyService withParamArray(String key, int index, Object value) {
        params.put(key + "[" + index + "]", value == null ? "" : value.toString());
        return this;
    }

    public VolleyService withCallbacks(IVolleyResult resultCallback) {
        mResultCallback = resultCallback;
        return this;
    }

    // GET Methods

    public String get(String url) {
        int requestMethod = Request.Method.GET;
        String requestName = DEFAULT_REQUEST_NAME_GET;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    public String get(String url, JSONObject jsonObject) {
        int requestMethod = Request.Method.GET;
        String requestName = DEFAULT_REQUEST_NAME_GET;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String get(String url, Map map) {
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.GET;
        String requestName = DEFAULT_REQUEST_NAME_GET;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String get(String requestName, String url) {
        int requestMethod = Request.Method.GET;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    public String get(String requestName, String url, JSONObject jsonObject) {
        int requestMethod = Request.Method.GET;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String get(String requestName, String url, Map map) {
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.GET;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String get(String url, Map map, IVolleyResult resultCallback) {
        String requestName = "GET/" + url;
        mResultCallback = resultCallback;
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.GET;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String get(String url, IVolleyResult resultCallback) {
        String requestName = "GET/" + url;
        mResultCallback = resultCallback;
        int requestMethod = Request.Method.GET;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    // POST Methods

    public String post(String url) {
        int requestMethod = Request.Method.POST;
        String requestName = DEFAULT_REQUEST_NAME_POST;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    public String post(String url, JSONObject jsonObject) {
        int requestMethod = Request.Method.POST;
        String requestName = DEFAULT_REQUEST_NAME_POST;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String post(String url, Map map) {
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.POST;
        String requestName = DEFAULT_REQUEST_NAME_POST;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String post(String requestName, String url) {
        int requestMethod = Request.Method.POST;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    public String post(String requestName, String url, JSONObject jsonObject) {
        int requestMethod = Request.Method.POST;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String post(String requestName, String url, Map map) {
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.POST;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String post(String url, Map map, IVolleyResult resultCallback) {
        String requestName = "POST/" + url;
        mResultCallback = resultCallback;
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.POST;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String post(String url, IVolleyResult resultCallback) {
        String requestName = "POST/" + url;
        mResultCallback = resultCallback;
        int requestMethod = Request.Method.POST;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    // PUT Methods

    public String put(String url) {
        int requestMethod = Request.Method.PUT;
        String requestName = DEFAULT_REQUEST_NAME_GET;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    public String put(String url, JSONObject jsonObject) {
        int requestMethod = Request.Method.PUT;
        String requestName = DEFAULT_REQUEST_NAME_GET;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String put(String url, Map map) {
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.PUT;
        String requestName = DEFAULT_REQUEST_NAME_GET;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String put(String requestName, String url) {
        int requestMethod = Request.Method.PUT;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    public String put(String requestName, String url, JSONObject jsonObject) {
        int requestMethod = Request.Method.PUT;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String put(String requestName, String url, Map map) {
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.PUT;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String put(String url, Map map, IVolleyResult resultCallback) {
        String requestName = "PUT/" + url;
        mResultCallback = resultCallback;
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.PUT;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String put(String url, IVolleyResult resultCallback) {
        String requestName = "PUT/" + url;
        mResultCallback = resultCallback;
        int requestMethod = Request.Method.PUT;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    // DELETE Methods

    public String delete(String url) {
        int requestMethod = Request.Method.DELETE;
        String requestName = DEFAULT_REQUEST_NAME_GET;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    public String delete(String url, JSONObject jsonObject) {
        int requestMethod = Request.Method.DELETE;
        String requestName = DEFAULT_REQUEST_NAME_GET;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String delete(String url, Map map) {
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.DELETE;
        String requestName = DEFAULT_REQUEST_NAME_GET;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String delete(String requestName, String url) {
        int requestMethod = Request.Method.DELETE;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    public String delete(String requestName, String url, JSONObject jsonObject) {
        int requestMethod = Request.Method.DELETE;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String delete(String requestName, String url, Map map) {
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.DELETE;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String delete(String url, Map map, IVolleyResult resultCallback) {
        String requestName = "DELETE/" + url;
        mResultCallback = resultCallback;
        JSONObject jsonObject = createJSONObjectFromMap(map);
        int requestMethod = Request.Method.DELETE;
        request(requestMethod, requestName, url, jsonObject);
        return requestName;
    }

    public String delete(String url, IVolleyResult resultCallback) {
        String requestName = "DELETE/" + url;
        mResultCallback = resultCallback;
        int requestMethod = Request.Method.DELETE;
        request(requestMethod, requestName, url, null);
        return requestName;
    }

    // Request Method

    public String request(int requestMethod, final String requestName, String url, JSONObject jsonObject) {

        if (jsonObject == null) {
            jsonObject = createJSONObjectFromMap(params);
        }

        if (requestMethod == Request.Method.GET || requestMethod == Request.Method.DELETE) {
            String params = createUriParamsFromJSON(jsonObject);
            url += params;
            w(TAG, url);
        }

        if (ACCEPT_JSON_RESPONSE) {
            requestJson(requestMethod, requestName, url, jsonObject);
        } else {
            requestString(requestMethod, requestName, url, jsonObject);
        }

        return requestName;
    }

    public String requestJson(int requestMethod, final String requestName, String url, final JSONObject jsonObject) {
        final String requestInfo = "(" + requestName + ") Requesting " + getRequestMethodType(requestMethod) + " " + url + " " + jsonObject;
        try {
            if (DEBUG) i(TAG, requestInfo);

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(requestMethod, url, jsonObject, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    if (DEBUG) {
                        VolleyService.this.logResponse(response, FORMAT_JSON_RESPONSE);
                    }
                    if (mResultCallback != null) {
                        mResultCallback.notifySuccess(requestName, response);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String statusCode = null;
                    String responseData = null;
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {

                        statusCode = String.valueOf(networkResponse.statusCode);
                        responseData = new String(networkResponse.data);

                    }
                    if (DEBUG) {
                        e(TAG, error.toString());
                        e(TAG, "A Network error (" + statusCode + ") was encountered when " + requestInfo);
                        e(TAG, responseData);
                    }
                    if (mResultCallback != null) {
                        mResultCallback.notifyError(requestName, error, statusCode, responseData);
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map params = null;
                    try {
                        if (jsonObject != null) params = jsonToMap(jsonObject);
                    } catch (Exception e) {
                        if (DEBUG) e(TAG, e.toString());
                    }
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    return headers;
                }
            };

            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
            if (mResultCallback != null) {
                mResultCallback.notifyRequestQueued(requestName);
            }

        } catch (Exception e) {
            e(TAG, "An exception occured trying to request: (" + requestName + ") @ URL: (" + url + ")");
            e(TAG, e.toString());
        }
        return requestName;
    }

    public String requestString(int requestMethod, final String requestName, String url, final JSONObject jsonObject) {
        final String requestInfo = "(" + requestName + ") Requesting " + getRequestMethodType(requestMethod) + " " + url + " " + jsonObject;
        try {
            if (DEBUG) i(TAG, requestInfo);

            RequestQueue requestQueue = Volley.newRequestQueue(mContext);
            StringRequest stringRequest = new StringRequest(requestMethod, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String responseString) {
                    if (DEBUG) {
                        VolleyService.this.logResponse(responseString, FORMAT_JSON_RESPONSE);
                    }
                    if (isJSONArray(responseString)) {
                        JSONArray responseArray = VolleyService.this.createJSONArrayFromString(responseString);
                        if (mResultCallback != null) {
                            mResultCallback.notifySuccess(requestName, responseArray);
                        }
                    } else if (isJSONObject(responseString)) {
                        JSONObject responseObject = VolleyService.this.createJSONObjectFromString(responseString);
                        if (mResultCallback != null) {
                            mResultCallback.notifySuccess(requestName, responseObject);
                        }
                    } else {
                        e(TAG, responseString);
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    String statusCode = null;
                    String responseData = null;
                    NetworkResponse networkResponse = error.networkResponse;
                    if (networkResponse != null && networkResponse.data != null) {

                        statusCode = String.valueOf(networkResponse.statusCode);
                        responseData = new String(networkResponse.data);

                    }
                    if (DEBUG) {
                        e(TAG, error.toString());
                        e(TAG, "A Network error (" + statusCode + ") was encountered when " + requestInfo);
                        e(TAG, responseData);
                    }
                    if (mResultCallback != null) {
                        mResultCallback.notifyError(requestName, error, statusCode, responseData);
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map params = null;
                    try {
                        if (jsonObject != null) params = jsonToMap(jsonObject);
                    } catch (Exception e) {
                        if (DEBUG) e(TAG, e.toString());
                    }
                    return params;
                }

                @Override
                public Map<String, String> getHeaders() {
                    return headers;
                }
            };

            stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,DefaultRetryPolicy.DEFAULT_MAX_RETRIES,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
            if (mResultCallback != null) {
                mResultCallback.notifyRequestQueued(requestName);
            }

        } catch (Exception e) {
            e(TAG, "An exception occured trying to request: (" + requestName + ") @ URL: (" + url + ")");
            e(TAG, e.toString());
        }
        return requestName;
    }

    // Helper Methods

    private void initHeaders() {
        headers = new HashMap<String, String>();
        headers.put("X-Requested-With", "XMLHttpRequest");
    }

    private void initParams() {
        params = new HashMap<String, String>();
    }

    private JSONObject createJSONObjectFromString(String jsonString) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(jsonString);
        } catch (Exception e) {
            if (DEBUG) e(TAG, e.toString());
        }
        return jsonObject;
    }

    private JSONArray createJSONArrayFromString(String jsonString) {
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(jsonString);
        } catch (Exception e) {
            if (DEBUG) e(TAG, e.toString());
        }
        return jsonArray;
    }

    private JSONObject createJSONObjectFromMap(Map map) {
        JSONObject jsonObject = null;
        if (map != null) {
            try {
                jsonObject = new JSONObject(map);
            } catch (Exception e) {
                if (DEBUG) e(TAG, e.toString());
            }
        }
        return jsonObject;
    }

    private String createUriParamsFromJSON(JSONObject jsonObject) {
        StringBuilder sb = new StringBuilder();
        try {
            Iterator<String> keys = jsonObject.keys();
            sb.append("?"); //start of query args
            while (keys.hasNext()) {
                String key = keys.next();
                sb.append(key);
                sb.append("=");
                sb.append(jsonObject.get(key));
                sb.append("&"); //To allow for another argument.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        try {
            if (json != JSONObject.NULL) {
                retMap = toMap(json);
            }
        } catch (Exception e) {
            if (DEBUG) e(TAG, e.toString());
        }
        return retMap;
    }

    private Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    private List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }

    private void logResponse(JSONObject jsonObject, boolean format) {
        if (jsonObject != null) {
            try {
                if (format) d(TAG, jsonObject.toString(4));
                else d(TAG, jsonObject.toString());
            } catch (JSONException e) {
                if (DEBUG) e(TAG, e.toString());
            }
        } else if (DEBUG) d(TAG, "Json Object is Null");
    }

    private void logResponse(JSONArray jsonArray, boolean format) {
        if (jsonArray != null) {
            try {
                if (format) d(TAG, jsonArray.toString(4));
                else d(TAG, jsonArray.toString());
            } catch (JSONException e) {
                if (DEBUG) e(TAG, e.toString());
            }
        } else if (DEBUG) d(TAG, "Json Object is Null");
    }

    private void logResponse(String string, boolean format) {
        if (string != null) {
            try {
                if (format) {
                    if (isJSONObject(string)) {
                        d(TAG, new JSONObject(string).toString(4));
                    } else if (isJSONArray(string)) {
                        d(TAG, new JSONArray(string).toString(4));
                    } else {
                        w(TAG, "Cannot format because response String is not JSON");
                        d(TAG, string);
                    }
                } else {
                    d(TAG, string);
                }
            } catch (JSONException e) {
                if (DEBUG) e(TAG, e.toString());
            }
        } else if (DEBUG) d(TAG, "Response String is Null");
    }

    private String getRequestMethodType(int code) {
        String method = "UNKNOWN";
        switch (code) {
            case -1:
                method = "DEPRECATED_GET_OR_POST";
                break;
            case 0:
                method = "GET";
                break;
            case 1:
                method = "POST";
                break;
            case 2:
                method = "PUT";
                break;
            case 3:
                method = "DELETE";
                break;
            case 4:
                method = "HEAD";
                break;
            case 5:
                method = "OPTIONS";
                break;
            case 6:
                method = "TRACE";
                break;
            case 7:
                method = "PATCH";
                break;
        }
        return method;
    }

    private boolean isJSONArray(String data) {
        boolean isJSONArray = false;
        try {
            Object json = new JSONTokener(data).nextValue();
            isJSONArray = (json instanceof JSONArray);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return isJSONArray;
        }
    }

    private boolean isJSONObject(String data) {
        boolean isJSONObject = false;
        try {
            Object json = new JSONTokener(data).nextValue();
            isJSONObject = (json instanceof JSONObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            return isJSONObject;
        }
    }
}