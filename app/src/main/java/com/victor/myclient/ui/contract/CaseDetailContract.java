package com.victor.myclient.ui.contract;

import com.victor.myclient.base.BasePresenter;
import com.victor.myclient.base.BaseView;
import com.victor.myclient.datas.CaseInfor;

/**
 * Created by victor on 7/15/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public interface CaseDetailContract {
    interface View extends BaseView<Presenter> {
        void showData(CaseInfor caseInfor);

        void showNoData();
    }

    interface Presenter extends BasePresenter {

    }
}
