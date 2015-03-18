package me.b0ne.android.orcommon;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by b0ne on 14/01/06.
 */
public class SimpleRequest extends Request<String> {

    private final Response.Listener<String> mListener;
    private HashMap<String, String> mParams;
    private String mUrl;

    public SimpleRequest(int method, String url, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, errorListener);

        mListener = listener;
        mParams = new HashMap<String, String>();
    }

    @Override
    public String getUrl() {
        if (getMethod() == Method.GET || getMethod() == Method.DELETE) {
            return super.getUrl() + "?" + getParamsString(mParams);
        }
        return super.getUrl();
    }

    /**
     * Map<String, String>のパラメータを設定する
     * @param map
     */
    public void setParams(HashMap<String, String> map) {
        mParams = map;
    }

    @Override
    protected Map<String, String> getParams() {
        return mParams;
    }

    private String getParamsString(HashMap<String, String> params) {
        String paramsString = "";
        int count = 1;
        try {
            for (Map.Entry<String, String> item : params.entrySet()) {
                paramsString += URLEncoder.encode(item.getKey(), "UTF-8")
                        + "=" + URLEncoder.encode(item.getValue(), "UTF-8");

                if (count != params.size()) {
                    paramsString += "&";
                }

                count++;
            }
        } catch (Exception e) {
        }
        return paramsString;
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse networkResponse) {
        String resp = new String(networkResponse.data);
        return Response.success(resp, getCacheEntry());
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
