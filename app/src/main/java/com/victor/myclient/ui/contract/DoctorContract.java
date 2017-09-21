package com.victor.myclient.ui.contract;

import com.victor.myclient.data.DoctorInfor;
import com.victor.myclient.data.DoctorXiaoYu;
import com.victor.myclient.ui.base.BasePresenter;
import com.victor.myclient.ui.base.BaseView;

/**
 * Created by victor on 9/21/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public interface DoctorContract {

    interface View extends BaseView<Presenter> {
        int getDoctorId();

        void showInfo(DoctorInfor doctorInfor);

        void dimissDialog();

        void setDoctorXiaoYu(DoctorXiaoYu doctorXiaoyu);
    }

    interface Presenter extends BasePresenter {
        void getInfo();

        void unscribe();
    }
}
