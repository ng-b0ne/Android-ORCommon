package me.b0ne.android.orcommon;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;

import org.apache.http.entity.mime.MultipartEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by b0ne on 14/03/26.
 */
public class SimpleFileRequest extends Request<String> {
    private final Response.Listener<String> mListener;
    private MultipartEntity mEntity;

    public SimpleFileRequest(String url, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, url, errorListener);
        mListener = listener;
    }

    /**
     * リークエストのパラメーターを設定する
     * @param entity
     */
    public void setParams(MultipartEntity entity) {
        mEntity = entity;
    }

    @Override
    public String getBodyContentType() {
        return mEntity.getContentType().getValue();
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            mEntity.writeTo(bos);
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
        }
        return bos.toByteArray();
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
