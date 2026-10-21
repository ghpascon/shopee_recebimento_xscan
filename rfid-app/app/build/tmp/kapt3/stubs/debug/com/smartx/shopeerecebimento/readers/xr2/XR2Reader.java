package com.smartx.shopeerecebimento.readers.xr2;

/**
 * Adaptador para o leitor XR2 (Ubx / URFIDLibrary).
 * SDK: com.ubx.usdk (RFIDSDKManager / RfidManager)
 *
 * Conexão: ligar módulo via power(true), aguardar ~1500ms, chamar connect() em thread IO.
 * Escala de potência: SDK usa dBm diretamente em setOutputPower(byte).
 * Modo de inventário: setQueryMode(int) — 0=EPC, 1=EPC+TID, 2=EPC+TID+User.
 * Callback de tags: registerCallback(IRfidCallback) → onInventoryTag(epc, tid, rssi).
 */
@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000j\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0002\b\t\u0018\u0000 52\u00020\u0001:\u00015B\u0005\u00a2\u0006\u0002\u0010\u0002J\u0016\u0010$\u001a\u00020\n2\u0006\u0010%\u001a\u00020&H\u0096@\u00a2\u0006\u0002\u0010\'J\u0016\u0010(\u001a\u00020\n2\u0006\u0010)\u001a\u00020*H\u0096@\u00a2\u0006\u0002\u0010+J\u000e\u0010,\u001a\u00020-H\u0096@\u00a2\u0006\u0002\u0010.J\b\u0010/\u001a\u00020\nH\u0016J\b\u00100\u001a\u00020\nH\u0016J\b\u00101\u001a\u00020\nH\u0016J\u000e\u00102\u001a\u00020&H\u0096@\u00a2\u0006\u0002\u0010.J\b\u00103\u001a\u00020\nH\u0016J\b\u00104\u001a\u00020\nH\u0016R\u0010\u0010\u0003\u001a\u00020\u0004X\u0082D\u00a2\u0006\u0004\n\u0002\b\u0005R\u0014\u0010\u0006\u001a\b\u0012\u0004\u0012\u00020\b0\u0007X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\nX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u000b\u001a\b\u0012\u0004\u0012\u00020\r0\fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u000fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001a\u0010\u0011\u001a\b\u0012\u0004\u0012\u00020\b0\u0012X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0013\u0010\u0014R\u0014\u0010\u0015\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0016\u0010\u0017R\u0014\u0010\u0018\u001a\u00020\nX\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0014\u0010\u001a\u001a\u00020\u0004X\u0096D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001b\u0010\u0017R\u0010\u0010\u001c\u001a\u0004\u0018\u00010\u001dX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u001e\u001a\u00020\u001fX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010 \u001a\b\u0012\u0004\u0012\u00020\r0!X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\"\u0010#\u00a8\u00066"}, d2 = {"Lcom/smartx/shopeerecebimento/readers/xr2/XR2Reader;", "Lcom/smartx/shopeerecebimento/core/reader/IRfidReader;", "()V", "TAG", "", "TAG$1", "_connectionState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/smartx/shopeerecebimento/core/reader/ReaderConnectionState;", "_isInventorying", "", "_tagChannel", "Lkotlinx/coroutines/flow/MutableSharedFlow;", "Lcom/smartx/shopeerecebimento/core/reader/RfidTag;", "cachedPower", "", "cachedQueryMode", "connectionState", "Lkotlinx/coroutines/flow/StateFlow;", "getConnectionState", "()Lkotlinx/coroutines/flow/StateFlow;", "displayName", "getDisplayName", "()Ljava/lang/String;", "isBle", "()Z", "readerId", "getReaderId", "rfidManager", "Lcom/ubx/usdk/rfid/RfidManager;", "tagCallback", "Lcom/ubx/usdk/rfid/aidl/IRfidCallback;", "tagFlow", "Lkotlinx/coroutines/flow/Flow;", "getTagFlow", "()Lkotlinx/coroutines/flow/Flow;", "applyConfig", "config", "Lcom/smartx/shopeerecebimento/core/reader/ReaderConfig;", "(Lcom/smartx/shopeerecebimento/core/reader/ReaderConfig;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "connect", "context", "Landroid/content/Context;", "(Landroid/content/Context;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "disconnect", "", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "isInventorying", "onTriggerPressed", "onTriggerReleased", "readConfig", "startInventory", "stopInventory", "Companion", "app_debug"})
public final class XR2Reader implements com.smartx.shopeerecebimento.core.reader.IRfidReader {
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String TAG = "XR2Reader";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String RFID_KEY_MAPPING = "0-";
    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String BARCODE_SCAN_KEYS = "520-521-522-";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String readerId = "XR2";
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String displayName = "XR2";
    private final boolean isBle = false;
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String TAG$1 = "XR2Reader";
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.smartx.shopeerecebimento.core.reader.ReaderConnectionState> _connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.smartx.shopeerecebimento.core.reader.ReaderConnectionState> connectionState = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableSharedFlow<com.smartx.shopeerecebimento.core.reader.RfidTag> _tagChannel = null;
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.Flow<com.smartx.shopeerecebimento.core.reader.RfidTag> tagFlow = null;
    @kotlin.jvm.Volatile()
    @org.jetbrains.annotations.Nullable()
    private volatile com.ubx.usdk.rfid.RfidManager rfidManager;
    @kotlin.jvm.Volatile()
    private volatile boolean _isInventorying = false;
    private int cachedPower = 30;
    private int cachedQueryMode = 1;
    @org.jetbrains.annotations.NotNull()
    private final com.ubx.usdk.rfid.aidl.IRfidCallback tagCallback = null;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.shopeerecebimento.readers.xr2.XR2Reader.Companion Companion = null;
    
