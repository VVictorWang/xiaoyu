package com.victor.myclient.ui.presenter;

import com.victor.myclient.ui.contract.CaseDetailContract;
import com.victor.myclient.datas.CaseInfor;

import org.litepal.crud.DataSupport;

/**
 * Created by victor on 7/15/17.
 * email: chengyiwang@hustunique.com
 * blog: www.victorwang.science                                            #
 */

public class CaseDetailPresenter implements CaseDetailContract.Presenter {
    private CaseDetailContract.View mView;
    private int id;

    public CaseDetailPresenter(CaseDetailContract.View view,int id) {
        mView = view;
        this.id = id;
        mView.setPresenter(this);
    }

    @Override
    public void start() {
        if (DataSupport.isExist(CaseInfor.class)) {
            CaseInfor caseInfor = DataSupport.find(CaseInfor.class, (long) id);
            mView.showData(caseInfor);
        } else {
            mView.showNoData();
        }
    }
}
