package com.telefonica.euro_iaas.sdc.manager;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.Locale;

import org.apache.http.Header;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

public class HTTPClientMock implements HttpClient{

    @Override
    public HttpParams getParams() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ClientConnectionManager getConnectionManager() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse execute(HttpUriRequest request) throws IOException, ClientProtocolException {
        HttpResponse response = new HttpResponse() {
            
            @Override
            public void setParams(HttpParams arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setHeaders(Header[] arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setHeader(String arg0, String arg1) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setHeader(Header arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void removeHeaders(String arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void removeHeader(Header arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public HeaderIterator headerIterator(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public HeaderIterator headerIterator() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public ProtocolVersion getProtocolVersion() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public HttpParams getParams() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Header getLastHeader(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Header[] getHeaders(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Header getFirstHeader(String arg0) {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public Header[] getAllHeaders() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public boolean containsHeader(String arg0) {
                // TODO Auto-generated method stub
                return false;
            }
            
            @Override
            public void addHeader(String arg0, String arg1) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void addHeader(Header arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setStatusLine(ProtocolVersion arg0, int arg1, String arg2) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setStatusLine(ProtocolVersion arg0, int arg1) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setStatusLine(StatusLine arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setStatusCode(int arg0) throws IllegalStateException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setReasonPhrase(String arg0) throws IllegalStateException {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setLocale(Locale arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void setEntity(HttpEntity arg0) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public StatusLine getStatusLine() {
                StatusLine sl = new StatusLine() {
                    
                    @Override
                    public int getStatusCode() {
                        // TODO Auto-generated method stub
                        return HttpStatus.SC_OK;
                    }
                    
                    @Override
                    public String getReasonPhrase() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                    
                    @Override
                    public ProtocolVersion getProtocolVersion() {
                        // TODO Auto-generated method stub
                        return null;
                    }
                };
                return sl;
            }
            
            @Override
            public Locale getLocale() {
                // TODO Auto-generated method stub
                return null;
            }
            
            @Override
            public HttpEntity getEntity() {
                // TODO Auto-generated method stub
                return null;
            }
        };
       
        return response;
    }

    @Override
    public HttpResponse execute(HttpUriRequest request, HttpContext context) throws IOException,
            ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request) throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public HttpResponse execute(HttpHost target, HttpRequest request, HttpContext context) throws IOException,
            ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler) throws IOException,
            ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T execute(HttpUriRequest request, ResponseHandler<? extends T> responseHandler, HttpContext context)
            throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler)
            throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <T> T execute(HttpHost target, HttpRequest request, ResponseHandler<? extends T> responseHandler,
            HttpContext context) throws IOException, ClientProtocolException {
        // TODO Auto-generated method stub
        return null;
    }

}
