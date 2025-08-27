package uz.jastechno.riskc

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.ContentValues
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.webkit.WebViewAssetLoader
import org.json.JSONObject
import java.net.URLDecoder

class MainActivity : ComponentActivity() {

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val webView = WebView(this)
        setContentView(webView)

        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        // Serve offline assets: app/src/main/assets/www/**
        val assetLoader = WebViewAssetLoader.Builder()
            .addPathHandler("/assets/", WebViewAssetLoader.AssetsPathHandler(this))
            .build()

        webView.webViewClient = object : WebViewClient() {

            override fun shouldInterceptRequest(view: WebView, request: WebResourceRequest) =
                assetLoader.shouldInterceptRequest(request.url)

            override fun onPageStarted(view: WebView, url: String, favicon: android.graphics.Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // Hook URL.createObjectURL to capture blob data (base64) without changing HTML
                val hookJs = """
                    (function(){
                      if (window.__blobStoreHooked) return;
                      window.__blobStoreHooked = true;
                      window.__blobStore = Object.create(null);

                      const _create = URL.createObjectURL;
                      const _revoke = URL.revokeObjectURL;

                      URL.createObjectURL = function(obj) {
                        try {
                          const url = _create.call(URL, obj);
                          try {
                            const reader = new FileReader();
                            reader.onloadend = function() {
                              const res = String(reader.result || "");
                              const comma = res.indexOf(",");
                              const b64 = comma >= 0 ? res.slice(comma + 1) : "";
                              const mime = (obj && obj.type) ? String(obj.type) : "application/octet-stream";
                              window.__blobStore[url] = { b64: b64, mime: mime, ts: Date.now() };
                            };
                            reader.readAsDataURL(obj);
                          } catch (e) {}
                          return url;
                        } catch (e) {
                          return _create.call(URL, obj);
                        }
                      };

                      URL.revokeObjectURL = function(url) {
                        try { delete window.__blobStore[url]; } catch (e) {}
                        return _revoke.call(URL, url);
                      };
                    })();
                """.trimIndent()
                view.evaluateJavascript(hookJs, null)
            }

            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                val url = request.url.toString()
                return when {
                    url.startsWith("data:", ignoreCase = true) -> {
                        handleDataUrl(url)
                        true
                    }
                    url.startsWith("blob:", ignoreCase = true) -> {
                        handleBlobUrlViaHook(view, url)
                        true
                    }
                    else -> false
                }
            }
        }

        // Catch downloads (<a download>)
        webView.setDownloadListener(DownloadListener { url, _, contentDisposition, mimeType, _ ->
            when {
                url.startsWith("blob:", ignoreCase = true) -> handleBlobUrlViaHook(webView, url)
                url.startsWith("data:", ignoreCase = true) -> handleDataUrl(url)
                url.startsWith("http://") || url.startsWith("https://") -> {
                    // Real URL → DownloadManager (kept for completeness; not used for your offline SVG)
                    try {
                        val name = URLUtil.guessFileName(url, contentDisposition, mimeType)
                        val req = DownloadManager.Request(Uri.parse(url))
                            .setMimeType(mimeType)
                            .setTitle(name)
                            .setNotificationVisibility(
                                DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED
                            )
                            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, name)
                        getSystemService(DownloadManager::class.java).enqueue(req)
                        toast("Downloading…")
                    } catch (_: Exception) {
                        toast("Download failed")
                    }
                }
                else -> toast("Unsupported download URL")
            }
        })

        // Load your offline page
        webView.loadUrl("https://appassets.androidplatform.net/assets/www/index.html")
    }

    /** Always name as <timestamp>.jpg */
    private fun timestampJpgName(): String = "${System.currentTimeMillis()}.jpg"

    /** Save to Downloads/RiskCalculator (no permission on API 29+). */
    private fun saveToDownloadsJpg(bytes: ByteArray, mime: String) {
        val fileName = timestampJpgName()
        try {
            if (Build.VERSION.SDK_INT >= 29) {
                val values = ContentValues().apply {
                    put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                    put(MediaStore.Downloads.MIME_TYPE, "image/jpeg") // force JPG
                    put(
                        MediaStore.Downloads.RELATIVE_PATH,
                        Environment.DIRECTORY_DOWNLOADS + "/RiskCalculator"
                    )
                    put(MediaStore.Downloads.IS_PENDING, 1)
                }
                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)
                if (uri != null) {
                    contentResolver.openOutputStream(uri)?.use { it.write(bytes) }
                    values.clear(); values.put(MediaStore.Downloads.IS_PENDING, 0)
                    contentResolver.update(uri, values, null, null)
                    toast("Saved to Downloads/RiskCalculator as $fileName")
                } else toast("Save failed")
            } else {
                // Pre-Android 10: app-specific external dir (no permission)
                val dir = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
                val file = java.io.File(dir, fileName)
                file.parentFile?.mkdirs()
                file.outputStream().use { it.write(bytes) }
                toast("Saved: ${file.absolutePath}")
            }
        } catch (_: Exception) {
            toast("Save error")
        }
    }

    /** Handle data: URLs → data:[<mime>][;base64],<data> */
    private fun handleDataUrl(url: String) {
        try {
            val headerEnd = url.indexOf(',')
            if (headerEnd == -1) { toast("Invalid data URL"); return }
            val meta = url.substring(5, headerEnd) // after "data:"
            val payload = url.substring(headerEnd + 1)

            val isBase64 = meta.contains(";base64", ignoreCase = true)
            // even if mime is not exactly svg, we’ll force .svg for naming per your request
            val bytes = if (isBase64) {
                android.util.Base64.decode(payload, android.util.Base64.DEFAULT)
            } else {
                URLDecoder.decode(payload, "UTF-8").toByteArray(Charsets.UTF_8)
            }

            saveToDownloadsJpg(bytes, "image/jpeg")
        } catch (_: Exception) {
            toast("Invalid data URL")
        }
    }

    /**
     * Handle blob: URLs using the injected createObjectURL hook.
     * We look up the base64 & mime captured when the Blob URL was created.
     */
    private fun handleBlobUrlViaHook(webView: WebView, blobUrl: String) {
        val js = """
            (function(){
              try {
                const map = window.__blobStore || {};
                const hit = map["$blobUrl"];
                if (!hit || !hit.b64) return JSON.stringify({ error: "miss" });
                return JSON.stringify({ b64: hit.b64, mime: hit.mime || "image/jpeg" });
              } catch (e) {
                return JSON.stringify({ error: String(e) });
              }
            })();
        """.trimIndent()

        webView.evaluateJavascript(js) { json ->
            if (json == null || json == "null") { toast("Download failed"); return@evaluateJavascript }
            try {
                val obj = JSONObject(unescapeJsString(json))
                if (obj.has("error")) { toast("Download failed"); return@evaluateJavascript }

                val b64 = obj.getString("b64")
                val bytes = android.util.Base64.decode(b64, android.util.Base64.DEFAULT)

                // Force timestamp.jpg filename regardless of original
                saveToDownloadsJpg(bytes, "image/jpeg")
            } catch (_: Exception) {
                toast("Download failed")
            }
        }
    }

    private fun unescapeJsString(s: String): String {
        if (s.length >= 2 && s.first() == '"' && s.last() == '"') {
            return s.substring(1, s.length - 1)
                .replace("\\\\", "\\")
                .replace("\\\"", "\"")
                .replace("\\n", "\n")
        }
        return s
    }

    private fun toast(msg: String) =
        runOnUiThread { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show() }
}