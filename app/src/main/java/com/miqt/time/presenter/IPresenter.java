package com.miqt.time.presenter;

import com.miqt.time.view.IView;

public interface IPresenter<T extends IView> {
    void start(T view);
}