    public XR2Reader() {
        super();
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getReaderId() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public java.lang.String getDisplayName() {
        return null;
    }
    
    @java.lang.Override()
    public boolean isBle() {
        return false;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.StateFlow<com.smartx.shopeerecebimento.core.reader.ReaderConnectionState> getConnectionState() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public kotlinx.coroutines.flow.Flow<com.smartx.shopeerecebimento.core.reader.RfidTag> getTagFlow() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object connect(@org.jetbrains.annotations.NotNull()
    android.content.Context context, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object disconnect(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public boolean startInventory() {
        return false;
    }
    
    @java.lang.Override()
    public boolean stopInventory() {
        return false;
    }
    
    @java.lang.Override()
    public boolean isInventorying() {
        return false;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object applyConfig(@org.jetbrains.annotations.NotNull()
    com.smartx.shopeerecebimento.core.reader.ReaderConfig config, @org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super java.lang.Boolean> $completion) {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.Nullable()
    public java.lang.Object readConfig(@org.jetbrains.annotations.NotNull()
    kotlin.coroutines.Continuation<? super com.smartx.shopeerecebimento.core.reader.ReaderConfig> $completion) {
        return null;
    }
    
    @java.lang.Override()
    public boolean onTriggerPressed() {
        return false;
    }
    
    @java.lang.Override()
    public boolean onTriggerReleased() {
        return false;
    }
    
    @kotlin.jvm.JvmStatic()
    public static final void configureExclusiveRfidTrigger() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\u001a\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000e\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0007\u001a\u00020\bH\u0007R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000\u00a8\u0006\t"}, d2 = {"Lcom/smartx/shopeerecebimento/readers/xr2/XR2Reader$Companion;", "", "()V", "BARCODE_SCAN_KEYS", "", "RFID_KEY_MAPPING", "TAG", "configureExclusiveRfidTrigger", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
        
        /**
         * Reserve the XR2 handle trigger (523) for RFID and never for the barcode scan head.
         */
        @kotlin.jvm.JvmStatic()
        public final void configureExclusiveRfidTrigger() {
        }
    }
}