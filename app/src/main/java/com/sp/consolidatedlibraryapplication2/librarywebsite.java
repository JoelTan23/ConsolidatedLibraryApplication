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

public class librarywebsite extends Fragment {

    private static final String URL_TO_LOAD = "http://192.168.1.5:5173/"; // URL is a placeholdr since local host currently doesn't work.
    //private static final String URL_TO_LOAD = "https://www.sp.edu.sg/sp/student-services/libfl/about-us/about-library-fablab";
    public librarywebsite() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_librarywebsite, container, false);

        WebView webView = view.findViewById(R.id.website);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true); // Enable JavaScript if needed
        webView.setWebViewClient(new WebViewClient()); // Prevents opening in an external browser
        webView.loadUrl(URL_TO_LOAD);

        return view;
    }
}
