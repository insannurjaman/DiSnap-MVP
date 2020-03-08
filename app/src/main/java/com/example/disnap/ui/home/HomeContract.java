package com.example.disnap.ui.home;

import com.example.disnap.data.pojo.Disease;
import com.example.disnap.ui.base.BaseView;

import java.util.ArrayList;

public interface HomeContract {
    interface Presenter {
        void populateDiseaseInfo();

        void openDetailDiseaseInfoActivity(Disease disease);
    }

    interface View extends BaseView {
        void setDiseaseInfo(ArrayList<Disease> diseaseInfo);

        void showProgress();

        void hideProgress();

        void showDetailDiseaseInfoActivity(Disease disease);
    }

    interface OnItemClickListener {
        void clickItem(Disease disease);
    }
}
