package com.smartx.shopeerecebimento.core.xtrack;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u0092\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0006\n\u0002\u0010\t\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010$\n\u0002\u0018\u0002\n\u0002\b\u0007\n\u0002\u0018\u0002\n\u0002\b\u000f\n\u0002\u0010!\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0014\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010\u0018\u001a\u00020\u00042\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00040\u001aH\u0002J\u001e\u0010\u001b\u001a\u00020\u00042\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00040\u001a2\u0006\u0010\u001d\u001a\u00020\u0004H\u0002J$\u0010\u001e\u001a\b\u0012\u0004\u0012\u00020 0\u001f2\u0006\u0010!\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b\"\u0010#J\u0018\u0010$\u001a\u00020\u00042\u0006\u0010%\u001a\u00020\u00042\u0006\u0010&\u001a\u00020\u0004H\u0002J*\u0010\'\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020(0\u001a0\u001f2\u0006\u0010!\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b)\u0010#J*\u0010*\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u00020+0\u001a0\u001f2\u0006\u0010!\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b,\u0010#J>\u0010-\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020/0.0\u001f2\u0006\u0010!\u001a\u00020\u00042\f\u0010\u0019\u001a\b\u0012\u0004\u0012\u00020\u00040\u001aH\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b0\u00101J%\u00102\u001a\u0004\u0018\u00010(2\f\u00103\u001a\b\u0012\u0004\u0012\u00020(0\u001a2\u0006\u00104\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\b5J&\u00106\u001a\u0002072\u0006\u0010!\u001a\u00020\u00042\u0006\u00108\u001a\u00020\u00042\u0006\u00109\u001a\u00020\u0004H\u0086@\u00a2\u0006\u0002\u0010:J\u0006\u0010;\u001a\u00020 J\u0010\u0010<\u001a\u00020\u00042\u0006\u0010=\u001a\u00020\u0004H\u0002J:\u0010>\u001a\b\u0012\u0004\u0012\u00020\r0\u001f2\u0006\u0010!\u001a\u00020\u00042\f\u0010\u001c\u001a\b\u0012\u0004\u0012\u00020\u00040\u001a2\u0006\u0010\u001d\u001a\u00020\u0004H\u0086@\u00f8\u0001\u0000\u00f8\u0001\u0001\u00a2\u0006\u0004\b?\u0010@J\u0015\u0010A\u001a\u00020\u00042\u0006\u0010=\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\bBJ.\u0010C\u001a\n\u0012\u0004\u0012\u00020\u0004\u0018\u00010\u001a2\u0006\u0010D\u001a\u00020\u00042\u0006\u0010E\u001a\u00020\u00042\f\u0010F\u001a\b\u0012\u0004\u0012\u00020\u00040GH\u0002J\u0016\u0010H\u001a\b\u0012\u0004\u0012\u00020I0\u001a2\u0006\u0010J\u001a\u00020\u0004H\u0002J\u0015\u0010K\u001a\u00020L2\u0006\u0010M\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\bNJ\u001b\u0010O\u001a\b\u0012\u0004\u0012\u00020(0\u001a2\u0006\u0010P\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\bQJ\u001b\u0010R\u001a\b\u0012\u0004\u0012\u00020(0\u001a2\u0006\u0010P\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\bSJ\u001b\u0010T\u001a\b\u0012\u0004\u0012\u00020(0\u001a2\u0006\u0010J\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\bUJ\u001b\u0010V\u001a\b\u0012\u0004\u0012\u00020+0\u001a2\u0006\u0010J\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\bWJ!\u0010X\u001a\u000e\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020/0.2\u0006\u0010J\u001a\u00020\u0004H\u0000\u00a2\u0006\u0002\bYJ\u0018\u0010Z\u001a\u00020\u00042\u0006\u0010[\u001a\u00020\u00042\u0006\u0010\\\u001a\u00020\u0004H\u0002J\u0016\u0010]\u001a\b\u0012\u0004\u0012\u00020+0\u001a2\u0006\u0010[\u001a\u00020\u0004H\u0002J\u0010\u0010^\u001a\u00020\u00042\u0006\u0010=\u001a\u00020\u0004H\u0002J\u0010\u0010_\u001a\u00020\u00042\u0006\u0010=\u001a\u00020\u0004H\u0002J\u0016\u0010`\u001a\u0004\u0018\u00010\u0001*\u00020a2\u0006\u0010b\u001a\u00020\u0004H\u0002J\u0014\u0010c\u001a\u00020\u0004*\u00020a2\u0006\u0010b\u001a\u00020\u0004H\u0002J\u0016\u0010d\u001a\u00020\u0004*\u00020\u00042\b\b\u0002\u0010e\u001a\u00020\rH\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\rX\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u0016\u0010\u000f\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u0010X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u0019\u0010\u0012\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00110\u0013\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0015R\u000e\u0010\u0016\u001a\u00020\u0017X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u0082\u0002\u000b\n\u0002\b!\n\u0005\b\u00a1\u001e0\u0001\u00a8\u0006f"}, d2 = {"Lcom/smartx/shopeerecebimento/core/xtrack/xtrack_manager;", "", "()V", "GET_DEVICE_PAYLOAD", "", "GET_LOCATION_PAYLOAD", "GET_OBJECT_COMMAND", "GET_USER_PAYLOAD", "MOVE_LOCATION_COMMAND", "MOVE_LOCATION_TERMINAL", "NETWORK_TIMEOUT_SECONDS", "", "RESPONSE_PREVIEW_MAX", "", "TAG", "_currentUser", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/shopeerecebimento/core/xtrack/XTrackUser;", "currentUser", "Lkotlinx/coroutines/flow/StateFlow;", "getCurrentUser", "()Lkotlinx/coroutines/flow/StateFlow;", "httpClient", "Lokhttp3/OkHttpClient;", "buildGetObjectPayload", "epcs", "", "buildMoveLocationPayload", "objectIdCodes", "locationName", "checkCommunication", "Lkotlin/Result;", "", "xtrackUrl", "checkCommunication-gIAlu-s", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "extractFirstValue", "xmlBlock", "tagName", "fetchDevices", "Lcom/smartx/shopeerecebimento/core/xtrack/XTrackDevice;", "fetchDevices-gIAlu-s", "fetchLocations", "Lcom/smartx/shopeerecebimento/core/xtrack/XTrackLocation;", "fetchLocations-gIAlu-s", "fetchObjectInfos", "", "Lcom/smartx/shopeerecebimento/core/xtrack/XTrackObjectInfo;", "fetchObjectInfos-0E7RQCE", "(Ljava/lang/String;Ljava/util/List;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "findSelectedDevice", "devices", "selectedUid", "findSelectedDevice$app_debug", "login", "Lcom/smartx/shopeerecebimento/core/xtrack/XTrackLoginResult;", "username", "password", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "logout", "md5Upper", "value", "moveLocation", "moveLocation-BWLJW6A", "(Ljava/lang/String;Ljava/util/List;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "normalizeUid", "normalizeUid$app_debug", "parseConfigList", "source", "key", "errors", "", "parseDataRows", "Lcom/smartx/shopeerecebimento/core/xtrack/ParsedRow;", "xml", "parseDeviceConfig", "Lcom/smartx/shopeerecebimento/core/xtrack/XTrackDeviceConfig;", "rawName", "parseDeviceConfig$app_debug", "parseDevicesJson", "body", "parseDevicesJson$app_debug", "parseDevicesResponse", "parseDevicesResponse$app_debug", "parseDevicesXml", "parseDevicesXml$app_debug", "parseLocationsXml", "parseLocationsXml$app_debug", "parseObjectInfosXml", "parseObjectInfosXml$app_debug", "postXml", "url", "payload", "requestLocations", "xmlEscape", "xmlUnescape", "findIgnoreCase", "Lorg/json/JSONObject;", "name", "getStringIgnoreCase", "preview", "max", "app_debug"})
public final class xtrack_manager {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "xtrack_manager";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String GET_USER_PAYLOAD = "<msg><command>GetUser</command><terminal>ERP</terminal></msg>";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String GET_LOCATION_PAYLOAD = "<msg><command>GetLocation</command><terminal>ERP</terminal></msg>";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String GET_DEVICE_PAYLOAD = "<msg><command>GetDevice</command><terminal>ERP</terminal></msg>";
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
    private static final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.shopeerecebimento.core.xtrack.XTrackUser> _currentUser = null;
    @org.jetbrains.annotations.NotNull()
    private static final kotlinx.coroutines.flow.StateFlow<com.smartx.shopeerecebimento.core.xtrack.XTrackUser> currentUser = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.shopeerecebimento.core.xtrack.xtrack_manager INSTANCE = null;
    
    private xtrack_manager() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.smartx.shopeerecebimento.core.xtrack.XTrackUser> getCurrentUser() {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object login(@org.jetbrains.annotations.NotNull()
    java.lang.String xtrackUrl, @org.jetbrains.annotations.NotNull()
    java.lang.String username, @org.jetbrains.annotations.NotNull()
    java.lang.String password, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.shopeerecebimento.core.xtrack.XTrackLoginResult> $completion) {
        return null;
    }
    
    public final void logout() {
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.shopeerecebimento.core.xtrack.XTrackDevice> parseDevicesResponse$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String body) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.shopeerecebimento.core.xtrack.XTrackDevice> parseDevicesJson$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String body) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.shopeerecebimento.core.xtrack.XTrackDevice> parseDevicesXml$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String xml) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.shopeerecebimento.core.xtrack.XTrackDeviceConfig parseDeviceConfig$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String rawName) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String normalizeUid$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String value) {
        return null;
    }
    
    @org.jetbrains.annotations.Nullable()
    public final com.smartx.shopeerecebimento.core.xtrack.XTrackDevice findSelectedDevice$app_debug(@org.jetbrains.annotations.NotNull()
    java.util.List<com.smartx.shopeerecebimento.core.xtrack.XTrackDevice> devices, @org.jetbrains.annotations.NotNull()
    java.lang.String selectedUid) {
        return null;
    }
    
    private final java.util.List<com.smartx.shopeerecebimento.core.xtrack.XTrackLocation> requestLocations(java.lang.String url) {
        return null;
    }
    
    private final java.lang.String postXml(java.lang.String url, java.lang.String payload) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.smartx.shopeerecebimento.core.xtrack.XTrackLocation> parseLocationsXml$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String xml) {
        return null;
    }
    
    @org.jetbrains.annotations.NotNull()
    public final java.util.Map<java.lang.String, com.smartx.shopeerecebimento.core.xtrack.XTrackObjectInfo> parseObjectInfosXml$app_debug(@org.jetbrains.annotations.NotNull()
    java.lang.String xml) {
        return null;
    }
    
    private final java.util.List<com.smartx.shopeerecebimento.core.xtrack.ParsedRow> parseDataRows(java.lang.String xml) {
        return null;
    }
    
    private final java.util.List<java.lang.String> parseConfigList(java.lang.String source, java.lang.String key, java.util.List<java.lang.String> errors) {
        return null;
    }
    
    private final java.lang.Object findIgnoreCase(org.json.JSONObject $this$findIgnoreCase, java.lang.String name) {
        return null;
    }
    
    private final java.lang.String getStringIgnoreCase(org.json.JSONObject $this$getStringIgnoreCase, java.lang.String name) {
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