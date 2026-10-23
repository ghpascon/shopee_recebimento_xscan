package com.smartx.shopeerecebimento.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010\t\n\u0002\b\u0002\n\u0002\u0010\u0007\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\f\u0018\u0000 62\b\u0012\u0004\u0012\u00020\u00020\u0001:\u00016B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0006\u0010\u0014\u001a\u00020\u0015J\u0010\u0010\u0016\u001a\u00020\u000b2\u0006\u0010\u0017\u001a\u00020\u0018H\u0016J\b\u0010\u0019\u001a\u00020\u0015H\u0002J\u0006\u0010\u001a\u001a\u00020\u0015J\u0010\u0010\u001b\u001a\u00020\u00022\u0006\u0010\u001c\u001a\u00020\u001dH\u0014J\b\u0010\u001e\u001a\u00020\u0015H\u0002J\b\u0010\u001f\u001a\u00020\u0015H\u0002J\b\u0010 \u001a\u00020\u0015H\u0002J\u001a\u0010!\u001a\u00020\u00152\u0006\u0010\"\u001a\u00020#2\n\b\u0002\u0010$\u001a\u0004\u0018\u00010%J\b\u0010&\u001a\u00020\u0015H\u0002J\b\u0010\'\u001a\u00020\u0015H\u0002J\b\u0010(\u001a\u00020\u0015H\u0002J\u0012\u0010)\u001a\u00020\u00152\b\u0010*\u001a\u0004\u0018\u00010+H\u0014J\b\u0010,\u001a\u00020\u0015H\u0014J\b\u0010-\u001a\u00020\u0015H\u0014J\b\u0010.\u001a\u00020\u0015H\u0014J\b\u0010/\u001a\u00020\u0015H\u0014J\u0006\u00100\u001a\u00020\u0015J\b\u00101\u001a\u00020\u0015H\u0002J\b\u00102\u001a\u00020\u0015H\u0002J\u0012\u00103\u001a\u00020\u00152\b\b\u0002\u00104\u001a\u00020\u000bH\u0002J\b\u00105\u001a\u00020\u0015H\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0006\u001a\u00020\u0005X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0010\u0010\f\u001a\u0004\u0018\u00010\rX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u000e\u001a\u00020\u000f8FX\u0086\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0012\u0010\u0013\u001a\u0004\b\u0010\u0010\u0011\u00a8\u00067"}, d2 = {"Lcom/smartx/shopeerecebimento/ui/main/MainActivity;", "Lcom/smartx/shopeerecebimento/ui/base/BaseActivity;", "Lcom/smartx/shopeerecebimento/databinding/ActivityMainBinding;", "()V", "backPressedTime", "", "headerDownAt", "headerDownX", "", "headerDownY", "headerGestureCanceled", "", "toneGenerator", "Landroid/media/ToneGenerator;", "viewModel", "Lcom/smartx/shopeerecebimento/ui/main/MainViewModel;", "getViewModel", "()Lcom/smartx/shopeerecebimento/ui/main/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "continueToLoginAfterLocationConfirmation", "", "dispatchKeyEvent", "event", "Landroid/view/KeyEvent;", "enforceAuthOnHome", "goHome", "inflateBinding", "inflater", "Landroid/view/LayoutInflater;", "loadConfigAccess", "loadHome", "loadHomeOrLogin", "navigateTo", "fragment", "Landroidx/fragment/app/Fragment;", "tag", "", "observeBuzzer", "observeHeader", "observeNavigation", "onActivityReady", "savedInstanceState", "Landroid/os/Bundle;", "onLogoutPressed", "onResume", "onStart", "onStop", "requestLoginWithLocationConfirmation", "setupBackHandler", "setupHeaderAccessGesture", "showLocationConfirmation", "addToBackStack", "showLogin", "Companion", "app_debug"})
public final class MainActivity extends com.smartx.shopeerecebimento.ui.base.BaseActivity<com.smartx.shopeerecebimento.databinding.ActivityMainBinding> {
    @org.jetbrains.annotations.NotNull()
    private static final int[] TRIGGER_KEYCODES = {131, 80, 27, 293, 79, 103, 523};
    private static final long HEADER_HOLD_MS = 5000L;
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TAG_HOME = "home";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TAG_LOGIN = "login";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TAG_LOCATION_CONFIRMATION = "location_confirmation";
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TAG_CONFIG_ACCESS = "config_access";
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private long backPressedTime = 0L;
    private long headerDownAt = 0L;
    private float headerDownX = 0.0F;
    private float headerDownY = 0.0F;
    private boolean headerGestureCanceled = false;
    @org.jetbrains.annotations.Nullable()
    private android.media.ToneGenerator toneGenerator;
    @org.jetbrains.annotations.NotNull()
    public static final com.smartx.shopeerecebimento.ui.main.MainActivity.Companion Companion = null;
    
    public MainActivity() {
        super();
    }
    
    @org.jetbrains.annotations.NotNull()
    public final com.smartx.shopeerecebimento.ui.main.MainViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    protected com.smartx.shopeerecebimento.databinding.ActivityMainBinding inflateBinding(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater) {
        return null;
    }
    
    @java.lang.Override()
    protected void onActivityReady(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void loadHomeOrLogin() {
    }
    
    private final void observeHeader() {
    }
    
    private final void observeBuzzer() {
    }
    
    @java.lang.Override()
    protected void onStart() {
    }
    
    @java.lang.Override()
    protected void onResume() {
    }
    
    @java.lang.Override()
    protected void onStop() {
    }
    
    private final void loadHome() {
    }
    
    private final void showLogin() {
    }
    
    private final void showLocationConfirmation(boolean addToBackStack) {
    }
    
    public final void requestLoginWithLocationConfirmation() {
    }
    
    public final void continueToLoginAfterLocationConfirmation() {
    }
    
    private final void loadConfigAccess() {
    }
    
    public final void goHome() {
    }
    
    private final void enforceAuthOnHome() {
    }
    
    /**
     * Navega para um fragmento, adicionando ao back stack
     */
    public final void navigateTo(@org.jetbrains.annotations.NotNull()
    androidx.fragment.app.Fragment fragment, @org.jetbrains.annotations.Nullable()
    java.lang.String tag) {
    }
    
    private final void setupBackHandler() {
    }
    
    private final void observeNavigation() {
    }
    
    private final void setupHeaderAccessGesture() {
    }
    
    @java.lang.Override()
    public boolean dispatchKeyEvent(@org.jetbrains.annotations.NotNull()
    android.view.KeyEvent event) {
        return false;
    }
    
    @java.lang.Override()
    protected void onLogoutPressed() {
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0004\n\u0002\u0010\u0015\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002R\u000e\u0010\u0003\u001a\u00020\u0004X\u0082T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\b\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\t\u001a\u00020\u0006X\u0086T\u00a2\u0006\u0002\n\u0000R\u000e\u0010\n\u001a\u00020\u000bX\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\f"}, d2 = {"Lcom/smartx/shopeerecebimento/ui/main/MainActivity$Companion;", "", "()V", "HEADER_HOLD_MS", "", "TAG_CONFIG_ACCESS", "", "TAG_HOME", "TAG_LOCATION_CONFIRMATION", "TAG_LOGIN", "TRIGGER_KEYCODES", "", "app_debug"})
    public static final class Companion {
        
        private Companion() {
            super();
        }
    }
}