package com.victor.myclient.ui.contract;

import com.victor.myclient.ui.base.BasePresenter;
import com.victor.myclient.ui.base.BaseView;

/**
 * Created by victor on 9/21/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public interface MainContract {
    interface View extends BaseView<Presenter> {
        String getType();

    }

    interface Presenter extends BasePresenter {

    }
}
