package com.dabeloper.android.network;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONObject;
/**
 * Created by David Antonio Bolaños AKA DABELOPER on 04/09/2017.
 *
 * Class that allows received a response null or empty
 * without trigger the error listener
 *
 */

public class VolleyFixJsonObjectRequestWithNull extends JsonRequest<JSONObject> {

    public VolleyFixJsonObjectRequestWithNull(int method, String url, JSONObject jsonRequest,
                                     Response.Listener<JSONObject> successListener, Response.ErrorListener errorListener) {

        super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), successListener,
                errorListener);
    }

    public VolleyFixJsonObjectRequestWithNull(String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errorListener) {
        this(jsonRequest == null ? Request.Method.GET : Request.Method.POST, url, jsonRequest,
                listener, errorListener);
    }

    /*
    * When a request return empty return null
    * When a request return JSON null return null
    * */
    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
//            System.out.println( " JsonObjectRequestWithNull > parseNetworkResponse " );
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
//            System.out.println( "JsonObjectRequestWithNull > parseNetworkResponse : "+ jsonString );
            //Allow null
            if ( jsonString.equals("null") || jsonString.isEmpty() ) {
                return Response.success(null, HttpHeaderParser.parseCacheHeaders(response));
            }
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (Exception e) {
            //e.printStackTrace();
            return Response.error(new ParseError(e));
        }
    }

}
