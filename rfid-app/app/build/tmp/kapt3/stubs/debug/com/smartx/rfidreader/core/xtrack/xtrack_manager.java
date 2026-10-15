package com.smartx.rfidreader.core.xtrack;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000t\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u000e\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0017\u001a\u00020\u00042\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00040\u0019H\u0002J\u001e\u0010\u001a\u001a\u00020\u00042\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00192\u0006\u0010\u001c\u001a\u00020\u0004H\u0002J$\u0010\u001d\u001a\b\u0012\u0004\u0012\u00020\u001f0\u001e2\u0006\u0010 \u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b!\u0010\"J\u0018\u0010#\u001a\u00020\u00042\u0006\u0010$\u001a\u00020\u00042\u0006\u0010%\u001a\u00020\u0004H\u0002J*\u0010&\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020\'0\u00190\u001e2\u0006\u0010 \u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b(\u0010\"J>\u0010)\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020+0*0\u001e2\u0006\u0010 \u001a\u00020\u00042\f\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\u00040\u0019H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b,\u0010-J&\u0010.\u001a\u00020/2\u0006\u0010 \u001a\u00020\u00042\u0006\u00100\u001a\u00020\u00042\u0006\u00101\u001a\u00020\u0004H\u0086@\u00a2\u0006\u0002\u00102J\u0006\u00103\u001a\u00020\u001fJ\u0010\u00104\u001a\u00020\u00042\u0006\u00105\u001a\u00020\u0004H\u0002J:\u00106\u001a\b\u0012\u0004\u0012\u00020\f0\u001e2\u0006\u0010 \u001a\u00020\u00042\f\u0010\u001b\u001a\b\u0012\u0004\u0012\u00020\u00040\u00192\u0006\u0010\u001c\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b7\u00108J\u0016\u00109\u001a\b\u0012\u0004\u0012\u00020:0\u00192\u0006\u0010;\u001a\u00020\u0004H\u0002J\u001b\u0010<\u001a\b\u0012\u0004\u0012\u00020\'0\u00192\u0006\u0010;\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\b=J!\u0010>\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020+0*2\u0006\u0010;\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\b?J\u0018\u0010@\u001a\u00020\u00042\u0006\u0010A\u001a\u00020\u00042\u0006\u0010B\u001a\u00020\u0004H\u0002J\u0016\u0010C\u001a\b\u0012\u0004\u0012\u00020\'0\u00192\u0006\u0010A\u001a\u00020\u0004H\u0002J\u0010\u0010D\u001a\u00020\u00042\u0006\u00105\u001a\u00020\u0004H\u0002J\u0010\u0010E\u001a\u00020\u00042\u0006\u00105\u001a\u00020\u0004H\u0002J\u0016\u0010F\u001a\u00020\u0004*\u00020\u00042\b\b\u0002\u0010G\u001a\u00020\fH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000b\u001a\u00020\fX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000e\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\u000fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0011\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00100\u0012\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u000e\u0010\u0015\u001a\u00020\u0016X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006H"}, d2 = {"Lcom/smartx/rfidreader/core/xtrack/xtrack_manager;", "", "()V", "GET_LOCATION_PAYLOAD", "", "GET_OBJECT_COMMAND", "GET_USER_PAYLOAD", "MOVE_LOCATION_COMMAND", "MOVE_LOCATION_TERMINAL", "NETWORK_TIMEOUT_SECONDS", "", "RESPONSE_PREVIEW_MAX", "", "TAG", "_currentUser", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/rfidreader/core/xtrack/XTrackUser;", "currentUser", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentUser", "()Lkotlinx/coroutines/flow/StateFlow;", "httpClient", "Lokhttp3/OkHttpClient;", "buildGetObjectPayload", "epcs", "", "buildMoveLocationPayload", "objectIdCodes", "locationName", "checkCommunication", "Lkotlin/Result;", "", "xtrackUrl", "checkCommunication-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "extractFirstValue", "xmlBlock", "tagName", "fetchLocations", "Lcom/smartx/rfidreader/core/xtrack/XTrackLocation;", "fetchLocations-gIAlu-s", "fetchObjectInfos", "", "Lcom/smartx/rfidreader/core/xtrack/XTrackObjectInfo;", "fetchObjectInfos-0E7RQCE", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "login", "Lcom/smartx/rfidreader/core/xtrack/XTrackLoginResult;", "username", "password", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "md5Upper", "value", "moveLocation", "moveLocation-BWLJW6A", "(Ljava/lang/String;Ljava/util/List;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "parseDataRows", "Lcom/smartx/rfidreader/core/xtrack/ParsedRow;", "xml", "parseLocationsXml", "parseLocationsXml$app_debug", "parseObjectInfosXml", "parseObjectInfosXml$app_debug", "postXml", "url", "payload", "requestLocations", "xmlEscape", "xmlUnescape", "preview", "max", "app_debug"})
public final class xtrack_manager {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "xtrack_manager";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String GET_USER_PAYLOAD = "<msg><command>GetUser</command><terminal>ERP</terminal></msg>";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String GET_LOCATION_PAYLOAD = "<msg><command>GetLocation</command><terminal>ERP</terminal></msg>";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String GET_OBJECT_COMMAND = "GetObject";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MOVE_LOCATION_COMMAND = "MoveLocation";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MOVE_LOCATION_TERMINAL = "SAPext";
    private static final int RESPONSE_PREVIEW_MAX = 500;
    private static final long NETWORK_TIMEOUT_SECONDS = 5L;
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
    
    private final java.util.List<com.smartx.rfidreader.core.xtrack.XTrackLocation> requestLocations(java.lang.String url) {
        return null;
    }
    
    private final java.lang.String postXml(java.lang.String url, java.lang.String payload) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.rfidreader.core.xtrack.XTrackLocation> parseLocationsXml$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String xml) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, com.smartx.rfidreader.core.xtrack.XTrackObjectInfo> parseObjectInfosXml$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String xml) {
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
    
    private final java.lang.String buildGetObjectPayload(java.util.List<java.lang.String> epcs) {
        return null;
    }
    
    private final java.lang.String buildMoveLocationPayload(java.util.List<java.lang.String> objectIdCodes, java.lang.String locationName) {
        return null;
    }
    
    private final java.lang.String extractFirstValue(java.lang.String xmlBlock, java.lang.String tagName) {
        return null;
    }
    
    private final java.lang.String xmlEscape(java.lang.String value) {
        return null;
    }
    
    private final java.lang.String preview(java.lang.String $this$preview, int max) {
        return null;
    }
}