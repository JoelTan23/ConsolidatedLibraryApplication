package com.sp.consolidatedlibraryapplication2;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class InformationWebView extends Fragment {

    private static final String URL_TO_LOAD = "https://www.sp.edu.sg/sp/student-services/libfl/about-us/about-library-fablab"; // Change this to your desired URL

    public InformationWebView() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.informationwebview, container, false);

        WebView webView = view.findViewById(R.id.webview);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if needed
        webView.setWebViewClient(new WebViewClient()); // Prevents opening in an external browser
        webView.loadUrl(URL_TO_LOAD);

        return view;
    }
}
