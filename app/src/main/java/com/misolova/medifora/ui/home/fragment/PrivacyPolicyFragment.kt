package com.misolova.medifora.ui.home.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.Fragment
import com.misolova.medifora.R
import com.misolova.medifora.util.Constants.DOMAIN
import com.misolova.medifora.util.Constants.PRIVACY_POLICY_URL

class PrivacyPolicyFragment : Fragment() {
    companion object {
        private const val TAG = "PRIVACY_POLICY_FRAGMENT"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_privacy_policy, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val webView = view.findViewById(R.id.webViewPrivacyPolicy) as WebView?

        val webSettings = webView?.settings
        webSettings?.javaScriptEnabled = true
        webView?.loadUrl(PRIVACY_POLICY_URL)
        webView?.webViewClient = HelloWebViewClient()
        WebView.setWebContentsDebuggingEnabled(false)
    }

    private inner class HelloWebViewClient : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            if (Uri.parse(url).host == DOMAIN) {
                return false
            }
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url)
        }

    }
}