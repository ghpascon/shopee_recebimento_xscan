package com.smartx.shopeerecebimento.ui.selection;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\u0018\u00002\b\u0012\u0004\u0012\u00020\u00020\u0001B\u0005\u00a2\u0006\u0002\u0010\u0003J\u0010\u0010\f\u001a\u00020\u00022\u0006\u0010\r\u001a\u00020\u000eH\u0014J\b\u0010\u000f\u001a\u00020\u0010H\u0002J\u0012\u0010\u0011\u001a\u00020\u00102\b\u0010\u0012\u001a\u0004\u0018\u00010\u0013H\u0014J\u0010\u0010\u0014\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\b\u0010\u0017\u001a\u00020\u0010H\u0002J\u0010\u0010\u0018\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u0016H\u0002J\u0010\u0010\u0019\u001a\u00020\u00102\u0006\u0010\u0015\u001a\u00020\u001aH\u0002R\u000e\u0010\u0004\u001a\u00020\u0005X\u0082.\u00a2\u0006\u0002\n\u0000R\u001b\u0010\u0006\u001a\u00020\u00078BX\u0082\u0084\u0002\u00a2\u0006\f\n\u0004\b\n\u0010\u000b\u001a\u0004\b\b\u0010\t\u00a8\u0006\u001b"}, d2 = {"Lcom/smartx/shopeerecebimento/ui/selection/ReaderSelectionActivity;", "Lcom/smartx/shopeerecebimento/ui/base/BaseActivity;", "Lcom/smartx/shopeerecebimento/databinding/ActivitySelectionBinding;", "()V", "adapter", "Lcom/smartx/shopeerecebimento/ui/selection/ReaderListAdapter;", "viewModel", "Lcom/smartx/shopeerecebimento/ui/selection/ReaderSelectionViewModel;", "getViewModel", "()Lcom/smartx/shopeerecebimento/ui/selection/ReaderSelectionViewModel;", "viewModel$delegate", "Lkotlin/Lazy;", "inflateBinding", "inflater", "Landroid/view/LayoutInflater;", "observeState", "", "onActivityReady", "savedInstanceState", "Landroid/os/Bundle;", "onConnectClicked", "reader", "Lcom/smartx/shopeerecebimento/core/reader/IRfidReader;", "setupRecyclerView", "showBleScanDialog", "showZebraTransportDialog", "Lcom/smartx/shopeerecebimento/readers/zebra/ZebraReader;", "app_debug"})
public final class ReaderSelectionActivity extends com.smartx.shopeerecebimento.ui.base.BaseActivity<com.smartx.shopeerecebimento.databinding.ActivitySelectionBinding> {
    @org.jetbrains.annotations.NotNull()
    private final kotlin.Lazy viewModel$delegate = null;
    private com.smartx.shopeerecebimento.ui.selection.ReaderListAdapter adapter;
    
    public ReaderSelectionActivity() {
        super();
    }
    
    private final com.smartx.shopeerecebimento.ui.selection.ReaderSelectionViewModel getViewModel() {
        return null;
    }
    
    @java.lang.Override()
    @org.jetbrains.annotations.NotNull()
    protected com.smartx.shopeerecebimento.databinding.ActivitySelectionBinding inflateBinding(@org.jetbrains.annotations.NotNull()
    android.view.LayoutInflater inflater) {
        return null;
    }
    
    @java.lang.Override()
    protected void onActivityReady(@org.jetbrains.annotations.Nullable()
    android.os.Bundle savedInstanceState) {
    }
    
    private final void setupRecyclerView() {
    }
    
    private final void onConnectClicked(com.smartx.shopeerecebimento.core.reader.IRfidReader reader) {
    }
    
    private final void showBleScanDialog(com.smartx.shopeerecebimento.core.reader.IRfidReader reader) {
    }
    
    private final void showZebraTransportDialog(com.smartx.shopeerecebimento.readers.zebra.ZebraReader reader) {
    }
    
    private final void observeState() {
    }
}