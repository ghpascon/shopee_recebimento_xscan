package com.smartx.rfidreader.ui.main;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000h\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0004\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0000\u0018\u00002\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0002J\b\u0010\u0018\u001a\u00020\u0019H\u0002J$\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u001d2\b\u0010\u001e\u001a\u0004\u0018\u00010\u001f2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010\"\u001a\u00020\u0019H\u0016J\b\u0010#\u001a\u00020\u0019H\u0016J\u001a\u0010$\u001a\u00020\u00192\u0006\u0010%\u001a\u00020\u001b2\b\u0010 \u001a\u0004\u0018\u00010!H\u0016J\b\u0010&\u001a\u00020\u0019H\u0002J\b\u0010\'\u001a\u00020\u0019H\u0002J\b\u0010(\u001a\u00020\u0019H\u0002J\b\u0010)\u001a\u00020\u0019H\u0002J\u0010\u0010*\u001a\u00020\u00192\u0006\u0010+\u001a\u00020,H\u0002J\u0016\u0010-\u001a\u00020\u00192\f\u0010.\u001a\b\u0012\u0004\u0012\u0002000/H\u0002R\u0010\u0010\u0003\u001a\u0004\u0018\u00010\u0004X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0005\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0014\u0010\u0007\u001a\u00020\u00048BX\u0082\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u000e\u0010\n\u001a\u00020\u000bX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\f\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\r\u001a\u00020\u0006X\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u000e\u001a\u00020\u000fX\u0082.\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0010\u001a\u00020\u0011X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0012\u001a\u00020\u00138BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\u0016\u0010\u0017\u001a\u0004\b\u0014\u0010\u0015\u00a8\u00061"}, d2 = {"Lcom/smartx/rfidreader/ui/main/HomeFragment;", "Landroidx/fragment/app/Fragment;", "()V", "_binding", "Lcom/smartx/rfidreader/databinding/FragmentHomeBinding;", "allTagsWithDescription", "", "binding", "getBinding", "()Lcom/smartx/rfidreader/databinding/FragmentHomeBinding;", "currentTagCount", "", "hasRegisteredTags", "isCurrentlyInventorying", "registeredAdapter", "Lcom/smartx/rfidreader/ui/main/HomeEpcAdapter;", "unregisteredAdapter", "Lcom/smartx/rfidreader/ui/main/HomeUnregisteredAdapter;", "viewModel", "Lcom/smartx/rfidreader/ui/main/MainViewModel;", "getViewModel", "()Lcom/smartx/rfidreader/ui/main/MainViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "observeState", "", "onCreateView", "Landroid/view/View;", "inflater", "Landroid/view/LayoutInflater;", "container", "Landroid/view/ViewGroup;", "savedInstanceState", "Landroid/os/Bundle;", "onDestroyView", "onResume", "onViewCreated", "view", "setupActions", "setupRecyclerView", "stopWebhookSendingOnHome", "updateConfirmButtonState", "updateHomeStatus", "state", "Lcom/smartx/rfidreader/ui/main/MainUiState;", "updateUnregisteredTagsSection", "tags", "", "Lcom/smartx/rfidreader/core/reader/RfidTag;", "app_debug"})
public final class HomeFragment extends androidx.fragment.app.Fragment {
    @org.jetbrains.annotations.Nullable()
    private com.smartx.rfidreader.databinding.FragmentHomeBinding _binding;
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.smartx.rfidreader.ui.main.HomeEpcAdapter registeredAdapter;
    private com.smartx.rfidreader.ui.main.HomeUnregisteredAdapter unregisteredAdapter;
    private int currentTagCount = 0;
    private boolean isCurrentlyInventorying = false;
    private boolean allTagsWithDescription = false;
    private boolean hasRegisteredTags = false;
    
    public HomeFragment() {
        super();
    }
    
    private final com.smartx.rfidreader.databinding.FragmentHomeBinding getBinding() {
        return null;
    }
    
    private final com.smartx.rfidreader.ui.main.MainViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    public android.view.View onCreateView(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater, @org.jetbrains.annotations.Nullable()
    android.view.ViewGroup container, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
        return null;
    }
    
    @java.lang.Override()
    public void onViewCreated(@org.jetbrains.annotations.NotNull()
    android.view.View view, @org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    @java.lang.Override()
    public void onResume() {
    }
    
    private final void stopWebhookSendingOnHome() {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void setupActions() {
    }
    
    private final void observeState() {
    }
    
    private final void updateConfirmButtonState() {
    }
    
    private final void updateUnregisteredTagsSection(java.util.List<com.smartx.rfidreader.core.reader.RfidTag> tags) {
    }
    
    private final void updateHomeStatus(com.smartx.rfidreader.ui.main.MainUiState state) {
    }
    
    @java.lang.Override()
    public void onDestroyView() {
    }
}