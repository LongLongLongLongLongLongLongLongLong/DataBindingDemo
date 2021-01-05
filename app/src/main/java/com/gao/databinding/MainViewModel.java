package com.gao.databinding;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    //LiveData维护一个整型数 Number
    private MutableLiveData<Integer> Number;
    public MutableLiveData<Integer> getNumber() {

        if(Number==null){
            Number = new MutableLiveData<>();
            Number.setValue(0);
        }
        return Number;
    }

    /**
     * 实现数据加1
     */
    public void add(){
        Number.setValue(Number.getValue()+1);
    }
}
