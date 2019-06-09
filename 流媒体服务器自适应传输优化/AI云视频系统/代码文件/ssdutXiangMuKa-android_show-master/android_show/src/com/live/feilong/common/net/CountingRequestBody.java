package com.live.feilong.common.net;



import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

/**
 * Created by gavin on 17-9-21.
 */

public class CountingRequestBody extends RequestBody{
    //实际起作用的RequestBody
    private RequestBody delegate;

    //回调监听
    private Listener listener;

    private CountingSink countingSink;

    public CountingRequestBody(RequestBody delegate, Listener listener){
        this.delegate=delegate;
        this.listener=listener;
    }

    @Override
    public MediaType contentType() {
        return delegate.contentType();
    }

    @Override
    public long contentLength() {
        try {
            return delegate.contentLength();
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        countingSink=new CountingSink(sink);
        BufferedSink bufferedSink= Okio.buffer(countingSink);
        delegate.writeTo(bufferedSink);
        bufferedSink.flush();
    }


    protected final class CountingSink extends ForwardingSink{
        private long byteWritten;
        public CountingSink(Sink delegate) {
            super(delegate);
        }


        @Override
        public void write(Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            byteWritten+=byteCount;
            listener.onRequestProgress(byteWritten,contentLength());
        }
    }

    public static interface Listener{
        void onRequestProgress(long byteWritten, long contentLength);
    }
}



















