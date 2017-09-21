package com.victor.myclient.ui.contract;

import com.victor.myclient.data.UserInfor;
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

        void setName(String name);

        void connectXiaoYu(String xiaoNum, String xiaoName);

        void showImage(String imageUrl);

        void showToast(String info);

        void startClientService();

        void setUserInfo(UserInfor myUserInfo);

    }

    interface Presenter extends BasePresenter {

    }
}
