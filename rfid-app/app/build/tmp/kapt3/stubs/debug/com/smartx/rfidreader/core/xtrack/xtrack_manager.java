package com.smartx.rfidreader.core.xtrack;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000F\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0003\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J&\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0011\u001a\u00020\u00042\u0006\u0010\u0012\u001a\u00020\u0004H\u0086@\u00a2\u0006\u0002\u0010\u0013J\u0006\u0010\u0014\u001a\u00020\u0015J\u0010\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u0004H\u0002J\u0016\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u001a0\u00192\u0006\u0010\u001b\u001a\u00020\u0004H\u0002J\u0018\u0010\u001c\u001a\u00020\u00042\u0006\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u001e\u001a\u00020\u0004H\u0002J\u0010\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\u0004H\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\b\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\t\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u000e\u0010\f\u001a\u00020\rX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006 "}, d2 = {"Lcom/smartx/rfidreader/core/xtrack/xtrack_manager;", "", "()V", "GET_USER_PAYLOAD", "", "_currentUser", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/rfidreader/core/xtrack/XTrackUser;", "currentUser", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentUser", "()Lkotlinx/coroutines/flow/StateFlow;", "httpClient", "Lokhttp3/OkHttpClient;", "login", "Lcom/smartx/rfidreader/core/xtrack/XTrackLoginResult;", "xtrackUrl", "username", "password", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "", "md5Upper", "value", "parseDataRows", "", "Lcom/smartx/rfidreader/core/xtrack/ParsedRow;", "xml", "postXml", "url", "payload", "xmlUnescape", "app_debug"})
public final class xtrack_manager {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String GET_USER_PAYLOAD = "\n        <msg>\n            <command>GetUser</command>\n            <terminal>ERP</terminal>\n        </msg>\n    ";
    @org.jetbrains.annotations.NotNull()
    private static final okhttp3.OkHttpClient httpClient = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.rfidreader.core.xtrack.XTrackUser> _currentUser = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.core.xtrack.XTrackUser> currentUser = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.rfidreader.core.xtrack.xtrack_manager INSTANCE = null;
    
    private xtrack_manager() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.smartx.rfidreader.core.xtrack.XTrackUser> getCurrentUser() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object login(@org.jetbrains.annotations.NotNull()
    java.lang.String xtrackUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.rfidreader.core.xtrack.XTrackLoginResult> $completion) {
        return null;
    }
    
    public final void logout() {
    }
    
    private final java.lang.String postXml(java.lang.String url, java.lang.String payload) {
        return null;
    }
    
    private final java.util.List<com.smartx.rfidreader.core.xtrack.ParsedRow> parseDataRows(java.lang.String xml) {
        return null;
    }
    
    private final java.lang.String xmlUnescape(java.lang.String value) {
        return null;
    }
    
    private final java.lang.String md5Upper(java.lang.String value) {
        return null;
    }
}